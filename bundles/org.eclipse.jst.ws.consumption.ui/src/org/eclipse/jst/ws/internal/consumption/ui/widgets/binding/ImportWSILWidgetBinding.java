/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.internal.consumption.ui.widgets.binding;

import org.eclipse.jst.ws.internal.consumption.ui.ConsumptionUIMessages;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.ImportWSILWidget;
import org.eclipse.jst.ws.internal.consumption.ui.wsil.AddWSDLToWSILWrapperCommand;
import org.eclipse.wst.command.internal.env.core.data.DataMappingRegistry;
import org.eclipse.wst.command.internal.env.core.fragment.CommandFragment;
import org.eclipse.wst.command.internal.env.core.fragment.CommandFragmentFactory;
import org.eclipse.wst.command.internal.env.core.fragment.SequenceFragment;
import org.eclipse.wst.command.internal.env.core.fragment.SimpleFragment;
import org.eclipse.wst.command.internal.env.ui.widgets.CanFinishRegistry;
import org.eclipse.wst.command.internal.env.ui.widgets.CommandWidgetBinding;
import org.eclipse.wst.command.internal.env.ui.widgets.SelectionCommand;
import org.eclipse.wst.command.internal.env.ui.widgets.WidgetContributor;
import org.eclipse.wst.command.internal.env.ui.widgets.WidgetContributorFactory;
import org.eclipse.wst.command.internal.env.ui.widgets.WidgetRegistry;


public class ImportWSILWidgetBinding implements CommandWidgetBinding
{
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
  }

  /* (non-Javadoc)
   * @see org.eclipse.wst.command.env.ui.widgets.CommandWidgetBinding#registerDataMappings(org.eclipse.wst.command.internal.env.core.data.DataMappingRegistry)
   */
  public void registerDataMappings(DataMappingRegistry dataRegistry)
  {    
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

    widgetRegistry.add("ImportWSILWidget", 
      ConsumptionUIMessages.TITLE_WSIL_IMPORT,
      ConsumptionUIMessages.DESC_WSIL_IMPORT,
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
