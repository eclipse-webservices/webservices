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
<%@ page contentType="text/html; charset=UTF-8" import="org.eclipse.wst.ws.internal.explorer.platform.uddi.perspective.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.uddi.constants.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.perspective.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.util.*" %>

<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<jsp:useBean id="sectionHeaderInfo" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.SectionHeaderInfo" scope="request"/>
<%
   UDDIPerspective uddiPerspective = controller.getUDDIPerspective();
   NodeManager navigatorManager = uddiPerspective.getNavigatorManager();
   Node selectedNode = navigatorManager.getSelectedNode();
   FormToolPropertiesInterface formToolPI = (FormToolPropertiesInterface)(selectedNode.getCurrentToolManager().getSelectedTool());
%>
<table width="95%" border=0 cellpadding=3 cellspacing=0>
  <tr>
    <td colspan=3 height=40 valign="bottom" align="left" class="labels">
      <%=uddiPerspective.getMessage("FORM_LABEL_MAX_SEARCH_SET")%>
<%
   if (!formToolPI.isInputValid(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_MAX_SEARCH_SET))
   {
%>
      <%=HTMLUtils.redAsterisk()%>
<%
   }
%>
    </td>
  </tr>
  <tr>
    <td colspan=3>
      <input type="text" name="<%=UDDIActionInputs.QUERY_INPUT_ADVANCED_MAX_SEARCH_SET%>" class="smtextenter">
    </td>
  </tr>
  <tr>
    <td colspan=3 class="labels">
      <%=uddiPerspective.getMessage("FORM_LABEL_MAX_RESULTS")%>
<%
   if (!formToolPI.isInputValid(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_MAX_RESULTS))
   {
%>
      <%=HTMLUtils.redAsterisk()%>
<%
   }
%>
    </td>
  </tr>
  <tr>
    <td colspan=3>
      <input type="text" name="<%=UDDIActionInputs.QUERY_INPUT_ADVANCED_MAX_RESULTS%>" class="smtextenter">
    </td>
  </tr>
</table>
