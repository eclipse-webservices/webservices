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

import org.eclipse.jst.ws.internal.data.TypeRuntimeServer;
import org.eclipse.wst.command.env.core.SimpleCommand;
import org.eclipse.wst.command.env.core.common.Environment;
import org.eclipse.wst.command.env.core.common.SimpleStatus;
import org.eclipse.wst.command.env.core.common.Status;
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
  private TypeRuntimeServer clientIds;

  
  public FinishDefaultCommand ()
  {
    setDescription(DESCRIPTION);
    setName(LABEL);  
  }

  public Status execute(Environment env)
  {
  	Status status = new SimpleStatus( "" );
  	if(clientIds != null){
  	  if (clientIds.getServerInstanceId() != null) 
        sampleExistingServer = ServerCore.findServer(clientIds.getServerInstanceId());
  	  if (sampleExistingServer==null && existingServerId_!=null && existingServerId_.length()>0)
  	  {
  	    //Use the existingServerId
  	    sampleExistingServer = ServerCore.findServer(existingServerId_);
  	  }
      if (sampleExistingServer != null)
        sampleServerTypeID = sampleExistingServer.getServerType().getId();
      else
        sampleServerTypeID = clientIds.getServerId();
  	}
    return status;
  
  }
  
  public void setClientTypeRuntimeServer(TypeRuntimeServer ids)
  {
    clientIds = ids;
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
