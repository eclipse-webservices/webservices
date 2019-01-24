/*******************************************************************************
 * Copyright (c) 2006, 2015 IBM Corporation and others.
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
 * 20060427   138058 joan@ca.ibm.com - Joan Haggarty
 * 20070723   194434 kathy@ca.ibm.com - Kathy Chan, Check for non-existing EAR with content not deleted
 * 20150311   461526 jgwest@ca.ibm.com - Jonathan West,  Allow OSGi bundles to be selected in the Wizard 
 *******************************************************************************/
package org.eclipse.jst.ws.internal.consumption.ui.common;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jem.util.emf.workbench.ProjectUtilities;
import org.eclipse.jst.ws.internal.common.J2EEUtils;
import org.eclipse.jst.ws.internal.common.ResourceUtils;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;

public class DefaultingUtils {

	// Immutable map
	private static final Map<String /*template id*/, IWebServiceOSGISupportExtension> osgiExtensions;

	private static final String EXTENSION_POINT_WS_OSGI_SUPPORT = "webServiceOSGISupport";
	private static final String OSGI_SUPPORT_ELEMENT = "webServiceOSGISupport";
	private static final String OSGI_SUPPORT_CLASS_ATTR = "class";
	
	private static final String OSGI_SUPPORT_TEMPLATES_ATTR = "applicableTemplates";
	
	static {
		
		Map<String, IWebServiceOSGISupportExtension> result = new HashMap<String, IWebServiceOSGISupportExtension>();
		
		try {
		    IExtensionRegistry reg = Platform.getExtensionRegistry();
		    
		    IConfigurationElement[] wsImplExts = reg.getConfigurationElementsFor(
		        "org.eclipse.jst.ws.consumption.ui", EXTENSION_POINT_WS_OSGI_SUPPORT);
		    
		    for(int idx=0; idx<wsImplExts.length; idx++) 
		    {
		      IConfigurationElement elem = wsImplExts[idx];        
		        if (elem.getName().equals(OSGI_SUPPORT_ELEMENT))
		        {
		        	try {
		        		String applicableTemplatesStr = elem.getAttribute(OSGI_SUPPORT_TEMPLATES_ATTR);
		        		if(applicableTemplatesStr != null) {
		        			
		        			IWebServiceOSGISupportExtension provider = (IWebServiceOSGISupportExtension) elem.createExecutableExtension(OSGI_SUPPORT_CLASS_ATTR);
		        			
		        			if(provider != null) {
			        			// Split by all whitespace
			        			String[] templates = applicableTemplatesStr.split("\\s+");
			        			for(String template : templates) {
			        				result.put(template.toLowerCase(), provider);
			        			}
		        			}
		        		}
		        		
					} catch (CoreException e) {
						// Ignore issues with individual implementors
						e.printStackTrace();
					} 
		        	
		        }        
		    }
		} finally {
			osgiExtensions = Collections.unmodifiableMap(result);
		}
		
	}

	public DefaultingUtils()
	{
			
	}
	
