/*******************************************************************************
 * Copyright (c) 2013 by SAP AG, Walldorf. 
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
package org.eclipse.jst.ws.jaxws.dom.integration.tests.util;

import java.util.Collection;

import org.eclipse.jst.ws.jaxws.dom.integration.internal.util.CommonNavigatorFinder;
import org.eclipse.jst.ws.jaxws.testutils.jmock.Mock;
import org.eclipse.jst.ws.jaxws.testutils.jmock.MockObjectTestCase;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.navigator.CommonNavigator;

public class CommonNavigatorFinderTest extends MockObjectTestCase
{
	private CommonNavigatorFinder finder;
	private Mock<IWorkbenchWindow> wbWindow;
	private Mock<IWorkbenchPage> wbPage;

	@Override
	protected void setUp() throws Exception
	{
		super.setUp();
		wbPage = mock(IWorkbenchPage.class);
		wbWindow = mock(IWorkbenchWindow.class);
		wbWindow.stubs().method("getActivePage").will(returnValue(wbPage.proxy()));
		finder = new CommonNavigatorFinder(wbWindow.proxy());
	}

	public void testWbWindowIsNull()
	{
		finder = new CommonNavigatorFinder(null);
		assertTrue("Empty collection expected", finder.findCommonNavigators().isEmpty());
	}

	public void testNoCommonNavigatorsOpened()
	{
		wbPage.stubs().method("getViewReferences").will(returnValue(new IViewReference[] {}));
		assertTrue("Empty collection expected", finder.findCommonNavigators().isEmpty());
	}

	public void testCommonNavigatorNotYetInstantiated()
	{
		final IViewReference viewRef = mockViewReference(null);
		wbPage.stubs().method("getViewReferences").will(returnValue(new IViewReference[] { viewRef }));
		assertTrue("Empty collection expected", finder.findCommonNavigators().isEmpty());
	}

	public void testNonCommonNavigatorViewNotFound()
	{
		final Mock<IViewPart> viewPart = mock(IViewPart.class);
		final IViewReference viewRef = mockViewReference(viewPart.proxy());
		wbPage.stubs().method("getViewReferences").will(returnValue(new IViewReference[] { viewRef }));
		assertTrue("Empty collection expected", finder.findCommonNavigators().isEmpty());
	}

	public void testCommonNavigatorOpened()
	{
		final CommonNavigator navigatorMock = new CommonNavigator();
		final IViewReference viewRef = mockViewReference(navigatorMock);
		wbPage.stubs().method("getViewReferences").will(returnValue(new IViewReference[] { viewRef }));
		final Collection<CommonNavigator> foundNavigators = finder.findCommonNavigators();
		assertEquals("One navigator expected", 1, foundNavigators.size());
		assertSame("Unexpected navigator", navigatorMock, foundNavigators.iterator().next());
	}

	private IViewReference mockViewReference(final IViewPart viewPart)
	{
		final Mock<IViewReference> viewRefMock = mock(IViewReference.class);
		viewRefMock.stubs().method("getView").will(returnValue(viewPart));
		return viewRefMock.proxy();
	}

}
