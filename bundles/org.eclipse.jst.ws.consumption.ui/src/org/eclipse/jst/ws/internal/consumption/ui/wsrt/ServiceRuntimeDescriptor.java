/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug           Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060802   152150 mahutch@ca.ibm.com
 * 20071107        203826 kathy@ca.ibm.com - Kathy Chan
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


public class ServiceRuntimeDescriptor
{
  private IConfigurationElement elem;
  private Hashtable allWebServiceImpls;
  private Hashtable allRuntimes;
  private String id;
  private WebServiceImpl serviceImplementationType;
  private RuntimeDescriptor runtime;
  private boolean bottomUp = false;
  private boolean topDown = false;
  private String serviceRuntimeClassName;
  private IWebServiceRuntime webServiceRuntime;
  private RequiredFacetVersion[] requiredFacetVersions;
  private Set projectFacetVersions;
  private IWebServiceRuntimeChecker webServiceRuntimeChecker;
  
  public ServiceRuntimeDescriptor(IConfigurationElement elem, Hashtable allWebServiceImpls, Hashtable allRuntimes)
  {
    this.elem = elem;
    this.allWebServiceImpls = allWebServiceImpls;
    this.allRuntimes = allRuntimes;

    bottomUp = (Boolean.valueOf(elem.getAttribute("bottomUp"))).booleanValue();
    topDown = (Boolean.valueOf(elem.getAttribute("topDown"))).booleanValue();    
  }
  
  public boolean getBottomUp()
  {    
    return bottomUp;
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
    	String facetID = facetElems[i].getAttribute("facet");
    	if (ProjectFacetsManager.isProjectFacetDefined(facetID))
    	{
	        RequiredFacetVersion rfv = new RequiredFacetVersion();
	        IProjectFacet projectFacet = ProjectFacetsManager.getProjectFacet(facetID);        
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
  
  public WebServiceImpl getServiceImplementationType()
  {
    if (serviceImplementationType == null)
    {      
      String serviceImplementationTypeId = elem.getAttribute("serviceImplementationTypeId");
      serviceImplementationType = (WebServiceImpl)allWebServiceImpls.get(serviceImplementationTypeId);
    }
    return serviceImplementationType;
  }
  
  public String getServiceRuntimeClassName()
  {
    if (serviceRuntimeClassName == null)
    {
      serviceRuntimeClassName = elem.getAttribute("class");
    }
    return serviceRuntimeClassName;
  }
  
  public boolean getTopDown()
  {
    return topDown;
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
  
  public IWebServiceRuntimeChecker getServiceRuntimeChecker()
  {
    if (webServiceRuntimeChecker == null)
    {
        try
        {
        	if (elem.getAttribute("checkerClass") != null ) {
        		webServiceRuntimeChecker = (IWebServiceRuntimeChecker)elem.createExecutableExtension("checkerClass");
        	}
        }
        catch(CoreException ce)
        {
            
        }
    }
    
    return webServiceRuntimeChecker;
  } 
  
}
