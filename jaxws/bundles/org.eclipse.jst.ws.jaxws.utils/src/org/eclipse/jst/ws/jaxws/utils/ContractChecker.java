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

/**
 * Contains checks frequently applied on parameters.
 * 
 * @author Joerg Dehmel
 */
public final class ContractChecker
{
	private ContractChecker()
	{
		// prevent instantiation from outside
	}
	
	/** Checks if the given parameter value is null and reports an <code>NullPointerException</code>
	 * in this case.
	 * @param paramValue the value to be checked
	 * @param paramName name of the parameter, used to report the problem to the user
	 * @throws NullPointerException thrown either the paramValue or paramName is null */
	public static void nullCheckParam(final Object paramValue, final String paramName)
	{
		if (paramName == null)
		{
			throw new NullPointerException("paramName must not be null"); //$NON-NLS-1$ 
		}
		if (paramValue == null)
		{
			throw new NullPointerException(paramName + " must not be null"); //$NON-NLS-1$ 
		}
	}
	
	/** Checks whether the given parameter value is null and reports an <code>NullPointerException</code>
	 * in this case.
	 * @param paramValue the value to be checked
	 * @throws NullPointerException thrown if the paramValue is null */
	public static void nullCheckParam(final Object paramValue)
	{
		if (paramValue == null)
		{
			throw new NullPointerException("paramValue must not be null"); //$NON-NLS-1$
		}
	}
	
	/** Checks if the given field value is null and reports an <code>IllegalStateException</code>
	 * in this case.
	 * @param fieldValue the value to be checked
	 * @param fieldName name of the field, used to report the problem to the user
	 * @throws NullPointerException thrown if the fieldName is null
	 * @throws IllegalStateException thrown if the fieldValue is null */
	public static void nullCheckField(final Object fieldValue, final String fieldName)
	{
		if (fieldName == null)
		{
			throw new NullPointerException("fieldName must not be null");  //$NON-NLS-1$  
		}
		if (fieldValue == null)
		{
			throw new IllegalStateException(fieldName + " must not be null"); //$NON-NLS-1$ 
		}
	}
	
	/** Checks whether the given field value is null and reports an <code>IllegalStateException</code>
	 * in this case.
	 * @param fieldValue the value to be checked
	 * @throws IllegalStateException thrown if the fieldValue is null */
	public static void nullCheckField(final Object fieldValue)
	{
		if (fieldValue == null)
		{
			throw new IllegalStateException("fieldValue must not be null"); //$NON-NLS-1$
		}
	}
	
	/** Checks whether the given value of a local variable is null and reports an <code>IllegalStateException</code>
	 * in this case.
	 * @param varValue the value to be checked
	 * @param varName name of the local variable, used to report the problem to the user
	 * @throws NullPointerException thrown if the varName is null
	 * @throws IllegalStateException thrown if the varValue is null */
	public static void nullCheckVariable(final Object varValue, final String varName)
	{
		if (varName == null)
		{
			throw new NullPointerException("varName must not be null"); //$NON-NLS-1$ 
		}
		if (varValue == null)
		{
			throw new IllegalStateException(varName + " must not be null"); //$NON-NLS-1$
		}
	}
	
	/** Checks whether the given value of a local variable is null and reports an <code>IllegalStateException</code>
	 * in this case.
	 * @param varValue the value to be checked
	 * @throws IllegalStateException thrown if the varValue is null */
	public static void nullCheckVariable(final Object varValue)
	{
		if (varValue == null)
		{
			throw new IllegalStateException("varValue must not be null"); //$NON-NLS-1$
		}
	}
	
	/**
	 * This method checks if <code>param</code> is <code>null</code> or empty string.
	 * @param param
	 * @param varName
	 * @throws NullPointerException in case <code>param</code> is <code>null</code>
	 * @throws IllegalArgumentException in case <code>param</code> is empty string or contains only
	 * white space characters
	 */
	public static void emptyStringCheckParam(final String param, final String varName) 
	{
		nullCheckParam(param, varName);
		
		if (param.trim().length() == 0) {
			throw new IllegalArgumentException(MessageFormat.format("Parameter {0} is empty string or contains only white spaces", varName)); //$NON-NLS-1$
		}
	}
}
