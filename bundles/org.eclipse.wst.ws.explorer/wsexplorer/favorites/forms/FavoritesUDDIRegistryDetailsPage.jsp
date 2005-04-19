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
<%@ page contentType="text/html; charset=UTF-8" import="org.eclipse.wst.ws.internal.explorer.platform.perspective.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.constants.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.datamodel.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.favorites.constants.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.favorites.perspective.FavoritesPerspective,
                                                        org.eclipse.wst.ws.internal.explorer.platform.favorites.datamodel.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.util.*,
                                                        org.apache.wsil.extension.uddi.*,
                                                        java.io.*" %>

<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<%
        FavoritesPerspective favPerspective = controller.getFavoritesPerspective();
        NodeManager nodeManager = favPerspective.getNodeManager();
        Node selectedNode = nodeManager.getSelectedNode();
        ToolManager currentToolManager = selectedNode.getCurrentToolManager();
        TreeElement selectedElement = selectedNode.getTreeElement();
%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title><%=favPerspective.getMessage("FORM_TITLE_UDDI_REGISTRY_DETAILS")%></title>
    <link rel="stylesheet" type="text/css" href="<%=response.encodeURL(controller.getPathWithContext("css/windows.css"))%>">
</head>
<body class="contentbodymargin">
<div id="contentborder">

    <%
    String titleImagePath = "images/details_highlighted.gif";
    String title = favPerspective.getMessage("ALT_FAVORITES_UDDI_REGISTRY_DETAILS");
    %>
    <%@ include file = "/forms/formheader.inc" %>
    <%
    if (selectedElement instanceof FavoritesUDDIRegistryElement) {
        FavoritesUDDIRegistryElement regElement = (FavoritesUDDIRegistryElement)selectedElement;
        String registryName = regElement.getName();
    %>

        <table width="95%" border=0 cellpadding=3 cellspacing=0 class="tableborder">
            <tr>
                <th class="singleheadercolor" height=20 valign="bottom" align="left">
                    <%=favPerspective.getMessage("FORM_LABEL_REGISTRY_NAME")%>
                </th>
            </tr>
            <tr>
                <td class="tablecells">
                    <%=registryName%>
                </td>
            </tr>
        </table>

        <br>
        <table width="95%" border=0 cellpadding=3 cellspacing=0 class="tableborder">
            <tr>
                <th class="singleheadercolor" height=20 valign="bottom" align="left">
                    <%=favPerspective.getMessage("FORM_LABEL_INQUIRY_URL")%>
                </th>
            </tr>
            <tr>
                <td class="tablecells">
                    <%=regElement.getInquiryURL()%>
                </td>
            </tr>
        </table>
<%
     String publishURL = regElement.getPublishURL();
     if (Validator.validateURL(publishURL))
     {
%>
        <br>
        <table width="95%" border=0 cellpadding=3 cellspacing=0 class="tableborder">
            <tr>
                <th class="singleheadercolor" height=20 valign="bottom" align="left">
                    <%=favPerspective.getMessage("FORM_LABEL_PUBLISH_URL")%>
                </th>
            </tr>
            <tr>
                <td class="tablecells">
                    <%=publishURL%>
                </td>
            </tr>
        </table>
<%
     }
   
     String registryURL = regElement.getRegistrationURL();
     if (Validator.validateURL(registryURL))
     {
%>
            <br>
            <table>
                <tr>
                    <td align="left">
                        <%=favPerspective.getMessage("FORM_LABEL_REGISTRY_URL",registryURL)%>
                    </td>
                </tr>
            </table>
<%
      }
      
      StringBuffer categoryDirectory = new StringBuffer();
      FavoritesUDDIRegistryFolderElement.formCategoriesDirectory(categoryDirectory,controller.getServletEngineStateLocation(),registryName);
      File categoryDirectoryFile = new File(categoryDirectory.toString());
      String[] categoryFiles = categoryDirectoryFile.list();
      if (categoryFiles != null)
      {
%>
<br>
<table width="95%" border=0 cellpadding=3 cellspacing=0 class="tableborder">
  <tr>
    <th class="singleheadercolor" height=20 valign="bottom" align="left">
      <%=favPerspective.getMessage("FORM_LABEL_USER_DEFINED_CATEGORY_DATA_FILES")%>
    </th>
  </tr>
<%      
        for (int i=0;i<categoryFiles.length;i++)
        {
%>
  <tr>
    <td class="tablecells">
      <%=categoryFiles[i]%>
    </td>
  </tr>
<%        
        }
%>
</table>
<%        
      }      
%>
<jsp:useBean id="currentToolManagerHash" class="java.util.Hashtable" scope="request">
<%
  currentToolManagerHash.put(ActionInputs.CURRENT_TOOL_MANAGER,currentToolManager);
%>
</jsp:useBean>
<jsp:include page="/forms/otherActions.jsp" flush="true"/>
<%      
    }
%>
</div>
</body>
</html>
