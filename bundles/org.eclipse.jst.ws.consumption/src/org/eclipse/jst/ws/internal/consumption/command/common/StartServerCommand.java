package org.eclipse.jst.ws.internal.consumption.command.common;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jst.ws.internal.common.EnvironmentUtils;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.command.internal.env.eclipse.EclipseLog;
import org.eclipse.wst.command.internal.provisional.env.core.SimpleCommand;
import org.eclipse.wst.command.internal.provisional.env.core.common.Environment;
import org.eclipse.wst.command.internal.provisional.env.core.common.Log;
import org.eclipse.wst.command.internal.provisional.env.core.common.MessageUtils;
import org.eclipse.wst.command.internal.provisional.env.core.common.SimpleStatus;
import org.eclipse.wst.command.internal.provisional.env.core.common.Status;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.ServerCore;

/**
 * (Re)Starts and publishes the server specifed by the serverInstanceId attribute.
 * 
 *
 */
public class StartServerCommand extends SimpleCommand
{
  private java.lang.String DESCRIPTION = "(Re)Start and publish the server";
  private java.lang.String LABEL = "StartServerCommand";
  private MessageUtils msgUtils_;
  private IProgressMonitor monitor;
  private Log log;
  private boolean forcePublish_;
  private boolean doAsyncPublish_;
  
	private String serverInstanceId;
	
  
	public StartServerCommand()
  {
    String pluginId = "org.eclipse.jst.ws.consumption";
    msgUtils_ = new MessageUtils(pluginId + ".plugin", this);
    setDescription(DESCRIPTION);
    setName(LABEL);
    log             = new EclipseLog();
    forcePublish_   = false;
    doAsyncPublish_ = true;
  }
	 
  public StartServerCommand( boolean forcePublish, boolean doAsyncPublish )
  {
    this();
    
    forcePublish_   = forcePublish;
    doAsyncPublish_ = doAsyncPublish;
  }

  public Status execute(Environment env)
  {
    Status status = new SimpleStatus("");

    IServer server = ServerCore.findServer(serverInstanceId);
    if (server == null)
    {
      status = new SimpleStatus("", msgUtils_.getMessage("MSG_ERROR_INSTANCE_NOT_FOUND"), Status.ERROR);
      env.getStatusHandler().reportError(status);
      return status;
    }

    monitor = EnvironmentUtils.getIProgressMonitor(env);
    int serverState = server.getServerState();
    int publishState = server.getServerPublishState();
    
    //Publish if required
    switch (publishState)
    {
      case IServer.PUBLISH_STATE_INCREMENTAL:
        if (server.canPublish().getSeverity() == IStatus.OK)
        {
          status = publish(server, IServer.PUBLISH_INCREMENTAL);
          if (status.getSeverity() == Status.ERROR)
          {
            env.getStatusHandler().reportError(status);
            return status;
          }
        }    
        break;
      case IServer.PUBLISH_STATE_FULL:
        if (server.canPublish().getSeverity() == IStatus.OK)
        {
          status = publish(server, IServer.PUBLISH_FULL);
          if (status.getSeverity() == Status.ERROR)
          {
            env.getStatusHandler().reportError(status);
            return status;
          }
        }
        break;
        
      case IServer.PUBLISH_STATE_UNKNOWN:
        if (server.canPublish().getSeverity() == IStatus.OK)
        {
          status = publish(server, IServer.PUBLISH_INCREMENTAL);
          if (status.getSeverity() == Status.ERROR)
          {
            env.getStatusHandler().reportError(status);
            return status;
          }
        }
        break;  

        //TODO: Reassess need for this, since Tomcat should know
        // whether it needs to be published or not (or publish should
        // simply always be driven by us.
        case IServer.PUBLISH_STATE_NONE:
        {
          if( forcePublish_ )
          {
            status = publish(server, IServer.PUBLISH_INCREMENTAL);
            
            if (status.getSeverity() == Status.ERROR)
            {
              env.getStatusHandler().reportError(status);
              return status;
            }  
          }
          
    	  break;  
        }
      
        default:
    }


    switch (serverState)
    {
      case IServer.STATE_STOPPED:
        if (server.canStart(ILaunchManager.RUN_MODE).getSeverity()==IStatus.OK)
        {
          status = start(server);
          if (status.getSeverity() == Status.ERROR)
          {
            env.getStatusHandler().reportError(status);
            return status;
          }       
        }
        break;
      case IServer.STATE_STARTED:    	
    	boolean shouldRestart = server.getServerRestartState();    	
    	//TODO Ideally getServerRestartState() returning true should be a sufficient
    	//condition for us to restart. However, getServerRestartState() seems to 
    	//sometimes pessimistically return true when it doesn't need to for Tomcat 
    	//servers. In order to curb the number of restarts, we will only 
    	//restart if a publish was required earlier in this execute method AND 
    	//getServerRestartState() returns true. 
    	//A publish is normally required when a module is added to the server.
    	
    	if (publishState != IServer.PUBLISH_STATE_NONE && shouldRestart && server.canRestart(ILaunchManager.RUN_MODE).getSeverity()==IStatus.OK)    	  
        {
          status = restart(server);
          if (status.getSeverity() == Status.ERROR)
          {
            env.getStatusHandler().reportError(status);
            return status;
          }       
        }
    }
    
    return status;
  }

