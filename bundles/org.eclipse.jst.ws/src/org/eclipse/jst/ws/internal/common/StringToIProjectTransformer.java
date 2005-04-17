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
package org.eclipse.jst.ws.internal.common;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.wst.command.internal.env.common.FileResourceUtils;
import org.eclipse.wst.command.internal.provisional.env.core.data.Transformer;


/**
 * Transfroms a java.lang.String to an org.eclipse.core.runtime.IProject
 */
public class StringToIProjectTransformer implements Transformer
{
  /* (non-Javadoc)
   * @see org.eclipse.wst.command.internal.provisional.env.core.data.Transformer#transform(java.lang.Object)
   * @param Object This must be a java.lang.String
   * @return Object Returns an IProject
   */

  public Object transform(Object value)
  {
	String project     = (String)value;
	int    slashIndex  = project.indexOf( '/' );
	String projectName = project;
	
	if( slashIndex != -1 )
	{
	  projectName = project.substring( 0, slashIndex );
	}
	
    return ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
  }
}
