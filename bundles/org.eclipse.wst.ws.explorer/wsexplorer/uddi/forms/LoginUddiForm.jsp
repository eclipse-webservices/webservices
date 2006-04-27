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
<%@ page contentType="text/html; charset=UTF-8" import="org.eclipse.wst.ws.internal.explorer.platform.uddi.constants.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.uddi.actions.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.uddi.perspective.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.uddi.datamodel.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.perspective.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.datamodel.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.constants.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.util.*,
                                                        org.uddi4j.datatype.business.*,
                                                        org.uddi4j.datatype.tmodel.TModel,
                                                        org.uddi4j.datatype.*,
                                                        org.uddi4j.util.*,
                                                        java.util.*" %>

<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<%
   UDDIPerspective uddiPerspective = controller.getUDDIPerspective();
   NodeManager navigatorManager = uddiPerspective.getNavigatorManager();
   // selectedNode must be a registry node.
   Node regNode = navigatorManager.getSelectedNode();
   regNode.getToolManager().setSelectedToolId(1);
   List tools = regNode.getCurrentToolManager().getTools();
   Iterator iter = tools.iterator();
   Tool t = null;
   FormTool formTool = (FormTool)(regNode.getCurrentToolManager().getSelectedTool());
   RegistryElement regElement = (RegistryElement)regNode.getTreeElement();
%>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title>New Login Form</title>
  <link rel="stylesheet" type="text/css" href="<%=response.encodeURL(controller.getPathWithContext("css/windows.css"))%>">
<script language="javascript" src="<%=response.encodeURL(controller.getPathWithContext("scripts/browserdetect.js"))%>">
</script>
<jsp:include page="/scripts/formsubmit.jsp" flush="true"/>
<script language="javascript">
  function showMainForm(publishValue)
  {
    var loadScreenTable = document.getElementById("loadScreen");
    if (loadScreenTable.rows.length > 0)
      loadScreenTable.deleteRow(0);
    document.getElementById("mainScreen").style.display = "";
  }

</script>
</head>
<body class="contentbodymargin">
  <div id="contentborder">
    <table id="loadScreen">
      <tr>
        <td class="labels">
          <%=controller.getMessage("MSG_LOAD_IN_PROGRESS")%>
        </td>
      </tr>
    </table>
    <div id="mainScreen" >
<%
   String titleImagePath = "uddi/images/publish_highlighted.gif";
   String title = "Login";
%>
<%@ include file="/forms/formheader.inc" %>
    </div>
    <form action="<%=response.encodeURL(controller.getPathWithContext("uddi/actions/LoginAdvancedActionJSP.jsp"))%>" method="post" target="<%=FrameNames.PERSPECTIVE_WORKAREA%>" enctype="multipart/form-data">
    <jsp:include page="/uddi/forms/login_authentication_table.jsp" flush="true"/>
    <jsp:include page="/forms/simpleCommon_table.jsp" flush="true"/>
  </form>
  </div>
<script language="javascript">
  showMainForm(0);
</script>
</body>
</html>
