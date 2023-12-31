/*******************************************************************************
 * Copyright (c) 2005, 2013 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060802   152150 mahutch@ca.ibm.com
 * 20071107   203826 kathy@ca.ibm.com - Kathy Chan
 * 20071130   203826 kathy@ca.ibm.com - Kathy Chan
 * 20080326   221364 kathy@ca.ibm.com - Kathy Chan
 * 20080626   221364 kathy@ca.ibm.com - Kathy Chan
 * 20100811   322429 mahutch@ca.ibm.com - Mark Hutchinson, Improve performance of launching the Web Services Wizard
 *******************************************************************************/

package org.eclipse.jst.ws.internal.consumption.ui.wsrt;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
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
  private boolean allowServiceServerRestriction;
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
  private String runtimePreferredServerType;
  private  Set<String> suitableProjectTemplates;
  private  Set<String> unsuitableProjectTemplates;
  private List<String> supportedServers;
  
  public ServiceRuntimeDescriptor(IConfigurationElement elem, Hashtable allWebServiceImpls, Hashtable allRuntimes)
  {
    this.elem = elem;
    this.allWebServiceImpls = allWebServiceImpls;
    this.allRuntimes = allRuntimes;

    allowServiceServerRestriction = Boolean.parseBoolean(elem.getAttribute("allowServiceServerRestriction"));
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
  
  
  public void processServiceRuntimeProperties(IConfigurationElement elem) {
	  if (suitableProjectTemplates == null) {
		  suitableProjectTemplates = new HashSet<String>(5);
	  }
	  if (unsuitableProjectTemplates == null) {
		  unsuitableProjectTemplates = new HashSet<String>(5);
	  }
	  IConfigurationElement[] elements = elem.getChildren("suitable-project-template");
	  for (IConfigurationElement configElement : elements) {
		  String templateId = configElement.getAttribute("id");
		  if (templateId != null)
			  suitableProjectTemplates.add(templateId);
	  }
	  elements = elem.getChildren("unsuitable-project-template");
      for (IConfigurationElement configElement : elements) {
    	  String templateId = configElement.getAttribute("id");
    	  if (templateId != null)
    		  unsuitableProjectTemplates.add(templateId);
      }
  }
  
  //this will return an empty list if no valid suitable-project-template elements found
  public Set<String> getSuitableProjectTemplates() {
	  if (suitableProjectTemplates == null) {
		  suitableProjectTemplates = new HashSet<String>(5);
	   }
	   return suitableProjectTemplates;
  }
  
  //this will return an empty list if no valid unsuitable-project-template elements found
  public Set<String> getUnsuitableProjectTemplates() {
	  if (unsuitableProjectTemplates == null) {
		  unsuitableProjectTemplates = new HashSet<String>(5);
	  }
	  return unsuitableProjectTemplates;
  }
  
  public RequiredFacetVersion[] getRequiredFacetVersions()
  {
    if (requiredFacetVersions == null)
    {
      ArrayList requiredFacetVersionList = new ArrayList();
      IConfigurationElement[] facetElems = elem.getChildren("required-facet-version");
      for (int i = 0; i < facetElems.length; i++)
      {
    	try {
    	  String facetID = facetElems[i].getAttribute("facet");
    	  if (ProjectFacetsManager.isProjectFacetDefined(facetID))
    	  {
	        RequiredFacetVersion rfv = new RequiredFacetVersion();
	        IProjectFacet projectFacet = ProjectFacetsManager.getProjectFacet(facetID);
	        if (projectFacet != null) {
	        	IProjectFacetVersion projectFacetVersion = projectFacet.getVersion(facetElems[i].getAttribute("version"));
	        	if (projectFacetVersion != null) {
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
    	
    	  } 
        } catch (IllegalArgumentException e){
        	// ignore the facet
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
  
  public String getRuntimePreferredServerType()
  {
    if (runtimePreferredServerType == null)
    {
    	runtimePreferredServerType = elem.getAttribute("runtimePreferredServerType");
    }
    return runtimePreferredServerType;
  }
  
  public boolean allowServiceServersRestriction() {
	  return allowServiceServerRestriction;
  }
    
  public boolean isSupportedServer(String id) {
	if(!allowServiceServerRestriction)
		return false;
	if(supportedServers == null) {
		String serverElements = elem.getAttribute("supportedServers");
		supportedServers = parseServers(serverElements);
	}
	return supportedServers.contains(id);
  }
  
  private List<String> parseServers(String serverElements) {
	List<String> serverList = new ArrayList<String>();
	if(serverElements != null) {
		String[] servers = serverElements.split("\\s+");
		for (int i = 0; i < servers.length; i++)  {
			if (servers[i].length() > 0)
				serverList.add(servers[i]);
		}
	}
	return serverList;
  }
}
