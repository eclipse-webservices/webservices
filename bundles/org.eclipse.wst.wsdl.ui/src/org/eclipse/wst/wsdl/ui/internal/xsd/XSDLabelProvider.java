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
package org.eclipse.wst.wsdl.ui.internal.xsd;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.wst.wsdl.XSDSchemaExtensibilityElement;
import org.eclipse.wst.xsd.ui.internal.provider.XSDAdapterFactoryLabelProvider;
import org.eclipse.xsd.XSDSchema;



/**
 * This version of the XSDLabelProvider is a modified version of the one in XSDEditor.
 * This provider only needs to handle nodes in the XML Schema namespace.
 * Other XML nodes (including comments, PIs etc) get handled by the 'main' provider.
 */
public class XSDLabelProvider extends LabelProvider
{
  XSDAdapterFactoryLabelProvider adapterFactoryLabelProvider;
  
  /**
   * Constructor for XSDLabelProvider.
   */
  public XSDLabelProvider(XSDAdapterFactoryLabelProvider adapterFactoryLabelProvider)
  {
    super();
    this.adapterFactoryLabelProvider = adapterFactoryLabelProvider;
  }
  
  /*
   * @see ILabelProvider#getImage(Object)
   */
  public Image getImage(Object element)
  {
    if (element instanceof XSDSchemaExtensibilityElement)
    {
      element = ((XSDSchemaExtensibilityElement)element).getSchema().getElement();
    }
    return adapterFactoryLabelProvider.getImage(element);
  }
    
  /*
   * @see ILabelProvider#getText(Object)
   */
  public String getText(Object element)
  {
    // override text for xsd schema
    if (element instanceof XSDSchema)
    {
      return ((XSDSchema)element).getTargetNamespace();
    }
    else
    {
    	// We don't call "adapterFactoryLabelProvider.getText(element)" because it will return a object.toString() if
    	// there is no labelProvider....  but we don't want to display this 'garbage' info to the user.    	
    	AdapterFactory adapterFactory = adapterFactoryLabelProvider.getAdapterFactory();
        ILabelProvider labelProvider = (ILabelProvider)adapterFactory.adapt(element, ILabelProvider.class);
        String text = "";
        if (labelProvider != null) {
        	text = labelProvider.getText(element);
        }
        
        return text;
    }
  }

}


