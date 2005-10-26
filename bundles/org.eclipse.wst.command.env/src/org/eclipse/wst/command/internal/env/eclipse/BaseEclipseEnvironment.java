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
package org.eclipse.wst.command.internal.env.eclipse;

import org.eclipse.wst.command.internal.env.core.context.ResourceContext;
import org.eclipse.wst.common.environment.IEnvironment;


/**
 * This interface adds resources to the base IEnvironment.
 */
public interface BaseEclipseEnvironment extends IEnvironment 
{
  /**
   * 
   * @return returns a ResourceContext object for this environment.
   */	
  public ResourceContext getResourceContext();
}
