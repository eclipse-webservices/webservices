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

import java.util.Iterator;
import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jst.ws.jaxws.utils.annotations.ILocator;
import org.eclipse.jst.ws.jaxws.utils.annotations.IValue;

/**
 * @author Plamen Pavlov
 */

public abstract class ValueImpl implements IValue
{
	private ILocator locator = null;

	protected abstract Expression getExpression(CompilationUnit unit, AST ast);

	
	@SuppressWarnings("unchecked")
	protected boolean addImports(AST ast, CompilationUnit unit, String annotationName)
	{
		String packageAnnotationName = getPackageName(annotationName);
		if (packageAnnotationName == null)
		{
			return false;
		}

		if (packageAnnotationName.equals("java.lang")) //$NON-NLS-1$
		{
			return true;
		}
		if (unit != null)
		{
			ImportDeclaration imp = ast.newImportDeclaration();
			imp.setName(ast.newName(annotationName));
			List<ImportDeclaration> imports = unit.imports();
			String imp1 = getImport(imports, getSimpleName(annotationName));
			if (imp1 == null)
			{
				if (getOnDemandImport(imports, packageAnnotationName) == null)
				{
					imports.add(imp);
				}
			} else
			{
				if (!imp1.equals(annotationName))
				{
					return false;
				}
			}
			return true;
		}

		return false;
	}

	protected static String getImport(List<ImportDeclaration> imports, String imp)
	{
		Iterator<ImportDeclaration> iter = imports.iterator();
		while (iter.hasNext())
		{
			ImportDeclaration next = iter.next();
			Name fqName = next.getName();
			SimpleName name;
			if (fqName.isQualifiedName())
			{
				name = ((QualifiedName) fqName).getName();
			} else
			{
				name = (SimpleName) fqName;
			}
			String simpleName = name.getIdentifier();
			if (imp.equals(simpleName))
			{
				return fqName.getFullyQualifiedName();
			}
		}
		return null;
	}

	protected static String getOnDemandImport(List<ImportDeclaration> imports, String imp)
	{
		Iterator<ImportDeclaration> iter = imports.iterator();
		while (iter.hasNext())
		{
			ImportDeclaration next = iter.next();
			if (!next.isOnDemand())
			{
				continue;
			}
			Name fqName = next.getName();
			String name = fqName.getFullyQualifiedName();
			if (imp.equals(name))
			{
				return name;
			}
		}
		return null;
	}

	protected static String getPackageName(String name)
	{
		int i = name.lastIndexOf('.');
		if (i == -1)
			return null;
		return name.substring(0, i);
	}

	protected static String getSimpleName(String name)
	{
		int i = name.lastIndexOf('.');
		if (i == -1)
			return name;
		return name.substring(i + 1);
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
