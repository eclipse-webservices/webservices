/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.ws.internal.explorer.platform.actions;

import org.eclipse.wst.ws.internal.explorer.LaunchWizardRunnable;
import org.eclipse.wst.ws.internal.explorer.LaunchWizardTask;
import org.eclipse.wst.ws.internal.explorer.platform.constants.ActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller;
import org.eclipse.wst.ws.internal.explorer.platform.util.MultipartFormDataException;
import org.eclipse.wst.ws.internal.explorer.platform.util.MultipartFormDataParser;

public abstract class LaunchWebServiceWizardAction extends FormAction
{
  public LaunchWebServiceWizardAction(Controller controller)
  {
    super(controller);
  }

  protected boolean processParsedResults(MultipartFormDataParser parser) throws MultipartFormDataException
  {
    String wizardID = parser.getParameter(ActionInputs.WEB_SERVICE_WIZARD);
    propertyTable_.put(ActionInputs.WEB_SERVICE_WIZARD, wizardID);
    return true;
  }

  public boolean launchWizard(String wsdlURL)
  {
    LaunchWizardTask task = LaunchWizardTask.getInstance();
    if (task.getIsExecuting())
    {
      controller_.getCurrentPerspective().getMessageQueue().addMessage(controller_.getMessage("MSG_ERROR_WIZARD_ALREADY_RUNNING"));
      return false;
    }
    int wizardID = Integer.parseInt((String)propertyTable_.get(ActionInputs.WEB_SERVICE_WIZARD));
    LaunchWizardRunnable runnable;
    if (wizardID == ActionInputs.WEB_SERVICE_CLIENT_WIZARD)
      runnable = new LaunchWizardRunnable("org.eclipse.jst.ws.internal.consumption.ui.wizard.client.clientwizard", wsdlURL);
    else if (wizardID == ActionInputs.WEB_SERVICE_SKELETON_WIZARD)
      runnable = new LaunchWizardRunnable("org.eclipse.jst.ws.creation.ui.wizard.serverwizard", wsdlURL);
    else
      return false;
    task.checkAndAsyncExec(runnable);
    return true;
  }

  public abstract String getStatusContentVar();

  public abstract String getStatusContentPage();
}