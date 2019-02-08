/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.internal.consumption.ui.widgets;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jst.j2ee.internal.webservice.WebServiceNavigatorGroupType;
import org.eclipse.jst.ws.internal.consumption.ui.ConsumptionUIMessages;
import org.eclipse.wst.command.internal.env.core.common.StatusUtils;
import org.eclipse.wst.common.environment.IEnvironment;


/**
 * HandlersDefaultingCommand
 * 
 * Determine if Handlers belongs to service or client side
 */
public class HandlersDefaultingCommand extends AbstractHandlersWidgetDefaultingCmd 
{
  private boolean isClientHandler_ = false;
  private boolean isServiceHandler_ = false;

  public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable )
  {    
    IEnvironment env = getEnvironment();
    IStatus status = Status.OK_STATUS;

    IStructuredSelection selection = getInitialSelection();
    if (selection != null && selection.size() == 1) {
        Object obj = selection.getFirstElement();

        if (obj instanceof WebServiceNavigatorGroupType) {
          WebServiceNavigatorGroupType wsngt = (WebServiceNavigatorGroupType) obj;
          if (wsngt.getWsdlService()!=null){
          	isServiceHandler_ = true;
          }
          
          if (wsngt.getServiceRef()!=null){
          	isClientHandler_ = true;
          }

        }
      }
    else {
      status = StatusUtils.errorStatus( ConsumptionUIMessages.MSG_ERROR_TASK_EXCEPTED );
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
