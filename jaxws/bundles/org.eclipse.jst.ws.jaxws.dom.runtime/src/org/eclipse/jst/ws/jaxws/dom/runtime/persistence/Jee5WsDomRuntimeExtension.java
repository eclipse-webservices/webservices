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
package org.eclipse.jst.ws.jaxws.dom.runtime.persistence;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IDOM;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWsDOMRuntimeExtension;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWsDomLoadListener;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.WsDOMLoadCanceledException;
import org.eclipse.jst.ws.jaxws.dom.runtime.internal.plugin.JaxWsDomRuntimeMessages;

import static org.eclipse.jst.ws.jaxws.utils.ContractChecker.nullCheckParam;

public class Jee5WsDomRuntimeExtension implements IWsDOMRuntimeExtension
{
	public static final String ID = "org.eclipse.jst.ws.jaxws.dom.jee5";//$NON-NLS-1$
	
	private List<IWsDomLoadListener> listeners = new ArrayList<IWsDomLoadListener>();
	private JaxWsWorkspaceResource workspaceResource;
	private boolean finished;
	private boolean started;

	public void createDOM(final IProgressMonitor monitor) throws IOException, WsDOMLoadCanceledException
	{
		Job.getJobManager().beginRule(ResourcesPlugin.getWorkspace().getRoot(), monitor);
		try {
			if (workspaceResource == null || workspaceResource.isLoadCnaceled()) {			
				performLoad(monitor);
			}
		} finally {
			Job.getJobManager().endRule(ResourcesPlugin.getWorkspace().getRoot());
		}
	}

	private void performLoad(final IProgressMonitor monitor) throws IOException, WsDOMLoadCanceledException 
	{
		workspaceResource = createResource();
		workspaceResource.setProgressMonitor(monitor);
		try {
			started();
			workspaceResource.load(null);
			if (workspaceResource.isLoadCnaceled()) {
				throw createCanceledException();
			}
		} finally {
			finished();
		}
		
		workspaceResource.startSynchronizing();
	}

	public IDOM getDOM() throws WsDOMLoadCanceledException
	{
		if (workspaceResource == null) {
			return null;
		}
		
		if (workspaceResource.isLoadCnaceled()) {
			throw createCanceledException();
		}
		
		return workspaceResource.getDOM();
	}
	
	public void addLoadListener(IWsDomLoadListener listener) 
	{
		nullCheckParam(listener, "listener");//$NON-NLS-1$
		
		synchronized (listeners) {
			listeners.add(listener);
		}
		
		if (started) {
			listener.started();			
		}
		
		if (finished) {
			listener.finished();			
		}
	}
	
	public void removeLoadListener(IWsDomLoadListener listener) 
	{
		nullCheckParam(listener, "listener");//$NON-NLS-1$
		
		synchronized (listeners) {
			listeners.remove(listener);
		}
	}
	
	protected IJavaModel javaModel() {
		return JavaCore.create(ResourcesPlugin.getWorkspace().getRoot());
	}
	
	protected JaxWsWorkspaceResource createResource() { 
		return new JaxWsWorkspaceResource(javaModel());
	}
	
	protected void finished() 
	{
		finished = true;
		for (IWsDomLoadListener listener : listeners) {
			listener.finished();
		}
	}

	protected void started() 
	{
		started = true;
		finished = false;
		for (IWsDomLoadListener listener : listeners) {
			listener.started();
		}
	}
	
	private WsDOMLoadCanceledException createCanceledException() {
		return new WsDOMLoadCanceledException("JAX-WS DOM loading canceled", JaxWsDomRuntimeMessages.WorkspaceCUFinder_LOADING_CANCELED); //$NON-NLS-1$
	}
}
