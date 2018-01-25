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
<%@ page contentType="text/html; charset=UTF-8" import="org.eclipse.wst.ws.internal.explorer.platform.wsdl.actions.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.perspective.*" %>

<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<%
// Prepare the action
SwitchPerspectiveFromWSDLAction action = new SwitchPerspectiveFromWSDLAction(controller);
   
// Load the parameters for the action from the servlet request.
action.populatePropertyTable(request);
   
// Run the action and obtain the return code (fail/success).
boolean actionResult = action.execute();
   
// Determine if the action was added to the history list.
boolean isAddedToHistory = action.isAddedToHistory();
%>
<jsp:include page="/scripts/switchperspective.jsp" flush="true"/>
