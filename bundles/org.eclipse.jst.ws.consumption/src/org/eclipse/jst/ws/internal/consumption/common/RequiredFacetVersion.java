/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.jst.ws.internal.consumption.common;


import org.eclipse.wst.common.project.facet.core.IProjectFacetVersion;

public class RequiredFacetVersion
{
  private IProjectFacetVersion projectFacetVersion;
  private boolean allowNewer;
  
  public boolean getAllowNewer()
  {
    return allowNewer;
  }
  public void setAllowNewer(boolean allowNewer)
  {
    this.allowNewer = allowNewer;
  }
  public IProjectFacetVersion getProjectFacetVersion()
  {
    return projectFacetVersion;
  }
  public void setProjectFacetVersion(IProjectFacetVersion projectFacetVersion)
  {
    this.projectFacetVersion = projectFacetVersion;
  }  
  
}
