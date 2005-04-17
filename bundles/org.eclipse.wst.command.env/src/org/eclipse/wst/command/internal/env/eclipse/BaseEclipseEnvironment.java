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

import org.eclipse.wst.command.internal.provisional.env.core.common.Environment;
import org.eclipse.wst.command.internal.provisional.env.core.context.ResourceContext;


/**
 * This interface adds resources to the base Environment.
 */
public interface BaseEclipseEnvironment extends Environment 
{
  /**
   * 
   * @return returns a ResourceContext object for this environment.
   */	
  public ResourceContext getResourceContext();
}
