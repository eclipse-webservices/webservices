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
<%@ page contentType="text/html; charset=UTF-8" import="org.eclipse.wst.ws.internal.explorer.platform.favorites.perspective.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.favorites.datamodel.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.perspective.*" %>

<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<jsp:useBean id="formProperties" class="java.util.Hashtable" scope="request">
<%
   FavoritesPerspective favPerspective = controller.getFavoritesPerspective();
   NodeManager nodeManager = favPerspective.getNodeManager();
   // The selected node must be a FavoritesWSDLServiceNode.
   Node selectedNode = nodeManager.getSelectedNode();
   FavoritesWSDLServiceElement wsdlElement = (FavoritesWSDLServiceElement)selectedNode.getTreeElement();
   formProperties.put("formActionLink","favorites/actions/FavoritesImportToWorkbenchActionJSP.jsp");
   formProperties.put("wsdlURL",wsdlElement.getWsdlUrl());
%>
</jsp:useBean>
<jsp:include page="/forms/ImportWSDLAndWSILToWorkbenchForm.jsp" flush="true"/>
