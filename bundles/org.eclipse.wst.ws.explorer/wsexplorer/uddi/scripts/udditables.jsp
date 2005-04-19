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
                                                        org.eclipse.wst.ws.internal.explorer.platform.actions.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.util.*,
                                                        org.uddi4j.datatype.tmodel.*,
                                                        org.uddi4j.util.*,
                                                        java.util.*" %>

<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<jsp:include page="/scripts/wsdlbrowser.jsp" flush="true"/>
<jsp:include page="/scripts/tables.jsp" flush="true"/>
<jsp:include page="/scripts/formutils.jsp" flush="true"/>
<%
   String sessionId = session.getId();
   UDDIPerspective uddiPerspective = controller.getUDDIPerspective();
   NodeManager navigatorManager = uddiPerspective.getNavigatorManager();
   UDDIMainNode uddiMainNode = (UDDIMainNode)navigatorManager.getRootNode();
   RegistryNode regNode = uddiMainNode.getRegistryNode(navigatorManager.getSelectedNode());
   RegistryElement regElement = (RegistryElement)regNode.getTreeElement();
%>
<jsp:useBean id="commonCategoryModels" class="java.util.Hashtable" scope="application"/>
<script language="javascript">
  // Requires browserdetect.js, tables.jsp (explorer)
  var rowCheckboxName = "rowCheckboxName";
  var categoryWindow;
  var categoryWindowClosed = true;
  var targetCategoryRow = -1;
  var targetCategoryKeyNameElement;
  var targetCategoryKeyValueElement;
  var languageArray = new Array();
  var categoryArray = new Array();
  var wildcardCategoryBrowserURLRe = /(%)/;

  function closeAllUddiChildWindows()
  {
    closeCategoryBrowser();
    closeWSDLBrowser();
  }

  function Language(displayName,langId)
  {
    this.displayName = displayName;
    this.langId = langId;
  }

  function compareLanguageDisplayNames(a,b)
  {
    var result = a.displayName.localeCompare(b.displayName);
    if (result < 0)
      return -1;
    else if (result > 0)
      return 1;
    else
      return 0;
  }

  function setTModelKeySelect(itemSelect,tModelKey)
  {
    for (var i=0;i<itemSelect.options.length;i++)
    {
      if (itemSelect.options[i].value.toLowerCase() == tModelKey.toLowerCase())
      {
        itemSelect.options[i].selected = true;
        return itemSelect.options[i].text;
      }
    }
  }

  function setLanguageSelect(languageSelect,languageValue)
  {
    var start;
<%
   // Compare with the empty string only if the languageValue is the empty string.
%>
    if (languageValue.length < 1)
      start = 0;
    else
      start = 1;
    for (var i=start;i<languageSelect.options.length;i++)
    {
<%
   // Use a regular expression to check if languageValue begins with a key whose item is in the pick list. This enables "English" to be selected
   // for en-xxx (dialects).
%>
      var regExp = new RegExp("^"+languageSelect.options[i].value,"i");
      if (regExp.test(languageValue))
      {
        languageSelect.options[i].selected = true;
        return languageSelect.options[i].text;
      }
    }
  }

  function populateLanguageSelect(languageSelect)
  {
    if (languageArray.length == 0)
    {
      languageArray[languageArray.length] = new Language("","");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_EN"))%>","en");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_AA"))%>","aa");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_AB"))%>","ab");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_AF"))%>","af");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_AM"))%>","am");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_AR"))%>","ar");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_AS"))%>","as");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_AY"))%>","ay");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_AZ"))%>","az");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_BA"))%>","ba");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_BE"))%>","be");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_BG"))%>","bg");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_BH"))%>","bh");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_BI"))%>","bi");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_BN"))%>","bn");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_BO"))%>","bo");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_BR"))%>","br");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_CA"))%>","ca");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_CO"))%>","co");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_CZ"))%>","cz");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_CY"))%>","cy");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_DA"))%>","da");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_DE"))%>","de");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_DZ"))%>","dz");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_EL"))%>","el");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_EO"))%>","eo");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_ES"))%>","es");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_ET"))%>","et");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_EU"))%>","eu");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_FA"))%>","fa");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_FI"))%>","fi");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_FJ"))%>","fj");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_FO"))%>","fo");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_FR"))%>","fr");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_FY"))%>","fy");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_GA"))%>","ga");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_GD"))%>","gd");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_GL"))%>","gl");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_GN"))%>","gn");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_GU"))%>","gu");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_HA"))%>","ha");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_HI"))%>","hi");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_HR"))%>","hr");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_HU"))%>","hu");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_HY"))%>","hy");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_IA"))%>","ia");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_IE"))%>","ie");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_IK"))%>","ik");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_IN"))%>","in");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_IS"))%>","is");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_IT"))%>","it");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_IW"))%>","iw");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_JA"))%>","ja");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_JI"))%>","ji");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_JW"))%>","jw");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_KA"))%>","ka");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_KK"))%>","kk");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_KL"))%>","kl");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_KM"))%>","km");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_KN"))%>","kn");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_KO"))%>","ko");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_KS"))%>","ks");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_KU"))%>","ku");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_KY"))%>","ky");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_LA"))%>","la");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_LN"))%>","ln");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_LO"))%>","lo");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_LT"))%>","lt");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_LV"))%>","lv");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_MG"))%>","mg");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_MI"))%>","mi");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_MK"))%>","mk");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_ML"))%>","ml");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_MN"))%>","mn");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_MO"))%>","mo");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_MR"))%>","mr");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_MS"))%>","ms");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_MT"))%>","mt");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_MY"))%>","my");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_NA"))%>","na");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_NE"))%>","ne");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_NL"))%>","nl");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_NO"))%>","no");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_OC"))%>","oc");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_OM"))%>","om");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_OR"))%>","or");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_PA"))%>","pa");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_PL"))%>","pl");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_PS"))%>","ps");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_PT"))%>","pt");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_QU"))%>","qu");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_RM"))%>","rm");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_RN"))%>","rn");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_RO"))%>","ro");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_RU"))%>","ru");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_RW"))%>","rw");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_SA"))%>","sa");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_SD"))%>","sd");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_SG"))%>","sg");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_SH"))%>","sh");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_SI"))%>","si");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_SK"))%>","sk");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_SL"))%>","sl");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_SM"))%>","sm");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_SN"))%>","sn");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_SO"))%>","so");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_SQ"))%>","sq");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_SR"))%>","sr");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_SS"))%>","ss");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_ST"))%>","st")
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_SU"))%>","su");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_SV"))%>","sv");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_SW"))%>","sw");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_TA"))%>","ta");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_TE"))%>","te");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_TG"))%>","tg");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_TH"))%>","th");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_TI"))%>","ti");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_TK"))%>","tk");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_TL"))%>","tl");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_TN"))%>","tn");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_TO"))%>","to");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_TR"))%>","tr");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_TS"))%>","ts");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_TT"))%>","tt");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_TW"))%>","tw");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_UK"))%>","uk");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_UR"))%>","ur");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_UZ"))%>","uz");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_VI"))%>","vi");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_VO"))%>","vo");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_WO"))%>","wo");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_XH"))%>","xh");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_YO"))%>","yo");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_ZH"))%>","zh");
      languageArray[languageArray.length] = new Language("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_LANGUAGE_ZU"))%>","zu");
      languageArray.sort(compareLanguageDisplayNames);
    }

    for (var i=0;i<languageArray.length;i++)
      languageSelect.options[i] = new Option(languageArray[i].displayName,languageArray[i].langId);
  }

  function setLanguageInputRowSettings(tableContainerId,nameIndex,languageValue,nameTextValue)
  {
    var table = getTable(tableContainerId);
    var row = table.rows[numberOfHeaderRows+nameIndex];
    var languageSelect = row.getElementsByTagName("select").item(0);
    var nameText = row.getElementsByTagName("input").item(1);
    setLanguageSelect(languageSelect,languageValue);
    nameText.value = nameTextValue;
  }

  function removeSelectedCategoryRows(tableContainerId)
  {
    // Check if any rows above and including the targetCategoryRow are selected.
    var table = getTable(tableContainerId);
    var newTargetCategoryRow = targetCategoryRow;
    for (var i=numberOfHeaderRows;i<=targetCategoryRow;i++)
    {
      var tableRow = table.rows[i];
      var rowCheckbox = tableRow.getElementsByTagName("input").item(0);
      if (rowCheckbox.checked)
      {
        if (i == targetCategoryRow)
          newTargetCategoryRow = -1;
        else
          newTargetCategoryRow--;
      }
    }
    if (newTargetCategoryRow == -1)
      closeCategoryBrowser();
    targetCategoryRow = newTargetCategoryRow;
    removeSelectedRows(tableContainerId);
    // Fix the browse... links.
    for (var i=numberOfHeaderRows;i<table.rows.length;i++)
    {
      var tableRow = table.rows[i];
      var browseLink = tableRow.getElementsByTagName("a").item(0);
      setJSLinkRowTarget(browseLink,"openCategoryBrowser",tableContainerId,i);
    }
  }

  function addLanguageInputRow(tableContainerId,textControlTitle)
  {
    twistOpen(tableContainerId);
    var table = getTable(tableContainerId);
    var tableBody = table.getElementsByTagName("TBODY").item(0);
    var newRow = document.createElement("tr");
    var column0 = document.createElement("td");
    var column1 = document.createElement("td");
    var column2 = document.createElement("td");

    var rowCheckbox = createRowCheckbox();
    column0.appendChild(rowCheckbox);

    var languageSelect = document.createElement("select");
    populateLanguageSelect(languageSelect);
    column1.appendChild(languageSelect);

    var textInput = document.createElement("input");
    column2.appendChild(textInput);

    column0.className = "checkboxcells";
    languageSelect.className = "selectlist";
    column1.className = "tablecells";
    textInput.className = "tabletextenter";
    textInput.title = textControlTitle;
    column2.className = "tablecells";
    column2.width = "90%";
    newRow.appendChild(column0);
    newRow.appendChild(column1);
    newRow.appendChild(column2);
    tableBody.appendChild(newRow);
  }

  function setIdentifierRowSettings(tableContainerId,identifierIndex,tModelKey,keyName,keyValue)
  {
    var table = getTable(tableContainerId);
    var row = table.rows[numberOfHeaderRows+identifierIndex];
    var identifierTypeSelect = row.getElementsByTagName("select").item(0);
    var rowElementCollection = row.getElementsByTagName("input");
    var keyNameText = rowElementCollection.item(1);
    var keyValueText = rowElementCollection.item(2);
    setTModelKeySelect(identifierTypeSelect,tModelKey);
    keyNameText.value = keyName;
    keyValueText.value = keyValue;
  }

  function populateIdentifierTypeSelect(identifierTypeSelect)
  {
    var counter = 0;
    identifierTypeSelect.options[counter++] = new Option("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_IDENTIFIER_DUNS"))%>","<%=TModel.D_U_N_S_TMODEL_KEY%>");
    identifierTypeSelect.options[counter++] = new Option("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_OPTION_IDENTIFIER_THOMAS_REGISTRY"))%>","<%=TModel.THOMAS_REGISTER_TMODEL_KEY%>");
  }

  function handleIdentifierChange(e)
  {
    var identifierSelect;
    if (isMicrosoftInternetExplorer())
      identifierSelect = event.srcElement;
    else
      identifierSelect = e.target;
    var cell = identifierSelect.parentNode;
    var row = cell.parentNode;
    var inputElements = row.getElementsByTagName("input");
    for (var i=0;i<inputElements.length;i++)
    {
      if (inputElements.item(i).type == "text")
      {
        inputElements.item(i).value = identifierSelect.options[identifierSelect.selectedIndex].text;
        break;
      }
    }
  }

  function addIdentifierRow(tableContainerId)
  {
    twistOpen(tableContainerId);
    var table = getTable(tableContainerId);
    var tableBody = table.getElementsByTagName("TBODY").item(0);
    var newRow = document.createElement("tr");
    var column0 = document.createElement("td");
    var column1 = document.createElement("td");
    var column2 = document.createElement("td");
    var column3 = document.createElement("td");

    var rowCheckbox = createRowCheckbox();
    column0.appendChild(rowCheckbox);

    var keyTypeSelect = document.createElement("select");
    keyTypeSelect.onchange = handleIdentifierChange;
    populateIdentifierTypeSelect(keyTypeSelect);
    column1.appendChild(keyTypeSelect);

    var keyNameTextInput = document.createElement("input");
    column2.appendChild(keyNameTextInput);

    var keyValueTextInput = document.createElement("input");
    column3.appendChild(keyValueTextInput);

    column0.className = "checkboxcells";
    keyTypeSelect.className = "selectlist"
    column1.className = "tablecells";
    column1.width = "5%";
    keyNameTextInput.className = "tabletextenter";
    keyNameTextInput.title = "<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_CONTROL_TITLE_IDENTIFIER_KEY_NAME"))%>";
    column2.className = "tablecells";
    column2.width = "40%";
    keyValueTextInput.className = "tabletextenter";
    keyValueTextInput.title = "<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_CONTROL_TITLE_IDENTIFIER_KEY_VALUE"))%>";
    column3.className = "tablecells";
    column3.width = "55%";

    newRow.appendChild(column0);
    newRow.appendChild(column1);
    newRow.appendChild(column2);
    newRow.appendChild(column3);
    tableBody.appendChild(newRow);
    keyNameTextInput.value = keyTypeSelect.options[0].text;
  }
  
  function Category(displayName,tModelKey)
  {
    this.displayName = displayName;
    this.tModelKey = tModelKey;
  }

  function compareCategoryDisplayNames(a,b)
  {
    var result = a.displayName.localeCompare(b.displayName);
    if (result < 0)
      return -1;
    else if (result > 0)
      return 1;
    else
      return 0;
  }  

  function populateCategoryTypeSelect(categoryTypeSelect)
  {
    if (categoryArray.length == 0)
    {
<%
   Enumeration commonCategories = commonCategoryModels.elements();
   while (commonCategories.hasMoreElements())
   {
     CategoryModel commonCategoryModel = (CategoryModel)commonCategories.nextElement();
%>
      categoryArray[categoryArray.length] = new Category("<%=HTMLUtils.JSMangle(commonCategoryModel.getDisplayName())%>","<%=commonCategoryModel.getTModelKey()%>");
<%
   }
   Enumeration userDefinedCategories = regElement.getUserDefinedCategories();
   if (userDefinedCategories != null)
   {
     while (userDefinedCategories.hasMoreElements())
     {
       CategoryModel userDefinedCategory = (CategoryModel)userDefinedCategories.nextElement();
%>
      categoryArray[categoryArray.length] = new Category("<%=HTMLUtils.JSMangle(userDefinedCategory.getDisplayName())%>","<%=userDefinedCategory.getTModelKey()%>");
<%
     }
   }
%>
      categoryArray.sort(compareCategoryDisplayNames);
    }
    
    for (var i=0;i<categoryArray.length;i++)
      categoryTypeSelect.options[i] = new Option(categoryArray[i].displayName,categoryArray[i].tModelKey);
  }

  function setJSLinkRowTarget(link,jsFunction,tableContainerId,row)
  {
    link.href = "javascript:"+jsFunction+"('"+tableContainerId+"',"+row+")";
  }

  function openCategoryBrowser(tableContainerId,row)
  {
    var table = getTable(tableContainerId);
    var categorySelect = table.rows[row].getElementsByTagName("select").item(0);
    var selectedCategoryOption = categorySelect.options[categorySelect.options.selectedIndex];
    var categoryType = table.rows[row].getElementsByTagName("select").item(0).options.selectedIndex;
    var columns = table.rows[row].getElementsByTagName("td");
    targetCategoryRow = row;
    targetCategoryKeyNameElement = columns.item(2).childNodes[0];
    targetCategoryKeyValueElement = columns.item(3).childNodes[0];
    var link = "<%=response.encodeURL(controller.getPathWithContext(OpenCategoryBrowserAction.getWildCardActionLink(sessionId)))%>";
    categoryWindow = window.open(link.replace(wildcardCategoryBrowserURLRe,selectedCategoryOption.value),"categoryWindow","height=300,width=300,status=yes,scrollbars=yes,resizable=yes");
    if (categoryWindow.focus)
      categoryWindow.focus();
  }

  function closeCategoryBrowser()
  {
    if (!categoryWindowClosed)
      categoryWindow.close();
  }

  function setCategoryRowSettings(tableContainerId,categoryIndex,tModelKey,keyName,keyValue)
  {
    var table = getTable(tableContainerId);
    var row = table.rows[numberOfHeaderRows+categoryIndex];
    var categoryTypeSelect = row.getElementsByTagName("select").item(0);
    var rowElementCollection = row.getElementsByTagName("input");
    var keyNameText = rowElementCollection.item(1);
    var keyValueText = rowElementCollection.item(2);
    setTModelKeySelect(categoryTypeSelect,tModelKey);
    keyNameText.value = keyName;
    keyValueText.value = keyValue;
  }

  function addCategoryRowBrowseLink(tableContainerId,column,rowIndex)
  {
    var browseLink = document.createElement("a");
    setJSLinkRowTarget(browseLink,"openCategoryBrowser",tableContainerId,rowIndex);
    browseLink.appendChild(document.createTextNode("<%=HTMLUtils.JSMangle(controller.getMessage("FORM_LINK_BROWSE"))%>"));
    column.appendChild(browseLink);
  }

  function addCategoryRow(tableContainerId)
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

    var categoryTypeSelect = document.createElement("select");
    populateCategoryTypeSelect(categoryTypeSelect);
    column1.appendChild(categoryTypeSelect);

    var keyNameTextInput = document.createElement("input");
    column2.appendChild(keyNameTextInput);

    var keyValueTextInput = document.createElement("input");
    column3.appendChild(keyValueTextInput);

    addCategoryRowBrowseLink(tableContainerId,column4,table.rows.length);

    column0.className = "checkboxcells";
    categoryTypeSelect.className = "selectlist";
    column1.className = "tablecells";
    column1.width = "5%";
    keyNameTextInput.className = "tabletextenter";
    keyNameTextInput.title = "<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_CONTROL_TITLE_CATEGORY_KEY_NAME"))%>";
    column2.className = "tablecells";
    column2.width = "55%";
    keyValueTextInput.className = "tabletextenter";
    keyValueTextInput.title = "<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_CONTROL_TITLE_CATEGORY_KEY_VALUE"))%>";
    column3.className = "tablecells";
    column3.width = "25%";
    column4.className = "tablecells";

    newRow.appendChild(column0);
    newRow.appendChild(column1);
    newRow.appendChild(column2);
    newRow.appendChild(column3);
    newRow.appendChild(column4);
    tableBody.appendChild(newRow);
  }

  function setDiscoveryURLRowSettings(tableContainerId,discoveryURLIndex,discoveryURL)
  {
    var table = getTable(tableContainerId);
    var row = table.rows[numberOfHeaderRows+discoveryURLIndex];
    var rowElementCollection = row.getElementsByTagName("input");
    var discoveryURLTextInput = rowElementCollection.item(1);
    discoveryURLTextInput.value = discoveryURL;
  }

  function addDiscoveryURLRow(tableContainerId)
  {
    twistOpen(tableContainerId);
    var table = getTable(tableContainerId);
    var tableBody = table.getElementsByTagName("TBODY").item(0);
    var newRow = document.createElement("tr");
    var column0 = document.createElement("td");
    var column1 = document.createElement("td");

    var rowCheckbox = createRowCheckbox();
    column0.appendChild(rowCheckbox);

    var discoveryURLTextInput = document.createElement("input");
    column1.appendChild(discoveryURLTextInput);

    column0.className = "checkboxcells";
    discoveryURLTextInput.className = "tabletextenter";
    discoveryURLTextInput.title = "<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("FORM_CONTROL_TITLE_DISCOVERY_URL"))%>";
    column1.className = "tablecells";
    column1.width = "95%";

    newRow.appendChild(column0);
    newRow.appendChild(column1);
    tableBody.appendChild(newRow);
  }

  function addResultRow(tableContainerId,nodeId,url,name,description)
  {
    twistOpen(tableContainerId);
    var table = getTable(tableContainerId);
    var tableBody = table.getElementsByTagName("TBODY").item(0);
    var newRow = document.createElement("tr");
    var column0 = document.createElement("td");
    var column1 = document.createElement("td");
    var column2 = document.createElement("td");

    var rowCheckbox = createRowCheckbox();
    column0.appendChild(rowCheckbox);
    column0.appendChild(createHiddenElement("",nodeId));

    var nameDetailsLink = document.createElement("a");
    nameDetailsLink.href = url;
    nameDetailsLink.target = "<%=FrameNames.PERSPECTIVE_WORKAREA%>";
    nameDetailsLink.appendChild(document.createTextNode(name));
    column1.appendChild(nameDetailsLink);

    column2.appendChild(document.createTextNode(getDefaultDisplayString(description)));

    column0.className = "checkboxcells";
    column1.className = "tablecells";
    column2.className = "tablecells";
    column1.width = "48%";
    column2.width = "48%";
    newRow.appendChild(column0);
    newRow.appendChild(column1);
    newRow.appendChild(column2);
    tableBody.appendChild(newRow);
  }

  function getFindQualifierTable(containerId)
  {
    var container = document.getElementById(containerId);
    return container.getElementsByTagName("table").item(1);
  }

  function setFindQualifier(findQualifier,nameTableContainerId,categoryTableContainerId,findQualifiersTableContainerId)
  {
    var twistOpenNames = false;
    var twistOpenCategories = false;
    var twistOpenFindQualifiers = false;

    if (findQualifier == "<%=FindQualifier.exactNameMatch%>")
    {
      var fqTable = getFindQualifierTable(nameTableContainerId);
      var exactNameMatchCheckbox = fqTable.getElementsByTagName("input").item(0);
      exactNameMatchCheckbox.checked = true;
      twistOpenNames = true;
    }
    else if (findQualifier == "<%=FindQualifier.caseSensitiveMatch%>")
    {
      var fqTable = getFindQualifierTable(nameTableContainerId);
      var caseSensitiveMatchCheckbox = fqTable.getElementsByTagName("input").item(1);
      caseSensitiveMatchCheckbox.checked = true;
      twistOpenNames = true;
    }
    else if (findQualifier == "<%=FindQualifier.serviceSubset%>" || findQualifier == "<%=FindQualifier.combineCategoryBags%>")
    {
      var fqTable = getFindQualifierTable(categoryTableContainerId);
      var categorySelect = fqTable.getElementsByTagName("select").item(0);
      setSelect(categorySelect,findQualifier);
      twistOpenCategories = true;
    }
    else if (findQualifier == "<%=FindQualifier.andAllKeys%>" || findQualifier == "<%=FindQualifier.orAllKeys%>" || findQualifier == "<%=FindQualifier.orLikeKeys%>")
    {
      var fqTable = getTable(findQualifiersTableContainerId);
      var keySelect = fqTable.getElementsByTagName("select").item(0);
      setSelect(keySelect,findQualifier);
      if (findQualifier != "<%=FindQualifier.andAllKeys%>")
        twistOpenFindQualifiers = true;
    }
    else if (findQualifier == "<%=FindQualifier.sortByNameAsc%>" || findQualifier == "<%=FindQualifier.sortByNameDesc%>" || findQualifier == "<%=FindQualifier.sortByDateAsc%>" || findQualifier == "<%=FindQualifier.sortByDateDesc%>")
    {
      var fqTable = getTable(findQualifiersTableContainerId);
      var sortCollection = fqTable.getElementsByTagName("input");
      for (var i=0;i<sortCollection.length;i++)
      {
        if (sortCollection.item(i).value == findQualifier)
          sortCollection.item(i).checked = true;
      }
      if (findQualifier != "<%=FindQualifier.sortByNameAsc%>" && findQualifier != "<%=FindQualifier.sortByDateAsc%>")
        twistOpenFindQualifiers = true;
    }

    if (twistOpenNames)
      twistOpen(nameTableContainerId);
    if (twistOpenCategories)
      twistOpen(categoryTableContainerId);
    if (twistOpenFindQualifiers)
      twistOpen(findQualifiersTableContainerId);
  }

  function processLanguageInputTable(tableContainerId,langVar,textVar,form)
  {
    var table = getTable(tableContainerId);
    var languageSelects = table.getElementsByTagName("select");
    var textInputs = table.getElementsByTagName("input");
    for (var i=0;i<languageSelects.length;i++)
    {
      var hiddenLanguageSelect = createHiddenElement(langVar,languageSelects[i].value);
      form.appendChild(hiddenLanguageSelect);
    }

    for (var i=0;i<textInputs.length;i++)
    {
      if (textInputs[i].type == "text")
      {
        var hiddenTextInput = createHiddenElement(textVar,textInputs[i].value);
        form.appendChild(hiddenTextInput);
      }
    }
  }

  function processNameFindQualifiers(tableContainerId,form)
  {
    var table = getFindQualifierTable(tableContainerId);
    var nameFindQualifierCheckboxes = table.getElementsByTagName("input");
    for (var i=0;i<nameFindQualifierCheckboxes.length;i++)
    {
      var item = nameFindQualifierCheckboxes.item(i);
      if (item.checked)
      {
        var hiddenFindQualifier = createHiddenElement("<%=UDDIActionInputs.QUERY_INPUT_ADVANCED_FINDQUALIFIER%>",item.value);
        form.appendChild(hiddenFindQualifier);
      }
    }
  }

  function processIdentifierTable(tableContainerId,form)
  {
    var table = getTable(tableContainerId);
    var identifierTypeSelects = table.getElementsByTagName("select");
    var textInputs = table.getElementsByTagName("input");
    for (var i=0;i<identifierTypeSelects.length;i++)
    {
      var hiddenIdentifierTypeSelect = createHiddenElement("<%=UDDIActionInputs.QUERY_INPUT_ADVANCED_IDENTIFIER_TYPE%>",identifierTypeSelects[i].value);
      form.appendChild(hiddenIdentifierTypeSelect);
    }

    var isKeyName = true;
    for (var i=0;i<textInputs.length;i++)
    {
      if (textInputs[i].type == "text")
      {
        var name;
        if (isKeyName)
          name = "<%=UDDIActionInputs.QUERY_INPUT_ADVANCED_IDENTIFIER_KEY_NAME%>";
        else
          name = "<%=UDDIActionInputs.QUERY_INPUT_ADVANCED_IDENTIFIER_KEY_VALUE%>";
        var hiddenTextInput = createHiddenElement(name,textInputs[i].value);
        form.appendChild(hiddenTextInput);
        isKeyName = !isKeyName;
      }
    }
  }

  function processCategoryTable(tableContainerId,form,isFindBusiness)
  {
    var table = getTable(tableContainerId);
    var categoryTypeSelects = table.getElementsByTagName("select");
    var textInputs = table.getElementsByTagName("input");
    for (var i=0;i<categoryTypeSelects.length;i++)
    {
      var hiddenCategoryTypeSelect = createHiddenElement("<%=UDDIActionInputs.QUERY_INPUT_ADVANCED_CATEGORY_TYPE%>",categoryTypeSelects[i].value);
      form.appendChild(hiddenCategoryTypeSelect);
    }

    var isKeyName = true;
    for (var i=0;i<textInputs.length;i++)
    {
      if (textInputs[i].type == "text")
      {
        var name;
        if (isKeyName)
          name = "<%=UDDIActionInputs.QUERY_INPUT_ADVANCED_CATEGORY_KEY_NAME%>";
        else
          name = "<%=UDDIActionInputs.QUERY_INPUT_ADVANCED_CATEGORY_KEY_VALUE%>";
        var hiddenTextInput = createHiddenElement(name,textInputs[i].value);
        form.appendChild(hiddenTextInput);
        isKeyName = !isKeyName;
      }
    }

    if (isFindBusiness)
    {
      table = getFindQualifierTable(tableContainerId);
      var categoryFindQualifier = table.getElementsByTagName("select").item(0);
      if (categoryFindQualifier.selectedIndex != 0)
      {
        var hiddenFindQualifier = createHiddenElement("<%=UDDIActionInputs.QUERY_INPUT_ADVANCED_FINDQUALIFIER%>",categoryFindQualifier.value);
        form.appendChild(hiddenFindQualifier);
      }
    }
  }

  function processDiscoveryURLTable(tableContainerId,form)
  {
    var table = getTable(tableContainerId);
    var textInputs = table.getElementsByTagName("input");
    for (var i=0;i<textInputs.length;i++)
    {
      if (textInputs[i].type == "text")
      {
        var hiddenTextInput = createHiddenElement("<%=UDDIActionInputs.QUERY_INPUT_ADVANCED_DISCOVERYURL%>",textInputs[i].value);
        form.appendChild(hiddenTextInput);
      }
    }
  }

  function processFindQualifierTable(tableContainerId,form)
  {
    var table = getTable(tableContainerId);
    var findQualifierSelects = table.getElementsByTagName("select");
    for (var i=0;i<findQualifierSelects.length;i++)
    {
      var findQualifierSelect = findQualifierSelects.item(i);
      var hiddenFindQualifier = createHiddenElement("<%=UDDIActionInputs.QUERY_INPUT_ADVANCED_FINDQUALIFIER%>",findQualifierSelect.value);
      form.appendChild(hiddenFindQualifier);
    }

    var findQualifierSortRadios = table.getElementsByTagName("input");
    for (var i=0;i<findQualifierSortRadios.length;i++)
    {
      if (findQualifierSortRadios.item(i).checked)
      {
        var hiddenSortElement = createHiddenElement("<%=UDDIActionInputs.QUERY_INPUT_ADVANCED_FINDQUALIFIER%>",findQualifierSortRadios.item(i).value);
        form.appendChild(hiddenSortElement);
      }
    }
  }

  function processResultTable(tableContainerId,inputName,form,selectedOnly)
  {
    var table = getTable(tableContainerId);
    for (var i=numberOfHeaderRows;i<table.rows.length;i++)
    {
      var inputs = table.rows[i].getElementsByTagName("input");
      if (selectedOnly)
      {
        if (inputs.item(0).checked)
          form.appendChild(createHiddenElement(inputName,inputs.item(1).value));
      }
      else
        form.appendChild(createHiddenElement(inputName,inputs.item(1).value));
    }
  }

  function initiateSubQuery(formContainerId,newSubQueryKey,queryItem,isGet)
  {
    if (isGet)
    {
      var hasGetItems = false;
      switch (queryItem)
      {
        case <%=UDDIActionInputs.QUERY_ITEM_BUSINESSES%>:
          hasGetItems = <%=regNode.hasBusiness()%>;
          break;
        case <%=UDDIActionInputs.QUERY_ITEM_SERVICES%>:
          hasGetItems = <%=regNode.hasService()%>;
          break;
        case <%=UDDIActionInputs.QUERY_ITEM_SERVICE_INTERFACES%>:
        default:
          hasGetItems = <%=regNode.hasServiceInterface()%>;
      }
      if (!hasGetItems)
      {
        alert("<%=HTMLUtils.JSMangle(uddiPerspective.getMessage("MSG_ERROR_NO_ITEMS_TO_GET"))%>");
        return;
      }
    }
    var formContainer = document.getElementById(formContainerId);
    var form = formContainer.getElementsByTagName("form").item(0);
    form.<%=UDDIActionInputs.SUBQUERY_GET%>.value = isGet;
    form.<%=UDDIActionInputs.NEW_SUBQUERY_INITIATED%>.value = newSubQueryKey;
    form.<%=UDDIActionInputs.NEW_SUBQUERY_QUERY_ITEM%>.value = queryItem;
    var okToSubmit = false;
    if (formContainerId == "findBusinessesAdvanced")
      okToSubmit = processFindBusinessesAdvancedForm(form);
    else if (formContainerId == "findServicesAdvanced")
      okToSubmit = processFindServicesAdvancedForm(form);
    else if (formContainerId == "findServiceInterfacesAdvanced")
      okToSubmit = processFindServiceInterfacesAdvancedForm(form);
    else if (formContainerId == "publishServiceSimple")
      okToSubmit = processPublishServiceSimpleForm(form);
    else if (formContainerId == "publishServiceAdvanced")
      okToSubmit = processPublishServiceAdvancedForm(form);
    else if (formContainerId == "publisherAssertions")
      okToSubmit = handleSubmit(form);
    else if (formContainerId == "manageReferencedServices")
      okToSubmit = processForm(form);
    if (okToSubmit)
      form.submit();
  }
</script>
