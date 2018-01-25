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
  <meta http-equiv="Content-Type" content="text/html; UTF-8">
<%
   StringBuffer panesFile = new StringBuffer("/");
   panesFile.append(currentPerspective.getPanesFile());
%>
<jsp:include page="<%=panesFile.toString()%>" flush="true"/>
<script language="javascript" src="<%=response.encodeURL(controller.getPathWithContext("scripts/browserdetect.js"))%>">
</script>
</head>
<body dir="<%=org.eclipse.wst.ws.internal.explorer.platform.util.DirUtils.getDir()%>">
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
     String propertiesContainerVar = action.getPropertiesContainerVar();
     if (action.requiresTreeViewRefresh())
     {
       if (treeContentVar != null)
       {
%>
<script language="javascript">
  if (isMicrosoftInternetExplorer())
  {
    // Update the treeview.
    <%=treeContentVar%>.location = "<%=response.encodeURL(controller.getPathWithContext(action.getTreeContentPage()))%>";
  }
  else
    perspectiveContent.location = "<%=response.encodeURL(controller.getPathWithContext(currentPerspective.getPerspectiveContentPage()))%>";  
</script>
<%
       }
       if (propertiesContainerVar != null)
       {
%>
<script language="javascript">
  if (isMicrosoftInternetExplorer())
    <%=propertiesContainerVar%>.location = "<%=response.encodeURL(controller.getPathWithContext(action.getPropertiesContainerPage()))%>";
</script>
<%
       }
     }
     else
     {
       Node selectedNode = action.getSelectedNode();
       NodeManager nodeManager = selectedNode.getNodeManager();
       Node previousSelectedNode = nodeManager.getPreviousSelectedNode();
       int selectedNodeId = selectedNode.getNodeId();
       if (treeContentVar != null)
       {
%>
<script language="javascript">
  // Update the perspective.
<%
         if (previousSelectedNode != null && previousSelectedNode.isVisible())
         {
%>
  <%=treeContentVar%>.alterImage('<%=previousSelectedNode.getAnchorName()%>','<%=response.encodeURL(controller.getPathWithContext(previousSelectedNode.getClosedImagePath()))%>');
<%
         }
         String selectedNodeAnchorName = selectedNode.getAnchorName();
%>
  <%=treeContentVar%>.selectNode('<%=selectedNodeAnchorName%>','<%=response.encodeURL(controller.getPathWithContext(selectedNode.getOpenImagePath()))%>');
  <%=treeContentVar%>.location.hash = '#<%=selectedNodeAnchorName%>';
<%
       }
       if (propertiesContainerVar != null)
       {
%>
  <%=propertiesContainerVar%>.location = "<%=response.encodeURL(controller.getPathWithContext(action.getPropertiesContainerPage()))%>";
<%
       }
%>
</script>
<%
     }
   }
%>
</body>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 3.2 Final//EN">
<html lang="<%=response.getLocale().getLanguage()%>">
