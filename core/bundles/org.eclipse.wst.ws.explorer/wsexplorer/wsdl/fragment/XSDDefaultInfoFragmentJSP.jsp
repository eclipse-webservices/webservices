<%
/*******************************************************************************
 * Copyright (c) 2002, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
%>
<%@ page contentType="text/html; charset=UTF-8" import="org.eclipse.wst.ws.internal.explorer.platform.wsdl.perspective.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.wsdl.datamodel.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.perspective.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.util.*,
                                                        org.eclipse.emf.common.util.EList,
                                                        org.eclipse.xsd.*,
                                                        org.w3c.dom.Element,
                                                        org.w3c.dom.NodeList" %>

<jsp:useBean id="sessionID" class="java.lang.StringBuffer" scope="request"/>
<jsp:useBean id="fragID" class="java.lang.StringBuffer" scope="request"/>
<jsp:useBean id="nodeID" class="java.lang.StringBuffer" scope="request"/>

<%!
private static final String XMLNS_LANG = "xml:lang";
private static final String DEFAULT_XMLNS_LANG = "en-US";
%>

<%
HttpSession currentSession = (HttpSession)application.getAttribute(sessionID.toString());
Controller controller = (Controller)currentSession.getAttribute("controller");
WSDLPerspective wsdlPerspective = controller.getWSDLPerspective();
Node selectedNode = wsdlPerspective.getNodeManager().getNode(Integer.parseInt(nodeID.toString()));
WSDLOperationElement operElement = (WSDLOperationElement)selectedNode.getTreeElement();
IXSDFragment frag = operElement.getFragmentByID(fragID.toString());
XSDComponent xsdComponent = frag.getXSDToFragmentConfiguration().getXSDComponent();
XSDAnnotation annotation = null;
EList appInfoList = null;
EList docList = null;
if (xsdComponent instanceof XSDTypeDefinition)
  annotation = ((XSDTypeDefinition)xsdComponent).getAnnotation();
else if (xsdComponent instanceof XSDElementDeclaration)
  annotation = ((XSDElementDeclaration)xsdComponent).getAnnotation();
if (annotation != null) {
  appInfoList = annotation.getApplicationInformation();
  docList = annotation.getUserInformation();
}
String appInfoContainerID = "::appInfo";
String xappInfoContainerID = "x::appInfo";
String documentationContainerID = "::documentation";
String xdocumentationContainerID = "x::documentation";
%>

<script language="javascript">
  function twist(tableContainerId,twistImageName)
  {
    var tableContainer = document.getElementById(tableContainerId);
    var twistImage = document.images[twistImageName];
    if (tableContainer.style.display == "none")
    {
      tableContainer.style.display = "";
      twistImage.src = "<%=response.encodeURL(controller.getPathWithContext("images/twistopened.gif"))%>";
      twistImage.alt = "<%=HTMLUtils.JSMangle(controller.getMessage("ALT_TWIST_OPENED"))%>";
    }
    else
    {
      tableContainer.style.display = "none";
      twistImage.src = "<%=response.encodeURL(controller.getPathWithContext("images/twistclosed.gif"))%>";
      twistImage.alt = "<%=HTMLUtils.JSMangle(controller.getMessage("ALT_TWIST_CLOSED"))%>";
    }
  }

  function twistClose(tableContainerId)
  {
    if (document.getElementById(tableContainerId).style.display == "")
      twist(tableContainerId,"x"+tableContainerId);
  }
</script>

<table width="95%" border=0 cellpadding=3 cellspacing=0>
  <tr>
    <td height=25 valign="bottom" align="left" nowrap width=11>
      <a href="javascript:twist('<%=appInfoContainerID%>','<%=xappInfoContainerID%>')"><img name="<%=xappInfoContainerID%>" src="<%=response.encodeURL(controller.getPathWithContext("images/twistopened.gif"))%>" alt="<%=controller.getMessage("ALT_TWIST_OPENED")%>" class="twist"></a>
    </td>
    <td class="labels" height=25 valign="bottom" align="left" nowrap>
     <strong><%=wsdlPerspective.getMessage("FORM_LABEL_XSD_APPLICATION_INFORMATION")%></strong>
    </td>
  </tr>
</table>
<span id="<%=appInfoContainerID%>">
<table width="95%" cellpadding=3 cellspacing=0 class="tableborder">
  <tr>
    <th class="headercolor"><%=wsdlPerspective.getMessage("FORM_LABEL_LANGUAGE")%></th>
    <th class="headercolor" width="100%"><%=wsdlPerspective.getMessage("FORM_LABEL_XSD_APPLICATION_INFORMATION")%></th>
  </tr>
  <%
  boolean openAppInfoTable = false;
  if (appInfoList != null) {
    for (int i = 0; i < appInfoList.size(); i++) {
      Element e = (Element)appInfoList.get(i);
      String appInfoLang = e.getAttribute(XMLNS_LANG);
      String appInfoText = null;
      NodeList nl = e.getChildNodes();
      for (int j = 0; j < nl.getLength(); j++) {
        if (nl.item(j).getNodeType() == org.w3c.dom.Node.TEXT_NODE) {
          appInfoText = nl.item(j).getNodeValue();
          break;
        }
      }
      if (appInfoText != null)
      { 
        openAppInfoTable = true;
        if (appInfoText.trim().length() < 1)
          appInfoText = controller.getMessage("TABLE_BLANK_PLACEHOLDER");
  %>
  <tr>
    <td class="tablecells"><%=((appInfoLang != null && appInfoLang.length() > 0) ? appInfoLang : DEFAULT_XMLNS_LANG)%></td>
    <td class="tablecells"><%=appInfoText%></td>
  </tr>
  <%
      }
    }
  }
  %>
</table>
</span>
<%
if (!openAppInfoTable) {
%>
<script language="javascript">
  twistClose('<%=appInfoContainerID%>');
</script>
<%
}
%>

<table>
  <tr>
    <td height=20>&nbsp;</td>
  </tr>
</table>

<table width="95%" border=0 cellpadding=3 cellspacing=0>
  <tr>
    <td height=25 valign="bottom" align="left" nowrap width=11>
      <a href="javascript:twist('<%=documentationContainerID%>','<%=xdocumentationContainerID%>')"><img name="<%=xdocumentationContainerID%>" src="<%=response.encodeURL(controller.getPathWithContext("images/twistopened.gif"))%>" alt="<%=controller.getMessage("ALT_TWIST_OPENED")%>" class="twist"></a>
    </td>
    <td class="labels" height=25 valign="bottom" align="left" nowrap>
     <strong><%=wsdlPerspective.getMessage("FORM_LABEL_XSD_DOCUMENTATION")%></strong>
    </td>
  </tr>
</table>
<span id="<%=documentationContainerID%>">
<table width="95%" cellpadding=3 cellspacing=0 class="tableborder">
  <tr>
    <th class="headercolor"><%=wsdlPerspective.getMessage("FORM_LABEL_LANGUAGE")%></th>
    <th class="headercolor" width="100%"><%=wsdlPerspective.getMessage("FORM_LABEL_XSD_DOCUMENTATION")%></th>
  </tr>
  <%
  boolean openDocTable = false;
  if (docList != null) {
    for (int i = 0; i < docList.size(); i++) {
      Element e = (Element)docList.get(i);
      String docLang = e.getAttribute(XMLNS_LANG);
      String docText = null;
      NodeList nl = e.getChildNodes();
      for (int j = 0; j < nl.getLength(); j++) {
        if (nl.item(j).getNodeType() == org.w3c.dom.Node.TEXT_NODE) {
          docText = nl.item(j).getNodeValue();
          break;
        }
      }      
      if (docText != null)
      {
        openDocTable = true;
        if (docText.trim().length() < 1)
          docText = controller.getMessage("TABLE_BLANK_PLACEHOLDER");
  %>
  <tr>
    <td class="tablecells"><%=((docLang != null && docLang.length() > 0) ? docLang : DEFAULT_XMLNS_LANG)%></td>
    <td class="tablecells"><%=docText%></td>
  </tr>
  <%
      }
    }
  }
  %>
</table>
</span>
<%
if (!openDocTable) {
%>
<script language="javascript">
  twistClose('<%=documentationContainerID%>');
</script>
<%
}
%>
