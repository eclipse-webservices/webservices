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
<%@ page contentType="text/html; charset=UTF-8" import="org.eclipse.wst.ws.internal.explorer.platform.perspective.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.constants.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.actions.*" %>

<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<%
   Perspective currentPerspective = controller.getCurrentPerspective();
   StringBuffer framesetsFile = new StringBuffer("/");
   framesetsFile.append(currentPerspective.getFramesetsFile());
   
   StringBuffer framesetsForm = new StringBuffer("/");
   framesetsForm.append(currentPerspective.getProcessFramesetsForm());
   
   int targetPerspectiveId;
   boolean isHistory;
   try
   {
     targetPerspectiveId = Integer.parseInt(request.getParameter(ActionInputs.PERSPECTIVE));
     isHistory = ("1".equals(request.getParameter(ActionInputs.ISHISTORY)));
   }
   catch (NumberFormatException e)
   {
     targetPerspectiveId = ActionInputs.PERSPECTIVE_UDDI;
     isHistory = false;
   }
   
   if (controller.isPerspectiveContentBlank())
   {
     controller.enablePerspectiveContentBlank(false);
     controller.addToHistory(currentPerspective.getPerspectiveId(),ShowPerspectiveAction.getActionLink(targetPerspectiveId,true));
%>
<jsp:include page="/scripts/switchperspective.jsp" flush="true"/>
<%
   }
   else
   {
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 3.2 Final//EN">
<html lang="<%=response.getLocale().getLanguage()%>">
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <jsp:include page="<%=framesetsFile.toString()%>" flush="true"/>
</head>
<body dir="<%=org.eclipse.wst.ws.internal.explorer.platform.util.DirUtils.getDir()%>">
<jsp:useBean id="formAction" class="java.lang.StringBuffer" scope="request">
<%
   formAction.append(currentPerspective.getSwitchPerspectiveFormActionLink(targetPerspectiveId,isHistory));
%>
</jsp:useBean>
<jsp:useBean id="formFrameName" class="java.lang.StringBuffer" scope="request"/>
<jsp:include page="<%=framesetsForm.toString()%>" flush="true"/>
<script language="javascript">
  processFramesetSizes(document.forms[0]);
</script>
</body>
</html>
<%
   }
%>   
