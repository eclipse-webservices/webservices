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
                                                        org.eclipse.wst.ws.internal.explorer.platform.uddi.actions.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.perspective.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.constants.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.datamodel.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.util.*,
                                                        org.uddi4j.datatype.*,
                                                        org.uddi4j.datatype.business.*,
                                                        org.uddi4j.datatype.service.*,
                                                        org.uddi4j.datatype.tmodel.*,
                                                        org.uddi4j.util.*,
                                                        java.util.*" %>

<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<%
   UDDIPerspective uddiPerspective = controller.getUDDIPerspective();
   NodeManager navigatorManager = uddiPerspective.getNavigatorManager();
   // selectedNode could be the registry or query nodes.
   Node selectedNode = navigatorManager.getSelectedNode();
   FormTool formTool = (FormTool)(selectedNode.getCurrentToolManager().getSelectedTool());
%>
<jsp:useBean id="subQueryKeyProperty" class="org.eclipse.wst.ws.internal.explorer.platform.uddi.perspective.SubQueryKeyProperty" scope="request">
<%
   String subQueryKey = (String)formTool.getProperty(UDDIActionInputs.SUBQUERY_KEY);
   subQueryKeyProperty.setSubQueryKey(subQueryKey);
%>
</jsp:useBean>
<%
   FormToolPropertiesInterface formToolPI = ((MultipleFormToolPropertiesInterface)formTool).getFormToolProperties(subQueryKeyProperty.getSubQueryKey());
   UDDIMainNode uddiMainNode = (UDDIMainNode)navigatorManager.getRootNode();
   Node regNode = uddiMainNode.getRegistryNode(selectedNode);
   RegistryElement regElement = (RegistryElement)regNode.getTreeElement();
%>

<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title><%=uddiPerspective.getMessage("FORM_TITLE_REGISTRY_FIND")%></title>
  <link rel="stylesheet" type="text/css" href="<%=response.encodeURL(controller.getPathWithContext("css/windows.css"))%>">
