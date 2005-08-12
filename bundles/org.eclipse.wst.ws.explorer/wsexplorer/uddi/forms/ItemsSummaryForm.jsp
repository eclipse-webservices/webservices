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
<%@ page contentType="text/html; charset=UTF-8" import="org.eclipse.wst.ws.internal.explorer.platform.uddi.perspective.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.uddi.datamodel.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.uddi.actions.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.uddi.constants.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.perspective.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.constants.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.datamodel.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.util.*,
                                                        org.uddi4j.datatype.business.*,
                                                        org.uddi4j.datatype.service.*,
                                                        org.uddi4j.datatype.tmodel.*,
                                                        java.util.*" %>
<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<jsp:useBean id="sectionHeaderInfo" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.SectionHeaderInfo" scope="request"/>
<%
   int queryItem = Integer.parseInt(request.getParameter(UDDIActionInputs.QUERY_ITEM));
   UDDIPerspective uddiPerspective = controller.getUDDIPerspective();
   NodeManager nodeManager = uddiPerspective.getNavigatorManager();
   UDDIMainNode uddiMainNode = (UDDIMainNode)nodeManager.getRootNode();
   Node selectedNode = nodeManager.getSelectedNode();
   Tool selectedTool = selectedNode.getCurrentToolManager().getSelectedTool();
   RegistryNode regNode = uddiMainNode.getRegistryNode(selectedNode);
   TreeElement treeElement = selectedNode.getTreeElement();
   Vector summaryNodes = new Vector();
   if (treeElement instanceof QueryParentElement)
     regNode.getDiscoveredNodes(summaryNodes,queryItem);
   else if (treeElement instanceof PublishedItemsElement)
   {
     switch (queryItem)
     {
       case UDDIActionInputs.QUERY_ITEM_BUSINESSES:
         regNode.getPublishedBusinessNodes(summaryNodes);
         break;
       case UDDIActionInputs.QUERY_ITEM_SERVICES:
         regNode.getPublishedServiceNodes(summaryNodes);
         break;
       case UDDIActionInputs.QUERY_ITEM_SERVICE_INTERFACES:
       default:
         regNode.getPublishedServiceInterfaceNodes(summaryNodes);
     }
   }
   boolean hasTarget = false;
%>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title><%=uddiPerspective.getMessage("FORM_TITLE_ITEMS_SUMMARY")%></title>
  <link rel="stylesheet" type="text/css" href="<%=response.encodeURL(controller.getPathWithContext("css/windows.css"))%>">
<script language="javascript" src="<%=response.encodeURL(controller.getPathWithContext("scripts/browserdetect.js"))%>">
</script>
<script language="javascript" src="<%=response.encodeURL(controller.getPathWithContext("scripts/resumeproxyloadpage.js"))%>">
</script>
<jsp:include page="/scripts/panes.jsp" flush="true"/>
<jsp:include page="/uddi/scripts/results.jsp" flush="true"/>
<script language="javascript">
  function setDefaults()
  {
<%   
   Hashtable allUniqueNodes = new Hashtable();
   for (int i=0;i<summaryNodes.size();i++)
   {
     Node summaryNode = (Node)summaryNodes.elementAt(i);
     allUniqueNodes.put(summaryNode.getTreeElement().getKey(),summaryNode);
   }
   
   if (!allUniqueNodes.isEmpty())
   {
     Object[] itemNodes = allUniqueNodes.values().toArray();
     QuickSort.sort(itemNodes,0,itemNodes.length-1);
     for (int i=0;i<itemNodes.length;i++)
     {
       Node itemNode = (Node)itemNodes[i];
       String url = SelectNavigatorNodeAction.getActionLink(itemNode.getNodeId(),false);
       String name = null;
       String description = null;
       if (itemNode instanceof QueryNode)
       {
         QueryElement queryElement = (QueryElement)itemNode.getTreeElement();
         name = queryElement.getName();
         String numberOfChildNodesString = String.valueOf(itemNode.getChildNodes().size());
         switch (queryElement.getQueryType())
         {
           case UDDIActionInputs.QUERY_ITEM_BUSINESSES:
             description = uddiPerspective.getMessage("MSG_INFO_BUSINESSES_FOUND",numberOfChildNodesString);
             break;
           case UDDIActionInputs.QUERY_ITEM_SERVICES:
             description = uddiPerspective.getMessage("MSG_INFO_SERVICES_FOUND",numberOfChildNodesString);
             break;
           case UDDIActionInputs.QUERY_ITEM_SERVICE_INTERFACES:
           default:
             description = uddiPerspective.getMessage("MSG_INFO_SERVICE_INTERFACES_FOUND",numberOfChildNodesString);
         }             
       }
       else if (itemNode instanceof BusinessNode)
       {
         BusinessElement busElement = (BusinessElement)itemNode.getTreeElement();
         BusinessEntity be = busElement.getBusinessEntity();
         name = be.getDefaultNameString();
         description = be.getDefaultDescriptionString();
       }
       else if (itemNode instanceof ServiceNode)
       {
         ServiceElement serviceElement = (ServiceElement)itemNode.getTreeElement();
         BusinessService bs = serviceElement.getBusinessService();
         name = bs.getDefaultNameString();
         description = bs.getDefaultDescriptionString();
       }
       else if (itemNode instanceof ServiceInterfaceNode)
       {
         ServiceInterfaceElement siElement = (ServiceInterfaceElement)itemNode.getTreeElement();
         TModel tModel = siElement.getTModel();
         name = tModel.getNameString();
         description = tModel.getDefaultDescriptionString();
       }
%>
    addResultRow("Summary",<%=itemNode.getNodeId()%>,"<%=response.encodeURL(controller.getPathWithContext(url))%>","<%=HTMLUtils.JSMangle(name)%>","<%=HTMLUtils.JSMangle(description)%>");
<%
     }
   }
%>
    var loadScreenTable = document.getElementById("loadScreen");
    if (loadScreenTable.rows.length > 0)
      loadScreenTable.deleteRow(0);
    document.getElementById("mainScreen").style.display = "";
  }
