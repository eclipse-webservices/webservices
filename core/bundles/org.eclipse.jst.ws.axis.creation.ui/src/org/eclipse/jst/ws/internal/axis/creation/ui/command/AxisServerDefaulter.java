/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.jst.ws.internal.axis.creation.ui.command;

import org.eclipse.core.resources.IProject;
import org.eclipse.jst.ws.internal.consumption.common.IServerDefaulter;
import org.eclipse.jst.ws.internal.consumption.common.ServerInfo;



public class AxisServerDefaulter implements IServerDefaulter
{
  /* (non-Javadoc)
   * @see org.eclipse.jst.ws.internal.consumption.common.IServerDefaulter#recommendDefaultServer(org.eclipse.core.resources.IProject)
   */
  public ServerInfo recommendDefaultServer(IProject project)
  {    
    return null;
  }
}
