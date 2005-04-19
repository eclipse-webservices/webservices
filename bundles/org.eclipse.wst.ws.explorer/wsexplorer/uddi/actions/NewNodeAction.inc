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
<%
   if (!inputsValid)
   {
%>
<script language="javascript">
  propertiesContent.location = "<%=response.encodeURL(controller.getPathWithContext("uddi/properties_content.jsp"))%>";
  statusContent.location = "<%=response.encodeURL(controller.getPathWithContext("uddi/status_content.jsp"))%>";
</script>
<%
   }
   else
   {
     // Run the action and obtain the return code (fail/success).
     boolean actionResult = action.execute();

     if (actionResult)
     {
       // Ensure that the selected node is visible.
       controller.getUDDIPerspective().getNavigatorManager().makeSelectedNodeVisible();
%>
<script language="javascript">
  if (isMicrosoftInternetExplorer())
  {
    navigatorContent.location = "<%=response.encodeURL(controller.getPathWithContext("uddi/navigator_content.jsp"))%>";
    propertiesContainer.location = "<%=response.encodeURL(controller.getPathWithContext("uddi/properties_container.jsp"))%>";
    statusContent.location = "<%=response.encodeURL(controller.getPathWithContext("uddi/status_content.jsp"))%>";
  }
  else
    perspectiveContent.location = "<%=response.encodeURL(controller.getPathWithContext("uddi/uddi_perspective_content.jsp"))%>";
</script>
<%
     }
     else
     {
%>
<script language="javascript">
  propertiesContent.location = "<%=response.encodeURL(controller.getPathWithContext("uddi/properties_content.jsp"))%>";
  statusContent.location = "<%=response.encodeURL(controller.getPathWithContext("uddi/status_content.jsp"))%>";
</script>
<%
     }
   }
%>
