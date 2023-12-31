/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.internal.consumption.ui.widgets;

import org.eclipse.jst.ws.internal.ui.WSUIPluginMessages;
import org.eclipse.jst.ws.internal.ui.common.UIUtils;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.wst.command.internal.env.core.context.ResourceContext;
import org.eclipse.wst.command.internal.env.ui.widgets.SimpleWidgetDataContributor;
import org.eclipse.wst.command.internal.env.ui.widgets.WidgetDataEvents;


public class PreferencesSelectionWidget extends SimpleWidgetDataContributor
{    
  private ResourceContext context_;
  
  /*CONTEXT_ID PWPR0003 for the Overwrite Files check box of the Project Page*/
  private String INFOPOP_PWPR_CHECKBOX_OVERWRITE_FILES = "PWPR0003";
  private Button overwriteFilesCheckbox_;
  
  /*CONTEXT_ID PWPR0004 for the Create Folders check box of the Project Page*/
  private String INFOPOP_PWPR_CHECKBOX_CREATE_FOLDERS = "PWPR0004";
  private Button createFoldersCheckbox_;
  
  /*CONTEXT_ID PWPR0015 for the Check Out Files check box of the Project Page*/
  private String INFOPOP_PWPR_CHECKBOX_CHECKOUT_FILES = "PWPR0015";  
  private Button checkoutFilesCheckbox_;
  
  /* (non-Javadoc)
   * @see org.eclipse.wst.command.env.ui.widgets.WidgetContributor#addControls(org.eclipse.swt.widgets.Composite, org.eclipse.swt.widgets.Listener)
   */
  public WidgetDataEvents addControls( Composite parent, Listener statusListener)
  {
    String       conPluginId = "org.eclipse.jst.ws.consumption.ui";
  	UIUtils      utils       = new UIUtils( conPluginId );
  	
    Composite resourcesGroup = utils.createComposite( parent, 1 );
    
    overwriteFilesCheckbox_ = utils.createCheckbox( resourcesGroup, WSUIPluginMessages.BUTTON_OVERWRITE_FILES,
    		WSUIPluginMessages.TOOLTIP_PPRM_CHECKBOX_OVERWRITE_FILES, 
                                                    INFOPOP_PWPR_CHECKBOX_OVERWRITE_FILES );
                                                 
    createFoldersCheckbox_ = utils.createCheckbox( resourcesGroup, WSUIPluginMessages.BUTTON_CREATE_FOLDERS,
    		WSUIPluginMessages.TOOLTIP_PPRM_CHECKBOX_CREATE_FOLDERS , 
                                                   INFOPOP_PWPR_CHECKBOX_CREATE_FOLDERS );
                                                      
    checkoutFilesCheckbox_ = utils.createCheckbox( resourcesGroup, WSUIPluginMessages.BUTTON_CHECKOUT_FILES,
    		WSUIPluginMessages.TOOLTIP_PPRM_CHECKBOX_CHECK_OUT, 
                                                   INFOPOP_PWPR_CHECKBOX_CHECKOUT_FILES );
    
    return this;
  }

  public void setResourceContext( ResourceContext context )
  { 
    overwriteFilesCheckbox_.setSelection( context.isOverwriteFilesEnabled() );
    createFoldersCheckbox_.setSelection( context.isCreateFoldersEnabled() );
    checkoutFilesCheckbox_.setSelection( context.isCheckoutFilesEnabled() ); 
    
    context_ = context;
  }
  
  public ResourceContext getResourceContext() 
  {    
    context_.setOverwriteFilesEnabled( overwriteFilesCheckbox_.getSelection() );
    context_.setCreateFoldersEnabled( createFoldersCheckbox_.getSelection() );
    context_.setCheckoutFilesEnabled( checkoutFilesCheckbox_.getSelection() );
    
    return context_;
  }
}
