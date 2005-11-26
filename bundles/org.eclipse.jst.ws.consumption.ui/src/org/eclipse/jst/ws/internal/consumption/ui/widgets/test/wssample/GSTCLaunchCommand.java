/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
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
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jem.util.emf.workbench.ProjectUtilities;
import org.eclipse.jst.ws.internal.common.J2EEUtils;
import org.eclipse.jst.ws.internal.common.ServerUtils;
import org.eclipse.jst.ws.internal.consumption.command.common.PublishProjectCommand;
import org.eclipse.jst.ws.internal.consumption.ui.ConsumptionUIMessages;
import org.eclipse.jst.ws.internal.consumption.ui.command.StartServerCommand;
import org.eclipse.jst.ws.internal.consumption.ui.plugin.WebServiceConsumptionUIPlugin;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.browser.IWebBrowser;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;
import org.eclipse.wst.command.internal.env.core.common.StatusUtils;
import org.eclipse.wst.common.environment.IEnvironment;
import org.eclipse.wst.common.environment.ILog;
import org.eclipse.wst.common.environment.StatusException;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;
import org.eclipse.wst.ws.internal.wsrt.TestInfo;

public class GSTCLaunchCommand extends AbstractDataModelOperation
{

  public static String INPUT       = "Input.jsp";
  public static String TEST_CLIENT = "TestClient.jsp";
  public static String RESULT      = "Result.jsp";
  public static String METHOD      = "Method.jsp";
		
  private TestInfo testInfo;
  private String jspfolder;
  
  public GSTCLaunchCommand(TestInfo testInfo){
    this.testInfo = testInfo;
  }
		
  public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable )
  {    
    IEnvironment env = getEnvironment();
    
    setJSPFolder();
    
	  return launchSample(env, monitor);
  }
  private void setJSPFolder(){
	    //if the client is not a webcomponent then the 
		//sample must have been created, we must now factor in 
		//flexible projects  
		  
		IProject clientIProject = ProjectUtilities.getProject(testInfo.getClientProject());
	    if (clientIProject != null && !J2EEUtils.isWebComponent(clientIProject)){   
		  IProject project = ProjectUtilities.getProject(testInfo.getGenerationProject());
		  IPath path = J2EEUtils.getWebContentPath(project);
		  int index = testInfo.getJspFolder().lastIndexOf("/");
		  String jsp = testInfo.getJspFolder().substring(index + 1);
		  StringBuffer sb = new StringBuffer();	
		  sb.append("/").append(path.toString()).append("/").append(jsp);	  
		  jspfolder = sb.toString();
		} 
	    else
		  jspfolder = testInfo.getJspFolder();	
	  
	  
	  }
  
  private IStatus launchSample (IEnvironment env, IProgressMonitor monitor ) {
    IStatus status = Status.OK_STATUS;
	  IPath fDestinationFolderPath = new Path(jspfolder);
	  fDestinationFolderPath = fDestinationFolderPath.makeAbsolute();    
    PublishProjectCommand ppc = new PublishProjectCommand();
    ppc.setServerTypeID(testInfo.getClientServerTypeID());
    ppc.setExistingServer(testInfo.getClientExistingServer());
    ppc.setProject(testInfo.getGenerationProject());
    ppc.setEnvironment( env );
	status = ppc.execute( monitor, null );

	StartServerCommand serverCommand = new StartServerCommand( true, true );
	serverCommand.setServerInstanceId( testInfo.getClientExistingServer().getId() );
  serverCommand.setEnvironment( env );
	
	status = serverCommand.execute(monitor, null);
	if (status.getSeverity() == Status.ERROR) return status;
	
	IProject sampleProject = ProjectUtilities.getProject(testInfo.getGenerationProject());
	String   newPath = ServerUtils.getWebComponentURL(sampleProject, testInfo.getClientServerTypeID(),testInfo.getClientExistingServer());
	int count = J2EEUtils.getWebContentPath(sampleProject).segmentCount();
	
	newPath = newPath + "/" + fDestinationFolderPath.removeFirstSegments(count);
	StringBuffer urlString = new StringBuffer( newPath + "/" + TEST_CLIENT );
	if (testInfo.getEndpoint() != null && !testInfo.getEndpoint().isEmpty())
	{
	  urlString.append("?endpoint=");
	  urlString.append(testInfo.getEndpoint().get(0).toString());
	}
	    
	try{
	      URL url;
	      url = new URL(urlString.toString());
        
        InputStream resultStream = null;
        InputStream methodStream = null;
        InputStream inputStream  = null;
        InputStream clientStream = null;

	      for( int retries = 0; retries < 10; retries++ )
	      {
	        try
	        {
	          // Test the URLs
            
	          resultStream = new URL(newPath + "/" + RESULT).openStream();
	          methodStream = new URL(newPath + "/" + METHOD).openStream();
	          inputStream  = new URL(newPath + "/" + INPUT).openStream();
	          clientStream = new URL(newPath + "/" + TEST_CLIENT).openStream();
            
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
          finally
          {
            try
            {
              if( resultStream != null ) resultStream.close();
              if( methodStream != null ) methodStream.close();
              if( inputStream != null ) inputStream.close();
              if( clientStream != null ) clientStream.close();
            }
            catch( IOException exc )
            {
            }
          }
	      }

			IWorkbenchBrowserSupport browserSupport = WebServiceConsumptionUIPlugin.getInstance().getWorkbench().getBrowserSupport();
			IWebBrowser browser = browserSupport.createBrowser(IWorkbenchBrowserSupport.LOCATION_BAR, null, null, null);
			browser.openURL(url);
	      return status;
		 }catch(PartInitException exc){
			//TODO: change error message
			env.getLog().log(ILog.WARNING, 5048, this, "launchSample", exc);
			status = StatusUtils.warningStatus( ConsumptionUIMessages.MSG_ERROR_MALFORMED_URL );
			try {
				env.getStatusHandler().report(status);
			} catch (StatusException e) {
				status = StatusUtils.errorStatus( ConsumptionUIMessages.MSG_ERROR_MALFORMED_URL, e );
			}
	    	return status;
	    }catch(MalformedURLException exc){
	    	env.getLog().log(ILog.WARNING, 5048, this, "launchSample", exc);
			status = StatusUtils.warningStatus( ConsumptionUIMessages.MSG_ERROR_MALFORMED_URL, exc );
			try {
				env.getStatusHandler().report(status);
			} catch (StatusException e) {
				status = StatusUtils.errorStatus( ConsumptionUIMessages.MSG_ERROR_MALFORMED_URL, e );
			}
	    	return status;
	    }
     }
}