/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.jst.ws.internal.consumption.ui.widgets.test.explorer;

import org.eclipse.jst.ws.internal.ext.WebServiceExecutable;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;

/**
* This is the interface for objects that represent a kind of
* Web Service-Server-Runtime artifact. The primary purpose of a WebServiceServerRuntimeType
* object is to manufacture the wizard pages that support the type, server, and runtime configuration.
*/
public class WebServiceExplorerExecutable implements WebServiceExecutable
{
  // Copyright
  public static final String copyright = "(c) Copyright IBM Corporation 2000, 2002.";
   
  
  public AbstractDataModelOperation getFinishCommand()
  {
    return new ExplorerServiceTestCommand();
  }
  
}


