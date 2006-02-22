/*******************************************************************************
 * Copyright (c) 2004, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060221   124302 rsinha@ca.ibm.com - Rupam Kuehner
 *******************************************************************************/
package org.eclipse.jst.ws.internal.consumption.ui.command.data;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.wst.command.internal.env.core.data.Transformer;

public class ProjectName2IProjectTransformer implements Transformer
{
  public Object transform(Object value)
  {
    if (value != null)
    {
      String project = (String) value;
      int slashIndex = project.indexOf('/');
      String projectName = project;

      if (slashIndex != -1)
      {
        projectName = project.substring(0, slashIndex);
      }

      return ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
    } else
    {
      return null;
    }
  }
}
