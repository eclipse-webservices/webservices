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

import java.util.Collections;
import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.wst.wsdl.ui.internal.model.ModelAdapter;
import org.eclipse.wst.wsdl.ui.internal.model.ModelAdapterFactory;
import org.eclipse.wst.wsdl.ui.internal.model.ModelAdapterListener;



public class ModelAdapterContentProvider implements ITreeContentProvider, ModelAdapterListener
{                                 
  protected Viewer viewer;
  protected ModelAdapterFactory adapterFactory;

  public ModelAdapterContentProvider(ModelAdapterFactory adapterFactory)
  {                                      
    this.adapterFactory = adapterFactory;  
  }
        
  protected void attachListener(Object object)
  {
    ModelAdapter adapter = adapterFactory.getAdapter(object);
// TODO: port check
//    ModelAdapter adapter = EcoreUtil.getAdapter(adapterFactory.eAdapters(),object);
    if (adapter != null)
    {
      adapter.addListener(this);
    }
  } 

  public void propertyChanged(Object object, String property)
  {             
    if (viewer != null)
    {
      if (viewer instanceof StructuredViewer)
      {
        ((StructuredViewer)viewer).refresh(object);
      }
      else
      {     
        viewer.refresh();
      }
    }
  }

  /*
   * @see ITreeContentProvider#getChildren(Object)
   */
  public Object[] getChildren(Object parentObject)
  {                                     
    attachListener(parentObject);

    List list = null;
// TODO: port check    
    ModelAdapter modelAdapter = adapterFactory.getAdapter(parentObject);
//    ModelAdapter modelAdapter = EcoreUtil.getAdapter(adapterFactory.eAdapters(),parentObject);
    if (modelAdapter != null)
    {
      list = (List)modelAdapter.getProperty(parentObject, ModelAdapter.CHILDREN_PROPERTY);     
    }
    list =  list != null ? list : Collections.EMPTY_LIST;
    return list.toArray();
  }

  /*
   * @see ITreeContentProvider#getParent(Object)
   */
  public Object getParent(Object element)
  {
    return null;
  }

  /*
   * @see ITreeContentProvider#hasChildren(Object)
   */
  public boolean hasChildren(Object element)
  {
    Object[] children = getChildren(element);
    return children != null && children.length > 0;   
  }

  /*
   * @see IStructuredContentProvider#getElements(Object)
   */
  public Object[] getElements(Object inputElement)
  {
    return getChildren(inputElement);
  }

  /*
   * @see IContentProvider#dispose()
   */
  public void dispose()
  { 
    viewer = null;
  }
  
  /*
   * @see IContentProvider#inputChanged(Viewer, Object, Object)
   */
  public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
  {               
    this.viewer = viewer;
  }   
}