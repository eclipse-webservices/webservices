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
<%@ page contentType="text/html; charset=UTF-8" import="org.eclipse.wst.ws.internal.explorer.platform.uddi.constants.*,
                                                                                          org.eclipse.wst.ws.internal.explorer.platform.constants.*,
                                   					                    org.eclipse.wst.ws.internal.explorer.platform.uddi.actions.AddToFavoritesAction,
                                   					                    org.eclipse.wst.ws.internal.explorer.platform.util.*,
                                   					                    java.util.*"%>


<jsp:include page="/uddi/scripts/uddipanes.jsp" flush="true"/>
<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 3.2 Final//EN">
<html lang="<%=response.getLocale().getLanguage()%>">
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body dir="<%=org.eclipse.wst.ws.internal.explorer.platform.util.DirUtils.getDir()%>">
<form action="<%=response.encodeURL(controller.getPathWithContext("uddi/actions/AddToFavoritesActionJSP.jsp"))%>" method="post" target="<%=FrameNames.PERSPECTIVE_WORKAREA%>">
<%
Enumeration paramNames = request.getParameterNames();
while (paramNames.hasMoreElements()) {
  String paramName = (String)paramNames.nextElement();
  String[] paramValues = request.getParameterValues(paramName);
  for (int i = 0; i < paramValues.length; i++) {
%>
    <input type="hidden" name="<%=paramName%>" value="<%=paramValues[i]%>">
<%
  }
}
%>
</form>
</body>
</html>

<%
// Prepare the action.
AddToFavoritesAction action = new AddToFavoritesAction(controller);
// Load the parameters for the action from the servlet request.
action.populatePropertyTable(request);
if (action.favoriteExists()) {
%>
  <script language="javascript">
    if (confirm("<%=HTMLUtils.JSMangle(controller.getUDDIPerspective().getMessage("MSG_QUESTION_OVERWRITE_FAVORITES"))%>"))
      document.forms[0].submit();
  </script>
<%
}
else {
%>
  <script language="javascript">
    document.forms[0].submit();
  </script>
<%
}
%>
