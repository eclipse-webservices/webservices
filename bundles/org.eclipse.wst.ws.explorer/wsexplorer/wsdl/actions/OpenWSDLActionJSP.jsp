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
<%@ page contentType="text/html; charset=UTF-8" import="org.eclipse.wst.ws.internal.explorer.platform.wsdl.constants.WSDLFrameNames,
                                                        org.eclipse.wst.ws.internal.explorer.platform.constants.FrameNames,
                                                        org.eclipse.wst.ws.internal.explorer.platform.wsdl.actions.OpenWSDLAction"%>

<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<jsp:include page="/wsdl/scripts/wsdlpanes.jsp" flush="true"/>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <script language="javascript" src="<%=response.encodeURL(controller.getPathWithContext("scripts/browserdetect.js"))%>">
  </script>
</head>
<body>
<%
  // Prepare the action.
  OpenWSDLAction action = new OpenWSDLAction(controller);

  // Load the parameters for the action from the servlet request.
  boolean inputsValid = action.populatePropertyTable(request);
  if (!inputsValid) {
%>
  <script language="javascript">
    wsdlPropertiesContent.location = "<%=response.encodeURL(controller.getPathWithContext("wsdl/wsdl_properties_content.jsp"))%>";
    wsdlStatusContent.location = "<%=response.encodeURL(controller.getPathWithContext("wsdl/wsdl_status_content.jsp"))%>";
  </script>
<%
  }
  else {
    // Run the action and obtain the return code (fail/success).
    boolean actionResult = action.execute();
%>
    <script language="javascript">
    if (isMicrosoftInternetExplorer()) {
      wsdlNavigatorContent.location="<%=response.encodeURL(controller.getPathWithContext("wsdl/wsdl_navigator_content.jsp"))%>";
      wsdlPropertiesContainer.location = "<%=response.encodeURL(controller.getPathWithContext("wsdl/wsdl_properties_container.jsp"))%>";
      wsdlStatusContent.location = "<%=response.encodeURL(controller.getPathWithContext("wsdl/wsdl_status_content.jsp"))%>";
    }
    else {
      perspectiveContent.location = "<%=response.encodeURL(controller.getPathWithContext("wsdl/wsdl_perspective_content.jsp"))%>";
    }
    </script>
<%
  }
%>
</body>
</html>
