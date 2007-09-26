/*******************************************************************************
 * Copyright (c) 2002, 2007 IBM Corporation and others.
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
package org.eclipse.wst.ws.internal.explorer.platform.wsdl.datamodel;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import javax.wsdl.Binding;
import javax.wsdl.BindingInput;
import javax.wsdl.BindingOperation;
import javax.wsdl.Definition;
import javax.wsdl.Input;
import javax.wsdl.Operation;
import javax.wsdl.Output;
import javax.wsdl.Part;
import javax.wsdl.extensions.ExtensibilityElement;
import javax.wsdl.extensions.soap.SOAPBinding;
import javax.wsdl.extensions.soap.SOAPBody;
import javax.wsdl.extensions.soap.SOAPOperation;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.TransportProviderRegistry;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.constants.BindingTypes;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.constants.FragmentConstants;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.IXSDFragment;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.XSDToFragmentConfiguration;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.XSDToFragmentController;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.impl.SOAPHeaderWrapperFragment;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.xsd.WSDLPartsToXSDTypeMapper;
import org.eclipse.wst.ws.internal.explorer.transport.ISOAPTransportProvider;
import org.eclipse.wst.ws.internal.explorer.transport.MessageContext;
import org.eclipse.wst.wsdl.binding.soap.SOAPHeader;
import org.eclipse.xsd.XSDNamedComponent;

public class WSDLOperationElement extends WSDLCommonElement
{
  public static final int OPERATION_TYPE_SOAP = 0;
  public static final int OPERATION_TYPE_HTTP_GET = 1;
  public static final int OPERATION_TYPE_HTTP_POST = 2;

  private int operationType_;
  private Operation operation_;
  private WSDLPartsToXSDTypeMapper wsdlPartsToXsdTypeMapper_;
  private XSDToFragmentController fragController_;
  private boolean isDocumentStyle_;
  private boolean isUseLiteral_;
  private String soapAction_;
  private String encodingStyle_;
  private String encodingNamespace_;

  private Map headerCache;
  
  private MessageContext messageContext = null;
  private ISOAPTransportProvider soapTransportProvider = null;  
  
  private final void gatherSoapInformation(WSDLBindingElement bindingElement,SOAPBinding soapBinding)
  {
    // Initialize defaults.
    isDocumentStyle_ = true;
    soapAction_ = "";
    isUseLiteral_ = true;
    encodingStyle_ = null;
    encodingNamespace_ = null;

    if (soapBinding != null)
      isDocumentStyle_ = "document".equals(soapBinding.getStyle());
    BindingOperation bindingOperation = getBindingOperation(bindingElement);
    SOAPOperation soapOperation = null;
    
    for (Iterator i = bindingOperation.getExtensibilityElements().iterator();i.hasNext();)
    {
      ExtensibilityElement e = (ExtensibilityElement)i.next();
      if (e instanceof SOAPOperation)
      {
        soapOperation = (SOAPOperation)e;
        soapAction_ = soapOperation.getSoapActionURI();
        String style = soapOperation.getStyle();
        if (style != null)
          isDocumentStyle_ = style.equals("document");
        break;
      }
    }

    BindingInput bindingInput = bindingOperation.getBindingInput();
    SOAPBody soapBody = null;
    for (Iterator i = bindingInput.getExtensibilityElements().iterator();i.hasNext();)
    {
      ExtensibilityElement e = (ExtensibilityElement)i.next();
      if (e instanceof SOAPBody)
      {
        soapBody = (SOAPBody)e;
        isUseLiteral_ = "literal".equals(soapBody.getUse());
        if (!isUseLiteral_)
        {
          // Encoded.
          for (Iterator j = soapBody.getEncodingStyles().iterator();j.hasNext();)
          {
            encodingStyle_ = (String)j.next();
            encodingNamespace_ = soapBody.getNamespaceURI();
            break;
          }
        }
        break;
      }
    }
  }

  public WSDLOperationElement(String name,WSDLBindingElement bindingElement,Operation operation)
  {
    super(name, bindingElement.getModel());
    // Set the default operation type to be SOAP.
    setOperation(bindingElement,operation);
  }

  private Definition getDefinition(WSDLBindingElement bindingElement) {
	  WSDLServiceElement serviceElement = (WSDLServiceElement) bindingElement.getParentElement();
      WSDLElement wsdlElement = (WSDLElement) serviceElement.getParentElement();
      return wsdlElement.getDefinition();
  }
  
  private void setMessageContext(WSDLBindingElement bindingElement) {
	  messageContext = new MessageContext();
	  messageContext.setDefinition(getDefinition(bindingElement));
	  messageContext.setBindingOperation(getBindingOperation(bindingElement));
	  messageContext.setBindingProtocol(bindingElement.getBindingExtensibilityElement());
	  messageContext.setDocumentStyle(isDocumentStyle_);
  }
  
  public MessageContext getMessageContext() {
	  return messageContext;
  }
  
  private void setSOAPTransportProvider(SOAPBinding soapBinding) {
	  	  
	  String namespaceURI = soapBinding.getElementType().getNamespaceURI();
	  String transportURI = soapBinding.getTransportURI();
	  	  
	  soapTransportProvider = TransportProviderRegistry.getInstance()
	  	.getSOAPTransportProvider(namespaceURI, transportURI);
  }
  
  public ISOAPTransportProvider getSOAPTransportProvider() {
	  return soapTransportProvider;
  }
  
  public void setOperation(WSDLBindingElement bindingElement,Operation operation) {
    operation_ = operation;
    setDocumentation(operation.getDocumentationElement());
    fragController_ = null;
    wsdlPartsToXsdTypeMapper_ = null;
    operationType_ = bindingElement.getBindingType();
    ExtensibilityElement bindingExtensibilityElement = bindingElement.getBindingExtensibilityElement();
    switch (operationType_)
    {
      case BindingTypes.SOAP:
        gatherSoapInformation(bindingElement,(SOAPBinding)bindingExtensibilityElement);
        setMessageContext(bindingElement);
        setSOAPTransportProvider((SOAPBinding) bindingExtensibilityElement);
        headerCache = new Hashtable();
      case BindingTypes.HTTP_GET:
      case BindingTypes.HTTP_POST:
      default:
        break;
    }
  }

  public Operation getOperation() {
    return operation_;
  }

  public BindingOperation getBindingOperation()
  {
    return getBindingOperation((WSDLBindingElement)getParentElement());
  }

  private BindingOperation getBindingOperation(WSDLBindingElement bindingElement)
  {
    Binding binding = bindingElement.getBinding();
    String operationInputName = null;
    String operationOutputName = null;
    Input operationInput = operation_.getInput();
    Output operationOutput = operation_.getOutput();
    if (operationInput != null)
      operationInputName = operationInput.getName();
    if (operationOutput != null)
      operationOutputName = operationOutput.getName();
    BindingOperation bindingOperation = binding.getBindingOperation(operation_.getName(),operationInputName,operationOutputName);
    if (bindingOperation == null)
      bindingOperation = binding.getBindingOperation(operation_.getName(),null,null);
    return bindingOperation;
  }

  /**
   * Return a list of input headers.
   * 
   * @return A List
   */
  public List getSOAPHeaders() {
	  return getSOAPHeaders(true);
  }
	  
  /**
   * Return a list of headers.
   * 
   * @param isInput If true, returns the input headers.  If false, returns the output headers.
   * @return A List
   */
  public List getSOAPHeaders(boolean isInput) {
	  List headers = new Vector();
	
      BindingOperation bindingOperation = getBindingOperation();
	  List extensibilityElements = isInput ?
			bindingOperation.getBindingInput().getExtensibilityElements() : 
			bindingOperation.getBindingOutput().getExtensibilityElements();	  	  
	  
	  for (Iterator it = extensibilityElements.iterator(); it.hasNext();) {
	      
		  ExtensibilityElement e = (ExtensibilityElement) it.next();
	      
	      if (e instanceof SOAPHeader && !headers.contains(e))	    	 
    		  headers.add(e);
	  }   
	  
	  return headers;
  }  
  
  public List getOrderedBodyParts()
  {
    List parts = new Vector(operation_.getInput().getMessage().getOrderedParts(operation_.getParameterOrdering()));
    BindingOperation bindingOperation = getBindingOperation();
    BindingInput bindingInput = bindingOperation.getBindingInput();
    for (Iterator it = bindingInput.getExtensibilityElements().iterator(); it.hasNext();)
    {
      ExtensibilityElement e = (ExtensibilityElement)it.next();
      if (e instanceof SOAPBody)
      {
        SOAPBody soapBody = (SOAPBody)e;
        List bodyParts = soapBody.getParts();
        if (bodyParts != null)
        {
          for (int i = 0; i < parts.size(); i++)
          {
            Part part = (Part)parts.get(i);
            if (!bodyParts.contains(part) && !bodyParts.contains(part.getName()))
            {
              parts.remove(i);
              i--;
            }
          }
        }
        break;
      }
    }
    return parts;
  }

  private XSDToFragmentController getXSDToFragmentController() {
    if (fragController_ == null) {
      fragController_ = new XSDToFragmentController();
      fragController_.setWSDLPartsToXSDTypeMapper(wsdlPartsToXsdTypeMapper_);
    }
    return fragController_;
  }

  private XSDNamedComponent getSchema(Part part, String id) {
    if (wsdlPartsToXsdTypeMapper_ == null) {
      wsdlPartsToXsdTypeMapper_ = new WSDLPartsToXSDTypeMapper();
      WSDLBindingElement bindingElement = (WSDLBindingElement)getParentElement();
      WSDLServiceElement serviceElement = (WSDLServiceElement)bindingElement.getParentElement();
      WSDLElement wsdlElement = (WSDLElement)serviceElement.getParentElement();
      wsdlPartsToXsdTypeMapper_.addSchemas(wsdlElement.getSchemaList());
    }
    return wsdlPartsToXsdTypeMapper_.getXSDType(part, id);
  }

  public IXSDFragment getHeaderFragment(SOAPHeader soapHeader) {
	return getHeaderFragment(soapHeader, true);
  }
  
  public IXSDFragment getHeaderFragment(SOAPHeader soapHeader, boolean isInput) {
	StringBuffer id = new StringBuffer();
	Part part = soapHeader.getEPart();
	if (isInput)
	  id.append(FragmentConstants.INPUT_ID).append(soapHeader.getMessage()).append(FragmentConstants.PART_TOKEN);
	else
	  id.append(FragmentConstants.OUTPUT_ID).append(soapHeader.getMessage()).append(FragmentConstants.PART_TOKEN);
	
	return getFragment(part, id, true, isInput); // only wrap input header fragments
  }
  
  public IXSDFragment getFragment(Part part) {
    return getFragment(part, true);
  }

  public IXSDFragment getFragment(Part part, boolean isInput) {
    StringBuffer id = new StringBuffer();
    if (isInput)
      id.append(FragmentConstants.INPUT_ID);
    else
      id.append(FragmentConstants.OUTPUT_ID);

	return getFragment(part, id, false, false);
  }
  
  private IXSDFragment getFragment(Part part, StringBuffer id, boolean isHeader, boolean useSOAPHeaderWrapper) {
    String partName = part.getName();
    id.append(partName);
    XSDToFragmentConfiguration config = new XSDToFragmentConfiguration();
    config.setIsWSDLPart(true);
    config.setWSDLPartName(partName);
    config.setXSDComponent(getSchema(part, id.toString()));
    if (isDocumentStyle() || isHeader)
      config.setStyle(FragmentConstants.STYLE_DOCUMENT);
    else
      config.setStyle(FragmentConstants.STYLE_RPC);
    if (operationType_ == BindingTypes.SOAP)
    {
      if (!isUseLiteral_)
        config.setPartEncoding(FragmentConstants.ENCODING_SOAP);
    }
    else
      config.setPartEncoding(FragmentConstants.ENCODING_URL);
    IXSDFragment fragment = getXSDToFragmentController().getFragment(config, id.toString(), part.getName());
     
    // let's see if there's a corresponding wrapper for this fragment
    if (useSOAPHeaderWrapper && !(fragment instanceof SOAPHeaderWrapperFragment)) {
    	SOAPHeaderWrapperFragment wrapper = (SOAPHeaderWrapperFragment) headerCache.get(fragment.getID());
    	
    	// no wrapper, let's wrap it
    	if (wrapper == null) {
    		wrapper = new SOAPHeaderWrapperFragment(fragment);    		
    		headerCache.put(fragment.getID(), wrapper);
    		
    		// also put this wrapper fragment in the master cache
    		getXSDToFragmentController().addToCache(wrapper.getID(), wrapper);
    	}
    	
    	return wrapper;
    }
    
    return fragment;
  }

  public IXSDFragment getFragmentByID(String id) {
    return getXSDToFragmentController().getCachedFragment(id);
  }

  public void removeAllFragment() {
    getXSDToFragmentController().emptyCache();
  }

  public boolean isDocumentStyle()
  {
    return isDocumentStyle_;
  }

  public String getSoapAction()
  {
    return soapAction_;
  }

  public boolean isUseLiteral()
  {
    return isUseLiteral_;
  }

  public String getEncodingStyle()
  {
    return encodingStyle_;
  }

  public String getEncodingNamespace()
  {
    return encodingNamespace_;
  }

  public int getOperationType()
  {
    return operationType_;
  }
}
