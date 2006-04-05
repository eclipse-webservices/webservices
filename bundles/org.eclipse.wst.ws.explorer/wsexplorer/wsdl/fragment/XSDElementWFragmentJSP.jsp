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
* File plugins/com.ibm.etools.webservice.explorer/wsexplorer/wsdl/fragment/XSDDelegationWFragmentJSP.jsp, wsa.etools.ws.explorer, lunar-5.1.2, 20031231a 1
* Version 1.1 03/02/28 15:34:04
*/
%>
<%@ page contentType="text/html; charset=UTF-8" import="org.eclipse.wst.ws.internal.explorer.platform.wsdl.perspective.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.wsdl.datamodel.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.perspective.*" %>

<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<jsp:useBean id="fragID" class="java.lang.StringBuffer" scope="request"/>
<jsp:useBean id="nodeID" class="java.lang.StringBuffer" scope="request"/>
<jsp:useBean id="elementID" class="java.lang.StringBuffer" scope="request"/>

<%
WSDLPerspective wsdlPerspective = controller.getWSDLPerspective();
Node selectedNode = wsdlPerspective.getNodeManager().getNode(Integer.parseInt(nodeID.toString()));
WSDLOperationElement operElement = (WSDLOperationElement)selectedNode.getTreeElement();
IXSDElementFragment elementFragment = (IXSDElementFragment)operElement.getFragmentByID(fragID.toString());
IXSDFragment delegationFragment = elementFragment.getXSDDelegationFragment();
XSDToFragmentConfiguration xsdConfig = elementFragment.getXSDToFragmentConfiguration();
fragID.delete(0, fragID.length());
fragID.append(delegationFragment.getID());
elementID.delete(0, elementID.length());
elementID.append(elementFragment.getID());
%>

 

<table  cellpadding=0 cellspacing=0 class="<%=("innerfixfragtable")%>">
  <tr>
    <td>
      <jsp:include page="<%=delegationFragment.getWriteFragment()%>" flush="true"/>
    </td>
  </tr>
</table>

 

