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
package org.eclipse.wst.wsdl.ui.internal.gef.util.editparts;

import java.util.Iterator;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LayoutManager;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.wst.xsd.ui.internal.gef.util.figures.IExpandable;
import org.eclipse.wst.wsdl.ui.internal.gef.util.figures.Interactor;

                 
// hooks up an interactor with an edit part and the figure that needs to be expanded and collapsed
//
public class InteractorHelper implements MouseListener
{                             
  protected boolean needToPerformDefaultExpansion = true;
  protected EditPart editPart;               
  protected Interactor interactor;                                                        
  protected IExpandable expandable;

  public InteractorHelper(EditPart editPart, Interactor interactor, IExpandable expandable)
  {                          
    this.editPart = editPart;
    this.interactor = interactor;
    this.expandable = expandable;
    interactor.addMouseListener(this);
    expandable.setExpanded(interactor.isExpanded());
  }                  

  public void setExpanded(boolean isExpanded)
  {
    interactor.setExpanded(isExpanded);
    interactorExpansionChanged(isExpanded);
  }
  
  protected void interactorExpansionChanged(boolean isInteractorExpanded)
  { 
    expandable.setExpanded(isInteractorExpanded);
    expandable.setVisible(isInteractorExpanded);

    editPart.refresh();

    EditPart root = editPart.getRoot();
    if (root instanceof AbstractGraphicalEditPart)
    {                               
      IFigure rootFigure = ((AbstractGraphicalEditPart)root).getFigure();
      invalidateAll(rootFigure);
      rootFigure.validate();
      rootFigure.repaint();
    }      
  }

         
  protected void invalidateAll(IFigure figure)
  {
    figure.invalidate();   
    LayoutManager manager = figure.getLayoutManager();
    if (manager != null)
    {
      manager.invalidate();
    }
    for (Iterator i = figure.getChildren().iterator(); i.hasNext(); )
    {
      IFigure child = (IFigure)i.next();
      invalidateAll(child);
    }
  } 
     

  // implements MouseListener
  //
  public void mouseDoubleClicked(MouseEvent me) 
  {      
  }
 
  public void mousePressed(MouseEvent me)
  {      
    boolean newExpansionState = !interactor.isExpanded();
    interactor.setExpanded(newExpansionState);
    interactorExpansionChanged(newExpansionState);
  }
 
  public void mouseReleased(MouseEvent me)  
  { 
  }
}