<%
/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
%>
<%@ page contentType="text/html; charset=UTF-8" import="org.eclipse.wst.ws.internal.explorer.platform.uddi.perspective.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.uddi.constants.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.perspective.*" %>

<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<jsp:useBean id="sectionHeaderInfo" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.SectionHeaderInfo" scope="request"/>
<%
   UDDIPerspective uddiPerspective = controller.getUDDIPerspective();
%>
<div id="publishBusiness" style="display:none;">
  <form name="publishBusinessStyle">
    <table width="95%" border=0 cellpadding=3 cellspacing=0>
      <tr>
        <td class="labels">
          <%=uddiPerspective.getMessage("FORM_LABEL_PUBLISH_FORM")%>
        </td>
      </tr>
      <tr>
        <td>
          <input type="radio" id="radio_publish_business_simple" class="radio" name="<%=UDDIActionInputs.QUERY_STYLE_BUSINESSES%>" onClick="showPublishForm(<%=UDDIActionInputs.QUERY_ITEM_BUSINESSES%>,<%=UDDIActionInputs.QUERY_STYLE_SIMPLE%>)"><label for="radio_publish_business_simple"><%=uddiPerspective.getMessage("FORM_RADIO_SIMPLE")%></label>
          <input type="radio" id="radio_publish_business_advance" class="radio" name="<%=UDDIActionInputs.QUERY_STYLE_BUSINESSES%>" onClick="showPublishForm(<%=UDDIActionInputs.QUERY_ITEM_BUSINESSES%>,<%=UDDIActionInputs.QUERY_STYLE_ADVANCED%>)"><label for="radio_publish_business_advance"><%=uddiPerspective.getMessage("FORM_RADIO_ADVANCED")%></label>
        </td>
      </tr>
    </table>
  </form>
<jsp:include page="/uddi/forms/RegPublishBusinessSimpleForm.jsp" flush="true"/>
<jsp:include page="/uddi/forms/RegPublishBusinessAdvancedForm.jsp" flush="true"/>
</div>
