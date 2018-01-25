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
                                                        org.eclipse.wst.ws.internal.explorer.platform.perspective.*" %>

<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<jsp:useBean id="sectionHeaderInfo" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.SectionHeaderInfo" scope="request"/>
<%
   UDDIPerspective uddiPerspective = controller.getUDDIPerspective();
%>
<div id="findServiceInterfaces" style="display:none;">
  <form name="findServiceInterfacesStyle">
    <table>
      <tr>
        <td> <%=uddiPerspective.getMessage("FORM_LABEL_SEARCH_TYPE")%> </td>
      </tr>
      <tr>
        <td>
          <input type="radio" id="radio_find_service_interface_simple" name="<%=UDDIActionInputs.QUERY_STYLE_SERVICE_INTERFACES%>" onClick="showFindForm(<%=UDDIActionInputs.QUERY_ITEM_SERVICE_INTERFACES%>,<%=UDDIActionInputs.QUERY_STYLE_SIMPLE%>)"><label for="radio_find_service_interface_simple"><%=uddiPerspective.getMessage("FORM_RADIO_SIMPLE")%></label>
          <input type="radio" id="radio_find_service_interface_advance" name="<%=UDDIActionInputs.QUERY_STYLE_SERVICE_INTERFACES%>" onClick="showFindForm(<%=UDDIActionInputs.QUERY_ITEM_SERVICE_INTERFACES%>,<%=UDDIActionInputs.QUERY_STYLE_ADVANCED%>)"><label for="radio_find_service_interface_advance"><%=uddiPerspective.getMessage("FORM_RADIO_ADVANCED")%></label>
          <input type="radio" id="radio_find_service_interface_uuid" name="<%=UDDIActionInputs.QUERY_STYLE_SERVICE_INTERFACES%>" onClick="showFindForm(<%=UDDIActionInputs.QUERY_ITEM_SERVICE_INTERFACES%>,<%=UDDIActionInputs.QUERY_STYLE_UUID%>)"><label for="radio_find_service_interface_uuid"><%=uddiPerspective.getMessage("FORM_RADIO_UUID")%></label>
        </td>
      </tr>
    </table>
  </form>
<jsp:include page="/uddi/forms/RegFindServiceInterfacesSimpleForm.jsp" flush="true"/>
<jsp:include page="/uddi/forms/RegFindServiceInterfacesAdvancedForm.jsp" flush="true"/>
<%
   sectionHeaderInfo.setContainerId("findServiceInterfaceUUID");
   String[] otherProperties = {"uddi/actions/RegFindServiceInterfaceUUIDActionJSP.jsp",UDDIActionInputs.QUERY_INPUT_UUID_SERVICE_INTERFACE_KEY,"FORM_LABEL_FIND_SERVICE_INTERFACE_UUID_DESC"};
   sectionHeaderInfo.setOtherProperties(otherProperties);
%>
<jsp:include page="/uddi/forms/RegFindUUIDForm.jsp" flush="true"/>
</div>
