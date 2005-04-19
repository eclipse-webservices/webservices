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
<%@ page contentType="text/html; charset=UTF-8" import="org.eclipse.wst.ws.internal.explorer.platform.wsil.constants.*,
                                                                                          org.eclipse.wst.ws.internal.explorer.platform.constants.*"%>

<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<jsp:include page="/scripts/tables.jsp" flush="true"/>
<script language="javascript" src="<%=response.encodeURL(controller.getPathWithContext("scripts/browserdetect.js"))%>">
</script>
<script language="javascript">

    function wsilHandleCheckAllClick(tableContainerId, checkAllCheckbox) {
        var table = getTable(tableContainerId);
        for (var i=numberOfHeaderRows; i<table.rows.length; i++) {
            var rowCheckboxCell = table.rows[i].getElementsByTagName("td").item(0);
            var rowCheckbox = rowCheckboxCell.childNodes[0];
            rowCheckbox.checked = checkAllCheckbox.checked;
            if (rowCheckbox.checked) {
                highlightRow(table.rows[i],"rowcolor");
                rowCheckbox.name = "<%=ActionInputs.VIEWID%>";
            }
            else {
                highlightRow(table.rows[i],"tablecells");
                rowCheckbox.name = "";
            }
        }
    }

    function validateCheckBoxInput(checkbox, isChecked) {
        if (isChecked) {
            checkbox.name = "<%=ActionInputs.VIEWID%>";
        }
        else {
            checkbox.name = "";
        }
        handleRowCheckboxClick();
    }

    function twistInit(tableContainerId,twistImageName) {
        var tableContainer = document.getElementById(tableContainerId);
        var table = getTable(tableContainerId);
        if (table.rows.length > numberOfHeaderRows) {
            tableContainer.style.display = "none";
            twist(tableContainerId, twistImageName);
        }
        else {
            tableContainer.style.display = "";
            twist(tableContainerId, twistImageName);
        }
    }

    function addKeyValueToRow(row, key, value) {
        var keyText = document.createTextNode(key);
        var valueText = document.createTextNode(value);
        var newCol = document.createElement("td");
        var newCol2 = document.createElement("td");
        newCol.appendChild(keyText);
        newCol.className = "tablecells";
        newCol2.appendChild(valueText);
        newCol2.className = "tablecells";
        row.appendChild(newCol);
        row.appendChild(newCol2);
    }

    function clearTable(tableBody, numHeader) {
        for(var i = numHeader; i < tableBody.rows.length; i++) {
            tableBody.deleteRow(i);
            i--;
        }
    }

</script>
