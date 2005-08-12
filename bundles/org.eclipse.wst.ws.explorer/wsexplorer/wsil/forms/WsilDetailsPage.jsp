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
<%@ page contentType="text/html; charset=UTF-8" import="org.eclipse.wst.ws.internal.explorer.platform.perspective.*,
                                                                                          org.eclipse.wst.ws.internal.explorer.platform.constants.*,
                                                                                          org.eclipse.wst.ws.internal.explorer.platform.datamodel.TreeElement,
                                                                                          org.eclipse.wst.ws.internal.explorer.platform.wsil.constants.*,
                                                                                          org.eclipse.wst.ws.internal.explorer.platform.wsil.perspective.WSILPerspective,
                                                                                          org.eclipse.wst.ws.internal.explorer.platform.wsil.datamodel.WsilElement,
                                                                                          org.eclipse.wst.ws.internal.explorer.platform.wsil.actions.AddToFavoritesAction,
                                                                                          org.eclipse.wst.ws.internal.explorer.platform.wsil.actions.RefreshAction,
                                                                                          java.util.Enumeration" %>

<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<jsp:useBean id="abstractLangs" class="java.util.Vector" scope="request"/>
<jsp:useBean id="abstracts" class="java.util.Vector" scope="request"/>
<%
    WSILPerspective wsilPerspective =controller.getWSILPerspective();
    NodeManager nodeManager = wsilPerspective.getNodeManager();
    Node selectedNode = nodeManager.getSelectedNode();
    ToolManager currentToolManager = selectedNode.getCurrentToolManager();
    TreeElement selectedElement = selectedNode.getTreeElement();
    WsilElement wsilElement = null;
    if (selectedElement instanceof WsilElement) {
        wsilElement = (WsilElement)selectedElement;
        abstracts.addAll(wsilElement.getWSILAbstracts());
        abstractLangs.addAll(wsilElement.getWSILAbstractLangs());
    }
%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title><%=wsilPerspective.getMessage("FORM_TITLE_WSIL_DETAILS")%></title>
    <link rel="stylesheet" type="text/css" href="<%=response.encodeURL(controller.getPathWithContext("css/windows.css"))%>">
    <jsp:include page="/wsil/scripts/wsilFormSubmit.jsp" flush="true"/>
</head>
<body dir="<%=org.eclipse.wst.ws.internal.explorer.platform.util.DirUtils.getDir()%>" class="contentbodymargin">
<div id="contentborder">
    <form action="<%=response.encodeURL(controller.getPathWithContext(AddToFavoritesAction.getBaseActionLink()))%>" method="post" target="<%=FrameNames.PERSPECTIVE_WORKAREA%>">

    <%
    String titleImagePath = "images/details_highlighted.gif";
    String title = wsilPerspective.getMessage("ALT_WSIL_DETAILS");
    %>
    <%@ include file = "/forms/formheader.inc" %>
    <%
    if (selectedElement instanceof WsilElement) {
    %>
        <input type="hidden" name="<%=ActionInputs.NODEID%>" value="<%=selectedNode.getNodeId()%>">
        <input type="hidden" name="<%=ActionInputs.VIEWID%>" value="<%=ActionInputs.VIEWID_DEFAULT%>">

        <table width="95%" border=0 cellpadding=3 cellspacing=0 class="tableborder">
            <tr>
                <th class="singleheadercolor" height=20 valign="bottom" align="left">
                    <strong><%=wsilPerspective.getMessage("FORM_LABEL_URL")%></strong>
                </th>
            </tr>
            <tr>
                <td class="tablecells">
                    <%=wsilElement.getWsilUrl()%>
                </td>
            </tr>
        </table>

        <jsp:include page="/wsil/scripts/wsilAbstractTable.jsp" flush="true"/>

<jsp:useBean id="currentToolManagerHash" class="java.util.Hashtable" scope="request">
<%
  currentToolManagerHash.put(ActionInputs.CURRENT_TOOL_MANAGER,currentToolManager);
%>
</jsp:useBean>
<jsp:include page="/forms/otherActions.jsp" flush="true"/>

    <%
    }
    %>
</div>
</body>
</html>
