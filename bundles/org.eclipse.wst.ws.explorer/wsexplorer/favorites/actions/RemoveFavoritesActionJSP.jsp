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
<%@ page contentType="text/html; charset=UTF-8" import="org.eclipse.wst.ws.internal.explorer.platform.favorites.constants.*,
                                                                                          org.eclipse.wst.ws.internal.explorer.platform.perspective.*,
                                   					                    org.eclipse.wst.ws.internal.explorer.platform.favorites.actions.RemoveFavoritesAction"%>

<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<jsp:include page="/favorites/scripts/favoritesPanes.jsp" flush="true"/>
<%
    // Prepare the action.
    RemoveFavoritesAction action = new RemoveFavoritesAction(controller);

    // Load the parameters for the action from the servlet request.
    boolean inputsValid = action.populatePropertyTable(request);

    // Run the action and obtain the return code (fail/success).
    boolean actionResult = action.execute();
%>
<%@ include file="/actions/ClearNodeAction.inc" %>
