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
package org.eclipse.wst.wsdl.ui.internal.contentgenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


import org.eclipse.wst.wsdl.Binding;
import org.eclipse.wst.wsdl.BindingOperation;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.Fault;
import org.eclipse.wst.wsdl.Input;
import org.eclipse.wst.wsdl.Operation;
import org.eclipse.wst.wsdl.Output;
import org.eclipse.wst.wsdl.PortType;
import org.eclipse.wst.wsdl.Service;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.actions.AddElementAction;
import org.eclipse.wst.wsdl.ui.internal.util.ComponentReferenceUtil;
import org.eclipse.wst.wsdl.ui.internal.util.WSDLEditorUtil;
import org.eclipse.wst.wsdl.internal.util.WSDLConstants;
import org.w3c.dom.Element;
import org.w3c.dom.Node;


public class BindingGenerator extends AbstractGenerator
{
  protected Definition definition;
  protected String portTypeName;
  protected boolean generateBindingBody = true; 

  public BindingGenerator(Definition definition)
  {
    this.definition = definition;
  }
  
  public void setGenerateBindingBody(boolean generateBindingBody)
  {
    this.generateBindingBody = generateBindingBody;
  }
  
  public int getType()
  {
  	return BINDING_GENERATOR;
  }

  public Definition getDefinition()
  {
    return this.definition;
  }

  protected String getUndoDescription()
  {
    return WSDLEditorPlugin.getWSDLString("_UI_MENUBAR_GENERATE_BINDING");
  }

  public void setRefName(String refName)
  {
    setPortTypeName(refName);
  }
  
  public String getRefName()
  {
  	return portTypeName;
  }
 
  public void setPortTypeName(String portTypeName)
  {
    this.portTypeName = portTypeName;
  }

  public String getPortTypeName()
  {
    return portTypeName;
  }

  class BindingOperationTable
  {
    HashMap map = new HashMap();

    public void addBinding(Binding binding)
    {
      for (Iterator i = binding.getEBindingOperations().iterator(); i.hasNext();)
      {
        putBindingOperation((BindingOperation) i.next());
      }
    }

    public void putBindingOperation(BindingOperation bindingOperation)
    {
      String key = bindingOperation.getName();
      List list = (List) map.get(key);
      if (list == null)
      {
        list = new ArrayList();
        map.put(key, list);
      }
      if (!list.contains(bindingOperation))
      {
        list.add(bindingOperation);
      }
    }

    public BindingOperation lookupBindingOperation(Operation operation)
    {
      BindingOperation bindingOperation = null;

      List list = (List) map.get(operation.getName());
      if (list != null && list.size() > 0)
      {
        bindingOperation = (BindingOperation) list.get(0);
      }

      return bindingOperation;
    }
  }

  public void generateContent()
  {
    Element definitionElement = WSDLEditorUtil.getInstance().getElementForObject(definition);

    Binding binding = getOrCreateBinding(name, definitionElement);
	newComponent = binding;
    if (binding != null)
    {
      ComponentReferenceUtil.setPortTypeReference(binding, portTypeName != null ? portTypeName : "");
    }

    Element bindingElement = binding != null ? WSDLEditorUtil.getInstance().getElementForObject(binding) : null;

    if (bindingElement != null)
    {
      if (overwrite)
      {
        List nodes = new ArrayList();
        for (Node node = bindingElement.getFirstChild(); node != null; node = node.getNextSibling())
        {
          nodes.add(node);
        }
        for (Iterator i = nodes.iterator(); i.hasNext();)
        {
          Node node = (Node) i.next();
          bindingElement.removeChild(node);
        }
      }

	  generatePortContent(binding);
      PortType portType = binding.getEPortType();
           
      if (binding.getEExtensibilityElements().size() == 0)
      {
         bindingContentGenerator.generateBindingContent(bindingElement, portType);
      }

	  if (portType != null && generateBindingBody)
	  {	  
			BindingOperationTable table = new BindingOperationTable();
			table.addBinding(binding);
				  	
        for (Iterator i = portType.getEOperations().iterator(); i.hasNext();)
        {
          Operation operation = (Operation) i.next();
          BindingOperation bindingOperation = table.lookupBindingOperation(operation);
          if (bindingOperation == null)
          {
            generateBindingOperation(binding, bindingElement, operation);
          }
        }
      }

    }
  }

