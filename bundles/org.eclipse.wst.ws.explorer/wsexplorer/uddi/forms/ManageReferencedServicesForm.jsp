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
                                                        org.eclipse.wst.ws.internal.explorer.platform.uddi.datamodel.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.uddi.constants.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.uddi.actions.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.perspective.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.constants.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.datamodel.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.util.*,
                                                        org.uddi4j.datatype.business.*,
                                                        org.uddi4j.datatype.service.*,
                                                        java.util.*" %>

<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<jsp:useBean id="sectionHeaderInfo" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.SectionHeaderInfo" scope="request"/>
<%
   UDDIPerspective uddiPerspective = controller.getUDDIPerspective();
   NodeManager navigatorManager = uddiPerspective.getNavigatorManager();
   UDDIMainNode uddiMainNode = (UDDIMainNode)navigatorManager.getRootNode();
   Node selectedNode = navigatorManager.getSelectedNode();
   FormTool formTool = (FormTool)(selectedNode.getCurrentToolManager().getSelectedTool());
   RegistryNode regNode = uddiMainNode.getRegistryNode(selectedNode);
   RegistryElement regElement = (RegistryElement)regNode.getTreeElement();
   BusinessElement busElement = (BusinessElement)selectedNode.getTreeElement();
%>
<jsp:useBean id="subQueryKeyProperty" class="org.eclipse.wst.ws.internal.explorer.platform.uddi.perspective.SubQueryKeyProperty" scope="request">
<%
   String sk = (String)formTool.getProperty(UDDIActionInputs.SUBQUERY_KEY);
   subQueryKeyProperty.setSubQueryKey(sk);
%>
</jsp:useBean>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 3.2 Final//EN">
<html lang="<%=response.getLocale().getLanguage()%>">
<head>
  <title><%=uddiPerspective.getMessage("FORM_TITLE_MANAGE_REFERENCED_SERVICES")%></title>
  <link rel="stylesheet" type="text/css" href="<%=response.encodeURL(controller.getPathWithContext("css/windows.css"))%>">
<script language="javascript" src="<%=response.encodeURL(controller.getPathWithContext("scripts/browserdetect.js"))%>">
</script>
<jsp:include page="/uddi/scripts/results.jsp" flush="true"/>
<script language="javascript">
  function setDefaults()
  {
<%
   if (!regElement.isLoggedIn())
   {
     String publishURL = (String)formTool.getProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_PUBLISH_URL);
     String userId = (String)formTool.getProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_USERID);
     String password = (String)formTool.getProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_PASSWORD);
%>
    document.forms[0].<%=UDDIActionInputs.QUERY_INPUT_ADVANCED_PUBLISH_URL%>.value = "<%=HTMLUtils.JSMangle(publishURL)%>";
    document.forms[0].<%=UDDIActionInputs.QUERY_INPUT_ADVANCED_USERID%>.value = "<%=HTMLUtils.JSMangle(userId)%>";
    document.forms[0].<%=UDDIActionInputs.QUERY_INPUT_ADVANCED_PASSWORD%>.value = "<%=HTMLUtils.JSMangle(password)%>";
    document.getElementById("manageReferencedServicesAuthentication").style.display = "";
