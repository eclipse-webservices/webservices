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

import org.eclipse.wst.ws.internal.datamodel.*;
import org.eclipse.wst.ws.internal.explorer.platform.constants.*;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.constants.*;

import javax.xml.namespace.QName;
import javax.wsdl.extensions.soap.*;
import javax.wsdl.extensions.http.*;
import javax.wsdl.extensions.*;
import javax.wsdl.*;

import java.util.*;

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
