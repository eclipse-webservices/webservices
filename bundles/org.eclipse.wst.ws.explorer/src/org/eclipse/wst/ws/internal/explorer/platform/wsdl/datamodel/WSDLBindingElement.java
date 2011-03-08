/*******************************************************************************
 * Copyright (c) 2004, 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributors:

 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20110301 336771   mahutch@ca.ibm.com  - Mark Hutchinson, Test with Web Services Error when WSDL has more than one Operation 
 * 
 *******************************************************************************/
package org.eclipse.wst.ws.internal.explorer.platform.wsdl.datamodel;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import javax.wsdl.Binding;
import javax.wsdl.BindingOperation;
import javax.wsdl.Input;
import javax.wsdl.Operation;
import javax.wsdl.Output;
import javax.wsdl.PortType;
import javax.wsdl.extensions.ExtensibilityElement;
import javax.wsdl.extensions.http.HTTPBinding;
import javax.wsdl.extensions.soap.SOAPBinding;
import javax.xml.namespace.QName;
import org.eclipse.wst.ws.internal.datamodel.Model;
import org.eclipse.wst.ws.internal.explorer.platform.constants.ModelConstants;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.constants.BindingTypes;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.constants.WSDLModelConstants;

public class WSDLBindingElement extends WSDLCommonElement
{
  private int bindingType_;
  private Binding binding_;
  private ExtensibilityElement bindingExtensibilityElement_;
  private Vector endPoints_;

  private final void setBindingExtensibilityElement()
  {
    bindingExtensibilityElement_ = null;
    bindingType_ = BindingTypes.UNSUPPORTED;
    List extensibilityElements = binding_.getExtensibilityElements();
    for (Iterator i = extensibilityElements.iterator();i.hasNext();)
    {
      ExtensibilityElement e = (ExtensibilityElement)i.next();
      if (e instanceof HTTPBinding)
      {
        HTTPBinding binding = (HTTPBinding)e;
        String verb = binding.getVerb();
        if (BindingTypes.HTTP_VERB_GET.equals(verb))
          bindingType_ = BindingTypes.HTTP_GET;
        else if (BindingTypes.HTTP_VERB_POST.equals(verb))
          bindingType_ = BindingTypes.HTTP_POST;
      }
      else if (e instanceof SOAPBinding)
        bindingType_ = BindingTypes.SOAP;
      if (bindingType_ != BindingTypes.UNSUPPORTED)
      {
        bindingExtensibilityElement_ = e;
        break;
      }
    }
  }
  
  public WSDLBindingElement(String name, Model model, Binding binding)
  {
    super(name, model);
    setBinding(binding);
    endPoints_ = new Vector();
  }

  public void setBinding(Binding binding) {
    binding_ = binding;
    setDocumentation(binding.getDocumentationElement());
    setBindingExtensibilityElement();
  }

  public Binding getBinding() {
    return binding_;
  }
  
  public ExtensibilityElement getBindingExtensibilityElement()
  {
    return bindingExtensibilityElement_;
  }
  
  public int getBindingType()
  {
    return bindingType_;
  }

  private void resetEndPoints()
  {
    WSDLServiceElement serviceElement = (WSDLServiceElement)getParentElement();
    String fixedEndpointString = serviceElement.getAddressLocation(binding_);
    Endpoint fixedEndpoint = getEndpoint(fixedEndpointString);
    if (fixedEndpoint == null)
    {
      fixedEndpoint = new Endpoint();
      fixedEndpoint.setEndpoint(fixedEndpointString);
    }
    endPoints_.clear();
    endPoints_.add(fixedEndpoint);
  }

  public String[] getEndPoints()
  {
    String[] endPoints = new String[endPoints_.size()];
    for (int i = 0; i < endPoints.length; i++)
      endPoints[i] = ((Endpoint)endPoints_.get(i)).getEndpoint();
    return endPoints;
  }
  
  public Endpoint getEndpoint(String endpointString)
  {
    if (endpointString != null && endpointString.length() > 0)
    {
      for (Iterator it = endPoints_.iterator(); it.hasNext();)
      {
        Endpoint endpoint = (Endpoint)it.next();
        if (endpointString.equals(endpoint.getEndpoint()))
          return endpoint;
      }
    }
    return null;
  }

