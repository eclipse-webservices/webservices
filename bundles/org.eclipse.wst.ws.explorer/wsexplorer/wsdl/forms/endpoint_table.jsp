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
<%@ page contentType="text/html; charset=UTF-8" import="org.eclipse.wst.ws.internal.explorer.platform.wsdl.perspective.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.wsdl.datamodel.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.wsdl.constants.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.constants.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.perspective.*" %>

<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<jsp:useBean id="sectionHeaderInfo" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.SectionHeaderInfo" scope="request"/>
<script language="javascript">
  function addEndpoint(tableContainerID, id, name, value, isRemovable)
  {
    twistOpen(tableContainerID);
    var table = getTable(tableContainerID);
    var tableBody = table.getElementsByTagName("tbody").item(0);
    var newRow = document.createElement("tr");
    var column0 = document.createElement("td");
    var column1 = document.createElement("td");

    var rowCheckbox = createRowCheckbox();
    if (!isRemovable)
    {
      rowCheckbox.disabled = true;
    }
    column0.appendChild(rowCheckbox);

    var endPoint;
    if (isRemovable)
    {
      endPoint = document.createElement("input");
      endPoint.type = "text";
      endPoint.id = id;
      endPoint.name = name;
      if (value != null)
        endPoint.value = value;
      endPoint.className = "tabletextenter";
    }
    else
      endPoint = document.createTextNode(value);
    column1.appendChild(endPoint);

    column0.className = "checkboxcells";
    column1.className = "tablecells";
    newRow.appendChild(column0);
    newRow.appendChild(column1);
    tableBody.appendChild(newRow);
  }

  function handleEndpointCheckAllClick(tableContainerId,checkAllCheckbox)
  {
    var table = getTable(tableContainerId);
    for (var i=numberOfHeaderRows;i<table.rows.length;i++)
    {
      var rowCheckboxCell = table.rows[i].getElementsByTagName("td").item(0);
      var rowCheckbox = rowCheckboxCell.childNodes[0];
      if (!rowCheckbox.disabled)
      {
        rowCheckbox.checked = checkAllCheckbox.checked;
        if (rowCheckbox.checked)
          highlightRow(table.rows[i],"rowcolor");
        else
          highlightRow(table.rows[i],"tablecells");
      }
    }
  }
</script>
<%
   WSDLPerspective wsdlPerspective = controller.getWSDLPerspective();
   String tableContainerID = sectionHeaderInfo.getContainerId();
   StringBuffer twistImageName = new StringBuffer("x");
   twistImageName.append(tableContainerID);   
   WSDLBindingElement bindingElement = (WSDLBindingElement)sectionHeaderInfo.getOtherProperties();
   String[] endpoints = bindingElement.getEndPoints();
   String addressLocation = ((WSDLServiceElement)bindingElement.getParentElement()).getAddressLocation(bindingElement.getBinding());   
%>
<table border=0 cellpadding=6 cellspacing=0>
  <tr>
    <td height=20 valign="bottom" align="left" nowrap width=11>
      <a href="javascript:twist('<%=tableContainerID%>','<%=twistImageName.toString()%>')"><img name="<%=twistImageName.toString()%>" src="<%=response.encodeURL(controller.getPathWithContext("images/twistclosed.gif"))%>" alt="<%=controller.getMessage("ALT_TWIST_CLOSED")%>" class="twist"></a>
    </td>
    <td height=20 valign="bottom" align="left" nowrap class="labels">
      <strong><%=wsdlPerspective.getMessage("FORM_LABEL_END_POINTS")%></strong>
    </td>
    <td class="labels" height=25 valign="bottom" align="left" nowrap>
      <a href="javascript:addEndpoint('<%=tableContainerID%>', '<%=WSDLActionInputs.END_POINT%>', '<%=WSDLActionInputs.END_POINT%>', '<%=addressLocation%>', true)"><%=wsdlPerspective.getMessage("FORM_LINK_ADD")%></a>
    </td>
    <td class="labels" height=25 valign="bottom" align="left" nowrap>
      <a href="javascript:removeSelectedRows('<%=tableContainerID%>')"><%=wsdlPerspective.getMessage("FORM_LINK_REMOVE")%></a>
    </td>
  </tr>
</table>

<table width="95%" border=0 cellpadding=0 cellspacing=0>
  <tr>
    <td valign="top" height=10><img src="<%=response.encodeURL(controller.getPathWithContext("images/keyline.gif"))%>" height=2 width="100%"></td>
  </tr>
</table>

<div id="<%=tableContainerID%>" style="display:none;">
  <table width="95%" cellpadding=3 cellspacing=0 class="tableborder">
    <tr>
      <th class="checkboxcells" width=10><input type="checkbox" onClick="handleEndpointCheckAllClick('<%=tableContainerID%>',this)" title="<%=controller.getMessage("FORM_CONTROL_TITLE_SELECT_ALL_CHECK_BOX")%>"></th>
      <th class="headercolor"><%=wsdlPerspective.getMessage("FORM_LABEL_END_POINTS")%></th>
    </tr>
  </table>
</div>
<script language="javascript">
<%
  boolean isDefaultAdded = false;
  for (int i = 0; i < endpoints.length; i++)
  {
    if (endpoints[i].equals(addressLocation) && !isDefaultAdded)
    {
%>
    addEndpoint('<%=tableContainerID%>', '<%=WSDLActionInputs.END_POINT%>', "", '<%=endpoints[i]%>', false);
    
<%
      isDefaultAdded = true;
    }
    else
    {
%>
    addEndpoint('<%=tableContainerID%>', '<%=WSDLActionInputs.END_POINT%>', '<%=WSDLActionInputs.END_POINT%>', '<%=endpoints[i]%>', true);
<%
    }
  }
%>
</script>
