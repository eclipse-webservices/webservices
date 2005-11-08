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

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobManager;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jst.ws.internal.consumption.ui.ConsumptionUIMessages;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.object.ValidateWSDLJob;
import org.eclipse.wst.command.internal.env.core.common.StatusUtils;
import org.eclipse.wst.common.environment.Choice;
import org.eclipse.wst.common.environment.IEnvironment;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;
import org.eclipse.wst.ws.internal.plugin.WSPlugin;


public class CheckWSDLValidationCommand extends AbstractDataModelOperation
{	  
	
	public CheckWSDLValidationCommand () {
	}
  
  public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable )
  {
    IEnvironment env = getEnvironment();
    
    IJobManager    jobManager     = Platform.getJobManager();
	  Job[]          jobs           = jobManager.find( ValidateWSDLJob.VALIDATE_WSDL_JOB_FAMILY );
	  ValidateWSDLJob existingValidateWSDLJob = null;
	  
	  if( jobs.length > 0 )
	  {
		  for (int i=0; i<jobs.length; i++) {
			  existingValidateWSDLJob = (ValidateWSDLJob)jobs[i];
			  
			  if (existingValidateWSDLJob.getState() != Job.NONE) { 
				  if (ignoreWSDLValidation(env)) {
					  // if don't want to wait for validation, cancel existing validation job
					  existingValidateWSDLJob.cancel();
					  return Status.OK_STATUS;
				  } else {
					  // wait for WSDL validation
					  return StatusUtils.errorStatus( ConsumptionUIMessages.WAIT_FOR_WSDL );
				  }
			  }
		  }
	  }
	  
      return Status.OK_STATUS;
	  
  }
  
  private boolean ignoreWSDLValidation(IEnvironment env) {
	  if (!WSPlugin.getInstance().getWaitForWSDLValidationContext().getPersistentWaitForWSDLValidation()) 
		  return true; // do not want to wait for WSDL validation, i.e. Ignore all
		  
		// give a warning message with the options to stop, ignore this one, or
		// ignore all coming messages
		IStatus status_ = StatusUtils.warningStatus( ConsumptionUIMessages.STILL_VALIDATING_WSDL );
		// adding all messages from WSI Incompliances

		Choice ignoreChoice = new Choice('C', ConsumptionUIMessages.CANCEL_VALIDATION_LABEL, 
				ConsumptionUIMessages.CANCEL_VALIDATION_DESCRIPTION);
		Choice ignoreAllChoice = new Choice('A', ConsumptionUIMessages.CANCEL_ALL_VALIDATION_LABEL, 
				ConsumptionUIMessages.CANCEL_ALL_VALIDATION_DESCRIPTION);
		Choice cancelChoice = new Choice('W', ConsumptionUIMessages.WAIT_VALIDATION_LABEL, 
				ConsumptionUIMessages.WAIT_VALIDATION_DESCRIPTION);

		Choice result = env.getStatusHandler().report(status_,
				new Choice[] { ignoreChoice, ignoreAllChoice, cancelChoice });

		// if the user closes the message box or selects ignore continue
		if (result == null
				|| (result.getLabel().equals(ignoreChoice.getLabel()))) {
			return true;
		// if the user selects ignore all, change the preference
		} else if (result.getLabel().equals(ignoreAllChoice.getLabel())) {
			// update ignore WSDL validation preference
			WSPlugin.getInstance().getWaitForWSDLValidationContext().setWaitForWSDLValidation(false);
			return true;
		}
		// if the user selects to cancel , do not continue with the command
		else if (result.getLabel().equals(cancelChoice.getLabel())) {
			return false;
		}
		return true;
	}
  

}