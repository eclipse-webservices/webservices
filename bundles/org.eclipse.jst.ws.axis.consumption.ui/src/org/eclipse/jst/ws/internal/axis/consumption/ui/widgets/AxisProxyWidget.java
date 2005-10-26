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
package org.eclipse.jst.ws.internal.axis.consumption.ui.widgets;

import org.eclipse.jst.ws.internal.ui.common.UIUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.command.internal.env.core.common.MessageUtils;
import org.eclipse.wst.command.internal.env.ui.widgets.SimpleWidgetDataContributor;
import org.eclipse.wst.command.internal.env.ui.widgets.WidgetDataEvents;


public class AxisProxyWidget extends SimpleWidgetDataContributor
{
  private String pluginId_ = "org.eclipse.jst.ws.axis.consumption.ui";
  
  /*CONTEXT_ID PWJB0001 for the WSDL to Java Bindings Page*/
  private final String INFOPOP_PWJB_PAGE = "PWJB0001";	//$NON-NLS-1$
  private final String TOOLTIP_PWJB_PAGE = "TOOLTIP_PWJB_PAGE";
		
  private Text folderText_;
  /*CONTEXT_ID PWJB0003 for the Folder field of the WSDL to Java Bindings Page*/
  private final String INFOPOP_PWJB_TEXT_FOLDER = "PWJB0003";	//$NON-NLS-1$
  private final String TOOLTIP_PWJB_TEXT_FOLDER = "TOOLTIP_PWJB_TEXT_FOLDER";

  private Button genProxyCheckbox_;
  /*CONTEXT_ID PWJB0009 Indicates whether to generate a proxy or not. */
  private final String INFOPOP_PWJB_CHECKBOX_GENPROXY = "PWJB0009";	//$NON-NLS-1$
  private final String TOOLTIP_PWJB_CHECKBOX_GENPROXY = "TOOLTIP_PWJB_CHECKBOX_GENPROXY";
		
  private Button showMappingsCheckbox_;
  /*CONTEXT_ID PWJB0016 for the Show Mappings checkbox of the Bean Methods Page*/
  private String INFOPOP_N2P_SHOW_MAPPINGS = "PWJB0016"; //$NON-NLS-1$
  
  public WidgetDataEvents addControls( Composite parent, Listener statusListener )
  {
    MessageUtils msgUtils = new MessageUtils( pluginId_ + ".plugin", this );
    UIUtils      uiUtils  = new UIUtils(msgUtils, pluginId_ );
    
	parent.setToolTipText( msgUtils.getMessage( TOOLTIP_PWJB_PAGE ) );
	PlatformUI.getWorkbench().getHelpSystem().setHelp( parent, pluginId_ + "." +  INFOPOP_PWJB_PAGE);
    
    genProxyCheckbox_ = uiUtils.createCheckbox( parent, "CHECKBOX_GENPROXY",
                                                TOOLTIP_PWJB_CHECKBOX_GENPROXY,
                                                INFOPOP_PWJB_CHECKBOX_GENPROXY );
    genProxyCheckbox_.addListener( SWT.Selection, statusListener );
    
    genProxyCheckbox_.addSelectionListener( new SelectionAdapter()
                                            {
                                              public void widgetSelected( SelectionEvent evt )
                                              {
                                                handleGenProxy();  
                                              }
                                            });
    
    Composite textGroup = uiUtils.createComposite( parent, 2, 5, 0 );
    
    folderText_ = uiUtils.createText( textGroup, "LABEL_FOLDER_NAME",
                                      TOOLTIP_PWJB_TEXT_FOLDER,
                                      INFOPOP_PWJB_TEXT_FOLDER,
                                      SWT.SINGLE | SWT.BORDER | SWT.READ_ONLY );
    folderText_.addListener( SWT.Modify, statusListener );
    
    showMappingsCheckbox_ = uiUtils.createCheckbox( parent, "LABEL_EXPLORE_MAPPINGS_XML2BEAN",
                                                    "TOOLTIP_N2P_SHOW_MAPPINGS",
                                                    INFOPOP_N2P_SHOW_MAPPINGS );
    // Since this widget affects whether the next page is shown or not we
    // need to add the statusListener.
    showMappingsCheckbox_.addListener( SWT.Selection, statusListener );
    
    return this;
  }
  
  private void handleGenProxy()
  {
    boolean enabled = genProxyCheckbox_.getSelection();
    
    folderText_.setEnabled( enabled );
    showMappingsCheckbox_.setEnabled( enabled );
  }
  
  public void setProxyFolder( String proxyFolder )
  {
    folderText_.setText( proxyFolder );  
  }
  
  public String getProxyFolder()
  {
     return folderText_.getText();  
  }
  
  public void setGenerateProxy( Boolean genProxy )
  {
    genProxyCheckbox_.setSelection( genProxy.booleanValue() );
  }    
  
  public Boolean getGenerateProxy()
  {
    return new Boolean( genProxyCheckbox_.getSelection() );
  }   
  
  public void setCustomizeClientMappings( boolean showMappings )
  {
    showMappingsCheckbox_.setSelection( showMappings );
  }            
  
  public boolean getCustomizeClientMappings()
  {
    return showMappingsCheckbox_.getSelection() && genProxyCheckbox_.getSelection();
  }   
  
  public void setIsClientScenario( boolean isClientScenario )
  {
    genProxyCheckbox_.setEnabled( !isClientScenario ); 
  }  
}
