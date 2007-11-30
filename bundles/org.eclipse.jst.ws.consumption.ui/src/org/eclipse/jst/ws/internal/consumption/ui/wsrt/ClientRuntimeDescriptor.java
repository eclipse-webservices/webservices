/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20071107 203826 kathy@ca.ibm.com - Kathy Chan
 * 20071130   203826 Kathy Chan - Kathy Chan
 *******************************************************************************/

package org.eclipse.jst.ws.internal.consumption.ui.wsrt;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.jst.ws.internal.consumption.common.RequiredFacetVersion;
import org.eclipse.wst.common.project.facet.core.IProjectFacet;
import org.eclipse.wst.common.project.facet.core.IProjectFacetVersion;
import org.eclipse.wst.common.project.facet.core.ProjectFacetsManager;
import org.eclipse.wst.ws.internal.wsrt.IWebServiceRuntime;
import org.eclipse.wst.ws.internal.wsrt.IWebServiceRuntimeChecker;

public class ClientRuntimeDescriptor
{
  private IConfigurationElement elem;
  private Hashtable allWebServiceClientImpls;
  private Hashtable allRuntimes;
  private String id;
  private WebServiceClientImpl clientImplementationType;
  private RuntimeDescriptor runtime;
  private String clientRuntimeClassName;
  private IWebServiceRuntime webServiceRuntime;
  private RequiredFacetVersion[] requiredFacetVersions;
  private Set projectFacetVersions;
  private IWebServiceRuntimeChecker webServiceRuntimeChecker;
  
  public ClientRuntimeDescriptor(IConfigurationElement elem, Hashtable allWebServiceClientImpls, Hashtable allRuntimes)
  {
    this.elem = elem;
    this.allWebServiceClientImpls = allWebServiceClientImpls;
    this.allRuntimes = allRuntimes;
  }
  
  public WebServiceClientImpl getClientImplementationType()
  {
    if (clientImplementationType == null)
    {
     String clientImplementationTypeId = elem.getAttribute("clientImplementationTypeId");
     clientImplementationType = (WebServiceClientImpl)allWebServiceClientImpls.get(clientImplementationTypeId);
    }
    return clientImplementationType;
  }
  
  public String getClientRuntimeClassName()
  {
    if (clientRuntimeClassName == null)
    {
      clientRuntimeClassName = elem.getAttribute("class");
    }
    return clientRuntimeClassName;
  }
  
  public String getId()
  {
    if (id == null)
    {
      id = elem.getAttribute("id");
    }    
    return id;
  }
  
  public RequiredFacetVersion[] getRequiredFacetVersions()
  {
    if (requiredFacetVersions == null)
    {
      ArrayList requiredFacetVersionList = new ArrayList();
      IConfigurationElement[] facetElems = elem.getChildren("required-facet-version");
      for (int i = 0; i < facetElems.length; i++)
      {
        RequiredFacetVersion rfv = new RequiredFacetVersion();
        IProjectFacet projectFacet = ProjectFacetsManager.getProjectFacet(facetElems[i].getAttribute("facet"));        
        IProjectFacetVersion projectFacetVersion = projectFacet.getVersion(facetElems[i].getAttribute("version"));
        rfv.setProjectFacetVersion(projectFacetVersion);
        String allowNewerValue = facetElems[i].getAttribute("allow-newer");
        if (allowNewerValue == null)
        {
          rfv.setAllowNewer(false);
        }
        else
        {
          rfv.setAllowNewer(Boolean.valueOf(allowNewerValue).booleanValue());
        }
        
        requiredFacetVersionList.add(rfv);
      }
      
      requiredFacetVersions = (RequiredFacetVersion[])requiredFacetVersionList.toArray(new RequiredFacetVersion[]{});
    }
    
    return requiredFacetVersions;
  }
  
  public Set getProjectFacetVersions()
  {
    if (projectFacetVersions == null)
    {
     projectFacetVersions = new HashSet();
     RequiredFacetVersion[] rfv = getRequiredFacetVersions();
     for (int i=0; i<rfv.length; i++)
     {
       projectFacetVersions.add(rfv[i].getProjectFacetVersion());
     }
    }
    
    return projectFacetVersions;    
  }  
  
  public RuntimeDescriptor getRuntime()
  {
    if (runtime == null)
    {
     String runtimeId = elem.getAttribute("runtimeId");
     runtime = (RuntimeDescriptor)allRuntimes.get(runtimeId);
    }    
    return runtime;
  }
  
  public IWebServiceRuntime getWebServiceRuntime()
  {
    if (webServiceRuntime == null)
    {
        try
        {
            webServiceRuntime = (IWebServiceRuntime)elem.createExecutableExtension("class");
        }
        catch(CoreException ce)
        {
            
        }
    }
    
    return webServiceRuntime;
  }
  
  public IWebServiceRuntimeChecker getClientRuntimeChecker()
  {
    if (webServiceRuntimeChecker == null)
    {
        try
        {
        	if (elem.getAttribute("runtimeChecker") != null ) {
        		webServiceRuntimeChecker = (IWebServiceRuntimeChecker)elem.createExecutableExtension("runtimeChecker");
        	}
        }
        catch(CoreException ce)
        {
            
        }
    }
    
    return webServiceRuntimeChecker;
  } 
}
