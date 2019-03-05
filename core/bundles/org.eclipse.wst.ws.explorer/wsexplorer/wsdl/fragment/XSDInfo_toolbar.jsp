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
<%@ page contentType="text/html; charset=UTF-8" import="org.eclipse.wst.ws.internal.explorer.platform.wsdl.perspective.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.wsdl.datamodel.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.wsdl.constants.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.perspective.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.constants.*" %>

<%
   String sessionId = request.getParameter(ActionInputs.SESSIONID);
   HttpSession currentSession = (HttpSession)application.getAttribute(request.getParameter(ActionInputs.SESSIONID));
   Controller controller = (Controller)currentSession.getAttribute("controller");
   WSDLPerspective wsdlPerspective = controller.getWSDLPerspective();
   Node selectedNode = wsdlPerspective.getNodeManager().getNode(Integer.parseInt(request.getParameter(ActionInputs.NODEID)));
   WSDLOperationElement operElement = (WSDLOperationElement)selectedNode.getTreeElement();
   IXSDFragment frag = operElement.getFragmentByID(request.getParameter(WSDLActionInputs.FRAGMENT_ID));   
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 3.2 Final//EN">
<html lang="<%=response.getLocale().getLanguage()%>">
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title><%=wsdlPerspective.getMessage("FRAME_TITLE_XSD_INFORMATION_TOOLBAR")%></title>
  <link rel="stylesheet" type="text/css" href="<%=response.encodeURL(controller.getPathWithContext("css/toolbar.css"))%>">
</head>
<body dir="<%=org.eclipse.wst.ws.internal.explorer.platform.util.DirUtils.getDir()%>" onUnload="top.opener.xsdInfoDialogClosed=true" class="toolbarbodymargin">
<div id="toolbarborder">
  <div id="toolbar">
    <table width="100%" height=25 cellpadding=0 cellspacing=0 border=0>
      <tr>
        <td valign="middle" align="center" width=25 height=25><img class="normal" src="<%=response.encodeURL(controller.getPathWithContext("images/wsdl.gif"))%>" alt="" width=16 height=16></td>
        <td valign="middle" align="left" width="*" height=25 nowrap class="text"><%=frag.getName()%></td>
      </tr>
    </table>
  </div>
</div>
<script language="javascript">
  top.opener.xsdInfoDialogClosed = false;
</script>
</body>
</html>
