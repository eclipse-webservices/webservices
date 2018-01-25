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
                                                                                          org.eclipse.wst.ws.internal.explorer.platform.favorites.constants.*,
                                                                                          org.eclipse.wst.ws.internal.explorer.platform.favorites.perspective.FavoritesPerspective,
                                                                                          org.eclipse.wst.ws.internal.explorer.platform.favorites.datamodel.*,
                                                                                          org.apache.wsil.extension.uddi.*" %>

<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<%
        FavoritesPerspective favPerspective = controller.getFavoritesPerspective();
        NodeManager nodeManager = favPerspective.getNodeManager();
        Node selectedNode = nodeManager.getSelectedNode();
        ToolManager currentToolManager = selectedNode.getCurrentToolManager();
        TreeElement selectedElement = selectedNode.getTreeElement();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 3.2 Final//EN">
<html lang="<%=response.getLocale().getLanguage()%>">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title><%=favPerspective.getMessage("FORM_TITLE_UDDI_SERVICE_DETAILS")%></title>
    <link rel="stylesheet" type="text/css" href="<%=response.encodeURL(controller.getPathWithContext("css/windows.css"))%>">
</head>
<body dir="<%=org.eclipse.wst.ws.internal.explorer.platform.util.DirUtils.getDir()%>" class="contentbodymargin">
<div id="contentborder">

    <%
    String titleImagePath = "images/details_highlighted.gif";
    String title = favPerspective.getMessage("ALT_FAVORITES_UDDI_SERVICE_DETAILS");
    %>
    <%@ include file = "/forms/formheader.inc" %>
    <%
    if (selectedElement instanceof FavoritesUDDIServiceElement) {
        FavoritesUDDIServiceElement serElement = (FavoritesUDDIServiceElement)selectedElement;
    %>

        <table width="95%" border=0 cellpadding=3 cellspacing=0 class="tableborder">
            <tr>
                <th class="singleheadercolor" height=20 valign="bottom" align="left">
                    <%=favPerspective.getMessage("FORM_LABEL_SERVICE_NAME")%>
                </th>
            </tr>
            <tr>
                <td class="tablecells">
                    <%=serElement.getName()%>
                </td>
            </tr>
        </table>

        <br>
        <table width="95%" border=0 cellpadding=3 cellspacing=0 class="tableborder">
            <tr>
                <th class="singleheadercolor" height=20 valign="bottom" align="left">
                    <%=favPerspective.getMessage("FORM_LABEL_INQUIRY_URL")%>
                </th>
            </tr>
            <tr>
                <td class="tablecells">
                    <%=serElement.getInquiryURL()%>
                </td>
            </tr>
        </table>

        <br>
        <table width="95%" border=0 cellpadding=3 cellspacing=0 class="tableborder">
            <tr>
                <th class="singleheadercolor" height=20 valign="bottom" align="left">
                    <%=favPerspective.getMessage("FORM_LABEL_SERVICE_KEY")%>
                </th>
            </tr>
            <tr>
                <td class="tablecells">
                    <%=serElement.getServiceKey()%>
                </td>
            </tr>
        </table>
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
