/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.jst.ws.internal.consumption.ui.widgets.test.wssample;

import org.eclipse.jst.ws.internal.ext.WebServiceExecutable;
import org.eclipse.jst.ws.internal.ext.WebServiceFinishCommand;

/**
* This is the interface for objects that represent a kind of
* Web Service-Server-Runtime artifact. The primary purpose of a WebServiceServerRuntimeType
* object is to manufacture the wizard pages that support the type, server, and runtime configuration.
*/
public class WebServiceWSSampleExecutable implements WebServiceExecutable
{
   
  
  public WebServiceFinishCommand getFinishCommand()
  {
  	return new WSSampleFinishCommand();
  }
    
}


