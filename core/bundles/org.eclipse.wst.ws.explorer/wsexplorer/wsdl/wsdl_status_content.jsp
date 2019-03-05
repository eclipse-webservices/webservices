<%
/*******************************************************************************
 * Copyright (c) 2001, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060427   127443 jesper@selskabet.org - Jesper S Moller
 *******************************************************************************/
%>
<%@ page contentType="text/html; charset=UTF-8" import="org.eclipse.wst.ws.internal.explorer.platform.wsdl.perspective.*,
	org.eclipse.wst.ws.internal.explorer.platform.util.*" %>

<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<%
   WSDLPerspective wsdlPerspective = controller.getWSDLPerspective();
   wsdlPerspective.setStatusContentType(WSDLPerspective.STATUS_CONTENT_DEFAULT);
%>   
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 3.2 Final//EN">
<html lang="<%=response.getLocale().getLanguage()%>">
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title><%=wsdlPerspective.getMessage("FRAME_TITLE_STATUS_CONTENT")%></title>
  <link rel="stylesheet" type="text/css" href="<%=response.encodeURL(controller.getPathWithContext("css/windows.css"))%>">
</head>
<body dir="<%=org.eclipse.wst.ws.internal.explorer.platform.util.DirUtils.getDir()%>" class="contentbodymargin">
<div id="contentborder">
  <table>
    <tr>
      <td>
        <pre>
<%=HTMLUtils.charactersToHTMLEntitiesStrict(wsdlPerspective.getMessageQueue().getMessagesFromList())%>
        </pre>
      </td>
    </tr>
  </table>
</div>
</body>
</html>
