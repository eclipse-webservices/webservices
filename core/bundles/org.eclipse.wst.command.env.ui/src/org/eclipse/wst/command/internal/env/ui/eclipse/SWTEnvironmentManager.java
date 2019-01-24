/***************************************************************************************************
 * Copyright (c) 2005 IBM Corporation and others. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors: IBM Corporation - initial API and implementation
 **************************************************************************************************/
package org.eclipse.wst.command.internal.env.ui.eclipse;

import org.eclipse.wst.command.internal.env.context.PersistentResourceContext;
import org.eclipse.wst.command.internal.env.eclipse.BaseEclipseEnvironment;
import org.eclipse.wst.command.internal.env.eclipse.EclipseEnvironment;

public class SWTEnvironmentManager
{
  /**
   * Returns a new instance of an IEnvironment for the Eclipse SWT GUI.
   */
  public static BaseEclipseEnvironment getNewSWTEnvironment()
  {
    PersistentResourceContext context        = PersistentResourceContext.getInstance();
    EclipseStatusHandler      handler        = new EclipseStatusHandler();
    EclipseEnvironment        environment    = new EclipseEnvironment( null, context, handler );
    
    return environment;  
  }
}
