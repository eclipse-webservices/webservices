/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.ws.tests.util;

import org.eclipse.jface.operation.IRunnableContext;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.wst.command.internal.env.context.PersistentResourceContext;
import org.eclipse.wst.command.internal.env.core.data.DataFlowManager;
import org.eclipse.wst.command.internal.env.core.data.DataMappingRegistryImpl;
import org.eclipse.wst.command.internal.env.core.fragment.CommandFragment;
import org.eclipse.wst.command.internal.env.ui.eclipse.EclipseEnvironment;
import org.eclipse.wst.command.internal.env.ui.eclipse.EclipseProgressMonitor;
import org.eclipse.wst.command.internal.env.ui.widgets.SimpleCommandEngineManager;
import org.eclipse.wst.command.internal.env.ui.widgets.popup.DynamicPopupWizard;
import org.eclipse.wst.command.internal.provisional.env.core.common.StatusHandler;

public class DynamicPopupJUnitWizard extends DynamicPopupWizard {
	private StatusHandler handler_;
	public DynamicPopupJUnitWizard(StatusHandler handler)
	{
		handler_ = handler;
	}
	public void runHeadLess(IStructuredSelection selection,IRunnableContext context) {
		CommandFragment            rootFragment    = getRootFragment( selection, null );
		PersistentResourceContext  resourceContext = PersistentResourceContext.getInstance();
		EclipseProgressMonitor     monitor         = new EclipseProgressMonitor();
		EclipseEnvironment         environment     = new EclipseEnvironment( null, resourceContext, monitor, handler_ );
		    
		DataMappingRegistryImpl    dataRegistry_   = new DataMappingRegistryImpl();	    
		DataFlowManager            dataManager     = new DataFlowManager( dataRegistry_, environment );
		SimpleCommandEngineManager manager         = new SimpleCommandEngineManager(environment, dataManager);
		  
		commandWidgetBinding_.registerDataMappings( dataRegistry_ );
		manager.setRootFragment( rootFragment );
		manager.runForwardToNextStop( context );
	}
}