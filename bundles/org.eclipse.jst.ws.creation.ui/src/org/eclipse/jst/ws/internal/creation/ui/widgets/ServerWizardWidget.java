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
package org.eclipse.jst.ws.internal.creation.ui.widgets;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.ws.internal.consumption.ui.ConsumptionUIMessages;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.PreferencesSelectionWidget;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.WebServiceClientTypeWidget;
import org.eclipse.jst.ws.internal.consumption.ui.wsrt.WebServiceRuntimeExtensionUtils2;
import org.eclipse.jst.ws.internal.data.LabelsAndIds;
import org.eclipse.jst.ws.internal.data.TypeRuntimeServer;
import org.eclipse.jst.ws.internal.ui.common.UIUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.command.internal.env.core.common.StatusUtils;
import org.eclipse.wst.command.internal.env.core.context.ResourceContext;
import org.eclipse.wst.command.internal.env.ui.widgets.SimpleWidgetDataContributor;
import org.eclipse.wst.command.internal.env.ui.widgets.WidgetDataEvents;


public class ServerWizardWidget extends SimpleWidgetDataContributor
{   
  private WebServiceClientTypeWidget clientWidget_;
  private PreferencesSelectionWidget preferencesWidget_;
  private Listener                   statusListener_;
  private TypeRuntimeServer          ids_;
  private LabelsAndIds               labelIds_;
  private boolean                    displayPreferences_;
  
  /*CONTEXT_ID PWPR0001 for the Project Page*/
  private String INFOPOP_PWPR_PAGE = "PWPR0001";
  
  /*CONTEXT_ID PWPR0005 for the Generate Proxy check box of the Project Page*/
  private String INFOPOP_PWPR_CHECKBOX_GENERATE_PROXY = "PWPR0005";
  private Button  generateProxy_;
  
  /*CONTEXT_ID PWPR0010 for the Web Service Type combo box of the Project Page*/
  private String INFOPOP_PWPR_COMBO_TYPE = "PWPR0010";
  private Combo  webserviceType_;
  
  /*CONTEXT_ID PWPR0011 for the Wizard Scenario Service group of the Project Page*/
  private String INFOPOP_PWPR_GROUP_SCENARIO_SERVICE = "PWPR0011";
  private Button startService_;
  
  /*CONTEXT_ID PWPR0009 for the Start Web Project check box check box of the Project Page*/
  private String INFOPOP_PWPR_CHECKBOX_START_WEB_PROJECT = "PWPR0009";
  
  /*CONTEXT_ID PWPR0013 for the test service checkbox of the project page*/  
  private String INFOPOP_PWPR_CHECKBOX_TEST_SERVICE = "PWPR0013";
  private Button testService_;
  
  /*CONTEXT_ID PWPR0014 for the monitor service checkbox of the projec page*/
  private String INFOPOP_PWPR_CHECKBOX_MONITOR_SERVICE = "PWPR0014";
  private Button monitorService;
  
  /*CONTEXT_ID PWPR0012 for the Launch UDDI check box of the Project Page*/
  private String INFOPOP_PWPR_CHECKBOX_LAUNCH_WS = "PWPR0012";
  private Button launchUddi_;
  
  public ServerWizardWidget( boolean displayPreferences )
  {
    displayPreferences_ = displayPreferences;  
  }
  
  public WidgetDataEvents addControls( Composite parent, Listener statusListener)
  {
    String       pluginId = "org.eclipse.jst.ws.consumption.ui";
    String       createPluginId = "org.eclipse.jst.ws.creation.ui";
    UIUtils      utils    = new UIUtils( createPluginId );
    
    statusListener_ = statusListener;
    
  	PlatformUI.getWorkbench().getHelpSystem().setHelp( parent, pluginId + "." +  INFOPOP_PWPR_PAGE );
  	
  	Composite serverComposite = utils.createComposite( parent, 1 );
  	
  	Group serviceGroup = utils.createGroup( serverComposite, ConsumptionUIMessages.GROUP_SCENARIO_SERVICE,
  			ConsumptionUIMessages.TOOLTIP_PWPR_GROUP_SCENARIO_SERVICE, 
  	                                        INFOPOP_PWPR_GROUP_SCENARIO_SERVICE,
											2, 10, 10);
  	
  	
  	// Create webservice combo box.
    webserviceType_ = utils.createCombo( serviceGroup, ConsumptionUIMessages.LABEL_WEBSERVICETYPE,
    		ConsumptionUIMessages.TOOLTIP_PWPR_COMBO_TYPE,
                                         INFOPOP_PWPR_COMBO_TYPE, 
										 SWT.SINGLE | SWT.BORDER | SWT.READ_ONLY );
    webserviceType_.addListener( SWT.Modify, statusListener );
    
    Composite buttonsGroup = utils.createComposite( serviceGroup, 1 );
    GridData  buttonGrid   = new GridData();
    buttonGrid.horizontalSpan = 2;
    buttonsGroup.setLayoutData( buttonGrid );
    
    // Create start service check box.
    startService_ = utils.createCheckbox( buttonsGroup, ConsumptionUIMessages.BUTTON_START_WEB_PROJECT,
    		ConsumptionUIMessages.TOOLTIP_PWPR_CHECKBOX_START_WEB_PROJECT,
                                          INFOPOP_PWPR_CHECKBOX_START_WEB_PROJECT );
    startService_.addSelectionListener( new SelectionAdapter()
                                        {
                                          public void widgetSelected( SelectionEvent evt )
                                          {
                                            handleStartPressed();
                                          }
                                        });
    
    launchUddi_ = utils.createCheckbox( buttonsGroup, ConsumptionUIMessages.BUTTON_WS_PUBLISH,
    		ConsumptionUIMessages.TOOLTIP_PWPR_CHECKBOX_LAUNCH_WS,
                                        INFOPOP_PWPR_CHECKBOX_LAUNCH_WS );
    
    Composite proxyComposite = utils.createComposite( serverComposite, 1, 5, 0 );
    
    // Create generate proxy check box.
    generateProxy_ = utils.createCheckbox( proxyComposite, ConsumptionUIMessages.BUTTON_GENERATE_PROXY,
    		ConsumptionUIMessages.TOOLTIP_PWPR_CHECKBOX_GENERATE_PROXY,
                                           INFOPOP_PWPR_CHECKBOX_GENERATE_PROXY );
    generateProxy_.addSelectionListener( new SelectionAdapter()
                                         {
                                           public void widgetSelected( SelectionEvent evt )
                                           {
                                             clientWidget_.enableWidget( generateProxy_.getSelection() );
                                           }
                                         });
    
    // Create client selection widget.
    clientWidget_ = new WebServiceClientTypeWidget();
    clientWidget_.addControls( serverComposite, statusListener );
    
    Composite testGroup = utils.createComposite(serverComposite,1);
    
    //  Create test service check box.
    testService_ = utils.createCheckbox( testGroup, ConsumptionUIMessages.CHECKBOX_TEST_WEBSERVICE,
    		ConsumptionUIMessages.TOOLTIP_PWPR_CHECKBOX_TEST_SERVICE,
                                         INFOPOP_PWPR_CHECKBOX_TEST_SERVICE );

    monitorService = utils.createCheckbox( testGroup, ConsumptionUIMessages.CHECKBOX_MONITOR_WEBSERVICE,
    		ConsumptionUIMessages.TOOLTIP_PWPR_CHECKBOX_MONITOR_SERVICE,
                                         INFOPOP_PWPR_CHECKBOX_MONITOR_SERVICE );
    
    if( displayPreferences_ )
    {
      // Create the preferences widgets.
      preferencesWidget_ = new PreferencesSelectionWidget();
      preferencesWidget_.addControls( serverComposite, statusListener );
    }
    
    return this;
  }

