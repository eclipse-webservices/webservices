/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.jst.ws.internal.consumption.ui.wizard;

import java.util.Enumeration;
import java.util.Hashtable;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.wst.command.env.core.common.Log;
import org.eclipse.wst.command.env.eclipse.EclipseLog;

public class WebServiceTypeRegistry
{
  // Copyright
  public static final String copyright = "(c) Copyright IBM Corporation 2000, 2002.";
  private static WebServiceTypeRegistry instance_;

  private Hashtable typesByName_;
  private String[] typeNames_;

  //
  // Loads WebServiceType objects into this registry.
  // This is done by querying the plugin registry for all extensions
  // hanging on the webServiceType extension point. Extensions
  // must implement the org.eclipse.jst.ws.ui.wizard.WebServiceType
  // interface.
  //
  private void loadTypes ()
  {
  	Log log = new EclipseLog();
    IExtensionRegistry reg = Platform.getExtensionRegistry();
    IConfigurationElement[] config = reg.getConfigurationElementsFor(
                                     "org.eclipse.jst.ws.consumption.ui",
                                     "webServiceType");

    for(int idx=0; idx<config.length; idx++) 
    {
      IConfigurationElement elem = config[idx];
      try 
      {
        Object webServiceType = elem.createExecutableExtension("class");
        if (webServiceType instanceof WebServiceType) 
        {
          add((WebServiceType)webServiceType);
        }
        else
        {
          String implementedInterface = "org.eclipse.jst.ws.ui.wizard.WebServiceType ";
          String errMsg = "Extensions of the webServiceTypes extension point must implement the ";
          errMsg = errMsg + implementedInterface + "interface.";
      	  log.log(Log.ERROR, 5056, this, "loadTypes", errMsg);
        }
      } catch (CoreException e)
      {
      	log.log(Log.ERROR, 5057, this, "loadTypes", e);
      }
    }
  }

  //
  // Loads WebServiceType objects into this registry.
  // See method getInstance().
  //
  private void load ()
  {
    typesByName_ = new Hashtable();
    loadTypes();
    indexTypesByName();
  }

  //
  // Add the given WebServiceType to this registry.
  // See method load().
  //
  private void add ( WebServiceType wst )
  {
    typesByName_.put(wst.getName(),wst);
  }

  //
  // Builds an index of WebServiceType objects by their names.
  // See method load().
  //
  private void indexTypesByName ()
  {
    if (typeNames_ == null)
    {
      typeNames_ = new String[typesByName_.size()];
      Enumeration e = typesByName_.keys();
      for (int i=0; e.hasMoreElements(); i++)
      {
        typeNames_[i] = (String)e.nextElement();
      }
    }
  }

  /**
  * Returns a singleton instance of this class.
  * @return A singleton WebServiceTypeRegistry object.
  */
  public static WebServiceTypeRegistry getInstance ()
  {
    if (instance_ == null)
    {
      instance_ = new WebServiceTypeRegistry();
      instance_.load();
    }
    return instance_;
  }

  /**
  * Returns all registered <code>WebServiceType</code> objects.
  * @return All registered <code>WebServiceType</code> objects.
  */
  public WebServiceType[] getWebServiceTypes ()
  {
    WebServiceType[] wsts = new WebServiceType[typesByName_.size()];
    Enumeration e = typesByName_.elements();
    for (int i=0; e.hasMoreElements(); i++)
    {
      wsts[i] = (WebServiceType)e.nextElement();
    }
    return wsts;
  }

  /**
  * Returns the names of all registered <code>WebServiceType</code>
  * objects.
  * @return The names of all registered <code>WebServiceType</code>
  * objects.
  */
  public String[] getWebServiceTypeNames ()
  {
    return typeNames_;
  }

  /**
  * Returns the <code>WebServiceType</code> object of the given
  * <code>name</code> or null if there is no such object.
  * @param name The name of the <code>WebServiceType</code> to find.
  * @return The <code>WebServiceType</code> or null if no such object
  * was found.
  */
  public WebServiceType getWebServiceTypeByName ( String name )
  {
    return (name == null ? null : (WebServiceType)typesByName_.get(name));
  }

}
