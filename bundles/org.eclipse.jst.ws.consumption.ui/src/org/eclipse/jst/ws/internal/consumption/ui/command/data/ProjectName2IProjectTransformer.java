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
package org.eclipse.jst.ws.internal.consumption.ui.command.data;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.wst.command.env.core.data.Transformer;

public class ProjectName2IProjectTransformer implements Transformer
{
  public Object transform(Object value)
  {
    return ResourcesPlugin.getWorkspace().getRoot().getProject(value.toString());
  }
}