  private void handleStartPressed()
  {
    boolean enabled = startService_.getSelection();
    
    testService_.setEnabled( enabled );
    monitorService.setEnabled(enabled);
    launchUddi_.setEnabled( enabled );
    generateProxy_.setEnabled( enabled );
    clientWidget_.enableWidget( getGenerateProxy().booleanValue() );
  }
  
  public void setClientTypeRuntimeServer( TypeRuntimeServer ids )
  {
    clientWidget_.setTypeRuntimeServer( ids );
  }
  
  public TypeRuntimeServer getClientTypeRuntimeServer()
  {
    return clientWidget_.getTypeRuntimeServer();  
  }
  
  public void setServiceTypeRuntimeServer( TypeRuntimeServer ids )
  {
    LabelsAndIds                        labelIds   = WebServiceRuntimeExtensionUtils2.getServiceTypeLabels();
	//rskreg
    int                                 selection  = 0;
    String[]                            serviceIds = labelIds.getIds_();
    String                              selectedId = ids.getTypeId();
    
    webserviceType_.removeListener( SWT.Modify, statusListener_ );
    webserviceType_.setItems( labelIds.getLabels_() );
        
    // Now find the selected one.
    for( int index = 0; index < serviceIds.length; index++ )
    {
      if( selectedId.equals( serviceIds[index ]) )
      {
        selection = index;
        break;
      }
    }
    
    webserviceType_.select( selection );
    webserviceType_.addListener( SWT.Modify, statusListener_ );
    ids_      = ids;  
    labelIds_ = labelIds;
  }
  
  public TypeRuntimeServer getServiceTypeRuntimeServer()
  {
    int selectionIndex = webserviceType_.getSelectionIndex();
    
    ids_.setTypeId( labelIds_.getIds_()[selectionIndex] );
    
    return ids_;  
  }
  
  public Boolean getStartService()
  {
    return new Boolean( startService_.getSelection() );  
  }
  
  public void setStartService( Boolean value )
  {
    startService_.setSelection( value.booleanValue() );
  }

  public Boolean getTestService()
  {
    return new Boolean( testService_.getSelection() && startService_.getSelection() );
  }
  
  public void setTestService( Boolean value )
  {
    testService_.setSelection( value.booleanValue() );  
  }
  
  public Boolean getMonitorService()
  {
    return new Boolean(monitorService.getSelection() && startService_.getSelection());
  }
  
  public void setMonitorService(Boolean value)
  {
    monitorService.setSelection(value.booleanValue());
  }

  public Boolean getPublishService()
  {
    return new Boolean( launchUddi_.getSelection() && startService_.getSelection() );
  }
  
  public void setPublishService( Boolean value )
  {
    launchUddi_.setSelection( value.booleanValue() );  
  }

  public Boolean getGenerateProxy()
  {
    return new Boolean( generateProxy_.getSelection() && startService_.getSelection() );
  }
  
  public void setGenerateProxy( Boolean value )
  {
    generateProxy_.setSelection( value.booleanValue() );  
  }

  public ResourceContext getResourceContext()
  {
    return preferencesWidget_.getResourceContext(); 
  }

  public void setResourceContext( ResourceContext context )
  {
    preferencesWidget_.setResourceContext( context );
  }

  public void internalize()
  {
    handleStartPressed();  
  }
  
  public IStatus getStatus()
  {
    IStatus status = Status.OK_STATUS;
    
    // If the webservice has not been selected then user can not move
    // forward to the next page.
    if( webserviceType_.getText().equals("") )
    {
      status = StatusUtils.errorStatus( "" );
    }
    
    return status;
  }
}
