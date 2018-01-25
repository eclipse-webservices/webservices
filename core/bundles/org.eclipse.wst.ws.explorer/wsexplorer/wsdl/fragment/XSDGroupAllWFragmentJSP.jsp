<%
/*******************************************************************************
 * Copyright (c) 2002, 2004 IBM Corporation and others.
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
                                                        org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.perspective.*" %>

<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<jsp:useBean id="fragID" class="java.lang.StringBuffer" scope="request"/>
<jsp:useBean id="nodeID" class="java.lang.StringBuffer" scope="request"/>

<%
WSDLPerspective wsdlPerspective = controller.getWSDLPerspective();
Node selectedNode = wsdlPerspective.getNodeManager().getNode(Integer.parseInt(nodeID.toString()));
WSDLOperationElement operElement = (WSDLOperationElement)selectedNode.getTreeElement();
IXSDGroupAllFragment frag = (IXSDGroupAllFragment)operElement.getFragmentByID(fragID.toString());
XSDToFragmentConfiguration xsdConfig = frag.getXSDToFragmentConfiguration();
boolean enableGroup = (xsdConfig.getMinOccurs() > 0 || frag.getGroupIDs().length > 0);
String tableContainerID = (new StringBuffer(FragmentConstants.TABLE_ID)).append(frag.getID()).toString();
String twistImageName = (new StringBuffer("x")).append(tableContainerID).toString();
String groupID = frag.getGroupAllInstance();
String groupCheckboxID = (new StringBuffer(FragmentConstants.XSD_ALL_GROUP_ID)).append(frag.getID()).toString();
String groupMemberRadioID = (new StringBuffer(groupCheckboxID)).append(frag.getID()).toString();
%>
<script language="javascript">
  function initGroupAll(tableContainerID, twistImageName, fragID, groupID, groupCheckboxID, enable) {
    var checkbox = document.getElementById(groupCheckboxID);
    checkbox.checked = enable;
    enableGroupAll(tableContainerID, twistImageName, fragID, groupID, checkbox);
  }

  function enableGroupAll(tableContainerID, twistImageName, fragID, groupID, checkbox) {
    var tableContainer = document.getElementById(tableContainerID);
    var checkboxTable = checkbox.parentNode.parentNode.parentNode;
    if (checkbox.checked) {
      if (tableContainer.style.display == "none")
        twist(tableContainerID, twistImageName);
      checkbox.name = fragID;
      checkbox.value = groupID;
    }
    else {
      if (tableContainer.style.display == "")
        twist(tableContainerID, twistImageName);
      checkbox.name = "";
      checkbox.value = "";
    }
    var tds = checkboxTable.getElementsByTagName("td");
    var checkboxCellIndex = checkbox.parentNode.cellIndex;
    for (var i = 0; i < tds.length; i++) {
      if (i == checkboxCellIndex || checkbox.checked)
        tds.item(i).style.display = "";
      else
        tds.item(i).style.display = "none";
    }
  }

  function groupAllOrderingMoveUp(tableContainerID) {
    var table = getTable(tableContainerID);
    if (table.rows[numberOfHeaderRows].cells[0].getElementsByTagName("input").item(0).checked) {
      alert('<%=wsdlPerspective.getMessage("MSG_ERROR_CANNOT_MOVE_FIRST_ELEMENT_UP")%>');
      return;
    }
    for (var i = numberOfHeaderRows + 1; i < table.rows.length; i++) {
      if (table.rows[i].cells[0].getElementsByTagName("input").item(0).checked) {
        swapRows(table, i-1, i);
        table.rows[i-1].cells[0].getElementsByTagName("input").item(0).checked = true;
        return;
      }
    }
    alert('<%=controller.getWSDLPerspective().getMessage("MSG_ERROR_NOTHING_SELECTED")%>');
  }

  function groupAllOrderingMoveDown(tableContainerID) {
    var table = getTable(tableContainerID);
    if (table.rows[table.rows.length - 1].cells[0].getElementsByTagName("input").item(0).checked) {
      alert('<%=wsdlPerspective.getMessage("MSG_ERROR_CANNOT_MOVE_LAST_ELEMENT_DOWN")%>');
      return;
    }
    for (var i = numberOfHeaderRows; i < table.rows.length - 1; i++) {
      if (table.rows[i].cells[0].getElementsByTagName("input").item(0).checked) {
        swapRows(table, i, i+1);
        table.rows[i+1].cells[0].getElementsByTagName("input").item(0).checked = true;
        return;
      }
    }
    alert('<%=controller.getWSDLPerspective().getMessage("MSG_ERROR_NOTHING_SELECTED")%>');
  }

  <%
  // rowIndex1 must be smaller than rowIndex2 in order to work
  %>
  function swapRows(table, rowIndex1, rowIndex2) {
    var row1ChildNodes = cloneArray(table.rows[rowIndex1].childNodes);
    var row2ChildNodes = cloneArray(table.rows[rowIndex2].childNodes);
    table.deleteRow(rowIndex2);
    table.deleteRow(rowIndex1);
    var row1 = table.insertRow(rowIndex1);
    var row2 = table.insertRow(rowIndex2);
    appendToRow(row1, row2ChildNodes);
    appendToRow(row2, row1ChildNodes);
  }

  function cloneArray(oldArray) {
    var arrayClone = new Array();
    for (var i = 0; i < oldArray.length; i++) {
      arrayClone[i] = oldArray[i].cloneNode(true);
    }
    return arrayClone;
  }

  function appendToRow(row, nodesArray) {
    for (var i = 0; i < nodesArray.length; i++) {
      row.appendChild(nodesArray[i]);
    }
  }
</script>
<table width="95%" border=0 cellpadding=3 cellspacing=0>
  <tr>
    <td height=25 valign="bottom" align="left" nowrap width=11>
      <a href="javascript:twist('<%=tableContainerID%>','<%=twistImageName%>')"><img name="<%=twistImageName%>" src="<%=response.encodeURL(controller.getPathWithContext("images/twistopened.gif"))%>" alt="<%=controller.getMessage("ALT_TWIST_OPENED")%>" class="twist"></a>
    </td>
    <%
    if (xsdConfig.getMinOccurs() > 0) {
    %>
    <input type="hidden" name="<%=frag.getID()%>" value="<%=groupID%>">
    <%
    }
    else {
    %>
    <td class="labels" height=25 valign="bottom" align="left" nowrap>
      <input type="checkbox" id="<%=groupCheckboxID%>" name="<%=frag.getID()%>" value="<%=groupID%>" title="<%=wsdlPerspective.getMessage("FORM_CONTROL_TITLE_ENABLE_DISABLE_GROUP")%>" onClick="javascript:enableGroupAll('<%=tableContainerID%>', '<%=twistImageName%>', '<%=frag.getID()%>', '<%=groupID%>', this)" checked><label for="<%=groupCheckboxID%>"><%=wsdlPerspective.getMessage("FORM_LABEL_ENABLE_DISABLE_GROUP")%></label>
    </td>
    <%
    }
    %>
    <td class="labels" height=25 valign="bottom" align="left" nowrap>
      <a href="javascript:groupAllOrderingMoveUp('<%=tableContainerID%>')"><%=wsdlPerspective.getMessage("FORM_LABEL_GROUP_ALL_ORDERING_MOVE_UP")%></a>
    </td>
    <td class="labels" height=25 valign="bottom" align="left" nowrap>
      <a href="javascript:groupAllOrderingMoveDown('<%=tableContainerID%>')"><%=wsdlPerspective.getMessage("FORM_LABEL_GROUP_ALL_ORDERING_MOVE_DOWN")%></a>
    </td>
    <td nowrap width="90%">&nbsp;</td>
  </tr>
</table>
<span id="<%=tableContainerID%>">
<table cellpadding=3 cellspacing=0 class="<%=(xsdConfig.getIsWSDLPart() ? "rangefragtable" : "innerrangefragtable")%>">
  <tr>
    <th class="headercolor" width=10></th>
    <th class="headercolor"><%=wsdlPerspective.getMessage("FORM_LABEL_ELEMENTS")%></th>
  </tr>
  <%
  IXSDFragment[] groupMemberFragments = frag.getGroupMemberFragments(groupID);
  for (int i = 0; i < groupMemberFragments.length; i++) {
    fragID.delete(0, fragID.length());
    fragID.append(groupMemberFragments[i].getID());
  %>
  <tr>
    <td class="tablecells">
      <input type="radio" class="radio" name="<%=groupMemberRadioID%>" value="<%=fragID.toString()%>" title="<%=wsdlPerspective.getMessage("FORM_CONTROL_TITLE_SELECT_ELEMENT_IN_GROUP")%>">
    </td>
    <td class="tablecells" width="100%">
      <input type="hidden" name="<%=groupID%>" value="<%=fragID.toString()%>">
      <jsp:include page="<%=groupMemberFragments[i].getWriteFragment()%>" flush="true"/>
    </td>
  </tr>
  <%
  }
  %>
</table>
</span>
<%
if (!enableGroup) {
%>
  <script language="javascript">
    initGroupAll('<%=tableContainerID%>', '<%=twistImageName%>', '<%=frag.getID()%>', '<%=groupID%>', '<%=groupCheckboxID%>', <%=enableGroup%>);
  </script>
<%
}
%>
