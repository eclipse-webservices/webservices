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
                                                        org.eclipse.wst.ws.internal.explorer.platform.constants.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.perspective.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.datamodel.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.util.*,
                                                        org.uddi4j.response.CompletionStatus,
                                                        org.uddi4j.datatype.business.*,
                                                        java.util.*" %>

<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<jsp:useBean id="sectionHeaderInfo" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.SectionHeaderInfo" scope="request"/>
<%
   String pubAssertionForm = "showPublisherAssertionsForm";
   String publisherAssertionsBusiness = "publisherAssertionsBusiness";

   UDDIPerspective uddiPerspective = controller.getUDDIPerspective();
   NodeManager navigatorManager = uddiPerspective.getNavigatorManager();
   UDDIMainNode uddiMainNode = (UDDIMainNode)navigatorManager.getRootNode();
   Node selectedNode = navigatorManager.getSelectedNode();
   FormTool formTool = (FormTool)(selectedNode.getCurrentToolManager().getSelectedTool());   
   RegistryNode regNode = uddiMainNode.getRegistryNode(selectedNode);
   RegistryElement regElement = (RegistryElement)regNode.getTreeElement();
   TreeElement selectedElement = selectedNode.getTreeElement();

   String divPublisherAssertionTable = "divPublisherAssertionTable";
   String xdivPublisherAssertionTable = "xdivPublisherAssertionTable";
%>

<jsp:useBean id="subQueryKeyProperty" class="org.eclipse.wst.ws.internal.explorer.platform.uddi.perspective.SubQueryKeyProperty" scope="request">
<%
   String subQueryKey = (String)formTool.getProperty(UDDIActionInputs.SUBQUERY_KEY);
   subQueryKeyProperty.setSubQueryKey(subQueryKey);
%>
</jsp:useBean>
<%
   FormToolPropertiesInterface formToolPI = ((MultipleFormToolPropertiesInterface)formTool).getFormToolProperties(subQueryKeyProperty.getSubQueryKey());
%>

<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title><%=uddiPerspective.getMessage("FORM_TITLE_MANAGE_PUBLISHER_ASSERTIONS")%></title>
  <link rel="stylesheet" type="text/css" href="<%=response.encodeURL(controller.getPathWithContext("css/windows.css"))%>">

<script language="javascript" src="<%=response.encodeURL(controller.getPathWithContext("scripts/browserdetect.js"))%>">
</script>

<script language="javascript" src="<%=response.encodeURL(controller.getPathWithContext("scripts/resumeproxyloadpage.js"))%>">
</script>

<jsp:include page="/scripts/formsubmit.jsp" flush="true"/>
<jsp:include page="/uddi/scripts/udditables.jsp" flush="true"/>
<jsp:include page="/uddi/scripts/uddipanes.jsp" flush="true"/>
<jsp:include page="/uddi/scripts/results.jsp" flush="true"/>
<script language="javascript">
  function setDefaults()
  {
    closeAllUddiChildWindows();
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
    document.getElementById("<%=pubAssertionForm%>").style.display = "";
<%
   }
   
   Vector business = (Vector)formToolPI.getProperty(UDDIActionInputs.QUERY_INPUT_ADD_PUBLISHER_ASSERTIONS);
   ListManager businessCopy = (ListManager)formToolPI.getProperty(UDDIActionInputs.QUERY_INPUT_ADD_PUBLISHER_ASSERTIONS_COPY);
   if (business != null)
   {
     if (businessCopy == null)
       businessCopy = new ListManager();
     else
       businessCopy.clear();
     for (int i=0;i<business.size();i++)
     {
       ListElement listElement = (ListElement)business.elementAt(i);
       int targetNodeId = listElement.getTargetNodeId();
       int targetToolId = listElement.getTargetToolId();
       int targetViewId = listElement.getTargetViewId();
       String url = SelectSubQueryItemAction.getActionLink(targetNodeId,targetToolId,targetViewId,subQueryKeyProperty.getSubQueryKey(),UDDIActionInputs.QUERY_INPUT_ADD_PUBLISHER_ASSERTIONS,i,UDDIActionInputs.QUERY_ITEM_BUSINESSES,false);
       BusinessEntity be = (BusinessEntity)listElement.getObject();
       businessCopy.add(listElement);
%>
    addPublisherAssertionResultRow("<%=publisherAssertionsBusiness%>",<%=listElement.getViewId()%>,"<%=response.encodeURL(controller.getPathWithContext(url))%>","<%=HTMLUtils.JSMangle(be.getDefaultNameString())%>","<%=HTMLUtils.JSMangle(be.getDefaultDescriptionString())%>");
<%
     }
     formToolPI.setProperty(UDDIActionInputs.QUERY_INPUT_ADD_PUBLISHER_ASSERTIONS_COPY,businessCopy);
   }
   else
   {
     business = new Vector();
     businessCopy = new ListManager();
     formToolPI.setProperty(UDDIActionInputs.QUERY_INPUT_ADD_PUBLISHER_ASSERTIONS,business);
     formToolPI.setProperty(UDDIActionInputs.QUERY_INPUT_ADD_PUBLISHER_ASSERTIONS_COPY,businessCopy);
   }
