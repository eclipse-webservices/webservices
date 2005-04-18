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
package org.eclipse.wst.wsdl.ui.internal.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import org.eclipse.core.resources.IFile;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.wst.wsdl.Binding;
import org.eclipse.wst.wsdl.BindingFault;
import org.eclipse.wst.wsdl.BindingInput;
import org.eclipse.wst.wsdl.BindingOperation;
import org.eclipse.wst.wsdl.BindingOutput;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.Fault;
import org.eclipse.wst.wsdl.Input;
import org.eclipse.wst.wsdl.Message;
import org.eclipse.wst.wsdl.Operation;
import org.eclipse.wst.wsdl.Output;
import org.eclipse.wst.wsdl.Part;
import org.eclipse.wst.wsdl.Port;
import org.eclipse.wst.wsdl.PortType;
import org.eclipse.wst.wsdl.Service;
import org.eclipse.wst.wsdl.WSDLElement;
import org.eclipse.wst.wsdl.internal.impl.ImportImpl;
import org.eclipse.wst.wsdl.internal.impl.WSDLElementImpl;
import org.eclipse.wst.wsdl.ui.internal.extension.ITypeSystemProvider;
import org.eclipse.wst.wsdl.util.WSDLConstants;
import org.eclipse.wst.xml.uriresolver.util.URIHelper;
import org.w3c.dom.Element;
import org.w3c.dom.Node;


public class ComponentReferenceUtil
{
  protected Definition rootDefinition;

  public ComponentReferenceUtil(Definition rootDefinition)
  {
    this.rootDefinition = rootDefinition;
  }

  protected static boolean isEqual(String a, String b)
  {
    boolean result = false;
    if (a != null)
    {
      result = a.equals(b) || (a.length() == 0 && b == null);
    }
    else
    {
      result = (b == null || b.length() == 0);
    }
    return result;
  }

  protected static boolean isEqualInputName(Operation operation, BindingOperation bindingOperation)
  {
    boolean result = false;
    Input operationInput = operation.getEInput();
    BindingInput bindingOperationInput = bindingOperation.getEBindingInput();

    if (operationInput != null && bindingOperationInput != null)
    {
      result = isEqual(operationInput.getName(), bindingOperationInput.getName());
    }
    else if (operationInput == null && bindingOperationInput == null)
    {
      result = true;
    }
    return result;
  }

  protected static boolean isEqualOutputName(Operation operation, BindingOperation bindingOperation)
  {
    boolean result = false;
    Output operationOutput = operation.getEOutput();
    BindingOutput bindingOperationOutput = bindingOperation.getEBindingOutput();

    if (operationOutput != null && bindingOperationOutput != null)
    {
      result = isEqual(operationOutput.getName(), bindingOperationOutput.getName());
    }
    else if (operationOutput == null && bindingOperationOutput == null)
    {
      result = true;
    }
    return result;
  }

  protected static boolean isMatchingBinding(Operation operation, BindingOperation bindingOperation)
  {
    return isEqual(operation.getName(), bindingOperation.getName()) && isEqualInputName(operation, bindingOperation) && isEqualOutputName(operation, bindingOperation);
  }

  public List getPortsForPortType(PortType portType)
  {
    List list = new ArrayList();
    for (Iterator i = getServices().iterator(); i.hasNext();)
    {
      Service service = (Service) i.next();
      for (Iterator j = service.getEPorts().iterator(); j.hasNext();)
      {
        Port port = (Port) j.next();
        Binding binding = port.getEBinding();
        if (binding != null && binding.getEPortType() == portType)
        {
          list.add(port);
        }
      }
    }
    return list;
  }

  public List getPortsForBinding(Binding binding)
  {
    List list = new ArrayList();
    for (Iterator i = getServices().iterator(); i.hasNext();)
    {
      Service service = (Service) i.next();
      for (Iterator j = service.getEPorts().iterator(); j.hasNext();)
      {
        Port port = (Port) j.next();
        if (port.getEBinding() == binding)
        {
          list.add(port);
        }
      }
    }
    return list;
  }

  public List getBindingOperations(Operation operation)
  {
    List list = new ArrayList();
    String operationName = operation.getName();

    if (operationName != null)
    {
      PortType portType = getEnclosingPortType(operation);
      for (Iterator i = getBindings(portType).iterator(); i.hasNext();)
      {
        Binding binding = (Binding) i.next();
        BindingOperation bindingOperation = getBindingOperation(operation, binding);
        if (bindingOperation != null)
        {
          list.add(bindingOperation);
        }
      }
    }
    return list;
  }

