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
<jsp:useBean id="subQueryKeyProperty" class="org.eclipse.wst.ws.internal.explorer.platform.uddi.perspective.SubQueryKeyProperty" scope="request"/>
<jsp:useBean id="sectionHeaderInfo" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.SectionHeaderInfo" scope="request"/>
<script language="javascript">
  function processPublishServiceAdvancedForm(form)
  {
    if (handleSubmit(form))
    {
      processResultTable("publishServiceAdvancedServiceInterface","<%=UDDIActionInputs.NODEID_SERVICE_INTERFACE%>",form,false);
      processLanguageInputTable("publishServiceAdvancedNames","<%=UDDIActionInputs.QUERY_INPUT_ADVANCED_NAME_LANGUAGE%>","<%=UDDIActionInputs.QUERY_INPUT_ADVANCED_NAME%>",form);
      processLanguageInputTable("publishServiceAdvancedDescriptions","<%=UDDIActionInputs.QUERY_INPUT_ADVANCED_DESCRIPTION_LANGUAGE%>","<%=UDDIActionInputs.QUERY_INPUT_ADVANCED_DESCRIPTION%>",form);
      processCategoryTable("publishServiceAdvancedCategories",form,false);
      return true;
    }
    return false;
  }
</script>
<%
   UDDIPerspective uddiPerspective = controller.getUDDIPerspective();
   NodeManager navigatorManager = uddiPerspective.getNavigatorManager();
   Node selectedNode = navigatorManager.getSelectedNode();
   FormTool formTool = (FormTool)(selectedNode.getCurrentToolManager().getSelectedTool());
   FormToolPropertiesInterface formToolPI = ((MultipleFormToolPropertiesInterface)formTool).getFormToolProperties(subQueryKeyProperty.getSubQueryKey());
%>
<div id="publishServiceAdvanced" style="display:none;">
  <form action="<%=response.encodeURL(controller.getPathWithContext("uddi/actions/BusPublishServiceAdvancedActionJSP.jsp"))%>" method="post" target="<%=FrameNames.PERSPECTIVE_WORKAREA%>" enctype="multipart/form-data" onSubmit="return processAdvancedForm(this)">
    <input type="hidden" name="subQueryGet">
    <input type="hidden" name="<%=UDDIActionInputs.NODEID_BUSINESS%>" value="<%=selectedNode.getNodeId()%>">  
    <input type="hidden" name="<%=UDDIActionInputs.NEW_SUBQUERY_INITIATED%>">
    <input type="hidden" name="<%=UDDIActionInputs.NEW_SUBQUERY_QUERY_ITEM%>">
    <table width="95%" border=0 cellpadding=3 cellspacing=0>
      <tr>
        <td class="labels" height=10 valign="bottom">
          <%=uddiPerspective.getMessage("FORM_LABEL_BUSINESS_PUBLISH_SERVICE_ADVANCED_DESC")%>
        </td>
      </tr>
    </table>
<%
   sectionHeaderInfo.clear();
   sectionHeaderInfo.setContainerId("publishServiceAdvancedAuthentication");
%>
<jsp:include page="/uddi/forms/authentication_table.jsp" flush="true"/>
    <table width="95%" border=0 cellpadding=3 cellspacing=0>
      <tr>
        <td nowrap class="labels" height=30 valign="bottom">
          <label for="input_business_publish_service_advance_wsdl_url"><%=controller.getMessage("FORM_LABEL_WSDL_URL")%></label>
<%
   if (!formToolPI.isInputValid(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_WSDL_URL))
   {
%>
          <%=HTMLUtils.redAsterisk()%>
<%
   }
%>
        </td>
        <td nowrap height=30 valign="bottom">
          <a href="javascript:openWSDLBrowser('publishServiceAdvanced',<%=ActionInputs.WSDL_TYPE_SERVICE%>)"><%=controller.getMessage("FORM_LINK_BROWSE")%></a>
        </td>
        <td width="90%">&nbsp;</td>
      </tr>
      <tr>
        <td colspan=3>
          <input type="text" id="input_business_publish_service_advance_wsdl_url" name="<%=ActionInputs.QUERY_INPUT_WSDL_URL%>" class="textenter">
        </td>
      </tr>
    </table>
<%
   boolean tableHasErrors = !formToolPI.isInputValid(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_SERVICE_INTERFACES);
   String[] serviceInterfaceSpecificInfo = {"FORM_LABEL_SERVICE_INTERFACES",(new Boolean(tableHasErrors)).toString(),String.valueOf(UDDIActionInputs.QUERY_ITEM_SERVICE_INTERFACES),"publishServiceAdvanced",UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_SERVICE_INTERFACES};
   sectionHeaderInfo.clear();
   sectionHeaderInfo.setContainerId("publishServiceAdvancedServiceInterface");
   sectionHeaderInfo.setOtherProperties(serviceInterfaceSpecificInfo);
%>
<jsp:include page="/uddi/forms/uddiObjects_table.jsp" flush="true"/>
<%
   sectionHeaderInfo.clear();
   sectionHeaderInfo.setContainerId("publishServiceAdvancedNames");
   String[] nameSpecificInfo = {"FORM_LABEL_NAMES","FORM_LABEL_NAME",String.valueOf(!formToolPI.isInputValid(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_NAMES))};
   sectionHeaderInfo.setOtherProperties(nameSpecificInfo);
%>
<jsp:include page="/uddi/forms/languageInput_table.jsp" flush="true"/>
<%
   sectionHeaderInfo.clear();
   sectionHeaderInfo.setContainerId("publishServiceAdvancedDescriptions");
   String[] descSpecificInfo = {"FORM_LABEL_DESCRIPTIONS","FORM_LABEL_DESCRIPTION",String.valueOf(!formToolPI.isInputValid(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_DESCRIPTIONS))};
   sectionHeaderInfo.setOtherProperties(descSpecificInfo);
%>
<jsp:include page="/uddi/forms/languageInput_table.jsp" flush="true"/>
<%
   sectionHeaderInfo.clear();
   sectionHeaderInfo.setContainerId("publishServiceAdvancedCategories");
%>
<jsp:include page="/uddi/forms/categories_table.jsp" flush="true"/>
<jsp:include page="/forms/simpleCommon_table.jsp" flush="true"/>
  </form>
</div>
