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
package org.eclipse.wst.wsdl.ui.internal.graph.figures;
                                         
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MarginBorder;
              

public class TreeNodeContentFigure extends ExpandableGraphNodeContentFigure
{            
  protected void createOutlinedArea(IFigure parent)
  {                   
    super.createOutlinedArea(parent);
    outlinedArea.setBorder(new MarginBorder(0,2,0,2)); 
  }    
  
  public IFigure getConnectionFigure()
  {
    return outlinedArea;
  }           
}