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
package org.eclipse.jst.ws.internal.consumption.ui.widgets;

import org.eclipse.jst.ws.internal.data.TypeRuntimeServer;
import org.eclipse.jst.ws.internal.ui.common.UIUtils;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.command.env.core.common.MessageUtils;
import org.eclipse.wst.command.env.core.context.ResourceContext;
import org.eclipse.wst.command.env.ui.widgets.SimpleWidgetDataContributor;
import org.eclipse.wst.command.env.ui.widgets.WidgetDataEvents;


public class ClientWizardWidget extends SimpleWidgetDataContributor
{  
  private WebServiceClientTypeWidget clientWidget_;
  private PreferencesSelectionWidget preferencesWidget_;
  
  /*CONTEXT_ID PWPR0013 for the test service checkbox of the project page*/  
  private String INFOPOP_PWPR_CHECKBOX_TEST_SERVICE = "PWPR0013";
  private Button testService_;
  
  /*CONTEXT_ID PWPR0014 for the monitor service checkbox of the project page*/
  private String INFOPOP_PWPR_CHECKBOX_MONITOR_SERVICE = "PWRPR0014";
  private Button monitorService;
  
  /*CONTEXT_ID PWPR0001 for the Project Page*/
  private String INFOPOP_PWPR_PAGE = "PWPR0001";
    
  public WidgetDataEvents addControls( Composite parent, Listener statusListener)
  {
    String       pluginId = "org.eclipse.jst.ws.consumption.ui";
    MessageUtils msgUtils = new MessageUtils( pluginId + ".plugin", this );
    UIUtils      utils    = new UIUtils( msgUtils, pluginId );
    
    PlatformUI.getWorkbench().getHelpSystem().setHelp( parent, pluginId + "." +  INFOPOP_PWPR_PAGE );
  	
  	Composite clientComposite = utils.createComposite( parent, 1 );
  	
    clientWidget_ = new WebServiceClientTypeWidget();
    clientWidget_.addControls(clientComposite, statusListener );
    
    
    //  Create test service check box.
    Composite testGroup = utils.createComposite(clientComposite,1);
    
    testService_ = utils.createCheckbox( testGroup, "CHECKBOX_TEST_WEBSERVICE",
                                         "TOOLTIP_PWPR_CHECKBOX_TEST_SERVICE",
                                         INFOPOP_PWPR_CHECKBOX_TEST_SERVICE );
    
    // Create monitor service check box.
    monitorService = utils.createCheckbox(testGroup, "CHECKBOX_MONITOR_WEBSERVICE", "TOOLTIP_PWPR_CHECKBOX_MONITOR_SERVICE", INFOPOP_PWPR_CHECKBOX_MONITOR_SERVICE);

    // Create the preferences controls.
    preferencesWidget_ = new PreferencesSelectionWidget();
    preferencesWidget_.addControls( clientComposite, statusListener );
    
    return this;
  }
  
  public void setResourceContext( ResourceContext context )
  {
    preferencesWidget_.setResourceContext( context );
  }
   
  public ResourceContext getResourceContext()
  {
    return preferencesWidget_.getResourceContext(); 
  }
  
  public void setClientTypeRuntimeServer( TypeRuntimeServer ids )
  {
    clientWidget_.setTypeRuntimeServer( ids );
  }
  
  public TypeRuntimeServer getClientTypeRuntimeServer()
  {
    return clientWidget_.getTypeRuntimeServer();  
  }
  
  public Boolean getTestService()
  {
    return new Boolean( testService_.getSelection() );
  }
  
  public void setTestService( Boolean value )
  {
    testService_.setSelection( value.booleanValue() );  
  }
  
  public Boolean getMonitorService()
  {
    return new Boolean(monitorService.getSelection());
  }
  
  public void setMonitorService(Boolean value)
  {
    monitorService.setSelection(value.booleanValue());
  }
}