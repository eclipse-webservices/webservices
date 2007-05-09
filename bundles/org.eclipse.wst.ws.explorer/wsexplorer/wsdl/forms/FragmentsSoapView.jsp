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
 * 20060222   127443 jesper@selskabet.org - Jesper S Moller
 * 20060726   144824 mahutch@ca.ibm.com - Mark Hutchinson
 * 20070305   117034 makandre@ca.ibm.com - Andrew Mak, Web Services Explorer should support SOAP Headers
 * 20070413   176493 makandre@ca.ibm.com - Andrew Mak, WSE: Make message/transport stack pluggable
 *******************************************************************************/
%>
<%@ page contentType="text/html; charset=UTF-8" import="org.eclipse.wst.ws.internal.explorer.platform.wsdl.perspective.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.wsdl.datamodel.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.wsdl.constants.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.wsdl.util.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.constants.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.util.*,
                                                        org.eclipse.wst.ws.internal.explorer.transport.*,
                                                        org.eclipse.wst.wsdl.binding.soap.SOAPHeader,
                                                        org.w3c.dom.*,
                                                        javax.wsdl.*,
                                                        javax.wsdl.extensions.ExtensibilityElement,
                                                        javax.wsdl.extensions.soap.SOAPBody,
                                                        java.util.*" %>

<jsp:useBean id="controller" class="org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller" scope="session"/>
<jsp:useBean id="fragID" class="java.lang.StringBuffer" scope="request"/>
<%
   WSDLPerspective wsdlPerspective = controller.getWSDLPerspective();
   WSDLOperationElement operElement = (WSDLOperationElement)(wsdlPerspective.getNodeManager().getSelectedNode().getTreeElement());
   ISOAPTransport soapTransport = operElement.getSOAPTransportProvider().newTransport();
   ISOAPMessage soapMessage = soapTransport.newMessage(operElement.getMessageContext());
   operElement.setPropertyAsObject(WSDLModelConstants.PROP_SOAP_REQUEST_TMP, soapMessage);
   
   Hashtable soapEnvelopeNamespaceTable = new Hashtable(soapMessage.getNamespaceTable());      
   
   String cachedHeaderContent = operElement.getPropertyAsString(WSDLModelConstants.PROP_SOURCE_CONTENT_HEADER);
   String cachedBodyContent = operElement.getPropertyAsString(WSDLModelConstants.PROP_SOURCE_CONTENT);
   
   // if either header or body has been cached, need to ensure namespace table is updated
   // from the cached copy
   if (cachedHeaderContent != null || cachedBodyContent != null) {
	   if (SOAPMessageUtils.decodeNamespaceTable(soapEnvelopeNamespaceTable, operElement))
		   soapMessage.setNamespaceTable(soapEnvelopeNamespaceTable);
   }
	            
   String headerContent;
   String bodyContent;
   
   if (cachedHeaderContent != null)
       headerContent = cachedHeaderContent;
   else {
	   try {
	       SOAPMessageUtils.setHeaderContentFromModel(soapEnvelopeNamespaceTable, operElement, soapMessage);
	       
	       // ensure namespace table updated in message before serialize operation
	       soapMessage.setNamespaceTable(soapEnvelopeNamespaceTable);
	       headerContent = soapTransport.newSerializer().serialize(ISOAPMessage.HEADER_CONTENT, soapMessage);
	   }
	   catch (Exception e) {
		   headerContent = "";
	   }
   }
   
   if (cachedBodyContent != null)
	   bodyContent = cachedBodyContent;
   else {
	   try {
	       SOAPMessageUtils.setBodyContentFromModel(soapEnvelopeNamespaceTable, operElement, soapMessage);
	       
	       // ensure namespace table updated in message before serialize operation
	       soapMessage.setNamespaceTable(soapEnvelopeNamespaceTable);
	       bodyContent = soapTransport.newSerializer().serialize(ISOAPMessage.BODY_CONTENT, soapMessage);
	   }
	   catch (Exception e) {
		   bodyContent = "";
	   }
   }      
   
   // cache the namespace table
   Enumeration enm = soapEnvelopeNamespaceTable.keys();
   while (enm.hasMoreElements())
   {
     String uri = (String)enm.nextElement();
     String prefix = (String)soapEnvelopeNamespaceTable.get(uri);
     String value = SoapHelper.encodeNamespaceDeclaration(prefix, uri);
     %>
     <input type="hidden" name="<%=FragmentConstants.SOURCE_CONTENT_NAMESPACE%>" value="<%=value%>">
     <%
   }
