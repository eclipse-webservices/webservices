package org.eclipse.wst.ws.internal.ui.plugin;

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

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;


/**
* This is the plugin class for the Web Services plugin.
* <p>
* This plugin contains the graphic user interface to the
* Web Services runtime found in org.eclipse.jst.ws.
*/
public class WSUIPlugin extends AbstractUIPlugin
{

 /**
 * The identifier of the descriptor of this plugin in plugin.xml.
 */
 public static final String ID = "org.eclipse.wst.ws.ui";

 /**
 * The reference to the singleton instance of this plugin.
 */
 private static WSUIPlugin instance_;
 
 /**
  * Constructs a runtime plugin object for this plugin.
  */
 public WSUIPlugin() {
	super();
	instance_ = this;
 }

 /**
 * Returns the singleton instance of this plugin. Equivalent to calling
 * (WSUIPlugin)Platform.getPlugin("org.eclipse.wst.ws.ui");
 * @return The WSUIPlugin singleton.
 */
 static public WSUIPlugin getInstance() {
   return instance_;
 }

 /**
 * Returns an image descriptor for the named resource
 * as relative to the plugin install location.
 * @return An image descriptor, possibly null.
 */
	public static ImageDescriptor getImageDescriptor(String name) {
		try {
			URL installURL = instance_.getBundle().getEntry("/");
			URL imageURL = new URL(installURL, name);
			return ImageDescriptor.createFromURL(imageURL);
		} catch (MalformedURLException e) {
     return null;
   }
 }

}
