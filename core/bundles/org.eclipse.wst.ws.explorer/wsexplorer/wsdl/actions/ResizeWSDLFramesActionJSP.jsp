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
<%@ page contentType="text/html; charset=UTF-8" import="org.eclipse.wst.ws.internal.explorer.platform.wsdl.perspective.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.wsdl.actions.*" %>

<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<%
   // Create the action.
   ResizeWSDLFramesAction action = new ResizeWSDLFramesAction(controller);
   
   // Populate the action with the request properties.
   boolean result = action.populatePropertyTable(request);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 3.2 Final//EN">
<html lang="<%=response.getLocale().getLanguage()%>">
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<jsp:include page="/wsdl/scripts/wsdlframesets.jsp" flush="true"/>
<script language="javascript" src="<%=response.encodeURL(controller.getPathWithContext("scripts/browserdetect.js"))%>">
</script>
</head>
<body dir="<%=org.eclipse.wst.ws.internal.explorer.platform.util.DirUtils.getDir()%>">
<%
   if (result)
   {
     action.execute(false);
     
     WSDLPerspective wsdlPerspective = controller.getWSDLPerspective();
%>
<script language="javascript">
  if (isMicrosoftInternetExplorer())
  {
    var perspectiveContentFrameset = getPerspectiveContentFrameset();
    var actionsContainerFrameset = getActionsContainerFrameset();  
    perspectiveContentFrameset.setAttribute("cols","<%=wsdlPerspective.getPerspectiveContentFramesetCols()%>");
    actionsContainerFrameset.setAttribute("rows","<%=wsdlPerspective.getActionsContainerFramesetRows()%>");
  }
  else
    perspectiveContent.location = "<%=response.encodeURL(controller.getPathWithContext("wsdl/wsdl_perspective_content.jsp"))%>";
</script>
<%
   }
%>
</body>
</html>
