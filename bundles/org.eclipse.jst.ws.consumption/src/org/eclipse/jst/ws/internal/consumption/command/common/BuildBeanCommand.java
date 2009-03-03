/*******************************************************************************
 * Copyright (c) 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20081111   252062 mahutch@ca.ibm.com - Mark Hutchinson, Don't depend on AutoBuild to compile bean
 *******************************************************************************/
package org.eclipse.jst.ws.internal.consumption.command.common;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.wst.command.internal.env.core.common.StatusUtils;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;

public class BuildBeanCommand extends AbstractDataModelOperation {

	private IProject project_;
	
	/*
	 * This Command builds the service project, and all projects it references. This is different from the
	 * BuildProjectCommand which only builds the service project
	 */
	public BuildBeanCommand() {
		//default constructor
	}
	
	public IStatus execute(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		IProject[] referencedProjects;
		
		try {
			referencedProjects = project_.getReferencedProjects();
			for (int i = 0; i < referencedProjects.length; i++) {
				referencedProjects[i].build(IncrementalProjectBuilder.INCREMENTAL_BUILD, monitor);
			}
			project_.build(IncrementalProjectBuilder.INCREMENTAL_BUILD, monitor);
			
		} catch (Exception e) {
			return StatusUtils.errorStatus(e);
		}
		return Status.OK_STATUS;
	}
	
	
	  /**
	   * @param project the service project to be built.
	   */
	  public void setProject(IProject project) {
	    this.project_ = project;
	  }

}
