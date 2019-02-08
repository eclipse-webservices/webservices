/*******************************************************************************
 * Copyright (c) 2001, 2007 IBM Corporation and others.
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
 * 20070305   117034 makandre@ca.ibm.com - Andrew Mak, Web Services Explorer should support SOAP Headers
 * 20070413   176493 makandre@ca.ibm.com - Andrew Mak, WSE: Make message/transport stack pluggable
 *******************************************************************************/

package org.eclipse.wst.ws.internal.explorer.platform.wsdl.constants;

public class WSDLModelConstants
{
    // Relation
    public final static String REL_WSDL = "relWSDL";
    public final static String REL_WSDL_SERVICE = "relWSDLService";
    public final static String REL_WSDL_BINDING = "relWSDLBinding";
    public final static String REL_WSDL_OPERATION = "relWSDLOperation";

    // Documentation
    public final static String PROP_DOCUMENTATION = "propDocumentation";

    // Source Content
    public final static String PROP_SOURCE_CONTENT_HEADER = "propSourceContentHeader";
    public final static String PROP_SOURCE_CONTENT = "propSourceContent";
    public final static String PROP_SOURCE_CONTENT_NAMESPACE = "propSourceContentNS";
    
    // Transport
    public final static String PROP_SOAP_REQUEST_TMP = "propSOAPRequestTmp";
    public final static String PROP_SOAP_REQUEST = "propSOAPRequest";    
    public final static String PROP_SOAP_RESPONSE = "propSOAPResponse";
}
