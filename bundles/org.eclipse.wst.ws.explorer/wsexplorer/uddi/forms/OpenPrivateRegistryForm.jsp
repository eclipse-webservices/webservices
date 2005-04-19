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
<%@ page contentType="text/html; charset=UTF-8" import="org.eclipse.wst.ws.internal.explorer.platform.favorites.datamodel.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.uddi.perspective.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.uddi.constants.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.perspective.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.constants.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.datamodel.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.util.*,
                                                        java.util.*" %>

<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<%
UDDIPerspective uddiPerspective = controller.getUDDIPerspective();
NodeManager navigatorManager = uddiPerspective.getNavigatorManager();
UDDIMainNode uddiMainNode = (UDDIMainNode)navigatorManager.getRootNode();
OpenRegistryTool openRegistryTool = (OpenRegistryTool)(uddiMainNode.getCurrentToolManager().getSelectedTool());
%>
<script language="javascript">
  function toInquiryURL()
  {
    var inputHost = document.getElementById("privateRegistryHost");
    var inputPort = document.getElementById("privateRegistryPort");
    var inputInquiryURL = document.getElementById("privateRegistryInquiryURL");
    var inputPublishURL = document.getElementById("privateRegistryPublishURL");
    inputInquiryURL.value = "http://" + inputHost.value + ":" + inputPort.value + "/uddisoap/inquiryapi";
    inputPublishURL.value = "http://" + inputHost.value + ":" + inputPort.value + "/uddisoap/publishapi";
  }
</script>
<form id="openPrivateRegistryForm" action="<%=response.encodeURL(controller.getPathWithContext("uddi/actions/OpenRegistryActionJSP.jsp"))%>" method="post" target="<%=FrameNames.PERSPECTIVE_WORKAREA%>" enctype="multipart/form-data" onSubmit="return handleSubmit(this)" style="display:none;">
  <input type="hidden" id="privateRegistryPublishURL" name="<%=UDDIActionInputs.PUBLISH_URL%>">
  <input type="hidden" name="<%=UDDIActionInputs.UDDI_USERNAME%>" value="UNAUTHENTICATED">
  <input type="hidden" name="<%=UDDIActionInputs.UDDI_PASSWORD%>" value="">
  <table width="95%" border=0 cellpadding=3 cellspacing=0>
    <tr>
      <td class="labels" height=25 valign="bottom">
        <label for="privateRegistryName"><%=uddiPerspective.getMessage("FORM_LABEL_REGISTRY_NAME")%></label>
        <%
        if (!openRegistryTool.isInputValid(UDDIActionInputs.REGISTRY_NAME))
        {
        %>
          <%=HTMLUtils.redAsterisk()%>
        <%
        }
        %>
      </td>
    </tr>
    <tr>
      <td>
        <input type="text" id="privateRegistryName" name="<%=UDDIActionInputs.REGISTRY_NAME%>" value="<%=uddiPerspective.getMessage("DEFAULT_REGISTRY_NAME_WEBSPHERE")%>" class="textenter">
      </td>
    </tr>
    <tr>
      <td class="labels" height=25 valign="bottom">
        <label for="privateRegistryHost"><%=uddiPerspective.getMessage("FORM_LABEL_REGISTRY_HOST")%></label>
      </td>
    </tr>
    <tr>
      <td>
        <input type="text" id="privateRegistryHost" value="localhost" onkeyup="javascript:toInquiryURL()" onpaste="javascript:toInquiryURL()" class="textenter">
      </td>
    </tr>
    <tr>
      <td class="labels" height=25 valign="bottom">
        <label for="privateRegistryPort"><%=uddiPerspective.getMessage("FORM_LABEL_REGISTRY_PORT")%></label>
      </td>
    </tr>
    <tr>
      <td>
        <input type="text" id="privateRegistryPort" value="9080" onkeyup="javascript:toInquiryURL()" onpaste="javascript:toInquiryURL()" class="textenter">
      </td>
    </tr>
    <tr>
      <td class="labels" height=30 valign="bottom">
        <label for="privateRegistryInquiryURL"><%=uddiPerspective.getMessage("FORM_LABEL_INQUIRY_URL")%></label>
        <%
        if (!openRegistryTool.isInputValid(UDDIActionInputs.INQUIRY_URL))
        {
        %>
          <%=HTMLUtils.redAsterisk()%>
        <%
        }
        %>
      </td>
    </tr>
    <tr>
      <td>
        <input type="text" id="privateRegistryInquiryURL" name="<%=UDDIActionInputs.INQUIRY_URL%>" class="textenter">
      </td>
    </tr>
    <tr>
      <td class="labels" height=40 valign="bottom">
        <input type="checkbox" id="privateRegistryCategories" name="<%=UDDIActionInputs.CHECK_USER_DEFINED_CATEGORIES%>"><label for="privateRegistryCategories"><%=uddiPerspective.getMessage("FORM_LABEL_CHECK_USER_DEFINED_CATEGORIES")%></label>
      </td>
    </tr>
  </table>
  <jsp:include page="/forms/simpleCommon_table.jsp" flush="true"/>
</form>
<script language="javascript">
  toInquiryURL();
</script>
