/*******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.internal.consumption.ui.widgets.test;

import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jst.ws.internal.consumption.command.common.CreateWebProjectCommand;
import org.eclipse.wst.command.internal.provisional.env.core.SimpleCommand;
import org.eclipse.wst.command.internal.provisional.env.core.common.Environment;
import org.eclipse.wst.command.internal.provisional.env.core.common.SimpleStatus;
import org.eclipse.wst.command.internal.provisional.env.core.common.Status;
import org.eclipse.wst.server.core.IServer;

public class CreateSampleProjectCommand extends SimpleCommand
{
  private String sampleProject;
  private String sampleProjectEar;
  private IServer existingServer;
  private String serverFactoryId;
  private boolean needEAR;
  private String j2eeVersion;
  
  
  public CreateSampleProjectCommand()
  {
    super("org.eclipse.jst.ws.internal.consumption.ui.wizard.client.common.CreateSampleProjectCommand","org.eclipse.jst.ws.internal.consumption.ui.wizard.client.common.CreateSampleProjectCommand");
  }

  public Status execute(Environment env)
  {
    IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
    if (!root.getProject(sampleProject).exists() || !root.getProject(sampleProjectEar).exists())
    {
      CreateWebProjectCommand c = new CreateWebProjectCommand();
      c.setProjectName(sampleProject);
      c.setEarProjectName(sampleProjectEar);
      c.setExistingServer(existingServer);
      c.setServerFactoryId(serverFactoryId);
      c.setNeedEAR(needEAR);
      c.setJ2EEVersion(j2eeVersion);
      return c.execute(env);
    }
    return new SimpleStatus("");
  }

  public void setSampleProject(String sampleProject)
  {
    this.sampleProject = sampleProject; 	
  }

  public void setSampleProjectEAR(String sampleProjectEar)
  {
  	this.sampleProjectEar = sampleProjectEar;
  }

  public void setExistingServer(IServer existingServer) {

    this.existingServer = existingServer;
  }
  
  public void setServerFactoryId(String serverFactoryId)
  {
  	this.serverFactoryId = serverFactoryId;
  }

  /**
   * @param needEAR The needEAR to set.
   */
  public void setNeedEAR(boolean needEAR)
  {
    this.needEAR = needEAR;
  }
    
  /**
   * @param version The j2eeVersion to set.
   */
  public void setJ2eeVersion(String version)
  {
    j2eeVersion = version;
  }
}