<script language="javascript" src="<%=response.encodeURL(controller.getPathWithContext("scripts/browserdetect.js"))%>">
</script>
<jsp:include page="/scripts/formsubmit.jsp" flush="true"/>
<jsp:include page="/scripts/tables.jsp" flush="true"/>
<jsp:include page="/uddi/scripts/udditables.jsp" flush="true"/>
<script language="javascript">
  var sectionIds = ["findBusinesses","findServices","findServiceInterfaces"];
  var findBusinessesSectionIds = ["findBusinessesSimple","findBusinessesAdvanced","findBusinessUUID"];
  var findServicesSectionIds = ["findServicesSimple","findServicesAdvanced","findServiceUUID"];
  var findServiceInterfacesSectionIds = ["findServiceInterfacesSimple","findServiceInterfacesAdvanced","findServiceInterfaceUUID"];
  var styleForms = ["findBusinessesStyle","findServicesStyle","findServiceInterfacesStyle"];
  var authenticationSectionIds = ["findBusinessesAdvancedAuthentication","findServicesAdvancedAuthentication","findServiceInterfacesAdvancedAuthentication"];

  function validateQueryName()
  {
<%
   String subQueryKeyValue = subQueryKeyProperty.getSubQueryKey();
   boolean isSubQuery = (subQueryKeyValue != null && subQueryKeyValue.length() > 0);
%>   
    if (<%=isSubQuery%>)
    {
      // Subqueries must have a name which is different from that of the current query or the closest query enclosing this object.
<%
   String currentQueryName = "";
   if (selectedNode instanceof QueryNode)
     currentQueryName = selectedNode.getNodeName();
   else if (selectedNode instanceof BusinessNode)
   {
     Node parentNode = selectedNode.getParent();
     if (parentNode instanceof QueryNode)
       currentQueryName = parentNode.getNodeName();
   }
%>    
      var queryName = document.getElementById("queryName").value;
      if (queryName == "<%=HTMLUtils.JSMangle(currentQueryName)%>")
      {
        alert("<%=uddiPerspective.getMessage("MSG_ERROR_QUERY_NAME_CONFLICT")%>");
        return false;
      }
    }
    return true;
  }
  
  function processForm(form)
  {
    if (!handleSubmit(form))
      return false;
    if (!validateQueryName())
      return false;
    form.<%=UDDIActionInputs.SUBQUERY_KEY%>.value = "<%=subQueryKeyProperty.getSubQueryKey()%>";
    form.<%=UDDIActionInputs.QUERY_NAME%>.value = document.getElementById("queryName").value;
    return true;
  }

  function processFindBusinessesAdvancedForm(form)
  {
    if (processForm(form))
    {
      processLanguageInputTable("findBusinessesAdvancedNames","<%=UDDIActionInputs.QUERY_INPUT_ADVANCED_NAME_LANGUAGE%>","<%=UDDIActionInputs.QUERY_INPUT_ADVANCED_NAME%>",form);
      processNameFindQualifiers("findBusinessesAdvancedNames",form);
      processIdentifierTable("findBusinessesAdvancedIdentifiers",form);
      processCategoryTable("findBusinessesAdvancedCategories",form,true);
      processResultTable("findBusinessesAdvancedServiceInterfaces","<%=UDDIActionInputs.NODEID_SERVICE_INTERFACE%>",form,false);
      processDiscoveryURLTable("findBusinessesAdvancedDiscoveryURLs",form);
      processFindQualifierTable("findBusinessesAdvancedFindQualifiers",form);
      return true;
    }
    return false;
  }

  function processFindServicesAdvancedForm(form)
  {
    if (processForm(form))
    {
      processResultTable("findServicesAdvancedBusiness","<%=UDDIActionInputs.NODEID_BUSINESS%>",form,false);
      processLanguageInputTable("findServicesAdvancedNames","<%=UDDIActionInputs.QUERY_INPUT_ADVANCED_NAME_LANGUAGE%>","<%=UDDIActionInputs.QUERY_INPUT_ADVANCED_NAME%>",form);
      processNameFindQualifiers("findServicesAdvancedNames",form);
      processCategoryTable("findServicesAdvancedCategories",form,false);
      processResultTable("findServicesAdvancedServiceInterfaces","<%=UDDIActionInputs.NODEID_SERVICE_INTERFACE%>",form,false);
      processFindQualifierTable("findServicesAdvancedFindQualifiers",form);
      return true;
    }
    return false;
  }

  function processFindServiceInterfacesAdvancedForm(form)
  {
    if (processForm(form))
    {
      processResultTable("findServiceInterfacesAdvancedService","<%=UDDIActionInputs.NODEID_SERVICE%>",form,false);
      processNameFindQualifiers("findServiceInterfacesAdvancedName",form);
      processIdentifierTable("findServiceInterfacesAdvancedIdentifiers",form);
      processCategoryTable("findServiceInterfacesAdvancedCategories",form,false);
      processFindQualifierTable("findServiceInterfacesAdvancedFindQualifiers",form);
      return true;
    }
    return false;
  }

  function setDefaults()
  {
    // Set the query name
    document.getElementById("queryName").value = "<%=HTMLUtils.JSMangle((String)formToolPI.getProperty(UDDIActionInputs.QUERY_NAME))%>";
    var searchFor = document.getElementById("searchFor");
    searchFor.selectedIndex = <%=formToolPI.getProperty(UDDIActionInputs.QUERY_ITEM)%>;
    setDefaultsForFindBusinessesForms();
    setDefaultsForFindServicesForms();
    setDefaultsForFindServiceInterfacesForms();
    showMainForm(searchFor.value);
  }

  function setAuthenticationSectionDefaults(advancedForm,queryItemIndex,isOwnedChecked)
  {
<%
   String publishURL = (String)formTool.getProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_PUBLISH_URL);
   String userId = (String)formTool.getProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_USERID);
   String password = (String)formTool.getProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_PASSWORD);
%>
    advancedForm.<%=UDDIActionInputs.QUERY_INPUT_ADVANCED_OWNED%>.checked = isOwnedChecked;
    advancedForm.<%=UDDIActionInputs.QUERY_INPUT_ADVANCED_PUBLISH_URL%>.value = "<%=HTMLUtils.JSMangle(publishURL)%>";
    advancedForm.<%=UDDIActionInputs.QUERY_INPUT_ADVANCED_USERID%>.value = "<%=HTMLUtils.JSMangle(userId)%>";
    advancedForm.<%=UDDIActionInputs.QUERY_INPUT_ADVANCED_PASSWORD%>.value = "<%=HTMLUtils.JSMangle(password)%>";
    toggleAdvancedAuthenticationSection(queryItemIndex,isOwnedChecked);
  }

  function setAdvancedCommonSectionDefaults(form,maxSearchSet,maxResults)
  {
    form.<%=UDDIActionInputs.QUERY_INPUT_ADVANCED_MAX_SEARCH_SET%>.value = maxSearchSet;
    form.<%=UDDIActionInputs.QUERY_INPUT_ADVANCED_MAX_RESULTS%>.value = maxResults;
  }

  function setDefaultsForFindBusinessesForms()
  {
    // Initialize the findBusinesses form.
<%
   String findBusinessesStyleIndex = (String)formToolPI.getProperty(UDDIActionInputs.QUERY_STYLE_BUSINESSES);
%>
    document.forms[styleForms[<%=UDDIActionInputs.QUERY_ITEM_BUSINESSES%>]].<%=UDDIActionInputs.QUERY_STYLE_BUSINESSES%>[<%=findBusinessesStyleIndex%>].checked = true;
    var findBusinessesSimpleSection = document.getElementById(findBusinessesSectionIds[<%=UDDIActionInputs.QUERY_STYLE_SIMPLE%>]);
    var findBusinessesSimpleForm = findBusinessesSimpleSection.getElementsByTagName("form").item(0);
    findBusinessesSimpleForm.<%=UDDIActionInputs.QUERY_INPUT_SIMPLE_BUSINESS_NAME%>.value = "<%=HTMLUtils.JSMangle((String)formToolPI.getProperty(UDDIActionInputs.QUERY_INPUT_SIMPLE_BUSINESS_NAME))%>";

    // Advanced form details.
    var findBusinessesAdvancedSection = document.getElementById(findBusinessesSectionIds[<%=UDDIActionInputs.QUERY_STYLE_ADVANCED%>]);
    var findBusinessesAdvancedForm = findBusinessesAdvancedSection.getElementsByTagName("form").item(0);
    var isOwnedChecked = <%=(formToolPI.getProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_BUSINESS_OWNED) != null)%>;
    setAuthenticationSectionDefaults(findBusinessesAdvancedForm,<%=UDDIActionInputs.QUERY_ITEM_BUSINESSES%>,isOwnedChecked);
