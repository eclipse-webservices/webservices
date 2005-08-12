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
<%@ page contentType="text/html; charset=UTF-8" import="org.eclipse.wst.ws.internal.explorer.platform.constants.FrameNames,
                                                                                          org.eclipse.wst.ws.internal.explorer.platform.perspective.FormTool,
                                                                                          org.eclipse.wst.ws.internal.explorer.platform.wsil.perspective.WSILPerspective,
                                                                                          org.eclipse.wst.ws.internal.explorer.platform.wsil.constants.*,
                                                                                          org.eclipse.wst.ws.internal.explorer.platform.util.HTMLUtils" %>

<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<%
WSILPerspective wsilPerspective = controller.getWSILPerspective();
FormTool formTool = (FormTool)wsilPerspective.getNodeManager().getSelectedNode().getToolManager().getSelectedTool();
%>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title><%=wsilPerspective.getMessage("FORM_TITLE_OPEN_WSIL")%></title>
  <link rel="stylesheet" type="text/css" href="<%=response.encodeURL(controller.getPathWithContext("css/windows.css"))%>">
<jsp:include page="/wsil/scripts/wsilPanes.jsp" flush="true"/>
<jsp:include page="/scripts/formsubmit.jsp" flush="true"/>
<jsp:include page="/scripts/formutils.jsp" flush="true"/>
</head>
<body dir="<%=org.eclipse.wst.ws.internal.explorer.platform.util.DirUtils.getDir()%>" class="contentbodymargin">
<div id="contentborder">
  <form action="<%=response.encodeURL(controller.getPathWithContext("wsil/actions/OpenWSILActionJSP.jsp"))%>" method="post" target="<%=FrameNames.PERSPECTIVE_WORKAREA%>" enctype="multipart/form-data" onSubmit="return handleSubmit(this)">
  <%
  String titleImagePath = "wsil/images/open_wsil_highlighted.gif";
  String title = wsilPerspective.getMessage("ALT_OPEN_WSIL");
  %>
  <%@ include file = "/forms/formheader.inc" %>
  <table>
    <tr>
      <td class="labels">
        <%=wsilPerspective.getMessage("FORM_LABEL_OPEN_WSIL_DESC")%>
      </td>
    </tr>
  </table>        
  <table width="95%" border=0 cellpadding=3 cellspacing=0>
    <tr>
      <td class="labels" height=25 valign="bottom">
        <label for="input_wsil_url"><%=wsilPerspective.getMessage("WSIL_URL")%></label>
        <%
        if (!formTool.isInputValid(WsilActionInputs.WSIL_URL)) {
        %>
          <%=HTMLUtils.redAsterisk()%>
        <%
        }
        String wsilURL = (String)formTool.getProperty(WsilActionInputs.WSIL_URL);
        if (wsilURL == null)
          wsilURL = "";
        %>
      </td>
    </tr>
    <tr>
      <td>
        <input type="text" id="input_wsil_url" name="<%=WsilActionInputs.WSIL_URL%>" value="<%=wsilURL%>" size="50" class="textenter">
      </td>
    </tr>
  </table>
  <%
  String inspectionTypeString = (String)formTool.getProperty(WsilActionInputs.WSIL_INSPECTION_TYPE);
  int inspectionType = WsilActionInputs.WSIL_DETAILS;
  if (inspectionTypeString != null)
    inspectionType = Integer.parseInt(inspectionTypeString);
  %>
  <table width="95%" border=0 cellpadding=3 cellspacing=0>
    <tr>
      <td class="labels" height=30 valign="bottom">
        <label for="inspection_type"><%=wsilPerspective.getMessage("FORM_LABEL_CHOOSE_WSIL_INSPECTION_TYPE")%></label>
      </td>
    </tr>
    <tr>
      <td valign="bottom">
        <select id="inspection_type" name="<%=WsilActionInputs.WSIL_INSPECTION_TYPE%>" class="selectlist">
          <option value="<%=WsilActionInputs.WSIL_DETAILS%>" <% if (inspectionType == WsilActionInputs.WSIL_DETAILS) { %>selected<% } %>><%=wsilPerspective.getMessage("FORM_LABEL_WSIL")%>
          <option value="<%=WsilActionInputs.WSDL_SERVICES%>" <% if (inspectionType == WsilActionInputs.WSDL_SERVICES) { %>selected<% } %>><%=wsilPerspective.getMessage("FORM_LABEL_WSDL_SERVICE")%>
          <option value="<%=WsilActionInputs.UDDI_SERVICES%>" <% if (inspectionType == WsilActionInputs.UDDI_SERVICES) { %>selected<% } %>><%=wsilPerspective.getMessage("FORM_LABEL_UDDI_SERVICE")%>
          <option value="<%=WsilActionInputs.UDDI_BUSINESSES%>" <% if (inspectionType == WsilActionInputs.UDDI_BUSINESSES) { %>selected<% } %>><%=wsilPerspective.getMessage("FORM_LABEL_UDDI_BUSINESS")%>
          <option value="<%=WsilActionInputs.WSIL_LINKS%>" <% if (inspectionType == WsilActionInputs.WSIL_LINKS) { %>selected<% } %>><%=wsilPerspective.getMessage("FORM_LABEL_WSIL_LINKS")%>
        </select>
      </td>
    </tr>
  </table>
<jsp:include page="/forms/simpleCommon_table.jsp" flush="true"/>
  </form>
</div>
</body>
</html>
