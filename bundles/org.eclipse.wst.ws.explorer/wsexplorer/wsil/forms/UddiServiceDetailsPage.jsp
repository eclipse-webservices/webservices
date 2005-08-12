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
                                                                                          org.eclipse.wst.ws.internal.explorer.platform.wsil.constants.*,
                                                                                          org.eclipse.wst.ws.internal.explorer.platform.wsil.datamodel.*,
                                                                                          org.eclipse.wst.ws.internal.explorer.platform.wsil.actions.AddToFavoritesAction,
                                                                                          org.eclipse.wst.ws.internal.explorer.platform.wsil.actions.AddToUDDIPerspectiveAction,
                                                                                          org.eclipse.wst.ws.internal.explorer.platform.wsil.actions.RefreshAction,
                                                                                          org.eclipse.wst.ws.internal.explorer.platform.wsil.perspective.WSILPerspective" %>

<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<jsp:useBean id="nameLangs" class="java.util.Vector" scope="request"/>
<jsp:useBean id="names" class="java.util.Vector" scope="request"/>
<jsp:useBean id="abstractLangs" class="java.util.Vector" scope="request"/>
<jsp:useBean id="abstracts" class="java.util.Vector" scope="request"/>
<%
    WSILPerspective wsilPerspective = controller.getWSILPerspective();
    NodeManager nodeManager = wsilPerspective.getNodeManager();
    Node selectedNode = nodeManager.getSelectedNode();
    ToolManager currentToolManager = selectedNode.getCurrentToolManager();
    int viewID = selectedNode.getViewId();
    TreeElement selectedElement = selectedNode.getTreeElement();
    String inquiryAPI = null;
    String serviceKey = null;
    String discoveryURL = null;
    WsilServiceElement service = null;
    if (selectedElement instanceof WsilElement) {
        ListElement le = ((WsilElement)selectedElement).getAllUDDIServices().getElementWithViewId(viewID);
        service = (WsilServiceElement)le.getObject();
        if (service != null) {
            names.addAll(service.getServiceNames());
            nameLangs.addAll(service.getServiceNameLangs());
            abstracts.addAll(service.getServiceAbstracts());
            abstractLangs.addAll(service.getServiceAbstractLangs());
            inquiryAPI = ((WsilUddiServiceElement)service).getUDDIServiceInquiryAPI();
            serviceKey = ((WsilUddiServiceElement)service).getUDDIServiceKey();
            discoveryURL = ((WsilUddiServiceElement)service).getUDDIServiceDiscoveryURL();
        }
    }
%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title><%=wsilPerspective.getMessage("FORM_TITLE_UDDI_SERVICE_DETAILS")%></title>
    <link rel="stylesheet" type="text/css" href="<%=response.encodeURL(controller.getPathWithContext("css/windows.css"))%>">
    <jsp:include page="/wsil/scripts/wsilFormSubmit.jsp" flush="true"/>
</head>
<body dir="<%=org.eclipse.wst.ws.internal.explorer.platform.util.DirUtils.getDir()%>" class="contentbodymargin">
<div id="contentborder">
    <form action="<%=response.encodeURL(controller.getPathWithContext(AddToFavoritesAction.getBaseActionLink()))%>" method="post" target="<%=FrameNames.PERSPECTIVE_WORKAREA%>">

    <%
    String titleImagePath = "images/details_highlighted.gif";
    String title = wsilPerspective.getMessage("ALT_WSIL_SERVICE_DETAILS");
    %>
    <%@ include file = "/forms/formheader.inc" %>
    <%
    if (selectedElement instanceof WsilElement && service != null) {
    %>
        <input type="hidden" name="<%=ActionInputs.NODEID%>" value="<%=selectedNode.getNodeId()%>">
        <input type="hidden" name="<%=ActionInputs.VIEWID%>" value="<%=viewID%>">

        <table width="95%" border=0 cellpadding=3 cellspacing=0 class="tableborder">
            <tr>
                <th class="singleheadercolor" height=20 valign="bottom" align="left">
                    <strong><%=wsilPerspective.getMessage("FORM_LABEL_INQUIRY_API")%></strong>
                </th>
            </tr>
            <tr>
                <td class="tablecells">
                    <%=((inquiryAPI != null) ? inquiryAPI : wsilPerspective.getMessage("FORM_LABEL_NA"))%>
                </td>
            </tr>
        </table>

        <br>
        <table width="95%" border=0 cellpadding=3 cellspacing=0 class="tableborder">
            <tr>
                <th class="singleheadercolor" height=20 valign="bottom" align="left">
                    <strong><%=wsilPerspective.getMessage("FORM_LABEL_SERVICE_KEY")%></strong>
                </th>
            </tr>
            <tr>
                <td class="tablecells">
                    <%=((serviceKey != null) ? serviceKey : wsilPerspective.getMessage("FORM_LABEL_NA"))%>
                </td>
            </tr>
        </table>

        <br>
        <table width="95%" border=0 cellpadding=3 cellspacing=0 class="tableborder">
            <tr>
                <th class="singleheadercolor" height=20 valign="bottom" align="left">
                    <strong><%=wsilPerspective.getMessage("FORM_LABEL_DISCOVERY_URL")%></strong>
                </th>
            </tr>
            <tr>
                <td class="tablecells">
                    <%=((discoveryURL != null) ? discoveryURL : wsilPerspective.getMessage("FORM_LABEL_NA"))%>
                </td>
            </tr>
        </table>

        <jsp:include page="/wsil/scripts/wsilNameTable.jsp" flush="true"/>

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
