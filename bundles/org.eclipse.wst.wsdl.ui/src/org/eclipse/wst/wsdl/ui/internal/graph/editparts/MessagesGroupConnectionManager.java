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
import org.eclipse.wst.wsdl.Fault;
import org.eclipse.wst.wsdl.Input;
import org.eclipse.wst.wsdl.Message;
import org.eclipse.wst.wsdl.Output;
import org.eclipse.wst.wsdl.Part;
import org.eclipse.wst.wsdl.internal.util.WSDLSwitch;

                                                   
public class MessagesGroupConnectionManager extends AbstractConnectionManager
{ 
  public MessagesGroupConnectionManager(GroupEditPart groupEditPart)
  {
    super(groupEditPart);
  }                                    
              
  class InternalWSDLSwitch extends WSDLSwitch
  {            
    public Object caseFault(Fault fault)
    {                      
      groupEditPart.setEmphasizedModelObject(fault.getEMessage());
      groupEditPart.setInputConnectionModelObject(fault.getEMessage());
      groupEditPart.setOutputConnectionModelObject(getFirstPart(fault.getEMessage()));
      return Boolean.TRUE;
    } 
          
    public Object caseInput(Input input)
    {       
      groupEditPart.setEmphasizedModelObject(input.getEMessage());
      groupEditPart.setInputConnectionModelObject(input.getEMessage());
      groupEditPart.setOutputConnectionModelObject(getFirstPart(input.getEMessage()));
      return Boolean.TRUE;
    }     

    public Object caseMessage(Message message)
    {  
      groupEditPart.setEmphasizedModelObject(message);  
      groupEditPart.setInputConnectionModelObject(message);
      groupEditPart.setOutputConnectionModelObject(getFirstPart(message));
      return Boolean.TRUE;
    } 
                      
    public Object caseOutput(Output output)
    {  
      groupEditPart.setEmphasizedModelObject(output.getEMessage());  
      groupEditPart.setInputConnectionModelObject(output.getEMessage());
      groupEditPart.setOutputConnectionModelObject(getFirstPart(output.getEMessage()));
      return Boolean.TRUE;
    }                                                         

    public Object casePart(Part part)
    {  
      groupEditPart.setEmphasizedModelObject(part.eContainer());  
      groupEditPart.setInputConnectionModelObject(part.eContainer());
      groupEditPart.setOutputConnectionModelObject(part);
      return Boolean.TRUE;
    } 
      
    public Object defaultCase(EObject object)
    {                       
      groupEditPart.setEmphasizedModelObject(null);
      groupEditPart.setInputConnectionModelObject(null);
      groupEditPart.setOutputConnectionModelObject(null);
      return Boolean.TRUE;
    }      

    public Object doSwitch(EObject theEObject)
    {
      Object result = super.doSwitch(theEObject);
      ((DefinitionEditPart)groupEditPart.getParent()).getPartReferenceSectionEditPart().setInput(groupEditPart.getOutputConnectionModelObject());
      return result;
    }
  }

  protected WSDLSwitch createSwitch(int selectionType)
  {
    return new InternalWSDLSwitch();
  }   

  protected Part getFirstPart(Message message)
  {           
    Part result = null;
    if (message != null)
    {    
      List parts = message.getEParts();
      result = (parts.size() > 0) ? (Part)parts.get(0) : null;
    }
    return result;
  }
  
  protected void propagateBackToPrevious(Object model)
  {  	
  	model = (model instanceof Part) ?((Part)model).eContainer() : model; 
    super.propagateBackToPrevious(model);
  }
}