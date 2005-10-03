/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.ws.internal.explorer.platform.wsdl.actions;

import org.eclipse.wst.ws.internal.explorer.platform.perspective.*;
import org.eclipse.wst.ws.internal.explorer.platform.util.*;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.constants.*;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.datamodel.*;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.*;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.perspective.*;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.transport.HTTPException;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.transport.HTTPTransport;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.util.*;
import org.eclipse.wst.ws.internal.parser.discovery.*;

import org.apache.axis.Constants;

import org.w3c.dom.*;

import javax.xml.parsers.*;
import javax.wsdl.*;
import javax.wsdl.extensions.ExtensibilityElement;
import javax.wsdl.extensions.soap.SOAPBody;
import javax.servlet.http.HttpServletResponse;

import java.util.*;
import java.io.*;

public abstract class InvokeWSDLSOAPOperationAction extends WSDLPropertiesFormAction
{
  public InvokeWSDLSOAPOperationAction(Controller controller)
  {
    super(controller);
  }

  protected boolean processParsedResults(MultipartFormDataParser parser) throws MultipartFormDataException
  {
    super.processParsedResults(parser);
    String endPoint = parser.getParameter(WSDLActionInputs.END_POINT);
    InvokeWSDLOperationTool invokeWSDLOperationTool = (InvokeWSDLOperationTool)(getSelectedNavigatorNode().getCurrentToolManager().getSelectedTool());
    invokeWSDLOperationTool.setEndPoint(endPoint);
    return true;
  }

