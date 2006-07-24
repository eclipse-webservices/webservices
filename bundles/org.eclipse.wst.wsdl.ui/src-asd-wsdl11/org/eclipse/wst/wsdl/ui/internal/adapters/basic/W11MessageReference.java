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
package org.eclipse.wst.wsdl.ui.internal.adapters.basic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.gef.commands.Command;
import org.eclipse.swt.graphics.Image;
import org.eclipse.wst.wsdl.Fault;
import org.eclipse.wst.wsdl.Input;
import org.eclipse.wst.wsdl.MessageReference;
import org.eclipse.wst.wsdl.Operation;
import org.eclipse.wst.wsdl.Output;
import org.eclipse.wst.wsdl.ui.internal.Messages;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.adapters.WSDLBaseAdapter;
import org.eclipse.wst.wsdl.ui.internal.adapters.actions.W11AddPartAction;
import org.eclipse.wst.wsdl.ui.internal.adapters.actions.W11SetExistingMessageAction;
import org.eclipse.wst.wsdl.ui.internal.adapters.actions.W11SetNewMessageAction;
import org.eclipse.wst.wsdl.ui.internal.adapters.commands.W11AddFaultParameterCommand;
import org.eclipse.wst.wsdl.ui.internal.adapters.commands.W11AddInputParameterCommand;
import org.eclipse.wst.wsdl.ui.internal.adapters.commands.W11AddOutputParameterCommand;
import org.eclipse.wst.wsdl.ui.internal.adapters.commands.W11DeleteCommand;
import org.eclipse.wst.wsdl.ui.internal.adapters.commands.W11ReorderParametersCommand;
import org.eclipse.wst.wsdl.ui.internal.asd.actions.ASDAddFaultAction;
import org.eclipse.wst.wsdl.ui.internal.asd.actions.ASDAddOperationAction;
import org.eclipse.wst.wsdl.ui.internal.asd.actions.ASDDeleteAction;
import org.eclipse.wst.wsdl.ui.internal.asd.actions.BaseSelectionAction;
import org.eclipse.wst.wsdl.ui.internal.asd.actions.ShowPropertiesViewAction;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IASDObject;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IASDObjectListener;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IMessageReference;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IOperation;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IParameter;
import org.eclipse.wst.wsdl.ui.internal.asd.outline.ITreeElement;
import org.eclipse.wst.wsdl.ui.internal.visitor.WSDLVisitorForParameters;


public class W11MessageReference extends WSDLBaseAdapter implements IMessageReference, IASDObjectListener
{
  protected int messageKind = -1;
  protected List parameters = null;
  protected List otherThingsToListenTo = null;

  public W11MessageReference(int messageKind)
  {
    this.messageKind = messageKind;
  }

  public IOperation getOwnerOperation()
  {
    return (IOperation) owner;
  }
  
  public String getPreview() {
	  String previewString = "()";
	  List params = getParameters();
	  // For now, just look at the first Part for the preview
	  if (params.size() > 0) {
		  IParameter param = (IParameter) params.get(0);
		  
		  previewString = param.getPreview();
	  }	  
	  
	  return previewString;
  }
  
  // Convenience method
  public MessageReference getMessageReference()
  {
    return (MessageReference) target;
  }

  protected void computeMessageKind()
  {
    if (getMessageReference() instanceof Input)
    {
      messageKind = KIND_INPUT;
    }
    else if (getMessageReference() instanceof Output)
    {
      messageKind = KIND_OUTPUT;
    }
    else if (getMessageReference() instanceof Fault)
      messageKind = KIND_FAULT;
  }

  public int getKind()
  {
	  if (messageKind == -1)
	  {
		  computeMessageKind();
	  }
	  return messageKind;
  }
  
  public String getName()
  {
	  MessageReference messageRef = getMessageReference();
	  if (messageRef.getName() != null) {
		  return messageRef.getName();
	  }
	  
	  return "";
  }

  /*
   * XSD Elements or WSDL Parts...
   */
  public List getParameters()
  {
	  List parameters = new ArrayList();
	  List parts = new ArrayList();
	  MessageReference messageRef = getMessageReference();
	  if (messageRef != null) {
		  if (messageRef.getEMessage() != null) {
			  if (messageRef.getEMessage().getEParts() != null) {
				  parts = messageRef.getEMessage().getEParts();
			  }
		  }
	  }
	  
	  populateAdapterList(parts, parameters);
	  
	  otherThingsToListenTo = new ArrayList();
      WSDLVisitorForParameters visitorForParameters = new WSDLVisitorForParameters();
      visitorForParameters.visitMessageReference(messageRef);
      populateAdapterList(visitorForParameters.concreteComponents, otherThingsToListenTo);
      populateAdapterList(visitorForParameters.thingsToListenTo, otherThingsToListenTo);
      
      // now we listen to all the 'things we need to listen to'
      //
      for (Iterator i = otherThingsToListenTo.iterator(); i.hasNext();)
      {
        Adapter adapter = (Adapter) i.next();
        if (adapter instanceof IASDObject)
        {
          IASDObject asdObject = (IASDObject) adapter;
          asdObject.registerListener(this);
        }
      }
	  
	  return parameters;
    }
  
