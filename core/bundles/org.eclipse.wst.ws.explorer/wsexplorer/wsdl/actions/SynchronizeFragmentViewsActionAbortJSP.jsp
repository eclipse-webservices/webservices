<%
/*******************************************************************************
 * Copyright (c) 2002, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
%>
<%@ page contentType="text/html; charset=UTF-8" import="org.eclipse.wst.ws.internal.explorer.platform.constants.FrameNames,
                                                                                          org.eclipse.wst.ws.internal.explorer.platform.wsdl.constants.WSDLFrameNames,
                                                                                          org.eclipse.wst.ws.internal.explorer.platform.wsdl.constants.FragmentConstants,
                                                                                          org.eclipse.wst.ws.internal.explorer.platform.wsdl.perspective.InvokeWSDLOperationTool"%>

<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<jsp:include page="/wsdl/scripts/wsdlpanes.jsp" flush="true"/>

<%
InvokeWSDLOperationTool invokeWSDLOperationTool = (InvokeWSDLOperationTool)controller.getWSDLPerspective().getNodeManager().getSelectedNode().getCurrentToolManager().getSelectedTool();
if (invokeWSDLOperationTool.getFragmentViewID().equals(FragmentConstants.FRAGMENT_VIEW_SWITCH_SOURCE_TO_FORM))
  invokeWSDLOperationTool.setFragmentViewID(FragmentConstants.FRAGMENT_VIEW_SWITCH_FORM_TO_SOURCE);
else
  invokeWSDLOperationTool.setFragmentViewID(FragmentConstants.FRAGMENT_VIEW_SWITCH_SOURCE_TO_FORM);
%>
<script language="javascript">
  wsdlPropertiesContent.location = "<%=response.encodeURL(controller.getPathWithContext("wsdl/wsdl_properties_content.jsp"))%>";
</script>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 3.2 Final//EN">
<html lang="<%=response.getLocale().getLanguage()%>">
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body dir="<%=org.eclipse.wst.ws.internal.explorer.platform.util.DirUtils.getDir()%>">
</body>
</html>
