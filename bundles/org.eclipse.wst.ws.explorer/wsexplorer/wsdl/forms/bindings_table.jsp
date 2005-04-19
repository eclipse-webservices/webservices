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
                                                        org.eclipse.wst.ws.internal.explorer.platform.wsdl.actions.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.perspective.*,
                                                        javax.wsdl.*,
                                                        java.util.*" %>

<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<jsp:useBean id="sectionHeaderInfo" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.SectionHeaderInfo" scope="request"/>
<%
   WSDLPerspective wsdlPerspective = controller.getWSDLPerspective();
   String tableContainerId = sectionHeaderInfo.getContainerId();
   StringBuffer twistImageName = new StringBuffer("x");
   twistImageName.append(tableContainerId);
%>
<table border=0 cellpadding=6 cellspacing=0>
  <tr>
    <td height=20 valign="bottom" align="left" nowrap width=11><a href="javascript:twist('<%=tableContainerId%>','<%=twistImageName.toString()%>')"><img name="<%=twistImageName.toString()%>" src="<%=response.encodeURL(controller.getPathWithContext("images/twistclosed.gif"))%>" alt="<%=controller.getMessage("ALT_TWIST_CLOSED")%>" class="twist"></a></td>
    <td height=20 valign="bottom" align="left" nowrap class="labels"><strong><%=wsdlPerspective.getMessage("FORM_LABEL_BINDINGS")%></strong></td>
  </tr>
</table>

<table width="95%" border=0 cellpadding=0 cellspacing=0>
  <tr>
    <td valign="top" height=10><img src="<%=response.encodeURL(controller.getPathWithContext("images/keyline.gif"))%>" height=2 width="100%"></td>
  </tr>
</table>

<div id="<%=tableContainerId%>" style="display:none;">
  <table width="95%" cellpadding=3 cellspacing=0 class="tableborder">
    <tr>
      <th class="headercolor" width="20%"><%=wsdlPerspective.getMessage("FORM_LABEL_NAME")%></th>
      <th class="headercolor" width="20%"><%=wsdlPerspective.getMessage("FORM_LABEL_BINDING_TYPE")%></th>
      <th class="headercolor"><%=wsdlPerspective.getMessage("FORM_LABEL_DOCUMENTATION")%></th>
    </tr>
<%
   Vector bindingNodes = (Vector)sectionHeaderInfo.getOtherProperties();
   for (int i=0;i<bindingNodes.size();i++)
   {
     Node bindingNode = (Node)bindingNodes.elementAt(i);
     WSDLBindingElement wsdlBindingElement = (WSDLBindingElement)bindingNode.getTreeElement();
     Binding binding = wsdlBindingElement.getBinding();
%>
    <tr>
      <td class="tablecells"><a href="<%=response.encodeURL(controller.getPathWithContext(SelectWSDLNavigatorNodeAction.getActionLink(bindingNode.getNodeId(),false)))%>"><%=binding.getQName().getLocalPart()%></td>
      <td class="tablecells"><%=wsdlPerspective.getBindingTypeString(wsdlBindingElement.getBindingType())%></td>
<%
     WSDLServiceElement wsdlServiceElement = (WSDLServiceElement)wsdlBindingElement.getParentElement();
     String documentation = wsdlServiceElement.getPropertyAsString(WSDLModelConstants.PROP_DOCUMENTATION);
     if (documentation.length() < 1)
       documentation = controller.getMessage("TABLE_BLANK_PLACEHOLDER");     
%>
      <td class="tablecells"><%=documentation%></td>
    </tr>
<%
   }
%>
  </table>
</div>
