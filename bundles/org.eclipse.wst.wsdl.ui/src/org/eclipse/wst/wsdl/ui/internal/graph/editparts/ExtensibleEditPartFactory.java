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
package org.eclipse.wst.wsdl.ui.internal.graph.editparts;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.extension.WSDLEditorExtension;
import org.eclipse.wst.wsdl.ui.internal.extension.WSDLEditorExtensionRegistry;

public class ExtensibleEditPartFactory implements EditPartFactory
{                           
  protected WSDLEditorExtension[] extensions;
  protected EditPartFactory[] editPartFactories;

  protected static ExtensibleEditPartFactory instance;
                      
  public static ExtensibleEditPartFactory getInstance()
  {
    if (instance == null)
    {               
      instance = new ExtensibleEditPartFactory();
    }
    return instance;
  }
                                              
  public ExtensibleEditPartFactory()
  {
    WSDLEditorExtensionRegistry registry = WSDLEditorPlugin.getInstance().getWSDLEditorExtensionRegistry(); 

    extensions = registry.getRegisteredExtensions(WSDLEditorExtension.EDIT_PART_FACTORY); 
    editPartFactories = new EditPartFactory[extensions.length]; 
    for (int i = 0; i < extensions.length; i++)
    {
      editPartFactories[i] = (EditPartFactory)extensions[i].createExtensionObject(WSDLEditorExtension.EDIT_PART_FACTORY, null);
    }
  }        

  protected EditPartFactory getApplicableEditPartFactory(Object object)
  {                             
    EditPartFactory result = null;
    for (int i = 0; i < extensions.length; i++)
    {
      if (extensions[i].isApplicable(object))
      {
        result  = editPartFactories[i];
        if (result != null)
        {
          break;
        }
      }
    }
    
    if (result == null) {
    	boolean b = true;
    }
    return result;
  }  

  public EditPart createEditPart(EditPart parent, Object model)
  {                     
    EditPart result = null;
    EditPartFactory factory = getApplicableEditPartFactory(model);
    if (factory != null)   
    {
      result = factory.createEditPart(parent, model);
    }  
    else
    {
      result = new UnknownObjectEditPart();
      result.setModel(model);
      result.setParent(parent);
    }
    return result;
  }
}