%>
  }

  function populateAssertionTable() {
<%
    if (selectedElement instanceof BusinessElement)
    {
      ListManager pubAssertions = ((BusinessElement)selectedElement).getPublisherAssertions();
      if (pubAssertions == null)
      {
        ShowPublisherAssertionsAction action = new ShowPublisherAssertionsAction(controller);
        action.run();
%>
        statusContent.location = "<%=response.encodeURL(controller.getPathWithContext("uddi/status_content.jsp"))%>";
<%
        pubAssertions = ((BusinessElement)selectedElement).getPublisherAssertions();
      }

      pubAssertions = ((BusinessElement)selectedElement).getPublisherAssertions();
      Enumeration e = ((pubAssertions != null) ? pubAssertions.getListElements() : null);
      while (e != null && e.hasMoreElements())
      {
        ListElement le = (ListElement)e.nextElement();
        PublisherAssertionElement pubAssertionElement = (PublisherAssertionElement)le.getObject();
        int viewId = le.getViewId();
        String direction;
        if (((BusinessElement)selectedElement).getBusinessEntity().getBusinessKey().equals(pubAssertionElement.getFromKey()))
          direction = uddiPerspective.getMessage("FORM_OPTION_ASSERTION_DIRECTION_FROM");
        else
          direction = uddiPerspective.getMessage("FORM_OPTION_ASSERTION_DIRECTION_TO");
        ListElement sp = (ListElement)pubAssertionElement.getServiceProvider();
        int targetNodeId = sp.getTargetNodeId();
        int targetToolId = sp.getTargetToolId();
        int targetViewId = sp.getTargetViewId();
        int subQueryItemId = pubAssertionElement.getSubQueryItemId();
        String url = SelectSubQueryItemAction.getActionLink(targetNodeId,targetToolId,targetViewId,subQueryKeyProperty.getSubQueryKey(),UDDIActionInputs.QUERY_INPUT_EXISTING_PUBLISHER_ASSERTIONS,subQueryItemId,UDDIActionInputs.QUERY_ITEM_BUSINESSES,false);
        String name = ((BusinessEntity)sp.getObject()).getDefaultNameString();
        String desc = ((BusinessEntity)sp.getObject()).getDefaultDescriptionString();
        String status = pubAssertionElement.getStatus();
        String type = pubAssertionElement.getKeyedRef().getKeyValue();
%>
      addAssertion("<%=divPublisherAssertionTable%>",<%=viewId%>,"<%=direction%>","<%=response.encodeURL(controller.getPathWithContext(url))%>","<%=HTMLUtils.JSMangle(name)%>","<%=HTMLUtils.JSMangle(desc)%>","<%=status%>","<%=HTMLUtils.JSMangle(type)%>");
<%
      }
    }
%>
  }

  function addAssertion(tableContainerId,viewId,direction,url,name,desc,status,type)
  {
    twistOpen(tableContainerId);
    var table = getTable(tableContainerId);
    var tableBody = table.getElementsByTagName("TBODY").item(0);
    var newRow = document.createElement("tr");
    var column0 = document.createElement("td");
    var column1 = document.createElement("td");
    var column2 = document.createElement("td");
    var column3 = document.createElement("td");
    var column4 = document.createElement("td");
    var column5 = document.createElement("td");
    var column6 = document.createElement("td");

    var rowCheckbox = createRowCheckbox();
    rowCheckbox.name = "<%=UDDIActionInputs.PUBLISHER_ASSERTIONS_VIEWID%>";
    rowCheckbox.value = viewId;
    column0.appendChild(rowCheckbox);

    column1.appendChild(document.createTextNode("<%=uddiPerspective.getMessage("FORM_LABEL_THIS_BUSINESS")%>"));

    column2.appendChild(document.createTextNode(direction));

    var nameDetailsLink = document.createElement("a");
    nameDetailsLink.href = url;
    nameDetailsLink.target = "<%=FrameNames.PERSPECTIVE_WORKAREA%>";
    nameDetailsLink.appendChild(document.createTextNode(name));
    column3.appendChild(nameDetailsLink);

    column4.appendChild(document.createTextNode(getDefaultDisplayString(desc)));

    column5.appendChild(document.createTextNode(status));

    column6.appendChild(document.createTextNode(type));

    column0.className = "checkboxcells";
    column1.className = "tablecells";
    column2.className = "tablecells";
    column3.className = "tablecells";
    column4.className = "tablecells";
    column5.className = "tablecells";
    column6.className = "tablecells";
    newRow.appendChild(column0);
    newRow.appendChild(column1);
    newRow.appendChild(column2);
    newRow.appendChild(column3);
    newRow.appendChild(column4);
    newRow.appendChild(column5);
    newRow.appendChild(column6);
    tableBody.appendChild(newRow);

  }

  function addPublisherAssertionResultRow(tableContainerId,nodeId,url,name,description)
  {
    twistOpen(tableContainerId);
    var table = getTable(tableContainerId);
    var tableBody = table.getElementsByTagName("TBODY").item(0);
    var newRow = document.createElement("tr");
    var column0 = document.createElement("td");
    var column1 = document.createElement("td");
    var column2 = document.createElement("td");
    var column3 = document.createElement("td");
    var column4 = document.createElement("td");
    var column5 = document.createElement("td");

    var rowCheckbox = createRowCheckbox();
    column0.appendChild(rowCheckbox);
    column0.appendChild(createHiddenElement("",nodeId));

    column1.appendChild(document.createTextNode("<%=uddiPerspective.getMessage("FORM_LABEL_THIS_BUSINESS")%>"));

    var direction = document.createElement("select");
    direction.id = "label_publisher_assertion_direction";
    direction.name = "<%=UDDIActionInputs.PUBLISHER_ASSERTIONS_DIRECTION%>";
    direction.className = "selectlist";
    direction.options[0] = new Option("<%=uddiPerspective.getMessage("FORM_OPTION_ASSERTION_DIRECTION_FROM")%>", "<%=String.valueOf(UDDIActionInputs.DIRECTION_FROM)%>");
    direction.options[1] = new Option("<%=uddiPerspective.getMessage("FORM_OPTION_ASSERTION_DIRECTION_TO")%>", "<%=String.valueOf(UDDIActionInputs.DIRECTION_TO)%>");
    column2.appendChild(direction);

    var nameDetailsLink = document.createElement("a");
    nameDetailsLink.href = url;
    nameDetailsLink.target = "<%=FrameNames.PERSPECTIVE_WORKAREA%>";
    nameDetailsLink.appendChild(document.createTextNode(name));
    column3.appendChild(nameDetailsLink);

    column4.appendChild(document.createTextNode(getDefaultDisplayString(description)));

    var assertionType = document.createElement("select");
    assertionType.id = "label_publisher_assertion_type";
    assertionType.name = "<%=UDDIActionInputs.PUBLISHER_ASSERTIONS_TYPE%>";
    assertionType.className = "selectlist";
    assertionType.options[0] = new Option("<%=uddiPerspective.getMessage("FORM_OPTION_ASSERTION_TYPE_PARENT_CHILD")%>", "<%=String.valueOf(UDDIActionInputs.PUBLISHER_ASSERTIONS_TYPE_PARENT_CHILD)%>");
    assertionType.options[1] = new Option("<%=uddiPerspective.getMessage("FORM_OPTION_ASSERTION_TYPE_PEER_TO_PEER")%>", "<%=String.valueOf(UDDIActionInputs.PUBLISHER_ASSERTIONS_TYPE_PEER_TO_PEER)%>");
    assertionType.options[2] = new Option("<%=uddiPerspective.getMessage("FORM_OPTION_ASSERTION_TYPE_IDENTITY")%>", "<%=String.valueOf(UDDIActionInputs.PUBLISHER_ASSERTIONS_TYPE_IDENTITY)%>");
    column5.appendChild(assertionType);

    column0.className = "checkboxcells";
    column1.className = "tablecells";
    column2.className = "tablecells";
    column3.className = "tablecells";
    column4.className = "tablecells";
    column5.className = "tablecells";
    newRow.appendChild(column0);
    newRow.appendChild(column1);
    newRow.appendChild(column2);
    newRow.appendChild(column3);
    newRow.appendChild(column4);
    newRow.appendChild(column5);
    tableBody.appendChild(newRow);
  }

  function setFormLocationAndSubmit(form,location)
  {
    form.action = location;
    if (handleSubmit(form))
    {
      processResultTable("<%=publisherAssertionsBusiness%>", "<%=UDDIActionInputs.PUBLISHER_ASSERTIONS_SELECTED_BUS_ID%>", form, false);
      form.submit();
    }
  }

  function checkTableEntriesAndSubmit(tableContainerId,form,location)
  {
    if (getNumberOfSelections(tableContainerId) > 0) {
      setFormLocationAndSubmit(form,location);
      return;
    }
    alert("<%=controller.getUDDIPerspective().getMessage("MSG_ERROR_NOTHING_SELECTED")%>");
  }

  function checkTableSizeAndSubmit(tableContainerId,form,location)
  {
    var table = getTable(tableContainerId);
    if (table.rows.length > numberOfHeaderRows) {
      setFormLocationAndSubmit(form,location);
      return;
    }
    alert("<%=controller.getUDDIPerspective().getMessage("MSG_ERROR_NOTHING_SELECTED")%>");
  }

