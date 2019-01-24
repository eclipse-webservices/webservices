/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20080318   223118 ericdp@ca.ibm.com - Eric D. Peters, metadata required in WS-I profiles to support editors & validators
 *******************************************************************************/

package org.eclipse.wst.ws.internal.preferences;

import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.wst.ws.internal.WstWSPluginMessages;
import org.eclipse.wst.ws.internal.plugin.WSPlugin;



public class PersistentWSISSBPContext extends PersistentWSIContext
{
	private static final String NON_WSI_SSBP_COMPLIANCE = "nonWSISSBPCompliance";

  public PersistentWSISSBPContext () 
  {
	  super();
	  non_wsi_compliance = NON_WSI_SSBP_COMPLIANCE;
	  name = new QualifiedName(WSPlugin.ID , non_wsi_compliance);
	  wsiWarning_ = WstWSPluginMessages.WSI_SSBP_WARNING;
	  wsiError_ = WstWSPluginMessages.WSI_SSBP_ERROR;
  }

  protected String getServicePolicyId()
  {
    return WSIServicePoliciesConstants.ServicePolicyID_SSBP10;
  }  
}
