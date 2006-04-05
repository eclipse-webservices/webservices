<%
/**
* <copyright>
*
* Licensed Material - Property of IBM
* (C) Copyright IBM Corp. 2002 - All Rights Reserved.
* US Government Users Restricted Rights - Use, duplication or disclosure
* restricted by GSA ADP Schedule Contract with IBM Corp.
*
* </copyright>
*
* File plugins/com.ibm.etools.webservice.explorer/wsexplorer/wsdl/fragment/XSDComplexRFragmentJSP.jsp, wsa.etools.ws.explorer, lunar-5.1.2 1
* Version 1.1 03/02/28 15:33:57
*/
%>
<%@ page contentType="text/html; charset=UTF-8" import="org.eclipse.wst.ws.internal.explorer.platform.wsdl.perspective.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.wsdl.datamodel.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.wsdl.constants.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.perspective.*" %>

<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<jsp:useBean id="fragID" class="java.lang.StringBuffer" scope="request"/>
<jsp:useBean id="nodeID" class="java.lang.StringBuffer" scope="request"/>
<jsp:useBean id="elementID" class="java.lang.StringBuffer" scope="request"/>
<jsp:useBean id="attribute" class="java.lang.StringBuffer" scope="request"/>

<%
WSDLPerspective wsdlPerspective = controller.getWSDLPerspective();
Node operNode = wsdlPerspective.getNodeManager().getNode(Integer.parseInt(nodeID.toString()));
WSDLOperationElement operElement = (WSDLOperationElement)operNode.getTreeElement();
IXSDComplexFragment frag = (IXSDComplexFragment)operElement.getFragmentByID(fragID.toString());
XSDToFragmentConfiguration xsdConfig = frag.getXSDToFragmentConfiguration();
String tableContainerIDBase = (new StringBuffer(FragmentConstants.TABLE_ID)).append(frag.getID()).toString();
String twistImageNameBase = (new StringBuffer("x")).append(tableContainerIDBase).toString();
IXSDFragment[] childFrags = frag.getAllFragments();
for (int i = 0; i < childFrags.length; i++) {
  fragID.delete(0, fragID.length());
  fragID.append(childFrags[i].getID());
%>
  <table width="95%" border=0 cellpadding=3 cellspacing=0>
    <tr>
      <td height=25 valign="bottom" align="left" nowrap width=11>
        <a href="javascript:twist('<%=i + tableContainerIDBase%>','<%=i + twistImageNameBase%>')"><img name="<%=i + twistImageNameBase%>" src="<%=response.encodeURL(controller.getPathWithContext("images/twistopened.gif"))%>" alt="<%=controller.getMessage("ALT_TWIST_OPENED")%>" class="twist"></a>
      </td>
      <td class="labels" height=25 valign="bottom" align="left" nowrap>
        <%=frag.getName()%>
      </td>
    </tr>
  </table>
  <span id="<%=i + tableContainerIDBase%>">
  <table cellpadding=0 cellspacing=0 class="<%=(xsdConfig.getIsWSDLPart() ? "fixfragtable" : "innerfixfragtable")%>">
    <tr>
      <td width=16>
        <img width=16 src="<%=response.encodeURL(controller.getPathWithContext("images/space.gif"))%>">
      </td>
      <td>
        <jsp:include page="<%=childFrags[i].getReadFragment()%>" flush="true"/>
      </td>
    </tr>
  <%

  IXSDAttributeFragment[] attributeFragments = frag.getAllAttributeFragments();
  IXSDAttributeFragment attributeFragment;
  for(int j = 0; j < attributeFragments.length; j++){
    attributeFragment = attributeFragments[j];
    
    if(attributeFragment.getID().startsWith(childFrags[i].getID())){
      IXSDFragment delegationFragment = attributeFragment.getXSDDelegationFragment();
      fragID.delete(0, fragID.length());
      fragID.append(delegationFragment.getID());
      attribute.delete(0, attribute.length());
      attribute.append("true");
    
      
      %>
      <tr>
        <td width=16>
          <img width=16 src="<%=response.encodeURL(controller.getPathWithContext("images/space.gif"))%>">
        </td>
        <td>
          <input type="hidden" name="<%=frag.getID()%>" value="<%=attributeFragment.getID()%>">
          <jsp:include page="<%=delegationFragment.getReadFragment()%>" flush="true"/>
      </td>
     </tr>
    <%
    
    
    }  
  }
}
  %>
  
  </table>
  </span>
