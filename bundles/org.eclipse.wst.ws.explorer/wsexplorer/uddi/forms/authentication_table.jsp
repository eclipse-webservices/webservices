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
                                                        org.eclipse.wst.ws.internal.explorer.platform.uddi.datamodel.AbstractUDDIElement,
                                                        org.eclipse.wst.ws.internal.explorer.platform.uddi.datamodel.RegistryElement,
                                                        org.eclipse.wst.ws.internal.explorer.platform.perspective.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.datamodel.TreeElement,
                                                        org.eclipse.wst.ws.internal.explorer.platform.util.*" %>

<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<jsp:useBean id="subQueryKeyProperty" class="org.eclipse.wst.ws.internal.explorer.platform.uddi.perspective.SubQueryKeyProperty" scope="request"/>
<jsp:useBean id="sectionHeaderInfo" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.SectionHeaderInfo" scope="request"/>
<%
UDDIPerspective uddiPerspective = controller.getUDDIPerspective();
NodeManager navigatorManager = uddiPerspective.getNavigatorManager();
Node selectedNode = navigatorManager.getSelectedNode();
TreeElement selectedElement = selectedNode.getTreeElement();
FormTool formTool = (FormTool)(selectedNode.getCurrentToolManager().getSelectedTool());
String tableContainerId = sectionHeaderInfo.getContainerId();
String username = null;
String password = null;
if (selectedElement instanceof AbstractUDDIElement)
{
  RegistryElement regElement = ((AbstractUDDIElement)selectedElement).getRegistryElement();
  username = regElement.getUserId();
  password = regElement.getCred();
}
if (username == null)
  username = "";
if (password == null)
  password = "";
%>
<div id="<%=tableContainerId%>" style="display:none;">
  <table width="95%" border=0 cellpadding=3 cellspacing=0>
    <tr>
      <td class="labels" height=30 valign="bottom">
        <label for="<%=tableContainerId+"_input_publish_url"%>"><%=uddiPerspective.getMessage("FORM_LABEL_PUBLISH_URL")%></label>
        <%
        if (!formTool.isInputValid(UDDIActionInputs.QUERY_INPUT_ADVANCED_PUBLISH_URL))
        {
        %>
          <%=HTMLUtils.redAsterisk()%>
        <%
        }
        %>
      </td>
    </tr>
    <tr>
      <td><input type="text" id="<%=tableContainerId+"_input_publish_url"%>" name="<%=UDDIActionInputs.QUERY_INPUT_ADVANCED_PUBLISH_URL%>" class="textenter"></td>
    </tr>
    <tr>
      <td class="labels" height=30 valign="bottom">
        <label for="<%=tableContainerId+"_input_user_id"%>"><%=uddiPerspective.getMessage("FORM_LABEL_USERID")%></label>
        <%
        if (!formTool.isInputValid(UDDIActionInputs.QUERY_INPUT_ADVANCED_USERID))
        {
        %>
          <%=HTMLUtils.redAsterisk()%>
        <%
        }
        %>
      </td>
    </tr>
    <tr>
      <td><input type="text" id="<%=tableContainerId+"_input_user_id"%>" name="<%=UDDIActionInputs.QUERY_INPUT_ADVANCED_USERID%>" value="<%=username%>" class="textenter"></td>
    </tr>
    <tr>
      <td class="labels" height=30 valign="bottom"><label for="<%=tableContainerId+"_input_password"%>"><%=uddiPerspective.getMessage("FORM_LABEL_PASSWORD")%></td></label>
    </tr>
    <tr>
      <td><input type="password" id="<%=tableContainerId+"_input_password"%>" name="<%=UDDIActionInputs.QUERY_INPUT_ADVANCED_PASSWORD%>" value="<%=password%>" class="textenter"></td>
    </tr>
  </table>
</div>
