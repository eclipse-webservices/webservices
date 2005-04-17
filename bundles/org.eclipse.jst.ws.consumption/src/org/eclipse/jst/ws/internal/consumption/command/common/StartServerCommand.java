package org.eclipse.jst.ws.internal.consumption.command.common;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.jst.ws.internal.common.EnvironmentUtils;
import org.eclipse.wst.command.env.core.SimpleCommand;
import org.eclipse.wst.command.env.core.common.Environment;
import org.eclipse.wst.command.env.core.common.Log;
import org.eclipse.wst.command.env.core.common.MessageUtils;
import org.eclipse.wst.command.env.core.common.SimpleStatus;
import org.eclipse.wst.command.env.core.common.Status;
import org.eclipse.wst.command.internal.env.eclipse.EclipseLog;
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
  
	private String serverInstanceId;
	
  
	public StartServerCommand()
  {
    String pluginId = "org.eclipse.jst.ws.consumption";
    msgUtils_ = new MessageUtils(pluginId + ".plugin", this);
    setDescription(DESCRIPTION);
    setName(LABEL);
    log = new EclipseLog();
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
          status = publish(server, IServer.PUBLISH_CLEAN);
          if (status.getSeverity() == Status.ERROR)
          {
            env.getStatusHandler().reportError(status);
            return status;
          }
        }
        break;  
        
        default:
    }


    switch (serverState)
    {
      case IServer.STATE_STOPPED:
        if (server.canStart(ILaunchManager.RUN_MODE).getSeverity()==IStatus.OK)
        {
          start(server);
          if (status.getSeverity() == Status.ERROR)
          {
            env.getStatusHandler().reportError(status);
            return status;
          }       
        }
        break;
      case IServer.STATE_STARTED:
        //Restart the server if needed
        if (publishState != IServer.PUBLISH_STATE_NONE && server.canRestart(ILaunchManager.RUN_MODE).getSeverity()==IStatus.OK)
        {
          restart(server);
          if (status.getSeverity() == Status.ERROR)
          {
            env.getStatusHandler().reportError(status);
            return status;
          }       
        }
    }
        
    return status;
  }

  private Status publish(IServer server, int kind)
  {
    Status status = new SimpleStatus("");
    monitor.subTask(msgUtils_.getMessage("PROGRESS_INFO_PUBLISHING_SERVER"));
    IStatus istatus = server.publish(kind, monitor);
    if (istatus.getSeverity() != IStatus.OK)
    {
      status = EnvironmentUtils.convertIStatusToStatus(istatus);
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
