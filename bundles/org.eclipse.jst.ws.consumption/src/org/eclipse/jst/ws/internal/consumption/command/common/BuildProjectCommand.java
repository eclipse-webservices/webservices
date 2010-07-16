/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20090929   250984 mahutch@ca.ibm.com - Mark Hutchinson, remove "wait for auto build" joins
 *******************************************************************************/

package org.eclipse.jst.ws.internal.consumption.command.common;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.ws.internal.consumption.datamodel.validate.ValidationManager;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;


public class BuildProjectCommand extends AbstractDataModelOperation 
{
  private ValidationManager validationManager;
  private IProject project_;
  private boolean forceBuild_;
  // rm private Model model;

  /**
   * Default CTOR;
   */
  public BuildProjectCommand()
  {
  }

  /**
   * Execute the command
   */
  public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable )
  {
    try
    {
      if (forceBuild_)
        project_.build(IncrementalProjectBuilder.INCREMENTAL_BUILD, new NullProgressMonitor());
      else if (validationManager == null)
        project_.build(IncrementalProjectBuilder.INCREMENTAL_BUILD, new NullProgressMonitor());
      else if (validationManager.getWorkspaceAutoBuildPreference())
        project_.build(IncrementalProjectBuilder.INCREMENTAL_BUILD, new NullProgressMonitor());
    }
    catch (Exception e)
    {
    }
    
     
    return Status.OK_STATUS;
  }
  /**
   * @param forceBuild The forceBuild to set.
   */
  public void setForceBuild(boolean forceBuild) {
    this.forceBuild_ = forceBuild;
  }

  /**
   * @param project The project to set.
   */
  public void setProject(IProject project) {
    this.project_ = project;
  }

  /**
   * @param validationManager The validationManager to set.
   */
  public void setValidationManager(ValidationManager validationManager) {
    this.validationManager = validationManager;
  }

  

}
