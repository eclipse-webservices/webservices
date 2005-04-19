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
                                                        org.eclipse.wst.ws.internal.explorer.platform.wsdl.datamodel.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.wsdl.actions.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.perspective.*,
                                                        org.eclipse.xsd.*" %>

<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<jsp:useBean id="fragID" class="java.lang.StringBuffer" scope="request"/>
<jsp:useBean id="nodeID" class="java.lang.StringBuffer" scope="request"/>

<%
WSDLPerspective wsdlPerspective = controller.getWSDLPerspective();
Node selectedNode = wsdlPerspective.getNodeManager().getNode(Integer.parseInt(nodeID.toString()));
WSDLOperationElement operElement = (WSDLOperationElement)selectedNode.getTreeElement();
IXSDSimpleUnionFragment frag = (IXSDSimpleUnionFragment)operElement.getFragmentByID(fragID.toString());
XSDToFragmentConfiguration xsdConfig = frag.getXSDToFragmentConfiguration();
String tableContainerID = (new StringBuffer(FragmentConstants.TABLE_ID)).append(frag.getID()).toString();
String twistImageName = (new StringBuffer("x")).append(tableContainerID).toString();
String nameAnchorID = (new StringBuffer(FragmentConstants.NAME_ANCHOR_ID)).append(frag.getID()).toString();
%>
<script language="javascript">
  function changeUnionMemberType(select) {
    for (var i = 0; i < select.options.length; i++) {
      var tableContainerID = '<%=FragmentConstants.TABLE_ID%>';
      tableContainerID += select.options(i).value;
      showTable(tableContainerID, select.options(i).selected);
    }
  }

  function showTable(tableContainerID, show) {
    var tableContainer = document.getElementById(tableContainerID);
    if (show)
      tableContainer.style.display = "";
    else
      tableContainer.style.display = "none";
  }
</script>
<a name="<%=nameAnchorID%>"/>
<table width="95%" border=0 cellpadding=3 cellspacing=0>
  <tr>
    <td height=25 valign="bottom" align="left" nowrap width=11>
      <a href="javascript:twist('<%=tableContainerID%>','<%=twistImageName%>')"><img name="<%=twistImageName%>" src="<%=response.encodeURL(controller.getPathWithContext("images/twistopened.gif"))%>" alt="<%=controller.getMessage("ALT_TWIST_OPENED")%>" class="twist"></a>
    </td>
    <td class="labels" height=25 valign="bottom" align="left" nowrap>
      <a href="javascript:openXSDInfoDialog('<%=response.encodeURL(controller.getPathWithContext(OpenXSDInfoDialogAction.getActionLink(session.getId(),selectedNode.getNodeId(),fragID.toString())))%>')"><%=frag.getName()%></a>
    </td>
    <td class="labels" height=25 valign="bottom" align="left" nowrap>
      <a href="javascript:createInstance('<%=tableContainerID%>', <%=xsdConfig.getMaxOccurs()%>, '<%=fragID%>', '<%=nameAnchorID%>')"><%=wsdlPerspective.getMessage("FORM_LINK_ADD")%></a>
    </td>
    <td class="labels" height=25 valign="bottom" align="left" nowrap>
      <a href="javascript:checkMinOccursAndRemoveSelectedRows('<%=tableContainerID%>', <%=xsdConfig.getMinOccurs()%>)"><%=wsdlPerspective.getMessage("FORM_LINK_REMOVE")%></a>
    </td>
    <td nowrap width="90%">&nbsp;</td>
  </tr>
</table>
<span id="<%=tableContainerID%>">
<table cellpadding=3 cellspacing=0 class="<%=(xsdConfig.getIsWSDLPart() ? "rangefragtable" : "innerrangefragtable")%>">
  <tr>
    <th class="checkboxcells" width=10><input type="checkbox" onClick="handleCheckAllClick('<%=tableContainerID%>',this)" title="<%=controller.getMessage("FORM_CONTROL_TITLE_SELECT_ALL_CHECK_BOX")%>"></th>
    <th class="headercolor" nowrap><%=wsdlPerspective.getMessage("FORM_LABEL_UNION_TYPE")%></th>
    <th class="headercolor" width="100%" nowrap><%=wsdlPerspective.getMessage("FORM_LABEL_UNION_MEMBERS")%></th>
  </tr>
  <%
  XSDSimpleTypeDefinition[] memberTypes = frag.getMemberTypeDefinitions();
  IXSDFragment[] memberFrags = frag.getAllFragments();
  for (int i = 0; i < memberFrags.length || i < xsdConfig.getMinOccurs(); i++) {
    int typeSelectionIndex = 0;
    String[] newMemberFragIDs = new String[memberTypes.length];
    IXSDFragment[] newMemberFrags = new IXSDFragment[memberTypes.length];
    for (int j = 0; j < memberTypes.length; j++) {
      if (i < memberFrags.length && memberFrags[i].getXSDTypeDefinition().getQName().equals(memberTypes[j].getQName())) {
        newMemberFragIDs[j] = memberFrags[i].getID();
        newMemberFrags[j] = memberFrags[i];
        typeSelectionIndex = j;
      }
      else {
        newMemberFragIDs[j] = frag.createUnionInstance(j);
        newMemberFrags[j] = frag.getFragment(newMemberFragIDs[j]);
      }
    }
    %>
    <tr>
      <td class="checkboxcells" width=10>
        <input type="checkbox" onClick="handleRowCheckboxClick()" title="<%=controller.getMessage("FORM_CONTROL_TITLE_SELECT_ROW_CHECK_BOX")%>">
      </td>
      <td class="tablecells">
        <select id="<%=frag.getID()%>" name="<%=frag.getID()%>" onChange="javascript:changeUnionMemberType(this)" title="<%=wsdlPerspective.getMessage("FORM_CONTROL_TITLE_SELECT_UNION_TYPE")%>">
          <%
          for (int k = 0; k < memberTypes.length; k++) {
            if (k == typeSelectionIndex) {
              %>
              <option value="<%=newMemberFragIDs[k]%>" selected><%=memberTypes[k].getQName()%>
              <%
            }
            else {
              %>
              <option value="<%=newMemberFragIDs[k]%>"><%=memberTypes[k].getQName()%>
              <%
            }
          }
          %>
        </select>
      </td>
      <td class="tablecells">
        <%
        for (int l = 0; l < memberTypes.length; l++) {
          fragID.delete(0, fragID.length());
          fragID.append(newMemberFragIDs[l]);
          String memberTableContainerID = (new StringBuffer(FragmentConstants.TABLE_ID)).append(newMemberFrags[l].getID()).toString();
        %>
        <span id="<%=memberTableContainerID%>">
          <jsp:include page="<%=newMemberFrags[l].getWriteFragment()%>" flush="true"/>
        </span>
        <script language="javascript">
          showTable('<%=memberTableContainerID%>', <%=(l == typeSelectionIndex)%>);
        </script>
        <%
        }
        %>
      </td>
    </tr>
  <%
  }
  %>
</table>
</span>
