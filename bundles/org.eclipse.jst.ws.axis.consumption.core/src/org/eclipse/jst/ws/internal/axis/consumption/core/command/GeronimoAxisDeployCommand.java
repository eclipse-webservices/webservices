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
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ResourceBundle;
import org.apache.axis.AxisEngine;
import org.apache.axis.server.AxisServer;
import org.apache.axis.tools.ant.axis.AdminClientTask;
import org.apache.axis.utils.ClassUtils;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Target;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.ws.internal.axis.consumption.core.common.JavaWSDLParameter;
import org.eclipse.jst.ws.internal.common.J2EEUtils;
import org.eclipse.jst.ws.internal.common.ResourceUtils;
import org.eclipse.wst.command.internal.provisional.env.core.common.ProgressUtils;
import org.eclipse.wst.command.internal.provisional.env.core.common.StatusUtils;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;
import org.eclipse.wst.common.environment.Environment;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;

/**
 * Commands are executable, undoable, redoable objects. Every Command has a name and a description.
 */

public class GeronimoAxisDeployCommand extends AbstractDataModelOperation
{

  private JavaWSDLParameter javaWSDLParam;
  private ResourceBundle resource = ResourceBundle.getBundle("org.eclipse.jst.ws.axis.consumption.core.consumption"); //$NON-NLS-1$

  private String projectName_;
  private String componentName_;
  
  private static final String AXIS_SERVER_CONFIG_FILE = "axis.ServerConfigFile";
  
  IFolder outputRoot;
  
  /**
   * Constructor for GeronimoAxisDeployCommand.
   * @param String projectName
   * @param String componentName
   * 
   */
  public GeronimoAxisDeployCommand(String projectName, String componentName)
  {
  }
  
  public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable )
  {
	Environment environment = getEnvironment();
    if (javaWSDLParam == null)
    {
      return StatusUtils.errorStatus(getMessage("MSG_ERROR_JAVA_WSDL_PARAM_NOT_SET"));
    }

    if (javaWSDLParam.getProjectURL() == null || javaWSDLParam.getProjectURL().equals(""))
    { //$NON-NLS-1$
      return StatusUtils.errorStatus(getMessage("MSG_ERROR_PROJECT_URL_PARAM_NOT_SET"));
    }

    if (javaWSDLParam.getDeploymentFiles() == null || javaWSDLParam.getDeploymentFiles().length == 0)
    {
      return StatusUtils.errorStatus(
      getMessage("MSG_ERROR_DEPLOY_FILE_PARAM_NOT_SET"));
    }

    ProgressUtils.report(monitor, getMessage("MSG_AXIS_DEPLOY"));

    IStatus status = executeAdminTask();
    if (status.getSeverity() == Status.ERROR)
    {
        environment.getStatusHandler().reportError(status);
    }    
    
    status = executeAntTask();
    if (status.getSeverity() == Status.ERROR)
    {
        environment.getStatusHandler().reportError(status);
    }    
    return status;
  }
  
  protected IStatus executeAntTask()
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
    String url = javaWSDLParam.getProjectURL() + AxisDeployCommand.SERVICE_EXT;
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
      
//      Status[] childStatus = new Status[1];
//      childStatus[0] = new SimpleStatus("AxisDeployCommand", message, Status.ERROR);
//      return new SimpleStatus("AxisDeployCommand", //$NON-NLS-1$
//      getMessage("MSG_ERROR_AXIS_DEPLOY"), childStatus);
//    }
//    return new SimpleStatus("AxisDeployCommand", //$NON-NLS-1$
//    getMessage("MSG_AXIS_DEPLOY_OK"), Status.OK);
      IStatus[] childStatus = new Status[1];
      childStatus[0] = StatusUtils.errorStatus( message);
      return StatusUtils.multiStatus(getMessage("MSG_ERROR_AXIS_DEPLOY"), childStatus);
    }
    return Status.OK_STATUS; 

  }
  
  protected IStatus executeAdminTask(){
    
    IStatus status = Status.OK_STATUS;
    // check if server-config.wsdd exists
    IVirtualComponent component = J2EEUtils.getVirtualComponent( projectName_, componentName_ );
    outputRoot = J2EEUtils.getOutputContainerRoot( component );
    IPath path = new Path( "WEB-INF" ).append( "server-config.wsdd" );    
    IFile descriptorFile = outputRoot.getFile( path );
    
    if (!descriptorFile.exists()){
      status = createServerConfigFile();
      if (status.getSeverity()==Status.ERROR)
        return status;
    }
    
    // check if deploy.wsdd exists
    String deployWSDD = javaWSDLParam.getDeploymentFiles()[0];
    File deployFile = new File(deployWSDD);
    if (deployFile==null || !deployFile.exists()){
      return status;
    }
    
    try {
      // get Classpath
      String jarsCP = new String();
      // classes dir
      IPath classesPath = new Path("WEB-INF").append("classes");
      IFile classesDir = outputRoot.getFile(classesPath);
      jarsCP = "\""+classesDir.getRawLocation().toOSString()+"\"";
      
      // lib JARs
      IPath libPath = new Path("WEB-INF").append("lib");
      IFile libEntry = outputRoot.getFile(libPath);
      IFolder libFolder = (IFolder)ResourceUtils.findResource(libEntry.getFullPath());
      IResource[] JARfiles = libFolder.members();
      for (int i=0;i<JARfiles.length;i++){
        IResource res = JARfiles[i];
        if (res.getFileExtension().equals("jar")){
          jarsCP = jarsCP + ";\""+ res.getRawLocation().toOSString()+"\"";
        }
      }

      // form and run utils.Admin command
      String adminCommand = new String("java -Daxis.ServerConfigFile="+ descriptorFile.getRawLocation().toOSString() 
          +" -cp "+jarsCP+" org.apache.axis.utils.Admin server "+deployFile.getCanonicalPath());
      Runtime.getRuntime().exec(adminCommand);

    }
    catch(Exception e){
      System.setProperty(AXIS_SERVER_CONFIG_FILE,"server-conifg.wsdd");      
      e.printStackTrace();
      String message = e.getMessage();
      if (e.getCause() != null)
      {
        message = e.getCause().toString();
      }
      
      IStatus[] childStatus = new Status[1];
      childStatus[0] = StatusUtils.errorStatus( message);
      return StatusUtils.multiStatus(getMessage("MSG_ERROR_AXIS_DEPLOY"), childStatus);
    }
    
    return status;
  }
  
  /**t
   * Creates the initial server-config.wsdd file from a template in Axis
   * @return
   */
  private IStatus createServerConfigFile(){
    try{

      // server-config.wsdd file
      IPath             path           = new Path( "WEB-INF" ).append( "server-config.wsdd" );    
      IFile             descriptorFile = outputRoot.getFile( path );      

      // create the initial server-config.wsdd file
      AxisEngine preEngine = new AxisServer();
      InputStream is = ClassUtils.getResourceAsStream(preEngine.getClass(), "server-config.wsdd");
      FileOutputStream fos = new FileOutputStream(descriptorFile.getRawLocation().toOSString());
      ResourceUtils.copyStream(is, fos);
      fos.close();
      
      return Status.OK_STATUS;
    }
    catch(Exception e){
      e.printStackTrace();
      String message = e.getMessage();
      if (e.getCause() != null)
      {
        message = e.getCause().toString();
      }

      IStatus[] childStatus = new Status[1];
      childStatus[0] = StatusUtils.errorStatus( message);
      return StatusUtils.multiStatus(getMessage("MSG_ERROR_AXIS_DEPLOY"), childStatus);

    }
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
