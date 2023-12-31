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
<%@ page contentType="text/html; charset=UTF-8" import="org.eclipse.wst.ws.internal.explorer.platform.wsdl.constants.WSDLFrameNames,
                                                        org.eclipse.wst.ws.internal.explorer.platform.constants.FrameNames,
                                                        org.eclipse.wst.ws.internal.explorer.platform.wsdl.actions.UpdateWSDLBindingAction"%>

<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<%
if (controller.getSessionId() == null) {
%>

 <script language="javascript">
	    var perspectiveContent = top.frames["<%=FrameNames.PERSPECTIVE_CONTENT%>"];
		perspectiveContent.location = "http://<%=request.getServerName()%>:<%=request.getServerPort()%><%=request.getContextPath()%>/actions/SessionTimedOut.jsp";
 </script>
<%
}
else {
%>
<jsp:include page="/wsdl/scripts/wsdlpanes.jsp" flush="true"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 3.2 Final//EN">
<html lang="<%=response.getLocale().getLanguage()%>">
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body dir="<%=org.eclipse.wst.ws.internal.explorer.platform.util.DirUtils.getDir()%>">
<%
  UpdateWSDLBindingAction action = new UpdateWSDLBindingAction(controller);
  action.populatePropertyTable(request);
  action.execute();
%>
  <script language="javascript">
    wsdlPropertiesContent.location = "<%=response.encodeURL(controller.getPathWithContext("wsdl/wsdl_properties_content.jsp"))%>";
    wsdlStatusContent.location = "<%=response.encodeURL(controller.getPathWithContext("wsdl/wsdl_status_content.jsp"))%>";
  </script>
</body>
</html>
<%
}
%>
