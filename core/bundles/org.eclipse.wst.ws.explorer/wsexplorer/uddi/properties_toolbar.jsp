<%
/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
%>
<%@ page contentType="text/html; charset=UTF-8" import="org.eclipse.wst.ws.internal.explorer.platform.uddi.perspective.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.uddi.constants.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.perspective.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.constants.*" %>


<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<%
   UDDIPerspective uddiPerspective = controller.getUDDIPerspective();
%>   
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 3.2 Final//EN">
<html lang="<%=response.getLocale().getLanguage()%>">
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title><%=uddiPerspective.getMessage("FRAME_TITLE_PROPERTIES_TOOLBAR")%></title>
  <link rel="stylesheet" type="text/css" href="<%=response.encodeURL(controller.getPathWithContext("css/toolbar.css"))%>">
<jsp:include page="/uddi/scripts/uddiframesets.jsp" flush="true"/>
<script language="javascript" src="<%=response.encodeURL(controller.getPathWithContext("scripts/toolbar.js"))%>">
</script>
</head>
<body dir="<%=org.eclipse.wst.ws.internal.explorer.platform.util.DirUtils.getDir()%>" class="toolbarbodymargin">
<div id="toolbarborder">
  <div id="toolbar" ondblclick="processFramesetSizes(document.forms[0])">
<jsp:useBean id="formAction" class="java.lang.StringBuffer" scope="request">
<%
  formAction.append("uddi/actions/ResizeUDDIFramesActionJSP.jsp");
%>  
</jsp:useBean>
<jsp:useBean id="formFrameName" class="java.lang.StringBuffer" scope="request">
<%
   formFrameName.append(UDDIFrameNames.PROPERTIES_CONTAINER);
%>
</jsp:useBean>
<jsp:include page="/uddi/forms/ProcessUDDIFramesetsForm.jsp" flush="true"/>
    <table width="100%" height=25 cellpadding=0 cellspacing=0 border=0>
      <tr>
        <td valign="middle" align="center" width=25 height=25><img class="normal" src="<%=response.encodeURL(controller.getPathWithContext("images/actions.gif"))%>" alt="" width=16 height=16></td>
<%
   String doubleClickColumnTitle = null;
   if (uddiPerspective.getPerspectiveContentFramesetCols().endsWith("100%"))
   {
     if (uddiPerspective.getActionsContainerFramesetRows().startsWith("100%"))
       doubleClickColumnTitle = controller.getMessage("ALT_DOUBLE_CLICK_TO_RESTORE");
   }
   if (doubleClickColumnTitle == null)
     doubleClickColumnTitle = controller.getMessage("ALT_DOUBLE_CLICK_TO_MAXIMIZE");
%>        
        <td id="doubleclickcolumn" title="<%=doubleClickColumnTitle%>" valign="middle" width="*" height=25 nowrap class="text"><%=controller.getMessage("ALT_ACTIONS")%></td>
<%
   NodeManager navigatorManager = uddiPerspective.getNavigatorManager();
   Node selectedNavigatorNode = navigatorManager.getSelectedNode();
   if (selectedNavigatorNode != null)
   {
     ToolManager toolManager;
     if (selectedNavigatorNode.getViewId() == ActionInputs.VIEWID_DEFAULT)
       toolManager = selectedNavigatorNode.getToolManager();
     else
       toolManager = selectedNavigatorNode.getViewToolManager();
     for (int i=0;i<toolManager.getNumberOfTools();i++)
     {
       Tool tool = (Tool)toolManager.getTool(i);
%>
        <td valign="middle" align="center" width=25 height=25><%=tool.renderTool(response,controller)%></td>
<%
     }
   }
%>
      </tr>
    </table>
  </div>
</div>
</body>
</html>
