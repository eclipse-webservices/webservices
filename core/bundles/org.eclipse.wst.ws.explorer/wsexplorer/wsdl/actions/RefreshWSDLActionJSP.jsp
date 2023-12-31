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
<%@ page contentType="text/html; charset=UTF-8" import="org.eclipse.wst.ws.internal.explorer.platform.wsdl.actions.RefreshWSDLAction,
                                                        org.eclipse.wst.ws.internal.explorer.platform.wsdl.actions.ClearWSDLAction,
                                                        org.eclipse.wst.ws.internal.explorer.platform.wsdl.perspective.WSDLPerspective,
                                                        org.eclipse.wst.ws.internal.explorer.platform.perspective.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.constants.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.util.*" %>

<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 3.2 Final//EN">
<html lang="<%=response.getLocale().getLanguage()%>">
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
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

 <script language="javascript" src="<%=response.encodeURL(controller.getPathWithContext("scripts/browserdetect.js"))%>">
 </script>
 <jsp:include page="/wsdl/scripts/wsdlpanes.jsp" flush="true"/>
 <%
  // Prepare the action.
  RefreshWSDLAction action = new RefreshWSDLAction(controller);

  // The action may be executed via program link.
  boolean paramsValid = action.populatePropertyTable(request);
  if (paramsValid) {
    int nodeID = Integer.parseInt((String)action.getPropertyTable().get(ActionInputs.NODEID));
    WSDLPerspective wsdlPerspective = controller.getWSDLPerspective();
    NodeManager nodeManager = wsdlPerspective.getNodeManager();
    Node wsdlNode = nodeManager.getNode(nodeID);
  
    // Run the action.
    boolean actionResult = action.execute();
  
    if (actionResult) {
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
   else {
   %>
    <script language="javascript">
    	if (confirm("<%=HTMLUtils.JSMangle(wsdlPerspective.getMessage("MSG_QUESTION_REMOVE_WSDL_NODE", wsdlNode.getNodeName()))%>"))
      	perspectiveWorkArea.location = "<%=response.encodeURL(controller.getPathWithContext(ClearWSDLAction.getActionLink(nodeID)))%>";
    	else {
      	wsdlPropertiesContainer.location = "<%=response.encodeURL(controller.getPathWithContext("wsdl/wsdl_properties_container.jsp"))%>";
      	wsdlStatusContent.location = "<%=response.encodeURL(controller.getPathWithContext("wsdl/wsdl_status_content.jsp"))%>";
    	}
  	</script>
   <%
  }
 }
%>
<%
}
%>
</head>
<body dir="<%=org.eclipse.wst.ws.internal.explorer.platform.util.DirUtils.getDir()%>">
</body>
</html>