  public BindingOperation getBindingOperation(Operation operation, Binding binding)
  {
    BindingOperation result = null;
    for (Iterator j = binding.getBindingOperations().iterator(); j.hasNext();)
    {
      BindingOperation bindingOperation = (BindingOperation) j.next();
      if (isMatchingBinding(operation, bindingOperation))
      {
        result = bindingOperation;
        break;
      }
    }
    return result;
  }

  public BindingInput getBindingInput(Input input, Binding binding)
  {
    BindingOperation bindingOperation = getBindingOperation((Operation) input.eContainer(), binding);
    return bindingOperation != null ? bindingOperation.getEBindingInput() : null;
  }

  public BindingOutput getBindingOutput(Output output, Binding binding)
  {
    BindingOperation bindingOperation = getBindingOperation((Operation) output.eContainer(), binding);
    return bindingOperation != null ? bindingOperation.getEBindingOutput() : null;
  }

  public BindingFault getBindingFault(Fault fault, Binding binding)
  {
    BindingFault result = null;
    String faultName = fault.getName();
    if (faultName != null)
    {
      BindingOperation bindingOperation = getBindingOperation((Operation) fault.eContainer(), binding);
      if (bindingOperation != null)
      {
        result = (BindingFault) bindingOperation.getBindingFault(faultName);
      }
    }
    return result;
  }

  public EObject getBindingObject(EObject interfaceObject, Binding binding)
  {
    EObject result = null;
    if (interfaceObject instanceof Input)
    {
      result = getBindingInput((Input) interfaceObject, binding);
    }
    else if (interfaceObject instanceof Output)
    {
      result = getBindingOutput((Output) interfaceObject, binding);
    }
    else if (interfaceObject instanceof Fault)
    {
      result = getBindingFault((Fault) interfaceObject, binding);
    }
    else if (interfaceObject instanceof Operation)
    {
      result = getBindingOperation((Operation) interfaceObject, binding);
    }
    else if (interfaceObject instanceof PortType)
    {
      result = binding;
    }
    return result;
  }

  public List getBindingInputs(Input input)
  {
    List list = new ArrayList();
    List operations = getBindingOperations((Operation) input.eContainer());
    for (Iterator i = operations.iterator(); i.hasNext();)
    {
      BindingOperation bindingOperation = (BindingOperation) i.next();
      if (bindingOperation.getBindingInput() != null)
      {
        list.add(bindingOperation.getBindingInput());
      }
    }
    return list;
  }

  public List getBindingOutputs(Output output)
  {
    List list = new ArrayList();
    Operation operation = (Operation) output.eContainer();
    if (operation != null)
    {
      List operations = getBindingOperations(operation);
      for (Iterator i = operations.iterator(); i.hasNext();)
      {
        BindingOperation bindingOperation = (BindingOperation) i.next();
        if (bindingOperation.getBindingOutput() != null)
        {
          list.add(bindingOperation.getBindingOutput());
        }
      }
    }
    return list;
  }

  public List getBindingFaults(Fault fault)
  {
    List list = new ArrayList();
    String faultName = fault.getName();
    if (faultName != null)
    {
      Operation operation = (Operation) fault.eContainer();
      if (operation != null)
      {
        List operations = getBindingOperations(operation);
        for (Iterator i = operations.iterator(); i.hasNext();)
        {
          BindingOperation bindingOperation = (BindingOperation) i.next();
          BindingFault bindingFault = (BindingFault) bindingOperation.getBindingFault(faultName);
          if (bindingFault != null)
          {
            list.add(bindingFault);
          }
        }
      }
    }
    return list;
  }

  protected PortType getEnclosingPortType(Operation operation)
  {
    return (PortType) operation.eContainer();
  }

  public List getBindings(Operation operation)
  {
    return getBindings(getEnclosingPortType(operation));
  }

  public List getBindings(PortType portType)
  {
    List list = new ArrayList();
    for (Iterator i = getDefinitions().iterator(); i.hasNext();)
    {
      Definition definition = (Definition) i.next();
      for (Iterator j = definition.getBindings().values().iterator(); j.hasNext();)
      {
        Binding binding = (Binding) j.next();
        if (portType == null || binding.getPortType() == portType)
        {
          list.add(binding);
        }
      }
    }
    return list;
  }

