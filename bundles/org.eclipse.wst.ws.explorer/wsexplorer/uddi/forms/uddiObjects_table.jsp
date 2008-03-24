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
                                                        org.eclipse.wst.ws.internal.explorer.platform.uddi.actions.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.uddi.constants.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.util.*" %>

<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<jsp:useBean id="sectionHeaderInfo" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.SectionHeaderInfo" scope="request"/>
<%
   UDDIPerspective uddiPerspective = controller.getUDDIPerspective();
   String tableContainerId = sectionHeaderInfo.getContainerId();
   String[] specificInfo = (String[])sectionHeaderInfo.getOtherProperties();
   String tableTitleKey = specificInfo[0];
   boolean hasErrors = specificInfo[1].equals(Boolean.TRUE.toString());
   String queryItemString = specificInfo[2];
   String parentContainerId = specificInfo[3];
   String transferTargetKey = specificInfo[4];
   StringBuffer twistImageName = new StringBuffer("x");
   twistImageName.append(tableContainerId);
%>
<table width="95%" border=0 cellpadding=6 cellspacing=0>
  <tr>
    <td height=40 valign="bottom" align="left" nowrap width=11><a href="javascript:twist('<%=tableContainerId%>','<%=twistImageName.toString()%>')"><img name="<%=twistImageName.toString()%>" src="<%=response.encodeURL(controller.getPathWithContext("images/twistclosed.gif"))%>" alt="<%=controller.getMessage("ALT_TWIST_CLOSED")%>" class="twist"></a></td>
    <td height=40 valign="bottom" align="left" nowrap class="labels">
      <strong><%=uddiPerspective.getMessage(tableTitleKey)%></strong>
<%
   if (hasErrors)
   {
%>
      <%=HTMLUtils.redAsterisk()%>
<%
   }
%>         
    </td>
<%
   if (sectionHeaderInfo.isDynamic())
   {
%>       
    <td height=40 valign="bottom" align="left" nowrap class="labels">
      <a href="javascript:initiateSubQuery('<%=parentContainerId%>','<%=transferTargetKey%>',<%=queryItemString%>,true)"><%=uddiPerspective.getMessage("FORM_LINK_GET")%></a>
    </td>
    <td height=40 valign="bottom" align="left" nowrap class="labels">
      <a href="javascript:initiateSubQuery('<%=parentContainerId%>','<%=transferTargetKey%>',<%=queryItemString%>,false)"><%=uddiPerspective.getMessage("FORM_LINK_FIND")%></a>
    </td>
    <td height=40 valign="bottom" align="left" nowrap class="labels">
      <a href="javascript:removeSelectedRows('<%=tableContainerId%>')"><%=uddiPerspective.getMessage("FORM_LINK_REMOVE")%></a>
    </td>
<%
   }
%>       
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
      <th class="headercolor"><%=uddiPerspective.getMessage("FORM_LABEL_NAME")%></th>
      <th class="headercolor"><%=uddiPerspective.getMessage("FORM_LABEL_DESCRIPTION")%></th>
    </tr>
  </table>
</div>
