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
<%@ page contentType="text/html; charset=UTF-8" import="org.eclipse.wst.ws.internal.explorer.platform.wsdl.perspective.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.wsdl.datamodel.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.wsdl.constants.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.wsdl.util.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.constants.*,
                                                        org.eclipse.wst.ws.internal.explorer.platform.util.*,
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
   Operation oper = operElement.getOperation();
   Iterator it = operElement.getOrderedBodyParts().iterator();
   StringBuffer sourceContent = new StringBuffer();
   String cachedSourceContent = operElement.getPropertyAsString(WSDLModelConstants.PROP_SOURCE_CONTENT);
   Hashtable soapEnvelopeNamespaceTable = new Hashtable();
   SoapHelper.addDefaultSoapEnvelopeNamespaces(soapEnvelopeNamespaceTable);
   if (cachedSourceContent != null)
   {
     sourceContent.append(cachedSourceContent);
     String[] nsDeclarations = (String[])operElement.getPropertyAsObject(WSDLModelConstants.PROP_SOURCE_CONTENT_NAMESPACE);
     if (nsDeclarations != null)
     {
       for (int i = 0; i < nsDeclarations.length; i++)
       {
         String[] prefix_ns = SoapHelper.decodeNamespaceDeclaration(nsDeclarations[i]);
         if (!soapEnvelopeNamespaceTable.contains(prefix_ns[1]))
           soapEnvelopeNamespaceTable.put(prefix_ns[1], prefix_ns[0]);
       }
     }
   }
   else
   {
     while (it.hasNext())
     {
       Part part = (Part)it.next();
       IXSDFragment frag = operElement.getFragment(part);
       Element[] instanceDocuments = frag.genInstanceDocumentsFromParameterValues(!operElement.isUseLiteral(), soapEnvelopeNamespaceTable, XMLUtils.createNewDocument(null));
       for (int i = 0; i < instanceDocuments.length; i++)
       {
         sourceContent.append(XMLUtils.serialize(instanceDocuments[i], true));
         sourceContent.append(HTMLUtils.LINE_SEPARATOR);
       }     
     }
   }

   Enumeration enum = soapEnvelopeNamespaceTable.keys();
   while (enum.hasMoreElements())
   {
     String uri = (String)enum.nextElement();
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
    Document doc = XMLUtils.createNewDocument(null);
    Element soapEnvelopeElement = SoapHelper.createSoapEnvelopeElement(doc,soapEnvelopeNamespaceTable);
    StringBuffer header = new StringBuffer("<");
    header.append(soapEnvelopeElement.getTagName());
    NamedNodeMap attributes = soapEnvelopeElement.getAttributes();
    int numberOfAttributes = attributes.getLength();
    if (numberOfAttributes == 0)
      header.append('>');
%>
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

    Element soapBodyElement = SoapHelper.createSoapBodyElement(doc);
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
<table
<%
    }

    Element wrapperElement = null;
    if (!operElement.isDocumentStyle())
    {
      // Must be RPC style.
      String encodingNamespaceURI = null;
      /*
       * WS-I: In a rpc-literal SOAP binding, the serialized child element of the 
       * soap:Body element consists of a wrapper element, whose namespace is the value 
       * of the namespace attribute of the soapbind:body element and whose local name is 
       * either the name of the operation or the name of the operation suffixed 
       * with "Response". The namespace attribute is required, as opposed to being 
       * optional, to ensure that the children of the soap:Body element are namespace-
       * qualified.
       */
      BindingOperation bindingOperation = operElement.getBindingOperation();
      if (bindingOperation != null)
      {
        BindingInput bindingInput = bindingOperation.getBindingInput();
        if (bindingInput != null)
        {
          List extElements = bindingInput.getExtensibilityElements();
          for (Iterator extElementsIt = extElements.iterator(); extElementsIt.hasNext();)
          {
            ExtensibilityElement extElement = (ExtensibilityElement)extElementsIt.next();
            if (extElement instanceof SOAPBody)
            {
              encodingNamespaceURI = ((SOAPBody)extElement).getNamespaceURI();
              break;
            }
          }
        }
      }
      // If the namespace of the soapbind:body element is not set, get it from the operation element
      if (encodingNamespaceURI == null)
        encodingNamespaceURI = operElement.getEncodingNamespace();
      // If the namespace of the operation element is not set, get it from the definition element
      if (encodingNamespaceURI == null)
      {
        WSDLBindingElement bindingElement = (WSDLBindingElement)operElement.getParentElement();
        WSDLServiceElement serviceElement = (WSDLServiceElement)bindingElement.getParentElement();
        WSDLElement wsdlElement = (WSDLElement)serviceElement.getParentElement();
        Definition definition = wsdlElement.getDefinition();
        encodingNamespaceURI = definition.getTargetNamespace();
      }
      // Generate an RPC style wrapper element.
      String encodingStyle = (operElement.isUseLiteral() ? null : operElement.getEncodingStyle());
      wrapperElement = SoapHelper.createRPCWrapperElement(doc,soapEnvelopeNamespaceTable,encodingNamespaceURI,oper.getName(),encodingStyle);
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
      <a href="javascript:showNewFileContents()"><%=wsdlPerspective.getMessage("BUTTON_LABEL_LOAD")%></a>
    </td>
    <td valign="center" align="left" class="labels" nowrap>
      <a href="javascript:saveSourceContent()"><%=wsdlPerspective.getMessage("BUTTON_LABEL_SAVE_AS")%></a>
    </td>
  </tr>
</table>
<table width="95%" border=0 cellpadding=3 cellspacing=3>
  <tr>
    <td width=<%=sourceContentIndentationImageWidth%>>
      <img width=<%=sourceContentIndentationImageWidth%> height=16 src="<%=response.encodeURL(controller.getPathWithContext("images/space.gif"))%>">
    </td>
    <td width="100%">
      <textarea id="soap_body_content" name="<%=FragmentConstants.SOURCE_CONTENT%>" class="bigtextareaenter"><%=sourceContent.toString()%></textarea>
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
