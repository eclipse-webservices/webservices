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
<%@ page contentType="text/html; charset=UTF-8" import="org.eclipse.wst.ws.internal.explorer.platform.wsil.constants.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.constants.*" %>

<jsp:include page="/scripts/panes.jsp" flush="true"/>
<script language="javascript">
  function getActionsContainerFrameset()
  {
    return perspectiveContent.frames["<%=WsilFrameNames.WSIL_ACTIONS_CONTAINER%>"].document.getElementsByTagName("frameset").item(0);
  }

  function processFramesetSizes(framesetSizesForm)
  {
    toggleDoubleClickColumnTitle();
    var perspectiveContentFrameset = getPerspectiveContentFrameset();
    var actionsContainerFrameset = getActionsContainerFrameset();
    framesetSizesForm.<%=WsilActionInputs.FRAMESET_COLS_PERSPECTIVE_CONTENT%>.value = perspectiveContentFrameset.cols;
    framesetSizesForm.<%=WsilActionInputs.FRAMESET_ROWS_ACTIONS_CONTAINER%>.value = actionsContainerFrameset.rows;
    framesetSizesForm.submit();
  }
</script>
