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
<%@ page contentType="text/html; charset=UTF-8" import="org.eclipse.wst.ws.internal.explorer.platform.wsil.constants.WsilFrameNames,
                                                        org.eclipse.wst.ws.internal.explorer.platform.constants.FrameNames,
                                   	                    org.eclipse.wst.ws.internal.explorer.platform.wsil.actions.AddToFavoritesAction"%>

<jsp:include page="/wsil/scripts/wsilPanes.jsp" flush="true"/>
<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<%
// Prepare the action.
AddToFavoritesAction action = AddToFavoritesAction.newAction(request, controller);

// Load the parameters for the action from the servlet request.
action.populatePropertyTable(request);

// Run the action
action.execute();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 3.2 Final//EN">
<html lang="<%=response.getLocale().getLanguage()%>">
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body dir="<%=org.eclipse.wst.ws.internal.explorer.platform.util.DirUtils.getDir()%>">
  <script language="javascript">
    wsilPropertiesContainer.location = "<%=response.encodeURL(controller.getPathWithContext("wsil/wsil_properties_container.jsp"))%>";
    wsilStatusContent.location = "<%=response.encodeURL(controller.getPathWithContext("wsil/wsil_status_content.jsp"))%>";
  </script>
</body>
</html>
