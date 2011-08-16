/*******************************************************************************
 * Copyright (c) 2011 by SAP AG, Walldorf. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     SAP AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.jaxws.dom.integration.internal.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.eclipse.jst.ws.jaxws.dom.integration.navigator.ILoadingWsProject;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebServiceProject;
import org.eclipse.swt.widgets.TreeItem;

/**
 * Utility which collects all tree items whose data is associated with {@link ILoadingWsProject} instance
 * @see TreeItem#getData()
 * @author Georgi Vachkov, Danail Branekov
 */
public class LoadingWsProjectNodesCollector
{
	public Collection<TreeItem> getLoadingWsProjects(final TreeItem[] treeItems)
	{
		final Collection<TreeItem> result = new ArrayList<TreeItem>();
		for (TreeItem item : treeItems) {
			result.addAll(getLoadingWsProjects(item));
		}
		
		return result;
	}
	
	private Collection<TreeItem> getLoadingWsProjects(final TreeItem treeItem)
	{
		if (treeItem.getData() instanceof IWebServiceProject) {
			return Collections.emptyList();
		}
		
		final Collection<TreeItem> result = new ArrayList<TreeItem>();
		if (treeItem.getData() instanceof ILoadingWsProject) {
			result.add(treeItem);
		}
		else {
			for (TreeItem child : treeItem.getItems()) {
				result.addAll(getLoadingWsProjects(child));
			}
		}
		
		return result;
	}
}
