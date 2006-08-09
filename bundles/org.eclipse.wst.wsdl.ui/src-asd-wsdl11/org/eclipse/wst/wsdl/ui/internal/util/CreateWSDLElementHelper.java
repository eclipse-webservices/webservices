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

import java.util.Iterator;
import java.util.List;

import javax.wsdl.OperationType;

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
import org.eclipse.wst.wsdl.WSDLElement;
import org.eclipse.wst.wsdl.XSDSchemaExtensibilityElement;
import org.eclipse.wst.wsdl.internal.impl.MessageReferenceImpl;
import org.eclipse.wst.wsdl.internal.impl.WSDLElementImpl;
import org.eclipse.wst.wsdl.ui.internal.commands.AddBaseParameterCommand;
import org.eclipse.wst.wsdl.ui.internal.commands.AddBindingCommand;
import org.eclipse.wst.wsdl.ui.internal.commands.AddFaultCommand;
import org.eclipse.wst.wsdl.ui.internal.commands.AddInputCommand;
import org.eclipse.wst.wsdl.ui.internal.commands.AddInputParameterCommand;
import org.eclipse.wst.wsdl.ui.internal.commands.AddMessageCommand;
import org.eclipse.wst.wsdl.ui.internal.commands.AddOperationCommand;
import org.eclipse.wst.wsdl.ui.internal.commands.AddOutputCommand;
import org.eclipse.wst.wsdl.ui.internal.commands.AddOutputParameterCommand;
import org.eclipse.wst.wsdl.ui.internal.commands.AddPartCommand;
import org.eclipse.wst.wsdl.ui.internal.commands.AddPortCommand;
import org.eclipse.wst.wsdl.ui.internal.commands.AddPortTypeCommand;
import org.eclipse.wst.wsdl.ui.internal.commands.AddServiceCommand;
import org.eclipse.wst.wsdl.ui.internal.commands.AddXSDElementDeclarationCommand;
import org.eclipse.wst.wsdl.util.WSDLConstants;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.eclipse.wst.xml.core.internal.provisional.format.FormatProcessorXML;
import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDSchema;
import org.w3c.dom.Element;

public class CreateWSDLElementHelper {
	// Constants used for getting Part information
	public static final String PART_INFO_ELEMENT_DECLARATION = "ELEMENT_DECLARATION"; //$NON-NLS-1$
	public static final String PART_INFO_TYPE_DEFINITION     = "TYPE_DEFINITION"; //$NON-NLS-1$

	// The following variables should be set if a name other than the computed name is to be used.
	public static String serviceName = null;
	public static String portName = null;
	public static String bindingName = null;
	public static String portTypeName = null;
	public static String operationName = null;
	public static String inputName = null;
	public static String outputName = null;
	public static String faultName = null;
	public static String messageName = null;
	public static String partName = null;
	
	public static String PART_TYPE_OR_DEFINITION = PART_INFO_TYPE_DEFINITION;
	public static boolean CREATE_DOWN_TO_PART = true;
		
/*
 * The following methods creates the 'specified' (by calling a certain method) WSDLElement
 * and it's 'children' all the way to the PortType level.
 */
	public static Service createService(Definition definition) {
		if (serviceName == null || serviceName.trim().equals("")) //$NON-NLS-1$
			serviceName = NameUtil.buildUniqueServiceName(definition);		
		
		CreateWSDLElementHelper.portTypeName = serviceName;
		
	    AddServiceCommand addService = new AddServiceCommand(definition, serviceName, false);
	    addService.run();
	    Service service = (Service) addService.getWSDLElement();
	    CreateWSDLElementHelper.createPort(service);
  		
	    return service;
	}
	
	public static Port createPort(Service service) {
		if (portName == null || portName.trim().equals("")) //$NON-NLS-1$
			portName = NameUtil.buildUniquePortName(service, null);
		
	    AddPortCommand addPort = new AddPortCommand(service, portName);
	    addPort.run();
	    Port port = (Port) addPort.getWSDLElement();
		Binding binding = CreateWSDLElementHelper.createBinding(port.getEnclosingDefinition(), port);

		port.setBinding(binding);
		
	    return port;
	}
	
	public static Binding createBinding(Definition definition, Port port) {
		bindingName = port.getName();
		if (bindingName == null || bindingName.trim().equals("")) //$NON-NLS-1$
			bindingName = NameUtil.buildUniqueBindingName(definition, null);
		
		AddBindingCommand addBinding = new AddBindingCommand(definition, bindingName);
		addBinding.run();
		Binding binding = (Binding) addBinding.getWSDLElement();
		PortType portType = CreateWSDLElementHelper.createPortType(binding.getEnclosingDefinition());

		binding.setPortType(portType);
		
		return binding;
	}
	
