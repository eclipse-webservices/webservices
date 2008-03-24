<%
/*******************************************************************************
 * Copyright (c) 2001, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20070305   117034 makandre@ca.ibm.com - Andrew Mak, Web Services Explorer should support SOAP Headers
 * 20070413   176493 makandre@ca.ibm.com - Andrew Mak, WSE: Make message/transport stack pluggable
 * 20070507   185600 makandre@ca.ibm.com - Andrew Mak, WSE status pane's "header" twistie should not appear for responses without headers
 *******************************************************************************/
%>
<%@ page contentType="text/html; charset=UTF-8" import="org.eclipse.wst.ws.internal.explorer.platform.wsdl.perspective.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.wsdl.datamodel.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.wsdl.constants.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.wsdl.xsd.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.impl.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.wsdl.util.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.constants.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.perspective.Node,
                                                        org.eclipse.wst.ws.internal.explorer.platform.perspective.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.util.*,
                                                        org.eclipse.wst.ws.internal.explorer.transport.*,
                                                        org.eclipse.wst.wsdl.binding.soap.SOAPHeader,
                                                        org.w3c.dom.*,
                                                        javax.wsdl.*,
                                                        javax.xml.parsers.*,
                                                        java.util.*" %>

<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<jsp:useBean id="fragID" class="java.lang.StringBuffer" scope="request"/>
<jsp:useBean id="nodeID" class="java.lang.StringBuffer" scope="request"/>
<%
WSDLPerspective wsdlPerspective = controller.getWSDLPerspective();
wsdlPerspective.setStatusContentType(WSDLPerspective.STATUS_CONTENT_RESULT_FORM);
Node operNode = wsdlPerspective.getOperationNode();
nodeID.delete(0, nodeID.length());
nodeID.append(operNode.getNodeId());
WSDLOperationElement operElement = (WSDLOperationElement)operNode.getTreeElement();
Operation oper = operElement.getOperation();
if (oper.getOutput() == null)
{
%>
  <table width="95%" border=0 cellpadding=6 cellspacing=0>
    <tr>
      <td height=20 valign="bottom" align="left" class="labels">
        <%=wsdlPerspective.getMessage("FORM_LABEL_NOTHING_TO_DISPLAY_IN_FORM_VIEW")%>
      </td>
    </tr>
  </table>
<%
}
else
{
  ISOAPMessage soapMessage = (ISOAPMessage) operElement.getPropertyAsObject(WSDLModelConstants.PROP_SOAP_RESPONSE);
  Element[] headerContent = soapMessage.getHeaderContent();;
  Element[] bodyContent   = soapMessage.getBodyContent();
  
  boolean cached = ((Boolean)operElement.getPropertyAsObject(WSDLActionInputs.SOAP_RESPONSE_CACHED)).booleanValue();

  if (soapMessage.getBody(false) == null) // body is mandatory
  {
  %>
    <table width="95%" border=0 cellpadding=6 cellspacing=0>
      <tr>
        <td height=20 valign="bottom" align="left" class="labels">
          <%=wsdlPerspective.getMessage("FORM_LABEL_SOAP_RESPONSE_FAILED_VALIDATION_IN_FORM_VIEW")%>
        </td>
      </tr>
    </table>
  <%
  }
  else
  {
	if (headerContent != null && headerContent.length > 0) {		

		String headerDivId = "Header";
		String headerImgId = "xHeader";		
	    %>
	    <table border=0 cellpadding=6 cellspacing=0>
		  <tr>
		    <td height=20 valign="bottom" align="left" nowrap width=11><a href="javascript:twist('<%=headerDivId%>','<%=headerImgId%>')"><img name="<%=headerImgId%>" src="<%=response.encodeURL(controller.getPathWithContext("images/twistopened.gif"))%>" alt="<%=controller.getMessage("ALT_TWIST_OPENED")%>" class="twist"></a></td>
		    <td height=20 valign="bottom" align="left" nowrap class="labels"><strong><%=wsdlPerspective.getMessage("FORM_LABEL_HEADER")%></strong></td>
		  </tr>
		</table>
		
		<table width="95%" border=0 cellpadding=0 cellspacing=0>
		  <tr>
		    <td valign="top" height=10><img src="<%=response.encodeURL(controller.getPathWithContext("images/keyline.gif"))%>" alt="" height=2 width="100%"></td>
		  </tr>
		</table>
		
		<div id="<%=headerDivId%>" class="fragarea">
		<%
		boolean hasSOAPHeaders = false;		
		Iterator it = operElement.getSOAPHeaders(false).iterator();
		int start = 0;
		while (it.hasNext() && start < headerContent.length) {
			SOAPHeader soapHeader = (SOAPHeader) it.next();									
			IXSDFragment fragment = operElement.getHeaderFragment(soapHeader, false);
			
			if (!cached) {				
				int pos = SOAPMessageUtils.findFirstMatchingElement(
					soapHeader.getEPart(),
					headerContent,
					soapMessage.getNamespaceTable(),
					fragment.getName(),
					start);
				
				if (pos == -1)
					continue;
				
				Element element = headerContent[pos];
				start = pos + 1;
				
				if (!fragment.setParameterValuesFromInstanceDocuments(new Element[] { element }))
					continue;
			}
			else if (!fragment.validateAllParameterValues())
				continue;
			
			hasSOAPHeaders = true;				
			fragID.delete(0, fragID.length());
			fragID.append(fragment.getID());
			%>
			<jsp:include page="<%=fragment.getReadFragment()%>" flush="true"/>
			<%  
		}		
		if (!hasSOAPHeaders) {
			%>
			<table width="95%" border=0 cellpadding=6 cellspacing=0>
		      <tr>
		        <td height=20 valign="bottom" align="left" class="labels">
		          <%=wsdlPerspective.getMessage("FORM_LABEL_CANNOT_DISPLAY_HEADER_IN_FORM_VIEW")%>
		        </td>
		      </tr>
		    </table>
			<%
		}
		%>
		</div>
		<%   
	}
		
	String bodyDivId = "Body";
	String bodyImgId = "xBody";	  		
	%>			
	<table border=0 cellpadding=6 cellspacing=0>
	  <tr>
	    <td height=20 valign="bottom" align="left" nowrap width=11><a href="javascript:twist('<%=bodyDivId%>','<%=bodyImgId%>')"><img name="<%=bodyImgId%>" src="<%=response.encodeURL(controller.getPathWithContext("images/twistopened.gif"))%>" alt="<%=controller.getMessage("ALT_TWIST_OPENED")%>" class="twist"></a></td>
	    <td height=20 valign="bottom" align="left" nowrap class="labels"><strong><%=wsdlPerspective.getMessage("FORM_LABEL_BODY")%></strong></td>
	  </tr>
	</table> 
	
	<table width="95%" border=0 cellpadding=0 cellspacing=0>
	  <tr>
	    <td valign="top" height=10><img src="<%=response.encodeURL(controller.getPathWithContext("images/keyline.gif"))%>" alt="" height=2 width="100%"></td>
	  </tr>
	</table>
	
	<div id="<%=bodyDivId%>" class="fragarea">
	<%	
	boolean hasSOAPBody = false;
	if (bodyContent != null && bodyContent.length > 0) {				
		
	    Map partsMap = oper.getOutput().getMessage().getParts();
	    Iterator it = partsMap.values().iterator();
	    Hashtable uriReferences = null;
	    while (it.hasNext())
	    {
	      IXSDFragment fragment = operElement.getFragment((Part)it.next(), false);
	      if (!cached)
	      {
	        if (!operElement.isUseLiteral() && (fragment instanceof ISOAPEncodingWrapperFragment))
	        {
	          if (uriReferences == null)
	            uriReferences = SOAPEncodingWrapperFragment.parseURIReferences(soapMessage.getEnvelope(true), true);
	          ((ISOAPEncodingWrapperFragment)fragment).setURIReferences(uriReferences);
	        }
	        if (!fragment.setParameterValuesFromInstanceDocuments(bodyContent))
	        	continue;
	      }
	      else if (!fragment.validateAllParameterValues())
			continue;
			
		  hasSOAPBody = true;				
	      fragID.delete(0, fragID.length());
	      fragID.append(fragment.getID());
	      %>
	      <jsp:include page="<%=fragment.getReadFragment()%>" flush="true"/>
	      <%
	    }
	    operElement.setPropertyAsObject(WSDLActionInputs.SOAP_RESPONSE_CACHED, new Boolean(true));
	}
	
	if (soapMessage.getFault() == null && (bodyContent == null || bodyContent.length == 0)) {
		%>
		<table width="95%" border=0 cellpadding=6 cellspacing=0>
	      <tr>
	        <td height=20 valign="bottom" align="left" class="labels">
	          <%=wsdlPerspective.getMessage("FORM_LABEL_BODY_IS_EMPTY")%>
	        </td>
	      </tr>
	    </table>
		<%
    }
	else if (!hasSOAPBody) {
		%>
		<table width="95%" border=0 cellpadding=6 cellspacing=0>
	      <tr>
	        <td height=20 valign="bottom" align="left" class="labels">
	          <%=wsdlPerspective.getMessage("FORM_LABEL_CANNOT_DISPLAY_BODY_IN_FORM_VIEW")%>
	        </td>
	      </tr>
	    </table>
		<%
	} 
    %>
    </div>
    <%
  }
}
%>
