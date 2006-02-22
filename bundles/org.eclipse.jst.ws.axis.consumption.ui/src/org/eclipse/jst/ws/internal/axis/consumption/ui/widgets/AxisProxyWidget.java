/*******************************************************************************
 * Copyright (c) 2004, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060216   115144 pmoogk@ca.ibm.com - Peter Moogk
 *******************************************************************************/
package org.eclipse.jst.ws.internal.axis.consumption.ui.widgets;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jst.ws.internal.axis.consumption.ui.AxisConsumptionUIMessages;
import org.eclipse.jst.ws.internal.common.ResourceUtils;
import org.eclipse.jst.ws.internal.ui.common.UIUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.command.internal.env.ui.widgets.SimpleWidgetDataContributor;
import org.eclipse.wst.command.internal.env.ui.widgets.WidgetDataEvents;


public class AxisProxyWidget extends SimpleWidgetDataContributor
{
  private String pluginId_ = "org.eclipse.jst.ws.axis.consumption.ui";
  
  /*CONTEXT_ID PWJB0001 for the WSDL to Java Bindings Page*/
  private final String INFOPOP_PWJB_PAGE = "PWJB0001";	//$NON-NLS-1$
		
  private Combo outputFolderCombo_;
  /*CONTEXT_ID PWJB0003 for the Folder field of the WSDL to Java Bindings Page*/
  private final String INFOPOP_PWJB_TEXT_FOLDER = "PWJB0003";	//$NON-NLS-1$

  private Button genProxyCheckbox_;
  /*CONTEXT_ID PWJB0009 Indicates whether to generate a proxy or not. */
  private final String INFOPOP_PWJB_CHECKBOX_GENPROXY = "PWJB0009";	//$NON-NLS-1$
		
  private Button showMappingsCheckbox_;
  /*CONTEXT_ID PWJB0016 for the Show Mappings checkbox of the Bean Methods Page*/
  private String INFOPOP_N2P_SHOW_MAPPINGS = "PWJB0016"; //$NON-NLS-1$
  
  public WidgetDataEvents addControls( Composite parent, Listener statusListener )
  {
    UIUtils      uiUtils  = new UIUtils( pluginId_ );
    
	parent.setToolTipText( AxisConsumptionUIMessages.TOOLTIP_PWJB_PAGE );
	PlatformUI.getWorkbench().getHelpSystem().setHelp( parent, pluginId_ + "." +  INFOPOP_PWJB_PAGE);
    
    genProxyCheckbox_ = uiUtils.createCheckbox( parent, AxisConsumptionUIMessages.CHECKBOX_GENPROXY,
    											AxisConsumptionUIMessages.TOOLTIP_PWJB_CHECKBOX_GENPROXY,
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
    
    outputFolderCombo_ = uiUtils.createCombo( textGroup, AxisConsumptionUIMessages.LABEL_FOLDER_NAME,
    									AxisConsumptionUIMessages.TOOLTIP_PWJB_TEXT_FOLDER,
    									INFOPOP_PWJB_TEXT_FOLDER,
    									SWT.SINGLE | SWT.BORDER | SWT.READ_ONLY );
    
    showMappingsCheckbox_ = uiUtils.createCheckbox( parent, AxisConsumptionUIMessages.LABEL_EXPLORE_MAPPINGS_XML2BEAN,
    									AxisConsumptionUIMessages.TOOLTIP_N2P_SHOW_MAPPINGS,
    									INFOPOP_N2P_SHOW_MAPPINGS );
    // Since this widget affects whether the next page is shown or not we
    // need to add the statusListener.
    showMappingsCheckbox_.addListener( SWT.Selection, statusListener );
    
    return this;
  }
  
  private void handleGenProxy()
  {
    boolean enabled = genProxyCheckbox_.getSelection();
    
    outputFolderCombo_.setEnabled( enabled );
    showMappingsCheckbox_.setEnabled( enabled );
  }
  
  public void setClientProject(IProject clientProject)
  {  	
    IPath[] paths = ResourceUtils.getAllJavaSourceLocations(clientProject);
    
    for (int i = 0; i < paths.length ; i++)
    {
      outputFolderCombo_.add(paths[i].toString());
    }
    
    if( paths.length > 0 )
    {
    	outputFolderCombo_.select(0);
    }
  }
  
  public String getOutputFolder()
  {
  	return outputFolderCombo_.getText();
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
