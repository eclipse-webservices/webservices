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
package org.eclipse.jst.ws.internal.uddiregistry.widgets.binding;

import org.eclipse.jst.ws.internal.uddiregistry.plugin.WebServiceUDDIRegistryPlugin;
import org.eclipse.jst.ws.internal.uddiregistry.widgets.PrivateUDDISelectionCommand;
import org.eclipse.jst.ws.internal.uddiregistry.widgets.PrivateUDDISelectionCommandFragment;
import org.eclipse.jst.ws.internal.uddiregistry.widgets.PrivateUDDISelectionWidget;
import org.eclipse.jst.ws.internal.uddiregistry.widgets.PrivateUDDISelectionWidgetConditionCommand;
import org.eclipse.wst.command.env.core.common.MessageUtils;
import org.eclipse.wst.command.env.core.data.DataMappingRegistry;
import org.eclipse.wst.command.env.core.fragment.CommandFragment;
import org.eclipse.wst.command.env.core.fragment.CommandFragmentFactory;
import org.eclipse.wst.command.env.core.fragment.SequenceFragment;
import org.eclipse.wst.command.env.core.fragment.SimpleFragment;
import org.eclipse.wst.command.internal.env.ui.widgets.CanFinishRegistry;
import org.eclipse.wst.command.internal.env.ui.widgets.CommandWidgetBinding;
import org.eclipse.wst.command.internal.env.ui.widgets.WidgetContributor;
import org.eclipse.wst.command.internal.env.ui.widgets.WidgetContributorFactory;
import org.eclipse.wst.command.internal.env.ui.widgets.WidgetRegistry;


public class PrivateUDDIWidgetBinding implements CommandWidgetBinding
{
  private CanFinishRegistry   canFinishRegistry;
  private WidgetRegistry      widgetRegistry;
  private DataMappingRegistry dataMappingRegistry;
  private PrivateUDDISelectionCommandFragment privateUDDISelFragment;
  private PrivateUDDISelectionWidgetConditionCommand conditionCommand;
  
  public PrivateUDDIWidgetBinding()
  {
    privateUDDISelFragment = new PrivateUDDISelectionCommandFragment();
    conditionCommand = new PrivateUDDISelectionWidgetConditionCommand();
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
        root.add(new SimpleFragment(conditionCommand, ""));
        root.add(new SimpleFragment("PrivateUDDISelectionWidget"));
        root.add(new SimpleFragment(conditionCommand, ""));
        root.add(new SimpleFragment(new PrivateUDDISelectionCommand(), ""));
        root.add(privateUDDISelFragment);
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
    privateUDDISelFragment.registerCanFinish(this.canFinishRegistry);
    
    canFinishRegistry.addCondition(conditionCommand);
  }

  /* (non-Javadoc)
   * @see org.eclipse.wst.command.env.ui.widgets.CommandWidgetBinding#registerDataMappings(org.eclipse.wst.command.env.core.data.DataMappingRegistry)
   */
  public void registerDataMappings(DataMappingRegistry dataRegistry)
  {
    dataMappingRegistry = dataRegistry;
    privateUDDISelFragment.registerDataMappings(dataMappingRegistry);
    
    // PrivateUDDISelectionCommand
    dataMappingRegistry.addMapping(PrivateUDDISelectionWidget.class, "PrivateUDDIRegistryType", PrivateUDDISelectionCommand.class);
    dataMappingRegistry.addMapping(PrivateUDDISelectionWidget.class, "OperationType", PrivateUDDISelectionCommand.class);
    
    // PrivateUDDISelectionCommandFragment
    dataMappingRegistry.addMapping(PrivateUDDISelectionWidget.class, "PrivateUDDIRegistryType", PrivateUDDISelectionCommandFragment.class);
  }

  /* (non-Javadoc)
   * @see org.eclipse.wst.command.env.ui.widgets.CommandWidgetBinding#registerWidgetMappings(org.eclipse.wst.command.env.ui.widgets.WidgetRegistry)
   */
  public void registerWidgetMappings(WidgetRegistry widgetRegistry)
  {
    MessageUtils msgUtils = new MessageUtils(WebServiceUDDIRegistryPlugin.ID + ".plugin", this);
    this.widgetRegistry = widgetRegistry;
    privateUDDISelFragment.registerWidgetMappings(this.widgetRegistry);

    widgetRegistry.add("PrivateUDDISelectionWidget", 
      msgUtils.getMessage("PAGE_TITLE_PRIVATE_UDDI_CONFIG"),
      msgUtils.getMessage("PAGE_DESC_PRIVATE_UDDI_CONFIG"),
	  new WidgetContributorFactory()
      {
	    public WidgetContributor create()
	    {
	      return new PrivateUDDISelectionWidget();
	    }
	  }
    );
  }
}
