<%
/*******************************************************************************
 * Copyright (c) 2001, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060427   127443 jesper@selskabet.org - Jesper S Moller
 *******************************************************************************/
%>
<%@ page contentType="text/html; charset=UTF-8" import="org.eclipse.wst.ws.internal.explorer.platform.constants.*,
                                                                                          org.eclipse.wst.ws.internal.explorer.platform.perspective.FormTool,
                                                                                          org.eclipse.wst.ws.internal.explorer.platform.wsdl.perspective.WSDLPerspective,
                                                                                          org.eclipse.wst.ws.internal.explorer.platform.util.HTMLUtils" %>

<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<%
  WSDLPerspective wsdlPerspective = controller.getWSDLPerspective();
  FormTool formTool = (FormTool)wsdlPerspective.getNodeManager().getSelectedNode().getToolManager().getSelectedTool();
%>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title><%=wsdlPerspective.getMessage("FORM_TITLE_OPEN_WSDL")%></title>
  <link rel="stylesheet" type="text/css" href="<%=response.encodeURL(controller.getPathWithContext("css/windows.css"))%>">
<jsp:include page="/wsdl/scripts/wsdlpanes.jsp" flush="true"/>
<jsp:include page="/scripts/formsubmit.jsp" flush="true"/>
<jsp:include page="/scripts/formutils.jsp" flush="true"/>
<jsp:include page="/scripts/wsdlbrowser.jsp" flush="true"/>
</head>
<body dir="<%=org.eclipse.wst.ws.internal.explorer.platform.util.DirUtils.getDir()%>" class="contentbodymargin" onUnload="closeWSDLBrowser()">
<div id="contentborder">
  <form action="<%=response.encodeURL(controller.getPathWithContext("wsdl/actions/OpenWSDLActionJSP.jsp"))%>" method="post" target="<%=FrameNames.PERSPECTIVE_WORKAREA%>" enctype="multipart/form-data" onSubmit="return handleSubmit(this)">
    <%
    String titleImagePath = "wsdl/images/open_wsdl_highlighted.gif";
    String title = wsdlPerspective.getMessage("ALT_OPEN_WSDL");
    %>
    <%@ include file = "/forms/formheader.inc" %>
      <table>
        <tr>
          <td class="labels">
            <%=wsdlPerspective.getMessage("FORM_LABEL_OPEN_WSDL_DESC")%>
          </td>
        </tr>
      </table>        
        <table width="95%" border=0 cellpadding=3 cellspacing=0>
          <tr>
            <td class="labels" height=25 valign="bottom" nowrap>
              <label for="input_wsdl_url"><%=controller.getMessage("FORM_LABEL_WSDL_URL")%></label>
              <%
              if (!formTool.isInputValid(ActionInputs.QUERY_INPUT_WSDL_URL)) {
              %>
              <%=HTMLUtils.redAsterisk()%>
              <%
              }
              String wsdlUrl = (String)formTool.getProperty(ActionInputs.QUERY_INPUT_WSDL_URL);
              if (wsdlUrl == null)
                wsdlUrl = "";
              %>
            </td>
            <td height=25 valign="bottom" nowrap>
              <a href="javascript:openWSDLBrowser('contentborder',<%=ActionInputs.WSDL_TYPE_SERVICE%>)"><%=controller.getMessage("FORM_LINK_BROWSE")%></a>
            </td>
            <td width="90%">&nbsp;</td>
          </tr>
          <tr>
            <td colspan=3>
              <input type="text" id="input_wsdl_url" name="<%=ActionInputs.QUERY_INPUT_WSDL_URL%>" value="<%=HTMLUtils.charactersToHTMLEntitiesStrict(wsdlUrl)%>" size="50" class="textenter">
            </td>
          </tr>
        </table>
<jsp:include page="/forms/simpleCommon_table.jsp" flush="true"/>
    </form>
</div>
<script language="javascript">
  closeWSDLBrowser();
</script>  
</body>
</html>
