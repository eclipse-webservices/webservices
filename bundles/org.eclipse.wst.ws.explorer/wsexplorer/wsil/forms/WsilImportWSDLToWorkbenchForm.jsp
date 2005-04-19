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
<%@ page contentType="text/html; charset=UTF-8" import="org.eclipse.wst.ws.internal.explorer.platform.wsil.perspective.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.wsil.datamodel.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.perspective.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.datamodel.*" %>
<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<%
  WSILPerspective wsilPerspective = controller.getWSILPerspective();
  NodeManager nodeManager = wsilPerspective.getNodeManager();
  Node selectedNode = nodeManager.getSelectedNode();
  Tool selectedTool = selectedNode.getToolManager().getSelectedTool();
%>
<jsp:useBean id="formProperties" class="java.util.Hashtable" scope="request">
<%
  if (selectedTool instanceof ListWSDLServicesTool)
  {
    int viewId = selectedNode.getViewId();
    WsilElement wsilElement = (WsilElement)selectedNode.getTreeElement();
    ListElement le = wsilElement.getAllWSDLServices().getElementWithViewId(viewId);
    WsilWsdlServiceElement wsilWsdlServiceElement = (WsilWsdlServiceElement)le.getObject();
    formProperties.put("wsdlURL",wsilWsdlServiceElement.getWSDLServiceURL());
  }
  formProperties.put("formActionLink","wsil/actions/WsilImportWSDLToWorkbenchActionJSP.jsp");
%>   
</jsp:useBean>
<%
  if (selectedTool instanceof ListWSDLServicesTool)
  {
%>
<jsp:include page="/forms/ImportWSDLAndWSILToWorkbenchForm.jsp" flush="true"/>
<%
  }
  else if (selectedTool instanceof ListUDDIServicesTool)
  {
%>
<jsp:include page="/forms/ImportToWorkbenchForm.jsp" flush="true"/>
<%
  }
%>
