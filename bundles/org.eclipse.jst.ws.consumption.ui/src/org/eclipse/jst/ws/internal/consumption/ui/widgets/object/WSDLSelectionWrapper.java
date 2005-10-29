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

package org.eclipse.jst.ws.internal.consumption.ui.widgets.object;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.wst.ws.internal.parser.wsil.WebServicesParser;

/**
 * This class is a very simple wrapper that binds a WSDL parser with
 * a wsdlSelection object.
 */
public class WSDLSelectionWrapper 
{
  public WebServicesParser    parser;
  public IStructuredSelection wsdlSelection;
  
  public WSDLSelectionWrapper( WebServicesParser    parser,
                               IStructuredSelection selection )
  {
    this.parser        = parser;
    this.wsdlSelection = selection;
  }
}
