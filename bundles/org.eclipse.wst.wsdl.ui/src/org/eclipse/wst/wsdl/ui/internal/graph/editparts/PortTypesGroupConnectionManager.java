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
package org.eclipse.wst.wsdl.ui.internal.graph.editparts;
                                  
import java.util.Iterator;

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
import org.eclipse.wst.wsdl.Port;
import org.eclipse.wst.wsdl.PortType;
import org.eclipse.wst.wsdl.WSDLElement;
import org.eclipse.wst.wsdl.internal.util.WSDLSwitch;
import org.eclipse.wst.wsdl.ui.internal.model.WSDLGroupObject;
import org.eclipse.wst.wsdl.ui.internal.util.ComponentReferenceUtil;

                                                   
public class PortTypesGroupConnectionManager extends AbstractConnectionManager
{ 
  public PortTypesGroupConnectionManager(GroupEditPart groupEditPart)
  {
    super(groupEditPart);
  }                                    

    
  class InternalWSDLSwitch extends WSDLSwitch
  {        
    public boolean isBindingGroupShowing()
    {
      GroupEditPart prevGroupEditPart = groupEditPart.getPrevious();
      return prevGroupEditPart  != null && prevGroupEditPart.getType() == WSDLGroupObject.BINDINGS_GROUP;
    } 

    public Object caseBinding(Binding binding)
    {                             
      Object portType = getPortTypeForBindingObject(binding);
      groupEditPart.setEmphasizedModelObject(portType);      
      groupEditPart.setInputConnectionModelObject(portType);
      groupEditPart.setOutputConnectionModelObject(null);
      return Boolean.TRUE;
    }  

    public Object caseBindingFault(BindingFault bindingFault)
    {                
      groupEditPart.setEmphasizedModelObject(getPortTypeForBindingObject(bindingFault));
      Fault fault = ComponentReferenceUtil.computeFault(bindingFault);
      groupEditPart.setInputConnectionModelObject(fault);
      groupEditPart.setOutputConnectionModelObject(fault);
      return Boolean.TRUE;
    } 
      
    public Object caseBindingInput(BindingInput bindingInput)
    {    
      groupEditPart.setEmphasizedModelObject(getPortTypeForBindingObject(bindingInput)); 
      Input input = ComponentReferenceUtil.computeInput(bindingInput);
      groupEditPart.setInputConnectionModelObject(input);
      groupEditPart.setOutputConnectionModelObject(input);
      return Boolean.TRUE;
    }
      
    public Object caseBindingOperation(BindingOperation bindingOperation)
    {                       
      groupEditPart.setEmphasizedModelObject(getPortTypeForBindingObject(bindingOperation));
      groupEditPart.setInputConnectionModelObject(ComponentReferenceUtil.computeOperation(bindingOperation));
      groupEditPart.setOutputConnectionModelObject(null);
      return Boolean.TRUE;
    }
      
    public Object caseBindingOutput(BindingOutput bindingOutput)
    {                
      groupEditPart.setEmphasizedModelObject(getPortTypeForBindingObject(bindingOutput));
      Output output = ComponentReferenceUtil.computeOutput(bindingOutput);
      groupEditPart.setInputConnectionModelObject(output);
      groupEditPart.setOutputConnectionModelObject(output);
      return Boolean.TRUE;
    }                                            
                 
    public Object caseFault(Fault fault)
    {                      
      groupEditPart.setEmphasizedModelObject(getEnclosingPortType(fault));   
      if (isBindingGroupShowing())
      {
        groupEditPart.setInputConnectionModelObject(fault);
      }
      else
      {
        groupEditPart.setInputConnectionModelObject(getEnclosingPortType(fault));
      }
      groupEditPart.setOutputConnectionModelObject(fault);
      return Boolean.TRUE;
    } 
          
    public Object caseInput(Input input)
    {       
      groupEditPart.setEmphasizedModelObject(getEnclosingPortType(input));   
      if (isBindingGroupShowing())
      {
        groupEditPart.setInputConnectionModelObject(input);
      }
      else
      {
        groupEditPart.setInputConnectionModelObject(getEnclosingPortType(input));
      }
      groupEditPart.setOutputConnectionModelObject(input);
      return Boolean.TRUE;
    }     
    
    public Object caseMessage(Message message)
    {
      Object previousContext = groupEditPart.getOutputConnectionModelObject();
      if (previousContext == null)      
      {
        previousContext = groupEditPart.getEmphasizedModelObject();
      }
      
      EObject messageReference = null;      
      if (previousContext == null || previousContext instanceof EObject)
      {
      messageReference = getMessageReference(message, (EObject)previousContext);
      }

      if (messageReference != null)
      {
        doSwitch(messageReference);
      }
      else
      {
      defaultCase(null);
      }
      return Boolean.TRUE;
    }  
            
    public Object caseOperation(Operation operation) 
    {      
      groupEditPart.setEmphasizedModelObject(getEnclosingPortType(operation));
      if (isBindingGroupShowing())
      {
        groupEditPart.setInputConnectionModelObject(operation);
      }
      else
      {
        groupEditPart.setInputConnectionModelObject(getEnclosingPortType(operation));
      }
      groupEditPart.setOutputConnectionModelObject(null);      
      return Boolean.TRUE;
    }
          
