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
                                                        org.eclipse.wst.ws.internal.explorer.platform.constants.*" %>

<%
   String sessionId = request.getParameter(ActionInputs.SESSIONID);
   Controller controller;
   StringBuffer targetContentLink = new StringBuffer(request.getParameter(ActionInputs.TARGET_PAGE));
   if (sessionId != null)
   {
     HttpSession currentSession = (HttpSession)application.getAttribute(sessionId);
     controller = (Controller)currentSession.getAttribute("controller");
     if (targetContentLink.toString().indexOf("?") != -1)
       targetContentLink.append('&');
     else
       targetContentLink.append('?');
     targetContentLink.append(ActionInputs.SESSIONID).append('=').append(sessionId);
   }
   else
     controller = (Controller)session.getAttribute("controller");
   
%>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title><%=controller.getMessage("FORM_TITLE_PAGE_LOADER")%></title>
  <link rel="stylesheet" type="text/css" href="<%=response.encodeURL(controller.getPathWithContext("css/windows.css"))%>">
<script language="javascript">
  function loadContent()
  {
    var frameset = parent.document.getElementById("proxyPage");
    var contentFrame = frameset.getElementsByTagName("frame").item(1);
    contentFrame.src = "<%=response.encodeURL(controller.getPathWithContext(targetContentLink.toString()))%>";  
  }
  
  function handleCompletion()
  {
    var table = document.getElementsByTagName("table").item(0);
    var tBody = table.getElementsByTagName("TBODY").item(0);
    var doneRow = document.createElement("tr");
    var doneColumn = document.createElement("td");
    doneColumn.appendChild(document.createTextNode("Page loaded."));
    doneRow.appendChild(doneColumn);
    tBody.appendChild(doneRow);   
  }
</script>  
</head>
<body dir="<%=org.eclipse.wst.ws.internal.explorer.platform.util.DirUtils.getDir()%>" class="contentbodymargin">
  <div id="contentborder">
    <table>
      <tr>
        <td class="labels">
          <%=controller.getMessage("MSG_LOAD_IN_PROGRESS")%>
        </td>
      </tr>
    </table>
  </div>
<script language="javascript">
  loadContent();
</script>
</body>
</html>
