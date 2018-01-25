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
<%@ page contentType="text/html; charset=UTF-8" import="org.eclipse.wst.ws.internal.explorer.platform.constants.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.actions.*" %>

<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<%
   String sessionId = session.getId();
%>
<script language="javascript">
  var wsdlWindow;
  var wsdlWindowClosed = true;
  var targetWSDLURLElement;
  function openWSDLBrowser(formContainerId,type)
  {
    var form = document.getElementById(formContainerId).getElementsByTagName("form").item(0);
    targetWSDLURLElement = form.<%=ActionInputs.QUERY_INPUT_WSDL_URL%>;
    var link;
    switch (type)
    {
      case <%=ActionInputs.WSDL_TYPE_SERVICE%>:
        link = "<%=response.encodeURL(controller.getPathWithContext(OpenWSDLBrowserAction.getActionLinkForService(sessionId)))%>";
        break;
      case <%=ActionInputs.WSDL_TYPE_SERVICE_INTERFACE%>:
      default:
        link = "<%=response.encodeURL(controller.getPathWithContext(OpenWSDLBrowserAction.getActionLinkForServiceInterface(sessionId)))%>";
    }
    wsdlWindow = window.open(link,"wsdlWindow","height=300,width=450,status=yes,scrollbars=yes,resizable=yes");
    if (wsdlWindow.focus)
      wsdlWindow.focus();
  }

  function closeWSDLBrowser()
  {
    if (!wsdlWindowClosed)
      wsdlWindow.close();
  }
</script>
