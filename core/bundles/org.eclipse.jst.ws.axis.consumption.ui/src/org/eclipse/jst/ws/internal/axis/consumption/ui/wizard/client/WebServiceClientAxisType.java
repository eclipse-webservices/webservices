/*******************************************************************************
 * Copyright (c) 2004, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060216   115144 pmoogk@ca.ibm.com - Peter Moogk
 * 20060523   143284 sengpl@ca.ibm.com - Seng Phung-Lu
 *******************************************************************************/
package org.eclipse.jst.ws.internal.axis.consumption.ui.wizard.client;

import org.eclipse.jst.ws.internal.axis.consumption.ui.AxisConsumptionUIMessages;
import org.eclipse.jst.ws.internal.axis.consumption.ui.command.AxisClientDefaultingCommand;
import org.eclipse.jst.ws.internal.axis.consumption.ui.command.DefaultsForClientJavaWSDLCommand;
import org.eclipse.jst.ws.internal.axis.consumption.ui.task.ClientCodeGenOperation;
import org.eclipse.jst.ws.internal.axis.consumption.ui.task.DefaultsForHTTPBasicAuthCommand;
import org.eclipse.jst.ws.internal.axis.consumption.ui.task.ValidateWSDLCommand;
import org.eclipse.jst.ws.internal.axis.consumption.ui.widgets.AxisMappingsWidget;
import org.eclipse.jst.ws.internal.axis.consumption.ui.widgets.AxisProxyWidget;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.extensions.ClientExtensionOutputCommand;
import org.eclipse.wst.command.internal.env.core.data.DataMappingRegistry;
import org.eclipse.wst.command.internal.env.core.fragment.CommandFragment;
import org.eclipse.wst.command.internal.env.core.fragment.CommandFragmentFactory;
import org.eclipse.wst.command.internal.env.core.fragment.SimpleFragment;
import org.eclipse.wst.command.internal.env.ui.widgets.CanFinishRegistry;
import org.eclipse.wst.command.internal.env.ui.widgets.CommandWidgetBinding;
import org.eclipse.wst.command.internal.env.ui.widgets.WidgetContributor;
import org.eclipse.wst.command.internal.env.ui.widgets.WidgetContributorFactory;
import org.eclipse.wst.command.internal.env.ui.widgets.WidgetRegistry;


/**
  * Developers who are adding web service clients into the wizard should create
  * a class that implements this interface.  
**/
public class WebServiceClientAxisType implements CommandWidgetBinding 
{

	
  /* (non-Javadoc)
   * @see org.eclipse.wst.command.env.ui.widgets.CommandWidgetBinding#registerDataMappings(org.eclipse.wst.command.internal.env.core.data.DataMappingRegistry)
   */
  public void registerDataMappings(DataMappingRegistry dataRegistry) 
  {
  	// AxisClientDefaultingCommand  	
    dataRegistry.addMapping( AxisClientDefaultingCommand.class, "CustomizeClientMappings", AxisProxyWidget.class );
    dataRegistry.addMapping( AxisClientDefaultingCommand.class, "ProxyProjectFolder", AxisProxyWidget.class, "ProxyFolder", null );
    dataRegistry.addMapping( AxisClientDefaultingCommand.class, "GenerateProxy", AxisProxyWidget.class);    
    dataRegistry.addMapping( AxisClientDefaultingCommand.class, "IsClientScenario", AxisProxyWidget.class);    
    dataRegistry.addMapping( AxisClientDefaultingCommand.class, "ClientProject", AxisProxyWidget.class );
    dataRegistry.addMapping( AxisClientDefaultingCommand.class, "JavaWSDLParam", AxisMappingsWidget.class, "JavaParameter", null);
    
    // AxisProxyWidget
    dataRegistry.addMapping( AxisProxyWidget.class, "GenerateProxy", ClientExtensionOutputCommand.class );
    dataRegistry.addMapping( AxisProxyWidget.class, "OutputFolder", DefaultsForClientJavaWSDLCommand.class );
    
    //AxisMappingsWidget
	dataRegistry.addMapping(AxisMappingsWidget.class, "JavaParameter", DefaultsForHTTPBasicAuthCommand.class, "JavaWSDLParam", null); //OK
	dataRegistry.addMapping(AxisMappingsWidget.class, "JavaParameter", DefaultsForClientJavaWSDLCommand.class, "JavaWSDLParam", null); //OK
	dataRegistry.addMapping(AxisMappingsWidget.class, "JavaParameter", ValidateWSDLCommand.class, "JavaWSDLParam", null); //OK
	dataRegistry.addMapping(AxisMappingsWidget.class, "JavaParameter", ClientCodeGenOperation.class, "JavaWSDLParam", null); //OK

  }
  
  /* (non-Javadoc)
   * @see org.eclipse.wst.command.env.ui.widgets.CommandWidgetBinding#registerWidgetMappings(org.eclipse.wst.command.env.ui.widgets.WidgetRegistry)
   */
  public void registerWidgetMappings(WidgetRegistry widgetRegistry) 
  {
   
    widgetRegistry.add( "AxisClientStart", 
        AxisConsumptionUIMessages.PAGE_TITLE_WS_AXIS_PROXY,
        AxisConsumptionUIMessages.PAGE_DESC_WS_AXIS_PROXY,
        new WidgetContributorFactory()
        {
          public WidgetContributor create()
          {
            return new AxisProxyWidget();
          }
        } );
    
    widgetRegistry.add( "AxisClientBeanMapping", 
        AxisConsumptionUIMessages.PAGE_TITLE_WS_XML2PROXY,
        AxisConsumptionUIMessages.LABEL_EXPLORE_MAPPINGS_XML2BEAN,
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