</script>
</head>
<body dir="<%=org.eclipse.wst.ws.internal.explorer.platform.util.DirUtils.getDir()%>" class="contentbodymargin" onUnload="closeAllUddiChildWindows()">
  <div id="contentborder">
    <div id="publisherAssertions">
      <form action="<%=response.encodeURL(controller.getPathWithContext("uddi/actions/ShowPublisherAssertionsActionJSP.jsp"))%>" method="post" target="<%=FrameNames.PERSPECTIVE_WORKAREA%>" enctype="multipart/form-data">
        <input type="hidden" name="<%=UDDIActionInputs.SUBQUERY_GET%>">
        <input type="hidden" name="<%=UDDIActionInputs.NEW_SUBQUERY_INITIATED%>">
        <input type="hidden" name="<%=UDDIActionInputs.NEW_SUBQUERY_QUERY_ITEM%>">

<%
   String titleImagePath = "uddi/images/publisher_assertions_highlighted.gif";
   String title = uddiPerspective.getMessage("ALT_MANAGE_PUBLISHER_ASSERTIONS");
%>
<%@ include file="/forms/formheader.inc" %>
        <table width="90%" border=0 cellpadding=3 cellspacing=0>
          <tr>
            <td class="labels">
              <%=uddiPerspective.getMessage("FORM_LABEL_MANAGE_PUBLISHER_ASSERTIONS_DESC")%>
            </td>
          </tr>
        </table>
        <table border=0 cellpadding=6 cellspacing=0>
          <tr>
            <td height=40 valign="bottom" align="left" nowrap width=11>
                <a href="javascript:twist('<%=divPublisherAssertionTable%>','<%=xdivPublisherAssertionTable%>')"><img name="<%=xdivPublisherAssertionTable%>" src="<%=response.encodeURL(controller.getPathWithContext("images/twistclosed.gif"))%>" alt="<%=controller.getMessage("ALT_TWIST_CLOSED")%>" class="twist"></a>
            </td>
            <td height=40 valign="bottom" align="left" nowrap class="labels">
              <strong><%=uddiPerspective.getMessage("FORM_LABEL_PUBLISH_ASSERTIONS")%></strong>
            </td>
          </tr>
        </table>

        <table width="95%" border=0 cellpadding=0 cellspacing=0>
          <tr>
            <td valign="top" height=10><img src="<%=response.encodeURL(controller.getPathWithContext("images/keyline.gif"))%>" height=2 width="100%"></td>
          </tr>
        </table>

        <div id="<%=divPublisherAssertionTable%>" style="display:none;">
          <table width="95%" cellpadding=3 cellspacing=0 class="tableborder">
            <tr>
              <th class="checkboxcells" width=10><input type="checkbox" onClick="handleCheckAllClick('<%=divPublisherAssertionTable%>',this)" title="<%=controller.getMessage("FORM_CONTROL_TITLE_SELECT_ALL_CHECK_BOX")%>"></th>
              <th class="headercolor"><%=((BusinessElement)selectedElement).getBusinessEntity().getDefaultNameString()%></th>
              <th class="headercolor"><label for="label_publisher_assertion_direction"><%=uddiPerspective.getMessage("FORM_LABEL_DIRECTION")%></label></th>
              <th class="headercolor"><%=uddiPerspective.getMessage("FORM_LABEL_BUSINESS")%></th>
              <th class="headercolor"><%=uddiPerspective.getMessage("FORM_LABEL_DESCRIPTION")%></th>
              <th class="headercolor"><%=uddiPerspective.getMessage("FORM_LABEL_STATUS")%></th>
              <th class="headercolor"><label for="label_publisher_assertion_type"><%=uddiPerspective.getMessage("FORM_LABEL_ASSERTION_TYPE")%></label></th>
            </tr>
          </table>
          <table width="90%" border=0 cellpadding=3 cellspacing=0>
            <tr>
              <td height=30 valign="bottom" align="left" nowrap class="labels">
                <%=uddiPerspective.getMessage("FORM_LABEL_INCOMPLETE_ASSERTIONS_VISIBILITY_DESC")%>
              </td>
            </tr>
          </table>
        </div>

