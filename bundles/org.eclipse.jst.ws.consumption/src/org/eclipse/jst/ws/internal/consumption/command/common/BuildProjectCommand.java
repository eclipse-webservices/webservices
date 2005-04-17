/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
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
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jst.ws.internal.consumption.datamodel.validate.ValidationManager;
import org.eclipse.jst.ws.internal.consumption.plugin.WebServiceConsumptionPlugin;
import org.eclipse.wst.command.internal.provisional.env.core.SimpleCommand;
import org.eclipse.wst.command.internal.provisional.env.core.common.Environment;
import org.eclipse.wst.command.internal.provisional.env.core.common.SimpleStatus;
import org.eclipse.wst.command.internal.provisional.env.core.common.Status;


public class BuildProjectCommand extends SimpleCommand 
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
    super( WebServiceConsumptionPlugin.getMessage("%TASK_LABEL_WEBSERVICE_BUILD_PROJECT"),
           WebServiceConsumptionPlugin.getMessage("%TASK_DESC_WEBSERVICE_BUILD_PROJECT") );
  }

  /**
   * Execute the command
   */
  public Status execute(Environment env)
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
    /*
    try
    {
      Platform.getJobManager().join(null, new NullProgressMonitor());
    }
    catch (InterruptedException ie)
    {
      // continue execution
    }
    catch (OperationCanceledException oce)
    {
      // continue execution
    }
    */
    return new SimpleStatus("");
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