	public static PortType createPortType(Definition definition) {
		if (portTypeName == null || portTypeName.trim().equals("")) //$NON-NLS-1$
			portTypeName = NameUtil.buildUniquePortTypeName(definition, "PortType"); //$NON-NLS-1$
		
		AddPortTypeCommand addPortTypeCommand = new AddPortTypeCommand(definition, portTypeName);
		addPortTypeCommand.run();
		PortType portType = (PortType) addPortTypeCommand.getWSDLElement();

		if (CREATE_DOWN_TO_PART) {
			CreateWSDLElementHelper.createOperation(portType);
		}
		
		return portType;
	}

	
/*
 * The following methods creates the 'specified' (by calling a certain method) WSDLElement
 * and it's 'children' all the way to the Part level.
 */	
  	public static Operation createOperation(PortType portType) {
  		if (operationName == null || operationName.trim().equals("")) //$NON-NLS-1$
  			operationName = NameUtil.buildUniqueOperationName(portType);
  		
		AddOperationCommand action = new AddOperationCommand(portType, operationName);
		action.run();
		Operation operation = (Operation) action.getWSDLElement();
		
		int style = AddBaseParameterCommand.PART_ELEMENT_SEQ_ELEMENT;
  		if (PART_TYPE_OR_DEFINITION == PART_INFO_TYPE_DEFINITION) {
  			style = AddBaseParameterCommand.PART_SIMPLETYPE;
  		}
  		else if (PART_TYPE_OR_DEFINITION == PART_INFO_ELEMENT_DECLARATION) {
  			style = AddBaseParameterCommand.PART_ELEMENT_SEQ_ELEMENT;
//  			style = AddBaseParameterCommand.PART_ELEMENT;
  		}
  		
		AddInputParameterCommand inputCommand = new AddInputParameterCommand(operation, style);
		inputCommand.run();
		AddOutputParameterCommand outputCommand = new AddOutputParameterCommand(operation, style);
		outputCommand.run();
		operation.setStyle(OperationType.REQUEST_RESPONSE);

		return operation;
  	}

  	public static Input createInput(PortType portType, Operation operation, String inputName) {
  		if (inputName == null || inputName.trim().equals("")) //$NON-NLS-1$
  	  		inputName = NameUtil.buildUniqueInputName(portType, operation.getName(), ""); //$NON-NLS-1$
  		
  		AddInputCommand action = new AddInputCommand(operation, inputName);
  		action.run();
  		Input input = (Input) action.getWSDLElement(); 
  		Message mess = CreateWSDLElementHelper.createMessage(input);
  		input.setMessage(mess);
  			  		
  		return input;
  	}
  			  	
  	public static Output createOutput(PortType portType, Operation operation) {
  		if (outputName == null || outputName.trim().equals("")) //$NON-NLS-1$
  	  		outputName = NameUtil.buildUniqueOutputName(portType, operation.getName(), ""); //$NON-NLS-1$
  		
  		AddOutputCommand action = new AddOutputCommand(operation, outputName);
  		action.run();
  		Output output = (Output) action.getWSDLElement();
  		Message mess = CreateWSDLElementHelper.createMessage(output);
  		output.setMessage(mess);	
  			  		
  		return output;
  	}
  			  	
  	public static Fault createFault(Operation operation) {
  		if (faultName == null || faultName.trim().equals(""))  //$NON-NLS-1$
	  		faultName = NameUtil.buildUniqueFaultName(operation);
  		
  		AddFaultCommand action = new AddFaultCommand(operation, faultName);
  		action.run();
  		Fault fault = (Fault) action.getWSDLElement();
  		Message mess = CreateWSDLElementHelper.createMessage(fault);
  		fault.setMessage(mess);
  		 		
  		return fault;
  	}
  			  	
  	public static Message createMessage(MessageReference iof) {
  		if (messageName == null || messageName.trim().equals("")) //$NON-NLS-1$
  	  		messageName = NameUtil.buildUniqueMessageName(iof.getEnclosingDefinition(), iof);
//	  		messageName = NameUtil.buildMessageName(iof.getName());
  		
  		Definition def = iof.getEnclosingDefinition();
  		AddMessageCommand action = new AddMessageCommand(def, messageName);
  		action.run();
  		Message message = (Message) action.getWSDLElement();
  		CreateWSDLElementHelper.createPart(message);
  		Element parentNode = message.getElement();
      if (parentNode instanceof IDOMNode) 
      {
		    // format selected node                                                    
        FormatProcessorXML formatProcessorXML = new FormatProcessorXML();
        formatProcessorXML.formatNode((IDOMNode)parentNode);
      }
  		
  		messageName = null;
  		return message;
  	}
  			  	
