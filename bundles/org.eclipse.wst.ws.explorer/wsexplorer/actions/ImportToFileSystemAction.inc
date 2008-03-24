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
<%
   // Load the parameters for the action from the servlet request.
   action.populatePropertyTable(request);

   // run the action
   boolean actionResult = action.run();
   boolean written = false;

   if (actionResult)
   {
     // write to file system
     response.setContentType("application/octet-stream");
     response.setHeader("Content-Disposition","attachment;filename="+action.getDefaultFileName());
     action.write(response.getOutputStream());
     written = true;
   }

   if (!written)
   {
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
<%
      if (action.getStatusContentVar() != null)
      {
%>
<script language="javascript">
  <%=action.getStatusContentVar()%>.location = "<%=response.encodeURL(controller.getPathWithContext(action.getStatusContentPage()))%>";
</script>
<%
      }
%>      
</body>
</html>
<%
   }
%>  
