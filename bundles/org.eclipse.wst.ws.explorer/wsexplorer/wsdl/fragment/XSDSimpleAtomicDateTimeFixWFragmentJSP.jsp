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
                                                        org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.util.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.impl.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.wsdl.actions.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.perspective.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.constants.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.util.*,
                                                        org.eclipse.xsd.*" %>

<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<jsp:useBean id="fragID" class="java.lang.StringBuffer" scope="request"/>
<jsp:useBean id="nodeID" class="java.lang.StringBuffer" scope="request"/>

<%
WSDLPerspective wsdlPerspective = controller.getWSDLPerspective();
Node selectedNode = wsdlPerspective.getNodeManager().getNode(Integer.parseInt(nodeID.toString()));
WSDLOperationElement operElement = (WSDLOperationElement)selectedNode.getTreeElement();
String fragIDString = fragID.toString();
IXSDFragment frag = operElement.getFragmentByID(fragID.toString());
XSDToFragmentConfiguration xsdConfig = frag.getXSDToFragmentConfiguration();
XSDSimpleTypeDefinition simpleType = (XSDSimpleTypeDefinition)frag.getXSDTypeDefinition();
XSDTypeDefinition xsdBuiltInType = XSDTypeDefinitionUtil.resolveToXSDBuiltInTypeDefinition(simpleType);
%>
<table width="95%" border=0 cellpadding=3 cellspacing=0>
  <tr>
    <td class="labels" height=25 valign="bottom" align="left" nowrap>
      <label for="<%=fragIDString%>"><a href="javascript:openXSDInfoDialog('<%=response.encodeURL(controller.getPathWithContext(OpenXSDInfoDialogAction.getActionLink(session.getId(),selectedNode.getNodeId(),fragID.toString())))%>')"><%=frag.getName()%></a></label>
    </td>
    <td class="labels" height=25 valign="bottom" align="left" nowrap>
      <%=(xsdBuiltInType != null ? xsdBuiltInType.getName() : simpleType.getName())%>
    </td>
    <td>
      <%
      if (!frag.validateParameterValues(fragIDString)) {
      %>
      <%=HTMLUtils.redAsterisk()%>
      <%
      }
      %>
    </td>
    <td nowrap width="90%">&nbsp;</td>
  </tr>
</table>
<table cellpadding=3 cellspacing=0 class="<%=(xsdConfig.getIsWSDLPart() ? "fixfragtable" : "innerfixfragtable")%>">
  <%
  for (int i = 0; i < xsdConfig.getMaxOccurs(); i++) {
  %>
  <tr>
    <td width="60%">
      <%
      String value = frag.getParameterValue(frag.getID(), i);
      %>
      <input type="text" id="<%=frag.getID()%>" name="<%=fragIDString%>" value="<%=((value != null) ? value : "")%>" size="50" class="tabletextenter">
    </td>
    <td valign="center" align="left" nowrap>
      <a href="javascript:openCalendarBrowser(<%=request.getParameter(ActionInputs.CALENDAR_TYPE)%>,'contentborder','<%=fragIDString%>',<%=i%>)"><%=controller.getMessage("FORM_LINK_BROWSE")%></a>
    </td>
    <td nowrap width="30%">&nbsp;</td>    
  </tr>
  <%
  }
  %>
</table>
