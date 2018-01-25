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
<%@ page contentType="text/html; charset=UTF-8" import="org.eclipse.wst.ws.internal.explorer.platform.uddi.constants.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.constants.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.util.*" %>
<jsp:include page="/scripts/panes.jsp" flush="true"/>
<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<script language="javascript">
  function getActionsContainerFrameset()
  {
    return perspectiveContent.frames["<%=UDDIFrameNames.ACTIONS_CONTAINER%>"].document.getElementsByTagName("frameset").item(0);
  }

  function processFramesetSizes(framesetSizesForm)
  {
    toggleDoubleClickColumnTitle();
    var perspectiveContentFrameset = getPerspectiveContentFrameset();
    var actionsContainerFrameset = getActionsContainerFrameset();
    framesetSizesForm.<%=UDDIActionInputs.FRAMESET_COLS_PERSPECTIVE_CONTENT%>.value = perspectiveContentFrameset.cols;
    framesetSizesForm.<%=UDDIActionInputs.FRAMESET_ROWS_ACTIONS_CONTAINER%>.value = actionsContainerFrameset.rows;
    framesetSizesForm.submit();
  }
</script>
