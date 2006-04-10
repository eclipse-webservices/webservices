/*******************************************************************************
 * Copyright (c) 2001, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.ui.internal.asd.design.connections;

import org.eclipse.draw2d.AbstractConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;

public class TopLeftConnectionAnchor extends AbstractConnectionAnchor
{ 
  IFigure label;
  public TopLeftConnectionAnchor(IFigure owner, IFigure label) {
    super(owner);
    this.label = label;
  }
  public Point getLocation(Point reference) {
    int x = getOwner().getBounds().x;
    int y = label.getBounds().y + label.getBounds().height / 2;
    Point p = new Point(x,y);
    getOwner().translateToAbsolute(p);
    return p;
  }
  
  public Point getReferencePoint() {
    return getLocation(null);
  }
}
