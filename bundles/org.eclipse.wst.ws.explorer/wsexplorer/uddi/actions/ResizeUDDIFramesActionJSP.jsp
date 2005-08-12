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
                                                        org.eclipse.wst.ws.internal.explorer.platform.uddi.actions.*" %>

<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<%
   // Create the action.
   ResizeUDDIFramesAction action = new ResizeUDDIFramesAction(controller);
   
   // Populate the action with the request properties.
   boolean result = action.populatePropertyTable(request);
%>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<jsp:include page="/uddi/scripts/uddiframesets.jsp" flush="true"/>
<script language="javascript" src="<%=response.encodeURL(controller.getPathWithContext("scripts/browserdetect.js"))%>">
</script>
</head>
<body dir="<%=org.eclipse.wst.ws.internal.explorer.platform.util.DirUtils.getDir()%>">
<%
   if (result)
   {
     action.execute(false);
     
     UDDIPerspective uddiPerspective = controller.getUDDIPerspective();
%>
<script language="javascript">
  if (isMicrosoftInternetExplorer())
  {
    var perspectiveContentFrameset = getPerspectiveContentFrameset();
    var actionsContainerFrameset = getActionsContainerFrameset();
    perspectiveContentFrameset.setAttribute("cols","<%=uddiPerspective.getPerspectiveContentFramesetCols()%>");
    actionsContainerFrameset.setAttribute("rows","<%=uddiPerspective.getActionsContainerFramesetRows()%>");
  }
  else
    perspectiveContent.location = "<%=response.encodeURL(controller.getPathWithContext("uddi/uddi_perspective_content.jsp"))%>";
</script>
<%
   }
%>
</body>
</html>
