package org.eclipse.jst.ws.internal.consumption.ui.widgets.test.wssample;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jem.util.emf.workbench.ProjectUtilities;
import org.eclipse.jst.ws.internal.common.J2EEUtils;
import org.eclipse.jst.ws.internal.common.ServerUtils;
import org.eclipse.jst.ws.internal.consumption.command.common.PublishProjectCommand;
import org.eclipse.jst.ws.internal.consumption.command.common.StartServerCommand;
import org.eclipse.jst.ws.internal.consumption.ui.plugin.WebServiceConsumptionUIPlugin;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.browser.IWebBrowser;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;
import org.eclipse.wst.command.internal.provisional.env.core.SimpleCommand;
import org.eclipse.wst.command.internal.provisional.env.core.common.Environment;
import org.eclipse.wst.command.internal.provisional.env.core.common.Log;
import org.eclipse.wst.command.internal.provisional.env.core.common.MessageUtils;
import org.eclipse.wst.command.internal.provisional.env.core.common.SimpleStatus;
import org.eclipse.wst.command.internal.provisional.env.core.common.Status;
import org.eclipse.wst.command.internal.provisional.env.core.common.StatusException;
import org.eclipse.wst.ws.internal.provisional.wsrt.TestInfo;

public class GSTCLaunchCommand extends SimpleCommand {

  public static String INPUT       = "Input.jsp";
  public static String TEST_CLIENT = "TestClient.jsp";
  public static String RESULT      = "Result.jsp";
  public static String METHOD      = "Method.jsp";
		
  private TestInfo testInfo;
  private MessageUtils msgUtils;
  
  public GSTCLaunchCommand(TestInfo testInfo){
    this.testInfo = testInfo;
	String pluginId = "org.eclipse.jst.ws.consumption.ui";
	msgUtils = new MessageUtils(pluginId + ".plugin", this);
  }
		
  public Status execute(Environment env)
  {
	return launchSample(env);
  }
  
  private Status launchSample (Environment env) {
    Status status = new SimpleStatus( "" );
	IPath fDestinationFolderPath = new Path(testInfo.getJspFolder());
	fDestinationFolderPath = fDestinationFolderPath.makeAbsolute();    
    PublishProjectCommand ppc = new PublishProjectCommand();
    ppc.setServerTypeID(testInfo.getClientServerTypeID());
    ppc.setExistingServer(testInfo.getClientExistingServer());
    ppc.setProject(testInfo.getGenerationProject());
	status = ppc.execute(env);

	StartServerCommand serverCommand = new StartServerCommand( true );
	serverCommand.setServerInstanceId( testInfo.getClientExistingServer().getId() );
	
	status = serverCommand.execute(env);
	if (status.getSeverity() == Status.ERROR) return status;
	
	IProject sampleProject = ProjectUtilities.getProject(testInfo.getGenerationProject());
	IPath newPath = new Path(ServerUtils.getWebComponentURL(sampleProject,testInfo.getGenerationModule(),testInfo.getClientServerTypeID(),testInfo.getClientExistingServer()));
	int count = J2EEUtils.getWebContentPath(sampleProject,testInfo.getGenerationModule()).segmentCount();
	
	newPath = newPath.append(fDestinationFolderPath.removeFirstSegments(count).makeAbsolute());
	StringBuffer urlString = new StringBuffer(newPath.append(TEST_CLIENT).toString());
	if (testInfo.getEndpoint() != null && !testInfo.getEndpoint().isEmpty())
	{
	  urlString.append("?endpoint=");
	  urlString.append(testInfo.getEndpoint().get(0).toString());
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
			IWebBrowser browser = browserSupport.createBrowser(IWorkbenchBrowserSupport.LOCATION_BAR, null, null, null);
			browser.openURL(url);
	      return status;
		 }catch(PartInitException exc){
			//TODO: change error message
			env.getLog().log(Log.WARNING, 5048, this, "launchSample", exc);
			status = new SimpleStatus( "launchSample", msgUtils.getMessage("MSG_ERROR_MALFORMED_URL"), Status.WARNING );
			try {
				env.getStatusHandler().report(status);
			} catch (StatusException e) {
				status = new SimpleStatus( "launchSample", msgUtils.getMessage("MSG_ERROR_MALFORMED_URL"), Status.ERROR );
			}
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
}