<%
/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
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
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 3.2 Final//EN">
<html lang="<%=response.getLocale().getLanguage()%>">
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
   //Load the parameters for the action from the servlet request.
   boolean inputsValid = action.populatePropertyTable(request);
   if (!inputsValid)
   {
     String statusContentVar = action.getStatusContentVar();
     if (statusContentVar != null)
     {
%>
  <%=statusContentVar%>.location = "<%=response.encodeURL(controller.getPathWithContext(action.getStatusContentPage()))%>";
<%
     }
   }
   else
   {
     session.setAttribute(ActionInputs.IMPORT_ACTION,action);   
     if (action.fileExists())
     {
%>
  var proceed = true;
<%        
       if (!action.isOverwriteFilesEnabled())
       {
%>
  proceed = confirm("<%=HTMLUtils.JSMangle(action.getWebServicePluginFileMessage("MSG_ERROR_FILE_OVERWRITE_DISABLED"))%>");
<%
       }
%>
  if (proceed)
    perspectiveWorkArea.location = "<%=response.encodeURL(controller.getPathWithContext(CheckWorkbenchFileReadOnlyAction.getActionLink()))%>";
<%
     }
     else
     {
%>
  perspectiveWorkArea.location = "<%=response.encodeURL(controller.getPathWithContext(WriteWSDLToWorkbenchAction.getActionLink()))%>";
<%
     }
   }
%>
</script>   
</body>
</html>
