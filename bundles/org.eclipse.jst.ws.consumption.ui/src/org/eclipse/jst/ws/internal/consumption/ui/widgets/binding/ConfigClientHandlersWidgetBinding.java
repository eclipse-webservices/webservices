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
import org.eclipse.jst.ws.internal.consumption.ui.widgets.ClientHandlersWidgetDefaultingCommand;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.ClientHandlersWidgetOutputCommand;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.ConfigClientHandlersTableWidget;
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
public class ConfigClientHandlersWidgetBinding implements CommandWidgetBinding {
  
  private CanFinishRegistry   canFinishRegistry_;
  private WidgetRegistry      widgetRegistry_;
  private DataMappingRegistry dataMappingRegistry_;
  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.wst.command.env.ui.widgets.CommandWidgetBinding#registerWidgetMappings(org.eclipse.wst.command.env.ui.widgets.WidgetRegistry)
   */
  public void registerWidgetMappings(WidgetRegistry widgetRegistry) {

    String pluginId_ = "org.eclipse.jst.ws.consumption.ui";
    MessageUtils msgUtils = new MessageUtils(pluginId_ + ".plugin", this);
    widgetRegistry_ = widgetRegistry;
    
    widgetRegistry.add("ConfigClientHandlersTableWidget", 
        msgUtils.getMessage("PAGE_TITLE_CLIENT_HDLR_CONFIG"), 
        msgUtils.getMessage("PAGE_DESC_CLIENT_HDLR_CONFIG"), 
        new WidgetContributorFactory() {

      	public WidgetContributor create() {
      	  return new ConfigClientHandlersTableWidget();
      	}
    });

  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.wst.command.env.ui.widgets.CommandWidgetBinding#registerDataMappings(org.eclipse.wst.command.internal.provisional.env.core.data.DataMappingRegistry)
   */
  public void registerDataMappings(DataMappingRegistry dataRegistry) {
    dataMappingRegistry_ = dataRegistry;
    
    
//    dataRegistry.addMapping(ClientHandlersWidgetDefaultingCommand.class,"AllHandlers", ConfigClientHandlersTableWidget.class);
    dataRegistry.addMapping(ClientHandlersWidgetDefaultingCommand.class,"Handlers", ConfigClientHandlersTableWidget.class,"WsRefsToHandlers", null);
    
    dataRegistry.addMapping(ClientHandlersWidgetDefaultingCommand.class,"GenSkeletonEnabled", ConfigClientHandlersTableWidget.class);
    dataRegistry.addMapping(ClientHandlersWidgetDefaultingCommand.class,"SourceOutputLocation", ConfigClientHandlersTableWidget.class);
    dataRegistry.addMapping(ClientHandlersWidgetDefaultingCommand.class,"ServiceRefName", ConfigClientHandlersTableWidget.class);
    dataRegistry.addMapping(ClientHandlersWidgetDefaultingCommand.class,"RefNameToServiceRef", ConfigClientHandlersTableWidget.class);
    
    //dataRegistry.addMapping(ConfigClientHandlersTableWidget.class,"AllHandlersList", ClientHandlersWidgetOutputCommand.class);
    dataRegistry.addMapping(ConfigClientHandlersTableWidget.class,"WsRefsToHandlers", ClientHandlersWidgetOutputCommand.class,"HandlersTable",null);
    
    dataRegistry.addMapping(ConfigClientHandlersTableWidget.class,"SourceOutputLocation", GenerateHandlerSkeletonCommand.class,"OutputLocation",null);
    dataRegistry.addMapping(ConfigClientHandlersTableWidget.class,"HandlerClassNames", GenerateHandlerSkeletonCommand.class,"HandlerNames",null);
    dataRegistry.addMapping(ConfigClientHandlersTableWidget.class,"GenSkeletonEnabled", GenerateHandlerSkeletonCommand.class);

    dataRegistry.addMapping(GenerateHandlerSkeletonCommand.class,"Project", OpenJavaEditorCommand.class);
    dataRegistry.addMapping(GenerateHandlerSkeletonCommand.class,"ClassNames", OpenJavaEditorCommand.class);
    
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.wst.command.env.ui.widgets.CommandWidgetBinding#registerCanFinish(org.eclipse.wst.command.env.ui.widgets.CanFinishRegistry)
   */
  public void registerCanFinish(CanFinishRegistry canFinishRegistry) {
    canFinishRegistry_ = canFinishRegistry;

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
     * @see org.eclipse.wst.command.internal.env.core.fragment.CommandFragment#registerDataMappings(org.eclipse.wst.command.internal.provisional.env.core.data.DataMappingRegistry)
     */
    public void registerDataMappings(DataMappingRegistry dataRegistry)
    {
      dataRegistry.addMapping(SelectionCommand.class, "InitialSelection", ClientHandlersWidgetDefaultingCommand.class);
      
      dataRegistry.addMapping(ClientHandlersWidgetDefaultingCommand.class,"WsClientResource", ClientHandlersWidgetOutputCommand.class);
      dataRegistry.addMapping(ClientHandlersWidgetDefaultingCommand.class,"WsServiceRefs", ClientHandlersWidgetOutputCommand.class);      
      dataRegistry.addMapping(ClientHandlersWidgetDefaultingCommand.class,"ClientProject", ClientHandlersWidgetOutputCommand.class);
    }
 
  }
  
}
