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

import org.eclipse.jst.ws.internal.consumption.ui.ConsumptionUIMessages;
import org.eclipse.jst.ws.internal.consumption.ui.wsrt.WebServiceRuntimeExtensionUtils2;
import org.eclipse.jst.ws.internal.data.LabelsAndIds;
import org.eclipse.jst.ws.internal.data.TypeRuntimeServer;
import org.eclipse.jst.ws.internal.ui.common.UIUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.wst.command.internal.env.ui.widgets.SimpleWidgetDataContributor;
import org.eclipse.wst.command.internal.env.ui.widgets.WidgetDataEvents;


public class WebServiceClientTypeWidget extends SimpleWidgetDataContributor
{     
  /*CONTEXT_ID PWPR0013 for the Wizard Scenario Client group of the Project Page*/
  private String INFOPOP_PWPR_GROUP_SCENARIO_CLIENT = "PWPR0013";
  
  /*CONTEXT_ID PWPR0014 for the Web Service Client Type combo box of the Project Page*/
  private String INFOPOP_PWPR_COMBO_CLIENTTYPE = "PWPR0014";

  /*CONTEXT_ID PWPR0016 for the Install Client check box of the Scenario page of Service and Client wizards*/
  private String INFOPOP_PWPR_CHECKBOX_INSTALL_CLIENT = "PWPR0016";
  
  private Combo  clientTypeCombo_;
  
  private Button installClient_;
      
  private TypeRuntimeServer ids_;
  private LabelsAndIds      labelIds_;
    
  /* (non-Javadoc)
   * @see org.eclipse.wst.command.env.ui.widgets.WidgetContributor#addControls(org.eclipse.swt.widgets.Composite, org.eclipse.swt.widgets.Listener)
   */
  public WidgetDataEvents addControls( Composite parent, Listener statusListener)
  {
    String       pluginId = "org.eclipse.jst.ws.consumption.ui";
    UIUtils      utils    = new UIUtils( pluginId );
  	
    Group clientGroup = utils.createGroup( parent, 
    		ConsumptionUIMessages.GROUP_SCENARIO_CLIENT,
    		ConsumptionUIMessages.TOOLTIP_PWPR_GROUP_SCENARIO_CLIENT, 
                                           INFOPOP_PWPR_GROUP_SCENARIO_CLIENT,
										   2, 10, 10 );                                             
    
    
    int comboStyle = SWT.SINGLE | SWT.BORDER | SWT.READ_ONLY;
    clientTypeCombo_ = utils.createCombo( clientGroup, 
    		ConsumptionUIMessages.LABEL_WEBSERVICECLIENTTYPE,
    		ConsumptionUIMessages.TOOLTIP_PWPR_COMBO_CLIENTTYPE, 
                                          INFOPOP_PWPR_COMBO_CLIENTTYPE, 
                                          comboStyle );
           
    Composite buttonGroup = utils.createComposite( clientGroup, 1 );
    GridData  buttonGrid   = new GridData();
    buttonGrid.horizontalSpan = 2;
    buttonGroup.setLayoutData( buttonGrid );
    
    installClient_ = utils.createCheckbox( buttonGroup, ConsumptionUIMessages.BUTTON_INSTALL_CLIENT_WEB_PROJECT,
			ConsumptionUIMessages.TOOLTIP_PWPR_CHECKBOX_INSTALL_CLIENT_WEB_PROJECT,
			INFOPOP_PWPR_CHECKBOX_INSTALL_CLIENT );
    
    
    
    return this;
  }
  
  public void enableWidget( boolean enable )
  {
    clientTypeCombo_.setEnabled( enable );
    installClient_.setEnabled( enable );
  }

  public void setTypeRuntimeServer( TypeRuntimeServer ids )
  {
		// rskreg
    //WebServiceClientTypeRegistry registry   = WebServiceClientTypeRegistry.getInstance();
    //LabelsAndIds                 labelIds   = registry.getClientTypeLabels();
		LabelsAndIds                 labelIds   = WebServiceRuntimeExtensionUtils2.getClientTypeLabels();
    int                          selection  = 0;
    String[]                     clientIds  = labelIds.getIds_();
    String                       selectedId = ids.getTypeId();
    
		// rskreg
    clientTypeCombo_.setItems( labelIds.getLabels_() );
    
    // Now find the selected one.
    for( int index = 0; index < clientIds.length; index++ )
    {
      if( selectedId.equals( clientIds[index ]) )
      {
        selection = index;
        break;
      }
    }
    
    clientTypeCombo_.select( selection );
    ids_      = ids;  
    labelIds_ = labelIds;
  }
  
  public TypeRuntimeServer getTypeRuntimeServer()
  {
    int selectionIndex = clientTypeCombo_.getSelectionIndex();
    
    ids_.setTypeId( labelIds_.getIds_()[selectionIndex] );
    
    return ids_;  
  }
      
  public Boolean getInstallClient()
  {
    return new Boolean( installClient_.getSelection() );
  }
  
  public void setInstallClient( Boolean value )
  {
    installClient_.setSelection( value.booleanValue() );  
  }
  

}
