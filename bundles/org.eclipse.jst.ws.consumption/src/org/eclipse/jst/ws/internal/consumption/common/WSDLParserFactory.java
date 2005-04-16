/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.jst.ws.internal.consumption.common;

import org.eclipse.wst.ws.internal.parser.discovery.WebServicesParserExt;
import org.eclipse.wst.ws.internal.parser.wsil.WebServicesParser;

/**
 * 
 * The factory class can be used to get at a single instance of the
 * WSDL parser.  Note:  This class is temporary.  We will probably come
 * up with a better method of handling passing around the parser.
 *
 */
public class WSDLParserFactory 
{
  private static WebServicesParserExt parser_;
  
  private WSDLParserFactory()
  {
  }
  
  public static WebServicesParser getWSDLParser()
  {
    if( parser_ == null )
	{
	  parser_ = new WebServicesParserExt();
	}
	
	return parser_;
  }
}
