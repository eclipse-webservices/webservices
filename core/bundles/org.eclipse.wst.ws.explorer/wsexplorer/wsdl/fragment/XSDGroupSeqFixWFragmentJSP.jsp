<%
/*******************************************************************************
 * Copyright (c) 2002, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
%>
<%@ page contentType="text/html; charset=UTF-8" import="org.eclipse.wst.ws.internal.explorer.platform.wsdl.perspective.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.wsdl.datamodel.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.perspective.*" %>

<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<jsp:useBean id="fragID" class="java.lang.StringBuffer" scope="request"/>
<jsp:useBean id="nodeID" class="java.lang.StringBuffer" scope="request"/>

<%
WSDLPerspective wsdlPerspective = controller.getWSDLPerspective();
Node selectedNode = wsdlPerspective.getNodeManager().getNode(Integer.parseInt(nodeID.toString()));
WSDLOperationElement operElement = (WSDLOperationElement)selectedNode.getTreeElement();
IXSDGroupSeqFragment frag = (IXSDGroupSeqFragment)operElement.getFragmentByID(fragID.toString());
XSDToFragmentConfiguration xsdConfig = frag.getXSDToFragmentConfiguration();
String[] groupIDs = frag.getGroupIDs();
for (int i = 0; i < xsdConfig.getMaxOccurs(); i++) {
  String groupID;
  if (i < groupIDs.length)
    groupID = groupIDs[i];
  else
    groupID = frag.createGroupSeqInstance();
  IXSDFragment[] groupMemberFragments = frag.getGroupMemberFragments(groupID);
%>
  <input type="hidden" name="<%=frag.getID()%>" value="<%=groupID%>">
  <table cellpadding=0 cellspacing=0 class="<%=(xsdConfig.getIsWSDLPart() ? "fixfragtable" : "innerfixfragtable")%>">
<%
  for (int j = 0; j < groupMemberFragments.length; j++) {
    fragID.delete(0, fragID.length());
    fragID.append(groupMemberFragments[j].getID());
%>
    <tr>
      <td>
        <jsp:include page="<%=groupMemberFragments[j].getWriteFragment()%>" flush="true"/>
      </td>
    </tr>
<%
  }
%>
  </table>
<%
}
%>
