<%
/*******************************************************************************
 * Copyright (c) 2001, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060427   127443 jesper@selskabet.org - Jesper S Moller
 *******************************************************************************/
%>
<%@ page contentType="text/html; charset=UTF-8" import="org.eclipse.wst.ws.internal.explorer.platform.wsil.perspective.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.wsil.datamodel.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.constants.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.perspective.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.datamodel.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.util.*,
                                                        org.eclipse.core.resources.*,
                                                        org.eclipse.core.runtime.*" %>

<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<%
WSILPerspective wsilPerspective = controller.getWSILPerspective();
NodeManager nodeManager = wsilPerspective.getNodeManager();
Node selectedNode = nodeManager.getSelectedNode();
WsilElement wsilElement = (WsilElement)selectedNode.getTreeElement();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 3.2 Final//EN">
<html lang="<%=response.getLocale().getLanguage()%>">
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title><%=controller.getMessage("FORM_TITLE_IMPORT_TO_WORKBENCH")%></title>
  <link rel="stylesheet" type="text/css" href="<%=response.encodeURL(controller.getPathWithContext("css/windows.css"))%>">
  <jsp:include page="/scripts/formsubmit.jsp" flush="true"/>
  <jsp:include page="/scripts/formutils.jsp" flush="true"/>
</head>
<body dir="<%=org.eclipse.wst.ws.internal.explorer.platform.util.DirUtils.getDir()%>" class="contentbodymargin">
<div id="contentborder">
  <form action="<%=response.encodeURL(controller.getPathWithContext("wsil/actions/WSILImportWSILToWorkbenchActionJSP.jsp"))%>" method="post" target="<%=FrameNames.PERSPECTIVE_WORKAREA%>" enctype="multipart/form-data">
  <input type="hidden" name="<%=ActionInputs.IMPORT_FILE%>" value="<%=ActionInputs.IMPORT_FILE%>">
  <%
  String titleImagePath = "images/import_to_workbench_highlighted.gif";
  String title = wsilPerspective.getMessage("ATL_IMPORT_WSIL_TO_WORKBENCH");
  %>
  <%@ include file = "/forms/formheader.inc" %>
  <table>
    <tr>
      <td>
        <%=wsilPerspective.getMessage("FORM_LABEL_IMPORT_WSIL_TO_WORKBENCH_DESC")%>
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
        for (int i = 0; i < projects.length; i++)
        {
          try
          {
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
      <td>
        <label for="input_imported_wsil_file_name"><%=controller.getMessage("WSIL_FILE_NAME")%></label>
      </td>
    </tr>
    <tr>
      <td height=10 valign="bottom" width="60%">
<%
 String wsilFileName = "temp.wsil";
 String wsilURL = wsilElement.getWsilUrl();
 if (wsilURL.endsWith(".wsil"))
 {
   int lastSeparatorPos = Math.max(wsilURL.lastIndexOf("/"), wsilURL.lastIndexOf("\\"));
   if (lastSeparatorPos > 0)
     wsilFileName = wsilURL.substring(lastSeparatorPos+1, wsilURL.length());
 }
%>   
        <input type="text" id="input_imported_wsil_file_name" name="<%=ActionInputs.IMPORTED_FILE_NAME%>" value="<%=HTMLUtils.charactersToHTMLEntitiesStrict(wsilFileName)%>" size=50 class="textenter">
      </td>
    </tr>
  </table>
<jsp:include page="/forms/simpleCommon_table.jsp" flush="true"/>    
  </form>
</div>
</body>
</html>
