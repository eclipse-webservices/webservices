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
import org.eclipse.jst.ws.internal.consumption.datamodel.validate.ValidationManager;
import org.eclipse.wst.command.env.core.SimpleCommand;
import org.eclipse.wst.command.env.core.common.Environment;
import org.eclipse.wst.command.env.core.common.MessageUtils;
import org.eclipse.wst.command.env.core.common.SimpleStatus;
import org.eclipse.wst.command.env.core.common.Status;


public class DisableValidationCommand extends SimpleCommand {

  private MessageUtils msgUtils_;
  private ValidationManager manager_;
  private IProject serviceProject_;
  private String LABEL = "TASK_LABEL_WEBSERVICE_DISABLE_VALIDATION";
  private String DESCRIPTION = "TASK_DESC_WEBSERVICE_DISABLE_VALIDATION";

  /**
   * CTOR;
   */
  public DisableValidationCommand() {
    String pluginId = "org.eclipse.jst.ws.consumption";
    msgUtils_ = new MessageUtils(pluginId + ".plugin", this);

    setName(msgUtils_.getMessage(LABEL));
    setDescription(msgUtils_.getMessage(DESCRIPTION));
  }

  /**
   * Execute the command
   */
  public Status execute(Environment env) {
    Status status = new SimpleStatus("");

    env.getProgressMonitor().report(msgUtils_.getMessage("TASK_DESC_WEBSERVICE_DISABLE_VALIDATION"));

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
