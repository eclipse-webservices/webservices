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
 * 20060427   127443 jesper@selskabet.org - Jesper S Moller
 *******************************************************************************/
%>
<%@ page contentType="text/html; charset=UTF-8" import="org.eclipse.wst.ws.internal.explorer.platform.constants.*,
                                                                                          org.eclipse.wst.ws.internal.explorer.platform.perspective.*,
                                                                                          org.eclipse.wst.ws.internal.explorer.platform.util.*,
                                                                                          org.eclipse.core.resources.*,
                                                                                          org.eclipse.core.runtime.*" %>

<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<jsp:useBean id="formProperties" class="java.util.Hashtable" scope="request"/>
<script language="javascript">
  function toggleDivContainer(divId)
  {
    var divContainer = document.getElementById(divId);
    if (divContainer.style.display == "none")
      divContainer.style.display = "";
    else
      divContainer.style.display = "none";
  }
</script>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title><%=controller.getMessage("FORM_TITLE_IMPORT_TO_WORKBENCH")%></title>
  <link rel="stylesheet" type="text/css" href="<%=response.encodeURL(controller.getPathWithContext("css/windows.css"))%>">
  <jsp:include page="/scripts/formsubmit.jsp" flush="true"/>
  <jsp:include page="/scripts/formutils.jsp" flush="true"/>
</head>
<body dir="<%=org.eclipse.wst.ws.internal.explorer.platform.util.DirUtils.getDir()%>" class="contentbodymargin">
<div id="contentborder">

  <form action="<%=response.encodeURL(controller.getPathWithContext((String)formProperties.get("formActionLink")))%>" method="post" target="<%=FrameNames.PERSPECTIVE_WORKAREA%>" enctype="multipart/form-data">

  <%
  String titleImagePath = "images/import_to_workbench_highlighted.gif";
  String title = controller.getMessage("ALT_IMPORT_WSDL_TO_WORKBENCH");
  %>
  <%@ include file = "/forms/formheader.inc" %>
  <table>
    <tr>
      <td>
        <%=controller.getMessage("FORM_LABEL_IMPORT_TO_WORKBENCH_DESC")%>
      </td>
    </tr>
  </table>
  <table>
    <tr>
      <td class="labels" height=40 valign="center">
        <label for="select_workbench_project_name"><%=controller.getMessage("WORKBENCH_PROJECT_NAME")%></label>
      </td>
      <td class="labels" height=40 valign="center" nowrap>
        <%
        IWorkspaceRoot iWorkspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
        IProject[] projects = iWorkspaceRoot.getProjects();
        %>
        <select id="select_workbench_project_name" name="<%=ActionInputs.WORKBENCH_PROJECT_NAME%>" class="selectlist">
        <%
        for (int i = 0; i < projects.length; i++) {
          try {
            if (!projects[i].isOpen())
              continue;
            String projectName = projects[i].getName();
        %>
        <option value="<%=projectName%>"><%=projectName%>
        <%
          }
          catch (Exception e) {}
        }
        %>
        </select>
      </td>
    </tr>
  </table>

  <table width="95%" border=0 cellpadding=3 cellspacing=0>
    <tr>
      <td class="labels" height=10 valign="bottom">
        <input type="checkbox" checked id="input_import_wsdl" name="<%=ActionInputs.IMPORT_FILE%>" value="<%=ActionInputs.IMPORT_FILE%>" onClick="toggleDivContainer('div_import_wsdl')">
        <label for="input_import_wsdl"><%=controller.getMessage("LABEL_IMPORT_WSDL")%></label>
      </td>
    </tr>
  </table>
  <div id="div_import_wsdl">
    <table width="95%" border=0 cellpadding=3 cellspacing=0>
      <tr>
        <td>
          <label for="input_imported_wsdl_file_name"><%=controller.getMessage("IMPORTED_WSDL_FILE_NAME")%></label>
        </td>
      </tr>
      <tr>
        <td height=10 valign="bottom" width="60%">
<%
 String wsdlFileName="temp.wsdl";
 String wsdlURL = (String)formProperties.get("wsdlURL");
 if (wsdlURL != null && wsdlURL.endsWith(".wsdl"))
 {
   int lastSeparatorPos = Math.max(wsdlURL.lastIndexOf("/"),wsdlURL.lastIndexOf("\\"));
   if (lastSeparatorPos > 0)
     wsdlFileName = wsdlURL.substring(lastSeparatorPos+1,wsdlURL.length());
 }
%>   
          <input type="text" id="input_imported_wsdl_file_name" name="<%=ActionInputs.IMPORTED_FILE_NAME%>" value="<%=HTMLUtils.charactersToHTMLEntitiesStrict(wsdlFileName)%>" size=50 class="textenter">
        </td>
      </tr>
    </table>
  </div>

  <table width="95%" border=0 cellpadding=3 cellspacing=0>
    <tr>
      <td class="labels" height=10 valign="bottom">&nbsp;</td>
    </tr>
  </table>

  <input type="hidden" name="<%=ActionInputs.IMPORTED_WSDL_URL%>" value="<%=wsdlURL%>">
  <table width="95%" border=0 cellpadding=3 cellspacing=0>
    <tr>
      <td class="labels" height=10 valign="bottom">
        <input type="checkbox" id="input_import_wsil" name="<%=ActionInputs.IMPORT_TO_WSIL%>" value="<%=ActionInputs.IMPORT_TO_WSIL%>" onClick="toggleDivContainer('div_import_wsil')">
        <label for="input_import_wsil"><%=controller.getMessage("LABEL_IMPORT_TO_WSIL")%></label>
      </td>
    </tr>
  </table>
  <div id="div_import_wsil" style="display:none;">
    <table width="95%" border=0 cellpadding=3 cellspacing=0>
      <tr>
        <td class="labels" height=10 valign="bottom">
          <label for="input_imported_wsil_file_name"><%=controller.getMessage("WSIL_FILE_NAME")%></label>
        </td>
      </tr>
      <tr>
        <td height=10 valign="bottom" width="60%">
<%
 String wsilFileName = (String)formProperties.get("wsilFileName");
 if (wsilFileName == null || wsilFileName.length() <= 0)
   wsilFileName = wsdlFileName.substring(0, wsdlFileName.length()-4) + "wsil";
%>   
          <input type="text" id="input_imported_wsil_file_name" name="<%=ActionInputs.WSIL_FILE_NAME%>" value="<%=HTMLUtils.charactersToHTMLEntitiesStrict(wsilFileName)%>" size=50 class="textenter">
        </td>
      </tr>
    </table>
  </div>

<jsp:include page="/forms/simpleCommon_table.jsp" flush="true"/>    
  </form>
</div>
</body>
</html>