	/**
	 * 
	 * Uses the following steps to determine a valid default EAR project name:
	 *  
	 * 1. If project does exist:
	 * 		a - look for an existing, referencing EAR component, if one exists return that EAR name
	 * 		b - look for an existing EAR component with the same J2EE version as the project
	 * 2. If can't find an appropriate existing EAR component or project does not exist in the 
	 *     workspace, return a new EAR name of form projectNameEAR
	 * 3. If project name is null or an empty string return the default EAR name
	 * 
	 * @param projectName a string representing the name of the project for the service
	 * @return a string to be used as the default EAR project name for the project name provided
	 */
	public static String getDefaultEARProjectName(String projectName)
	{

		if (projectName != null && projectName.length() > 0) {
			
			if(isOSGIProject(projectName)) 
			{
				return getDefaultOSGIAppProjectName(projectName);
			}
			
			IProject proj = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);

			if (proj.exists()) {
				
				//Step 1a - return a referencing EAR project
				IVirtualComponent[] ears = J2EEUtils.getReferencingEARComponents(proj);
				if (ears != null && ears.length > 0) {
					return ears[0].getName();
				}
				
				//Step 1b - return an appropriate existing EAR project
			    IVirtualComponent[] allEarComps = J2EEUtils.getAllEARComponents();			      

			    if (allEarComps.length > 0)
			    {			      
			        for (int i=0; i < allEarComps.length; i++)
			        {
			          IProject earProject = allEarComps[i].getProject();
			          boolean canAssociate = J2EEUtils.canAssociateProjectToEARWithoutWarning(proj, earProject);
			          if (canAssociate)
			          {
			            return allEarComps[i].getName(); 
			          }
			        }			      			      
			    }
			}
			
			
			String baseEARName = projectName + ResourceUtils.getDefaultEARExtension();
			String earName = baseEARName;

			boolean foundEAR = false;
			int i = 1;
			
			while (!foundEAR) {
				// 194434 - Check for non-existing EAR with contents that's not deleted previously
				IStatus canCreateEARStatus = J2EEUtils.canCreateEAR(ProjectUtilities.getProject(earName));
				if (canCreateEARStatus.isOK()) {
					foundEAR = true;
				} else {
					earName = baseEARName + i;
					i++;
				}
			}
			
			
			//Step 2 - return project name with EAR on the end
			return earName;			
		}
		
		//Step 3 - return the default EAR project name
		return ResourceUtils.getDefaultServiceEARProjectName();		
	}

	public static boolean isOSGITemplate(String name) 
	{
		if(name == null) { return false; } 
		
		try {
			if(osgiExtensions == null) { return false; }
	
			for(String template : osgiExtensions.keySet()) 
			{
				if(name.equalsIgnoreCase(template.trim())) 
				{
					return true;
				}
				
			}
			
		} catch(Exception e) 
		{
			// Ignore; first, do no harm.
			e.printStackTrace();
		}
		
		return false;
	}
	
	private static IProject utilFindProjectWithName(String proj) 
	{
		if(proj == null) 
		{
			return null;
		}
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		
		IProject project = root.getProject(proj);
		return project;
	}
	
	public static boolean isOSGIProject(String proj) 
	{
		try 
		{
			IProject project = utilFindProjectWithName(proj);
			if(project == null) 
			{
				return false;
			} else {
				return isSupportedOSGIProject(project);
			}
		} catch(Exception e) 
		{
			// First, do no harm.
			e.printStackTrace();
			return false;
		}
		
	}
	
	
	
	public static boolean isSupportedOSGIProject(IProject project) 
	{
		try {
			
			if(osgiExtensions == null || project == null) { return false; }
			
			for(Map.Entry<String, IWebServiceOSGISupportExtension> entry : osgiExtensions.entrySet()) 
			{
				
				if(entry.getValue().isSupportedOSGIProject(project)) 
				{
					return true;
				}
				
			}
			
			return false;
			
		} catch(Exception e) {
			// First, do no harm.
			e.printStackTrace();
			return false;
		}
				
	}
	
	/** Return the interface of the first extension that says it supports this project */
	private static IWebServiceOSGISupportExtension getFirstSupportedExtension(String projectName) 
	{
		IProject proj = utilFindProjectWithName(projectName);
		if(proj == null) { return null; } 
		
		for(Map.Entry<String, IWebServiceOSGISupportExtension> entry : osgiExtensions.entrySet()) 
		{
			
			if(entry.getValue().isSupportedOSGIProject(proj)) 
			{
				return entry.getValue();
			}
			
		}
		
		return null;
		
	}
	
	public static String getDefaultOSGIAppProjectName(String projectName) 
	{
		try 
		{
			if(osgiExtensions == null || projectName == null) { return null; }
			
			IWebServiceOSGISupportExtension ext = getFirstSupportedExtension(projectName);
			
			if(ext != null) 
			{
				return ext.getDefaultOSGIAppProjectName(projectName);
			}
			
		} catch(Exception e) 
		{
			// First, do no harm.
			e.printStackTrace();
		}
		
		
		return projectName+".app";	
		
	}
	
	
	
}
