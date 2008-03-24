<%
/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
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
                                                        org.eclipse.wst.ws.internal.explorer.platform.perspective.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.uddi.constants.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.uddi.actions.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.uddi.perspective.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.uddi.datamodel.*,
                                                        org.uddi4j.util.*" %>

<%
   String sessionId = request.getParameter(ActionInputs.SESSIONID);
   HttpSession currentSession = (HttpSession)application.getAttribute(sessionId);
   Controller controller = (Controller)currentSession.getAttribute("controller");
%>   
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 3.2 Final//EN">
<html lang="<%=response.getLocale().getLanguage()%>">
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<jsp:include page="/uddi/scripts/cbpanes.jsp" flush="true"/>
<%
   // Prepare the action.
   SelectCategoryNodeAction action = new SelectCategoryNodeAction(controller);

   // Load the parameters for the action from the servlet request.
   action.populatePropertyTable(request);

   // Run the action and obtain the return code (fail/success).
   boolean actionResult = action.execute(false);

   CategoryNode selectedNode = (CategoryNode)action.getSelectedNode();
   NodeManager nodeManager = selectedNode.getNodeManager();
   Node previousSelectedNode = nodeManager.getPreviousSelectedNode();
   int selectedNodeId = selectedNode.getNodeId();
%>
<script language="javascript">
<%
   String treeContentVar = action.getTreeContentVar();
   if (previousSelectedNode != null)
   {
     if (treeContentVar != null)
     {
%>
  <%=treeContentVar%>.alterImage('<%=previousSelectedNode.getAnchorName()%>','<%=response.encodeURL(controller.getPathWithContext(previousSelectedNode.getClosedImagePath()))%>');
<%
     }
   }
   String selectedNodeAnchorName = selectedNode.getAnchorName();
   if (treeContentVar != null)
   {
%>     
  <%=treeContentVar%>.selectNode('<%=selectedNodeAnchorName%>','<%=response.encodeURL(controller.getPathWithContext(selectedNode.getOpenImagePath()))%>');
  <%=treeContentVar%>.location.hash = '#<%=selectedNodeAnchorName%>';
<%  
   }
   
   CategoryElement categoryElement = (CategoryElement)selectedNode.getTreeElement();
   KeyedReference kr = categoryElement.getCategory();
%>
  categoryBrowserWindow.opener.targetCategoryKeyNameElement.value = "<%=kr.getKeyName()%>";
  categoryBrowserWindow.opener.targetCategoryKeyValueElement.value = "<%=kr.getKeyValue()%>";
  categoryBrowserWindow.opener.closeCategoryBrowser();
</script>
</head>
<body dir="<%=org.eclipse.wst.ws.internal.explorer.platform.util.DirUtils.getDir()%>">
</body>
</html>