    public Object caseOutput(Output output)
    {  
      groupEditPart.setEmphasizedModelObject(getEnclosingPortType(output));   
      if (isBindingGroupShowing())
      {
        groupEditPart.setInputConnectionModelObject(output);
      }
      else
      {
        groupEditPart.setInputConnectionModelObject(getEnclosingPortType(output));
      }
      groupEditPart.setOutputConnectionModelObject(output);  
      return Boolean.TRUE;
    }   
                                        
    public Object casePortType(PortType portType)
    { 
      groupEditPart.setEmphasizedModelObject(portType); 
      groupEditPart.setInputConnectionModelObject(portType);
      groupEditPart.setOutputConnectionModelObject(null);
      return Boolean.TRUE;
    }  

    public Object casePort(Port port)
    {          
      Binding binding = port.getEBinding();
      PortType portType = binding != null ? binding.getEPortType() : null;
      groupEditPart.setEmphasizedModelObject(portType);     
      groupEditPart.setInputConnectionModelObject(portType);
      groupEditPart.setOutputConnectionModelObject(null);
      return Boolean.TRUE;
    }                                         
      
    public Object defaultCase(EObject object)
    {                       
      groupEditPart.setEmphasizedModelObject(null);
      groupEditPart.setInputConnectionModelObject(null);
      groupEditPart.setOutputConnectionModelObject(null);
      return Boolean.TRUE;
    }      
  } 

  protected WSDLSwitch createSwitch(int selectionType)
  {
    return new InternalWSDLSwitch();
  }

  protected PortType getPortTypeForBindingObject(EObject object)
  { 
    Binding binding = getEnclosingBinding(object);
    return binding.getEPortType();
  }   
            
  public void propagateForward(Object model)
  { 
    if (model instanceof WSDLElement)
    {
      WSDLSwitch theSwitch = createSwitch(PROPAGATE_FORWARD);
      theSwitch.doSwitch((EObject)model);    
      groupEditPart.scrollToRevealInputConnection();
    }       
    else
    {
      removeConnections();
    }

    Object o = groupEditPart.getOutputConnectionModelObject();
    propagateForwardToNext(o != null ? o : model); 
  }
  
  protected void propagateBackToPrevious(Object model)
  {
    model = (model instanceof Message) ? groupEditPart.getInputConnectionModelObject() : model; 
    super.propagateBackToPrevious(model);
  } 
  

  protected Message getReferencedMessage(EObject o)
  {      
    Message result = null;
    if (o instanceof Input)
    {
      result = ((Input)o).getEMessage();
    }                                   
    else if (o instanceof Output)
    {
      result = ((Output)o).getEMessage();
    }  
    else if (o instanceof Fault)
    {
      result = ((Fault)o).getEMessage();
    }                     
    return result;
  }
  
  protected boolean isMatchingMessageReference(EObject messageReference, Message message)
  {
    Message m = getReferencedMessage(messageReference);
    return (m != null) && (m == messageReference);
  }
  
  public EObject getMessageReference(Message message, EObject prevContext)
  {
    EObject result = null;
        
    PortType prevPortType = getEnclosingPortType(prevContext);
    Operation prevOperation = getEnclosingOperation(prevContext);  
    EObject prevMessageReference = getMessageReference(prevContext);
    
    if (prevPortType != null)
    {
      result = getMessageReference(message, prevPortType, prevOperation, prevMessageReference);  
    }   
    if (result == null)
    {    
      Definition definition = groupEditPart.getDefinition();
      ComponentReferenceUtil util = new ComponentReferenceUtil(definition);
      for (Iterator i = util.getPortTypes().iterator(); i.hasNext(); )
      {
        PortType portType = (PortType)i.next();              
        result = getMessageReference(message, portType, null, null);  
        
        if (result != null)
        {
          break;
        }
      }
    }
    return result;
  }
  
  public EObject getMessageReference(Message message, PortType portType, Operation prevOperation, EObject prevMessageReference)
  {
    EObject result = null;
    if (prevMessageReference != null && isMatchingMessageReference(prevMessageReference, message)) 
    {
      result = prevMessageReference;
    }
      
    if (result == null && prevOperation != null)
    {
      result = getMessageReference(message, prevOperation);       
    }
            
    if (result == null)
    {             
      for (Iterator i = portType.getOperations().iterator(); i.hasNext();)
      { 
        Operation operation = (Operation)i.next();
        result = getMessageReference(message, operation);
        if (result != null)
        {
          break; 
        }
      }
    }
    return result;    
  } 
  
  public EObject getMessageReference(Message message, Operation operation)
  {
    EObject result = null;
    Input input = operation.getEInput();
    Output output = operation.getEOutput(); 
    if (input != null && input.getMessage() == message)
    {
      result = input;
    }
    else if (output != null && output.getMessage() == message)
    {
      result = output;
    }
    if (result == null)
    {
      for (Iterator i = operation.getEFaults().iterator(); i.hasNext();)
      {
        Fault fault = (Fault)i.next();
        if (fault.getMessage() == message)
        {
          result = fault;
          break;
        }  
      }
    }
    return result;        
  }
}