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
<%@ page import="org.eclipse.wst.ws.internal.explorer.platform.constants.*,
                             org.eclipse.wst.ws.internal.explorer.platform.wsdl.constants.*" %>

<jsp:include page="/scripts/panes.jsp" flush="true"/>
<script language="javascript">
  var wsdlNavigatorContainer = perspectiveContent.frames["<%=WSDLFrameNames.WSDL_NAVIGATOR_CONTAINER%>"];
  var wsdlNavigatorToolbar = wsdlNavigatorContainer.frames["<%=WSDLFrameNames.WSDL_NAVIGATOR_TOOLBAR%>"];
  var wsdlNavigatorContent = wsdlNavigatorContainer.frames["<%=WSDLFrameNames.WSDL_NAVIGATOR_CONTENT%>"];
  var wsdlActionsContainer = perspectiveContent.frames["<%=WSDLFrameNames.WSDL_ACTIONS_CONTAINER%>"];
  var wsdlPropertiesContainer = wsdlActionsContainer.frames["<%=WSDLFrameNames.WSDL_PROPERTIES_CONTAINER%>"];
  var wsdlPropertiesToolbar = wsdlPropertiesContainer.frames["<%=WSDLFrameNames.WSDL_PROPERTIES_TOOLBAR%>"];
  var wsdlPropertiesContent = wsdlPropertiesContainer.frames["<%=WSDLFrameNames.WSDL_PROPERTIES_CONTENT%>"];
  var wsdlStatusContainer = wsdlActionsContainer.frames["<%=WSDLFrameNames.WSDL_STATUS_CONTAINER%>"];
  var wsdlStatusToolbar = wsdlStatusContainer.frames["<%=WSDLFrameNames.WSDL_STATUS_TOOLBAR%>"];
  var wsdlStatusContent = wsdlStatusContainer.frames["<%=WSDLFrameNames.WSDL_STATUS_CONTENT%>"];
</script>
