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
<%@ page contentType="text/html; charset=UTF-8" import="org.eclipse.wst.ws.internal.explorer.platform.uddi.perspective.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.uddi.actions.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.perspective.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.constants.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.util.*,
                                                        java.util.*" %>

<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 3.2 Final//EN">
<html lang="<%=response.getLocale().getLanguage()%>">
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script language="javascript" src="<%=response.encodeURL(controller.getPathWithContext("scripts/browserdetect.js"))%>">
</script>
<jsp:include page="/uddi/scripts/uddipanes.jsp" flush="true"/>
<jsp:include page="/scripts/formutils.jsp" flush="true"/>
<%
   // Prepare the action.
   RefreshUDDINodesAction action = new RefreshUDDINodesAction(controller);

   // Load the parameters for the action from the servlet request.
   boolean inputsValid = action.populatePropertyTable(request);
   
   // Run the action
   boolean actionResult = action.execute();
%>
<script language="javascript">
  function processClearNodesForm()
  {
    var clearNodesForm = document.forms[0];
<%
     Vector staleNodes = action.getStaleNodes();
     UDDIPerspective uddiPerspective = controller.getUDDIPerspective();
     for (int i=0;i<staleNodes.size();i++)
     {
       Node staleNode = (Node)staleNodes.elementAt(i);
%>
    if (confirm("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("MSG_QUESTION_REMOVE_NODE",staleNode.getNodeName()))%>"))
      clearNodesForm.appendChild(createHiddenElement("<%=ActionInputs.NODEID%>",<%=staleNode.getNodeId()%>));
<%    
     }
%>
    clearNodesForm.submit();
  }         
</script>
</head>
<body dir="<%=org.eclipse.wst.ws.internal.explorer.platform.util.DirUtils.getDir()%>">
<form action="<%=response.encodeURL(controller.getPathWithContext("uddi/actions/ClearNavigatorNodesActionJSP.jsp"))%>" target="<%=FrameNames.PERSPECTIVE_WORKAREA%>" method="post" enctype="multipart/form-data">
</form>
<script language="javascript">
  processClearNodesForm();
</script>  
</body>
</html>