<%
   }
   String subQueryKey = subQueryKeyProperty.getSubQueryKey();
   FormToolPropertiesInterface formToolPI = ((MultipleFormToolPropertiesInterface)formTool).getFormToolProperties(subQueryKey);
   Vector referencedServices = (Vector)formToolPI.getProperty(UDDIActionInputs.QUERY_INPUT_REFERENCED_SERVICES);
   if (referencedServices == null)
   {
     referencedServices = new Vector();
     BusinessEntity businessEntity = busElement.getBusinessEntity();
     String businessKey = businessEntity.getBusinessKey();
     BusinessServices businessSvcs = businessEntity.getBusinessServices();
     if (businessSvcs != null)
     {
       Vector businessSvcVector = businessSvcs.getBusinessServiceVector();
       for (int i=0;i<businessSvcVector.size();i++)
       {
         BusinessService busService = (BusinessService)businessSvcVector.elementAt(i);
         if (!busService.getBusinessKey().equals(businessKey))
         {
           ListElement listElement = new ListElement(busService);
           referencedServices.addElement(listElement);
         }
       }
     }
     formToolPI.setProperty(UDDIActionInputs.QUERY_INPUT_REFERENCED_SERVICES,referencedServices);
   }

   for (int i=0;i<referencedServices.size();i++)
   {
     ListElement listElement = (ListElement)referencedServices.elementAt(i);
     int targetNodeId = listElement.getTargetNodeId();
     int targetToolId = listElement.getTargetToolId();
     int targetViewId = listElement.getTargetViewId();
     String url = SelectSubQueryItemAction.getActionLink(targetNodeId,targetToolId,targetViewId,subQueryKey,UDDIActionInputs.QUERY_INPUT_REFERENCED_SERVICES,i,UDDIActionInputs.QUERY_ITEM_SERVICES,false);
     BusinessService bs = (BusinessService)listElement.getObject();
     String name = bs.getDefaultNameString();
     String description = bs.getDefaultDescriptionString();
%>
    addResultRow("manageReferencedServicesReferences",<%=targetNodeId%>,"<%=response.encodeURL(controller.getPathWithContext(url))%>","<%=HTMLUtils.JSMangle(name)%>","<%=HTMLUtils.JSMangle(description)%>");
<%
   }

   Vector services = (Vector)formToolPI.getProperty(UDDIActionInputs.QUERY_INPUT_SERVICES);
   if (services != null)
   {
     for (int i=0;i<services.size();i++)
     {
       ListElement listElement = (ListElement)services.elementAt(i);
       int targetNodeId = listElement.getTargetNodeId();
       int targetToolId = listElement.getTargetToolId();
       int targetViewId = listElement.getTargetViewId();
       String url = SelectSubQueryItemAction.getActionLink(targetNodeId,targetToolId,targetViewId,subQueryKey,UDDIActionInputs.QUERY_INPUT_SERVICES,i,UDDIActionInputs.QUERY_ITEM_SERVICES,false);
       BusinessService bs = (BusinessService)listElement.getObject();
       String name = bs.getDefaultNameString();
       String description = bs.getDefaultDescriptionString();
%>
    addResultRow("manageReferencedServicesServices",<%=targetNodeId%>,"<%=response.encodeURL(controller.getPathWithContext(url))%>","<%=HTMLUtils.JSMangle(name)%>","<%=HTMLUtils.JSMangle(description)%>");
<%
     }
   }
%>
  }

  function processReferenceTable(tableContainerId,inputName,form)
  {
    var table = getTable(tableContainerId);
    for (var i=numberOfHeaderRows;i<table.rows.length;i++)
    {
      var inputs = table.rows[i].getElementsByTagName("input");
      form.appendChild(createHiddenElement(inputName,inputs.item(0).checked));
    }
  }

  function processForm(form)
  {
    if (handleSubmit(form))
    {
      processReferenceTable("manageReferencedServicesReferences","<%=UDDIActionInputs.REFERENCED_SERVICE_SELECT_STATE%>",form);
      processResultTable("manageReferencedServicesServices","<%=UDDIActionInputs.NODEID_SERVICE%>",form,false);
      form.submit();
    }
  }

  function processAddServices(form)
  {
    var table = getTable("manageReferencedServicesServices");
    if (table.rows.length == numberOfHeaderRows)
      alert("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("MSG_ERROR_NO_SERVICES"))%>");
    else
    {
      form.<%=UDDIActionInputs.MANAGE_REFERENCED_SERVICES_OPERATION%>.value = "<%=UDDIActionInputs.MANAGE_REFERENCED_SERVICES_OPERATION_ADD%>";
      processForm(form);
    }
  }

  function processRemoveReferences(form)
  {
    var numberOfSelections = getNumberOfSelections("manageReferencedServicesReferences");
    if (getNumberOfSelections("manageReferencedServicesReferences") == 0)
      alert("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("MSG_ERROR_NO_REFERENCE_SELECTED"))%>");
    else
    {
      form.<%=UDDIActionInputs.MANAGE_REFERENCED_SERVICES_OPERATION%>.value = "<%=UDDIActionInputs.MANAGE_REFERENCED_SERVICES_OPERATION_REMOVE%>";
      processForm(form);
    }
  }
