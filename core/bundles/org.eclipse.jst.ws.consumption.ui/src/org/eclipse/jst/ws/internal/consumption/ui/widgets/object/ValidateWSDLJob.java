/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.jst.ws.internal.consumption.ui.widgets.object;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.wst.wsdl.validation.internal.IValidationMessage;
import org.eclipse.wst.wsdl.validation.internal.IValidationReport;
import org.eclipse.wst.wsdl.validation.internal.eclipse.WSDLValidator;



public class ValidateWSDLJob extends Job {
	
	final public static String VALIDATE_WSDL_JOB_FAMILY = "ValidateWSDLJobJamily";
	
	private String wsdlURI_ = null;
	private IValidationMessage[] validationMessages_ = null;
	private int validationMessageSeverity_ = -1; // default value

	public ValidateWSDLJob(String wsdlURI) {
		super("ValidateWSDLJob");
		wsdlURI_ = wsdlURI;
	}

	protected IStatus run(IProgressMonitor monitor) {
		WSDLValidator wsdlValidator = WSDLValidator.getInstance();
	    IValidationReport valReport = wsdlValidator.validate(wsdlURI_);
	    validationMessages_ = valReport.getValidationMessages();
	    reportSummary();
		return Status.OK_STATUS;
	}
	
	// calculate the higher severity for all the validation messages
	private void reportSummary() {
		int i;
		int severity;
		int errorCount = 0;
		int warningCount = 0;
		for (i=0; i<validationMessages_.length && errorCount == 0; i++) {
			severity = validationMessages_[i].getSeverity();
			switch (severity) {
			case IValidationMessage.SEV_ERROR:
				errorCount++;
				break;
			case IValidationMessage.SEV_WARNING:
				warningCount++;
				break;
			default:
				break;
			}
		}
		if (errorCount > 0) {
			validationMessageSeverity_ = IValidationMessage.SEV_ERROR;
		} else if (warningCount > 0) {
			validationMessageSeverity_ = IValidationMessage.SEV_WARNING;
		}
		
	}

	public boolean belongsTo(Object family)
	{
		return family == VALIDATE_WSDL_JOB_FAMILY;
	}
	
	public IValidationMessage[] getValidationMessages() {
		return validationMessages_;
	}
	
	public int getValidationMessageSeverity() {
		return validationMessageSeverity_;
	}

	public String getWsdlURI() {
		return wsdlURI_;
	}
}
