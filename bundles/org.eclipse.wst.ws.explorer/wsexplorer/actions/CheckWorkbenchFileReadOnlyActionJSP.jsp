<%
/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
%>
<%@ page contentType="text/html; charset=UTF-8" import="org.eclipse.wst.ws.internal.explorer.platform.constants.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.actions.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.util.*" %>
<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>                                                        
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%
   StringBuffer panesFile = new StringBuffer("/");
   panesFile.append(controller.getCurrentPerspective().getPanesFile());
%>
<jsp:include page="<%=panesFile.toString()%>" flush="true"/>
</head>
<body dir="<%=org.eclipse.wst.ws.internal.explorer.platform.util.DirUtils.getDir()%>">
<script language="javascript">
<%
   ImportToWorkbenchAction action = (ImportToWorkbenchAction)session.getAttribute(ActionInputs.IMPORT_ACTION);
   if (action.isTargetFileResourceReadOnly())
   {
%>
  var proceed = true;
<%   
     if (!action.isCheckoutFilesEnabled())
     {
%>
  proceed = confirm("<%=HTMLUtils.JSMangle(action.getWebServicePluginFileMessage("MSG_ERROR_FILE_CHECKOUT_DISABLED"))%>");
<%     
     }
%>
  if (proceed)
    perspectiveWorkArea.location = "<%=response.encodeURL(controller.getPathWithContext(ValidateEditAction.getActionLink()))%>";
<%     
   }
   else
   {
%>
  perspectiveWorkArea.location = "<%=response.encodeURL(controller.getPathWithContext(WriteWSDLToWorkbenchAction.getActionLink()))%>";
<%
   }
%>     
</script>
</body>
</html>   
