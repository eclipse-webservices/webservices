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
        
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.wst.wsdl.ui.internal.model.ModelAdapter;
import org.eclipse.wst.wsdl.ui.internal.model.ModelAdapterFactory;



public class ModelAdapterLabelProvider extends LabelProvider
{                     
  protected ModelAdapterFactory adapterFactory;

  public ModelAdapterLabelProvider(ModelAdapterFactory adapterFactory)
  {                                      
    this.adapterFactory = adapterFactory;
  }

 
  public Image getImage(Object object)
  {
    Image result = null;           
// TODO: port check
    ModelAdapter modelAdapter = adapterFactory.getAdapter(object);
//    ModelAdapter modelAdapter = EcoreUtil.getAdapter(adapterFactory.eAdapters(),object);
    if (modelAdapter != null)
    {
      result = (Image)modelAdapter.getProperty(object, ModelAdapter.IMAGE_PROPERTY);     
    }                                            
    return result;
  } 
    

  public String getText(Object object)
  {                
    String result = null;               
// TODO: port check
    ModelAdapter modelAdapter = adapterFactory.getAdapter(object);
//    ModelAdapter modelAdapter = EcoreUtil.getAdapter(adapterFactory.eAdapters(),object);
    if (modelAdapter != null)
    {                       
      result = (String)modelAdapter.getProperty(object, ModelAdapter.LABEL_PROPERTY);     
    }                                            
    return result;
  }   
}
