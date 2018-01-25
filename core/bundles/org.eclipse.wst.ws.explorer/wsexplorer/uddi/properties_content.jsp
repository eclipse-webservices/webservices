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
<%@ page contentType="text/html; charset=UTF-8" import="org.eclipse.wst.ws.internal.explorer.platform.uddi.perspective.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.constants.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.perspective.*" %>

<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<%
   UDDIPerspective uddiPerspective = controller.getUDDIPerspective();
%>   
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 3.2 Final//EN">
<html lang="<%=response.getLocale().getLanguage()%>">
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title><%=uddiPerspective.getMessage("FRAME_TITLE_PROPERTIES_CONTENT")%></title>
  <link rel="stylesheet" type="text/css" href="<%=response.encodeURL(controller.getPathWithContext("css/windows.css"))%>">
</head>
<body dir="<%=org.eclipse.wst.ws.internal.explorer.platform.util.DirUtils.getDir()%>" class="contentbodymargin">
<div id="contentborder">
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
     Tool selectedTool = toolManager.getSelectedTool();
     if (selectedTool != null && selectedTool.getToolType() != ToolTypes.ACTION)
     {
%>
<script language="javascript">
  location="<%=response.encodeURL(controller.getPathWithContext(selectedTool.getFormLink()))%>";
</script>
<%
     }
     else
     {
%>
<table>
  <tr>
    <td>
      <%=controller.getMessage("MSG_ERROR_NO_ACTION_SELECTED")%>
    </td>
  </tr>
</table>
<%
     }
   }
   else
   {
%>
<table>
  <tr>
    <td>
      <%=controller.getMessage("MSG_ERROR_NO_NODE_SELECTED")%>
    </td>
  </tr>
</table>
<%
   }
%>
</div>
</body>
</html>
