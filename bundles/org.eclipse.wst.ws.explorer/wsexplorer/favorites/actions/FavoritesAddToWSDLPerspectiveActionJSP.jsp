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
<%@ page contentType="text/html; charset=UTF-8" import="org.eclipse.wst.ws.internal.explorer.platform.favorites.constants.FavoritesActionInputs,
                                                        org.eclipse.wst.ws.internal.explorer.platform.constants.ActionInputs,
                                                        org.eclipse.wst.ws.internal.explorer.platform.actions.ShowPerspectiveAction,
                                                        org.eclipse.wst.ws.internal.explorer.platform.favorites.actions.FavoritesAddToWSDLPerspectiveAction"%>

<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>

<jsp:include page="/scripts/panes.jsp" flush="true"/>
<jsp:include page="/favorites/scripts/favoritesPanes.jsp" flush="true"/>

<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body>
<%
// Prepare the action.
FavoritesAddToWSDLPerspectiveAction action = new FavoritesAddToWSDLPerspectiveAction(controller);

// Load the parameters for the action from the servlet request.
boolean inputsValid = action.populatePropertyTable(request);

// Run the action and obtain the return code (fail/success).
boolean actionResult = action.execute();

if (actionResult) {
%>
<script language="javascript">
  perspectiveWorkArea.location = "<%=response.encodeURL(controller.getPathWithContext(ShowPerspectiveAction.getActionLink(ActionInputs.PERSPECTIVE_WSDL,false)))%>";
</script>
<%
}
else {
  if (request.getParameter(FavoritesActionInputs.MULTIPLE_LINK_ACTION) == null) {
%>
    <jsp:include page="/favorites/actions/RemoveFavoritesConfirmJSP.jsp" flush="true"/>
<%
  }
  else {
%>
    <script language="javascript">
      favPropertiesContainer.location = "<%=response.encodeURL(controller.getPathWithContext("favorites/fav_properties_container.jsp"))%>";
      favStatusContent.location = "<%=response.encodeURL(controller.getPathWithContext("favorites/fav_status_content.jsp"))%>";
    </script>
<%
  }
}
%>
</body>
</html>