<%
   Vector busNameVector = (Vector)formToolPI.getProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_BUSINESS_NAMES);
   if (busNameVector != null)
   {
     for (int i=0;i<busNameVector.size();i++)
     {
       Name name = (Name)busNameVector.elementAt(i);
%>
    addLanguageInputRow("findBusinessesAdvancedNames","<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_CONTROL_TITLE_NAME_TEXT_VALUE"))%>");
    setLanguageInputRowSettings("findBusinessesAdvancedNames",<%=i%>,"<%=HTMLUtils.JSMangle(name.getLang())%>","<%=HTMLUtils.JSMangle(name.getText())%>");
<%
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
    addIdentifierRow("findBusinessesAdvancedIdentifiers");
    setIdentifierRowSettings("findBusinessesAdvancedIdentifiers",<%=i%>,"<%=HTMLUtils.JSMangle(kr.getTModelKey())%>","<%=HTMLUtils.JSMangle(kr.getKeyName())%>","<%=HTMLUtils.JSMangle(kr.getKeyValue())%>");
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
    addCategoryRow("findBusinessesAdvancedCategories");
    setCategoryRowSettings("findBusinessesAdvancedCategories",<%=i%>,"<%=HTMLUtils.JSMangle(kr.getTModelKey())%>","<%=HTMLUtils.JSMangle(kr.getKeyName())%>","<%=HTMLUtils.JSMangle(kr.getKeyValue())%>");
<%
     }
   }

   Vector busServiceInterfaces = (Vector)formToolPI.getProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_BUSINESS_SERVICE_INTERFACES);
   Vector busServiceInterfacesCopy = (Vector)formToolPI.getProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_BUSINESS_SERVICE_INTERFACES_COPY);
   if (busServiceInterfaces != null)
   {
     if (busServiceInterfacesCopy == null)
       busServiceInterfacesCopy = new Vector();
     else
       busServiceInterfacesCopy.removeAllElements();
     for (int i=0;i<busServiceInterfaces.size();i++)
     {
       ListElement listElement = (ListElement)busServiceInterfaces.elementAt(i);
       int targetNodeId = listElement.getTargetNodeId();
       int targetToolId = listElement.getTargetToolId();
       int targetViewId = listElement.getTargetViewId();
       String url = SelectSubQueryItemAction.getActionLink(targetNodeId,targetToolId,targetViewId,subQueryKeyProperty.getSubQueryKey(),UDDIActionInputs.QUERY_INPUT_ADVANCED_BUSINESS_SERVICE_INTERFACES,i,UDDIActionInputs.QUERY_ITEM_SERVICE_INTERFACES,false);
       TModel tModel = (TModel)listElement.getObject();
       busServiceInterfacesCopy.addElement(busServiceInterfaces.elementAt(i));
%>
    addResultRow("findBusinessesAdvancedServiceInterfaces",<%=targetNodeId%>,"<%=response.encodeURL(controller.getPathWithContext(url))%>","<%=HTMLUtils.JSMangle(tModel.getNameString())%>","<%=HTMLUtils.JSMangle(tModel.getDefaultDescriptionString())%>");
<%
     }
     formToolPI.setProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_BUSINESS_SERVICE_INTERFACES_COPY,busServiceInterfacesCopy);
   }
   else
   {
     busServiceInterfaces = new Vector();
     busServiceInterfacesCopy = new Vector();
     formToolPI.setProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_BUSINESS_SERVICE_INTERFACES,busServiceInterfaces);
     formToolPI.setProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_BUSINESS_SERVICE_INTERFACES_COPY,busServiceInterfacesCopy);
   }

   DiscoveryURLs busDiscoveryURLs = (DiscoveryURLs)formToolPI.getProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_BUSINESS_DISCOVERYURLS);
   if (busDiscoveryURLs != null)
   {
     Vector discoveryURLVector = busDiscoveryURLs.getDiscoveryURLVector();
     for (int i=0;i<discoveryURLVector.size();i++)
     {
       DiscoveryURL discoveryURL = (DiscoveryURL)discoveryURLVector.elementAt(i);
%>
    addDiscoveryURLRow("findBusinessesAdvancedDiscoveryURLs");
    setDiscoveryURLRowSettings("findBusinessesAdvancedDiscoveryURLs",<%=i%>,"<%=HTMLUtils.JSMangle(discoveryURL.getText())%>");
<%
     }
   }

   FindQualifiers busFindQualifiers = (FindQualifiers)formToolPI.getProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_BUSINESS_FINDQUALIFIERS);
   if (busFindQualifiers != null)
   {
     Vector findQualifierVector = busFindQualifiers.getFindQualifierVector();
     for (int i=0;i<findQualifierVector.size();i++)
     {
       FindQualifier findQualifier = (FindQualifier)findQualifierVector.elementAt(i);
%>
    setFindQualifier("<%=HTMLUtils.JSMangle(findQualifier.getText())%>","findBusinessesAdvancedNames","findBusinessesAdvancedCategories","findBusinessesAdvancedFindQualifiers");
<%
     }
   }
