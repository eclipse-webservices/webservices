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
import org.eclipse.wst.wsdl.internal.util.WSDLSwitch;

                                                   
public class ServicesGroupConnectionManager extends AbstractConnectionManager
{   
  public ServicesGroupConnectionManager(GroupEditPart groupEditPart)
  {
    super(groupEditPart);
  }                                    
    
  protected class InternalWSDLSwitch extends WSDLSwitch
  {
   	public Object caseBinding(Binding binding)
    {                   
      handleBindingHelper(binding);
      return Boolean.TRUE;
    }         

    public Object caseBindingFault(BindingFault bindingFault)
    {    
      handleBindingHelper(getEnclosingBinding(bindingFault)); 
      return Boolean.TRUE;
    }

    public Object caseBindingInput(BindingInput bindingInput)
    {    
      handleBindingHelper(getEnclosingBinding(bindingInput)); 
      return Boolean.TRUE;
    }
      
    public Object caseBindingOperation(BindingOperation bindingOperation)
    {                       
      handleBindingHelper(getEnclosingBinding(bindingOperation));
      return Boolean.TRUE;
    }
      
    public Object caseBindingOutput(BindingOutput bindingOutput)
    {                
      handleBindingHelper(getEnclosingBinding(bindingOutput));
      return Boolean.TRUE;
    }      

    public Object caseFault(Fault fault)
    {                      
      handlePortTypeHelper(getEnclosingPortType(fault));   
      return Boolean.TRUE;
    } 
          
    public Object caseInput(Input input)
    {       
      handlePortTypeHelper(getEnclosingPortType(input));   
      return Boolean.TRUE;
    }     
            
    public Object caseOperation(Operation operation) 
    {      
      handlePortTypeHelper(getEnclosingPortType(operation));    
      return Boolean.TRUE;
    }
          
    public Object caseOutput(Output output)
    {  
      handlePortTypeHelper(getEnclosingPortType(output));   
      return Boolean.TRUE;
    }  

   	public Object casePort(Port port)
    {           
      groupEditPart.setEmphasizedModelObject(port);     
      groupEditPart.setOutputConnectionModelObject(port);
      return Boolean.TRUE;
    }   

   	public Object casePortType(PortType portType)
    {           
      handlePortTypeHelper(portType);
      return Boolean.TRUE;
    }          
        
   	public Object defaultCase(EObject object)
    {
      groupEditPart.setEmphasizedModelObject(null);
      groupEditPart.setOutputConnectionModelObject(null);
      return Boolean.TRUE;
    }          

    //
    protected void handlePortTypeHelper(PortType portType)
    {       
      if (portType != null)
      { 
        Port port = null;   
        Object previousContext = groupEditPart.getOutputConnectionModelObject();
        if (previousContext == null)      
        {
          previousContext = groupEditPart.getEmphasizedModelObject();
        }
        if (previousContext instanceof Port)
        {
          Port previousPort = (Port)previousContext;
          if (previousPort.getEBinding() != null && previousPort.getEBinding().getEPortType() == portType)
          {
            port = previousPort;
          }          
        }                  

        if (port == null)
        {
          ComponentReferenceUtil util = new ComponentReferenceUtil(groupEditPart.getDefinition());           
          List list = util.getPortsForPortType(portType);          
          if (list.size() > 0)
          {
            port = (Port)list.get(0);
          }                          
        }

        groupEditPart.setEmphasizedModelObject(port);     
        groupEditPart.setOutputConnectionModelObject(port);
      } 
    }   

    protected void handleBindingHelper(Binding binding)
    {       
      if (binding != null)
      {
        ComponentReferenceUtil util = new ComponentReferenceUtil(groupEditPart.getDefinition());
        List list = util.getPortsForBinding(binding);
        Port port = null;
        if (list.size() > 0)
        {
          port = (Port)list.get(0);
        }                          
        groupEditPart.setEmphasizedModelObject(port);     
        groupEditPart.setOutputConnectionModelObject(port);                                          
      }
    }
  }

  protected WSDLSwitch createSwitch(int selectionType)
  {
    return new InternalWSDLSwitch();
  }
}