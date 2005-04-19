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
    <td align="left" nowrap>
      <img src="<%=response.encodeURL(controller.getPathWithContext(titleImagePath))%>">
      <strong><%=title%></strong>
    </td>
<%
   int colspan = 1;
   if (hasTarget)
   {
     String returnLink = TransferSubQueryResultsAction.getActionLinkForReturn();
%>
    <td nowrap width="*">&nbsp;</td>
    <td align="right" nowrap>
      <a href="<%=response.encodeURL(controller.getPathWithContext(returnLink))%>" target="<%=FrameNames.PERSPECTIVE_WORKAREA%>"><%=uddiPerspective.getMessage("FORM_LINK_TARGET")%></a>
    </td>
<%
     colspan += 2;
   }
%>
  </tr>
  <tr>
    <td height=20 colspan=<%=colspan%>> <img height=2 width="100%" align="top" src="<%=response.encodeURL(controller.getPathWithContext("images/keyline.gif"))%>"> </td>
  </tr>
  <tr>
    <td colspan=<%=colspan%>>
<%
   if (hasTarget)
   {
%>
<%=uddiPerspective.getMessage("FORM_LABEL_SUBQUERY")%>
<%
   }
%>
<%=uddiPerspective.getMessage("FORM_LABEL_QUERY")%>
    </td>
  </tr>
</table>
