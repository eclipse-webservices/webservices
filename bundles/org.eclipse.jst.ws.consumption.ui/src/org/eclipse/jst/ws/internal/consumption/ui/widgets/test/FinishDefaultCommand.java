/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.jst.ws.internal.consumption.ui.widgets.test;

import org.eclipse.wst.command.internal.provisional.env.core.SimpleCommand;
import org.eclipse.wst.command.internal.provisional.env.core.common.Environment;
import org.eclipse.wst.command.internal.provisional.env.core.common.SimpleStatus;
import org.eclipse.wst.command.internal.provisional.env.core.common.Status;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.ServerCore;


/*
* The JSPGeneration task runs the jsp generation engine found in the 
* JBWizard Plugin
*
*
*/
public class FinishDefaultCommand extends SimpleCommand
{
  private String LABEL = "JSPGenerationTask";
  private String DESCRIPTION = "Run the JSP Generation";
  
  private String sampleServerTypeID;
  private IServer sampleExistingServer;
  private String existingServerId_;
  private String sampleServerInstanceId;

  
  public FinishDefaultCommand ()
  {
    setDescription(DESCRIPTION);
    setName(LABEL);  
  }

  public Status execute(Environment env)
  {
  	Status status = new SimpleStatus( "" );
  	if(sampleServerInstanceId != null){
  	  sampleExistingServer = ServerCore.findServer(sampleServerInstanceId);
  	if (sampleExistingServer != null)
      sampleServerTypeID = sampleExistingServer.getServerType().getId();
      
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
    existingServerId_ = existingServerId;
  }  

}
