/***************************************************************************************************
 * Copyright (c) 2005 IBM Corporation and others. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 **************************************************************************************************/
package org.eclipse.wst.command.internal.env.eclipse;

import org.eclipse.wst.command.internal.env.context.PersistentResourceContext;
import org.eclipse.wst.common.environment.IStatusHandler;
import org.eclipse.wst.common.environment.NullStatusHandler;

public class EnvironmentManager
{
  /**
   * Returns a new instance of an IEnvironment for the Eclipse non-GUI.
   */
  public static BaseEclipseEnvironment getNewEnvironment()
  {
    PersistentResourceContext context        = PersistentResourceContext.getInstance();
    IStatusHandler            handler        = new NullStatusHandler();
    EclipseEnvironment        environment    = new EclipseEnvironment( null, context, handler );
    
    return environment;  
  }
}
