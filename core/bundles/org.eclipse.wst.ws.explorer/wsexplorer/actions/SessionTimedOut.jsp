<%
/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 * 20100414   306773 mahutch@ca.ibm.com - Mark Hutchinson, make session time out configurable
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
<%=controller.getMessage("MSG_ERROR_SESSION_TIMED_OUT", Integer.toString(controller.getSessionTimeoutInMinutes()))%>
<br>
<%=controller.getMessage("MSG_ERROR_RESTART_SESSION")%>
</center>
<br>
</body>
</html>   
