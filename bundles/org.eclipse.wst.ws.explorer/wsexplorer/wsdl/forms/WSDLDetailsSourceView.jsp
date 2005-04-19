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
<%@ page contentType="text/xml; charset=UTF-8" import="org.eclipse.wst.ws.internal.explorer.platform.wsdl.perspective.*,
                                                       org.eclipse.wst.ws.internal.explorer.platform.wsdl.datamodel.*,
                                                       org.eclipse.wst.ws.internal.explorer.platform.perspective.*,
                                                       org.eclipse.wst.wsdl.internal.impl.wsdl4j.WSDLFactoryImpl,
                                                       java.io.*,
                                                       javax.wsdl.*,
                                                       javax.wsdl.xml.*,
                                                       javax.wsdl.factory.*" %>

<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<%
WSDLPerspective wsdlPerspective = controller.getWSDLPerspective();
NodeManager nodeManager = wsdlPerspective.getNodeManager();
Node selectedNode = nodeManager.getSelectedNode();
WSDLElement wsdlElement = (WSDLElement)selectedNode.getTreeElement();
Definition definition = wsdlElement.getDefinition();
WSDLFactory wsdlFactory = new WSDLFactoryImpl();
WSDLWriter wsdlWriter = wsdlFactory.newWSDLWriter();
StringWriter stringWriter = new StringWriter();
try
{
  wsdlWriter.writeWSDL(definition, stringWriter);
  %>
  <%=stringWriter.toString()%>
  <%
}
catch (WSDLException wsdle)
{
  %>
  <?xml version="1.0"?>
  <%
}
%>
