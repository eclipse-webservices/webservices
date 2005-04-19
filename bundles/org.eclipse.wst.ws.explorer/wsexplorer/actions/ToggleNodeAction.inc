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
   Perspective currentPerspective = controller.getCurrentPerspective();
%>   
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; UTF-8">
<%
   StringBuffer panesFile = new StringBuffer("/");
   panesFile.append(currentPerspective.getPanesFile());
%>     
<jsp:include page="<%=panesFile.toString()%>" flush="true"/>
<script language="javascript" src="<%=response.encodeURL(controller.getPathWithContext("scripts/browserdetect.js"))%>">
</script>  
</head>
<body>
<%
   if (!actionResult)
   {
     if (action.isStaleBreadCrumb())
     {
%>
<script language="javascript">
  // Rerun the history action to get the next available item.
  perspectiveWorkArea.location = "<%=response.encodeURL(controller.getPathWithContext(action.getNextHistoryActionLink()))%>";
</script>
<%
     }
   }
   else
   {
     String treeContentVar = action.getTreeContentVar();
     if (treeContentVar != null)
     {
%>
<script language="javascript">
  if (isMicrosoftInternetExplorer())
  {
    // Update the perspective.
    <%=treeContentVar%>.location = "<%=response.encodeURL(controller.getPathWithContext(action.getTreeContentPage()))%>";
  }
  else
    perspectiveContent.location = "<%=response.encodeURL(controller.getPathWithContext(currentPerspective.getPerspectiveContentPage()))%>";
</script>
<%
     }
     if (action.requiresNodeSelection())
     {
       String propertiesContainerVar = action.getPropertiesContainerVar();
       if (propertiesContainerVar != null)
       {
%>
<script language="javascript">
  if (isMicrosoftInternetExplorer())
  {
    // Update the properties container.
    <%=propertiesContainerVar%>.location = "<%=response.encodeURL(controller.getPathWithContext(action.getPropertiesContainerPage()))%>";
  }
</script>
<%
       }
     }
   }
%>
</body>
<html>
