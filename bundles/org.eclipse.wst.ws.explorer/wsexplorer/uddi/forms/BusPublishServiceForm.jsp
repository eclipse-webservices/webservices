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
                                                        org.eclipse.wst.ws.internal.explorer.platform.uddi.actions.SelectSubQueryItemAction,
                                                        org.eclipse.wst.ws.internal.explorer.platform.perspective.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.constants.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.datamodel.ListElement,
                                                        org.eclipse.wst.ws.internal.explorer.platform.util.*,
                                                        org.uddi4j.datatype.*,
                                                        org.uddi4j.datatype.tmodel.TModel,
                                                        org.uddi4j.util.*,
                                                        java.util.*" %>

<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<jsp:useBean id="sectionHeaderInfo" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.SectionHeaderInfo" scope="request"/>
<%
   UDDIPerspective uddiPerspective = controller.getUDDIPerspective();
   NodeManager navigatorManager = uddiPerspective.getNavigatorManager();
   // selectedNode must be a business node.
   Node busNode = navigatorManager.getSelectedNode();
   FormTool formTool = (FormTool)(busNode.getCurrentToolManager().getSelectedTool());
%>
<jsp:useBean id="subQueryKeyProperty" class="org.eclipse.wst.ws.internal.explorer.platform.uddi.perspective.SubQueryKeyProperty" scope="request">
<%
   String subQueryKey = (String)formTool.getProperty(UDDIActionInputs.SUBQUERY_KEY);
   subQueryKeyProperty.setSubQueryKey(subQueryKey);
%>
</jsp:useBean>
<%
   FormToolPropertiesInterface formToolPI = ((MultipleFormToolPropertiesInterface)formTool).getFormToolProperties(subQueryKeyProperty.getSubQueryKey());
   // business parent node may be either the published items folder or a query node.
   UDDIMainNode uddiMainNode = (UDDIMainNode)navigatorManager.getRootNode();
   Node regNode = uddiMainNode.getRegistryNode(busNode);
   RegistryElement regElement = (RegistryElement)regNode.getTreeElement();
%>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title><%=uddiPerspective.getMessage("FORM_TITLE_BUSINESS_PUBLISH_SERVICE")%></title>
  <link rel="stylesheet" type="text/css" href="<%=response.encodeURL(controller.getPathWithContext("css/windows.css"))%>">
<script language="javascript" src="<%=response.encodeURL(controller.getPathWithContext("scripts/browserdetect.js"))%>">
</script>
<jsp:include page="/scripts/formsubmit.jsp" flush="true"/>
<jsp:include page="/scripts/tables.jsp" flush="true"/>
<jsp:include page="/uddi/scripts/udditables.jsp" flush="true"/>
<script language="javascript">
  var sectionIds = ["publishServiceSimple","publishServiceAdvanced"];
  var styleForm = "publishServiceStyle";
  
  function processAdvancedForm(form)
  {
    if (handleSubmit(form))
    {
      processResultTable("publishServiceAdvancedServiceInterface","<%=UDDIActionInputs.NODEID_SERVICE_INTERFACE%>",form,false);
      processLanguageInputTable("publishServiceAdvancedNames","<%=UDDIActionInputs.QUERY_INPUT_ADVANCED_NAME_LANGUAGE%>","<%=UDDIActionInputs.QUERY_INPUT_ADVANCED_NAME%>",form);
      processLanguageInputTable("publishServiceAdvancedDescriptions","<%=UDDIActionInputs.QUERY_INPUT_ADVANCED_DESCRIPTION_LANGUAGE%>","<%=UDDIActionInputs.QUERY_INPUT_ADVANCED_DESCRIPTION%>",form);
      processCategoryTable("publishServiceAdvancedCategories",form,false);
      return true;
    }
    return false;
  }
  
  function setAuthenticationSectionDefaults(form)
  {
<%
   String publishURL = (String)formTool.getProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_PUBLISH_URL);
   String userId = (String)formTool.getProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_USERID);
   String password = (String)formTool.getProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_PASSWORD);
