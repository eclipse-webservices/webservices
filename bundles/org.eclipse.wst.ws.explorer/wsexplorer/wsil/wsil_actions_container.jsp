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
<%@ page contentType="text/html; charset=UTF-8" import="org.eclipse.wst.ws.internal.explorer.platform.wsil.perspective.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.wsil.constants.*" %>

<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<%
   WSILPerspective wsilPerspective = controller.getWSILPerspective();
%>   
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title><%=wsilPerspective.getMessage("FRAME_TITLE_ACTIONS_CONTAINER")%></title>
</head>
<frameset rows="<%=wsilPerspective.getActionsContainerFramesetRows()%>" bgcolor="#ECE9D8">
  <frame name="<%=WsilFrameNames.WSIL_PROPERTIES_CONTAINER%>" title="<%=wsilPerspective.getMessage("FRAME_TITLE_PROPERTIES_CONTAINER")%>" src="<%=response.encodeURL("wsil_properties_container.jsp")%>" scrolling="no" frameborder=1 marginwidth=0 marginheight=0>
  <frame name="<%=WsilFrameNames.WSIL_STATUS_CONTAINER%>" title="<%=wsilPerspective.getMessage("FRAME_TITLE_STATUS_CONTAINER")%>" src="<%=response.encodeURL("wsil_status_container.jsp")%>" scrolling="no" frameborder=1 marginwidth=0 marginheight=0>
</frameset>
</html>
