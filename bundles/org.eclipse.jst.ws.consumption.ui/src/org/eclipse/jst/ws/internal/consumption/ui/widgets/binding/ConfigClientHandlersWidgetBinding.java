/*******************************************************************************
 * Copyright (c) 2004, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060404   134913 sengpl@ca.ibm.com - Seng Phung-Lu       
 * 20060517   142339 sengpl@ca.ibm.com - Seng Phung-Lu
 *******************************************************************************/
package org.eclipse.jst.ws.internal.consumption.ui.widgets.binding;

import org.eclipse.jst.ws.internal.consumption.ui.ConsumptionUIMessages;
import org.eclipse.jst.ws.internal.consumption.ui.command.GenerateHandlerSkeletonCommand;
import org.eclipse.jst.ws.internal.consumption.ui.command.OpenJavaEditorCommand;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.ClientHandlersWidgetDefaultingCommand;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.ClientHandlersWidgetOutputCommand;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.ConfigClientHandlersTableWidget;
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


/**
 * 
 * Handler Configuration Window - Preferences - Java - Code Generation - Code
 * and Comments
 */
public class ConfigClientHandlersWidgetBinding implements CommandWidgetBinding 
{
  
  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.wst.command.env.ui.widgets.CommandWidgetBinding#registerWidgetMappings(org.eclipse.wst.command.env.ui.widgets.WidgetRegistry)
   */
  public void registerWidgetMappings(WidgetRegistry widgetRegistry) {

    widgetRegistry.add("ConfigClientHandlersTableWidget", 
        ConsumptionUIMessages.PAGE_TITLE_CLIENT_HDLR_CONFIG, 
        ConsumptionUIMessages.PAGE_DESC_CLIENT_HDLR_CONFIG, 
        new WidgetContributorFactory() {

      	public WidgetContributor create() {
      	  return new ConfigClientHandlersTableWidget();
      	}
    });

  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.wst.command.env.ui.widgets.CommandWidgetBinding#registerDataMappings(org.eclipse.wst.command.internal.env.core.data.DataMappingRegistry)
   */
  public void registerDataMappings(DataMappingRegistry dataRegistry) {
    
    // widget
    dataRegistry.addMapping(ClientHandlersWidgetDefaultingCommand.class,"HandlerServiceRefHolder", ConfigClientHandlersTableWidget.class);
    dataRegistry.addMapping(ClientHandlersWidgetDefaultingCommand.class,"IsMultipleSelection", ConfigClientHandlersTableWidget.class);
    dataRegistry.addMapping(ClientHandlersWidgetDefaultingCommand.class,"GenSkeletonEnabled", ConfigClientHandlersTableWidget.class);
    dataRegistry.addMapping(ClientHandlersWidgetDefaultingCommand.class,"ServiceRefName", ConfigClientHandlersTableWidget.class);
    
    // output
    dataRegistry.addMapping(ClientHandlersWidgetDefaultingCommand.class,"IsMultipleSelection", ClientHandlersWidgetOutputCommand.class);
    dataRegistry.addMapping(ClientHandlersWidgetDefaultingCommand.class,"ClientProject", ClientHandlersWidgetOutputCommand.class);
    dataRegistry.addMapping(ConfigClientHandlersTableWidget.class,"HandlerServiceRefHolder", ClientHandlersWidgetOutputCommand.class);
    
    // gen skeleton
    dataRegistry.addMapping(ConfigClientHandlersTableWidget.class,"HandlerServiceRefHolder", GenerateHandlerSkeletonCommand.class);
    dataRegistry.addMapping(ConfigClientHandlersTableWidget.class,"GenSkeletonEnabled", GenerateHandlerSkeletonCommand.class);

    // open in editor
    dataRegistry.addMapping(GenerateHandlerSkeletonCommand.class,"Project", OpenJavaEditorCommand.class);
    dataRegistry.addMapping(GenerateHandlerSkeletonCommand.class,"ClassNames", OpenJavaEditorCommand.class);
    dataRegistry.addMapping(ConfigClientHandlersTableWidget.class,"GenSkeletonEnabled", OpenJavaEditorCommand.class,"IsEnabled", null);
    
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.wst.command.env.ui.widgets.CommandWidgetBinding#registerCanFinish(org.eclipse.wst.command.env.ui.widgets.CanFinishRegistry)
   */
  public void registerCanFinish(CanFinishRegistry canFinishRegistry) {

  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.wst.command.internal.env.core.fragment.CommandFragmentFactoryFactory#create()
   */
  public CommandFragmentFactory create() {
    return new CommandFragmentFactory() {

      public CommandFragment create() {

        return new ClientHandlersConfigCommandFragment();
      }
    };
  }

  private class ClientHandlersConfigCommandFragment extends SequenceFragment{
   
    public ClientHandlersConfigCommandFragment()
    {
      add( new SimpleFragment( new ClientHandlersWidgetDefaultingCommand(), "" ));
      add( new SimpleFragment( "ConfigClientHandlersTableWidget" ) );
      add( new SimpleFragment( new ClientHandlersWidgetOutputCommand(), "" ));
      add( new SimpleFragment( new GenerateHandlerSkeletonCommand(), ""));
      add( new SimpleFragment( new OpenJavaEditorCommand(), ""));
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.wst.command.internal.env.core.fragment.CommandFragment#registerDataMappings(org.eclipse.wst.command.internal.env.core.data.DataMappingRegistry)
     */
    public void registerDataMappings(DataMappingRegistry dataRegistry)
    {
      dataRegistry.addMapping(SelectionCommand.class, "InitialSelection", ClientHandlersWidgetDefaultingCommand.class);
      
      dataRegistry.addMapping(ClientHandlersWidgetDefaultingCommand.class,"WsServiceRefs", ClientHandlersWidgetOutputCommand.class);      
      dataRegistry.addMapping(ClientHandlersWidgetDefaultingCommand.class,"ClientProject", ClientHandlersWidgetOutputCommand.class);
    }
 
  }
  
}
