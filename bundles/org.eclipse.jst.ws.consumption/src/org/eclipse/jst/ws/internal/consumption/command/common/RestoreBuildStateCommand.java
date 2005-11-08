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


import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.ws.internal.consumption.ConsumptionMessages;
import org.eclipse.jst.ws.internal.consumption.datamodel.validate.ValidationManager;
import org.eclipse.wst.command.internal.env.core.common.ProgressUtils;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;


public class RestoreBuildStateCommand extends AbstractDataModelOperation
{

  private ValidationManager manager_;
  
  /**
   * Default CTOR;
   */
  public RestoreBuildStateCommand()
  {
  }

  /**
   * Execute the command
   */
  public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable )
  {
    ProgressUtils.report( monitor, ConsumptionMessages.TASK_DESC_WEBSERVICE_RESTORE_BUILD);

    manager_.restoreAutoBuild();

    return Status.OK_STATUS;
  }
    
  /**
   * @param manager_ The manager_ to set.
   */
  public void setValidationManager(ValidationManager manager) {
    this.manager_ = manager;
  }
}

