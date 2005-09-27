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
package org.eclipse.jst.ws.internal.axis.consumption.core.command;


import java.io.File;
import java.util.ResourceBundle;

import org.apache.axis.tools.ant.axis.AdminClientTask;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Target;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jst.ws.internal.axis.consumption.core.common.JavaWSDLParameter;
import org.eclipse.wst.command.internal.provisional.env.core.EnvironmentalOperation;
import org.eclipse.wst.command.internal.provisional.env.core.common.Environment;
import org.eclipse.wst.command.internal.provisional.env.core.common.SimpleStatus;
import org.eclipse.wst.command.internal.provisional.env.core.common.Status;

/**
 * Commands are executable, undoable, redoable objects. Every Command has a name and a description.
 */

public class AxisDeployCommand extends EnvironmentalOperation
{
  protected static final String SERVICE_EXT = "/services/AdminService"; //$NON-NLS-1$

  private JavaWSDLParameter javaWSDLParam;
  private ResourceBundle resource = ResourceBundle.getBundle("org.eclipse.jst.ws.axis.consumption.core.consumption"); //$NON-NLS-1$

  /**
   * Constructor for AxisDeployCommand.
   * @param String description
   * @param String name
   * 
   */
  public AxisDeployCommand()
  {
  }

  public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable )
  {
    Environment environment = getEnvironment();
    if (javaWSDLParam == null)
    {
      return new SimpleStatus("Java2WSDLCommand", //$NON-NLS-1$
      getMessage("MSG_ERROR_JAVA_WSDL_PARAM_NOT_SET"), Status.ERROR);
    }

    if (javaWSDLParam.getProjectURL() == null || javaWSDLParam.getProjectURL().equals(""))
    { //$NON-NLS-1$
      return new SimpleStatus("Java2WSDLCommand", //$NON-NLS-1$
      getMessage("MSG_ERROR_PROJECT_URL_PARAM_NOT_SET"), Status.ERROR);
    }

    if (javaWSDLParam.getDeploymentFiles() == null || javaWSDLParam.getDeploymentFiles().length == 0)
    {
      return new SimpleStatus("Java2WSDLCommand", //$NON-NLS-1$
      getMessage("MSG_ERROR_DEPLOY_FILE_PARAM_NOT_SET"), Status.ERROR);
    }

    environment.getProgressMonitor().report(getMessage("MSG_AXIS_DEPLOY"));

    Status status = executeAntTask();
    if (status.getSeverity() == Status.ERROR)
    {
    	environment.getStatusHandler().reportError(status);
    }
    
    return status;
  }

  protected Status executeAntTask()
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
    }
    catch (BuildException e)
    {
      e.printStackTrace();
      String message = e.getMessage();
      if (e.getCause() != null)
      {
        message = e.getCause().toString();
      }
      
      Status[] childStatus = new Status[1];
      childStatus[0] = new SimpleStatus("AxisDeployCommand", message, Status.ERROR);
      return new SimpleStatus("AxisDeployCommand", //$NON-NLS-1$
      getMessage("MSG_ERROR_AXIS_DEPLOY"), childStatus);
    }
    return new SimpleStatus("AxisDeployCommand", //$NON-NLS-1$
    getMessage("MSG_AXIS_DEPLOY_OK"), Status.OK);

  }

  /**
   * Returns the message string identified by the given key from plugin.properties.
   * 
   * @return The String message.
   */
  public String getMessage(String key)
  {
    return resource.getString(key);
  }

  /**
   * @param javaWSDLParam The javaWSDLParam to set.
   */
  public void setJavaWSDLParam(JavaWSDLParameter javaWSDLParam)
  {
    this.javaWSDLParam = javaWSDLParam;
  }

}
