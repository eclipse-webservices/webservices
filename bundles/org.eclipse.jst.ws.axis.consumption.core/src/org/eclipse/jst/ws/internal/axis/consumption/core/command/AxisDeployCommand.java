/*******************************************************************************
 * Copyright (c) 2003, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20080227   119964 trungha@ca.ibm.com - Trung Ha
 *******************************************************************************/
package org.eclipse.jst.ws.internal.axis.consumption.core.command;


import java.io.File;

import org.apache.axis.client.AdminClient;
import org.apache.axis.tools.ant.axis.AdminClientTask;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Target;
import org.apache.tools.ant.filters.StringInputStream;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.ws.internal.axis.consumption.core.AxisConsumptionCoreMessages;
import org.eclipse.jst.ws.internal.axis.consumption.core.common.JavaWSDLParameter;
import org.eclipse.jst.ws.internal.common.J2EEUtils;
import org.eclipse.jst.ws.internal.plugin.WebServicePlugin;
import org.eclipse.wst.command.internal.env.common.FileResourceUtils;
import org.eclipse.wst.command.internal.env.core.common.ProgressUtils;
import org.eclipse.wst.command.internal.env.core.common.StatusUtils;
import org.eclipse.wst.command.internal.env.core.context.ResourceContext;
import org.eclipse.wst.common.environment.IEnvironment;
import org.eclipse.wst.common.environment.IStatusHandler;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;
/**
 * Commands are executable, undoable, redoable objects. Every Command has a name and a description.
 */

public class AxisDeployCommand extends AbstractDataModelOperation
{
  protected static final String SERVICE_EXT = "/services/AdminService"; //$NON-NLS-1$
  protected static final String SERVER_CONFIG = "server-config.wsdd"; //$NON-NLS-1$

  private JavaWSDLParameter javaWSDLParam;
  private String project_;
  
  /**
   * Constructor for AxisDeployCommand.
   * @param String description
   * @param String name
   * 
   */
  public AxisDeployCommand()
  {
  }
  
  public AxisDeployCommand(String project ){
	  this.project_ = project;
  }

  public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable )
  {
    IEnvironment environment = getEnvironment();
    if (javaWSDLParam == null)
    {     
      return StatusUtils.errorStatus(AxisConsumptionCoreMessages.MSG_ERROR_JAVA_WSDL_PARAM_NOT_SET);
    }

    if (javaWSDLParam.getProjectURL() == null || javaWSDLParam.getProjectURL().equals(""))
    { //$NON-NLS-1$
      return StatusUtils.errorStatus(AxisConsumptionCoreMessages.MSG_ERROR_PROJECT_URL_PARAM_NOT_SET);
    }

    if (javaWSDLParam.getDeploymentFiles() == null || javaWSDLParam.getDeploymentFiles().length == 0)
    {
      return StatusUtils.errorStatus(AxisConsumptionCoreMessages.MSG_ERROR_DEPLOY_FILE_PARAM_NOT_SET);
    }

    ProgressUtils.report(monitor, AxisConsumptionCoreMessages.MSG_AXIS_DEPLOY);

    IStatus status = executeAntTask(monitor);
    if (status.getSeverity() == Status.ERROR)
    {
    	environment.getStatusHandler().reportError(status);
    }
    
    return status;
  }

  protected IStatus executeAntTask(IProgressMonitor monitor)
  {
    final class DeployTask extends AdminClientTask
    {
      public DeployTask()
      {
		super.setProject(new Project());
		super.getProject().init();
		super.setTaskType("axis"); //$NON-NLS-1$
		super.setTaskName("axis-admin"); //$NON-NLS-1$
		super.setOwningTarget(new Target());
      }
    }

    DeployTask adminClient = new DeployTask();
    String url = javaWSDLParam.getProjectURL() + SERVICE_EXT;
    adminClient.setUrl(url);
    adminClient.setXmlFile(new File(javaWSDLParam.getDeploymentFiles()[0]));

    // Since the admin server may not be available right away we will try
    // several times to execute it.
    try
    {
      BuildException lastException = null;
      
      for( int index = 0; index < 20; index++ )
      {
        try
        {
          lastException = null;
          adminClient.execute();
        }
        catch( BuildException exc )
        {
          lastException = exc;
          
          try
          {
            Thread.sleep( 200 );
          }
          catch( InterruptedException threadException  )
          {
          }
        }
        
        // If no exception occured then we should break out of the loop.
        if( lastException == null ) break;
      }
      
      // If after many tries we still get an exception, then we will re throw it.
      if( lastException != null ) throw lastException;
      
      return createConfigFile(monitor, url);
    }
    catch (BuildException e)
    {
      e.printStackTrace();
      String message = e.getMessage();
      if (e.getCause() != null)
      {
        message = e.getCause().toString();
      }
      
      IStatus[] childStatus = new Status[1];
      childStatus[0] = StatusUtils.errorStatus( message);
      return StatusUtils.multiStatus(AxisConsumptionCoreMessages.MSG_ERROR_AXIS_DEPLOY, childStatus);
    }
  }

  /** Creates 'server-config.wsdd' in the WebContent folder in the Eclipse workspace project 
   * @param monitor
   * @param url
   * @return
   */
  private IStatus createConfigFile(IProgressMonitor monitor, String url) {
	AdminClient listAdmin = new AdminClient();
	String config = "";
    try {
		config = listAdmin.process(new String[] { "-l" + url, "list" });

		IPath webInfPath = J2EEUtils.getWebInfPath(ResourcesPlugin
					.getWorkspace().getRoot().getProject(project_));
		IPath relativeServerConfigPath = webInfPath.append(SERVER_CONFIG);
		IStatusHandler statusHandler = getEnvironment().getStatusHandler();
		ResourceContext context = WebServicePlugin.getInstance()
					.getResourceContext();
		FileResourceUtils.createFile(context, relativeServerConfigPath,
					new StringInputStream(config), monitor, statusHandler);
	} catch (Exception e){
		e.printStackTrace();
	    String message = e.getMessage();
	    if (e.getCause() != null)
	    {
	       message = e.getCause().toString();
	    }
	      
	    IStatus[] childStatus = new Status[1];
	    childStatus[0] = StatusUtils.errorStatus( message);
	    return StatusUtils.multiStatus(AxisConsumptionCoreMessages.MSG_ERROR_AXIS_DEPLOY, childStatus);
	}
	return Status.OK_STATUS; 
  }

  /**
   * @param javaWSDLParam The javaWSDLParam to set.
   */
  public void setJavaWSDLParam(JavaWSDLParameter javaWSDLParam)
  {
    this.javaWSDLParam = javaWSDLParam;
  }

}
