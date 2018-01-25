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
<%@ page contentType="text/html; charset=UTF-8" import="org.uddi4j.util.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.uddi.perspective.*" %>

<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<%
   UDDIPerspective uddiPerspective = controller.getUDDIPerspective();
%>   
<table>
  <tr>
    <td>
      <input type="checkbox" id="find_qualifier_exact_name_match" value="<%=FindQualifier.exactNameMatch%>"><label for="find_qualifier_exact_name_match"><%=uddiPerspective.getMessage("FORM_LABEL_EXACT_NAME_MATCH")%></label>
    </td>
  </tr>
  <tr>
    <td>
      <input type="checkbox" id="find_qualifier_case_sensitive_match" value="<%=FindQualifier.caseSensitiveMatch%>"><label for="find_qualifier_case_sensitive_match"><%=uddiPerspective.getMessage("FORM_LABEL_CASE_SENSITIVE_MATCH")%></label>
    </td>
  </tr>
</table>
