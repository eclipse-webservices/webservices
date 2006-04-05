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
                                                        org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.wsdl.actions.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.perspective.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.constants.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.util.*,
                                                        org.eclipse.xsd.*" %>

<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<jsp:useBean id="fragID" class="java.lang.StringBuffer" scope="request"/>
<jsp:useBean id="nodeID" class="java.lang.StringBuffer" scope="request"/>
<jsp:useBean id="elementID" class="java.lang.StringBuffer" scope="request"/>
<%
WSDLPerspective wsdlPerspective = controller.getWSDLPerspective();
Node selectedNode = wsdlPerspective.getNodeManager().getNode(Integer.parseInt(nodeID.toString()));
WSDLOperationElement operElement = (WSDLOperationElement)selectedNode.getTreeElement();
IXSDFragment frag = operElement.getFragmentByID(fragID.toString());
IXSDElementFragment elementFragment = (IXSDElementFragment)operElement.getFragmentByID(elementID.toString());
XSDToFragmentConfiguration xsdConfig = frag.getXSDToFragmentConfiguration();
XSDSimpleTypeDefinition simpleType = (XSDSimpleTypeDefinition)frag.getXSDTypeDefinition();
XSDTypeDefinition xsdBuiltInType = XSDTypeDefinitionUtil.resolveToXSDBuiltInTypeDefinition(simpleType);
String tableContainerID = (new StringBuffer(FragmentConstants.TABLE_ID)).append(frag.getID()).toString();
String twistImageName = (new StringBuffer("x")).append(tableContainerID).toString();
int calendarType = Integer.parseInt(request.getParameter(ActionInputs.CALENDAR_TYPE));
%>
<table width="95%" border=0 cellpadding=3 cellspacing=0>
  <tr>
    <td height=25 valign="bottom" align="left" nowrap width=11>
      <a href="javascript:twist('<%=tableContainerID%>','<%=twistImageName%>')"><img name="<%=twistImageName%>" src="<%=response.encodeURL(controller.getPathWithContext("images/twistopened.gif"))%>" alt="<%=controller.getMessage("ALT_TWIST_OPENED")%>" class="twist"></a>
    </td>
    <td class="labels" height=25 valign="bottom" align="left" nowrap>
      <label for="<%=frag.getID()%>"><a href="javascript:openXSDInfoDialog('<%=response.encodeURL(controller.getPathWithContext(OpenXSDInfoDialogAction.getActionLink(session.getId(),selectedNode.getNodeId(),fragID.toString())))%>')"><%=frag.getName()%></a></label>
    </td>
    <td class="labels" height=25 valign="bottom" align="left" nowrap>
      <%=(xsdBuiltInType != null ? xsdBuiltInType.getName() : simpleType.getName())%>
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
      <a href="javascript:addSimpleAtomicDateTimeRows(<%=calendarType%>,'<%=tableContainerID%>', '<%=frag.getID()%>', '<%=frag.getID()%>', null, <%=xsdConfig.getMaxOccurs()%>)"><%=wsdlPerspective.getMessage("FORM_LINK_ADD")%></a>
    </td>
    <td class="labels" height=25 valign="bottom" align="left" nowrap>
      <a href="javascript:checkMinOccursAndRemoveSelectedDateTimeRows(<%=calendarType%>,'<%=tableContainerID%>',<%=xsdConfig.getMinOccurs()%>)"><%=wsdlPerspective.getMessage("FORM_LINK_REMOVE")%></a>
    </td>
    <td>
      <%
      if (!frag.validateParameterValues(frag.getID())) {
      %>
      <%=HTMLUtils.redAsterisk()%>
      <%
      }
      %>
    </td>
    <td nowrap width="90%">&nbsp;</td>
  </tr>
</table>
<span id="<%=tableContainerID%>">
<table cellpadding=3 cellspacing=0 class="<%=(xsdConfig.getIsWSDLPart() ? "rangefragtable" : "innerrangefragtable")%>">
  <tr>
    <th class="checkboxcells" width=10><input type="checkbox" onClick="handleCheckAllClick('<%=tableContainerID%>',this)" title="<%=controller.getMessage("FORM_CONTROL_TITLE_SELECT_ALL_CHECK_BOX")%>"></th>
    <th width="90%" class="headercolor"><%=wsdlPerspective.getMessage("FORM_LABEL_VALUES")%></th>
    <th class="headercolor"><%=controller.getMessage("FORM_LABEL_ACTIONS")%></th>
  </tr>
</table>
</span>
<%
String[] values = frag.getParameterValues(frag.getID());
int i = 0;
if (values != null) {
  for ( ; i < values.length; i++) {
%>
    <script language="javascript">
      addSimpleAtomicDateTimeRows(<%=calendarType%>,"<%=tableContainerID%>","<%=frag.getID()%>","<%=frag.getID()%>","<%=values[i]%>",<%=xsdConfig.getMaxOccurs()%>);
    </script>
<%
  }
}
for ( ; i < xsdConfig.getMinOccurs(); i++) {
%>
  <script language="javascript">
    addSimpleAtomicDateTimeRows(<%=calendarType%>,"<%=tableContainerID%>","<%=frag.getID()%>","<%=frag.getID()%>",null,<%=xsdConfig.getMaxOccurs()%>);
  </script>
<%
}
%>
