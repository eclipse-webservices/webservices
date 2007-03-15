/*******************************************************************************
 * Copyright (c) 2004, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20070314   176886 pmoogk@ca.ibm.com - Peter Moogk
 *******************************************************************************/
package org.eclipse.jst.ws.tests.util;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.operation.IRunnableContext;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.wst.command.internal.env.context.PersistentResourceContext;
import org.eclipse.wst.command.internal.env.core.data.DataFlowManager;
import org.eclipse.wst.command.internal.env.core.data.DataMappingRegistryImpl;
import org.eclipse.wst.command.internal.env.core.fragment.CommandFragment;
import org.eclipse.wst.command.internal.env.eclipse.EclipseEnvironment;
import org.eclipse.wst.command.internal.env.eclipse.IEclipseStatusHandler;
import org.eclipse.wst.command.internal.env.ui.widgets.CommandWidgetBinding;
import org.eclipse.wst.command.internal.env.ui.widgets.SimpleCommandEngineManager;
import org.eclipse.wst.command.internal.env.ui.widgets.popup.DynamicPopupWizard;
import org.eclipse.wst.common.environment.ILog;

public class DynamicPopupJUnitWizard extends DynamicPopupWizard {
	private IEclipseStatusHandler handler_;
    private ILog log_;
	public DynamicPopupJUnitWizard(IEclipseStatusHandler handler)
	{
		handler_ = handler;
	}
  
    public DynamicPopupJUnitWizard(IEclipseStatusHandler handler, ILog log)
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
