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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.gef.EditPartViewer;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.wst.wsdl.Binding;
import org.eclipse.wst.wsdl.BindingOperation;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.Fault;
import org.eclipse.wst.wsdl.Import;
import org.eclipse.wst.wsdl.Input;
import org.eclipse.wst.wsdl.Message;
import org.eclipse.wst.wsdl.Operation;
import org.eclipse.wst.wsdl.Output;
import org.eclipse.wst.wsdl.Part;
import org.eclipse.wst.wsdl.Port;
import org.eclipse.wst.wsdl.PortType;
import org.eclipse.wst.wsdl.Service;
import org.eclipse.wst.wsdl.WSDLElement;
import org.eclipse.wst.wsdl.internal.impl.WSDLElementImpl;
import org.eclipse.wst.wsdl.util.WSDLConstants;
import org.eclipse.wst.wsdl.util.WSDLSwitch;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditor;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.commands.AddInputCommand;
import org.eclipse.wst.wsdl.ui.internal.commands.AddMessageCommand;
import org.eclipse.wst.wsdl.ui.internal.commands.AddOperationCommand;
import org.eclipse.wst.wsdl.ui.internal.commands.AddOutputCommand;
import org.eclipse.wst.wsdl.ui.internal.extension.IMenuActionContributor;
import org.eclipse.wst.wsdl.ui.internal.graph.editparts.WSDLTreeNodeEditPart;
import org.eclipse.wst.wsdl.ui.internal.model.WSDLGroupObject;
import org.eclipse.wst.wsdl.ui.internal.util.ComponentReferenceUtil;
import org.eclipse.wst.wsdl.ui.internal.util.CreateWSDLElementHelper;
import org.eclipse.wst.wsdl.ui.internal.util.NameUtil;
import org.eclipse.wst.wsdl.ui.internal.util.WSDLEditorUtil;
import org.eclipse.wst.wsdl.ui.internal.widgets.NewComponentDialog;
import org.eclipse.wst.wsdl.ui.internal.widgets.NewComponentWithChildDialog;
import org.eclipse.wst.wsdl.ui.internal.wizards.BindingWizard;
import org.eclipse.wst.wsdl.ui.internal.wizards.PortWizard;
import org.eclipse.wst.xml.core.document.IDOMNode;
import org.eclipse.wst.xsd.ui.internal.XSDEditorPlugin;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class WSDLMenuActionContributor implements IMenuActionContributor
{
  protected AddEEMenuActionContributor addEEMenuActionContributor;
  IEditorPart editorPart;

  public WSDLMenuActionContributor(IEditorPart editorPart)
  {
    addEEMenuActionContributor = new AddEEMenuActionContributor();
    this.editorPart = editorPart;
  }

  protected static List createList(Action action)
  {
    List list = new ArrayList();
    list.add(action);
    return list;
  }
  
	private class OpenSchemaOnSelectionHelper extends Action {
		Object object;
		
		public OpenSchemaOnSelectionHelper(Object object) {
			setText(WSDLEditorPlugin.getWSDLString("_UI_OPEN_IMPORT"));
			this.object = object;
			
			if (object instanceof Import) {
				String location = ((Import) object).getLocationURI();
				
				if (location == null || location.trim().equals(""))
					setEnabled(false);
			}
		}
		
		public void run() {
			Definition definition = ((WSDLElement) object).getEnclosingDefinition();
			org.eclipse.wst.wsdl.ui.internal.util.OpenOnSelectionHelper helper = new org.eclipse.wst.wsdl.ui.internal.util.OpenOnSelectionHelper(definition);
		    helper.openEditor((org.eclipse.emf.ecore.EObject) object);
		}
	}

  public void contributeMenuActions(final IMenuManager menu, final Node node, Object object)
  {
    final IMenuManager addMenu;
    boolean addEditAction = false;
    final boolean isEditable = (node instanceof IDOMNode);
    if (object instanceof WSDLElement && node != null && isEditable)
    {
      addEditAction = true;
      addMenu = new MenuManager(WSDLEditorPlugin.getWSDLString("_UI_ACTION_ADD_CHILD"), "addchild"); //$NON-NLS-1$ //$NON-NLS-2$
      menu.add(addMenu);
      Definition definition = ((WSDLElement) object).getEnclosingDefinition();

      final String prefix = definition.getPrefix(WSDLConstants.WSDL_NAMESPACE_URI);

      if (object instanceof Import) {
      	menu.add(new OpenSchemaOnSelectionHelper(object));
      }
      
      WSDLSwitch wsdlSwitch = new WSDLSwitch()
      {
        public Object caseBinding(Binding binding)
        {
          if (isEditable)
          {
            menu.add(new AddBindingOperationAction(node, "NewBindingOperation", prefix)); //$NON-NLS-1$
            menu.add(new Separator());
          }
          
          menu.add(createSetPortTypeMenu(binding, isEditable));
          menu.add(new GenerateBindingContentAction(binding, isEditable));
          return null;
        }

        public Object caseBindingOperation(BindingOperation bindingOperation)
        {
          if (isEditable)
          {
            menu.add(new AddBindingInputAction(editorPart, node, prefix));
            menu.add(new AddBindingOutputAction(editorPart, node, prefix));
            menu.add(new AddBindingFaultAction(node, prefix));
          }
          return null;
        }

        public Object caseDefinition(Definition definition)
        {
           menu.add(new EditNamespacesAction(definition));
           menu.add(new Separator());
        	
          if (isEditable)
          {
            menu.add(new AddMessageAction(definition, null, node, prefix, ((WSDLEditor) editorPart).getXMLDocument()));
            menu.add(new AddServiceAction(definition, null, node, prefix, ((WSDLEditor) editorPart).getXMLDocument()));
            menu.add(new AddPortTypeAction(definition, null, node, prefix, ((WSDLEditor) editorPart).getXMLDocument()));
            menu.add(new AddBindingAction(definition, ((WSDLEditor) editorPart).getXMLDocument()));//, null, node, prefix));
            menu.add(new AddImportAction(editorPart, definition, node, prefix));
          }
          return null;
        }

        public Object caseFault(Fault fault)
        {
          menu.add(createSetMessageMenu(fault, isEditable));
          //menu.add(createAddBindingMenuForIOF(fault));
          return null;
        }

        public Object caseInput(Input input)
        {
          menu.add(createSetMessageMenu(input, isEditable));
          //menu.add(createAddBindingMenuForIOF(input));
          return null;
        }

        public Object caseOutput(Output output)
        {
          menu.add(createSetMessageMenu(output, isEditable));
          //menu.add(createAddBindingMenuForIOF(output));
          return null;
        }

        public Object caseMessage(Message message)
        {
          if (isEditable)
          {
            menu.add(new AddPartAction(editorPart, message, node, prefix));
          }
          return null;
        }

        public Object caseOperation(Operation operation)
        {
          if (isEditable)
          {
            menu.add(new AddInputAction(editorPart, node, operation, prefix));
            menu.add(new AddOutputAction(editorPart, node, operation, prefix));
            menu.add(new AddFaultAction(editorPart, node, operation, prefix));
          }
          //menu.add(createAddBindingMenuForOperation(operation));
          return null;
        }

        public Object casePart(Part part)
        {
          menu.add(createSetPartMenu(part, (Element) node, "element", isEditable)); //$NON-NLS-1$
          menu.add(createSetPartMenu(part, (Element) node, "type", isEditable)); //$NON-NLS-1$
          return null;
        }

        public Object casePort(Port port)
        {
          if (node instanceof Element)
          {
            menu.add(createSetBindingMenu(port, (Element) node, isEditable));
            if (port.getEBinding() != null)
            {
              menu.add(createSetPortTypeMenu(port.getEBinding(), isEditable));
            }
          }
          return null;
        }

        public Object casePortType(PortType portType)
        {
          if (isEditable)
          {
            menu.add(new AddOperationAction(editorPart, portType, node, prefix));
          }
          return null;
        }

        public Object caseService(Service service)
        {
          if (isEditable)
          {
            menu.add(new AddPortAction(service));
          }
          return null;
        }

        //public Object caseTypes(Types types)
        //{   
        //  return createTypesAdapter();
        // } 
      };
      wsdlSwitch.doSwitch((WSDLElement) object);
    }
    else if (object instanceof WSDLGroupObject)
    {
      addMenu = new MenuManager(WSDLEditorPlugin.getWSDLString("_UI_ACTION_ADD_CHILD"), "addchild"); //$NON-NLS-1$ //$NON-NLS-2$
      menu.add(addMenu);
      
      WSDLGroupObject groupObject = (WSDLGroupObject) object;
      Definition definition = groupObject.getDefinition();

      final String prefix = definition.getPrefix(WSDLConstants.WSDL_NAMESPACE_URI);

      //for (Iterator i = actionList.iterator(); i.hasNext(); )
      //{                           
      //  Action action = (Action)i.next();
      //  manager.add(action); 
      //}

      switch (groupObject.getType())
      {
        case WSDLGroupObject.MESSAGES_GROUP :
          {
            menu.add(new AddMessageAction(groupObject.getDefinition(), null, node, prefix, ((WSDLEditor) editorPart).getXMLDocument()));
            break;
          }
        case WSDLGroupObject.SERVICES_GROUP :
          {
            menu.add(new AddServiceAction(groupObject.getDefinition(), null, node, prefix, ((WSDLEditor) editorPart).getXMLDocument()));
            break;
          }
        case WSDLGroupObject.PORT_TYPES_GROUP :
          {
            menu.add(new AddPortTypeAction(groupObject.getDefinition(), null, node, prefix, ((WSDLEditor) editorPart).getXMLDocument()));
            break;
          }
        case WSDLGroupObject.BINDINGS_GROUP :
          {
            menu.add(new AddBindingAction(groupObject.getDefinition(), ((WSDLEditor) editorPart).getXMLDocument()));//, null, node, prefix));
            break;
          }
        case WSDLGroupObject.IMPORTS_GROUP :
          {
            menu.add(new AddImportAction(editorPart, groupObject.getDefinition(), node, prefix));
            break;
          }
      }
    }

    menu.add(new Separator());
    
   	addEEMenuActionContributor.contributeMenuActions(menu, node, object);

    menu.add(new Separator());

    if (object instanceof WSDLElement && node != null && addEditAction && node != null && isEditable)
    {
      //////////////////////////////////////// Currently, only allow Copy and Paste on Operations
    	if (object instanceof Operation) {
    		// Add Cut, Copy Actions
    		// Note:  The Cut, Copy, Paste Actions are Model Driven.  The Delete Action is still
    		// currently DOM driven.
    		//      menu.add(new CutAction((WSDLElement) object, editorPart));
   		 	menu.add(new CopyGlobalAction((WSDLElement) object, editorPart));
    	}

   	 	Action pasteAction = new PasteGlobalAction((WSDLElement) object, editorPart);
   	 	if (pasteAction.isEnabled()) {
   	 		menu.add(pasteAction);
   	 	}
   	 	
	 	menu.add(new Separator());
      //////////////////////////////////////// Currently, only allow Copy and Paste on Operations
		
		// Get the list of selected elements.  Use this list for the DeleteAction.		
		IStructuredSelection selectionList = (IStructuredSelection) ((WSDLEditor) editorPart).getSelectionManager().getSelection();	
		Action deleteAtion = new DeleteWSDLAndXSDAction(selectionList.toList(), node, (WSDLEditor) editorPart);
		menu.add(deleteAtion);
		deleteAtion.setEnabled(isEditable);
    }
    
    // Allow Rename through a dialog mechanism.....  This should be changed when
    // direct editing is ready
    if (object instanceof WSDLElement && isEditable) {
    	RenameDialogAction renameDialog = new RenameDialogAction((WSDLElement) object);
    	if (renameDialog.showRenameDialog())
    		menu.add(renameDialog);
    }
  }

  /*
  protected MenuManager createAddBindingMenuForOperation(Operation operation) 
  {
    MenuManager subMenu = new MenuManager("Add Binding");                    
  
    Definition definition = operation.getEnclosingDefinition(); 
    ComponentReferenceUtil util = new ComponentReferenceUtil(definition);
  
    String operationName = operation.getName();
    if (operationName != null)
    {       
      for (Iterator i = util.getBindings(operation).iterator(); i.hasNext(); )
      {   
        Binding binding = (Binding)i.next();
            
        BindingOperation matchingBindingOperation = null;
  
        for (Iterator j = binding.getBindingOperations().iterator(); j.hasNext(); )
        {
          BindingOperation bindingOperation = (BindingOperation)j.next();
          if (operationName.equals(bindingOperation.getName()))
          { 
            matchingBindingOperation = bindingOperation;
            break;
          }
        }         
  
        if (matchingBindingOperation == null)
        {                                                                            
          Element bindingElement = WSDLUtil.getInstance().getElementForObject(binding);
          subMenu.add(new AddBindingOperationAction(bindingElement, operation, prefix));
        }    
      }
    }
    return subMenu;
  }*/

  /*
  protected MenuManager createAddBindingMenuForIOF(WSDLElement iof) 
  {
    MenuManager subMenu = new MenuManager("Add Binding");                    
  
    Definition definition = iof.getEnclosingDefinition(); 
    Operation operation = (Operation)iof.eContainer();
  
    ComponentReferenceUtil util = new ComponentReferenceUtil(definition);
  
    String operationName = operation.getName();
    if (operationName != null)
    {  
      for (Iterator i = util.getBindings(operation).iterator(); i.hasNext(); )
      {   
        Binding binding = (Binding)i.next();
  
        boolean hasName = false;
        for (Iterator j = binding.getBindingOperations().iterator(); j.hasNext(); )
        {
          BindingOperation bindingOperation = (BindingOperation)j.next();
          if (operationName.equals(bindingOperation.getName()))
          {
            hasName = true;
            break;
          }
        }         
  
        if (!hasName)
        {                                                                            
          // TODO... should we add the operation to all of the bindings?
          //
          Element bindingElement = WSDLUtil.getInstance().getElementForObject(binding);
          subMenu.add(new AddBindingOperationAction(bindingElement, operationName));
        }
      }
    }
    return subMenu;
  }
  */

  protected MenuManager createSetBindingMenu(Port port, Element portElement, boolean isEditable)
  {
    MenuManager submenu = new MenuManager("Set Binding");       // TODO: Externalize String
    
    Definition definition = port.getEnclosingDefinition();
    String prefix = definition.getPrefix(WSDLConstants.WSDL_NAMESPACE_URI);
    Element definitionElement = WSDLEditorUtil.getInstance().getElementForObject(definition);

    // TODO: Externalize String below
    SetNewComponentAction setNewAction = new SetNewComponentAction("New Binding...", definitionElement, prefix, "binding", port);
    setNewAction.setEditor(editorPart);
    setNewAction.setEnabled(true);
    submenu.add(setNewAction);     
    
    // WSDLEditorPlugin.getWSDLString("_UI_ACTION_SET_BINDIG")
    SetExistingComponentAction action = new SetExistingComponentAction("Existing Binding...", definitionElement, prefix, "binding", port);
    action.setEditor(editorPart);
    action.setEnabled(isEditable);
    submenu.add(action);
    
    return submenu;
  }

  protected MenuManager createSetPortTypeMenu(Binding binding, boolean isEditable)
  {      
    MenuManager submenu = new MenuManager("Set PortType");  // TODO: Externalize Strings

    if (binding != null)
    {
      Definition definition = binding.getEnclosingDefinition();
      Element definitionElement = WSDLEditorUtil.getInstance().getElementForObject(definition);
      String prefix = definition.getPrefix(WSDLConstants.WSDL_NAMESPACE_URI);
      
      // TODO: Externalize String below
      SetNewComponentAction setNewAction = new SetNewComponentAction("New Port Type...", definitionElement, prefix, "portType", binding);
      setNewAction.setEditor(editorPart);
      setNewAction.setEnabled(true);
      submenu.add(setNewAction);      

      // WSDLEditorPlugin.getWSDLString("_UI_ACTION_SET_PORTTYPE")
      SetExistingComponentAction action = new SetExistingComponentAction("Existing Port Type...", definitionElement, prefix, "portType", binding);
      action.setEditor(editorPart);
      action.setEnabled(isEditable);      
      submenu.add(action);
    }
    return submenu;
  }

  protected MenuManager createSetMessageMenu(WSDLElement wsdlElement, boolean isEditable)
  {
    Definition definition = wsdlElement.getEnclosingDefinition();
    Element definitionElement = WSDLEditorUtil.getInstance().getElementForObject(definition);
    Element referencingElement = WSDLEditorUtil.getInstance().getElementForObject(wsdlElement);

    MenuManager submenu = new MenuManager("Set Message");       // TODO: Externalize Strings
    if (definitionElement != null && referencingElement != null)
    {
      String prefix = definition.getPrefix(WSDLConstants.WSDL_NAMESPACE_URI);
      
      // TODO: Externalize String below
      SetNewComponentAction setNewAction = new SetNewComponentAction("New Message...", definitionElement, prefix, "message", wsdlElement);
      setNewAction.setEditor(editorPart);
      setNewAction.setEnabled(true);
      submenu.add(setNewAction);  
      
      // WSDLEditorPlugin.getWSDLString("_UI_ACTION_SET_MESSAGE")
      SetExistingComponentAction action = new SetExistingComponentAction("Existing Messgae...", definitionElement, prefix, "message", wsdlElement);
      action.setEditor(editorPart);
      action.setEnabled(isEditable);
      submenu.add(action);
    }
    
    return submenu;
  }

  protected MenuManager createSetPartMenu(Part part, Element portElement, String referenceKind, boolean isEditable)
  {
    Definition definition = part.getEnclosingDefinition();
    String prefix = definition.getPrefix(WSDLConstants.WSDL_NAMESPACE_URI);
    Element definitionElement = WSDLEditorUtil.getInstance().getElementForObject(definition);

    String actionName;
    MenuManager submenu = null;
    if (referenceKind.equalsIgnoreCase("element")) // NON NLS
    {
//      actionName = WSDLEditorPlugin.getWSDLString("_UI_ACTION_SET_ELEMENT");
      actionName = "Existing Element...";
  
      submenu = new MenuManager("Set Element");       // TODO: Externalize Strings
      
      // TODO: Externalize String below
      SetNewComponentAction setNewElementAction = new SetNewComponentAction("New Element...", definitionElement, prefix, "element", part);
      setNewElementAction.setEditor(editorPart);
      setNewElementAction.setEnabled(true);
      setNewElementAction.setReferenceKind(referenceKind);
      submenu.add(setNewElementAction);
    }
    else
    {
//      actionName = WSDLEditorPlugin.getWSDLString("_UI_ACTION_SET_TYPE");
      actionName = "Existing Type...";
          
      submenu = new MenuManager("Set Type");       // TODO: Externalize Strings
      
      // TODO: Externalize String below
      SetNewComponentAction setNewComplexAction = new SetNewComponentAction("New Complex Type...", definitionElement, prefix, "complex type", part);
      setNewComplexAction.setEditor(editorPart);
      setNewComplexAction.setEnabled(true);
      setNewComplexAction.setReferenceKind(referenceKind);
      setNewComplexAction.setTypeKind("complex");
      Image image = XSDEditorPlugin.getXSDImage("icons/XSDComplexType.gif");
      setNewComplexAction.setImageDescriptor(ImageDescriptor.createFromImage(image));
      submenu.add(setNewComplexAction);
      
      // TODO: Externalize String below
      SetNewComponentAction setNewSimpleAction = new SetNewComponentAction("New Simple Type...", definitionElement, prefix, "simple type", part);
      setNewSimpleAction.setEditor(editorPart);
      setNewSimpleAction.setEnabled(true);
      setNewSimpleAction.setReferenceKind(referenceKind);
      setNewSimpleAction.setTypeKind("simple");
      image = XSDEditorPlugin.getXSDImage("icons/XSDSimpleType.gif");
      setNewSimpleAction.setImageDescriptor(ImageDescriptor.createFromImage(image));

      submenu.add(setNewSimpleAction); 
    }

    
    SetExistingComponentAction action = new SetExistingComponentAction(actionName, definitionElement, prefix, actionName, part);
    action.setReferenceKind(referenceKind);
    action.setEditor(editorPart);
    action.setEnabled(isEditable);
    submenu.add(action);
    
    return submenu;
  }
  
  private WSDLElement showPasteAction(WSDLElement element, IEditorPart ePart) {
  	WSDLElement parentElement = null;
  	
  	if (ePart instanceof WSDLEditor) {
  		WSDLElement clipboardElement = ((WSDLEditor) ePart).getClipboardContents();
  		
  		if (element instanceof PortType) {
  			if (clipboardElement instanceof Operation) {
  				parentElement = element;
  			}
  		}
  		if (element instanceof Operation) {
  			if (clipboardElement instanceof Operation) {
  				parentElement = (WSDLElement) ((Operation) element).eContainer();
  			}
  		}
  	}
  	
  	return parentElement;
  }
}

