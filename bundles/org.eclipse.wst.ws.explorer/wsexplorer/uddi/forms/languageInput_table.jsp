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
                                                        org.eclipse.wst.ws.internal.explorer.platform.util.*,
                                                        org.uddi4j.util.*" %>

<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<jsp:useBean id="sectionHeaderInfo" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.SectionHeaderInfo" scope="request"/>
<%
   UDDIPerspective uddiPerspective = controller.getUDDIPerspective();
   String tableContainerId = sectionHeaderInfo.getContainerId();
   String[] otherProperties = (String[])sectionHeaderInfo.getOtherProperties();
   String[] titleKeys = {otherProperties[0],otherProperties[1]};
   boolean hasErrors = otherProperties[2].equals(Boolean.TRUE.toString());
   boolean isFind = tableContainerId.startsWith("find");
   StringBuffer twistImageName = new StringBuffer("x");
   twistImageName.append(tableContainerId);
%>
<table border=0 cellpadding=6 cellspacing=0>
  <tr>
    <td height=40 valign="bottom" align="left" nowrap width=11><a href="javascript:twist('<%=tableContainerId%>','<%=twistImageName.toString()%>')"><img name="<%=twistImageName.toString()%>" src="<%=response.encodeURL(controller.getPathWithContext("images/twistclosed.gif"))%>" alt="<%=controller.getMessage("ALT_TWIST_CLOSED")%>" class="twist"></a></td>
    <td height=40 valign="bottom" align="left" nowrap class="labels">
      <strong><%=uddiPerspective.getMessage(titleKeys[0])%></strong>
<%
   if (hasErrors)
   {
%>
      <%=HTMLUtils.redAsterisk()%>
<%
   }
%>
    </td>
    <td height=40 valign="bottom" align="left" nowrap class="labels">
<%
   String textControlTitleKey;
   if (titleKeys[0].equals("FORM_LABEL_NAMES"))
     textControlTitleKey = "FORM_CONTROL_TITLE_NAME_TEXT_VALUE";
   else
     textControlTitleKey = "FORM_CONTROL_TITLE_DESCRIPTION_TEXT_VALUE";
%>       
      <a href="javascript:addLanguageInputRow('<%=tableContainerId%>','<%=HTMLUtils.JSMangle(uddiPerspective.getMessage(textControlTitleKey))%>')">
        <%=uddiPerspective.getMessage("FORM_LINK_ADD")%>
      </a>
    </td>
    <td height=40 valign="bottom" align="left" nowrap class="labels">
      <a href="javascript:removeSelectedRows('<%=tableContainerId%>')"><%=uddiPerspective.getMessage("FORM_LINK_REMOVE")%></a>
    </td>
    <td nowrap width="90%">&nbsp;</td>
  </tr>
</table>

<table width="95%" border=0 cellpadding=0 cellspacing=0>
  <tr>
    <td valign="top" height=10><img src="<%=response.encodeURL(controller.getPathWithContext("images/keyline.gif"))%>" alt="" height=2 width="100%"></td>
  </tr>
</table>

<div id="<%=tableContainerId%>" style="display:none;">
  <table width="95%" cellpadding=3 cellspacing=0 class="tableborder">
    <tr>
      <th class="checkboxcells" width=10><input type="checkbox" onClick="handleCheckAllClick('<%=tableContainerId%>',this)" title="<%=controller.getMessage("FORM_CONTROL_TITLE_SELECT_ALL_CHECK_BOX")%>"></th>
      <th class="headercolor"><%=uddiPerspective.getMessage("FORM_LABEL_LANGUAGE")%></th>
      <th class="headercolor"><%=uddiPerspective.getMessage(titleKeys[1])%></th>
    </tr>
  </table>
<% 
   if (isFind)
   {// condition to turn off when publishing to come.
%>     
<jsp:include page="/uddi/forms/names_findQualifiers_table.jsp" flush="true"/>
<%
   }
%>     
</div>
