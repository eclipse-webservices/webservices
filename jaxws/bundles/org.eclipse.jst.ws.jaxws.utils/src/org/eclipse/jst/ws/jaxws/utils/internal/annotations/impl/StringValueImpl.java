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

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.StringLiteral;

/**
 * Implementor for IStringValue
 * 
 * @author Plamen Pavlov
 */
public class StringValueImpl extends ValueImpl
{
	private String value;

	/**
	 * Constructor
	 * 
	 * @param value
	 */
	public StringValueImpl(String value)
	{
		this.value = value;
	}

	@Override
	protected Expression getExpression(CompilationUnit unit, AST ast)
	{
		StringLiteral literal = ast.newStringLiteral();
		literal.setLiteralValue(value);
		return literal;
	}

	@Override
	public int hashCode()
	{
		return value.hashCode();
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
		final StringValueImpl other = (StringValueImpl) obj;
		return value.equals(other.value);
	}
	
	@Override
	public String toString()
	{
		return value;
	}
}
