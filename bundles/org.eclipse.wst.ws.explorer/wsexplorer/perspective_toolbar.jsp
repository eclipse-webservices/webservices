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
                                                        org.eclipse.wst.ws.internal.explorer.platform.perspective.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.actions.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.engine.ActionEngine" %>

<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<%
StringBuffer hrefActionEngine = new StringBuffer();
hrefActionEngine.append(response.encodeURL(controller.getPathWithContext("actionengine_container.jsp")));
hrefActionEngine.append("?");
hrefActionEngine.append(ActionInputs.SESSIONID);
hrefActionEngine.append("=");
hrefActionEngine.append(session.getId());
%>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title><%=controller.getMessage("FRAME_TITLE_PERSPECTIVE_TOOLBAR")%></title>
  <link rel="stylesheet" type="text/css" href="<%=response.encodeURL(controller.getPathWithContext("css/toolbar.css"))%>">
  <script language="javascript" src="<%=response.encodeURL(controller.getPathWithContext("scripts/toolbar.js"))%>">
  </script>
  <script language="javascript">
    function openActionEngineContainer()
    {
      var link = "<%=hrefActionEngine.toString()%>";
      var actionEngineContainer = window.open(link,"actionEngineContainer","height=100,width=350,status=yes,scrollbars=yes,resizable=yes");
      if (actionEngineContainer.focus)
        actionEngineContainer.focus();
    }
  </script>
