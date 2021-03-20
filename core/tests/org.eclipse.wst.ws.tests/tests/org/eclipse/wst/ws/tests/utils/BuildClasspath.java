/*******************************************************************************
 * Copyright (c) 2007, 2021 IBM Corporation and others.
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.ws.tests.utils;

import java.util.Enumeration;
import java.util.ResourceBundle;

import org.eclipse.core.runtime.Platform;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.osgi.util.ManifestElement;
import org.osgi.framework.Bundle;
import org.osgi.framework.Constants;

/**
 * This program will generate a classpath that can be used to build the wsexplorer.war file.
 * 
 */
public class BuildClasspath implements IApplication
{
    public Object run(Object args) throws Exception 
    {        
      ResourceBundle pluginRequires = ResourceBundle.getBundle( "org.eclipse.wst.command.env.property.tools.ExplorerRequires" );
      Enumeration    requireKeys   = pluginRequires.getKeys();
      
      while( requireKeys.hasMoreElements() )
      {
        String key = (String)requireKeys.nextElement();
        buildClasspath( pluginRequires.getString(key) );
      }
            
      return IApplication.EXIT_OK;
    }
    
    private void buildClasspath( String pluginId ) throws Exception
    {
      Bundle bundle    = Platform.getBundle( pluginId );
      String classpath = (String)bundle.getHeaders().get( Constants.BUNDLE_CLASSPATH );
      String location  = bundle.getLocation();
              
      // Add classpath to list
      ManifestElement[] classpaths = ManifestElement.parseHeader(Constants.BUNDLE_CLASSPATH, classpath);
      
      if( classpaths != null )
      {
        for( int index = 0; index < classpaths.length; index++ )
        {
          String newPath = null;
          
          // We have a special case for the SWT plugin since it
          if( location.indexOf( "org.eclipse.swt" ) != -1 )
          {
              Bundle[] bundles     = Platform.getFragments( bundle );  
              String   newLocation = bundles[0].getLocation().substring(8);
              String   path        = classpaths[index].getValue().replaceFirst( "\\$ws\\$", "ws/win32");
              
              newPath = newLocation + path;
          }
          else if( location.startsWith( "update" ) )
          {
              newPath = location.substring(8) + classpaths[index].getValue();    
          }
          else if( location.startsWith( "initial" ))
          {
              newPath = location.substring(23) + classpaths[index].getValue();
          }
          else if( location.startsWith( "System" ))
          {
              String platformLocation = Platform.getInstallLocation().getURL().toString().substring(6);
              newPath = platformLocation + "plugins/org.eclipse.osgi_3.0.1/" + classpaths[index].getValue();
          }
          
          System.out.print( newPath + ';'); 
        }
      }
    }

	public Object start(IApplicationContext context) throws Exception {
		return run(context.getArguments().get(IApplicationContext.APPLICATION_ARGS));
	}

	public void stop() {
	}
}