<%
   boolean tableHasErrors = !formToolPI.isInputValid(UDDIActionInputs.QUERY_INPUT_ADD_PUBLISHER_ASSERTIONS);
   String[] busSpecificInfo = {"FORM_LABEL_BUSINESSES",(new Boolean(tableHasErrors)).toString(),String.valueOf(UDDIActionInputs.QUERY_ITEM_BUSINESSES),"publisherAssertions",UDDIActionInputs.QUERY_INPUT_ADD_PUBLISHER_ASSERTIONS};
   sectionHeaderInfo.clear();
   sectionHeaderInfo.setContainerId(publisherAssertionsBusiness);
   sectionHeaderInfo.setOtherProperties(busSpecificInfo);
%>
<jsp:include page="/uddi/forms/uddiObjectsPublisherAssertions_table.jsp" flush="true"/>

<%
   sectionHeaderInfo.clear();
   sectionHeaderInfo.setContainerId(pubAssertionForm);
%>
<jsp:include page="authentication_table.jsp" flush="true"/>
        <br>
        <table>
          <tr>
            <td>
              <input type="button" value="<%=uddiPerspective.getMessage("FORM_BUTTON_SHOW_ALL_PUBLISHER_ASSERTIONS")%>" onClick="setFormLocationAndSubmit(this.form,'<%=response.encodeURL(controller.getPathWithContext("uddi/actions/ShowPublisherAssertionsActionJSP.jsp"))%>')" class="button">
            </td>
            <td>
              <input type="button" value="<%=uddiPerspective.getMessage("FORM_BUTTON_COMPLETE_PUBLISHER_ASSERTIONS")%>" onClick="checkTableEntriesAndSubmit('<%=divPublisherAssertionTable%>',this.form,'<%=response.encodeURL(controller.getPathWithContext("uddi/actions/CompletePublisherAssertionsActionJSP.jsp"))%>')" class="button">
            </td>
            <td>
              <input type="button" value="<%=uddiPerspective.getMessage("FORM_BUTTON_ADD_PUBLISHER_ASSERTIONS")%>" onClick="checkTableSizeAndSubmit('<%=publisherAssertionsBusiness%>',this.form,'<%=response.encodeURL(controller.getPathWithContext("uddi/actions/AddPublisherAssertionsActionJSP.jsp"))%>')" class="button">
            </td>
            <td>
              <input type="button" value="<%=uddiPerspective.getMessage("FORM_BUTTON_REMOVE_PUBLISHER_ASSERTIONS")%>" onClick="checkTableEntriesAndSubmit('<%=divPublisherAssertionTable%>',this.form,'<%=response.encodeURL(controller.getPathWithContext("uddi/actions/RemovePublisherAssertionsActionJSP.jsp"))%>')" class="button">
            </td>
          </tr>
        </table>
      </form>

    </div>
  </div>
<script language="javascript">
  populateAssertionTable();
  setDefaults();
  resumeProxyLoadPage();
</script>
</body>
</html>
