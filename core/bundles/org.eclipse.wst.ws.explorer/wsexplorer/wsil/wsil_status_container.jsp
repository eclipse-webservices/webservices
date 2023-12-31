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
<%@ page contentType="text/html; charset=UTF-8" import="org.eclipse.wst.ws.internal.explorer.platform.wsil.perspective.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.wsil.constants.*" %>

<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<%
   WSILPerspective wsilPerspective = controller.getWSILPerspective();
%>   
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 3.2 Final//EN">
<html lang="<%=response.getLocale().getLanguage()%>">
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title><%=wsilPerspective.getMessage("FRAME_TITLE_STATUS_CONTAINER")%></title>
</head>
<frameset rows="33,*" border=0 bgcolor="#ECE9D8">
  <frame name="<%=WsilFrameNames.WSIL_STATUS_TOOLBAR%>" title="<%=wsilPerspective.getMessage("FRAME_TITLE_STATUS_TOOLBAR")%>" src="<%=response.encodeURL("wsil_status_toolbar.jsp")%>" noresize scrolling="no" frameborder=0 marginwidth=0 marginheight=0>
  <frame name="<%=WsilFrameNames.WSIL_STATUS_CONTENT%>" title="<%=wsilPerspective.getMessage("FRAME_TITLE_STATUS_CONTENT")%>" src="<%=response.encodeURL("wsil_status_content.jsp")%>" noresize scrolling="no" frameborder=0 marginwidth=0 marginheight=0>
</frameset>
</html>
