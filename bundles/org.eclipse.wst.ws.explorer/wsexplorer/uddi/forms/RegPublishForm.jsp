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
<jsp:useBean id="sectionHeaderInfo" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.SectionHeaderInfo" scope="request"/>
<%
   UDDIPerspective uddiPerspective = controller.getUDDIPerspective();
   NodeManager navigatorManager = uddiPerspective.getNavigatorManager();
   // selectedNode must be a registry node.
   Node regNode = navigatorManager.getSelectedNode();
   FormTool formTool = (FormTool)(regNode.getCurrentToolManager().getSelectedTool());
%>
<jsp:useBean id="subQueryKeyProperty" class="org.eclipse.wst.ws.internal.explorer.platform.uddi.perspective.SubQueryKeyProperty" scope="request">
<%
   String subQueryKey = (String)formTool.getProperty(UDDIActionInputs.SUBQUERY_KEY);
   subQueryKeyProperty.setSubQueryKey(subQueryKey);
%>
</jsp:useBean>
<%
   FormToolPropertiesInterface formToolPI = ((MultipleFormToolPropertiesInterface)formTool).getFormToolProperties(subQueryKeyProperty.getSubQueryKey());
   RegistryElement regElement = (RegistryElement)regNode.getTreeElement();
%>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title><%=uddiPerspective.getMessage("FORM_TITLE_REGISTRY_PUBLISH")%></title>
  <link rel="stylesheet" type="text/css" href="<%=response.encodeURL(controller.getPathWithContext("css/windows.css"))%>">
