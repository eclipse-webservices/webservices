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
 * Implementor for {@link IQualifiedNameValue}
 * 
 * @author Plamen Pavlov
 */
public class QualifiedNameValueImpl extends ValueImpl
{
	private String value;

	/**
	 * Constructor
	 * 
	 * @param value
	 */
	public QualifiedNameValueImpl(String value)
	{
		this.value = value;
	}

	@Override
	protected Expression getExpression(CompilationUnit unit, AST ast)
	{
		// "javax.ejb.TransactionAttributeType.REQUIRED"
		int i = value.lastIndexOf('.');
		if (i != -1)
		{
			String clsName = value.substring(0, i);
			if (addImports(ast, unit, clsName))
			{
				return ast.newName(getSimpleName(clsName) + value.substring(i));
			}
		}
		return ast.newName(value);
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
		final QualifiedNameValueImpl other = (QualifiedNameValueImpl) obj;
		return value.equals(other.value);
	}
	
	@Override
	public String toString()
	{
		return value;
	}
}
