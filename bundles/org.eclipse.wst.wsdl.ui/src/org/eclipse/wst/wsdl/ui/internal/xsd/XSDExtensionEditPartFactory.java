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

import org.eclipse.gef.EditPart;
import org.eclipse.wst.wsdl.ui.internal.model.WSDLGroupObject;
import org.eclipse.wst.wsdl.ui.internal.xsd.graph.editparts.XSDSchemaDrillDownEditPart;
import org.eclipse.wst.xsd.ui.internal.graph.editparts.XSDEditPartFactory;
import org.eclipse.xsd.XSDSchema;

public class XSDExtensionEditPartFactory extends XSDEditPartFactory
{
  public EditPart createEditPart(EditPart parent, Object model)
  {
    EditPart editPart = null;
            
    if (model instanceof XSDSchema && parent.getModel() instanceof WSDLGroupObject)
    {         
      editPart = new XSDSchemaDrillDownEditPart();
      editPart.setModel(model);
      editPart.setParent(parent);
    }
    else
    {
      editPart = super.createEditPart(parent, model);
    }
 
    return editPart;
  }
}