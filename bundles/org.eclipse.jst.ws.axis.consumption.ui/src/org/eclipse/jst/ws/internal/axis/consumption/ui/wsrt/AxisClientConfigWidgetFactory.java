/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.jst.ws.internal.axis.consumption.ui.wsrt;

import org.eclipse.jst.ws.internal.axis.consumption.ui.widgets.AxisProxyWidget;
import org.eclipse.jst.ws.internal.axis.consumption.ui.wizard.client.WebServiceClientAxisType;
import org.eclipse.wst.command.internal.env.ui.widgets.WidgetBindingToWidgetFactoryAdapter;
import org.eclipse.wst.command.internal.provisional.env.core.data.DataMappingRegistry;
import org.eclipse.wst.command.internal.provisional.env.ui.widgets.INamedWidgetContributor;
import org.eclipse.wst.command.internal.provisional.env.ui.widgets.INamedWidgetContributorFactory;

public class AxisClientConfigWidgetFactory implements INamedWidgetContributorFactory {

	private INamedWidgetContributor             proxyConfigWidget_;
	private INamedWidgetContributor             mappingsWidget_;
	private AxisProxyWidget                     proxyWidget_;
	private WidgetBindingToWidgetFactoryAdapter adapter_;
	
	public AxisClientConfigWidgetFactory()
	{
	  adapter_ = new WidgetBindingToWidgetFactoryAdapter( new WebServiceClientAxisType() );
	  
	  proxyConfigWidget_ = adapter_.getWidget( "AxisClientStart" );
	  proxyWidget_       = (AxisProxyWidget)proxyConfigWidget_.getWidgetContributorFactory().create();
	  mappingsWidget_   = adapter_.getWidget( "AxisClientBeanMapping" );
	}
	
	public INamedWidgetContributor getFirstNamedWidget() 
	{
	  return proxyConfigWidget_;
	}

	public INamedWidgetContributor getNextNamedWidget( INamedWidgetContributor widgetContributor) 
	{
	  return widgetContributor == proxyConfigWidget_ && proxyWidget_.getCustomizeClientMappings() ? mappingsWidget_ : null;
	}
	
	public void registerDataMappings(DataMappingRegistry dataRegistry) 
	{
	  adapter_.registerDataMappings( dataRegistry );
	}
}
