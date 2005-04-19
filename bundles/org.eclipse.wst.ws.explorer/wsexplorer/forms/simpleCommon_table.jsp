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
<%@ page contentType="text/html; charset=UTF-8" %>

<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<table border=0 cellpadding=2 cellspacing=0>
  <tr>
    <td height=30 valign="bottom" align="left" nowrap>
      <input type="submit" value="<%=controller.getMessage("FORM_BUTTON_GO")%>" class="button">
    </td>
    <td height=30 valign="bottom" align="left" nowrap>
      <input type="button" value="<%=controller.getMessage("FORM_BUTTON_RESET")%>" onClick="resetFormInputs()" class="button">
    </td>
    <td nowrap width="90%">&nbsp;</td>
  </tr>
</table>
