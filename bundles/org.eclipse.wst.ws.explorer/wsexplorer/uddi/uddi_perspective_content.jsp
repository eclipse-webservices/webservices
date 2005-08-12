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
<%@ page contentType="text/html; charset=UTF-8" import="org.eclipse.wst.ws.internal.explorer.platform.uddi.perspective.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.uddi.constants.*" %>

<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<%
   UDDIPerspective uddiPerspective = controller.getUDDIPerspective();
%>   
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title><%=uddiPerspective.getMessage("FRAME_TITLE_UDDI_PERSPECTIVE_CONTENT")%></title>
</head>
<frameset cols="<%=uddiPerspective.getPerspectiveContentFramesetCols()%>" bgcolor=#ECE9D8>
  <%
  if (org.eclipse.wst.ws.internal.explorer.platform.util.DirUtils.isRTL())
  {
  %>
  <frame name="<%=UDDIFrameNames.ACTIONS_CONTAINER%>" title="<%=uddiPerspective.getMessage("FRAME_TITLE_ACTIONS_CONTAINER")%>" src="<%=response.encodeURL(controller.getPathWithContext("uddi/actions_container.jsp"))%>" marginwidth=0 marginheight=0 scrolling="no" frameborder=1>
  <frame name="<%=UDDIFrameNames.NAVIGATOR_CONTAINER%>" title="<%=uddiPerspective.getMessage("FRAME_TITLE_NAVIGATOR_CONTAINER")%>" src="<%=response.encodeURL(controller.getPathWithContext("uddi/navigator_container.jsp"))%>" marginwidth=0 marginheight=0 scrolling="no" frameborder=1>
  <%
  }
  else
  {
  %>
  <frame name="<%=UDDIFrameNames.NAVIGATOR_CONTAINER%>" title="<%=uddiPerspective.getMessage("FRAME_TITLE_NAVIGATOR_CONTAINER")%>" src="<%=response.encodeURL(controller.getPathWithContext("uddi/navigator_container.jsp"))%>" marginwidth=0 marginheight=0 scrolling="no" frameborder=1>
  <frame name="<%=UDDIFrameNames.ACTIONS_CONTAINER%>" title="<%=uddiPerspective.getMessage("FRAME_TITLE_ACTIONS_CONTAINER")%>" src="<%=response.encodeURL(controller.getPathWithContext("uddi/actions_container.jsp"))%>" marginwidth=0 marginheight=0 scrolling="no" frameborder=1>
  <%
  }
  %>
</frameset>