  public List getBindingsWithoutOperation(PortType portType, String operationName)
  {
    List result = new ArrayList();
    if (operationName != null)
    {
      List bindings = getBindings(portType);
      for (Iterator i = bindings.iterator(); i.hasNext();)
      {
        Binding binding = (Binding) i.next();
        boolean hasName = false;
        for (Iterator j = binding.getBindingOperations().iterator(); j.hasNext();)
        {
          BindingOperation bindingOperation = (BindingOperation) j.next();
          if (operationName.equals(bindingOperation.getName()))
          {
            hasName = true;
            break;
          }
        }
        if (!hasName)
        {
          result.add(binding);
        }
      }
    }
    return result;
  }

  public List getBindings()
  {
    return getBindings((PortType) null);
  }

  public List getBindingNames()
  {
    List list = new ArrayList();
    for (Iterator i = getBindings().iterator(); i.hasNext();)
    {
      Binding binding = (Binding) i.next();
      list.addAll(getPrefixedNames(binding.getQName()));
    }
    return list;
  }

  public List getPortTypeNames()
  {
    List list = new ArrayList();
    for (Iterator i = getDefinitions().iterator(); i.hasNext();)
    {
      Definition definition = (Definition) i.next();
      for (Iterator j = definition.getPortTypes().values().iterator(); j.hasNext();)
      {
        PortType portType = (PortType) j.next();
        list.addAll(getPrefixedNames(portType.getQName()));
      }
    }
    return list;
  }

  public List getServices()
  {
    List list = new ArrayList();
    for (Iterator i = getDefinitions().iterator(); i.hasNext();)
    {
      Definition definition = (Definition) i.next();
      list.addAll(definition.getEServices());
    }
    return list;
  }

  public List getPortTypes()
  {
    List list = new ArrayList();
    for (Iterator i = getDefinitions().iterator(); i.hasNext();)
    {
      Definition definition = (Definition) i.next();
      list.addAll(definition.getEPortTypes());
    }
    return list;
  }

  public List getMessages()
  {
    List list = new ArrayList();
    for (Iterator i = getDefinitions().iterator(); i.hasNext();)
    {
      Definition definition = (Definition) i.next();
      list.addAll(definition.getEMessages());
    }
    return list;
  }

  public List getTypes()
  {
    List list = new ArrayList();
    for (Iterator i = getDefinitions().iterator(); i.hasNext();)
    {
      Definition definition = (Definition) i.next();
      if (definition.getETypes() != null)
      {
        list.add(definition.getETypes());
      }
    }
    return list;
  }

  public List getMessageNames()
  {
    List list = new ArrayList();
    for (Iterator i = getDefinitions().iterator(); i.hasNext();)
    {
      Definition definition = (Definition) i.next();
      for (Iterator j = definition.getMessages().values().iterator(); j.hasNext();)
      {
        Message message = (Message) j.next();
        list.addAll(getPrefixedNames(message.getQName()));
      }
    }
    return list;
  }

  public List getDefinitions()
  {
    List list = new ArrayList();
    addDefinition(rootDefinition, list);
    return list;
  }

  /**
   * @deprecated -- we should always use getPrefixedNames
   */
  public String getPrefixedName(QName qname)
  {
    String name = null;
    if (qname != null)
    {
      String prefix = rootDefinition.getPrefix(qname.getNamespaceURI());
      if (prefix != null)
      {
        name = prefix + ":" + qname.getLocalPart();
      }
    }
    return name;
  }

  public List getPrefixedNames(QName qname)
  {
    List list = new ArrayList();
    if (qname != null)
    {
      Map map = rootDefinition.getNamespaces();
      for (Iterator i = map.keySet().iterator(); i.hasNext();)
      {
        String prefix = (String) i.next();
        String namespace = (String) map.get(prefix);
        if (namespace != null && namespace.equals(qname.getNamespaceURI()))
        {
          String name = prefix.length() > 0 ? prefix + ":" + qname.getLocalPart() : 
                        qname.getLocalPart();
          list.add(name);
        }
      }
    }
    return list;
  }