<script language="javascript" src="<%=response.encodeURL(controller.getPathWithContext("scripts/browserdetect.js"))%>">
</script>
<jsp:include page="/scripts/formsubmit.jsp" flush="true"/>
<jsp:include page="/uddi/scripts/udditables.jsp" flush="true"/>
<script language="javascript">
  var sectionIds = ["publishBusiness","publishService","publishServiceInterface"];
  var publishBusinessSectionIds = ["publishBusinessSimple","publishBusinessAdvanced"];
  var publishServiceSectionIds = ["publishServiceSimple","publishServiceAdvanced"];
  var publishServiceInterfaceSectionIds = ["publishServiceInterfaceSimple","publishServiceInterfaceAdvanced"];
  var styleForms = ["publishBusinessStyle","publishServiceStyle","publishServiceInterfaceStyle"];

  function processPublishBusinessAdvancedForm(form)
  {
    if (handleSubmit(form))
    {
      processLanguageInputTable("publishBusinessAdvancedNames","<%=UDDIActionInputs.QUERY_INPUT_ADVANCED_NAME_LANGUAGE%>","<%=UDDIActionInputs.QUERY_INPUT_ADVANCED_NAME%>",form);
      processLanguageInputTable("publishBusinessAdvancedDescriptions","<%=UDDIActionInputs.QUERY_INPUT_ADVANCED_DESCRIPTION_LANGUAGE%>","<%=UDDIActionInputs.QUERY_INPUT_ADVANCED_DESCRIPTION%>",form);
      processIdentifierTable("publishBusinessAdvancedIdentifiers",form);
      processCategoryTable("publishBusinessAdvancedCategories",form,false);
      processDiscoveryURLTable("publishBusinessAdvancedDiscoveryURLs",form);
      return true;
    }
    return false;
  }

  function processPublishServiceSimpleForm(form)
  {
    if (handleSubmit(form))
    {
      processResultTable("publishServiceSimpleBusiness","<%=UDDIActionInputs.NODEID_BUSINESS%>",form,false);
      return true;
    }
    return false;
  }

  function processPublishServiceAdvancedForm(form)
  {
    if (handleSubmit(form))
    {
      processResultTable("publishServiceAdvancedBusiness","<%=UDDIActionInputs.NODEID_BUSINESS%>",form,false);
      processResultTable("publishServiceAdvancedServiceInterface","<%=UDDIActionInputs.NODEID_SERVICE_INTERFACE%>",form,false);
      processLanguageInputTable("publishServiceAdvancedNames","<%=UDDIActionInputs.QUERY_INPUT_ADVANCED_NAME_LANGUAGE%>","<%=UDDIActionInputs.QUERY_INPUT_ADVANCED_NAME%>",form);
      processLanguageInputTable("publishServiceAdvancedDescriptions","<%=UDDIActionInputs.QUERY_INPUT_ADVANCED_DESCRIPTION_LANGUAGE%>","<%=UDDIActionInputs.QUERY_INPUT_ADVANCED_DESCRIPTION%>",form);
      processCategoryTable("publishServiceAdvancedCategories",form,false);
      return true;
    }
    return false;
  }

  function processPublishServiceInterfaceAdvancedForm(form)
  {
    if (handleSubmit(form))
    {
      processLanguageInputTable("publishServiceInterfaceAdvancedDescriptions","<%=UDDIActionInputs.QUERY_INPUT_ADVANCED_DESCRIPTION_LANGUAGE%>","<%=UDDIActionInputs.QUERY_INPUT_ADVANCED_DESCRIPTION%>",form);
      processIdentifierTable("publishServiceInterfaceAdvancedIdentifiers",form);
      processCategoryTable("publishServiceInterfaceAdvancedCategories",form,false);
      return true;
    }
    return false;
  }

  function setDefaults()
  {
    var publish = document.getElementById("publish");
    publish.selectedIndex = <%=formToolPI.getProperty(UDDIActionInputs.QUERY_ITEM)%>;
    setDefaultsForPublishBusinessForms();
    setDefaultsForPublishServiceForms();
    setDefaultsForPublishServiceInterfaceForms();
<%
   if (!regElement.isLoggedIn())
   {
%>
    document.getElementById("publishBusinessSimpleAuthentication").style.display = "";
    document.getElementById("publishBusinessAdvancedAuthentication").style.display = "";
    document.getElementById("publishServiceSimpleAuthentication").style.display = "";
    document.getElementById("publishServiceAdvancedAuthentication").style.display = "";
    document.getElementById("publishServiceInterfaceSimpleAuthentication").style.display = "";
    document.getElementById("publishServiceInterfaceAdvancedAuthentication").style.display = "";
<%
   }
%>
    showMainForm(publish.value);
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

  function setDefaultsForPublishBusinessForms()
  {
    // Initialize the publishBusiness form.
<%
   String publishBusinessStyleIndex = (String)formToolPI.getProperty(UDDIActionInputs.QUERY_STYLE_BUSINESSES);
%>
    document.forms[styleForms[<%=UDDIActionInputs.QUERY_ITEM_BUSINESSES%>]].<%=UDDIActionInputs.QUERY_STYLE_BUSINESSES%>[<%=publishBusinessStyleIndex%>].checked = true;
    var publishBusinessSimpleSection = document.getElementById(publishBusinessSectionIds[<%=UDDIActionInputs.QUERY_STYLE_SIMPLE%>]);
    var publishBusinessSimpleForm = publishBusinessSimpleSection.getElementsByTagName("form").item(0);
    setAuthenticationSectionDefaults(publishBusinessSimpleForm);
    publishBusinessSimpleForm.<%=UDDIActionInputs.QUERY_INPUT_SIMPLE_BUSINESS_NAME%>.value = "<%=HTMLUtils.JSMangle((String)formToolPI.getProperty(UDDIActionInputs.QUERY_INPUT_SIMPLE_BUSINESS_NAME))%>";
    publishBusinessSimpleForm.<%=UDDIActionInputs.QUERY_INPUT_SIMPLE_BUSINESS_DESCRIPTION%>.value = "<%=HTMLUtils.JSMangle((String)formToolPI.getProperty(UDDIActionInputs.QUERY_INPUT_SIMPLE_BUSINESS_DESCRIPTION))%>";

    // Advanced form details.
    var publishBusinessAdvancedSection = document.getElementById(publishBusinessSectionIds[<%=UDDIActionInputs.QUERY_STYLE_ADVANCED%>]);
    var publishBusinessAdvancedForm = publishBusinessAdvancedSection.getElementsByTagName("form").item(0);
    setAuthenticationSectionDefaults(publishBusinessAdvancedForm);
    var publishBusinessAdvancedNamesTable = getTable("publishBusinessAdvancedNames");
    var publishBusinessAdvancedDescriptionsTable = getTable("publishBusinessAdvancedDescriptions");
    var publishBusinessAdvancedDiscoveryURLsTable = getTable("publishBusinessAdvancedDiscoveryURLs");
<%
   Vector busNameVector = (Vector)formToolPI.getProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_BUSINESS_NAMES);
   if (busNameVector != null)
   {
     for (int i=0;i<busNameVector.size();i++)
     {
       Name name = (Name)busNameVector.elementAt(i);
%>
    addLanguageInputRow("publishBusinessAdvancedNames","<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_CONTROL_TITLE_NAME_TEXT_VALUE"))%>");
    setLanguageInputRowSettings("publishBusinessAdvancedNames",<%=i%>,"<%=HTMLUtils.JSMangle(name.getLang())%>","<%=HTMLUtils.JSMangle(name.getText())%>");
<%
       if (!formToolPI.isRowInputValid(UDDIActionInputs.QUERY_INPUT_ADVANCED_BUSINESS_NAMES,i))
       {
%>
    highlightErrantRow(publishBusinessAdvancedNamesTable.rows[<%=i%>+numberOfHeaderRows],"errantrow");
<%
       }
     }
   }

   Vector busDescriptionVector = (Vector)formToolPI.getProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_BUSINESS_DESCRIPTIONS);
   if (busDescriptionVector != null)
   {
     for (int i=0;i<busDescriptionVector.size();i++)
     {
       Description description = (Description)busDescriptionVector.elementAt(i);
%>
    addLanguageInputRow("publishBusinessAdvancedDescriptions","<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_CONTROL_TITLE_DESCRIPTION_TEXT_VALUE"))%>");
    setLanguageInputRowSettings("publishBusinessAdvancedDescriptions",<%=i%>,"<%=HTMLUtils.JSMangle(description.getLang())%>","<%=HTMLUtils.JSMangle(description.getText())%>");
<%
       if (!formToolPI.isRowInputValid(UDDIActionInputs.QUERY_INPUT_ADVANCED_BUSINESS_DESCRIPTIONS,i))
       {
%>
    highlightErrantRow(publishBusinessAdvancedDescriptionsTable.rows[<%=i%>+numberOfHeaderRows],"errantrow");
<%
       }
     }
   }

   IdentifierBag busIdBag = (IdentifierBag)formToolPI.getProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_BUSINESS_IDENTIFIERS);
   if (busIdBag != null)
   {
     Vector keyedReferenceVector = busIdBag.getKeyedReferenceVector();
     for (int i=0;i<keyedReferenceVector.size();i++)
     {
       KeyedReference kr = (KeyedReference)keyedReferenceVector.elementAt(i);
%>
    addIdentifierRow("publishBusinessAdvancedIdentifiers");
    setIdentifierRowSettings("publishBusinessAdvancedIdentifiers",<%=i%>,"<%=HTMLUtils.JSMangle(kr.getTModelKey())%>","<%=HTMLUtils.JSMangle(kr.getKeyName())%>","<%=HTMLUtils.JSMangle(kr.getKeyValue())%>");
<%
     }
   }

   CategoryBag busCatBag = (CategoryBag)formToolPI.getProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_BUSINESS_CATEGORIES);
   if (busCatBag != null)
   {
     Vector keyedReferenceVector = busCatBag.getKeyedReferenceVector();
     for (int i=0;i<keyedReferenceVector.size();i++)
     {
       KeyedReference kr = (KeyedReference)keyedReferenceVector.elementAt(i);
%>
    addCategoryRow("publishBusinessAdvancedCategories");
    setCategoryRowSettings("publishBusinessAdvancedCategories",<%=i%>,"<%=HTMLUtils.JSMangle(kr.getTModelKey())%>","<%=HTMLUtils.JSMangle(kr.getKeyName())%>","<%=HTMLUtils.JSMangle(kr.getKeyValue())%>");
<%
     }
   }
  
   DiscoveryURLs discoveryURLs = (DiscoveryURLs)formToolPI.getProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_BUSINESS_DISCOVERYURLS);
   if (discoveryURLs != null)
   {
     Vector discoveryURLVector = discoveryURLs.getDiscoveryURLVector();
     for (int i=0;i<discoveryURLVector.size();i++)
     {
       DiscoveryURL discoveryURL = (DiscoveryURL)discoveryURLVector.elementAt(i);
%>
    addDiscoveryURLRow("publishBusinessAdvancedDiscoveryURLs");
    setDiscoveryURLRowSettings("publishBusinessAdvancedDiscoveryURLs",<%=i%>,"<%=HTMLUtils.JSMangle(discoveryURL.getText())%>");
<%
       if (!formToolPI.isRowInputValid(UDDIActionInputs.QUERY_INPUT_ADVANCED_BUSINESS_DISCOVERYURLS,i))
       {
%>
    highlightErrantRow(publishBusinessAdvancedDiscoveryURLsTable.rows[<%=i%>+numberOfHeaderRows],"errantrow");
<%
       }
     }
   }