class AddBindingOperationAction extends AddElementAction
{
  protected String name;
  protected Operation operation;

  public AddBindingOperationAction(Node parentNode, String name, String prefix)
  {
    // super(WSDLEditorPlugin.getWSDLString("_UI_ACTION_ADD") + " " + WSDLEditorPlugin.getWSDLString("_UI_LABEL_BINDING_OPERATION"), "icons/operationbinding_obj.gif", parentNode, prefix, "operation");
    super(WSDLEditorPlugin.getWSDLString("_UI_ACTION_ADD_BINDING_OPERATION"), "icons/operationbinding_obj.gif", parentNode, prefix, "operation");
    this.name = name;
  }

  public AddBindingOperationAction(Node parentNode, Operation operation, String prefix)
  {
    this(parentNode, operation.getName(), prefix);
    this.operation = operation;
  }

  protected void addAttributes(Element newElement)
  {
    newElement.setAttribute("name", name);
  }

  protected void performAddElement()
  {
    super.performAddElement();

    if (operation != null)
    {
      if (operation.getInput() != null)
      {
        new AddBindingInputAction(editorPart, newElement, prefix).run();
      }
      if (operation.getOutput() != null)
      {
        new AddBindingOutputAction(editorPart, newElement, prefix).run();
      }
      for (Iterator i = operation.getEFaults().iterator(); i.hasNext();)
      {
        Fault fault = (Fault) i.next();
        new AddBindingFaultAction(newElement, fault, prefix).run();
      }
      format(operation.getElement());
    }
  }
}


