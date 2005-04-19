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
                                                        org.eclipse.wst.ws.internal.explorer.platform.constants.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.perspective.*" %>

<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<%
   WSDLPerspective wsdlPerspective = controller.getWSDLPerspective();
%>   
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title><%=wsdlPerspective.getMessage("FRAME_TITLE_PROPERTIES_CONTENT")%></title>
  <link rel="stylesheet" type="text/css" href="<%=response.encodeURL(controller.getPathWithContext("css/windows.css"))%>">
</head>
<body class="contentbodymargin">
<div id="contentborder">
<%
    NodeManager nodeManager = wsdlPerspective.getNodeManager();
    Node selectedNode = nodeManager.getSelectedNode();
    if (selectedNode != null)
    {
        ToolManager toolManager;
        if (selectedNode.getViewId() == ActionInputs.VIEWID_DEFAULT)
            toolManager = selectedNode.getToolManager();
        else
            toolManager = selectedNode.getViewToolManager();
        Tool selectedTool = toolManager.getSelectedTool();
        if (selectedTool != null && selectedTool.getToolType() != ToolTypes.ACTION) {
        %>
        <script language="javascript">
            location="<%=response.encodeURL(controller.getPathWithContext(selectedTool.getFormLink()))%>";
        </script>
        <%
        }
        else {
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
    else {
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
