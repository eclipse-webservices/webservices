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

package org.eclipse.jst.ws.internal.consumption.command.common;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jem.util.emf.workbench.ProjectUtilities;
import org.eclipse.jst.ws.internal.common.J2EEUtils;
import org.eclipse.jst.ws.internal.consumption.ConsumptionMessages;
import org.eclipse.osgi.util.NLS;
import org.eclipse.wst.command.internal.env.core.common.StatusUtils;
import org.eclipse.wst.common.environment.IEnvironment;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;

public class AssociateModuleWithEARCommand extends AbstractDataModelOperation
{
	private String project_;
	private String module_;
	private String earProject_;
	private String ear_;
	

  public AssociateModuleWithEARCommand(){

  }
  
	public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable )
	{
    IEnvironment env = getEnvironment();
    
		IStatus status = Status.OK_STATUS;
		IProject moduleProject = null;
		IProject earProject = null;

		// get projects
		if (project_!=null)
			moduleProject = ProjectUtilities.getProject(project_);
		if (earProject_!=null)
			earProject = ProjectUtilities.getProject(earProject_);
		
		// associate modules if not already associated
		if (moduleProject!=null && earProject!=null) {
			if (!J2EEUtils.isComponentAssociated(earProject, moduleProject))
				J2EEUtils.associateComponentToEAR(moduleProject, earProject);
		}
		
		// ensure modules are associated otherwise report error
		if (!J2EEUtils.isComponentAssociated(earProject, moduleProject)){
			status = StatusUtils.errorStatus( NLS.bind(ConsumptionMessages.MSG_ERROR_UNABLE_TO_ASSOCIATE, new String[]{module_, ear_}) );
			if (env!=null)
				env.getStatusHandler().reportError(status);
		    return status; 
		}
		
		return status;
	}
	
  public void setProject( String project )
  {
	  project_ = project;
  }
	  
  public void setModule( String module )
  {
	  module_ = module;
  }	
	
  public void setEARProject( String earProject )
  {
	  earProject_ = earProject;
  }
  
  public void setEar( String ear )
  {
	  ear_ = ear;  
  }	
}
