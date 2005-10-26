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
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.ws.internal.common.ResourceUtils;
import org.eclipse.jst.ws.internal.consumption.command.common.PublishProjectCommand;
import org.eclipse.jst.ws.internal.consumption.command.common.StartProjectCommand;
import org.eclipse.jst.ws.internal.consumption.sampleapp.codegen.InputFileGenerator;
import org.eclipse.jst.ws.internal.consumption.sampleapp.codegen.MethodFileGenerator;
import org.eclipse.jst.ws.internal.consumption.sampleapp.codegen.ResultFileGenerator;
import org.eclipse.jst.ws.internal.consumption.sampleapp.codegen.TestClientFileGenerator;
import org.eclipse.jst.ws.internal.consumption.sampleapp.command.GeneratePageCommand;
import org.eclipse.jst.ws.internal.consumption.sampleapp.command.JavaToModelCommand;
import org.eclipse.jst.ws.internal.consumption.ui.plugin.WebServiceConsumptionUIPlugin;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.test.CopyWebServiceUtilsJarCommand;
import org.eclipse.jst.ws.internal.ext.test.JavaProxyTestCommand;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.browser.IWebBrowser;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;
import org.eclipse.wst.command.internal.env.core.common.MessageUtils;
import org.eclipse.wst.command.internal.env.core.common.StatusUtils;
import org.eclipse.wst.command.internal.env.core.selection.BooleanSelection;
import org.eclipse.wst.common.environment.IEnvironment;
import org.eclipse.wst.common.environment.ILog;
import org.eclipse.wst.common.environment.StatusException;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.ws.internal.common.EnvironmentUtils;
import org.eclipse.wst.ws.internal.datamodel.Model;

public class WSSampleFinishCommand extends AbstractDataModelOperation implements JavaProxyTestCommand
{

  public static String INPUT       = "Input.jsp";
  public static String TEST_CLIENT = "TestClient.jsp";
  public static String RESULT      = "Result.jsp";
  public static String METHOD      = "Method.jsp";

  private MessageUtils msgUtils;
  
