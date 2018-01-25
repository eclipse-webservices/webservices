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
                                                        org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.util.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.wsdl.actions.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.perspective.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.util.*,
                                                        org.eclipse.xsd.*" %>

<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<jsp:useBean id="fragID" class="java.lang.StringBuffer" scope="request"/>
<jsp:useBean id="nodeID" class="java.lang.StringBuffer" scope="request"/>

<%
WSDLPerspective wsdlPerspective = controller.getWSDLPerspective();
Node operNode = wsdlPerspective.getNodeManager().getNode(Integer.parseInt(nodeID.toString()));
WSDLOperationElement operElement = (WSDLOperationElement)operNode.getTreeElement();
IXSDFragment frag = operElement.getFragmentByID(fragID.toString());
XSDToFragmentConfiguration xsdConfig = frag.getXSDToFragmentConfiguration();
XSDSimpleTypeDefinition simpleType = (XSDSimpleTypeDefinition)frag.getXSDTypeDefinition();
XSDTypeDefinition xsdBuiltInType = XSDTypeDefinitionUtil.resolveToXSDBuiltInTypeDefinition(simpleType);
String[] labelArgs = {frag.getName(), (xsdBuiltInType != null ? xsdBuiltInType.getName() : simpleType.getName())};
String[] params = frag.getParameterValues(frag.getID());
%>
<table cellpadding=3 cellspacing=0 class="<%=(xsdConfig.getIsWSDLPart() ? "fixfragtable" : "innerfixfragtable")%>">
  <tr>
    <th id="<%=frag.getID()%>" class="labels" height=25 valign="bottom" align="left" nowrap>
      <%=wsdlPerspective.getMessage("FORM_LABEL_ATOMIC_NAME_TYPE", labelArgs)%>
    </th>
    <td headers="<%=frag.getID()%>" class="labels" height=25 width="100%" valign="bottom" align="left" nowrap>
      <%=((params != null && params.length > 0) ? HTMLUtils.charactersToHTMLEntities(params[0]) : wsdlPerspective.getMessage("FORM_LABEL_NULL"))%>
    </td>
  </tr>
  <%
  if (params != null) {
    for (int i = 1; i < params.length; i++) {
    %>
      <tr>
        <td class="labels" height=25 valign="bottom" align="left" nowrap></td>
        <td headers="<%=frag.getID()%>" class="labels" height=25 width="100%" valign="bottom" align="left" nowrap>
          <%=HTMLUtils.charactersToHTMLEntities(params[i])%>
        </td>
      </tr>
    <%
    }
  }
  %>
</table>
