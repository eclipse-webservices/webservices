<%
/*******************************************************************************
 * Copyright (c) 2000, 2004,2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
%>
<%@ page contentType="text/html; charset=UTF-8" import="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" %>
  <jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <link rel="stylesheet" type="text/css" href="<%=response.encodeURL("/wsexplorer/css/treeview.css")%>">
  <link rel="stylesheet" type="text/css" href="<%=response.encodeURL("/wsexplorer/css/windows.css")%>">
</head>
<body>
<center>
<%=controller.getMessage("MSG_ERROR_SESSION_TIMED_OUT")%>
<br>
<%=controller.getMessage("MSG_ERROR_RESTART_SESSION")%>
</center>
<br>
</body>
</html>   
