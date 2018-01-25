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
<%@ page contentType="text/html; charset=UTF-8" import="org.eclipse.wst.ws.internal.explorer.platform.perspective.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.wsdl.constants.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.constants.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.wsdl.actions.ClearWSDLAction"%>
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
<%
// Prepare the action.
ClearWSDLAction action = new ClearWSDLAction(controller);

// Load the parameters for the action from the servlet request.
action.populatePropertyTable(request);

// Run the action and obtain the return code (fail/success).
boolean actionResult = action.execute();
%>
<%@ include file="/actions/ClearNodeAction.inc" %>
<%
}
%>
