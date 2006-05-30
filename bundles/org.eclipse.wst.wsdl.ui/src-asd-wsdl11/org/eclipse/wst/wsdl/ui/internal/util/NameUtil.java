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
package org.eclipse.wst.wsdl.ui.internal.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.wst.wsdl.Binding;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.Fault;
import org.eclipse.wst.wsdl.Input;
import org.eclipse.wst.wsdl.Message;
import org.eclipse.wst.wsdl.MessageReference;
import org.eclipse.wst.wsdl.Operation;
import org.eclipse.wst.wsdl.Output;
import org.eclipse.wst.wsdl.Part;
import org.eclipse.wst.wsdl.Port;
import org.eclipse.wst.wsdl.PortType;
import org.eclipse.wst.wsdl.Service;
import org.eclipse.xsd.XSDComplexTypeDefinition;
import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDModelGroup;
import org.eclipse.xsd.XSDParticle;
import org.eclipse.xsd.XSDSchema;

public class NameUtil
{
  /**
   * Return a name which is not used by any other fault in the operation.
   * @return String
   */
  public static String buildUniqueFaultName(Operation operation)
  {
  	return buildUniqueFaultName(operation, "NewFault"); //$NON-NLS-1$
  }
  
  public static String buildUniqueFaultName(Operation operation, String baseName) {
  	if (baseName == null)
  		baseName = "NewFault"; //$NON-NLS-1$
  	
  	List names = getUsedFaultNames(operation);

    // Now search the list until we find an unused name
    return getUniqueNameHelper(baseName, names);
  }

  /**
   * Return a name which is not used by any other input in the portType.  Returned name will be of the form:
   * <operationName> + <ending> [+ unique Integer]
   * @return String
   */
  public static String buildUniqueInputName(PortType portType, String operationName, String ending)
  {
    String name = null;
    String candidate = operationName + ending;

    int i = 0;

    // loop until we find a unique name (the name will consist of the operationName + ending + an integer)
    while (name == null)
    {
      boolean unique = true;

      // determine if this combination is unique within the current porttype
      for (Iterator it = portType.getEOperations().iterator(); it.hasNext() && unique;)
      {
        Operation current = (Operation) it.next();
        // TODO : port check
        // old  if(current.isSetEInput() && current.getEInput().isSetName()) {
        if (current.getEInput() != null && current.getEInput().getName() != null)
        {
          if (current.getEInput().getName().equals(candidate))
            unique = false;
        }
      }
      if (unique)
        name = candidate;
      else
        candidate = operationName + ending + i;
      i++;
    }
    return name;
  }

  /**
   * Return a name which is not used by any other message in the definition.
   * @return String
   */
  public static String buildUniqueMessageName(Definition definition, String baseName)
  {
    if (baseName == null)
    {
      baseName = "NewMessage"; //$NON-NLS-1$
    }

    List names = getUsedMessageNames(definition);

    // Now search the list until we find an unused name
    return getUniqueNameHelper(baseName, names);
  }

  /**
   * Return a name which is not used by any other operation in the port type.
   * @return String
   */
  public static String buildUniqueOperationName(PortType portType)
  {
  	return buildUniqueOperationName(portType, "NewOperation"); //$NON-NLS-1$
  }
  
  public static String buildUniqueOperationName(PortType portType, String baseName)
  {
  	if (baseName == null) {
  		baseName = "NewOperation"; //$NON-NLS-1$
  	}

  	List names = getUsedOperationNames(portType);

    // Now search the list until we find an unused name
    return getUniqueNameHelper(baseName, names);
  }

