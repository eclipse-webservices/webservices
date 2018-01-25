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
                                                        org.eclipse.wst.ws.internal.explorer.platform.perspective.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.util.*" %>

<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<jsp:useBean id="fragID" class="java.lang.StringBuffer" scope="request"/>
<jsp:useBean id="nodeID" class="java.lang.StringBuffer" scope="request"/>

<%
WSDLPerspective wsdlPerspective = controller.getWSDLPerspective();
Node selectedNode = wsdlPerspective.getNodeManager().getNode(Integer.parseInt(nodeID.toString()));
WSDLOperationElement operElement = (WSDLOperationElement)selectedNode.getTreeElement();
IXSDFragment frag = operElement.getFragmentByID(fragID.toString());
XSDToFragmentConfiguration xsdConfig = frag.getXSDToFragmentConfiguration();
int minOccurs = xsdConfig.getMinOccurs();
int maxOccurs = xsdConfig.getMaxOccurs();
if (minOccurs == maxOccurs) {
%>
<input type="hidden" name="<%=frag.getID()%>" value="<%=maxOccurs%>"/>
<%
} else {
  String[] params = frag.getParameterValues(frag.getID());
%>
  <table cellpadding=3 cellspacing=0 class="<%=(xsdConfig.getIsWSDLPart() ? "fixfragtable" : "innerfixfragtable")%>">
    <tr>
      <td>
        <label for="<%=frag.getID()%>"><%=wsdlPerspective.getMessage("FORM_LABEL_OCCURANCE")%>&nbsp;<a href="javascript:openXSDInfoDialog('<%=response.encodeURL(controller.getPathWithContext(OpenXSDInfoDialogAction.getActionLink(session.getId(),selectedNode.getNodeId(),fragID.toString())))%>')"><%=frag.getName()%></a></label>
      </td>
    </tr>
    <tr>
      <td>
      <%
      if (maxOccurs != FragmentConstants.UNBOUNDED) {
      %>
        <select id="<%=frag.getID()%>" name="<%=frag.getID()%>" class="selectlist">
        <%
        for (int i = minOccurs; i <= maxOccurs; i++) {
        %>
          <option value="<%=i%>" <% if (params != null && i == params.length) { %>selected<% } %>><%=i%>
        <%
        }
        %>
        </select>
      <%
      } else {
      %>
        <input type="text" id="<%=frag.getID()%>" name="<%=frag.getID()%>" value="<%=((params != null) ? String.valueOf(params.length) : "")%>" class="tabletextenter">
      <%
        if (!frag.validateParameterValues(frag.getID())) {
        %>
        <%=HTMLUtils.redAsterisk()%>
        <%
        }
      }
      %>
      </td>
    </tr>
  </table>
<%
}
%>
