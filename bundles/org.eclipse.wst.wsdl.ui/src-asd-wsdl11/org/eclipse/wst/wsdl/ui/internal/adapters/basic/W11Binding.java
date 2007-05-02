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

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.wst.wsdl.Binding;
import org.eclipse.wst.wsdl.BindingFault;
import org.eclipse.wst.wsdl.BindingOperation;
import org.eclipse.wst.wsdl.ExtensibilityElement;
import org.eclipse.wst.wsdl.Fault;
import org.eclipse.wst.wsdl.Operation;
import org.eclipse.wst.wsdl.PortType;
import org.eclipse.wst.wsdl.binding.http.HTTPBinding;
import org.eclipse.wst.wsdl.binding.soap.SOAPBinding;
import org.eclipse.wst.wsdl.ui.internal.Messages;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.actions.OpenInNewEditor;
import org.eclipse.wst.wsdl.ui.internal.adapters.WSDLBaseAdapter;
import org.eclipse.wst.wsdl.ui.internal.adapters.commands.W11GenerateBindingCommand;
import org.eclipse.wst.wsdl.ui.internal.adapters.commands.W11SetInterfaceCommand;
import org.eclipse.wst.wsdl.ui.internal.asd.actions.ASDDeleteAction;
import org.eclipse.wst.wsdl.ui.internal.asd.actions.ASDGenerateBindingAction;
import org.eclipse.wst.wsdl.ui.internal.asd.actions.ASDSetExistingInterfaceAction;
import org.eclipse.wst.wsdl.ui.internal.asd.actions.ASDSetNewInterfaceAction;
import org.eclipse.wst.wsdl.ui.internal.asd.actions.BaseSelectionAction;
import org.eclipse.wst.wsdl.ui.internal.asd.design.editparts.model.BindingContentPlaceHolder;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IBinding;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IDescription;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IInterface;
import org.eclipse.wst.wsdl.ui.internal.asd.outline.ITreeElement;
import org.eclipse.wst.wsdl.ui.internal.util.WSDLAdapterFactoryHelper;
import org.eclipse.wst.xsd.ui.internal.common.properties.sections.appinfo.custom.NodeCustomizationRegistry;
import org.eclipse.wst.xsd.ui.internal.editor.XSDEditorPlugin;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class W11Binding extends WSDLBaseAdapter implements IBinding {

	public IInterface getInterface() {
        PortType portType = ((Binding) target).getEPortType();
        if (portType != null)
        {  
	      return (IInterface) createAdapter(portType);
		}		
		return null;
	}
    
    public List getBindingContentList()
    {
      List adapterList = new ArrayList();
      List list = new ArrayList();

      List bindingOperations = copyList(((Binding)target).getEBindingOperations()); 
      List operations = copyList(((Binding)target).getEPortType().getEOperations());
      
      // Determine if we need placeholders for Operations
	  List toAdaptList = new ArrayList();
      
	  Iterator bindingOpIt = bindingOperations.iterator();
	  while (bindingOpIt.hasNext()) {
		  BindingOperation item = (BindingOperation) bindingOpIt.next();
		  operations.remove(item.getEOperation());
	  }
	  
      Iterator operationsIt = operations.iterator();
      while (operationsIt.hasNext()) {
    	  Operation op = (Operation) operationsIt.next();
    	  toAdaptList.add(op);
    	  toAdaptList.add(op.getEInput());
    	  toAdaptList.add(op.getEOutput());
    	  toAdaptList.addAll(op.getEFaults());
      }

      Iterator it = toAdaptList.iterator();
      while (it.hasNext()) {
          Object o = it.next(); 
          if (o != null)
          {  
    	    Object adapted = WSDLAdapterFactoryHelper.getInstance().adapt((Notifier)o);
    	    BindingContentPlaceHolder placeHolder = new BindingContentPlaceHolder(adapted);
    	    adapterList.add(placeHolder);
          }  
      }
      
      for (Iterator i = ((Binding)target).getEBindingOperations().iterator(); i.hasNext(); )
      {
        BindingOperation bindingOperation = (BindingOperation)i.next();
        Operation operation = bindingOperation.getEOperation();
        
        list.add(bindingOperation);
        
        // Handle Input
        if (bindingOperation.getEBindingInput() != null)
        {
          list.add(bindingOperation.getEBindingInput());
        }
        else if (bindingOperation.getEBindingInput() == null && operation.getEInput() != null) {
        	// Need a placeholder
        	Object adaptedObject = createAdapter(operation.getEInput());
        	BindingContentPlaceHolder temp = new BindingContentPlaceHolder(adaptedObject);
        	adapterList.add(temp);
        }
        
        // Handle Output
        if (bindingOperation.getEBindingOutput() != null)
        {
          list.add(bindingOperation.getEBindingOutput());
        }
        else if (bindingOperation.getEBindingOutput() == null && operation.getEOutput() != null) {
        	// Need a placeholder
        	Object adaptedObject = createAdapter(operation.getEOutput());
        	BindingContentPlaceHolder temp = new BindingContentPlaceHolder(adaptedObject);
        	adapterList.add(temp);
        }

        // Handle Faults
        if (operation != null)
        {  
        List faults = copyList(operation.getEFaults());
        List bindingFaults = copyList(bindingOperation.getEBindingFaults());
        for (int index = 0; index < bindingFaults.size(); index++) {
        	BindingFault bindingFault = (BindingFault) bindingFaults.get(index);
        	list.add(bindingFault);
        	
        	faults.remove(bindingFault.getEFault());        	
        }
        
        // Left over faults need placeholders
        Iterator remaningFaults = faults.iterator();
        while (remaningFaults.hasNext()) {
        	Object adaptedObject = createAdapter((Fault) remaningFaults.next());
        	BindingContentPlaceHolder temp = new BindingContentPlaceHolder(adaptedObject);
        	adapterList.add(temp);
        }
      }
      }

      populateAdapterList(list, adapterList);
      return adapterList;
    }
    
    private List copyList(List origList) {
    	List newList = new ArrayList();
    	Iterator it = origList.iterator();
    	while (it.hasNext()) {
    		newList.add(it.next());
    	}
    	
    	return newList;
    }

	public List getExtensiblityObjects() {

		return null;
	}

	public String getName() {
		return ((Binding) target).getQName().getLocalPart();
	}
	
	public String getProtocol() {
		Iterator it = ((Binding) target).getEExtensibilityElements().iterator();
		while (it.hasNext()) {
			Object item = it.next();
			if (item instanceof SOAPBinding) {
				return "SOAP"; //$NON-NLS-1$
			}
			else if (item instanceof HTTPBinding) {
				return "HTTP"; //$NON-NLS-1$
			}
		}
		
		return ""; //$NON-NLS-1$
	}
	
	public IDescription getOwnerDescription() {
		return (IDescription) owner;
	}
	
	public String[] getActions(Object object) {
    List actionIDs = new ArrayList();
    actionIDs.add(ASDGenerateBindingAction.ID);
    actionIDs.add(BaseSelectionAction.SUBMENU_START_ID + Messages._UI_ACTION_SET_PORTTYPE); //$NON-NLS-1$
    actionIDs.add(ASDSetNewInterfaceAction.ID);
    actionIDs.add(ASDSetExistingInterfaceAction.ID);
    actionIDs.add(BaseSelectionAction.SUBMENU_END_ID);
    actionIDs.add(ASDDeleteAction.ID);
    if (isReadOnly()) {
      actionIDs.add(OpenInNewEditor.ID);
    }
    return (String [])actionIDs.toArray(new String[0]);
	}
	
	public Command getSetInterfaceCommand(IInterface newInterface) {
		W11Interface w11Interface = (W11Interface) newInterface;
		return new W11SetInterfaceCommand((Binding) target, (PortType) w11Interface.getTarget());
	}
	
	public Command getGenerateBindingCommand() {
		return new W11GenerateBindingCommand((Binding) this.getTarget());
	}
    
    private ILabelProvider getLabelProvider(Node node)
    {
      String namespace = node.getNamespaceURI();      
      if (namespace != null)
      {  
        NodeCustomizationRegistry registry = XSDEditorPlugin.getDefault().getNodeCustomizationRegistry();      
        return registry.getLabelProvider(namespace);
      }        
      return null;
    }
    
  public Image getImage()
  {
    Image image = null;
    List list = ((Binding) target).getExtensibilityElements();
    if (list.size() > 0)
    {  
       ExtensibilityElement ee = (ExtensibilityElement)list.get(0);
       Element element = ee.getElement();
       if (element != null)
       {  
         ILabelProvider labelProvider = getLabelProvider(element);
         if (labelProvider != null)
         {
           image= labelProvider.getImage(element);
         }
       }
    }        
    if (image == null)
    {
      image = WSDLEditorPlugin.getInstance().getImage("icons/binding_obj.gif");          //$NON-NLS-1$
    }  
    return image;
   }

  public String getText()
  {
    return "binding"; //$NON-NLS-1$
  }
  
  public ITreeElement[] getChildren()
  {
    List list = getBindingOperations();
    ITreeElement[] result = new ITreeElement[list.size()];
    list.toArray(result);
    return result;
  }
  
  public boolean hasChildren() {
	  if (getBindingOperations().size() > 0) {
		  return true;
	  }
	  
	  return false;
  }
  
  public ITreeElement getParent() {
	  return null;
  }
  
  public List getBindingOperations()
  {
    List list = new ArrayList();
    populateAdapterList(((Binding)target).getEBindingOperations(), list);
    return list;
  }
  
  public List getBindingOperations(boolean createMissingOperations, boolean showExtraOperations)
  {
    return getBindingOperations();
  }
}
