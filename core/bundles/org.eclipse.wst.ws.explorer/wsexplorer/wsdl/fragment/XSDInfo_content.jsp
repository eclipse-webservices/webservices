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
<%@ page contentType="text/html; charset=UTF-8" import="org.eclipse.wst.ws.internal.explorer.platform.wsdl.perspective.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.wsdl.datamodel.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.wsdl.constants.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.wsdl.datamodel.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.perspective.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.constants.*" %>

<jsp:useBean id="sessionID" class="java.lang.StringBuffer" scope="request"/>
<jsp:useBean id="fragID" class="java.lang.StringBuffer" scope="request"/>
<jsp:useBean id="nodeID" class="java.lang.StringBuffer" scope="request"/>
<%
   sessionID.append(request.getParameter(ActionInputs.SESSIONID));
   fragID.append(request.getParameter(WSDLActionInputs.FRAGMENT_ID));

   HttpSession currentSession = (HttpSession)application.getAttribute(sessionID.toString());
   Controller controller = (Controller)currentSession.getAttribute("controller");
   WSDLPerspective wsdlPerspective = controller.getWSDLPerspective();
   nodeID.append(request.getParameter(ActionInputs.NODEID));
   Node selectedNode = wsdlPerspective.getNodeManager().getNode(Integer.parseInt(nodeID.toString()));
   WSDLOperationElement operElement = (WSDLOperationElement)selectedNode.getTreeElement();
   IXSDFragment frag = operElement.getFragmentByID(fragID.toString());
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 3.2 Final//EN">
<html lang="<%=response.getLocale().getLanguage()%>">
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title><%=wsdlPerspective.getMessage("FRAME_TITLE_XSD_INFORMATION_CONTENT")%></title>
  <link rel="stylesheet" type="text/css" href="<%=response.encodeURL(controller.getPathWithContext("css/windows.css"))%>">
</head>
<body dir="<%=org.eclipse.wst.ws.internal.explorer.platform.util.DirUtils.getDir()%>" class="contentbodymargin">
<div id="contentborder">
<jsp:include page="<%=frag.getInformationFragment()%>" flush="true"/>
</div>
</body>
</html>
