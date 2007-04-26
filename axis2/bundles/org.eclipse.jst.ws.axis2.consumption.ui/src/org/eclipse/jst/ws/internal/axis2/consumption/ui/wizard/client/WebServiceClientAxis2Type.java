/*******************************************************************************
 * Copyright (c) 2007 WSO2 Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * WSO2 Inc. - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20070130   168762 sandakith@wso2.com - Lahiru Sandakith, Initial code to introduse the Axis2 
 * 										  runtime to the framework for 168762
 * 20070426   183046 sandakith@wso2.com - Lahiru Sandakith
 *******************************************************************************/
package org.eclipse.jst.ws.internal.axis2.consumption.ui.wizard.client;

import org.eclipse.jst.ws.axis2.consumption.core.data.DataModel;
import org.eclipse.jst.ws.axis2.consumption.core.messages.Axis2ConsumptionUIMessages;
import org.eclipse.jst.ws.internal.axis2.consumption.ui.widgets.Axis2ProxyWidget;
import org.eclipse.wst.command.internal.env.core.data.DataMappingRegistry;
import org.eclipse.wst.command.internal.env.core.fragment.CommandFragment;
import org.eclipse.wst.command.internal.env.core.fragment.CommandFragmentFactory;
import org.eclipse.wst.command.internal.env.core.fragment.SimpleFragment;
import org.eclipse.wst.command.internal.env.ui.widgets.CanFinishRegistry;
import org.eclipse.wst.command.internal.env.ui.widgets.CommandWidgetBinding;
import org.eclipse.wst.command.internal.env.ui.widgets.WidgetContributor;
import org.eclipse.wst.command.internal.env.ui.widgets.WidgetContributorFactory;
import org.eclipse.wst.command.internal.env.ui.widgets.WidgetRegistry;

public class WebServiceClientAxis2Type implements CommandWidgetBinding {
	
	DataModel model;
	
	public WebServiceClientAxis2Type(DataModel model){
		this.model = model;
	}

	
	  /* (non-Javadoc)
	   * @see org.eclipse.wst.command.env.ui.widgets.CommandWidgetBinding#registerDataMappings
	   * (org.eclipse.wst.command.internal.env.core.data.DataMappingRegistry)
	   */
	  public void registerDataMappings(DataMappingRegistry dataRegistry) {
	  	// AxisClientDefaultingCommand  	
	    // dataRegistry.addMapping( Axis2ClientDefaultingCommand.class, 
		// "CustomizeClientMappings", Axis2ProxyWidget.class );
	    
	  }
	  
	  /* (non-Javadoc)
	   * @see org.eclipse.wst.command.env.ui.widgets.CommandWidgetBinding#registerWidgetMappings
	   * (org.eclipse.wst.command.env.ui.widgets.WidgetRegistry)
	   */
	  public void registerWidgetMappings(WidgetRegistry widgetRegistry) {
	   
	    widgetRegistry.add( "AxisClientStart", 
	        Axis2ConsumptionUIMessages.PAGE_TITLE_WS_AXIS2_PROXY,
	        Axis2ConsumptionUIMessages.PAGE_DESC_WS_AXIS2_PROXY,
	        new WidgetContributorFactory()
	        {
	          public WidgetContributor create()
	          {
	            return new Axis2ProxyWidget(model);
	          }
	        } );
	    
	    //widgetRegistry.add( "AxisClientBeanMapping", 
	    //    Axis2ConsumptionUIMessages.PAGE_TITLE_WS_XML2PROXY,
	    //    Axis2ConsumptionUIMessages.LABEL_EXPLORE_MAPPINGS_XML2BEAN,
	    //    new WidgetContributorFactory()
	    //    {
	    //      public WidgetContributor create()
	    //      {
	    //        return new AxisMappingsWidget(AxisMappingsWidget.MODE_XML2PROXY );
	    //      }
	    //    } );
	  }
	  
	  /* (non-Javadoc)
	   * @see org.eclipse.wst.command.internal.env.core.fragment
	   * .CommandFragmentFactoryFactory#create()
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
	   * @see org.eclipse.wst.command.env.ui.widgets.CommandWidgetBinding#registerCanFinish
	   * (org.eclipse.wst.command.env.ui.widgets.CanFinishRegistry)
	   */
	  public void registerCanFinish(CanFinishRegistry canFinishRegistry){
	  }
	  
	  public void setWebServiceDataModel( DataModel model ){
			this.model = model;
	  }
}
