/*******************************************************************************
 * Copyright (c) 2001, 2007 IBM Corporation and others.
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
import org.eclipse.wst.wsdl.Part;
import org.eclipse.wst.wsdl.ui.internal.Messages;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.adapters.WSDLBaseAdapter;
import org.eclipse.wst.wsdl.ui.internal.adapters.actions.W11AddPartAction;
import org.eclipse.wst.wsdl.ui.internal.adapters.actions.W11SetExistingMessageAction;
import org.eclipse.wst.wsdl.ui.internal.adapters.actions.W11SetNewMessageAction;
import org.eclipse.wst.wsdl.ui.internal.adapters.commands.W11AddFaultParameterCommand;
import org.eclipse.wst.wsdl.ui.internal.adapters.commands.W11AddInputParameterCommand;
import org.eclipse.wst.wsdl.ui.internal.adapters.commands.W11AddOutputParameterCommand;
import org.eclipse.wst.wsdl.ui.internal.adapters.commands.W11ReorderParametersCommand;
import org.eclipse.wst.wsdl.ui.internal.adapters.visitor.W11FindInnerElementVisitor;
import org.eclipse.wst.wsdl.ui.internal.asd.actions.ASDAddFaultAction;
import org.eclipse.wst.wsdl.ui.internal.asd.actions.ASDAddOperationAction;
import org.eclipse.wst.wsdl.ui.internal.asd.actions.ASDDeleteAction;
import org.eclipse.wst.wsdl.ui.internal.asd.actions.BaseSelectionAction;
import org.eclipse.wst.wsdl.ui.internal.asd.design.figures.ModelDiagnosticInfo;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IASDObject;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IASDObjectListener;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IMessageReference;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IOperation;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IParameter;
import org.eclipse.wst.wsdl.ui.internal.asd.outline.ITreeElement;
import org.eclipse.wst.wsdl.ui.internal.visitor.WSDLVisitorForParameters;
import org.eclipse.wst.xsd.ui.internal.adt.editor.ProductCustomizationProvider;
import org.eclipse.xsd.XSDElementDeclaration;


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
  
  /*
  private String getMessageString(String key, Object[] args) {
	  String string = null;

	  Object object = WSDLEditorPlugin.getInstance().getProductCustomizationProvider();
	  if (object instanceof ProductCustomizationProvider) {
		  ProductCustomizationProvider productCustomizationProvider = (ProductCustomizationProvider)object;
		  String newString = productCustomizationProvider.getProductString(key, args);
		  if (newString != null) {
			  string = newString;
		  }
	  }

	  return string;
  }
  */
  
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
	  
	  return ""; //$NON-NLS-1$
  }

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
//    Object notifier = null;
//    if (object instanceof Adapter)
//    {
//      notifier = ((Adapter)object).getTarget();
//    }  
//    System.out.println("something in the 'otherThingsToListenTo' list has changed " + notifier);    
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
	  actions.add(BaseSelectionAction.SUBMENU_START_ID + Messages._UI_ACTION_SET_MESSAGE); //$NON-NLS-1$
	  actions.add(W11SetNewMessageAction.ID);
	  actions.add(W11SetExistingMessageAction.ID);
	  actions.add(BaseSelectionAction.SUBMENU_END_ID);
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
	      return "input"; //$NON-NLS-1$
	    }
	    else if (getKind() == KIND_OUTPUT)
	    {
	      return "output"; //$NON-NLS-1$
	    }
	    else if (getKind() == KIND_FAULT)
	    {
	      return getName();
	    }
	    return ""; //$NON-NLS-1$
	}
    
	/*
	 * @deprecated.  This method will be removed in the near future.
	 * Use getSimplifiedParameters().
	 */
	public List getParameters2()
	{
		return getSimplifiedDiagnosticMessages();
	}
	
	public List getSimplifiedParameters()
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

	private void processAdvancedW11MessageReference() {
		diagnosticMessages = new ArrayList();

		MessageReference messageRef = (MessageReference) getTarget();
		List parts = new ArrayList();
		if (messageRef.getEMessage() != null) {
			if (messageRef.getEMessage().getEParts() != null) {
				parts = messageRef.getEMessage().getEParts();
			}
		}

		// messageRef can not be null, given above
		if (messageRef.getEMessage() == null) {
			addErrorDiagnosticMessage(getUndefinedArg1String("message")); //$NON-NLS-1$
		}
		else if (parts.size() <= 0) {
			String[] args = new String[1];
			args[0] = "part"; //$NON-NLS-1$
			addWarningDiagnosticMessage(getStringForKey("_UI_LABEL_NO_OBJECT_SPECIFIED_ARG1", args)); //$NON-NLS-1$
		}
	}

	private void processSimplifiedW11MessageReference() {
		diagnosticMessages = new ArrayList();
		
		MessageReference messageRef = (MessageReference) getTarget();
		  if (messageRef == null || messageRef.getEMessage() == null) {
			  addErrorDiagnosticMessage(getUndefinedArg1String("message")); //$NON-NLS-1$
		  }
		  else if (messageRef.getEMessage().getEParts().size() <= 0) {
			  addWarningDiagnosticMessage(getNoParametersSpecifiedString());
		  }
		  else {
			  Part part = (Part) messageRef.getEMessage().getEParts().get(0);
			  XSDElementDeclaration xsdElement = part.getElementDeclaration();
			  if (xsdElement == null || xsdElement.getSchema() == null) {
				  // No XSD Element
				  addErrorDiagnosticMessage(getUndefinedArg1String("element")); //$NON-NLS-1$
			  }
			  else {
				  MyInnerElementVisitor visitor = new MyInnerElementVisitor();
				  visitor.findErrorsAndWarnings(xsdElement);
				  diagnosticMessages.addAll(visitor.getDiagnosticMessages());
			  }
		  }
	}

	private void addErrorDiagnosticMessage(String txt) {
		diagnosticMessages.add(new ModelDiagnosticInfo(txt, ModelDiagnosticInfo.ERROR_TYPE, null));
	}
	
	private void addWarningDiagnosticMessage(String txt) {
		diagnosticMessages.add(new ModelDiagnosticInfo(txt, ModelDiagnosticInfo.WARNING_TYPE, null));		
	}
	
	protected List diagnosticMessages = new ArrayList();

	public List getDiagnosticMessages() {
		processAdvancedW11MessageReference();
		return diagnosticMessages;
	}

	public List getSimplifiedDiagnosticMessages() {
		processSimplifiedW11MessageReference();
		return diagnosticMessages;
	}
	
	private class MyInnerElementVisitor extends W11FindInnerElementVisitor {
		private List diagMessages = new ArrayList();
		
		public void findErrorsAndWarnings(XSDElementDeclaration xsdElement) {
			if (xsdElement.getTypeDefinition() == null || xsdElement.getTypeDefinition().getSchema() == null) {
				// No XSD type (non anonymous) defined
				diagMessages.add(new ModelDiagnosticInfo(getUndefinedArg1String("type"), ModelDiagnosticInfo.ERROR_TYPE, null)); //$NON-NLS-1$
			}

			XSDElementDeclaration innerElement = super.getInnerXSDElement(xsdElement);
			if (innerElement.equals(xsdElement)) {
				diagMessages.add(new ModelDiagnosticInfo(getNoParametersSpecifiedString(), ModelDiagnosticInfo.WARNING_TYPE, null));
			}
		}
		
		public List getDiagnosticMessages() {
			return diagMessages;
		}
	}
	
	  private String getUndefinedArg1String(String arg) {
		  String[] args = new String[1];
		  args[0] = arg;
		  String newString = getStringForKey("_UI_LABEL_UNDEFINED_ARG1", args); //$NON-NLS-1$
		  return newString;
	  }
	  
	  private String getNoParametersSpecifiedString() {
		  String string = null;
		  String[] args = new String[0];
		  string = getStringForKey("_UI_LABEL_NO_PARAMETERS_SPECIFIED", args); //$NON-NLS-1$
		  return string;
	  }
	  
	  private String getStringForKey(String key, Object[] args) {
		  String newString = ""; //$NON-NLS-1$
		  newString = Messages.getString(key, args);
		  
		  Object object = WSDLEditorPlugin.getInstance().getProductCustomizationProvider();
		  if (object instanceof ProductCustomizationProvider) {
			  ProductCustomizationProvider productCustomizationProvider = (ProductCustomizationProvider)object;
			  String customizedString = ""; //$NON-NLS-1$
			  if (args == null) {
				  customizedString = productCustomizationProvider.getProductString(key);
			  }
			  else {
				  customizedString = productCustomizationProvider.getProductString(key, args);
			  }
			  
			  if (customizedString != null && !customizedString.equals("")) { //$NON-NLS-1$
				  newString = customizedString;
			  }
		  }

		  if (newString == null) {
			  newString = ""; //$NON-NLS-1$
		  }

		  return newString;
	  }
  }
