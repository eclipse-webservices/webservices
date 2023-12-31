<%
/*******************************************************************************
 * Copyright (c) 2001, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060512   121210 mahutch@ca.ibm.com - Mark Hutchinson
 * 20070305   117034 makandre@ca.ibm.com - Andrew Mak, Web Services Explorer should support SOAP Headers 
 * 20080825   158366 mahutch@ca.ibm.com - Mark Hutchinson
 *******************************************************************************/
%>
<%@ page contentType="text/html; charset=UTF-8" import="org.eclipse.wst.ws.internal.explorer.platform.wsdl.perspective.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.wsdl.datamodel.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.wsdl.constants.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.constants.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.perspective.*,
                                                        javax.wsdl.*,
                                                        java.util.*" %>

<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<jsp:useBean id="fragID" class="java.lang.StringBuffer" scope="request"/>

<%
  WSDLPerspective wsdlPerspective = controller.getWSDLPerspective();
  Node selectedNode = wsdlPerspective.getNodeManager().getSelectedNode();
  InvokeWSDLOperationTool invokeWSDLOperationTool = (InvokeWSDLOperationTool)(selectedNode.getCurrentToolManager().getSelectedTool());
  WSDLOperationElement operElement = (WSDLOperationElement)selectedNode.getTreeElement();
  String fragmentViewID = invokeWSDLOperationTool.getFragmentViewID();
  int operationType = operElement.getOperationType();
  String invokeWSDLOperationURL = response.encodeURL(controller.getPathWithContext(invokeWSDLOperationTool.getFormActionLink(operationType,fragmentViewID)));
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 3.2 Final//EN">
<html lang="<%=response.getLocale().getLanguage()%>">
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title><%=wsdlPerspective.getMessage("FORM_TITLE_INVOKE_WSDL_OPERATION")%></title>
  <link rel="stylesheet" type="text/css" href="<%=response.encodeURL(controller.getPathWithContext("css/windows.css"))%>">
<jsp:include page="/scripts/formsubmit.jsp" flush="true"/>
<jsp:include page="/wsdl/scripts/fragmenttables.jsp" flush="true"/>
<jsp:include page="/wsdl/scripts/wsdlpanes.jsp" flush="true"/>
<script language="javascript">
  function doAction(action)
  {
    var form = document.forms[0];
    if (handleSubmit(form))
    {
      form.<%=WSDLActionInputs.SUBMISSION_ACTION%>.value = action;
      form.submit();
      form.<%=WSDLActionInputs.SUBMISSION_ACTION%>.value = "<%=invokeWSDLOperationURL%>";
      resetSubmission();
    }
  }
  
  function closeChildWindows()
  {
    closeXSDInfoDialog();
    closeCalendarBrowser();
  }
