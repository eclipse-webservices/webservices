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

import java.util.Vector;

import org.eclipse.jst.ws.internal.consumption.ui.plugin.WebServiceConsumptionUIPlugin;
import org.eclipse.jst.ws.internal.consumption.ui.wizard.PrivateUDDIRegistryTypeRegistry;
import org.eclipse.jst.ws.internal.consumption.ui.wizard.PublicUDDIRegistryTypeRegistry;
import org.eclipse.jst.ws.internal.consumption.ui.wizard.uddi.PublicUDDIRegistryType;
import org.eclipse.jst.ws.internal.ui.uddi.PrivateUDDIRegistryType;
import org.eclipse.jst.ws.internal.explorer.LaunchOption;
import org.eclipse.jst.ws.internal.explorer.LaunchOptions;
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
import org.eclipse.wst.command.env.core.common.MessageUtils;
import org.eclipse.wst.command.env.ui.widgets.SimpleWidgetDataContributor;
import org.eclipse.wst.command.env.ui.widgets.WidgetDataEvents;


public class PublishWSWidget extends SimpleWidgetDataContributor
{
  /* CONTEXT_ID PWPB0001 for the UDDI Publish Page */
  private String INFOPOP_PWPB_PAGE = WebServiceConsumptionUIPlugin.ID + ".PWPB0001";
  /* CONTEXT_ID PWPB0002 for the UDDI Launch check box of the UDDI Publish Page */
  private String INFOPOP_PWPB_CHECKBOX_WS_LAUNCH = WebServiceConsumptionUIPlugin.ID + ".PWPB0002";
  private Button launchUDDICheckbox_;
  private Button launchPrivateUDDICheckbox_;
  private Label pubilcUDDIRegComboLabel_;
  private Combo publicUDDIRegCombo_;
  private MessageUtils msgUtils;
  private Listener statusListener;
  private Boolean publish;

  public PublishWSWidget(boolean publish)
  {
    msgUtils = new MessageUtils(WebServiceConsumptionUIPlugin.ID + ".plugin", this);
    this.publish = new Boolean(publish);
  }

  public WidgetDataEvents addControls(Composite parent, Listener statusListener)
  {
    this.statusListener = statusListener;
    Listener selListener = new Listener()
    {
      public void handleEvent(Event event)
      {
        handleSelectionEvent(event);
      }
    };
    parent.setToolTipText(msgUtils.getMessage("TOOLTIP_PWPB_PAGE"));
    IWorkbenchHelpSystem helpSystem = PlatformUI.getWorkbench().getHelpSystem();
    helpSystem.setHelp(parent, INFOPOP_PWPB_PAGE);
    launchPrivateUDDICheckbox_ = new Button(parent, SWT.CHECK);
    if (publish.booleanValue())
      launchPrivateUDDICheckbox_.setText(msgUtils.getMessage("BUTTON_WS_PUBLISH_PRIVATE_UDDI"));
    else
      launchPrivateUDDICheckbox_.setText(msgUtils.getMessage("BUTTON_WS_FIND_PRIVATE_UDDI"));
    launchPrivateUDDICheckbox_.setToolTipText(msgUtils.getMessage("TOOLTIP_PWPB_CHECKBOX_WS_LAUNCH"));
    launchPrivateUDDICheckbox_.addListener(SWT.Selection, selListener);
    helpSystem.setHelp(launchPrivateUDDICheckbox_, INFOPOP_PWPB_CHECKBOX_WS_LAUNCH);
    launchUDDICheckbox_ = new Button(parent, SWT.CHECK);
    if (publish.booleanValue())
      launchUDDICheckbox_.setText(msgUtils.getMessage("BUTTON_WS_PUBLISH"));
    else
      launchUDDICheckbox_.setText(msgUtils.getMessage("BUTTON_WS_FIND"));
    launchUDDICheckbox_.setToolTipText(msgUtils.getMessage("TOOLTIP_PWPB_CHECKBOX_WS_LAUNCH"));
    launchUDDICheckbox_.addListener(SWT.Selection, selListener);
    helpSystem.setHelp(launchUDDICheckbox_, INFOPOP_PWPB_CHECKBOX_WS_LAUNCH);
    Composite c = new Composite(parent, SWT.NONE);
    GridLayout gl = new GridLayout();
    gl.numColumns = 3;
    gl.marginHeight = 0;
    gl.marginWidth = 0;
    c.setLayout(gl);
    GridData gd = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
    c.setLayoutData(gd);
    new Label(c, SWT.WRAP);
    pubilcUDDIRegComboLabel_ = new Label(c, SWT.WRAP);
    pubilcUDDIRegComboLabel_.setText(msgUtils.getMessage("LABEL_PUBLIC_UDDI_REGISTRIES"));
    publicUDDIRegCombo_ = new Combo(c, SWT.DROP_DOWN | SWT.READ_ONLY);
    publicUDDIRegCombo_.setEnabled(false);
    initPublicUDDI();
    initPrivateUDDI();
    return this;
  }

