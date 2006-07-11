/*******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060317   127456 cbrealey@ca.ibm.com - Chris Brealey
 * 20060711   147862 cbrealey@ca.ibm.com - Chris Brealey
 * 20060711   147864 cbrealey@ca.ibm.com - Chris Brealey
 *******************************************************************************/

package org.eclipse.jst.ws.axis.consumption.core.tests.unittest;

import java.util.Iterator;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jst.ws.axis.consumption.core.tests.util.JavaFilter;
import org.eclipse.jst.ws.axis.consumption.core.tests.util.StdoutProgressMonitor;
import org.eclipse.jst.ws.axis.consumption.core.tests.util.Util;
import org.eclipse.jst.ws.internal.wsrt.WebServiceJavaClientInfo;
import org.eclipse.wst.ws.internal.wsfinder.WebServiceFinder;
import org.eclipse.wst.ws.internal.wsrt.WebServiceClientInfo;

public class LocatorTests extends TestCase
{
	public static Test suite ()
	{
		return new TestSuite(LocatorTests.class);
	}

	public void test_AxisClientLocator ()
	{
		System.out.println("BEGIN test_AxisClientLocator");
		try
		{
			String[] projectNames = new String[] {"Java1","Java2","Java3"};
			IProject[] projects = new IProject[projectNames.length];
			for (int x=0; x<projectNames.length; x++)
			{
				projects[x] = ResourcesPlugin.getWorkspace().getRoot().getProject(projectNames[x]);
			}
			Util.init();
			try
			{
				for (int i=0; i<projects.length; i++)
				{
					IJavaProject javaProject = Util.createJavaProject(projectNames[i]);
					Util.addRequiredJarsToJavaProject(javaProject);
					Util.copyExamplesToJavaProject(javaProject,new JavaFilter());
				}
			}
			catch (Throwable t)
			{
				t.printStackTrace();
				fail("Failed to access or create prerequisite projects");
			}
			WebServiceFinder finder = WebServiceFinder.instance();
			String[] categoryIds = finder.getWebServiceCategoryIds();
			int n = -1;
			IProgressMonitor monitor = new StdoutProgressMonitor();
			for (int i=0; i<categoryIds.length; i++)
			{
				System.out.println("Category ID = ["+categoryIds[i]+"]");
				if (categoryIds[i].equals("org.eclipse.jst.ws.internal.axis.consumption.core.locator.category.axis"))
				{
					n = i;
					Iterator iter = finder.getWebServiceClientsByCategoryId(categoryIds[i],monitor);
					assertFoundClients(iter,6);
					iter = finder.getWebServiceClientsByCategoryId(categoryIds[i],projects,monitor);
					assertFoundClients(iter,6);
					iter = finder.getWebServiceClientsByCategoryId(categoryIds[i],new IProject[] {projects[0]},monitor);
					assertFoundClients(iter,2);
					iter = finder.getWebServiceClientsByCategoryId(categoryIds[i],new IProject[] {projects[1],projects[2]},monitor);
					assertFoundClients(iter,4);
					IProject noSuchProject = ResourcesPlugin.getWorkspace().getRoot().getProject("noSuchProject");
					iter = finder.getWebServiceClientsByCategoryId(categoryIds[i],new IProject[] {noSuchProject},monitor);
					assertFoundClients(iter,0);
				}
			}
			assertTrue("Axis locator extension missing.",n >= 0);
		}
		finally
		{
			System.out.println("ENDED test_AxisClientLocator");
		}
	}
	
	private void assertFoundClients ( Iterator iter, int x )
	{
		int p = 0;
		while (iter.hasNext())
		{
			Object obj = iter.next();
			assertTrue("Finder returned a "+obj.getClass().getName()+" instead of a WebServiceClientInfo.",obj instanceof WebServiceClientInfo);
			p++;
			WebServiceClientInfo wscInfo = (WebServiceClientInfo)obj;
			String proxyClassURL = wscInfo.getImplURL();
			System.out.println("Axis client proxy class = ["+proxyClassURL+"]");
			if (wscInfo instanceof WebServiceJavaClientInfo)
			{
				WebServiceJavaClientInfo wscJInfo = (WebServiceJavaClientInfo)wscInfo;
				assertNotNull("Found an unexpected null IType",wscJInfo.getType());
				System.out.println("Axis client proxy IType = ["+wscJInfo.getType()+"]");
			}
		}
		assertTrue("Found "+p+" proxies instead of the expected "+x+".",p == x);
	}
}
