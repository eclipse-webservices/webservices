<%
/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
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
     boolean refreshResult = action.refreshFromRegistry();
     
     if (!refreshResult)
     {
       session.setAttribute(UDDIActionInputs.PUBLISH_ACTION,action);
%>
<script language="javascript">
  statusContent.location = "<%=response.encodeURL(controller.getPathWithContext("uddi/status_content.jsp"))%>";
  if (confirm("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("MSG_QUESTION_PUBLISH_ITEM"))%>"))
    perspectiveWorkArea.location = "<%=response.encodeURL(controller.getPathWithContext("uddi/actions/UpdateNewPublishActionJSP.jsp"))%>";
  else
    propertiesContent.requestSubmitted = false;
</script>
<%
     }
     else
     {
%>
<%@ include file="/uddi/actions/NewNodeAction.inc" %>
<%
     }
   }
%>
