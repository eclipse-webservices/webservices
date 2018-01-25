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
<%@ page contentType="text/html; charset=UTF-8" import="org.eclipse.wst.ws.internal.explorer.platform.uddi.perspective.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.perspective.*" %>

<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<jsp:useBean id="formProperties" class="java.util.Hashtable" scope="request">
<%
   UDDIPerspective uddiPerspective = controller.getUDDIPerspective();
   NodeManager navigatorManager = uddiPerspective.getNavigatorManager();
   Node selectedNode = navigatorManager.getSelectedNode();
   String wsdlURL;
   // selectedNode should be either a service or a service interface node.
   if (selectedNode instanceof ServiceNode)
   {
     ServiceNode serviceNode = (ServiceNode)selectedNode;
     wsdlURL = serviceNode.getWSDLURLFromDetailsTool();
   }
   else
   {
     ServiceInterfaceNode siNode = (ServiceInterfaceNode)selectedNode;
     wsdlURL = siNode.getWSDLURLFromDetailsTool();
   }
   formProperties.put("formActionLink","uddi/actions/UDDIImportWSDLToWorkbenchActionJSP.jsp");
   formProperties.put("wsdlURL",wsdlURL);
%>
</jsp:useBean>
<jsp:include page="/forms/ImportToWorkbenchForm.jsp" flush="true"/>