%>
    setAdvancedCommonSectionDefaults(findBusinessesAdvancedForm,"<%=HTMLUtils.JSMangle((String)formToolPI.getProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_BUSINESS_MAX_SEARCH_SET))%>","<%=HTMLUtils.JSMangle((String)formToolPI.getProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_BUSINESS_MAX_RESULTS))%>");

    // UUID form details.
    var findBusinessesUUIDSection = document.getElementById(findBusinessesSectionIds[<%=UDDIActionInputs.QUERY_STYLE_UUID%>]);
    var findBusinessesUUIDForm = findBusinessesUUIDSection.getElementsByTagName("form").item(0);
    findBusinessesUUIDForm.<%=UDDIActionInputs.QUERY_INPUT_UUID_KEY%>.value = "<%=HTMLUtils.JSMangle((String)formToolPI.getProperty(UDDIActionInputs.QUERY_INPUT_UUID_BUSINESS_KEY))%>";

    showFindForm(<%=UDDIActionInputs.QUERY_ITEM_BUSINESSES%>,<%=findBusinessesStyleIndex%>);
  }

  function setDefaultsForFindServicesForms()
  {
    // Initialize the findServices form.
<%
   String findServicesStyleIndex = (String)formToolPI.getProperty(UDDIActionInputs.QUERY_STYLE_SERVICES);
%>
    document.forms[styleForms[<%=UDDIActionInputs.QUERY_ITEM_SERVICES%>]].<%=UDDIActionInputs.QUERY_STYLE_SERVICES%>[<%=findServicesStyleIndex%>].checked = true;
    var findServicesSimpleSection = document.getElementById(findServicesSectionIds[<%=UDDIActionInputs.QUERY_STYLE_SIMPLE%>]);
    var findServicesSimpleForm = findServicesSimpleSection.getElementsByTagName("form").item(0);
    findServicesSimpleForm.<%=UDDIActionInputs.QUERY_INPUT_SIMPLE_SERVICE_NAME%>.value = "<%=HTMLUtils.JSMangle((String)formToolPI.getProperty(UDDIActionInputs.QUERY_INPUT_SIMPLE_SERVICE_NAME))%>";

    // Advanced form details.
    var findServicesAdvancedSection = document.getElementById(findServicesSectionIds[<%=UDDIActionInputs.QUERY_STYLE_ADVANCED%>]);
    var findServicesAdvancedForm = findServicesAdvancedSection.getElementsByTagName("form").item(0);
    var isOwnedChecked = <%=(formToolPI.getProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_OWNED) != null)%>;
    setAuthenticationSectionDefaults(findServicesAdvancedForm,<%=UDDIActionInputs.QUERY_ITEM_SERVICES%>,isOwnedChecked);
