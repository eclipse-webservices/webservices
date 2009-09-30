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
package org.eclipse.jst.ws.jaxws.dom.runtime.internal.validation.provider;

import org.eclipse.jst.ws.jaxws.utils.ContractChecker;

/**
 * Type key - represents type in context of project. Overrides hashCode() for usage in maps.
 * 
 * @author Georgi Vachkov
 * 
 */
public class TypeKey
{
	private final String projectName;

	private final String qualifiedTypeName;

	/**
	 * Constructor.
	 * 
	 * @param projectName
	 * @param qualifiedTypeName
	 * @throws NullPointerException
	 *             in case some of the params is null.
	 */
	public TypeKey(String projectName, String qualifiedTypeName)
	{
		ContractChecker.nullCheckParam(projectName, "projectName");
		ContractChecker.nullCheckParam(qualifiedTypeName, "qualifiedTypeName");

		this.projectName = projectName;
		this.qualifiedTypeName = qualifiedTypeName;
	}

	@Override
	public int hashCode()
	{
		return projectName.hashCode() + qualifiedTypeName.hashCode();
	}

	@Override
	public boolean equals(Object o)
	{
		if (o instanceof TypeKey)
		{
			return ((TypeKey) o).hashCode() == hashCode();
		}

		return false;
	}
}
