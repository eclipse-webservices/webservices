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
<%@ page contentType="text/html; charset=UTF-8" import="org.eclipse.wst.ws.internal.explorer.platform.wsdl.perspective.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.wsdl.datamodel.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.wsdl.constants.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.constants.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.perspective.Node,
                                                        org.eclipse.wst.ws.internal.explorer.platform.util.*,
                                                        org.w3c.dom.Element,
                                                        javax.xml.rpc.NamespaceConstants,
                                                        java.util.Hashtable" %>

<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<jsp:useBean id="fragID" class="java.lang.StringBuffer" scope="request"/>
<jsp:useBean id="nodeID" class="java.lang.StringBuffer" scope="request"/>

<%
WSDLPerspective wsdlPerspective = controller.getWSDLPerspective();
Node operNode = wsdlPerspective.getNodeManager().getNode(Integer.parseInt(nodeID.toString()));
WSDLOperationElement operElement = (WSDLOperationElement)operNode.getTreeElement();
IXSDFragment frag = operElement.getFragmentByID(fragID.toString());
XSDToFragmentConfiguration xsdConfig = frag.getXSDToFragmentConfiguration();
Hashtable soapEnvelopeNamespaceTable = new Hashtable();
// TODO: Replace "SOAP-ENV" by NamespaceConstants.NSPREFIX_SOAP_ENVELOPE (="soapenv")
soapEnvelopeNamespaceTable.put(NamespaceConstants.NSURI_SOAP_ENVELOPE,"SOAP-ENV");
soapEnvelopeNamespaceTable.put(NamespaceConstants.NSURI_SCHEMA_XSI,NamespaceConstants.NSPREFIX_SCHEMA_XSI);
soapEnvelopeNamespaceTable.put(NamespaceConstants.NSURI_SCHEMA_XSD,NamespaceConstants.NSPREFIX_SCHEMA_XSD);
Element[] instanceDocuments = frag.genInstanceDocumentsFromParameterValues(!operElement.isUseLiteral(), soapEnvelopeNamespaceTable, XMLUtils.createNewDocument(null));
StringBuffer sb = new StringBuffer();
for (int i = 0; i < instanceDocuments.length; i++) {
  if (instanceDocuments[i] == null)
    continue;
  sb.append(XMLUtils.serialize(instanceDocuments[i], true));
  sb.append(HTMLUtils.LINE_SEPARATOR);
}
%>
<table width="95%" border=0 cellpadding=3 cellspacing=0>
  <tr>
    <td class="labels" height=25 valign="bottom" align="left" nowrap>
      <label for="<%=frag.getID()%>"><%=frag.getName()%></label>
    </td>
  </tr>
</table>
<table cellpadding=3 cellspacing=0 class="<%=(xsdConfig.getIsWSDLPart() ? "fixfragtable" : "innerfixfragtable")%>">
  <tr>
    <td>
      <textarea id="<%=frag.getID()%>" name="<%=frag.getID()%>" class="textareaenter" readonly><%=sb.toString()%></textarea>
<%
      /*
      StringBuffer url = new StringBuffer();
      url.append(response.encodeURL(controller.getPathWithContext("/wsdl/fragment/XSDDefaultRFragmentXML.jsp?")));
      url.append(WSDLActionInputs.FRAGMENT_ID).append("=").append(fragID).append("&");
      url.append(ActionInputs.NODEID).append("=").append(nodeID).append("&");
      url.append(ActionInputs.SESSIONID).append("=").append(session.getId());
      */
%>
      <!--
      <iframe src="" width="100%" height="150px"></iframe>
      -->
    </td>
  </tr>
</table>
