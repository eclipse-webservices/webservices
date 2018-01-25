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
<%@ page contentType="text/html; charset=UTF-8" import="org.eclipse.wst.ws.internal.explorer.platform.wsil.constants.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.wsil.actions.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.constants.*" %>

<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<jsp:include page="/wsil/scripts/wsilPanes.jsp" flush="true"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 3.2 Final//EN">
<html lang="<%=response.getLocale().getLanguage()%>">
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <script language="javascript" src="<%=response.encodeURL(controller.getPathWithContext("scripts/browserdetect.js"))%>">
  </script>
</head>
<body dir="<%=org.eclipse.wst.ws.internal.explorer.platform.util.DirUtils.getDir()%>">
<%
// Prepare the action.
OpenWSILAction action = new OpenWSILAction(controller);

// Load the parameters for the action from the servlet request.
boolean inputsValid = action.populatePropertyTable(request);
if (!inputsValid) {
%>
  <script language="javascript">
    wsilPropertiesContent.location = "<%=response.encodeURL(controller.getPathWithContext("wsil/wsil_properties_content.jsp"))%>";
    wsilStatusContent.location = "<%=response.encodeURL(controller.getPathWithContext("wsil/wsil_status_content.jsp"))%>";
  </script>
<%
}
else {
  // Run the action and obtain the return code (fail/success).
  boolean actionResult = action.execute();
  if (actionResult) {
%>
    <script language="javascript">
      if (isMicrosoftInternetExplorer()) {
        wsilNavigatorContent.location="<%=response.encodeURL(controller.getPathWithContext("wsil/wsil_navigator_content.jsp"))%>";
        wsilPropertiesContainer.location = "<%=response.encodeURL(controller.getPathWithContext("wsil/wsil_properties_container.jsp"))%>";
        wsilStatusContent.location = "<%=response.encodeURL(controller.getPathWithContext("wsil/wsil_status_content.jsp"))%>";
      }
      else
        perspectiveContent.location = "<%=response.encodeURL(controller.getPathWithContext("wsil/wsil_perspective_content.jsp"))%>";
    </script>
<%
  }
  else {
%>
    <script language="javascript">
      wsilPropertiesContent.location = "<%=response.encodeURL(controller.getPathWithContext("wsil/wsil_properties_content.jsp"))%>";
      wsilStatusContent.location = "<%=response.encodeURL(controller.getPathWithContext("wsil/wsil_status_content.jsp"))%>";
    </script>
<%
  }
}
%>
</body>
</html>