  protected void addDefinition(Definition definition, List list)
  {
    if (definition != null)
    {
      list.add(definition);
      for (Iterator i = definition.getEImports().iterator(); i.hasNext();)
      {
        ImportImpl theImport = (ImportImpl) i.next();
        if (theImport.getLocationURI() != null && !theImport.getLocationURI().endsWith("xsd"))
        {
          theImport.importDefinitionOrSchema();	 
          Definition importedDefinition = (Definition) theImport.getEDefinition();
          if (importedDefinition != null && !list.contains(importedDefinition))
          {
            addDefinition(importedDefinition, list);
          }
        }
      }
    }
  }

  public static String getPortTypeReference(Binding binding)
  {
    String result = null;
    Element element = WSDLEditorUtil.getInstance().getElementForObject(binding);
    if (element != null)
    {
      result = element.getAttribute("type");
    }
    return result;
  }

  public static void setPortTypeReference(Binding binding, String portType)
  {
    Element element = WSDLEditorUtil.getInstance().getElementForObject(binding);
    if (element != null)
    {
      element.setAttribute("type", portType);
    }
  }

  public static String getBindingReference(Port port)
  {
    String result = null;
    Element element = WSDLEditorUtil.getInstance().getElementForObject(port);
    if (element != null)
    {
      result = element.getAttribute("binding");
    }
    return result;
  }

  public static String getName(Binding binding)
  {
    String result = null;
    Element element = WSDLEditorUtil.getInstance().getElementForObject(binding);
    if (element != null)
    {
      result = element.getAttribute("name");
    }
    return result;
  }

  public static QName getPortTypeReferenceQName(Binding binding)
  {
    QName result = null;
    Definition definition = binding.getEnclosingDefinition();
    String prefixedName = getPortTypeReference(binding);
    if (prefixedName != null)
    {
      result = WSDLEditorUtil.createQName(definition, prefixedName);
    }
    return result;
  }

  public static QName getBindingReferenceQName(Port port)
  {
    QName result = null;
    Definition definition = port.getEnclosingDefinition();
    String prefixedName = getBindingReference(port);
    if (prefixedName != null)
    {
      result = WSDLEditorUtil.createQName(definition, prefixedName);
    }
    return result;
  }

  public static String getMessageReference(Input input)
  {
    return getMessageReferenceHelper(input);
  }

  public static String getMessageReference(Output output)
  {
    return getMessageReferenceHelper(output);
  }

  public static String getMessageReference(Fault fault)
  {
    return getMessageReferenceHelper(fault);
  }

  public static QName getMessageReferenceQName(Input input)
  {
    return getMessageReferenceQNameHelper(input);
  }

  public static QName getMessageReferenceQName(Output output)
  {
    return getMessageReferenceQNameHelper(output);
  }

  public static QName getMessageReferenceQName(Fault fault)
  {
    return getMessageReferenceQNameHelper(fault);
  }

  protected static String getMessageReferenceHelper(WSDLElement o)
  {
    String result = null;
    Element element = WSDLEditorUtil.getInstance().getElementForObject(o);
    if (element != null)
    {
      result = element.getAttribute("message");
    }
    return result;
  }

  public static QName getMessageReferenceQNameHelper(WSDLElement o)
  {
    QName result = null;
    Definition definition = o.getEnclosingDefinition();
    String prefixedName = getMessageReferenceHelper(o);
    if (prefixedName != null)
    {
      result = WSDLEditorUtil.createQName(definition, prefixedName);
    }
    return result;
  }

  public static void updatePortTypeReferences(Definition definition)
  {
    for (Iterator i = definition.getBindings().values().iterator(); i.hasNext();)
    {
      Binding binding = (Binding) i.next();
      QName qname = ComponentReferenceUtil.getPortTypeReferenceQName(binding);

      PortType portType = (qname != null) ? (PortType) definition.getPortType(qname) : null;

      if (binding.getPortType() != portType)
      {
        binding.setPortType(portType);
      }
    }
  }

  public static void updateBindingReferences(Definition definition)
  {
    for (Iterator i = definition.getServices().values().iterator(); i.hasNext();)
    {
      Service service = (Service) i.next();
      for (Iterator j = service.getEPorts().iterator(); j.hasNext();)
      {
        Port port = (Port) j.next();
        QName qname = ComponentReferenceUtil.getBindingReferenceQName(port);

        Binding binding = (qname != null) ? (Binding) definition.getBinding(qname) : null;

        if (port.getBinding() != binding)
        {
          port.setBinding(binding);
        }
      }
    }
  }

