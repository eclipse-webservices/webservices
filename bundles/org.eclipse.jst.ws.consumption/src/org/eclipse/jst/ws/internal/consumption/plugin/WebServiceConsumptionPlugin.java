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

package org.eclipse.jst.ws.internal.consumption.plugin;

import java.text.MessageFormat;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPluginDescriptor;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.wst.command.env.core.common.Log;
import org.eclipse.wst.command.env.core.common.MessageUtils;
import org.eclipse.wst.command.env.eclipse.EclipseLog;


/**
* This is the plugin class for the Web Services plugin.
* <p>
* This plugin contains the bulk of the Web Services runtime.
* Only the graphical user interface portion of the runtime is
* found elsewhere - in the org.eclipse.jst.ws.ui plugin.
*/
public class WebServiceConsumptionPlugin extends Plugin
{
  // Copyright
  public static final String copyright = "(c) Copyright IBM Corporation 2000, 2002.";

  /**
  * The identifier of the descriptor of this plugin in plugin.xml.
  */
  public static final String ID = "org.eclipse.jst.ws.consumption";

  /**
  * The reference to the singleton instance of this plugin.
  */
  private static WebServiceConsumptionPlugin instance_;

  private Log log_;
 
  /**
  * Constructs a runtime plugin object for this plugin.
  * The "plugin" element in plugin.xml should include the attribute
  * class = "org.eclipse.jst.ws.internal.consumption.plugin.WebServiceConsumptionPlugin".
  * @param descriptor The descriptor of this plugin.
  */
  public WebServiceConsumptionPlugin ( IPluginDescriptor descriptor )
  {
    super(descriptor);
    if (instance_ == null)
    {
      instance_ = this;
    }
    log_ = new EclipseLog();
  }

  /**
  * Returns the singleton instance of this plugin. Equivalent to calling
  * (WebServiceConsumptionPlugin)Platform.getPlugin("org.eclipse.jst.ws");
  * @return The WebServiceConsumptionPlugin singleton.
  */
  public static WebServiceConsumptionPlugin getInstance ()
  {
    return instance_;
  }

  /**
  * Called once by the platform when this plugin is first loaded.
  * @throws CoreException If this plugin fails to start.
  */
  public void startup () throws CoreException
  {
  	log_.log(Log.INFO, 5042, this, "startup", "Starting plugin org.eclipse.jst.ws.consumption");
    super.startup();
  }

  /**
  * Called once by the platform when this plugin is unloaded.
  * @throws CoreException If this plugin fails to shutdown.
  */
  public void shutdown () throws CoreException
  {
    log_.log(Log.INFO, 5043, this, "shutdown", "Shutting plugin org.eclipse.jst.ws.consumption");
    super.shutdown();
  }
  
  /**
  * Returns the message string identified by the given key from
  * plugin.properties.
  * @return The String message.
  */
  public static String getMessage ( String key )
  {
    MessageUtils msgUtils = new MessageUtils( "org.eclipse.jst.ws.internal.consumption.plugin", instance_ );
    
    if( key.startsWith("%"))
    {
      key = key.substring( 1, key.length() );
    }
    
    return msgUtils.getMessage(key);
  }

  /**
  * Returns the message string identified by the given key from
  * plugin.properties. Substitution sequences in the message string
  * are replaced by the given array of substitution objects (which
  * are most frequently strings). See the JDK's
  * {@link java.text.MessageFormat java.text.MessageFormat}
  * class for further details on substitution.
  * @return The String message.
  */
  public static String getMessage ( String key, Object[] args )
  {
    return MessageFormat.format(getMessage(key),args);
  }

}
