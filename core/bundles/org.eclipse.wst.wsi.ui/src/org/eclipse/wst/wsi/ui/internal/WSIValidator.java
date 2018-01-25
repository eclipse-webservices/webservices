/*******************************************************************************
 * Copyright (c) 2002-2005 IBM Corporation and others.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   IBM - Initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsi.ui.internal;

import org.eclipse.core.resources.IFile;
import org.eclipse.wst.wsi.ui.internal.actions.WSIValidateAction;

/**
 * A simple interface to the WS-I message log validator.
 * 
 * @author Lawrence Mandel, IBM
 */
public class WSIValidator {

	/**
	 * Constructor.
	 */
	public WSIValidator()
	{
	}
	
	/**
	 * Validate the given log file.
	 * 
	 * @param file The log file to validate.
	 */
	public void validate(IFile file)
	{
		WSIMessageValidator messageValidator = new WSIMessageValidator();
		WSIValidateAction validateAction = new WSIValidateAction(file, true);
		validateAction.setValidator(messageValidator);
		validateAction.run();
	}
	
	/**
	 * Validate the given log file with a WSDL document specified.
	 * 
	 * @param file The log file to validate.
	 * @param wsdlfile The WSDL file to use for validation.
	 * @param elementname The WSDL element to validate.
	 * @param namespace The namespace of the WSDL element to validate.
	 * @param parentname The name of the parent element of the element to validate.
	 * @param type The type of the element to validate.
	 */
	/**
	 * Validate the given log file.
	 * 
	 * @param file The log file to validate.
	 */
	public void validate(IFile file, String wsdlfile, String elementname, String namespace, String parentname, String type)
	{
		WSIMessageValidator messageValidator = new WSIMessageValidator();
		WSIValidateAction validateAction = new WSIValidateAction(file, true, wsdlfile, elementname, namespace, parentname, type);
		validateAction.setValidator(messageValidator);
		validateAction.run();
	}
}
