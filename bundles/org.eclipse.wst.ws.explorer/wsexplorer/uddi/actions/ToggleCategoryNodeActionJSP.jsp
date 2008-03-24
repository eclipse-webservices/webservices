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
                                                        org.eclipse.wst.ws.internal.explorer.platform.perspective.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.constants.*" %>

<%
   String sessionId = request.getParameter(ActionInputs.SESSIONID);
   HttpSession currentSession = (HttpSession)application.getAttribute(sessionId);
   Controller controller = (Controller)currentSession.getAttribute("controller");
%>   
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 3.2 Final//EN">
<html lang="<%=response.getLocale().getLanguage()%>">
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script language="javascript" src="<%=response.encodeURL(controller.getPathWithContext("scripts/browserdetect.js"))%>">
</script>
<jsp:include page="/uddi/scripts/cbpanes.jsp" flush="true"/>
<%
   // Prepare the action.
   ToggleCategoryNodeAction action = new ToggleCategoryNodeAction(controller);

   // Load the parameters for the action from the servlet request.
   action.populatePropertyTable(request);

   // Run the action and obtain the return code (fail/success).
   boolean actionResult = action.execute(false);

   String treeContentVar = action.getTreeContentVar();
   UDDIPerspective uddiPerspective = controller.getUDDIPerspective();
%>
<script language="javascript">
<%
   if (treeContentVar != null)
   {
%>
  if (isMicrosoftInternetExplorer())
    <%=treeContentVar%>.location = "<%=response.encodeURL(controller.getPathWithContext(action.getTreeContentPage()))%>";
  else
    categoryBrowserWindow.location = "<%=response.encodeURL(controller.getPathWithContext(action.getTreeContainerPage()))%>";
<%
   }
%>
</script>
</head>
<body dir="<%=org.eclipse.wst.ws.internal.explorer.platform.util.DirUtils.getDir()%>">
</body>
</html>
