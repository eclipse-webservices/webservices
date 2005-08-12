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
                                                        org.eclipse.wst.ws.internal.explorer.platform.wsdl.constants.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.constants.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.perspective.*,
                                                        java.util.*" %>

<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<jsp:useBean id="sectionHeaderInfo" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.SectionHeaderInfo" scope="request"/>
<%
  WSDLPerspective wsdlPerspective = controller.getWSDLPerspective();
  Node bindingNode = wsdlPerspective.getNodeManager().getSelectedNode();
%>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title><%=wsdlPerspective.getMessage("FORM_TITLE_WSDL_BINDING_DETAILS")%></title>
  <link rel="stylesheet" type="text/css" href="<%=response.encodeURL(controller.getPathWithContext("css/windows.css"))%>">
<script language="javascript">
  var rowCheckboxName = "rowCheckboxName";
</script>
<jsp:include page="/scripts/tables.jsp" flush="true"/>
<jsp:include page="/scripts/formsubmit.jsp" flush="true"/>
<jsp:include page="/scripts/formutils.jsp" flush="true"/>
<script language="javascript" src="<%=response.encodeURL(controller.getPathWithContext("scripts/browserdetect.js"))%>">
</script>
</head>
<body dir="<%=org.eclipse.wst.ws.internal.explorer.platform.util.DirUtils.getDir()%>" class="contentbodymargin">
<div id="contentborder">
<form action="<%=response.encodeURL(controller.getPathWithContext("wsdl/actions/UpdateWSDLBindingActionJSP.jsp"))%>" method="post" target="<%=FrameNames.PERSPECTIVE_WORKAREA%>" enctype="multipart/form-data" onSubmit="return handleSubmit(this)">
<%
   String titleImagePath = "wsdl/images/open_wsdl_highlighted.gif";
   String title = wsdlPerspective.getMessage("ALT_WSDL_BINDING_DETAILS");
   WSDLBindingElement wsdlBindingElement = (WSDLBindingElement)bindingNode.getTreeElement();
   Vector operationNodes = bindingNode.getChildNodes();
%>
<%@ include file = "/forms/formheader.inc" %>
<input type="hidden" name="<%=ActionInputs.NODEID%>" value="<%=bindingNode.getNodeId()%>">
<table>
  <tr>
    <td class="labels"><%=wsdlPerspective.getMessage("FORM_LABEL_BINDING_DETAILS_DESC",wsdlPerspective.getBindingTypeString(wsdlBindingElement.getBindingType()))%></td>
  </tr>
</table>
<%
   String documentation = wsdlBindingElement.getPropertyAsString(WSDLModelConstants.PROP_DOCUMENTATION);
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

   sectionHeaderInfo.clear();
   sectionHeaderInfo.setContainerId("Operations");
   sectionHeaderInfo.setOtherProperties(operationNodes);
%>
<jsp:include page="/wsdl/forms/operations_table.jsp" flush="true"/>
<%
   if (operationNodes.size() > 0)
   {
%>
<script language="javascript">
  twist("Operations","xOperations");
</script>
<%
   }

   sectionHeaderInfo.clear();
   sectionHeaderInfo.setContainerId("Endpoints");
   sectionHeaderInfo.setOtherProperties(wsdlBindingElement);
%>
<table>
  <tr>
    <td height=20>&nbsp;</td>
  </tr>
</table>
<jsp:include page="/wsdl/forms/endpoint_table.jsp" flush="true"/>
<%
  if (wsdlBindingElement.getEndPoints().length > 0)
  {
%>
<script language="javascript">
  twistOpen("Endpoints");
</script>
<%
  }
%>
<jsp:include page="/forms/simpleCommon_table.jsp" flush="true"/>
</form>
</div>
</body>
</html>
