/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060131 121071   rsinha@ca.ibm.com - Rupam Kuehner          
 *******************************************************************************/

package org.eclipse.jst.ws.internal.consumption.common;


import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.wst.common.project.facet.core.IProjectFacetVersion;

public class RequiredFacetVersion
{
  private IProjectFacetVersion projectFacetVersion;
  private boolean allowNewer;
  private IProjectFacetVersion[] allowedProjectFacetVersions;
  
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
  public IProjectFacetVersion[] getAllowedProjectFacetVersions()  
  {
    if (allowedProjectFacetVersions == null)
    {
      ArrayList versions = new ArrayList();
      IProjectFacetVersion minpfv = getProjectFacetVersion();
      versions.add(minpfv);
      //If allow-newer is true, add all the versions greater than the min version.
      if (getAllowNewer())
      {
        String minVersion = minpfv.getVersionString();
        Iterator allVersionsItr = minpfv.getProjectFacet().getVersions().iterator();
        while (allVersionsItr.hasNext())
        {
          IProjectFacetVersion testpfv = (IProjectFacetVersion)allVersionsItr.next();
          String testVersion = testpfv.getVersionString();
          if (FacetUtils.greaterThan(testVersion, minVersion))
          {
            versions.add(testpfv);              
          }
        }      
      }
      
      allowedProjectFacetVersions = (IProjectFacetVersion[])versions.toArray(new IProjectFacetVersion[0]);      
    }
    
    return allowedProjectFacetVersions;
  }
  
}