  private Model proxyModel;
  private String sampleServerTypeID;
  private IServer sampleExistingServer;
  
  
  private String clientProject;
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
  }

  public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable )
  {
    IEnvironment env = getEnvironment();
    
  	IStatus status = Status.OK_STATUS;
    //setters and getters to be removed
  	CopyWebServiceUtilsJarCommand copy = new CopyWebServiceUtilsJarCommand();    
  	copy.setSampleProject(sampleProject);
    copy.setEnvironment( env );
  	status = copy.execute( monitor, null );
    if (status.getSeverity() == Status.ERROR) return status;
    status = createModel(env, monitor );
    if (status.getSeverity() == Status.ERROR) return status;
    status = generatePages(env, monitor );
    if (status.getSeverity() == Status.ERROR) return status;
    //if (!isSuccessful()) return;
    status = launchSample(env, monitor);
    return status;   
  }

   
  /**
  * Generate the four jsps that make up this
  * sample app.
  */
  protected IStatus generatePages(IEnvironment env, IProgressMonitor monitor )
  {
  	IStatus status = Status.OK_STATUS;
    IPath fDestinationFolderPath = new Path(jspFolder);
    fDestinationFolderPath = fDestinationFolderPath.makeAbsolute();    
    IWorkspaceRoot fWorkspace = ResourcesPlugin.getWorkspace().getRoot();

    IPath pathTest = fDestinationFolderPath.append(TEST_CLIENT);
    IFile fileTest = fWorkspace.getFile(pathTest);
    GeneratePageCommand gpcTest = new GeneratePageCommand(EnvironmentUtils.getResourceContext(env), proxyModel,
      new TestClientFileGenerator(INPUT,METHOD,RESULT),fileTest);
    //gpcTest.setStatusMonitor(getStatusMonitor());
    gpcTest.setEnvironment( env );
    status = gpcTest.execute( monitor, null );
    if (status.getSeverity() == Status.ERROR )
    	return status;
    

    //input codegen
    IPath pathInput = fDestinationFolderPath.append(INPUT);
    IFile fileInput = fWorkspace.getFile(pathInput);
    InputFileGenerator inputGenerator = new InputFileGenerator(RESULT);
    GeneratePageCommand gpcInput = new GeneratePageCommand(EnvironmentUtils.getResourceContext(env), proxyModel,
      inputGenerator,fileInput);
    //gpcInput.setStatusMonitor(getStatusMonitor());
    gpcInput.setEnvironment( env );
    status = gpcInput.execute( monitor, null );
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
    gpcMethod.setEnvironment( env );
    status = gpcMethod.execute( monitor, null );
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
    gpcResult.setEnvironment( env );
    status = gpcResult.execute( monitor, null );
    
    return status;
  }


  protected IStatus launchSample (IEnvironment env, IProgressMonitor monitor ) {

  	IStatus status = Status.OK_STATUS;
    if (!runClientTest) return status;
    

    IPath fDestinationFolderPath = new Path(jspFolder);
    fDestinationFolderPath = fDestinationFolderPath.makeAbsolute();    

    PublishProjectCommand ppc = new PublishProjectCommand();
    ppc.setServerTypeID(sampleServerTypeID);
    ppc.setExistingServer(sampleExistingServer);
    ppc.setProject(sampleProject);
    ppc.setEnvironment( env );
    status = ppc.execute( monitor, null );

    StartProjectCommand spc = new StartProjectCommand(false );
    spc.setServiceServerTypeID(sampleServerTypeID);
    spc.setSampleExistingServer(sampleExistingServer);
    IProject project = (IProject) ResourceUtils.findResource(sampleProject);
    spc.setSampleProject(project);
    spc.setIsWebProjectStartupRequested(true);
    spc.setEnvironment( env );
    
    status = spc.execute( monitor, null );
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

		IWorkbenchBrowserSupport browserSupport = WebServiceConsumptionUIPlugin.getInstance().getWorkbench().getBrowserSupport();
		IWebBrowser browser = browserSupport.createBrowser(IWorkbenchBrowserSupport.LOCATION_BAR | IWorkbenchBrowserSupport.NAVIGATION_BAR, null, null, null);
		browser.openURL(url);
      return status;
	 }catch(PartInitException exc){
		//TODO: change error message
		env.getLog().log(ILog.WARNING, 5048, this, "launchSample", exc);
		status = StatusUtils.warningStatus( msgUtils.getMessage("MSG_ERROR_MALFORMED_URL"), exc );
		try {
			env.getStatusHandler().report(status);
		} catch (StatusException e) {
			status = StatusUtils.errorStatus( msgUtils.getMessage("MSG_ERROR_MALFORMED_URL"), e );
		}
    	return status;
    }catch(MalformedURLException exc){
    	env.getLog().log(ILog.WARNING, 5048, this, "launchSample", exc);
		status = StatusUtils.warningStatus( msgUtils.getMessage("MSG_ERROR_MALFORMED_URL"), exc );
		try {
			env.getStatusHandler().report(status);
		} catch (StatusException e) {
			status = StatusUtils.errorStatus( msgUtils.getMessage("MSG_ERROR_MALFORMED_URL"), e );
		}
    	return status;
    }
  }

  

  protected IStatus createModel(IEnvironment env, IProgressMonitor monitor ) {

  	//create the model from the resource
    JavaToModelCommand jtmc = new JavaToModelCommand();
    jtmc.setMethods(methods);
    jtmc.setClientProject(clientProject);
    jtmc.setProxyBean(proxyBean);
    jtmc.setEnvironment( env );
    //jtmc.setStatusMonitor(getStatusMonitor());
    IStatus status = jtmc.execute( monitor, null);
    if (status.getSeverity() == Status.ERROR) return status;

    proxyModel = jtmc.getJavaDataModel();
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