  /**
   * Return a name which is not used by any other output in the portType.  Returned name will be of the form:
   * <operationName> + <ending> [+ unique Integer]
   * @return String
   */
  public static String buildUniqueOutputName(PortType portType, String operationName, String ending)
  {
    String name = null;
    String candidate = operationName + ending;

    int i = 0;

    // loop until we find a unique name (the name will consist of the operationName + ending + an integer)
    while (name == null)
    {
      boolean unique = true;

      // determine if this combination is unique within the current porttype			
      for (Iterator it = portType.getEOperations().iterator(); it.hasNext() && unique;)
      {
        Operation current = (Operation) it.next();
        // TODO: port check
        // old				if(current.isSetEOutput() && current.getEOutput().isSetName()) {
        if (current.getEOutput() != null && current.getEOutput().getName() != null)
        {
          if (current.getEOutput().getName().equals(candidate))
            unique = false;
        }
      }
      if (unique)
        name = candidate;
      else
        candidate = operationName + ending + i;
      i++;
    }
    return name;
  }

  /**
   * Return a name which is not used by any other part in the message.
   * @return String
   */
  public static String buildUniquePartName(Message message)
  {
  	List names = getUsedPartNames(message);

    // Now search the list until we find an unused name
    return getUniqueNameHelper("NewPart", names); //$NON-NLS-1$
  }
  
  public static String buildUniquePartName(Message message, String baseName)
  {
  	if (baseName == null)
  	{
  		baseName = "NewPart"; //$NON-NLS-1$
  	}
  	
  	List names = getUsedPartNames(message);

    // Now search the list until we find an unused name
    return getUniqueNameHelper(baseName, names);
  }

  /**
   * Return a name which is not used by any other port type in the definition.
   * @return String
   */
  public static String buildUniquePortTypeName(Definition definition, String baseName)
  {
    if (baseName == null)
    {
      baseName = "NewPortType"; //$NON-NLS-1$
    }

    List names = getUsedPortTypeNames(definition);

    // Now search the list until we find an unused name
    return getUniqueNameHelper(baseName, names);
  }

  public static String getUniqueNameHelper(String baseName, List names)
  {
    int i = 0;

    String name = baseName;
    while (true)
    {
      if (!names.contains(name))
      {
        break;
      }
      i++;
      name = baseName + i;
    }

    return name;
  }

  /**
   * Return a name which is not used by any other service in the definition.
   * @return String
   */
  public static String buildUniqueServiceName(Definition definition)
  {
  	List names = getUsedServiceNames(definition);

    // Now search the list until we find an unused name
    return getUniqueNameHelper("NewService", names); //$NON-NLS-1$
  }

  /**
   * Return a name which is not used by any other binding in the definition.
   * @return String
   */
  public static String buildUniqueBindingName(Definition definition, String baseName)
  {
    if (baseName == null)
    {
      baseName = "NewBinding"; //$NON-NLS-1$
    }

    List names = getUsedBindingNames(definition);

    return getUniqueNameHelper(baseName, names);
  }

  public static String buildUniquePrefix(Definition definition, String basePrefix)
  {
    String prefix = basePrefix;
    for (int i = 1; definition.getNamespace(prefix) != null; i++)
    {
      prefix = basePrefix + i;
    }
    return prefix;
  }

  public static String buildUniquePortName(Service service, String baseName)
  {
    if (baseName == null)
    {
      baseName = "NewPort"; //$NON-NLS-1$
    }
  
    List names = getUsedPortNames(service);
    
	return getUniqueNameHelper(baseName, names);
  }
	
  public static String buildUniqueMessageName(Definition definition, MessageReference messRef)
  {   
    String name = null;
    if (messRef instanceof Input)
    {
      name = createOperationName(messRef, "Request");     //$NON-NLS-1$
    }
    else if (messRef instanceof Output)
    {
      name = createOperationName(messRef, "Response");  //$NON-NLS-1$
    }
    else if (messRef instanceof Fault)
    {                                
      String faultName = ((Fault) messRef).getName();
      if (faultName == null || faultName.length() == 0)
      {                     
        faultName = "Fault"; //$NON-NLS-1$
      }
      name = createOperationName(messRef, faultName); 
    }                                                                     

    return NameUtil.buildUniqueMessageName(definition, name);
  }
  
  
  public static List getUsedFaultNames(Operation operation) {
    ArrayList names = new ArrayList();
    for (Iterator i = operation.getEFaults().iterator(); i.hasNext();)
    {
      Fault fault = (Fault) i.next();
      names.add(fault.getName());
    }
    
    return names;
  }