%>
    form.<%=UDDIActionInputs.QUERY_INPUT_ADVANCED_PUBLISH_URL%>.value = "<%=HTMLUtils.JSMangle(publishURL)%>";
    form.<%=UDDIActionInputs.QUERY_INPUT_ADVANCED_USERID%>.value = "<%=HTMLUtils.JSMangle(userId)%>";
    form.<%=UDDIActionInputs.QUERY_INPUT_ADVANCED_PASSWORD%>.value = "<%=HTMLUtils.JSMangle(password)%>";
  }
  
  function setDefaults()
  {
<%
   String publishServiceStyleIndex = (String)formToolPI.getProperty(UDDIActionInputs.QUERY_STYLE_SERVICES);
%>
    document.forms[styleForm].<%=UDDIActionInputs.QUERY_STYLE_SERVICES%>[<%=publishServiceStyleIndex%>].checked = true;
    
    var publishServiceSimpleSection = document.getElementById(sectionIds[<%=UDDIActionInputs.QUERY_STYLE_SIMPLE%>]);
    var publishServiceSimpleForm = publishServiceSimpleSection.getElementsByTagName("form").item(0);
    setAuthenticationSectionDefaults(publishServiceSimpleForm);
    publishServiceSimpleForm.<%=ActionInputs.QUERY_INPUT_WSDL_URL%>.value = "<%=HTMLUtils.JSMangle((String)formToolPI.getProperty(UDDIActionInputs.QUERY_INPUT_SIMPLE_SERVICE_WSDL_URL))%>";
    publishServiceSimpleForm.<%=UDDIActionInputs.QUERY_INPUT_SIMPLE_SERVICE_NAME%>.value = "<%=HTMLUtils.JSMangle((String)formToolPI.getProperty(UDDIActionInputs.QUERY_INPUT_SIMPLE_SERVICE_NAME))%>";
    publishServiceSimpleForm.<%=UDDIActionInputs.QUERY_INPUT_SIMPLE_SERVICE_DESCRIPTION%>.value = "<%=HTMLUtils.JSMangle((String)formToolPI.getProperty(UDDIActionInputs.QUERY_INPUT_SIMPLE_SERVICE_DESCRIPTION))%>";
    
    // Advanced form details.
    var publishServiceAdvancedSection = document.getElementById(sectionIds[<%=UDDIActionInputs.QUERY_STYLE_ADVANCED%>]);
    var publishServiceAdvancedForm = publishServiceAdvancedSection.getElementsByTagName("form").item(0);
    setAuthenticationSectionDefaults(publishServiceAdvancedForm);
    var publishServiceAdvancedNamesTable = getTable("publishServiceAdvancedNames");
    var publishServiceAdvancedDescriptionsTable = getTable("publishServiceAdvancedDescriptions");
    publishServiceAdvancedForm.<%=ActionInputs.QUERY_INPUT_WSDL_URL%>.value = "<%=HTMLUtils.JSMangle((String)formToolPI.getProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_WSDL_URL))%>";
<%
   Vector serviceInterfaces = (Vector)formToolPI.getProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_SERVICE_INTERFACES);
   Vector serviceInterfacesCopy = (Vector)formToolPI.getProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_SERVICE_INTERFACES_COPY);
   if (serviceInterfaces != null)
   {
     if (serviceInterfacesCopy == null)
       serviceInterfacesCopy = new Vector();
     else
       serviceInterfacesCopy.removeAllElements();
     for (int i=0;i<serviceInterfaces.size();i++)
     {
       ListElement listElement = (ListElement)serviceInterfaces.elementAt(i);
       int targetNodeId = listElement.getTargetNodeId();
       int targetToolId = listElement.getTargetToolId();
       int targetViewId = listElement.getTargetViewId();
       String url = SelectSubQueryItemAction.getActionLink(targetNodeId,targetToolId,targetViewId,subQueryKeyProperty.getSubQueryKey(),UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_SERVICE_INTERFACES,i,UDDIActionInputs.QUERY_ITEM_SERVICE_INTERFACES,false);
       TModel tModel = (TModel)listElement.getObject();
       serviceInterfacesCopy.addElement(serviceInterfaces.elementAt(i));
%>
    addResultRow("publishServiceAdvancedServiceInterface",<%=targetNodeId%>,"<%=response.encodeURL(controller.getPathWithContext(url))%>","<%=HTMLUtils.JSMangle(tModel.getNameString())%>","<%=HTMLUtils.JSMangle(tModel.getDefaultDescriptionString())%>");
<%
     }
     formToolPI.setProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_SERVICE_INTERFACES_COPY,serviceInterfacesCopy);
   }
   else
   {
     serviceInterfaces = new Vector();
     serviceInterfacesCopy = new Vector();
     formToolPI.setProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_SERVICE_INTERFACES,serviceInterfaces);
     formToolPI.setProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_SERVICE_INTERFACES_COPY,serviceInterfacesCopy);
   }
