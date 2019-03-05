<%
/*******************************************************************************
 * Copyright (c) 2002, 2004 IBM Corporation and others.
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
<%@ page contentType="text/html; charset=UTF-8" import="org.eclipse.wst.ws.internal.explorer.platform.constants.FrameNames,
                                                                                          org.eclipse.wst.ws.internal.explorer.platform.perspective.Node,
                                                                                          org.eclipse.wst.ws.internal.explorer.platform.wsdl.constants.WSDLFrameNames,
                                   					                    org.eclipse.wst.ws.internal.explorer.platform.wsdl.perspective.WSDLPerspective,
                                   					                    org.eclipse.wst.ws.internal.explorer.platform.wsdl.perspective.WSDLDetailsTool"%>

<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<jsp:include page="/wsdl/scripts/wsdlpanes.jsp" flush="true"/>

<%
WSDLPerspective wsdlPerspective = controller.getWSDLPerspective();
Node wsdlNode = wsdlPerspective.getNodeManager().getSelectedNode();
WSDLDetailsTool wsdlDetailsTool = (WSDLDetailsTool)wsdlNode.getToolManager().getSelectedTool();
wsdlDetailsTool.toggleViewId();
%>
<script language="javascript">
  wsdlPropertiesContent.location = "<%=response.encodeURL(controller.getPathWithContext("wsdl/wsdl_properties_content.jsp"))%>";
</script>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 3.2 Final//EN">
<html lang="<%=response.getLocale().getLanguage()%>">
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body dir="<%=org.eclipse.wst.ws.internal.explorer.platform.util.DirUtils.getDir()%>">
</body>
</html>
