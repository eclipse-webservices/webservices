<%
/*******************************************************************************
 * Copyright (c) 2001, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20070305   117034 makandre@ca.ibm.com - Andrew Mak, Web Services Explorer should support SOAP Headers
 *******************************************************************************/
%>
<%@ page contentType="text/html; charset=UTF-8" import="org.eclipse.wst.ws.internal.explorer.platform.wsdl.perspective.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.wsdl.datamodel.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.wsdl.xsd.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.constants.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.perspective.*,
                                                        org.eclipse.wst.wsdl.binding.soap.SOAPHeader,
                                                        javax.wsdl.*,
                                                        java.util.*" %>

<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<jsp:useBean id="fragID" class="java.lang.StringBuffer" scope="request"/>
<jsp:useBean id="nodeID" class="java.lang.StringBuffer" scope="request"/>

<%
WSDLPerspective wsdlPerspective = controller.getWSDLPerspective();
Node selectedNode = wsdlPerspective.getNodeManager().getSelectedNode();
nodeID.delete(0, nodeID.length());
nodeID.append(selectedNode.getNodeId());
InvokeWSDLOperationTool invokeWSDLOperationTool = (InvokeWSDLOperationTool)(selectedNode.getCurrentToolManager().getSelectedTool());
WSDLOperationElement operElement = (WSDLOperationElement)selectedNode.getTreeElement();

Iterator it = operElement.getSOAPHeaders().iterator();

if (it.hasNext()) {
	String headerDivId = "Header";
	String headerImgId = "xHeader";
    %>
	<table border=0 cellpadding=6 cellspacing=0>
	  <tr>
	    <td height=20 valign="bottom" align="left" nowrap width=11><a href="javascript:twist('<%=headerDivId%>','<%=headerImgId%>')"><img name="<%=headerImgId%>" src="<%=response.encodeURL(controller.getPathWithContext("images/twistopened.gif"))%>" alt="<%=controller.getMessage("ALT_TWIST_OPENED")%>" class="twist"></a></td>
	    <td height=20 valign="bottom" align="left" nowrap class="labels"><strong><%=wsdlPerspective.getMessage("FORM_LABEL_HEADER")%></strong></td>
	  </tr>
	</table>
	
	<table width="95%" border=0 cellpadding=0 cellspacing=0>
	  <tr>
	    <td valign="top" height=10><img src="<%=response.encodeURL(controller.getPathWithContext("images/keyline.gif"))%>" height=2 width="100%"></td>
	  </tr>
	</table>
	
	<div id="<%=headerDivId%>">
	<%
	while (it.hasNext()) {
	  SOAPHeader soapHeader = (SOAPHeader) it.next();
	  IXSDFragment frag = operElement.getHeaderFragment(soapHeader);	   
	  fragID.delete(0, fragID.length());
	  fragID.append(frag.getID());
	  %>
	  <jsp:include page="<%=frag.getWriteFragment()%>" flush="true"/>
	  <%  
	}
	%>
	</div>
    <%    
}

boolean hasInput = Boolean.parseBoolean(request.getParameter("hasInput"));
String bodyDivId = "Body";

if (hasInput) {
	String bodyImgId = "xBody";
    %>
	<table border=0 cellpadding=6 cellspacing=0>
	  <tr>
	    <td height=20 valign="bottom" align="left" nowrap width=11><a href="javascript:twist('<%=bodyDivId%>','<%=bodyImgId%>')"><img name="<%=bodyImgId%>" src="<%=response.encodeURL(controller.getPathWithContext("images/twistopened.gif"))%>" alt="<%=controller.getMessage("ALT_TWIST_OPENED")%>" class="twist"></a></td>
	    <td height=20 valign="bottom" align="left" nowrap class="labels"><strong><%=wsdlPerspective.getMessage("FORM_LABEL_BODY")%></strong></td>
	  </tr>
	</table> 
	
	<table width="95%" border=0 cellpadding=0 cellspacing=0>
	  <tr>
	    <td valign="top" height=10><img src="<%=response.encodeURL(controller.getPathWithContext("images/keyline.gif"))%>" height=2 width="100%"></td>
	  </tr>
	</table>
    <%
}
%>

<div id="<%=bodyDivId%>" class="fragarea">
<%
it = operElement.getOrderedBodyParts().iterator();
while (it.hasNext()) {
  Part part = (Part)it.next();
  IXSDFragment frag = operElement.getFragment(part);
  fragID.delete(0, fragID.length());
  fragID.append(frag.getID());
  %>
  <jsp:include page="<%=frag.getWriteFragment()%>" flush="true"/>
  <%
}
%>
</div>