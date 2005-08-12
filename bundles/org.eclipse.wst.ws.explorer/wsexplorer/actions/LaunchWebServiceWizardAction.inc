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
   //Load the parameters for the action from the servlet request.
   boolean inputsValid = action.populatePropertyTable(request);

   // Run the action and obtain the return code (fail/success).
   boolean actionResult = action.run();
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
<body dir="<%=org.eclipse.wst.ws.internal.explorer.platform.util.DirUtils.getDir()%>">
<%
   if (!actionResult)
   {
     String statusContentVar = action.getStatusContentVar();
     if (statusContentVar != null)
     {
%>    
<script language="javascript">
  <%=statusContentVar%>.location = "<%=response.encodeURL(controller.getPathWithContext(action.getStatusContentPage()))%>";
</script>
<%
     }
   }
%>   
</body>
</html>
