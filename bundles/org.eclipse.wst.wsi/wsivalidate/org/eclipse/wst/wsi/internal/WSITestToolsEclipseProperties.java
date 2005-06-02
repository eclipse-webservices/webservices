/*******************************************************************************
 * Copyright (c) 2002-2005 IBM Corporation and others.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   IBM - Initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsi.internal;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.wst.ws.internal.ui.plugin.WSUIPlugin;
import org.eclipse.wst.ws.internal.ui.wsi.preferences.PersistentWSIContext;

/**
 * WS-I test tool properties specific for Eclipse.
 */
public class WSITestToolsEclipseProperties extends WSITestToolsProperties
{
 /**
  * @see org.eclipse.wst.wsi.internal.WSITestToolsProperties#checkWSIPreferences(java.lang.String)
  */
  public static WSIPreferences checkWSIPreferences(String fileuri)
  {
    WSIPreferences preferences = new WSIPreferences();
    
    // Remove file: and any slashes from the fileuri. 
    // Eclipse's resolution mechanism needs to start with the drive.
    String uriStr = trimURI(fileuri);

    WSUIPlugin wsui = WSUIPlugin.getInstance();
    PersistentWSIContext APcontext = wsui.getWSIAPContext();
    PersistentWSIContext SSBPcontext = wsui.getWSISSBPContext();
    
    IFile[] files = ResourcesPlugin.getWorkspace().getRoot().findFilesForLocation(new Path(uriStr));
    if (files != null && files.length == 1)
    {
      //check project level conpliance
      IProject project = files[0].getProject();
      
      if (APcontext.projectStopNonWSICompliances(project))
      {
        preferences.setTADFile(AP_ASSERTION_FILE);
        preferences.setComplianceLevel(PersistentWSIContext.STOP_NON_WSI);
      } 
      else if (APcontext.projectWarnNonWSICompliances(project))
      {
        preferences.setTADFile(AP_ASSERTION_FILE);
        preferences.setComplianceLevel(PersistentWSIContext.WARN_NON_WSI);
      }
      else if (SSBPcontext.projectStopNonWSICompliances(project))
      {
        preferences.setTADFile(SSBP_ASSERTION_FILE);
        preferences.setComplianceLevel(PersistentWSIContext.STOP_NON_WSI);
      }
      else if (SSBPcontext.projectWarnNonWSICompliances(project))
      {
        preferences.setTADFile(SSBP_ASSERTION_FILE);
        preferences.setComplianceLevel(PersistentWSIContext.WARN_NON_WSI);
      }
      else
      {
        preferences.setTADFile(DEFAULT_ASSERTION_FILE);
        preferences.setComplianceLevel(PersistentWSIContext.IGNORE_NON_WSI);
      }
    }
    else
    {
      // If we can't obtain the project preference use the global preference.
      String APlevel = APcontext.getPersistentWSICompliance();
      String SSBPlevel = SSBPcontext.getPersistentWSICompliance();
      if(APlevel.equals(PersistentWSIContext.STOP_NON_WSI))
      {
        preferences.setTADFile(AP_ASSERTION_FILE);
        preferences.setComplianceLevel(PersistentWSIContext.STOP_NON_WSI);
      }
      else if(APlevel.equals(PersistentWSIContext.WARN_NON_WSI))
      {
        preferences.setTADFile(AP_ASSERTION_FILE);
        preferences.setComplianceLevel(PersistentWSIContext.WARN_NON_WSI);
     }
     if(SSBPlevel.equals(PersistentWSIContext.STOP_NON_WSI))
     {
       preferences.setTADFile(SSBP_ASSERTION_FILE);
       preferences.setComplianceLevel(PersistentWSIContext.STOP_NON_WSI);
     }
     else if(SSBPlevel.equals(PersistentWSIContext.WARN_NON_WSI))
     {
      preferences.setTADFile(SSBP_ASSERTION_FILE);
        preferences.setComplianceLevel(PersistentWSIContext.WARN_NON_WSI);
     }
     else
     {
        preferences.setTADFile(DEFAULT_ASSERTION_FILE);
        preferences.setComplianceLevel(PersistentWSIContext.IGNORE_NON_WSI);
      }
    }
    return preferences;
  }

}
