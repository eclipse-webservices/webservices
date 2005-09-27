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
import org.eclipse.jst.ws.internal.consumption.datamodel.validate.ValidationManager;
import org.eclipse.wst.command.internal.provisional.env.core.EnvironmentalOperation;
import org.eclipse.wst.command.internal.provisional.env.core.common.Environment;
import org.eclipse.wst.command.internal.provisional.env.core.common.MessageUtils;
import org.eclipse.wst.command.internal.provisional.env.core.common.SimpleStatus;


public class RestoreValidationStateCommand extends EnvironmentalOperation 
{
  private MessageUtils msgUtils_;
  private ValidationManager manager_;
  
  private boolean runValidation_;

  /**
   * Default CTOR;
   */
  public RestoreValidationStateCommand( boolean runValidation )
  {
    String pluginId = "org.eclipse.jst.ws.consumption";
    msgUtils_ = new MessageUtils(pluginId + ".plugin", this);

    runValidation_ = runValidation;
  }

  /**
   * Execute the command
   */
  public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable )
  {
    Environment env = getEnvironment();
    env.getProgressMonitor().report(msgUtils_.getMessage("TASK_DESC_WEBSERVICE_RESTORE_VALIDATION"));

    manager_.restoreValidationForProjects( runValidation_ );    

    return new SimpleStatus("");
  }
    
  /**
   * @param manager The validation manager to set.
   */
  public void setValidationManager(ValidationManager manager) {
    this.manager_ = manager;
  }
}