<%
   Vector serviceBusiness = (Vector)formToolPI.getProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_BUSINESS);
   Vector serviceBusinessCopy = (Vector)formToolPI.getProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_BUSINESS_COPY);
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
    addResultRow("findServicesAdvancedBusiness",<%=targetNodeId%>,"<%=response.encodeURL(controller.getPathWithContext(url))%>","<%=HTMLUtils.JSMangle(be.getDefaultNameString())%>","<%=HTMLUtils.JSMangle(be.getDefaultDescriptionString())%>");
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

   Vector serviceNameVector = (Vector)formToolPI.getProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_NAMES);
   if (serviceNameVector != null)
   {
     for (int i=0;i<serviceNameVector.size();i++)
     {
       Name name = (Name)serviceNameVector.elementAt(i);
%>
    addLanguageInputRow("findServicesAdvancedNames","<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_CONTROL_TITLE_NAME_TEXT_VALUE"))%>");
    setLanguageInputRowSettings("findServicesAdvancedNames",<%=i%>,"<%=HTMLUtils.JSMangle(name.getLang())%>","<%=HTMLUtils.JSMangle(name.getText())%>");
<%
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
    addCategoryRow("findServicesAdvancedCategories");
    setCategoryRowSettings("findServicesAdvancedCategories",<%=i%>,"<%=HTMLUtils.JSMangle(kr.getTModelKey())%>","<%=HTMLUtils.JSMangle(kr.getKeyName())%>","<%=HTMLUtils.JSMangle(kr.getKeyValue())%>");
<%
     }
   }

   Vector serviceServiceInterfaces = (Vector)formToolPI.getProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_SERVICE_INTERFACES);
   Vector serviceServiceInterfacesCopy = (Vector)formToolPI.getProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_SERVICE_INTERFACES_COPY);
   if (serviceServiceInterfaces != null)
   {
     if (serviceServiceInterfacesCopy == null)
       serviceServiceInterfacesCopy = new Vector();
     else
       serviceServiceInterfacesCopy.removeAllElements();
     for (int i=0;i<serviceServiceInterfaces.size();i++)
     {
       ListElement listElement = (ListElement)serviceServiceInterfaces.elementAt(i);
       int targetNodeId = listElement.getTargetNodeId();
       int targetToolId = listElement.getTargetToolId();
       int targetViewId = listElement.getTargetViewId();
       String url = SelectSubQueryItemAction.getActionLink(targetNodeId,targetToolId,targetViewId,subQueryKeyProperty.getSubQueryKey(),UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_SERVICE_INTERFACES,i,UDDIActionInputs.QUERY_ITEM_SERVICE_INTERFACES,false);
       TModel tModel = (TModel)listElement.getObject();
       serviceServiceInterfacesCopy.addElement(serviceServiceInterfaces.elementAt(i));
%>
    addResultRow("findServicesAdvancedServiceInterfaces",<%=targetNodeId%>,"<%=response.encodeURL(controller.getPathWithContext(url))%>","<%=HTMLUtils.JSMangle(tModel.getNameString())%>","<%=HTMLUtils.JSMangle(tModel.getDefaultDescriptionString())%>");
<%
     }
     formToolPI.setProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_SERVICE_INTERFACES_COPY,serviceServiceInterfacesCopy);
   }
   else
   {
     serviceServiceInterfaces = new Vector();
     serviceServiceInterfacesCopy = new Vector();
     formToolPI.setProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_SERVICE_INTERFACES,serviceServiceInterfaces);
     formToolPI.setProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_SERVICE_INTERFACES_COPY,serviceServiceInterfacesCopy);
   }

   FindQualifiers serviceFindQualifiers = (FindQualifiers)formToolPI.getProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_FINDQUALIFIERS);
   if (serviceFindQualifiers != null)
   {
     Vector findQualifierVector = serviceFindQualifiers.getFindQualifierVector();
     for (int i=0;i<findQualifierVector.size();i++)
     {
       FindQualifier findQualifier = (FindQualifier)findQualifierVector.elementAt(i);
%>
    setFindQualifier("<%=HTMLUtils.JSMangle(findQualifier.getText())%>","findServicesAdvancedNames","findServicesAdvancedCategories","findServicesAdvancedFindQualifiers");
<%
     }
   }
