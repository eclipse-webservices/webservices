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
                                                        org.eclipse.wst.ws.internal.explorer.platform.uddi.datamodel.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.favorites.perspective.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.perspective.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.constants.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.util.*,
                                                        java.util.*" %>

<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<jsp:useBean id="sectionHeaderInfo" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.SectionHeaderInfo" scope="request"/>
<%
   UDDIPerspective uddiPerspective = controller.getUDDIPerspective();
   FavoritesPerspective favPerspective = controller.getFavoritesPerspective();
   NodeManager navigatorManager = uddiPerspective.getNavigatorManager();
   // The selected node must be a registry node.
   Node registryNode = navigatorManager.getSelectedNode();
   ToolManager currentToolManager = registryNode.getCurrentToolManager();
   FormTool formTool = (FormTool)(currentToolManager.getSelectedTool());   
   RegistryElement regElement = (RegistryElement)registryNode.getTreeElement();
%>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title><%=uddiPerspective.getMessage("FORM_TITLE_REGISTRY_DETAILS")%></title>
  <link rel="stylesheet" type="text/css" href="<%=response.encodeURL(controller.getPathWithContext("css/windows.css"))%>">
<jsp:include page="/scripts/formsubmit.jsp" flush="true"/>
<jsp:include page="/uddi/scripts/detailstables.jsp" flush="true"/>
<script language="javascript" src="<%=response.encodeURL(controller.getPathWithContext("scripts/browserdetect.js"))%>">
</script>
<script language="javascript" src="<%=response.encodeURL(controller.getPathWithContext("scripts/resumeproxyloadpage.js"))%>">
</script>
<script language="javascript">
  function setDefaults()
  {
    var registryDetailsNameTable = getTable("registryDetailsName");
<%
   String name = regElement.getName();
   boolean isError = !formTool.isInputValid(UDDIActionInputs.QUERY_INPUT_ADVANCED_REGISTRY_NAME);   
%>
    addDetailsSingleItemRow("registryDetailsName",false);
    setDetailsNameSingleItemRow("registryDetailsName","<%=HTMLUtils.JSMangle(name)%>");
<%
   if (isError)
   {
%>
    highlightErrantRow(registryDetailsNameTable.rows[numberOfHeaderRows],"errantRow");
<%
   }
   
   Enumeration userDefinedCategories = regElement.getUserDefinedCategories();
   boolean containsUserDefinedCategories;
   if (userDefinedCategories != null && userDefinedCategories.hasMoreElements())
   {
%>
    var registryCategoriesTable = getTable("registryUserDefinedCategories");
<%   
     containsUserDefinedCategories = true;
     ArrayList userDefinedCategoriesList = new ArrayList();
     do
     {
       userDefinedCategoriesList.add(userDefinedCategories.nextElement());
     } while (userDefinedCategories.hasMoreElements());
     CategoryModel[] userDefinedCategoriesArray = new CategoryModel[userDefinedCategoriesList.size()];
     userDefinedCategoriesList.toArray(userDefinedCategoriesArray);
     QuickSort.sort(userDefinedCategoriesArray,0,userDefinedCategoriesArray.length-1);
     StringBuffer fileName = new StringBuffer();
     for (int i=0;i<userDefinedCategoriesArray.length;i++)
     {
       name = userDefinedCategoriesArray[i].getDisplayName();
       String tModelKey = userDefinedCategoriesArray[i].getTModelKey();
       boolean checked = userDefinedCategoriesArray[i].isChecked();
       fileName.setLength(0);
       if (userDefinedCategoriesArray[i].isDataLoaded())
         fileName.append(userDefinedCategoriesArray[i].getCategoryKey()).append(".txt");
       else
         fileName.append(controller.getMessage("TABLE_BLANK_PLACEHOLDER"));
       boolean hasError = !formTool.isRowInputValid(UDDIActionInputs.USER_DEFINED_CATEGORIES,tModelKey);
%>
    addDetailsUserDefinedCategoryRow("registryUserDefinedCategories","<%=tModelKey%>","<%=HTMLUtils.JSMangle(name)%>",<%=checked%>,"<%=HTMLUtils.JSMangle(fileName.toString())%>",<%=hasError%>);
<%
       if (hasError)
       {
%>
    highlightErrantRow(registryCategoriesTable.rows[numberOfHeaderRows+<%=i%>],"errantRow");
<%       
       }
     }
   }
   else
     containsUserDefinedCategories = false;   
%>       
    showMainForm();
  }
  
  function showMainForm()
  {
    var loadScreenTable = document.getElementById("loadScreen");
    if (loadScreenTable.rows.length > 0)
      loadScreenTable.deleteRow(0);
    document.getElementById("mainScreen").style.display = "";
  }
  
  function processForm(form)
  {
    if (handleSubmit(form))
    {
      processDetailsSingleItemTable("registryDetailsName","<%=UDDIActionInputs.NAME_MODIFIED%>","<%=UDDIActionInputs.QUERY_INPUT_ADVANCED_REGISTRY_NAME%>",form);
      return true;
    }
    return false;
  }
