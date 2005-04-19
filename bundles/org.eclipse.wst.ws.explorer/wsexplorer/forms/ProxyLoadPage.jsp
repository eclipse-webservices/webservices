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
<%@ page contentType="text/html; charset=UTF-8" import="org.eclipse.wst.ws.internal.explorer.platform.perspective.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.constants.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.util.URLUtils,
                                                        java.util.*,
                                                        java.net.*" %>

<%
   String sessionId = request.getParameter(ActionInputs.SESSIONID);
   Controller controller;
   StringBuffer loadContentLink = new StringBuffer("forms/ProxyLoadContent.jsp?");
   if (sessionId != null)
   {
     HttpSession currentSession = (HttpSession)application.getAttribute(sessionId);
     controller = (Controller)currentSession.getAttribute("controller");
     loadContentLink.append(ActionInputs.SESSIONID).append('=').append(sessionId).append('&');
   }
   else
     controller = (Controller)session.getAttribute("controller");
   loadContentLink.append(ActionInputs.TARGET_PAGE).append('=').append(URLUtils.encode(request.getParameter(ActionInputs.TARGET_PAGE)));
%>
<frameset id="proxyPage" rows="100%,*" border=0>
  <frame src="<%=controller.getPathWithContext(loadContentLink.toString())%>" title="<%=controller.getMessage("FORM_TITLE_PAGE_LOADER")%>" marginwidth=0 marginheight=0 scrolling="no" frameborder=0 noresize>
  <frame title="<%=controller.getMessage("FORM_TITLE_PAGE_CONTENT")%>" marginwidth=0 marginheight=0 scrolling="no" frameborder=0 noresize>
</frameset>