class AddOperationAction extends AddElementAction
{
  protected String name = "NewOperation";
  protected PortType portType;
  protected boolean createSubComponents = false;

  public AddOperationAction(IEditorPart editorPart, PortType portType, Node parentNode, String prefix)
  {
    super(WSDLEditorPlugin.getWSDLString("_UI_ACTION_ADD_OPERATION"), "icons/operation_obj.gif", parentNode, prefix, "operation");
    setEditorPart(editorPart);
    setDefinition(portType.getEnclosingDefinition());
    this.portType = portType;
  }

  public boolean showDialog()
  {
    name = NameUtil.buildUniqueOperationName(portType);
    name = showDialogHelper(WSDLEditorPlugin.getWSDLString("_UI_ACTION_NEW_OPERATION"), name, NameUtil.getUsedOperationNames(portType));
    return name != null;
  }

//  protected void addAttributes(Element newElement)
//  {
//    newElement.setAttribute("name", name);
//  }

  protected void performAddElement()
  {
  	if (createSubComponents) {
  	  	CreateWSDLElementHelper.operationName = name;
  	  	CreateWSDLElementHelper.PART_TYPE_OR_DEFINITION = CreateWSDLElementHelper.getPartInfo(portType);
  		Operation operation = CreateWSDLElementHelper.createOperation(portType);
  		format(operation.getElement());  		
  		selectObject(operation);
  		expandEditParts(operation,true);
  	}
  	else {
  		Definition def = portType.getEnclosingDefinition();	
		
  		// Add Operation
  		AddOperationCommand opAction = new AddOperationCommand(portType, name);
		opAction.run();
		Operation operation = (Operation) opAction.getWSDLElement();
		
		// Add Output
  		AddOutputCommand outAction = new AddOutputCommand(operation, NameUtil.buildUniqueInputName(portType, operation.getName(), ""));
  		outAction.run();
  		
		// Add Input
		AddInputCommand inAction = new AddInputCommand(operation, NameUtil.buildUniqueOutputName(portType, operation.getName(), ""));
  		inAction.run();

  		format(operation.getElement());

 	  	selectObject(operation);
 	  	expandEditParts(operation, true);
  	}
  }

