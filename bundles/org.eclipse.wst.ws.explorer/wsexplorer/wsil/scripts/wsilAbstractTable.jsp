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
<jsp:useBean id="abstractLangs" class="java.util.Vector" scope="request"/>
<jsp:useBean id="abstracts" class="java.util.Vector" scope="request"/>

<jsp:include page="/wsil/scripts/wsilTable.jsp" flush="true"/>

<%
String abstractTable = "abstractTable";
String xabstractTable = "xabstractTable";
String showAllAbstractID = "showAllAbstractID";
WSILPerspective wsilPerspective =controller.getWSILPerspective();
%>

<script language="javascript">
    function showAbstract(tableContainerID) {
        var table = getTable(tableContainerID);
        var tableBody = table.getElementsByTagName("TBODY").item(0);
        var hiddenInput = document.getElementById('<%=showAllAbstractID%>');
        <%
        for (int i = 0; i < abstracts.size(); i++) {
        %>
           addRowToAbstractTable(tableBody,
               "<%=HTMLUtils.JSMangle((String)abstractLangs.elementAt(i))%>",
               "<%=HTMLUtils.JSMangle((String)abstracts.elementAt(i))%>");
        <%
        }
        %>
    }

    function addRowToAbstractTable(tableBody, lang, abst) {
        var newRow = document.createElement("tr");
        addKeyValueToRow(newRow, lang, abst);
        tableBody.appendChild(newRow);
    }
</script>

<table border=0 cellpadding=6 cellspacing=0>
    <tr>
        <td height=40 valign="bottom" align="left" nowrap width=11>
            <a href="javascript:twist('<%=abstractTable%>','<%=xabstractTable%>')"><img name="<%=xabstractTable%>" src="<%=response.encodeURL(controller.getPathWithContext("images/twistclosed.gif"))%>" alt="<%=controller.getMessage("ALT_TWIST_CLOSED")%>" class="twist"></a>
        </td>
        <td height=40 valign="bottom" align="left" nowrap class="labels">
            <strong><%=wsilPerspective.getMessage("FORM_LABEL_ABSTRACT")%></strong>
        </td>
    </tr>
</table>

<table width="95%" border=0 cellpadding=0 cellspacing=0>
    <tr>
        <td valign="top" height=10><img src="<%=response.encodeURL(controller.getPathWithContext("images/keyline.gif"))%>" alt="" height=2 width="100%"></td>
    </tr>
</table>

<div id="<%=abstractTable%>">
    <table width="95%" cellpadding=3 cellspacing=0 class="tableborder">
        <tr>
            <th class="headercolor"><%=wsilPerspective.getMessage("FORM_LABEL_LANGUAGE")%></th>
            <th class="headercolor" width="100%"><%=wsilPerspective.getMessage("FORM_LABEL_ABSTRACT")%></th>
        </tr>
    </table>
</div>

<script language="javascript">
    showAbstract('<%=abstractTable%>');
    twistInit('<%=abstractTable%>','<%=xabstractTable%>');
</script>
