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
<%@ page contentType="text/html; charset=UTF-8" import="org.eclipse.wst.ws.internal.explorer.platform.perspective.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.constants.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.uddi.perspective.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.uddi.constants.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.uddi.datamodel.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.uddi.actions.*" %>
                                                        
<jsp:useBean id="commonCategoryModels" class="java.util.Hashtable" scope="application"/>
<%
   String sessionId = request.getParameter(ActionInputs.SESSIONID);
   String categoryTModelKey = request.getParameter(UDDIActionInputs.CATEGORY_TMODEL_KEY);
   HttpSession currentSession = (HttpSession)application.getAttribute(sessionId);
   Controller controller = (Controller)currentSession.getAttribute("controller");
   UDDIPerspective uddiPerspective = controller.getUDDIPerspective();
%>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title><%=uddiPerspective.getMessage("FRAME_TITLE_CATEGORIES_CONTENT")%></title>
  <link rel="stylesheet" type="text/css" href="<%=response.encodeURL(controller.getPathWithContext("css/treeview.css"))%>">
  <link rel="stylesheet" type="text/css" href="<%=response.encodeURL(controller.getPathWithContext("css/windows.css"))%>">
  <script language="javascript" src="<%=response.encodeURL(controller.getPathWithContext("scripts/browserdetect.js"))%>">
  </script>
  <script language="javascript" src="<%=response.encodeURL(controller.getPathWithContext("scripts/treeview.js"))%>">
  </script>
<script language="javascript">
  function transferToAddCategoryData()
  {
    top.opener.top.frames["<%=FrameNames.PERSPECTIVE_WORKAREA%>"].location = "<%=response.encodeURL(controller.getPathWithContext(TransferToAddCategoryDataAction.getActionLink(sessionId,categoryTModelKey)))%>";
  }  
</script>
</head>
<body dir="<%=org.eclipse.wst.ws.internal.explorer.platform.util.DirUtils.getDir()%>" class="contentbodymargin">
<%   
   // Check if the category is a common category.
   CategoryModel categoryModel = (CategoryModel)commonCategoryModels.get(categoryTModelKey);
   if (categoryModel == null)
   {
     // The category is user-defined.     
     NodeManager navigatorManager = uddiPerspective.getNavigatorManager();
     UDDIMainNode uddiMainNode = (UDDIMainNode)navigatorManager.getRootNode();
     RegistryNode regNode = uddiMainNode.getRegistryNode(navigatorManager.getSelectedNode());
     RegistryElement regElement = (RegistryElement)regNode.getTreeElement();   
     categoryModel = (CategoryModel)regElement.getUserDefinedCategory(categoryTModelKey);
   }
   byte rc = CategoryModel.OPERATION_SUCCESSFUL;
   if (!categoryModel.isDataLoaded())
   {
     synchronized(categoryModel)
     {
       if (!categoryModel.isDataLoaded())
         rc = categoryModel.loadFromDefaultDataFile();
     }
   }
   if (rc != CategoryModel.OPERATION_SUCCESSFUL)
   {
%>
<div id="contentborder">
  <table>
    <tr>
      <td>
        <%=uddiPerspective.getMessage("MSG_INFO_NO_CATEGORY_DATA",categoryModel.getDisplayName())%>
      </td>
    </tr>
    <tr>
      <td height=10 valign="bottom">&nbsp;</td>
    </tr>
    <tr>
      <td>
        <%=uddiPerspective.getMessage("FORM_LABEL_ADD_CATEGORY_DATA","javascript:transferToAddCategoryData()")%>
      </td>
    </tr>
  </table>
</div>
<%   
   }
   else
   {
     NodeManager categoryManager = uddiPerspective.getCategoryManager(categoryModel);
     String selectedAnchorName = "";
     int focusedNodeId = categoryManager.getFocusedNodeId();
     String focusedAnchorName = String.valueOf(focusedNodeId);
     Node selectedNode = categoryManager.getSelectedNode();
     if (selectedNode != null)
     {
       selectedAnchorName = selectedNode.getAnchorName();
       if (focusedNodeId == selectedNode.getNodeId())
         focusedAnchorName = selectedAnchorName;
     }
%>
<div id="treecontentborder">
<%=categoryManager.renderTreeView(response)%>
</div>
<script language="javascript">
  self.location.hash="#<%=focusedAnchorName%>"
  setSelectedAnchorName("<%=selectedAnchorName%>");
</script>
<%
   }
%>   
</body>
</html>
