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
                                                        org.eclipse.wst.ws.internal.explorer.platform.wsdl.constants.*" %>

<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<%
   WSDLPerspective wsdlPerspective = controller.getWSDLPerspective();
%>   
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<frameset cols="<%=wsdlPerspective.getPerspectiveContentFramesetCols()%>" bgcolor="#ECE9D8">
  <%
  if (org.eclipse.wst.ws.internal.explorer.platform.util.DirUtils.isRTL())
  {
  %>
  <frame name="<%=WSDLFrameNames.WSDL_ACTIONS_CONTAINER%>" title="<%=wsdlPerspective.getMessage("FRAME_TITLE_ACTIONS_CONTAINER")%>" src="<%=response.encodeURL("wsdl_actions_container.jsp")%>" marginwidth=0 marginheight=0 scrolling="no" frameborder=1>
  <frame name="<%=WSDLFrameNames.WSDL_NAVIGATOR_CONTAINER%>" title="<%=wsdlPerspective.getMessage("FRAME_TITLE_NAVIGATOR_CONTAINER")%>" src="<%=response.encodeURL("wsdl_navigator_container.jsp")%>" marginwidth=0 marginheight=0 scrolling="no" frameborder=1>
  <%
  }
  else
  {
  %>
  <frame name="<%=WSDLFrameNames.WSDL_NAVIGATOR_CONTAINER%>" title="<%=wsdlPerspective.getMessage("FRAME_TITLE_NAVIGATOR_CONTAINER")%>" src="<%=response.encodeURL("wsdl_navigator_container.jsp")%>" marginwidth=0 marginheight=0 scrolling="no" frameborder=1>
  <frame name="<%=WSDLFrameNames.WSDL_ACTIONS_CONTAINER%>" title="<%=wsdlPerspective.getMessage("FRAME_TITLE_ACTIONS_CONTAINER")%>" src="<%=response.encodeURL("wsdl_actions_container.jsp")%>" marginwidth=0 marginheight=0 scrolling="no" frameborder=1>
  <%
  }
  %>
</frameset>
</html>
