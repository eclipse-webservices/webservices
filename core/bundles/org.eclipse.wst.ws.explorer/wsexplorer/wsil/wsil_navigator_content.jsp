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
<%@ page contentType="text/html; charset=UTF-8" import="org.eclipse.wst.ws.internal.explorer.platform.wsil.perspective.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.wsil.constants.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.perspective.*" %>

<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<%
   WSILPerspective wsilPerspective = controller.getWSILPerspective();
%>      
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 3.2 Final//EN">
<html lang="<%=response.getLocale().getLanguage()%>">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title><%=wsilPerspective.getMessage("FRAME_TITLE_NAVIGATOR_CONTENT")%></title>
    <link rel="stylesheet" type="text/css" href="<%=response.encodeURL(controller.getPathWithContext("css/treeview.css"))%>">
    <link rel="stylesheet" type="text/css" href="<%=response.encodeURL(controller.getPathWithContext("css/windows.css"))%>">
    <script language="javascript" src="<%=response.encodeURL(controller.getPathWithContext("scripts/browserdetect.js"))%>">
    </script>
    <script language="javascript" src="<%=response.encodeURL(controller.getPathWithContext("scripts/treeview.js"))%>">
    </script>
</head>
<%
    String selectedAnchorName = "";
    NodeManager nodeManager = controller.getWSILPerspective().getNodeManager();
    int focusedNodeId = nodeManager.getFocusedNodeId();
    String focusedAnchorName = String.valueOf(focusedNodeId);
    Node selectedNode = nodeManager.getSelectedNode();
    if (selectedNode != null) {
        selectedAnchorName = selectedNode.getAnchorName();
        if (focusedNodeId == selectedNode.getNodeId())
            focusedAnchorName = selectedAnchorName;
    }
%>
<body dir="<%=org.eclipse.wst.ws.internal.explorer.platform.util.DirUtils.getDir()%>" onLoad="self.location.hash='#<%=focusedAnchorName%>';setSelectedAnchorName('<%=selectedAnchorName%>')" class="contentbodymargin">
    <div id="treecontentborder">
        <%=nodeManager.renderTreeView(response)%>
    </div>
</body>
</html>
