/*******************************************************************************
 * Copyright (c) 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20080506   202945 pmoogk@ca.ibm.com - Peter Moogk, Allow WSE to launch on WSIL file.
 *******************************************************************************/
package org.eclipse.wst.ws.internal.explorer.popup;

import java.util.Vector;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.wst.ws.internal.explorer.LaunchOption;
import org.eclipse.wst.ws.internal.explorer.LaunchOptions;

public class PopupTestWSIL extends PopupTestWSDL
{
  protected IStructuredSelection selection = null;
	
  public PopupTestWSIL()
  {
    super();
  }


  protected void addLaunchOptions(Vector launchOptions, String wsdlURL, String stateLocation, String defaultFavoritesLocation)
  {
    launchOptions.add(new LaunchOption(LaunchOptions.WSIL_URL, wsdlURL));
	  launchOptions.add(new LaunchOption(LaunchOptions.STATE_LOCATION,stateLocation));
	  launchOptions.add(new LaunchOption(LaunchOptions.DEFAULT_FAVORITES_LOCATION,defaultFavoritesLocation));
  }
}
