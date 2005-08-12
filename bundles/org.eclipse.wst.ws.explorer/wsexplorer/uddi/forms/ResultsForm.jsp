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
                                                        org.eclipse.wst.ws.internal.explorer.platform.uddi.actions.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.uddi.datamodel.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.uddi.constants.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.datamodel.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.perspective.*,
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
   TreeElement treeElement = selectedNode.getTreeElement();
   SubQueryTransferTarget subQueryTransferTarget = null;
   boolean hasTarget = false;
   if (treeElement instanceof QueryElement)
   {
     subQueryTransferTarget = (SubQueryTransferTarget)treeElement.getPropertyAsObject(UDDIModelConstants.SUBQUERY_TRANSFER_TARGET);
     if (subQueryTransferTarget != null && subQueryTransferTarget.getTargetFormTool() != null)
     {
       subQueryTransferTargetHolder.removeAllElements();
       subQueryTransferTargetHolder.addElement(subQueryTransferTarget);
       hasTarget = true;
     }
   }
%>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title><%=uddiPerspective.getMessage("FORM_TITLE_RESULTS")%></title>
  <link rel="stylesheet" type="text/css" href="<%=response.encodeURL(controller.getPathWithContext("css/windows.css"))%>">
<script language="javascript" src="<%=response.encodeURL(controller.getPathWithContext("scripts/browserdetect.js"))%>">
</script>
<script language="javascript" src="<%=response.encodeURL(controller.getPathWithContext("scripts/resumeproxyloadpage.js"))%>">
</script>
<jsp:include page="/uddi/scripts/results.jsp" flush="true"/>
<script language="javascript">
  function setDefaults()
  {
<%
   Vector childNodes = selectedNode.getChildNodes();
   for (int i=0;i<childNodes.size();i++)
   {
     Node childNode = (Node)childNodes.elementAt(i);
     String url = SelectNavigatorNodeAction.getActionLink(childNode.getNodeId(),false);
     String name = null;
     String description = null;
     if (childNode instanceof BusinessNode)
     {
       BusinessElement busElement = (BusinessElement)childNode.getTreeElement();
       BusinessEntity be = busElement.getBusinessEntity();
       name = be.getDefaultNameString();
       description = be.getDefaultDescriptionString();
     }
     else if (childNode instanceof ServiceNode)
     {
       ServiceElement serviceElement = (ServiceElement)childNode.getTreeElement();
       BusinessService bs = serviceElement.getBusinessService();
       name = bs.getDefaultNameString();
       description = bs.getDefaultDescriptionString();
     }
     else if (childNode instanceof ServiceInterfaceNode)
     {
       ServiceInterfaceElement siElement = (ServiceInterfaceElement)childNode.getTreeElement();
       TModel tModel = siElement.getTModel();
       name = tModel.getNameString();
       description = tModel.getDefaultDescriptionString();
     }
%>
    addResultRow("Results",<%=childNode.getNodeId()%>,"<%=response.encodeURL(controller.getPathWithContext(url))%>","<%=HTMLUtils.JSMangle(name)%>","<%=HTMLUtils.JSMangle(description)%>");
<%
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
   String title = uddiPerspective.getMessage("ALT_QUERY_RESULTS");
   String titleImagePath = "images/details_highlighted.gif";
%>
<%@ include file="/uddi/forms/resultsformheader.inc" %>
    <form target="<%=FrameNames.PERSPECTIVE_WORKAREA%>" method="post" enctype="multipart/form-data" style="margin-top:10px;">
<%
   sectionHeaderInfo.clear();
   String tableTitle;
   int itemType = Integer.parseInt(treeElement.getPropertyAsString(UDDIModelConstants.QUERY_TYPE));   
   switch (itemType)
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
   sectionHeaderInfo.setContainerId("Results");
   sectionHeaderInfo.setOtherProperties(otherProperties);
%>
<jsp:include page="/uddi/forms/results_table.jsp" flush="true"/>
      <table border=0 cellpadding=2 cellspacing=0>
        <tr>
<%
   if (hasTarget)
   {
%>
          <td height=40 align="bottom" nowrap>
            <input type="button" value="<%=uddiPerspective.getMessage("FORM_BUTTON_TRANSFER")%>" onClick="transferSelections('Results',this.form)" class="button">
          </td>
<%
   }
%>
          <td height=40 align="bottom" nowrap>
            <input type="button" value="<%=controller.getMessage("FORM_BUTTON_REFRESH")%>" onClick="refreshSelections('Results',this.form)" class="button">
          </td>
          <td height=40 align="bottom" nowrap>
            <input type="button" value="<%=uddiPerspective.getMessage("FORM_BUTTON_ADD_TO_FAVORITES")%>" onClick="addSelectionsToFavorites('Results',this.form)" class="button">
          </td>
          <td height=40 align="bottom" nowrap>
            <input type="button" value="<%=uddiPerspective.getMessage("FORM_BUTTON_CLEAR")%>" onClick="clearSelections('Results',this.form)" class="button">
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
