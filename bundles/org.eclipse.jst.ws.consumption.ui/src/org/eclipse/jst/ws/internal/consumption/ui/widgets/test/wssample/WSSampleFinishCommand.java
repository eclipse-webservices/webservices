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

package org.eclipse.jst.ws.internal.consumption.ui.widgets.test.wssample;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jst.ws.internal.common.EnvironmentUtils;
import org.eclipse.jst.ws.internal.common.ResourceUtils;
import org.eclipse.jst.ws.internal.consumption.command.common.PublishProjectCommand;
import org.eclipse.jst.ws.internal.consumption.command.common.StartProjectCommand;
import org.eclipse.jst.ws.internal.consumption.sampleapp.codegen.InputFileGenerator;
import org.eclipse.jst.ws.internal.consumption.sampleapp.codegen.MethodFileGenerator;
import org.eclipse.jst.ws.internal.consumption.sampleapp.codegen.ResultFileGenerator;
import org.eclipse.jst.ws.internal.consumption.sampleapp.codegen.TestClientFileGenerator;
import org.eclipse.jst.ws.internal.consumption.sampleapp.command.GeneratePageCommand;
import org.eclipse.jst.ws.internal.consumption.sampleapp.command.JavaToModelCommand;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.test.CopyWebServiceUtilsJarCommand;
import org.eclipse.jst.ws.internal.datamodel.Model;
import org.eclipse.jst.ws.internal.ext.test.JavaProxyTestCommand;
import org.eclipse.wst.command.env.core.SimpleCommand;
import org.eclipse.wst.command.env.core.common.Environment;
import org.eclipse.wst.command.env.core.common.Log;
import org.eclipse.wst.command.env.core.common.MessageUtils;
import org.eclipse.wst.command.env.core.common.SimpleStatus;
import org.eclipse.wst.command.env.core.common.Status;
import org.eclipse.wst.command.env.core.common.StatusException;
import org.eclipse.wst.command.env.core.selection.BooleanSelection;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.webbrowser.WebBrowser;

public class WSSampleFinishCommand extends SimpleCommand implements JavaProxyTestCommand
{

  public static String INPUT       = "Input.jsp";
  public static String TEST_CLIENT = "TestClient.jsp";
  public static String RESULT      = "Result.jsp";
  public static String METHOD      = "Method.jsp";

  private static String LABEL = "JavaBeanToSampleActiveTask";
  private static String DESCRIPTION = "default actions";
  private MessageUtils msgUtils;
  
  private Model proxyModel;
  private String sampleServerTypeID;
  private IServer sampleExistingServer;
  
  
  private String clientProject;
  private String qname;
  private String jspFolder;
  private boolean runClientTest;
  private String sampleProject;
  private BooleanSelection[] methods;
  private String proxyBean;
  private String setEndpointMethod;
  private List endpoints;
  
  /**
  * Constructs a new JavaBeanToSampleActiveTask object with the given label and description.
  */
  public WSSampleFinishCommand ()
  {
	String pluginId = "org.eclipse.jst.ws.consumption.ui";
	msgUtils = new MessageUtils(pluginId + ".plugin", this);
	setDescription(DESCRIPTION);
	setName(LABEL);  	
  }

  public Status execute(Environment env)
  {
  	Status status = new SimpleStatus( "" );
    //setters and getters to be removed
  	CopyWebServiceUtilsJarCommand copy = new CopyWebServiceUtilsJarCommand();    
  	copy.setSampleProject(sampleProject);
  	status = copy.execute(env);
    if (status.getSeverity() == Status.ERROR) return status;
    status = createModel(env);
    if (status.getSeverity() == Status.ERROR) return status;
    status = generatePages(env);
    if (status.getSeverity() == Status.ERROR) return status;
    //if (!isSuccessful()) return;
    status = launchSample(env);
    return status;   
  }

   
  /**
  * Generate the four jsps that make up this
  * sample app.
  */
  protected Status generatePages(Environment env)
  {
  	Status status = new SimpleStatus( "" );
    IPath fDestinationFolderPath = new Path(jspFolder);
    fDestinationFolderPath = fDestinationFolderPath.makeAbsolute();    
    IWorkspaceRoot fWorkspace = ResourcesPlugin.getWorkspace().getRoot();

    IPath pathTest = fDestinationFolderPath.append(TEST_CLIENT);
    IFile fileTest = fWorkspace.getFile(pathTest);
    GeneratePageCommand gpcTest = new GeneratePageCommand(EnvironmentUtils.getResourceContext(env), proxyModel,
      new TestClientFileGenerator(INPUT,METHOD,RESULT),fileTest);
    //gpcTest.setStatusMonitor(getStatusMonitor());
    status = gpcTest.execute(env);
    if (status.getSeverity() == Status.ERROR )
    	return status;
    

    //input codegen
    IPath pathInput = fDestinationFolderPath.append(INPUT);
    IFile fileInput = fWorkspace.getFile(pathInput);
    InputFileGenerator inputGenerator = new InputFileGenerator(RESULT);
    GeneratePageCommand gpcInput = new GeneratePageCommand(EnvironmentUtils.getResourceContext(env), proxyModel,
      inputGenerator,fileInput);
    //gpcInput.setStatusMonitor(getStatusMonitor());
    status = gpcInput.execute(env);
    if (status.getSeverity() == Status.ERROR )
    	return status;

    //method codegen
    IPath pathMethod = fDestinationFolderPath.append(METHOD);
    IFile fileMethod = fWorkspace.getFile(pathMethod);
    MethodFileGenerator methodGenerator = new MethodFileGenerator(INPUT);
    methodGenerator.setClientFolderPath(jspFolder);
    GeneratePageCommand gpcMethod = new GeneratePageCommand(EnvironmentUtils.getResourceContext(env), proxyModel,
      methodGenerator,fileMethod);
    //gpcMethod.setStatusMonitor(getStatusMonitor());
    status = gpcMethod.execute(env);
    if (status.getSeverity() == Status.ERROR )
    	return status;    


    //result codegen
    IPath pathResult = fDestinationFolderPath.append(RESULT);
    IFile fileResult = fWorkspace.getFile(pathResult);
    ResultFileGenerator rfg = new ResultFileGenerator();
    rfg.setClientFolderPath(jspFolder);
    rfg.setSetEndpointMethod(setEndpointMethod);
    GeneratePageCommand gpcResult = new GeneratePageCommand(EnvironmentUtils.getResourceContext(env), proxyModel,
      rfg,fileResult);
    //gpcResult.setStatusMonitor(getStatusMonitor());
    status = gpcResult.execute(env);
    
    return status;
  }


