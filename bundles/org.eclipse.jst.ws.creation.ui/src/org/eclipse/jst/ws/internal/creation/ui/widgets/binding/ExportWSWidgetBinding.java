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
package org.eclipse.jst.ws.internal.creation.ui.widgets.binding;

import org.eclipse.jst.ws.internal.consumption.ui.widgets.PublishToPrivateUDDICommandFragment;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.PublishWSWidget;
import org.eclipse.jst.ws.internal.explorer.WSExplorerLauncherCommand;
import org.eclipse.wst.command.internal.env.core.fragment.CommandFragment;
import org.eclipse.wst.command.internal.env.core.fragment.CommandFragmentFactory;
import org.eclipse.wst.command.internal.env.core.fragment.SequenceFragment;
import org.eclipse.wst.command.internal.env.core.fragment.SimpleFragment;
import org.eclipse.wst.command.internal.env.ui.widgets.CanFinishRegistry;
import org.eclipse.wst.command.internal.env.ui.widgets.CommandWidgetBinding;
import org.eclipse.wst.command.internal.env.ui.widgets.WidgetContributor;
import org.eclipse.wst.command.internal.env.ui.widgets.WidgetContributorFactory;
import org.eclipse.wst.command.internal.env.ui.widgets.WidgetRegistry;
import org.eclipse.wst.command.internal.provisional.env.core.common.MessageUtils;
import org.eclipse.wst.command.internal.provisional.env.core.data.DataMappingRegistry;


public class ExportWSWidgetBinding implements CommandWidgetBinding
{
  private CanFinishRegistry   canFinishRegistry;
  private WidgetRegistry      widgetRegistry;
  private DataMappingRegistry dataMappingRegistry;
  private PublishToPrivateUDDICommandFragment publishToPrivateUDDICmdFrag;

  public ExportWSWidgetBinding()
  {
    publishToPrivateUDDICmdFrag = new PublishToPrivateUDDICommandFragment();
  }
  /* (non-Javadoc)
   * @see org.eclipse.wst.command.env.ui.widgets.CommandWidgetBinding#create()
   */
  public CommandFragmentFactory create()
  {
    return new CommandFragmentFactory()
           {
             public CommandFragment create()
             {
               SequenceFragment root = new SequenceFragment();
               root.add(new SimpleFragment("WSExport"));
               root.add(publishToPrivateUDDICmdFrag);
               root.add(new SimpleFragment(new WSExplorerLauncherCommand(), ""));
               return root;  
             }
           };
  }

  /* (non-Javadoc)
   * @see org.eclipse.wst.command.env.ui.widgets.CommandWidgetBinding#registerCanFinish(org.eclipse.wst.command.env.ui.widgets.CanFinishRegistry)
   */
  public void registerCanFinish(CanFinishRegistry canFinishRegistry)
  {
    this.canFinishRegistry = canFinishRegistry;
    publishToPrivateUDDICmdFrag.registerCanFinish(this.canFinishRegistry);
  }

  /* (non-Javadoc)
   * @see org.eclipse.wst.command.env.ui.widgets.CommandWidgetBinding#registerDataMappings(org.eclipse.wst.command.internal.provisional.env.core.data.DataMappingRegistry)
   */
  public void registerDataMappings(DataMappingRegistry dataRegistry)
  {
    this.dataMappingRegistry = dataRegistry;
    publishToPrivateUDDICmdFrag.registerDataMappings(this.dataMappingRegistry);
    
    // PublishToPrivateUDDICommandFragment
    dataMappingRegistry.addMapping(PublishWSWidget.class, "PublishToPrivateUDDI", PublishToPrivateUDDICommandFragment.class);
    
    // LaunchWebServicesExplorerCommand
    dataMappingRegistry.addMapping(PublishWSWidget.class, "ForceLaunchOutsideIDE", WSExplorerLauncherCommand.class);
    dataMappingRegistry.addMapping(PublishWSWidget.class, "LaunchOptions", WSExplorerLauncherCommand.class);
  }

  /* (non-Javadoc)
   * @see org.eclipse.wst.command.env.ui.widgets.CommandWidgetBinding#registerWidgetMappings(org.eclipse.wst.command.env.ui.widgets.WidgetRegistry)
   */
  public void registerWidgetMappings(WidgetRegistry widgetRegistry)
  {
    String       pluginId_ = "org.eclipse.jst.ws.consumption.ui";
    MessageUtils msgUtils = new MessageUtils( pluginId_ + ".plugin", this );
    this.widgetRegistry = widgetRegistry;
    publishToPrivateUDDICmdFrag.registerWidgetMappings(this.widgetRegistry);

    widgetRegistry.add("WSExport", 
      msgUtils.getMessage("PAGE_TITLE_WS_PUBLISH"),
      msgUtils.getMessage("PAGE_DESC_WS_PUBLISH"),
      new WidgetContributorFactory()
      {
        public WidgetContributor create()
        {
          return new PublishWSWidget(true);
        }
      }
    );
  }
}