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

import org.eclipse.jface.window.Window;
import org.eclipse.jst.ws.internal.common.J2EEUtils;
import org.eclipse.jst.ws.internal.consumption.ui.wizard.RuntimeServerSelectionDialog;
import org.eclipse.jst.ws.internal.consumption.ui.wizard.WebServiceClientTypeRegistry;
import org.eclipse.jst.ws.internal.consumption.ui.wizard.WebServiceServer;
import org.eclipse.jst.ws.internal.consumption.ui.wizard.WebServiceServerRuntimeTypeRegistry;
import org.eclipse.jst.ws.internal.data.TypeRuntimeServer;
import org.eclipse.jst.ws.internal.ui.common.UIUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wst.command.env.core.common.MessageUtils;
import org.eclipse.wst.command.env.core.common.SimpleStatus;
import org.eclipse.wst.command.env.core.common.Status;
import org.eclipse.wst.command.env.ui.widgets.SimpleWidgetDataContributor;
import org.eclipse.wst.command.env.ui.widgets.WidgetDataEvents;



public class RuntimeServerSelectionWidget extends SimpleWidgetDataContributor
{
  private String            pluginId_ = "org.eclipse.jst.ws.consumption.ui";
  private Text              runtime_;
  private Text              server_;
  private Text              j2eeVersionText;
  private Composite         parent_;
  private boolean           isClientContext_;
  private TypeRuntimeServer ids_;
  private String             j2eeVersion_;
  private MessageUtils       msgUtils_;
  private Listener           statusListener_;
  
  /* CONTEXT_ID PWRS0004 for the service-side runtime selection of the runtime selection Page */
  private String INFOPOP_PWRS_GROUP_SERVICE_RUNTIME = pluginId_ + ".PWRS0004";

  /* CONTEXT_ID PWRS0005 for the service-sdie server selection of the runtime selection Page */
  private String INFOPOP_PWRS_GROUP_SERVICE_SERVER = pluginId_ + ".PWRS0005";

  /* CONTEXT_ID PWRS0009 for the J2EE version selection of the runtime selection Page */
  private String INFOPOP_PWRS_J2EE_VERSION = pluginId_ + ".PWRS0009";

