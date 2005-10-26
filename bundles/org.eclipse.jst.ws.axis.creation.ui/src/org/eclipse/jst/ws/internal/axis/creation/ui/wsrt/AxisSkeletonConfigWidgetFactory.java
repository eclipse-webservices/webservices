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

import org.eclipse.jst.ws.internal.axis.creation.ui.widgets.skeleton.SkeletonConfigWidget;
import org.eclipse.jst.ws.internal.axis.creation.ui.wizard.wsdl.WSWSDLAxisType;
import org.eclipse.wst.command.internal.env.core.data.DataMappingRegistry;
import org.eclipse.wst.command.internal.env.ui.widgets.INamedWidgetContributor;
import org.eclipse.wst.command.internal.env.ui.widgets.INamedWidgetContributorFactory;
import org.eclipse.wst.command.internal.env.ui.widgets.WidgetBindingToWidgetFactoryAdapter;

public class AxisSkeletonConfigWidgetFactory implements INamedWidgetContributorFactory {

	private INamedWidgetContributor             skelConfigWidget_;
	private INamedWidgetContributor             mappingsWidget_;
	private SkeletonConfigWidget                skelWidget_;
	private WidgetBindingToWidgetFactoryAdapter adapter_;
	
	public AxisSkeletonConfigWidgetFactory()
	{
	  adapter_ = new WidgetBindingToWidgetFactoryAdapter( new WSWSDLAxisType() );
	  
	  skelConfigWidget_ = adapter_.getWidget( "SkeletonConfig" );
	  skelWidget_       = (SkeletonConfigWidget)skelConfigWidget_.getWidgetContributorFactory().create();
	  mappingsWidget_   = adapter_.getWidget( "AxisMappingsWidget" );
	}
	
	public INamedWidgetContributor getFirstNamedWidget() 
	{
	  return skelConfigWidget_;
	}

	public INamedWidgetContributor getNextNamedWidget( INamedWidgetContributor widgetContributor) 
	{
	  return widgetContributor == skelConfigWidget_ && skelWidget_.getShowMapping() ? mappingsWidget_ : null;
	}
	
	public void registerDataMappings(DataMappingRegistry dataRegistry) 
	{
	  adapter_.registerDataMappings( dataRegistry );
	}
}
