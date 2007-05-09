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
                                                        org.eclipse.wst.ws.internal.explorer.platform.wsdl.constants.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.wsdl.datamodel.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.perspective.*,
                                                        java.util.*" %>

<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<jsp:useBean id="sectionHeaderInfo" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.SectionHeaderInfo" scope="request"/>
<%
  WSDLPerspective wsdlPerspective = controller.getWSDLPerspective();
  Node serviceNode = wsdlPerspective.getNodeManager().getSelectedNode();
%>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title><%=wsdlPerspective.getMessage("FORM_TITLE_WSDL_SERVICE_DETAILS")%></title>
  <link rel="stylesheet" type="text/css" href="<%=response.encodeURL(controller.getPathWithContext("css/windows.css"))%>">
<jsp:include page="/scripts/tables.jsp" flush="true"/>
</head>
<body dir="<%=org.eclipse.wst.ws.internal.explorer.platform.util.DirUtils.getDir()%>" class="contentbodymargin">
<div id="contentborder">
<%
   String titleImagePath = "wsdl/images/open_wsdl_highlighted.gif";
   String title = wsdlPerspective.getMessage("ALT_WSDL_SERVICE_DETAILS");
%>
<%@ include file = "/forms/formheader.inc" %>
<table>
  <tr>
    <td class="labels"><%=wsdlPerspective.getMessage("FORM_LABEL_SERVICE_DETAILS_DESC")%></td>
  </tr>
</table>
<%
   WSDLServiceElement wsdlServiceElement = (WSDLServiceElement)serviceNode.getTreeElement();
   String documentation = wsdlServiceElement.getPropertyAsString(WSDLModelConstants.PROP_DOCUMENTATION);
   if (documentation.length() > 0)
   {
%>
<table>
  <tr>
    <td height=20 valign="bottom" class="labels"><%=documentation%></td>
  </tr>
  <tr>
    <td height=10>&nbsp;</td>
  </tr>
</table>
<%
   }
   Vector bindingNodes = serviceNode.getChildNodes();
   sectionHeaderInfo.clear();
   sectionHeaderInfo.setContainerId("Bindings");
   sectionHeaderInfo.setOtherProperties(bindingNodes);
%>
<jsp:include page="/wsdl/forms/bindings_table.jsp" flush="true"/>
</div>
<%
   if (bindingNodes.size() > 0)
   {
%>
<script language="javascript">
  twist("Bindings","xBindings");
</script>
<%
   }
%>
</body>
</html>
