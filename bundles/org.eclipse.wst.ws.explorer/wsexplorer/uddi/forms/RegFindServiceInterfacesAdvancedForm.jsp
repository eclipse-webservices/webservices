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
                                                        org.eclipse.wst.ws.internal.explorer.platform.constants.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.util.*" %>

<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<jsp:useBean id="sectionHeaderInfo" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.SectionHeaderInfo" scope="request"/>
<jsp:useBean id="subQueryKeyProperty" class="org.eclipse.wst.ws.internal.explorer.platform.uddi.perspective.SubQueryKeyProperty" scope="request"/>
<%
   UDDIPerspective uddiPerspective = controller.getUDDIPerspective();
   NodeManager navigatorManager = uddiPerspective.getNavigatorManager();
   // selectedNode could be the registry or query nodes.
   Node selectedNode = navigatorManager.getSelectedNode();
   FormTool formTool = (FormTool)(selectedNode.getCurrentToolManager().getSelectedTool());
   FormToolPropertiesInterface formToolPI = ((MultipleFormToolPropertiesInterface)formTool).getFormToolProperties(subQueryKeyProperty.getSubQueryKey());
%>
<div id="findServiceInterfacesAdvanced" style="display:none;">
  <form action="<%=response.encodeURL(controller.getPathWithContext("uddi/actions/RegFindServiceInterfacesAdvancedActionJSP.jsp"))%>" method="post" target="<%=FrameNames.PERSPECTIVE_WORKAREA%>" enctype="multipart/form-data" onSubmit="return processFindServiceInterfacesAdvancedForm(this)">
    <input type="hidden" name="<%=UDDIActionInputs.SUBQUERY_KEY%>">
    <input type="hidden" name="<%=UDDIActionInputs.SUBQUERY_GET%>">    
    <input type="hidden" name="<%=UDDIActionInputs.NEW_SUBQUERY_INITIATED%>">
    <input type="hidden" name="<%=UDDIActionInputs.NEW_SUBQUERY_QUERY_ITEM%>">
    <input type="hidden" name="<%=UDDIActionInputs.QUERY_NAME%>">
    <table width="95%" border=0 cellpadding=3 cellspacing=0>
      <tr>
        <td class="labels" height=20 valign="bottom"> <%=uddiPerspective.getMessage("FORM_LABEL_FIND_ADVANCED_DESC")%> </td>
      </tr>
      <tr>
        <td height=40 valign="bottom">
          <input type="checkbox" id="input_find_service_interface_advance_use_service" name="<%=UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_USE_SERVICE%>" onClick="toggleFindServiceInterfacesAdvancedServiceParameters(this.form)"><label for="input_find_service_interface_advance_use_service"><%=uddiPerspective.getMessage("FORM_LABEL_USE_SERVICE")%></label>
        </td>
      </tr>
    </table>
<%
   String[] serviceSpecificInfo = {"FORM_LABEL_SERVICE",Boolean.FALSE.toString(),String.valueOf(UDDIActionInputs.QUERY_ITEM_SERVICES),"findServiceInterfacesAdvanced",UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_SERVICE};
   sectionHeaderInfo.clear();
   sectionHeaderInfo.setContainerId("findServiceInterfacesAdvancedService");
   sectionHeaderInfo.setOtherProperties(serviceSpecificInfo);
%>
<div id="findServiceInterfacesAdvancedServiceSection" style="display:none;">
  <jsp:include page="/uddi/forms/uddiObjects_table.jsp" flush="true"/>
</div>
    <div id="findServiceInterfacesAdvancedOwned">
      <table width="95%" border=0 cellpadding=3 cellspacing=0>
        <tr>
          <td height=35 valign="bottom">
            <input type="checkbox" id="input_find_service_interface_advance_owned" name="<%=UDDIActionInputs.QUERY_INPUT_ADVANCED_OWNED%>" onClick="toggleAdvancedAuthenticationSection(<%=UDDIActionInputs.QUERY_ITEM_SERVICE_INTERFACES%>,this.checked)">
            <label for="input_find_service_interface_advance_owned"><%=uddiPerspective.getMessage("FORM_LABEL_OWNED")%></label>
          </td>
        </tr>
      </table>
    </div>
<%
   sectionHeaderInfo.clear();
   sectionHeaderInfo.setContainerId("findServiceInterfacesAdvancedAuthentication");
%>
<jsp:include page="/uddi/forms/authentication_table.jsp" flush="true"/>
    <div id="findServiceInterfacesAdvancedName">
      <table width="95%" border=0 cellpadding=3 cellspacing=0>
        <tr>
          <td class="labels" height=40 valign="bottom">
            <label for="input_find_service_interface_advanced_name"><%=uddiPerspective.getMessage("FORM_LABEL_NAME")%></label>
<%
   if (!formToolPI.isInputValid(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_NAME))
   {
%>
            <%=HTMLUtils.redAsterisk()%>
<%
   }
%>
          </td>
        </tr>
        <tr>
          <td><input type="text" id="input_find_service_interface_advanced_name" name="<%=UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_NAME%>" class="textenter"></td>
        </tr>
      </table>
<div id="findServiceInterfacesAdvancedNameFindQualifiersSection">
<jsp:include page="/uddi/forms/names_findQualifiers_table.jsp" flush="true"/>
</div>
    </div>
<%
   sectionHeaderInfo.clear();
   sectionHeaderInfo.setContainerId("findServiceInterfacesAdvancedIdentifiers");
%>
<jsp:include page="/uddi/forms/identifiers_table.jsp" flush="true"/>
<%
   sectionHeaderInfo.clear();
   sectionHeaderInfo.setContainerId("findServiceInterfacesAdvancedCategories");
%>
<jsp:include page="/uddi/forms/categories_table.jsp" flush="true"/>
<%
   sectionHeaderInfo.clear();
   sectionHeaderInfo.setContainerId("findServiceInterfacesAdvancedFindQualifiers");
%>
<div id="findServiceInterfacesAdvancedFindQualifiersSection">
<jsp:include page="/uddi/forms/findQualifiers_table.jsp" flush="true"/>
</div>
<jsp:include page="/uddi/forms/advancedCommon_table.jsp" flush="true"/>
<jsp:include page="/forms/simpleCommon_table.jsp" flush="true"/>
  </form>
</div>
