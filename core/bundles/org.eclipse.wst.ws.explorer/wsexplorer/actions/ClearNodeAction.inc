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
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 3.2 Final//EN">
<html lang="<%=response.getLocale().getLanguage()%>">
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%
   StringBuffer panesFile = new StringBuffer("/");
   panesFile.append(currentPerspective.getPanesFile());
%>
<jsp:include page="<%=panesFile.toString()%>" flush="true"/>
<script language="javascript" src="<%=response.encodeURL(controller.getPathWithContext("scripts/browserdetect.js"))%>">
</script>
</head>
<body dir="<%=org.eclipse.wst.ws.internal.explorer.platform.util.DirUtils.getDir()%>">
<script language="javascript">
  if (isMicrosoftInternetExplorer())
  {    
<%
   String treeContentVar = action.getTreeContentVar();
   if (treeContentVar != null)
   {
%>
    <%=treeContentVar%>.location = "<%=response.encodeURL(controller.getPathWithContext(action.getTreeContentPage()))%>";
<%
   }
   String propertiesContainerVar = action.getPropertiesContainerVar();
   if (propertiesContainerVar != null)
   {
%>
    <%=propertiesContainerVar%>.location = "<%=response.encodeURL(controller.getPathWithContext(action.getPropertiesContainerPage()))%>";
<%   
   }
   String statusContentVar = action.getStatusContentVar();
   if (statusContentVar != null)
   {
%>
    <%=statusContentVar%>.location = "<%=response.encodeURL(controller.getPathWithContext(action.getStatusContentPage()))%>";
<%
   }
%>
  }
  else
    perspectiveContent.location = "<%=response.encodeURL(controller.getPathWithContext(currentPerspective.getPerspectiveContentPage()))%>";
</script>
</body>
</html>
