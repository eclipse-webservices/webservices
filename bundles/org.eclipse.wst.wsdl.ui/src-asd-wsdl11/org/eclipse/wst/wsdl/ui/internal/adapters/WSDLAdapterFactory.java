/*******************************************************************************
 * Copyright (c) 2001, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.ui.internal.adapters;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
import org.eclipse.wst.wsdl.Binding;
import org.eclipse.wst.wsdl.BindingFault;
import org.eclipse.wst.wsdl.BindingInput;
import org.eclipse.wst.wsdl.BindingOperation;
import org.eclipse.wst.wsdl.BindingOutput;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.ExtensibilityElement;
import org.eclipse.wst.wsdl.Fault;
import org.eclipse.wst.wsdl.Import;
import org.eclipse.wst.wsdl.Input;
import org.eclipse.wst.wsdl.Message;
import org.eclipse.wst.wsdl.MessageReference;
import org.eclipse.wst.wsdl.Operation;
import org.eclipse.wst.wsdl.Output;
import org.eclipse.wst.wsdl.Part;
import org.eclipse.wst.wsdl.Port;
import org.eclipse.wst.wsdl.PortType;
import org.eclipse.wst.wsdl.Service;
import org.eclipse.wst.wsdl.binding.http.HTTPAddress;
import org.eclipse.wst.wsdl.binding.soap.SOAPAddress;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.adapters.basic.W11Binding;
import org.eclipse.wst.wsdl.ui.internal.adapters.basic.W11BindingMessageReference;
import org.eclipse.wst.wsdl.ui.internal.adapters.basic.W11BindingOperation;
import org.eclipse.wst.wsdl.ui.internal.adapters.basic.W11Description;
import org.eclipse.wst.wsdl.ui.internal.adapters.basic.W11EndPoint;
import org.eclipse.wst.wsdl.ui.internal.adapters.basic.W11Import;
import org.eclipse.wst.wsdl.ui.internal.adapters.basic.W11Interface;
import org.eclipse.wst.wsdl.ui.internal.adapters.basic.W11Message;
import org.eclipse.wst.wsdl.ui.internal.adapters.basic.W11MessageReference;
import org.eclipse.wst.wsdl.ui.internal.adapters.basic.W11Operation;
import org.eclipse.wst.wsdl.ui.internal.adapters.basic.W11ParameterForAttribute;
import org.eclipse.wst.wsdl.ui.internal.adapters.basic.W11ParameterForElement;
import org.eclipse.wst.wsdl.ui.internal.adapters.basic.W11ParameterForPart;
import org.eclipse.wst.wsdl.ui.internal.adapters.basic.W11Service;
import org.eclipse.wst.wsdl.ui.internal.adapters.basic.W11Type;
import org.eclipse.wst.wsdl.ui.internal.adapters.specialized.W11AddressExtensibilityElementAdapter;
import org.eclipse.wst.wsdl.ui.internal.adapters.specialized.W11ExtensibilityElementAdapter;
import org.eclipse.wst.wsdl.ui.internal.asd.adapterfactory.extension.AdapterFactoryExtension;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IMessageReference;
import org.eclipse.xsd.XSDAttributeUse;
import org.eclipse.xsd.XSDConcreteComponent;
import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDSchema;

public class WSDLAdapterFactory extends AdapterFactoryImpl
{
  static WSDLAdapterFactory instance;
  
  public static WSDLAdapterFactory getInstance()
  {
    if (instance == null)
    {
      instance = new WSDLAdapterFactory();
    }  
    return instance;
  }
  
  public Adapter createAdapter(Notifier target)
  {
	  Adapter adapter = null;

	  if (target instanceof Definition) {
		  adapter = new W11Description();
	  }
	  else if (target instanceof Service) {
		  adapter = new W11Service();
	  }
	  else if (target instanceof Binding) {
		  adapter = new W11Binding();
	  }
      else if (target instanceof BindingOperation) {
          adapter = new W11BindingOperation();
      }
      else if (target instanceof BindingInput ||
               target instanceof BindingOutput ||
               target instanceof BindingFault)
      {  
		  adapter = new W11BindingMessageReference();
	  }
	  else if (target instanceof Port) {
		  adapter = new W11EndPoint();
	  }
      else if (target instanceof PortType) {
          adapter = new W11Interface();
      }  
	  else if (target instanceof Operation) {
		  adapter = new W11Operation();
	  }
	  else if (target instanceof MessageReference)
	  {
	    int kind = -1;
	    if (target instanceof Input)
	    {
	      kind = IMessageReference.KIND_INPUT;
	    }
	    else if (target instanceof Output)
	    {
	      kind = IMessageReference.KIND_OUTPUT;
	    }
	    else if (target instanceof Fault)
	    {
	      kind = IMessageReference.KIND_FAULT;
	    }
	    adapter = new W11MessageReference(kind);
	  }
	  else if (target instanceof Import) {
		  adapter = new W11Import();
	  }
	  else if (target instanceof XSDSchema) {
		  adapter = new W11Type();
	  }
	  else if (target instanceof Message) {
		  adapter = new W11Message();
	  }
      else if (target instanceof Part)
      {
    	  adapter = new W11ParameterForPart(); 
      }
      else if (target instanceof XSDElementDeclaration)
      {
          adapter = new W11ParameterForElement();
      }
      else if (target instanceof XSDAttributeUse)
      {
          adapter = new W11ParameterForAttribute();
      }        
      else if (target instanceof Message || 
               target instanceof XSDConcreteComponent)
      {
        // not we only need this adapter to serve as an 'otherThingsToListenTo' adapter
        // for use by the W11MessageReference when computing a parameter list
        adapter = new WSDLBaseAdapter();
      }  
      else if (target instanceof SOAPAddress ||
               target instanceof HTTPAddress)
      {
        // TODO.. we need to enable the factory to delegate to extension language adapter factories
        // there should be no references to SOAP or HTTP stuff from within this class
        //
        adapter = new W11AddressExtensibilityElementAdapter();        
      }         
      else if (target instanceof ExtensibilityElement)
      {
    	  Adapter extensibilityAdapter = null;
    	  ExtensibilityElement extElement = (ExtensibilityElement) target;
    	  String namespace = extElement.getElementType().getNamespaceURI();
    	  AdapterFactoryExtension extension = WSDLEditorPlugin.getInstance().getAdapterFactoryExtensionRegistry().getExtensionForNamespace(namespace);
    	  if (extension != null) {
    		  AdapterFactory factory = extension.getAdapterFactory();
    		  extensibilityAdapter = factory.adapt(target, Adapter.class);
    	  }
    	  
    	  if (extensibilityAdapter == null) {
    		  adapter = new W11ExtensibilityElementAdapter();
    	  }
    	  else {
    		  adapter = extensibilityAdapter;
    	  }
      }  
	  if (adapter == null)
	  {
	    System.out.println("NO ADAPTER CREATED FOR " + target);    //$NON-NLS-1$
	    Thread.dumpStack();
	  }

	  return adapter;		  
  }
  
  public Adapter adapt(Notifier target)
  {
    return adapt(target, this);
  }
}