  public void propertyChanged(Object object, String property)
  {
    // this method is called when one of the 'otherThingsToListenTo' has changed
    // when one of these things changes it means our paramter list may have changes
    // so we need to recompute it
    Object notifier = null;
    if (object instanceof Adapter)
    {
      notifier = ((Adapter)object).getTarget();
    }  
    //System.out.println("something in the 'otherThingsToListenTo' list has changed " + notifier);    
    clearParameters();
    notifyListeners(this, null);     
  }
  
  protected void clearParameters()
  {
    if (otherThingsToListenTo != null)
    {
      for (Iterator i = otherThingsToListenTo.iterator(); i.hasNext();)
      {
        Adapter adapter = (Adapter) i.next();
        if (adapter instanceof IASDObject)
        {
          IASDObject asdObject = (IASDObject) adapter;
          asdObject.unregisterListener(this);
        }
      }
    }
    parameters = null;    
    otherThingsToListenTo = null;
  }
  
  public void notifyChanged(Notification msg)
  {
    clearParameters();    
    super.notifyChanged(msg);    
  }
  
  public String[] getActions(Object object)
  {
	  List actions = new ArrayList();
	  actions.add(ASDAddOperationAction.ID);	  
	  actions.addAll(((W11Operation) getOwnerOperation()).getValidInputOutpuActions()); 
	  actions.add(ASDAddFaultAction.ID);
	  actions.add(W11AddPartAction.ID);
	  actions.add(ASDDeleteAction.ID);
	  actions.add(BaseSelectionAction.SUBMENU_START_ID + Messages.getString("_UI_ACTION_SET_MESSAGE")); //$NON-NLS-1$
	  actions.add(W11SetNewMessageAction.ID);
	  actions.add(W11SetExistingMessageAction.ID);
	  actions.add(BaseSelectionAction.SUBMENU_END_ID);
    actions.add(ShowPropertiesViewAction.ID);
//    if (isReadOnly()) {
//      actions.add(OpenInNewEditor.ID);
//    }

	  String[] actionIDs = new String[actions.size()];
	  for (int index = 0; index < actions.size(); index++) {
		  actionIDs[index] = (String) actions.get(index);
	  }
	  
	  return actionIDs;
  }
  
  public Command getReorderParametersCommand(IParameter leftSibling, IParameter rightSibling, IParameter movingParameter) {
	  return new W11ReorderParametersCommand(leftSibling, rightSibling, movingParameter);
  }

  public Command getDeleteCommand() {
	  return new W11DeleteCommand(this);
  }
  
  public Command getAddParamterCommand() {
      Command command = null;
      Operation operation = (Operation)getMessageReference().eContainer();
      if (getKind() == KIND_INPUT)
      {    
        command = new W11AddInputParameterCommand(operation);
      }  
      else if (getKind() == KIND_OUTPUT)
      {
        command = new W11AddOutputParameterCommand(operation);        
      } 
      else
      {
        command = new W11AddFaultParameterCommand(operation, (Fault)getMessageReference());        
      } 
      return command;
  }  
  
	public Image getImage() {
	    if (getKind() == KIND_INPUT)
	    {
	    	return WSDLEditorPlugin.getInstance().getImage("icons/input_obj.gif"); //$NON-NLS-1$
	    }
	    else if (getKind() == KIND_OUTPUT)
	    {
	    	return WSDLEditorPlugin.getInstance().getImage("icons/output_obj.gif"); //$NON-NLS-1$
	    }
	    else if (getKind() == KIND_FAULT)
	    {
	    	return WSDLEditorPlugin.getInstance().getImage("icons/fault_obj.gif"); //$NON-NLS-1$
	    }
	    return null;
	}
	
	public String getText() {
	    if (getKind() == KIND_INPUT)
	    {
	      return "Input";
	    }
	    else if (getKind() == KIND_OUTPUT)
	    {
	      return "Output";
	    }
	    else if (getKind() == KIND_FAULT)
	    {
	      return getName();
	    }
	    return "NoName";
	}
    
	public List getParameters2() 
    {
	  if (parameters == null)
	  {
	    parameters = new ArrayList();
	    otherThingsToListenTo = new ArrayList();
	    WSDLVisitorForParameters visitorForParameters = new WSDLVisitorForParameters();
	    visitorForParameters.visitMessageReference(getMessageReference());
	    populateAdapterList(visitorForParameters.concreteComponents, parameters);
	    populateAdapterList(visitorForParameters.thingsToListenTo, otherThingsToListenTo);

	    // now we listen to all the 'things we need to listen to'
	    //
	    for (Iterator i = otherThingsToListenTo.iterator(); i.hasNext();)
	    {
	      Adapter adapter = (Adapter) i.next();
	      if (adapter instanceof IASDObject)
	      {
	        IASDObject asdObject = (IASDObject) adapter;
	        asdObject.registerListener(this);
	      }
	    } 
	  }
	  return parameters;
	}    
	
	public ITreeElement[] getChildren() {
      /*
		List parts = getParameters();
		ITreeElement[] treeElements = new ITreeElement[parts.size()];
		
		for (int index = 0; index < parts.size(); index++) {
			treeElements[index] = (ITreeElement) parts.get(index);
		}
		*/
		return ITreeElement.EMPTY_LIST;
	}

	public boolean hasChildren() {
		if (getChildren().length > 0) {
			return true;
		}
		
		return false;
	}

	public ITreeElement getParent() {
		return null;
	}
  }