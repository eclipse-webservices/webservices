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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;

/**
 * Extends {@link ProjectBasedTest} by adding finctionalities to create classes in some package eider by reading it from text file or passed as
 * strings.
 * 
 * </pre>
 * 
 * @author Georgi Vachkov
 */
@SuppressWarnings("nls")
public abstract class ClassLoadingTest extends ProjectBasedTest
{
	private final static String JAVA_EXT = ".java";

	private final static char SEPARATOR = '#';

	protected IPackageFragment defaultPackage;

	public void createJavaProject(String srcFolder, String defaultPackageName) throws CoreException
	{
		createJavaProject(srcFolder);

		if (defaultPackageName != null)
		{
			defaultPackage = getSourceFolder().createPackageFragment(defaultPackageName, true, null);
		}
	}
	
	/**
	 * Creates class in test project. <code>sourceFilePath</code> must contain the class source without package declaration. The class is created in
	 * default package.
	 * 
	 * Project must be created before this method is called.
	 * 
	 * @param sourceFilePath
	 * @param className
	 * @return IType object for the created class
	 * @throws IOException
	 * @throws JavaModelException
	 */
	protected IType createClass(String sourceFilePath, String className) throws IOException, CoreException
	{
		if (sourceFilePath == null)
		{
			throw new NullPointerException("sourceFilePath should not be null.");
		}

		String source = TestProjectsUtils.readSource(this.getClass(), sourceFilePath);

		return createClass(defaultPackage, className, source);
	}

	/**
	 * Creates class in <code>pck</code> package with name and source <code>className, source</code>. Source must not incluse package
	 * declaration. Project must be created before this method is called.
	 * 
	 * @param pck
	 * @param className
	 * @param source
	 * @return
	 * @throws NullPointerException
	 *             in case some of the params is null.
	 * @throws JavaModelException
	 */
	protected IType createClass(IPackageFragment pck, String className, String source) throws CoreException
	{
		if (className == null)
		{
			throw new NullPointerException("param 'className' should not be null.");
		}

		if (source == null)
		{
			throw new NullPointerException("param 'source' should not be null.");
		}

		if (getTestProject() == null)
		{
			throw new NullPointerException("project must be created before classes can be added to it");
		}

		return getTestProject().createType(pck, className + JAVA_EXT, source);
	}

	/**
	 * Reads the content of <code>sourcesFilePath</code> and creates classes in test project. The content of <code>sourcesFilePath</code> sould be -
	 * '#' char (separator for classes) followed by Class name, new line and after that class source without package declaration.<br>
	 * Example:
	 * 
	 * <pre>
	 *  #ImplementsRemote
	 *  public class ImplementsRemote implements java.rmi.Remote
	 *  {
	 *  }
	 * 	
	 *  #NoDefaultConstructor
	 *  public class NoDefaultConstructor implements java.io.Serializable
	 *  {
	 * 	public NoDefaultConstructor(String param) {
	 *  	}
	 *  }
	 * </pre>
	 * 
	 * This method is useful if lots of small classes should be created from a source file. Project must be created before this method is called.
	 * 
	 * @param sourcesFilePath
	 * @return map of className->IType. that have been created
	 * @throws Exception
	 */
	protected Map<String, IType> createClasses(String sourcesFilePath) throws Exception
	{
		Map<String, IType> types = new HashMap<String, IType>();
		String source = TestProjectsUtils.readSource(this.getClass(), sourcesFilePath);

		int startPos = 0;
		while ((startPos = source.indexOf(SEPARATOR)) > -1)
		{
			int endPos = source.indexOf(SEPARATOR, startPos + 1);
			if (endPos == -1)
				endPos = source.length();

			String src = source.substring(startPos + 1, endPos);
			source = source.substring(endPos);

			String className = src.substring(0, src.indexOf('\n')).replaceAll("\r", "");
			src = src.substring(src.indexOf('\n') + 1);

			types.put(className, createClass(defaultPackage, className, src));
		}

		return types;
	}
}
