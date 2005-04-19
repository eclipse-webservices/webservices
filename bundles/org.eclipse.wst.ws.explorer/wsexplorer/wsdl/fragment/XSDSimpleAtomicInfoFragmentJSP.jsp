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
                                                        org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.util.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.perspective.*,
                                                        org.eclipse.emf.common.util.EList,
                                                        org.eclipse.xsd.*,
                                                        java.util.*" %>

<jsp:useBean id="sessionID" class="java.lang.StringBuffer" scope="request"/>
<jsp:useBean id="fragID" class="java.lang.StringBuffer" scope="request"/>
<jsp:useBean id="nodeID" class="java.lang.StringBuffer" scope="request"/>
<%
HttpSession currentSession = (HttpSession)application.getAttribute(sessionID.toString());
Controller controller = (Controller)currentSession.getAttribute("controller");
WSDLPerspective wsdlPerspective = controller.getWSDLPerspective();
Node selectedNode = wsdlPerspective.getNodeManager().getNode(Integer.parseInt(nodeID.toString()));
WSDLOperationElement operElement = (WSDLOperationElement)selectedNode.getTreeElement();
IXSDFragment frag = operElement.getFragmentByID(fragID.toString());
XSDSimpleTypeDefinition simpleType = (XSDSimpleTypeDefinition)frag.getXSDToFragmentConfiguration().getXSDComponent();

int minLength = -1;
int maxLength = -1;
Vector patternVector = new Vector();
String whiteSpace = null;
boolean minExclusive = true;
Object min = null;
boolean maxExclusive = true;
Object max = null;
int totalDigits = -1;
int fractionDigits = -1;

XSDSimpleTypeDefinition currType = simpleType;
while (!XSDTypeDefinitionUtil.isRootTypeDefinition(currType)) {
  // minLength
  if (minLength < 0) {
    XSDLengthFacet xsdLengthFacet = currType.getLengthFacet();
    XSDMinLengthFacet xsdMinLengthFacet = currType.getMinLengthFacet();
    // port to org.eclipse.xsd
    if (xsdLengthFacet != null)
      minLength = xsdLengthFacet.getValue();
    else if (xsdMinLengthFacet != null)
      minLength = xsdMinLengthFacet.getValue();
  }
  // maxLength
  if (maxLength < 0) {
    XSDLengthFacet xsdLengthFacet = currType.getLengthFacet();
    XSDMaxLengthFacet xsdMaxLengthFacet = currType.getMaxLengthFacet();
    // port to org.eclipse.xsd
    if (xsdLengthFacet != null)
      maxLength = xsdLengthFacet.getValue();
    else if (xsdMaxLengthFacet != null)
      maxLength = xsdMaxLengthFacet.getValue();
  }
  // patternVector
  EList xsdPatternFacets = currType.getPatternFacets();
  if (xsdPatternFacets != null) {
    for (int i = 0; i < xsdPatternFacets.size(); i++) {
      XSDPatternFacet xsdPatternFacet = (XSDPatternFacet)xsdPatternFacets.get(i);
      EList patterns = xsdPatternFacet.getValue();
      for (int j = 0; j < patterns.size(); j++) {
        patternVector.add(patterns.get(j));
      }
    }
  }
  // whiteSpace
  if (whiteSpace == null) {
    XSDWhiteSpaceFacet xsdWhiteSpaceFacet = currType.getWhiteSpaceFacet();
    // port to org.eclipse.xsd
    if (xsdWhiteSpaceFacet != null)
      whiteSpace = xsdWhiteSpaceFacet.getValue().getName();
  }
  // min in/exclusive
  if (min == null ) {
    XSDMinExclusiveFacet xsdMinExclusiveFacet = currType.getMinExclusiveFacet();
    XSDMinInclusiveFacet xsdMinInclusiveFacet = currType.getMinInclusiveFacet();
    if (xsdMinExclusiveFacet != null) {
      min = xsdMinExclusiveFacet.getValue();
      minExclusive = true;
    }
    else if (xsdMinInclusiveFacet != null) {
      min = xsdMinInclusiveFacet.getValue();
      minExclusive = false;
    }
  }
  // max in/exclusive
  if (max == null) {
    XSDMaxExclusiveFacet xsdMaxExclusiveFacet = currType.getMaxExclusiveFacet();
    XSDMaxInclusiveFacet xsdMaxInclusiveFacet = currType.getMaxInclusiveFacet();
    if (xsdMaxExclusiveFacet != null) {
      max = xsdMaxExclusiveFacet.getValue();
      maxExclusive = true;
    }
    else if (xsdMaxInclusiveFacet != null) {
      max = xsdMaxInclusiveFacet.getValue();
      maxExclusive = false;
    }
  }
  // totalDigits
  if (totalDigits < 0) {
    XSDTotalDigitsFacet xsdTotalDigitsFacet = currType.getTotalDigitsFacet();
    if (xsdTotalDigitsFacet != null)
      totalDigits = xsdTotalDigitsFacet.getValue();
  }
  // fractionDigits
  if (fractionDigits < 0) {
    XSDFractionDigitsFacet xsdFractionDigitsFacet = currType.getFractionDigitsFacet();
    if (xsdFractionDigitsFacet != null)
      fractionDigits = xsdFractionDigitsFacet.getValue();
  }
  // walk to base type
  currType = currType.getBaseTypeDefinition();
}
%>

