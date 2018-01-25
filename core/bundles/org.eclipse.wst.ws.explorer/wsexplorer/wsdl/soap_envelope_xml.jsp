<%
	/*******************************************************************************
	 * Copyright (c) 2001, 2014 IBM Corporation and others.
	 * All rights reserved. This program and the accompanying materials
	 * are made available under the terms of the Eclipse Public License v1.0
	 * which accompanies this distribution, and is available at
	 * http://www.eclipse.org/legal/epl-v10.html
	 * 
	 * Contributors:
	 * IBM Corporation - initial API and implementation
	 * yyyymmdd bug      Email and other contact information
	 * -------- -------- -----------------------------------------------------------
	 * 20070413   176493 makandre@ca.ibm.com - Andrew Mak, WSE: Make message/transport stack pluggable
	 *******************************************************************************/

	String userAgent = request.getHeader("User-Agent");
	
	// Safari has no native support for viewing raw XML, so display as plain text
	if(userAgent != null && userAgent.contains("Safari")) {
		response.setContentType("text/plain");
	} else {
		response.setContentType("text/xml");
	}
	
%><%@ page 
	import="org.eclipse.wst.ws.internal.explorer.platform.wsdl.perspective.*,
                                                         org.eclipse.wst.ws.internal.explorer.platform.wsdl.constants.*,
                                                         org.eclipse.wst.ws.internal.explorer.platform.wsdl.datamodel.WSDLOperationElement,
                                                         org.eclipse.wst.ws.internal.explorer.transport.*"
   %><jsp:useBean id="controller"
	class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller"
	scope="session" /><%

	int soapEnvelopeType = Integer.parseInt(request.getParameter(WSDLActionInputs.SOAP_ENVELOPE_TYPE));
	WSDLPerspective wsdlPerspective = controller.getWSDLPerspective();
	WSDLOperationElement operElement = (WSDLOperationElement) wsdlPerspective.getOperationNode().getTreeElement();
	ISOAPMessage soapMessage;
	switch (soapEnvelopeType) {
	case WSDLActionInputs.SOAP_ENVELOPE_TYPE_REQUEST:
		soapMessage = (ISOAPMessage) operElement.getPropertyAsObject(WSDLModelConstants.PROP_SOAP_REQUEST);
		break;
	case WSDLActionInputs.SOAP_ENVELOPE_TYPE_RESPONSE:
	default:
		soapMessage = (ISOAPMessage) operElement.getPropertyAsObject(WSDLModelConstants.PROP_SOAP_RESPONSE);
		break;
	}
	String messages = soapMessage.toXML();	
%><%=messages%>