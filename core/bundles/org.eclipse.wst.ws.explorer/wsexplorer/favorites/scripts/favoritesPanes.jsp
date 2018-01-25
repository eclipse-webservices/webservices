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
<%@ page import="org.eclipse.wst.ws.internal.explorer.platform.favorites.constants.*,
                 org.eclipse.wst.ws.internal.explorer.platform.constants.*" %>

<jsp:include page="/scripts/panes.jsp" flush="true"/>
<script language="javascript">
  var favNavigatorContainer = perspectiveContent.frames["<%=FavoritesFrameNames.NAVIGATOR_CONTAINER%>"];
  var favNavigatorToolbar = favNavigatorContainer.frames["<%=FavoritesFrameNames.NAVIGATOR_TOOLBAR%>"];
  var favNavigatorContent = favNavigatorContainer.frames["<%=FavoritesFrameNames.NAVIGATOR_CONTENT%>"];
  var favActionsContainer = perspectiveContent.frames["<%=FavoritesFrameNames.ACTIONS_CONTAINER%>"];
  var favPropertiesContainer = favActionsContainer.frames["<%=FavoritesFrameNames.PROPERTIES_CONTAINER%>"];
  var favPropertiesToolbar = favPropertiesContainer.frames["<%=FavoritesFrameNames.PROPERTIES_TOOLBAR%>"];
  var favPropertiesContent = favPropertiesContainer.frames["<%=FavoritesFrameNames.PROPERTIES_CONTENT%>"];
  var favStatusContainer = favActionsContainer.frames["<%=FavoritesFrameNames.STATUS_CONTAINER%>"];
  var favStatusToolbar = favStatusContainer.frames["<%=FavoritesFrameNames.STATUS_TOOLBAR%>"];
  var favStatusContent = favStatusContainer.frames["<%=FavoritesFrameNames.STATUS_CONTENT%>"];
</script>
