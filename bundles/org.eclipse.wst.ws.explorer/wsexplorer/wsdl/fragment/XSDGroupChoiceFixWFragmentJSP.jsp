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
XSDToFragmentConfiguration xsdConfig = frag.getXSDToFragmentConfiguration();
%>
<table cellpadding=3 cellspacing=0 class="<%=(xsdConfig.getIsWSDLPart() ? "rangefragtable" : "innerrangefragtable")%>">
  <tr>
    <th class="headercolor" nowrap><%=wsdlPerspective.getMessage("FORM_LABEL_CHOICES")%></th>
    <th class="headercolor" width="100%" nowrap><%=wsdlPerspective.getMessage("FORM_LABEL_ELEMENTS")%></th>
  </tr>
<%
  XSDParticle[] choices = frag.getChoices();
  String[] groupIDs = frag.getGroupIDs();
  for (int i = 0; i < xsdConfig.getMaxOccurs(); i++) {
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
