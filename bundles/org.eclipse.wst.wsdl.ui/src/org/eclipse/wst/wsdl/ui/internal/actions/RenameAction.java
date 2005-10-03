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
package org.eclipse.wst.wsdl.ui.internal.actions; 

import java.util.Iterator;
import java.util.List;

import org.eclipse.wst.wsdl.Binding;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.Fault;
import org.eclipse.wst.wsdl.Input;
import org.eclipse.wst.wsdl.Message;
import org.eclipse.wst.wsdl.Operation;
import org.eclipse.wst.wsdl.Output;
import org.eclipse.wst.wsdl.PortType;
import org.eclipse.wst.wsdl.WSDLElement;
import org.eclipse.wst.wsdl.ui.internal.util.ComponentReferenceUtil;
import org.eclipse.wst.wsdl.ui.internal.util.WSDLEditorUtil;
import org.eclipse.wst.wsdl.ui.internal.visitor.BindingRenamer;
import org.eclipse.wst.wsdl.ui.internal.visitor.MessageRenamer;
import org.eclipse.wst.wsdl.ui.internal.visitor.PortTypeRenamer;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

//import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;

public class RenameAction //extends BaseNodeAction implements Runnable
{
  protected Object modelObject;                 
  protected String newName;
  protected Node node;                        

  public RenameAction(Object modelObject, String newName)
  {                          
    this.modelObject = modelObject;
    this.newName = newName;
    this.node = WSDLEditorUtil.getInstance().getNodeForObject(modelObject);
  }      

  /*
  public Node getNode()
  {
    return node;
  }
  */

  /*
  public String getUndoDescription()
  {
    return WSDLEditorPlugin.getWSDLString("_UI_ACTION_RENAME");  //$NON-NLS-1$
  }
  */

  public void run()
  {
    if (modelObject instanceof Operation) 
    {
      renameOperationHelper((Operation)modelObject);
    }  
    else if (modelObject instanceof Input ||
             modelObject instanceof Output ||
             modelObject instanceof Fault)
    {
      renameIOFHelper((WSDLElement)modelObject);
    }
    else
    {
      renameModelObjectHelper(modelObject, newName);
    }
  } 

  protected void renameModelObjectHelper(Object modelObject, String theNewName)
  {
    Element element = WSDLEditorUtil.getInstance().getElementForObject(modelObject);
    if (element != null)
    { 
      element.setAttribute("name", theNewName);
    }    

    if (modelObject instanceof Message)
    {
      MessageRenamer renamer = new MessageRenamer((Message)modelObject, theNewName);
      renamer.visitBindings();
    }
    else if (modelObject instanceof PortType)
    {
      PortTypeRenamer renamer = new PortTypeRenamer((PortType)modelObject, theNewName);
      renamer.visitBindings();
    }
    else if (modelObject instanceof Binding)
    {
      BindingRenamer renamer = new BindingRenamer((Binding)modelObject, theNewName);
      renamer.visitServices();
    }
  }

  protected void renameOperationHelper(Operation operation)
  {
    renameModelObjectHelper(operation, newName);
  }


  protected void renameIOFHelper(WSDLElement object)
  {                         
    Definition definition = object.getEnclosingDefinition(); 
    ComponentReferenceUtil util = new ComponentReferenceUtil(definition);
                          
    List list = null;

    if (object instanceof Input)
    {
      list = util.getBindingInputs((Input)object);
    }
    else if (object instanceof Output)
    {
      list = util.getBindingOutputs((Output)object);
    }
    else // fault
    {
      list = util.getBindingFaults((Fault)object);
    }
      
    if (list != null)
    {
      for (Iterator i = list.iterator(); i.hasNext(); )
      {
        Object bindingObject = i.next();
        renameModelObjectHelper(bindingObject, newName);
      }                                           
    }

    renameModelObjectHelper(object, newName);
  }  
}