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
package org.eclipse.jst.ws.internal.uddiregistry.widgets;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jst.ws.internal.uddiregistry.UDDIRegistryMessages;
import org.eclipse.jst.ws.internal.uddiregistry.plugin.WebServiceUDDIRegistryPlugin;
import org.eclipse.jst.ws.internal.uddiregistry.wizard.PrivateUDDIRegistryType;
import org.eclipse.jst.ws.internal.uddiregistry.wizard.PrivateUDDIRegistryTypeRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.help.IWorkbenchHelpSystem;
import org.eclipse.wst.command.internal.env.core.common.StatusUtils;
import org.eclipse.wst.command.internal.env.ui.widgets.SimpleWidgetDataContributor;
import org.eclipse.wst.command.internal.env.ui.widgets.WidgetDataEvents;


/**
* This UDDI configuration page prompts the user
* for the information required to configure the
* unit test UDDI registry.
*/
public class PrivateUDDISelectionWidget extends SimpleWidgetDataContributor
{
  private Listener statusListener;

  private Button deployRadio_;
  private Label typesLabel_;
  private Combo typesCombo_;
  private Button updateRadio_;
  private Button removeRadio_;
  private PrivateUDDIRegistryType[] types_;
  private PrivateUDDIRegistryType installedType_;

  // Infopop
  private final String INFOPOP_PUPR_PRIVATE_UDDI_PAGE = WebServiceUDDIRegistryPlugin.ID + ".pupr0001";
  private final String INFOPOP_PUPR_PRIVATE_UDDI_TYPE = WebServiceUDDIRegistryPlugin.ID + ".pupr0002";
  private final String INFOPOP_PUPR_DEPLOY_PRIVATE_UDDI = WebServiceUDDIRegistryPlugin.ID + ".pupr0003";
  private final String INFOPOP_PUPR_UPDATE_PRIVATE_UDDI = WebServiceUDDIRegistryPlugin.ID + ".pupr0005";
  private final String INFOPOP_PUPR_REMOVE_PRIVATE_UDDI = WebServiceUDDIRegistryPlugin.ID + ".pupr0004";

  public PrivateUDDISelectionWidget()
  {
  }

  public WidgetDataEvents addControls(Composite parent, Listener statusListener)
  {
  	this.statusListener = statusListener;
    parent.setToolTipText(UDDIRegistryMessages.TOOLTIP_PUPR_PRIVATE_UDDI_PAGE);
    PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, INFOPOP_PUPR_PRIVATE_UDDI_PAGE);

    GridLayout gl;
    GridData gd;
    
    Listener listener = new Listener()
    {
      public void handleEvent(Event event)
      {
      	handleWidgetEvent(event);
      }
    };

    IWorkbenchHelpSystem helpSystem = PlatformUI.getWorkbench().getHelpSystem();
    
    deployRadio_ = new Button(parent, SWT.RADIO);
    deployRadio_.setText(UDDIRegistryMessages.BUTTON_DEPLOY_UDDI_REGISTRY);
    deployRadio_.setSelection(true);
    deployRadio_.addListener(SWT.Selection, listener);
    deployRadio_.setToolTipText(UDDIRegistryMessages.TOOLTIP_PUPR_DEPLOY_PRIVATE_UDDI);
    helpSystem.setHelp(deployRadio_, INFOPOP_PUPR_DEPLOY_PRIVATE_UDDI);

    Composite typeComposite = new Composite(parent,SWT.NONE);
    gl = new GridLayout();
    gl.numColumns = 2;
    gl.marginHeight = 5;
    gl.verticalSpacing = 15;
    gl.marginWidth = 20;
    typeComposite.setLayout(gl);
    gd = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
    typeComposite.setLayoutData(gd);

    typesLabel_ = new Label(typeComposite, SWT.WRAP);
    typesLabel_.setText(UDDIRegistryMessages.LABEL_PRIVATE_UDDI_REGISTRY_TYPES);
    typesLabel_.setToolTipText(UDDIRegistryMessages.TOOLTIP_PUPR_PRIVATE_UDDI_TYPE);
    helpSystem.setHelp(typesLabel_, INFOPOP_PUPR_PRIVATE_UDDI_TYPE);

