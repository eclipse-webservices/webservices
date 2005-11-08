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
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.ws.internal.consumption.ConsumptionMessages;
import org.eclipse.jst.ws.internal.consumption.datamodel.validate.ValidationManager;
import org.eclipse.wst.command.internal.env.core.common.ProgressUtils;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;


public class DisableValidationCommand extends AbstractDataModelOperation 
{

  private ValidationManager manager_;
  private IProject serviceProject_;

  /**
   * CTOR;
   */
  public DisableValidationCommand() {
  }

  /**
   * Execute the command
   */
  public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable )
  {
    IStatus status = Status.OK_STATUS;

    ProgressUtils.report(monitor, ConsumptionMessages.TASK_DESC_WEBSERVICE_DISABLE_VALIDATION);

    IProject project = serviceProject_;
    if (project != null) manager_.disableValidationForProject(project);

    return status;
  }

  /**
   * @param manager_
   *            The manager_ to set.
   */
  public void setValidationManager(ValidationManager manager) {
    this.manager_ = manager;
  }
  
  public void setServiceProject(IProject serviceProject){
    this.serviceProject_ = serviceProject;
  }
}
