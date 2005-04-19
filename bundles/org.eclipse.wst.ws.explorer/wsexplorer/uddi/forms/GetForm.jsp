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
                                                        org.eclipse.wst.ws.internal.explorer.platform.uddi.constants.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.uddi.actions.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.uddi.datamodel.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.perspective.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.datamodel.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.constants.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.util.*,
                                                        org.uddi4j.datatype.business.*,
                                                        org.uddi4j.datatype.service.*,
                                                        org.uddi4j.datatype.tmodel.*,
                                                        java.util.*" %>

<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<jsp:useBean id="sectionHeaderInfo" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.SectionHeaderInfo" scope="request"/>
<jsp:useBean id="subQueryTransferTargetHolder" class="java.util.Vector" scope="request">
<%
   subQueryTransferTargetHolder.removeAllElements();
%>
</jsp:useBean>
<%
   UDDIPerspective uddiPerspective = controller.getUDDIPerspective();
   NodeManager nodeManager = uddiPerspective.getNavigatorManager();
   Node selectedNode = nodeManager.getSelectedNode();
   FormTool formTool = (FormTool)(selectedNode.getCurrentToolManager().getSelectedTool());
   String subQueryKey = (String)formTool.getProperty(UDDIActionInputs.SUBQUERY_KEY);
   FormToolPropertiesInterface formToolPI = ((MultipleFormToolPropertiesInterface)formTool).getFormToolProperties(subQueryKey);
   TreeElement treeElement = selectedNode.getTreeElement();
   String parentQueryKey;
   int lastSeparatorPos = subQueryKey.lastIndexOf(':');
   if (lastSeparatorPos == -1)
     parentQueryKey = "";
   else
     parentQueryKey = subQueryKey.substring(0,lastSeparatorPos);
   FormToolPropertiesInterface parentFormToolPI = ((MultipleFormToolPropertiesInterface)formTool).getFormToolProperties(parentQueryKey);
   Object queryItemProperty = parentFormToolPI.getProperty(UDDIActionInputs.QUERY_ITEM);
   Object queryStyleBus = parentFormToolPI.getProperty(UDDIActionInputs.QUERY_STYLE_BUSINESSES);
   Object queryStyleSer = parentFormToolPI.getProperty(UDDIActionInputs.QUERY_STYLE_SERVICES);
   Object queryStyleSerInt = parentFormToolPI.getProperty(UDDIActionInputs.QUERY_STYLE_SERVICE_INTERFACES);
   Hashtable parentQueryData = new Hashtable();
   if (queryItemProperty != null)
     parentQueryData.put(UDDIActionInputs.QUERY_ITEM, queryItemProperty);
   if (queryStyleBus != null)
     parentQueryData.put(UDDIActionInputs.QUERY_STYLE_BUSINESSES, queryStyleBus);
   if (queryStyleSer != null)
     parentQueryData.put(UDDIActionInputs.QUERY_STYLE_SERVICES, queryStyleSer);
   if (queryStyleSerInt != null)
     parentQueryData.put(UDDIActionInputs.QUERY_STYLE_SERVICE_INTERFACES, queryStyleSerInt);
   SubQueryTransferTarget subQueryTransferTarget = new SubQueryTransferTarget(selectedNode,subQueryKey,parentQueryData);
   treeElement.setPropertyAsObject(UDDIModelConstants.SUBQUERY_TRANSFER_TARGET,subQueryTransferTarget);
   subQueryTransferTargetHolder.addElement(subQueryTransferTarget);
   boolean hasTarget = true;
%>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title><%=uddiPerspective.getMessage("FORM_TITLE_GET_FORM")%></title>
  <link rel="stylesheet" type="text/css" href="<%=response.encodeURL(controller.getPathWithContext("css/windows.css"))%>">
