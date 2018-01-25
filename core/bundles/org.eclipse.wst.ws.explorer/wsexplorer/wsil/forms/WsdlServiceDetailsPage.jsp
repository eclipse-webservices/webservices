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
                                                                                          org.eclipse.wst.ws.internal.explorer.platform.wsil.perspective.WSILPerspective,
                                                                                          org.eclipse.wst.ws.internal.explorer.platform.wsil.datamodel.*,
                                                                                          org.eclipse.wst.ws.internal.explorer.platform.wsil.actions.AddToFavoritesAction,
                                                                                          org.apache.wsil.QName,
                                                                                          java.util.Vector,
                                                                                          java.util.Enumeration" %>

<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<jsp:useBean id="nameLangs" class="java.util.Vector" scope="request"/>
<jsp:useBean id="names" class="java.util.Vector" scope="request"/>
<jsp:useBean id="abstractLangs" class="java.util.Vector" scope="request"/>
<jsp:useBean id="abstracts" class="java.util.Vector" scope="request"/>
<%
    String bindingTable = "bindingTable";
    String xbindingTable = "xbindingTable";

    WSILPerspective wsilPerspective =controller.getWSILPerspective();
    NodeManager nodeManager = wsilPerspective.getNodeManager();
    Node selectedNode = nodeManager.getSelectedNode();
    ToolManager currentToolManager = selectedNode.getCurrentToolManager();
    int viewID = selectedNode.getViewId();
    TreeElement selectedElement = selectedNode.getTreeElement();
    WsilServiceElement service = null;
    String wsdlURL = null;
    Vector wsdlBindings = new Vector();
    if (selectedElement instanceof WsilElement) {
        ListElement le = ((WsilElement)selectedElement).getAllWSDLServices().getElementWithViewId(viewID);
        service = (WsilServiceElement)le.getObject();
        if (service != null) {
            names.addAll(service.getServiceNames());
            nameLangs.addAll(service.getServiceNameLangs());
            abstracts.addAll(service.getServiceAbstracts());
            abstractLangs.addAll(service.getServiceAbstractLangs());
            wsdlURL = ((WsilWsdlServiceElement)service).getWSDLServiceURL();
            wsdlBindings = ((WsilWsdlServiceElement)service).getWSDLBinding();
        }
    }
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 3.2 Final//EN">
<html lang="<%=response.getLocale().getLanguage()%>">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title><%=wsilPerspective.getMessage("FORM_TITLE_WSDL_SERVICE_DETAILS")%></title>
    <link rel="stylesheet" type="text/css" href="<%=response.encodeURL(controller.getPathWithContext("css/windows.css"))%>">
    <jsp:include page="/wsil/scripts/wsilTable.jsp" flush="true"/>
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
                    <strong><%=wsilPerspective.getMessage("FORM_LABEL_URL")%></strong>
                </th>
            </tr>
            <tr>
                <td class="tablecells">
                    <%=wsdlURL%>
                </td>
            </tr>
        </table>

        <jsp:include page="/wsil/scripts/wsilNameTable.jsp" flush="true"/>

        <jsp:include page="/wsil/scripts/wsilAbstractTable.jsp" flush="true"/>

        <table border=0 cellpadding=6 cellspacing=0>
            <tr>
                <td height=40 valign="bottom" align="left" nowrap width=11>
                    <a href="javascript:twist('<%=bindingTable%>','<%=xbindingTable%>')"><img name="<%=xbindingTable%>" src="<%=response.encodeURL(controller.getPathWithContext("images/twistclosed.gif"))%>" alt="<%=controller.getMessage("ALT_TWIST_CLOSED")%>" class="twist"></a>
                </td>
                <td height=40 valign="bottom" align="left" nowrap class="labels">
                    <strong><%=wsilPerspective.getMessage("FORM_LABEL_WSDL_BINDINGS")%></strong>
                </td>
            </tr>
        </table>

        <table width="95%" border=0 cellpadding=0 cellspacing=0>
            <tr>
                <td valign="top" height=10><img src="<%=response.encodeURL(controller.getPathWithContext("images/keyline.gif"))%>" alt="" height=2 width="100%"></td>
            </tr>
        </table>

        <div id="<%=bindingTable%>">
        <table width="95%" cellpadding=3 cellspacing=0 class="tableborder">
            <tr>
                <th class="headercolor"><%=wsilPerspective.getMessage("FORM_LABEL_NAMESPACE_URI")%></th>
                <th class="headercolor"><%=wsilPerspective.getMessage("FORM_LABEL_LOCAL_NAME")%></th>
            </tr>
            <%
            Enumeration e = wsdlBindings.elements();
            while (e.hasMoreElements()) {
                QName qname = (QName)e.nextElement();
                %>
                 <tr>
                    <td class="tablecells" nowrap><%=qname.getNamespaceURI()%></td>
                    <td class="tablecells" nowrap><%=qname.getLocalName()%></td>
                 </tr>
                <%
            }
            %>
        </table>
        </div>
        <script language="javascript">
            twistInit('<%=bindingTable%>','<%=xbindingTable%>');
        </script>

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
