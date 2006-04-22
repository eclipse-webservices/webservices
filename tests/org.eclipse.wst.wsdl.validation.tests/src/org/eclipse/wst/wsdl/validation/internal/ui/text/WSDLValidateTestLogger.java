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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.wst.wsdl.validation.internal.logging.ILogger;

/**
 * A helper class for testing that allows access to the messages logged.
 */
public class WSDLValidateTestLogger implements ILogger {

	protected List errors = new ArrayList();
	protected List warnings = new ArrayList();
	protected List infos = new ArrayList();
	protected List verboses = new ArrayList();
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.wsdl.validation.internal.logging.ILogger#log(java.lang.String, int)
	 */
	public void log(String message, int severity) 
	{
		if(severity == ILogger.SEV_ERROR)
		{
			errors.add(message);
		}
		else if(severity == ILogger.SEV_WARNING)
		{
			warnings.add(message);
		}
		else if(severity == ILogger.SEV_INFO)
		{
			infos.add(message);
		}
		else if(severity == ILogger.SEV_VERBOSE)
		{
			verboses.add(message);
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.wsdl.validation.internal.logging.ILogger#log(java.lang.String, int, java.lang.Throwable)
	 */
	public void log(String message, int severity, Throwable throwable) 
	{
		log(message, severity);
		log(throwable.toString(), severity);
	}
	
	/**
	 * Get the error list.
	 * 
	 * @return
	 * 		The error list.
	 */
	public List getErrors()
	{
		return errors;
	}
	
	/**
	 * Get the warning list.
	 * 
	 * @return
	 * 		The warning list.
	 */
	public List getWarnings()
	{
		return warnings;
	}
	
	/**
	 * Get the info list.
	 * 
	 * @return
	 * 		The info list.
	 */
	public List getInfos()
	{
		return infos;
	}

	/**
	 * Get the verbose list.
	 * 
	 * @return
	 * 		The verbose list.
	 */
	public List getVerboses()
	{
		return verboses;
	}
}
