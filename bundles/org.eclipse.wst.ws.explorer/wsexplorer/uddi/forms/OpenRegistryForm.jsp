<%
/*******************************************************************************
 * Copyright (c) 2001, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060515   135307 gilberta@ca.ibm.com - Gilbert Andrews
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
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 3.2 Final//EN">
<html lang="<%=response.getLocale().getLanguage()%>">
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

   
  </script>
</head>
<body dir="<%=org.eclipse.wst.ws.internal.explorer.platform.util.DirUtils.getDir()%>" class="contentbodymargin">
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
      
      <form id="openRegistryForm" action="<%=response.encodeURL(controller.getPathWithContext("uddi/actions/OpenRegistryActionJSP.jsp"))%>" method="post" target="<%=FrameNames.PERSPECTIVE_WORKAREA%>" enctype="multipart/form-data" onSubmit="return handleSubmit(this)">
        <table width="95%" border=0 cellpadding=3 cellspacing=0>
         
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
      
    </div>
  </div>
</body>
</html>
