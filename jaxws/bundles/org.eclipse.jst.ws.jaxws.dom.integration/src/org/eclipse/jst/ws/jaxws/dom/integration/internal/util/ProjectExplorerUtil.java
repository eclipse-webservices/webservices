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

import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

/**
 * Utility class that holds methods to manipulate ProjectExplorer 
 * 
 * @author Georgi Vachkov
 */
public class ProjectExplorerUtil 
{
	public static final ProjectExplorerUtil INSTANCE = new ProjectExplorerUtil();
	
	/**
	 * This method should be called only from UI thread otherwise <code>null</code> will be returned
	 * 
	 * @return the project explorer {@link IViewPart} in case it is available otherwise <code>null</code>
	 */
	public IViewPart findProjectExplorer()
	{
		final IWorkbenchWindow workbenchWin = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		if (workbenchWin==null || workbenchWin.getActivePage()==null) {
			return null;
		}
		
		return workbenchWin.getActivePage().findView("org.eclipse.ui.navigator.ProjectExplorer"); //$NON-NLS-1$
	}
}