%>       
    showPublishForm(<%=UDDIActionInputs.QUERY_ITEM_BUSINESSES%>,<%=publishBusinessStyleIndex%>);
  }

  function setDefaultsForPublishServiceForms()
  {
<%
   String publishServiceStyleIndex = (String)formToolPI.getProperty(UDDIActionInputs.QUERY_STYLE_SERVICES);
%>
    document.forms[styleForms[<%=UDDIActionInputs.QUERY_ITEM_SERVICES%>]].<%=UDDIActionInputs.QUERY_STYLE_SERVICES%>[<%=publishServiceStyleIndex%>].checked = true;
    var publishServiceSimpleSection = document.getElementById(publishServiceSectionIds[<%=UDDIActionInputs.QUERY_STYLE_SIMPLE%>]);
    var publishServiceSimpleForm = publishServiceSimpleSection.getElementsByTagName("form").item(0);
    setAuthenticationSectionDefaults(publishServiceSimpleForm);
<%
   Vector serviceBusiness = (Vector)formToolPI.getProperty(UDDIActionInputs.QUERY_INPUT_SIMPLE_SERVICE_BUSINESS);
   Vector serviceBusinessCopy = (Vector)formToolPI.getProperty(UDDIActionInputs.QUERY_INPUT_SIMPLE_SERVICE_BUSINESS_COPY);
   if (serviceBusiness != null)
   {
     if (serviceBusinessCopy == null)
       serviceBusinessCopy = new Vector();
     else
       serviceBusinessCopy.removeAllElements();
     for (int i=0;i<serviceBusiness.size();i++)
     {
       ListElement listElement = (ListElement)serviceBusiness.elementAt(i);
       int targetNodeId = listElement.getTargetNodeId();
       int targetToolId = listElement.getTargetToolId();
       int targetViewId = listElement.getTargetViewId();
       String url = SelectSubQueryItemAction.getActionLink(targetNodeId,targetToolId,targetViewId,subQueryKeyProperty.getSubQueryKey(),UDDIActionInputs.QUERY_INPUT_SIMPLE_SERVICE_BUSINESS,i,UDDIActionInputs.QUERY_ITEM_BUSINESSES,false);
       BusinessEntity be = (BusinessEntity)listElement.getObject();
       serviceBusinessCopy.addElement(serviceBusiness.elementAt(i));
%>
    addResultRow("publishServiceSimpleBusiness",<%=targetNodeId%>,"<%=response.encodeURL(controller.getPathWithContext(url))%>","<%=HTMLUtils.JSMangle(be.getDefaultNameString())%>","<%=HTMLUtils.JSMangle(be.getDefaultDescriptionString())%>");
<%
     }
     formToolPI.setProperty(UDDIActionInputs.QUERY_INPUT_SIMPLE_SERVICE_BUSINESS_COPY,serviceBusinessCopy);
   }
   else
   {
     serviceBusiness = new Vector();
     serviceBusinessCopy = new Vector();
     formToolPI.setProperty(UDDIActionInputs.QUERY_INPUT_SIMPLE_SERVICE_BUSINESS,serviceBusiness);
     formToolPI.setProperty(UDDIActionInputs.QUERY_INPUT_SIMPLE_SERVICE_BUSINESS_COPY,serviceBusinessCopy);
   }
%>
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
    publishServiceSimpleForm.<%=ActionInputs.QUERY_INPUT_WSDL_URL%>.value = "<%=HTMLUtils.JSMangle((String)formToolPI.getProperty(UDDIActionInputs.QUERY_INPUT_SIMPLE_SERVICE_WSDL_URL))%>";
    publishServiceSimpleForm.<%=UDDIActionInputs.QUERY_INPUT_SIMPLE_SERVICE_NAME%>.value = "<%=HTMLUtils.JSMangle((String)formToolPI.getProperty(UDDIActionInputs.QUERY_INPUT_SIMPLE_SERVICE_NAME))%>";
    publishServiceSimpleForm.<%=UDDIActionInputs.QUERY_INPUT_SIMPLE_SERVICE_DESCRIPTION%>.value = "<%=HTMLUtils.JSMangle((String)formToolPI.getProperty(UDDIActionInputs.QUERY_INPUT_SIMPLE_SERVICE_DESCRIPTION))%>";

    // Advanced form details.
    var publishServiceAdvancedSection = document.getElementById(publishServiceSectionIds[<%=UDDIActionInputs.QUERY_STYLE_ADVANCED%>]);
    var publishServiceAdvancedForm = publishServiceAdvancedSection.getElementsByTagName("form").item(0);
    setAuthenticationSectionDefaults(publishServiceAdvancedForm);
    var publishServiceAdvancedNamesTable = getTable("publishServiceAdvancedNames");
    var publishServiceAdvancedDescriptionsTable = getTable("publishServiceAdvancedDescriptions");
<%
   serviceBusiness = (Vector)formToolPI.getProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_BUSINESS);
   serviceBusinessCopy = (Vector)formToolPI.getProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_BUSINESS_COPY);
   if (serviceBusiness != null)
   {
     if (serviceBusinessCopy == null)
       serviceBusinessCopy = new Vector();
     else
       serviceBusinessCopy.removeAllElements();
     for (int i=0;i<serviceBusiness.size();i++)
     {
       ListElement listElement = (ListElement)serviceBusiness.elementAt(i);
       int targetNodeId = listElement.getTargetNodeId();
       int targetToolId = listElement.getTargetToolId();
       int targetViewId = listElement.getTargetViewId();
       String url = SelectSubQueryItemAction.getActionLink(targetNodeId,targetToolId,targetViewId,subQueryKeyProperty.getSubQueryKey(),UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_BUSINESS,i,UDDIActionInputs.QUERY_ITEM_BUSINESSES,false);
       BusinessEntity be = (BusinessEntity)listElement.getObject();
       serviceBusinessCopy.addElement(serviceBusiness.elementAt(i));
%>
    addResultRow("publishServiceAdvancedBusiness",<%=targetNodeId%>,"<%=response.encodeURL(controller.getPathWithContext(url))%>","<%=HTMLUtils.JSMangle(be.getDefaultNameString())%>","<%=HTMLUtils.JSMangle(be.getDefaultDescriptionString())%>");
<%
     }
     formToolPI.setProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_BUSINESS_COPY,serviceBusinessCopy);
   }
   else
   {
     serviceBusiness = new Vector();
     serviceBusinessCopy = new Vector();
     formToolPI.setProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_BUSINESS,serviceBusiness);
     formToolPI.setProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_BUSINESS_COPY,serviceBusinessCopy);
   }
