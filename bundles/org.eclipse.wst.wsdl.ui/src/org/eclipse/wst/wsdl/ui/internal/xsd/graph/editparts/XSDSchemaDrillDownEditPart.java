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
         
import org.eclipse.draw2d.IFigure;
import org.eclipse.wst.wsdl.ui.internal.graph.editparts.DrillDownEditPart;
import org.eclipse.wst.xsd.ui.internal.XSDEditorPlugin;
import org.eclipse.xsd.XSDSchema;


public class XSDSchemaDrillDownEditPart extends DrillDownEditPart 
{                                 
  protected IFigure createFigure()
  {                                     
    IFigure figure = super.createFigure();
    label.setIcon(XSDEditorPlugin.getXSDImage("icons/XSDFile.gif"));
    return figure;
  }   

  protected void refreshVisuals()
  {                                       
    XSDSchema schema = (XSDSchema)getModel();
    String ns = schema.getTargetNamespace() != null ? schema.getTargetNamespace() : "";
    label.setText(ns);
  }
}