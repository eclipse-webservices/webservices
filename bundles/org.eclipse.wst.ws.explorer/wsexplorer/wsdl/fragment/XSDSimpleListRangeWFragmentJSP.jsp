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
                                                        org.eclipse.wst.ws.internal.explorer.platform.wsdl.actions.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.perspective.*" %>

<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<jsp:useBean id="fragID" class="java.lang.StringBuffer" scope="request"/>
<jsp:useBean id="nodeID" class="java.lang.StringBuffer" scope="request"/>

<%
WSDLPerspective wsdlPerspective = controller.getWSDLPerspective();
Node selectedNode = wsdlPerspective.getNodeManager().getNode(Integer.parseInt(nodeID.toString()));
WSDLOperationElement operElement = (WSDLOperationElement)selectedNode.getTreeElement();
IXSDSimpleListFragment frag = (IXSDSimpleListFragment)operElement.getFragmentByID(fragID.toString());
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
    <th class="headercolor"><%=wsdlPerspective.getMessage("FORM_LABEL_LIST_ITEMS")%></th>
  </tr>
  <%
  IXSDFragment[] childFrags = frag.getAllFragments();
  for (int i = 0; i < childFrags.length || i < xsdConfig.getMinOccurs(); i++) {
    IXSDFragment childFrag;
    String childFragID;
    if (i < childFrags.length) {
      childFrag = childFrags[i];
      childFragID = childFrag.getID();
    }
    else {
      childFragID = frag.createListInstance();
      childFrag = frag.getFragment(childFragID);
    }
    fragID.delete(0, fragID.length());
    fragID.append(childFragID);
  %>
  <tr>
    <td class="checkboxcells" width=10>
      <input type="checkbox" onClick="handleRowCheckboxClick()" title="<%=controller.getMessage("FORM_CONTROL_TITLE_SELECT_ROW_CHECK_BOX")%>">
    </td>
    <td class="tablecells">
      <input type="hidden" name="<%=frag.getID()%>" value="<%=childFragID%>">
      <jsp:include page="<%=childFrag.getWriteFragment()%>" flush="true"/>
    </td>
  </tr>
  <%
  }
  %>
</table>
</span>
