/*******************************************************************************
 * Copyright (c) 2008 IBM Corporation and others.
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
 * 20080324   223118 ericdp@ca.ibm.com - Eric D. Peters, metadata required in WS-I profiles to support editors & validators
 *******************************************************************************/
package org.eclipse.wst.ws.internal.preferences;

public interface WSIServicePoliciesConstants {
	public static final String stateKeyDefaultProtocol = "defaultProtocol";
	public static final String stateKeyDefaultProtocol_VALUE_SOAP = "http://schemas.xmlsoap.org/wsdl/soap/";
	public static final String stateKeyDefaultProtocol_VALUE_SOAP12 = "http://schemas.xmlsoap.org/wsdl/soap12/";
	
	public static final String ServicePolicyID_SSBP10 = "org.eclipse.wst.ws.service.policy.ui.servicepols.wsiprofilecomp.wsissbp";
	public static final String ServicePolicyID_AP10 = "org.eclipse.wst.ws.service.policy.ui.servicepols.wsiprofilecomp.wsiap";
	public static final String ServicePolicyID_WSIProfileRoot = "org.eclipse.wst.ws.service.policy.ui.servicepols.wsiprofilecomp";
	
	public static final String stateEnumRequire = "org.eclipse.wst.sug.require";
	public static final String stateEnumSuggest = "org.eclipse.wst.sug.suggest";
	public static final String stateEnumIgnore = "org.eclipse.wst.sug.ignore";


}
