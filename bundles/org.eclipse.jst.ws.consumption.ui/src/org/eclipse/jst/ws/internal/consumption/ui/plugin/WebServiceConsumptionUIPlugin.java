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

package org.eclipse.jst.ws.internal.consumption.ui.plugin;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jst.ws.internal.consumption.ui.preferences.PersistentProjectTopologyContext;
import org.eclipse.jst.ws.internal.consumption.ui.preferences.PersistentServerRuntimeContext;
import org.eclipse.jst.ws.internal.consumption.ui.preferences.ProjectTopologyContext;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.wst.command.internal.env.core.common.MessageUtils;


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
  * Returns the message string identified by the given key from
  * the plugin.properties file for the appropriate locale.
  * @param key The message key string prefixed by a "%" symbol.
  * That is, the string passed in must be of the form "%KEY"
  * where the plugin.properties file contains a line of the
  * form: "KEY = value".
  * @return The locale-specific message.
  */
  public static String getMessage ( String key )
  {
    MessageUtils msgUtils = new MessageUtils( "org.eclipse.jst.ws.consumption.ui.plugin", instance_ );
    
    if( key.startsWith("%"))
    {
      key = key.substring( 1, key.length() );
    }
    
    return msgUtils.getMessage(key);
  }

  /**
  * Returns the message string identified by the given key from
  * the plugin.properties file for the appropriate locale.
  * Substitution sequences in the message string
  * are replaced by the given array of substitution objects (which
  * are most frequently strings). See java.text.MessageFormat for
  * further details on substitution.
  * @param key The message key string prefixed by a "%" symbol.
  * That is, the string passed in must be of the form "%KEY"
  * where the plugin.properties file contains a line of the
  * form: "KEY = value".
  * @param args The substitution values for the message
  * as required by the message in plugin.properties and
  * by the rules of class java.text.MessageFormat.
  * @return The locale-specific message.
  */
  public static String getMessage ( String key, Object[] args )
  {
    return MessageFormat.format(getMessage(key),args);
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
      URL installURL = instance_.getBundle().getEntry("/");;
      URL imageURL = new URL(installURL,name);
      return ImageDescriptor.createFromURL(imageURL);
    }
    catch (MalformedURLException e)
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
