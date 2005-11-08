/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.internal.consumption.ui.widgets.runtime;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.window.Window;
import org.eclipse.jst.ws.internal.consumption.ui.ConsumptionUIMessages;
import org.eclipse.jst.ws.internal.consumption.ui.wizard.RuntimeServerSelectionDialog;
import org.eclipse.jst.ws.internal.consumption.ui.wsrt.WebServiceRuntimeExtensionUtils2;
import org.eclipse.jst.ws.internal.data.TypeRuntimeServer;
import org.eclipse.jst.ws.internal.ui.common.UIUtils;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wst.command.internal.env.core.common.StatusUtils;
import org.eclipse.wst.command.internal.env.ui.widgets.SimpleWidgetDataContributor;
import org.eclipse.wst.command.internal.env.ui.widgets.WidgetDataEvents;
import org.eclipse.wst.server.core.IRuntime;
import org.eclipse.wst.server.core.IServerType;
import org.eclipse.wst.server.core.ServerCore;
import org.eclipse.wst.server.core.ServerUtil;



public class RuntimeServerSelectionWidget extends SimpleWidgetDataContributor
{
  private String            pluginId_ = "org.eclipse.jst.ws.consumption.ui";
  private Text              runtime_;
  private Text              server_;

  private Composite         parent_;
  private boolean           isClientContext_;
  private TypeRuntimeServer ids_;

  private Listener           statusListener_;
  
  /* CONTEXT_ID PWRS0004 for the service-side runtime selection of the runtime selection Page */
  private String INFOPOP_PWRS_GROUP_SERVICE_RUNTIME = pluginId_ + ".PWRS0004";

  /* CONTEXT_ID PWRS0005 for the service-sdie server selection of the runtime selection Page */
  private String INFOPOP_PWRS_GROUP_SERVICE_SERVER = pluginId_ + ".PWRS0005";

