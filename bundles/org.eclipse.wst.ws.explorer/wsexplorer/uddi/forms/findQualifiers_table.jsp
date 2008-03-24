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
                                                        org.uddi4j.util.*" %>

<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<jsp:useBean id="sectionHeaderInfo" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.SectionHeaderInfo" scope="request"/>
<%
   UDDIPerspective uddiPerspective = controller.getUDDIPerspective();
   String tableContainerId = sectionHeaderInfo.getContainerId();
   StringBuffer twistImageName = new StringBuffer("x");
   twistImageName.append(tableContainerId);   
%>
<table border=0 cellpadding=6 cellspacing=0>
  <tr>
    <td height=40 valign="bottom" align="left" nowrap width=11><a href="javascript:twist('<%=tableContainerId%>','<%=twistImageName.toString()%>')"><img name="<%=twistImageName.toString()%>" src="<%=response.encodeURL(controller.getPathWithContext("images/twistclosed.gif"))%>" alt="<%=controller.getMessage("ALT_TWIST_CLOSED")%>" class="twist"></a></td>
    <td height=40 valign="bottom" align="left" nowrap class="labels"><strong><%=uddiPerspective.getMessage("FORM_LABEL_FINDQUALIFIERS")%></strong></td>
  </tr>
</table>

<table width="95%" border=0 cellpadding=0 cellspacing=0>
  <tr>
    <td valign="top" height=10><img src="<%=response.encodeURL(controller.getPathWithContext("images/keyline.gif"))%>" alt="" height=2 width="100%"></td>
  </tr>
</table>

<div id="<%=tableContainerId%>" style="display:none;">
  <table>
    <tr>
      <td><label for="combine_keys"><%=uddiPerspective.getMessage("FORM_LABEL_COMBINE_KEYS")%></label></td>
      <td>
        <select id="combine_keys" class="selectlist">
          <option value="<%=FindQualifier.andAllKeys%>"><%=uddiPerspective.getMessage("FORM_OPTION_ANDALLKEYS")%>
          <option value="<%=FindQualifier.orAllKeys%>"><%=uddiPerspective.getMessage("FORM_OPTION_ORALLKEYS")%>
          <option value="<%=FindQualifier.orLikeKeys%>"><%=uddiPerspective.getMessage("FORM_OPTION_ORLIKEKEYS")%>
        </select>
      </td>
    </tr>
    <tr>
      <td><%=uddiPerspective.getMessage("FORM_LABEL_SORT_BY_NAME")%></td>
      <td>
        <input type="radio" id="advance_sort_by_name_asc" name="<%=UDDIActionInputs.QUERY_INPUT_ADVANCED_SORT_BY_NAME%>" value="<%=FindQualifier.sortByNameAsc%>" checked><label for="advance_sort_by_name_asc"><%=uddiPerspective.getMessage("FORM_RADIO_SORT_ASC")%></label>
        <input type="radio" id="advance_sort_by_name_desc" name="<%=UDDIActionInputs.QUERY_INPUT_ADVANCED_SORT_BY_NAME%>" value="<%=FindQualifier.sortByNameDesc%>"><label for="advance_sort_by_name_desc"><%=uddiPerspective.getMessage("FORM_RADIO_SORT_DESC")%></label>
      </td>
    </tr>
    <tr>
      <td><%=uddiPerspective.getMessage("FORM_LABEL_SORT_BY_DATE")%></td>
      <td>
        <input type="radio" id="advance_sort_by_date_asc" name="<%=UDDIActionInputs.QUERY_INPUT_ADVANCED_SORT_BY_DATE%>" value="<%=FindQualifier.sortByDateAsc%>" checked><label for="advance_sort_by_date_asc"><%=uddiPerspective.getMessage("FORM_RADIO_SORT_ASC")%></label>
        <input type="radio" id="advance_sort_by_date_desc" name="<%=UDDIActionInputs.QUERY_INPUT_ADVANCED_SORT_BY_DATE%>" value="<%=FindQualifier.sortByDateDesc%>"><label for="advance_sort_by_date_desc"><%=uddiPerspective.getMessage("FORM_RADIO_SORT_DESC")%></label>
      </td>
    </tr>
  </table>
</div>
