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
<%@ page contentType="text/html; charset=UTF-8" import="org.eclipse.wst.ws.internal.explorer.platform.uddi.constants.*" %>

<script language="javascript">
  var categoryBrowserWindow = top;
  var categoryBrowserWindowWorkArea = top.frames["<%=UDDIFrameNames.CATEGORIES_WORKAREA%>"];
  var categoryBrowserWindowToolbar = categoryBrowserWindow.frames["<%=UDDIFrameNames.CATEGORIES_TOOLBAR%>"];
  var categoryBrowserWindowContent = categoryBrowserWindow.frames["<%=UDDIFrameNames.CATEGORIES_CONTENT%>"];
</script>
