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

package org.eclipse.wst.command.internal.provisional.ui.widgets;

import java.util.Hashtable;

import org.eclipse.wst.command.env.core.data.DataMappingRegistry;
import org.eclipse.wst.command.env.ui.widgets.CommandWidgetBinding;
import org.eclipse.wst.command.env.ui.widgets.WidgetContributor;
import org.eclipse.wst.command.env.ui.widgets.WidgetContributorFactory;
import org.eclipse.wst.command.env.ui.widgets.WidgetRegistry;

public class WidgetBindingToWidgetFactoryAdapter 
{
  private Hashtable            widgetContributor_;
  private CommandWidgetBinding binding_;
  
  public WidgetBindingToWidgetFactoryAdapter( CommandWidgetBinding binding )
  {
    widgetContributor_ = new Hashtable(); 
	binding_           = binding;
	
	binding.registerWidgetMappings( new MyWidgetRegistry() );
  }
  
  public INamedWidgetContributor getWidget( String id )
  {
    return (INamedWidgetContributor)widgetContributor_.get( id );  
  }
  
  public void registerDataMappings( DataMappingRegistry registry )
  {
    binding_.registerDataMappings( registry );  
  }
  
  private class MyWidgetRegistry implements WidgetRegistry
  {
	public void add(String fragmentId, String pageName, String pageTitle, WidgetContributorFactory widgetFactory) 
	{
	  SimpleWidgetContributor widget = new SimpleWidgetContributor();
	  widget.setTitle( pageName );
	  widget.setDescription( pageName );
	  widget.setFactory( new MyWidgetContributorFactory( widgetFactory ) );
	  
	  widgetContributor_.put( fragmentId, widget );
	}	  
  }
  
  private class MyWidgetContributorFactory implements WidgetContributorFactory
  {
	private WidgetContributorFactory factory_;
	private WidgetContributor        widget_;
	
	public MyWidgetContributorFactory( WidgetContributorFactory factory )
	{
	  factory_ = factory;	
	}
	
	public WidgetContributor create() 
	{
      if( widget_ == null )
	  {
	    widget_ = factory_.create();
	  }
	  
	  return widget_;
	}	  
  }
}
