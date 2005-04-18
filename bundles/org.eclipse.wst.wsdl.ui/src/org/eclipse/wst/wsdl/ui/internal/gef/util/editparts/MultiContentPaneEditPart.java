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
                               
import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;


public abstract class MultiContentPaneEditPart extends AbstractGraphicalEditPart
{
  protected IFigure[] contentPanes;
  protected int[] contentPaneIndexAdjustment;
                         
  protected IFigure[] initContentPanes()
  {                             
    return null;
  }    

  protected int getContentPane(Object model)
  {  
    return 0;
  }
    
  protected List sortByContentPane(List list)
  {                    
    return list;
  }  


  protected void setFigure(IFigure figure)
  {
	  super.setFigure(figure);
    contentPanes = initContentPanes();   
    if (contentPanes != null)
    {
      contentPaneIndexAdjustment = new int[contentPanes.length];
    }
  }        
     

  protected void updateContentPaneInfoIndexAdjustment(List list)
  { 
    int index = 0;  
    int listSize = list.size();

    for (int  j = 0; j < contentPanes.length; j++)
    { 
      contentPaneIndexAdjustment[j] = index;
      while (index < listSize)
      {              
        Object model = list.get(index);
        if (getContentPane(model) != j)
        {
          break;
        }                          
        index++;
      }
    }
  }
                            

  protected void refreshChildren()
  {                        
    if (contentPanes != null)
    {
      List sortedList = sortByContentPane(getModelChildren());
      updateContentPaneInfoIndexAdjustment(sortedList);
    }
    super.refreshChildren();
  }
                            

  protected void addChildVisual(EditPart childEditPart, int index)
  {                
    if (contentPanes != null)
    {
      int j = getContentPane(childEditPart.getModel());
      if (contentPaneIndexAdjustment.length > j)
      {
        int adjustedIndex = index - contentPaneIndexAdjustment[j];
        if (adjustedIndex >= 0)
        {
  	      IFigure child = ((GraphicalEditPart)childEditPart).getFigure();
  	      contentPanes[j].add(child, adjustedIndex);
        }
      }
    }  
    else
    {
      super.addChildVisual(childEditPart, index);
    }
  }

  
  protected void removeChildVisual(EditPart childEditPart)
  {                         
    if (contentPanes != null)
    {
      int j = getContentPane(childEditPart.getModel());
      IFigure child = ((GraphicalEditPart)childEditPart).getFigure();
      contentPanes[j].remove(child);  
    }    
    else
    {
      super.removeChildVisual(childEditPart);
    }
  }

  protected void reorderChild(EditPart editpart, int index)
  {
    removeChildVisual(editpart);
    List children = getChildren();
    children.remove(editpart);
    children.add(index, editpart);
    addChildVisual(editpart, index);
  }
}     