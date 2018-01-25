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
package org.eclipse.jst.ws.jaxws.testutils.project;

import junit.framework.TestCase;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IPackageFragmentRoot;

/**
 * General class for tests that should be started as JUnit Plugin tests. Provides basic functionalities for functional testing: 1. Create Java project
 * in junit-runtime-workbench. 2. Create default source folder. 3. Create default package.
 * 
 * @author Georgi Vachkov
 */
public abstract class ProjectBasedTest extends TestCase
{
	private TestProject project;

	/**
	 * <code>srcFolderName</code> the source folder where classes will be created, <code>packageName</code> is the package where classes will be
	 * created.
	 * 
	 * @param srcFolderName
	 * @throws CoreException
	 * @throws NullPointerException -
	 *             in case srcFolderName is null
	 */
	protected void createJavaProject(String srcFolderName) throws CoreException
	{
		if (srcFolderName == null)
		{
			throw new NullPointerException("srcFolderName should not be null.");
		}

		project = new TestProject();
		project.createSourceFolder(srcFolderName);
	}

	@Override
	protected void tearDown() throws Exception
	{
		if (project != null)
		{
			project.dispose();
		}
	}

	protected final TestProject getTestProject()
	{
		return project;
	}

	protected IPackageFragmentRoot getSourceFolder()
	{
		return project.getSourceFolder();
	}

	protected String getProjectName()
	{
		return project.getProject().getName();
	}
}
