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
<%@ page contentType="text/html; charset=UTF-8" import="org.eclipse.wst.ws.internal.explorer.platform.wsdl.perspective.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.wsdl.constants.*" %>

<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<%
   WSDLPerspective wsdlPerspective = controller.getWSDLPerspective();
   int viewID = wsdlPerspective.getStatusContentType();
%>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title><%=wsdlPerspective.getMessage("FRAME_TITLE_STATUS_CONTENT")%></title>
  <link rel="stylesheet" type="text/css" href="<%=response.encodeURL(controller.getPathWithContext("css/windows.css"))%>">
<jsp:include page="/scripts/tables.jsp" flush="true"/>
<jsp:include page="/scripts/panes.jsp" flush="true"/>
<script language="javascript">
  function switchReadOnlyFragmentsView() {
    perspectiveWorkArea.location = "<%=response.encodeURL(controller.getPathWithContext("wsdl/actions/SwitchReadOnlyFragmentViewsActionJSP.jsp"))%>";
  }
</script>
</head>
<body dir="<%=org.eclipse.wst.ws.internal.explorer.platform.util.DirUtils.getDir()%>" class="contentbodymargin">
<div id="contentborder">
  <table width="95%" border=0 cellpadding=3 cellspacing=0>
    <tr>
      <td align="right" class="labels">
<%
        if (viewID == wsdlPerspective.STATUS_CONTENT_RESULT_SOURCE)
        {     
%>
          <a href="javascript:switchReadOnlyFragmentsView()" title="<%=wsdlPerspective.getMessage("ALT_SWITCH_TO_FORM_VIEW")%>"><%=wsdlPerspective.getMessage("FORM_LINK_FORM")%></a>
<%
        }
        else
        {
%>
          <a href="javascript:switchReadOnlyFragmentsView()" title="<%=wsdlPerspective.getMessage("ALT_SWITCH_TO_SOURCE_VIEW")%>"><%=wsdlPerspective.getMessage("FORM_LINK_SOURCE")%></a>
<%
        }
%>   
      </td>
    </tr>
  </table>
<%
  if (viewID == wsdlPerspective.STATUS_CONTENT_RESULT_SOURCE)
  {
%>
    <jsp:include page="/wsdl/forms/ReadOnlyFragmentsSoapView.jsp" flush="true"/>
<%
  }
  else
  {
%>
    <jsp:include page="/wsdl/forms/ReadOnlyFragmentsFormView.jsp" flush="true"/>
<%
  }
%>
</div>
</body>
</html>
