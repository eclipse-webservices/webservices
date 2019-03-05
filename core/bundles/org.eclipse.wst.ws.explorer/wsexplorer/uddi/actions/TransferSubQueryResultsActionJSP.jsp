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
<%@ page contentType="text/html; charset=UTF-8" import="org.eclipse.wst.ws.internal.explorer.platform.uddi.actions.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.uddi.constants.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.perspective.*" %>

<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 3.2 Final//EN">
<html lang="<%=response.getLocale().getLanguage()%>">
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<jsp:include page="/uddi/scripts/uddipanes.jsp" flush="true"/>
</head>
<body dir="<%=org.eclipse.wst.ws.internal.explorer.platform.util.DirUtils.getDir()%>">
<%
   TransferSubQueryResultsAction action = new TransferSubQueryResultsAction(controller);

   if (request.getParameter(UDDIActionInputs.SHOW_RESULTS_TARGET) != null)
     action.enableShowResultsTarget();
   else
     action.populatePropertyTable(request);

   boolean actionResult = action.execute();
   Node targetNode = action.getTargetNode();
   String targetNodeAnchorName = targetNode.getAnchorName();
%>
<script language="javascript">
  navigatorContent.selectNode('<%=targetNodeAnchorName%>','<%=response.encodeURL(controller.getPathWithContext(targetNode.getOpenImagePath()))%>');
  navigatorContent.location.hash='#<%=targetNodeAnchorName%>';
  propertiesContainer.location = "<%=response.encodeURL(controller.getPathWithContext("uddi/properties_container.jsp"))%>";
</script>
</body>
</html>
