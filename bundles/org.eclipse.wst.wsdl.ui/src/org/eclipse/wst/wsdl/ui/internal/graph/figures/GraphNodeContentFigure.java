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
import org.eclipse.wst.xsd.ui.internal.gef.util.figures.ContainerFigure;
import org.eclipse.wst.xsd.ui.internal.gef.util.figures.IConnectedFigure;
              

//  ------------------------------
//  | GraphNodeContentFigure     |
//  |                            |
//  | -------------------------  |
//  | | vertical group        |  |
//  | | --------------------- |  |
//  | | | outlined area     | |  |
//  | | | ----------------- | |  |
//  | | | | icon area     | | |  |
//  | | | ----------------- | |  |
//  | | | ----------------- | |  |
//  | | | | inner content | | |  |
//  | | | ----------------- | |  |
//  | | --------------------- |  |
//  | -------------------------  |
//  ------------------------------

public class GraphNodeContentFigure extends ContainerFigure implements IConnectedFigure
{                        
  protected ContainerFigure verticalGroup; 
  protected ContainerFigure outlinedArea;
  protected ContainerFigure iconArea;
  protected ContainerFigure innerContentArea; 
         
  public GraphNodeContentFigure()
  {                                    
    createFigure();                    
  }      
                  
  protected void createFigure()
  {                     
    //getContainerLayout().setHorizontal(true);
    //setLayoutManager(new FillLayout());
    createVerticalGroup(this);
    createOutlinedArea(verticalGroup); 
  }

  protected void createVerticalGroup(IFigure parent)
  {
    verticalGroup = new ContainerFigure();
    verticalGroup.getContainerLayout().setHorizontal(false);
    ///verticalGroup.setLayoutManager(new FillLayout());
    parent.add(verticalGroup);
  }

  protected void createOutlinedArea(IFigure parent)
  { 
    outlinedArea = new ContainerFigure();
    //outlinedArea.setLayoutManager(new FillLayout());
    outlinedArea.getContainerLayout().setHorizontal(false);
    parent.add(outlinedArea);      
     
    iconArea = new ContainerFigure();
    iconArea.getContainerLayout().setHorizontal(true);
    outlinedArea.add(iconArea);

    innerContentArea = new ContainerFigure();
    //innerContentArea.setLayoutManager(new FillLayout());
    innerContentArea.getContainerLayout().setHorizontal(false);
    outlinedArea.add(innerContentArea);  
  }   

  public ContainerFigure getIconArea()
  {
    return iconArea;
  }

  public ContainerFigure getOutlinedArea()
  {
    return outlinedArea;
  }

  public ContainerFigure getInnerContentArea()
  {
    return innerContentArea;
  } 

  public IFigure getConnectionFigure()
  {
    return outlinedArea;
  }
}