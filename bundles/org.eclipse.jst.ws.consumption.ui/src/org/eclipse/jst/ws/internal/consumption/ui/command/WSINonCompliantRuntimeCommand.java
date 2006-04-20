/*******************************************************************************
 * Copyright (c) 2003, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060420   120714 kathy@ca.ibm.com - Kathy Chan
 *******************************************************************************/

package org.eclipse.jst.ws.internal.consumption.ui.command;

import java.util.Vector;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.wst.command.internal.env.core.common.StatusUtils;
import org.eclipse.wst.common.environment.IEnvironment;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;
import org.eclipse.wst.ws.internal.WstWSPluginMessages;
import org.eclipse.wst.ws.internal.plugin.WSPlugin;
import org.eclipse.wst.ws.internal.preferences.PersistentWSIAPContext;
import org.eclipse.wst.ws.internal.preferences.PersistentWSISSBPContext;
import org.eclipse.wst.ws.internal.preferences.WSIComplianceUtils;



public class WSINonCompliantRuntimeCommand extends AbstractDataModelOperation
{

  private IProject serviceProject_;
	
  public WSINonCompliantRuntimeCommand()
  {
  } 
  

  public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable )
  {
    IEnvironment environment = getEnvironment();
    
  	PersistentWSISSBPContext wsiSSBPContext = WSPlugin.getInstance().getWSISSBPContext(); 
  	PersistentWSIAPContext wsiAPContext = WSPlugin.getInstance().getWSIAPContext(); 
    Vector statusSSBP = new Vector();
    statusSSBP.add( new Status( WSIComplianceUtils.getWSISeverity(serviceProject_, wsiSSBPContext), "id", 0,
        WstWSPluginMessages.WSI_SSBP_INCOMPLIANT_RUNTIME, null ) );
    Status[] statusesSSBP = (Status[]) statusSSBP.toArray(new Status[statusSSBP.size()]);
    
    Vector statusAP = new Vector();
    statusAP.add( new Status( WSIComplianceUtils.getWSISeverity(serviceProject_, wsiAPContext), "id", 0, 
    		WstWSPluginMessages.WSI_AP_INCOMPLIANT_RUNTIME, null ));
    Status[] statusesAP = (Status[]) statusAP.toArray(new Status[statusAP.size()]);

    if (WSIComplianceUtils.checkWSICompliance (environment.getStatusHandler(), statusesAP, serviceProject_, wsiAPContext)) 
    {
    	if (WSIComplianceUtils.checkWSICompliance (environment.getStatusHandler(), statusesSSBP, serviceProject_, wsiSSBPContext)) 
      {
    		return Status.OK_STATUS;
    	} 
      else 
      {
    	  // Set message to empty string so that an error dialog does
    	  // not pop-up telling the user that they cannot continue
    	  // because they choose not to ignore the WS-I non-compliance warning.
    		return StatusUtils.errorStatus( "" );
    	}
    } 
    else 
    {
    	// Set message to empty string so that an error dialog does
  	  	// not pop-up telling the user that they cannot continue
  	  	// because they choose not to ignore the WS-I non-compliance warning.
		  return StatusUtils.errorStatus( "" );
	}
  }
  
  public void setServiceProject(IProject serviceProject) {
	this.serviceProject_ = serviceProject;
  }
}
