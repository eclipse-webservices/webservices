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
<%@ page contentType="text/html; charset=UTF-8" import="org.eclipse.wst.ws.internal.explorer.platform.favorites.constants.*,
                                                                                          org.eclipse.wst.ws.internal.explorer.platform.constants.*,
                                                                                          org.eclipse.wst.ws.internal.explorer.platform.perspective.*,
                                   					                    org.eclipse.wst.ws.internal.explorer.platform.favorites.actions.RemoveFavoritesAction"%>

<jsp:include page="/favorites/scripts/favoritesPanes.jsp" flush="true"/>
<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 3.2 Final//EN">
<html lang="<%=response.getLocale().getLanguage()%>">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body dir="<%=org.eclipse.wst.ws.internal.explorer.platform.util.DirUtils.getDir()%>">
    <script language="javascript">
        if (confirm("<%=controller.getFavoritesPerspective().getMessage("MSG_CONFIRM_REMOVE_NODE", controller.getFavoritesPerspective().getNodeManager().getSelectedNode().getNodeName())%>")) {
            <%
            Node selectedNode = controller.getFavoritesPerspective().getNodeManager().getSelectedNode();
            %>
            perspectiveWorkArea.location = "<%=response.encodeURL(controller.getPathWithContext(RemoveFavoritesAction.getActionLink(selectedNode.getNodeId(), selectedNode.getToolManager().getSelectedToolId(), ActionInputs.VIEWID_DEFAULT, ActionInputs.VIEWTOOLID_DEFAULT)))%>";
        }
        else {
            favPropertiesContainer.location = "<%=response.encodeURL(controller.getPathWithContext("favorites/fav_properties_container.jsp"))%>";
            favStatusContent.location = "<%=response.encodeURL(controller.getPathWithContext("favorites/fav_status_content.jsp"))%>";
        }
    </script>
</body>
</html>
