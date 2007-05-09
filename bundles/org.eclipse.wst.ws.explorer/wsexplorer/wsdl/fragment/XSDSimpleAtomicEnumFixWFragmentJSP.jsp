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
                                                        org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.impl.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.util.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.wsdl.actions.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.perspective.*,
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
   XSDSimpleAtomicEnumFragment frag = (XSDSimpleAtomicEnumFragment)operElement.getFragmentByID(fragID.toString());
   IXSDElementFragment elementFragment = (IXSDElementFragment)operElement.getFragmentByID(elementID.toString());
   XSDToFragmentConfiguration xsdConfig = frag.getXSDToFragmentConfiguration();
   XSDSimpleTypeDefinition simpleType = (XSDSimpleTypeDefinition)frag.getXSDTypeDefinition();
   XSDTypeDefinition xsdBuiltInType = XSDTypeDefinitionUtil.resolveToXSDBuiltInTypeDefinition(simpleType);
%>
<table width="95%" border=0 cellpadding=3 cellspacing=0>
  <tr>
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
    <td>
<%
   if (!frag.validateParameterValues(frag.getID()))
   {
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
   OptionVector optionVector = frag.getOptionVector();
   for (int i = 0; i < xsdConfig.getMaxOccurs(); i++)
   {
%>
  <tr>
    <td>
<%
     String selectedValue = frag.getParameterValue(frag.getID(), i);
%>
      <select id="<%=frag.getID()%>" name="<%=frag.getID()%>" class="selectlist">
<%              
     for (int j = 0; j < optionVector.size(); j++)
     {
       String displayValue = optionVector.getDisplayValue(j);
       String value = optionVector.getValue(j);
       if (value.equals(selectedValue))
       {
%>
        <option value="<%=value%>" selected><%=displayValue%>
<%
       }
       else
       {
%>
        <option value="<%=value%>"><%=displayValue%>
<%
       }
     }
%>
      </select>
    </td>
  </tr>
<%
   }
  %>
</table>
