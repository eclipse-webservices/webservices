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
<%@ page contentType="text/html; charset=UTF-8" import="org.eclipse.wst.ws.internal.explorer.platform.uddi.constants.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.uddi.perspective.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.uddi.actions.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.perspective.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.constants.*" %>

<%
   String sessionId = request.getParameter(ActionInputs.SESSIONID);
   String categoryTModelKey = request.getParameter(UDDIActionInputs.CATEGORY_TMODEL_KEY);
   HttpSession currentSession = (HttpSession)application.getAttribute(sessionId);
   Controller controller = (Controller)currentSession.getAttribute("controller");
   UDDIPerspective uddiPerspective = controller.getUDDIPerspective();
   uddiPerspective.setCategoryTModelKey(categoryTModelKey);
   
   StringBuffer toolbarLink = new StringBuffer("uddi/category_toolbar.jsp?");
   toolbarLink.append(ActionInputs.SESSIONID).append('=').append(sessionId);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 3.2 Final//EN">
<html lang="<%=response.getLocale().getLanguage()%>">
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title><%=uddiPerspective.getMessage("TITLE_CATEGORY_BROWSER")%></title>
</head>
<frameset rows="0,33,*" border=0 bgcolor="#ECE9D8">
  <frame name="<%=UDDIFrameNames.CATEGORIES_WORKAREA%>" title="<%=uddiPerspective.getMessage("FRAME_TITLE_CATEGORIES_WORKAREA")%>" frameborder=0 noresize>
  <frame name="<%=UDDIFrameNames.CATEGORIES_TOOLBAR%>" title="<%=uddiPerspective.getMessage("FRAME_TITLE_CATEGORIES_TOOLBAR")%>" src="<%=response.encodeURL(controller.getPathWithContext(toolbarLink.toString()))%>" noresize scrolling="no" frameborder=0 marginwidth=0 marginheight=0>
  <frame name="<%=UDDIFrameNames.CATEGORIES_CONTENT%>" title="<%=uddiPerspective.getMessage("FRAME_TITLE_CATEGORIES_CONTENT")%>" src="<%=response.encodeURL(controller.getPathWithContext(OpenCategoryBrowserAction.getCategoryContentPage(sessionId,categoryTModelKey)))%>" noresize scrolling="no" frameborder=0 marginwidth=0 marginheight=0>
</frameset>
</html>