<script language="javascript" src="<%=response.encodeURL(controller.getPathWithContext("scripts/browserdetect.js"))%>">
</script>
<jsp:include page="/scripts/panes.jsp" flush="true"/>
<jsp:include page="/uddi/scripts/results.jsp" flush="true"/>
<script language="javascript">
  function setDefaults()
  {
<%
   int queryItem = Integer.parseInt((String)formToolPI.getProperty(UDDIActionInputs.QUERY_ITEM));
   Hashtable allUniqueNodes = new Hashtable();
   Vector allNodes = new Vector();
   UDDIMainNode uddiMainNode = (UDDIMainNode)nodeManager.getRootNode();
   RegistryNode regNode = uddiMainNode.getRegistryNode(selectedNode);
   switch (queryItem)
   {
     case UDDIActionInputs.QUERY_ITEM_BUSINESSES:
       regNode.getAllBusinessNodes(allNodes);
       break;
     case UDDIActionInputs.QUERY_ITEM_SERVICES:
       regNode.getAllServiceNodes(allNodes);
       break;
     case UDDIActionInputs.QUERY_ITEM_SERVICE_INTERFACES:
     default:
       regNode.getAllServiceInterfaceNodes(allNodes);
   }
   
   for (int i=0;i<allNodes.size();i++)
   {
     Node node = (Node)allNodes.elementAt(i);
     allUniqueNodes.put(node.getTreeElement().getKey(),node);
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
       if (itemNode instanceof BusinessNode)
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
    addResultRow("Get",<%=itemNode.getNodeId()%>,"<%=response.encodeURL(controller.getPathWithContext(url))%>","<%=HTMLUtils.JSMangle(name)%>","<%=HTMLUtils.JSMangle(description)%>");
<%
     }
%>
    var loadScreenTable = document.getElementById("loadScreen");
    if (loadScreenTable.rows.length > 0)
      loadScreenTable.deleteRow(0);
    document.getElementById("mainScreen").style.display = "";
<%
   }
   else
   {
%>
    perspectiveWorkArea.location = "<%=response.encodeURL(controller.getPathWithContext(TransferSubQueryResultsAction.getActionLinkForReturn()))%>";
<%
   }
%>
  }
</script>
</head>
<body class="contentbodymargin">
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
   String title = uddiPerspective.getMessage("ALT_GET");
   String titleImagePath = "uddi/images/find_highlighted.gif";
%>
<%@ include file="/uddi/forms/resultsformheader.inc" %>
    <form target="<%=FrameNames.PERSPECTIVE_WORKAREA%>" method="post" enctype="multipart/form-data" style="margin-top:10px;">
<%
   sectionHeaderInfo.clear();
   String tableTitle;
   switch (queryItem)
   {
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
   sectionHeaderInfo.setContainerId("Get");
   sectionHeaderInfo.setOtherProperties(otherProperties);
%>
<jsp:include page="/uddi/forms/results_table.jsp" flush="true"/>
      <table border=0 cellpadding=2 cellspacing=0>
        <tr>
          <td height=40 align="bottom" nowrap>
            <input type="button" value="<%=uddiPerspective.getMessage("FORM_BUTTON_TRANSFER")%>" onClick="transferSelections('Get',this.form)" class="button">
          </td>
          <td height=40 align="bottom" nowrap>
            <input type="button" value="<%=controller.getMessage("FORM_BUTTON_REFRESH")%>" onClick="refreshSelections('Get',this.form)" class="button">
          </td>
          <td height=40 align="bottom" nowrap>
            <input type="button" value="<%=uddiPerspective.getMessage("FORM_BUTTON_ADD_TO_FAVORITES")%>" onClick="addSelectionsToFavorites('Get',this.form)" class="button">
          </td>
          <td height=40 align="bottom" nowrap>
            <input type="button" value="<%=uddiPerspective.getMessage("FORM_BUTTON_CLEAR")%>" onClick="clearSelections('Get',this.form)" class="button">
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
</script>
</body>
</html>
