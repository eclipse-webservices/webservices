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

package org.eclipse.jst.ws.internal.consumption.ui.server;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.IJobManager;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.jface.wizard.ProgressMonitorPart;
import org.eclipse.jst.ws.internal.consumption.command.common.StartServerCommand;
import org.eclipse.jst.ws.internal.ui.common.UIUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.command.internal.env.core.common.MessageUtils;
import org.eclipse.wst.command.internal.env.core.common.StatusUtils;
import org.eclipse.wst.command.internal.env.core.context.TransientResourceContext;
import org.eclipse.wst.command.internal.env.ui.eclipse.EclipseEnvironment;
import org.eclipse.wst.command.internal.env.ui.eclipse.EclipseStatusHandler;
import org.eclipse.wst.command.internal.env.ui.widgets.SimpleWidgetDataContributor;
import org.eclipse.wst.command.internal.env.ui.widgets.WidgetDataEvents;
import org.eclipse.wst.common.environment.NullStatusHandler;
import org.eclipse.wst.server.core.IServer;

public class StartServerWidget extends SimpleWidgetDataContributor 
{
  private IServer             server_;
  private IStatus              status_;
  private Listener            statusListener_;
  private Button              button_;
  private JobChangeAdapter    jobChangeAdapter_;
  private Text                serverStateText_;
  private ProgressMonitorPart progressMonitor_;
  private MessageUtils        msgUtils_;
  private String              pluginId_;
  private Composite           buttonGroup_;
    
  /*CONTEXT_ID SSWP0001 Start the server button. */
  private String INFOPOP_SSWP_SERVER_BUTTON = "SSWP0001";
  
  public StartServerWidget( IServer server )
  {
	pluginId_ = "org.eclipse.jst.ws.consumption.ui";
	msgUtils_ = new MessageUtils( pluginId_ + ".plugin", this );
		
    server_ = server;
    
    jobChangeAdapter_ = new JobChangeAdapter()
                        {
                          public void done(final IJobChangeEvent event) 
                          {
                        	Display.getDefault().asyncExec( new Runnable()
                        			                        {
	        					                              public void run() 
							                                  {
	        						                            if( !progressMonitor_.isDisposed() )
	        						                            {
							                                      setServerState();	
							                                      progressMonitor_.done();
							                                      reportErrorIfRequired( (StartServerJob)event.getJob() );
	        						                            }
							                                  }
                        			                        });
                          }
                        };
  }
  
