/*******************************************************************************
 * Copyright (c) 2002, 2006 IBM Corporation and others.
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
 * 20060504   138118 joan@ca.ibm.com - Joan Haggarty
 *******************************************************************************/
package org.eclipse.jst.ws.internal.creation.ui.plugin;

import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.jface.resource.ImageDescriptor;


/**
* This is the plugin class for the Web Services Creation UI plugin.
* <p>
* The Web Services Creation UI plugin's sole function is to add 
* user interface of the Web Services Creation Wizard, contained in  
* the org.eclipse.jst.ws.ui plugin.
*/
public class WebServiceCreationUIPlugin extends Plugin
{

  /**
  * The identifier of the descriptor of this plugin in plugin.xml.
  */
  public static final String ID = "org.eclipse.jst.ws.creation.ui";

  /**
  * The reference to the singleton instance of this plugin.
  */
  private static WebServiceCreationUIPlugin instance_;

  /**
  * Constructs a runtime plugin object for this plugin.
  * The "plugin" element in plugin.xml should include the attribute
  * class = "org.eclipse.jst.ws.internal.creation.ui.plugin.WebServiceCreationUIPlugin".
  * @param descriptor The descriptor of this plugin.
  */
  public WebServiceCreationUIPlugin ()
  {
    super();
    instance_ = this;
  }

  /**
  * Returns the singleton instance of this plugin. Equivalent to calling
  * (WebServiceCreationUIPlugin)Platform.getPlugin("org.eclipse.jst.ws.creation.ui");
  * @return The WebServiceCreationUIPlugin singleton.
  */
  public static WebServiceCreationUIPlugin getInstance ()
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
}