%>
    setAdvancedCommonSectionDefaults(findServicesAdvancedForm,"<%=HTMLUtils.JSMangle((String)formToolPI.getProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_MAX_SEARCH_SET))%>","<%=HTMLUtils.JSMangle((String)formToolPI.getProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_MAX_RESULTS))%>");

    // UUID form details.
    var findServicesUUIDSection = document.getElementById(findServicesSectionIds[<%=UDDIActionInputs.QUERY_STYLE_UUID%>]);
    var findServicesUUIDForm = findServicesUUIDSection.getElementsByTagName("form").item(0);
    findServicesUUIDForm.<%=UDDIActionInputs.QUERY_INPUT_UUID_KEY%>.value = "<%=HTMLUtils.JSMangle((String)formToolPI.getProperty(UDDIActionInputs.QUERY_INPUT_UUID_SERVICE_KEY))%>";

    showFindForm(<%=UDDIActionInputs.QUERY_ITEM_SERVICES%>,<%=findServicesStyleIndex%>);
  }

  function setDefaultsForFindServiceInterfacesForms()
  {
    // Initialize the findServiceInterfaces form.
<%
   String findServiceInterfacesStyleIndex = (String)formToolPI.getProperty(UDDIActionInputs.QUERY_STYLE_SERVICE_INTERFACES);
%>
    document.forms[styleForms[<%=UDDIActionInputs.QUERY_ITEM_SERVICE_INTERFACES%>]].<%=UDDIActionInputs.QUERY_STYLE_SERVICE_INTERFACES%>[<%=findServiceInterfacesStyleIndex%>].checked = true;
    var findServiceInterfacesSimpleSection = document.getElementById(findServiceInterfacesSectionIds[<%=UDDIActionInputs.QUERY_STYLE_SIMPLE%>]);
    var findServiceInterfacesSimpleForm = findServiceInterfacesSimpleSection.getElementsByTagName("form").item(0);
    findServiceInterfacesSimpleForm.<%=UDDIActionInputs.QUERY_INPUT_SIMPLE_SERVICE_INTERFACE_NAME%>.value = "<%=HTMLUtils.JSMangle((String)formToolPI.getProperty(UDDIActionInputs.QUERY_INPUT_SIMPLE_SERVICE_INTERFACE_NAME))%>";

    // Advanced form details.
    var findServiceInterfacesAdvancedSection = document.getElementById(findServiceInterfacesSectionIds[<%=UDDIActionInputs.QUERY_STYLE_ADVANCED%>]);
    var findServiceInterfacesAdvancedForm = findServiceInterfacesAdvancedSection.getElementsByTagName("form").item(0);

    var isOwnedChecked = <%=(formToolPI.getProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_OWNED) != null)%>;
    setAuthenticationSectionDefaults(findServiceInterfacesAdvancedForm,<%=UDDIActionInputs.QUERY_ITEM_SERVICE_INTERFACES%>,isOwnedChecked);
    var isUseServiceChecked = <%=(formToolPI.getProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_USE_SERVICE) != null)%>;
    findServiceInterfacesAdvancedForm.<%=UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_USE_SERVICE%>.checked = isUseServiceChecked;
    toggleFindServiceInterfacesAdvancedServiceParameters(findServiceInterfacesAdvancedForm);
    findServiceInterfacesAdvancedForm.<%=UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_NAME%>.value = "<%=HTMLUtils.JSMangle((String)formToolPI.getProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_NAME))%>";

<%
   Vector siService = (Vector)formToolPI.getProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_SERVICE);
   Vector siServiceCopy = (Vector)formToolPI.getProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_SERVICE_COPY);
   if (siService != null)
   {
     if (siServiceCopy == null)
       siServiceCopy = new Vector();
     else
       siServiceCopy.removeAllElements();
     for (int i=0;i<siService.size();i++)
     {
       ListElement listElement = (ListElement)siService.elementAt(i);
       int targetNodeId = listElement.getTargetNodeId();
       int targetToolId = listElement.getTargetToolId();
       int targetViewId = listElement.getTargetViewId();
       String url = SelectSubQueryItemAction.getActionLink(targetNodeId,targetToolId,targetViewId,subQueryKeyProperty.getSubQueryKey(),UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_SERVICE,i,UDDIActionInputs.QUERY_ITEM_SERVICES,false);
       BusinessService bs = (BusinessService)listElement.getObject();
       siServiceCopy.addElement(siService.elementAt(i));
%>
    addResultRow("findServiceInterfacesAdvancedService",<%=targetNodeId%>,"<%=response.encodeURL(controller.getPathWithContext(url))%>","<%=HTMLUtils.JSMangle(bs.getDefaultNameString())%>","<%=HTMLUtils.JSMangle(bs.getDefaultDescriptionString())%>");
<%
     }
     formToolPI.setProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_SERVICE_COPY,siServiceCopy);
   }
   else
   {
     siService = new Vector();
     siServiceCopy = new Vector();
     formToolPI.setProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_SERVICE,siService);
     formToolPI.setProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_SERVICE_COPY,siServiceCopy);
   }

   IdentifierBag siIdBag = (IdentifierBag)formToolPI.getProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_IDENTIFIERS);
   if (siIdBag != null)
   {
     Vector keyedReferenceVector = siIdBag.getKeyedReferenceVector();
     for (int i=0;i<keyedReferenceVector.size();i++)
     {
       KeyedReference kr = (KeyedReference)keyedReferenceVector.elementAt(i);
%>
    addIdentifierRow("findServiceInterfacesAdvancedIdentifiers");
    setIdentifierRowSettings("findServiceInterfacesAdvancedIdentifiers",<%=i%>,"<%=HTMLUtils.JSMangle(kr.getTModelKey())%>","<%=HTMLUtils.JSMangle(kr.getKeyName())%>","<%=HTMLUtils.JSMangle(kr.getKeyValue())%>");
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
    addCategoryRow("findServiceInterfacesAdvancedCategories");
    setCategoryRowSettings("findServiceInterfacesAdvancedCategories",<%=i%>,"<%=HTMLUtils.JSMangle(kr.getTModelKey())%>","<%=HTMLUtils.JSMangle(kr.getKeyName())%>","<%=HTMLUtils.JSMangle(kr.getKeyValue())%>");
<%
     }
   }

   FindQualifiers siFindQualifiers = (FindQualifiers)formToolPI.getProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_FINDQUALIFIERS);
   if (siFindQualifiers != null)
   {
     Vector findQualifierVector = siFindQualifiers.getFindQualifierVector();
     for (int i=0;i<findQualifierVector.size();i++)
     {
       FindQualifier findQualifier = (FindQualifier)findQualifierVector.elementAt(i);
%>
    setFindQualifier("<%=HTMLUtils.JSMangle(findQualifier.getText())%>","findServiceInterfacesAdvancedName","findServiceInterfacesAdvancedCategories","findServiceInterfacesAdvancedFindQualifiers");
<%
     }
   }