  public static void updateMessageReferences(Definition definition)
  {
    // for each port type
    //
    for (Iterator i = definition.getPortTypes().values().iterator(); i.hasNext();)
    {
      PortType portType = (PortType) i.next();

      // for each operation
      //
      for (Iterator j = portType.getEOperations().iterator(); j.hasNext();)
      {
        Operation operation = (Operation) j.next();

        // handle Input
        //
        Input input = (Input) operation.getInput();
        if (input != null)
        {
          QName qname = ComponentReferenceUtil.getMessageReferenceQName(input);
          Message message = (qname != null) ? (Message) definition.getMessage(qname) : null;
          if (input.getMessage() != message)
          {
            input.setMessage(message);
          }
        }

        // handle Output
        //
        Output output = (Output) operation.getOutput();
        if (output != null)
        {
          QName qname = ComponentReferenceUtil.getMessageReferenceQName(output);
          Message message = (qname != null) ? (Message) definition.getMessage(qname) : null;
          if (output.getMessage() != message)
          {
            output.setMessage(message);
          }
        }

        // handle Faults
        //
        for (Iterator k = operation.getEFaults().iterator(); k.hasNext();)
        {
          Fault fault = (Fault) k.next();

          QName qname = ComponentReferenceUtil.getMessageReferenceQName(fault);
          Message message = (qname != null) ? (Message) definition.getMessage(qname) : null;
          if (fault.getMessage() != message)
          {
            fault.setMessage(message);
          }
        }
      }
    }
  }

  public static void updateSchemaReferences(Definition definition)
  {
    for (Iterator i = definition.getEMessages().iterator(); i.hasNext();)
    {
      Message message = (Message) i.next();
      for (Iterator j = message.getEParts().iterator(); j.hasNext();)
      {
        Part part = (Part) j.next();
        Element element = WSDLEditorUtil.getInstance().getElementForObject(part);
        if (element != null)
        {
          ((WSDLElementImpl)part).elementChanged(element);
        }
      }
    }
  }

  public static Operation getOperation(PortType portType, BindingOperation bindingOperation)
  {
    Operation result = null;
    for (Iterator i = portType.getEOperations().iterator(); i.hasNext();)
    {
      Operation operation = (Operation) i.next();
      if (isMatchingBinding(operation, bindingOperation))
      {
        result = operation;
        break;
      }
    }
    return result;
  }

  public static void updateOperationReference(BindingOperation bindingOperation)
  {
    Operation operation = computeOperation(bindingOperation);
    if (operation != bindingOperation.getOperation())
    {
      bindingOperation.setOperation(operation);
    }
  }

  public List getComponentNameList(boolean isType)
  {
    List result = Collections.EMPTY_LIST;
    ITypeSystemProvider typeSystemProvider = WSDLEditorUtil.getInstance().getTypeSystemProvider(rootDefinition);
    if (typeSystemProvider != null)
    {
      result = isType ? typeSystemProvider.getAvailableTypeNames(rootDefinition, 0) : typeSystemProvider.getAvailableElementNames(rootDefinition);
    }
    return result;
  }

  public static List getComponentNameList(Part part, boolean isType)
  {
    List result = Collections.EMPTY_LIST;
    Definition definition = part.getEnclosingDefinition();
    ITypeSystemProvider typeSystemProvider = WSDLEditorUtil.getInstance().getTypeSystemProvider(definition);
    if (typeSystemProvider != null)
    {
      result = isType ? typeSystemProvider.getAvailableTypeNames(definition, 0) : typeSystemProvider.getAvailableElementNames(definition);
    }
    return result;
  }

  public static boolean isType(Part part)
  {
    //Element element = WSDLEditorUtil.getInstance().getElementForObject(part);
    Element element = part.getElement();
    return !element.hasAttribute("element");
  }

  public static String getPartComponentReference(Part part)
  {
    //Element element = WSDLEditorUtil.getInstance().getElementForObject(part);
    Element element = part.getElement();
    String result = null;
    if (element.hasAttribute("type"))
    {
      result = element.getAttribute("type");
    }
    else if (element.hasAttribute("element"))
    {
      result = element.getAttribute("element");
    }
    return result;
  }