</head>
<body class="gtoolbarbodymargin">
<div id="globaltoolbar">
<table width="100%" cellpadding=3 cellspacing=0 border=0>
  <tr>
    <td class="text" nowrap valign="middle" align="left" width="100%" height=25><%=controller.getMessage("TITLE_WSEXPLORER")%></td>
    <%
    String altHistoryBack = controller.getMessage("ALT_BACK");
    String altHistoryForward = controller.getMessage("ALT_FORWARD");
    String altActionEngine = controller.getMessage("ALT_ACTION_ENGINE");
    String altUDDI = controller.getMessage("ALT_UDDI_PERSPECTIVE");
    String altWSIL = controller.getMessage("ALT_WSIL_PERSPECTIVE");
    String altWSDL = controller.getMessage("ALT_WSDL_PERSPECTIVE");
    String altFavorites = controller.getMessage("ALT_FAVORITES_PERSPECTIVE");
    %>   
    <td class="text" nowrap valign="middle" align="center" width=16 height=26><a href="<%=response.encodeURL(controller.getPathWithContext(RetrieveHistoryAction.getActionLink(ActionInputs.JUMP_BACK)))%>" target="<%=FrameNames.PERSPECTIVE_WORKAREA%>"><img class="normal" alt="<%=altHistoryBack%>" title="<%=altHistoryBack%>" src="<%=response.encodeURL(controller.getPathWithContext("images/back_enabled.gif"))%>" onMouseOver="src='<%=response.encodeURL(controller.getPathWithContext("images/back_highlighted.gif"))%>';mouseover(this);" onMouseOut="src='<%=response.encodeURL(controller.getPathWithContext("images/back_enabled.gif"))%>';mouseout(this)" onMouseDown="src='<%=response.encodeURL(controller.getPathWithContext("images/back_highlighted.gif"))%>';mousedown(this)" onMouseUp="src='<%=response.encodeURL(controller.getPathWithContext("images/back_enabled.gif"))%>';mouseup(this)"></a></td>
    <td class="text" nowrap valign="middle" align="left" height=25><a href="<%=response.encodeURL(controller.getPathWithContext(RetrieveHistoryAction.getActionLink(ActionInputs.JUMP_FORWARD)))%>" target="<%=FrameNames.PERSPECTIVE_WORKAREA%>"><img class="normal" alt="<%=altHistoryForward%>" title="<%=altHistoryForward%>" src="<%=response.encodeURL(controller.getPathWithContext("images/forward_enabled.gif"))%>" onMouseOver="src='<%=response.encodeURL(controller.getPathWithContext("images/forward_highlighted.gif"))%>';mouseover(this);" onMouseOut="src='<%=response.encodeURL(controller.getPathWithContext("images/forward_enabled.gif"))%>';mouseout(this)" onMouseDown="src='<%=response.encodeURL(controller.getPathWithContext("images/forward_highlighted.gif"))%>';mousedown(this)" onMouseUp="src='<%=response.encodeURL(controller.getPathWithContext("images/forward_enabled.gif"))%>';mouseup(this)"></a></td>
    <%
    ActionEngine actionEngine = controller.getActionEngine();
    if (actionEngine != null && actionEngine.getMode() != ActionEngine.MODE_DISABLED)
    {
    %>
      <td class="text" nowrap valign="middle" align="left" height=25><a href="javascript:openActionEngineContainer()"><img class="normal" alt="<%=altActionEngine%>" title="<%=altActionEngine%>" src="<%=response.encodeURL(controller.getPathWithContext("images/eview16/actionengine.gif"))%>" onMouseOver="src='<%=response.encodeURL(controller.getPathWithContext("images/eview16/actionengine.gif"))%>';mouseover(this);" onMouseOut="src='<%=response.encodeURL(controller.getPathWithContext("images/eview16/actionengine.gif"))%>';mouseout(this)" onMouseDown="src='<%=response.encodeURL(controller.getPathWithContext("images/eview16/actionengine.gif"))%>';mousedown(this)" onMouseUp="src='<%=response.encodeURL(controller.getPathWithContext("images/eview16/actionengine.gif"))%>';mouseup(this)"></a></td>
    <%
    }
    %>
    <td class="text" nowrap valign="middle" align="left" height=25><a href="<%=response.encodeURL(controller.getPathWithContext(ShowPerspectiveAction.getActionLink(ActionInputs.PERSPECTIVE_UDDI,false)))%>" target="<%=FrameNames.PERSPECTIVE_WORKAREA%>"><img class="normal" alt="<%=altUDDI%>" title="<%=altUDDI%>" src="<%=response.encodeURL(controller.getPathWithContext("images/uddi_perspective_enabled.gif"))%>" onMouseOver="src='<%=response.encodeURL(controller.getPathWithContext("images/uddi_perspective_highlighted.gif"))%>';mouseover(this);" onMouseOut="src='<%=response.encodeURL(controller.getPathWithContext("images/uddi_perspective_enabled.gif"))%>';mouseout(this)" onMouseDown="src='<%=response.encodeURL(controller.getPathWithContext("images/uddi_perspective_highlighted.gif"))%>';mousedown(this)" onMouseUp="src='<%=response.encodeURL(controller.getPathWithContext("images/uddi_perspective_enabled.gif"))%>';mouseup(this)"></a></td>
    <td class="text" nowrap valign="middle" align="left" height=25><a href="<%=response.encodeURL(controller.getPathWithContext(ShowPerspectiveAction.getActionLink(ActionInputs.PERSPECTIVE_WSIL,false)))%>" target="<%=FrameNames.PERSPECTIVE_WORKAREA%>"><img class="normal" alt="<%=altWSIL%>" title="<%=altWSIL%>" src="<%=response.encodeURL(controller.getPathWithContext("images/wsil_perspective_enabled.gif"))%>" onMouseOver="src='<%=response.encodeURL(controller.getPathWithContext("images/wsil_perspective_highlighted.gif"))%>';mouseover(this);" onMouseOut="src='<%=response.encodeURL(controller.getPathWithContext("images/wsil_perspective_enabled.gif"))%>';mouseout(this)" onMouseDown="src='<%=response.encodeURL(controller.getPathWithContext("images/wsil_perspective_highlighted.gif"))%>';mousedown(this)" onMouseUp="src='<%=response.encodeURL(controller.getPathWithContext("images/wsil_perspective_enabled.gif"))%>';mouseup(this)"></a></td>
    <td class="text" nowrap valign="middle" align="left" height=25><a href="<%=response.encodeURL(controller.getPathWithContext(ShowPerspectiveAction.getActionLink(ActionInputs.PERSPECTIVE_WSDL,false)))%>" target="<%=FrameNames.PERSPECTIVE_WORKAREA%>"><img class="normal" alt="<%=altWSDL%>" title="<%=altWSDL%>" src="<%=response.encodeURL(controller.getPathWithContext("images/wsdl_perspective_enabled.gif"))%>" onMouseOver="src='<%=response.encodeURL(controller.getPathWithContext("images/wsdl_perspective_highlighted.gif"))%>';mouseover(this);" onMouseOut="src='<%=response.encodeURL(controller.getPathWithContext("images/wsdl_perspective_enabled.gif"))%>';mouseout(this)" onMouseDown="src='<%=response.encodeURL(controller.getPathWithContext("images/wsdl_perspective_highlighted.gif"))%>';mousedown(this)" onMouseUp="src='<%=response.encodeURL(controller.getPathWithContext("images/wsdl_perspective_enabled.gif"))%>';mouseup(this)"></a></td>    
    <td class="text" nowrap valign="middle" align="left" height=25><a href="<%=response.encodeURL(controller.getPathWithContext(ShowPerspectiveAction.getActionLink(ActionInputs.PERSPECTIVE_FAVORITES,false)))%>" target="<%=FrameNames.PERSPECTIVE_WORKAREA%>"><img class="normal" alt="<%=altFavorites%>" title="<%=altFavorites%>" src="<%=response.encodeURL(controller.getPathWithContext("images/favorites_perspective_enabled.gif"))%>" onMouseOver="src='<%=response.encodeURL(controller.getPathWithContext("images/favorites_perspective_highlighted.gif"))%>';mouseover(this);" onMouseOut="src='<%=response.encodeURL(controller.getPathWithContext("images/favorites_perspective_enabled.gif"))%>';mouseout(this)" onMouseDown="src='<%=response.encodeURL(controller.getPathWithContext("images/favorites_perspective_highlighted.gif"))%>';mousedown(this)" onMouseUp="src='<%=response.encodeURL(controller.getPathWithContext("images/favorites_perspective_enabled.gif"))%>';mouseup(this)"></a></td>
  </tr>
</table>
</div>
</body>
</html>
