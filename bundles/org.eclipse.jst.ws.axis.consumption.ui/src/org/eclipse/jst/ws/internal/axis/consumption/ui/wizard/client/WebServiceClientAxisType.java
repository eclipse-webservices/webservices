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
package org.eclipse.jst.ws.internal.axis.consumption.ui.wizard.client;

import org.eclipse.jst.ws.internal.axis.consumption.core.command.WSDL2JavaCommand;
import org.eclipse.jst.ws.internal.axis.consumption.ui.command.AxisClientDefaultingCommand;
import org.eclipse.jst.ws.internal.axis.consumption.ui.command.DefaultsForClientJavaWSDLCommand;
import org.eclipse.jst.ws.internal.axis.consumption.ui.task.DefaultsForHTTPBasicAuthCommand;
import org.eclipse.jst.ws.internal.axis.consumption.ui.task.Stub2BeanCommand;
import org.eclipse.jst.ws.internal.axis.consumption.ui.task.ValidateWSDLCommand;
import org.eclipse.jst.ws.internal.axis.consumption.ui.widgets.AxisMappingsWidget;
import org.eclipse.jst.ws.internal.axis.consumption.ui.widgets.AxisProxyWidget;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.extensions.ClientExtensionOutputCommand;
import org.eclipse.wst.command.internal.env.core.fragment.CommandFragment;
import org.eclipse.wst.command.internal.env.core.fragment.CommandFragmentFactory;
import org.eclipse.wst.command.internal.env.core.fragment.SimpleFragment;
import org.eclipse.wst.command.internal.env.ui.widgets.CanFinishRegistry;
import org.eclipse.wst.command.internal.env.ui.widgets.CommandWidgetBinding;
import org.eclipse.wst.command.internal.env.ui.widgets.WidgetContributor;
import org.eclipse.wst.command.internal.env.ui.widgets.WidgetContributorFactory;
import org.eclipse.wst.command.internal.env.ui.widgets.WidgetRegistry;
import org.eclipse.wst.command.internal.provisional.env.core.common.MessageUtils;
import org.eclipse.wst.command.internal.provisional.env.core.data.DataMappingRegistry;


/**
  * Developers who are adding web service clients into the wizard should create
  * a class that implements this interface.  
**/
public class WebServiceClientAxisType implements CommandWidgetBinding 
{

	
  /* (non-Javadoc)
   * @see org.eclipse.wst.command.env.ui.widgets.CommandWidgetBinding#registerDataMappings(org.eclipse.wst.command.internal.provisional.env.core.data.DataMappingRegistry)
   */
  public void registerDataMappings(DataMappingRegistry dataRegistry) 
  {
  	// AxisClientDefaultingCommand  	
    dataRegistry.addMapping( AxisClientDefaultingCommand.class, "CustomizeClientMappings", AxisProxyWidget.class );
    dataRegistry.addMapping( AxisClientDefaultingCommand.class, "ProxyProjectFolder", AxisProxyWidget.class, "ProxyFolder", null );
    dataRegistry.addMapping( AxisClientDefaultingCommand.class, "GenerateProxy", AxisProxyWidget.class);    
    dataRegistry.addMapping( AxisClientDefaultingCommand.class, "IsClientScenario", AxisProxyWidget.class);    
    dataRegistry.addMapping( AxisClientDefaultingCommand.class, "JavaWSDLParam", AxisMappingsWidget.class, "JavaParameter", null);
    
    // AxisProxyWidget
    dataRegistry.addMapping( AxisProxyWidget.class, "GenerateProxy", ClientExtensionOutputCommand.class );
    
    //AxisMappingsWidget
	dataRegistry.addMapping(AxisMappingsWidget.class, "JavaParameter", DefaultsForHTTPBasicAuthCommand.class, "JavaWSDLParam", null); //OK
	dataRegistry.addMapping(AxisMappingsWidget.class, "JavaParameter", DefaultsForClientJavaWSDLCommand.class, "JavaWSDLParam", null); //OK
	dataRegistry.addMapping(AxisMappingsWidget.class, "JavaParameter", ValidateWSDLCommand.class, "JavaWSDLParam", null); //OK
	dataRegistry.addMapping(AxisMappingsWidget.class, "JavaParameter", WSDL2JavaCommand.class, "JavaWSDLParam", null); //OK
	dataRegistry.addMapping(AxisMappingsWidget.class, "JavaParameter", Stub2BeanCommand.class, "JavaWSDLParam", null); //OK
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.wst.command.env.ui.widgets.CommandWidgetBinding#registerWidgetMappings(org.eclipse.wst.command.env.ui.widgets.WidgetRegistry)
   */
  public void registerWidgetMappings(WidgetRegistry widgetRegistry) 
  {
    String       pluginId_     = "org.eclipse.jst.ws.consumption.ui";
    String       axisPluginId_ = "org.eclipse.jst.ws.axis.consumption.ui";
    MessageUtils msgUtils = new MessageUtils( pluginId_ + ".plugin", this );
    MessageUtils axisMsgUtils = new MessageUtils( axisPluginId_ + ".plugin", this );
    
    widgetRegistry.add( "AxisClientStart", 
        axisMsgUtils.getMessage("PAGE_TITLE_WS_AXIS_PROXY"),
        axisMsgUtils.getMessage("PAGE_DESC_WS_AXIS_PROXY"),
        new WidgetContributorFactory()
        {
          public WidgetContributor create()
          {
            return new AxisProxyWidget();
          }
        } );
    
    widgetRegistry.add( "AxisClientBeanMapping", 
        axisMsgUtils.getMessage("PAGE_TITLE_WS_XML2PROXY"),
        msgUtils.getMessage("PAGE_DESC_N2P_MAPPINGS"),
        new WidgetContributorFactory()
        {
          public WidgetContributor create()
          {
            return new AxisMappingsWidget(AxisMappingsWidget.MODE_XML2PROXY );
          }
        } );
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.wst.command.internal.env.core.fragment.CommandFragmentFactoryFactory#create()
   */
  public CommandFragmentFactory create() 
  {
    return new CommandFragmentFactory()
           {
             public CommandFragment create()
             {
               //dead code - doesn't matter what gets returned here.
               return new SimpleFragment();
             }
           };
  }
  /* (non-Javadoc)
   * @see org.eclipse.wst.command.env.ui.widgets.CommandWidgetBinding#registerCanFinish(org.eclipse.wst.command.env.ui.widgets.CanFinishRegistry)
   */
  public void registerCanFinish(CanFinishRegistry canFinishRegistry) 
  {
  }
}
