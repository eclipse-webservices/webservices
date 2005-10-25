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

package org.eclipse.jst.ws.internal.consumption.ui.widgets.test.explorer;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.ws.internal.common.ResourceUtils;
import org.eclipse.jst.ws.internal.consumption.command.common.StartProjectCommand;
import org.eclipse.jst.ws.internal.ext.test.WSDLTestFinishCommand;
import org.eclipse.wst.common.environment.IEnvironment;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.ws.internal.explorer.LaunchOption;
import org.eclipse.wst.ws.internal.explorer.LaunchOptions;
import org.eclipse.wst.ws.internal.explorer.WSExplorerLauncherCommand;
import org.eclipse.wst.ws.internal.explorer.plugin.ExplorerPlugin;

public class ExplorerServiceTestCommand extends AbstractDataModelOperation implements WSDLTestFinishCommand
{

  private boolean externalBrowser = true;
  private String wsdlServiceURL;
  private String serviceServerTypeID;
  private IServer serviceExistingServer;
  private String serviceProject;
  private List endpoints;
  
  /**
  * Constructs a new WebServiceExplorerLaunch object with the given label and description.
  */
  public ExplorerServiceTestCommand ()
  {
  }

  public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable )
  {
    IEnvironment env = getEnvironment();
    
  	IStatus status = Status.OK_STATUS;
    
    StartProjectCommand spc = new StartProjectCommand( true );
    spc.setServiceServerTypeID(serviceServerTypeID);
    spc.setServiceExistingServer(serviceExistingServer);
    IProject project = (IProject) ResourceUtils.findResource(serviceProject);
    spc.setServiceProject(project);
    spc.setIsWebProjectStartupRequested(true);
    spc.setEnvironment( env );
    
    status = spc.execute( monitor, null );
    if (status.getSeverity() == Status.ERROR)
    	return status;

    WSExplorerLauncherCommand launchCommand = new WSExplorerLauncherCommand();
    launchCommand.setForceLaunchOutsideIDE(externalBrowser);
    Vector launchOptionVector = new Vector();
	String stateLocation = ExplorerPlugin.getInstance().getPluginStateLocation();
	String defaultFavoritesLocation = ExplorerPlugin.getInstance().getDefaultFavoritesLocation();
	launchOptionVector.add(new LaunchOption(LaunchOptions.STATE_LOCATION,stateLocation));
	launchOptionVector.add(new LaunchOption(LaunchOptions.DEFAULT_FAVORITES_LOCATION,defaultFavoritesLocation));
    launchOptionVector.add(new LaunchOption(LaunchOptions.WSDL_URL,wsdlServiceURL));
    if (endpoints != null)
      for (Iterator it = endpoints.iterator(); it.hasNext();)
        launchOptionVector.add(new LaunchOption(LaunchOptions.WEB_SERVICE_ENDPOINT, it.next().toString()));
    launchCommand.setLaunchOptions((LaunchOption[])launchOptionVector.toArray(new LaunchOption[0]));
    launchCommand.setEnvironment( env );
    status = launchCommand.execute( monitor, null );
    return status;
  }

  public void setExternalBrowser(boolean externalBrowser)
  {
  	this.externalBrowser = externalBrowser;
  }
  
  public void setWsdlServiceURL(String wsdlServiceURL)
  {
  	this.wsdlServiceURL = wsdlServiceURL;
  }
  
  public void setServerTypeID(String serviceServerTypeID)
  {
  	this.serviceServerTypeID = serviceServerTypeID;
  }
  
  public void setExistingServer(IServer serviceExistingServer)
  {
  	this.serviceExistingServer = serviceExistingServer;
  }
  
  public void setServiceProject(String serviceProject)
  {
    this.serviceProject = serviceProject;
  }
  
  public void setEndpoint(List endpoints)
  {
    this.endpoints = endpoints;
  }
}