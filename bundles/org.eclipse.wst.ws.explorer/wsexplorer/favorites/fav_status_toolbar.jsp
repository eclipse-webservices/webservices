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
                                                        org.eclipse.wst.ws.internal.explorer.platform.favorites.constants.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.constants.*" %>

<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<%
   FavoritesPerspective favPerspective = controller.getFavoritesPerspective();
%>   
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title><%=favPerspective.getMessage("FRAME_TITLE_STATUS_TOOLBAR")%></title>
  <link rel="stylesheet" type="text/css" href="<%=response.encodeURL(controller.getPathWithContext("css/toolbar.css"))%>">
<jsp:include page="/favorites/scripts/favoritesframesets.jsp" flush="true"/>  
<script language="javascript" src="<%=response.encodeURL(controller.getPathWithContext("scripts/toolbar.js"))%>">
</script>
</head>
<body class="toolbarbodymargin">
<div id="toolbarborder">
  <div id="toolbar" ondblclick="processFramesetSizes(document.forms[0])">
<jsp:useBean id="formAction" class="java.lang.StringBuffer" scope="request">
<%
  formAction.append("favorites/actions/ResizeFavoritesFramesActionJSP.jsp");
%>  
</jsp:useBean>
<jsp:useBean id="formFrameName" class="java.lang.StringBuffer" scope="request">
<%
   formFrameName.append(FavoritesFrameNames.STATUS_CONTAINER);
%>
</jsp:useBean>
<jsp:include page="/favorites/forms/ProcessFavoritesFramesetsForm.jsp" flush="true"/>
    <table width="100%" height=25 cellpadding=0 cellspacing=0 border=0>
      <tr>
        <td valign="middle" align="center" width=25 height=25><img class="normal" src="<%=response.encodeURL(controller.getPathWithContext("images/status.gif"))%>" width=16 height=16></td>
<%
   String doubleClickColumnTitle = null;
   if (favPerspective.getPerspectiveContentFramesetCols().endsWith("100%"))
   {
     if (favPerspective.getActionsContainerFramesetRows().endsWith("100%"))
       doubleClickColumnTitle = controller.getMessage("ALT_DOUBLE_CLICK_TO_RESTORE");       
   }
   if (doubleClickColumnTitle == null)
     doubleClickColumnTitle = controller.getMessage("ALT_DOUBLE_CLICK_TO_MAXIMIZE");
%>        
        <td id="doubleclickcolumn" title="<%=doubleClickColumnTitle%>" valign="middle" align="left" width="*" height=25 nowrap class="text"><%=controller.getMessage("ALT_STATUS")%></td>
<%
   String altClear = controller.getMessage("ALT_CLEAR");
%>           
        <td valign="middle" align="center" width=25 height=25><a href="<%=response.encodeURL(controller.getPathWithContext("favorites/fav_status_content.jsp"))%>" target="<%=FavoritesFrameNames.STATUS_CONTENT%>"><img class="normal" src="<%=response.encodeURL(controller.getPathWithContext("images/clear_enabled.gif"))%>" width=16 height=16 onMouseOver="src='<%=response.encodeURL(controller.getPathWithContext("images/clear_highlighted.gif"))%>';mouseover(this)" onMouseOut="src='<%=response.encodeURL(controller.getPathWithContext("images/clear_enabled.gif"))%>';mouseout(this)" onMouseDown="src='<%=response.encodeURL(controller.getPathWithContext("images/clear_highlighted.gif"))%>';mousedown(this)" onMouseUp="src='<%=response.encodeURL(controller.getPathWithContext("images/clear_enabled.gif"))%>';mouseup(this)" alt="<%=altClear%>" title="<%=altClear%>"></a></td>
      </tr>
    </table>
  </div>
</div>
</body>
</html>
