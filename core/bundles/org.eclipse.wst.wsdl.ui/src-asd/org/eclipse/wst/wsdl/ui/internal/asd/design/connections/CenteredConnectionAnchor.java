/*******************************************************************************
 * Copyright (c) 2001, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.ui.internal.asd.design.connections;

import org.eclipse.draw2d.AbstractConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

public class CenteredConnectionAnchor extends AbstractConnectionAnchor
{
  public static final int TOP = 0;
  public static final int BOTTOM = 1;
  public static final int LEFT = 2;
  public static final int RIGHT = 3;
  
  // These two are custom for rectangles with header blocks
  public static final int HEADER_LEFT = 4;
  public static final int HEADER_RIGHT = 5;

  private int location;
  private int inset;
  private int offset = 0;
  
  public CenteredConnectionAnchor(IFigure owner, int location, int inset) {
    super(owner);
    this.location = location;
    this.inset = inset;
  }
  
  public CenteredConnectionAnchor(IFigure owner, int location, int inset, int offset) {
    this(owner, location, inset);
    this.offset = offset;
  }
  
  public Point getLocation(Point reference) {
    Rectangle r = getOwner().getBounds();
    int x, y;
    switch (location) {
      case TOP:
        x = r.right() - r.width / 2 + offset;
        y = r.y + inset;
        break;
      case BOTTOM:
        x = r.right() - r.width / 2 + offset;
        y = r.bottom() - inset;
        break;
      case LEFT:
        x = r.x + inset;
        y = r.bottom() - r.height / 2 + offset;
        break;
      case RIGHT:
        x = r.right() - inset;
        y = r.bottom() - r.height / 2 + offset;
        break;
      case HEADER_LEFT:
        x = r.x + inset;
        y = r.y + offset;
        break;
      case HEADER_RIGHT:
        x = r.right() - inset;
        y = r.y + offset;
        break;        
        
      default:
        // Something went wrong. Attach the anchor to the middle
        x = r.right() - r.width / 2;
        y = r.bottom() - r.height / 2;
    }
    Point p = new Point(x,y);

    getOwner().translateToAbsolute(p);
    return p;
  }
  
  public Point getReferencePoint() {
    return getLocation(null);
  }

}
