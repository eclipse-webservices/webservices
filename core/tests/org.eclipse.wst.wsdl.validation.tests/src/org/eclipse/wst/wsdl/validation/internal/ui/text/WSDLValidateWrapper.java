/*******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.validation.internal.ui.text;

import java.util.List;

import org.eclipse.wst.wsdl.validation.internal.IValidationMessage;
import org.eclipse.wst.wsdl.validation.internal.IValidationReport;
import org.eclipse.wst.wsdl.validation.internal.WSDLValidationConfiguration;
import org.eclipse.wst.wsdl.validation.internal.WSDLValidator;

/**
 * A wrapper class of WSDLValidate that exposes internals for testing.
 */
public class WSDLValidateWrapper extends WSDLValidate 
{

	/* (non-Javadoc)
	 * @see org.eclipse.wst.wsdl.validation.internal.ui.text.WSDLValidate#getMessages(org.eclipse.wst.wsdl.validation.internal.IValidationMessage[])
	 */
	public String getMessages(IValidationMessage[] messages) 
	{
		return super.getMessages(messages);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.wsdl.validation.internal.ui.text.WSDLValidate#parseArguments(java.lang.String[])
	 */
	public void parseArguments(String[] args) 
	{
		super.parseArguments(args);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.wsdl.validation.internal.ui.text.WSDLValidate#validate()
	 */
	public void validate() 
	{
		super.validate();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.wsdl.validation.internal.ui.text.WSDLValidate#validateFile(java.lang.String)
	 */
	public IValidationReport validateFile(String filename) 
	{
		return super.validateFile(filename);
	}
	
	/**
	 * Get the internal WSDL validator.
	 * 
	 * @return
	 * 		The internal WSDL validator.
	 */
	public WSDLValidator getWSDLValidator()
	{
		return wsdlValidator;
	}
	
	/**
	 * Get the WSDL validation configuration.
	 * 
	 * @return
	 * 		The WSDL validation configuration.
	 */
	public WSDLValidationConfiguration getConfiguration()
	{
		return configuration;
	}
	
	/**
	 * Get the list of WSDL files to validate.
	 * 
	 * @return
	 * 		The list of WSDL files to validate.
	 */
	public List getWSDLFiles()
	{
		return wsdlFiles;
	}
	
	/**
	 * Returns true if verbose is on, false otherwise.
	 * 
	 * @return
	 * 		True if verbose is on, false otherwise.
	 */
	public boolean isVerbose()
	{
		return verbose;
	}
	
	/**
	 * Set whether reporting should be done in a verbose way.
	 * 
	 * @param verbose
	 * 		If true reporting will be done in a verbose way, otherwise it will not.
	 */
	public void setVerbose(boolean verbose)
	{
		this.verbose = verbose;
	}

}