</script>
</head>
<body dir="<%=org.eclipse.wst.ws.internal.explorer.platform.util.DirUtils.getDir()%>" class="contentbodymargin" onUnload="closeChildWindows()">
<div id="contentborder">
  <form action="<%=invokeWSDLOperationURL%>" method="post" target="<%=FrameNames.PERSPECTIVE_WORKAREA%>" enctype="multipart/form-data">
    <input type="hidden" name="<%=WSDLActionInputs.SUBMISSION_ACTION%>" value="<%=WSDLActionInputs.SUBMISSION_ACTION_FORM%>">
    <%
    String titleImagePath = "wsdl/images/invoke_wsdl_operation_highlighted.gif";
    String title = wsdlPerspective.getMessage("ALT_INVOKE_WSDL_OPERATION");
    if (operationType == WSDLOperationElement.OPERATION_TYPE_SOAP)
    {
    %>
<table width="95%" border=0 cellpadding=3 cellspacing=0>
  <tr>
    <td>
      <img src="<%=response.encodeURL(controller.getPathWithContext(titleImagePath))%>" alt="">
      <strong><%=title%></strong>
    </td>
    <td width="*">&nbsp;</td>
    <td align="right" class="labels">
<%
   if (fragmentViewID.equals(FragmentConstants.FRAGMENT_VIEW_SWITCH_FORM_TO_SOURCE))
   {
%>
      <a href="javascript:synchronizeFragmentViews('<%=FragmentConstants.FRAGMENT_VIEW_SWITCH_SOURCE_TO_FORM%>')" title="<%=wsdlPerspective.getMessage("ALT_SWITCH_TO_FORM_VIEW")%>"><%=wsdlPerspective.getMessage("FORM_LINK_FORM")%></a>
<%
   }
   else
   {
%>
      <a href="javascript:synchronizeFragmentViews('<%=FragmentConstants.FRAGMENT_VIEW_SWITCH_FORM_TO_SOURCE%>')" title="<%=wsdlPerspective.getMessage("ALT_SWITCH_TO_SOURCE_VIEW")%>"><%=wsdlPerspective.getMessage("FORM_LINK_SOURCE")%></a>
<%
   }
%>
    </td>
  </tr>
  <tr>
    <td height=20 colspan=3><img height=2 width="100%" align="top" src="<%=response.encodeURL(controller.getPathWithContext("images/keyline.gif"))%>" alt=""></td>
  </tr>
</table>
    <%
    }
    else
    {
    %>
    <%@ include file = "/forms/formheader.inc" %>
    <%
    }
    %>
    <table>
      <tr>
        <td class="labels">

        <%           
		//here we determine if there are any input parameters for this operation
		boolean hasInput = true;
		selectedNode = wsdlPerspective.getNodeManager().getSelectedNode();  
		Iterator iter = operElement.getOrderedBodyParts().iterator();        
		String operationName = operElement.getName();

		while (iter.hasNext())
		{
			javax.wsdl.Part part = (javax.wsdl.Part)iter.next(); 
			IXSDFragment frag = operElement.getFragment(part);  
			org.eclipse.xsd.XSDTypeDefinition def = frag.getXSDTypeDefinition();
			//we check the type of the operation's input message
			if (def == null)
			{
				//this is the pattern <element name="foo"></element>
				hasInput = false;
			}
			else
			{
				org.w3c.dom.Element element = def.getElement();				
				if (element.getLocalName().equals("complexType") && !element.hasChildNodes())
				{	//this is the pattern  <element name="foo"><complexType/></element>
					//there could be other patterns with no input but they are uncommon
					hasInput = false;
				}
			} 
      	}
		if (hasInput || !operElement.getSOAPHeaders().isEmpty())
		{
			out.print(wsdlPerspective.getMessage("FORM_LABEL_INVOKE_WSDL_OPERATION_DESC", operationName));
		}
		else
		{
			out.print(wsdlPerspective.getMessage("FORM_LABEL_INVOKE_WSDL_OPERATION_DESC_NO_INPUT", operationName ));
		}
        %>
         
        </td>
      </tr>
    </table>
    <table width="95%" border=0 cellpadding=3 cellspacing=0>
      <tr>
        <td>
          <fieldset class="keylinefieldset">
            <legend class="labels">
              <label for="<%=WSDLActionInputs.END_POINT%>"><%=wsdlPerspective.getMessage("FORM_LABEL_END_POINTS")%></label>
            </legend>
            <table border=0 cellpadding=10 cellspacing=0>
              <tr>
                <td>
                  <select id="<%=WSDLActionInputs.END_POINT%>" name="<%=WSDLActionInputs.END_POINT%>" class="selectlist">
                    <%
                    WSDLBindingElement bindingElement = (WSDLBindingElement)operElement.getParentElement();
                    String[] endPoints = bindingElement.getEndPoints();
                    String currEndPoint = invokeWSDLOperationTool.getEndPoint();
                    for (int i = 0; i < endPoints.length; i++)
                    {
                    %>
                      <option value="<%=endPoints[i]%>" <%if (endPoints[i].equals(currEndPoint)) {%>selected<%}%>><%=endPoints[i]%>
                    <%
                    }
                    %>
                  </select>
                </td>
              </tr>
            </table>
          </fieldset>
        </td>
      </tr>
    </table>
    <%
    if (fragmentViewID.equals(FragmentConstants.FRAGMENT_VIEW_SWITCH_FORM_TO_SOURCE))
    {
    %>
<jsp:include page="/wsdl/forms/FragmentsSoapView.jsp" flush="true"/>
    <%
    }
    else
    {
    %>
<jsp:include page="/wsdl/forms/FragmentsFormView.jsp" flush="true">
	<jsp:param name="hasInput" value="<%=hasInput%>"/>
</jsp:include>
    <%
    }
    %>
    <jsp:include page="/forms/simpleCommon_table.jsp" flush="true"/>
  </form>
</div>
</body>
</html>
