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
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%
   StringBuffer panesFile = new StringBuffer("/");
   panesFile.append(controller.getCurrentPerspective().getPanesFile());
%>
<jsp:include page="<%=panesFile.toString()%>" flush="true"/>
</head>
<body>
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
