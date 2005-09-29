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

import org.eclipse.jst.ws.internal.consumption.ui.wsrt.WebServiceRuntimeExtensionUtils;
import org.eclipse.jst.ws.internal.data.LabelsAndIds;
import org.eclipse.jst.ws.internal.data.TypeRuntimeServer;
import org.eclipse.jst.ws.internal.ui.common.UIUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.wst.command.internal.env.ui.widgets.SimpleWidgetDataContributor;
import org.eclipse.wst.command.internal.env.ui.widgets.WidgetDataEvents;
import org.eclipse.wst.command.internal.provisional.env.core.common.MessageUtils;


public class WebServiceClientTypeWidget extends SimpleWidgetDataContributor
{     
  /*CONTEXT_ID PWPR0013 for the Wizard Scenario Client group of the Project Page*/
  private String INFOPOP_PWPR_GROUP_SCENARIO_CLIENT = "PWPR0013";
  
  /*CONTEXT_ID PWPR0014 for the Web Service Client Type combo box of the Project Page*/
  private String INFOPOP_PWPR_COMBO_CLIENTTYPE = "PWPR0014";
  private Combo  clientTypeCombo_;
      
  private TypeRuntimeServer ids_;
  private LabelsAndIds      labelIds_;
    
  /* (non-Javadoc)
   * @see org.eclipse.wst.command.env.ui.widgets.WidgetContributor#addControls(org.eclipse.swt.widgets.Composite, org.eclipse.swt.widgets.Listener)
   */
  public WidgetDataEvents addControls( Composite parent, Listener statusListener)
  {
    String       pluginId = "org.eclipse.jst.ws.consumption.ui";
    MessageUtils msgUtils = new MessageUtils( pluginId + ".plugin", this );
    UIUtils      utils    = new UIUtils( msgUtils, pluginId );
  	
    Group clientGroup = utils.createGroup( parent, 
                                           "GROUP_SCENARIO_CLIENT",
                                           "TOOLTIP_PWPR_GROUP_SCENARIO_CLIENT", 
                                           INFOPOP_PWPR_GROUP_SCENARIO_CLIENT,
										   2, 10, 10 );
                                             
    
    int comboStyle = SWT.SINGLE | SWT.BORDER | SWT.READ_ONLY;
    clientTypeCombo_ = utils.createCombo( clientGroup, 
                                          "LABEL_WEBSERVICECLIENTTYPE",
                                          "TOOLTIP_PWPR_COMBO_CLIENTTYPE", 
                                          INFOPOP_PWPR_COMBO_CLIENTTYPE, 
                                          comboStyle );
                                            
    
    
    return this;
  }
  
  public void enableWidget( boolean enable )
  {
    clientTypeCombo_.setEnabled( enable );
  }

  public void setTypeRuntimeServer( TypeRuntimeServer ids )
  {
		// rskreg
    //WebServiceClientTypeRegistry registry   = WebServiceClientTypeRegistry.getInstance();
    //LabelsAndIds                 labelIds   = registry.getClientTypeLabels();
		LabelsAndIds                 labelIds   = WebServiceRuntimeExtensionUtils.getClientTypeLabels();
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
      
  
}
