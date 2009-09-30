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
package org.eclipse.jst.ws.jaxws.dom.runtime.persistence.sync;

import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.WsDOMLoadCanceledException;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.JaxWsWorkspaceResource;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.JaxWsWorkspaceResource.ServiceModelData;

public class InitialModelSynchronizer extends AbstractModelSynchronizer
{
	public InitialModelSynchronizer(JaxWsWorkspaceResource resource, ServiceModelData serviceData)
	{
		super(resource, serviceData);
	}

	public void load(Map<?,?> options, IProgressMonitor monitor) throws CoreException, WsDOMLoadCanceledException
	{
		try {
			resource().disableSaving();			
			resource().newCompilationUnitFinder(resource().javaModel(), resource().getProjectSelectors()).
				find(monitor, new LoaderCompilationUnitHandler());
		} finally {
			resource().enableSaving();
		}
	}
}
