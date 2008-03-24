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
<table width="95%" border=0 cellpadding=3 cellspacing=0>
  <tr>
    <td>
      <img src="<%=response.encodeURL(controller.getPathWithContext(titleImagePath))%>" alt="">
      <strong><%=title%></strong>
    </td>
    <td width="*">&nbsp;</td>
<%
   int colspan = 2;
   if (subQueryKey != null && subQueryKey.length() > 0)
   {
     int nodeId = selectedNode.getNodeId();
     int toolId = formTool.getToolId();
     int viewId = selectedNode.getViewId();
     int viewToolId = selectedNode.getViewToolId();
     int count = 1;
%>
    <td align="right">
      <a href="<%=response.encodeURL(controller.getPathWithContext(SelectFindToolAction.getActionLink(nodeId,toolId,viewId,viewToolId,"",false)))%>"><%=count%></a>
    </td>
<%
     String currentSubQueryKey = subQueryKey;
     int separatorPos = -1;
     while ((separatorPos = currentSubQueryKey.indexOf(':')) != -1)
     {
       count++;
%>
    <td align="right">
      <a href="<%=response.encodeURL(controller.getPathWithContext(SelectFindToolAction.getActionLink(nodeId,toolId,viewId,viewToolId,currentSubQueryKey.substring(0,separatorPos),false)))%>"><%=count%></a>
    </td>      
<%   
       currentSubQueryKey = currentSubQueryKey.substring(separatorPos+1,currentSubQueryKey.length());
     }
     colspan += count;
   }
%>    
  </tr>
  <tr>
    <td height=20 colspan=<%=colspan%>> <img height=2 width="100%" align="top" src="<%=response.encodeURL(controller.getPathWithContext("images/keyline.gif"))%>" alt=""> </td>
  </tr>
</table>
