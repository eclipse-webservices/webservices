/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20080425   221232 gilberta@ca.ibm.com - Gilbert Andrews
 *******************************************************************************/

package org.eclipse.jst.ws.internal.consumption.ui.widgets.test;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.ws.internal.consumption.command.common.CreateServerCommand;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.ServerCore;


/*
* The JSPGeneration task runs the jsp generation engine found in the 
* JBWizard Plugin
*
*
*/
public class FinishDefaultCommand extends AbstractDataModelOperation
{
  private String sampleServerTypeID;
  private IServer sampleExistingServer;
  private String sampleServerInstanceId;

  
  public FinishDefaultCommand ()
  {
  }

  public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable )
  {
  	IStatus status = Status.OK_STATUS;
  	if(sampleServerInstanceId != null){
  	  sampleExistingServer = ServerCore.findServer(sampleServerInstanceId);
  	  if (sampleExistingServer != null)
  		  sampleServerTypeID = sampleExistingServer.getServerType().getId();
    }
  	else if(sampleServerTypeID != null){
  		CreateServerCommand createServer = new CreateServerCommand();
  		createServer.setServerFactoryid(sampleServerTypeID);
  		createServer.execute(monitor, adaptable);
  		sampleServerInstanceId = createServer.getServerInstanceId();
  		sampleExistingServer = ServerCore.findServer(sampleServerInstanceId);
  	
  	}
    return status;
  
  }
  
  public void setServerInstanceId(String serverInstanceId)
  {
	this.sampleServerInstanceId = serverInstanceId;
  }
  
  public String getSampleServerTypeID()
  {
  	return sampleServerTypeID;
	
  }
  
  public IServer getSampleExistingServer()
  {
  	return sampleExistingServer;
  }

  public void setExistingServerId(String existingServerId)
  {
	  sampleServerTypeID=existingServerId;
  }  

}
