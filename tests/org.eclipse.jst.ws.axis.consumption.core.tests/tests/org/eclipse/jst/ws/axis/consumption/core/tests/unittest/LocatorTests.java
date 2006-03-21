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
 *******************************************************************************/

package org.eclipse.jst.ws.axis.consumption.core.tests.unittest;

import java.util.Iterator;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jst.ws.axis.consumption.core.tests.util.StdoutProgressMonitor;
import org.eclipse.jst.ws.axis.consumption.core.tests.util.Util;
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
			Util.init();
			try
			{
				String[] projects = new String[] {"Java1","Java2","Java3"};
				for (int i=0; i<projects.length; i++)
				{
					IJavaProject javaProject = Util.createJavaProject(projects[i]);
					Util.addRequiredJarsToJavaProject(javaProject);
					Util.copyExamplesToJavaProject(javaProject);
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
			int p = 0;
			IProgressMonitor monitor = new StdoutProgressMonitor();
			for (int i=0; i<categoryIds.length; i++)
			{
				System.out.println("Category ID = ["+categoryIds[i]+"]");
				if (categoryIds[i].equals("org.eclipse.jst.ws.internal.axis.consumption.core.locator.category.axis"))
				{
					n = i;
					Iterator iter = finder.getWebServiceClientsByCategoryId(categoryIds[i],monitor);
					while (iter.hasNext())
					{
						Object obj = iter.next();
						assertTrue("Finder returned a "+obj.getClass().getName()+" instead of a WebServiceClientInfo.",obj instanceof WebServiceClientInfo);
						p++;
						WebServiceClientInfo wscInfo = (WebServiceClientInfo)obj;
						String proxyClassURL = wscInfo.getImplURL();
						System.out.println("Axis client proxy class = ["+proxyClassURL+"]");
					}
				}
			}
			assertTrue("Axis locator extension missing.",n >= 0);
			assertTrue("Did not find the expected six proxies.",p == 6);
		}
		finally
		{
			System.out.println("ENDED test_AxisClientLocator");
		}
	}
}
