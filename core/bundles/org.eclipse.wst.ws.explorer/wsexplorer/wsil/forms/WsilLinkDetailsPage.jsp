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
                                                        org.eclipse.wst.ws.internal.explorer.platform.datamodel.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.wsil.datamodel.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.wsil.constants.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.wsil.datamodel.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.wsil.perspective.*" %>

<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<jsp:useBean id="abstractLangs" class="java.util.Vector" scope="request"/>
<jsp:useBean id="abstracts" class="java.util.Vector" scope="request"/>
<%
    WSILPerspective wsilPerspective =controller.getWSILPerspective();
    NodeManager nodeManager = wsilPerspective.getNodeManager();
    Node selectedNode = nodeManager.getSelectedNode();
    ToolManager currentToolManager = selectedNode.getCurrentToolManager();
    int viewID = selectedNode.getViewId();
    TreeElement selectedElement = selectedNode.getTreeElement();
    WsilWsilLinkElement link = null;
    if (selectedElement instanceof WsilElement) {
        ListElement le = ((WsilElement)selectedElement).getAllWSILLinks().getElementWithViewId(viewID);
        link = (WsilWsilLinkElement)le.getObject();
        if (link != null) {
            abstractLangs.addAll(link.getLinkAbstractLangs());
            abstracts.addAll(link.getLinkAbstracts());
        }
    }
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 3.2 Final//EN">
<html lang="<%=response.getLocale().getLanguage()%>">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title><%=wsilPerspective.getMessage("FORM_TITLE_WSIL_LINK_DETAILS")%></title>
    <link rel="stylesheet" type="text/css" href="<%=response.encodeURL(controller.getPathWithContext("css/windows.css"))%>">
</head>
<body dir="<%=org.eclipse.wst.ws.internal.explorer.platform.util.DirUtils.getDir()%>" class="contentbodymargin">
<div id="contentborder">
    <%
    String titleImagePath = "images/details_highlighted.gif";
    String title = wsilPerspective.getMessage("ALT_WSIL_LINK_DETAILS");
    %>
    <%@ include file = "/forms/formheader.inc" %>
    <%
    if (selectedElement instanceof WsilElement && link != null) {
    %>

        <table width="95%" border=0 cellpadding=3 cellspacing=0 class="tableborder">
            <tr>
                <th class="singleheadercolor" height=20 valign="bottom" align="left">
                    <%=wsilPerspective.getMessage("FORM_LABEL_URL")%>
                </th>
            </tr>
            <tr>
                <td class="tablecells">
                    <%=link.getWSILLinkLocation()%>
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
