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
 * 20060606   105069 mahutch@ca.ibm.com - Mark Hutchinson
 * 20060803   152790 mahutch@ca.ibm.com - Mark Hutchinson
 * 20070327   172339 kathy@ca.ibm.com - Kathy Chan
 *******************************************************************************/
package org.eclipse.wst.ws.internal.explorer.popup;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.ws.internal.explorer.LaunchOption;
import org.eclipse.wst.ws.internal.explorer.LaunchOptions;
import org.eclipse.wst.ws.internal.explorer.WSExplorerLauncherCommand;
import org.eclipse.wst.ws.internal.explorer.plugin.ExplorerPlugin;
import org.eclipse.wst.ws.internal.monitor.GetMonitorCommand;
import org.eclipse.wst.ws.internal.parser.wsil.WebServicesParser;
import org.eclipse.wst.ws.internal.ui.utils.AdapterUtils;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.internal.impl.ServiceImpl;
import org.eclipse.wst.wsdl.util.WSDLResourceImpl;

public class PopupTestWSDL extends Action implements IActionDelegate
{
  public PopupTestWSDL()
  {
    super(ExplorerPlugin.getMessage("%POPUP_TEST_WSDL"));
  }

  protected IStructuredSelection getWorkbenchSelection()
  {
    IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
    if (window != null)
    {
      ISelection selection = window.getSelectionService().getSelection();
      if (selection instanceof IStructuredSelection)
        return (IStructuredSelection)selection;
    }
    return null;
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
        if (object instanceof IResource)
        {
        	File wsdlFile = ((IResource)object).getLocation().toFile();
        	try
        	{
        		wsdlURL = wsdlFile.toURL().toString();
        	}
        	catch (MalformedURLException murle)
        	{
        		wsdlURL = wsdlFile.toString();
        	}
        } else if (object instanceof ServiceImpl)
        {
        	ServiceImpl serviceImpl = (ServiceImpl)object;          
        	Definition definition = serviceImpl.getEnclosingDefinition();        
        	wsdlURL = definition.getLocation();
        } else if (object instanceof WSDLResourceImpl)
        {
        	WSDLResourceImpl WSDLRImpl = (WSDLResourceImpl)object;
        	Definition definition = WSDLRImpl.getDefinition();
        	wsdlURL = definition.getLocation();
        } else if (object instanceof String) {
        	wsdlURL = (String) object;
        } else {
        	// Object is not any types we recognized, wsdlURL is still null.
          	// Try looking up an adapter for the object.
        	// If found, update wsdlURL contains the adapted WSDL string.  
        	// If not found, wsdlURL would still be null.
        	wsdlURL = getAdaptedWSDL(object);
        }
        
       addLaunchOptions(launchOptions, wsdlURL, stateLocation, defaultFavoritesLocation);        
      }
    }
    command.setLaunchOptions((LaunchOption[])launchOptions.toArray(new LaunchOption[0]));
    command.execute();
  }

  /**
   * Set and add the WEB_SERVICE_ENDPOINT, WSDL_URL, STATE_LOCATIION and 
   * DEFAULT_FAVORITES_LOCATION LaunchOptions to the launchOptions vector
   * 
   * @param launchOptions - vector of launchOptions to add to
   * @param wsdlURL
   * @param stateLocation
   * @param defaultFavoritesLocation
   */
  protected void addLaunchOptions(Vector launchOptions, String wsdlURL, String stateLocation, String defaultFavoritesLocation)
  {
	  GetMonitorCommand getMonitorCmd = new GetMonitorCommand();
      getMonitorCmd.setMonitorService(true);
      getMonitorCmd.setCreate(false);
      getMonitorCmd.setWebServicesParser(new WebServicesParser());
      getMonitorCmd.setWsdlURI(wsdlURL);
      getMonitorCmd.execute(null, null);
      List endpoints = getMonitorCmd.getEndpoints();
      for (Iterator endpointsIt = endpoints.iterator(); endpointsIt.hasNext();)
      {
    	  launchOptions.add(new LaunchOption(LaunchOptions.WEB_SERVICE_ENDPOINT, (String)endpointsIt.next()));
      }
      launchOptions.add(new LaunchOption(LaunchOptions.WSDL_URL, wsdlURL));
	  launchOptions.add(new LaunchOption(LaunchOptions.STATE_LOCATION,stateLocation));
	  launchOptions.add(new LaunchOption(LaunchOptions.DEFAULT_FAVORITES_LOCATION,defaultFavoritesLocation));
  }
  
  public void run(IAction action)
  {
    run();
  }

  public void selectionChanged(IAction action, ISelection selection)
  {
  }
  
  /**
   * @param object Look up an adapter mapping the object to IFile or String.
   * @return The WSDL string returned by the adapter or null if no adapter is found.
   */
  public static String getAdaptedWSDL (Object object) {
	  return AdapterUtils.getAdaptedWSDL(object);
  }
  
}