%>
    publishServiceAdvancedForm.<%=ActionInputs.QUERY_INPUT_WSDL_URL%>.value = "<%=HTMLUtils.JSMangle((String)formToolPI.getProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_WSDL_URL))%>";
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
%>
    showPublishForm(<%=UDDIActionInputs.QUERY_ITEM_SERVICES%>,<%=publishServiceStyleIndex%>);
  }

  function setDefaultsForPublishServiceInterfaceForms()
  {
<%
   String publishServiceInterfaceStyleIndex = (String)formToolPI.getProperty(UDDIActionInputs.QUERY_STYLE_SERVICE_INTERFACES);
%>
    document.forms[styleForms[<%=UDDIActionInputs.QUERY_ITEM_SERVICE_INTERFACES%>]].<%=UDDIActionInputs.QUERY_STYLE_SERVICE_INTERFACES%>[<%=publishServiceInterfaceStyleIndex%>].checked = true;
    var publishServiceInterfaceSimpleSection = document.getElementById(publishServiceInterfaceSectionIds[<%=UDDIActionInputs.QUERY_STYLE_SIMPLE%>]);
    var publishServiceInterfaceSimpleForm = publishServiceInterfaceSimpleSection.getElementsByTagName("form").item(0);
    setAuthenticationSectionDefaults(publishServiceInterfaceSimpleForm);
    publishServiceInterfaceSimpleForm.<%=ActionInputs.QUERY_INPUT_WSDL_URL%>.value = "<%=HTMLUtils.JSMangle((String)formToolPI.getProperty(UDDIActionInputs.QUERY_INPUT_SIMPLE_SERVICE_INTERFACE_WSDL_URL))%>";
    publishServiceInterfaceSimpleForm.<%=UDDIActionInputs.QUERY_INPUT_SIMPLE_SERVICE_INTERFACE_NAME%>.value = "<%=HTMLUtils.JSMangle((String)formToolPI.getProperty(UDDIActionInputs.QUERY_INPUT_SIMPLE_SERVICE_INTERFACE_NAME))%>";
    publishServiceInterfaceSimpleForm.<%=UDDIActionInputs.QUERY_INPUT_SIMPLE_SERVICE_INTERFACE_DESCRIPTION%>.value = "<%=HTMLUtils.JSMangle((String)formToolPI.getProperty(UDDIActionInputs.QUERY_INPUT_SIMPLE_SERVICE_INTERFACE_DESCRIPTION))%>";

    // Advanced form details.
    var publishServiceInterfaceAdvancedSection = document.getElementById(publishServiceInterfaceSectionIds[<%=UDDIActionInputs.QUERY_STYLE_ADVANCED%>]);
    var publishServiceInterfaceAdvancedForm = publishServiceInterfaceAdvancedSection.getElementsByTagName("form").item(0);
    setAuthenticationSectionDefaults(publishServiceInterfaceAdvancedForm);
    publishServiceInterfaceAdvancedForm.<%=ActionInputs.QUERY_INPUT_WSDL_URL%>.value = "<%=HTMLUtils.JSMangle((String)formToolPI.getProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_WSDL_URL))%>";
    publishServiceInterfaceAdvancedForm.<%=UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_NAME%>.value = "<%=HTMLUtils.JSMangle((String)formToolPI.getProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_NAME))%>";
    var publishServiceInterfaceAdvancedDescriptionsTable = getTable("publishServiceInterfaceAdvancedDescriptions");
<%
   Vector siDescriptionVector = (Vector)formToolPI.getProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_DESCRIPTIONS);
   if (siDescriptionVector != null)
   {
     for (int i=0;i<siDescriptionVector.size();i++)
     {
       Description description = (Description)siDescriptionVector.elementAt(i);
%>
    addLanguageInputRow("publishServiceInterfaceAdvancedDescriptions","<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_CONTROL_TITLE_DESCRIPTION_TEXT_VALUE"))%>");
    setLanguageInputRowSettings("publishServiceInterfaceAdvancedDescriptions",<%=i%>,"<%=HTMLUtils.JSMangle(description.getLang())%>","<%=HTMLUtils.JSMangle(description.getText())%>");
<%
       if (!formToolPI.isRowInputValid(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_DESCRIPTIONS,i))
       {
%>
    highlightErrantRow(publishServiceInterfaceAdvancedDescriptionsTable.rows[<%=i%>+numberOfHeaderRows],"errantrow");
<%
       }
     }
   }

   IdentifierBag siIdBag = (IdentifierBag)formToolPI.getProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_IDENTIFIERS);
   if (siIdBag != null)
   {
     Vector keyedReferenceVector = siIdBag.getKeyedReferenceVector();
     for (int i=0;i<keyedReferenceVector.size();i++)
     {
       KeyedReference kr = (KeyedReference)keyedReferenceVector.elementAt(i);
%>
    addIdentifierRow("publishServiceInterfaceAdvancedIdentifiers");
    setIdentifierRowSettings("publishServiceInterfaceAdvancedIdentifiers",<%=i%>,"<%=HTMLUtils.JSMangle(kr.getTModelKey())%>","<%=HTMLUtils.JSMangle(kr.getKeyName())%>","<%=HTMLUtils.JSMangle(kr.getKeyValue())%>");
<%
     }
   }

   CategoryBag siCatBag = (CategoryBag)formToolPI.getProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_CATEGORIES);
   if (siCatBag != null)
   {
     Vector keyedReferenceVector = siCatBag.getKeyedReferenceVector();
     for (int i=0;i<keyedReferenceVector.size();i++)
     {
       KeyedReference kr = (KeyedReference)keyedReferenceVector.elementAt(i);
%>
    addCategoryRow("publishServiceInterfaceAdvancedCategories");
    setCategoryRowSettings("publishServiceInterfaceAdvancedCategories",<%=i%>,"<%=HTMLUtils.JSMangle(kr.getTModelKey())%>","<%=HTMLUtils.JSMangle(kr.getKeyName())%>","<%=HTMLUtils.JSMangle(kr.getKeyValue())%>");
<%
     }
   }
