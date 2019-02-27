/*******************************************************************************
 * Copyright (c) 2002-2005 IBM Corporation and others.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   IBM - Initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsi.ui.internal;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * WSIValidatePlugin is a plugin that integrates with the Eclipse platform UI. 
 * 
 * @author David Lauzon, IBM
 * @author Lawrence Mandel, IBM
 */

public class WSIUIPlugin extends AbstractUIPlugin
{
  /**
   * The singleton.
   */
  protected static WSIUIPlugin instance;                
  public static final String PLUGIN_ID = "org.eclipse.wst.wsi.ui";
  /**
   * Constructor.
   */
  public WSIUIPlugin()
  {
    super();
    instance = this;
  }    

  /**
   * Returns the singleton. (Based on the Singleton Pattern).
   */
  public static WSIUIPlugin getInstance()
  {
    return instance;
  }

  protected ImageRegistry createImageRegistry() 
  {
		ImageRegistry registry = super.createImageRegistry();
		registerImage(registry, Resource.VALIDATE_WSI_LOGFILE_WIZ);
		return registry;
  }

	/**
	 * Register an image with the registry.
	 * 
	 * @param key the key
	 */
	private void registerImage(ImageRegistry registry, String key)
	{
  	  try 
  	  {
		ImageDescriptor id = ImageDescriptor.createFromFile(WSIUIPlugin.class, key);
		registry.put(key, id);
	  } catch (Exception e) 
	  {
      }
	}

  /**
   * Convience API.  
   * Finds the specified image stored in this plug-in's image registry.
   * @param iconName: the name of the image.
   * @return the image associated with the given name.
   */
  public static Image getResourceImage(String iconName)
  {
    return instance.getImageRegistry().get(iconName);
  }
}
