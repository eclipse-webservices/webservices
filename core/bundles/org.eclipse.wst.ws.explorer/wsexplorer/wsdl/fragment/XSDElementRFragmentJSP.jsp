<%
/**
 *
 * Copyright (c) 2002, 2016 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
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
      <jsp:include page="<%=delegationFragment.getReadFragment()%>" flush="true"/>
    </td>
  </tr>
</table>

 

