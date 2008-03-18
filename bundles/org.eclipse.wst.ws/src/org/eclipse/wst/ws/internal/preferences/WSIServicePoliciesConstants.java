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
 * 20080318   223118 ericdp@ca.ibm.com - Eric D. Peters, metadata required in WS-I profiles to support editors & validators
 *******************************************************************************/
package org.eclipse.wst.ws.internal.preferences;

public interface WSIServicePoliciesConstants {
	public static final String stateKeyDefaultProtocol = "defaultProtocol";
	public static final String stateKeyDefaultProtocol_VALUE_SOAP = "http://schemas.xmlsoap.org/wsdl/soap/";
	public static final String stateKeyDefaultProtocol_VALUE_SOAP12 = "http://schemas.xmlsoap.org/wsdl/soap12/";
	
	public static final String ServicePolicyID_SSBP10 = "org.eclipse.wst.ws.service.policy.ui.servicepols.wsiprofilecomp.wsissbp";
	public static final String ServicePolicyID_AP10 = "org.eclipse.wst.ws.service.policy.ui.servicepols.wsiprofilecomp.wsiap";	

}
