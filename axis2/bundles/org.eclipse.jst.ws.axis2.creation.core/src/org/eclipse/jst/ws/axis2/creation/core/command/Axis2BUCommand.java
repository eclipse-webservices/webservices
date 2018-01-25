/*******************************************************************************
 * Copyright (c) 2007 WSO2 Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * WSO2 Inc. - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20070110   168762 sandakith@wso2.com - Lahiru Sandakith, Initial code to introduse the Axis2 runtime to the framework for 168762
 *******************************************************************************/
package org.eclipse.jst.ws.axis2.creation.core.command;


import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.ws.axis2.creation.core.data.DataModel;
import org.eclipse.jst.ws.axis2.creation.core.messages.Axis2CreationUIMessages;
import org.eclipse.wst.command.internal.env.core.common.StatusUtils;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;


public class Axis2BUCommand extends AbstractDataModelOperation 
{
  private DataModel model;
  
  public Axis2BUCommand( DataModel model )
  {
    this.model = model;  
  }
  
  public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable ) 
  {
    IStatus status ;  
    // Check for the nessesary data are available for the scenario to begin
    if (model.getServiceClass() == null){
		status = StatusUtils.errorStatus(Axis2CreationUIMessages.ERROR_INVALID_SERVICE_CREATION);
    }else {
		status = Status.OK_STATUS; // Ok to proceed 
	}
    return status;      	
  }
}
