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
<%@ page contentType="text/html; charset=UTF-8" import="org.eclipse.wst.ws.internal.explorer.platform.uddi.actions.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.perspective.*" %>

<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<%
   // Prepare the action.
   NodeManager navigatorManager = controller.getUDDIPerspective().getNavigatorManager();
   Node selectedNode = navigatorManager.getSelectedNode();
   ClearNavigatorNodeAction action;
   if (selectedNode instanceof FolderNode)
     action = new ClearNavigatorFolderNodeAction(controller);
   else
     action = new ClearNavigatorNodeAction(controller);

   // The action may be executed programmed link.
   action.populatePropertyTable(request);
   
   boolean inputsValid = true;
   
   // Run the action and obtain the return code (fail/success).
   boolean actionResult = action.execute();
%>
<%@ include file="/actions/ClearNodeAction.inc" %>
