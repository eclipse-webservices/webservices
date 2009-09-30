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

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jst.ws.jaxws.dom.runtime.PrimitiveTypeHandler;
import org.eclipse.jst.ws.jaxws.dom.runtime.validation.provider.exceptions.TypeNotFoundException;

/**
 * Factory for IType objects. Contains cache with already found types per project this way minimizing the time needed to obtain an IType instance for
 * concreate type. Registers ResourceChangedListener to listen for changes in projects. If some resource in some projects has been changed the cache
 * for this project is cleaned.
 * 
 * @author Georgi Vachkov
 */
public class TypeFactory
{
	/**
	 * Analysed types cache.
	 */
	private static Hashtable<String, Map<String, IType>> typesCache = new Hashtable<String, Map<String, IType>>();

	private static TypeCache cache = null;

	/**
	 * Creates TypeProxy instance for <code>qualifiedName</code> for the project with name <code>projectName</code>.
	 * 
	 * @param projectName
	 * @param qualifiedName
	 * @return instance of IType in case type has been found
	 * @throws TypeNotFoundException
	 *             if type with fully qualified name <tt>qualifiedName</tt> is not found
	 * @throws JavaModelException
	 */
	public static IType create(String projectName, String qualifiedName) throws TypeNotFoundException, JavaModelException
	{
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);

		return create(JavaCore.create(project), qualifiedName);
	}

	/**
	 * Finds IType object for <code>qualifiedName</code> for the project in which <code>type</code> resides.
	 * 
	 * @param qualifiedName
	 * @param type
	 * @return instance of IType in case type has been found
	 * @throws TypeNotFoundException
	 *             if type with fully qualified name <tt>qualifiedName</tt> is not found
	 * @throws JavaModelException
	 */
	public static IType create(String qualifiedName, IType type) throws TypeNotFoundException, JavaModelException
	{
		return create(type.getJavaProject(), qualifiedName);
	}

	/**
	 * Finds IType object for <code>qualifiedName</code> type in <code>javaProject</code> project. On first steps tries to find this type in cache
	 * and if not found new instance is created.
	 * 
	 * @param javaProject
	 * @param qualifiedName
	 * @return instance of IType in case type has been found
	 * @throws TypeNotFoundException
	 *             if type with fully qualified name <tt>qualifiedName</tt> is not found
	 * @throws JavaModelException
	 */
	private static IType create(IJavaProject javaProject, String qualifiedName) throws TypeNotFoundException, JavaModelException
	{
		String localQualifiedName = qualifiedName;

		if (PrimitiveTypeHandler.isArrayType(localQualifiedName))
		{
			localQualifiedName = PrimitiveTypeHandler.getTypeFromArrayType(localQualifiedName);
		}

		if (PrimitiveTypeHandler.isJavaPrimitiveType(localQualifiedName))
		{
			localQualifiedName = PrimitiveTypeHandler.getObjectTypeForPrimitiveType(localQualifiedName);
		}

		if (cache == null)
		{
			cache = new TypeCache();
			ResourcesPlugin.getWorkspace().addResourceChangeListener(cache);
		}

		IType type = cache.findType(localQualifiedName, javaProject);
		if (type != null)
		{
			return type;
		}

		type = javaProject.findType(localQualifiedName);
		if (type == null)
		{
			throw new TypeNotFoundException(localQualifiedName);
		}

		cache.addType(localQualifiedName, type, javaProject);

		return type;
	}

	/**
	 * Caching functionality. All types that have been retrieved are cached. Caching is per project. Search is executen in followin way:<br>
	 * 1. Search for chache for the project. If cache is not found an empty cache is created.<br>
	 * 2. Type is searched in the projects cache and is returned. If a type is not found <code>null</code> is returned.
	 * 
	 * @author Georgi Vachkov
	 */
	private static class TypeCache implements IResourceChangeListener
	{
		public void resourceChanged(IResourceChangeEvent event)
		{
			if (event != null && event.getDelta() != null)
			{
				for (IResourceDelta children : event.getDelta().getAffectedChildren())
				{
					IResourceDelta rDelta = (IResourceDelta) children.getAdapter(IResourceDelta.class);
					if (rDelta.getResource() != null && rDelta.getResource() instanceof IProject)
					{
						removeMapForProject(rDelta.getResource().getName());
					}
				}
			}
		}

		protected IType findType(String qualifiedName, IJavaProject javaProject)
		{
			return getMapForProject(javaProject).get(qualifiedName);
		}

		protected void addType(String qualifiedName, IType type, IJavaProject javaProject)
		{
			getMapForProject(javaProject).put(qualifiedName, type);
		}

		private void removeMapForProject(String projectName)
		{
			typesCache.remove(projectName);
		}

		private Map<String, IType> getMapForProject(IJavaProject javaProject)
		{
			String projectName = javaProject.getProject().getName();
			Map<String, IType> projectTypes = typesCache.get(projectName);
			if (projectTypes == null)
			{
				projectTypes = new HashMap<String, IType>();
				typesCache.put(projectName, projectTypes);
			}

			return projectTypes;
		}
	}
}
