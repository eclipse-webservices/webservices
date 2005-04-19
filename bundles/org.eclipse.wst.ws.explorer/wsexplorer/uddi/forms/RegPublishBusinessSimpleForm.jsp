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
<%
   UDDIPerspective uddiPerspective = controller.getUDDIPerspective();
   NodeManager navigatorManager = uddiPerspective.getNavigatorManager();
   Node selectedNode = navigatorManager.getSelectedNode();
   FormTool formTool = (FormTool)(selectedNode.getCurrentToolManager().getSelectedTool());
   FormToolPropertiesInterface formToolPI = ((MultipleFormToolPropertiesInterface)formTool).getFormToolProperties(subQueryKeyProperty.getSubQueryKey());
%>
<div id="publishBusinessSimple" style="display:none;">
  <form action="<%=response.encodeURL(controller.getPathWithContext("uddi/actions/RegPublishBusinessSimpleActionJSP.jsp"))%>" method="post" target="<%=FrameNames.PERSPECTIVE_WORKAREA%>" enctype="multipart/form-data" onSubmit="return handleSubmit(this)">
    <table width="95%" border=0 cellpadding=3 cellspacing=0>
      <tr>
        <td class="labels" height=10 valign="bottom">
          <%=uddiPerspective.getMessage("FORM_LABEL_PUBLISH_BUSINESS_SIMPLE_DESC")%>
        </td>
      </tr>
    </table>
<%
   sectionHeaderInfo.clear();
   sectionHeaderInfo.setContainerId("publishBusinessSimpleAuthentication");
%>
<jsp:include page="/uddi/forms/authentication_table.jsp" flush="true"/>
    <table width="95%" border=0 cellpadding=3 cellspacing=0>
      <tr>
        <td class="labels" height=20 valign="bottom">
          <label for="input_publish_business_simple_name"><%=uddiPerspective.getMessage("FORM_LABEL_NAME")%></label>
<%
   if (!formToolPI.isInputValid(UDDIActionInputs.QUERY_INPUT_SIMPLE_BUSINESS_NAME))
   {
%>
          <%=HTMLUtils.redAsterisk()%>
<%
   }
%>
        </td>
      </tr>
      <tr>
        <td> <input type="text" id="input_publish_business_simple_name" name="<%=UDDIActionInputs.QUERY_INPUT_SIMPLE_BUSINESS_NAME%>" class="textenter"> </td>
      </tr>
      <tr>
        <td class="labels">
          <label for="input_publish_business_simple_desc"><%=uddiPerspective.getMessage("FORM_LABEL_DESCRIPTION")%></label>
        </td>
      </tr>
      <tr>
        <td> <input type="text" id="input_publish_business_simple_desc" name="<%=UDDIActionInputs.QUERY_INPUT_SIMPLE_BUSINESS_DESCRIPTION%>" class="textenter"> </td>
      </tr>
    </table>
<jsp:include page="/forms/simpleCommon_table.jsp" flush="true"/>
  </form>
</div>