%>
<table width="95%" border=0 cellpadding=0 cellspacing=0>
  <tr>
    <td height=30 valign="bottom" class="labels">
<%
	Element soapEnvelopeElement = soapMessage.getEnvelope(false);
    StringBuffer header = new StringBuffer("<");
    header.append(soapEnvelopeElement.getTagName());
    NamedNodeMap attributes = soapEnvelopeElement.getAttributes();
    int numberOfAttributes = attributes.getLength();
    if (numberOfAttributes == 0)
      header.append('>');
%>
      <%=HTMLUtils.charactersToHTMLEntitiesStrict(header.toString())%>
    </td>
  </tr>
</table>
<%
    for (int i=0;i<numberOfAttributes;i++)
    {
      header.setLength(0);
      Node attrNode = attributes.item(i);
      header.append(attrNode.getNodeName()).append("=\"").append(attrNode.getNodeValue()).append('\"');
      if (i == numberOfAttributes-1)
        header.append('>');
%>
<table width="95%" border=0 cellpadding=0 cellspacing=0>
  <tr>
    <td width=8>
      <img width=8 height=16 src="<%=response.encodeURL(controller.getPathWithContext("images/space.gif"))%>">
    </td>
    <td valign="bottom" class="labels">
      <%=HTMLUtils.charactersToHTMLEntities(header.toString())%>
    </td>
  </tr>
</table>
<%
    }
    
    Element soapHeaderElement = soapMessage.getHeader(false);
    header.setLength(0);
    header.append('<').append(soapHeaderElement.getTagName());
    attributes = soapHeaderElement.getAttributes();
    numberOfAttributes = attributes.getLength();
    if (numberOfAttributes == 0)
      header.append('>');
%>
<table width="95%" border=0 cellpadding=0 cellspacing=0>
  <tr>
    <td width=8>
      <img width=8 height=16 src="<%=response.encodeURL(controller.getPathWithContext("images/space.gif"))%>">
    </td>
    <td valign="bottom" class="labels">
      <%=HTMLUtils.charactersToHTMLEntities(header.toString())%>
    </td>
  </tr>
</table>
<%
    for (int i=0;i<numberOfAttributes;i++)
    {
      header.setLength(0);
      Node attrNode = attributes.item(i);
      header.append(attrNode.getNodeName()).append("=\"").append(attrNode.getNodeValue()).append('\"');
      if (i == numberOfAttributes-1)
        header.append('>');
%>
<table width="95%" border=0 cellpadding=0 cellspacing=0>
  <tr>
    <td width=16>
      <img width=16 height=16 src="<%=response.encodeURL(controller.getPathWithContext("images/space.gif"))%>">
    </td>
    <td valign="bottom" class="labels">
      <%=HTMLUtils.charactersToHTMLEntities(header.toString())%>
    </td>
  </tr>
</table>
<%
    }
%>
<table border=0 cellpadding=3 cellspacing=3>
  <tr>
    <td width="16">
      <img width="16" height=16 src="<%=response.encodeURL(controller.getPathWithContext("images/space.gif"))%>">
    </td>    
    <td valign="center" align="left" nowrap>
      <input type="file" name="<%=WSDLActionInputs.SELECTED_FILE_HEADER%>" title="<%=wsdlPerspective.getMessage("FORM_CONTROL_TITLE_SOAP_FILE")%>">
    </td>
    <td valign="center" align="left" class="labels" nowrap>
      <a href="javascript:doAction('<%=WSDLActionInputs.SUBMISSION_ACTION_BROWSE_FILE_HEADER%>')"><%=wsdlPerspective.getMessage("BUTTON_LABEL_LOAD")%></a>
    </td>
    <td valign="center" align="left" class="labels" nowrap>
      <a href="javascript:doAction('<%=WSDLActionInputs.SUBMISSION_ACTION_SAVE_AS_HEADER%>')"><%=wsdlPerspective.getMessage("BUTTON_LABEL_SAVE_AS")%></a>
    </td>
  </tr>
