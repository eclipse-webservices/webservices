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
<%@ page contentType="text/html; charset=UTF-8" import="org.eclipse.wst.ws.internal.explorer.platform.constants.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.perspective.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.actions.*" %>

<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<%
   // Prepare the action.
   RetrieveHistoryAction action = new RetrieveHistoryAction(controller);

   // Load the parameters for the action from the servlet request.
   action.populatePropertyTable(request);

   // Run the action and obtain the return code (fail/success).
   boolean actionResult = action.run();
%>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; UTF-8">
  <jsp:include page="/scripts/panes.jsp" flush="true"/>
</head>
<body dir="<%=org.eclipse.wst.ws.internal.explorer.platform.util.DirUtils.getDir()%>">
<script language="javascript">
<%
   BreadCrumb b = action.getBreadCrumb();
   if (b != null)
   {
     int breadCrumbPerspectiveId = b.getPerspectiveId();
     if (breadCrumbPerspectiveId != controller.getCurrentPerspective().getPerspectiveId())
     {
       // Switch the perspective.
%>
  perspectiveWorkArea.location = "<%=response.encodeURL(controller.getPathWithContext(ShowPerspectiveAction.getActionLink(breadCrumbPerspectiveId,true)))%>";
<%
     }
     else
     {
       // Jump to the page retrieved from history.
%>
  perspectiveWorkArea.location="<%=response.encodeURL(controller.getPathWithContext(b.getURL()))%>";
<%
     }
   }
%>
</script>
</body>
</html>
