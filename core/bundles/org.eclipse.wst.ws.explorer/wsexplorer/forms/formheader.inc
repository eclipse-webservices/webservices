<%
/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
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
  </tr>
  <tr>
    <td height=20> <img height=2 width="100%" align="top" src="<%=response.encodeURL(controller.getPathWithContext("images/keyline.gif"))%>" alt=""> </td>
  </tr>
</table>
