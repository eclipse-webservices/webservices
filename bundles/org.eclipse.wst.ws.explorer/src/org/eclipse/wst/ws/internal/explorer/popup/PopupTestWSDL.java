/*******************************************************************************
 * Copyright (c) 2002, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
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
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.internal.impl.ServiceImpl;

public class PopupTestWSDL extends Action implements IActionDelegate
{
  public PopupTestWSDL()
  {
    super(ExplorerPlugin.getMessage("%POPUP_TEST_WSDL"));
  }

  private IStructuredSelection getWorkbenchSelection()
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
        }
       
        if (object instanceof ServiceImpl)
        {
          ServiceImpl serviceImpl = (ServiceImpl)object;          
          Definition definition = serviceImpl.getEnclosingDefinition();        
          wsdlURL = definition.getLocation();
        }        
        /* TODO: Move this up to org.eclipse.jst.ws.ui.
        if (object instanceof ServiceRef)
        {
          ServiceRef serviceImpl = (ServiceRef)object;
          wsdlURL = J2EEActionAdapterFactory.getWSDLURI(serviceImpl);
        }
        if (object instanceof WSDLResourceImpl)
        {
          WSDLResourceImpl WSDLRImpl = (WSDLResourceImpl)object;
          wsdlURL = J2EEActionAdapterFactory.getWSDLURI(WSDLRImpl);
        }
        */
        GetMonitorCommand getMonitorCmd = new GetMonitorCommand();
        getMonitorCmd.setMonitorService(true);
        getMonitorCmd.setCreate(false);
        getMonitorCmd.setWebServicesParser(new WebServicesParser());
        getMonitorCmd.setWsdlURI(wsdlURL);
        getMonitorCmd.execute(null, null);
        List endpoints = getMonitorCmd.getEndpoints();
        for (Iterator endpointsIt = endpoints.iterator(); endpointsIt.hasNext();)
          launchOptions.add(new LaunchOption(LaunchOptions.WEB_SERVICE_ENDPOINT, (String)endpointsIt.next()));
        launchOptions.add(new LaunchOption(LaunchOptions.WSDL_URL, wsdlURL));
		launchOptions.add(new LaunchOption(LaunchOptions.STATE_LOCATION,stateLocation));
		launchOptions.add(new LaunchOption(LaunchOptions.DEFAULT_FAVORITES_LOCATION,defaultFavoritesLocation));
      }
    }
    command.setLaunchOptions((LaunchOption[])launchOptions.toArray(new LaunchOption[0]));
    command.execute();
  }

  public void run(IAction action)
  {
    run();
  }

  public void selectionChanged(IAction action, ISelection selection)
  {
  }
}