%>
    setAdvancedCommonSectionDefaults(findServiceInterfacesAdvancedForm,"<%=HTMLUtils.JSMangle((String)formToolPI.getProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_MAX_SEARCH_SET))%>","<%=HTMLUtils.JSMangle((String)formToolPI.getProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_MAX_RESULTS))%>");

    // UUID form details.
    var findServiceInterfacesUUIDSection = document.getElementById(findServiceInterfacesSectionIds[<%=UDDIActionInputs.QUERY_STYLE_UUID%>]);
    var findServiceInterfacesUUIDForm = findServiceInterfacesUUIDSection.getElementsByTagName("form").item(0);
    findServiceInterfacesUUIDForm.<%=UDDIActionInputs.QUERY_INPUT_UUID_KEY%>.value = "<%=HTMLUtils.JSMangle((String)formToolPI.getProperty(UDDIActionInputs.QUERY_INPUT_UUID_SERVICE_INTERFACE_KEY))%>";

    showFindForm(<%=UDDIActionInputs.QUERY_ITEM_SERVICE_INTERFACES%>,<%=findServiceInterfacesStyleIndex%>);
  }

  function showMainForm(searchForValueString)
  {
    closeAllUddiChildWindows();
    var queryInfoItemList = document.getElementById("queryInfoItemList");
    var searchForIndex = parseInt(searchForValueString);
    for (var i=0;i<sectionIds.length;i++)
    {
      if (i == searchForIndex)
        document.getElementById(sectionIds[i]).style.display = "";
      else
        document.getElementById(sectionIds[i]).style.display = "none";
    }
<%
   if (subQueryKeyProperty.getSubQueryKey().length() < 1)
   {
%>
    document.getElementById("queryInfoItemList").style.display = "";
<%
   }
%>
    var loadScreenTable = document.getElementById("loadScreen");
    if (loadScreenTable.rows.length > 0)
      loadScreenTable.deleteRow(0);
    document.getElementById("mainScreen").style.display = "";
  }

  function showFindForm(queryItemIndex,style)
  {
    closeAllUddiChildWindows();
    var findSectionIds;
    switch (queryItemIndex)
    {
      case <%=UDDIActionInputs.QUERY_ITEM_BUSINESSES%>:
        findSectionIds = findBusinessesSectionIds;
        break;
      case <%=UDDIActionInputs.QUERY_ITEM_SERVICES%>:
        findSectionIds = findServicesSectionIds;
        break;
      case <%=UDDIActionInputs.QUERY_ITEM_SERVICE_INTERFACES%>:
      default:
        findSectionIds = findServiceInterfacesSectionIds;
    }
    for (var i=0;i<findSectionIds.length;i++)
    {
      if (i == style)
        document.getElementById(findSectionIds[i]).style.display = "";
      else
        document.getElementById(findSectionIds[i]).style.display = "none";
    }
  }

  function toggleAdvancedAuthenticationSection(queryItemIndex,isChecked)
  {
    if (isChecked && <%=!regElement.isLoggedIn()%>)
      document.getElementById(authenticationSectionIds[queryItemIndex]).style.display = "";
    else
      document.getElementById(authenticationSectionIds[queryItemIndex]).style.display = "none";
  }

  function toggleFindServiceInterfacesAdvancedServiceParameters(form)
  {
    var isUseServiceChecked = form.<%=UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_USE_SERVICE%>.checked;
    var isOwnedChecked = form.<%=UDDIActionInputs.QUERY_INPUT_ADVANCED_OWNED%>.checked;
    if (isUseServiceChecked)
    {
      document.getElementById("findServiceInterfacesAdvancedServiceSection").style.display = "";
      document.getElementById("findServiceInterfacesAdvancedOwned").style.display = "none";
      toggleAdvancedAuthenticationSection(<%=UDDIActionInputs.QUERY_ITEM_SERVICE_INTERFACES%>,false);
      document.getElementById("findServiceInterfacesAdvancedNameFindQualifiersSection").style.display = "none";
      document.getElementById("findServiceInterfacesAdvancedFindQualifiersSection").style.display = "none";
    }
    else
    {
      document.getElementById("findServiceInterfacesAdvancedServiceSection").style.display = "none";
      document.getElementById("findServiceInterfacesAdvancedOwned").style.display = "";
      toggleAdvancedAuthenticationSection(<%=UDDIActionInputs.QUERY_ITEM_SERVICE_INTERFACES%>,isOwnedChecked);
      document.getElementById("findServiceInterfacesAdvancedNameFindQualifiersSection").style.display = "";
      document.getElementById("findServiceInterfacesAdvancedFindQualifiersSection").style.display = "";
    }
  }
