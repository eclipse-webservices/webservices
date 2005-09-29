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
import org.eclipse.jst.ws.internal.consumption.ui.widgets.object.ValidateWSDLJob;
import org.eclipse.wst.command.internal.provisional.env.core.EnvironmentalOperation;
import org.eclipse.wst.command.internal.provisional.env.core.common.Choice;
import org.eclipse.wst.command.internal.provisional.env.core.common.Environment;
import org.eclipse.wst.command.internal.provisional.env.core.common.MessageUtils;
import org.eclipse.wst.command.internal.provisional.env.core.common.StatusUtils;
import org.eclipse.wst.ws.internal.plugin.WSPlugin;


public class CheckWSDLValidationCommand extends EnvironmentalOperation
{	  
	private static MessageUtils msgUtils_;
	
	public CheckWSDLValidationCommand () {
		String       pluginId = "org.eclipse.jst.ws.consumption.ui";
	  msgUtils_ = new MessageUtils( pluginId + ".plugin", this );
	}
  
  public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable )
  {
    Environment env = getEnvironment();
    
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
					  return StatusUtils.errorStatus( msgUtils_.getMessage("WAIT_FOR_WSDL") );
				  }
			  }
		  }
	  }
	  
      return Status.OK_STATUS;
	  
  }
  
  private boolean ignoreWSDLValidation(Environment env) {
	  if (!WSPlugin.getInstance().getWaitForWSDLValidationContext().getPersistentWaitForWSDLValidation()) 
		  return true; // do not want to wait for WSDL validation, i.e. Ignore all
		  
		// give a warning message with the options to stop, ignore this one, or
		// ignore all coming messages
		IStatus status_ = StatusUtils.warningStatus( msgUtils_.getMessage("STILL_VALIDATING_WSDL") );
		// adding all messages from WSI Incompliances

		Choice ignoreChoice = new Choice('I', msgUtils_
				.getMessage("IGNORE_LABEL"), msgUtils_
				.getMessage("IGNORE_DESCRIPTION"));
		Choice ignoreAllChoice = new Choice('A', msgUtils_
				.getMessage("IGNORE_ALL_LABEL"), msgUtils_
				.getMessage("IGNORE_ALL_DESCRIPTION"));
		Choice cancelChoice = new Choice('C', msgUtils_
				.getMessage("CANCEL_LABEL"), msgUtils_
				.getMessage("CANCEL_DESCRIPTION"));

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