  private Status publish(final IServer server, final int kind)
  {
    Status status = new SimpleStatus("");
    final IStatus[] istatus = new IStatus[1]; 
    monitor.subTask(msgUtils_.getMessage("PROGRESS_INFO_PUBLISHING_SERVER"));
    IRunnableWithProgress runnable = new IRunnableWithProgress()
	{
  		public void run(IProgressMonitor shellMonitor) throws InvocationTargetException, InterruptedException
		{
  			istatus[0] = server.publish(kind, shellMonitor);
		}  		
	};
	
	try
	{
		if( doAsyncPublish_ )
		{	
		  PlatformUI.getWorkbench().getProgressService().run(true,false,runnable);
		}
		else
		{
		  runnable.run( monitor );
		}
		
	}
	catch(InvocationTargetException ite)
	{
	  istatus[0] = new org.eclipse.core.runtime.Status( IStatus.ERROR, "id", 0, ite.getMessage(), ite );
	  ite.printStackTrace();
	}
	catch(InterruptedException ie)
	{
	  istatus[0] = new org.eclipse.core.runtime.Status( IStatus.ERROR, "id", 0, ie.getMessage(), ie );
	  ie.printStackTrace();
	}
    
    
	if (istatus[0].getSeverity() != IStatus.OK)
    {
      status = EnvironmentUtils.convertIStatusToStatus(istatus[0]);
      return status;
    }
    
    log.log(Log.INFO, 5051, this, "publishProject", "IServer=" + server + ", Publish command completed");
    return status;
  }

  private Status restart(IServer server)
  {
    Status status = new SimpleStatus("");
    try
    {
      monitor.subTask(msgUtils_.getMessage("PROGRESS_INFO_STARTING_SERVER"));
      server.synchronousRestart(ILaunchManager.RUN_MODE, monitor);
      log.log(Log.INFO, 5052, this, "execute", "IServer=" + server + ", Restart command completed");
      return status;
    } catch (CoreException e)
    {
      status = new SimpleStatus("", msgUtils_.getMessage("MSG_ERROR_SERVER"), Status.ERROR, e);
      return status;
    }

  }

  private Status start(IServer server)
  {
    Status status = new SimpleStatus("");
    try
    {
      monitor.subTask(msgUtils_.getMessage("PROGRESS_INFO_STARTING_SERVER"));
      server.synchronousStart(ILaunchManager.RUN_MODE, monitor);
      log.log(Log.INFO, 5053, this, "execute", "IServer=" + server + ", Start command completed");
      return status;
    } catch (CoreException e)
    {
      status = new SimpleStatus("", msgUtils_.getMessage("MSG_ERROR_SERVER"), Status.ERROR, e);
      return status;
    }
  }

  public void setServerInstanceId(String serverInstanceId)
  {
    this.serverInstanceId = serverInstanceId;
  }	
}