%>
    showPublishForm(<%=UDDIActionInputs.QUERY_ITEM_SERVICE_INTERFACES%>,<%=publishServiceInterfaceStyleIndex%>);
  }

  function showMainForm(publishValue)
  {
    closeAllUddiChildWindows();
    var publish = parseInt(publishValue);
    for (var i=0;i<sectionIds.length;i++)
    {
      if (i == publish)
        document.getElementById(sectionIds[i]).style.display = "";
      else
        document.getElementById(sectionIds[i]).style.display = "none";
    }
    var loadScreenTable = document.getElementById("loadScreen");
    if (loadScreenTable.rows.length > 0)
      loadScreenTable.deleteRow(0);
    document.getElementById("mainScreen").style.display = "";
  }

  function showPublishForm(queryItemIndex,style)
  {
    closeAllUddiChildWindows();
    var publishSectionIds;
    switch (queryItemIndex)
    {
      case <%=UDDIActionInputs.QUERY_ITEM_BUSINESSES%>:
        publishSectionIds = publishBusinessSectionIds;
        break;
      case <%=UDDIActionInputs.QUERY_ITEM_SERVICES%>:
        publishSectionIds = publishServiceSectionIds;
        break;
      case <%=UDDIActionInputs.QUERY_ITEM_SERVICE_INTERFACES%>:
      default:
        publishSectionIds = publishServiceInterfaceSectionIds;
        break;
    }
    for (var i=0;i<publishSectionIds.length;i++)
    {
      if (i == style)
        document.getElementById(publishSectionIds[i]).style.display = "";
      else
        document.getElementById(publishSectionIds[i]).style.display = "none";
    }
  }