  private void initPublicUDDI()
  {
    PublicUDDIRegistryTypeRegistry reg = PublicUDDIRegistryTypeRegistry.getInstance();
    PublicUDDIRegistryType[] types = reg.getAllPublicUDDIRegistryTypes();
    for (int i = 0; i < types.length; i++)
    {
      if (publish.booleanValue())
      {
        // Ignore read-only registries.
        String publishURL = types[i].getPublishURL();
        if (publishURL == null || publishURL.indexOf("://") == -1)
          continue;
      }
      publicUDDIRegCombo_.add(types[i].getName());
    }
    if (types.length > 0)
      publicUDDIRegCombo_.setText(publicUDDIRegCombo_.getItem(0));
  }

  private void initPrivateUDDI()
  {
    if (!publish.booleanValue() && !PrivateUDDIRegistryTypeRegistry.getInstance().getPrivateUDDIRegistryType().isPrivateUDDIRegistryInstalled())
      launchPrivateUDDICheckbox_.setEnabled(false);
  }

  private void handleSelectionEvent(Event event)
  {
    if (launchUDDICheckbox_ == event.widget)
      publicUDDIRegCombo_.setEnabled(launchUDDICheckbox_.getSelection());
    statusListener.handleEvent(event);
  }

  public void setPublishToPublicUDDI(boolean publish)
  {
    launchUDDICheckbox_.setSelection(publish);
    publicUDDIRegCombo_.setEnabled(publish);
  }

  public void setPublishToPublicUDDI(Boolean publish)
  {
    setPublishToPublicUDDI(publish.booleanValue());
  }

  public void setPublishToPrivateUDDI(boolean publish)
  {
    launchPrivateUDDICheckbox_.setSelection(publish);
  }

  public void setPublishToPrivateUDDI(Boolean publish)
  {
    setPublishToPrivateUDDI(publish.booleanValue());
  }

  public boolean getPublishToPublicUDDI()
  {
    return launchUDDICheckbox_.getSelection();
  }

  public boolean getPublishToPrivateUDDI()
  {
    return launchPrivateUDDICheckbox_.getSelection();
  }

  public boolean getForceLaunchOutsideIDE()
  {
    return false;
  }

  public LaunchOption[] getLaunchOptions()
  {
    Vector launchOptionVector = new Vector();
    if (launchUDDICheckbox_.getSelection())
    {
      PublicUDDIRegistryTypeRegistry reg = PublicUDDIRegistryTypeRegistry.getInstance();
      int UDDIRegSelectionIndex = publicUDDIRegCombo_.getSelectionIndex();
      if (UDDIRegSelectionIndex != -1)
      {
        String name = publicUDDIRegCombo_.getItem(UDDIRegSelectionIndex);
        String id = reg.getPublicUDDIRegistryTypeIDByName(name);
        PublicUDDIRegistryType type = reg.getPublicUDDIRegistryTypeByID(id);
        launchOptionVector.add(new LaunchOption(LaunchOptions.INQUIRY_URL, type.getInquiryURL()));
        launchOptionVector.add(new LaunchOption(LaunchOptions.PUBLISH_URL, type.getPublishURL()));
      }
    }
    if (launchPrivateUDDICheckbox_.getSelection())
    {
      PrivateUDDIRegistryTypeRegistry privateReg = PrivateUDDIRegistryTypeRegistry.getInstance();
      PrivateUDDIRegistryType privateType = privateReg.getPrivateUDDIRegistryType();
      String[] privateInquiryURL = privateType.getPrivateUDDIRegistryInquiryAPI();
      String[] privatePublishURL = privateType.getPrivateUDDIRegistryPublishAPI();
      for (int i = 0; i < privateInquiryURL.length; i++)
      {
        launchOptionVector.add(new LaunchOption(LaunchOptions.INQUIRY_URL, privateInquiryURL[i]));
        launchOptionVector.add(new LaunchOption(LaunchOptions.PUBLISH_URL, privatePublishURL[i]));
      }
    }
    return (LaunchOption[]) launchOptionVector.toArray(new LaunchOption[0]);
  }
}