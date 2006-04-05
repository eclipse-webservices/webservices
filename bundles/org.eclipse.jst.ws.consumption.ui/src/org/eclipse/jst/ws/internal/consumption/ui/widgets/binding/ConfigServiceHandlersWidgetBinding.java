/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060404 134913   sengpl@ca.ibm.com - Seng Phung-Lu       
 *******************************************************************************/
package org.eclipse.jst.ws.internal.consumption.ui.widgets.binding;

import org.eclipse.jst.ws.internal.consumption.ui.ConsumptionUIMessages;
import org.eclipse.jst.ws.internal.consumption.ui.command.GenerateHandlerSkeletonCommand;
import org.eclipse.jst.ws.internal.consumption.ui.command.OpenJavaEditorCommand;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.ConfigServiceHandlersTableWidget;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.ServiceHandlersWidgetDefaultingCommand;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.ServiceHandlersWidgetOutputCommand;
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
public class ConfigServiceHandlersWidgetBinding implements CommandWidgetBinding {
  
  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.wst.command.env.ui.widgets.CommandWidgetBinding#registerWidgetMappings(org.eclipse.wst.command.env.ui.widgets.WidgetRegistry)
   */
  public void registerWidgetMappings(WidgetRegistry widgetRegistry) {

    widgetRegistry.add("ConfigServiceHandlersTableWidget", 
   		ConsumptionUIMessages.PAGE_TITLE_SERVICE_HDLR_CONFIG, 
   		ConsumptionUIMessages.PAGE_DESC_SERVICE_HDLR_CONFIG, 
        new WidgetContributorFactory() {

      	public WidgetContributor create() {
      	  return new ConfigServiceHandlersTableWidget();
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
    dataRegistry.addMapping(ServiceHandlersWidgetDefaultingCommand.class,"HandlerDescriptionHolders", ConfigServiceHandlersTableWidget.class);
    dataRegistry.addMapping(ServiceHandlersWidgetDefaultingCommand.class, "IsMultipleSelection", ConfigServiceHandlersTableWidget.class);
    dataRegistry.addMapping(ServiceHandlersWidgetDefaultingCommand.class,"GenSkeletonEnabled", ConfigServiceHandlersTableWidget.class);
    dataRegistry.addMapping(ServiceHandlersWidgetDefaultingCommand.class,"DescriptionName", ConfigServiceHandlersTableWidget.class);
    
    // output command (adds/removes from model)
    dataRegistry.addMapping(ConfigServiceHandlersTableWidget.class,"HandlerDescriptionHolders", ServiceHandlersWidgetOutputCommand.class);
    dataRegistry.addMapping(ServiceHandlersWidgetDefaultingCommand.class, "IsMultipleSelection", ServiceHandlersWidgetOutputCommand.class);
    dataRegistry.addMapping(ServiceHandlersWidgetDefaultingCommand.class,"WsddResource", ServiceHandlersWidgetOutputCommand.class);    

    // gen skeleton command
    dataRegistry.addMapping(ConfigServiceHandlersTableWidget.class,"HandlerDescriptionHolders", GenerateHandlerSkeletonCommand.class);    
    dataRegistry.addMapping(ConfigServiceHandlersTableWidget.class,"GenSkeletonEnabled", GenerateHandlerSkeletonCommand.class);
 
    // open Java editor  
    dataRegistry.addMapping(GenerateHandlerSkeletonCommand.class,"Project", OpenJavaEditorCommand.class);
    dataRegistry.addMapping(GenerateHandlerSkeletonCommand.class,"ClassNames", OpenJavaEditorCommand.class);
    dataRegistry.addMapping(ConfigServiceHandlersTableWidget.class,"GenSkeletonEnabled", OpenJavaEditorCommand.class,"IsEnabled", null);
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

        return new ServiceHandlersConfigCommandFragment();
      }
    };
  }

  private class ServiceHandlersConfigCommandFragment extends SequenceFragment{
   
    public ServiceHandlersConfigCommandFragment()
    {
      add( new SimpleFragment( new ServiceHandlersWidgetDefaultingCommand(), "" ));
      add( new SimpleFragment( "ConfigServiceHandlersTableWidget" ) );
      add( new SimpleFragment( new ServiceHandlersWidgetOutputCommand(), "" ));
      add( new SimpleFragment( new GenerateHandlerSkeletonCommand(), ""));
      add( new SimpleFragment( new OpenJavaEditorCommand(), ""));

    }
    
    /* (non-Javadoc)
     * @see org.eclipse.wst.command.internal.env.core.fragment.CommandFragment#registerDataMappings(org.eclipse.wst.command.internal.env.core.data.DataMappingRegistry)
     */
    public void registerDataMappings(DataMappingRegistry dataRegistry)
    {
      dataRegistry.addMapping(SelectionCommand.class, "InitialSelection", ServiceHandlersWidgetDefaultingCommand.class);
      
      dataRegistry.addMapping(ServiceHandlersWidgetDefaultingCommand.class, "WsddResource", ServiceHandlersWidgetOutputCommand.class);
      
      
    }
 
  }
  
}
