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
package org.eclipse.jst.ws.internal.creation.ui.widgets.test;

import java.util.List;
import java.util.Vector;

import org.eclipse.jst.ws.internal.common.EnvironmentUtils;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.test.WSDLTestLaunchCommand;
import org.eclipse.jst.ws.internal.data.TypeRuntimeServer;
import org.eclipse.jst.ws.internal.ui.common.UIUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.wst.command.internal.env.ui.widgets.SimpleWidgetDataContributor;
import org.eclipse.wst.command.internal.env.ui.widgets.WidgetDataEvents;
import org.eclipse.wst.command.internal.provisional.env.core.common.Environment;
import org.eclipse.wst.command.internal.provisional.env.core.common.MessageUtils;
import org.eclipse.wst.command.internal.provisional.env.core.common.Status;
import org.eclipse.wst.command.internal.provisional.env.core.selection.SelectionList;


public class ServiceTestWidget extends SimpleWidgetDataContributor
{
  private String pluginId_ = "org.eclipse.jst.ws.consumption.ui";
  private String createPluginId_ = "org.eclipse.jst.ws.creation.ui";

  private Combo testTypeCombo_;
  /*CONTEXT_ID PSTP0001 for the Test Type Combo box of the Service Test Page*/
  private final String INFOPOP_PSTP_COMBOBOX_TEST = "PSTP0001";
  private final String TOOLTIP_PSTP_COMBOBOX_TEST = "TOOLTIP_PSTP_COMBOBOX_TEST";

  private Button launchButton_;
  /*CONTEXT_ID PSTP0002 for the launch button of the Service Test Page*/
  private final String INFOPOP_PSTP_LAUNCH_BUTTON = "PSTP0002";
  private final String TOOLTIP_PSTP_LAUNCH_BUTTON = "TOOLTIP_PSTP_LAUNCH_BUTTON";
  
  private SelectionList facilities_;
  
  private String serviceServerInstanceId = null;
  
  public WidgetDataEvents addControls( Composite parent, Listener statusListener )
  {
    MessageUtils msgUtils = new MessageUtils( pluginId_ + ".plugin", this );
    UIUtils      uiUtils  = new UIUtils(msgUtils, createPluginId_ );
        
    Composite testComposite = uiUtils.createComposite( parent, 3, 0, 0 );
    
    testTypeCombo_ = uiUtils.createCombo( testComposite, "LABEL_TEST_TYPES",
                                          TOOLTIP_PSTP_COMBOBOX_TEST,
                                          INFOPOP_PSTP_COMBOBOX_TEST,
                                          SWT.SINGLE | SWT.BORDER | SWT.READ_ONLY );
    
    launchButton_ = uiUtils.createPushButton( testComposite, "BUTTON_LAUNCH_SERVICE_TEST",
                                          TOOLTIP_PSTP_LAUNCH_BUTTON,
                                          INFOPOP_PSTP_LAUNCH_BUTTON );
    launchButton_.addSelectionListener( new SelectionAdapter()
                                        {
                                          public void widgetSelected( SelectionEvent event )
                                          {
                                            handleLaunchButton();
                                          }
                                        });
    
     
    return this;
  }
  
  private TypeRuntimeServer serviceids;
  private String serverProject;
  private String module;
  private String wsdlURI;
  private String launchedServiceTestName = "";
  private Environment env;
  private List endpoints;
  
  private void handleLaunchButton()
  {
	// Split up the project and module
	int p = serverProject.indexOf("/");
	if (p != -1){
		module = serverProject.substring(p+1);
		serverProject = serverProject.substring(0,p);
	}
  	
  	String testID = testTypeCombo_.getText();
  	launchedServiceTestName = testID;
  	WSDLTestLaunchCommand wtlc = new WSDLTestLaunchCommand();
  	wtlc.setTestID(testID);
  	wtlc.setServiceTypeRuntimeServer(serviceids);
	wtlc.setServiceServerInstanceId(serviceServerInstanceId);
  	wtlc.setServerProject(serverProject);
	wtlc.setServerModule(module);
  	wtlc.setWsdlURI(wsdlURI);
  	wtlc.setExternalBrowser(true);
  	wtlc.setEndpoint(endpoints);
  	wtlc.setEnvironment( env );
  	Status status = EnvironmentUtils.convertIStatusToStatus(wtlc.execute( null, null ));
  }
  
  public void setServiceTestFacilities( SelectionList facilities )
  {
    facilities_ = facilities;
    testTypeCombo_.setItems( facilities.getList() );
    testTypeCombo_.select( facilities.getIndex() );
  }
  
  public SelectionList getServiceTestFacilities()
  {
    facilities_.setIndex( testTypeCombo_.getSelectionIndex() );
    return facilities_;
  }

  public String getTestID()
  {
  	return testTypeCombo_.getText();
  }
  
  public void setWsdlURI(String wsdlURI)
  {
  	this.wsdlURI = wsdlURI;
  }
    
  public void setServerProject(String serverProject)
  {
    this.serverProject = serverProject;
  }

  public void setServiceTypeRuntimeServer(TypeRuntimeServer serviceids)
  {
    this.serviceids = serviceids;
  }
  
  public String getLaunchedServiceTestName()
  {
    return launchedServiceTestName;
  }

  public void setEnvironment(Environment env)
  {
  	this.env = env;
  }

  public void setEndpoint(String endpoint)
  {
    if (endpoint != null && endpoint.length() > 0)
    {
      Vector v = new Vector();
      v.add(endpoint);
      setEndpoints(v);
    }
  }
  
  public void setEndpoints(List endpoints)
  {
    this.endpoints = endpoints;
  }
  
  public void setServiceServerInstanceId(String ssInstanceId){
	  this.serviceServerInstanceId = ssInstanceId;
  }
  
}