	private void expandEditParts(Object element, boolean expandChildren) {
	    if (element != null) {
	    	EditPartViewer editPartViewer = ((WSDLEditor) editorPart).getGraphViewer().getComponentViewer();
	    	Map editPartMap = editPartViewer.getEditPartRegistry();
	    	WSDLTreeNodeEditPart wsdlEditPart = (WSDLTreeNodeEditPart) editPartMap.get(element);
			wsdlEditPart.setExpanded(true);
			
			if (expandChildren) {
				Iterator iterator = WSDLEditorUtil.getModelGraphViewChildren(element).iterator();
				
				while (iterator.hasNext()) {
					expandEditParts(iterator.next(), expandChildren);
				}
			}
	    }
	}

  public String showDialogHelper(String title, String defaultName, List usedNames)
  {   
    String result = defaultName; 
    NewComponentWithChildDialog dialog = new NewComponentWithChildDialog(WSDLEditorPlugin.getShell(), title, defaultName, usedNames);
    int rc = dialog.createAndOpen();
    if (rc == IDialogConstants.OK_ID)
    {
      result = dialog.getName();  
      createSubComponents = dialog.createSubComponents();
    }
    else
    {
      result = null;
    }               
    return result;
  }
}

abstract class AddIOFAction extends AddElementAction
{
  protected Operation operation;

