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
                                                                                          org.eclipse.wst.ws.internal.explorer.platform.wsil.perspective.WSILPerspective,
                                                                                          org.eclipse.wst.ws.internal.explorer.platform.util.HTMLUtils" %>

<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<jsp:useBean id="nameLangs" class="java.util.Vector" scope="request"/>
<jsp:useBean id="names" class="java.util.Vector" scope="request"/>

<jsp:include page="/wsil/scripts/wsilTable.jsp" flush="true"/>

<%
String nameTable = "nameTable";
String xnameTable = "xnameTable";
String showAllNameID = "showAllNameID";
WSILPerspective wsilPerspective =controller.getWSILPerspective();
%>

<script language="javascript">
    function showName(tableContainerID) {
        var table = getTable(tableContainerID);
        var tableBody = table.getElementsByTagName("TBODY").item(0);
        var hiddenInput = document.getElementById('<%=showAllNameID%>');
        <%
        for (int i = 0; i < names.size(); i++) {
        %>
           addRowToNameTable(tableBody,
               "<%=HTMLUtils.JSMangle((String)nameLangs.elementAt(i))%>",
               "<%=HTMLUtils.JSMangle((String)names.elementAt(i))%>");
        <%
        }
        %>
    }

    function addRowToNameTable(tableBody, lang, name) {
        var newRow = document.createElement("tr");
        addKeyValueToRow(newRow, lang, name);
        tableBody.appendChild(newRow);
    }
</script>

<table border=0 cellpadding=6 cellspacing=0>
    <tr>
        <td height=40 valign="bottom" align="left" nowrap width=11>
            <a href="javascript:twist('<%=nameTable%>','<%=xnameTable%>')"><img name="<%=xnameTable%>" src="<%=response.encodeURL(controller.getPathWithContext("images/twistclosed.gif"))%>" alt="<%=controller.getMessage("ALT_TWIST_CLOSED")%>" class="twist"></a>
        </td>
        <td height=40 valign="bottom" align="left" nowrap class="labels">
            <strong><%=wsilPerspective.getMessage("FORM_LABEL_NAME")%></strong>
        </td>
    </tr>
</table>

<table width="95%" border=0 cellpadding=0 cellspacing=0>
    <tr>
        <td valign="top" height=10><img src="<%=response.encodeURL(controller.getPathWithContext("images/keyline.gif"))%>" height=2 width="100%"></td>
    </tr>
</table>

<div id="<%=nameTable%>">
    <table width="95%" cellpadding=3 cellspacing=0 class="tableborder">
        <tr>
            <th class="headercolor"><%=wsilPerspective.getMessage("FORM_LABEL_LANGUAGE")%></th>
            <th class="headercolor" width="100%"><%=wsilPerspective.getMessage("FORM_LABEL_NAME")%></th>
        </tr>
    </table>
</div>

<script language="javascript">
    showName('<%=nameTable%>');
    twistInit('<%=nameTable%>','<%=xnameTable%>');
</script>