  public WidgetDataEvents addControls(Composite parent, Listener statusListener) 
  {
	statusListener_ = statusListener;
	
	UIUtils      uiUtils     = new UIUtils( msgUtils_, pluginId_ );
	Composite    group       = uiUtils.createComposite( parent, 1 );
	
	Text         text1        = uiUtils.createText( group, null, null, null, SWT.READ_ONLY );
	text1.setText( msgUtils_.getMessage( "LABEL_START_SERVER_TEXT1", new String[]{ server_.getName() } ));
	
	Text         text2        = uiUtils.createText( group, null, null, null, SWT.READ_ONLY );
	text2.setText( msgUtils_.getMessage( "LABEL_START_SERVER_TEXT2", new String[]{ server_.getName() } ));
	
	Text         text3        = uiUtils.createText( group, null, null, null, SWT.READ_ONLY );
	text3.setText( msgUtils_.getMessage( "LABEL_START_SERVER_TEXT3", new String[]{ server_.getName() } ));
	
	Text         text4        = uiUtils.createText( group, null, null, null, SWT.READ_ONLY );
	text4.setText( msgUtils_.getMessage( "LABEL_START_SERVER_TEXT4", new String[]{ server_.getName() } )); 
	
	buttonGroup_ = uiUtils.createComposite( group, 2,-1, 0 );
	serverStateText_ = uiUtils.createText( buttonGroup_, null, null, null, SWT.READ_ONLY );
	serverStateText_.setLayoutData( new GridData() );
		
	button_ = uiUtils.createPushButton( buttonGroup_, 
	                                    "LABEL_START_SERVER_BUTTON",
			                            "TOOLTIP_START_SERVER_BUTTON",
			                            INFOPOP_SSWP_SERVER_BUTTON );
	
	button_.addSelectionListener( new SelectionAdapter()
			                      {
									 public void widgetSelected( SelectionEvent evt )
		                             {
			                       	   serverStateText_.setText( getStateMessage( "TEXT_SERVER_STATUS", "TEXT_SERVER_STARTING" ) );
			                    	   progressMonitor_.beginTask( getStateMessage( "TEXT_SERVER_MSG", "TEXT_SERVER_STARTING" ), IProgressMonitor.UNKNOWN );
			                    	   button_.setEnabled( false );
			                    	   buttonGroup_.pack();
		                               startServerJob();
		                             }	                          
			                      } );	
	
	progressMonitor_ = new ProgressMonitorPart( group, new GridLayout() );
	progressMonitor_.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ));
	
	setServerState();
	return this;
  }

  public IStatus getStatus() 
  {
	return status_;
  }
  
  private void setServerState()
  {
	int state = server_.getServerState();
	
	switch( state )
	{
	  case IServer.STATE_STARTED:
	  {
		status_ = Status.OK_STATUS;
		button_.setEnabled( false );
		serverStateText_.setText( getStateMessage( "TEXT_SERVER_STATUS", "TEXT_SERVER_STARTED" ) );
		
	    break;	  
	  }
	  
	  case IServer.STATE_STARTING:
	  {
		status_ = StatusUtils.errorStatus( "" );
		button_.setEnabled( false );  
		serverStateText_.setText( getStateMessage( "TEXT_SERVER_STATUS", "TEXT_SERVER_STARTING" ) );
		progressMonitor_.beginTask( getStateMessage( "TEXT_SERVER_MSG", "TEXT_SERVER_STARTING" ), IProgressMonitor.UNKNOWN );
		
		// The server is still starting so we need to reconnect to the job
		// that is starting it.
		startServerJob();
	    break;	  
	  }
	  
	  default:
	  {
		status_ = StatusUtils.errorStatus( "" );
        button_.setEnabled( true );
		serverStateText_.setText( getStateMessage( "TEXT_SERVER_STATUS", "TEXT_SERVER_STOPPED" ) );
		break;  
	  }
	};
	
	statusListener_.handleEvent( null );
	buttonGroup_.pack();
  }  
  
  private String getStateMessage( String mainKey, String subKey )
  {
	return msgUtils_.getMessage( mainKey, new String[]{ msgUtils_.getMessage( subKey ) } );
  }
  
  // Connect to an existing server thread otherwise start a new one.
  private void startServerJob()
  {
	IJobManager    jobManager     = Platform.getJobManager();
	Job[]          jobs           = jobManager.find( StartServerFamily );
	StartServerJob startServerJob = null;
	
	// There may be more than one job starting for different servers.
	// Therefore, we need to find the one for our server if it is available.
	for( int index = 0; index < jobs.length; index++ )
	{
	  StartServerJob jobFound = (StartServerJob)jobs[index];
	  
	  if( jobFound.getServer() == server_ )
	  {
	    startServerJob = jobFound;
	    break;
	  }
	}
	
	if( startServerJob != null )
	{
	  synchronized( StartServerFamily ) 
	  {
        IStatus status = startServerJob.getStatus();
        
        // We are using status to determine if the job has completed or not.
        // Normally, we would not get here if the job had already completed,
        // but there is a slim window where the job manager gives us the job
        // and then it immediately completes.
        if( status == null )
        {        
          // The job had not completed yet so we will add a job change listener.
          // We are adding the job listener here so that we don't have to assume
          // that this instance of this widget is the same as the previous instance.
          // If startServerJob already has "jobChangeAdapter_" added to it this
          // method call will not have an effect.(Which is what we want.)
          startServerJob.addJobChangeListener( jobChangeAdapter_ ); 
          
          // Note: this job was reporting progress to different progressMonitor_
          //       control.  We need to tell the job that it should report progress
          //       to our new progressMonitor_ control on this wizard page.
          ProgressMonitorWrapper monitor = (ProgressMonitorWrapper)startServerJob.getMonitor();
          monitor.setMonitor( progressMonitor_ );
        }
        else
        {
          // The job completed before we had a chance to add the job change listener
          // Therefore, we will just call jobChangeAdapter_ directly to notify
          // the UI that the job has completed.
          jobChangeAdapter_.done( null );
        }
	  }
	}
	else
	{
	  startServerJob = new StartServerJob();	
	  startServerJob.addJobChangeListener( jobChangeAdapter_ );
	  startServerJob.schedule();
	}
  }
     
  private void reportErrorIfRequired( StartServerJob serverJob )
  {
	  IStatus status = serverJob.getStatus();
	
	  if( status.getSeverity() == Status.ERROR )
	  {
	    Shell                shell   = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
      EclipseStatusHandler handler = new EclipseStatusHandler( shell );
      
      handler.reportError( status );
	  }
  }
  
  final private static String  StartServerFamily = "StartServerFamily";
  
  // This class is used to start up the server in an Eclipse job.
  private class StartServerJob extends Job
  {
	  private IStatus                status_ = null;
	  private ProgressMonitorWrapper envMonitor_;
	
	  public StartServerJob()
	  {
	    super( "StartServerJob" );
	  
	    envMonitor_ = new ProgressMonitorWrapper( progressMonitor_ );
	  }
	
	  public IServer getServer()
	  {
	    return server_;	
	  }
	
	  public ProgressMonitorWrapper getMonitor()
	  {
	    return envMonitor_;	
	  }
	
	  public boolean belongsTo(Object family) 
	  {
	    return family == StartServerFamily;
	  }

	  protected IStatus run(IProgressMonitor monitor) 
	  {
	    NullStatusHandler        handler         = new NullStatusHandler();
	    TransientResourceContext resourceContext = new TransientResourceContext();
	    EclipseEnvironment       environment     = new EclipseEnvironment( null,resourceContext, handler );
	    StartServerCommand       serverCommand   = new StartServerCommand( false, false );
	    
	    serverCommand.setServerInstanceId( server_.getId() );
      serverCommand.setEnvironment( environment );
	    
	    try
	    {
	      setStatus( serverCommand.execute( envMonitor_, null ) );		
	    }
	    catch( Throwable exc )
	    {
		    exc.printStackTrace();
	      setStatus( StatusUtils.errorStatus( exc ) );
	    }
	  	
	    return Status.OK_STATUS;
	  }
	
	  // Calls to this method need to first synchronize on the
	  // StartServerFamily object.
	  public IStatus getStatus()
	  {
	    return status_;
	  }
	
	  private void setStatus( IStatus status )
	  {
	    synchronized( StartServerFamily ) 
	    {
        status_ = status;
	    }
	  }
  }
    
  private class ProgressMonitorWrapper implements IProgressMonitor
  {
    private IProgressMonitor monitor_ = new NullProgressMonitor();
  	  	
    public ProgressMonitorWrapper( IProgressMonitor monitor )
    {
      monitor_ = monitor;  
    }
    
    public void setMonitor( IProgressMonitor monitor )
    {
      monitor_ = monitor;	
    }
    
    public void beginTask(final String name, final int totalWork) 
    {
      Display.getDefault().asyncExec( new Runnable()
      		                          {
                                        public void run()
                                        {
                                          if( !progressMonitor_.isDisposed() )
                                          {
                                      	    monitor_.beginTask( name, totalWork );
                                          }
                                        }
      		                          } );
    }

    public void done() 
    {
      Display.getDefault().asyncExec( new Runnable()
                                      {
                                        public void run()
                                        {
                                          if( !progressMonitor_.isDisposed() )
                                          {
              	                            monitor_.done();
                                          }
                                        }
                                      } );
    }

    public void internalWorked(final double work) 
    {
      Display.getDefault().asyncExec( new Runnable()
                                      {
                                        public void run()
                                        {
                                          if( !progressMonitor_.isDisposed() )
                                          {
              	                            monitor_.internalWorked( work );
                                          }
                                        }
                                      } );
    }

    public boolean isCanceled() 
    {
      return progressMonitor_.isDisposed() ? false : monitor_.isCanceled();
    }

    public void setCanceled(boolean value) 
    {
      if( !progressMonitor_.isDisposed() )
      {
  	    monitor_.setCanceled( value );
      }
    }

    public void setTaskName(final String name) 
    {
  	  Display.getDefault().asyncExec( new Runnable()
                                      {
                                        public void run()
                                        {
                                          if( !progressMonitor_.isDisposed() )
                                          {
                  	                        monitor_.setTaskName( name );
                                          }
                                        }
                                      } );
    }

    public void subTask( final String name) 
    {
  	  Display.getDefault().asyncExec( new Runnable()
                                      {
                                        public void run()
                                        {
                                          if( !progressMonitor_.isDisposed() )
                                          {
                  	                        monitor_.subTask( name );
                                          }
                                        }
                                      } );
    }

    public void worked(final int work) 
    {
      Display.getDefault().asyncExec( new Runnable()
                                      {
                                        public void run()
                                        {
                                          if( !progressMonitor_.isDisposed() )
                                          {
              	                            monitor_.worked( work );
                                          }
                                        }
                                      } );
    }
  }
}
