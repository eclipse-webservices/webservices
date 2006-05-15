<%
/*******************************************************************************
 * Copyright (c) 2001, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060509   103072 mahutch@ca.ibm.com - Mark Hutchinson
 * 20060515   140607 mahutch@ca.ibm.com - Mark Hutchinson
 *******************************************************************************/
%>
<%@ page contentType="text/html; charset=UTF-8" import="org.eclipse.wst.ws.internal.explorer.platform.perspective.Node,
                                                        org.eclipse.wst.ws.internal.explorer.platform.wsdl.perspective.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.wsdl.constants.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.wsdl.datamodel.WSDLOperationElement" %>

<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>

<%
WSDLPerspective wsdlPerspective = controller.getWSDLPerspective();
wsdlPerspective.setStatusContentType(WSDLPerspective.STATUS_CONTENT_RESULT_SOURCE);
Node operNode = wsdlPerspective.getOperationNode();
WSDLOperationElement operElement = (WSDLOperationElement)operNode.getTreeElement();
String soapRequestTableContainerId = "SOAPRequest";
StringBuffer soapRequestTwistImageName = new StringBuffer("x");
soapRequestTwistImageName.append(soapRequestTableContainerId);
String soapResponseTableContainerId = "SOAPResponse";
StringBuffer soapResponseTwistImageName = new StringBuffer("x");
soapResponseTwistImageName.append(soapResponseTableContainerId);
%>

<script language="javascript">
  var sectionIds = ["<%=soapRequestTableContainerId%>","<%=soapResponseTableContainerId%>"];
  function toggleSOAPEnvelope(soapEnvelopeType,twistImageName)
  {
<%
    // If both envelopes are expanded, each should have 50% height. Otherwise, the remaining displayed envelope should be reset to 100% height.
%>
    var soapSectionId = sectionIds[soapEnvelopeType];
    var soapSection = document.getElementById(soapSectionId);
    var soapSectionTable = getTable(soapSectionId);
    var otherSoapSectionId = sectionIds[1-soapEnvelopeType];
    var otherSoapSection = document.getElementById(otherSoapSectionId);
    var otherSoapSectionTable = getTable(otherSoapSectionId);
    if (soapSection.style.display == "none")
    {
      if (otherSoapSection.style.display == "")
      {
        soapSectionTable.height = "50%";
        otherSoapSectionTable.height = "50%";
      }
      else
        soapSectionTable.height = "100%";
    }
    else
    {
      if (otherSoapSection.style.display == "")
        otherSoapSectionTable.height = "100%";
    }
    twist(soapSectionId,twistImageName);
  }
</script>

<table width="95%" border=0 cellpadding=6 cellspacing=0>
  <tr>
    <td height=20 valign="bottom" align="left" nowrap width=11><a href="javascript:toggleSOAPEnvelope(<%=WSDLActionInputs.SOAP_ENVELOPE_TYPE_REQUEST%>,'<%=soapRequestTwistImageName.toString()%>')"><img name="<%=soapRequestTwistImageName.toString()%>" src="<%=response.encodeURL(controller.getPathWithContext("images/twistclosed.gif"))%>" alt="<%=controller.getMessage("ALT_TWIST_CLOSED")%>" class="twist"></a></td>
    <td height=20 valign="bottom" align="left" nowrap class="labels">
      <strong><%=wsdlPerspective.getMessage("FORM_LABEL_SOAP_REQUEST_ENVELOPE")%></strong>
    </td>
    <td nowrap width="90%">&nbsp;</td>
  </tr>
</table>
<div id="<%=soapRequestTableContainerId%>" style="display:none">
  <table width="95%" height="50%" border=0 cellpadding=0 cellpadding=0>
    <tr>
      <td>
        <iframe name="requestEnvelopeFrame" frameborder=0 src="<%=response.encodeURL(controller.getPathWithContext(wsdlPerspective.getSOAPEnvelopeXMLLink(WSDLActionInputs.SOAP_ENVELOPE_TYPE_REQUEST)))%>" width="95%" height="100%"></iframe>
      </td>
    </tr>
  </table>
</div>
<script language="javascript">
  twist("<%=soapRequestTableContainerId%>","<%=soapRequestTwistImageName.toString()%>");
</script>

<%
if (operElement.getOperation().getOutput() != null)
{
%>
<table width="95%" border=0 cellpadding=6 cellspacing=0>
  <tr>
    <td height=20 valign="bottom" align="left" nowrap width=11><a href="javascript:toggleSOAPEnvelope(<%=WSDLActionInputs.SOAP_ENVELOPE_TYPE_RESPONSE%>,'<%=soapResponseTwistImageName.toString()%>')"><img name="<%=soapResponseTwistImageName.toString()%>" src="<%=response.encodeURL(controller.getPathWithContext("images/twistclosed.gif"))%>" alt="<%=controller.getMessage("ALT_TWIST_CLOSED")%>" class="twist"></a></td>
    <td height=20 valign="bottom" align="left" nowrap class="labels">
      <strong><%=wsdlPerspective.getMessage("FORM_LABEL_SOAP_RESPONSE_ENVELOPE")%></strong>
    </td>
    <td nowrap width="90%">&nbsp;</td>
  </tr>
</table>
<div id="<%=soapResponseTableContainerId%>" style="display:none">
  <table width="95%" height="50%" border=0 cellpadding=0 cellpadding=0>
    <tr>
      <td>
      	<!-- the onload call is to fix bug 140607 in bugzilla -->
        <iframe onload="javascript:requestEnvelopeFrame.document.location.reload()" frameborder=0 src="<%=response.encodeURL(controller.getPathWithContext(wsdlPerspective.getSOAPEnvelopeXMLLink(WSDLActionInputs.SOAP_ENVELOPE_TYPE_RESPONSE)))%>" width="95%" height="100%"></iframe>
      </td>
    </tr>
  </table>
</div>
<script language="javascript">
  twist("<%=soapResponseTableContainerId%>","<%=soapResponseTwistImageName.toString()%>");
</script>
<%
}
%>
