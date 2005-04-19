<%
/*******************************************************************************
 * Copyright (c) 2002, 2004 IBM Corporation and others.
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
                                   					                    org.eclipse.wst.ws.internal.explorer.platform.wsdl.perspective.WSDLPerspective"%>

<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<jsp:include page="/wsdl/scripts/wsdlpanes.jsp" flush="true"/>

<%
WSDLPerspective wsdlPerspective = controller.getWSDLPerspective();
if (wsdlPerspective.getStatusContentType() == WSDLPerspective.STATUS_CONTENT_RESULT_FORM)
  wsdlPerspective.setStatusContentType(WSDLPerspective.STATUS_CONTENT_RESULT_SOURCE);
else
  wsdlPerspective.setStatusContentType(WSDLPerspective.STATUS_CONTENT_RESULT_FORM);
%>
<script language="javascript">
  wsdlStatusContent.location = "<%=response.encodeURL(controller.getPathWithContext("wsdl/wsdl_result_content.jsp"))%>";
</script>

<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body>
</body>
</html>
