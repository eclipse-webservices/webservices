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
                                                        org.eclipse.wst.ws.internal.explorer.platform.perspective.Node,
                                                        org.eclipse.wst.ws.internal.explorer.platform.perspective.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.constants.*,
                                                        java.util.*" %>

<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<jsp:useBean id="sectionHeaderInfo" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.SectionHeaderInfo" scope="request"/>
<%
  WSDLPerspective wsdlPerspective = controller.getWSDLPerspective();
  Node wsdlNode = wsdlPerspective.getNodeManager().getSelectedNode();
  ToolManager currentToolManager = wsdlNode.getCurrentToolManager();
  WSDLDetailsTool wsdlDetailsTool = (WSDLDetailsTool)currentToolManager.getSelectedTool();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 3.2 Final//EN">
<html lang="<%=response.getLocale().getLanguage()%>">
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title><%=wsdlPerspective.getMessage("FORM_TITLE_WSDL_DETAILS")%></title>
  <link rel="stylesheet" type="text/css" href="<%=response.encodeURL(controller.getPathWithContext("css/windows.css"))%>">
<jsp:include page="/scripts/tables.jsp" flush="true"/>
<jsp:include page="/scripts/panes.jsp" flush="true"/>
<script language="javascript">
  function switchWSDLDetailsView() {
    perspectiveWorkArea.location = "<%=response.encodeURL(controller.getPathWithContext("wsdl/actions/SwitchWSDLDetailsViewsActionJSP.jsp"))%>";
  }
</script>
</head>
<body dir="<%=org.eclipse.wst.ws.internal.explorer.platform.util.DirUtils.getDir()%>" class="contentbodymargin">
<div id="contentborder">
<%
   String titleImagePath = "wsdl/images/open_wsdl_highlighted.gif";
   String title = wsdlPerspective.getMessage("ALT_WSDL_DETAILS");
%>
<table width="95%" border=0 cellpadding=3 cellspacing=0>
  <tr>
    <td>
      <img src="<%=response.encodeURL(controller.getPathWithContext(titleImagePath))%>" alt="">
      <strong><%=title%></strong>
    </td>
    <td width="*">&nbsp;</td>
    <td align="right" class="labels">
<%
   if (wsdlDetailsTool.getViewId() == WSDLDetailsTool.SOURCE_VIEW_ID)
   {     
%>
      <a href="javascript:switchWSDLDetailsView()" title="<%=wsdlPerspective.getMessage("ALT_SWITCH_TO_FORM_VIEW")%>"><%=wsdlPerspective.getMessage("FORM_LINK_FORM")%></a>
<%
   }
   else
   {
%>
      <a href="javascript:switchWSDLDetailsView()" title="<%=wsdlPerspective.getMessage("ALT_SWITCH_TO_SOURCE_VIEW")%>"><%=wsdlPerspective.getMessage("FORM_LINK_SOURCE")%></a>
<%
   }
%>   
    </td>
  </tr>
  <tr>
    <td height=20 colspan=3><img height=2 width="100%" align="top" src="<%=response.encodeURL(controller.getPathWithContext("images/keyline.gif"))%>" alt=""></td>
  </tr>
</table>
<%
  if (wsdlDetailsTool.getViewId() == WSDLDetailsTool.FORM_VIEW_ID)
  {
%>
    <jsp:include page="/wsdl/forms/WSDLDetailsFormView.jsp" flush="true"/>
<%
  }
  else
  {
%>
    <table width="95%" height="100%" border=0 cellpadding=0 cellpadding=0>
      <tr>
        <td>
          <iframe src="<%=response.encodeURL(controller.getPathWithContext("/wsdl/forms/WSDLDetailsSourceView.jsp"))%>" width="95%" height="100%"></iframe>
        </td>
      </tr>
    </table>
<%
  }
%>
<jsp:useBean id="currentToolManagerHash" class="java.util.Hashtable" scope="request">
<%
  currentToolManagerHash.put(ActionInputs.CURRENT_TOOL_MANAGER,currentToolManager);
%>
</jsp:useBean>
<jsp:include page="/forms/otherActions.jsp" flush="true"/>
</div>
</body>
</html>
