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
package org.eclipse.wst.wsdl.ui.internal.properties.section;

import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.wst.common.ui.properties.ITabbedPropertySheetPageContributor;
import org.eclipse.wst.common.ui.properties.TabbedPropertySheetPage;
import org.eclipse.wst.sse.ui.view.events.INodeSelectionListener;
import org.eclipse.wst.sse.ui.view.events.NodeSelectionChangedEvent;
import org.eclipse.wst.wsdl.XSDSchemaExtensibilityElement;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditor;
import org.eclipse.wst.wsdl.ui.internal.WSDLSelectionManager;
import org.eclipse.wst.wsdl.ui.internal.model.ModelAdapter;
import org.eclipse.wst.wsdl.ui.internal.model.ModelAdapterListener;
import org.eclipse.wst.wsdl.ui.internal.model.WSDLModelAdapterFactory;
import org.eclipse.wst.wsdl.ui.internal.util.WSDLEditorUtil;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class WSDLTabbedPropertySheetPage extends TabbedPropertySheetPage implements ISelectionChangedListener, INodeSelectionListener, ModelAdapterListener
{
  private WSDLSelectionManager fViewerSelectionManager;
  private WSDLEditor wsdlEditor;
  private Object currentObject;

  /**
   * @param tabbedPropertySheetPageContributor
   */
  public WSDLTabbedPropertySheetPage(ITabbedPropertySheetPageContributor tabbedPropertySheetPageContributor, WSDLEditor editor)
  {
    super(tabbedPropertySheetPageContributor);
    this.wsdlEditor = editor;
  }
  
  public void createControl(Composite parent) {
  	super.createControl(parent);
  	wsdlEditor.getSelectionManager().setSelection(new StructuredSelection(wsdlEditor.getDefinition()));
  }
  
	public void setSelectionManager(WSDLSelectionManager viewerSelectionManager) {
		// disconnect from old one
		if (fViewerSelectionManager != null) {
			fViewerSelectionManager.removeSelectionChangedListener(this);
		}

		fViewerSelectionManager = viewerSelectionManager;

		// connect to new one
		if (fViewerSelectionManager != null) {
			fViewerSelectionManager.addSelectionChangedListener(this);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ISelectionChangedListener#selectionChanged(org.eclipse.jface.viewers.SelectionChangedEvent)
	 */
	public void selectionChanged(SelectionChangedEvent event)
	{
		if (!event.getSelection().isEmpty()) {
			selectionChanged(getSite().getWorkbenchWindow().getActivePage().getActivePart(), event.getSelection());
		    //super.selectionChanged(getSite().getWorkbenchWindow().getActivePage().getActivePart(), event.getSelection());
		}
	}
  
  public void selectionChanged(IWorkbenchPart part, ISelection selection)
  {
    // override for category
    if (selection != null)
    {
      if (selection instanceof StructuredSelection)
      {
        StructuredSelection structuredSelection = (StructuredSelection)selection;
        if (structuredSelection.isEmpty())
        {
          return;
        }
        Object obj = structuredSelection.getFirstElement();
        Node node = WSDLEditorUtil.getInstance().getNodeForObject(obj);
        Object o = WSDLEditorUtil.getInstance().findModelObjectForElement(wsdlEditor.getDefinition(), (Element)node);
        
        // bad hack to get schema from category
        // until we provide a better mechanism (selection manager extension)
        // see also WSDLGraphViewer
        if (o instanceof XSDSchemaExtensibilityElement)
        {
          obj = ((XSDSchemaExtensibilityElement)o).getSchema();
          selection = new StructuredSelection(obj);
        } 
      	
        attachListener(o);
      }
      else if (selection instanceof TextSelection)
      {
        return;
      }
    }
    
    super.selectionChanged(wsdlEditor.getEditorPart(), selection);   // event.getSelection()); 
  }
  
  protected void attachListener(Object object)
  {
  	WSDLModelAdapterFactory adapterFactory = WSDLModelAdapterFactory.getWSDLModelAdapterFactory();
    ModelAdapter adapter = adapterFactory.getAdapter(object);
    if (adapter != null)
    {
    	// remove listener from the previously selected object
    	removeListener(currentObject);
    	
    	// add listener to the newly selected object
    	adapter.addListener(this);
    	currentObject = object;
    }
  } 
  
  protected void removeListener(Object object)
  {
  	WSDLModelAdapterFactory adapterFactory = WSDLModelAdapterFactory.getWSDLModelAdapterFactory();
    ModelAdapter adapter = adapterFactory.getAdapter(object);
    if (adapter != null)
    {
      adapter.removeListener(this);
      currentObject = null;
    }
  }
	public void dispose() {
		// disconnect from the ViewerSelectionManager
		if (fViewerSelectionManager != null) {
			fViewerSelectionManager.removeSelectionChangedListener(this);
		}
		
		// disconnect listener from the current object
		if (currentObject != null) {
			removeListener(currentObject);
		}
		
		super.dispose();
	}

  
  /* (non-Javadoc)
   * @see com.ibm.sse.editor.view.events.INodeSelectionListener#nodeSelectionChanged(com.ibm.sse.editor.view.events.NodeSelectionChangedEvent)
   */
  public void nodeSelectionChanged(NodeSelectionChangedEvent event)
  {
  }
  
  public void propertyChanged(Object object, String property) {
  	if (getCurrentTab() != null) {
  		refresh();
  	}
  }
}