  public AddIOFAction(IEditorPart editorPart, String text, String imageDescriptorKey, Node parentNode, String prefix, String localName, Operation operation)
  {
    super(text, imageDescriptorKey, parentNode, prefix, localName);
    this.operation = operation;
    setEditorPart(editorPart);
    setDefinition(operation.getEnclosingDefinition());
  }

  abstract protected void performAddElementToBindingOperation(BindingOperation bindingOperation, Element bindingOperationElement);

  /*
  protected void addAttributes(Element newElement)
  {
    newElement.setAttribute("message", "");
  }
  */

  protected void performAddElement()
  {
    if (operation != null)
    {
      Definition definition = operation.getEnclosingDefinition();
      ComponentReferenceUtil util = new ComponentReferenceUtil(definition);

      for (Iterator i = util.getBindingOperations(operation).iterator(); i.hasNext();)
      {
        BindingOperation bindingOperation = (BindingOperation) i.next();
        Element bindingOperationElement = WSDLEditorUtil.getInstance().getElementForObject(bindingOperation);
        if (bindingOperationElement != null)
        {
          performAddElementToBindingOperation(bindingOperation, bindingOperationElement);
        }
      }
      format(operation.getElement());
    }
  }
}

class AddInputAction extends AddIOFAction
{
  public AddInputAction(IEditorPart editorPart, Node parentNode, Operation operation, String prefix)
  {
    super(editorPart, WSDLEditorPlugin.getWSDLString("_UI_ACTION_ADD_INPUT"), "icons/input_obj.gif", parentNode, prefix, "input", operation);
  }

  protected void performAddElementToBindingOperation(BindingOperation bindingOperation, Element bindingOperationElement)
  {
    if (bindingOperation.getBindingInput() == null)
    {
      new AddBindingInputAction(editorPart, bindingOperationElement, prefix).run();
    }
  }
  