</script>
</head>
<body class="contentbodymargin" onUnload="closeAllUddiChildWindows()">
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
   String titleImagePath = "uddi/images/find_highlighted.gif";
   String title;
   String subQueryKey = subQueryKeyProperty.getSubQueryKey();
   if (subQueryKey != null && subQueryKey.length() > 0)
   {
     int queryItem = Integer.parseInt((String)formToolPI.getProperty(UDDIActionInputs.QUERY_ITEM));
     switch (queryItem)
     {
       case UDDIActionInputs.QUERY_ITEM_BUSINESSES:
         title = uddiPerspective.getMessage("ALT_SUBQUERY",uddiPerspective.getMessage("FORM_OPTION_BUSINESSES"));
         break;
       case UDDIActionInputs.QUERY_ITEM_SERVICES:
         title = uddiPerspective.getMessage("ALT_SUBQUERY",uddiPerspective.getMessage("FORM_OPTION_SERVICES"));
         break;
       case UDDIActionInputs.QUERY_ITEM_SERVICE_INTERFACES:
       default:
         title = uddiPerspective.getMessage("ALT_SUBQUERY",uddiPerspective.getMessage("FORM_OPTION_SERVICE_INTERFACES"));
     }
   }
   else
     title = uddiPerspective.getMessage("ALT_FIND");
%>
<%@ include file="/uddi/forms/findformheader.inc" %>
      <table width="95%" border=0 cellpadding=3 cellspacing=0>
        <tr>
          <td class="labels" height=20 valign="bottom">
            <label for="queryName"><%=uddiPerspective.getMessage("FORM_LABEL_QUERY_NAME")%></label>
<%
   if (!formToolPI.isInputValid(UDDIActionInputs.QUERY_NAME))
   {
%>
            <%=HTMLUtils.redAsterisk()%>
<%
   }
%>
          </td>
        </tr>
        <tr>
          <td> <input id="queryName" type="text" value="<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("DEFAULT_QUERY_NAME"))%>" class="textenter"> </td>
        </tr>
      </table>
      <div id="queryInfoItemList" style="display:none;">
        <table width="95%" border=0 cellpadding=3 cellspacing=0>
          <tr>
            <td class="labels" valign="middle" nowrap height=40>
              <label for="searchFor"><%=uddiPerspective.getMessage("FORM_LABEL_SEARCH_FOR")%></label>
            </td>
            <td valign="middle" nowrap height=40>
              <select id="searchFor" onChange="showMainForm(this.value)" class="selectlist">
                <option value="<%=UDDIActionInputs.QUERY_ITEM_BUSINESSES%>"><%=uddiPerspective.getMessage("FORM_OPTION_BUSINESSES")%>
                <option value="<%=UDDIActionInputs.QUERY_ITEM_SERVICES%>"><%=uddiPerspective.getMessage("FORM_OPTION_SERVICES")%>
                <option value="<%=UDDIActionInputs.QUERY_ITEM_SERVICE_INTERFACES%>"><%=uddiPerspective.getMessage("FORM_OPTION_SERVICE_INTERFACES")%>
              </select>
            </td>
            <td width="90%" height=40>&nbsp;</td>
          </tr>
        </table>
      </div>
    </div>
<jsp:include page="/uddi/forms/RegFindBusinessesForm.jsp" flush="true"/>
<jsp:include page="/uddi/forms/RegFindServicesForm.jsp" flush="true"/>
<jsp:include page="/uddi/forms/RegFindServiceInterfacesForm.jsp" flush="true"/>
  </div>
<script language="javascript">
  setDefaults();
</script>
</body>
</html>
