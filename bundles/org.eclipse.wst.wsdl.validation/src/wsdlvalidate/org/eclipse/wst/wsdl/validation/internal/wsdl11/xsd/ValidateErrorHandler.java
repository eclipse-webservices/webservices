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

package org.eclipse.wst.wsdl.validation.internal.wsdl11.xsd;

import java.util.ArrayList;
import java.util.List;

import org.apache.xerces.dom3.DOMError;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLErrorHandler;
import org.apache.xerces.xni.parser.XMLParseException;
import org.eclipse.wst.wsdl.validation.internal.util.ErrorMessage;

/**
 * An implementation of XMLErrorHandler that captures error from Xerces.
 */
public class ValidateErrorHandler implements XMLErrorHandler
{
  ArrayList errorList = new ArrayList();

  /**
   * Get the error messages created by Xerces.
   * 
   * @return The errors list.
   */
  public List getErrorMessages()
  {
    return errorList;
  }

  /**
   * Create a validation message from the exception and severity.
   * 
   * @param error The error.
   * @param severity The severity.
   * @return An error message.
   */
  protected ErrorMessage createValidationMessageForException(XMLParseException error, int severity)
  {
    String uri = error.getLiteralSystemId();
    if(uri == null)
    {
      uri = error.getPublicId();
    }
    ErrorMessage errorMessage = new ErrorMessage();
    errorMessage.setErrorLine(error.getLineNumber());
    errorMessage.setErrorMessage(error.getMessage());
    errorMessage.setErrorColumn(error.getColumnNumber());
    errorMessage.setURI(uri);
    errorMessage.setSeverity(severity);
    return errorMessage;
  }

  /**
   * @see org.apache.xerces.xni.parser.XMLErrorHandler#error(java.lang.String, java.lang.String, org.apache.xerces.xni.parser.XMLParseException)
   */
  public void error(String arg0, String arg1, XMLParseException exception) throws XNIException
  {
    errorList.add(createValidationMessageForException(exception, DOMError.SEVERITY_ERROR));
  }

  /**
   * @see org.apache.xerces.xni.parser.XMLErrorHandler#fatalError(java.lang.String, java.lang.String, org.apache.xerces.xni.parser.XMLParseException)
   */
  public void fatalError(String arg0, String arg1, XMLParseException exception) throws XNIException
  {
    errorList.add(createValidationMessageForException(exception, DOMError.SEVERITY_FATAL_ERROR));
  }

  /**
   * @see org.apache.xerces.xni.parser.XMLErrorHandler#warning(java.lang.String, java.lang.String, org.apache.xerces.xni.parser.XMLParseException)
   */
  public void warning(String arg0, String arg1, XMLParseException exception) throws XNIException
  {
    errorList.add(createValidationMessageForException(exception, DOMError.SEVERITY_WARNING));
  }
}