  protected void generatePortContent(Binding binding)
  {
    for (Iterator i = definition.getEServices().iterator(); i.hasNext();)
    {
      Service service = (Service) i.next();
      /*
      for (Iterator j = service.getEPorts().iterator(); j.hasNext();)
      {
        Port port = (Port) j.next();
        if (port.getBinding() == binding)
        {
          if (port.getEExtensibilityElements().size() == 0)
          {
            Element portElement = WSDLUtil.getInstance().getElementForObject(port);
            bindingContentGenerator.generatePortContent(portElement, port);
          }
        }
      }*/
    }
  }

  protected void generateBindingOperation(Binding binding, Element bindingElement, Operation operation)
  {
    Element bindingOperationElement = createWSDLElement(bindingElement, "operation");
    bindingOperationElement.setAttribute("name", operation.getName());
    bindingContentGenerator.generateBindingOperationContent(bindingOperationElement, operation);

    Input input = operation.getEInput();
    if (input != null)
    {
      Element bindingInputElement = createWSDLElement(bindingOperationElement, "input");
      if (input.getName() != null)
      {
        bindingInputElement.setAttribute("name", input.getName());
      }
      bindingContentGenerator.generateBindingInputContent(bindingInputElement, input);
    }

    Output output = operation.getEOutput();
    if (output != null)
    {
      Element bindingOutputElement = createWSDLElement(bindingOperationElement, "output");
      if (output.getName() != null)
      {
        bindingOutputElement.setAttribute("name", output.getName());
      }
      bindingContentGenerator.generateBindingOutputContent(bindingOutputElement, output);
    }

    for (Iterator i = operation.getEFaults().iterator(); i.hasNext();)
    {
      Fault fault = (Fault) i.next();

      Element bindingFaultElement = createWSDLElement(bindingOperationElement, "fault");
      if (fault.getName() != null)
      {
        bindingFaultElement.setAttribute("name", fault.getName());
      }
      bindingContentGenerator.generateBindingFaultContent(bindingFaultElement, fault);
    }
  }

  protected Binding getOrCreateBindingHelper(String name)
  {
    Binding result = null;
    if (name != null)
    {
      for (Iterator i = getDefinition().getEBindings().iterator(); i.hasNext();)
      {
        Binding binding = (Binding) i.next();
        String bindingName = binding.getQName().getLocalPart();
        if (name.equals(bindingName))
        {
          result = binding;
          break;
        }
      }
    }
    return result;
  }

  protected Binding getOrCreateBinding(String name, Element definitionElement)
  {
    Binding result = getOrCreateBindingHelper(name);
    if (result == null)
    {
      String prefix = definition.getPrefix(WSDLConstants.WSDL_NAMESPACE_URI);
      AddBindingAction action = new AddBindingAction(definition, name, definitionElement, prefix);
      action.run();
      result = getOrCreateBindingHelper(name);
    }
    return result;
  }

  public Node getParentNode()
  {
    return WSDLEditorUtil.getInstance().getElementForObject(definition);
  }
}

class AddBindingAction extends AddElementAction
{
  protected String name;
  protected Definition definition;

  public AddBindingAction(Definition definition, String name, Node parentNode, String prefix)
  {
    super("binding", "icons/binding_obj.gif", parentNode, prefix, "binding");
    this.definition = definition;
    this.name = name;
    setComputeTopLevelRefChild(true);
  }

  protected void addAttributes(Element newElement)
  {
    newElement.setAttribute("name", name);
    newElement.setAttribute("type", "");
  }

  public String getBindingName()
  {
    return name;
  }
}