  protected void performAddElement() {
  	PortType portType = (PortType) ((WSDLElementImpl) operation).getContainer();
  	CreateWSDLElementHelper.PART_TYPE_OR_DEFINITION = CreateWSDLElementHelper.getPartInfo(portType);
  	Input input = CreateWSDLElementHelper.createInput(portType, operation, null); 
  	format(input.getElement());
  	selectObject(input);
  }
}

class AddOutputAction extends AddIOFAction
{
  public AddOutputAction(IEditorPart editorPart, Node parentNode, Operation operation, String prefix)
  {
    super(editorPart, WSDLEditorPlugin.getWSDLString("_UI_ACTION_ADD_OUTPUT"), "icons/output_obj.gif", parentNode, prefix, "output", operation);
  }

  protected void performAddElementToBindingOperation(BindingOperation bindingOperation, Element bindingOperationElement)
  {
    if (bindingOperation.getBindingOutput() == null)
    {
      new AddBindingOutputAction(editorPart, bindingOperationElement, prefix).run();
    }
  }
  
  protected void performAddElement() {
  	// Determine Part info
  	PortType portType = (PortType) ((WSDLElementImpl) operation).getContainer();
  	CreateWSDLElementHelper.PART_TYPE_OR_DEFINITION = CreateWSDLElementHelper.getPartInfo(portType);
  	Output output = CreateWSDLElementHelper.createOutput(portType, operation); 
  	format(output.getElement());
  	selectObject(output);
  }
}

class AddFaultAction extends AddIOFAction
{
  protected String name = "NewFault";
  public AddFaultAction(IEditorPart editorPart, Node parentNode, Operation operation, String prefix)
  {
    super(editorPart, WSDLEditorPlugin.getWSDLString("_UI_ACTION_ADD_FAULT"), "icons/fault_obj.gif", parentNode, prefix, "fault", operation);
    name = NameUtil.buildUniqueFaultName(operation);
  }

  public boolean showDialog()
  {
    name = NameUtil.buildUniqueFaultName(operation);
    name = showDialogHelper(WSDLEditorPlugin.getWSDLString("_UI_ACTION_NEW_FAULT"), name, NameUtil.getUsedFaultNames(operation));
    return name != null;
  }

  /*
  protected void addAttributes(Element newElement)
  {
    newElement.setAttribute("message", "");
    newElement.setAttribute("name", name);
  }
  */

  protected void performAddElementToBindingOperation(BindingOperation bindingOperation, Element bindingOperationElement)
  {
    if (bindingOperation.getBindingFault(name) == null)
    {
      new AddBindingFaultAction(bindingOperationElement, prefix).run();
    }
  }
  
  protected void performAddElement() {
  	CreateWSDLElementHelper.faultName = name;
  	CreateWSDLElementHelper.PART_TYPE_OR_DEFINITION = CreateWSDLElementHelper.getPartInfo(operation);
  	Fault fault = CreateWSDLElementHelper.createFault(operation);
  	format(fault.getElement());  	
  	selectObject(fault);
  }
}

class AddBindingInputAction extends AddElementAction
{
  public AddBindingInputAction(IEditorPart editorPart, Node parentNode, String prefix)
  {
    super(WSDLEditorPlugin.getWSDLString("_UI_ACTION_ADD_INPUT"), "icons/input_obj.gif", parentNode, prefix, "input");
    setEditorPart(editorPart);
  }
}

class AddBindingOutputAction extends AddElementAction
{
  public AddBindingOutputAction(IEditorPart editorPart, Node parentNode, String prefix)
  {
    super(WSDLEditorPlugin.getWSDLString("_UI_ACTION_ADD_OUTPUT"), "icons/output_obj.gif", parentNode, prefix, "output");
    setEditorPart(editorPart);
  }
}

class AddBindingFaultAction extends AddElementAction
{
  protected Fault fault;

  public AddBindingFaultAction(Node parentNode, String prefix)
  {
    super(WSDLEditorPlugin.getWSDLString("_UI_ACTION_ADD_FAULT"), "icons/fault_obj.gif", parentNode, prefix, "fault");
  }

  public AddBindingFaultAction(Node parentNode, Fault fault, String prefix)
  {
    this(parentNode, prefix);
    this.fault = fault;
  }

  protected void addAttributes(Element newElement)
  {
    if (fault == null)
    {
      newElement.setAttribute("name", "NewFault");
    }
    else
    {
      newElement.setAttribute("name", fault.getName());
    }
  }
}

class AddMessageAction extends AddElementAction
{
  protected Definition definition;
  protected String name;

  public AddMessageAction(Definition definition, String name, Node parentNode, String prefix)
  {
    super(WSDLEditorPlugin.getWSDLString("_UI_ACTION_ADD_MESSAGE"), "icons/message_obj.gif", parentNode, prefix, "message");
    this.definition = definition;
    this.name = name;
    setComputeTopLevelRefChild(true);
  }

  public AddMessageAction(Definition definition, String name, Node parentNode, String prefix, Document document)
  {
    super(WSDLEditorPlugin.getWSDLString("_UI_ACTION_ADD_MESSAGE"), "icons/message_obj.gif", parentNode, prefix, "message");
    this.definition = definition;
    this.name = name;
    this.setDefinition(definition);
    this.document = document;
    setComputeTopLevelRefChild(true);
  }
  
  public boolean showDialog()
  {
    name = NameUtil.buildUniqueMessageName(definition, name);
    name = showDialogHelper(WSDLEditorPlugin.getWSDLString("_UI_ACTION_NEW_MESSAGE"), name, NameUtil.getUsedMessageNames(definition));
    return name != null;
  }

//  protected void addAttributes(Element newElement)
//  {
//    newElement.setAttribute("name", name);
//  }
  
