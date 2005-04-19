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
                                                        org.eclipse.wst.ws.internal.explorer.platform.util.HTMLUtils" %>

<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<jsp:useBean id="formActionLink" class="java.lang.StringBuffer" scope="request"/>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title><%=controller.getMessage("FORM_TITLE_LAUNCH_WEBSERVICE_WIZARD")%></title>
  <link rel="stylesheet" type="text/css" href="<%=response.encodeURL(controller.getPathWithContext("css/windows.css"))%>">
<jsp:include page="/scripts/formsubmit.jsp" flush="true"/>
<jsp:include page="/scripts/formutils.jsp" flush="true"/>
</head>
<body class="contentbodymargin">
<div id="contentborder">
  <form action="<%=response.encodeURL(controller.getPathWithContext(formActionLink.toString()))%>" method="post" target="<%=FrameNames.PERSPECTIVE_WORKAREA%>" enctype="multipart/form-data">

<%
   String titleImagePath = "images/launch_wswizard_highlighted.gif";
   String title = controller.getMessage("ALT_LAUNCH_WEB_SERVICE_WIZARD");
%>
<%@ include file = "/forms/formheader.inc" %>
    <table>
      <tr>
        <td class="labels" height=20 valign="bottom">
          <%=controller.getMessage("FORM_LABEL_CHOOSE_WIZARD")%>
        </td>
      </tr>
      <tr>
        <td>
          <input type="radio" id="radio_ws_client_wizard" class="radio" name="<%=ActionInputs.WEB_SERVICE_WIZARD%>" checked=true value="<%=String.valueOf(ActionInputs.WEB_SERVICE_CLIENT_WIZARD)%>"><label for="radio_ws_client_wizard"><%=controller.getMessage("FORM_RADIO_WEB_SERVICE_CLIENT_WIZARD")%></label>
        </td>
      </tr>
      <tr>
        <td>
          <input type="radio" id="radio_ws_skeleton_wizard" class="radio" name="<%=ActionInputs.WEB_SERVICE_WIZARD%>" value="<%=String.valueOf(ActionInputs.WEB_SERVICE_SKELETON_WIZARD)%>"><label for="radio_ws_skeleton_wizard"><%=controller.getMessage("FORM_RADIO_WEB_SERVICE_SKELETON_WIZARD")%></label>
        </td>
      </tr>
    </table>
<jsp:include page="/forms/simpleCommon_table.jsp" flush="true"/>    
  </form>
</div>
</body>
</html>
