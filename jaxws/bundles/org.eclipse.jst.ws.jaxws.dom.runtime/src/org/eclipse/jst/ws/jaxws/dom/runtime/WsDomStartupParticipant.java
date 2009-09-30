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
package org.eclipse.jst.ws.jaxws.dom.runtime;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.WsDOMRuntimeManager;
import org.eclipse.jst.ws.jaxws.dom.runtime.internal.plugin.JaxWsDomRuntimeMessages;
import org.eclipse.ui.IStartup;

/**
 * Startup participant registered on IDE startup extension point
 * 
 * @author Georgi Vachkov
 */
public class WsDomStartupParticipant implements IStartup 
{
	
	/**
	 * Called by the Eclipse on workbench startup.
	 * Starts load for registered web service runtimes 
	 */
    public void earlyStartup()
    {
		Job job = new Job(JaxWsDomRuntimeMessages.WsDomStartupParticipant_Startup_Job_Message)
		{
			@Override
			protected IStatus run(final IProgressMonitor monitor)
			{
		    	WsDOMRuntimeManager.instance().createDOMRuntimes(monitor);
		    	return Status.OK_STATUS;
			}
		};
		
		job.setUser(false);
		job.setPriority(Job.LONG);
		job.setRule(ResourcesPlugin.getWorkspace().getRoot());
		job.schedule();
    }
}
