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


import org.eclipse.jst.ws.internal.consumption.datamodel.validate.ValidationManager;
import org.eclipse.wst.command.env.core.SimpleCommand;
import org.eclipse.wst.command.env.core.common.Environment;
import org.eclipse.wst.command.env.core.common.MessageUtils;
import org.eclipse.wst.command.env.core.common.SimpleStatus;
import org.eclipse.wst.command.env.core.common.Status;


public class RestoreValidationStateCommand extends SimpleCommand 
{
  private MessageUtils msgUtils_;
  private ValidationManager manager_;
  
  private String LABEL = "TASK_LABEL_WEBSERVICE_RESTORE_VALIDATION";
  private String DESCRIPTION = "TASK_DESC_WEBSERVICE_RESTORE_VALIDATION";  

  private boolean runValidation_;

  /**
   * Default CTOR;
   */
  public RestoreValidationStateCommand( boolean runValidation )
  {
    String pluginId = "org.eclipse.jst.ws.consumption";
    msgUtils_ = new MessageUtils(pluginId + ".plugin", this);

    setName(msgUtils_.getMessage(LABEL));
    setDescription(msgUtils_.getMessage(DESCRIPTION)); 
    runValidation_ = runValidation;
  }

  /**
   * Execute the command
   */
  public Status execute(Environment env)
  {
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