  public static List getUsedOperationNames(PortType portType) {
    ArrayList names = new ArrayList();
    for (Iterator i = portType.getEOperations().iterator(); i.hasNext();)
    {
      Operation op = (Operation) i.next();
      names.add(op.getName());
    }
    
    return names;
  }
  
  public static List getUsedPartNames(Message message) {
    ArrayList names = new ArrayList();
    for (Iterator i = message.getEParts().iterator(); i.hasNext();)
    {
      Part part = (Part) i.next();
      names.add(part.getName());
    }    
    return names;
  }
  
  public static List getUsedPortTypeNames(Definition definition) {
    ArrayList names = new ArrayList();
    for (Iterator i = definition.getEPortTypes().iterator(); i.hasNext();)
    {
      PortType portType = (PortType) i.next();
      // TODO: port check
      //			if (portType.isSetQName())
      if (portType.getQName() != null)
      {
        names.add(portType.getQName().getLocalPart());
      }
    }
    
    return names;
    
  }
  public static List getUsedServiceNames(Definition definition) {
    // First build a list of names already used
    ArrayList names = new ArrayList();
    for (Iterator i = definition.getEServices().iterator(); i.hasNext();)
    {
      Service service = (Service) i.next();
      // TODO: port check
      // 		if(service.isSetQName())
      if (service.getQName() != null)
        names.add(service.getQName().getLocalPart());
    }
    
    return names;
  }
  
  public static List getUsedMessageNames(Definition definition) {
    ArrayList names = new ArrayList();
    for (Iterator i = definition.getEMessages().iterator(); i.hasNext();)
    {
      Message msg = (Message) i.next();
      // TODO: port check
      if (msg.getQName() != null)
        //			if(msg.isSetQName())
        names.add(msg.getQName().getLocalPart());
    }
    
    return names;
  }

  public static List getUsedBindingNames(Definition definition) {
    ArrayList names = new ArrayList();
    for (Iterator i = definition.getEBindings().iterator(); i.hasNext();)
    {
      Binding binding = (Binding) i.next();
      // TODO: port check
      //			if (binding.isSetQName())
      if (binding.getQName() != null)
      {
        names.add(binding.getQName().getLocalPart());
      }
    }
    
    return names;
  }

  public static List getUsedPortNames(Service service) {
    // First build a list of names already used
    ArrayList names = new ArrayList();
    for (Iterator i = service.getEPorts().iterator(); i.hasNext();)
    {
      Port port = (Port) i.next();

      if (port.getName() != null)
      {
        names.add(port.getName());
      }
    }
    
    return names;
  }
  
