package org.eclipse.jst.ws.internal.consumption.ui.widgets.test.explorer;

import java.util.Iterator;
import java.util.Vector;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jem.util.emf.workbench.ProjectUtilities;
import org.eclipse.jst.ws.internal.common.EnvironmentUtils;
import org.eclipse.jst.ws.internal.consumption.command.common.StartProjectCommand;
import org.eclipse.wst.command.internal.provisional.env.core.EnvironmentalOperation;
import org.eclipse.wst.command.internal.provisional.env.core.common.Environment;
import org.eclipse.wst.command.internal.provisional.env.core.common.SimpleStatus;
import org.eclipse.wst.command.internal.provisional.env.core.common.Status;
import org.eclipse.wst.ws.internal.explorer.LaunchOption;
import org.eclipse.wst.ws.internal.explorer.LaunchOptions;
import org.eclipse.wst.ws.internal.explorer.WSExplorerLauncherCommand;
import org.eclipse.wst.ws.internal.explorer.plugin.ExplorerPlugin;
import org.eclipse.wst.ws.internal.provisional.wsrt.TestInfo;

public class WSEGenerateCommand extends EnvironmentalOperation 
{

  private TestInfo testInfo;
	  
  public WSEGenerateCommand(TestInfo testInfo){
    this.testInfo = testInfo;
  }

  public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable )
  {
    Environment env = getEnvironment();
    
  	Status status = new SimpleStatus( "" );
    
    StartProjectCommand spc = new StartProjectCommand( true );
    spc.setServiceServerTypeID(testInfo.getServiceServerTypeID());
    spc.setServiceExistingServer(testInfo.getServiceExistingServer());
    IProject project = (IProject) ProjectUtilities.getProject(testInfo.getServiceProject());
    spc.setServiceProject(project);
    spc.setIsWebProjectStartupRequested(true);
    spc.setEnvironment( env );
    
    status = EnvironmentUtils.convertIStatusToStatus(spc.execute( null, null ) );
    if (status.getSeverity() == Status.ERROR)
    	return status;

    WSExplorerLauncherCommand launchCommand = new WSExplorerLauncherCommand();
    launchCommand.setForceLaunchOutsideIDE(false);
    Vector launchOptionVector = new Vector();
	String stateLocation = ExplorerPlugin.getInstance().getPluginStateLocation();
	String defaultFavoritesLocation = ExplorerPlugin.getInstance().getDefaultFavoritesLocation();
	launchOptionVector.add(new LaunchOption(LaunchOptions.STATE_LOCATION,stateLocation));
	launchOptionVector.add(new LaunchOption(LaunchOptions.DEFAULT_FAVORITES_LOCATION,defaultFavoritesLocation));
    launchOptionVector.add(new LaunchOption(LaunchOptions.WSDL_URL,testInfo.getWsdlServiceURL()));
    if (testInfo.getEndpoint() != null)
      for (Iterator it = testInfo.getEndpoint().iterator(); it.hasNext();)
        launchOptionVector.add(new LaunchOption(LaunchOptions.WEB_SERVICE_ENDPOINT, it.next().toString()));
    launchCommand.setLaunchOptions((LaunchOption[])launchOptionVector.toArray(new LaunchOption[0]));
    launchCommand.setEnvironment( env );
    status = EnvironmentUtils.convertIStatusToStatus(launchCommand.execute( null, null ));
    return status;
  }
}
