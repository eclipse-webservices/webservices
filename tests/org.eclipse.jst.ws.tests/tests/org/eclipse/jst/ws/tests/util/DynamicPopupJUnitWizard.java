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
package org.eclipse.jst.ws.tests.util;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.operation.IRunnableContext;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.wst.command.internal.env.context.PersistentResourceContext;
import org.eclipse.wst.command.internal.env.core.data.DataFlowManager;
import org.eclipse.wst.command.internal.env.core.data.DataMappingRegistryImpl;
import org.eclipse.wst.command.internal.env.core.fragment.CommandFragment;
import org.eclipse.wst.command.internal.env.ui.eclipse.EclipseEnvironment;
import org.eclipse.wst.command.internal.env.ui.widgets.CommandWidgetBinding;
import org.eclipse.wst.command.internal.env.ui.widgets.SimpleCommandEngineManager;
import org.eclipse.wst.command.internal.env.ui.widgets.popup.DynamicPopupWizard;
import org.eclipse.wst.common.environment.Log;
import org.eclipse.wst.common.environment.StatusHandler;

public class DynamicPopupJUnitWizard extends DynamicPopupWizard {
	private StatusHandler handler_;
    private Log log_;
	public DynamicPopupJUnitWizard(StatusHandler handler)
	{
		handler_ = handler;
	}
  
    public DynamicPopupJUnitWizard(StatusHandler handler, Log log)
    {
        handler_ = handler;
        log_ = log;
    }
    
	public void runHeadLess(IStructuredSelection selection,IRunnableContext context) {
		
		PersistentResourceContext  resourceContext = PersistentResourceContext.getInstance();
		EclipseEnvironment         environment     = new EclipseEnvironment( null, resourceContext, handler_ );
		if (log_!=null)
          environment.setLog(log_);
    
		DataMappingRegistryImpl    dataRegistry_   = new DataMappingRegistryImpl();	    
		DataFlowManager            dataManager     = new DataFlowManager( dataRegistry_, environment );
		SimpleCommandEngineManager manager         = new SimpleCommandEngineManager(environment, dataManager);
		  
	    try
	    {
	      commandWidgetBinding_ = (CommandWidgetBinding)wizardElement_.createExecutableExtension( "class" );
	    }
	    catch( CoreException exc )
	    {
	      exc.printStackTrace();
	    }
	    
		commandWidgetBinding_.registerDataMappings( dataRegistry_ );
		
		CommandFragment            rootFragment    = getRootFragment( selection, null );
		manager.setRootFragment( rootFragment );
		manager.runForwardToNextStop( context );
	}
}