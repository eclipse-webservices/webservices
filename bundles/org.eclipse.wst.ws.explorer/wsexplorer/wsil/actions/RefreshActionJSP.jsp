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
<%@ page contentType="text/html; charset=UTF-8" import="org.eclipse.wst.ws.internal.explorer.platform.wsil.constants.WsilFrameNames,
                                                                                          org.eclipse.wst.ws.internal.explorer.platform.constants.*,
                                                                                          org.eclipse.wst.ws.internal.explorer.platform.actions.Action,
                                   					                    org.eclipse.wst.ws.internal.explorer.platform.wsil.actions.*"%>

<jsp:include page="/wsil/scripts/wsilPanes.jsp" flush="true"/>
<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<%
    // Prepare the action.
    Action action = RefreshAction.newAction(request, controller);

    // Load the parameters for the action from the servlet request.
    action.populatePropertyTable(request);

    // Run the action and obtain the return code (fail/success).
    boolean actionResult = action.execute();
%>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body dir="<%=org.eclipse.wst.ws.internal.explorer.platform.util.DirUtils.getDir()%>">
    <%
    if (!actionResult) {
        if (action instanceof RefreshWSILAction) {
        %>
            <jsp:include page="/wsil/actions/ClearNodeConfirmJSP.jsp" flush="true"/>
        <%
        }
        else {
        %>
            <script language="javascript">
                wsilPropertiesContainer.location = "<%=response.encodeURL(controller.getPathWithContext("wsil/wsil_properties_container.jsp"))%>";
                wsilStatusContent.location = "<%=response.encodeURL(controller.getPathWithContext("wsil/wsil_status_content.jsp"))%>";
            </script>
        <%
        }
        %>
    <%
    }
    else {
    %>
        <script language="javascript">
            wsilPropertiesContainer.location = "<%=response.encodeURL(controller.getPathWithContext("wsil/wsil_properties_container.jsp"))%>";
            wsilStatusContent.location = "<%=response.encodeURL(controller.getPathWithContext("wsil/wsil_status_content.jsp"))%>";
        </script>
    <%
    }
    %>
</body>
</html>
