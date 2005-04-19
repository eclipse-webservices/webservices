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
<%@ page contentType="text/html; charset=UTF-8" import="org.eclipse.wst.ws.internal.explorer.platform.wsdl.perspective.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.wsdl.constants.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.perspective.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.constants.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.actions.*,
                                                        java.net.*" %>

<%
   String sessionId = request.getParameter(ActionInputs.SESSIONID);
   HttpSession currentSession = (HttpSession)application.getAttribute(sessionId);
   Controller controller = (Controller)currentSession.getAttribute("controller");
   WSDLPerspective wsdlPerspective = controller.getWSDLPerspective();
   String nodeId = request.getParameter(ActionInputs.NODEID);
   String fragmentId = request.getParameter(WSDLActionInputs.FRAGMENT_ID);
%>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title>
    <%=wsdlPerspective.getMessage("TITLE_XSD_INFORMATION_DIALOG")%>
  </title>
</head>
<frameset rows="33,*" border=0 bgcolor="#ECE9D8">
<%
   StringBuffer link = new StringBuffer("wsdl/fragment/XSDInfo_toolbar.jsp?");
   link.append(ActionInputs.SESSIONID).append('=').append(sessionId);
   link.append('&').append(ActionInputs.NODEID).append('=').append(nodeId);
   link.append('&').append(WSDLActionInputs.FRAGMENT_ID).append('=').append(fragmentId);
%>
  <frame name="<%=WSDLFrameNames.XSD_INFO_TOOLBAR%>" title="<%=controller.getMessage("FRAME_TITLE_XSD_INFORMATION_TOOLBAR")%>" src="<%=response.encodeURL(controller.getPathWithContext(link.toString()))%>" noresize scrolling="no" frameborder=0 marginwidth=0 marginheight=0>
<%      
   link.setLength(0);
   link.append("wsdl/fragment/XSDInfo_content.jsp?");
   link.append(ActionInputs.SESSIONID).append('=').append(sessionId);
   link.append('&').append(ActionInputs.NODEID).append('=').append(nodeId);
   link.append('&').append(WSDLActionInputs.FRAGMENT_ID).append('=').append(fragmentId);
%>  
  <frame name="<%=WSDLFrameNames.XSD_INFO_CONTENT%>" title="<%=controller.getMessage("FRAME_TITLE_XSD_INFORMATION_CONTENT")%>" src="<%=response.encodeURL(controller.getPathWithContext(link.toString()))%>" noresize scrolling="no" frameborder=0 marginwidth=0 marginheight=0>
</frameset>
</html>