  public RuntimeServerSelectionWidget( boolean isClientScenario )
  {
    isClientContext_ = isClientScenario; 
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.wst.command.env.ui.widgets.WidgetContributor#addControls(org.eclipse.swt.widgets.Composite, org.eclipse.swt.widgets.Listener)
   */
  public WidgetDataEvents addControls( Composite parent, Listener statusListener )
  {
    msgUtils_ = new MessageUtils( pluginId_ + ".plugin", this );
    UIUtils      uiUtils  = new UIUtils(msgUtils_, pluginId_ ); 
    
    parent_         = parent;
    statusListener_ = statusListener;
        
    // Runtime label and text
    runtime_ = uiUtils.createText( parent, "LABEL_RUNTIMES_LIST",  
                                   "TOOLTIP_PWRS_TEXT_RUNTIME",
                                   INFOPOP_PWRS_GROUP_SERVICE_RUNTIME, SWT.READ_ONLY );
    
    // Server label and text
    server_ = uiUtils.createText( parent, "LABEL_SERVERS_LIST",  
                                  "TOOLTIP_PWRS_TEXT_SERVER",
                                  INFOPOP_PWRS_GROUP_SERVICE_SERVER, SWT.READ_ONLY );
    
    j2eeVersionText = uiUtils.createText(parent, "LABEL_J2EE_VERSION", "TOOLTIP_PWRS_J2EE_VERSION", INFOPOP_PWRS_J2EE_VERSION, SWT.READ_ONLY);
    
    Button editButton = new Button( parent, SWT.NONE );
    editButton.setText( msgUtils_.getMessage("LABEL_EDIT_BUTTON")); 
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
  
  public String getJ2EEVersion()
  {
    return j2eeVersion_;
  }
  
  public void setJ2EEVersion(String j2eeVersion)
  {
    this.j2eeVersion_ = j2eeVersion;
    j2eeVersionText.setText((j2eeVersion_ != null) ? J2EEUtils.getLabelFromJ2EEVersion(j2eeVersion_) : msgUtils_.getMessage("LABEL_NA"));
    //setLabels();
  }
  
  private void setLabels()
  {
    WebServiceClientTypeRegistry registry     = WebServiceClientTypeRegistry.getInstance();
    String                       runtimeLabel = registry.getRuntimeLabelById( ids_.getRuntimeId() );
    String                       serverLabel  = null;
    
    if( ids_.getServerInstanceId() == null )
    {
      // Get the label for the general server type.
      WebServiceServer server = registry.getWebServiceServerByFactoryId( ids_.getServerId() );
      serverLabel = server == null ? "" : server.getLabel();
    }
    else
    {
      // Get the label for the server instance.
      serverLabel = registry.getServerInstanceLabelFromInstanceId( ids_.getServerInstanceId() );
    }
    
    runtimeLabel = runtimeLabel == null ? "" : runtimeLabel;
    
    runtime_.setText( runtimeLabel );
    server_.setText( serverLabel );
    j2eeVersionText.setText((j2eeVersion_ != null) ? J2EEUtils.getLabelFromJ2EEVersion(j2eeVersion_) : msgUtils_.getMessage("LABEL_NA"));
  }
  
  public void handleEditButton()
  {
    byte mode = isClientContext_ ? (byte)1 : (byte)0;
    
    RuntimeServerSelectionDialog dialog     
      = dialog = new RuntimeServerSelectionDialog( parent_.getShell(), mode, ids_, j2eeVersion_ );
    dialog.create();
    dialog.handleServerViewSelectionEvent();
    int result = dialog.open();

    if (result == Window.OK)
    {
      ids_ = dialog.getTypeRuntimeServer();
      j2eeVersion_ = dialog.getJ2EEVersion();
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
  public Status getStatus() 
  {
    Status status = new SimpleStatus( "" );
    String scenario = isClientContext_ ? msgUtils_.getMessage( "MSG_CLIENT_SUB" )
                                        : msgUtils_.getMessage( "MSG_SERVICE_SUB" );
    
    // Kludge!!!  We shouldn't have to check for blank labels.  The defaulting commands should be setting the
    //            ids to null if there isn't a valid one.  This code should be removed in C4.
    WebServiceClientTypeRegistry wsctRegistry = WebServiceClientTypeRegistry.getInstance();
    String                       runtimeLabel = wsctRegistry.getRuntimeLabelById( ids_.getRuntimeId() );
    WebServiceServer             server       = wsctRegistry.getWebServiceServerByFactoryId( ids_.getServerId() );
    String                       serverLabel  = server == null ? "" : server.getLabel();
 
    if( ids_.getRuntimeId() == null || runtimeLabel == null || runtimeLabel.equals("" ))
    {
      status = new SimpleStatus( "", msgUtils_.getMessage( "MSG_NO_RUNTIME", new String[]{ scenario } ), Status.ERROR );
    }
    else if( ids_.getServerId() == null || serverLabel.equals( "" ))
    {
      status = new SimpleStatus( "", msgUtils_.getMessage( "MSG_NO_SERVER", new String[]{ scenario } ), Status.ERROR );      
    }

	//--------- check if WSCT exists for these selections
	if (!(ids_.getServerId() == null) && !(ids_.getRuntimeId() == null) && isClientContext_)
	{
		WebServiceServer wss = wsctRegistry.getWebServiceServerByFactoryId(ids_.getServerId());
		if (wss != null)
		{
			String serverTypeId = wss.getId();
			String runtimeId = ids_.getTypeId();
			if (!wsctRegistry.webServiceClientRuntimeTypeExists( serverTypeId, ids_.getRuntimeId(), runtimeId)) {
				status = new SimpleStatus( "", msgUtils_.getMessage( "MSG_INVALID_SRT_SELECTIONS", new String[]{ scenario } ), Status.ERROR );		  
			}
		}
		else
			status = new SimpleStatus( "", msgUtils_.getMessage( "MSG_INVALID_SRT_SELECTIONS", new String[]{ scenario } ), Status.ERROR );
	}    
    
    //--------- check if WSSRT exists for these selections
    if (!(ids_.getServerId() == null) && !(ids_.getRuntimeId() == null) && !isClientContext_)
    {
      WebServiceServerRuntimeTypeRegistry wssrtRegistry = WebServiceServerRuntimeTypeRegistry.getInstance();
      String serverTypeId = wsctRegistry.getWebServiceServerByFactoryId(ids_.getServerId()).getId();
      if (!wssrtRegistry.isServerRuntimeTypeSupported(serverTypeId, ids_.getRuntimeId(), ids_.getTypeId())) {
        status = new SimpleStatus( "", msgUtils_.getMessage( "MSG_INVALID_SRT_SELECTIONS", new String[]{ scenario } ), Status.ERROR );      
      }
    }
	
    
    
    return status;
  }
}
