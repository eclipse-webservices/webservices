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

<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<jsp:useBean id="currentToolManagerHash" class="java.util.Hashtable" scope="request"/>
<%
   ToolManager currentToolManager = (ToolManager)currentToolManagerHash.get(ActionInputs.CURRENT_TOOL_MANAGER);
   int numberOfTools = currentToolManager.getNumberOfTools();
   if (numberOfTools > 1)
   {
%>
<table>
  <tr>
    <td height=20>&nbsp;</td>
  </tr>
</table>
<table width="95%" border=0 cellpadding=3 cellspacing=0>
  <tr>
    <td>
      <strong><%=controller.getMessage("ALT_OTHER_ACTIONS")%></strong>
    </td>
  </tr>
  <tr>
    <td height=20> <img height=2 width="100%" align="top" src="<%=response.encodeURL(controller.getPathWithContext("images/keyline.gif"))%>"> </td>
  </tr>
</table>
<table>
<%
     for (int i=1;i<numberOfTools;i++)
     {
       Tool t = currentToolManager.getTool(i);
%>
  <tr>
    <td><a href="<%=response.encodeURL(controller.getPathWithContext(t.getSelectToolActionHref(false)))%>" target="<%=t.getSelectToolActionTarget()%>"><%=t.getAltText()%></a></td>
  </tr>
<%
     }
%>
</table>
<%
   }
%>       
