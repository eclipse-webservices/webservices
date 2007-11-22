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
 * 20071122   209858 pmoogk@ca.ibm.com - Peter Moogk, Initial coding.
 *******************************************************************************/
package org.eclipse.wst.ws.service.policy.ui.utils;

import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.activities.IActivityManager;
import org.eclipse.ui.activities.IIdentifier;
import org.eclipse.wst.ws.service.policy.IFilter;
import org.eclipse.wst.ws.service.policy.IServicePolicy;

public class ActivityUtils
{
  public static IFilter getCurrentActivitiesFilter()
  {
    final IWorkbench       workbench = PlatformUI.getWorkbench();
    final IActivityManager manager   = workbench.getActivitySupport().getActivityManager();
         
    return new IFilter()
    {
      public boolean accept( IServicePolicy policy )
      { 
        IIdentifier identifier = manager.getIdentifier( policy.getId() );
        
        return identifier == null ? false : identifier.isEnabled();
      }  
    };
  }
}
