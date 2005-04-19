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
<%@ page contentType="text/html; charset=UTF-8" import="org.eclipse.wst.ws.internal.explorer.platform.uddi.actions.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.uddi.perspective.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.perspective.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.constants.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.util.*" %>

<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<jsp:include page="/uddi/scripts/uddipanes.jsp" flush="true"/>
<script language="javascript" src="<%=response.encodeURL(controller.getPathWithContext("scripts/browserdetect.js"))%>">
</script>
<%
   // Prepare the action.
   ServiceGetBusinessAction action = new ServiceGetBusinessAction(controller);

   // Load the parameters for the action from the servlet request.
   boolean inputsValid = action.populatePropertyTable(request);

   boolean serviceExists = action.validateService();

   if (!serviceExists)
   {
     int nodeId = Integer.parseInt((String)(action.getPropertyTable().get(ActionInputs.NODEID)));
     UDDIPerspective uddiPerspective = controller.getUDDIPerspective();
%>
<script language="javascript">
  if (confirm("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("MSG_QUESTION_REMOVE_SERVICE_NODE"))%>"))
    perspectiveWorkArea.location = "<%=response.encodeURL(controller.getPathWithContext(ClearNavigatorNodeAction.getActionLink(nodeId)))%>";
</script>
<%
   }
   else
   {
%>
<%@ include file="/uddi/actions/NewNodeAction.inc" %>
<%
   }
%>
