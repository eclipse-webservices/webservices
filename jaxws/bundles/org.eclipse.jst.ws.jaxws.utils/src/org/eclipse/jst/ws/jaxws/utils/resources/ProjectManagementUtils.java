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
package org.eclipse.jst.ws.jaxws.utils.resources;

import java.text.MessageFormat;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jst.ws.jaxws.utils.exception.MissingResourceException;
import org.eclipse.jst.ws.jaxws.utils.facets.FacetUtils;
import org.eclipse.jst.ws.jaxws.utils.facets.IFacetUtils;
import org.eclipse.jst.ws.jaxws.utils.internal.text.JaxWsUtilMessages;
import org.eclipse.jst.ws.jaxws.utils.logging.ILogger;
import org.eclipse.jst.ws.jaxws.utils.logging.Logger;


/**
 * Utility that provides useful project management services
 * 
 * @author Joerg Dehmel
 * @author Danail Branekov
 */
public final class ProjectManagementUtils
{
	private static final ILogger logger = new Logger();
	
	/**
	 * Provides the meta-inf folder of a EJB project.
	 * 
	 * @param pProjectName
	 *            project name
	 * @return the resource of the meta-inf folder
	 * @throws IllegalArgumentException
	 *             when project name specified is <code>null</code>, empty string, the project does not exist or is not accessible, or the project
	 *             is not an ejb project
	 * @throws MissingResourceException
	 *             either the project doesn't exist in the workspace or it doesn't contain a meta-inf folder
	 * @throws RuntimeException
	 *             when error occured while accessing java model
	 */
	public static IFolder findMetaInfFolder(final String pProjectName) throws MissingResourceException
	{
		final String metaInfName = "META-INF"; //$NON-NLS-1$
		final String ejbModuleName = "ejbModule"; //$NON-NLS-1$

		if (!isEjb3Project(pProjectName))
		{
			throw new IllegalArgumentException("Project " + pProjectName + " is not an EJB project"); //$NON-NLS-1$ //$NON-NLS-2$
		}

		IProject project = getProjectByName(pProjectName);
		IJavaProject javaProject = getJavaProject(project);
		IPackageFragment ejbModulePkgFragment = null;

		try
		{
			for (IPackageFragmentRoot fragmentRoot : javaProject.getPackageFragmentRoots())
			{
				if (fragmentRoot.getKind() == IPackageFragmentRoot.K_SOURCE && fragmentRoot.getElementName().equals(ejbModuleName))
				{
					ejbModulePkgFragment = fragmentRoot.getPackageFragment(metaInfName);
				}
			}

			verifyMetaInfFolder(ejbModulePkgFragment, pProjectName);
			return (IFolder) ejbModulePkgFragment.getCorrespondingResource();
		} catch (JavaModelException e)
		{
			logger.logError(e.getMessage(), e);
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	private static void verifyMetaInfFolder(IPackageFragment fragment, String projectName) throws MissingResourceException
	{
		if (fragment == null)
		{
			final String msg = MessageFormat.format("project {0} does not have a META-INF folder", new Object[] { projectName }); //$NON-NLS-1$
			final String localizedMsg = MessageFormat.format(JaxWsUtilMessages.ProjectManagementUtils_ProjectHasNoMetaInfFolderMsg, new Object[] { projectName });
			throw new MissingResourceException(msg, localizedMsg);
		}

		try
		{
			IResource resource = fragment.getCorrespondingResource();
			// should not happen!
			if (!(resource instanceof IFolder))
			{
				throw new RuntimeException("Resource " + resource.getName() + " is not a folder"); //$NON-NLS-1$ //$NON-NLS-2$
			}
		} catch (JavaModelException e)
		{
			logger.logError(e.getMessage(), e);
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	/**
	 * Check whether a project is an ejb 3 project
	 * 
	 * @param projectName
	 *            the name of the project; cannot be <code>null</code> or empty string
	 * @return true in case the project specified is a ejb 3 project; false otherwise
	 * @throws IllegalArgumentException
	 *             when the project name specified is <code>null</code>, empty string
	 * @throws IllegalStateException
	 *             when the project does not exist or is not accessible
	 * @throws RuntimeException
	 *             when an error occurred while accessing the project metadata
	 */
	public static boolean isEjb3Project(final String projectName)
	{
		IProject project = getProjectByName(projectName);

		try
		{
			return facetUtils().hasFacetWithVersion(project, IFacetUtils.EJB_30_FACET_VERSION, IFacetUtils.EJB_30_FACET_ID, true);
		} catch (CoreException e)
		{
			logger.logError(e.getMessage(), e);
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	/**
	 * Check whether a project is an web 2.5 project
	 * 
	 * @param projectName
	 *            the name of the project; cannot be <code>null</code> or empty string
	 * @return true in case the project specified is a web 2.5 project; false otherwise
	 * @throws IllegalArgumentException
	 *             when the project name specified is <code>null</code>, empty string
	 * @throws IllegalStateException
	 *             when the project does not exist or is not accessible
	 * @throws RuntimeException
	 *             when an error occurred while accessing the project metadata
	 */
	public static boolean isWeb25Project(final String projectName)
	{
		IProject project = getProjectByName(projectName);

		try
		{
			return facetUtils().hasFacetWithVersion(project, IFacetUtils.WEB_25_FACET_VERSION, IFacetUtils.WEB_25_FACET_ID, true);
		} catch (CoreException e)
		{
			logger.logError(e.getMessage(), e);
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	/**
	 * Retrieve the project instance by project name
	 * 
	 * @param projectName
	 *            the project name
	 * @return the project instance
	 * @throws IllegalArgumentException
	 *             when the project name specified is null or empty string
	 * @throws IllegalStateException
	 *             when the project does not exist or is not accessible
	 * 
	 */
	public static IProject getProjectByName(String projectName)
	{
		if (projectName == null || projectName.length() == 0)
		{
			throw new IllegalArgumentException("Illegal project name: " + projectName); //$NON-NLS-1$
		}

		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
		if (project == null || !project.isAccessible())
		{
			throw new IllegalStateException("Project is not accessible"); //$NON-NLS-1$
		}

		return project;
	}

	/**
	 * Get the java project instance
	 * 
	 * @param project
	 *            the project as eclipse resource
	 * @return the java project instance
	 * @throws IllegalArgumentException
	 *             when <code>project</code> null or the project is not accessible
	 */
	public static IJavaProject getJavaProject(IProject project)
	{
		if (project == null || !project.isAccessible())
		{
			throw new IllegalArgumentException("Project is not accessible"); //$NON-NLS-1$
		}
		return JavaCore.create(project);
	}
	
	/**
	 * Refresh the resource specified. Refresh depth used is zero
	 * @param resource the resource to be refreshed
	 * @throws CoreException 
	 * @see IResource#DEPTH_ZERO
	 */
	public static void refreshResource(IResource resource) throws CoreException
	{
		if(resource == null)
		{
			throw new NullPointerException("resource"); //$NON-NLS-1$
		}
		
		resource.refreshLocal(IResource.DEPTH_ZERO, null);
	}

	/**
	 * Refreshes the project the resource specified belongs to. Refresh depth used is infinite.
	 * @param the resource which project is to be refreshed
	 * @throws CoreException 
	 * @throws IllegalArgumentException when the resource specified is a workspace root
	 * @see IResource#DEPTH_INFINITE
	 */
	public static void refreshProjectByResource(IResource resource) throws CoreException
	{
		if(resource == null)
		{
			throw new NullPointerException("resource"); //$NON-NLS-1$
		}
		
		if(resource instanceof IWorkspaceRoot)
		{
			throw new IllegalArgumentException("Operation is not defined for workspace roots"); //$NON-NLS-1$
		}
		
		resource.getProject().refreshLocal(IResource.DEPTH_INFINITE, null);
	}
	
	private static IFacetUtils facetUtils()
	{
		return new FacetUtils();
	}
}
