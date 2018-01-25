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
                                                        org.eclipse.wst.ws.internal.explorer.platform.wsil.constants.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.constants.*" %>

<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<%
   WSILPerspective wsilPerspective = controller.getWSILPerspective();
%>   
<jsp:useBean id="formAction" class="java.lang.StringBuffer" scope="request"/>
<jsp:useBean id="formFrameName" class="java.lang.StringBuffer" scope="request"/>
<form action="<%=response.encodeURL(controller.getPathWithContext(formAction.toString()))%>" method="post" target="<%=FrameNames.PERSPECTIVE_WORKAREA%>" enctype="multipart/form-data" style="margin-top:0;margin-bottom:0">
  <input name="<%=ActionInputs.FRAME_NAME%>" type="hidden" value="<%=formFrameName%>">
  <input name="<%=WsilActionInputs.FRAMESET_COLS_PERSPECTIVE_CONTENT%>" type="hidden" value="<%=wsilPerspective.getPerspectiveContentFramesetCols()%>">
  <input name="<%=WsilActionInputs.FRAMESET_ROWS_ACTIONS_CONTAINER%>" type="hidden" value="<%=wsilPerspective.getActionsContainerFramesetRows()%>">
</form>
