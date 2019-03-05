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
<%@ page contentType="text/html; charset=UTF-8" import="org.eclipse.wst.ws.internal.explorer.platform.wsdl.perspective.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.wsdl.constants.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.constants.*" %>

<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<%
   WSDLPerspective wsdlPerspective = controller.getWSDLPerspective();
%>   
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 3.2 Final//EN">
<html lang="<%=response.getLocale().getLanguage()%>">
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title><%=wsdlPerspective.getMessage("FRAME_TITLE_NAVIGATOR_TOOLBAR")%></title>
  <link rel="stylesheet" type="text/css" href="<%=response.encodeURL(controller.getPathWithContext("css/toolbar.css"))%>">
<jsp:include page="/wsdl/scripts/wsdlframesets.jsp" flush="true"/>  
<script language="javascript" src="<%=response.encodeURL(controller.getPathWithContext("scripts/toolbar.js"))%>">
</script>  
</head>
<body dir="<%=org.eclipse.wst.ws.internal.explorer.platform.util.DirUtils.getDir()%>" class="toolbarbodymargin">
<div id="toolbarborder">
  <div id="toolbar" ondblclick="processFramesetSizes(document.forms[0])">
<jsp:useBean id="formAction" class="java.lang.StringBuffer" scope="request">
<%
  formAction.append("wsdl/actions/ResizeWSDLFramesActionJSP.jsp");
%>  
</jsp:useBean>
<jsp:useBean id="formFrameName" class="java.lang.StringBuffer" scope="request">
<%
   formFrameName.append(WSDLFrameNames.WSDL_NAVIGATOR_CONTAINER);
%>
</jsp:useBean>
<jsp:include page="/wsdl/forms/ProcessWSDLFramesetsForm.jsp" flush="true"/>
    <table width="100%" height=25 cellpadding=0 cellspacing=0 border=0>
      <tr>
        <td valign="middle" align="center" width=25 height=25><img class="normal" src="<%=response.encodeURL(controller.getPathWithContext("images/navigator.gif"))%>" alt="" width=16 height=16></td>
<%
   String doubleClickColumnTitle = null;
   if (wsdlPerspective.getPerspectiveContentFramesetCols().startsWith("100%"))
     doubleClickColumnTitle = controller.getMessage("ALT_DOUBLE_CLICK_TO_RESTORE");
   else
     doubleClickColumnTitle = controller.getMessage("ALT_DOUBLE_CLICK_TO_MAXIMIZE");
%>        
        <td id="doubleclickcolumn" title="<%=doubleClickColumnTitle%>" valign="middle" width="*" height=25 nowrap class="text"><%=controller.getMessage("ALT_NAVIGATOR")%></td>
<%
   String altRefresh = controller.getMessage("ALT_REFRESH");
   String altClear = controller.getMessage("ALT_CLEAR");
%>           
        <td valign="middle" align="center" width=25 height=25><a href="<%=response.encodeURL(controller.getPathWithContext("wsdl/actions/RefreshWSDLActionJSP.jsp"))%>" target="<%=FrameNames.PERSPECTIVE_WORKAREA%>"><img class="normal" src="<%=response.encodeURL(controller.getPathWithContext("images/refresh_enabled.gif"))%>" width=16 height=16 onMouseOver="src='<%=response.encodeURL(controller.getPathWithContext("images/refresh_highlighted.gif"))%>';mouseover(this)" onMouseOut="src='<%=response.encodeURL(controller.getPathWithContext("images/refresh_enabled.gif"))%>';mouseout(this)" onMouseDown="src='<%=response.encodeURL(controller.getPathWithContext("images/refresh_highlighted.gif"))%>';mousedown(this)" onMouseUp="src='<%=response.encodeURL(controller.getPathWithContext("images/refresh_enabled.gif"))%>';mouseup(this)" alt="<%=altRefresh%>" title="<%=altRefresh%>"></a></td>
        <td valign="middle" align="center" width=25 height=25><a href="<%=response.encodeURL(controller.getPathWithContext("wsdl/actions/ClearWSDLActionJSP.jsp"))%>" target="<%=FrameNames.PERSPECTIVE_WORKAREA%>"><img class="normal" src="<%=response.encodeURL(controller.getPathWithContext("images/clear_enabled.gif"))%>" width=16 height=16 onMouseOver="src='<%=response.encodeURL(controller.getPathWithContext("images/clear_highlighted.gif"))%>';mouseover(this)" onMouseOut="src='<%=response.encodeURL(controller.getPathWithContext("images/clear_enabled.gif"))%>';mouseout(this)" onMouseDown="src='<%=response.encodeURL(controller.getPathWithContext("images/clear_highlighted.gif"))%>';mousedown(this)" onMouseUp="src='<%=response.encodeURL(controller.getPathWithContext("images/clear_enabled.gif"))%>';mouseup(this)" alt="<%=altClear%>" title="<%=altClear%>"></a></td>
      </tr>
    </table>
  </div>
</div>
</body>
</html>
