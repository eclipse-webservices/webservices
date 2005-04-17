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

package org.eclipse.jst.ws.internal.axis.creation.ui.wsrt;

import org.eclipse.jst.ws.internal.axis.creation.ui.widgets.bean.BeanConfigWidget;
import org.eclipse.jst.ws.internal.axis.creation.ui.wizard.beans.WSBeanAxisType;
import org.eclipse.wst.command.env.core.data.DataMappingRegistry;
import org.eclipse.wst.command.internal.env.ui.widgets.WidgetBindingToWidgetFactoryAdapter;
import org.eclipse.wst.command.internal.provisional.env.ui.widgets.INamedWidgetContributor;
import org.eclipse.wst.command.internal.provisional.env.ui.widgets.INamedWidgetContributorFactory;

public class AxisBeanConfigWidgetFactory implements INamedWidgetContributorFactory {

	private INamedWidgetContributor             beanConfigWidget_;
	private INamedWidgetContributor             mappingsWidget_;
	private BeanConfigWidget                    beanWidget_;
	private WidgetBindingToWidgetFactoryAdapter adapter_;
	
	public AxisBeanConfigWidgetFactory()
	{
	  adapter_ = new WidgetBindingToWidgetFactoryAdapter( new WSBeanAxisType() );
	  
	  beanConfigWidget_ = adapter_.getWidget( "BeanConfig" );
	  beanWidget_       = (BeanConfigWidget)beanConfigWidget_.getWidgetContributorFactory().create();
	  mappingsWidget_   = adapter_.getWidget( "AxisServiceBeanMapping" );
	}
	
	public INamedWidgetContributor getFirstNamedWidget() 
	{
	  return beanConfigWidget_;
	}

	public INamedWidgetContributor getNextNamedWidget( INamedWidgetContributor widgetContributor) 
	{
	  return widgetContributor == beanConfigWidget_ && beanWidget_.getCustomizeServiceMappings() ? mappingsWidget_ : null;
	}
	
	public void registerDataMappings(DataMappingRegistry dataRegistry) 
	{
	  adapter_.registerDataMappings( dataRegistry );
	}
}
