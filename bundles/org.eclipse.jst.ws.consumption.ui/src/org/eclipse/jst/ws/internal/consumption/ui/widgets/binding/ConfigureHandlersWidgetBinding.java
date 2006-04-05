/*******************************************************************************
 * Copyright (c) 2004,2006 IBM Corporation and others.
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
import org.eclipse.jst.ws.internal.consumption.ui.widgets.ClientHandlersWidgetDefaultingCommand;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.ClientHandlersWidgetOutputCommand;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.ConfigClientHandlersTableWidget;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.ConfigServiceHandlersTableWidget;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.HandlersDefaultingCommand;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.ServiceHandlersWidgetDefaultingCommand;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.ServiceHandlersWidgetOutputCommand;
import org.eclipse.wst.command.internal.env.core.common.Condition;
import org.eclipse.wst.command.internal.env.core.data.DataMappingRegistry;
import org.eclipse.wst.command.internal.env.core.fragment.BooleanFragment;
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


public class ConfigureHandlersWidgetBinding implements CommandWidgetBinding {


  public ConfigureHandlersWidgetBinding() {

  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.wst.command.env.ui.widgets.CommandWidgetBinding#create()
   */
  public CommandFragmentFactory create() {
    return new CommandFragmentFactory() {

      public CommandFragment create() {
        return new HandlersCommandFragment();
      }
    };
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.wst.command.env.ui.widgets.CommandWidgetBinding#registerCanFinish(org.eclipse.wst.command.env.ui.widgets.CanFinishRegistry)
   */
  public void registerCanFinish(CanFinishRegistry canFinishRegistry) {
  }

  /*
   * (non-Javadoc),
   * 
   * @see org.eclipse.wst.command.env.ui.widgets.CommandWidgetBinding#registerDataMappings(org.eclipse.wst.command.internal.env.core.data.DataMappingRegistry)
   */
  public void registerDataMappings(DataMappingRegistry dataRegistry) {

    // Map client-side widgets to commands
    // widget
    dataRegistry.addMapping(ClientHandlersWidgetDefaultingCommand.class,"HandlerServiceRefHolder", ConfigClientHandlersTableWidget.class);
    dataRegistry.addMapping(ClientHandlersWidgetDefaultingCommand.class,"IsMultipleSelection", ConfigClientHandlersTableWidget.class);
    dataRegistry.addMapping(ClientHandlersWidgetDefaultingCommand.class,"GenSkeletonEnabled", ConfigClientHandlersTableWidget.class);
    dataRegistry.addMapping(ClientHandlersWidgetDefaultingCommand.class,"ServiceRefName", ConfigClientHandlersTableWidget.class);
    
    // output

    dataRegistry.addMapping(ConfigClientHandlersTableWidget.class,"HandlerServiceRefHolder", ClientHandlersWidgetOutputCommand.class);
    
    // gen skeleton
    dataRegistry.addMapping(ConfigClientHandlersTableWidget.class,"HandlerServiceRefHolder", GenerateHandlerSkeletonCommand.class);
    dataRegistry.addMapping(ConfigClientHandlersTableWidget.class,"GenSkeletonEnabled", GenerateHandlerSkeletonCommand.class);

    // open in editor
    dataRegistry.addMapping(ConfigClientHandlersTableWidget.class,"GenSkeletonEnabled", OpenJavaEditorCommand.class,"IsEnabled", null);
  
 
    // Map service-side widgets to commands
    // widget
    dataRegistry.addMapping(ServiceHandlersWidgetDefaultingCommand.class,"HandlerDescriptionHolders", ConfigServiceHandlersTableWidget.class);
    dataRegistry.addMapping(ServiceHandlersWidgetDefaultingCommand.class, "IsMultipleSelection", ConfigServiceHandlersTableWidget.class);
    dataRegistry.addMapping(ServiceHandlersWidgetDefaultingCommand.class,"GenSkeletonEnabled", ConfigServiceHandlersTableWidget.class);
    dataRegistry.addMapping(ServiceHandlersWidgetDefaultingCommand.class,"DescriptionName", ConfigServiceHandlersTableWidget.class);
    
    // output command (adds/removes from model)
    dataRegistry.addMapping(ConfigServiceHandlersTableWidget.class,"HandlerDescriptionHolders", ServiceHandlersWidgetOutputCommand.class);
    

    // gen skeleton command
    dataRegistry.addMapping(ConfigServiceHandlersTableWidget.class,"HandlerDescriptionHolders", GenerateHandlerSkeletonCommand.class);    
    dataRegistry.addMapping(ConfigServiceHandlersTableWidget.class,"GenSkeletonEnabled", GenerateHandlerSkeletonCommand.class);
 
    // open Java editor  
    dataRegistry.addMapping(ConfigServiceHandlersTableWidget.class,"GenSkeletonEnabled", OpenJavaEditorCommand.class,"IsEnabled", null);

  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.wst.command.env.ui.widgets.CommandWidgetBinding#registerWidgetMappings(org.eclipse.wst.command.env.ui.widgets.WidgetRegistry)
   */
  public void registerWidgetMappings(WidgetRegistry widgetRegistry) {

    widgetRegistry.add("ConfigServiceHandlersTableWidget", ConsumptionUIMessages.PAGE_TITLE_SERVICE_HDLR_CONFIG, 
    		ConsumptionUIMessages.PAGE_DESC_SERVICE_HDLR_CONFIG, new WidgetContributorFactory() {

      public WidgetContributor create() {
        return new ConfigServiceHandlersTableWidget();
      }
    });

    widgetRegistry.add("ConfigClientHandlersTableWidget", ConsumptionUIMessages.PAGE_TITLE_CLIENT_HDLR_CONFIG, 
    		ConsumptionUIMessages.PAGE_DESC_CLIENT_HDLR_CONFIG, new WidgetContributorFactory() {

      public WidgetContributor create() {
        return new ConfigClientHandlersTableWidget();
      }
    });

  }

  private class HandlersCommandFragment extends SequenceFragment {

    public HandlersCommandFragment() {

      add(new SimpleFragment(new HandlersDefaultingCommand(), ""));

      add(new ServiceHandlersFragment());
      add(new ClientHandlersFragment());
    }

    public void registerDataMappings(DataMappingRegistry dataRegistry) {

      dataRegistry.addMapping(SelectionCommand.class, "InitialSelection", HandlersDefaultingCommand.class);

      dataRegistry.addMapping(HandlersDefaultingCommand.class, "IsClientHandler", ClientHandlersFragment.class, "ClientHandlersEnabled", null);
      dataRegistry.addMapping(HandlersDefaultingCommand.class, "IsServiceHandler", ServiceHandlersFragment.class, "ServiceHandlersEnabled", null);

      dataRegistry.addMapping(ClientHandlersWidgetDefaultingCommand.class,"IsMultipleSelection", ClientHandlersWidgetOutputCommand.class);
      dataRegistry.addMapping(GenerateHandlerSkeletonCommand.class,"Project", OpenJavaEditorCommand.class);
      dataRegistry.addMapping(GenerateHandlerSkeletonCommand.class,"ClassNames", OpenJavaEditorCommand.class);
   
      dataRegistry.addMapping(ServiceHandlersWidgetDefaultingCommand.class, "IsMultipleSelection", ServiceHandlersWidgetOutputCommand.class);
      dataRegistry.addMapping(ServiceHandlersWidgetDefaultingCommand.class,"WsddResource", ServiceHandlersWidgetOutputCommand.class);
      dataRegistry.addMapping(GenerateHandlerSkeletonCommand.class,"Project", OpenJavaEditorCommand.class);
      dataRegistry.addMapping(GenerateHandlerSkeletonCommand.class,"ClassNames", OpenJavaEditorCommand.class);      

    }

    public void registerCanFinish(CanFinishRegistry canFinishRegistry) {
    }
  }

  private class ClientHandlersFragment extends BooleanFragment {

    boolean isClient_ = false;

    public ClientHandlersFragment() {
      setCondition(new Condition() {

        public boolean evaluate() {
          return isClient_;
        }
      });

      SequenceFragment clientHandlersRootFragment = new SequenceFragment();

      clientHandlersRootFragment.add(new SimpleFragment(new ClientHandlersWidgetDefaultingCommand(), ""));

      clientHandlersRootFragment.add(new SimpleFragment("ConfigClientHandlersTableWidget"));
      clientHandlersRootFragment.add(new SimpleFragment(new ClientHandlersWidgetOutputCommand(), ""));
      clientHandlersRootFragment.add(new SimpleFragment(new GenerateHandlerSkeletonCommand(), ""));

      setTrueFragment(clientHandlersRootFragment);
    }

    public void setClientHandlersEnabled(boolean isClient) {
      isClient_ = isClient;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.wst.command.internal.env.core.fragment.CommandFragment#registerDataMappings(org.eclipse.wst.command.internal.env.core.data.DataMappingRegistry)
     */
    public void registerDataMappings(DataMappingRegistry dataRegistry) {
      dataRegistry.addMapping(SelectionCommand.class, "InitialSelection", ClientHandlersWidgetDefaultingCommand.class);

      dataRegistry.addMapping(ClientHandlersWidgetDefaultingCommand.class, "ClientProject", ClientHandlersWidgetOutputCommand.class);

    }

  }

  private class ServiceHandlersFragment extends BooleanFragment {

    boolean isService_ = false;

    public ServiceHandlersFragment() {
      setCondition(new Condition() {

        public boolean evaluate() {
          return isService_;
        }
      });

      SequenceFragment serviceHandlersRootFragment = new SequenceFragment();
      serviceHandlersRootFragment.add(new SimpleFragment(new ServiceHandlersWidgetDefaultingCommand(), ""));

      SequenceFragment serviceHandlersFragment = new SequenceFragment();
      serviceHandlersFragment.add(new SimpleFragment("ConfigServiceHandlersTableWidget"));
      serviceHandlersFragment.add(new SimpleFragment(new ServiceHandlersWidgetOutputCommand(), ""));
      serviceHandlersFragment.add(new SimpleFragment(new GenerateHandlerSkeletonCommand(), ""));

      serviceHandlersRootFragment.add(serviceHandlersFragment);

      setTrueFragment(serviceHandlersRootFragment);
    }

    public void setServiceHandlersEnabled(boolean isService) {
      isService_ = isService;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.wst.command.internal.env.core.fragment.CommandFragment#registerDataMappings(org.eclipse.wst.command.internal.env.core.data.DataMappingRegistry)
     */
    public void registerDataMappings(DataMappingRegistry dataRegistry) {
      dataRegistry.addMapping(SelectionCommand.class, "InitialSelection", ServiceHandlersWidgetDefaultingCommand.class);

      dataRegistry.addMapping(ServiceHandlersWidgetDefaultingCommand.class, "WsddResource", ServiceHandlersWidgetOutputCommand.class);

    }

  }
}