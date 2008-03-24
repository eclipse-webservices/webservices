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
<%@ page contentType="text/html; charset=UTF-8" import="java.util.Vector,
                                                                                          java.util.Enumeration,
                                                                                          org.eclipse.wst.ws.internal.explorer.platform.util.QuickSort,
                                                                                          org.eclipse.wst.ws.internal.explorer.platform.perspective.*,
                                                                                          org.eclipse.wst.ws.internal.explorer.platform.constants.*,
                                                                                          org.eclipse.wst.ws.internal.explorer.platform.datamodel.*,
                                                                                          org.eclipse.wst.ws.internal.explorer.platform.wsil.actions.SelectWSILToolAction,
                                                                                          org.eclipse.wst.ws.internal.explorer.platform.wsil.actions.AddToFavoritesAction,
                                                                                          org.eclipse.wst.ws.internal.explorer.platform.wsil.actions.WsilAddToWSDLPerspectiveAction,
                                                                                          org.eclipse.wst.ws.internal.explorer.platform.wsil.constants.*,
                                                                                          org.eclipse.wst.ws.internal.explorer.platform.wsil.perspective.WSILPerspective,
                                                                                          org.eclipse.wst.ws.internal.explorer.platform.wsil.datamodel.*" %>

<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<%
    String divWSDLList = "divWSDLList";
    String xdivWSDLList = "xdivWSDLList";

    WSILPerspective wsilPerspective = controller.getWSILPerspective();
    NodeManager nodeManager = wsilPerspective.getNodeManager();
    Node selectedNode = nodeManager.getSelectedNode();
    TreeElement selectedElement = selectedNode.getTreeElement();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 3.2 Final//EN">
<html lang="<%=response.getLocale().getLanguage()%>">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title><%=wsilPerspective.getMessage("VIEW_TITLE_WSDL_SERVICES")%></title>
    <link rel="stylesheet" type="text/css" href="<%=response.encodeURL(controller.getPathWithContext("css/windows.css"))%>">
    <jsp:include page="/wsil/scripts/wsilTable.jsp" flush="true"/>
    <jsp:include page="/wsil/scripts/wsilFormSubmit.jsp" flush="true"/>
