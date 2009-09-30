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
package org.eclipse.jst.ws.jaxws.utils.internal.annotations.impl;

import org.eclipse.jst.ws.jaxws.utils.annotations.ILocator;
import org.eclipse.jst.ws.jaxws.utils.annotations.IParamValuePair;
import org.eclipse.jst.ws.jaxws.utils.annotations.IValue;

/**
 * @author Plamen Pavlov
 */

public class ParamValuePairImpl implements IParamValuePair
{
	
	private ILocator locator = null;


	private String param;

	private IValue value;

	/**
	 * Constructor
	 * 
	 * @param param
	 * @param value
	 */
	public ParamValuePairImpl(String param, IValue value)
	{
		this.param = param;
		this.value = value;
	}

	public String getParam()
	{
		return param;
	}

	public IValue getValue()
	{
		return value;
	}

	@Override
	public int hashCode()
	{
		if(value != null)
		{
			return 31 * param.hashCode() + value.hashCode();
		}
		else
		{
			return 31 * param.hashCode();
		}
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if (getClass() != obj.getClass())
		{
			return false;
		}
		final ParamValuePairImpl other = (ParamValuePairImpl) obj;
		if (!param.equals(other.param))
		{
			return false;
		}
		return value.equals(other.value);
	}
	
	public ILocator getLocator()
	{
		return this.locator;
	}

	public void setLocator(final ILocator locator)
	{
		this.locator = locator;
	}
}
