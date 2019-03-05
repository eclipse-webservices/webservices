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
<%@ page contentType="text/html; charset=UTF-8" import="org.eclipse.wst.ws.internal.explorer.platform.wsdl.perspective.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.wsdl.constants.*" %>

<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<%
   WSDLPerspective wsdlPerspective = controller.getWSDLPerspective();
   String statusContentSrc = "wsdl_status_content.jsp";
   switch (wsdlPerspective.getStatusContentType())
   {
     case WSDLPerspective.STATUS_CONTENT_RESULT_FORM:
       statusContentSrc = "wsdl_result_content.jsp";
       break;
     case WSDLPerspective.STATUS_CONTENT_RESULT_SOURCE:
     	statusContentSrc = "wsdl_result_content.jsp";
     	break;
   }
%>   
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 3.2 Final//EN">
<html lang="<%=response.getLocale().getLanguage()%>">
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title><%=wsdlPerspective.getMessage("FRAME_TITLE_STATUS_CONTAINER")%></title>
</head>
<frameset rows="33,*" border=0 bgcolor="#ECE9D8">
  <frame name="<%=WSDLFrameNames.WSDL_STATUS_TOOLBAR%>" title="<%=wsdlPerspective.getMessage("FRAME_TITLE_STATUS_TOOLBAR")%>" src="<%=response.encodeURL("wsdl_status_toolbar.jsp")%>" noresize scrolling="no" frameborder=0 marginwidth=0 marginheight=0>
  <frame name="<%=WSDLFrameNames.WSDL_STATUS_CONTENT%>" title="<%=wsdlPerspective.getMessage("FRAME_TITLE_STATUS_CONTENT")%>" src="<%=response.encodeURL(statusContentSrc)%>" noresize scrolling="no" frameborder=0 marginwidth=0 marginheight=0>
</frameset>
</html>
