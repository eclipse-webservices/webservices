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
                                                        org.eclipse.wst.ws.internal.explorer.platform.constants.*" %>

<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<jsp:useBean id="sectionHeaderInfo" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.SectionHeaderInfo" scope="request"/>
<jsp:useBean id="subQueryKeyProperty" class="org.eclipse.wst.ws.internal.explorer.platform.uddi.perspective.SubQueryKeyProperty" scope="request"/>
<%
   UDDIPerspective uddiPerspective = controller.getUDDIPerspective();
%>
<div id="findServicesAdvanced">
  <form action="<%=response.encodeURL(controller.getPathWithContext("uddi/actions/RegFindServicesAdvancedActionJSP.jsp"))%>" method="post" target="<%=FrameNames.PERSPECTIVE_WORKAREA%>" enctype="multipart/form-data" onSubmit="return processFindServicesAdvancedForm(this)">
    <input type="hidden" name="<%=UDDIActionInputs.SUBQUERY_KEY%>">
    <input type="hidden" name="<%=UDDIActionInputs.SUBQUERY_GET%>">    
    <input type="hidden" name="<%=UDDIActionInputs.NEW_SUBQUERY_INITIATED%>">
    <input type="hidden" name="<%=UDDIActionInputs.NEW_SUBQUERY_QUERY_ITEM%>">
    <input type="hidden" name="<%=UDDIActionInputs.QUERY_NAME%>">
    <table width="90%" border=0 cellpadding=3 cellspacing=0>
      <tr>
        <td class="labels" height=20 valign="bottom">
          <%=uddiPerspective.getMessage("FORM_LABEL_FIND_ADVANCED_DESC")%>
        </td>
      </tr>
    </table>
    <table width="95%" border=0 cellpadding=3 cellspacing=0>
      <tr>
        <td height=35 valign="bottom">
          <input type="checkbox" id="input_find_services_advance_owned" name="<%=UDDIActionInputs.QUERY_INPUT_ADVANCED_OWNED%>" onClick="toggleAdvancedAuthenticationSection(<%=UDDIActionInputs.QUERY_ITEM_SERVICES%>,this.checked)">
          <label for="input_find_services_advance_owned"><%=uddiPerspective.getMessage("FORM_LABEL_OWNED")%></label>
        </td>
      </tr>
    </table>
<%
   sectionHeaderInfo.clear();
   sectionHeaderInfo.setContainerId("findServicesAdvancedAuthentication");
%>
<jsp:include page="/uddi/forms/authentication_table.jsp" flush="true"/>
<%
   String[] busSpecificInfo = {"FORM_LABEL_BUSINESS",Boolean.FALSE.toString(),String.valueOf(UDDIActionInputs.QUERY_ITEM_BUSINESSES),"findServicesAdvanced",UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_BUSINESS};
   sectionHeaderInfo.clear();
   sectionHeaderInfo.setContainerId("findServicesAdvancedBusiness");
   sectionHeaderInfo.setOtherProperties(busSpecificInfo);
%>
<jsp:include page="/uddi/forms/uddiObjects_table.jsp" flush="true"/>
<%
   sectionHeaderInfo.clear();
   sectionHeaderInfo.setContainerId("findServicesAdvancedNames");
   String[] nameSpecificInfo = {"FORM_LABEL_NAMES","FORM_LABEL_NAME",Boolean.FALSE.toString()};
   sectionHeaderInfo.setOtherProperties(nameSpecificInfo);
%>
<jsp:include page="/uddi/forms/languageInput_table.jsp" flush="true"/>
<%
   sectionHeaderInfo.clear();
   sectionHeaderInfo.setContainerId("findServicesAdvancedCategories");
%>
<jsp:include page="/uddi/forms/categories_table.jsp" flush="true"/>
<%
   String[] siSpecificInfo = {"FORM_LABEL_SERVICE_INTERFACES",Boolean.FALSE.toString(),String.valueOf(UDDIActionInputs.QUERY_ITEM_SERVICE_INTERFACES),"findServicesAdvanced",UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_SERVICE_INTERFACES};
   sectionHeaderInfo.clear();
   sectionHeaderInfo.setContainerId("findServicesAdvancedServiceInterfaces");
   sectionHeaderInfo.setOtherProperties(siSpecificInfo);
%>
<jsp:include page="/uddi/forms/uddiObjects_table.jsp" flush="true"/>
<%
   sectionHeaderInfo.clear();
   sectionHeaderInfo.setContainerId("findServicesAdvancedFindQualifiers");
%>
<jsp:include page="/uddi/forms/findQualifiers_table.jsp" flush="true"/>
<jsp:include page="/uddi/forms/advancedCommon_table.jsp" flush="true"/>
<jsp:include page="/forms/simpleCommon_table.jsp" flush="true"/>
  </form>
</div>
