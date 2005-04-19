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
<%@ page contentType="text/html; charset=UTF-8" import="org.eclipse.wst.ws.internal.explorer.platform.constants.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.util.*" %>
<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<script language="javascript">
  var perspectiveWorkArea = top.frames["<%=FrameNames.PERSPECTIVE_WORKAREA%>"];
  var perspectiveToolbar = top.frames["<%=FrameNames.PERSPECTIVE_TOOLBAR%>"];
  var perspectiveContent = top.frames["<%=FrameNames.PERSPECTIVE_CONTENT%>"];

  function getPerspectiveContentFrameset()
  {
    return perspectiveContent.document.getElementsByTagName("frameset").item(0);
  }

  function toggleDoubleClickColumnTitle()
  {
    var doubleClickColumn = document.getElementById("doubleclickcolumn");
    if (doubleClickColumn == null)
      return;
<%
   String jsAltRestore = HTMLUtils.JSMangle(controller.getMessage("ALT_DOUBLE_CLICK_TO_RESTORE"));
%>
    if (doubleClickColumn.title == "<%=jsAltRestore%>")
      doubleClickColumn.title = "<%=HTMLUtils.JSMangle(controller.getMessage("ALT_DOUBLE_CLICK_TO_MAXIMIZE"))%>";
    else
      doubleClickColumn.title = "<%=jsAltRestore%>";
  }
</script>
