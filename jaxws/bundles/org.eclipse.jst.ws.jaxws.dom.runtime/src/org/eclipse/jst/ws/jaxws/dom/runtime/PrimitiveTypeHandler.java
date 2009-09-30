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

import java.util.HashMap;
import java.util.Map;

/**
 * Utility class used for some general primitive type checks/operations.
 * 
 * @author Georgi Vachkov
 * 
 */

public class PrimitiveTypeHandler
{
	private static final String ARRAY_MARK = "[]"; //$NON-NLS-1$

	private static final String VOID = "void"; //$NON-NLS-1$

	private static Map<String, String> primitiveTypes = new HashMap<String, String>();

	static
	{
		primitiveTypes.put("boolean", "java.lang.Boolean"); //$NON-NLS-1$ //$NON-NLS-2$
		primitiveTypes.put("java.lang.Boolean", "java.lang.Boolean"); //$NON-NLS-1$ //$NON-NLS-2$
		primitiveTypes.put("byte", "java.lang.Byte"); //$NON-NLS-1$ //$NON-NLS-2$
		primitiveTypes.put("java.lang.Byte", "java.lang.Byte"); //$NON-NLS-1$ //$NON-NLS-2$
		primitiveTypes.put("char", "java.lang.Character"); //$NON-NLS-1$ //$NON-NLS-2$
		primitiveTypes.put("java.lang.Character", "java.lang.Character"); //$NON-NLS-1$ //$NON-NLS-2$
		primitiveTypes.put("double", "java.lang.Double"); //$NON-NLS-1$ //$NON-NLS-2$
		primitiveTypes.put("java.lang.Double", "java.lang.Double"); //$NON-NLS-1$ //$NON-NLS-2$
		primitiveTypes.put("float", "java.lang.Float"); //$NON-NLS-1$ //$NON-NLS-2$
		primitiveTypes.put("java.lang.Float", "java.lang.Float"); //$NON-NLS-1$ //$NON-NLS-2$
		primitiveTypes.put("int", "java.lang.Integer"); //$NON-NLS-1$ //$NON-NLS-2$
		primitiveTypes.put("java.lang.Integer", "java.lang.Integer"); //$NON-NLS-1$ //$NON-NLS-2$
		primitiveTypes.put("java.lang.String", "java.lang.String"); //$NON-NLS-1$ //$NON-NLS-2$
		primitiveTypes.put("long", "java.lang.Long"); //$NON-NLS-1$ //$NON-NLS-2$
		primitiveTypes.put("java.lang.Long", "java.lang.Long"); //$NON-NLS-1$ //$NON-NLS-2$
		primitiveTypes.put("short", "java.lang.Short"); //$NON-NLS-1$ //$NON-NLS-2$
		primitiveTypes.put("java.lang.Short", "java.lang.Short"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * Returns the object type for primitive type. Mapping between types:
	 * 
	 * <pre>
	 *  &quot;boolean,              &quot;java.lang.Boolean&quot;
	 *  &quot;java.lang.Boolean&quot;,   &quot;java.lang.Boolean&quot;
	 *  &quot;byte&quot;,                &quot;java.lang.Byte&quot;
	 *  &quot;java.lang.Byte&quot;,      &quot;java.lang.Byte&quot;
	 *  &quot;char&quot;,                &quot;java.lang.Character&quot;
	 *  &quot;java.lang.Character&quot;, &quot;java.lang.Character&quot;
	 *  &quot;double&quot;,              &quot;java.lang.Double&quot;
	 *  &quot;java.lang.Double&quot;,    &quot;java.lang.Double&quot;
	 *  &quot;float&quot;,               &quot;java.lang.Float&quot;
	 *  &quot;java.lang.Float&quot;,     &quot;java.lang.Float&quot;
	 *  &quot;int&quot;,                 &quot;java.lang.Integer&quot;
	 *  &quot;java.lang.Integer&quot;,   &quot;java.lang.Integer&quot;
	 *  &quot;java.lang.String&quot;,    &quot;java.lang.String&quot;
	 *  &quot;long&quot;,                &quot;java.lang.Long&quot;
	 *  &quot;java.lang.Long&quot;,      &quot;java.lang.Long&quot;
	 *  &quot;short&quot;,               &quot;java.lang.Short&quot;
	 *  &quot;java.lang.Short&quot;,     &quot;java.lang.Short&quot;
	 * </pre>
	 * 
	 * @param typeName
	 * @return wrapper class name for <tt>typeName</tt>.
	 */
	public static String getObjectTypeForPrimitiveType(String typeName)
	{
		return primitiveTypes.get(typeName);
	}

	/**
	 * @param typeName
	 * @return true if <tt>typeName</tt> is java primitive type.
	 */
	public static boolean isJavaPrimitiveType(String typeName)
	{
		return (primitiveTypes.get(typeName) != null);
	}

	/**
	 * Checks if the <tt>typeName</tt> represents an array type.
	 * 
	 * @param typeName
	 * @return true if <tt>typeName</tt> is array type
	 */
	public static boolean isArrayType(String typeName)
	{
		return typeName.trim().endsWith(ARRAY_MARK);
	}

	/**
	 * Checks if <tt>typeName</tt> is void.
	 * 
	 * @param typeName
	 * @return true if <tt>typeName</tt> is void.
	 */
	public static boolean isVoidType(String typeName)
	{
		return typeName.trim().equals(VOID);
	}

	/**
	 * Retrieves the real type name from array type. Example:
	 * 
	 * <pre>
	 *   For type 'int[]' returns 'int'
	 *   For type 'String[][]' returns 'String'
	 * </pre>
	 * 
	 * @param typeName
	 * @return real type name
	 */
	public static String getTypeFromArrayType(String typeName)
	{
		String name = typeName.trim().replaceAll(" ", ""); //$NON-NLS-1$ //$NON-NLS-2$
		while (name.endsWith(ARRAY_MARK))
		{
			name = name.substring(0, name.length() - ARRAY_MARK.length());
		}

		return name;
	}
}