</table>
<table width="95%" border=0 cellpadding=3 cellspacing=3>
  <tr>
    <td width="16">
      <img width="16" height="16" src="<%=response.encodeURL(controller.getPathWithContext("images/space.gif"))%>">
    </td>
    <td width="100%">
      <textarea id="soap_header_content" name="<%=FragmentConstants.SOURCE_CONTENT_HEADER%>" class="textareaenter"><%=HTMLUtils.charactersToHTMLEntitiesStrict(headerContent)%></textarea>
    </td>
  </tr>
</table>
<%
    header.setLength(0);
    header.append("</").append(soapHeaderElement.getTagName()).append('>');
%>
<table width="95%" cellpadding=1 cellspacing=0>
  <tr>
    <td width=8>
      <img width=8 height=16 src="<%=response.encodeURL(controller.getPathWithContext("images/space.gif"))%>">
    </td>
    <td valign="bottom" class="labels">
      <%=HTMLUtils.charactersToHTMLEntities(header.toString())%>
    </td>
  </tr>
</table>
<%
    Element soapBodyElement = soapMessage.getBody(false);
    header.setLength(0);
    header.append('<').append(soapBodyElement.getTagName());
    attributes = soapBodyElement.getAttributes();
    numberOfAttributes = attributes.getLength();
    if (numberOfAttributes == 0)
      header.append('>');
%>
<table width="95%" border=0 cellpadding=0 cellspacing=0>
  <tr>
    <td width=8>
      <img width=8 height=16 src="<%=response.encodeURL(controller.getPathWithContext("images/space.gif"))%>">
    </td>
    <td valign="bottom" class="labels">
      <%=HTMLUtils.charactersToHTMLEntities(header.toString())%>
    </td>
  </tr>
</table>
<%
    for (int i=0;i<numberOfAttributes;i++)
    {
      header.setLength(0);
      Node attrNode = attributes.item(i);
      header.append(attrNode.getNodeName()).append("=\"").append(attrNode.getNodeValue()).append('\"');
      if (i == numberOfAttributes-1)
        header.append('>');
%>
<table width="95%" border=0 cellpadding=0 cellspacing=0>
  <tr>
    <td width=16>
      <img width=16 height=16 src="<%=response.encodeURL(controller.getPathWithContext("images/space.gif"))%>">
    </td>
    <td valign="bottom" class="labels">
      <%=HTMLUtils.charactersToHTMLEntities(header.toString())%>
    </td>
  </tr>
</table>
<%
    }

    Element wrapperElement = null;
    if (!operElement.isDocumentStyle())
    {      
      // Generate an RPC style wrapper element.      
      wrapperElement = (Element) soapBodyElement.getFirstChild();
      header.setLength(0);
      header.append('<').append(wrapperElement.getTagName());
      attributes = wrapperElement.getAttributes();
      numberOfAttributes = attributes.getLength();
      if (numberOfAttributes == 0)
        header.append('>');
%>
<table width="95%" border=0 cellpadding=0 cellspacing=0>
  <tr>
    <td width=16>
      <img width=16 height=16 src="<%=response.encodeURL(controller.getPathWithContext("images/space.gif"))%>">
    </td>
    <td valign="bottom" class="labels">
      <%=HTMLUtils.charactersToHTMLEntities(header.toString())%>
    </td>
  </tr>
</table>
<%
      for (int i=0;i<numberOfAttributes;i++)
      {
        header.setLength(0);
        Node attrNode = attributes.item(i);
        header.append(attrNode.getNodeName()).append("=\"").append(attrNode.getNodeValue()).append('\"');
        if (i == numberOfAttributes-1)
          header.append('>');
%>
<table width="95%" border=0 cellpadding=0 cellspacing=0>
  <tr>
    <td width=24>
      <img width=24 height=16 src="<%=response.encodeURL(controller.getPathWithContext("images/space.gif"))%>">
    </td>
    <td valign="bottom" class="labels">
      <%=HTMLUtils.charactersToHTMLEntities(header.toString())%>
    </td>
  </tr>
</table>
<%
      }
    }

    int sourceContentIndentationImageWidth = 16;
    if (wrapperElement != null)
      sourceContentIndentationImageWidth += 8;
