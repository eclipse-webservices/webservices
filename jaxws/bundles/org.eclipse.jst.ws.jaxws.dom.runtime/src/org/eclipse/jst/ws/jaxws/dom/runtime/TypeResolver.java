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
package org.eclipse.jst.ws.jaxws.dom.runtime;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.Signature;

/**
 * Utility class containing logic for resolving type names from IType object.
 * 
 * @author Georgi Vachkov
 */
public class TypeResolver
{
	private static final String DOT = "."; //$NON-NLS-1$

	private static final String QUOTE = "?"; //$NON-NLS-1$

	/**
	 * Resolves <tt>typeName</tt> using <tt>type</tt>. In case when <tt>typeName</tt> contains generics declaration all types used in
	 * declaraion are returned.
	 * 
	 * @param typeName
	 * @param type
	 * @return list of resolved types
	 * @throws JavaModelException
	 */
	public static List<String> resolveTypes(String typeName, IType type) throws JavaModelException
	{
		List<String> names = new ArrayList<String>(1);
		String realTypeName = Signature.toString(typeName);

		if (hasGenerics(realTypeName))
		{
			names = getTypesFromGenerics(realTypeName, type);
		} else
		{
			names.add(realTypeName);
		}

		List<String> resolvedNames = new ArrayList<String>(names.size());
		for (String name : names)
		{
			String resolvedName = resolveType(name, type);

			if (!resolvedName.equals(QUOTE))
			{
				resolvedNames.add(resolvedName);
			}
		}

		return resolvedNames;
	}

	/**
	 * Retrieves real type in case type is present by generics.
	 * 
	 * @param typeName
	 * @param declaringType
	 * @return the real type of the expression if <tt>typeName</tt> is an expression otherwise <tt>typeName</tt>
	 * @throws JavaModelException
	 */
	private static String getType(String typeName, IType declaringType) throws JavaModelException
	{
		if (typeName.indexOf('?') > -1)
		{
			int pos = typeName.trim().lastIndexOf(' ');
			if (pos > -1)
			{
				return TypeResolver.resolveType(typeName.substring(pos + 1), declaringType);
			}
		}

		return typeName;
	}

	/**
	 * Gets the types used in generics declaration.
	 * @param typeName
	 * @param type the type containing resolved type
	 * @return list of type names.
	 * @throws JavaModelException 
	 */
	public static final List<String> getTypesFromGenerics(String typeName, IType type) throws JavaModelException
	{
		String name = typeName;			
		List<String> names= new ArrayList<String>();
		
		int ltPos = -1;		
		int gtPos = -1;
		while ((ltPos = name.indexOf('<')) > -1)
		{			
			if ((gtPos = name.lastIndexOf('>')) == -1)
			{
				gtPos = name.length()-2;
			}
			
			String firstName = name.substring(0, ltPos) + name.substring(gtPos+1);
			name = name.substring(ltPos+1, gtPos);
			
			addName(firstName, names, type);
			if (name.indexOf('<') == -1) 
			{
				addName(name, names, type);
			}
		}
		
		return names;
	}
	
	private static final void addName(final String name, final List<String> names, final IType type) throws JavaModelException
	{
		int comaPos = name.indexOf(',');
		if( comaPos > -1)
		{
			names.add(name.substring(0, comaPos));
			names.add(name.substring(comaPos+1));
		} 
		else {
			names.add(getType(name, type));
		}		
	}

	/**
	 * Resolves type.
	 * 
	 * @param typeName
	 * @param type
	 * @return the resolved type name.
	 * @throws JavaModelException
	 */
	public static String resolveType(final String typeName, final IType type) throws JavaModelException
	{
		String realTypeName = typeName;
		if (PrimitiveTypeHandler.isArrayType(typeName))
		{
			return resolveType(PrimitiveTypeHandler.getTypeFromArrayType(typeName), type);
		}

		if (PrimitiveTypeHandler.isJavaPrimitiveType(realTypeName) || PrimitiveTypeHandler.isVoidType(realTypeName))
		{
			return realTypeName;
		}

		if (realTypeName.indexOf(DOT) > -1)
		{
			return realTypeName;
		}

		String[][] st = type.resolveType(realTypeName);
		if (st != null)
		{
			if (st[0][0].length() == 0)
			{
				return st[0][1];
			}

			return st[0][0] + DOT + st[0][1];
		}

		if (realTypeName.startsWith(new String(new char[] { Signature.C_UNRESOLVED })))
		{
			return resolveType(realTypeName.substring(1, realTypeName.length()), type);
		}

		return realTypeName;
	}

	/**
	 * Checks whether <code>typeName</code> uses generics declaration. Example: for 'java.util.List<Strng>' the method will return true.
	 * 
	 * @param typeName
	 * @return true is <tt>typeName</tt> contains generics declaration.
	 */
	private static boolean hasGenerics(String typeName)
	{
		return (typeName.indexOf('<') > -1);
	}

}
