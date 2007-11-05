/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20071105   196997 pmoogk@ca.ibm.com - Peter Moogk
 *******************************************************************************/
package org.eclipse.wst.ws.service.internal.policy.migration;

import org.eclipse.wst.ws.service.policy.listeners.IPolicyPlatformLoadListener;

public class MigrateWSIpreferencesLoadListener implements IPolicyPlatformLoadListener
{
  public void load()
  {
    // We need to check to see if the user has set any WSI preferences.
    // If not we want to default WSI preferences to any old WSI preferences that
    // have been set.
    
    //IEclipsePreferences preferences = new InstanceScope().getNode( ServicePolicyActivator.PLUGIN_ID );
    //IServicePolicy           apPolicy    = platform.getServicePolicy( "org.eclipse.wst.ws.service.policy.ui.servicepols.wsiprofilecomp.wsiap" );
    //IPolicyStateEnum         apState     = apPolicy.getPolicyStateEnum();
    //IServicePolicy           ssbpPolicy  = platform.getServicePolicy( "org.eclipse.wst.ws.service.policy.ui.servicepols.wsiprofilecomp.wsissbp" );
  }
}