  protected final void addRPCWrapper(Vector bodyEntries,WSDLElement wsdlElement,WSDLOperationElement operElement,Hashtable soapEnvelopeNamespaceTable) throws ParserConfigurationException
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
        for (Iterator it = extElements.iterator(); it.hasNext();)
        {
          ExtensibilityElement extElement = (ExtensibilityElement)it.next();
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
      Definition definition = wsdlElement.getDefinition();
      encodingNamespaceURI = definition.getTargetNamespace();
    }
    // Generate an RPC style wrapper element.
    Document doc = XMLUtils.createNewDocument(null);
    String encodingStyle = (operElement.isUseLiteral() ? null : operElement.getEncodingStyle());
    Element wrapperElement = SoapHelper.createRPCWrapperElement(doc,soapEnvelopeNamespaceTable,encodingNamespaceURI,operElement.getOperation().getName(),encodingStyle);
    for (int i=0;i<bodyEntries.size();i++)
      wrapperElement.appendChild(doc.importNode((Element)bodyEntries.elementAt(i),true));
    bodyEntries.removeAllElements();
    bodyEntries.addElement(wrapperElement);
  }

  /**
   * Generate a Vector of the elements inside the Soap Body.
   * @param soapEnvelopeNamespaceTable - Hashtable containing a map of the namespace URIs to prefixes.
   * @param operElement - WSDLOperationElement encapsulating the WSDL operation.
   */
  protected Vector getBodyEntries(Hashtable soapEnvelopeNamespaceTable,WSDLOperationElement operElement,WSDLBindingElement bindingElement,WSDLServiceElement serviceElement) throws ParserConfigurationException,Exception
  {
    Vector bodyEntries = new Vector();
    boolean isUseLiteral = operElement.isUseLiteral();
    String encodingStyle = operElement.getEncodingStyle();
    boolean addEncodingStyle = (!isUseLiteral && !Constants.URI_SOAP11_ENC.equals(encodingStyle));
    Iterator it = operElement.getOrderedBodyParts().iterator();
    while (it.hasNext())
    {
      Part part = (Part)it.next();
      IXSDFragment frag = (IXSDFragment)operElement.getFragment(part);
      Element[] instanceDocuments = frag.genInstanceDocumentsFromParameterValues(!isUseLiteral,soapEnvelopeNamespaceTable, XMLUtils.createNewDocument(null));
      for (int j=0;j<instanceDocuments.length;j++)
      {
        if (instanceDocuments[j] == null)
          continue;
        if (addEncodingStyle)
          instanceDocuments[j].setAttribute("soapenv:encodingStyle",encodingStyle);
        bodyEntries.addElement(instanceDocuments[j]);
      }
    }

    if (!operElement.isDocumentStyle())
    {
      try
      {
        addRPCWrapper(bodyEntries,(WSDLElement)serviceElement.getParentElement(),operElement,soapEnvelopeNamespaceTable);
      }
      catch (ParserConfigurationException e)
      {
        throw e;
      }
    }
    return bodyEntries;
  }

  protected Element getSOAPEnvelope(Hashtable soapEnvelopeNamespaceTable, Vector bodyEntries) throws ParserConfigurationException
  {
    DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
    Document doc = XMLUtils.createNewDocument(docBuilder);
    Element soapEnvelopeElement = SoapHelper.createSoapEnvelopeElement(doc,soapEnvelopeNamespaceTable);
    Element soapBodyElement = SoapHelper.createSoapBodyElement(doc);
    for (int i=0;i<bodyEntries.size();i++)
      soapBodyElement.appendChild(doc.importNode((Element)bodyEntries.elementAt(i),true));
    soapEnvelopeElement.appendChild(soapBodyElement);
    return soapEnvelopeElement;
  }

  private final void recordSoapRequest(SOAPMessageQueue soapRequestQueue,Hashtable soapEnvelopeNamespaceTable,Element soapEnvelope) throws ParserConfigurationException,IOException
  {
    soapRequestQueue.clear();
    soapRequestQueue.addMessage(XMLUtils.serialize(soapEnvelope,false));
  }

  private final void recordSOAPResponse(SOAPMessageQueue soapResponseQueue,BufferedReader responseReader) throws IOException
  {
    soapResponseQueue.clear();
    if (responseReader != null)
    {
      String line = null;
      while ((line = responseReader.readLine()) != null)
        soapResponseQueue.addMessage(line);
      responseReader.close();
    }
  }

  public boolean run()
  {
    String soapAddressLocation = (String)propertyTable_.get(WSDLActionInputs.END_POINT);
    WSDLPerspective wsdlPerspective = controller_.getWSDLPerspective();
    MessageQueue messageQueue = wsdlPerspective.getMessageQueue();
    WSDLOperationElement operElement = (WSDLOperationElement)getSelectedNavigatorNode().getTreeElement();
    WSDLBindingElement bindingElement = (WSDLBindingElement)operElement.getParentElement();
    WSDLServiceElement serviceElement = (WSDLServiceElement)bindingElement.getParentElement();
    operElement.setPropertyAsObject(WSDLActionInputs.SOAP_RESPONSE_CACHED, new Boolean(false));
    try
    {
      // Generate the SOAP envelope and its children. We need to create a DOM element version to display and the object version to execute.
      // <SOAP-ENV:Envelope
      //    xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/"
      //    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
      //    xmlns:xsd="http://www.w3.org/2001/XMLSchema">
      // ...
      Hashtable soapEnvelopeNamespaceTable = new Hashtable();
      SoapHelper.addDefaultSoapEnvelopeNamespaces(soapEnvelopeNamespaceTable);
      Vector bodyEntries = getBodyEntries(soapEnvelopeNamespaceTable,operElement,bindingElement,serviceElement);
      Element soapEnvelope = getSOAPEnvelope(soapEnvelopeNamespaceTable, bodyEntries);
      recordSoapRequest(wsdlPerspective.getSOAPRequestQueue(),soapEnvelopeNamespaceTable,soapEnvelope);

      // Execute the SOAP operation.
      if (soapAddressLocation != null)
      {
        // Send the message and record the SOAP Response Envelope.
        HTTPTransport transport = createTransport(bindingElement, soapAddressLocation);
        transport.send(NetUtils.createURL(soapAddressLocation),operElement.getSoapAction(),XMLUtils.serialize(soapEnvelope, true));
        recordSOAPResponse(wsdlPerspective.getSOAPResponseQueue(),transport.receive());
        wsdlPerspective.setOperationNode(getSelectedNavigatorNode());
        return true;
      }
      throw new IOException(wsdlPerspective.getMessage("MSG_ERROR_UNABLE_TO_CONNECT",soapAddressLocation));
    }
    catch (ParserConfigurationException e)
    {
      handleUnexpectedException(wsdlPerspective,messageQueue,"ParserConfigurationException",e);
    }
    catch (IOException e)
    {
      handleUnexpectedException(wsdlPerspective,messageQueue,"IOException",e);
    }
    catch (HTTPException httpe)
    {
      throwHTTPException(bindingElement, soapAddressLocation, httpe);
    }
    catch (Exception e)
    {
      handleUnexpectedException(wsdlPerspective,messageQueue,"Exception",e);
    }
    return false;
  }
  
  private void throwHTTPException(WSDLBindingElement bindingElement, String endpointString, HTTPException httpException) throws HTTPException
  {
    if (httpException.getStatusCode() == HttpServletResponse.SC_UNAUTHORIZED)
    {
      Endpoint endpoint = bindingElement.getEndpoint(endpointString);
      if (endpoint != null)
      {
        endpoint.setRequireHTTPBasicAuth(true);
        endpoint.setHttpBasicAuthUsername(null);
        endpoint.setHttpBasicAuthPassword(null);
      }
    }
    throw httpException;
  }
  
  private HTTPTransport createTransport(WSDLBindingElement bindingElement, String endpointString)
  {
    HTTPTransport transport = new HTTPTransport();
    Endpoint endpoint = bindingElement.getEndpoint(endpointString);
    if (endpoint != null)
    {
      if (endpoint.isRequireHTTPBasicAuth())
      {
        String httpBasicAuthUsername = endpoint.getHttpBasicAuthUsername();
        String httpBasicAuthPassword = endpoint.getHttpBasicAuthPassword();
        if (httpBasicAuthUsername == null || httpBasicAuthPassword == null)
        {
          httpBasicAuthUsername = (String)propertyTable_.get(WSDLActionInputs.HTTP_BASIC_AUTH_USERNAME);
          httpBasicAuthPassword = (String)propertyTable_.get(WSDLActionInputs.HTTP_BASIC_AUTH_PASSWORD);
          endpoint.setHttpBasicAuthUsername(httpBasicAuthUsername);
          endpoint.setHttpBasicAuthPassword(httpBasicAuthPassword);
        }
        if (httpBasicAuthUsername != null && httpBasicAuthPassword != null)
        {
          transport.setHttpBasicAuthUsername(httpBasicAuthUsername);
          transport.setHttpBasicAuthPassword(httpBasicAuthPassword);
        }
      }
    }
    return transport;
  }
}