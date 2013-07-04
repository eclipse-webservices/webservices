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
package org.eclipse.jst.ws.jaxws.dom.integration.internal.util;

import java.util.Collection;
import java.util.LinkedList;

import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.navigator.CommonNavigator;

/**
 * Finder for {@link CommonNavigator} views currently opened. This class must be used from the UI thread
 * 
 * @author Georgi Vachkov
 */
public class CommonNavigatorFinder
{
	private final IWorkbenchWindow wbWindow;

	public CommonNavigatorFinder(final IWorkbenchWindow wbWindow)
	{
		this.wbWindow = wbWindow;
	}

	public CommonNavigatorFinder()
	{
		this(PlatformUI.getWorkbench().getActiveWorkbenchWindow());
	}

	/**
	 * @return collection of {@link CommonNavigator} views currently opened, or empty collection if none
	 */
	public Collection<CommonNavigator> findCommonNavigators()
	{
		final Collection<CommonNavigator> result = new LinkedList<CommonNavigator>();

		if (wbWindow == null || wbWindow.getActivePage() == null)
		{
			return result;
		}

		for (IViewReference viewRef : wbWindow.getActivePage().getViewReferences())
		{
			final IViewPart viewPart = viewRef.getView(false);
			if (viewPart instanceof CommonNavigator)
			{
				result.add((CommonNavigator) viewPart);
			}
		}
		return result;
	}
}
