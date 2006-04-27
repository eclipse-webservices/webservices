<%
/*******************************************************************************
 * Copyright (c) 2001, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060427   136449 brunssen@us.ibm.com - Vince Brunssen  
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
<jsp:useBean id="sectionHeaderInfo" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.SectionHeaderInfo" scope="request"/>
<%
UDDIPerspective uddiPerspective = controller.getUDDIPerspective();
NodeManager navigatorManager = uddiPerspective.getNavigatorManager();
Node selectedNode = navigatorManager.getSelectedNode();
TreeElement selectedElement = selectedNode.getTreeElement();

RegistryElement regElement = ((AbstractUDDIElement)selectedElement).getRegistryElement();
String username = regElement.getUserId();
String password = regElement.getCred();
String publishURL = regElement.getPublishURL();

if (username == null) {
	username = "";
}

if (password == null) {
	password = "";
}

if (publishURL == null) {
	publishURL = "";
}

%>
  <input type="hidden" name="<%=UDDIActionInputs.QUERY_INPUT_ADVANCED_OWNED%>" value="true"/>
  <input type="hidden" name="<%=UDDIActionInputs.QUERY_NAME%>" value="Query Results"/>
  <input type="hidden" name="<%=UDDIActionInputs.QUERY_INPUT_ADVANCED_MAX_SEARCH_SET%>" value="100"/>
  <input type="hidden" name="<%=UDDIActionInputs.QUERY_INPUT_ADVANCED_MAX_RESULTS%>" value="10"/>
  <table width="95%" border=0 cellpadding=3 cellspacing=0>
    <tr>
      <td class="labels" height=30 valign="bottom">
         <label><%=uddiPerspective.getMessage("FORM_LABEL_PUBLISH_URL")%></label>
      </td>
    </tr>
    <tr>
       <td>
          <input type="text" name="<%=UDDIActionInputs.QUERY_INPUT_ADVANCED_PUBLISH_URL%>" value="<%=publishURL%>"/>
       </td>
    </tr>
    <tr>
      <td class="labels" height=30 valign="bottom">
        <label><%=uddiPerspective.getMessage("FORM_LABEL_USERID")%></label>
      </td>
    </tr>
    <tr>
      <td><input type="text" value="<%=username%>" name="<%=UDDIActionInputs.QUERY_INPUT_ADVANCED_USERID%>"></td>
    </tr>
    <tr>
      <td class="labels" height=30 valign="bottom"><label><%=uddiPerspective.getMessage("FORM_LABEL_PASSWORD")%></label></td>
    </tr>
    <tr>
      <td><input type="password" value ="<%=password%>" name="<%=UDDIActionInputs.QUERY_INPUT_ADVANCED_PASSWORD%>"></td>
    </tr>
  </table>
