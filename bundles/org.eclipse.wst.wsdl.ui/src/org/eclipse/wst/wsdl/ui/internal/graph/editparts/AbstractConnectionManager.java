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
import org.eclipse.wst.wsdl.PortType;
import org.eclipse.wst.wsdl.WSDLElement;
import org.eclipse.wst.wsdl.internal.util.WSDLSwitch;
import org.eclipse.wst.wsdl.ui.internal.util.WSDLEditorUtil;
import org.eclipse.wst.wsdl.util.WSDLConstants;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

                                                   
public class AbstractConnectionManager implements IConnectionManager
{            
  public static final int PROPAGATE_FORWARD = 0;
  public static final int PROPAGATE_BACK    = 1;
  public static final int SELECTED          = 2;

  protected GroupEditPart groupEditPart;

  public AbstractConnectionManager(GroupEditPart groupEditPart)
  {
    this.groupEditPart = groupEditPart;
  }                                    
                                                
  protected WSDLSwitch createSwitch(int selectionType)
  {
    return new WSDLSwitch();
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
    propagateForwardToNext(model);
  }

  public void propagateBack(Object model)
  {                               
    if (model instanceof WSDLElement)
    {                        
      WSDLSwitch theSwitch = createSwitch(PROPAGATE_BACK);
      theSwitch.doSwitch((EObject)model);   
      groupEditPart.scrollToRevealOutputConnection();
    }
    else
    {
      removeConnections();
    }
    propagateBackToPrevious(model);
  }

  public void setSelectedModelObject(Object model)
  {  
	if (model instanceof Node)
	{
    	Node node = (Node)model;
		//Definition definition = groupEditPart.getDefinition();
        while(node != null)
        {        
    	  if (WSDLConstants.WSDL_NAMESPACE_URI.equals(node.getNamespaceURI()))
    	  {
    		break;
    	  }  
    	  else
    	  {
    	  	node = node.getParentNode();
    	  }
        }
        if (node instanceof Element)
        {
        	model = WSDLEditorUtil.getInstance().findModelObjectForElement( groupEditPart.getDefinition(), (Element)node);
        }  
	}
	  	
    if (model instanceof WSDLElement)
    {
      WSDLSwitch theSwitch = createSwitch(SELECTED);
      theSwitch.doSwitch((EObject)model);                                                       
    }
    else 
    {
      removeConnections();
    }
    propagateForwardToNext(model);
    propagateBackToPrevious(model);
  } 

 
  protected void removeConnections()
  {
    groupEditPart.setEmphasizedModelObject(null);
    groupEditPart.setInputConnectionModelObject(null);
    groupEditPart.setOutputConnectionModelObject(null);
  }
     
  protected void propagateForwardToNext(Object model)
  {
    GroupEditPart group = groupEditPart.getNext();
    if (group != null)
    {
      IConnectionManager connectionManager = group.getConnectionManager();
      if (connectionManager != null)
      {
        connectionManager.propagateForward(model);
      }
    }
  }

  protected void propagateBackToPrevious(Object model)
  {
    GroupEditPart group = groupEditPart.getPrevious();
    if (group != null)
    {
      IConnectionManager connectionManager = group.getConnectionManager();
      if (connectionManager != null)
      {
        connectionManager.propagateBack(model);
      }
    }
  }  

  protected Binding getEnclosingBinding(EObject object)
  { 
    Binding binding = null;
    if (object instanceof Binding)
    {     
      binding = (Binding)object;
    }
    else if (object instanceof BindingOperation)
    {     
      binding = (Binding)object.eContainer();
    }
    else if (object instanceof BindingInput ||
             object instanceof BindingOutput ||
             object instanceof BindingFault)
    {
      binding = (Binding)object.eContainer().eContainer();
    }
    return binding;
  }   

  protected PortType getEnclosingPortType(EObject object)
  { 
    PortType portType = null;
    if (object instanceof Operation)
    {     
      portType = (PortType)object.eContainer();
    }
    else if (object instanceof Input ||
             object instanceof Output ||
             object instanceof Fault)
    {
    	
      portType = object.eContainer() != null ? (PortType)object.eContainer().eContainer() : null;
    } 
    else if (object instanceof PortType)
    {
      portType = (PortType)object;	
    }                         
    return portType;
  }
  
  protected Operation getEnclosingOperation(EObject object)
  { 
    Operation operation = null;
    if (object instanceof Operation)
    {     
      operation = (Operation)object;
    }
    else if (object instanceof Input ||
             object instanceof Output ||
             object instanceof Fault)
    {
      operation = (Operation)object.eContainer();
    } 	                
    return operation;
  } 
	
  protected EObject getMessageReference(EObject object)
  {      
    return (object instanceof Input ||
            object instanceof Output ||
            object instanceof Fault) ? object : null;  
  }	
}  