</script>
</head>
<body dir="<%=org.eclipse.wst.ws.internal.explorer.platform.util.DirUtils.getDir()%>" class="contentbodymargin">
  <div id="contentborder">
    <div id="manageReferencedServices">
<%
   String titleImagePath = "uddi/images/referenced_services_highlighted.gif";
   String title = uddiPerspective.getMessage("ALT_MANAGE_REFERENCED_SERVICES");
%>
<%@ include file="/forms/formheader.inc" %>
      <form action="<%=response.encodeURL(controller.getPathWithContext("uddi/actions/ManageReferencedServicesActionJSP.jsp"))%>" method="post" target="<%=FrameNames.PERSPECTIVE_WORKAREA%>" enctype="multipart/form-data" style="margin-top:0;">
        <input type="hidden" name="<%=UDDIActionInputs.SUBQUERY_KEY%>">
        <input type="hidden" name="<%=UDDIActionInputs.SUBQUERY_GET%>">
        <input type="hidden" name="<%=UDDIActionInputs.NEW_SUBQUERY_INITIATED%>">
        <input type="hidden" name="<%=UDDIActionInputs.NEW_SUBQUERY_QUERY_ITEM%>">
        <input type="hidden" name="<%=UDDIActionInputs.MANAGE_REFERENCED_SERVICES_OPERATION%>">
        <table width="90%" border=0 cellpadding=3 cellspacing=0>
          <tr>
            <td class="labels">
              <%=uddiPerspective.getMessage("FORM_LABEL_MANAGE_REFERENCED_SERVICES_DESC")%>
            </td>
          </tr>
        </table>
<%
   String[] projectionsSpecificInfo = {"FORM_LABEL_REFERENCED_SERVICES",Boolean.FALSE.toString(),String.valueOf(UDDIActionInputs.QUERY_ITEM_SERVICES),"manageReferencedServices",UDDIActionInputs.QUERY_INPUT_REFERENCED_SERVICES};
   sectionHeaderInfo.clear();
   sectionHeaderInfo.setContainerId("manageReferencedServicesReferences");
   sectionHeaderInfo.setOtherProperties(projectionsSpecificInfo);
   sectionHeaderInfo.enableDynamic(false);
%>
<jsp:include page="/uddi/forms/uddiObjects_table.jsp" flush="true"/>
<%
   String[] servicesSpecificInfo = {"FORM_OPTION_SERVICES",Boolean.FALSE.toString(),String.valueOf(UDDIActionInputs.QUERY_ITEM_SERVICES),"manageReferencedServices",UDDIActionInputs.QUERY_INPUT_SERVICES};
   sectionHeaderInfo.clear();
   sectionHeaderInfo.setContainerId("manageReferencedServicesServices");
   sectionHeaderInfo.setOtherProperties(servicesSpecificInfo);
%>
<jsp:include page="/uddi/forms/uddiObjects_table.jsp" flush="true"/>
<%
   sectionHeaderInfo.clear();
   sectionHeaderInfo.setContainerId("manageReferencedServicesAuthentication");
%>
<jsp:include page="/uddi/forms/authentication_table.jsp" flush="true"/>
        <table border=0 cellpadding=2 cellspacing=0>
          <tr>
            <td height=30 valign="bottom" align="left" nowrap>
              <input type="button" value="<%=uddiPerspective.getMessage("FORM_BUTTON_ADD_SERVICES")%>" onClick="processAddServices(this.form)" class="button">
            </td>
            <td height=30 valign="bottom" align="left" nowrap>
              <input type="button" value="<%=uddiPerspective.getMessage("FORM_BUTTON_REMOVE_REFERENCES")%>" onClick="processRemoveReferences(this.form)" class="button">
            </td>
            <td nowrap width="90%">&nbsp;</td>
          </tr>
        </table>
      </form>
    </div>
  </div>
<script language="javascript">
  setDefaults();
</script>
</body>
</html>
