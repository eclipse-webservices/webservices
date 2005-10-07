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
package org.eclipse.wst.ws.internal.common;

import org.eclipse.wst.command.internal.env.ui.eclipse.EclipseEnvironment;
import org.eclipse.wst.command.internal.provisional.env.core.context.ResourceContext;
import org.eclipse.wst.command.internal.provisional.env.core.context.TransientResourceContext;
import org.eclipse.wst.common.environment.Environment;

public final class EnvironmentUtils
{     
  /**
   * 
   * @param env This should be an EclipseEnvironment.
   * @return Returns a ResourceContext.
   */ 
  public static ResourceContext getResourceContext( Environment env )
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
