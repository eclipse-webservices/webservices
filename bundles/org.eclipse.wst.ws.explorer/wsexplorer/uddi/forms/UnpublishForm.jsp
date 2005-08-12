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
<%@ page contentType="text/html; charset=UTF-8" import="org.eclipse.wst.ws.internal.explorer.platform.uddi.constants.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.uddi.actions.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.uddi.perspective.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.uddi.datamodel.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.constants.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.perspective.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.datamodel.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.util.*,
                                                        java.util.*" %>

<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<jsp:useBean id="sectionHeaderInfo" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.SectionHeaderInfo" scope="request"/>
<%
   UDDIPerspective uddiPerspective = controller.getUDDIPerspective();
   NodeManager navigatorManager = uddiPerspective.getNavigatorManager();
   UDDIMainNode uddiMainNode = (UDDIMainNode)navigatorManager.getRootNode();
   RegistryNode regNode = uddiMainNode.getRegistryNode(navigatorManager.getSelectedNode());
   RegistryElement regElement = (RegistryElement)regNode.getTreeElement();
   
   FormTool unpublishTool = (FormTool)(navigatorManager.getSelectedNode().getCurrentToolManager().getSelectedTool());
%>

<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title><%=uddiPerspective.getMessage("FORM_TITLE_UNPUBLISH")%></title>
  <link rel="stylesheet" type="text/css" href="<%=response.encodeURL(controller.getPathWithContext("css/windows.css"))%>">
<script language="javascript" src="<%=response.encodeURL(controller.getPathWithContext("scripts/browserdetect.js"))%>">
</script>
<jsp:include page="/scripts/formsubmit.jsp" flush="true"/>
<jsp:include page="/uddi/scripts/udditables.jsp" flush="true"/>

<script language="javascript">
  function setDefaults()
  {
<%
   if (!regElement.isLoggedIn())
   {
%>
    var authenticationSection = document.getElementById("unpublishAuthentication");
    authenticationSection.style.display = "";
    var form = document.forms[0];
    form.<%=UDDIActionInputs.QUERY_INPUT_ADVANCED_PUBLISH_URL%>.value = "<%=HTMLUtils.JSMangle((String)unpublishTool.getProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_PUBLISH_URL))%>";
    form.<%=UDDIActionInputs.QUERY_INPUT_ADVANCED_USERID%>.value = "<%=HTMLUtils.JSMangle((String)unpublishTool.getProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_USERID))%>";
    form.<%=UDDIActionInputs.QUERY_INPUT_ADVANCED_PASSWORD%>.value = "<%=HTMLUtils.JSMangle((String)unpublishTool.getProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_PASSWORD))%>";    
<%
   }
%>
  }
</script>
</head>
<body dir="<%=org.eclipse.wst.ws.internal.explorer.platform.util.DirUtils.getDir()%>" class="contentbodymargin" onUnload="closeAllUddiChildWindows()">
  <div id="contentborder">
    <form action="<%=response.encodeURL(controller.getPathWithContext("uddi/actions/UnpublishActionJSP.jsp"))%>" method="post" target="<%=FrameNames.PERSPECTIVE_WORKAREA%>" enctype="multipart/form-data" onSubmit="return handleSubmit(this)">
<%
   String titleImagePath = unpublishTool.getHighlightedImageLink();
   String title = unpublishTool.getAltText();
%>
<%@ include file="/forms/formheader.inc" %>
    <table>
      <tr>
        <td>
          <%=uddiPerspective.getMessage("FORM_LABEL_UNPUBLISH_DESC")%>
        </td>
      </tr>
    </table>
<%
   sectionHeaderInfo.clear();
   sectionHeaderInfo.setContainerId("unpublishAuthentication");
%>       
<jsp:include page="/uddi/forms/authentication_table.jsp" flush="true"/>
<jsp:include page="/forms/simpleCommon_table.jsp" flush="true"/>
  </div>
<script language="javascript">
  setDefaults();
</script>
</body>
</html>
