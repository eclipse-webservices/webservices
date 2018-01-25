/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.command.internal.env.ui.registry;

import java.util.Hashtable;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.wst.command.internal.env.ui.widgets.INamedWidgetContributorFactory;

public class WidgetRegistry 
{
  private static WidgetRegistry registry_;
  
  private Hashtable entries_;
  
  private WidgetRegistry()
  {
  }
  
  public static WidgetRegistry instance()
  {
	if (registry_ == null)
	{
		registry_ = new WidgetRegistry();
		registry_.load();
	}

	return registry_;
  }
  
  public static void initialize()
  {
    registry_ = null;  
  }
  
  /**
   * 
   * @param id the methodId that may have WidgetContributor associated with it.
   * @return returns a INamedWidgetContributorFactory if one was defined in the registry.
   * Otherwise null is returned.
   */
  public INamedWidgetContributorFactory getFactory( String id )
  {
	INamedWidgetContributorFactory factory = null;
	Entry                          entry   = (Entry)entries_.get( id );
	
	if( entry != null )
	{
	  try
	  {
		if( entry.factory_ == null )
		{
		  entry.factory_ = (INamedWidgetContributorFactory)entry.element_.createExecutableExtension( "class" );
		}
		
		factory = entry.factory_;
	  }
	  catch( CoreException exc )
	  {
	    exc.printStackTrace();
	  }
	  catch( Throwable exc )
	  {
		exc.printStackTrace();
	  }
	}
	
	return factory;
  }
  
  private void load()
  {
	IConfigurationElement[] elements = Platform.getExtensionRegistry().getConfigurationElementsFor("org.eclipse.wst.command.env.ui.widgetRegistry");
	
	entries_ = new Hashtable();
	
	for( int index = 0; index < elements.length; index++ )
	{
	  // If this is not an element that know about we will skip it.
	  if( !elements[index].getName().equals("widgetFactory") ) continue;
	  
	  Entry                 entry    = new Entry();
	  IConfigurationElement element  = elements[index];
	  String                id       = element.getAttribute( "id" );
	  String                methodId = element.getAttribute( "insertBeforeCommandId" );
	  
	  entry.element_ = element;
	  entry.id_      = id;
	  
	  entries_.put( methodId, entry );
	}
  }
  
  private class Entry
  {
    String                         id_;
	IConfigurationElement          element_;
	INamedWidgetContributorFactory factory_;
  }
}
