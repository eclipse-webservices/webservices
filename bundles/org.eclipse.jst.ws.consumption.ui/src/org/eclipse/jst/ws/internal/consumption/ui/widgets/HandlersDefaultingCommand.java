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
package org.eclipse.jst.ws.internal.consumption.ui.widgets;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jst.j2ee.internal.webservice.WebServiceNavigatorGroupType;
import org.eclipse.wst.command.env.core.common.Environment;
import org.eclipse.wst.command.env.core.common.MessageUtils;
import org.eclipse.wst.command.env.core.common.SimpleStatus;
import org.eclipse.wst.command.env.core.common.Status;
import org.eclipse.wst.wsdl.Service;


/**
 * HandlersDefaultingCommand
 * 
 * Determine if Handlers belongs to service or client side
 */
public class HandlersDefaultingCommand extends AbstractHandlersWidgetDefaultingCmd {

  private MessageUtils msgUtils_;

  private boolean isClientHandler_ = false;
  private boolean isServiceHandler_ = false;

  public Status execute(Environment env) {
    String pluginId = "org.eclipse.jst.ws.consumption.ui";
    MessageUtils msgUtils_ = new MessageUtils(pluginId + ".plugin", this);
    Status status = new SimpleStatus("");

    IStructuredSelection selection = getInitialSelection();
    if (selection != null && selection.size() == 1) {
        Object obj = selection.getFirstElement();

        if (obj instanceof WebServiceNavigatorGroupType) {
          WebServiceNavigatorGroupType wsngt = (WebServiceNavigatorGroupType) obj;
          Service service = wsngt.getWsdlService();
          if (wsngt.getWsdlService()!=null){
          	isServiceHandler_ = true;
          }
          
          if (wsngt.getServiceRef()!=null){
          	isClientHandler_ = true;
          }

        }
      }
    else {
      status = new SimpleStatus("", msgUtils_.getMessage("MSG_ERROR_TASK_EXCEPTED"), Status.ERROR, null);
      env.getStatusHandler().reportError(status);
      return status;
    }

    return status;

  }

  
/**
 * @return Returns the isClientHandler_.
 */
public boolean getIsClientHandler() {
	return isClientHandler_;
}
/**
 * @return Returns the isServiceHandler_.
 */
public boolean getIsServiceHandler() {
	return isServiceHandler_;
}
}