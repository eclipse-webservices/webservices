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
                                                        org.eclipse.wst.ws.internal.explorer.platform.constants.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.util.*" %>

<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<jsp:include page="/uddi/scripts/udditables.jsp" flush="true"/>
<%
   UDDIPerspective uddiPerspective = controller.getUDDIPerspective();
%>   
<script language="javascript">
  function addDetailsLanguageInputRow(tableContainerId,textControlTitle,hasError)
  {
    twistOpen(tableContainerId);
    var table = getTable(tableContainerId);
    var tableBody = table.getElementsByTagName("TBODY").item(0);
    var newRow = document.createElement("tr");
    var column0 = document.createElement("td");
    var column1 = document.createElement("td");
    var column2 = document.createElement("td");
    var column3 = document.createElement("td");

    column0.className = "checkboxcells";
    column1.className = "tablecells";
    column2.className = "tablecells";
    column2.width = "70%";
    column3.className = "tablecells";

    var rowCheckbox = createRowCheckbox();
    column0.appendChild(rowCheckbox);

    column1.appendChild(createHiddenElement("","<%=ActionInputs.VIEWID_DEFAULT%>")); <%// viewId%>
    column1.appendChild(createHiddenElement("","")); <%// original language%>
    column1.appendChild(createHiddenElement("","")); <%// original text%>
    column1.appendChild(createHiddenElement("",textControlTitle)); <%// control title%>
    column1.appendChild(createHiddenElement("",hasError)); <%// mode%>
    if (hasError)
    {
      var languageSelect = document.createElement("select");
      languageSelect.className = "selectlist";
      populateLanguageSelect(languageSelect);
      column1.appendChild(languageSelect);
      
      var textInput = document.createElement("input");
      textInput.className = "tabletextenter";
      textInput.title = textControlTitle;
      column2.appendChild(textInput);
      
      addDetailsLanguageInputRowRemoveActionLink(tableContainerId,column3,table.rows.length);
    }
    else
    {
      column1.appendChild(document.createTextNode(""));
      column2.appendChild(document.createTextNode(""));
      addDetailsLanguageInputRowEditActionLink(tableContainerId,column3,table.rows.length);
    }
    
    newRow.appendChild(column0);
    newRow.appendChild(column1);
    newRow.appendChild(column2);
    newRow.appendChild(column3);
    
    tableBody.appendChild(newRow);
  }

  function removeColumnChildren(column)
  {
    for (var i=0;i<column.childNodes.length;i++)
    {
      column.removeChild(column.childNodes[i]);
      i--;
    }
  }
  
  function addDetailsLanguageInputRowRemoveActionLink(tableContainerId,column,rowIndex)
  {
    var removeLink = document.createElement("a");
    setJSLinkRowTarget(removeLink,"removeDetailsLanguageInputRow",tableContainerId,rowIndex);
    removeLink.appendChild(document.createTextNode("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_LINK_REMOVE"))%>"));
    column.appendChild(removeLink);
  }

  function addDetailsLanguageInputRowEditActionLink(tableContainerId,column,rowIndex)
  {
    var editLink = document.createElement("a");
    setJSLinkRowTarget(editLink,"editDetailsLanguageInputRow",tableContainerId,rowIndex);
    editLink.appendChild(document.createTextNode("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_LINK_EDIT"))%>"));
    column.appendChild(editLink);  
  }  
  
  function addDetailsLanguageInputRowCancelActionLink(tableContainerId,column,rowIndex)
  {
    var cancelLink = document.createElement("a");
    setJSLinkRowTarget(cancelLink,"cancelDetailsLanguageInputRowEdit",tableContainerId,rowIndex);
    cancelLink.appendChild(document.createTextNode("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_LINK_CANCEL"))%>"));
    column.appendChild(cancelLink);
  }
  
  function editDetailsLanguageInputRow(tableContainerId,rowIndex)
  {
    var table = getTable(tableContainerId);
    var row = table.rows[rowIndex];
    var columns = row.getElementsByTagName("td");
    
    var column1 = columns.item(1);
    var column2 = columns.item(2);
    var column3 = columns.item(3);
    
    var langId = column1.childNodes[1].value;
    var inputValue = column1.childNodes[2].value;
    var textControlTitle = column1.childNodes[3].value;
    var editable = (column1.childNodes[4].value == "true")
    if (editable)
      return;
    column1.childNodes[4].value = true;
    var column1Control = column1.childNodes[5];
    
    var languageSelect = document.createElement("select");
    populateLanguageSelect(languageSelect);
    column1.replaceChild(languageSelect,column1Control);
    languageSelect.className = "selectlist";
    setLanguageSelect(languageSelect,langId);
    
    var column2Control = column2.childNodes[0];
    var textInput = document.createElement("input");
    column2.replaceChild(textInput,column2Control);
    textInput.className = "tabletextenter";
    textInput.title = textControlTitle;
    textInput.value = inputValue;

    removeColumnChildren(column3);
    addDetailsLanguageInputRowCancelActionLink(tableContainerId,column3,rowIndex);
  }
  
  function setDetailsLanguageInputRow(tableContainerId,index,viewId,langId,inputValue)
  {
    var table = getTable(tableContainerId);
    var row = table.rows[numberOfHeaderRows+index];
    var columns = row.getElementsByTagName("td");
    var column1 = columns.item(1);
    var column2 = columns.item(2);
    column1.childNodes[0].value = viewId;
    column1.childNodes[1].value = langId;
    column1.childNodes[2].value = inputValue;
    var editable = (column1.childNodes[4].value == "true");
    var column1Control = column1.childNodes[5];
    if (editable)
      setLanguageSelect(column1Control,langId);
    else
    {
      var tempSelect = document.createElement("select");
      populateLanguageSelect(tempSelect);
      var language = setLanguageSelect(tempSelect,langId);
      column1.replaceChild(document.createTextNode(getDefaultDisplayString(language)),column1Control);
    }
    
    var column2Control = column2.childNodes[0];
    if (editable)
      column2Control.value = inputValue;
    else
      column2.replaceChild(document.createTextNode(getDefaultDisplayString(inputValue)),column2Control)
  }
  
  function removeDetailsLanguageInputRow(tableContainerId,rowIndex)
  {
    var table = getTable(tableContainerId);
    table.deleteRow(rowIndex);
    fixDetailsLanguageInputRowLinks(tableContainerId);
  }
    
  function cancelDetailsLanguageInputRowEdit(tableContainerId,rowIndex)
  {
    var table = getTable(tableContainerId);
    var row = table.rows[rowIndex];
    var columns = row.getElementsByTagName("td");
    var column1 = columns.item(1);
    var column2 = columns.item(2);
    var column3 = columns.item(3);
    
    var viewId = column1.childNodes[0].value;
    var langId = column1.childNodes[1].value;
    var inputValue = column1.childNodes[2].value;
    var rowActionLinkHref = column3.childNodes[column3.childNodes.length-1].href;
    if (rowActionLinkHref.indexOf("javascript:cancel") != 0)
      return;
      
    column1.childNodes[4].value = false;
    
    var column1Control = column1.childNodes[5];
    var tempSelect = document.createElement("select");
    populateLanguageSelect(tempSelect);
    var language = setLanguageSelect(tempSelect,langId);
    column1.replaceChild(document.createTextNode(getDefaultDisplayString(language)),column1Control);
    
    var column2Control = column2.childNodes[0];
    column2.replaceChild(document.createTextNode(getDefaultDisplayString(inputValue)),column2Control);

    removeColumnChildren(column3);
    addDetailsLanguageInputRowEditActionLink(tableContainerId,column3,rowIndex);
  }

  function removeSelectedDetailsLanguageInputRows(tableContainerId)
  {
    removeSelectedRows(tableContainerId);
    fixDetailsLanguageInputRowLinks(tableContainerId);
  }
  
  function fixDetailsLanguageInputRowLinks(tableContainerId)
  {
    <%// Fix the links%>
    var table = getTable(tableContainerId);
    for (var i=numberOfHeaderRows;i<table.rows.length;i++)
    {
      var row = table.rows[i];
      var columns = row.getElementsByTagName("td");
      var rowCheckbox = columns.item(0).getElementsByTagName("input").item(0);
      
      for (var j=1;j<columns.length;j++)
      {
        if (rowCheckbox.checked)
          columns.item(j).className = "rowcolor";
        else
          columns.item(j).className = "tablecells";
      }
      
      var column1 = columns.item(1);
      var column3 = columns.item(3);
      var link = column3.childNodes[column3.childNodes.length-1];
      var removeLink;
      if (link.href.indexOf("javascript:remove") == 0)
        removeLink = true;
      else
        removeLink = false;
        
      removeColumnChildren(column3);            
      var editable = (column1.childNodes[4].value == "true");
      if (link.href.indexOf("javascript:remove") == 0)
        addDetailsLanguageInputRowRemoveActionLink(tableContainerId,column3,i);
      else if (link.href.indexOf("javascript:cancel") == 0)
        addDetailsLanguageInputRowCancelActionLink(tableContainerId,column3,i);
      else if (link.href.indexOf("javascript:edit") == 0)
        addDetailsLanguageInputRowEditActionLink(tableContainerId,column3,i);
    }
  }
  
  function editSelectedDetailsLanguageInputRows(tableContainerId)
  {
    var table = getTable(tableContainerId);
    for (var i=numberOfHeaderRows;i<table.rows.length;i++)
    {
      var rowCheckbox = table.rows[i].getElementsByTagName("input").item(0);
      if (rowCheckbox.checked)
        editDetailsLanguageInputRow(tableContainerId,i);
    }
  }
    
  function cancelSelectedDetailsLanguageInputRows(tableContainerId)
  {
    var table = getTable(tableContainerId);
    for (var i=numberOfHeaderRows;i<table.rows.length;i++)
    {
      var rowCheckbox = table.rows[i].getElementsByTagName("input").item(0);
      if (rowCheckbox.checked)
        cancelDetailsLanguageInputRowEdit(tableContainerId,i);
    }
  }
  
  function addDetailsIdentifierRow(tableContainerId,hasError)
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
    
    var rowCheckbox = createRowCheckbox();
    column0.appendChild(rowCheckbox);
    
    column0.className = "checkboxcells";
    column1.className = "tablecells";
    column1.width = "10%";
    column2.className = "tablecells";
    column2.width = "40%";
    column3.className = "tablecells";
    column3.width = "40%";
    column4.className = "tablecells";

    column1.appendChild(createHiddenElement("","<%=ActionInputs.VIEWID_DEFAULT%>")); <%// viewId%>
    column1.appendChild(createHiddenElement("","")); <%// original key type%>
    column1.appendChild(createHiddenElement("","")); <%// original key name%>
    column1.appendChild(createHiddenElement("","")); <%// original key value%>
    column1.appendChild(createHiddenElement("",hasError)); <%// mode%>
    
    var identifierTypeSelect;
    var keyNameTextInput;
    if (hasError)
    {
      identifierTypeSelect = document.createElement("select");
      identifierTypeSelect.onchange = handleIdentifierChange;
      populateIdentifierTypeSelect(identifierTypeSelect);
      identifierTypeSelect.className = "selectlist";
      column1.appendChild(identifierTypeSelect);    
    
      keyNameTextInput = document.createElement("input");
      keyNameTextInput.className = "tabletextenter";
      keyNameTextInput.title = "<%=uddiPerspective.getMessage("FORM_CONTROL_TITLE_IDENTIFIER_KEY_NAME")%>";
      column2.appendChild(keyNameTextInput);

      var keyValueTextInput = document.createElement("input");
      keyValueTextInput.className = "tabletextenter";
      keyValueTextInput.title = "<%=uddiPerspective.getMessage("FORM_CONTROL_TITLE_IDENTIFIER_KEY_VALUE")%>";
      column3.appendChild(keyValueTextInput);      
      addDetailsIdentifierRowRemoveActionLink(tableContainerId,column4,table.rows.length);
    }
    else
    {
      column1.appendChild(document.createTextNode(""));
      column2.appendChild(document.createTextNode(""));
      column3.appendChild(document.createTextNode(""));
      addDetailsIdentifierRowEditActionLink(tableContainerId,column4,table.rows.length);
    }
    
    newRow.appendChild(column0);
    newRow.appendChild(column1);
    newRow.appendChild(column2);
    newRow.appendChild(column3);
    newRow.appendChild(column4);
    tableBody.appendChild(newRow);
    if (hasError)
      keyNameTextInput.value = identifierTypeSelect.options[0].text;
  }

  function addDetailsIdentifierRowRemoveActionLink(tableContainerId,column,rowIndex)
  {
    var removeLink = document.createElement("a");
    setJSLinkRowTarget(removeLink,"removeDetailsIdentifierRow",tableContainerId,rowIndex);
    removeLink.appendChild(document.createTextNode("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_LINK_REMOVE"))%>"));
    column.appendChild(removeLink);
  }
  
  function addDetailsIdentifierRowEditActionLink(tableContainerId,column,rowIndex)
  {
    var editLink = document.createElement("a");
    setJSLinkRowTarget(editLink,"editDetailsIdentifierRow",tableContainerId,rowIndex);
    editLink.appendChild(document.createTextNode("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_LINK_EDIT"))%>"));
    column.appendChild(editLink);
  }  

  function addDetailsIdentifierRowCancelActionLink(tableContainerId,column,rowIndex)
  {
    var cancelLink = document.createElement("a");
    setJSLinkRowTarget(cancelLink,"cancelDetailsIdentifierRowEdit",tableContainerId,rowIndex);
    cancelLink.appendChild(document.createTextNode("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_LINK_CANCEL"))%>"));
    column.appendChild(cancelLink);  
  }

  function editDetailsIdentifierRow(tableContainerId,rowIndex)
  {
    var table = getTable(tableContainerId);
    var row = table.rows[rowIndex];
    var columns = row.getElementsByTagName("td");
    
    var column1 = columns.item(1);
    var column2 = columns.item(2);
    var column3 = columns.item(3);
    var column4 = columns.item(4);
    
    var tModelKey = column1.childNodes[1].value     
    var keyName = column1.childNodes[2].value;
    var keyValue = column1.childNodes[3].value;
    var editable = (column1.childNodes[4].value == "true")
    if (editable)
      return;
    column1.childNodes[4].value = true;
    
    var column1Control = column1.childNodes[5];
    var identifierTypeSelect = document.createElement("select");
    identifierTypeSelect.onchange = handleIdentifierChange;
    populateIdentifierTypeSelect(identifierTypeSelect);
    column1.replaceChild(identifierTypeSelect,column1Control);
    identifierTypeSelect.className = "selectlist";
    setTModelKeySelect(identifierTypeSelect,tModelKey);    
    
    var column2Control = column2.childNodes[0];
    var keyNameTextInput = document.createElement("input");
    column2.replaceChild(keyNameTextInput,column2Control);
    keyNameTextInput.className = "tabletextenter";
    keyNameTextInput.title = "<%=uddiPerspective.getMessage("FORM_CONTROL_TITLE_IDENTIFIER_KEY_NAME")%>";
    keyNameTextInput.value = keyName;
    
    var column3Control = column3.childNodes[0];
    var keyValueTextInput = document.createElement("input");
    column3.replaceChild(keyValueTextInput,column3Control);
    keyValueTextInput.className = "tabletextenter";
    keyValueTextInput.title = "<%=uddiPerspective.getMessage("FORM_CONTROL_TITLE_IDENTIFIER_KEY_VALUE")%>";
    keyValueTextInput.value = keyValue;
   
    removeColumnChildren(column4);
    
    addDetailsIdentifierRowCancelActionLink(tableContainerId,column4,rowIndex);
  }

  function setDetailsIdentifierRow(tableContainerId,index,viewId,tModelKey,keyName,keyValue)
  {
    var table = getTable(tableContainerId);
    var row = table.rows[numberOfHeaderRows+index];
    var columns = row.getElementsByTagName("td");
    var column1 = columns.item(1);
    var column2 = columns.item(2);
    var column3 = columns.item(3);
    column1.childNodes[0].value = viewId;
    column1.childNodes[1].value = tModelKey;
    column1.childNodes[2].value = keyName;
    column1.childNodes[3].value = keyValue;
    var editable = (column1.childNodes[4].value == "true");
    var column1Control = column1.childNodes[5];
    if (editable)
      setTModelKeySelect(column1Control,tModelKey);
    else
    {
      var tempSelect = document.createElement("select");
      populateIdentifierTypeSelect(tempSelect);
      var tModelKeyValue = setTModelKeySelect(tempSelect,tModelKey);
      column1.replaceChild(document.createTextNode(tModelKeyValue),column1Control);
    }
        
    var column2Control = column2.childNodes[0];
    if (editable)
      column2Control.value = keyName;
    else
      column2.replaceChild(document.createTextNode(keyName),column2Control);
      
    var column3Control = column3.childNodes[0];
    if (editable)
      column3Control.value = keyValue;
    else
      column3.replaceChild(document.createTextNode(keyValue),column3Control);
  }

  function removeDetailsIdentifierRow(tableContainerId,rowIndex)
  {
    var table = getTable(tableContainerId);
    table.deleteRow(rowIndex);
    fixDetailsIdentifierRowLinks(tableContainerId);
  }
  
  function cancelDetailsIdentifierRowEdit(tableContainerId,rowIndex)
  {
    var table = getTable(tableContainerId);
    var row = table.rows[rowIndex];
    var columns = row.getElementsByTagName("td");
    var column1 = columns.item(1);
    var column2 = columns.item(2);
    var column3 = columns.item(3);
    var column4 = columns.item(4);
    
    var viewId = column1.childNodes[0].value;
    var tModelKey = column1.childNodes[1].value;
    var keyName = column1.childNodes[2].value;
    var keyValue = column1.childNodes[3].value;
    var rowActionLinkHref = column4.childNodes[column4.childNodes.length-1].href;
    if (rowActionLinkHref.indexOf("javascript:cancel") != 0)
      return;
    column1.childNodes[4].value = false;
    
    var column1Control = column1.childNodes[5];
    var tempSelect = document.createElement("select");
    populateIdentifierTypeSelect(tempSelect);
    var tModelKeyValue = setTModelKeySelect(tempSelect,tModelKey);
    column1.replaceChild(document.createTextNode(tModelKeyValue),column1Control);
    
    var column2Control = column2.childNodes[0];
    column2.replaceChild(document.createTextNode(keyName),column2Control);
    
    var column3Control = column3.childNodes[0];
    column3.replaceChild(document.createTextNode(keyValue),column3Control);

    removeColumnChildren(column4);
    addDetailsIdentifierRowEditActionLink(tableContainerId,column4,rowIndex);
  }
  
  function removeSelectedDetailsIdentifierRows(tableContainerId)
  {
    removeSelectedRows(tableContainerId);
    <%// Fix the links%>
    fixDetailsIdentifierRowLinks(tableContainerId);
  }
  
  function fixDetailsIdentifierRowLinks(tableContainerId)
  {
    var table = getTable(tableContainerId);
    for (var i=numberOfHeaderRows;i<table.rows.length;i++)
    {
      var row = table.rows[i];
      var columns = row.getElementsByTagName("td");
      var rowCheckbox = columns.item(0).getElementsByTagName("input").item(0);
      for (var j=1;j<columns.length;j++)
      {
        if (rowCheckbox.checked)
          columns.item(j).className = "rowcolor";
        else
          columns.item(j).className = "tablecells";
      }
      var column1 = columns.item(1);
      var viewId = column1.childNodes[0].value;
      var column4 = columns.item(4);
      var link = column4.childNodes[column4.childNodes.length-1];
      var removeLink;
      if (link.href.indexOf("javascript:remove") == 0)
        removeLink = true;
      else
        removeLink = false;      
      removeColumnChildren(column4);
      
      if (link.href.indexOf("javascript:remove") == 0)
        addDetailsIdentifierRowRemoveActionLink(tableContainerId,column4,i);
      else if (link.href.indexOf("javascript:edit") == 0)
        addDetailsIdentifierRowEditActionLink(tableContainerId,column4,i);
      else if (link.href.indexOf("javascript:cancel") == 0)
        addDetailsIdentifierRowCancelActionLink(tableContainerId,column4,i);
    }  
  }
  
  function editSelectedDetailsIdentifierRows(tableContainerId)
  {
    var table = getTable(tableContainerId);
    for (var i=numberOfHeaderRows;i<table.rows.length;i++)
    {
      var rowCheckbox = table.rows[i].getElementsByTagName("input").item(0);
      if (rowCheckbox.checked)
        editDetailsIdentifierRow(tableContainerId,i);
    }  
  }
  
  function cancelSelectedDetailsIdentifierRows(tableContainerId)
  {
    var table = getTable(tableContainerId);
    for (var i=numberOfHeaderRows;i<table.rows.length;i++)
    {
      var rowCheckbox = table.rows[i].getElementsByTagName("input").item(0);
      if (rowCheckbox.checked)
        cancelDetailsIdentifierRowEdit(tableContainerId,i);
    }  
  }
  
  function addDetailsCategoryRow(tableContainerId,hasError)
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
    
    var rowCheckbox = createRowCheckbox();
    column0.appendChild(rowCheckbox);
    
    column0.className = "checkboxcells";
    column1.className = "tablecells";
    column1.width = "5%";
    column2.className = "tablecells";
    column2.width = "55%";
    column3.className = "tablecells";
    column3.width = "25%";
    column4.className = "tablecells";

    column1.appendChild(createHiddenElement("","<%=ActionInputs.VIEWID_DEFAULT%>")); <%// viewId%>
    column1.appendChild(createHiddenElement("","")); <%// original key type%>
    column1.appendChild(createHiddenElement("","")); <%// original key name%>
    column1.appendChild(createHiddenElement("","")); <%// original key value%>
    column1.appendChild(createHiddenElement("",hasError)); <%// mode%>
    
    if (hasError)
    {
      var categoryTypeSelect = document.createElement("select");
      populateCategoryTypeSelect(categoryTypeSelect);
      categoryTypeSelect.className = "selectlist";
      column1.appendChild(categoryTypeSelect);
      
      var keyNameTextInput = document.createElement("input");
      keyNameTextInput.className = "tabletextenter";
      keyNameTextInput.title = "<%=uddiPerspective.getMessage("FORM_CONTROL_TITLE_CATEGORY_KEY_NAME")%>";
      column2.appendChild(keyNameTextInput);
      
      var keyValueTextInput = document.createElement("input");
      keyValueTextInput.className = "tabletextenter";
      keyValueTextInput.title = "<%=uddiPerspective.getMessage("FORM_CONTROL_TITLE_CATEGORY_KEY_VALUE")%>";
      column3.appendChild(keyValueTextInput);
      addCategoryRowBrowseLink(tableContainerId,column4,table.rows.length);
    }
    else
    {
      column1.appendChild(document.createTextNode(""));
      column2.appendChild(document.createTextNode(""));
      column3.appendChild(document.createTextNode(""));
      addDetailsCategoryRowEditActionLink(tableContainerId,column4,table.rows.length);
    }
      
    newRow.appendChild(column0);
    newRow.appendChild(column1);
    newRow.appendChild(column2);
    newRow.appendChild(column3);
    newRow.appendChild(column4);
    tableBody.appendChild(newRow);      
  }

  function addDetailsCategoryRowCancelActionLink(tableContainerId,column,rowIndex)
  {
    var cancelLink = document.createElement("a");
    setJSLinkRowTarget(cancelLink,"cancelDetailsCategoryRowEdit",tableContainerId,rowIndex);
    cancelLink.appendChild(document.createTextNode("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_LINK_CANCEL"))%>"));
    column.appendChild(cancelLink);  
  }

  function addDetailsCategoryRowBrowseCancelActionLinks(tableContainerId,column,rowIndex)
  {
    addCategoryRowBrowseLink(tableContainerId,column,rowIndex);
    column.appendChild(document.createTextNode(" "));
    addDetailsCategoryRowCancelActionLink(tableContainerId,column,rowIndex);
  }

  function addDetailsCategoryRowEditActionLink(tableContainerId,column,rowIndex)
  {
    var editLink = document.createElement("a");
    setJSLinkRowTarget(editLink,"editDetailsCategoryRow",tableContainerId,rowIndex);
    editLink.appendChild(document.createTextNode("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_LINK_EDIT"))%>"));
    column.appendChild(editLink);
  }

  function addDetailsCategoryRowRemoveActionLink(tableContainerId,column,rowIndex)
  {
    var removeLink = document.createElement("a");
    setJSLinkRowTarget(removeLink,"removeDetailsCategoryRow",tableContainerId,rowIndex);
    removeLink.appendChild(document.createTextNode("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_LINK_REMOVE"))%>"));
    column.appendChild(removeLink);  
  }

  function addDetailsCategoryRowBrowseRemoveActionLinks(tableContainerId,column,rowIndex)
  {
    addCategoryRowBrowseLink(tableContainerId,column,rowIndex);
    column.appendChild(document.createTextNode(" "));
    addDetailsCategoryRowRemoveActionLink(tableContainerId,column,rowIndex);
  }

  function editDetailsCategoryRow(tableContainerId,rowIndex)
  {
    var table = getTable(tableContainerId);
    var row = table.rows[rowIndex];
    var columns = row.getElementsByTagName("td");
    
    var column1 = columns.item(1);
    var column2 = columns.item(2);
    var column3 = columns.item(3);
    var column4 = columns.item(4);
    
    var tModelKey = column1.childNodes[1].value
    var keyName = column1.childNodes[2].value;
    var keyValue = column1.childNodes[3].value;
    var editable = (column1.childNodes[4].value == "true")
    if (editable)
      return;
    column1.childNodes[4].value = true;
    
    var column1Control = column1.childNodes[5];
    var categoryTypeSelect = document.createElement("select");
    populateCategoryTypeSelect(categoryTypeSelect);
    column1.replaceChild(categoryTypeSelect,column1Control);
    categoryTypeSelect.className = "selectlist";
    setTModelKeySelect(categoryTypeSelect,tModelKey);    
    
    var column2Control = column2.childNodes[0];
    var keyNameTextInput = document.createElement("input");
    column2.replaceChild(keyNameTextInput,column2Control);
    keyNameTextInput.className = "tabletextenter";
    keyNameTextInput.title = "<%=uddiPerspective.getMessage("FORM_CONTROL_TITLE_CATEGORY_KEY_NAME")%>";
    keyNameTextInput.value = keyName;
    
    var column3Control = column3.childNodes[0];
    var keyValueTextInput = document.createElement("input");
    column3.replaceChild(keyValueTextInput,column3Control);
    keyValueTextInput.className = "tabletextenter";
    keyValueTextInput.title = "<%=uddiPerspective.getMessage("FORM_CONTROL_TITLE_CATEGORY_KEY_VALUE")%>";
    keyValueTextInput.value = keyValue;
    
    removeColumnChildren(column4);
    
    addDetailsCategoryRowBrowseCancelActionLinks(tableContainerId,column4,rowIndex);
  }

  function setDetailsCategoryRow(tableContainerId,index,viewId,tModelKey,keyName,keyValue)
  {
    var table = getTable(tableContainerId);
    var row = table.rows[numberOfHeaderRows+index];
    var columns = row.getElementsByTagName("td");
    var column1 = columns.item(1);
    var column2 = columns.item(2);
    var column3 = columns.item(3);
    column1.childNodes[0].value = viewId;
    column1.childNodes[1].value = tModelKey;
    column1.childNodes[2].value = keyName;
    column1.childNodes[3].value = keyValue;
    var editable = (column1.childNodes[4].value == "true");
    
    var column1Control = column1.childNodes[5];
    if (editable)
      setTModelKeySelect(column1Control,tModelKey);
    else
    {
      var tempSelect = document.createElement("select");
      populateCategoryTypeSelect(tempSelect);
      var tModelKeyValue = setTModelKeySelect(tempSelect,tModelKey);
      column1.replaceChild(document.createTextNode(tModelKeyValue),column1Control);
    }
        
    var column2Control = column2.childNodes[0];
    if (editable)
      column2Control.value = keyName;
    else
      column2.replaceChild(document.createTextNode(keyName),column2Control);
      
    var column3Control = column3.childNodes[0];
    if (editable)
      column3Control.value = keyValue;
    else
      column3.replaceChild(document.createTextNode(keyValue),column3Control); 
  }

  function removeDetailsCategoryRow(tableContainerId,rowIndex)
  {
    var table = getTable(tableContainerId);
    table.deleteRow(rowIndex);
    fixDetailsCategoryRowLinks(tableContainerId);
  }
  
  function cancelDetailsCategoryRowEdit(tableContainerId,rowIndex)
  {
    var table = getTable(tableContainerId);
    var row = table.rows[rowIndex];
    var columns = row.getElementsByTagName("td");
    var column1 = columns.item(1);
    var column2 = columns.item(2);
    var column3 = columns.item(3);
    var column4 = columns.item(4);
    
    var viewId = column1.childNodes[0].value;
    var tModelKey = column1.childNodes[1].value;
    var keyName = column1.childNodes[2].value;
    var keyValue = column1.childNodes[3].value;
    var rowActionLinkHref = column4.childNodes[column4.childNodes.length-1].href;
    if (rowActionLinkHref.indexOf("javascript:cancel") != 0)
      return;
    column1.childNodes[4].value = false;
    
    var column1Control = column1.childNodes[5];
    var tempSelect = document.createElement("select");
    populateCategoryTypeSelect(tempSelect);
    var tModelKeyValue = setTModelKeySelect(tempSelect,tModelKey);
    column1.replaceChild(document.createTextNode(tModelKeyValue),column1Control);
    
    var column2Control = column2.childNodes[0];
    column2.replaceChild(document.createTextNode(keyName),column2Control);
    
    var column3Control = column3.childNodes[0];
    column3.replaceChild(document.createTextNode(keyValue),column3Control);

    removeColumnChildren(column4);    
    addDetailsCategoryRowEditActionLink(tableContainerId,column4,rowIndex);
    if (rowIndex == targetCategoryRow)
      closeCategoryBrowser();
  }
  
  function removeSelectedDetailsCategoryRows(tableContainerId)
  {
    removeSelectedRows(tableContainerId);
    <%// Fix the links%>
    fixDetailsCategoryRowLinks(tableContainerId);
  }
  
  function fixDetailsCategoryRowLinks(tableContainerId)
  {
    var table = getTable(tableContainerId);
    for (var i=numberOfHeaderRows;i<table.rows.length;i++)
    {
      var row = table.rows[i];
      var columns = row.getElementsByTagName("td");
      var rowCheckbox = columns.item(0).getElementsByTagName("input").item(0);
      for (var j=1;j<columns.length;j++)
      {
        if (rowCheckbox.checked)
          columns.item(j).className = "rowcolor";
        else
          columns.item(j).className = "tablecells";
      }
      var column1 = columns.item(1);
      var viewId = column1.childNodes[0].value;
      var column4 = columns.item(4);
<%      
      // Browse, Remove
      // Browse, Cancel
      // Edit
      // Browse
%>      
      var lastLink = column4.childNodes[column4.childNodes.length-1];
      removeColumnChildren(column4);
      if (lastLink.href.indexOf("javascript:remove") == 0)
        addDetailsCategoryRowBrowseRemoveActionLinks(tableContainerId,column4,i);
      else if (lastLink.href.indexOf("javascript:cancel") == 0)
        addDetailsCategoryRowBrowseCancelActionLinks(tableContainerId,column4,i);
      else if (lastLink.href.indexOf("javascript:edit") == 0)
        addDetailsCategoryRowEditActionLink(tableContainerId,column4,i);
      else if (lastLink.href.indexOf("javascript:open") == 0)
        addCategoryRowBrowseLink(tableContainerId,column4,i);
    }  
  }
  
  function editSelectedDetailsCategoryRows(tableContainerId)
  {
    var table = getTable(tableContainerId);
    for (var i=numberOfHeaderRows;i<table.rows.length;i++)
    {
      var rowCheckbox = table.rows[i].getElementsByTagName("input").item(0);
      if (rowCheckbox.checked)
        editDetailsCategoryRow(tableContainerId,i);
    }  
  }
  
  function cancelSelectedDetailsCategoryRows(tableContainerId)
  {
    var table = getTable(tableContainerId);
    for (var i=numberOfHeaderRows;i<table.rows.length;i++)
    {
      var rowCheckbox = table.rows[i].getElementsByTagName("input").item(0);
      if (rowCheckbox.checked)
        cancelDetailsCategoryRowEdit(tableContainerId,i);
    }  
  }
  
  function addDetailsUserDefinedCategoryRow(tableContainerId,tModelKey,categoryName,isChecked,fileName,hasError)
  {
    var table = getTable(tableContainerId);
    var tableBody = table.getElementsByTagName("TBODY").item(0);
    var newRow = document.createElement("tr");
    //var column0 = document.createElement("td");
    var column1 = document.createElement("td");
    //var column2 = document.createElement("td");
    //var column3 = document.createElement("td");
    //var column4 = document.createElement("td");
    
    //column0.className = "checkboxcells";
    column1.className = "tablecells";
    column1.width = "15%";
    //column2.className = "tablecells";
    //column2.width = "10%";
    //column3.className = "tablecells";
    //column3.width = "70%";
    //column4.className = "tablecells";
    
    //var rowCheckbox = createRowCheckbox();
    //column0.appendChild(rowCheckbox);
    
    //column1.appendChild(createHiddenElement("",fileName));  <%// original file name%>
    //column1.appendChild(createHiddenElement("",hasError));  <%// mode%>
    //column1.appendChild(createHiddenElement("",tModelKey)); <%// tModelKey%>
    column1.appendChild(document.createTextNode(categoryName));
    
    //column2.appendChild(document.createTextNode(isChecked));
    
    //if (hasError)
    //{
    //  column3.appendChild(createHiddenElement("<%=UDDIActionInputs.CATEGORY_TMODEL_KEY%>",tModelKey));
    //  var fileInput = document.createElement("input");
    //  fileInput.setAttribute("name","<%=UDDIActionInputs.CATEGORY_FILENAME%>");
    //  fileInput.setAttribute("type","file");
    //  fileInput.title = "<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_CONTROL_TITLE_CATEGORY_FILE"))%>";
    //  fileInput.className = "tablefilebrowse";
    //  column3.appendChild(fileInput);
    //  addDetailsUserDefinedCategoryRowCancelLink(tableContainerId,column4,table.rows.length);
    //}
    //else
    //{
    //  column3.appendChild(document.createTextNode(fileName));
    //  addDetailsUserDefinedCategoryRowEditLink(tableContainerId,column4,table.rows.length);
    //}
    
    //newRow.appendChild(column0);
    newRow.appendChild(column1);
    //newRow.appendChild(column2);
    //newRow.appendChild(column3);
    //newRow.appendChild(column4);
    
    tableBody.appendChild(newRow);
  }
  
  function addDetailsUserDefinedCategoryRowEditLink(tableContainerId,column,rowIndex)
  {
    var editLink = document.createElement("a");
    setJSLinkRowTarget(editLink,"editDetailsUserDefinedCategoryRow",tableContainerId,rowIndex);
    editLink.appendChild(document.createTextNode("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_LINK_EDIT"))%>"));
    column.appendChild(editLink);
  }
  
  function editDetailsUserDefinedCategoryRow(tableContainerId,rowIndex)
  {
    var table = getTable(tableContainerId);
    var row = table.rows[rowIndex];
    var columns = row.getElementsByTagName("td");
    
    var column1 = columns.item(1);    
    var column3 = columns.item(3);
    var column4 = columns.item(4);
    
    var editable = (column1.childNodes[1].value == "true");
    if (editable)
      return;
    var tModelKey = column1.childNodes[2].value;
    column1.childNodes[1].value = true;
    
    removeColumnChildren(column3);
    column3.appendChild(createHiddenElement("<%=UDDIActionInputs.CATEGORY_TMODEL_KEY%>",tModelKey));
    var fileInput = document.createElement("input");
    fileInput.setAttribute("name","<%=UDDIActionInputs.CATEGORY_FILENAME%>");
    fileInput.setAttribute("type","file");
    fileInput.title = "<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_CONTROL_TITLE_CATEGORY_FILE"))%>";
    fileInput.className = "tablefilebrowse";
    column3.appendChild(fileInput);
    
    removeColumnChildren(column4);
    
    addDetailsUserDefinedCategoryRowCancelLink(tableContainerId,column4,rowIndex);
  }
  
  function addDetailsUserDefinedCategoryRowCancelLink(tableContainerId,column,rowIndex)
  {
    var cancelLink = document.createElement("a");
    setJSLinkRowTarget(cancelLink,"cancelDetailsUserDefinedCategoryRowEdit",tableContainerId,rowIndex);
    cancelLink.appendChild(document.createTextNode("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_LINK_CANCEL"))%>"));
    column.appendChild(cancelLink);
  }
  
  function cancelDetailsUserDefinedCategoryRowEdit(tableContainerId,rowIndex)
  {
    var table = getTable(tableContainerId);
    var row = table.rows[rowIndex];
    var columns = row.getElementsByTagName("td");
    
    var column1 = columns.item(1);
    var column3 = columns.item(3);
    var column4 = columns.item(4);
    
    var editable = (column1.childNodes[1].value == "true");
    if (!editable)
      return;
    column1.childNodes[1].value = false;
    
    removeColumnChildren(column3);
    column3.appendChild(document.createTextNode(column1.childNodes[0].value));
    
    removeColumnChildren(column4);
    
    addDetailsUserDefinedCategoryRowEditLink(tableContainerId,column4,rowIndex);
  }
  
  function editSelectedDetailsUserDefinedCategoryRows(tableContainerId)
  {
    var table = getTable(tableContainerId);
    for (var i=numberOfHeaderRows;i<table.rows.length;i++)
    {
      var rowCheckbox = table.rows[i].getElementsByTagName("input").item(0);
      if (rowCheckbox.checked)
        editDetailsUserDefinedCategoryRow(tableContainerId,i);
    }
  }
  
  function cancelSelectedDetailsUserDefinedCategoryRows(tableContainerId)
  {
    var table = getTable(tableContainerId);
    for (var i=numberOfHeaderRows;i<table.rows.length;i++)
    {
      var rowCheckbox = table.rows[i].getElementsByTagName("input").item(0);
      if (rowCheckbox.checked)
        cancelDetailsUserDefinedCategoryRowEdit(tableContainerId,i);
    }
  }  
  
  function addDetailsDiscoveryURLRow(tableContainerId,hasError)
  {
    twistOpen(tableContainerId);
    var table = getTable(tableContainerId);
    var tableBody = table.getElementsByTagName("TBODY").item(0);
    var newRow = document.createElement("tr");
    var column0 = document.createElement("td");
    var column1 = document.createElement("td");
    var column2 = document.createElement("td");

    column0.className = "checkboxcells";
    column1.className = "tablecells";
    column1.width = "85%";
    column2.className = "tablecells";
    
    var rowCheckbox = createRowCheckbox();
    column0.appendChild(rowCheckbox);
    
    column1.appendChild(createHiddenElement("","<%=ActionInputs.VIEWID_DEFAULT%>")); <%// viewId%>
    column1.appendChild(createHiddenElement("","")); <%// original Discovery URL%>
    column1.appendChild(createHiddenElement("",hasError)); <%// mode%>
    if (hasError)
    {
      var textInput = document.createElement("input");
      textInput.className = "tabletextenter";
      textInput.title = "<%=uddiPerspective.getMessage("FORM_CONTROL_TITLE_DISCOVERY_URL")%>";
      column1.appendChild(textInput);
      addDetailsDiscoveryURLRowRemoveActionLink(tableContainerId,column2,table.rows.length);
    }
    else
    {
      column1.appendChild(document.createTextNode(""));
      addDetailsDiscoveryURLRowEditActionLink(tableContainerId,column2,table.rows.length);
    }
    
    newRow.appendChild(column0);
    newRow.appendChild(column1);
    newRow.appendChild(column2);
    
    tableBody.appendChild(newRow);
  }
  
  function addDetailsDiscoveryURLRowRemoveActionLink(tableContainerId,column,rowIndex)
  {
    var removeLink = document.createElement("a");
    setJSLinkRowTarget(removeLink,"removeDetailsDiscoveryURLRow",tableContainerId,rowIndex);
    removeLink.appendChild(document.createTextNode("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_LINK_REMOVE"))%>"));
    column.appendChild(removeLink);
  }
  
  function addDetailsDiscoveryURLRowEditActionLink(tableContainerId,column,rowIndex)
  {
    var editLink = document.createElement("a");
    setJSLinkRowTarget(editLink,"editDetailsDiscoveryURLRow",tableContainerId,rowIndex);
    editLink.appendChild(document.createTextNode("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_LINK_EDIT"))%>"));
    column.appendChild(editLink);
  }
  
  function addDetailsDiscoveryURLRowCancelActionLink(tableContainerId,column,rowIndex)
  {
    var cancelLink = document.createElement("a");
    setJSLinkRowTarget(cancelLink,"cancelDetailsDiscoveryURLRowEdit",tableContainerId,rowIndex);
    cancelLink.appendChild(document.createTextNode("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_LINK_CANCEL"))%>"));
    column.appendChild(cancelLink);
  }
  
  function editDetailsDiscoveryURLRow(tableContainerId,rowIndex)
  {
    var table = getTable(tableContainerId);
    var row = table.rows[rowIndex];
    var columns = row.getElementsByTagName("td");
    
    var column1 = columns.item(1);
    var column2 = columns.item(2);
    
    var discoveryURL = column1.childNodes[1].value;
    var editable = (column1.childNodes[2].value == "true")
    if (editable)
      return;
    column1.childNodes[2].value = true;
    
    var column1Control = column1.childNodes[3];
    var textInput = document.createElement("input");
    column1.replaceChild(textInput,column1Control);
    textInput.className = "tabletextenter";
    textInput.title = "<%=uddiPerspective.getMessage("FORM_CONTROL_TITLE_DISCOVERY_URL")%>";
    textInput.value = discoveryURL;
    
    removeColumnChildren(column2);
    addDetailsDiscoveryURLRowCancelActionLink(tableContainerId,column2,rowIndex);
  }
  
  function setDetailsDiscoveryURLRow(tableContainerId,index,viewId,discoveryURL)
  {
    var table = getTable(tableContainerId);
    var row = table.rows[numberOfHeaderRows+index];
    var columns = row.getElementsByTagName("td");
    var column1 = columns.item(1);
    column1.childNodes[0].value = viewId;
    column1.childNodes[1].value = discoveryURL;
    var editable = (column1.childNodes[2].value == "true");
    var column1Control = column1.childNodes[3];
    if (editable)
      column1Control.value = discoveryURL;
    else
      column1.replaceChild(document.createTextNode(getDefaultDisplayString(discoveryURL)),column1Control);
  }
  
  function removeDetailsDiscoveryURLRow(tableContainerId,rowIndex)
  {
    var table = getTable(tableContainerId);
    table.deleteRow(rowIndex);
    fixDetailsDiscoveryURLRowLinks(tableContainerId);
  }
  
  function cancelDetailsDiscoveryURLRowEdit(tableContainerId,rowIndex)
  {
    var table = getTable(tableContainerId);
    var row = table.rows[rowIndex];
    var columns = row.getElementsByTagName("td");
    var column1 = columns.item(1);
    var column2 = columns.item(2);
    
    var viewId = column1.childNodes[0].value;
    var discoveryURL = column1.childNodes[1].value;
    var rowActionLinkHref = column2.childNodes[column2.childNodes.length-1].href;
    if (rowActionLinkHref.indexOf("javascript:cancel") != 0)
      return;
      
    column1.childNodes[2].value = false;
    
    var column1Control = column1.childNodes[3];
    column1.replaceChild(document.createTextNode(getDefaultDisplayString(discoveryURL)),column1Control);
    
    removeColumnChildren(column2);
    addDetailsDiscoveryURLRowEditActionLink(tableContainerId,column2,rowIndex);
  }
  
  function removeSelectedDetailsDiscoveryURLRows(tableContainerId)
  {
    removeSelectedRows(tableContainerId);
    fixDetailsDiscoveryURLRowLinks(tableContainerId);
  }
  
  function fixDetailsDiscoveryURLRowLinks(tableContainerId)
  {
    <%// Fix the links%>
    var table = getTable(tableContainerId);
    for (var i=numberOfHeaderRows;i<table.rows.length;i++)
    {
      var row = table.rows[i];
      var columns = row.getElementsByTagName("td");
      var rowCheckbox = columns.item(0).getElementsByTagName("input").item(0);
      
      for (var j=1;j<columns.length;j++)
      {
        if (rowCheckbox.checked)
          columns.item(j).className = "rowcolor";
        else
          columns.item(j).className = "tablecells";
      }
      
      var column1 = columns.item(1);
      var column2 = columns.item(2);
      var link = column2.childNodes[column2.childNodes.length-1];
      removeColumnChildren(column2);
      var editable = (column1.childNodes[2].value == "true");
      if (link.href.indexOf("javascript:remove") == 0)
        addDetailsDiscoveryURLRowRemoveActionLink(tableContainerId,column2,i);
      else if (link.href.indexOf("javascript:cancel") == 0)
        addDetailsDiscoveryURLRowCancelActionLink(tableContainerId,column2,i);
      else if (link.href.indexOf("javascript:edit") == 0)
        addDetailsDiscoveryURLRowEditActionLink(tableContainerId,column2,i); 
    }
  }
  
  function editSelectedDetailsDiscoveryURLRows(tableContainerId)
  {
    var table = getTable(tableContainerId);
    for (var i=numberOfHeaderRows;i<table.rows.length;i++)
    {
      var rowCheckbox = table.rows[i].getElementsByTagName("input").item(0);
      if (rowCheckbox.checked)
        editDetailsDiscoveryURLRow(tableContainerId,i);
    }
  }
  
  function cancelSelectedDetailsDiscoveryURLRows(tableContainerId)
  {
    var table = getTable(tableContainerId);
    for (var i=numberOfHeaderRows;i<table.rows.length;i++)
    {
      var rowCheckbox = table.rows[i].getElementsByTagName("input").item(0);
      if (rowCheckbox.checked)
        cancelDetailsDiscoveryURLRowEdit(tableContainerId,i);
    }
  }

  function addDetailsSingleItemRow(tableContainerId,hasError)
  {
    var table = getTable(tableContainerId);
    var tableBody = table.getElementsByTagName("TBODY").item(0);
    var newRow = document.createElement("tr");
    var column0 = document.createElement("td");
    var column1 = document.createElement("td");
    
    column0.className = "tablecells";
    column0.width = "90%";
    column1.className = "tablecells";
    
    column0.appendChild(createHiddenElement("","")); <%// item value%>
    column0.appendChild(createHiddenElement("",hasError)); <%// mode%>
    
    newRow.appendChild(column0);
    newRow.appendChild(column1);
    
    newRow.appendChild(column1);
    tableBody.appendChild(newRow);
  }

  function addDetailsWSDLURLSingleItemRowBrowseActionLink(tableContainerId,wsdlType,column)
  {
    var browseLink = document.createElement("a");
    browseLink.href = "javascript:openWSDLBrowser('contentborder',"+wsdlType+")";
    browseLink.appendChild(document.createTextNode("<%=HTMLUtils.JSMangle(controller.getMessage("FORM_LINK_BROWSE"))%>"));
    column.appendChild(browseLink);    
  }
  
  function addDetailsWSDLURLSingleItemRowBrowseCancelActionLinks(tableContainerId,wsdlType,column)
  {
    addDetailsWSDLURLSingleItemRowBrowseActionLink(tableContainerId,wsdlType,column);
    column.appendChild(document.createTextNode(" "));
    var cancelLink = document.createElement("a");
    cancelLink.href = "javascript:cancelDetailsWSDLURLSingleItemRowEdit('"+tableContainerId+"')";
    cancelLink.appendChild(document.createTextNode("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_LINK_CANCEL"))%>"));
    column.appendChild(cancelLink);
  }
  
  function addDetailsWSDLURLSingleItemRowEditActionLink(tableContainerId,column)
  {
    var editLink = document.createElement("a");
    editLink.href = "javascript:editDetailsWSDLURLSingleItemRow('"+tableContainerId+"')";
    editLink.appendChild(document.createTextNode("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_LINK_EDIT"))%>"));
    column.appendChild(editLink);
  }
  
  function cancelDetailsWSDLURLSingleItemRowEdit(tableContainerId)
  {
    var table = getTable(tableContainerId);
    var columns = table.rows[numberOfHeaderRows].getElementsByTagName("td");
    var column0 = columns.item(0);
    var column1 = columns.item(1);
    
    column0.childNodes[2].value = false;
    
    var column0Control = column0.childNodes[column0.childNodes.length-1];
    column0.replaceChild(document.createTextNode(getWSDLURLDisplay(column0.childNodes[0].value)),column0Control);
    
    removeColumnChildren(column1);
    
    addDetailsWSDLURLSingleItemRowEditActionLink(tableContainerId,column1);
  }
  
  function editDetailsWSDLURLSingleItemRow(tableContainerId)
  {
    var table = getTable(tableContainerId);
    var columns = table.rows[numberOfHeaderRows].getElementsByTagName("td");
    var column0 = columns.item(0);
    var column1 = columns.item(1);
    
    var wsdlType = column0.childNodes[1].value;
    column0.childNodes[2].value = true;
    
    var column0Control = column0.childNodes[column0.childNodes.length-1];
    var wsdlURLEditControl = createWSDLURLEditControl();
    column0.replaceChild(wsdlURLEditControl,column0Control);
    wsdlURLEditControl.className = "tabletextenter";
    wsdlURLEditControl.value = getWSDLURLDisplay(column0.childNodes[0].value);
    
    removeColumnChildren(column1);
    
    addDetailsWSDLURLSingleItemRowBrowseCancelActionLinks(tableContainerId,wsdlType,column1);
  }
  
  function createWSDLURLEditControl()
  {
    var control;
    if (isMicrosoftInternetExplorer())
      control = document.createElement("<input name='<%=ActionInputs.QUERY_INPUT_WSDL_URL%>'>");
    else
    {
      control = document.createElement("input");
      control.setAttribute("name","<%=ActionInputs.QUERY_INPUT_WSDL_URL%>");
    }
    return control;
  }
  
  function getWSDLURLDisplay(wsdlURL)
  {
    if (wsdlURL.length < 1)
      wsdlURLDisplay = "<%=HTMLUtils.JSMangle(controller.getMessage("MSG_ERROR_WSDL_URL_IS_UNREACHABLE"))%>";
    else
      wsdlURLDisplay = wsdlURL;
    return wsdlURLDisplay;
  }
  
  function setDetailsWSDLURLSingleItemRow(tableContainerId,wsdlType,wsdlURL)
  {
    var table = getTable(tableContainerId);
    var columns = table.rows[numberOfHeaderRows].getElementsByTagName("td");
    var column0 = columns.item(0);
    var column1 = columns.item(1);

    column0.insertBefore(createHiddenElement("",wsdlType),column0.childNodes[1]);
    var editable = (column0.childNodes[2].value == "true");
    var wsdlURLDisplay = getWSDLURLDisplay(wsdlURL);
    column0.childNodes[0].value = wsdlURL;
    
    var column0Control;    
    if (editable)
    {
      column0Control = createWSDLURLEditControl();
      column0Control.className = "tabletextenter";
      addDetailsWSDLURLSingleItemRowBrowseActionLink(tableContainerId,wsdlType,column1);
    }
    else
    {
      column0Control = document.createTextNode(wsdlURLDisplay);
      addDetailsWSDLURLSingleItemRowEditActionLink(tableContainerId,column1);
    }
    column0.appendChild(column0Control);
    if (editable)
      column0Control.value = wsdlURLDisplay;
  }
  
  function addDetailsNameSingleItemRowCancelActionLink(tableContainerId,column)
  {
    var cancelLink = document.createElement("a");
    cancelLink.href = "javascript:cancelDetailsNameSingleItemRowEdit('"+tableContainerId+"')";
    cancelLink.appendChild(document.createTextNode("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_LINK_CANCEL"))%>"));
    column.appendChild(cancelLink);
  }

  function addDetailsNameSingleItemRowEditActionLink(tableContainerId,column)  
  {
    var editLink = document.createElement("a");
    editLink.href = "javascript:editDetailsNameSingleItemRow('"+tableContainerId+"')";
    editLink.appendChild(document.createTextNode("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_LINK_EDIT"))%>"));
    column.appendChild(editLink);
  }
  
  function editDetailsNameSingleItemRow(tableContainerId)
  {
    var table = getTable(tableContainerId);
    var columns = table.rows[numberOfHeaderRows].getElementsByTagName("td");
    var column0 = columns.item(0);
    var column1 = columns.item(1);
    
    column0.childNodes[1].value = true;
    
    var column0Control = column0.childNodes[column0.childNodes.length-1];
    var textInput = document.createElement("input");
    column0.replaceChild(textInput,column0Control);
    textInput.className = "tabletextenter";
    textInput.title = "<%=uddiPerspective.getMessage("FORM_CONTROL_TITLE_INPUT_TEXT_VALUE")%>";
    textInput.value = column0.childNodes[0].value;

    removeColumnChildren(column1);
    
    addDetailsNameSingleItemRowCancelActionLink(tableContainerId,column1);
  }
  
  function cancelDetailsNameSingleItemRowEdit(tableContainerId)
  {
    var table = getTable(tableContainerId);
    var columns = table.rows[numberOfHeaderRows].getElementsByTagName("td");
    var column0 = columns.item(0);
    var column1 = columns.item(1);
    
    column0.childNodes[1].value = false;
    
    var column0Control = column0.childNodes[column0.childNodes.length-1];
    column0.replaceChild(document.createTextNode(column0.childNodes[0].value),column0Control);
    
    for (var i=0;i<column1.childNodes.length;i++)
    {
      column1.removeChild(column1.childNodes[i]);
      i--;
    }
    addDetailsNameSingleItemRowEditActionLink(tableContainerId,column1);
  }
  
  function setDetailsNameSingleItemRow(tableContainerId,name)
  {
    var table = getTable(tableContainerId);
    var columns = table.rows[numberOfHeaderRows].getElementsByTagName("td");
    var column0 = columns.item(0);
    var column1 = columns.item(1);
    
    var editable = (column0.childNodes[1].value == "true");
    column0.childNodes[0].value = name;
    
    var column0Control;
    if (editable)
    {
      column0Control = document.createElement("input");
      column0Control.className = "tabletextenter";
      column0Control.title = "<%=uddiPerspective.getMessage("FORM_CONTROL_TITLE_INPUT_TEXT_VALUE")%>";
      column0Control.value = name;
      column1.appendChild(document.createTextNode("<%=uddiPerspective.getMessage("FORM_LABEL_ACTIONS_NONE")%>"));
    }
    else
    {
      column0Control = document.createTextNode(name);
      addDetailsNameSingleItemRowEditActionLink(tableContainerId,column1);
    }
    column0.appendChild(column0Control);
  }
  
  function processDetailsDiscoveryURLTable(tableContainerId,modifiedName,viewIdName,discoveryURLName,form)
  {
    var table = getTable(tableContainerId);
    for (var i=numberOfHeaderRows;i<table.rows.length;i++)
    {
      var columns = table.rows[i].getElementsByTagName("td");
      var column1 = columns.item(1);
      var viewId = column1.childNodes[0].value;
      var editable = (column1.childNodes[2].value == "true");
      var discoveryURL;
      if (editable)
        discoveryURL = column1.childNodes[3].value;
      else
        discoveryURL = column1.childNodes[1].value;
      form.appendChild(createHiddenElement(viewIdName,viewId));
      form.appendChild(createHiddenElement(discoveryURLName,discoveryURL));
      form.appendChild(createHiddenElement(modifiedName,editable));        
    }
  }
  
  function processDetailsLanguageInputTable(tableContainerId,modifiedName,viewIdName,langIdName,inputName,form)
  {
    var table = getTable(tableContainerId);
    for (var i=numberOfHeaderRows;i<table.rows.length;i++)
    {
      var columns = table.rows[i].getElementsByTagName("td");
      var column1 = columns.item(1);
      var viewId = column1.childNodes[0].value;
      var editable = (column1.childNodes[4].value == "true");
      var langId,inputText;
      if (editable)
      {
        langId = column1.childNodes[5].value;
        var column2 = columns.item(2);
        inputText = column2.childNodes[0].value;
      }
      else
      {
        langId = column1.childNodes[1].value;
        inputText = column1.childNodes[2].value;
      }
      form.appendChild(createHiddenElement(viewIdName,viewId));
      form.appendChild(createHiddenElement(langIdName,langId));
      form.appendChild(createHiddenElement(inputName,inputText));
      form.appendChild(createHiddenElement(modifiedName,editable));
    }
  }
  
  function processDetailsIdentifierTable(tableContainerId,form)
  {
    var table = getTable(tableContainerId);
    for (var i=numberOfHeaderRows;i<table.rows.length;i++)
    {
      var columns = table.rows[i].getElementsByTagName("td");
      var column1 = columns.item(1);
      var viewId = column1.childNodes[0].value;
      var editable = (column1.childNodes[4].value == "true");
      var tModelKey,idKeyName,idKeyValue;
      if (editable)
      {
        tModelKey = column1.childNodes[5].value;
        var column2 = columns.item(2);
        idKeyName = column2.childNodes[0].value;
        var column3 = columns.item(3);
        idKeyValue = column3.childNodes[0].value;
      }
      else
      {
        tModelKey = column1.childNodes[1].value;
        idKeyName = column1.childNodes[2].value;
        idKeyValue = column1.childNodes[3].value;
      }
      form.appendChild(createHiddenElement("<%=UDDIActionInputs.IDENTIFIER_VIEWID%>",viewId));
      form.appendChild(createHiddenElement("<%=UDDIActionInputs.QUERY_INPUT_ADVANCED_IDENTIFIER_TYPE%>",tModelKey));
      form.appendChild(createHiddenElement("<%=UDDIActionInputs.QUERY_INPUT_ADVANCED_IDENTIFIER_KEY_NAME%>",idKeyName));
      form.appendChild(createHiddenElement("<%=UDDIActionInputs.QUERY_INPUT_ADVANCED_IDENTIFIER_KEY_VALUE%>",idKeyValue));
      form.appendChild(createHiddenElement("<%=UDDIActionInputs.IDENTIFIER_MODIFIED%>",editable));      
    }
  }
  
  function processDetailsCategoryTable(tableContainerId,form)
  {
    var table = getTable(tableContainerId);
    for (var i=numberOfHeaderRows;i<table.rows.length;i++)
    {
      var columns = table.rows[i].getElementsByTagName("td");
      var column1 = columns.item(1);
      var viewId = column1.childNodes[0].value;
      var editable = (column1.childNodes[4].value == "true");
      var tModelKey,catKeyName,catKeyValue;
      if (editable)
      {
        tModelKey = column1.childNodes[5].value;
        var column2 = columns.item(2);
        catKeyName = column2.childNodes[0].value;
        var column3 = columns.item(3);
        catKeyValue = column3.childNodes[0].value;
      }
      else
      {
        tModelKey = column1.childNodes[1].value;
        catKeyName = column1.childNodes[2].value;
        catKeyValue = column1.childNodes[3].value;
      }
      form.appendChild(createHiddenElement("<%=UDDIActionInputs.CATEGORY_VIEWID%>",viewId));
      form.appendChild(createHiddenElement("<%=UDDIActionInputs.QUERY_INPUT_ADVANCED_CATEGORY_TYPE%>",tModelKey));
      form.appendChild(createHiddenElement("<%=UDDIActionInputs.QUERY_INPUT_ADVANCED_CATEGORY_KEY_NAME%>",catKeyName));
      form.appendChild(createHiddenElement("<%=UDDIActionInputs.QUERY_INPUT_ADVANCED_CATEGORY_KEY_VALUE%>",catKeyValue));
      form.appendChild(createHiddenElement("<%=UDDIActionInputs.CATEGORY_MODIFIED%>",editable));
    }
  }
  
  function processDetailsSingleItemTable(tableContainerId,modifiedName,inputName,form)
  {
    var table = getTable(tableContainerId);
    var columns = table.rows[numberOfHeaderRows].getElementsByTagName("td");
    var column0 = columns.item(0);
    var column1 = columns.item(1);
    var editable = (column0.childNodes[column0.childNodes.length-2].value == "true");
    var inputValue;
    if (editable)
      inputValue = column0.childNodes[column0.childNodes.length-1].value;
    else
      inputValue = column0.childNodes[0].value;
    form.appendChild(createHiddenElement(inputName,inputValue));
    form.appendChild(createHiddenElement(modifiedName,editable));    
  }
</script>
