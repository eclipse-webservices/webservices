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
     if (action.requiresStatusUpdate())
     {
       String statusContentVar = action.getStatusContentVar();
       if (statusContentVar != null)
       {
%>
<script language="javascript">
  // Update the status content with any messages arising from this action.
  <%=statusContentVar%>.location = "<%=response.encodeURL(controller.getPathWithContext(action.getStatusContentPage()))%>";
</script>  
<%    
       }
     } 
        
     String treeContentVar = action.getTreeContentVar();
     String propertiesContainerVar = action.getPropertiesContainerVar();
     if (action.requiresTreeViewRefresh())
     {
%>
<script language="javascript">
<%
       if (treeContentVar != null)
       {
%>
  if (isMicrosoftInternetExplorer())
  {
    // Reload the navigator tree view to show the structural change.
    <%=treeContentVar%>.location = "<%=response.encodeURL(controller.getPathWithContext(action.getTreeContentPage()))%>";
  }
  else
  {
    perspectiveContent.location = "<%=response.encodeURL(controller.getPathWithContext(currentPerspective.getPerspectiveContentPage()))%>";
  }
<%
       }
       if (propertiesContainerVar != null)
       {
%>
  if (isMicrosoftInternetExplorer())
    <%=propertiesContainerVar%>.location = "<%=response.encodeURL(controller.getPathWithContext(action.getPropertiesContainerPage()))%>";
<%
       }
%>
</script>
<%
     }
     else if (action.requiresNodeSelection())
     {
       Node selectedNode = action.getSelectedNode();
       NodeManager nodeManager = selectedNode.getNodeManager();
       Node previousSelectedNode = nodeManager.getPreviousSelectedNode();
       int selectedNodeId = selectedNode.getNodeId();
%>
<script language="javascript">
<%
       if (treeContentVar != null)
       {
%>
  // Update the navigator tree to show the selection.
<%
         if (previousSelectedNode != null)
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
     else if (action.requiresViewSelection())
     {
       if (propertiesContainerVar != null)
       {
%>
<script language="javascript">
  // Update the properties container to show the view.
  <%=propertiesContainerVar%>.location = "<%=response.encodeURL(controller.getPathWithContext(action.getPropertiesContainerPage()))%>";
</script>
<%
       }
     }
     else
     {
       String propertiesContentVar = action.getPropertiesContentVar();
       Tool tool = action.getSelectedViewTool();
       if (tool == null)
         tool = action.getSelectedTool();
       if (tool.getToolType() != ToolTypes.ACTION)
       {
         if (propertiesContentVar != null)
         {
%>
<script language="javascript">
  // Update the properties content with the tool's form.
  <%=propertiesContentVar%>.location = "<%=response.encodeURL(controller.getPathWithContext(tool.getFormLink()))%>";
</script>
<%
         }
       }
       else
       {
         if (!action.isHistoryAction())
         {
%>
<script language="javascript">
  // Run the tool's action only if it is not from the history.
  perspectiveWorkArea.location = "<%=response.encodeURL(controller.getPathWithContext(tool.getActionLink()))%>";
</script>
<%
         }
         else
         {
           if (propertiesContentVar != null)
           {
%>
<script language="javascript">
  <%=propertiesContentVar%>.location = "<%=response.encodeURL(controller.getPathWithContext(action.getPropertiesContentPage()))%>";
</script>
<%
           }
         }
       }
     }
   }
%>
</body>
<html>
