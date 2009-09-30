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

/**
 * Implementation for {@link IBooleanValue}
 * 
 * @author Plamen Pavlov
 */
public class BooleanValueImpl extends ValueImpl
{ //$JL-EQUALS$
	private boolean value;

	/**
	 * Constructor
	 * 
	 * @param value
	 */
	public BooleanValueImpl(boolean value)
	{
		this.value = value;
	}

	@Override
	protected Expression getExpression(CompilationUnit unit, AST ast)
	{
		return ast.newBooleanLiteral(value);
	}
	
	@Override
	public String toString()
	{
		return Boolean.toString(value);
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
		final BooleanValueImpl other = (BooleanValueImpl) obj;
		return value == other.value;
	}

}
