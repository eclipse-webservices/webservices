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
<%@ page contentType="text/html; charset=UTF-8" import="org.eclipse.wst.ws.internal.explorer.platform.favorites.constants.*,
                                                                                          org.eclipse.wst.ws.internal.explorer.platform.constants.*"%>

<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<jsp:include page="/scripts/tables.jsp" flush="true"/>
<script language="javascript" src="<%=response.encodeURL(controller.getPathWithContext("scripts/browserdetect.js"))%>">
</script>
<script language="javascript">

    function favHandleCheckAllClick(tableContainerId, checkAllCheckbox) {
        var table = getTable(tableContainerId);
        for (var i=numberOfHeaderRows; i<table.rows.length; i++) {
            var rowCheckboxCell = table.rows[i].getElementsByTagName("td").item(0);
            var rowCheckbox = rowCheckboxCell.childNodes[0];
            rowCheckbox.checked = checkAllCheckbox.checked;
            if (rowCheckbox.checked) {
                highlightRow(table.rows[i],"rowcolor");
                rowCheckbox.name = "<%=FavoritesActionInputs.MASS_ACTION_NODE_ID%>";
            }
            else {
                highlightRow(table.rows[i],"tablecells");
                rowCheckbox.name = "";
            }
        }
    }

    function validateCheckBoxInput(checkbox, isChecked) {
        if (isChecked) {
            checkbox.name = "<%=FavoritesActionInputs.MASS_ACTION_NODE_ID%>";
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

</script>
