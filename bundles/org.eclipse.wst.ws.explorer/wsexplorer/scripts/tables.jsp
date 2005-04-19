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
<%@ page contentType="text/html; charset=UTF-8" import="org.eclipse.wst.ws.internal.explorer.platform.util.*" %>

<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<script language="javascript">
  var numberOfHeaderRows = 1;
  function getTable(tableContainerId)
  {
    var container = document.getElementById(tableContainerId);
    return container.getElementsByTagName("table").item(0);
  }

  function setSelect(select,value)
  {
    for (var i=0;i<select.options.length;i++)
    {
      if (select.options[i].value == value)
      {
        select.selectedIndex = i;
        return select.options[i].text;
      }
    }
  }

  function removeSelectedRows(tableContainerId)
  {
    var table = getTable(tableContainerId);
    for (var i=numberOfHeaderRows;i<table.rows.length;i++)
    {
      var rowCheckbox = table.rows[i].getElementsByTagName("input").item(0);
      if (rowCheckbox.checked)
      {
        table.deleteRow(i);
        i--;
      }
    }
    var checkAllCheckboxRow = table.rows[0];
    var checkAllCheckbox = checkAllCheckboxRow.getElementsByTagName("input").item(0);
    if (checkAllCheckbox.checked)
      checkAllCheckbox.checked = false;
  }

  function handleRowCheckboxClick(e)
  {
    var checkbox;
    if (isMicrosoftInternetExplorer())
      checkbox = event.srcElement;
    else
      checkbox = e.target;
    var cell = checkbox.parentNode;
    var row = cell.parentNode;
    if (checkbox.checked)
      highlightRow(row,"rowcolor");
    else
      highlightRow(row,"tablecells");

    var tbody = row.parentNode;
    var table = tbody.parentNode;
    var checkAllCheckbox = table.rows[0].getElementsByTagName("input").item(0);
    if (checkAllCheckbox.checked)
      checkAllCheckbox.checked = false;
  }

  function createRowCheckbox()
  {
    var rowCheckbox = document.createElement("input");
    rowCheckbox.setAttribute("name",rowCheckboxName);
    rowCheckbox.type = "checkbox";
    rowCheckbox.title = "<%=HTMLUtils.JSMangle(controller.getMessage("FORM_CONTROL_TITLE_SELECT_ROW_CHECK_BOX"))%>";
    rowCheckbox.onclick = handleRowCheckboxClick;
    return rowCheckbox;
  }

  function handleCheckAllClick(tableContainerId,checkAllCheckbox)
  {
    var table = getTable(tableContainerId);
    for (var i=numberOfHeaderRows;i<table.rows.length;i++)
    {
      var rowCheckboxCell = table.rows[i].getElementsByTagName("td").item(0);
      var rowCheckbox = rowCheckboxCell.childNodes[0];
      rowCheckbox.checked = checkAllCheckbox.checked;
      if (rowCheckbox.checked)
        highlightRow(table.rows[i],"rowcolor");
      else
        highlightRow(table.rows[i],"tablecells");
    }
  }

  function highlightRow(row,className)
  {
    var rowColumns = row.getElementsByTagName("td");
    for (var i=1;i<rowColumns.length;i++)
      rowColumns.item(i).className = className;
  }

  function highlightErrantRow(row,className)
  {
    var rowColumns = row.getElementsByTagName("td");
    var column0 = rowColumns.item(0);
    var column0Control = column0.childNodes[0];
    var start;
    if (column0Control.type == "checkbox")
    {
      column0Control.checked = true;
      rowColumns.item(1).className = "firstcolumnerrantrowcolor";
      start = 2;
    }
    else
    {
      column0.className = "firstcolumnerrantrowcolor";
      start = 1;
    }
    rowColumns.item(rowColumns.length-1).className = "lastcolumnerrantrowcolor";
    for (var i=start;i<rowColumns.length-1;i++)
      rowColumns.item(i).className = "middleerrantrowcolor";
  }

  function twist(tableContainerId,twistImageName)
  {
    var tableContainer = document.getElementById(tableContainerId);
    var twistImage = document.images[twistImageName];
    if (tableContainer.style.display == "none")
    {
      tableContainer.style.display = "";
      twistImage.src = "<%=response.encodeURL(controller.getPathWithContext("images/twistopened.gif"))%>";
      twistImage.alt = "<%=HTMLUtils.JSMangle(controller.getMessage("ALT_TWIST_OPENED"))%>";
    }
    else
    {
      tableContainer.style.display = "none";
      twistImage.src = "<%=response.encodeURL(controller.getPathWithContext("images/twistclosed.gif"))%>";
      twistImage.alt = "<%=HTMLUtils.JSMangle(controller.getMessage("ALT_TWIST_CLOSED"))%>";
    }
  }

  function twistOpen(tableContainerId)
  {
    if (document.getElementById(tableContainerId).style.display == "none")
      twist(tableContainerId,"x"+tableContainerId);
  }

  function getDefaultDisplayString(inputValue)
  {
    if (inputValue == null || inputValue.length < 1)
      return "<%=HTMLUtils.JSMangle(controller.getMessage("TABLE_BLANK_PLACEHOLDER"))%>";
    return inputValue;
  }
</script>
