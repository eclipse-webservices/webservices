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
package org.eclipse.wst.wsdl.ui.internal.xsd.graph.editparts;
         
import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.wst.wsdl.XSDSchemaExtensibilityElement;
import org.eclipse.wst.xsd.ui.internal.gef.util.figures.ContainerFigure;


public class XSDSchemaExtensibilityElementEditPart extends AbstractGraphicalEditPart 
{                                 
  protected EditPartFactory factory;

  public XSDSchemaExtensibilityElementEditPart(EditPartFactory factory)
  {
    this.factory = factory;
  }

  protected IFigure createFigure()
  {                                     
    ContainerFigure figure = new ContainerFigure();    
    return figure;
  }    

  protected EditPart createChild(Object model) 
  {
    return factory.createEditPart(this, model);
  }      

  protected List getModelChildren() 
  { 
    XSDSchemaExtensibilityElement e = (XSDSchemaExtensibilityElement)getModel();
    List result = new ArrayList();
    result.add(e.getSchema());
    return result;
  }
 
  protected void createEditPolicies() 
  {
  }
}