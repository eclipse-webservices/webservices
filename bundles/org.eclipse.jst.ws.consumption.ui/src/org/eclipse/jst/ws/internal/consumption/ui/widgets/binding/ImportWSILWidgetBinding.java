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
package org.eclipse.jst.ws.internal.consumption.ui.widgets.binding;

import org.eclipse.jst.ws.internal.consumption.ui.widgets.ImportWSILWidget;
import org.eclipse.jst.ws.internal.consumption.ui.wsil.AddWSDLToWSILWrapperCommand;
import org.eclipse.wst.command.env.core.common.MessageUtils;
import org.eclipse.wst.command.env.core.data.DataMappingRegistry;
import org.eclipse.wst.command.env.core.fragment.CommandFragment;
import org.eclipse.wst.command.env.core.fragment.CommandFragmentFactory;
import org.eclipse.wst.command.env.core.fragment.SequenceFragment;
import org.eclipse.wst.command.env.core.fragment.SimpleFragment;
import org.eclipse.wst.command.env.ui.widgets.CanFinishRegistry;
import org.eclipse.wst.command.env.ui.widgets.CommandWidgetBinding;
import org.eclipse.wst.command.env.ui.widgets.SelectionCommand;
import org.eclipse.wst.command.env.ui.widgets.WidgetContributor;
import org.eclipse.wst.command.env.ui.widgets.WidgetContributorFactory;
import org.eclipse.wst.command.env.ui.widgets.WidgetRegistry;


public class ImportWSILWidgetBinding implements CommandWidgetBinding
{
  private CanFinishRegistry   canFinishRegistry;
  private WidgetRegistry      widgetRegistry;
  private DataMappingRegistry dataMappingRegistry;

  public ImportWSILWidgetBinding()
  {
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
               root.add(new SimpleFragment("ImportWSILWidget"));
               root.add(new SimpleFragment(new AddWSDLToWSILWrapperCommand(), ""));
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
  }

  /* (non-Javadoc)
   * @see org.eclipse.wst.command.env.ui.widgets.CommandWidgetBinding#registerDataMappings(org.eclipse.wst.command.env.core.data.DataMappingRegistry)
   */
  public void registerDataMappings(DataMappingRegistry dataRegistry)
  {
    this.dataMappingRegistry = dataRegistry;
    
    // ImportWSILWidget
    dataRegistry.addMapping(SelectionCommand.class, "InitialSelection", ImportWSILWidget.class);
    
    // AddWSDLToWSILWrapperCommand
    dataRegistry.addMapping(ImportWSILWidget.class, "GenWSILArguments", AddWSDLToWSILWrapperCommand.class, "Args", null);
  }

  /* (non-Javadoc)
   * @see org.eclipse.wst.command.env.ui.widgets.CommandWidgetBinding#registerWidgetMappings(org.eclipse.wst.command.env.ui.widgets.WidgetRegistry)
   */
  public void registerWidgetMappings(WidgetRegistry widgetRegistry)
  {
    String       pluginId_ = "org.eclipse.jst.ws.consumption.ui";
    MessageUtils msgUtils = new MessageUtils( pluginId_ + ".plugin", this );
    this.widgetRegistry = widgetRegistry;

    widgetRegistry.add("ImportWSILWidget", 
      msgUtils.getMessage("TITLE_WSIL_IMPORT"),
      msgUtils.getMessage("DESC_WSIL_IMPORT"),
      new WidgetContributorFactory()
      {
        public WidgetContributor create()
        {
          return new ImportWSILWidget();
        }
      }
    );
  }
}