</script>
</head>
<body dir="<%=org.eclipse.wst.ws.internal.explorer.platform.util.DirUtils.getDir()%>" class="contentbodymargin">
<div id="contentborder">
<div id="content">
  <table id="loadScreen">
    <tr>
      <td>
        <%=controller.getMessage("MSG_LOAD_IN_PROGRESS")%>
      </td>
    </tr>
  </table>
  <div id="mainScreen" style="display:none;">
<%
   String title = selectedTool.getAltText();
   String titleImagePath = selectedTool.getHighlightedImageLink();
%>
<%@ include file="/uddi/forms/resultsformheader.inc" %>
    <form target="<%=FrameNames.PERSPECTIVE_WORKAREA%>" method="post" enctype="multipart/form-data" style="margin-top:10px;">
<%
   sectionHeaderInfo.clear();
   String tableTitle;
   switch (queryItem)
   {
     case UDDIActionInputs.QUERY_ITEM_QUERIES:
       tableTitle = uddiPerspective.getMessage("FORM_LABEL_QUERIES");
       break;
     case UDDIActionInputs.QUERY_ITEM_BUSINESSES:
       tableTitle = uddiPerspective.getMessage("FORM_OPTION_BUSINESSES");
       break;
     case UDDIActionInputs.QUERY_ITEM_SERVICES:
       tableTitle = uddiPerspective.getMessage("FORM_OPTION_SERVICES");
       break;
     case UDDIActionInputs.QUERY_ITEM_SERVICE_INTERFACES:
     default:
       tableTitle = uddiPerspective.getMessage("FORM_OPTION_SERVICE_INTERFACES");
   }
   String[] otherProperties = {tableTitle,""};
   sectionHeaderInfo.setContainerId("Summary");
   sectionHeaderInfo.setOtherProperties(otherProperties);
%>
<jsp:include page="/uddi/forms/results_table.jsp" flush="true"/>
      <table border=0 cellpadding=2 cellspacing=0>
        <tr>
          <td height=40 align="bottom" nowrap>
            <input type="button" value="<%=controller.getMessage("FORM_BUTTON_REFRESH")%>" onClick="refreshSelections('Summary',this.form)" class="button">
          </td>
<%
   if (queryItem != UDDIActionInputs.QUERY_ITEM_QUERIES)
   {
%>             
          <td height=40 align="bottom" nowrap>
            <input type="button" value="<%=uddiPerspective.getMessage("FORM_BUTTON_ADD_TO_FAVORITES")%>" onClick="addSelectionsToFavorites('Summary',this.form)" class="button">
          </td>
<%
   }
%>             
          <td height=40 align="bottom" nowrap>
            <input type="button" value="<%=uddiPerspective.getMessage("FORM_BUTTON_CLEAR")%>" onClick="clearSelections('Summary',this.form)" class="button">
          </td>
          <td nowrap width="90%">&nbsp;</td>
        </tr>
      </table>
    </form>
  </div>
</div>
</div>
<script language="javascript">
  setDefaults();
  resumeProxyLoadPage();
</script>
</body>
</html>
