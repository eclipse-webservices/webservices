/*******************************************************************************
 * Copyright (c) 2009 by SAP AG, Walldorf. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     SAP AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.jaxws.utils;

import java.text.MessageFormat;

import org.apache.axis.types.NCName;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.ws.jaxws.utils.internal.text.JaxWsUtilMessages;


/**
 * Class that contains utility methods to validate names used in WSDL.  
 * 
 * @author Georgi Vachkov
 */
public class WsdlNamesValidator 
{
	/**
	 * Provides validation of NCName 
	 * @param title the title for the error message
	 * @param name the checked name
	 * @return error status in case the name is invalid otherwise {@link Status#OK_STATUS}.
	 */
	public static IStatus validateNCName(final String title, final String name)
	{
		if (name.trim().length() == 0) {
			return StatusUtils.statusError(MessageFormat.format(JaxWsUtilMessages.WsdlNamesValidator_EmptyXmlName, title));
		}
		
		int errPos = checkNCName(name);
		if (errPos > -1) {
			return StatusUtils.statusError(MessageFormat.format(JaxWsUtilMessages.WsdlNamesValidator_InvalidNCName, name.charAt(errPos), title, name));
		}
		
		return Status.OK_STATUS;
	}	
	
	/**
	 * Validates <code>name</code> for NCName
	 * @param name
	 * @return -1 if the name is valid or > -1 which is the position of the wrong character.
	 */
	public static IStatus validateNCName2(final String title, final String name) 
	{
		if (name==null || name.trim().length() == 0) {
			return StatusUtils.statusError(MessageFormat.format(JaxWsUtilMessages.WsdlNamesValidator_EmptyXmlName2, title));
		}
		
		int errPos = checkNCName(name);
		if (errPos > -1) {
			return StatusUtils.statusError(MessageFormat.format(JaxWsUtilMessages.WsdlNamesValidator_InvalidNCName2, name.charAt(errPos), title));
		}
		
		return Status.OK_STATUS;
	}
	
	/**
	 * Checks whether the name specified is a valid NCName
	 * @param name the name to check
	 * @return index of the first symbol which violates the NCName rules or -1 in case the name is a valid NCName
	 * @see NCName#isValid(String)
	 */
	private static int checkNCName(final String name)
	{
		final StringBuilder builder = new StringBuilder();
		for(int i = 0; i < name.length(); i++)
		{
			builder.append(name.charAt(i));
			if(!NCName.isValid(builder.toString()))
			{
				return i;
			}
		}
		
		return -1;
	}
}
