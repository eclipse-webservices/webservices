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
package org.eclipse.wst.wsdl.ui.internal.util.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.TableTree;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;

public class BorderPainter implements PaintListener
{
  Color backgroundColor, foregroundColor;
  static Color borderColor;

  public BorderPainter()
  {
    Display display = Display.getCurrent();
    if (borderColor == null)
    {
      borderColor = new Color(Display.getCurrent(), 195, 191, 179);  	
    }
    backgroundColor = display.getSystemColor(SWT.COLOR_LIST_BACKGROUND);
    foregroundColor = display.getSystemColor(SWT.COLOR_LIST_FOREGROUND);	
  }
	
  public void paintControl(PaintEvent event)
  {
    Composite composite = (Composite) event.widget;
    Control[] children = composite.getChildren();
    for (int i = 0; i < children.length; i++)
    {
      Control c = children[i];
      if (c.isEnabled()==false)
      {
        if (c instanceof Text
          || c instanceof Canvas
          || c instanceof List
          || c instanceof CCombo)
        {
          Rectangle b = c.getBounds();
          GC gc = event.gc;
          gc.setForeground(backgroundColor);
//        gc.drawRectangle(b.x - 1, b.y - 1, b.width + 1, b.height + 1);
          gc.setForeground(borderColor);
          gc.drawRectangle(b.x - 2, b.y - 2, b.width + 3, b.height + 3);
        }
        continue;
      }
      if (c instanceof Text
//        || c instanceof Canvas
        || c instanceof List
        || c instanceof CCombo)
      {
        Rectangle b = c.getBounds();
        GC gc = event.gc;
        gc.setForeground(backgroundColor);
        gc.drawRectangle(b.x - 1, b.y - 1, b.width + 1, b.height + 1);
        gc.setForeground(foregroundColor);
        gc.drawRectangle(b.x - 2, b.y - 2, b.width + 3, b.height + 3);
      }
      else if (c instanceof Table
        || c instanceof Tree
        || c instanceof TableTree) {
        Rectangle b = c.getBounds();
        GC gc = event.gc;
        gc.setForeground(foregroundColor);
        //gc.drawRectangle(b.x - 2, b.y - 2, b.width + 3, b.height + 3);
        gc.drawRectangle(b.x-1, b.y-1, b.width+2, b.height+2);
      }
    }
  }
}