%>
<%
   Vector serviceNameVector = (Vector)formToolPI.getProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_NAMES);
   if (serviceNameVector != null)
   {
     for (int i=0;i<serviceNameVector.size();i++)
     {
       Name name = (Name)serviceNameVector.elementAt(i);
%>
    addLanguageInputRow("publishServiceAdvancedNames","<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_CONTROL_TITLE_NAME_TEXT_VALUE"))%>");
    setLanguageInputRowSettings("publishServiceAdvancedNames",<%=i%>,"<%=HTMLUtils.JSMangle(name.getLang())%>","<%=HTMLUtils.JSMangle(name.getText())%>");
<%
       if (!formToolPI.isRowInputValid(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_NAMES,i))
       {
%>
    highlightErrantRow(publishServiceAdvancedNamesTable.rows[<%=i%>+numberOfHeaderRows],"errantrow");
<%
       }
     }
   }

   Vector serviceDescriptionVector = (Vector)formToolPI.getProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_DESCRIPTIONS);
   if (serviceDescriptionVector != null)
   {
     for (int i=0;i<serviceDescriptionVector.size();i++)
     {
       Description description = (Description)serviceDescriptionVector.elementAt(i);
%>
    addLanguageInputRow("publishServiceAdvancedDescriptions","<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_CONTROL_TITLE_DESCRIPTION_TEXT_VALUE"))%>");
    setLanguageInputRowSettings("publishServiceAdvancedDescriptions",<%=i%>,"<%=HTMLUtils.JSMangle(description.getLang())%>","<%=HTMLUtils.JSMangle(description.getText())%>");
<%
       if (!formToolPI.isRowInputValid(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_DESCRIPTIONS,i))
       {
%>
    highlightErrantRow(publishServiceAdvancedDescriptionsTable.rows[<%=i%>+numberOfHeaderRows],"errantrow");
<%
       }
     }
   }

   CategoryBag serviceCatBag = (CategoryBag)formToolPI.getProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_CATEGORIES);
   if (serviceCatBag != null)
   {
     Vector keyedReferenceVector = serviceCatBag.getKeyedReferenceVector();
     for (int i=0;i<keyedReferenceVector.size();i++)
     {
       KeyedReference kr = (KeyedReference)keyedReferenceVector.elementAt(i);
%>
    addCategoryRow("publishServiceAdvancedCategories");
    setCategoryRowSettings("publishServiceAdvancedCategories",<%=i%>,"<%=HTMLUtils.JSMangle(kr.getTModelKey())%>","<%=HTMLUtils.JSMangle(kr.getKeyName())%>","<%=HTMLUtils.JSMangle(kr.getKeyValue())%>");
<%
     }
   }
   
   if (!regElement.isLoggedIn())
   {
%>
    document.getElementById("publishServiceSimpleAuthentication").style.display = "";
    document.getElementById("publishServiceAdvancedAuthentication").style.display = "";
<%
   }
%>
    showPublishForm(<%=publishServiceStyleIndex%>);    
  }
  
  function showPublishForm(style)
  {
    closeAllUddiChildWindows();
    for (var i=0;i<sectionIds.length;i++)
    {
      if (i == style)
        document.getElementById(sectionIds[i]).style.display = "";
      else
        document.getElementById(sectionIds[i]).style.display = "none";
    }
    var loadScreenTable = document.getElementById("loadScreen");
    if (loadScreenTable.rows.length > 0)
      loadScreenTable.deleteRow(0);
    document.getElementById("mainScreen").style.display = "";    
  }
</script>
</head>
<body dir="<%=org.eclipse.wst.ws.internal.explorer.platform.util.DirUtils.getDir()%>" class="contentbodymargin" onUnload="closeAllUddiChildWindows()">
  <div id="contentborder">
    <table id="loadScreen">
      <tr>
        <td>
          <%=controller.getMessage("MSG_LOAD_IN_PROGRESS")%>
        </td>
      </tr>
    </table>
    <div id="mainScreen" style="display:none;">
<%
   String titleImagePath = "uddi/images/publish_service_highlighted.gif";
   String title = uddiPerspective.getMessage("ALT_PUBLISH_SERVICE");
%>
<%@ include file="/forms/formheader.inc" %>
      <form name="publishServiceStyle" style="margin-top:0;">
        <table width="95%" border=0 cellpadding=3 cellspacing=0>
          <tr>
            <td class="labels">
              <%=uddiPerspective.getMessage("FORM_LABEL_PUBLISH_FORM")%>
            </td>
          </tr>        
          <tr>
            <td>
              <input type="radio" id="radio_business_publish_service_simple" class="radio" name="<%=UDDIActionInputs.QUERY_STYLE_SERVICES%>" onClick="showPublishForm(<%=UDDIActionInputs.QUERY_STYLE_SIMPLE%>)"><label for="radio_business_publish_service_simple"><%=uddiPerspective.getMessage("FORM_RADIO_SIMPLE")%></label>
              <input type="radio" id="radio_business_publish_service_advance" class="radio" name="<%=UDDIActionInputs.QUERY_STYLE_SERVICES%>" onClick="showPublishForm(<%=UDDIActionInputs.QUERY_STYLE_ADVANCED%>)"><label for="radio_business_publish_service_advance"><%=uddiPerspective.getMessage("FORM_RADIO_ADVANCED")%></label>
            </td>
          </tr>
        </table>
      </form>
<jsp:include page="/uddi/forms/BusPublishServiceSimpleForm.jsp" flush="true"/>
<jsp:include page="/uddi/forms/BusPublishServiceAdvancedForm.jsp" flush="true"/>
    </div>
  </div>
<script language="javascript">
  setDefaults();
</script>  
</body>
</html>
