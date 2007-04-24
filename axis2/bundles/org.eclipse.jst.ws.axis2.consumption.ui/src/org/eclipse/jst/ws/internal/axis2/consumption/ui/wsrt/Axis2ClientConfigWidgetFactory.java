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
 * 20070230   168762 sandakith@wso2.com - Lahiru Sandakith, Initial code to introduse the Axis2 
 * 										  runtime to the framework for 168762
 *******************************************************************************/
package org.eclipse.jst.ws.internal.axis2.consumption.ui.wsrt;

import org.eclipse.jst.ws.axis2.consumption.core.command.Axis2ClientDefaultingCommand;
import org.eclipse.jst.ws.axis2.consumption.core.data.DataModel;
import org.eclipse.jst.ws.axis2.consumption.core.messages.Axis2ConsumptionUIMessages;
import org.eclipse.jst.ws.internal.axis2.consumption.ui.widgets.Axis2ProxyWidget;
import org.eclipse.jst.ws.internal.axis2.consumption.ui.wizard.client.WebServiceClientAxis2Type;
import org.eclipse.wst.command.internal.env.core.data.DataMappingRegistry;
import org.eclipse.wst.command.internal.env.ui.widgets.INamedWidgetContributor;
import org.eclipse.wst.command.internal.env.ui.widgets.INamedWidgetContributorFactory;
import org.eclipse.wst.command.internal.env.ui.widgets.WidgetBindingToWidgetFactoryAdapter;

public class Axis2ClientConfigWidgetFactory implements INamedWidgetContributorFactory{
	private INamedWidgetContributor             proxyConfigWidget;
	private INamedWidgetContributor             mappingsWidget;
	private Axis2ProxyWidget                     proxyWidget;
	private WidgetBindingToWidgetFactoryAdapter adapter;
	private DataModel 							model;
	private WebServiceClientAxis2Type           wsClientAxis2Type;
	private DataMappingRegistry dataRegistry;
	
	public Axis2ClientConfigWidgetFactory(){
	}
	
	public INamedWidgetContributor getFirstNamedWidget() {
		init();
		//wsClientAxis2Type.setWebServiceDataModel(model);
		return proxyConfigWidget;
	}

	public INamedWidgetContributor getNextNamedWidget( INamedWidgetContributor widgetContributor) {
	  return widgetContributor == proxyConfigWidget && proxyWidget.isGenProxy() ? mappingsWidget : null;
	}
	
	public void registerDataMappings(DataMappingRegistry dataRegistry) {
		this.dataRegistry=dataRegistry;
		// Map the data model from the defaulting command to this widget factory.
		// The framework will actually to the call to getWebServiceDataModel in
		// the ExampleDefaultingCommand class and then call the setWebServiceDataModel
		// method in this class.
		dataRegistry.addMapping( Axis2ClientDefaultingCommand.class, 
								 Axis2ConsumptionUIMessages.LABEL_DATA_MODEL, 
								 Axis2ClientConfigWidgetFactory.class );
	}
	
	public void setWebServiceDataModel( DataModel model ){
		this.model = model;
	}
	
	private void init(){
		  wsClientAxis2Type = new WebServiceClientAxis2Type(model);
		  adapter = new WidgetBindingToWidgetFactoryAdapter(wsClientAxis2Type );
		  proxyConfigWidget = adapter.getWidget( "AxisClientStart" );
		  proxyWidget       = (Axis2ProxyWidget)proxyConfigWidget.getWidgetContributorFactory().create();
		  mappingsWidget   = adapter.getWidget( "AxisClientBeanMapping" );
		  adapter.registerDataMappings( dataRegistry );

	}
}
