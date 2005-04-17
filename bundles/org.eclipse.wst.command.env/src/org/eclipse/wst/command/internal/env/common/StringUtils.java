/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.command.internal.env.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Enumeration;
import java.util.Vector;

/**
  * This class contains some useful string utilities that are not provided by
  * either String or StringBuffer.
  *
  * @author Peter Moogk
  * @date   July 13, 2000
**/
public final class StringUtils
{
  /**
  * The platform-specific line separator.
  */
  public static final String NEWLINE = System.getProperty("line.separator");

  private StringUtils(){};

  /**
    * This method splits a single line of text into multiple lines
    * based on a maximum line length.  The method will try to fit as
    * many words as possible onto a line without exceeding the maximum
    * line length.  Note: the only case where a line might exceed the
    * maximum is if a single word is longer than the maximum.
    * @param text a single line a text that is to be split.
    * @param max_length the maximum length of each split line.
    * @return a string array of the split lines.
  **/
  public static String[] splitter( String text, int max_length )
  {
    Vector          return_text = new Vector(20);
    String[]        return_str;
    int             index = 0;

    while( index < text.length() )
    {
      String str = text.substring( index, Math.min( max_length + index,
                                                    text.length() ) );
      int space_index = str.lastIndexOf( " " );

      if( index + str.length() < text.length() &&
          text.charAt( index + str.length() - 1 ) != ' ' &&
          text.charAt( index + str.length() ) != ' ' &&
          space_index != -1 )
      {
        return_text.addElement( str.substring( 0, space_index ) );
        index += space_index + 1;
      }
      else
      {
        return_text.addElement( str.trim() );
        index += str.length();
      }
    }

    return_str = new String[return_text.size()];

    for( index = 0; index < return_text.size(); index++ )
    {
      return_str[index] = (String)(return_text.elementAt(index));
    }

    return return_str;
  }

  /**
    * This method returns a string with a repeated number of characters.
    * @param the_char the character to be repeated.
    * @param count the number of time this character should be repeated.
    * @return the resulting string of repeated characters.
  **/
  static public String repeat( char the_char, int count )
  {
    StringBuffer buf = new StringBuffer( count );

    for( int index = 0; index < count; index++ )
    {
      buf.append( the_char );
    }

    return buf.toString();
  }

  /**
    * This method flattens an array of arguments to a string.
    * The method respects embedded whitespace and quotes.
    * <ul>
    * <li>Any argument with embedded whitespace will be flattened out
    * with enclosing quotes. For example, the single argument
    * <u>Hello World</u>
    * will be returned as
    * <u>"Hello World"</u>.
    * <li>Any argument with quotes will be flattened out with the
    * quotes escaped. For example, the single argument
    * <u>"Happy days"</u>
    * will be returned as
    * <u>\"Happy days\"</u>.
    * </ul>
    * @param arguments The array of strings to flatten.
    * @return the flattened string.
  */
  static public String flattenArguments ( String[] arguments )
  {
    StringBuffer buf = new StringBuffer();

    for (int i=0; i<arguments.length; i++)
    {
      //
      // Append a separator (except the first time).
      //
      if (i > 0) buf.append(' ');

      //
      // Look for whitespace.
      //
      boolean whitespace = false;
      char[] chars = arguments[i].toCharArray();
      for (int j=0; !whitespace && j<chars.length; j++)
      {
        if (Character.isWhitespace(chars[j]))
        {
          whitespace = true;
        }
      }

      //
      // Append the argument, quoted as necessary.
      //
      if (whitespace) buf.append('"');
      for (int j=0; j<chars.length; j++)
      {
        if (chars[j] == '"') buf.append('\\');
        buf.append(chars[j]);
      }
      if (whitespace) buf.append('"');
    }

    return buf.toString();
  }

  /**
    * This method parses whitespace-delimitted filenames from
    * the given <code>input</code> stream. <b>Limitation:</b>
    * Quoted filenames or filenames with embedded whitespace
    * are not currently supported.
    * @param input The input stream.
    * @return An enumeration of filenames from the stream.
  */
  static public Enumeration parseFilenamesFromStream ( InputStream input )
  throws IOException
  {
    Vector filenames = new Vector(64,64);
    StringBuffer buffer = null;
    byte state = STATE_WS;
    int ic = input.read();
    while (ic >= 0)
    {
      char c = (char)ic;
      switch (state)
      {
        case STATE_WS:
          if (!Character.isWhitespace(c))
          {
            buffer = new StringBuffer();
            buffer.append(c);
            state = STATE_NWS;
          }
          break;
        case STATE_NWS:
          if (!Character.isWhitespace(c))
          {
            buffer.append(c);
          }
          else
          {
            String filename = buffer.toString();
            filenames.add(filename);
            buffer = null;
            state = STATE_WS;
          }
          break;
        default:
          break;
      }
      ic = input.read();
    }
    return filenames.elements();
  }

  private static final byte STATE_WS = 0;
  private static final byte STATE_NWS = 1;


  /**
  * Returns true is the type passed in is a primtive java type
  * @param class name String
  * @return true is primitive type
  */
  public static boolean isPrimitiveType(String typeName) 
  {

    if (typeName.equalsIgnoreCase("boolean") ||
      typeName.equalsIgnoreCase("byte") ||
      typeName.equalsIgnoreCase("double") ||
      typeName.equalsIgnoreCase("float") ||
      typeName.equalsIgnoreCase("int") ||
      typeName.equalsIgnoreCase("long") ||
      typeName.equalsIgnoreCase("short") ||
      typeName.equalsIgnoreCase("char"))
      return true;
    return false;

  }

  /**
  * The method replace the characters that are allowed in URIs
  * and not allowed in Java class names to an underscore ('_')
  * @param URI String
  * @return valid Java class name String
  */
  public static String URI2ClassName( String uri ) {
    String className = uri;
    for ( int i = 0; i < URI_SYMBOLS.length; i++ ) {
    	className = className.replace ( URI_SYMBOLS[i], UNDERSCORE );
    }
    return className;
  }

  /**
   * Creates and array of strings containing the exception traceback information of
   * a Throwable.  This is the same traceback data that is displayed by exc.printStackTrace().
   * @param exc the exception
   * @return a string array of the traceback information.
   */
  public static String[] getStackTrace( Throwable exc )
  {
    Vector       lines        = new Vector();
    StringWriter stringWriter = new StringWriter();
    PrintWriter  printWriter  = new PrintWriter( stringWriter );
    
    exc.printStackTrace( printWriter );
    
    try
    {
      printWriter.close();
      stringWriter.close();
    }
    catch( Exception nestedExc )
    {
      return new String[0];
    }
    
    StringReader stringReader = new StringReader( stringWriter.toString() );
    BufferedReader reader     = new BufferedReader( stringReader );
    String         line       = null;
    
    try
    {
      line = reader.readLine();
    
      while( line != null )
      {
        lines.add( line.trim() );
        line = reader.readLine();
      }
    }
    catch( Exception nestedExc )
    {
      return new String[0];
    }
    
    return (String[])lines.toArray( new String[0] );
  }
  
  private static final char[] URI_SYMBOLS = {'-', '~', '#', '/', '.'};
  private static final char UNDERSCORE = '_';

  private static boolean isDelimiter(char character)
  {
    return "\u002D\u002E\u003A\u005F\u00B7\u0387\u06DD\u06DE".indexOf(character) != -1;
  }
  
  
}
