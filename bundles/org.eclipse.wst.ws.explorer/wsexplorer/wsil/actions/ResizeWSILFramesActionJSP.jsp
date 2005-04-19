<%@ page contentType="text/html; charset=UTF-8" import="org.eclipse.wst.ws.internal.explorer.platform.wsil.perspective.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.wsil.actions.*" %>

<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<%
   // Create the action.
   ResizeWSILFramesAction action = new ResizeWSILFramesAction(controller);
   
   // Populate the action with the request properties.
   boolean result = action.populatePropertyTable(request);
%>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<jsp:include page="/wsil/scripts/wsilframesets.jsp" flush="true"/>
<script language="javascript" src="<%=response.encodeURL(controller.getPathWithContext("scripts/browserdetect.js"))%>">
</script>
</head>
<body>
<%
   if (result)
   {
     action.execute(false);
     
     WSILPerspective wsilPerspective = controller.getWSILPerspective();
%>
<script language="javascript">
  if (isMicrosoftInternetExplorer())
  {
    var perspectiveContentFrameset = getPerspectiveContentFrameset();
    var actionsContainerFrameset = getActionsContainerFrameset();
    perspectiveContentFrameset.setAttribute("cols","<%=wsilPerspective.getPerspectiveContentFramesetCols()%>");
    actionsContainerFrameset.setAttribute("rows","<%=wsilPerspective.getActionsContainerFramesetRows()%>");
  }
  else
    perspectiveContent.location = "<%=response.encodeURL(controller.getPathWithContext("wsil/wsil_perspective_content.jsp"))%>";
</script>
<%
   }
%>
</body>
</html>