%>
<table border=0 cellpadding=3 cellspacing=3>
  <tr>
    <td width=<%=sourceContentIndentationImageWidth%>>
      <img width=<%=sourceContentIndentationImageWidth%> height=16 src="<%=response.encodeURL(controller.getPathWithContext("images/space.gif"))%>">
    </td>    
    <td valign="center" align="left" nowrap>
      <input type="file" name="<%=WSDLActionInputs.SELECTED_FILE%>" title="<%=wsdlPerspective.getMessage("FORM_CONTROL_TITLE_SOAP_FILE")%>">
    </td>
    <td valign="center" align="left" class="labels" nowrap>
      <a href="javascript:doAction('<%=WSDLActionInputs.SUBMISSION_ACTION_BROWSE_FILE%>')"><%=wsdlPerspective.getMessage("BUTTON_LABEL_LOAD")%></a>
    </td>
    <td valign="center" align="left" class="labels" nowrap>
      <a href="javascript:doAction('<%=WSDLActionInputs.SUBMISSION_ACTION_SAVE_AS%>')"><%=wsdlPerspective.getMessage("BUTTON_LABEL_SAVE_AS")%></a>
    </td>
  </tr>
</table>
<table width="95%" border=0 cellpadding=3 cellspacing=3>
  <tr>
    <td width=<%=sourceContentIndentationImageWidth%>>
      <img width=<%=sourceContentIndentationImageWidth%> height=16 src="<%=response.encodeURL(controller.getPathWithContext("images/space.gif"))%>">
    </td>
    <td width="100%">
      <textarea id="soap_body_content" name="<%=FragmentConstants.SOURCE_CONTENT%>" class="bigtextareaenter"><%=HTMLUtils.charactersToHTMLEntitiesStrict(bodyContent)%></textarea>
    </td>
  </tr>
</table>
<%
   if (wrapperElement != null)
   {
     header.setLength(0);
     header.append("</").append(wrapperElement.getTagName()).append('>');
%>
<table width="95%" border=0 cellpadding=0 cellspacing=0>
  <tr>
    <td width=16>
      <img width=16 height=16 src="<%=response.encodeURL(controller.getPathWithContext("images/space.gif"))%>">
    </td>
    <td valign="bottom" class="labels">
      <%=HTMLUtils.charactersToHTMLEntities(header.toString())%>
    </td>
  </tr>
</table>
<%
   }

   header.setLength(0);
   header.append("</").append(soapBodyElement.getTagName()).append('>');
%>
<table width="95%" cellpadding=1 cellspacing=0>
  <tr>
    <td width=8>
      <img width=8 height=16 src="<%=response.encodeURL(controller.getPathWithContext("images/space.gif"))%>">
    </td>
    <td valign="bottom" class="labels">
      <%=HTMLUtils.charactersToHTMLEntities(header.toString())%>
    </td>
  </tr>
</table>
<%
   header.setLength(0);
   header.append("</").append(soapEnvelopeElement.getTagName()).append('>');
%>
<table width="95%" cellpadding=1 cellspacing=0>
  <tr>
    <td valign="bottom" class="labels">
      <%=HTMLUtils.charactersToHTMLEntities(header.toString())%>
    </td>
  </tr>
</table>