  public RuntimeServerSelectionWidget( boolean isClientScenario )
  {
    isClientContext_ = isClientScenario; 
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.wst.command.env.ui.widgets.WidgetContributor#addControls(org.eclipse.swt.widgets.Composite, org.eclipse.swt.widgets.Listener)
   */
  public WidgetDataEvents addControls( Composite parent, Listener statusListener )
  {
    UIUtils      uiUtils  = new UIUtils( pluginId_ ); 
    
    parent_         = parent;
    statusListener_ = statusListener;
        
    // Runtime label and text
    runtime_ = uiUtils.createText( parent, ConsumptionUIMessages.LABEL_RUNTIMES_LIST,  
    		ConsumptionUIMessages.TOOLTIP_PWRS_TEXT_RUNTIME,
                                   INFOPOP_PWRS_GROUP_SERVICE_RUNTIME, SWT.READ_ONLY );
    
    // Server label and text
    server_ = uiUtils.createText( parent, ConsumptionUIMessages.LABEL_SERVERS_LIST,  
    		ConsumptionUIMessages.TOOLTIP_PWRS_TEXT_SERVER,
                                  INFOPOP_PWRS_GROUP_SERVICE_SERVER, SWT.READ_ONLY );
    
    Button editButton = new Button( parent, SWT.NONE );
    editButton.setText( ConsumptionUIMessages.LABEL_EDIT_BUTTON); 
    editButton.addSelectionListener( new SelectionAdapter()
                                     {
                                       public void widgetSelected( SelectionEvent  evt )
                                       {
                                         handleEditButton(); 
                                       }
                                     } );
    
    // Dummy label that goes on the right side of the edit button.
    new Label( parent, SWT.NONE );
    
    return this;
  }

  public TypeRuntimeServer getTypeRuntimeServer()
  {
    return ids_;  
  }
  
  public void setTypeRuntimeServer( TypeRuntimeServer ids )
  {
    ids_ = ids;
    setLabels();
  }
  
  private void setLabels()
  {
	String                       runtimeLabel = WebServiceRuntimeExtensionUtils2.getRuntimeLabelById( ids_.getRuntimeId() );
    String                       serverLabel  = null;
    
    if( ids_.getServerInstanceId() == null )
    {
      // Get the label for the general server type.
      //WebServiceServer server = registry.getWebServiceServerByFactoryId( ids_.getServerId() );
      //serverLabel = server == null ? "" : server.getLabel();
      String serverId = ids_.getServerId();
      if (serverId != null)
        serverLabel = WebServiceRuntimeExtensionUtils2.getServerLabelById(serverId);
    }
    else
    {
      // Get the label for the server instance.
      //serverLabel = registry.getServerInstanceLabelFromInstanceId( ids_.getServerInstanceId() );
      String serverInstanceId = ids_.getServerInstanceId();
      if (serverInstanceId != null)
        serverLabel = WebServiceRuntimeExtensionUtils2.getServerInstanceLabelFromInstanceId(serverInstanceId);
    }
    // rskreg
    runtimeLabel = runtimeLabel == null ? "" : runtimeLabel;
    
    runtime_.setText( runtimeLabel );
    if (serverLabel != null)
      server_.setText( serverLabel );
 
  }
  
  public void handleEditButton()
  {
    byte mode = isClientContext_ ? (byte)1 : (byte)0;
    
    RuntimeServerSelectionDialog dialog     
      = dialog = new RuntimeServerSelectionDialog( parent_.getShell(), mode, ids_, "14" ); //rm j2ee
    dialog.create();
    dialog.handleServerViewSelectionEvent();
    int result = dialog.open();

    if (result == Window.OK)
    {
      ids_ = dialog.getTypeRuntimeServer();
      setLabels();
      statusListener_.handleEvent(null);
    }  
  }  
  
  public void addModifyListener(ModifyListener listener)
  {
    if (runtime_ != null)
      runtime_.addModifyListener(listener);
  }
  
  public void removeModifyListener(ModifyListener listener)
  {
    if (runtime_ != null)
      runtime_.removeModifyListener(listener);
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.wst.command.env.ui.widgets.WidgetContributor#getStatus()
   */
  public IStatus getStatus() 
  {
    IStatus status = Status.OK_STATUS;
    String scenario = isClientContext_ ? ConsumptionUIMessages.MSG_CLIENT_SUB
                                        : ConsumptionUIMessages.MSG_SERVICE_SUB;
    
	String                       runtimeLabel = WebServiceRuntimeExtensionUtils2.getRuntimeLabelById( ids_.getRuntimeId() );
	String                       serverLabel  = ids_.getServerId() == null ? "" : WebServiceRuntimeExtensionUtils2.getServerLabelById(ids_.getServerId());
    
	
    if( ids_.getRuntimeId() == null || runtimeLabel == null || runtimeLabel.equals("" ))
    {
      status = StatusUtils.errorStatus(NLS.bind(ConsumptionUIMessages.MSG_NO_RUNTIME, new String[]{ scenario } ) );
    }
    else if( ids_.getServerId() == null || serverLabel.equals( "" ))
    {
      status = StatusUtils.errorStatus( NLS.bind(ConsumptionUIMessages.MSG_NO_SERVER, new String[]{ scenario } ) );      
    }

    //Check if only stub runtime is available for the selected server type
    
    String serverFactoryId = ids_.getServerId();
    //
    IServerType serverType = ServerCore.findServerType(serverFactoryId);
	if (serverType!=null) {
		
		//Find a Runtime which is not a stub
		//IRuntime nonStubRuntime = null;
		boolean foundNonStubRuntime = false;
		IRuntime[] runtimes = ServerUtil.getRuntimes(null, null);
		String serverRuntimeTypeId = serverType.getRuntimeType().getId();
		for (int i = 0; i < runtimes.length; i++) {
			IRuntime runtime = runtimes[i];
			String thisRuntimeTypeId = runtime.getRuntimeType().getId();
			if (thisRuntimeTypeId.equals(serverRuntimeTypeId) && !runtime.isStub()) {
		        //Found an appropriate IRuntime that is not a stub
				foundNonStubRuntime=true;
				break;
			}
		}				
		
		if (!foundNonStubRuntime)
		{	
			String servertypeLabel = WebServiceRuntimeExtensionUtils2.getServerLabelById(serverFactoryId);
			status = StatusUtils.errorStatus( NLS.bind(ConsumptionUIMessages.MSG_ERROR_STUB_ONLY,new String[]{servertypeLabel}) );					
		}
	}		
    
    //
    
	//--------- check if WSCT exists for these selections
	if (!(ids_.getServerId() == null) && !(ids_.getRuntimeId() == null) && isClientContext_)
	{

		if (!WebServiceRuntimeExtensionUtils2.isServerClientRuntimeTypeSupported( ids_.getServerId(), ids_.getRuntimeId(), ids_.getTypeId())) 
		{
			status = StatusUtils.errorStatus( NLS.bind(ConsumptionUIMessages.MSG_INVALID_SRT_SELECTIONS, new String[]{ scenario } ) );		  
		}

	}    
    
    //--------- check if WSSRT exists for these selections
    if (!(ids_.getServerId() == null) && !(ids_.getRuntimeId() == null) && !isClientContext_)
    {

	  if (!WebServiceRuntimeExtensionUtils2.isServerRuntimeTypeSupported(ids_.getServerId(), ids_.getRuntimeId(), ids_.getTypeId())) {	  
        status = StatusUtils.errorStatus( NLS.bind(ConsumptionUIMessages.MSG_INVALID_SRT_SELECTIONS, new String[]{ scenario } ) );      
      }

    }
	
    
    
    return status;
  }
}
