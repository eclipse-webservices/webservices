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
                                                        org.eclipse.wst.ws.internal.explorer.platform.perspective.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.datamodel.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.constants.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.util.*,
                                                        java.util.*" %>

<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<jsp:useBean id="subQueryTransferTargetHolder" class="java.util.Vector" scope="request"/>
<%
   UDDIPerspective uddiPerspective = controller.getUDDIPerspective();
%>
<jsp:include page="/scripts/formsubmit.jsp" flush="true"/>
<jsp:include page="/uddi/scripts/udditables.jsp" flush="true"/>
<script language="javascript">
  function getNumberOfSelections(tableContainerId)
  {
    var numberSelected = 0;
    var table = getTable(tableContainerId);
    for (var i=numberOfHeaderRows;i<table.rows.length;i++)
    {
      var rowCheckbox = table.rows[i].getElementsByTagName("input").item(0);
      if (rowCheckbox.checked)
        numberSelected++;
    }
    return numberSelected;
  }
  
  function processResultForm(tableContainerId,form)
  {
    if (handleSubmit(form))
    {
      processResultTable(tableContainerId,"<%=ActionInputs.NODEID%>",form,true);
      form.submit();
    }
  }
  
  function clearSelections(tableContainerId,form)
  {
    form.action = "<%=response.encodeURL(controller.getPathWithContext("uddi/actions/ClearNavigatorNodesActionJSP.jsp"))%>";
    // Ensure that at least one item is selected.
    var numberSelected = getNumberOfSelections(tableContainerId);
    if (numberSelected > 0)
      processResultForm(tableContainerId,form);
    else
      alert("<%=uddiPerspective.getMessage("MSG_ERROR_NOTHING_SELECTED")%>");
  }
  
  function refreshSelections(tableContainerId,form)
  {
    form.action = "<%=response.encodeURL(controller.getPathWithContext("uddi/actions/RefreshUDDINodesActionJSP.jsp"))%>";
    // Ensure that at least one item is selected.
    var numberSelected = getNumberOfSelections(tableContainerId);
    if (numberSelected > 0)
      processResultForm(tableContainerId,form);
    else
      alert("<%=uddiPerspective.getMessage("MSG_ERROR_NOTHING_SELECTED")%>");
  }
  
  function addSelectionsToFavorites(tableContainerId,form)
  {
    form.action = "<%=response.encodeURL(controller.getPathWithContext("uddi/actions/AddItemsToFavoritesActionJSP.jsp"))%>";
    // Ensure that at least one item is selected.
    var numberSelected = getNumberOfSelections(tableContainerId);
    if (numberSelected > 0)
      processResultForm(tableContainerId,form);
    else
      alert("<%=uddiPerspective.getMessage("MSG_ERROR_NOTHING_SELECTED")%>");
  }
  
<%
   if (subQueryTransferTargetHolder.size() > 0)
   {
     SubQueryTransferTarget subQueryTransferTarget = (SubQueryTransferTarget)subQueryTransferTargetHolder.elementAt(0);
     String subQueryKey = subQueryTransferTarget.getSubQueryKey();
     int lastSeparatorPos = subQueryKey.lastIndexOf(':');
     String targetQueryKey;
     if (lastSeparatorPos == -1)
       targetQueryKey = "";
     else
       targetQueryKey = subQueryKey.substring(0,lastSeparatorPos);
     String targetProperty = subQueryKey.substring(lastSeparatorPos+1,subQueryKey.length());
%>     
  function transferSelections(tableContainerId,form)
  {
    form.action = "<%=response.encodeURL(controller.getPathWithContext("uddi/actions/TransferSubQueryResultsActionJSP.jsp"))%>";
    // Ensure that at least one item is selected.
    var numberSelected = getNumberOfSelections(tableContainerId);
    if (numberSelected > 0)
    {
<%
     if (targetProperty.equals(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_BUSINESS) || targetProperty.equals(UDDIActionInputs.QUERY_INPUT_SIMPLE_SERVICE_BUSINESS) || targetProperty.equals(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_SERVICE))
     {
%>
      if (numberSelected != 1)
      {
        alert("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("MSG_ERROR_SINGLE_ITEM"))%>");
        return;
      }

<%
       FormTool targetFormTool = subQueryTransferTarget.getTargetFormTool();
       FormToolPropertiesInterface targetFormToolPI = ((MultipleFormToolPropertiesInterface)targetFormTool).getFormToolProperties(targetQueryKey);
       Vector serviceBusiness = (Vector)targetFormToolPI.getProperty(targetProperty);
       if (serviceBusiness != null && serviceBusiness.size() > 0)
       {
%>
      if (!confirm("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("MSG_QUESTION_ITEM_REPLACEMENT"))%>"))
        return;
<%
       }
     }
%>
      processResultForm(tableContainerId,form);
    }
    else
      alert("<%=uddiPerspective.getMessage("MSG_ERROR_NOTHING_SELECTED")%>");
  }
<%
   }
%>       
</script>  
