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
<%@ page contentType="text/html; charset=UTF-8" %>

<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<jsp:include page="/scripts/formsubmit.jsp" flush="true"/>
<jsp:include page="/favorites/scripts/favoritesTable.jsp" flush="true"/>
<script language="javascript">
    function isSomethingSelected(tableContainerId) {
        var table = getTable(tableContainerId);
        for (var i=numberOfHeaderRows; i<table.rows.length; i++) {
            var rowCheckboxCell = table.rows[i].getElementsByTagName("td").item(0);
            var rowCheckbox = rowCheckboxCell.childNodes[0];
            if (rowCheckbox.checked)
                return true;
        }
        alert("<%=controller.getFavoritesPerspective().getMessage("MSG_ERROR_NOTHING_SELECTED")%>");
        return false;
    }

    function setFormLocationAndSubmit(tableContainerId, form, location) {
        if (isSomethingSelected(tableContainerId))
            setLocationAndSubmit(form, location);
    }

    function setLocationAndSubmit(form, location) {
        if (handleSubmit(form))
        {
          form.action = location;
          form.submit();
        }
    }
</script>
