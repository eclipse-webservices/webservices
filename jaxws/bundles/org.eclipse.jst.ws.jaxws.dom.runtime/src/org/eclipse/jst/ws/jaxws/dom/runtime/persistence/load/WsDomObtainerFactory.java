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
package org.eclipse.jst.ws.jaxws.dom.runtime.persistence.load;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IDOM;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWsDOMRuntimeExtension;
import org.eclipse.jst.ws.jaxws.dom.runtime.internal.plugin.JaxWsDomRuntimeMessages;
import org.eclipse.jst.ws.jaxws.utils.operation.OperationInJobRunner;

/**
 * Factory for creating {@link IWsDomObtainer} instances
 * 
 * @author Danail Branekov
 */
public class WsDomObtainerFactory
{
	/**
	 * Creates a {@link IWsDomObtainer} instance which loads web service DOM asynchronously if necessary. The load routine is performed within a job
	 * which has the workspace root as a scheduling rule
	 * 
	 * @param domRuntime
	 *            the runtime for which the {@link IDOM} is to be loaded
	 */
	public IWsDomObtainer createAsynchronousObtainer(final IWsDOMRuntimeExtension domRuntime)
	{
		return new WsDomObtainer(domRuntime, new OperationInJobRunner(JaxWsDomRuntimeMessages.JAXWS_DOM_LOADING, ResourcesPlugin.getWorkspace().getRoot()));
	}
}
