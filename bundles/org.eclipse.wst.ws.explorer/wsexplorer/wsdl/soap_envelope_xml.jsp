<%
/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
%>
<%@ page contentType="text/xml; charset=UTF-8" import="org.eclipse.wst.ws.internal.explorer.platform.wsdl.perspective.*,
                                                       org.eclipse.wst.ws.internal.explorer.platform.wsdl.constants.*" %>

<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<%
int soapEnvelopeType = Integer.parseInt(request.getParameter(WSDLActionInputs.SOAP_ENVELOPE_TYPE));
WSDLPerspective wsdlPerspective = controller.getWSDLPerspective();
SOAPMessageQueue soapMessageQueue;
switch (soapEnvelopeType)
{
  case WSDLActionInputs.SOAP_ENVELOPE_TYPE_REQUEST:
    soapMessageQueue = wsdlPerspective.getSOAPRequestQueue();
    break;
  case WSDLActionInputs.SOAP_ENVELOPE_TYPE_RESPONSE:
  default:
    soapMessageQueue = wsdlPerspective.getSOAPResponseQueue();
    break;
}
String messages = soapMessageQueue.getMessagesFromList();
%>
<%=messages%>
