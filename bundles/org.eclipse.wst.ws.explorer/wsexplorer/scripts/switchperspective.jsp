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

<%@ page contentType="text/html; charset=UTF-8" %>

<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>

<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<jsp:include page="/scripts/panes.jsp" flush="true"/>
</head>
<body>
<script language="javascript">
  perspectiveContent.location = "<%=response.encodeURL(controller.getPathWithContext(controller.getCurrentPerspective().getPerspectiveContentPage()))%>";
</script>
</body>
</html>
