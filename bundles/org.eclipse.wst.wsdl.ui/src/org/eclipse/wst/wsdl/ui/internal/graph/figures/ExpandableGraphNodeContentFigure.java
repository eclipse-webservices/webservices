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

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.wst.wsdl.ui.internal.gef.util.figures.Interactor;

              
              

//  --------------------------------------------
//  | ExpandableGraphNodeContentFigure         |
//  |                                          |
//  |   ----------------------------------     |
//  |   | verticalGroup                  |     |
//  |   |                                |     |
//  |   | -----------------------------  |     |
//  |   | | horizontalGroup           |  |     |
//  |   | |                           |  |     |
//  |   | | ---------------------     |  |     |
//  |   | | | outlinedArea      |     |  |     |
//  |   | | | ----------------- |     |  |     |
//  |   | | | |[+]iconArea    | |     |  |     |
//  |   | | | ----------------- |     |  |     |
//  |   | | | ----------------- |     |  |     |
//  |   | | | | innerContent  | |     |  |     |
//  |   | | | ----------------- |     |  |     |
//  |   | | ---------------------     |  |     |
//  |   | -----------------------------  |     |
//  |   |                                |     |
//  |   ----------------------------------     |
//  --------------------------------------------

public class ExpandableGraphNodeContentFigure extends GraphNodeContentFigure
{   
  protected Interactor interactor; 
  //protected ContainerFigure occurenceArea;  
 
  public ExpandableGraphNodeContentFigure()
  {
    super();    
  }      
  
  public Interactor getInteractor()
  {
    return interactor;
  } 


  protected void createFigure()
  {   
    //createPreceedingSpace(this);           
    createVerticalGroup(this);
    createOutlinedArea(verticalGroup);   
    createInteractor(iconArea);
    innerContentArea.setBorder(new MarginBorder(0, 10, 0, 0));
    //createOccurenceArea(verticalGroup);   
  }
  
              

  protected void createInteractor(IFigure parent)
  {
    interactor = new Interactor();
    interactor.setBorder(new MarginBorder(0, 0, 0, 5));
    interactor.setForegroundColor(ColorConstants.black);
    interactor.setBackgroundColor(ColorConstants.white); 
    parent.add(interactor);
  } 

                                
  protected void createPreceedingSpace(IFigure parent)
  {
    // create a small space
    RectangleFigure space = new RectangleFigure();
    space.setVisible(false);
    space.setPreferredSize(new Dimension(10, 10));
    parent.add(space);  
  }  
  

  //protected void createOccurenceArea(IFigure parent)
  //{
  //  occurenceArea = new ContainerFigure();             
  //  parent.add(occurenceArea);
  //}                 
}