  protected Status launchSample (Environment env) {

  	Status status = new SimpleStatus( "" );
    if (!runClientTest) return status;
    

    IPath fDestinationFolderPath = new Path(jspFolder);
    fDestinationFolderPath = fDestinationFolderPath.makeAbsolute();    

    PublishProjectCommand ppc = new PublishProjectCommand();
    ppc.setServerTypeID(sampleServerTypeID);
    ppc.setExistingServer(sampleExistingServer);
    ppc.setProject(sampleProject);
    status = ppc.execute(env);

    StartProjectCommand spc = new StartProjectCommand(false );
    spc.setServiceServerTypeID(sampleServerTypeID);
    spc.setSampleExistingServer(sampleExistingServer);
    IProject project = (IProject) ResourceUtils.findResource(sampleProject);
    spc.setSampleProject(project);
    spc.setIsWebProjectStartupRequested(true);
    
    status = spc.execute(env);
    if (status.getSeverity() == Status.ERROR) return status;
    
    IPath newPath = new Path(ResourceUtils.getWebProjectURL(ResourceUtils.getProjectOf(fDestinationFolderPath),sampleServerTypeID,sampleExistingServer));
    newPath = newPath.append(fDestinationFolderPath.removeFirstSegments(2).makeAbsolute());
    StringBuffer urlString = new StringBuffer(newPath.append(TEST_CLIENT).toString());
    if (endpoints != null && !endpoints.isEmpty())
    {
      urlString.append("?endpoint=");
      urlString.append(endpoints.get(0).toString());
    }
    
    try{
      URL url;
      url = new URL(urlString.toString());
      int style = WebBrowser.SHOW_TOOLBAR | WebBrowser.SHOW_STATUSBAR | WebBrowser.FORCE_NEW_PAGE;  

      for( int retries = 0; retries < 10; retries++ )
      {
        try
        {
          // Test the URLs
          (new URL(newPath.append(RESULT).toString())).openStream();
          (new URL(newPath.append(METHOD).toString())).openStream();
          (new URL(newPath.append(INPUT).toString())).openStream();
          (new URL(newPath.append(TEST_CLIENT).toString())).openStream();
          // Looks good, exit loop
          break;
        }
        catch( IOException ioe )
        {
          try
          {
            Thread.sleep(1000);
          }
          catch (InterruptedException ie) {} 	  	          
        }
      }

      WebBrowser.openURL(url,style,null);
      return status;  
    }catch(MalformedURLException exc){
    	env.getLog().log(Log.WARNING, 5048, this, "launchSample", exc);
		status = new SimpleStatus( "launchSample", msgUtils.getMessage("MSG_ERROR_MALFORMED_URL"), Status.WARNING );
		try {
			env.getStatusHandler().report(status);
		} catch (StatusException e) {
			status = new SimpleStatus( "launchSample", msgUtils.getMessage("MSG_ERROR_MALFORMED_URL"), Status.ERROR );
		}
    	return status;
    }
  }

  

  protected Status createModel(Environment env) {

  	//create the model from the resource
    JavaToModelCommand jtmc = new JavaToModelCommand();
    jtmc.setMethods(methods);
    jtmc.setClientProject(clientProject);
    jtmc.setProxyBean(proxyBean);
    //jtmc.setStatusMonitor(getStatusMonitor());
    Status status = jtmc.execute(env);
    if (status.getSeverity() == Status.ERROR) return status;

    proxyModel = jtmc.getDataModel();
    return status;
  }

  public void setServerTypeID(String sampleServerTypeID)
  {
  	this.sampleServerTypeID = sampleServerTypeID;
  }
  
  public void setExistingServer(IServer sampleExistingServer)
  {
  	this.sampleExistingServer = sampleExistingServer;
  }
  
  public void setJspFolder(String jspFolder)
  {
    this.jspFolder = jspFolder;
  }
  
  public void setRunClientTest(boolean runClientTest)
  {
  	this.runClientTest = runClientTest;
  }
  
  public void setSampleProject(String sampleProject)
  {
  	this.sampleProject = sampleProject;
  }

  public void setClientProject(String clientProject)
  {
  	this.clientProject = clientProject;
  }
  
  public void setMethods(BooleanSelection[] methods)
  {
  	this.methods = methods;
  }

  public void setProxyBean(String proxyBean)
  {
  	this.proxyBean = proxyBean;
  }

  /**
   * @param setEndpointMethod The setEndpointMethod to set.
   */
  public void setSetEndpointMethod(String setEndpointMethod)
  {
    this.setEndpointMethod = setEndpointMethod;
  }
  
  public void setEndpoint(List endpoints)
  {
    this.endpoints = endpoints;
  }
}
