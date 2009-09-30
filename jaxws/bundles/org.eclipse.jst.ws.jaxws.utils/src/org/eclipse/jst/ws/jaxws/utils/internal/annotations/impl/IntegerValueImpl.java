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

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.NumberLiteral;

/**
 * Implementor for IIntegerValue
 * 
 * @author Plamen Pavlov
 */
public class IntegerValueImpl extends ValueImpl
{
	private String value;

	/**
	 * Constructor
	 * 
	 * @param value
	 */
	public IntegerValueImpl(String value)
	{
		this.value = value;
	}

	@Override
	protected Expression getExpression(CompilationUnit unit, AST ast)
	{
		NumberLiteral literal = ast.newNumberLiteral();
        literal.setToken(new Integer(value).toString());
		return literal;
	}

	@Override
	public int hashCode()
	{
		return new Integer(value);
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
		final IntegerValueImpl other = (IntegerValueImpl) obj;
		return value == other.value;
	}
	
	@Override
	public String toString()
	{
		return value;
	}
}
