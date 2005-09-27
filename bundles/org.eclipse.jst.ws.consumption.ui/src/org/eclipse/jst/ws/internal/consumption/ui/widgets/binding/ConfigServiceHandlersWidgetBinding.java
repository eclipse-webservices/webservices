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

import org.eclipse.jst.ws.internal.consumption.ui.command.GenerateHandlerSkeletonCommand;
import org.eclipse.jst.ws.internal.consumption.ui.command.OpenJavaEditorCommand;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.ConfigServiceHandlersTableWidget;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.ServiceHandlersWidgetDefaultingCommand;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.ServiceHandlersWidgetOutputCommand;
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
import org.eclipse.wst.command.internal.provisional.env.core.common.MessageUtils;
import org.eclipse.wst.command.internal.provisional.env.core.data.DataMappingRegistry;


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

    String pluginId_ = "org.eclipse.jst.ws.consumption.ui";
    MessageUtils msgUtils = new MessageUtils(pluginId_ + ".plugin", this);
    
    widgetRegistry.add("ConfigServiceHandlersTableWidget", 
        msgUtils.getMessage("PAGE_TITLE_SERVICE_HDLR_CONFIG"), 
        msgUtils.getMessage("PAGE_DESC_SERVICE_HDLR_CONFIG"), 
        new WidgetContributorFactory() {

      	public WidgetContributor create() {
      	  return new ConfigServiceHandlersTableWidget();
      	}
    });

  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.wst.command.env.ui.widgets.CommandWidgetBinding#registerDataMappings(org.eclipse.wst.command.internal.provisional.env.core.data.DataMappingRegistry)
   */
  public void registerDataMappings(DataMappingRegistry dataRegistry) {
    
    dataRegistry.addMapping(ServiceHandlersWidgetDefaultingCommand.class,"WsRefsToHandlers", ConfigServiceHandlersTableWidget.class);
    dataRegistry.addMapping(ServiceHandlersWidgetDefaultingCommand.class,"GenSkeletonEnabled", ConfigServiceHandlersTableWidget.class);
    dataRegistry.addMapping(ServiceHandlersWidgetDefaultingCommand.class,"SourceOutputLocation", ConfigServiceHandlersTableWidget.class);
    dataRegistry.addMapping(ServiceHandlersWidgetDefaultingCommand.class,"ServiceDescNameToDescObj", ConfigServiceHandlersTableWidget.class);
    
    dataRegistry.addMapping(ServiceHandlersWidgetDefaultingCommand.class,"DescriptionName", ConfigServiceHandlersTableWidget.class);
    
    
    dataRegistry.addMapping(ConfigServiceHandlersTableWidget.class,"HandlersList", ServiceHandlersWidgetOutputCommand.class);
    dataRegistry.addMapping(ConfigServiceHandlersTableWidget.class,"WsDescToHandlers", ServiceHandlersWidgetOutputCommand.class);
    dataRegistry.addMapping(ServiceHandlersWidgetDefaultingCommand.class,"ServiceDescNameToDescObj", ServiceHandlersWidgetOutputCommand.class);
    
    dataRegistry.addMapping(ConfigServiceHandlersTableWidget.class,"SourceOutputLocation", GenerateHandlerSkeletonCommand.class,"OutputLocation",null);
    dataRegistry.addMapping(ConfigServiceHandlersTableWidget.class,"HandlerClassNames", GenerateHandlerSkeletonCommand.class,"HandlerNames",null);
    dataRegistry.addMapping(ConfigServiceHandlersTableWidget.class,"GenSkeletonEnabled", GenerateHandlerSkeletonCommand.class);
 
    dataRegistry.addMapping(GenerateHandlerSkeletonCommand.class,"Project", OpenJavaEditorCommand.class);
    dataRegistry.addMapping(GenerateHandlerSkeletonCommand.class,"ClassNames", OpenJavaEditorCommand.class);
   
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
     * @see org.eclipse.wst.command.internal.env.core.fragment.CommandFragment#registerDataMappings(org.eclipse.wst.command.internal.provisional.env.core.data.DataMappingRegistry)
     */
    public void registerDataMappings(DataMappingRegistry dataRegistry)
    {
      dataRegistry.addMapping(SelectionCommand.class, "InitialSelection", ServiceHandlersWidgetDefaultingCommand.class);
      
      dataRegistry.addMapping(ServiceHandlersWidgetDefaultingCommand.class, "WsddResource", ServiceHandlersWidgetOutputCommand.class);
      
      
    }
 
  }
  
}
