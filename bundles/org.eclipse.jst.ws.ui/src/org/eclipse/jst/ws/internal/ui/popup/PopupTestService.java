/*******************************************************************************
 * Copyright (c) 2002, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060803   152790 mahutch@ca.ibm.com - Mark Hutchinson (created class)
 * 20070327   172339 kathy@ca.ibm.com - Kathy Chan
 *******************************************************************************/
package org.eclipse.jst.ws.internal.ui.popup;

import java.util.Iterator;
import java.util.Vector;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jst.j2ee.webservice.wsclient.ServiceRef;
import org.eclipse.jst.ws.internal.common.J2EEActionAdapterFactory;
import org.eclipse.wst.ws.internal.explorer.LaunchOption;
import org.eclipse.wst.ws.internal.explorer.WSExplorerLauncherCommand;
import org.eclipse.wst.ws.internal.explorer.plugin.ExplorerPlugin;
import org.eclipse.wst.ws.internal.explorer.popup.PopupTestWSDL;
import org.eclipse.wst.ws.internal.ui.utils.AdapterUtils;


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
    IStructuredSelection selection = getWorkbenchSelection();
    Vector launchOptions = new Vector();
    if (selection != null)
    {      
      for (Iterator it = selection.iterator(); it.hasNext();)
      {
        String wsdlURL = null;
        Object object = it.next();
        
        if (object instanceof ServiceRef)
        {
          ServiceRef serviceImpl = (ServiceRef)object;
          wsdlURL = J2EEActionAdapterFactory.getWSDLURI(serviceImpl);
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
