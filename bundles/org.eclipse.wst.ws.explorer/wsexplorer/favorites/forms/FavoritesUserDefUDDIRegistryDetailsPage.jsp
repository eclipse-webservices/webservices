<%
/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
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
                                                        java.io.*,
                                                        java.util.*,
                                                        org.eclipse.wst.ws.internal.model.v10.registry.Name,
                                                        org.eclipse.wst.ws.internal.model.v10.registry.Description,
                                                        org.eclipse.wst.ws.internal.model.v10.uddiregistry.Taxonomies,
                                                        org.eclipse.wst.ws.internal.model.v10.taxonomy.Taxonomy" %>

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
    <title><%=favPerspective.getMessage("FORM_TITLE_USER_DEF_UDDI_REGISTRY_DETAILS")%></title>
    <link rel="stylesheet" type="text/css" href="<%=response.encodeURL(controller.getPathWithContext("css/windows.css"))%>">
</head>
<body dir="<%=org.eclipse.wst.ws.internal.explorer.platform.util.DirUtils.getDir()%>" class="contentbodymargin">
<div id="contentborder">

    <%
    String titleImagePath = "images/details_highlighted.gif";
    String title = favPerspective.getMessage("ALT_FAVORITES_USER_DEF_UDDI_REGISTRY_DETAILS");
    %>
    <%@ include file = "/forms/formheader.inc" %>
    <%
    if (selectedElement instanceof FavoritesUserDefUDDIRegistryElement) {
        FavoritesUserDefUDDIRegistryElement regElement = (FavoritesUserDefUDDIRegistryElement)selectedElement;
    %>

        <%
        String version = regElement.getVersion();
        if (version != null)
        {
        %>
        <table width="95%" border=0 cellpadding=3 cellspacing=0 class="tableborder">
            <tr>
                <th class="singleheadercolor" height=20 valign="bottom" align="left">
                    <%=favPerspective.getMessage("FORM_LABEL_UDDI_VERSION")%>
                </th>
            </tr>
            <tr>
                <td class="tablecells">
                    <%=version%>
                </td>
            </tr>
        </table>
        <%
        }
        %>

        <%
        List names = regElement.getNames();
        if (names != null && !names.isEmpty())
        {
        %>
        <br>
        <table width="95%" border=0 cellpadding=3 cellspacing=0 class="tableborder">
            <tr>
                <th class="singleheadercolor" height=20 valign="bottom" align="left" width="1" nowrap>
                    <%=favPerspective.getMessage("FORM_LABEL_LANGUAGE")%>
                </th>
                <th class="singleheadercolor" height=20 valign="bottom" align="left">
                    <%=favPerspective.getMessage("FORM_LABEL_REGISTRY_NAME")%>
                </th>
            </tr>
            <%
            for (Iterator it = names.iterator(); it.hasNext();)
            {
              Name name = (Name)it.next();
              String lang = name.getLang();
              %>
              <tr>
                <td class="tablecells"><%=lang != null ? lang : "--"%></td>
                <td class="tablecells"><%=name.getValue()%></td>
              </tr>
              <%
            }
            %>
        </table>
        <%
        }
        %>

        <%
        List descs = regElement.getDescs();
        if (descs != null && !descs.isEmpty())
        {
        %>
        <br>
        <table width="95%" border=0 cellpadding=3 cellspacing=0 class="tableborder">
            <tr>
                <th class="singleheadercolor" height=20 valign="bottom" align="left" width="1" nowrap>
                    <%=favPerspective.getMessage("FORM_LABEL_LANGUAGE")%>
                </th>
                <th class="singleheadercolor" height=20 valign="bottom" align="left">
                    <%=favPerspective.getMessage("FORM_LABEL_DESC")%>
                </th>
            </tr>
            <%
            for (Iterator it = descs.iterator(); it.hasNext();)
            {
              Description desc = (Description)it.next();
              String lang = desc.getLang();
              %>
              <tr>
                <td class="tablecells"><%=lang != null ? lang : "--"%></td>
                <td class="tablecells"><%=desc.getValue()%></td>
              </tr>
              <%
            }
            %>
        </table>
        <%
        }
        %>

        <%
        String defaultLogin = regElement.getDefaultLogin();
        if (defaultLogin != null)
        {
        %>
        <br>
        <table width="95%" border=0 cellpadding=3 cellspacing=0 class="tableborder">
            <tr>
                <th class="singleheadercolor" height=20 valign="bottom" align="left">
                    <%=favPerspective.getMessage("FORM_LABEL_DEFAULT_LOGIN")%>
                </th>
            </tr>
            <tr>
                <td class="tablecells">
                    <%=defaultLogin%>
                </td>
            </tr>
        </table>
        <%
        }
        %>

        <%
        String defaultPassword = regElement.getDefaultPassword();
        if (defaultPassword != null)
        {
        %>
        <br>
        <table width="95%" border=0 cellpadding=3 cellspacing=0 class="tableborder">
            <tr>
                <th class="singleheadercolor" height=20 valign="bottom" align="left">
                    <%=favPerspective.getMessage("FORM_LABEL_DEFAULT_PASSWORD")%>
                </th>
            </tr>
            <tr>
                <td class="tablecells">
                    <%=defaultPassword%>
                </td>
            </tr>
        </table>
        <%
        }
        %>

        <%
        String inquiryURL = regElement.getInquiryURL();
        if (inquiryURL != null)
        {
        %>
        <br>
        <table width="95%" border=0 cellpadding=3 cellspacing=0 class="tableborder">
            <tr>
                <th class="singleheadercolor" height=20 valign="bottom" align="left">
                    <%=favPerspective.getMessage("FORM_LABEL_INQUIRY_URL")%>
                </th>
            </tr>
            <tr>
                <td class="tablecells">
                    <%=inquiryURL%>
                </td>
            </tr>
        </table>
        <%
        }
        %>

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

     String secureInquiryURL = regElement.getSecureInquiryURL();
     if (Validator.validateURL(secureInquiryURL))
     {
     %>
        <br>
        <table width="95%" border=0 cellpadding=3 cellspacing=0 class="tableborder">
            <tr>
                <th class="singleheadercolor" height=20 valign="bottom" align="left">
                    <%=favPerspective.getMessage("FORM_LABEL_SECURE_INQUIRY_URL")%>
                </th>
            </tr>
            <tr>
                <td class="tablecells">
                    <%=secureInquiryURL%>
                </td>
            </tr>
        </table>
     <%
     }

     String securePublishURL = regElement.getSecurePublishURL();
     if (Validator.validateURL(securePublishURL))
     {
     %>
        <br>
        <table width="95%" border=0 cellpadding=3 cellspacing=0 class="tableborder">
            <tr>
                <th class="singleheadercolor" height=20 valign="bottom" align="left">
                    <%=favPerspective.getMessage("FORM_LABEL_SECURE_PUBLISH_URL")%>
                </th>
            </tr>
            <tr>
                <td class="tablecells">
                    <%=securePublishURL%>
                </td>
            </tr>
        </table>
     <%
     }
     %>
     
     <%
     Taxonomy[] taxonomies = regElement.getTaxonomies();
     if (taxonomies != null && taxonomies.length > 0)
     {
     %>
       <br>
       <table width="95%" border=0 cellpadding=3 cellspacing=0 class="tableborder">
         <tr>
           <th class="singleheadercolor" height=20 valign="bottom" align="left">
             <%=favPerspective.getMessage("FORM_LABEL_TAXONOMY")%>
           </th>
         </tr>
         <%
         for (int i=0; i<taxonomies.length; i++)
         {
           Taxonomy taxonomy = taxonomies[i];
         %>
         <tr>
           <td class="tablecells"><%=taxonomy.getName()%></td>
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
