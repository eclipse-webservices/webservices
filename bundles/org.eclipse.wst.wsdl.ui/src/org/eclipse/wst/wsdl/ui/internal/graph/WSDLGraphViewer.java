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
package org.eclipse.wst.wsdl.ui.internal.graph;
                                                 
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.WSDLElement;
import org.eclipse.wst.wsdl.XSDSchemaExtensibilityElement;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditor;
import org.eclipse.wst.wsdl.ui.internal.actions.CopyGlobalAction;
import org.eclipse.wst.wsdl.ui.internal.actions.DeleteWSDLAndXSDAction;
import org.eclipse.wst.wsdl.ui.internal.actions.PasteGlobalAction;
import org.eclipse.wst.wsdl.ui.internal.graph.editparts.ComponentViewerRootEditPart;
import org.eclipse.wst.wsdl.ui.internal.graph.editparts.GroupEditPart;
import org.eclipse.wst.wsdl.ui.internal.graph.editparts.TreeNodeEditPart;
import org.eclipse.wst.wsdl.ui.internal.model.WSDLGroupObject;
import org.eclipse.wst.wsdl.ui.internal.util.SelectionAdapter;
import org.eclipse.wst.wsdl.ui.internal.util.WSDLEditorUtil;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.eclipse.xsd.XSDComplexTypeDefinition;
import org.eclipse.xsd.XSDConcreteComponent;
import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDModelGroupDefinition;
import org.eclipse.xsd.XSDSchema;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class WSDLGraphViewer implements ISelectionChangedListener
{
  protected Control componentViewerControl; 
  protected WSDLComponentViewer componentViewer;
  protected Definition definition;              
  protected WSDLEditor editor;  
  protected InternalSelectionAdapter internalSelectionAdapter = new InternalSelectionAdapter();

  GraphViewToolBar form;  // Tool bar view form
  
  public WSDLGraphViewer(WSDLEditor editor)
  {
    super();
    this.editor = editor;                 
  }    

  public void setDefinition(Definition definition)
  {
    this.definition = definition;              
  }

  public ISelectionProvider getSelectionProvider()
  {
    return internalSelectionAdapter;
  }

  public Control createControl(Composite parent)
  {
    componentViewer = new WSDLComponentViewer(editor, editor.getSelectionManager());

    componentViewer.addSelectionChangedListener(internalSelectionAdapter);
    internalSelectionAdapter.addSelectionChangedListener(editor.getSelectionManager());

    form = new GraphViewToolBar(editor, parent, SWT.NONE);

    componentViewerControl = componentViewer.createControl(form);
    form.setContent(componentViewerControl);

    editor.getSelectionManager().addSelectionChangedListener(this);
    
    componentViewerControl.addKeyListener(new KeyAdapter() {
    	public void keyPressed(KeyEvent e) {
    		if (e.character == SWT.DEL) {
    			List selections = ((IStructuredSelection) editor.getSelectionManager().getSelection()).toList();
       			DeleteWSDLAndXSDAction deleteAction = new DeleteWSDLAndXSDAction(selections, editor.getDefinition().getElement(), editor);
       			deleteAction.run();
    		}
    	}    	
    });
    
    return componentViewerControl; 
  }

  public WSDLComponentViewer getComponentViewer()
  {
    return componentViewer;
  }


  Node inputNode;    
  public void setInput(Object object)
  {
    componentViewer.setInput(object);
  }
  
  public void setBackButtonEnabled(boolean state)
  {
    form.setBackButtonEnabled(state);
  }

  //protected boolean isDeleted(Object object)
  //}            

    
  // this is called when selection changes in the selection manager
  //


  
  protected Object getInputComponentForNode(Node node)   
  {
    // TODO... logic to get WSDLElement or other object (e.g. XSD or extension for the Node)
    // TODO... consider case where the model is simply a chunk of DOM
    return null;
  }
   
  protected Object getSelectionComponentForNode(Node node)   
  {
    // TODO... logic to get WSDLElement or other object (e.g. XSD or extension for the Node)
    // TODO... consider case where the model is simply a chunk of DOM
    return null;
  } 

  protected class InternalSelectionAdapter extends SelectionAdapter implements ISelectionChangedListener
  {
    public Object getObjectForOtherModel(Object object)
    {            
      Object result = null;
      if (object instanceof EditPart)
      {       
        // fix for defect 4294 ... this test prevents selection from changing
        // when a component is renamed which indirectly causes it to be removed and
        // re-added from the 'sorted' list
        //
        if (!(object instanceof ComponentViewerRootEditPart))
        {  
          EditPart editPart = (EditPart)object;
          result = editPart.getModel();
        }  
      }
      return result;
    }  

    public void selectionChanged(SelectionChangedEvent event)                                       
    {                                        
      setSelection(event.getSelection()); 
    }  
  }
    
  // this gets called when the selection changes within the selection manager
  //
  public void selectionChanged(SelectionChangedEvent event) 
  {   
    //System.out.println("WSDLGraphViewer.selectionChanged( " + event.getSource() + ")");
  	
  	 IStructuredSelection selectionObject = (IStructuredSelection)event.getSelection();
     Object modelObject = selectionObject.getFirstElement();
     if (modelObject instanceof WSDLElement) {
     	Element node = ((WSDLElement) modelObject).getElement();
         boolean isEditable = (node instanceof IDOMNode);
         
     	Action copyAction = new CopyGlobalAction((WSDLElement) modelObject, editor);
     	Action pasteAction = new PasteGlobalAction((WSDLElement) modelObject, editor);
 		
 		IActionBars actionBars = editor.getEditorSite().getActionBars();
 		actionBars.setGlobalActionHandler(ActionFactory.COPY.getId(), copyAction);
 		actionBars.setGlobalActionHandler(ActionFactory.PASTE.getId(), pasteAction);
 		actionBars.updateActionBars();
 		
 		if (!isEditable) {
 			copyAction.setEnabled(false);
 			pasteAction.setEnabled(false);
 		}
     }

    if (event.getSource() != internalSelectionAdapter && event.getSource() != (editor.getTextEditor()).getSelectionProvider())
    {   
      boolean isEmptySelectionRequired = true;
      if (event.getSelection() instanceof IStructuredSelection)
      {
        IStructuredSelection selection = (IStructuredSelection)event.getSelection();
        Object model = selection.getFirstElement();

        if (model != null)
        {                                                                           
          EditPart editPart = getEditPart(componentViewer.getRootEditPart(), model);
          if (editPart == null)
          {                                                                         
            editPart = expandToReveal(componentViewer.getRootEditPart(), model);
          }
          if (editPart == null)
          {
          	if (model instanceof XSDConcreteComponent) {
          		model = getTopLevelComponent((XSDConcreteComponent) model);
          		
				if (model instanceof XSDElementDeclaration ||
					model instanceof XSDComplexTypeDefinition ||
					model instanceof XSDModelGroupDefinition ||
					model instanceof XSDSchema) {
						componentViewer.setInput(model);
				}
            }
          	else if (model instanceof WSDLElement || model instanceof WSDLGroupObject) {
          		// We need to switch the output to Definition
          		componentViewer.setInput(editor.getDefinition());
          		editPart = getEditPart(componentViewer.getRootEditPart(), model);
          		if (editPart == null)
          		{                                                                         
          			editPart = expandToReveal(componentViewer.getRootEditPart(), model);
          		}
          	}
            else
            {
              // bad hack to convert xsd editor category to xsd object
              // see also WSDLTabbedPropertySheetPage
              Node node = WSDLEditorUtil.getInstance().getNodeForObject(model);
              Object o = WSDLEditorUtil.getInstance().findModelObjectForElement(editor.getDefinition(), (Element)node);
              if (o instanceof XSDSchemaExtensibilityElement)
              {
                Object obj = ((XSDSchemaExtensibilityElement)o).getSchema();
                componentViewer.setInput(obj);
              }
            }
          }
          if (editPart != null)
          {
            isEmptySelectionRequired = false;
            componentViewer.setSelection(new StructuredSelection(editPart));
            scrollIntoView(editPart);
          }   
        }
      }
      if (isEmptySelectionRequired)
      {
        componentViewer.setSelection(new StructuredSelection());
      }
    }
  }
  
  protected XSDConcreteComponent getTopLevelComponent(XSDConcreteComponent component)
  {
    XSDConcreteComponent prev = component;
    XSDConcreteComponent container = component;
    while ( container != null && !(container instanceof XSDSchema))
    {
      prev = container;     
      container = container.getContainer();
    }
    return container != null ? prev : null;
  }

  protected EditPart getEditPart(EditPart editPart, Object model)  
  {                     
    EditPart result = null;
    if (editPart.getModel() == model)
    {
      result = editPart;      
    }                   
    else
    {
      for (Iterator i = editPart.getChildren().iterator(); i.hasNext(); )
      {
        result = getEditPart((EditPart)i.next(), model);
        if (result != null)
        {
          break;
        }
      }
    }
    return result;
  }


  public EditPart expandToReveal(EditPart editPart, Object model)
  {   
    // here we create the parent list
    //
    List parentList = new ArrayList();
    for (Object o = model; o != null; o = getParent(o))
    {
      parentList.add(0, o);
    }               
    EditPart result = expandToRevealHelper(editPart, parentList, 0);    
                 
    return result;
  }   


  public EditPart expandToRevealHelper(EditPart editPart, List parentList, int index)
  {                                      
    EditPart result = null;
    int parentListSize =  parentList.size();
    Object model = index < parentListSize ? parentList.get(index) : null;
    if (model != null && editPart != null)
    { 
      // search editpart and its descendants to find a match for this model object
      //
      result = getEditPart(editPart, model);  
      //System.out.println("getEditPart(" + index + ", " + model + ") = " + result);
    }

    if (result != null)
    {
      if (index < (parentListSize - 1))
      {
        // we've found the editPart for the item in the parentList 
        // now we need to find the next item 
        if (result instanceof TreeNodeEditPart)
        {
          ((TreeNodeEditPart)result).setExpanded(true);
        }           
        result = expandToRevealHelper(result, parentList, index + 1);
      }
    }
    return result;
  }
    
    
  protected Object getParent(Object model)
  { 
    Object parent = null;
    if (model instanceof EObject)
    {
      parent = ((EObject)model).eContainer();
    }                                        
    return parent;
  }     


  public void scrollIntoView(EditPart editPart)
  {                
    for (EditPart parent = editPart.getParent(); parent != null; parent = parent.getParent())
    {
      if (parent instanceof GroupEditPart)
      {
        GroupEditPart groupEditPart = (GroupEditPart)parent;
        groupEditPart.scrollToRevealEditPart(editPart);
        break;
      }
    }
  }
}