</script>
</head>
<body class="contentbodymargin">
  <div id="contentborder">
    <form action="<%=response.encodeURL(controller.getPathWithContext("uddi/actions/UpdateRegistryActionJSP.jsp"))%>" method="post" target="<%=FrameNames.PERSPECTIVE_WORKAREA%>" enctype="multipart/form-data" onSubmit="return processForm(this)">
      <table id="loadScreen">
        <tr>
          <td class="labels">
            <%=controller.getMessage("MSG_LOAD_IN_PROGRESS")%>
          </td>
        </tr>
      </table>
      <div id="mainScreen" style="display:none;">
<%
   String titleImagePath = "images/details_highlighted.gif";
   String title = uddiPerspective.getMessage("ALT_REGISTRY_DETAILS");
%>
<%@ include file="/forms/formheader.inc" %>
        <table>
          <tr>
            <td class="labels">
              <%=uddiPerspective.getMessage("FORM_LABEL_DETAILS_EDITABLE",registryNode.getNodeName())%>
            </td>
          </tr>
        </table>
<%
   sectionHeaderInfo.clear();
   sectionHeaderInfo.setContainerId("registryDetailsName");
   sectionHeaderInfo.setOtherProperties(uddiPerspective.getMessage("FORM_LABEL_REGISTRY_NAME"));
%>
<jsp:include page="/uddi/forms/detailsSingleItem_table.jsp" flush="true"/>
        <table>
          <tr>
            <td height=20>&nbsp;</td>
          </tr>
        </table>
        <table width="95%" border=0 cellpadding=3 cellspacing=0 class="tableborder">
          <tr>
            <th class="singleheadercolor" height=20 valign="bottom">
              <%=uddiPerspective.getMessage("FORM_LABEL_INQUIRY_URL")%>
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
        <table>
          <tr>
            <td height=20>&nbsp;</td>
          </tr>
        </table>
        <table width="95%" border=0 cellpadding=3 cellspacing=0 class="tableborder">
          <tr>
            <th class="singleheadercolor" height=20 valign="bottom">
              <%=uddiPerspective.getMessage("FORM_LABEL_PUBLISH_URL")%>
            </th>
          </tr>
          <tr>
            <td class="tablecells">
              <%=regElement.getPublishURL()%>
            </td>
          </tr>
        </table>
<%
   }
   
   if (containsUserDefinedCategories)
   {
     sectionHeaderInfo.clear();
     sectionHeaderInfo.setContainerId("registryUserDefinedCategories");
%>
<jsp:include page="/uddi/forms/ud_categories_table.jsp" flush="true"/>
<%
   }
   if (regElement.isLoggedIn())
   {
%>
        <table>
          <tr>
            <td height=20>&nbsp;</td>
          </tr>
        </table>
        <table width="95%" border=0 cellpadding=3 cellspacing=0 class="tableborder">
          <tr>
            <th class="singleheadercolor" height=20 valign="bottom">
              <%=uddiPerspective.getMessage("FORM_LABEL_USERID")%>
            </th>
          </tr>
          <tr>
            <td class="tablecells">
              <%=regElement.getUserId()%>
            </td>
          </tr>
        </table>
<%
   }
   
   String registrationURL = regElement.getRegistrationURL();
   if (Validator.validateURL(registrationURL))
   {
%>
        <table>
          <tr>
            <td height=20>&nbsp;</td>
          </tr>
        </table>
        <table>
          <tr>
            <td align="left">
              <%=favPerspective.getMessage("FORM_LABEL_REGISTRY_URL",registrationURL)%>
            </td>
          </tr>
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
<jsp:include page="/forms/simpleCommon_table.jsp" flush="true"/>
      </div>      
    </form>
  </div>
<script language="javascript">
  setDefaults();
  resumeProxyLoadPage();  
</script>    
</body>
</html>
