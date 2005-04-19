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
<%@ page import="org.eclipse.wst.ws.internal.explorer.platform.wsil.constants.*,
                 org.eclipse.wst.ws.internal.explorer.platform.constants.*" %>

<jsp:include page="/scripts/panes.jsp" flush="true"/>
<script language="javascript">
  var wsilNavigatorContainer = perspectiveContent.frames["<%=WsilFrameNames.WSIL_NAVIGATOR_CONTAINER%>"];
  var wsilNavigatorToolbar = wsilNavigatorContainer.frames["<%=WsilFrameNames.WSIL_NAVIGATOR_TOOLBAR%>"];
  var wsilNavigatorContent = wsilNavigatorContainer.frames["<%=WsilFrameNames.WSIL_NAVIGATOR_CONTENT%>"];
  var wsilActionsContainer = perspectiveContent.frames["<%=WsilFrameNames.WSIL_ACTIONS_CONTAINER%>"];
  var wsilPropertiesContainer = wsilActionsContainer.frames["<%=WsilFrameNames.WSIL_PROPERTIES_CONTAINER%>"];
  var wsilPropertiesToolbar = wsilPropertiesContainer.frames["<%=WsilFrameNames.WSIL_PROPERTIES_TOOLBAR%>"];
  var wsilPropertiesContent = wsilPropertiesContainer.frames["<%=WsilFrameNames.WSIL_PROPERTIES_CONTENT%>"];
  var wsilStatusContainer = wsilActionsContainer.frames["<%=WsilFrameNames.WSIL_STATUS_CONTAINER%>"];
  var wsilStatusToolbar = wsilStatusContainer.frames["<%=WsilFrameNames.WSIL_STATUS_TOOLBAR%>"];
  var wsilStatusContent = wsilStatusContainer.frames["<%=WsilFrameNames.WSIL_STATUS_CONTENT%>"];
</script>