  private static String createOperationName(Object object, String suffix)
  {               
    String result = null;
    if (object instanceof EObject)
    {
      EObject parent = ((EObject)object).eContainer();
      if (parent instanceof Operation)
      {
        result = ((Operation)parent).getName();
      }
    } 
    if (result != null)
    {
      result += suffix;
    }
    return result;
  }

public static String getMessageName(MessageReference messageRef) {	    
	String messageName = "NewMessage"; //$NON-NLS-1$
	List messageNames = new ArrayList();
	Operation operation = (Operation) messageRef.getContainer();
	Iterator messageIt = operation.getEnclosingDefinition().getEMessages().iterator();
	while (messageIt.hasNext()) {
		messageNames.add(((Message) messageIt.next()).getQName().getLocalPart());
	}
	
//	String requestResponseString = getRequestOrResponse(messageRef) + "Msg"; //$NON-NLS-1$
	String requestResponseString = getRequestOrResponse(messageRef); //$NON-NLS-1$
	messageName = getUniqueNameHelper(operation.getName() + requestResponseString, messageNames);
	
	return messageName;
}

public static String getPartName(MessageReference messageRef) {
	String partName = "NewPart"; //$NON-NLS-1$
	Message message = messageRef.getEMessage();
	
	Operation operation = (Operation) messageRef.getContainer();
	String operationName = operation.getName();
	String appendString = "";    	   //$NON-NLS-1$
	if (messageRef instanceof Input) {
//		appendString = "Parameters"; //$NON-NLS-1$
		appendString = "Request"; //$NON-NLS-1$
	}
	else if (messageRef instanceof Output) {
//		appendString = "Result"; //$NON-NLS-1$
		appendString = "Response"; //$NON-NLS-1$
	}
	else if (messageRef instanceof Fault) {
//		appendString = "Fault"; //$NON-NLS-1$
		appendString = "_Fault"; //$NON-NLS-1$
	}
	partName = operationName + appendString;
	
	List usedPartNames = new ArrayList();
	if (message != null) {
		partName = message.getQName().getLocalPart();
		Iterator partIt = message.getEParts().iterator();
		while (partIt.hasNext()) {
			usedPartNames.add(((Part) partIt.next()).getName());
		}
	}
	
	partName = getUniqueNameHelper(partName, usedPartNames);
	
	return partName;
}

public static String getOperationName(PortType portType) {
	String operationName = "NewOperation"; //$NON-NLS-1$
	Iterator operationIt = portType.getEOperations().iterator();
	List usedNames = new ArrayList();
	while (operationIt.hasNext()) {
		usedNames.add(((Operation) operationIt.next()).getName());
	}
	
	operationName = getUniqueNameHelper("NewOperation", usedNames); //$NON-NLS-1$
	
	return operationName;
}

public static String getRequestOrResponse(MessageReference messageRef) {
	if (messageRef instanceof Input)
	{
		return "Request"; //$NON-NLS-1$
	}
	else if (messageRef instanceof Output)
	{
		return "Response"; //$NON-NLS-1$
	}
	else if (messageRef instanceof Fault)
	{
		return "_Fault"; //$NON-NLS-1$
	}
	
	return ""; //$NON-NLS-1$
}

public static String getFaultName(Operation operation) {
	String faultName = "fault"; //$NON-NLS-1$
	List nameList = new ArrayList();
	Iterator faultIt = operation.getEFaults().iterator();
	while (faultIt.hasNext()) {
		nameList.add(((Fault) faultIt.next()).getName());
	}
	
	faultName = getUniqueNameHelper(faultName, nameList);
	
	return faultName;
}

public static String getXSDElementName(String baseName, Object parent) {
	String elementName = ""; //$NON-NLS-1$
	
	if (parent instanceof XSDSchema) {
		elementName = getUniqueNameHelper(baseName, getUsedElementNames((XSDSchema) parent));
	}
	else if (parent instanceof XSDModelGroup) {
		List existingNames = new ArrayList();
		XSDModelGroup modelGroup = (XSDModelGroup) parent;
		Iterator modelGroupIt = modelGroup.getContents().iterator();
		while (modelGroupIt.hasNext()) {
			Object item = modelGroupIt.next();
			if (item instanceof XSDParticle) {
				XSDParticle existingParticle = (XSDParticle) item;
				if (existingParticle.getContent() instanceof XSDElementDeclaration) {
					existingNames.add(((XSDElementDeclaration) existingParticle.getContent()).getName());
				}
			}
		}
		
		elementName = getUniqueNameHelper(baseName, existingNames);			
	}
	
	return elementName;
}

public static String getXSDComplexTypeName(String baseName, XSDSchema schema) {
	String typeName = ""; //$NON-NLS-1$
	List existingNames = new ArrayList();
	
	Iterator it = schema.getTypeDefinitions().iterator();
	while (it.hasNext()) {
		Object item = it.next();
		if (item instanceof XSDComplexTypeDefinition) {
			existingNames.add(((XSDComplexTypeDefinition) item).getName());
		}
	}
	
	typeName = getUniqueNameHelper(baseName, existingNames);
	return typeName;
}

private static List getUsedElementNames(XSDSchema xsdSchema) {
	List usedNames = new ArrayList();
	Iterator schemaIt = xsdSchema.getContents().iterator();
	while (schemaIt.hasNext()) {
		Object item = schemaIt.next();
		if (item instanceof XSDElementDeclaration) {
			usedNames.add(((XSDElementDeclaration) item).getName());
		}
	}
	
	return usedNames;
}
}
