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
package org.eclipse.wst.wsdl.ui.internal.gef.util.figures;
            
import org.eclipse.draw2d.*; 
import java.util.*; 

public interface IGraphNodeFigure extends IFigure
{                       
  public static final int UP_CONNECTION = 1; 
  public static final int DOWN_CONNECTION = 2;
  public static final int LEFT_CONNECTION = 3;
  public static final int RIGHT_CONNECTION = 4;

  public IFigure getSelectionFigure();
  public IFigure getConnectionFigure();
  public List getConnectedFigures(int type);
  public int getConnectionType();      
  public void addConnectedFigure(IGraphNodeFigure figure);
  public void removeConnectedFigure(IGraphNodeFigure figure);
}