  protected void performAddElement() {
  	NodeList list = document.getChildNodes();
  	if (parentNode == null|| list.getLength() == 0) {
  		createDefinitionStub();
  	}
  	
  	AddMessageCommand action = new AddMessageCommand(definition, name);
	action.run();
	Message message = (Message) action.getWSDLElement();
	CreateWSDLElementHelper.createPart(message);
	format(message.getElement());
	selectObject(message);
  }

  public String getName()
  {
    return name;
  }
}

class AddPartAction extends AddElementAction
{
  protected Message message;
  protected String name;

  public AddPartAction(IEditorPart editorPart, Message message, Node parentNode, String prefix)
  {
    super(WSDLEditorPlugin.getWSDLString("_UI_ACTION_ADD_PART"), "icons/part_obj.gif", parentNode, prefix, "part");
    this.message = message;
    setEditorPart(editorPart);
    setDefinition(message.getEnclosingDefinition());
  }

  public boolean showDialog()
  {
    name = NameUtil.buildUniquePartName(message, message.getQName().getLocalPart());
    name = showDialogHelper(WSDLEditorPlugin.getWSDLString("_UI_ACTION_NEW_PART"), name, NameUtil.getUsedPartNames(message));
    return name != null;
  }

  protected void addAttributes(Element newElement)
  {
    newElement.setAttribute("name", name);
    String xsdPrefix = message.getEnclosingDefinition().getPrefix(WSDLConstants.XSD_NAMESPACE_URI);
    if (xsdPrefix == null)
    {
    	Element definitionElement = WSDLEditorUtil.getInstance().getElementForObject(message.getEnclosingDefinition());    	
        new AddNamespaceDeclarationsAction(definitionElement, WSDLConstants.XSD_NAMESPACE_URI, "xsd").run();
		xsdPrefix = message.getEnclosingDefinition().getPrefix(WSDLConstants.XSD_NAMESPACE_URI);
    }
			
    String defaultTypeName = "string";
    if (xsdPrefix != null && xsdPrefix.length() > 0)
    {
      defaultTypeName = xsdPrefix + ":" + defaultTypeName;
    }
    newElement.setAttribute("type", defaultTypeName);
  }
}

class AddServiceAction extends AddElementAction
{
  protected String name;
  protected Definition definition;

  public AddServiceAction(Definition definition, String name, Node parentNode, String prefix)
  {
    super(WSDLEditorPlugin.getWSDLString("_UI_ACTION_ADD_SERVICE"), "icons/service_obj.gif", parentNode, prefix, "service");
    this.definition = definition;
    this.name = name;
    setComputeTopLevelRefChild(true);
  }

  public AddServiceAction(Definition definition, String name, Node parentNode, String prefix, Document document)
  {
    super(WSDLEditorPlugin.getWSDLString("_UI_ACTION_ADD_SERVICE"), "icons/service_obj.gif", parentNode, prefix, "service");
    this.definition = definition;
    this.name = name;
    this.setDefinition(definition);
    this.document = document;
    setComputeTopLevelRefChild(true);
  }

  public boolean showDialog()
  {
    if (name == null)
    {
      name = NameUtil.buildUniqueServiceName(definition);
    }
    name = showDialogHelper(WSDLEditorPlugin.getWSDLString("_UI_ACTION_NEW_SERVICE"), name, NameUtil.getUsedServiceNames(definition));
    return name != null;
  }

  protected void addAttributes(Element newElement)
  {
    newElement.setAttribute("name", name);
  }
  
  protected void performAddElement() {  	
  	NodeList list = document.getChildNodes();
  	if (parentNode == null || list.getLength() == 0) {
  		createDefinitionStub();
  	}
  	
  	super.performAddElement();
  }
}

class AddPortTypeAction extends AddElementAction
{
  protected String name;
  protected Definition definition;

  public AddPortTypeAction(Definition definition, String name, Node parentNode, String prefix)
  {
    super(WSDLEditorPlugin.getWSDLString("_UI_ACTION_ADD_PORTTYPE"), "icons/porttype_obj.gif", parentNode, prefix, "portType");
    this.definition = definition;
    this.name = name;
    setComputeTopLevelRefChild(true);
  }

  public AddPortTypeAction(Definition definition, String name, Node parentNode, String prefix, Document document)
  {
    super(WSDLEditorPlugin.getWSDLString("_UI_ACTION_ADD_PORTTYPE"), "icons/porttype_obj.gif", parentNode, prefix, "portType");
    this.definition = definition;
    this.name = name;
    this.setDefinition(definition);
    this.document = document;
    setComputeTopLevelRefChild(true);
  }
  
  public boolean showDialog()
  {
    name = NameUtil.buildUniquePortTypeName(definition, name);
    name = showDialogHelper(WSDLEditorPlugin.getWSDLString("_UI_ACTION_NEW_PORTTYPE"), name, NameUtil.getUsedPortTypeNames(definition));
    return name != null;
  }

  protected void addAttributes(Element newElement)
  {
    newElement.setAttribute("name", name);
  }

  public String getPortTypeName()
  {
    return name;
  }
  
  protected void performAddElement() {
  	NodeList list = document.getChildNodes();
  	if (parentNode == null || list.getLength() == 0) {
  		createDefinitionStub();
  	}
  	
  	super.performAddElement();
  }
}

class AddBindingAction extends Action
{
  protected Definition definition;
  protected Document document;

