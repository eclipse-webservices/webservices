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
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 3.2 Final//EN">
<html lang="<%=response.getLocale().getLanguage()%>">
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title><%=uddiPerspective.getMessage("FRAME_TITLE_ACTIONS_CONTAINER")%></title>
</head>
<frameset rows="<%=uddiPerspective.getActionsContainerFramesetRows()%>" bgcolor="#ECE9D8">
  <frame name="<%=UDDIFrameNames.PROPERTIES_CONTAINER%>" title="<%=uddiPerspective.getMessage("FRAME_TITLE_PROPERTIES_CONTAINER")%>" src="<%=response.encodeURL(controller.getPathWithContext("uddi/properties_container.jsp"))%>" scrolling="no" frameborder=1 marginwidth=0 marginheight=0>
  <frame name="<%=UDDIFrameNames.STATUS_CONTAINER%>" title="<%=uddiPerspective.getMessage("FRAME_TITLE_STATUS_CONTAINER")%>" src="<%=response.encodeURL(controller.getPathWithContext("uddi/status_container.jsp"))%>" scrolling="no" frameborder=1 marginwidth=0 marginheight=0>
</frameset>
</html>
