/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.jst.ws.internal.consumption.ui.command;

import java.util.Iterator;
import java.util.Vector;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.ws.internal.consumption.ui.ConsumptionUIMessages;
import org.eclipse.jst.ws.internal.consumption.ui.plugin.WebServiceConsumptionUIPlugin;
import org.eclipse.jst.ws.internal.consumption.ui.wsrt.WebServiceRuntimeExtensionUtils2;
import org.eclipse.osgi.util.NLS;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;
import org.eclipse.wst.server.core.IServerType;
import org.eclipse.wst.server.core.ServerCore;

/**
 * Command lists valid property values where requested.
 * Requests made via a Ant property value setting which is mapped to this command in the plugin.xml 
 */

public class ListOptionsCommand extends AbstractDataModelOperation {

	private boolean runtimes_ = false;
	private boolean servers_ = false;
	
	public IStatus execute(IProgressMonitor monitor, IAdaptable info)
			throws ExecutionException {
				  	
		if (runtimes_)
		{				
				String[] typeIds = WebServiceRuntimeExtensionUtils2.getServiceTypeLabels().getIds_();
			    Vector runtimeIds = new Vector();
					for (int k = 0; k < typeIds.length; k++) {
						
						String typeId = typeIds[k];						
				    	String[] runtimes = WebServiceRuntimeExtensionUtils2.getRuntimesByServiceType(typeId);				    	
								    
				    	for (int j = 0; j < runtimes.length; j++) {
				    		String runtime = runtimes[j];
				    		if (!runtimeIds.contains(runtime))
				    		{
				    			runtimeIds.add(runtime);	
				    		}
						}
					}
		
					Status statusObj = new Status(IStatus.INFO, 
							WebServiceConsumptionUIPlugin.ID,
							IStatus.OK,
							ConsumptionUIMessages.MSG_INFO_ANT_RUNTIME_HEADING, 
							null);					
					getEnvironment().getStatusHandler().reportInfo(statusObj);
					
	        
			for (Iterator iterator = runtimeIds.iterator(); iterator
					.hasNext();) {			
				  getEnvironment().getStatusHandler().reportInfo(new Status(IStatus.INFO, 
						WebServiceConsumptionUIPlugin.ID,
						IStatus.OK, 
						NLS.bind(ConsumptionUIMessages.MSG_INFO_ANT_SERVER_RUNTIME,
								iterator.next()), null));
			}
				
			
		    String[] clientRuntimes = WebServiceRuntimeExtensionUtils2.getAllRuntimesForClientSide();
		    
			for (int i = 0; i < clientRuntimes.length; i++) {				
				
				getEnvironment().getStatusHandler().reportInfo(new Status(IStatus.INFO, 
						WebServiceConsumptionUIPlugin.ID,
						IStatus.OK, 
						NLS.bind(ConsumptionUIMessages.MSG_INFO_ANT_CLIENT_RUNTIME,
								clientRuntimes[i]), null));
			}
		}
		
		if (servers_)
		{			
			getEnvironment().getStatusHandler().reportInfo(new Status(IStatus.INFO, 
					WebServiceConsumptionUIPlugin.ID,
					IStatus.OK, 
					ConsumptionUIMessages.MSG_INFO_ANT_SERVER_HEADING, null));
			
			IServerType[] serverTypes = ServerCore.getServerTypes();
		      for (int i=0; i<serverTypes.length; i++)
		      {		        
				getEnvironment().getStatusHandler().reportInfo(new Status(IStatus.INFO, 
						WebServiceConsumptionUIPlugin.ID,
						IStatus.OK, 
						NLS.bind(ConsumptionUIMessages.MSG_INFO_ANT_SERVER,
								serverTypes[i].getId()), null));

		      }
		}  
		  
		return OK_STATUS;
	}
	
	public void setListRuntimes(boolean value)
	{		
		runtimes_=value;
	}
	
	public void setListServers(boolean value)
	{		
		servers_=value;
	}
}
