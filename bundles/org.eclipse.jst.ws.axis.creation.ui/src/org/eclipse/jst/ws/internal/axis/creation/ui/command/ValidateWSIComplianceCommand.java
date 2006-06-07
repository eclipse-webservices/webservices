/*******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060607   144978 kathy@ca.ibm.com - Kathy Chan
 *******************************************************************************/

package org.eclipse.jst.ws.internal.axis.creation.ui.command;

import java.util.Vector;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.ws.internal.axis.consumption.core.AxisConsumptionCoreMessages;
import org.eclipse.jst.ws.internal.axis.consumption.core.common.JavaWSDLParameter;
import org.eclipse.jst.ws.internal.axis.creation.ui.AxisCreationUIMessages;
import org.eclipse.wst.command.internal.env.core.common.StatusUtils;
import org.eclipse.wst.common.environment.IEnvironment;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;
import org.eclipse.wst.ws.internal.preferences.PersistentWSIContext;
import org.eclipse.wst.ws.internal.preferences.WSIComplianceUtils;


public class ValidateWSIComplianceCommand extends AbstractDataModelOperation
{
   private JavaWSDLParameter javaWSDLParam = null;
   private Vector status = null;
   private IProject serviceProject_ = null;

   public ValidateWSIComplianceCommand() {
  }

   public IStatus execute (IProgressMonitor monitor, IAdaptable adaptable)
  {
 
	   IEnvironment environment = getEnvironment();

	   if (javaWSDLParam == null) {
		   IStatus simpleStatus = StatusUtils.errorStatus( AxisConsumptionCoreMessages.MSG_ERROR_JAVA_WSDL_PARAM_NOT_SET);
		   environment.getStatusHandler().reportError(simpleStatus);
		   return simpleStatus;
	   }		
	
    status = new Vector();
    
    PersistentWSIContext wsiContext = new PersistentWSIContext();
    int severity = WSIComplianceUtils.getWSISeverity(serviceProject_, wsiContext);
    checkRPCEncoded(severity);
    Status[] statuses = (Status[]) status.toArray(new Status[status.size()]);

    if (statuses.length == 0 ||
    		WSIComplianceUtils.checkWSICompliance (environment.getStatusHandler(), statuses, serviceProject_, wsiContext))
    	return Status.OK_STATUS;
    else {
    	return StatusUtils.errorStatus("");
    }
  }

  private void checkRPCEncoded(int severity)
  {
  		if (JavaWSDLParameter.STYLE_RPC.equals(javaWSDLParam.getStyle()) && 
  		   JavaWSDLParameter.USE_ENCODED.equals(javaWSDLParam.getUse()))
  		   status.add (new Status(severity, "ValidateWSIComplianceCommand", 0, 
  				 AxisCreationUIMessages.WSI_INCOMPLIANCE_RPC_ENCODED, null));

  }

  /**
   * Returns the message string identified by the given key from
   * plugin.properties.
   * @return The String message.
   */

/**
 * @param javaWSDLParam The javaWSDLParam to set.
 * @todo Generated comment
 */
public void setJavaWSDLParam(JavaWSDLParameter javaWSDLParam) {
	this.javaWSDLParam = javaWSDLParam;
}

/**
 * @param serviceProject_ The serviceProject_ to set.
 * @todo Generated comment
 */
public void setServiceProject(IProject serviceProject_) {
	this.serviceProject_ = serviceProject_;
}
}