</head>
<body dir="<%=org.eclipse.wst.ws.internal.explorer.platform.util.DirUtils.getDir()%>" class="contentbodymargin">
<div id="contentborder">
    <%
    String titleImagePath = "wsil/images/list_WSDL_service_highlighted.gif";
    String title = wsilPerspective.getMessage("ALT_LIST_WSDL_SERVICES");
    %>
    <%@ include file = "/forms/formheader.inc" %>
    <table>
      <tr>
        <td class="labels">
          <%=wsilPerspective.getMessage("FORM_LABEL_LIST_WSDL_SERVICES_DESC")%>
        </td>
      </tr>
    </table>    
    <form action="<%=response.encodeURL(controller.getPathWithContext(AddToFavoritesAction.getBaseActionLink()))%>" method="post" target="<%=FrameNames.PERSPECTIVE_WORKAREA%>" style="margin-top:10px;">
    <%
    if (selectedElement instanceof WsilElement) {
    %>
        <input type="hidden" name="<%=ActionInputs.NODEID%>" value="<%=selectedNode.getNodeId()%>">
        <input type="hidden" name="<%=WsilActionInputs.MULTIPLE_LINK_ACTION%>" value="1">
        <table border=0 cellpadding=6 cellspacing=0>
            <tr>
                <td height=25 valign="bottom" align="left" nowrap width=11>
                    <a href="javascript:twist('<%=divWSDLList%>','<%=xdivWSDLList%>')"><img name="<%=xdivWSDLList%>" src="<%=response.encodeURL(controller.getPathWithContext("images/twistclosed.gif"))%>" alt="<%=controller.getMessage("ALT_TWIST_CLOSED")%>" class="twist"></a>
                </td>
                <td height=25 valign="bottom" align="left" nowrap class="labels">
                    <strong><%=wsilPerspective.getMessage("FORM_LABEL_WSDL_SERVICE")%></strong>
                </td>
            </tr>
        </table>

        <table width="95%" border=0 cellpadding=0 cellspacing=0>
            <tr>
                <td valign="top" height=10><img src="<%=response.encodeURL(controller.getPathWithContext("images/keyline.gif"))%>" alt="" height=2 width="100%"></td>
            </tr>
        </table>

        <div id="<%=divWSDLList%>">
        <table width="95%" cellpadding=3 cellspacing=0 class="tableborder">
            <tr>
                <th class="checkboxcells" width=10><input type="checkbox" onClick="wsilHandleCheckAllClick('<%=divWSDLList%>',this)" title="<%=controller.getMessage("FORM_CONTROL_TITLE_SELECT_ALL_CHECK_BOX")%>"></th>
                <th class="headercolor"><%=wsilPerspective.getMessage("FORM_LABEL_ITEM_NUMBER")%></th>
                <th class="headercolor"><%=wsilPerspective.getMessage("FORM_LABEL_URL")%></th>
            </tr>
            <%
                ListManager wsdlServices = ((WsilElement)selectedElement).getAllWSDLServices();
                Enumeration e = wsdlServices.getListElements();
                int itemNum = 1;
                Vector sortedVector = new Vector();
                while (e.hasMoreElements()) {
                	sortedVector.add(e.nextElement());
             	  }
                QuickSort.sort(sortedVector);
                for (int i = 0; i < sortedVector.size(); i++) {
                    ListElement le = (ListElement)sortedVector.elementAt(i);
                    WsilWsdlServiceElement wsdlService = (WsilWsdlServiceElement)le.getObject();
                    String wsdlURL = wsdlService.getWSDLServiceURL();
                    %>
                    <tr>
                        <td class="checkboxcells" width=10><input type="checkbox"  name="<%=ActionInputs.VIEWID%>" value="<%=le.getViewId()%>" onClick="validateCheckBoxInput(this, this.checked)" title="<%=controller.getMessage("FORM_CONTROL_TITLE_SELECT_ROW_CHECK_BOX")%>"></td>
                        <td class="tablecells" nowrap><a href="<%=response.encodeURL(controller.getPathWithContext(SelectWSILToolAction.getActionLink(selectedNode.getNodeId(), selectedNode.getToolManager().getSelectedToolId(), le.getViewId(), 0, false)))%>" target="<%=FrameNames.PERSPECTIVE_WORKAREA%>"><%=String.valueOf(itemNum)%></a></td>
                        <td class="tablecells" nowrap><%=wsdlURL%></td>
                    </tr>
                    <%
                    itemNum++;
                }
            %>
        </table>
        </div>
        <script language="javascript">
            twistInit('<%=divWSDLList%>','<%=xdivWSDLList%>');
        </script>

        <table border=0 cellpadding=2 cellspacing=0>
            <tr>
                <td height=40 valign="bottom">
                    <input type="button" value="<%=wsilPerspective.getMessage("FORM_LABEL_ADD_TO_WSDL_PERSPECTIVE")%>" onClick="setFormLocationAndSubmit('<%=divWSDLList%>', this.form, '<%=response.encodeURL(controller.getPathWithContext(WsilAddToWSDLPerspectiveAction.getBaseActionLink()))%>')" class="button">
                </td>
                <td height=40 valign="bottom">
                    <input type="button" value="<%=wsilPerspective.getMessage("FORM_LABEL_ADD_TO_FAVORITES")%>" onClick="setFormLocationAndSubmit('<%=divWSDLList%>', this.form, '<%=response.encodeURL(controller.getPathWithContext(AddToFavoritesAction.getBaseActionLink()))%>')" class="button">
                </td>
                <td nowrap width="90%">&nbsp;</td>
            </tr>
        </table>

    <%
    }
%>
</div>
</body>
</html>
