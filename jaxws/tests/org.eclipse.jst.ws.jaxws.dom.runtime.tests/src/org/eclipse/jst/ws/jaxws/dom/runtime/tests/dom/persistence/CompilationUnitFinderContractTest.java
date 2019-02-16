/*******************************************************************************
 * Copyright (c) 2009 by SAP AG, Walldorf. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     SAP AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.jaxws.dom.runtime.tests.dom.persistence;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.WsDOMLoadCanceledException;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.ICompilationUnitFinder;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.ICompilationUnitHandler;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.IProjectSelector;
import org.eclipse.jst.ws.jaxws.testutils.jmock.Mock;
import org.eclipse.jst.ws.jaxws.testutils.jmock.MockObjectTestCase;
import org.eclipse.jst.ws.jaxws.testutils.project.TestProject;
import org.jmock.core.Constraint;
import org.jmock.core.Invocation;
import org.jmock.core.Stub;

/**
 * The tests in this class might be executed in a dirty workspace. Thus this class should assume that other projects are already existing and ignore them
 * @author Hristo Sabev
 *
 */
public abstract class CompilationUnitFinderContractTest extends MockObjectTestCase
{
//	private ICompilationUnitFinder target;
	private List<IProjectSelector> projectSelectors;
	
	private IJavaModel javaModel;
	
	private List<String> existingProjectsNames;
	
	protected IJavaModel javaModel() {
		return javaModel;
	}
	
	protected List<IProjectSelector> projectSelectors() {
		return projectSelectors;
	}
	
	protected abstract ICompilationUnitFinder createTarget();
	
	@Override
	public void setUp() {
		
		javaModel = JavaCore.create(ResourcesPlugin.getWorkspace().getRoot());
		projectSelectors = new ArrayList<IProjectSelector>();
		existingProjectsNames = new ArrayList<String>();
		for (IProject prj : (IProject[])javaModel.getWorkspace().getRoot().getProjects())
		{
			existingProjectsNames.add(prj.getName());
		}
	}
	
	public void test_find() throws CoreException, WsDOMLoadCanceledException {
	
		
		final TestProject project1 = new TestProject();
		project1.createSourceFolder("sourceFolder");
		final IPackageFragment pf1 = project1.createPackage("com.sap.test1");
		project1.createType(pf1, "CompilationUnit1.java", "class Ignorred{}");
		project1.createType(pf1, "CompilationUnit2.java", "class Ignorred{}");
		project1.createType(pf1, "CompilationUnit3.java", "class Ignorred{}");
		
		final TestProject project2 = new TestProject();
		project2.createSourceFolder("sourceFolder");
		final IPackageFragment pf2 = project2.createPackage("com.sap.test1");
		project2.createType(pf2, "CompilationUnit1.java", "class Ignorred{}");
		project2.createType(pf2, "CompilationUnit2.java", "class Ignorred{}");
		
		final TestProject project3 = new TestProject();
		project3.createSourceFolder("sourceFolder");
		final IPackageFragment pf3 = project3.createPackage("com.sap.test3");
		project3.createType(pf3, "CompilationUnit1.java", "class Ignorred{}");
		project3.createType(pf3, "CompilationUnit2.java", "class Ignorred{}");
	
		final IProjectSelector exludeProject2 = new IProjectSelector() {

			public boolean approve(IJavaProject prj)
			{
				return prj.getElementName().equals(project2.getJavaProject().getElementName()) || existingProjectsNames.contains(prj.getElementName())? false : true;
			}
			
		};
		
		final IProjectSelector addmitAll_exceptProject2 = new IProjectSelector() {
			public boolean approve(IJavaProject prj)
			{
				return prj.getElementName().equals(project2.getJavaProject().getElementName()) || existingProjectsNames.contains(prj.getElementName()) ? false : true;
			}
		};
		
		projectSelectors().add(exludeProject2);
		projectSelectors().add(addmitAll_exceptProject2);

		final ICompilationUnitFinder target = createTarget();
			
		
		final Mock<ICompilationUnitHandler> mockCUHandler = mock(ICompilationUnitHandler.class);
		
		final Stub configureExpectations = new Stub() 
		{

			public Object invoke(Invocation arg0) throws Throwable
			{
				mockCUHandler.expects(once()).method("handle").with(projectNamed(project1.getJavaProject().getElementName())).id("cuCall1");
				mockCUHandler.expects(once()).method("handle").with(cuNamed(pf1.getCompilationUnit("CompilationUnit1.java").getPath().toOSString())).after("cuCall1").id("cuCall2");
				mockCUHandler.expects(once()).method("handle").with(cuNamed(pf1.getCompilationUnit("CompilationUnit2.java").getPath().toOSString())).after("cuCall1").id("cuCall3");
				mockCUHandler.expects(once()).method("handle").with(cuNamed(pf1.getCompilationUnit("CompilationUnit3.java").getPath().toOSString())).after("cuCall1").id("cuCall4");
				
				mockCUHandler.expects(once()).method("handle").with(projectNamed(project3.getJavaProject().getElementName())).id("cuCall5");
				mockCUHandler.expects(once()).method("handle").with(cuNamed(pf3.getCompilationUnit("CompilationUnit1.java").getPath().toOSString())).after("cuCall5").id("cuCall6");
				mockCUHandler.expects(once()).method("handle").with(cuNamed(pf3.getCompilationUnit("CompilationUnit2.java").getPath().toOSString())).after("cuCall5").id("cuCall7");
				mockCUHandler.expects(once()).method("finished").withNoArguments().after("cuCall1").after("cuCall2").after("cuCall3").after("cuCall4").after("cuCall5").after("cuCall6").after("cuCall7");
				return null;
			}

			public StringBuffer describeTo(StringBuffer arg0)
			{
				return arg0.append("Configure expectations after start is invoked");
			}
			
		};
		mockCUHandler.expects(once()).method("started").withNoArguments().will(configureExpectations);
		target.find(null, mockCUHandler.proxy());
	}
	
	private Constraint projectNamed(final String prjName) {
		return new Constraint() {
			public StringBuffer describeTo(StringBuffer arg0)
			{
				return arg0.append(prjName);
			}

			public boolean eval(Object arg0)
			{
				if (!(arg0 instanceof IJavaProject))
				{
					return false;
				}
				final IJavaProject cast = (IJavaProject)arg0;
				return prjName.equals(cast.getElementName());
			}
		};
	}
	
	private Constraint cuNamed(final String cuName) {
		return new Constraint() {
			public StringBuffer describeTo(StringBuffer arg0)
			{
				return arg0.append(cuName);
			}

			public boolean eval(Object arg0)
			{
				if (!(arg0 instanceof ICompilationUnit))
				{
					return false;
				}
				final ICompilationUnit cast = (ICompilationUnit)arg0;
				return cuName.equals(cast.getPath().toOSString());
			}
		};
			
	}
	
}