<jsp:include page="/wsdl/fragment/XSDDefaultInfoFragmentJSP.jsp" flush="true"/>

<table>
  <tr>
    <td height=20>&nbsp;</td>
  </tr>
</table>
<table width="95%" border=0 cellpadding=3 cellspacing=0 class="tableborder">
  <tr>
    <th class="singleheadercolor" height=20 valign="bottom" align="left">
      <%=wsdlPerspective.getMessage("FORM_LABEL_BASE_TYPE")%>
    </th>
  </tr>
  <tr>
    <td class="tablecells">
      <%=XSDTypeDefinitionUtil.resolveToXSDBuiltInTypeDefinition(simpleType).getName()%>
    </td>
  </tr>
</table>

<%
String minLengthString = (minLength < 0) ? wsdlPerspective.getMessage("FORM_LABEL_UNBOUNDED") : String.valueOf(minLength);
String maxLengthString = (maxLength < 0) ? wsdlPerspective.getMessage("FORM_LABEL_UNBOUNDED") : String.valueOf(maxLength);
String[] lengthValues = {minLengthString, maxLengthString};
if (minLength >= 0 || maxLength >= 0) {
%>
  <table>
    <tr>
      <td height=20>&nbsp;</td>
    </tr>
  </table>
  <table width="95%" border=0 cellpadding=3 cellspacing=0 class="tableborder">
    <tr>
      <th class="singleheadercolor" height=20 valign="bottom" align="left">
        <%=wsdlPerspective.getMessage("FORM_LABEL_LENGTH")%>
      </th>
    </tr>
    <tr>
      <td class="tablecells">
        <%=wsdlPerspective.getMessage("FORM_LABEL_LENGTH_VALUE", lengthValues)%>
      </td>
    </tr>
  </table>
<%
}
%>

<%
if (patternVector.size() > 0) {
  String patternContainerID = "::pattern";
  String xpatternContainerID = "x::pattern";
%>
  <table>
    <tr>
      <td height=20>&nbsp;</td>
    </tr>
  </table>
  <table width="95%" border=0 cellpadding=3 cellspacing=0>
    <tr>
      <td height=25 valign="bottom" align="left" nowrap width=11>
        <a href="javascript:twist('<%=patternContainerID%>','<%=xpatternContainerID%>')"><img name="<%=xpatternContainerID%>" src="<%=response.encodeURL(controller.getPathWithContext("images/twistopened.gif"))%>" alt="<%=controller.getMessage("ALT_TWIST_OPENED")%>" class="twist"></a>
      </td>
      <td class="labels" height=25 valign="bottom" align="left" nowrap>
       <strong><%=wsdlPerspective.getMessage("FORM_LABEL_PATTERN")%></strong>
      </td>
    </tr>
  </table>
  <span id="<%=patternContainerID%>">
  <table width="95%" border=0 cellpadding=3 cellspacing=0 class="tableborder">
    <tr>
      <th class="singleheadercolor" height=20 valign="bottom" align="left">
        <%=wsdlPerspective.getMessage("FORM_LABEL_PATTERN")%>
      </th>
    </tr>
    <%
    for (int k = 0; k < patternVector.size(); k++) {
    %>
      <tr>
        <td class="tablecells">
          <%=(String)patternVector.get(k)%>
        </td>
      </tr>
    <%
    }
    %>
  </table>
  </span>
<%
}
%>

