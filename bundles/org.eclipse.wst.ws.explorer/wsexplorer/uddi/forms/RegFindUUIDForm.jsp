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
   // selectedNode could be the registry or query nodes.
   Node selectedNode = navigatorManager.getSelectedNode();
   FormTool formTool = (FormTool)(selectedNode.getCurrentToolManager().getSelectedTool());
   FormToolPropertiesInterface formToolPI = ((MultipleFormToolPropertiesInterface)formTool).getFormToolProperties(subQueryKeyProperty.getSubQueryKey());
   
   String formContainerId = sectionHeaderInfo.getContainerId();
   String[] otherProperties = (String[])(sectionHeaderInfo.getOtherProperties());
   String actionLink = (String)otherProperties[0];
   String uuidEntryKey = (String)otherProperties[1];
   String descriptionKey = (String)otherProperties[2];
%>
<div id="<%=formContainerId%>" style="display:none;">
  <form action="<%=response.encodeURL(controller.getPathWithContext(actionLink))%>" method="post" target="<%=FrameNames.PERSPECTIVE_WORKAREA%>" enctype="multipart/form-data" onSubmit="return processForm(this)">
    <input type="hidden" name="<%=UDDIActionInputs.SUBQUERY_KEY%>">
    <input type="hidden" name="<%=UDDIActionInputs.QUERY_NAME%>">
    <table width="95%" border=0 cellpadding=3 cellspacing=0>
      <tr>
        <td class="labels" height=10 valign="bottom">
          <%=uddiPerspective.getMessage(descriptionKey)%>
        </td>
      </tr>
      <tr>
        <td class="labels" height=30 valign="bottom">
          <label for="<%=(formContainerId+"_input_uuid_key")%>"><%=uddiPerspective.getMessage("FORM_LABEL_UUID_KEY")%></label>
<%
   if (!formToolPI.isInputValid(uuidEntryKey))
   {
%>
          <%=HTMLUtils.redAsterisk()%>
<%
   }
%>
        </td>
      </tr>
      <tr>
        <td> <input type="text" id="<%=(formContainerId+"_input_uuid_key")%>" name="<%=UDDIActionInputs.QUERY_INPUT_UUID_KEY%>" class="textenter"> </td>
      </tr>
    </table>
<jsp:include page="/forms/simpleCommon_table.jsp" flush="true"/>
  </form>
</div>
