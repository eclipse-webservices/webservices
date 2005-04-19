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
Enumeration favoriteRegistryElements = openRegistryTool.getFavoriteRegistryElements();
Vector favoriteRegistryList = new Vector();
while (favoriteRegistryElements.hasMoreElements())
  favoriteRegistryList.addElement(favoriteRegistryElements.nextElement());
%>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title><%=uddiPerspective.getMessage("FORM_TITLE_OPEN_REGISTRY")%></title>
  <link rel="stylesheet" type="text/css" href="<%=response.encodeURL(controller.getPathWithContext("css/windows.css"))%>">
  <jsp:include page="/scripts/formsubmit.jsp" flush="true"/>
  <jsp:include page="/scripts/formutils.jsp" flush="true"/>
  <script language="javascript">
    function fillFavoriteInfo(favoriteRegistrySelect)
    {
      var selectedFavoriteRegistryIndex = favoriteRegistrySelect.selectedIndex-1;
      if (selectedFavoriteRegistryIndex >= 0)
      {
        var form = document.getElementById("openRegistryForm");
        switch (selectedFavoriteRegistryIndex)
        {
          <%
          for (int i=0;i<favoriteRegistryList.size();i++)
          {
            FavoritesUDDIRegistryElement favRegElement = (FavoritesUDDIRegistryElement)favoriteRegistryList.elementAt(i);
          %>
            case <%=i%>:
              form.<%=UDDIActionInputs.REGISTRY_NAME%>.value = "<%=HTMLUtils.JSMangle(favRegElement.getName())%>";
              form.<%=UDDIActionInputs.INQUIRY_URL%>.value = "<%=HTMLUtils.JSMangle(favRegElement.getInquiryURL())%>";
              break;
          <%
          }
          %>
        }
      }
    }

    function setDefaults()
    {
      var form = document.getElementById("openRegistryForm");
      form.<%=UDDIActionInputs.REGISTRY_NAME%>.value = "<%=HTMLUtils.JSMangle((String)openRegistryTool.getProperty(UDDIActionInputs.REGISTRY_NAME))%>";
      form.<%=UDDIActionInputs.INQUIRY_URL%>.value = "<%=HTMLUtils.JSMangle((String)openRegistryTool.getProperty(UDDIActionInputs.INQUIRY_URL))%>";
      showMainForm();
    }

    function showMainForm()
    {
      var loadScreenTable = document.getElementById("loadScreen");
      if (loadScreenTable.rows.length > 0)
        loadScreenTable.deleteRow(0);
      document.getElementById("mainScreen").style.display = "";
    }

    function toggleRegistryType(isPrivateRegistry)
    {
      var openRegistryForm = document.getElementById("openRegistryForm");
      var openPrivateRegistryForm = document.getElementById("openPrivateRegistryForm");
      if (isPrivateRegistry)
      {
        openRegistryForm.style.display = "none";
        openPrivateRegistryForm.style.display = "";
      }
      else
      {
        openRegistryForm.style.display = "";
        openPrivateRegistryForm.style.display = "none";
      }
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
    <div id="mainScreen" style="display:none;">
      <%
      String titleImagePath = "uddi/images/open_registry_highlighted.gif";
      String title = uddiPerspective.getMessage("ALT_OPEN_REGISTRY");
      %>
      <%@ include file="/forms/formheader.inc" %>
      <table>
        <tr>
          <td class="labels">
            <%=uddiPerspective.getMessage("FORM_LABEL_OPEN_REGISTRY_DESC")%>
          </td>
        </tr>
      </table>
      <table width="95%" border=0 cellpadding=3 cellspacing=0>
        <tr>
          <td class="labels" height=40 valign="bottom">
            <input type="checkbox" id="registryType" onClick="javascript:toggleRegistryType(this.checked)"><label for="registryType"><%=uddiPerspective.getMessage("FORM_LABEL_IS_WEBSPHERE_UDDI_REGISTRY")%></label>
          </td>
        </tr>
      </table>
      <form id="openRegistryForm" action="<%=response.encodeURL(controller.getPathWithContext("uddi/actions/OpenRegistryActionJSP.jsp"))%>" method="post" target="<%=FrameNames.PERSPECTIVE_WORKAREA%>" enctype="multipart/form-data" onSubmit="return handleSubmit(this)">
        <table width="95%" border=0 cellpadding=3 cellspacing=0>
          <tr>
            <td class="labels" valign="bottom" nowrap height=25>
              <label for="favoriteRegistry"><%=uddiPerspective.getMessage("FORM_LABEL_FAVORITE_REGISTRY")%></label>
            </td>
          </tr>
          <tr>
            <td height=25 valign="bottom">
              <select id="favoriteRegistry" onChange="fillFavoriteInfo(this)" class="selectlist">
                <option value="" selected>
                <%
                for (int i=0;i<favoriteRegistryList.size();i++)
                {
                  FavoritesUDDIRegistryElement favRegElement = (FavoritesUDDIRegistryElement)favoriteRegistryList.elementAt(i);
                %>
                  <option value="<%=favRegElement.getName()%>"><%=favRegElement.getName()%>
                <%
                }
                %>
              </select>
            </td>
          </tr>
          <tr>
            <td class="labels" height=25 valign="bottom">
              <label for="input_registry_name"><%=uddiPerspective.getMessage("FORM_LABEL_REGISTRY_NAME")%></label>
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
              <input type="text" id="input_registry_name" name="<%=UDDIActionInputs.REGISTRY_NAME%>" class="textenter">
            </td>
          </tr>
          <tr>
            <td class="labels" height=30 valign="bottom">
              <label for="input_inquiry_url"><%=uddiPerspective.getMessage("FORM_LABEL_INQUIRY_URL")%></label>
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
              <input type="text" id="input_inquiry_url" name="<%=UDDIActionInputs.INQUIRY_URL%>" class="textenter">
            </td>
          </tr>
        </table>
        <jsp:include page="/forms/simpleCommon_table.jsp" flush="true"/>
      </form>
      <script language="javascript">
        setDefaults();
      </script>
      <jsp:include page="/uddi/forms/OpenPrivateRegistryForm.jsp" flush="true"/>
    </div>
  </div>
</body>
</html>