  public static void setComponentReference(Part part, boolean isType, String componentName)
  {
    Element element = WSDLEditorUtil.getInstance().getElementForObject(part);
    String newAttribute = isType ? "type" : "element";
    String oldAttribute = isType ? "element" : "type";
    element.removeAttribute(oldAttribute);

    String value = componentName != null ? componentName : element.getAttribute(newAttribute);

    if (value == null)
    {
      if (isType)
      {
        String xsdPrefix = part.getEnclosingDefinition().getPrefix(WSDLConstants.XSD_NAMESPACE_URI);
        value = "string";
        if (xsdPrefix != null && xsdPrefix.length() > 0)
        {
          value = xsdPrefix + ":" + value;
        }
      }
      else
      {
        List list = getComponentNameList(part, isType);
        value = list.size() > 0 ? (String) list.get(0) : "some-element-name";
      }
    }
    element.setAttribute(newAttribute, value);
  }

  public static Operation computeOperation(BindingOperation bindingOperation)
  {
    Operation result = null;
    Binding binding = (Binding) bindingOperation.eContainer();
    PortType portType = (PortType) binding.getPortType();
    if (portType != null)
    {
      result = getOperation(portType, bindingOperation);
    }
    return result;
  }

  public static Input computeInput(BindingInput bindingInput)
  {
    Operation operation = computeOperation((BindingOperation) bindingInput.eContainer());
    return operation != null ? operation.getEInput() : null;
  }

  public static Output computeOutput(BindingOutput bindingOutput)
  {
    Operation operation = computeOperation((BindingOperation) bindingOutput.eContainer());
    return operation != null ? operation.getEOutput() : null;
  }

  public static Fault computeFault(BindingFault bindingFault)
  {
    Fault result = null;
    Operation operation = computeOperation((BindingOperation) bindingFault.eContainer());
    if (operation != null)
    {
      for (Iterator i = operation.getEFaults().iterator(); i.hasNext();)
      {
        Fault fault = (Fault) i.next();
        String faultName = fault.getName();
        if (faultName != null && faultName.equals(bindingFault.getName()))
        {
          result = fault;
          break;
        }
      }
    }
    return result;
  }

  public static String getPartReferenceValue(Part part)
  {
    Element element = WSDLEditorUtil.getInstance().getElementForObject(part);
    String value = null;
    if (element != null)
    {
      if (element.hasAttribute("type"))
      {      		
        value = element.getAttribute("type");
      }
      else if (element.hasAttribute("element"))
      {  
        value = element.getAttribute("element");
      }         
    }  
    return value != null ? value : "";
  }

  public Operation getBindingOperation(Element bindingOperationContent)
  {
    Operation operation = null;
    Node parent = bindingOperationContent.getParentNode();
    if (parent instanceof Element)
    {
      Object object = WSDLEditorUtil.getInstance().findModelObjectForElement(rootDefinition, (Element) parent);
      if (object instanceof BindingOperation)
      {
        operation = ComponentReferenceUtil.computeOperation((BindingOperation) object);
      }
    }
    return operation;
  }

  public Message getBindingOperationInputMessage(Element bindingOperationContent)
  {
    Message message = null;
    Operation operation = getBindingOperation(bindingOperationContent);
    if (operation != null)
    {
      Input input = operation.getEInput();
      if (input != null)
      {
        message = input.getEMessage();
      }
    }
    return message;
  }

  public Message getBindingOperationOutputMessage(Element bindingOperationContent)
  {
    Message message = null;
    Operation operation = getBindingOperation(bindingOperationContent);
    if (operation != null)
    {
      Output output = operation.getEOutput();
      if (output != null)
      {
        message = output.getEMessage();
      }
    }
    return message;
  }

  public static String computeRelativeURI(IFile referencedLocation, IFile baseLocation, boolean enableIEStyleReferences)
  {
    // TODO... we need some extension to allow IE folks to plugin logic to create an 'IE' style path
    // TODO... consider using URI class in EMF 
    return URIHelper.getRelativeURI(referencedLocation.getLocation(), baseLocation.getLocation());
  }
  
  public static String computeRelativeURI(String referencedLocation, String baseLocation, boolean enableIEStyleReferences)
  {
    // TODO... we need some extension to allow IE folks to plugin logic to create an 'IE' style path
    // TODO... consider using URI class in EMF 
    return URIHelper.getRelativeURI(referencedLocation, baseLocation);
  }
}
