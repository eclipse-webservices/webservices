/*******************************************************************************
 * Copyright (c) 2002, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060803   152790 mahutch@ca.ibm.com - Mark Hutchinson (created class)
 * 20070327   172339 kathy@ca.ibm.com - Kathy Chan
 * 20080123   216372 kathy@ca.ibm.com - Kathy Chan
 * 20090127	  257618 mahutch@ca.ibm.com - Mark Hutchinson
 * 20090310   242440 yenlu@ca.ibm.com - Yen Lu, Pluggable IFile to URI Converter
 *******************************************************************************/
package org.eclipse.jst.ws.internal.ui.popup;

import java.util.Iterator;
import java.util.Vector;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jst.j2ee.webservice.wsclient.ServiceRef;
import org.eclipse.jst.ws.internal.common.J2EEActionAdapterFactory;
import org.eclipse.wst.ws.internal.explorer.LaunchOption;
import org.eclipse.wst.ws.internal.explorer.WSExplorerLauncherCommand;
import org.eclipse.wst.ws.internal.explorer.plugin.ExplorerPlugin;
import org.eclipse.wst.ws.internal.explorer.popup.PopupTestWSDL;
import org.eclipse.wst.ws.internal.ui.utils.AdapterUtils;
import org.eclipse.wst.ws.internal.wsfinder.WSDLURLStringWrapper;


public class PopupTestService extends PopupTestWSDL
{
  public PopupTestService()
  {
	  super();
  }

  public void run()
  {
	String stateLocation = ExplorerPlugin.getInstance().getPluginStateLocation();
	String defaultFavoritesLocation = ExplorerPlugin.getInstance().getDefaultFavoritesLocation();
  	WSExplorerLauncherCommand command = new WSExplorerLauncherCommand();
    command.setForceLaunchOutsideIDE(false);
    Vector launchOptions = new Vector();
    if (selection != null)
    {      
      for (Iterator it = selection.iterator(); it.hasNext();)
      {
        String wsdlURL = null;
        Object object = it.next();
        
        if (Platform.getAdapterManager().hasAdapter(object, WSDLURLStringWrapper.class.getName())) {
        	Object adaptedObject = Platform.getAdapterManager().loadAdapter(object, WSDLURLStringWrapper.class.getName());
        	WSDLURLStringWrapper wrapper = (WSDLURLStringWrapper)adaptedObject;
        	wsdlURL =wrapper.getWSDLURLString();  		
        }
        else if (object instanceof ServiceRef)
        {
          ServiceRef serviceRef = (ServiceRef)object;
          wsdlURL = J2EEActionAdapterFactory.getWSDLURI(serviceRef);
        } else if (object instanceof String) {
        	wsdlURL = (String) object;
        } else {
        	// Object is not any types we recognized, wsdlURL is still null.
          	// Try looking up an adapter for the object.
        	// If found, update wsdlURL contains the adapted WSDL string.  
        	// If not found, wsdlURL would still be null.
        	wsdlURL = AdapterUtils.getAdaptedWSDL(object);
        }
        
        addLaunchOptions(launchOptions, wsdlURL, stateLocation, defaultFavoritesLocation);
      }
    }
    command.setLaunchOptions((LaunchOption[])launchOptions.toArray(new LaunchOption[0]));
    command.execute();
  }
}
