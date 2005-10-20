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
package org.eclipse.wst.wsdl.ui.internal.outline;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.views.contentoutline.ContentOutlinePage;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditor;
import org.eclipse.wst.wsdl.ui.internal.WSDLSelectionManager;
import org.eclipse.wst.wsdl.ui.internal.actions.WSDLMenuListener;
import org.eclipse.wst.wsdl.ui.internal.util.OpenOnSelectionHelper;

/**
 * @deprecated Using SSE's ConfiguratbleContentOutlinePage 
 * instead via WSDLContentOutlineCOnfiguration
 */
public class WSDLContentOutlinePage extends ContentOutlinePage 
{                                                    
  protected WSDLEditor wsdlEditor;
	protected int level = 0;  
  protected Object model;                          
  protected ITreeContentProvider contentProvider;
  protected ILabelProvider labelProvider;
  protected WSDLSelectionManager selectionManager;
  protected SelectionManagerSelectionChangeListener selectionManagerSelectionChangeListener = new SelectionManagerSelectionChangeListener();
  protected TreeSelectionChangeListener treeSelectionChangeListener = new TreeSelectionChangeListener();
        
  public WSDLContentOutlinePage(WSDLEditor wsdlEditor)
  {                                          
    this.wsdlEditor = wsdlEditor;
  }


  public void setContentProvider(ITreeContentProvider contentProvider)
  {
    this.contentProvider = contentProvider;
  }


  public void setLabelProvider(ILabelProvider labelProvider)
  {
    this.labelProvider = labelProvider;
  }

                          
  // expose
  public TreeViewer getTreeViewer()
  {
    return super.getTreeViewer();
  }

	public void createControl(Composite parent) 
  {                                                 
		super.createControl(parent);                 

		getTreeViewer().setContentProvider(contentProvider);
		getTreeViewer().setLabelProvider(labelProvider);
    getTreeViewer().setInput(model);
    getTreeViewer().addSelectionChangedListener(this);
    
    KeyAdapter keyListener = new KeyAdapter()
    {
      public void keyReleased(KeyEvent e)
      {   	
        if (e.keyCode == SWT.F3)
        {
          ISelection selection = selectionManager.getSelection();
          if (selection instanceof IStructuredSelection)
          {
            Object object = ((IStructuredSelection)selection).getFirstElement();
            if (object instanceof EObject)
            {
			  OpenOnSelectionHelper helper = new OpenOnSelectionHelper(wsdlEditor.getDefinition());
              helper.openEditor((EObject)object);
            }
          }
        }
      }
    };
    getTreeViewer().getTree().addKeyListener(keyListener);

    MenuManager menuManager = new MenuManager("#popup");//$NON-NLS-1$
    menuManager.setRemoveAllWhenShown(true);
    Menu menu = menuManager.createContextMenu(getTreeViewer().getControl());
    getTreeViewer().getControl().setMenu(menu);
    
    WSDLMenuListener menuListener = new WSDLMenuListener(wsdlEditor, wsdlEditor.getSelectionManager());//, (XSDTextEditor)fTextEditor);
    menuManager.addMenuListener(menuListener);

    setSelectionManager(wsdlEditor.getSelectionManager());    
    
    // enable popupMenus extension - Rich - this class is deprecated.  TODO - change this
    getSite().registerContextMenu("org.eclipse.wst.wsdl.ui.popup.outline", menuManager, wsdlEditor.getSelectionManager());
	}


  public void setModel(Object object)
  {
    model = object;
  }

	
	public void setExpandToLevel(int i) 
  {
		level = i;
	}

	
	public void setInput(Object value) 
  {
		getTreeViewer().setInput(value);
	 	getTreeViewer().expandToLevel(level);
	}  


  public void setSelectionManager(WSDLSelectionManager newSelectionManager)
  { 
    TreeViewer treeViewer = getTreeViewer();

    // disconnect from old one
    if (selectionManager != null)
    {                                                        
      selectionManager.removeSelectionChangedListener(selectionManagerSelectionChangeListener);  
      treeViewer.removeSelectionChangedListener(treeSelectionChangeListener);
    }

    selectionManager = newSelectionManager;

    // connect to new one
    if (selectionManager != null)
    {
      selectionManager.addSelectionChangedListener(selectionManagerSelectionChangeListener);  
      treeViewer.addSelectionChangedListener(treeSelectionChangeListener);
    }
  }    

  class SelectionManagerSelectionChangeListener implements ISelectionChangedListener
  {
    public void selectionChanged(SelectionChangedEvent event)  
    {         
      if (event.getSelectionProvider() != getTreeViewer())
      {
        getTreeViewer().setSelection(event.getSelection(), true);
      }
    }    
  }

  class TreeSelectionChangeListener implements ISelectionChangedListener
  {
    public void selectionChanged(SelectionChangedEvent event)  
    { 
      if (selectionManager != null)
      {
        ISelection selection = event.getSelection();
        if (selection instanceof IStructuredSelection)
        {
          IStructuredSelection structuredSelection = (IStructuredSelection)selection;
          Object o = structuredSelection.getFirstElement();
        
          // TODO ... 
          // we need to implement a selectionManagerMapping extension point
          // so that extensions can specify how they'd like to map view objects 
          // to selection objects
          //                                        
//          if (o instanceof Element)
//          {  
//            try
//            {
//              Object modelObject = WSDLEditorUtil.getInstance().findModelObjectForElement(wsdlEditor.getDefinition(), (Element)o);
//              if (modelObject != null && !(modelObject instanceof UnknownExtensibilityElement))
//              {
//                o = modelObject;
//              }
//            }
//            catch (Exception e)
//            {
//            }
//          }

          if (o != null)
          {
            selectionManager.setSelection(new StructuredSelection(o), getTreeViewer());
          }
          else
          {
            selectionManager.setSelection(new StructuredSelection(), getTreeViewer());
          }   
        }
      }
    }    
  }
}