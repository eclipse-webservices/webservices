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
<%@ page contentType="text/html; charset=UTF-8" import="org.eclipse.wst.ws.internal.explorer.platform.favorites.perspective.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.favorites.constants.*" %>

<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<%
   FavoritesPerspective favPerspective = controller.getFavoritesPerspective();
%>   
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 3.2 Final//EN">
<html lang="<%=response.getLocale().getLanguage()%>">
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title><%=favPerspective.getMessage("FRAME_TITLE_PERSPECTIVE_CONTENT")%></title>
</head>
<frameset cols="<%=favPerspective.getPerspectiveContentFramesetCols()%>" bgcolor=#ECE9D8>
  <%
  if (org.eclipse.wst.ws.internal.explorer.platform.util.DirUtils.isRTL())
  {
  %>
  <frame name="<%=FavoritesFrameNames.ACTIONS_CONTAINER%>" title="<%=favPerspective.getMessage("FRAME_TITLE_ACTIONS_CONTAINER")%>" src="<%=response.encodeURL(controller.getPathWithContext("favorites/fav_actions_container.jsp"))%>" marginwidth=0 marginheight=0 scrolling="no" frameborder=1>
  <frame name="<%=FavoritesFrameNames.NAVIGATOR_CONTAINER%>" title="<%=favPerspective.getMessage("FRAME_TITLE_NAVIGATOR_CONTAINER")%>" src="<%=response.encodeURL(controller.getPathWithContext("favorites/fav_navigator_container.jsp"))%>" marginwidth=0 marginheight=0 scrolling="no" frameborder=1>
  <%
  }
  else
  {
  %>
  <frame name="<%=FavoritesFrameNames.NAVIGATOR_CONTAINER%>" title="<%=favPerspective.getMessage("FRAME_TITLE_NAVIGATOR_CONTAINER")%>" src="<%=response.encodeURL(controller.getPathWithContext("favorites/fav_navigator_container.jsp"))%>" marginwidth=0 marginheight=0 scrolling="no" frameborder=1>
  <frame name="<%=FavoritesFrameNames.ACTIONS_CONTAINER%>" title="<%=favPerspective.getMessage("FRAME_TITLE_ACTIONS_CONTAINER")%>" src="<%=response.encodeURL(controller.getPathWithContext("favorites/fav_actions_container.jsp"))%>" marginwidth=0 marginheight=0 scrolling="no" frameborder=1>
  <%
  }
  %>
</frameset>
