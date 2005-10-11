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
package org.eclipse.wst.ws.internal.explorer.platform.wsdl.datamodel;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.wsdl.Binding;
import javax.wsdl.Port;
import javax.wsdl.Service;
import javax.wsdl.extensions.ExtensibilityElement;
import javax.wsdl.extensions.http.HTTPAddress;
import javax.wsdl.extensions.soap.SOAPAddress;
import javax.xml.namespace.QName;
import org.eclipse.wst.ws.internal.datamodel.Model;
import org.eclipse.wst.ws.internal.explorer.platform.constants.ModelConstants;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.constants.WSDLModelConstants;

public class WSDLServiceElement extends WSDLCommonElement
{

  private Service service_;

  public WSDLServiceElement(String name, Model model, Service service)
  {
    super(name, model);
    setService(service);
  }
  
  public void setService(Service service) {
    service_ = service;
    setDocumentation(service.getDocumentationElement());
  }

  public Service getService() {
    return service_;
  }
  
  public String getAddressLocation(Binding binding)
  {
    Map ports = service_.getPorts();
    for (Iterator i = ports.values().iterator();i.hasNext();)
    {
      Port port = (Port)i.next();
      if (port.getBinding().getQName().equals(binding.getQName()))
      {
        List extensibilityElements = port.getExtensibilityElements();
        for (Iterator j = extensibilityElements.iterator();j.hasNext();)
        {
          ExtensibilityElement element = (ExtensibilityElement)j.next();
          if (element instanceof SOAPAddress)
          {
            SOAPAddress soapAddress = (SOAPAddress)element;
            return soapAddress.getLocationURI();
          }
          else if (element instanceof HTTPAddress)
          {
            HTTPAddress httpAddress = (HTTPAddress)element;
            return httpAddress.getLocationURI();
          }
        }
      }
    }
    return null;
  }

  public void buildModel() {
    if (service_ != null) {
      Map ports = service_.getPorts();
      Map bindings = new HashMap();
      for (Iterator it = ports.values().iterator();it.hasNext();) {
        Port port = (Port)it.next();
        Binding binding = port.getBinding();
        if (binding.getPortType() == null)
          continue;
        else
          bindings.put(binding.getQName(), binding);
      }
      WSDLBindingElement[] wsdlBindingElements = new WSDLBindingElement[getNumberOfElements(WSDLModelConstants.REL_WSDL_BINDING)];
      Enumeration e = getElements(WSDLModelConstants.REL_WSDL_BINDING);
      for (int i = 0; i < wsdlBindingElements.length; i++) {
        wsdlBindingElements[i] = (WSDLBindingElement)e.nextElement();
      }
      for (int j = 0; j < wsdlBindingElements.length; j++) {
        QName qname = wsdlBindingElements[j].getBinding().getQName();
        Binding binding = (Binding)bindings.get(qname);
        if (binding != null) {
          bindings.remove(qname);
          wsdlBindingElements[j].setBinding(binding);
          wsdlBindingElements[j].buildModel();
        }
        else
          disconnect(wsdlBindingElements[j], WSDLModelConstants.REL_WSDL_BINDING);
      }
      for (Iterator it = bindings.values().iterator();it.hasNext();) {
        Binding binding = (Binding)it.next();
        WSDLBindingElement wsdlBindingElement = new WSDLBindingElement(binding.getQName().getLocalPart(), getModel(), binding);
        connect(wsdlBindingElement,WSDLModelConstants.REL_WSDL_BINDING,ModelConstants.REL_OWNER);
        wsdlBindingElement.buildModel();
      }
    }
  }
}