  public AddBindingAction(Definition definition, Document document)
  {
    super(WSDLEditorPlugin.getWSDLString("_UI_ACTION_ADD_BINDING"), ImageDescriptor.createFromFile(WSDLEditorPlugin.class, "icons/binding_obj.gif")); //$NON-NLS-1$
    //setToolTipText(WSDLEditorPlugin.getInstance().getWSDLString("_UI_LABEL_BINDING_WIZARD")); //$NON-NLS-1$
    this.definition = definition;
    this.document = document;
  }

  public void run()
  {
    BindingWizard wizard = new BindingWizard(definition, document);
    WizardDialog wizardDialog = new WizardDialog(Display.getCurrent().getActiveShell(), wizard);
    wizardDialog.create();

    int result = wizardDialog.open();

    if (result == Window.OK)
    {
      //todo...      
    }
  }
}

class AddPortAction extends Action
{
	protected Service service;

	public AddPortAction(Service service)
	{
		super(WSDLEditorPlugin.getWSDLString("_UI_ACTION_ADD_PORT"), ImageDescriptor.createFromFile(WSDLEditorPlugin.class, "icons/binding_obj.gif")); //$NON-NLS-1$
		//setToolTipText(WSDLEditorPlugin.getInstance().getWSDLString("_UI_LABEL_BINDING_WIZARD")); //$NON-NLS-1$
		this.service = service;
	}

	public void run()
	{
		PortWizard wizard = new PortWizard(service);
		WizardDialog wizardDialog = new WizardDialog(Display.getCurrent().getActiveShell(), wizard);
		wizardDialog.create();

		int result = wizardDialog.open();
		if (result == Window.OK)
		{
			//todo...      
		}
	}
}

class GenerateBindingContentAction extends Action
{
	protected Binding binding;
	public GenerateBindingContentAction(Binding binding, boolean isEditable)
	{
		super(WSDLEditorPlugin.getWSDLString("_UI_GENERATE_BINDING_CONTENT"));
		this.binding = binding;
		setEnabled(isEditable);
	}
	
	public void run()
	{
		BindingWizard wizard = new BindingWizard(binding.getEnclosingDefinition(), BindingWizard.KIND_REGENERATE_BINDING);
		wizard.setBindingName(ComponentReferenceUtil.getName(binding));
		wizard.setPortTypeName(ComponentReferenceUtil.getPortTypeReference(binding));
		WizardDialog wizardDialog = new WizardDialog(Display.getCurrent().getActiveShell(), wizard);
		wizardDialog.create();
		wizardDialog.open();	
	}
}

//This is a temporary class to allow renaming.  This should be replaced when direct
//renaming is ready
class RenameDialogAction extends Action {
	private WSDLElement element;
	private String defaultName = "NewName";
	private List usedNames = new ArrayList();
	private boolean useSmartRename = false;
	private boolean showDialog = false;
	
	public RenameDialogAction(WSDLElement selection) {
		setText(WSDLEditorPlugin.getWSDLString("_UI_ACTION_RENAME"));
		element = selection;
		initialize();
	}
	
	public void run() {
		String result;

		NewComponentDialog dialog = new NewComponentDialog(WSDLEditorPlugin.getShell(), WSDLEditorPlugin.getWSDLString("_UI_ACTION_RENAME"), defaultName, usedNames);
		int rc = dialog.createAndOpen();
		if (rc == IDialogConstants.OK_ID)
		{
		  result = dialog.getName();
		  if (useSmartRename) {
		  	SmartRenameAction smartRename = new SmartRenameAction(element, result);	
		  	smartRename.run();
		  }
		  else {
		  	RenameAction rename = new RenameAction(element, result);
		  	rename.run();
		  }
		}
	}
	
	public void initialize() {
		if (element instanceof Fault) {
			usedNames = NameUtil.getUsedFaultNames((Operation) ((Fault) element).eContainer());
			defaultName = ((Fault) element).getName();
			
			showDialog = true;
			useSmartRename = true;
		}
		else if (element instanceof Message) {
			usedNames = NameUtil.getUsedMessageNames(element.getEnclosingDefinition());
			defaultName = ((Message) element).getQName().getLocalPart();
			
			showDialog = true;
			useSmartRename = true;
		}
		else if (element instanceof Operation) {
			usedNames = NameUtil.getUsedOperationNames((PortType) ((Operation) element).eContainer());		
			defaultName = ((Operation) element).getName();
			
			showDialog = true;
			useSmartRename = true;
		}
		else if (element instanceof Part) {
			usedNames = NameUtil.getUsedPartNames((Message) ((Part) element).eContainer());			
			defaultName = ((Part) element).getName();
			
			showDialog = true;
			useSmartRename = true;
		}
		else if (element instanceof PortType) {
			usedNames = NameUtil.getUsedPortTypeNames(element.getEnclosingDefinition());
			defaultName = ((PortType) element).getQName().getLocalPart();
			
			showDialog = true;
			useSmartRename = false;
		}
		else if (element instanceof Port) {
			usedNames = NameUtil.getUsedPortNames((Service) ((Port) element).eContainer());
			defaultName = ((Port) element).getName();
			
			showDialog = true;
			useSmartRename = true;
		}
		else if (element instanceof Binding) {
			usedNames = NameUtil.getUsedBindingNames(element.getEnclosingDefinition());			
			defaultName = ((Binding) element).getQName().getLocalPart();
			
			showDialog = true;
			useSmartRename = false;
		}
		else if (element instanceof Service) {
			usedNames = NameUtil.getUsedServiceNames(element.getEnclosingDefinition());
			defaultName = ((Service) element).getQName().getLocalPart();
			
			showDialog = true;
			useSmartRename = false;
		}
		usedNames.remove(defaultName);
	}
	
	public boolean showRenameDialog() {
		return showDialog;
	}
}