<%
if (whiteSpace != null) {
%>
  <table>
    <tr>
      <td height=20>&nbsp;</td>
    </tr>
  </table>
  <table width="95%" border=0 cellpadding=3 cellspacing=0 class="tableborder">
    <tr>
      <th class="singleheadercolor" height=20 valign="bottom" align="left">
        <%=wsdlPerspective.getMessage("FORM_LABEL_WHITESPACE")%>
      </th>
    </tr>
    <tr>
      <td class="tablecells">
        <%=whiteSpace%>
      </td>
    </tr>
  </table>
<%
}
%>

<%
String lowerBound;
if (minExclusive)
  lowerBound = wsdlPerspective.getMessage("FORM_LABEL_MIN_EXCLUSIVE", (min == null) ? wsdlPerspective.getMessage("FORM_LABEL_UNBOUNDED") : min.toString());
else
  lowerBound = wsdlPerspective.getMessage("FORM_LABEL_MIN_INCLUSIVE", (min == null) ? wsdlPerspective.getMessage("FORM_LABEL_UNBOUNDED") : min.toString());
String upperBound;
if (maxExclusive)
  upperBound = wsdlPerspective.getMessage("FORM_LABEL_MAX_EXCLUSIVE", (max == null) ? wsdlPerspective.getMessage("FORM_LABEL_UNBOUNDED") : max.toString());
else
  upperBound = wsdlPerspective.getMessage("FORM_LABEL_MAX_INCLUSIVE", (max == null) ? wsdlPerspective.getMessage("FORM_LABEL_UNBOUNDED") : max.toString());
if (min != null || max != null) {
%>
  <table>
    <tr>
      <td height=20>&nbsp;</td>
    </tr>
  </table>
  <table width="95%" border=0 cellpadding=3 cellspacing=0 class="tableborder">
    <tr>
      <th class="singleheadercolor" height=20 valign="bottom" align="left">
        <%=wsdlPerspective.getMessage("FORM_LABEL_RANGE_OF_VALUES")%>
      </th>
    </tr>
    <tr>
      <td class="tablecells">
        <%=lowerBound + upperBound%>
      </td>
    </tr>
  </table>
<%
}
%>

<%
if (totalDigits >= 0) {
%>
  <table>
    <tr>
      <td height=20>&nbsp;</td>
    </tr>
  </table>
  <table width="95%" border=0 cellpadding=3 cellspacing=0 class="tableborder">
    <tr>
      <th class="singleheadercolor" height=20 valign="bottom" align="left">
        <%=wsdlPerspective.getMessage("FORM_LABEL_TOTAL_DIGITS")%>
      </th>
    </tr>
    <tr>
      <td class="tablecells">
        <%=totalDigits%>
      </td>
    </tr>
  </table>
<%
}
%>

<%
if (fractionDigits >= 0) {
%>
  <table>
    <tr>
      <td height=20>&nbsp;</td>
    </tr>
  </table>
  <table width="95%" border=0 cellpadding=3 cellspacing=0 class="tableborder">
    <tr>
      <th class="singleheadercolor" height=20 valign="bottom" align="left">
        <%=wsdlPerspective.getMessage("FORM_LABEL_FRACTION_DIGITS")%>
      </th>
    </tr>
    <tr>
      <td class="tablecells">
        <%=fractionDigits%>
      </td>
    </tr>
  </table>
<%
}
%>
