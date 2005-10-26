/*******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.jst.ws.internal.consumption.ui.command;

import java.util.Vector;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.wst.command.internal.env.core.common.MessageUtils;
import org.eclipse.wst.command.internal.env.core.common.StatusUtils;
import org.eclipse.wst.common.environment.IEnvironment;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;
import org.eclipse.wst.ws.internal.ui.plugin.WSUIPlugin;
import org.eclipse.wst.ws.internal.ui.wsi.preferences.PersistentWSIAPContext;
import org.eclipse.wst.ws.internal.ui.wsi.preferences.PersistentWSISSBPContext;
import org.eclipse.wst.ws.internal.ui.wsi.preferences.WSIComplianceUtils;



public class WSINonCompliantRuntimeCommand extends AbstractDataModelOperation
{

  private IProject serviceProject_;
  private MessageUtils msgUtils_;
	
  public WSINonCompliantRuntimeCommand()
  {
    String       pluginId = "org.eclipse.wst.ws.ui";
  	msgUtils_ = new MessageUtils( pluginId + ".plugin", this );
  }
  

  public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable )
  {
    IEnvironment environment = getEnvironment();
    
  	PersistentWSISSBPContext wsiSSBPContext = WSUIPlugin.getInstance().getWSISSBPContext(); 
  	PersistentWSIAPContext wsiAPContext = WSUIPlugin.getInstance().getWSIAPContext(); 
    Vector statusSSBP = new Vector();
    statusSSBP.add( new Status( WSIComplianceUtils.getWSISeverity(serviceProject_, wsiSSBPContext), "id", 0,
        msgUtils_.getMessage("WSI_SSBP_INCOMPLIANT_RUNTIME"), null ) );
    Status[] statusesSSBP = (Status[]) statusSSBP.toArray(new Status[statusSSBP.size()]);
    
    Vector statusAP = new Vector();
    statusAP.add( new Status( WSIComplianceUtils.getWSISeverity(serviceProject_, wsiAPContext), "id", 0, 
    		msgUtils_.getMessage("WSI_AP_INCOMPLIANT_RUNTIME"), null ));
    Status[] statusesAP = (Status[]) statusAP.toArray(new Status[statusAP.size()]);

    if (WSIComplianceUtils.checkWSICompliance (environment.getStatusHandler(), statusesSSBP, serviceProject_, wsiSSBPContext)) 
    {
    	if (WSIComplianceUtils.checkWSICompliance (environment.getStatusHandler(), statusesAP, serviceProject_, wsiAPContext)) 
      {
    		return Status.OK_STATUS;
    	} 
      else 
      {
    		return StatusUtils.errorStatus(	msgUtils_.getMessage("NOT_OK") );
    	}
    } 
    else 
    {
		  return StatusUtils.errorStatus( msgUtils_.getMessage("NOT_OK") );
		}
  }
  
  public void setServiceProject(IProject serviceProject) {
	this.serviceProject_ = serviceProject;
  }
}