    typesCombo_ = new Combo(typeComposite, SWT.DROP_DOWN | SWT.READ_ONLY);
    gd = new GridData(GridData.FILL_HORIZONTAL);
    typesCombo_.setLayoutData(gd);
    typesCombo_.addListener(SWT.Selection, listener);
    typesCombo_.setToolTipText(UDDIRegistryMessages.TOOLTIP_PUPR_PRIVATE_UDDI_TYPE);
    helpSystem.setHelp(typesCombo_, INFOPOP_PUPR_PRIVATE_UDDI_TYPE);

    updateRadio_ = new Button(parent,SWT.RADIO);
    updateRadio_.setText(UDDIRegistryMessages.BUTTON_UPDATE_UDDI_REGISTRY);
    updateRadio_.setSelection(false);
    updateRadio_.addListener(SWT.Selection,listener);
    updateRadio_.setToolTipText(UDDIRegistryMessages.TOOLTIP_PUPR_UPDATE_PRIVATE_UDDI);
    helpSystem.setHelp(updateRadio_, INFOPOP_PUPR_UPDATE_PRIVATE_UDDI);

    removeRadio_ = new Button(parent, SWT.RADIO);
    removeRadio_.setText(UDDIRegistryMessages.BUTTON_REMOVE_UDDI_REGISTRY);
    removeRadio_.setSelection(false);
    removeRadio_.addListener(SWT.Selection, listener);
    removeRadio_.setToolTipText(UDDIRegistryMessages.TOOLTIP_PUPR_REMOVE_PRIVATE_UDDI);
    helpSystem.setHelp(removeRadio_, INFOPOP_PUPR_REMOVE_PRIVATE_UDDI);                

    loadTypes();
    init();
    return this;
  }

  private final void loadTypes()
  {
    types_ = PrivateUDDIRegistryTypeRegistry.getInstance().getTypes();
  }

  private final void init()
  {
    for (int i = 0; i < types_.length; i++)
    {
      if (types_[i] != null)
      {
        typesCombo_.add(types_[i].getName());
        if (types_[i].isPrivateUDDIRegistryInstalled())
          installedType_ = types_[i];
      }
    }
    if (typesCombo_.getItemCount() > 0)
      typesCombo_.setText(typesCombo_.getItem(0));
    if (installedType_ != null)
    {
      updateRadio_.setEnabled(true);
      removeRadio_.setEnabled(true);                
    }
    else
    {
      updateRadio_.setEnabled(false);
      removeRadio_.setEnabled(false);
    }
  }

  private void handleWidgetEvent(Event event)
  {
    if (event.widget == removeRadio_ || event.widget == updateRadio_)
    {
      boolean isUpdateOrRemoveSelected = ((Button)event.widget).getSelection();
      typesLabel_.setEnabled(!isUpdateOrRemoveSelected);
      typesCombo_.setEnabled(!isUpdateOrRemoveSelected);
    }
    else if (event.widget == deployRadio_)
    {
      boolean isDeploySelected = ((Button)event.widget).getSelection();
      typesLabel_.setEnabled(isDeploySelected);
      typesCombo_.setEnabled(isDeploySelected);
    }
    statusListener.handleEvent(event);
  }
  
  public PrivateUDDIRegistryType getPrivateUDDIRegistryType()
  {
  	if (deployRadio_.getSelection())
  	{
  	  String selectedRegistryName = typesCombo_.getText();
  	  for (int i = 0; i < types_.length; i++)
        if (selectedRegistryName.equals(types_[i].getName()))
  	  	  return types_[i];
      return null;
  	}
  	else
  	  return installedType_;
  }
  
  public byte getOperationType()
  {
  	if (deployRadio_.getSelection())
  	  return PrivateUDDIRegistryType.OP_DEPLOY;
  	else if (updateRadio_.getSelection())
      return PrivateUDDIRegistryType.OP_UPDATE;
  	else if (removeRadio_.getSelection())
      return PrivateUDDIRegistryType.OP_REMOVE;
  	else
  	  return PrivateUDDIRegistryType.OP_DEPLOY;
  }

  public IStatus getStatus()
  {
    PrivateUDDIRegistryType privateUDDIRegistryType = getPrivateUDDIRegistryType();
    if (privateUDDIRegistryType != null)
      return privateUDDIRegistryType.getOperationStatus(getOperationType());
    else
      return StatusUtils.errorStatus( UDDIRegistryMessages.MSG_ERROR_NO_UDDI_REGISTRY_AVAILABLE );
  }
}