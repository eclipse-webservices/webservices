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
 * 20071107 203826 kathy@ca.ibm.com - Kathy Chan
 * 20071130 203826 kathy@ca.ibm.com - Kathy Chan
 * 20080326   221364 kathy@ca.ibm.com - Kathy Chan
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
  private String runtimePreferredServerType;
  private  Set<String> suitableProjectTemplates;
  private  Set<String> unsuitableProjectTemplates;
  private boolean allowClientServerRestriction;
  private List<String> supportedServers;
  private List<String> unsupportedServers;
  
  public ClientRuntimeDescriptor(IConfigurationElement elem, Hashtable allWebServiceClientImpls, Hashtable allRuntimes)
  {
    this.elem = elem;
    this.allWebServiceClientImpls = allWebServiceClientImpls;
    this.allRuntimes = allRuntimes;
    
    allowClientServerRestriction = Boolean.parseBoolean(elem.getAttribute("allowClientServerRestriction"));
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
  
  public void processClientRuntimeProperties(IConfigurationElement elem) {
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
  
  public String getRuntimePreferredServerType()
  {
    if (runtimePreferredServerType == null)
    {
    	runtimePreferredServerType = elem.getAttribute("runtimePreferredServerType");
    }
    return runtimePreferredServerType;
  }
  
  public boolean allowClientServersRestriction() {
	  return allowClientServerRestriction;
  }
   
  /**
   * Note that only the supported or unsupported attribute should be used, not both.
   */
  public boolean isSupportedServer(String id) {
	if(!allowClientServerRestriction)
		return false;
	
	String serverElements = elem.getAttribute("supportedServers");
	// Extension defines supportedServers
	if (supportedServers == null)
 	      supportedServers = parseServers(serverElements);
	// If the extension does not define supportedServers but defines unsupportedServers
	// This is for the case when a server does not support a particular client runtime
	if (serverElements == null)
	{
       String unsupportedServerElements = elem.getAttribute("unsupportedServers");
       if (unsupportedServers == null)
          unsupportedServers = parseServers(unsupportedServerElements);
       // If it is not in the unsupported list, then it must be supported, as extensions should
       // not have to list off all the possible supported servers (and since currently only one 
       // of unsupportedServer or supportedServers is recognized).
   	   if (!unsupportedServers.contains(id) && !supportedServers.contains(id))
	   {
	     supportedServers.add(id);
	   }
	}
	return supportedServers.contains(id);
  }
  
  /**
   * Note that only the supported or unsupported attribute should be used, not both.
   */
  public boolean isUnsupportedServer(String id) {
	if(!allowClientServerRestriction)
		return false;
	if(unsupportedServers == null) {
		String serverElements = elem.getAttribute("unsupportedServers");
		unsupportedServers = parseServers(serverElements);
	}
	return unsupportedServers.contains(id);
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
