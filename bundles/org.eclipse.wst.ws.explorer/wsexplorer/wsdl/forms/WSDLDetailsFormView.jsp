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
                                                        org.eclipse.wst.ws.internal.explorer.platform.perspective.Node,
                                                        org.eclipse.wst.ws.internal.explorer.platform.perspective.*,
                                                        java.util.*" %>

<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<jsp:useBean id="sectionHeaderInfo" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.SectionHeaderInfo" scope="request"/>

<%
WSDLPerspective wsdlPerspective = controller.getWSDLPerspective();
%>
<table>
  <tr>
    <td class="labels"><%=wsdlPerspective.getMessage("FORM_LABEL_WSDL_DETAILS_DESC")%></td>
  </tr>
  <tr>
    <td height=20>&nbsp;</td>
  </tr>
</table>
<%
Node wsdlNode = wsdlPerspective.getNodeManager().getSelectedNode();
WSDLElement wsdlElement = (WSDLElement)wsdlNode.getTreeElement();
String documentation = wsdlElement.getPropertyAsString(WSDLModelConstants.PROP_DOCUMENTATION);
if (documentation != null && documentation.length() > 0)
{
%>
<table>
  <tr>
    <td height=20 valign="bottom" class="labels"><%=documentation%></td>
  </tr>
</table>
<%
}
%>
<table width="95%" cellpadding=3 cellspacing=0 class="tableborder">
  <tr>
    <th class="singleheadercolor"><%=controller.getMessage("FORM_LABEL_WSDL_URL")%></th>
  </tr>
  <tr>
    <td class="tablecells"><%=wsdlElement.getWsdlUrl()%></td>
  </tr>
</table>
<table>
  <tr>
    <td height=10>&nbsp;</td>
  </tr>
</table>
<%   
sectionHeaderInfo.clear();
sectionHeaderInfo.setContainerId("Services");
Vector serviceNodes = wsdlNode.getChildNodes();
sectionHeaderInfo.setOtherProperties(serviceNodes);
%>
<jsp:include page="/wsdl/forms/services_table.jsp" flush="true"/>
<table>
  <tr>
    <td height=10>&nbsp;</td>
  </tr>
</table>
<%   
Vector bindingNodes = new Vector();
for (int i=0;i<serviceNodes.size();i++)
{
  Node serviceNode = (Node)serviceNodes.elementAt(i);
  Vector serviceBindingNodes = serviceNode.getChildNodes();
  for (int j=0;j<serviceBindingNodes.size();j++)
  {
    Node serviceBindingNode = (Node)serviceBindingNodes.elementAt(j);
    bindingNodes.addElement(serviceBindingNode);
  }
}
sectionHeaderInfo.clear();
sectionHeaderInfo.setContainerId("Bindings");
sectionHeaderInfo.setOtherProperties(bindingNodes);
%>
<jsp:include page="/wsdl/forms/bindings_table.jsp" flush="true"/>
<script language="javascript">
<%
if (serviceNodes.size() > 0)
{
%>
  twist("Services","xServices");
<%
}
if (bindingNodes.size() > 0)
{
%>
  twist("Bindings","xBindings");
<%
}
%>
</script>
