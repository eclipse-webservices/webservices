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
                                  
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.wst.wsdl.Binding;
import org.eclipse.wst.wsdl.BindingFault;
import org.eclipse.wst.wsdl.BindingInput;
import org.eclipse.wst.wsdl.BindingOperation;
import org.eclipse.wst.wsdl.BindingOutput;
import org.eclipse.wst.wsdl.Fault;
import org.eclipse.wst.wsdl.Input;
import org.eclipse.wst.wsdl.Operation;
import org.eclipse.wst.wsdl.Output;
import org.eclipse.wst.wsdl.Port;
import org.eclipse.wst.wsdl.PortType;
import org.eclipse.wst.wsdl.ui.internal.util.ComponentReferenceUtil;
import org.eclipse.wst.wsdl.util.WSDLSwitch;

                                                   
public class BindingsGroupConnectionManager extends AbstractConnectionManager
{ 
  public BindingsGroupConnectionManager(GroupEditPart groupEditPart)
  {
    super(groupEditPart);
  }                                    

    
  class InternalWSDLSwitch extends WSDLSwitch
  {                             
    protected ComponentReferenceUtil util = new ComponentReferenceUtil(groupEditPart.getDefinition());

    public Object caseBinding(Binding binding)
    {                
      groupEditPart.setEmphasizedModelObject(binding);      
      groupEditPart.setInputConnectionModelObject(binding);
      groupEditPart.setOutputConnectionModelObject(binding);
      return Boolean.TRUE;
    }  
      
    public Object caseBindingFault(BindingFault bindingFault)
    {    
      groupEditPart.setEmphasizedModelObject(getEnclosingBinding(bindingFault)); 
      groupEditPart.setInputConnectionModelObject(getEnclosingBinding(bindingFault));
      groupEditPart.setOutputConnectionModelObject(bindingFault);
      return Boolean.TRUE;
    }

    public Object caseBindingInput(BindingInput bindingInput)
    {                                                
      groupEditPart.setEmphasizedModelObject(getEnclosingBinding(bindingInput)); 
      groupEditPart.setInputConnectionModelObject(getEnclosingBinding(bindingInput));
      groupEditPart.setOutputConnectionModelObject(bindingInput);
      return Boolean.TRUE;
    }
      
    public Object caseBindingOperation(BindingOperation bindingOperation)
    {                       
      groupEditPart.setEmphasizedModelObject(getEnclosingBinding(bindingOperation));
      groupEditPart.setInputConnectionModelObject(getEnclosingBinding(bindingOperation));
      groupEditPart.setOutputConnectionModelObject(bindingOperation);
      return Boolean.TRUE;
    }
      
    public Object caseBindingOutput(BindingOutput bindingOutput)
    {                
      groupEditPart.setEmphasizedModelObject(getEnclosingBinding(bindingOutput));
      groupEditPart.setInputConnectionModelObject(getEnclosingBinding(bindingOutput));
      groupEditPart.setOutputConnectionModelObject(bindingOutput);
      return Boolean.TRUE;
    }                                            
                 
    public Object caseFault(Fault fault)
    {                                               
      handleInterfaceHelper(fault); 
      return Boolean.TRUE;
    } 
          
    public Object caseInput(Input input)
    { 
                
      handleInterfaceHelper(input);
      return Boolean.TRUE;
    }     
            
    public Object caseOperation(Operation operation) 
    {      
      handleInterfaceHelper(operation);     
      return Boolean.TRUE;
    }
          
    public Object caseOutput(Output output)
    {  
      handleInterfaceHelper(output);
      return Boolean.TRUE;
    }   
                                        
    public Object casePortType(PortType portType)
    {                     
      handleInterfaceHelper(portType);
      return Boolean.TRUE;
    }  

    public Object casePort(Port port)
    {          
      Binding binding = port.getEBinding();
      groupEditPart.setEmphasizedModelObject(binding); 
      groupEditPart.setInputConnectionModelObject(binding);
      groupEditPart.setOutputConnectionModelObject(binding);
      return Boolean.TRUE;
    }                                         
      
    public Object defaultCase(EObject object)
    {                       
      groupEditPart.setEmphasizedModelObject(null);
      groupEditPart.setInputConnectionModelObject(null);
      groupEditPart.setOutputConnectionModelObject(null);
      return Boolean.TRUE;
    }   
    
    protected Binding getPreviousMatchingBinding(PortType portType)
    {
      Binding result = null;
      Object previousContext = groupEditPart.getOutputConnectionModelObject();
      if (previousContext == null)      
      {
        previousContext = groupEditPart.getEmphasizedModelObject();
      }
      if (previousContext instanceof EObject) 
      {
        Binding binding = getEnclosingBinding((EObject)previousContext);
        if (binding.getEPortType() == portType)
        {
          result = binding;  
        }        
      }
      return result;
    }     

    protected void handleInterfaceHelper(EObject interfaceObject)
    {   
      Binding binding = null;
      PortType portType = getEnclosingPortType(interfaceObject);
      if (portType != null)
      {              
        binding = getPreviousMatchingBinding(portType);     
        if (binding == null)
        {      
          List list = util.getBindings(portType);
          binding = list.size() > 0 ? (Binding)list.get(0) : null;  
        }
        if (binding != null)
        {
          groupEditPart.setEmphasizedModelObject(binding);   
          groupEditPart.setInputConnectionModelObject(binding);     
          EObject bindingObject = util.getBindingObject(interfaceObject, binding);
          groupEditPart.setOutputConnectionModelObject(bindingObject);                  
        }
      }
      if (binding == null)
      {
        defaultCase(null);
      }
    }   
  }    
    
  protected WSDLSwitch createSwitch(int selectionType)
  {
    return new InternalWSDLSwitch();
  }
  
  protected void propagateBackToPrevious(Object model)
  {   
    super.propagateBackToPrevious(groupEditPart.getInputConnectionModelObject());
  }  
}