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
package org.eclipse.wst.wsdl.ui.internal.viewers;
                                              
import java.util.HashMap;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.part.PageBook;
import org.eclipse.wst.wsdl.XSDSchemaExtensibilityElement;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditor;
import org.eclipse.wst.wsdl.ui.internal.model.WSDLGroupObject;
import org.eclipse.xsd.XSDSchema;


public class WSDLDetailsViewer implements ISelectionChangedListener
{                                  
  protected WSDLEditor editor;
  protected Composite client;  
  protected PageBook pageBook;
  protected ExtensibleDetailsViewerProvider detailsViewerProvider;  
  protected HashMap viewerMap = new HashMap();

  protected final static String EMPTY_VIEWER_KEY = "EMPTY_VIEWER_KEY";
  protected Viewer emptyViewer;

  public WSDLDetailsViewer(WSDLEditor editor)
  {
    this.editor = editor;                            
    detailsViewerProvider = new ExtensibleDetailsViewerProvider(editor);    
    editor.getSelectionManager().addSelectionChangedListener(this);
  }
  
  public Control createControl(Composite parent)
  {
    client = pageBook = new PageBook(parent, 0);
	pageBook.setLayoutData(new GridData(GridData.FILL_BOTH));
    emptyViewer = new EmptyViewer(pageBook, SWT.NONE);
    viewerMap.put(EMPTY_VIEWER_KEY, emptyViewer);
    pageBook.showPage(emptyViewer.getControl());
    return client;
  }  

  public void setInput(Object object)
  {                                 
    try
    {
      Object key = detailsViewerProvider.getViewerKey(object);  
      
      if (key == null)
      {
        key = EMPTY_VIEWER_KEY;
      }
      Viewer viewer = (Viewer)viewerMap.get(key);
      if (viewer == null)
      {
        viewer = detailsViewerProvider.createViewer(object, pageBook, editor);
        if (viewer != null)
        {
          viewerMap.put(key, viewer);
          if (viewer instanceof OperationViewer)
          {

          }
        }
      }
      
      if (viewer != null)
      {
        if (object instanceof XSDSchemaExtensibilityElement)
        {
          XSDSchemaExtensibilityElement schema = (XSDSchemaExtensibilityElement)object;
          XSDSchema xsdSchema = schema.getSchema();
          if (xsdSchema != null)
          {
            viewer.setInput(xsdSchema.getElement());
          }
        }
        else
        {
          viewer.setInput(object);
        }
        pageBook.showPage(viewer.getControl());        
      }
    }
    catch (Exception e)
    {
    	e.printStackTrace();
    }
  }   

  public void selectionChanged(SelectionChangedEvent event)  
  {                                 
    ISelection selection = event.getSelection();
    if (selection instanceof IStructuredSelection)
    {
      Object o = ((IStructuredSelection)selection).getFirstElement();
      if (o instanceof WSDLGroupObject)
      {
      	o = ((WSDLGroupObject)o).getDefinition();
      }
      setInput(o);       
    }
  }   
}
                      