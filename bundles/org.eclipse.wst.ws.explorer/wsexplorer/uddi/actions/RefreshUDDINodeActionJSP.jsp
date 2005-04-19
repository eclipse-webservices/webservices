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
<%@ page contentType="text/html; charset=UTF-8" import="org.eclipse.wst.ws.internal.explorer.platform.uddi.actions.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.uddi.perspective.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.perspective.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.constants.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.util.*,
                                                        java.util.*" %>

<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script language="javascript" src="<%=response.encodeURL(controller.getPathWithContext("scripts/browserdetect.js"))%>">
</script>
<jsp:include page="/uddi/scripts/uddipanes.jsp" flush="true"/>
<jsp:include page="/scripts/formutils.jsp" flush="true"/>
<%
   // Prepare the action.
   RefreshUDDINodeAction action = new RefreshUDDINodeAction(controller);

   // The action may be executed via program link.
   action.populatePropertyTable(request);
   boolean isNodeDataValid = action.verifyNodeData();

   boolean inputsValid = true;
   String onLoadAction;
   UDDIPerspective uddiPerspective = controller.getUDDIPerspective();
%>   
<script language="javascript">
  function processClearNodesForm()
  {
    var clearNodesForm = document.forms[0];
    var submitClearNodesForm = false;
<%
     Vector staleNodes = action.getStaleNodes();
     for (int i=0;i<staleNodes.size();i++)
     {
       Node staleNode = (Node)staleNodes.elementAt(i);
%>
    if (confirm("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("MSG_QUESTION_REMOVE_NODE",staleNode.getNodeName()))%>"))
    {
      clearNodesForm.appendChild(createHiddenElement("<%=ActionInputs.NODEID%>",<%=staleNode.getNodeId()%>));
      submitClearNodesForm = true;
    }
<%
     }
%>
    if (submitClearNodesForm)
      clearNodesForm.submit();
  }
</script>    
</head>
<body>
<form action="<%=response.encodeURL(controller.getPathWithContext("uddi/actions/ClearNavigatorNodesActionJSP.jsp"))%>" target="<%=FrameNames.PERSPECTIVE_WORKAREA%>" method="post" enctype="multipart/form-data">
</form>
<%
   if (!isNodeDataValid)
   {
     int nodeId = Integer.parseInt((String)(action.getPropertyTable().get(ActionInputs.NODEID)));
     String nodeName = uddiPerspective.getNavigatorManager().getNode(nodeId).getNodeName();
%>
<script language="javascript">
  if (confirm("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("MSG_QUESTION_REMOVE_NODE",nodeName))%>"))
    perspectiveWorkArea.location = "<%=response.encodeURL(controller.getPathWithContext(ClearNavigatorNodeAction.getActionLink(nodeId)))%>";
</script>    
<%
   }
   else
   {
%>
<script language="javascript">
  processClearNodesForm();
</script>
<%@ include file = "/uddi/actions/NewNodeAction.inc" %>
<%
   }
%>   
</body>
</html>
