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
<%@ page contentType="text/html; charset=UTF-8" import="org.eclipse.wst.ws.internal.explorer.platform.uddi.perspective.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.uddi.actions.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.uddi.constants.*" %>

<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script language="javascript" src="<%=response.encodeURL(controller.getPathWithContext("scripts/browserdetect.js"))%>">
</script>
<jsp:include page="/uddi/scripts/uddipanes.jsp" flush="true"/>
</head>
<body>
<%
   UpdateServiceAction action = (UpdateServiceAction)session.getAttribute(UDDIActionInputs.PUBLISH_ACTION);
   session.removeAttribute(UDDIActionInputs.PUBLISH_ACTION);

   RegPublishTool regPublishTool = action.setupRegPublishTool();
%>
<script language="javascript">
  perspectiveWorkArea.location = "<%=response.encodeURL(controller.getPathWithContext(regPublishTool.getSelectToolActionHref(false)))%>";
</script>
</body>
</html>
