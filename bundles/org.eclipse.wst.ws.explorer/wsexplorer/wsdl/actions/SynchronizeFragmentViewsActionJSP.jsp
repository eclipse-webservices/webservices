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
                                                        org.eclipse.wst.ws.internal.explorer.platform.wsdl.constants.WSDLFrameNames,
                                                        org.eclipse.wst.ws.internal.explorer.platform.wsdl.constants.FragmentConstants,
                                                        org.eclipse.wst.ws.internal.explorer.platform.wsdl.actions.SynchronizeFragmentViewsAction"%>

<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<jsp:include page="/wsdl/scripts/wsdlpanes.jsp" flush="true"/>

<%
SynchronizeFragmentViewsAction action = new SynchronizeFragmentViewsAction(controller);
boolean paramValid = action.populatePropertyTable(request);
String viewID = (String)action.getPropertyTable().get(FragmentConstants.FRAGMENT_VIEW_ID);
boolean sourceToForm = viewID.equals(FragmentConstants.FRAGMENT_VIEW_SWITCH_SOURCE_TO_FORM);
if (sourceToForm) {
  if (paramValid) {
%>
    <script language="javascript">
      if (!confirm("<%=controller.getWSDLPerspective().getMessage("MSG_QUESTION_SYNC_VIEWS")%>"))
        perspectiveWorkArea.location = "<%=response.encodeURL(controller.getPathWithContext("wsdl/actions/SynchronizeFragmentViewsActionAbortJSP.jsp"))%>";
    </script>
<%
  }
  else {
%>
    <script language="javascript">
      if (!confirm("<%=controller.getWSDLPerspective().getMessage("MSG_QUESTION_SYNC_VIEWS_DESPITE_INVALID_SOURCE_CONTENT")%>"))
        perspectiveWorkArea.location = "<%=response.encodeURL(controller.getPathWithContext("wsdl/actions/SynchronizeFragmentViewsActionAbortJSP.jsp"))%>";
    </script>
<%
  }
}
action.execute();
%>
<script language="javascript">
  wsdlPropertiesContent.location = "<%=response.encodeURL(controller.getPathWithContext("wsdl/wsdl_properties_content.jsp"))%>";
</script>

<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body>
</body>
</html>