</script>
</head>
<body dir="<%=org.eclipse.wst.ws.internal.explorer.platform.util.DirUtils.getDir()%>" class="contentbodymargin" onUnload="closeAllUddiChildWindows()">
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
   String titleImagePath = "uddi/images/publish_highlighted.gif";
   String title = uddiPerspective.getMessage("ALT_PUBLISH");
%>
<%@ include file="/forms/formheader.inc" %>
      <table width="95%" border=0 cellpadding=3 cellspacing=0>
        <tr>
          <td class="labels" valign="middle" nowrap>
            <label for="publish"><%=uddiPerspective.getMessage("FORM_LABEL_PUBLISH")%></label>
          </td>
          <td valign="middle" nowrap>
            <select id="publish" onChange="showMainForm(this.value)" class="selectlist">
              <option value="<%=UDDIActionInputs.QUERY_ITEM_BUSINESSES%>"><%=uddiPerspective.getMessage("FORM_OPTION_BUSINESS")%>
              <option value="<%=UDDIActionInputs.QUERY_ITEM_SERVICES%>"><%=uddiPerspective.getMessage("FORM_OPTION_SERVICE")%>
              <option value="<%=UDDIActionInputs.QUERY_ITEM_SERVICE_INTERFACES%>"><%=uddiPerspective.getMessage("FORM_OPTION_SERVICE_INTERFACE")%>
            </select>
          </td>
          <td width="90%">&nbsp;</td>
        </tr>
      </table>
    </div>
<jsp:include page="/uddi/forms/RegPublishBusinessForm.jsp" flush="true"/>
<jsp:include page="/uddi/forms/RegPublishServiceForm.jsp" flush="true"/>
<jsp:include page="/uddi/forms/RegPublishServiceInterfaceForm.jsp" flush="true"/>
  </div>
<script language="javascript">
  setDefaults();
</script>
</body>
</html>
