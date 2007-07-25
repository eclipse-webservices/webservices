/*******************************************************************************
 * Copyright (c) 2006, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060427   138058 joan@ca.ibm.com - Joan Haggarty
 * 20070723   194434 kathy@ca.ibm.com - Kathy Chan, Check for non-existing EAR with content not deleted
 *******************************************************************************/
package org.eclipse.jst.ws.internal.consumption.ui.common;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jem.util.emf.workbench.ProjectUtilities;
import org.eclipse.jst.ws.internal.common.J2EEUtils;
import org.eclipse.jst.ws.internal.common.ResourceUtils;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;

public class DefaultingUtils {
	
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
			          IStatus associationStatus = J2EEUtils.canAssociateProjectToEAR(proj, earProject);
			          if (associationStatus.getSeverity()==IStatus.OK)
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
}
