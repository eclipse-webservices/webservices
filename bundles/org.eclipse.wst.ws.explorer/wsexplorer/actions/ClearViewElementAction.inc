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
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%
   StringBuffer panesFile = new StringBuffer("/");
   panesFile.append(currentPerspective.getPanesFile());
%>
<jsp:include page="<%=panesFile.toString()%>" flush="true"/>
<head>
<body>
<script language="javascript">
<%
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
</script>
</body>
</html>
