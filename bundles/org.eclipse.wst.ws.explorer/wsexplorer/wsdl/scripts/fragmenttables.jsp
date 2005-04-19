<%
/*******************************************************************************
 * Copyright (c) 2002, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
%>
<%@ page contentType="text/html; charset=UTF-8" import="org.eclipse.wst.ws.internal.explorer.platform.wsdl.perspective.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.wsdl.constants.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.constants.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.util.*"%>

<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<jsp:include page="/scripts/tables.jsp" flush="true"/>
<jsp:include page="/scripts/formutils.jsp" flush="true"/>
<jsp:include page="/scripts/calendarbrowser.jsp" flush="true"/>
<script language="javascript" src="<%=response.encodeURL(controller.getPathWithContext("scripts/browserdetect.js"))%>">
</script>

<%
   WSDLPerspective wsdlPerspective = controller.getWSDLPerspective();
%>

<script language="javascript">
  var xsdInfoDialog;
  var xsdInfoDialogClosed = true;
  var rowCheckboxName = "rowCheckboxName";
  var unbounded = <%=FragmentConstants.UNBOUNDED%>;

  function checkMaxOccursReached(tableContainerID, maxOccurs) {
    var table = getTable(tableContainerID);
    if (maxOccurs != unbounded && (table.rows.length - numberOfHeaderRows) >= maxOccurs) {
      alert("<%=HTMLUtils.JSMangle(wsdlPerspective.getMessage("MSG_ERROR_MAX_OCCURS_VIOLATION"))%>");
      return true;
    }
    else
      return false;
  }

  function checkMinOccursAndRemoveSelectedRows(tableContainerID, minOccurs) {
    var table = getTable(tableContainerID);
    var rowsLength = table.rows.length - numberOfHeaderRows;
    var checkedRows = 0;
    for (var i = numberOfHeaderRows; i < table.rows.length; i++) {
      var tableRow = table.rows[i];
      var rowCheckbox = tableRow.getElementsByTagName("input").item(0);
      if (rowCheckbox.checked)
        checkedRows++;
    }
    if (checkedRows == 0)
      alert("<%=HTMLUtils.JSMangle(wsdlPerspective.getMessage("MSG_ERROR_NOTHING_SELECTED"))%>");
    else if ((rowsLength - checkedRows) < minOccurs)
      alert("<%=HTMLUtils.JSMangle(wsdlPerspective.getMessage("MSG_ERROR_MIN_OCCURS_VIOLATION"))%>");
    else
      removeSelectedRows(tableContainerID);
  }
  
  function checkMinOccursAndRemoveSelectedDateTimeRows(calendarType,tableContainerID,minOccurs)
  {
    var table = getTable(tableContainerID);
    var newCalendarTargetRow = calendarTargetRow;
    for (var i=numberOfHeaderRows;i<table.rows.length;i++)
    {
      var columns = table.rows[i].getElementsByTagName("td");
      var rowCheckbox = columns.item(0).getElementsByTagName("input").item(0);
      if (rowCheckbox.checked)
      {
        if (i-numberOfHeaderRows == calendarTargetRow)
          newCalendarTargetRow = -1;
        else
          newCalendarTargetRow--;
      }
    }
    if (newCalendarTargetRow == -1)
      closeCalendarBrowser();
    calendarTargetRow = newCalendarTargetRow;
      
    checkMinOccursAndRemoveSelectedRows(tableContainerID,minOccurs);
<%
   // Fix the browse... links.
%>
    var table = getTable(tableContainerID);
    for (var i=numberOfHeaderRows;i<table.rows.length;i++)
    {
      var columns = table.rows[i].getElementsByTagName("td");
      var dateTimeTextField = columns.item(1).getElementsByTagName("input").item(0);
      setDateTimeBrowseLinkHref(columns.item(2).getElementsByTagName("a").item(0),calendarType,dateTimeTextField.name,i-numberOfHeaderRows);
    }
  }

  function createInstance(tableContainerID, maxOccurs, fragmentID, nameAnchorID) {
    if (checkMaxOccursReached(tableContainerID, maxOccurs))
      return;
    twistOpen(tableContainerID);
    var table = getTable(tableContainerID);
    var tableBody = table.getElementsByTagName("tbody").item(0);
    var newRow = document.createElement("tr");
    var newColumn = document.createElement("td");
    newColumn.appendChild(createHiddenElement("<%=FragmentConstants.FRAGMENT_ID%>", fragmentID));
    newColumn.appendChild(createHiddenElement("<%=FragmentConstants.NAME_ANCHOR_ID%>", nameAnchorID));
    newRow.appendChild(newColumn);
    tableBody.appendChild(newRow);
    var form = document.getElementsByTagName("form");
    form[0].action = "<%=response.encodeURL(controller.getPathWithContext("wsdl/actions/CreateInstanceActionJSP.jsp"))%>";
    form[0].submit();
  }

  function openXSDInfoDialog(link) {
    xsdInfoDialog = window.open(link, "XSDInformationDialog", "height=500,width=500,scrollbars=no,resizable=yes");
    if (xsdInfoDialog.focus)
      xsdInfoDialog.focus();
  }

  function closeXSDInfoDialog()
  {
    if (!xsdInfoDialogClosed)
      xsdInfoDialog.close();
  }

  function synchronizeFragmentViews(viewID) {
    var form = document.getElementsByTagName("form");
    if (viewID == "<%=FragmentConstants.FRAGMENT_VIEW_SWITCH_FORM_TO_SOURCE%>")
      form[0].appendChild(createHiddenElement("<%=FragmentConstants.FRAGMENT_VIEW_ID%>","<%=FragmentConstants.FRAGMENT_VIEW_SWITCH_FORM_TO_SOURCE%>"));
    else
      form[0].appendChild(createHiddenElement("<%=FragmentConstants.FRAGMENT_VIEW_ID%>","<%=FragmentConstants.FRAGMENT_VIEW_SWITCH_SOURCE_TO_FORM%>"));
    form[0].action = "<%=response.encodeURL(controller.getPathWithContext("wsdl/actions/SynchronizeFragmentViewsActionJSP.jsp"))%>";
    form[0].submit();
  }
  
  function addSimpleAtomicDateTimeRows(calendarType,tableContainerID,id,name,value,maxOccurs) {
    if (checkMaxOccursReached(tableContainerID, maxOccurs))
      return;
    twistOpen(tableContainerID);
    var table = getTable(tableContainerID);
    var tableBody = table.getElementsByTagName("tbody").item(0);
    var newRow = document.createElement("tr");
    var column0 = document.createElement("td");
    var column1 = document.createElement("td");
    var column2 = document.createElement("td");

    var rowCheckbox = createRowCheckbox();
    column0.appendChild(rowCheckbox);

    var input = document.createElement("input");
    input.type = "text";
    input.id = id;
    input.name = name;
    if (value != null && value.length > 0)
      input.value = value;
    input.className = "tabletextenter";
    column1.appendChild(input);
    
    column2.appendChild(createDateTimeBrowseLink(calendarType,name,table.rows.length-1));
    column2.width = "90%";

    column0.className = "checkboxcells";
    column1.className = "tablecells";
    column2.className = "tablecells";
    newRow.appendChild(column0);
    newRow.appendChild(column1);
    newRow.appendChild(column2);
    tableBody.appendChild(newRow);
  }  
  
  function createDateTimeBrowseLink(calendarType,name,position)
  {
    var link = document.createElement("a");
    setDateTimeBrowseLinkHref(link,calendarType,name,position);
    link.appendChild(document.createTextNode("<%=HTMLUtils.JSMangle(controller.getMessage("FORM_LINK_BROWSE"))%>"));
    return link;
  }
  
  function setDateTimeBrowseLinkHref(link,calendarType,name,position)
  {
    link.href = "javascript:openCalendarBrowser("+calendarType+",'contentborder','"+name+"',"+position+")";
    return link;
  }
</script>
