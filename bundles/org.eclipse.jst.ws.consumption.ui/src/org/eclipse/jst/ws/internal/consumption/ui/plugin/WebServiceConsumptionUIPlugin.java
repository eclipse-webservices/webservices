/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060424   115690 sengpl@ca.ibm.com - Seng Phung-Lu
 * 20060504   136118 joan@ca.ibm.com - Joan Haggarty
 *******************************************************************************/

package org.eclipse.jst.ws.internal.consumption.ui.plugin;

import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jst.ws.internal.consumption.ui.preferences.PersistentProjectTopologyContext;
import org.eclipse.jst.ws.internal.consumption.ui.preferences.PersistentServerRuntimeContext;
import org.eclipse.jst.ws.internal.consumption.ui.preferences.ProjectTopologyContext;
import org.eclipse.ui.plugin.AbstractUIPlugin;


/**
* This is the plugin class for the Web Services plugin.
* <p>
* This plugin contains the graphic user interface to the
* Web Services runtime found in org.eclipse.jst.ws.
*/
public class WebServiceConsumptionUIPlugin extends AbstractUIPlugin
{

  /**
  * The identifier of the descriptor of this plugin in plugin.xml.
  */
  public static final String ID = "org.eclipse.jst.ws.consumption.ui";

  /**
  * The reference to the singleton instance of this plugin.
  */
  private static WebServiceConsumptionUIPlugin instance_;

  private PersistentServerRuntimeContext serverRuntimeContext_;
  private PersistentProjectTopologyContext projectTopologyContext_;
  
  /**
  * Constructs a runtime plugin object for this plugin.
  */
  public WebServiceConsumptionUIPlugin ()
  {
    super();
    instance_ = this;
  }

  /**
  * Returns the singleton instance of this plugin. Equivalent to calling
  * (WebServiceConsumptionUIPlugin)Platform.getPlugin("org.eclipse.jst.ws.ui");
  * @return The WebServiceConsumptionUIPlugin singleton.
  */
  static public WebServiceConsumptionUIPlugin getInstance ()
  {
    return instance_;
  }

  /**
   * Returns an image descriptor for the named resource
   * as relative to the plugin install location.
   * @return An image descriptor, possibly null.
   */
   public static ImageDescriptor getImageDescriptor ( String name )
   {
 	try
     {	
     	URL imageURL = FileLocator.find(instance_.getBundle(), new Path("$nl$/"+name), null);
     	return ImageDescriptor.createFromURL(imageURL);
     }
     catch (Exception e)
     {
       return null;
     }
   }

  public ProjectTopologyContext getProjectTopologyContext()
  {
    if (projectTopologyContext_ == null)
    {
      projectTopologyContext_ = new PersistentProjectTopologyContext();
      projectTopologyContext_.load();
    }
    
    return projectTopologyContext_;
  }

  public PersistentServerRuntimeContext getServerRuntimeContext() 
	{
	  if (serverRuntimeContext_ == null)
	  	{
	  		serverRuntimeContext_ = new PersistentServerRuntimeContext();
	  		serverRuntimeContext_.load();
	  	}
	  return serverRuntimeContext_;
	}  
  }
