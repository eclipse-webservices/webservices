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
<%@ page contentType="text/html; charset=UTF-8" import="org.eclipse.wst.ws.internal.explorer.platform.uddi.perspective.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.uddi.constants.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.uddi.actions.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.constants.*" %>

<jsp:include page="/scripts/panes.jsp" flush="true"/>

<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<%
   UDDIPerspective uddiPerspective = controller.getUDDIPerspective();
%>
<script language="javascript">
  var navigatorContainer = perspectiveContent.frames["<%=UDDIFrameNames.NAVIGATOR_CONTAINER%>"];
  var navigatorToolbar = navigatorContainer.frames["<%=UDDIFrameNames.NAVIGATOR_TOOLBAR%>"];
  var navigatorContent = navigatorContainer.frames["<%=UDDIFrameNames.NAVIGATOR_CONTENT%>"];
  var actionsContainer = perspectiveContent.frames["<%=UDDIFrameNames.ACTIONS_CONTAINER%>"];
  var propertiesContainer = actionsContainer.frames["<%=UDDIFrameNames.PROPERTIES_CONTAINER%>"];
  var propertiesToolbar = propertiesContainer.frames["<%=UDDIFrameNames.PROPERTIES_TOOLBAR%>"];
  var propertiesContent = propertiesContainer.frames["<%=UDDIFrameNames.PROPERTIES_CONTENT%>"];
  var statusContainer = actionsContainer.frames["<%=UDDIFrameNames.STATUS_CONTAINER%>"];
  var statusToolbar = statusContainer.frames["<%=UDDIFrameNames.STATUS_TOOLBAR%>"];
  var statusContent = statusContainer.frames["<%=UDDIFrameNames.STATUS_CONTENT%>"];
</script>
