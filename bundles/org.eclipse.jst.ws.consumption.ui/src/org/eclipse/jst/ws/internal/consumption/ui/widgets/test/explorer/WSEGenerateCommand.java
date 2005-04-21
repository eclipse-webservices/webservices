package org.eclipse.jst.ws.internal.consumption.ui.widgets.test.explorer;

import java.util.Iterator;
import java.util.Vector;

import org.eclipse.core.resources.IProject;
import org.eclipse.jem.util.emf.workbench.ProjectUtilities;
import org.eclipse.jst.ws.internal.common.ResourceUtils;
import org.eclipse.jst.ws.internal.consumption.command.common.StartProjectCommand;
import org.eclipse.wst.command.internal.provisional.env.core.SimpleCommand;
import org.eclipse.wst.command.internal.provisional.env.core.common.Environment;
import org.eclipse.wst.command.internal.provisional.env.core.common.SimpleStatus;
import org.eclipse.wst.command.internal.provisional.env.core.common.Status;
import org.eclipse.wst.ws.internal.datamodel.Model;
import org.eclipse.wst.ws.internal.explorer.LaunchOption;
import org.eclipse.wst.ws.internal.explorer.LaunchOptions;
import org.eclipse.wst.ws.internal.explorer.WSExplorerLauncherCommand;
import org.eclipse.wst.ws.internal.explorer.plugin.ExplorerPlugin;
import org.eclipse.wst.ws.internal.provisional.wsrt.TestInfo;

public class WSEGenerateCommand extends SimpleCommand {

  private TestInfo testInfo;
  private Model proxyModel;
	  
  public WSEGenerateCommand(TestInfo testInfo){
    this.testInfo = testInfo;
  }

  public Status execute(Environment env)
  {
  	Status status = new SimpleStatus( "" );
    
    StartProjectCommand spc = new StartProjectCommand( true );
    spc.setServiceServerTypeID(testInfo.getServiceServerTypeID());
    spc.setServiceExistingServer(testInfo.getServiceExistingServer());
    IProject project = (IProject) ProjectUtilities.getProject(testInfo.getServiceProject());
    spc.setServiceProject(project);
    spc.setIsWebProjectStartupRequested(true);
    
    status = spc.execute(env);
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
    status = launchCommand.execute(env);
    return status;
  }
}
