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
<%@ page contentType="text/html; charset=UTF-8" import="org.eclipse.wst.ws.internal.explorer.platform.uddi.perspective.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.uddi.constants.*" %>

<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<%
   UDDIPerspective uddiPerspective = controller.getUDDIPerspective();
%>   
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 3.2 Final//EN">
<html lang="<%=response.getLocale().getLanguage()%>">
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title><%=uddiPerspective.getMessage("FRAME_TITLE_STATUS_CONTAINER")%></title>
</head>
<frameset rows="33,*" border=0 bgcolor="#ECE9D8">
  <frame name="<%=UDDIFrameNames.STATUS_TOOLBAR%>" title="<%=uddiPerspective.getMessage("FRAME_TITLE_STATUS_TOOLBAR")%>" src="<%=response.encodeURL(controller.getPathWithContext("uddi/status_toolbar.jsp"))%>" noresize scrolling="no" frameborder=0 marginwidth=0 marginheight=0>
  <frame name="<%=UDDIFrameNames.STATUS_CONTENT%>" title="<%=uddiPerspective.getMessage("FRAME_TITLE_STATUS_CONTENT")%>" src="<%=response.encodeURL(controller.getPathWithContext("uddi/status_content.jsp"))%>" noresize scrolling="no" frameborder=0 marginwidth=0 marginheight=0>
</frameset>
</html>
