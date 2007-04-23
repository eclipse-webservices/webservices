/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.ws.internal.explorer.platform.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;

public final class MultipartFormDataParser
{
  private Hashtable paramTable_;

  private static final String HEADER_CONTENT_TYPE        = "Content-Type";
  private static final String HEADER_MULTIPART           = "multipart";
  private static final String HEADER_BOUNDARY            = "boundary=";
  private static final String HEADER_CONTENT_DISPOSITION = "Content-Disposition: form-data";
  private static final String HEADER_NAME                = "name=\"";

  private static final byte PARSER_STATE_INITIAL         = 0;
  private static final byte PARSER_STATE_BOUNDARY        = 1;
  private static final byte PARSER_STATE_PARAMETER       = 2;
  private static final byte PARSER_STATE_BLANK           = 3;
  private static final byte PARSER_STATE_DATA            = 4;

  private static String parserStates[] = {"initial","boundary","parameter name","blank line","data"};

  public MultipartFormDataParser()
  {
  }
  
  public MultipartFormDataParser(Hashtable parameters)
  {
    paramTable_ = new Hashtable();
    for (Iterator it = parameters.keySet().iterator(); it.hasNext();)
    {
      Object key = it.next();
      Object value = parameters.get(key);
      if (value instanceof List)
      {
        List list = (List)value;
        for (Iterator it2 = list.iterator(); it2.hasNext();)
          saveData(key.toString(), it2.next().toString());
      }
      else if (value.getClass().isArray())
      {
        Object[] array = (Object[])value;
        for (int i = 0; i < array.length; i++)
          saveData(key.toString(), array[i].toString());
      }
      else
      {
        saveData(key.toString(), value.toString());
      }
    }
  }

