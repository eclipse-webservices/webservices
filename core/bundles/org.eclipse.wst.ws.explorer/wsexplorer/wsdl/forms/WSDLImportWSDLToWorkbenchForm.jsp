<%
/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
%>
<%@ page contentType="text/html; charset=UTF-8" import="org.eclipse.wst.ws.internal.explorer.platform.wsdl.perspective.WSDLPerspective,
                                                        org.eclipse.wst.ws.internal.explorer.platform.wsdl.datamodel.WSDLElement,
                                                        org.eclipse.wst.ws.internal.explorer.platform.perspective.*" %>

<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<jsp:useBean id="formProperties" class="java.util.Hashtable" scope="request">
<%
   WSDLPerspective wsdlPerspective = controller.getWSDLPerspective();
   NodeManager nodeManager = wsdlPerspective.getNodeManager();
   Node selectedNode = nodeManager.getSelectedNode();
   String wsdlUrl = ((WSDLElement)selectedNode.getTreeElement()).getWsdlUrl();
   formProperties.put("formActionLink","wsdl/actions/WSDLImportWSDLToWorkbenchActionJSP.jsp");
   formProperties.put("wsdlURL",wsdlUrl);
%>
</jsp:useBean>
<jsp:include page="/forms/ImportWSDLAndWSILToWorkbenchForm.jsp" flush="true"/>
