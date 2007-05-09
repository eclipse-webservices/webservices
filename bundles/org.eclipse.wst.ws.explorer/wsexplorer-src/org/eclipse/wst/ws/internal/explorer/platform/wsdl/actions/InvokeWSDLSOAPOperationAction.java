/*******************************************************************************
 * Copyright (c) 2004, 2007 IBM Corporation and others.
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
 *******************************************************************************/
package org.eclipse.wst.ws.internal.explorer.platform.wsdl.actions;

import java.io.IOException;
import java.util.Hashtable;

import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.MessageQueue;
import org.eclipse.wst.ws.internal.explorer.platform.util.MultipartFormDataException;
import org.eclipse.wst.ws.internal.explorer.platform.util.MultipartFormDataParser;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.constants.WSDLActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.constants.WSDLModelConstants;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.datamodel.Endpoint;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.datamodel.WSDLBindingElement;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.datamodel.WSDLOperationElement;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.perspective.InvokeWSDLOperationTool;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.perspective.WSDLPerspective;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.util.SOAPMessageUtils;
import org.eclipse.wst.ws.internal.explorer.transport.HTTPTransportException;
import org.eclipse.wst.ws.internal.explorer.transport.ISOAPMessage;
import org.eclipse.wst.ws.internal.explorer.transport.ISOAPTransportProvider;
import org.eclipse.wst.ws.internal.explorer.transport.TransportException;

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

  /**
   * Returns an ISOAPMessage to use for the current SOAP operation invocation.
   * 
   * @param operElement The operation element from the WSDL model.
   * 
   * @return An ISOAPMessage, or null if a message cannot be constructed.
   */
  protected ISOAPMessage getSOAPRequestMessage(WSDLOperationElement operElement) {
	  ISOAPTransportProvider provider = operElement.getSOAPTransportProvider();
	  if (provider == null)
		  return null;	  
	  return provider.newTransport().newMessage(operElement.getMessageContext());
  }
  
  /**
   * Populate the given ISOAPMessage's header using the inputs from WSE
   * 
   * @param soapEnvelopeNamespaceTable Hashtable containing a map of the namespace URIs to prefixes.
   * @param operElement WSDLOperationElement encapsulating the WSDL operation.
   * @param soapMessage The ISOAPMessage to populate 
   */
  protected void setHeaderContent(Hashtable soapEnvelopeNamespaceTable, WSDLOperationElement operElement, ISOAPMessage soapMessage)
    throws ParserConfigurationException {
    SOAPMessageUtils.setHeaderContentFromModel(soapEnvelopeNamespaceTable, operElement, soapMessage);
  }

  /**
   * Populate the given ISOAPMessage's body using the inputs from WSE
   * 
   * @param soapEnvelopeNamespaceTable Hashtable containing a map of the namespace URIs to prefixes.
   * @param operElement WSDLOperationElement encapsulating the WSDL operation.
   * @param soapMessage The ISOAPMessage to populate 
   */  
  protected void setBodyContent(Hashtable soapEnvelopeNamespaceTable, WSDLOperationElement operElement, ISOAPMessage soapMessage) 
    throws ParserConfigurationException {
	SOAPMessageUtils.setBodyContentFromModel(soapEnvelopeNamespaceTable, operElement, soapMessage);
  }        

  public boolean run()
  {
    String soapAddressLocation = (String)propertyTable_.get(WSDLActionInputs.END_POINT);
    WSDLPerspective wsdlPerspective = controller_.getWSDLPerspective();
    MessageQueue messageQueue = wsdlPerspective.getMessageQueue();
    WSDLOperationElement operElement = (WSDLOperationElement)getSelectedNavigatorNode().getTreeElement();
    WSDLBindingElement bindingElement = (WSDLBindingElement)operElement.getParentElement();
    operElement.setPropertyAsObject(WSDLActionInputs.SOAP_RESPONSE_CACHED, new Boolean(false));
    try
    {
      // Generate the SOAP envelope and its children. We need to create a DOM element version to display and the object version to execute.
      // <SOAP-ENV:Envelope
      //    xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/"
      //    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
      //    xmlns:xsd="http://www.w3.org/2001/XMLSchema">
      // ...

	  ISOAPMessage soapMessage = getSOAPRequestMessage(operElement);
	  if (soapMessage == null)
		  throw new TransportException(wsdlPerspective.getMessage("MSG_ERROR_NO_SUITABLE_TRANSPORT"));
	  
	  Hashtable namespaceTable = new Hashtable(soapMessage.getNamespaceTable());	  
	  setHeaderContent(namespaceTable, operElement, soapMessage);
	  setBodyContent(namespaceTable, operElement, soapMessage);      
	  soapMessage.setNamespaceTable(namespaceTable);

	  // store the request
	  operElement.setPropertyAsObject(WSDLModelConstants.PROP_SOAP_REQUEST, soapMessage);  
	  
	  // Execute the SOAP operation.
      if (soapAddressLocation != null)
      {    	
    	soapMessage.setProperty(ISOAPMessage.PROP_SOAP_ACTION, operElement.getSoapAction());
    	String[] authParams = retrieveAuthParams(bindingElement, soapAddressLocation);
    	
    	// invoke!
    	ISOAPMessage soapResponse = operElement.getSOAPTransportProvider().newTransport()
    		.send(soapAddressLocation, authParams[0], authParams[1], soapMessage);
    	
    	// store the response
    	operElement.setPropertyAsObject(WSDLModelConstants.PROP_SOAP_RESPONSE, soapResponse);
    	
        wsdlPerspective.setOperationNode(getSelectedNavigatorNode());
        return true;
      }
      throw new IOException(wsdlPerspective.getMessage("MSG_ERROR_UNABLE_TO_CONNECT",soapAddressLocation));
    }
    catch (HTTPTransportException e) {
      throwHTTPTransportException(bindingElement, soapAddressLocation, e);
    }
    catch (Exception e) {
      Throwable t = e;
      if (e instanceof TransportException && e.getCause() != null)
    	t = e.getCause();
      handleUnexpectedException(wsdlPerspective, messageQueue, t.getClass().getSimpleName(), t);
    }
    return false;
  }
  
  private void throwHTTPTransportException(WSDLBindingElement bindingElement, String endpointString, HTTPTransportException httpTransportException) throws HTTPTransportException
  {
    if (httpTransportException.getStatusCode() == HttpServletResponse.SC_UNAUTHORIZED)
    {
      Endpoint endpoint = bindingElement.getEndpoint(endpointString);
      if (endpoint != null)
      {
        endpoint.setRequireHTTPBasicAuth(true);
        endpoint.setHttpBasicAuthUsername(null);
        endpoint.setHttpBasicAuthPassword(null);
      }
    }
    throw httpTransportException;
  }
  
  private String[] retrieveAuthParams(WSDLBindingElement bindingElement, String endpointString)
  {
	String[] authParams = new String[] { null, null };
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
          authParams[0] = httpBasicAuthUsername;
          authParams[1] = httpBasicAuthPassword;
        }
      }
    }
    return authParams;
  }
}