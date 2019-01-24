/*******************************************************************************
 * Copyright (c) 2005, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060418   134322 rsinha@ca.ibm.com - Rupam Kuehner
 * 20060424   124368 kathy@ca.ibm.com - Kathy Chan
 * 20090312	  250984 mahutch@ca.ibm.com - Mark Hutchinson, Use another mechanism to wait for build to be completed
 * 20091201   296529 yenlu@ca.ibm.com - Yen Lu, Determination of when the sample JSP goes live is not robust.
 *******************************************************************************/

package org.eclipse.jst.ws.internal.consumption.ui.widgets.test.wssample;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.core.commands.ExecutionException;
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
import org.eclipse.jst.ws.internal.consumption.command.common.BuildBeanCommand;
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

	IProject sampleProject = ProjectUtilities.getProject(testInfo.getGenerationProject());
	
	BuildBeanCommand buildCommand = new BuildBeanCommand();
	buildCommand.setEnvironment(env);
	buildCommand.setProject(sampleProject);
	try {
		//ensure the sample project is built
		buildCommand.execute(monitor, null);
	} catch (ExecutionException e2) {
		//just ignore and see if we can move on
	}
	 
	StartServerCommand serverCommand = new StartServerCommand( true );
	serverCommand.setServerInstanceId( testInfo.getClientExistingServer().getId() );
	serverCommand.setEnvironment( env );
	
	status = serverCommand.execute(monitor, null);
	if (status.getSeverity() == Status.ERROR) return status;
	
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
        
	      for( int retries = 0; retries < 20; retries++ )
	      {
	        try
	        {
	          // Test the URLs
	          URL sampleUrl = new URL(newPath + "/" + RESULT);
	          HttpURLConnection urlConn = (HttpURLConnection)sampleUrl.openConnection();
	          int responseCode = urlConn.getResponseCode();
	          //System.out.println(retries + ": result HTTP code = " + responseCode);
	          //dumpUrlContents(urlConn);	          
	          if (responseCode != HttpURLConnection.HTTP_OK)
	        	  throw new IOException();
	          
	          sampleUrl = new URL(newPath + "/" + METHOD);
	          urlConn = (HttpURLConnection)sampleUrl.openConnection();
	          responseCode = urlConn.getResponseCode();
	          //System.out.println(retries + ": method HTTP code = " + responseCode);
	          //dumpUrlContents(urlConn);	          
	          if (responseCode != HttpURLConnection.HTTP_OK)
	        	  throw new IOException();
	          
	          sampleUrl = new URL(newPath + "/" + INPUT);
	          urlConn = (HttpURLConnection)sampleUrl.openConnection();
	          responseCode = urlConn.getResponseCode();	          
	          //System.out.println(retries + ": input HTTP code = " + responseCode);
	          //dumpUrlContents(urlConn);	          
	          if (responseCode != HttpURLConnection.HTTP_OK)
	        	  throw new IOException();	
	          
	          sampleUrl = new URL(newPath + "/" + TEST_CLIENT);
	          urlConn = (HttpURLConnection)sampleUrl.openConnection();
	          responseCode = urlConn.getResponseCode();	          
	          //System.out.println(retries + ": test client HTTP code = " + responseCode);
	          //dumpUrlContents(urlConn);	          
	          if (responseCode != HttpURLConnection.HTTP_OK)
	        	  throw new IOException();	    
            
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
			IWebBrowser browser = browserSupport.createBrowser(IWorkbenchBrowserSupport.LOCATION_BAR, null, null, null);
			browser.openURL(url);
	      return status;
		 }catch(PartInitException exc){
			env.getLog().log(ILog.ERROR, 5048, this, "launchSample", exc);
			status = StatusUtils.errorStatus(exc.getMessage(), exc);
			env.getStatusHandler().reportError(status);
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
/*  
     private void dumpUrlContents(URLConnection conn)
     {    	 
    	 InputStream is = null;
    	 try
    	 {
    		 is = conn.getInputStream();
    		 BufferedReader br = new BufferedReader(new InputStreamReader(is));
    		 String s = null;
    		 while ((s = br.readLine()) != null)
    			 System.out.println(s);
    	 }
    	 catch (IOException e)
    	 {    		 
    	 }
    	 finally
    	 {
    		 if (is != null)
    		 {
    			 try
    			 {
    			   is.close();
    			 }
    			 catch (IOException e)
    			 {    				 
    			 }
    		 }
    	 }
     }
*/     
}
