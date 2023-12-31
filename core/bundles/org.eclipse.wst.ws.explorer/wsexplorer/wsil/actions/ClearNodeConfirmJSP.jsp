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
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060906 155434   makandre@ca.ibm.com - Andrew Mak, Back Slash '\' not appearing in "WSIL no longer resolvable" error message
 *******************************************************************************/
%>
<%@ page contentType="text/html; charset=UTF-8" import="org.eclipse.wst.ws.internal.explorer.platform.wsil.constants.WsilFrameNames,
                                                                                          org.eclipse.wst.ws.internal.explorer.platform.constants.*,
                                   					                    org.eclipse.wst.ws.internal.explorer.platform.wsil.actions.ClearWSILAction,
                                   					                    org.eclipse.wst.ws.internal.explorer.platform.util.*"%>

<jsp:include page="/wsil/scripts/wsilPanes.jsp" flush="true"/>
<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 3.2 Final//EN">
<html lang="<%=response.getLocale().getLanguage()%>">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body dir="<%=org.eclipse.wst.ws.internal.explorer.platform.util.DirUtils.getDir()%>">
    <script language="javascript">
        if (confirm("<%=HTMLUtils.JSMangle(controller.getWSILPerspective().getMessage("MSG_CONFIRM_REMOVE_NODE", controller.getWSILPerspective().getNodeManager().getSelectedNode().getNodeName()))%>")) {
            perspectiveWorkArea.location = "<%=response.encodeURL(controller.getPathWithContext(ClearWSILAction.getActionLink(controller.getWSILPerspective().getNodeManager().getSelectedNode().getNodeId())))%>";
        }
        else {
            wsilPropertiesContainer.location = "<%=response.encodeURL(controller.getPathWithContext("wsil/wsil_properties_container.jsp"))%>";
            wsilStatusContent.location = "<%=response.encodeURL(controller.getPathWithContext("wsil/wsil_status_content.jsp"))%>";
        }
    </script>
</body>
</html>
