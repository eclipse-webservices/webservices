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
<%@ page contentType="text/html; charset=UTF-8" import="java.util.Enumeration,
                                                                                          java.util.Vector,
                                                                                          org.eclipse.wst.ws.internal.explorer.platform.util.QuickSort,
                                                                                          org.eclipse.wst.ws.internal.explorer.platform.perspective.*,
                                                                                          org.eclipse.wst.ws.internal.explorer.platform.constants.*,
                                                                                          org.eclipse.wst.ws.internal.explorer.platform.datamodel.*,
                                                                                          org.eclipse.wst.ws.internal.explorer.platform.favorites.constants.*,
                                                                                          org.eclipse.wst.ws.internal.explorer.platform.favorites.perspective.FavoritesPerspective,
                                                                                          org.eclipse.wst.ws.internal.explorer.platform.favorites.actions.*,
                                                                                          org.eclipse.wst.ws.internal.explorer.platform.favorites.datamodel.*" %>

<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<%
    String divFavoriteUDDIRegistryTable = "divFavoriteUDDIRegistryTable";
    String xdivFavoriteUDDIRegistryTable = "xdivFavoriteUDDIRegistryTable";

    FavoritesPerspective favoritesPerspective = controller.getFavoritesPerspective();
    NodeManager nodeManager = favoritesPerspective.getNodeManager();
    Node selectedNode = nodeManager.getSelectedNode();
    TreeElement selectedElement = selectedNode.getTreeElement();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 3.2 Final//EN">
<html lang="<%=response.getLocale().getLanguage()%>">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title><%=favoritesPerspective.getMessage("VIEW_TITLE_UDDI_REGISTRY_LIST")%></title>
    <link rel="stylesheet" type="text/css" href="<%=response.encodeURL(controller.getPathWithContext("css/windows.css"))%>">
    <jsp:include page="/favorites/scripts/favoritesFormSubmit.jsp" flush="true"/>
    <jsp:include page="/favorites/scripts/favoritesTable.jsp" flush="true"/>
</head>
<body dir="<%=org.eclipse.wst.ws.internal.explorer.platform.util.DirUtils.getDir()%>" class="contentbodymargin">
<div id="contentborder">
    <%
    String titleImagePath = "favorites/images/list_registry_highlighted.gif";
    String title = favoritesPerspective.getMessage("ALT_LIST_FAVORITE_UDDI_REGISTRY");
    %>
    <%@ include file = "/forms/formheader.inc" %>
    <table>
      <tr>
        <td class="labels">
          <%=favoritesPerspective.getMessage("FORM_LABEL_LIST_FAVORITE_UDDI_REGISTRY_DESC")%>
        </td>
      </tr>
    </table>    
    <form action="<%=response.encodeURL(controller.getPathWithContext(RemoveFavoritesAction.getBaseActionLink()))%>" method="post" target="<%=FrameNames.PERSPECTIVE_WORKAREA%>" style="margin-top:10px;">
    <%
    if (selectedElement instanceof FavoritesUDDIRegistryFolderElement) {
    %>
        <input type="hidden" name="<%=FavoritesActionInputs.MULTIPLE_LINK_ACTION%>" value="1">
        <table width="95%" border=0 cellpadding=6 cellspacing=0>
            <tr>
                <td height=20 valign="bottom" align="left" nowrap width=11>
                    <a href="javascript:twist('<%=divFavoriteUDDIRegistryTable%>','<%=xdivFavoriteUDDIRegistryTable%>')"><img name="<%=xdivFavoriteUDDIRegistryTable%>" src="<%=response.encodeURL(controller.getPathWithContext("images/twistclosed.gif"))%>" alt="<%=controller.getMessage("ALT_TWIST_CLOSED")%>" class="twist"></a>
                </td>
                <td height=20 valign="bottom" align="left" nowrap class="labels">
                    <strong><%=favoritesPerspective.getMessage("FORM_LABEL_FAVORITE_UDDI_REGISTRY")%></strong>
                </td>
            </tr>
        </table>

        <table width="95%" border=0 cellpadding=0 cellspacing=0>
            <tr>
                <td valign="top" height=10><img src="<%=response.encodeURL(controller.getPathWithContext("images/keyline.gif"))%>" alt="" height=2 width="100%"></td>
            </tr>
        </table>

        <div id="<%=divFavoriteUDDIRegistryTable%>">
        <table width="95%" cellpadding=3 cellspacing=0 class="tableborder">
            <tr>
                <th class="checkboxcells" width=10><input type="checkbox" onClick="favHandleCheckAllClick('<%=divFavoriteUDDIRegistryTable%>',this)" title="<%=controller.getMessage("FORM_CONTROL_TITLE_SELECT_ALL_CHECK_BOX")%>"></th>
                <th class="headercolor"><%=favoritesPerspective.getMessage("FORM_LABEL_NAME")%></th>
            </tr>
            <%
                Enumeration e = ((FavoritesUDDIRegistryFolderElement)selectedElement).getAllFavorites();
                Vector sortedVector = new Vector();
                while (e.hasMoreElements()) {
                	sortedVector.add(e.nextElement());
             	  }
                QuickSort.sort(sortedVector);
                e = sortedVector.elements();
                while (e.hasMoreElements()) {
                    FavoritesUDDIRegistryElement favUDDIRegistryElement = (FavoritesUDDIRegistryElement)e.nextElement();
                    %>
                    <tr>
                        <td class="checkboxcells" width=10><input type="checkbox"  name="<%=FavoritesActionInputs.MASS_ACTION_NODE_ID%>" value="<%=selectedNode.getChildNode(favUDDIRegistryElement).getNodeId()%>" onClick="validateCheckBoxInput(this, this.checked)" title="<%=controller.getMessage("FORM_CONTROL_TITLE_SELECT_ROW_CHECK_BOX")%>"></td>
                        <td class="tablecells" nowrap><a href="<%=response.encodeURL(controller.getPathWithContext(SelectFavoritesNodeAction.getActionLink(selectedNode.getChildNode(favUDDIRegistryElement).getNodeId(), false)))%>" target="<%=FrameNames.PERSPECTIVE_WORKAREA%>"><%=favUDDIRegistryElement.getName()%></a></td>
                    </tr>
                    <%
                }
            %>
        </table>
        </div>
        <script language="javascript">
            twistInit('<%=divFavoriteUDDIRegistryTable%>','<%=xdivFavoriteUDDIRegistryTable%>');
        </script>

        <table border=0 cellpadding=2 cellspacing=0>
            <tr>
                <td height=40 valign="bottom" nowrap>
                    <input type="button" value="<%=favoritesPerspective.getMessage("FORM_LABEL_IMPORT_TO_UDDI_PERSPECTIVE")%>" onClick="setFormLocationAndSubmit('<%=divFavoriteUDDIRegistryTable%>', this.form, '<%=response.encodeURL(controller.getPathWithContext(AddToUDDIPerspectiveAction.getBaseActionLink()))%>')" class="button">
                </td>
                <td height=40 valign="bottom" nowrap>
                    <input type="button" value="<%=favoritesPerspective.getMessage("FORM_LABEL_REMOVE")%>" onClick="setFormLocationAndSubmit('<%=divFavoriteUDDIRegistryTable%>', this.form, '<%=response.encodeURL(controller.getPathWithContext(RemoveFavoritesAction.getBaseActionLink()))%>')" class="button">
                </td>
                <td nowrap width="90%">&nbsp;</td>
            </tr>
        </table>

    <%
    }
%>
    </form>
</div>
</body>
</html>
