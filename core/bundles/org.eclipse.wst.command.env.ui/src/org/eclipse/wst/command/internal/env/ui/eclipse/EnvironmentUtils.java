/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
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
package org.eclipse.wst.command.internal.env.ui.eclipse;

import org.eclipse.wst.command.internal.env.core.context.ResourceContext;
import org.eclipse.wst.command.internal.env.core.context.TransientResourceContext;
import org.eclipse.wst.command.internal.env.eclipse.EclipseEnvironment;
import org.eclipse.wst.common.environment.IEnvironment;

public final class EnvironmentUtils
{     
  /**
   * 
   * @param env This should be an EclipseEnvironment.
   * @return Returns a ResourceContext.
   */ 
  public static ResourceContext getResourceContext( IEnvironment env )
  {
    ResourceContext context = null;
    
    if( env instanceof EclipseEnvironment )
    {
      context = ((EclipseEnvironment)env).getResourceContext();
    }
    else
    {
      context = new TransientResourceContext();
    }
    
    return context;
  }
  
  
}
