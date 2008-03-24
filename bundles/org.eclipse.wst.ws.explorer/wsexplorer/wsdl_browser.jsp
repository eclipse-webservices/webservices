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
                                                        org.eclipse.wst.ws.internal.explorer.platform.actions.*,
                                                        java.net.*" %>

<%
   String sessionId = request.getParameter(ActionInputs.SESSIONID);
   HttpSession currentSession = (HttpSession)application.getAttribute(sessionId);
   Controller controller = (Controller)currentSession.getAttribute("controller");
   String wsdlTypeString = request.getParameter(ActionInputs.WSDL_TYPE);
   int wsdlType = ActionInputs.WSDL_TYPE_SERVICE_INTERFACE;
   try
   {
     wsdlType = Integer.parseInt(wsdlTypeString);
   }
   catch (NumberFormatException e)
   {
   }
   controller.setWSDLType(wsdlType);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 3.2 Final//EN">
<html lang="<%=response.getLocale().getLanguage()%>">
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title>
<%
   String wsdlSource;
   if (wsdlType == ActionInputs.WSDL_TYPE_SERVICE_INTERFACE)
     wsdlSource = controller.getMessage("WSDL_TYPE_SERVICE_INTERFACES");
   else
     wsdlSource = controller.getMessage("WSDL_TYPE_SERVICES");
     
   StringBuffer sessionIdParam = new StringBuffer(ActionInputs.SESSIONID);
   sessionIdParam.append('=').append(sessionId);
   
   StringBuffer toolbarLink = new StringBuffer("wsdl_toolbar.jsp?");
   toolbarLink.append(ActionInputs.SESSIONID).append('=').append(sessionId);
%>
    <%=controller.getMessage("TITLE_WSDL_BROWSER",wsdlSource)%>
  </title>
</head>
<frameset rows="33,*" border=0 bgcolor="#ECE9D8">
  <frame name="<%=FrameNames.WSDL_TOOLBAR%>" title="<%=controller.getMessage("FRAME_TITLE_WSDL_TOOLBAR")%>" src="<%=response.encodeURL(controller.getPathWithContext(toolbarLink.toString()))%>" noresize scrolling="no" frameborder=0 marginwidth=0 marginheight=0>
  <frame name="<%=FrameNames.WSDL_CONTENT%>" title="<%=controller.getMessage("FRAME_TITLE_WSDL_CONTENT")%>" src="<%=response.encodeURL(controller.getPathWithContext(ProxyLoadPageAction.getActionLink(sessionId,"wsdl_content.jsp")))%>" noresize scrolling="no" frameborder=0 marginwidth=0 marginheight=0>
</frameset>
</html>
