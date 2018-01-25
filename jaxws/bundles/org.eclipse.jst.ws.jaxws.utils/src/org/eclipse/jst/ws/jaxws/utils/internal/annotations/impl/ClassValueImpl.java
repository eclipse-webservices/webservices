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
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.TypeLiteral;

public class ClassValueImpl extends ValueImpl 
{
    private String classQName;

    public ClassValueImpl(String classQName)
    {
        this.classQName = classQName;
    }

    protected Expression getExpression(CompilationUnit unit, AST ast)
    {
        String clsName = this.classQName;
        if (addImports(ast, unit, clsName))
        {
            clsName = getSimpleName(clsName);
        }
        Name name = ast.newName(clsName);
        SimpleType simpleType = ast.newSimpleType(name);
        TypeLiteral literal = ast.newTypeLiteral();
        literal.setType(simpleType);
        return literal;
    }

    @Override
	public String toString()
	{
		return this.classQName;
	}
    
	@Override
	public int hashCode()
	{
		return this.classQName.hashCode();
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
		final ClassValueImpl other = (ClassValueImpl) obj;
		return this.classQName.equals(other.classQName);
	}
}
