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
import org.eclipse.wst.command.internal.provisional.env.core.EnvironmentalOperation;
import org.eclipse.wst.command.internal.provisional.env.core.common.Environment;
import org.eclipse.wst.command.internal.provisional.env.core.common.MessageUtils;
import org.eclipse.wst.command.internal.provisional.env.core.common.SimpleStatus;
import org.eclipse.wst.command.internal.provisional.env.core.common.Status;
import org.eclipse.wst.ws.internal.ui.plugin.WSUIPlugin;
import org.eclipse.wst.ws.internal.ui.wsi.preferences.PersistentWSIAPContext;
import org.eclipse.wst.ws.internal.ui.wsi.preferences.PersistentWSISSBPContext;
import org.eclipse.wst.ws.internal.ui.wsi.preferences.WSIComplianceUtils;



public class WSINonCompliantRuntimeCommand extends EnvironmentalOperation
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
    Environment environment = getEnvironment();
    
  	PersistentWSISSBPContext wsiSSBPContext = WSUIPlugin.getInstance().getWSISSBPContext(); 
  	PersistentWSIAPContext wsiAPContext = WSUIPlugin.getInstance().getWSIAPContext(); 
    Vector statusSSBP = new Vector();
    statusSSBP.add(new SimpleStatus("WSINonCompliantRuntimeCommand", 
    		msgUtils_.getMessage("WSI_SSBP_INCOMPLIANT_RUNTIME"), WSIComplianceUtils.getWSISeverity(serviceProject_, wsiSSBPContext)));
    Status[] statusesSSBP = (Status[]) statusSSBP.toArray(new Status[statusSSBP.size()]);
    
    Vector statusAP = new Vector();
    statusAP.add(new SimpleStatus("WSINonCompliantRuntimeCommand", 
    		msgUtils_.getMessage("WSI_AP_INCOMPLIANT_RUNTIME"), WSIComplianceUtils.getWSISeverity(serviceProject_, wsiAPContext)));
    Status[] statusesAP = (Status[]) statusAP.toArray(new Status[statusAP.size()]);

    if (WSIComplianceUtils.checkWSICompliance (environment.getStatusHandler(), statusesSSBP, serviceProject_, wsiSSBPContext)) {
    	if (WSIComplianceUtils.checkWSICompliance (environment.getStatusHandler(), statusesAP, serviceProject_, wsiAPContext)) {
    		return new SimpleStatus( "" );
    	} else {
    		return new SimpleStatus(
    				"WSINonCompliantRuntimeCommand",
    				msgUtils_.getMessage("NOT_OK"),
    				Status.ERROR);
    	}
    } else {
		return new SimpleStatus(
				"WSINonCompliantRuntimeCommand",
				msgUtils_.getMessage("NOT_OK"),
				Status.ERROR);
		}
  }

  public Status undo(Environment environment)
  {
    return null;
  }

  public Status redo(Environment environment)
  {
    return null;
  }
  
  public void setServiceProject(IProject serviceProject) {
	this.serviceProject_ = serviceProject;
  }
}
