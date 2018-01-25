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
                                                        org.eclipse.wst.ws.internal.explorer.platform.wsdl.constants.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.wsdl.datamodel.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.wsdl.actions.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.perspective.*,
                                                        org.eclipse.xsd.*" %>

<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<jsp:useBean id="fragID" class="java.lang.StringBuffer" scope="request"/>
<jsp:useBean id="nodeID" class="java.lang.StringBuffer" scope="request"/>
<jsp:useBean id="elementID" class="java.lang.StringBuffer" scope="request"/>

<script language="javascript">
  function choose(select) {
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

<%
WSDLPerspective wsdlPerspective = controller.getWSDLPerspective();
Node selectedNode = wsdlPerspective.getNodeManager().getNode(Integer.parseInt(nodeID.toString()));
WSDLOperationElement operElement = (WSDLOperationElement)selectedNode.getTreeElement();
IXSDGroupChoiceFragment frag = (IXSDGroupChoiceFragment)operElement.getFragmentByID(fragID.toString());
IXSDElementFragment elementFragment = (IXSDElementFragment)operElement.getFragmentByID(elementID.toString());
XSDToFragmentConfiguration xsdConfig = frag.getXSDToFragmentConfiguration();
String tableContainerID = (new StringBuffer(FragmentConstants.TABLE_ID)).append(frag.getID()).toString();
String twistImageName = (new StringBuffer("x")).append(tableContainerID).toString();
String nameAnchorID = (new StringBuffer(FragmentConstants.NAME_ANCHOR_ID)).append(frag.getID()).toString();
%>
<a name="<%=nameAnchorID%>"/>
<table width="95%" border=0 cellpadding=3 cellspacing=0>
  <tr>
    <td height=25 valign="bottom" align="left" nowrap width=11>
      <a href="javascript:twist('<%=tableContainerID%>','<%=twistImageName%>')"><img name="<%=twistImageName%>" src="<%=response.encodeURL(controller.getPathWithContext("images/twistopened.gif"))%>" alt="<%=controller.getMessage("ALT_TWIST_OPENED")%>" class="twist"></a>
    </td>
    <td class="labels" height=25 valign="bottom" align="left" nowrap>
      <a href="javascript:createInstance('<%=tableContainerID%>', <%=xsdConfig.getMaxOccurs()%>, '<%=fragID%>', '<%=nameAnchorID%>')"><%=wsdlPerspective.getMessage("FORM_LINK_ADD")%></a>
    </td>
    <% 
      if(elementFragment != null && elementFragment.isNillable()){
        if(elementFragment.isNil()){
          %>  
          <td width=10><input type="checkbox" name="<%=((IXSDElementFragment)elementFragment).getNilID()%>" value="<%=IXSDElementFragment.NIL_VALUE%>" checked><%=wsdlPerspective.getMessage("ALT_NIL")%></td> 
          <%
        } 
        else{
          %>  
          <td width=10><input type="checkbox" name="<%=((IXSDElementFragment)elementFragment).getNilID()%>" value="<%=IXSDElementFragment.NIL_VALUE%>" ><%=wsdlPerspective.getMessage("ALT_NIL")%></td> 
          <%
        }
      }
    %>
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
    <th class="headercolor" nowrap><%=wsdlPerspective.getMessage("FORM_LABEL_CHOICES")%></th>
    <th class="headercolor" width="100%" nowrap><%=wsdlPerspective.getMessage("FORM_LABEL_ELEMENTS")%></th>
  </tr>
<%
  XSDParticle[] choices = frag.getChoices();
  String[] groupIDs = frag.getGroupIDs();
  for (int i = 0; i < groupIDs.length || i < xsdConfig.getMinOccurs(); i++) {
    String groupID;
    int choiceIndex;
    if (i < groupIDs.length) {
      groupID = groupIDs[i];
      choiceIndex = frag.getChoiceIndex(groupID);
    }
    else {
      groupID = frag.createGroupChoiceInstance(0);
      choiceIndex = 0;
    }
    IXSDFragment[] choiceFrags = frag.getGroupMemberFragments(groupID);
%>
    <tr>
      <td class="checkboxcells" width=10>
        <input type="checkbox" onClick="handleRowCheckboxClick()" title="<%=controller.getMessage("FORM_CONTROL_TITLE_SELECT_ROW_CHECK_BOX")%>">
      </td>
      <td class="tablecells">
        <input type="hidden" name="<%=frag.getID()%>" value="<%=groupID%>">
        <select id="<%=groupID%>" name="<%=groupID%>" onChange="javascript:choose(this)" title="<%=wsdlPerspective.getMessage("FORM_CONTROL_TITLE_SELECT_CHOICES")%>">
<%
          for (int j = 0; j < choiceFrags.length; j++) {
            XSDElementDeclaration xsdElement = (XSDElementDeclaration)choiceFrags[j].getXSDToFragmentConfiguration().getXSDComponent();
            if (j == choiceIndex) {
%>
              <option value="<%=choiceFrags[j].getID()%>" selected><%=xsdElement.getQName()%>
<%
            }
            else {
%>
              <option value="<%=choiceFrags[j].getID()%>"><%=xsdElement.getQName()%>
<%
            }
          }
%>
        </select>
      </td>
      <td class="tablecells">
<%
        for (int j = 0; j < choiceFrags.length; j++) {
          fragID.delete(0, fragID.length());
          fragID.append(choiceFrags[j].getID());
          String choiceFragTableContainerID = (new StringBuffer(FragmentConstants.TABLE_ID)).append(choiceFrags[j].getID()).toString();
%>
          <span id="<%=choiceFragTableContainerID%>">
            <jsp:include page="<%=choiceFrags[j].getWriteFragment()%>" flush="true"/>
          </span>
          <script language="javascript">
            showTable('<%=choiceFragTableContainerID%>', <%=(j == choiceIndex)%>);
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