  	public static Part createPart(Message message) {
  		Definition def = message.getEnclosingDefinition();
  		String name = NameUtil.buildUniquePartName(message, message.getQName().getLocalPart());
  		AddPartCommand action = null;
  		
  		if (PART_TYPE_OR_DEFINITION == PART_INFO_TYPE_DEFINITION) {
  			action = new AddPartCommand(message, name, WSDLConstants.SCHEMA_FOR_SCHEMA_URI_2001, "string", true); //$NON-NLS-1$
  		}
  		else if (PART_TYPE_OR_DEFINITION == PART_INFO_ELEMENT_DECLARATION) {  			
  			//action = new AddPartCommand(message, name, WSDLConstants.SCHEMA_FOR_SCHEMA_URI_2001, "string", true);
  			String elementName = getNewNameHelper(name, def, false);
  			AddXSDElementDeclarationCommand elementAction = new AddXSDElementDeclarationCommand(def, elementName);
  			elementAction.run();
  			action = new AddPartCommand(message, name, def.getTargetNamespace(), elementName, false);
  		}

  		action.run();
  		
  		return (Part) action.getWSDLElement();
  	}	

  	/*
  	 * Used to determine a name for an Element
  	 */
    private static String getNewNameHelper(String base, Definition def, boolean isType)
    { 
      String name = base;    
      int count = 0;

      // Ugly....  Redo this...
      // Get a list of Elements...
      List elementList = null;
      if (def.getETypes() != null) {
      	List xsdsList = def.getETypes().getEExtensibilityElements();
      	if (xsdsList != null) {
      		Iterator xsdsIterator = xsdsList.iterator();
      		XSDSchemaExtensibilityElement xsdElement = (XSDSchemaExtensibilityElement) xsdsIterator.next();
      		XSDSchema schema = xsdElement.getSchema();
      		if (schema != null) {
      			elementList = schema.getElementDeclarations();
      		}
      	}
      }
      
      if (elementList != null) {
      	int index = 0;
      	while (index < elementList.size()) {
      		XSDElementDeclaration elementDeclaration = (XSDElementDeclaration) elementList.get(index);
      		
      		if (name.equals(elementDeclaration.getName())) {
      			count++;
      			name = name + count;
      			index = 0;
      		}
      		else {
      			index++;
      		}
      	}
      }

      return name;
    }

  	/*
  	 * Return the type of Part this WSDLElement should have (Element vs Type).  To determine this, we filter up to the 'parent' PortType
  	 * and go down to the first Part we encounter and check the type it has.  If this fails, default to Type.
  	 */
  	  public static String getPartInfo(WSDLElement element) {
  	  	String partInfo = null;
  	  	
  	  	if (element instanceof PortType) {
  	  		partInfo = getPartInfo((PortType) element);
  	  	}
  	  	else if (element instanceof Operation) {
  	  		partInfo = getPartInfo(((WSDLElementImpl) element).getContainer());
  	  	}
  	  	else if (element instanceof MessageReferenceImpl) {
  	  		partInfo = getPartInfo(((WSDLElementImpl) element).getContainer());
  	  	}

  	  	if (partInfo == null) {
  	  		partInfo = CreateWSDLElementHelper.PART_INFO_TYPE_DEFINITION;
  	  	}
  	  	
  	  	return partInfo;  	
  	  }
  	  
  	  private static String getPartInfo(PortType portType) {
  	  	String partInfo = null;
  	  	
  	  	if (portType.getOperations() != null) {
  	  		Iterator operationIt = portType.getOperations().iterator();
  	  		while (operationIt.hasNext()) {
  	  			Operation op = (Operation) operationIt.next();

  	  			if (op.getEInput() != null) {
  	  				partInfo = getMessageRefPartInfo((MessageReferenceImpl) op.getEInput());
  	  			}
  	  	
  	  			if (partInfo == null && op.getEOutput() != null) {
  	  				partInfo = getMessageRefPartInfo((MessageReferenceImpl) op.getEOutput());
  	  			}
  	  	
  	  			if (op.getEFaults() != null) {
  	  				Iterator faultIt = op.getEFaults().iterator();
  	  				while (partInfo == null && faultIt.hasNext()) {
  	  					Fault fault = (Fault) faultIt.next();
  	  					partInfo = getMessageRefPartInfo((MessageReferenceImpl) fault);
  	  				}
  	  			}
  	  	  	  	
  	  			if (partInfo != null)
  	  				break;
  	  		}
  	  	}

  	  	return partInfo;
  	  }
  	  
  	  private static String getMessageRefPartInfo(MessageReferenceImpl iof) {
  	  	String partInfo = null;
  	  	
  	  	if (iof.getEMessage() != null && iof.getEMessage().getEParts() != null) {
  	  		Iterator partIt = iof.getEMessage().getEParts().iterator();
  	  	
  	  		while (partInfo == null && partIt.hasNext()) {
  	  			Part part = (Part) partIt.next();
  	  		
  	  			if (part.getTypeDefinition() != null) {
  	  				partInfo = CreateWSDLElementHelper.PART_INFO_TYPE_DEFINITION;
  	  			}
  	  			else if (part.getElementDeclaration() != null) {
  	  				partInfo = CreateWSDLElementHelper.PART_INFO_ELEMENT_DECLARATION;
  	  			}
  	  		}
  	  	}
  	  	
  	  	return partInfo;
  	  }
}