  /**
   * Parse a multipart/form-data encoded post request with a given encoding.
   * If the encoding is null, use the system default encoding. utf-8 is not a
   * bad choice for the encoding.
   */
  public final void parseRequest(HttpServletRequest request,String encoding) throws MultipartFormDataException
  {
    // Content-Type header should have the form:
    // multipart/form-data; boundary=...
    //
    // RFC2046 5.1.1 page 19, paragraph 2:
    // The Content-Type field for multipart entities requires one parameter, "boundary" (no quotes)
    String contentType = request.getHeader(HEADER_CONTENT_TYPE);
    if (contentType == null || !contentType.startsWith(HEADER_MULTIPART) || contentType.indexOf(HEADER_BOUNDARY) == -1)
      throw new MultipartFormDataException("Content-Type is not multipart/form-data");

    // RFC2046 5.1.1 page 19, paragraph 4:
    // The boundary value may be enclosed in double quotes. Strip these if they are present.
    String boundary = contentType.substring(contentType.indexOf(HEADER_BOUNDARY)+HEADER_BOUNDARY.length(),contentType.length());
    if (boundary.charAt(0) == '\"' && boundary.charAt(boundary.length()-1) == '\"')
      boundary = boundary.substring(1,boundary.length()-1);

    // RFC2046 5.1.1 page 19, paragraph 2:
    // The boundary delimiter line is then defined as a line consisting entirely
    // of two hyphen characters.
    String delimiter = "--"+boundary;

    if (paramTable_ == null)
      paramTable_ = new Hashtable();
    else
      paramTable_.clear();

    try
    {
      BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream(),encoding));
      String line = null;
      String parameterName = null;
      StringBuffer parameterValue = new StringBuffer();
      byte currentParserState = PARSER_STATE_INITIAL;
      while ((line = br.readLine()) != null)
      {
        // Lines appear in the following sequence.
        // 1) boundary indicating the start of a new parameter and end of a data segment.
        // 2) Content-Disposition: form-data; name="..." - the name of a new parameter.
        // 3) a blank line
        // 4) data
        //
        // For each parameter, the sequence is repeated.
        if (line.startsWith(delimiter))
        {
          byte[] expectedParserStates = {PARSER_STATE_INITIAL,PARSER_STATE_DATA};
          if (isValidParserState(currentParserState,expectedParserStates))
          {
            // Save any current data and prepare for a new parameter name.
            if (parameterName != null)
            {
              saveData(parameterName,parameterValue.toString());
              parameterName = null;
              parameterValue.setLength(0);
            }
            currentParserState = PARSER_STATE_BOUNDARY;
          }
          else
            throw new MultipartFormDataException(getParserExceptionMessage(currentParserState,expectedParserStates));
        }
        else if (line.startsWith(HEADER_CONTENT_DISPOSITION))
        {
          byte[] expectedParserStates = {PARSER_STATE_BOUNDARY};
          if (isValidParserState(currentParserState,expectedParserStates))
          {
            // Obtain the parameter name without the surrounding double quotes. Accounts for RFC 1867 too.
            int parameterNameStartingPosition = line.indexOf(HEADER_NAME)+HEADER_NAME.length();
            parameterName = line.substring(parameterNameStartingPosition,+parameterNameStartingPosition+line.substring(parameterNameStartingPosition).indexOf('\"'));
            currentParserState = PARSER_STATE_PARAMETER;
          }
        }
        else if (currentParserState == PARSER_STATE_PARAMETER)
        {
          // A blank line should follow the PARAMETER. Discard the line and move on.
          currentParserState = PARSER_STATE_BLANK;
        }
        else
        {
          // Expect the line to contain data.
          if (parameterValue.length() > 0)
            parameterValue.append('\n');
          parameterValue.append(line);
          currentParserState = PARSER_STATE_DATA;
        }
      }
    }
    catch (Throwable t)
    {
      throw new MultipartFormDataException(t.getMessage());
    }
    //dumpParamTable();
  }

  /**
   * Returns the value of a request parameter as a String, or null if the parameter does not exist.
   * If the parameter has multiple values, only the first value is returned. Use getParameterValues()
   * for parameters with multiple values.
   */
  public final String getParameter(String parameter) throws MultipartFormDataException
  {
    if (paramTable_ == null)
      throw new MultipartFormDataException("Parser contains no parsed data");
    Vector values = (Vector)paramTable_.get(parameter);
    return ((values != null)?((String)values.elementAt(0)):null);
  }

  public final String[] getParameterValues(String parameter) throws MultipartFormDataException
  {
    if (paramTable_ == null)
      throw new MultipartFormDataException("Parser contains no parsed data");
    Vector valuesVector = (Vector)paramTable_.get(parameter);
    if (valuesVector == null)
      return null;
    String[] valuesArray = new String[valuesVector.size()];
    for (int i=0;i<valuesArray.length;i++)
      valuesArray[i] = (String)valuesVector.elementAt(i);
    return valuesArray;
  }

  public final String[] getParameterNames() throws MultipartFormDataException
  {
    if (paramTable_ == null)
      throw new MultipartFormDataException("Parser contains no parsed data");

    int size = paramTable_.size();
    if (size == 0)
      return null;
    String[] names = new String[size];
    Enumeration keys = paramTable_.keys();
    for (int i=0;i<size;i++)
      names[i] = (String)keys.nextElement();
    return names;
  }

  private final void saveData(String parameterName,String parameterValue)
  {
    Vector values = (Vector)paramTable_.get(parameterName);
    if (values == null)
      values = new Vector();
    values.addElement(parameterValue);
    paramTable_.put(parameterName,values);
  }

  private final boolean isValidParserState(byte currentState,byte[] expectedStates)
  {
    boolean validity = false;
    for (int i=0;i<expectedStates.length;i++)
    {
      if (currentState == expectedStates[i])
      {
        validity = true;
        break;
      }
    }
    return validity;
  }

  private final String getParserExceptionMessage(byte currentState,byte[] expectedStates)
  {
    StringBuffer msg = new StringBuffer("Parser state inconsistency!");
    msg.append('\n');
    msg.append("Current state    : ").append(parserStates[currentState]).append('\n');
    msg.append("Expected state(s): ");
    for (int i=0;i<expectedStates.length;i++)
    {
      msg.append(parserStates[expectedStates[i]]);
      if (i != expectedStates.length-1)
        msg.append(", ");
    }
    msg.append('\n');
    return msg.toString();
  }
}