  public void setEndPoints(String[] endpoints)
  {
    Vector endpointVector = new Vector();
    for (int i = 0; i < endpoints.length; i++)
    {
      Endpoint endpoint = getEndpoint(endpoints[i]);
      if (endpoint == null)
      {
        endpoint = new Endpoint();
        endpoint.setEndpoint(endpoints[i]);
      }
      endpointVector.add(endpoint);
    }
    resetEndPoints();
    endPoints_.addAll(endpointVector);
  }

  public void addEndPoint(String endpointString)
  {
    if (endpointString != null && endpointString.length() > 0)
    {
      Endpoint endpoint = new Endpoint();
      endpoint.setEndpoint(endpointString);
      endPoints_.add(endpoint);
    }
  }

  public void removeEndPoint(String endpointString)
  {
    if (endpointString != null && endpointString.length() > 0)
    {
      WSDLServiceElement serviceElement = (WSDLServiceElement)getParentElement();
      String fixedEndpointString = serviceElement.getAddressLocation(binding_);
      if (fixedEndpointString == null || !fixedEndpointString.equals(endpointString))
      {
        for (int i = 0; i < endPoints_.size(); i++)
        {
          if (endpointString.equals(((Endpoint)endPoints_.get(i)).getEndpoint()))
          {
            endPoints_.remove(i);
            break;
          }
        }
      }
    }
  }

  public void buildModel() {
    resetEndPoints();
    if (binding_ != null) {
      PortType pt = binding_.getPortType();
      if (pt != null) {
        List operations = pt.getOperations();
        HashMap operationsMap = new HashMap();
        for (Iterator it = operations.iterator();it.hasNext();) {
          Operation operation = (Operation)it.next();
          operationsMap.put(createOperationUniqueName(operation), operation);
        }
        WSDLOperationElement[] wsdlOperationElements = new WSDLOperationElement[getNumberOfElements(WSDLModelConstants.REL_WSDL_OPERATION)];
        Enumeration e = getElements(WSDLModelConstants.REL_WSDL_OPERATION);
        for (int i = 0; i < wsdlOperationElements.length; i++) {
          wsdlOperationElements[i] = (WSDLOperationElement)e.nextElement();
        }
        for (int j = 0; j < wsdlOperationElements.length; j++) {
          String operationName = createOperationUniqueName(wsdlOperationElements[j].getOperation());
          Operation operation = (Operation)operationsMap.get(operationName);
          if (operation != null) {
            operationsMap.remove(operationName);
            wsdlOperationElements[j].setOperation(this, operation);
          }
          else
            disconnect(wsdlOperationElements[j], WSDLModelConstants.REL_WSDL_OPERATION);
        }
        for (Iterator it = operationsMap.values().iterator();it.hasNext();) {
          Operation oper = (Operation)it.next();
          if (isBindingOperation(oper)) {
	          //if the operation is in the port type, but not in the binding then don't process the operation element
	          //only operations that are in the binding should show in the WSE
	          WSDLOperationElement wsdlOperationElement = new WSDLOperationElement(oper.getName(), this, oper);
	          connect(wsdlOperationElement,WSDLModelConstants.REL_WSDL_OPERATION,ModelConstants.REL_OWNER);
          }
        }
      }
    }
  }
  
  private boolean isBindingOperation(Operation operation)  {
    String operationInputName = null;
    String operationOutputName = null;
    Input operationInput = operation.getInput();
    Output operationOutput = operation.getOutput();
    if (operationInput != null)
      operationInputName = operationInput.getName();
    if (operationOutput != null)
      operationOutputName = operationOutput.getName();
    BindingOperation bindingOperation = binding_.getBindingOperation(operation.getName(),operationInputName,operationOutputName);
    if (bindingOperation == null)
      bindingOperation = binding_.getBindingOperation(operation.getName(),null,null);
    return bindingOperation != null;
  }

  private String createOperationUniqueName(Operation operation)
  {
    StringBuffer name = new StringBuffer();
    name.append(operation.getName());
    Input inputMsg = operation.getInput();
    if (inputMsg != null)
    {
      QName qname = inputMsg.getMessage().getQName();
      name.append(qname.getNamespaceURI());
      name.append(":");
      name.append(qname.getLocalPart());
    }
    Output outputMsg = operation.getOutput();
    if (outputMsg != null)
    {
      QName qname = outputMsg.getMessage().getQName();
      name.append(qname.getNamespaceURI());
      name.append(":");
      name.append(qname.getLocalPart());
    }
    return name.toString();
  }
}
