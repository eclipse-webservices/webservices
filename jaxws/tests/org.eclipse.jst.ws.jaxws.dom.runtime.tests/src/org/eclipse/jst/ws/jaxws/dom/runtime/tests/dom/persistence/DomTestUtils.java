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
package org.eclipse.jst.ws.jaxws.dom.runtime.tests.dom.persistence;

import junit.framework.Assert;

import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IServiceEndpointInterface;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebMethod;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebParam;
import org.eclipse.jst.ws.jaxws.testutils.project.TestProjectsUtils;
import org.eclipse.jst.ws.jaxws.utils.resources.FileUtils;

public class DomTestUtils
{
	public IMethod getExistingMethod(IType type, String methodName, String... args)
	{
		final IMethod m = type.getMethod(methodName, args);
		Assert.assertTrue(m.exists());
		return m;
	}

	public void setContents(final ICompilationUnit cu, final String contents) throws CoreException
	{
		final String contentToSet = "package "+cu.getParent().getElementName()+";\n"+contents;
		final IWorkspaceRunnable setContentsRunnable = new IWorkspaceRunnable()
		{
			public void run(IProgressMonitor monitor) throws CoreException
			{
				FileUtils.getInstance().setCompilationUnitContent(cu, contentToSet, true, null);
			}
		};
		
		TestProjectsUtils.executeWorkspaceRunnable(setContentsRunnable);
	}
	
	public IWebParam findParam(final IWebMethod method, final String implName) 
	{
		for (IWebParam param : method.getParameters()) {
			if (param.getImplementation().equals(implName)) {
				return param;
			}
		}
		
		return null;
	}	
	
	
	public IWebMethod findWebMethodByName(final IServiceEndpointInterface sei, final String name) 
	{
		for (IWebMethod webMethod : sei.getWebMethods()) {
			if(webMethod.getName().equals(name)) {
				return webMethod;
			}
		}
		
		return null;
	}		
}
