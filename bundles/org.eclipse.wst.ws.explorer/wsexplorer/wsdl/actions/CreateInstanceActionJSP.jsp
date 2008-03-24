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
                                                        org.eclipse.wst.ws.internal.explorer.platform.wsdl.constants.FragmentConstants,
                                                        org.eclipse.wst.ws.internal.explorer.platform.wsdl.actions.CreateInstanceAction"%>

<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<jsp:include page="/wsdl/scripts/wsdlpanes.jsp" flush="true"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 3.2 Final//EN">
<html lang="<%=response.getLocale().getLanguage()%>">
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body dir="<%=org.eclipse.wst.ws.internal.explorer.platform.util.DirUtils.getDir()%>">
  <%
  CreateInstanceAction action = new CreateInstanceAction(controller);
  action.populatePropertyTable(request);
  action.execute();
  %>
  <script language="javascript">
    wsdlPropertiesContent.location = "<%=response.encodeURL(controller.getPathWithContext("wsdl/wsdl_properties_content.jsp"))%>";
  </script>
</body>
</html>
