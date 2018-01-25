/*******************************************************************************
 * Copyright (c) 2004, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20080430   214624 makandre@ca.ibm.com - Andrew Mak, Remove favourite UDDI registries from Web Service Publish page
 *******************************************************************************/
package org.eclipse.jst.ws.internal.consumption.ui.widgets;

import java.util.Vector;

import org.eclipse.jst.ws.internal.consumption.ui.ConsumptionUIMessages;
import org.eclipse.jst.ws.internal.consumption.ui.plugin.WebServiceConsumptionUIPlugin;
import org.eclipse.jst.ws.internal.consumption.ui.wizard.PrivateUDDIRegistryTypeRegistry;
import org.eclipse.jst.ws.internal.ui.uddi.PrivateUDDIRegistryType;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.help.IWorkbenchHelpSystem;
import org.eclipse.wst.command.internal.env.ui.widgets.SimpleWidgetDataContributor;
import org.eclipse.wst.command.internal.env.ui.widgets.WidgetDataEvents;
import org.eclipse.wst.ws.internal.explorer.LaunchOption;
import org.eclipse.wst.ws.internal.explorer.LaunchOptions;


public class PublishWSWidget extends SimpleWidgetDataContributor
{
  /* CONTEXT_ID PWPB0001 for the UDDI Publish Page */
  private String INFOPOP_PWPB_PAGE = WebServiceConsumptionUIPlugin.ID + ".PWPB0001";
  /* CONTEXT_ID PWPB0002 for the UDDI Launch check box of the UDDI Publish Page */
  private String INFOPOP_PWPB_CHECKBOX_WS_LAUNCH = WebServiceConsumptionUIPlugin.ID + ".PWPB0002";
  private Button launchUDDICheckbox_;
  private Button launchPrivateUDDICheckbox_;
  private Listener statusListener;
  private Boolean publish;

  public PublishWSWidget(boolean publish)
  {
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
    parent.setToolTipText(ConsumptionUIMessages.TOOLTIP_PWPB_PAGE);
    IWorkbenchHelpSystem helpSystem = PlatformUI.getWorkbench().getHelpSystem();
    helpSystem.setHelp(parent, INFOPOP_PWPB_PAGE);
    launchPrivateUDDICheckbox_ = new Button(parent, SWT.CHECK);
    if (publish.booleanValue())
      launchPrivateUDDICheckbox_.setText(ConsumptionUIMessages.BUTTON_WS_PUBLISH_PRIVATE_UDDI);
    else
      launchPrivateUDDICheckbox_.setText(ConsumptionUIMessages.BUTTON_WS_FIND_PRIVATE_UDDI);
    launchPrivateUDDICheckbox_.setToolTipText(ConsumptionUIMessages.TOOLTIP_PWPB_CHECKBOX_WS_LAUNCH);
    launchPrivateUDDICheckbox_.addListener(SWT.Selection, selListener);
    helpSystem.setHelp(launchPrivateUDDICheckbox_, INFOPOP_PWPB_CHECKBOX_WS_LAUNCH);
    launchUDDICheckbox_ = new Button(parent, SWT.CHECK);
    if (publish.booleanValue())
      launchUDDICheckbox_.setText(ConsumptionUIMessages.BUTTON_WS_PUBLISH);
    else
      launchUDDICheckbox_.setText(ConsumptionUIMessages.BUTTON_WS_FIND);
    launchUDDICheckbox_.setToolTipText(ConsumptionUIMessages.TOOLTIP_PWPB_CHECKBOX_WS_LAUNCH);
    launchUDDICheckbox_.addListener(SWT.Selection, selListener);
    helpSystem.setHelp(launchUDDICheckbox_, INFOPOP_PWPB_CHECKBOX_WS_LAUNCH);
    initPrivateUDDI();
    return this;
  }

  private void initPrivateUDDI()
  {
    if (!publish.booleanValue() && !PrivateUDDIRegistryTypeRegistry.getInstance().getPrivateUDDIRegistryType().isPrivateUDDIRegistryInstalled())
      launchPrivateUDDICheckbox_.setEnabled(false);
  }

  private void handleSelectionEvent(Event event)
  {
    statusListener.handleEvent(event);
  }

  public void setPublishToPublicUDDI(boolean publish)
  {
    launchUDDICheckbox_.setSelection(publish);
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
      launchOptionVector.add(new LaunchOption(LaunchOptions.INQUIRY_URL, ""));
      launchOptionVector.add(new LaunchOption(LaunchOptions.PUBLISH_URL, ""));
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
