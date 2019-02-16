/*******************************************************************************
 * Copyright (c) 2009 by SAP AG, Walldorf. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     SAP AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.jaxws.utils.internal.annotations.impl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ArrayInitializer;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jst.ws.jaxws.utils.annotations.IValue;

/**
 * Implementation for {@link IArrayValue}
 * 
 * @author Plamen Pavlov
 */
public class ArrayValueImpl extends ValueImpl
{
	private Set<IValue> values = new HashSet<IValue>();

	/**
	 * Construction
	 * 
	 * @param values
	 * @throws NullPointerException
	 *             in case <code>values</code> is <code>null</code>.
	 */
	public ArrayValueImpl(Set<IValue> values)
	{
		if (values == null)
		{
			throw new NullPointerException("values should not be null"); //$NON-NLS-1$
		}

		this.values = values;
	}

	@Override
	public String toString()
	{
		String result = "["; //$NON-NLS-1$
		boolean first = true;
		for (IValue value : values)
		{
			if(first)
			{
				result = result + value.toString();
				first = false;
			}
			else
			{
				result = result + ", " + value.toString(); //$NON-NLS-1$
			}
		}
		result = result + "]"; //$NON-NLS-1$
		return result;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected Expression getExpression(CompilationUnit unit, AST ast)
	{
		//TODO check this method
		ArrayInitializer arr = ast.newArrayInitializer();
		for (IValue value : values)
		{
			arr.expressions().add(((ValueImpl) value).getExpression(unit, ast));
		}

		return arr;
	}

	@Override
	public int hashCode()
	{
		return Arrays.hashCode(values.toArray(new IValue[values.size()]));
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
		final ArrayValueImpl other = (ArrayValueImpl) obj;
		return Arrays.equals(values.toArray(new IValue[values.size()]), other.values.toArray(new IValue[values.size()]));
	}
}
