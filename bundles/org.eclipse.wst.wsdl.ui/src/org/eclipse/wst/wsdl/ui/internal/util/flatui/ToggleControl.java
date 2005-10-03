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
package org.eclipse.wst.wsdl.ui.internal.util.flatui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.accessibility.ACC;
import org.eclipse.swt.accessibility.AccessibleAdapter;
import org.eclipse.swt.accessibility.AccessibleControlAdapter;
import org.eclipse.swt.accessibility.AccessibleControlEvent;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TypedListener;

public class ToggleControl extends Canvas {
  private boolean fCollapsed;
  private Color fDecorationColor;
  private Color fActiveColor;
  private Cursor fActiveCursor;
  private boolean fHasFocus;
  private boolean fHover = false;
  private static final int MARGIN_WIDTH = 2;
  private static final int MARGIN_HEIGHT = 2;

  private static final int DEFAULT_HEIGHT = 8;
  private static final int[] EXPANDED_POINTS = { 0, 2, 8, 2, 4, 6 }; 
  private static final int[] COLLAPSED_POINTS = { 2, 0, 2, 8, 6, 4 }; 
  private int fHeight = DEFAULT_HEIGHT;  

  /* accessibility */
  private String fName;
  private String fDescription;

  public ToggleControl(Composite parent, int style) {
    super(parent, style);
    initAccessible();

    addPaintListener(new PaintListener() {
      public void paintControl(PaintEvent e) {
        paint(e);
      }
    });
    addMouseListener(new MouseAdapter() {
      public void mouseDown(MouseEvent e) {
        notifyListeners(SWT.Selection);
      }
    });
    addKeyListener(new KeyAdapter() {
      public void keyPressed(KeyEvent e) {
        if (e.character == '\r' || e.character == ' ') {
          // Activation
          notifyListeners(SWT.Selection);
        }
      }
    });
    addListener(SWT.Traverse, new Listener() {
      public void handleEvent(Event e) {
        if (e.detail != SWT.TRAVERSE_RETURN)
          e.doit = true;
      }
    });
    addFocusListener(new FocusListener() {
      public void focusGained(FocusEvent e) {
        if (!fHasFocus) {
          fHasFocus = true;
          redraw();
        }
      }
      public void focusLost(FocusEvent e) {
        if (fHasFocus) {
          fHasFocus = false;
          redraw();
        }
      }
    });

    addSelectionListener(new SelectionAdapter() {
      public void widgetSelected(SelectionEvent e) {
        fCollapsed = !fCollapsed;
        redraw();
      }
    });

    addMouseTrackListener(new MouseTrackAdapter() {
      public void mouseEnter(MouseEvent e) {
        fHover = true;
        if (fActiveCursor != null)
          setCursor(fActiveCursor);
        redraw();
      }
      public void mouseExit(MouseEvent e) {
        fHover = false;
        if (fActiveCursor != null)
          setCursor(null);
        redraw();
      }
    });
  }

  public void addSelectionListener(SelectionListener listener) {
    checkWidget();
    if (listener != null)
      addListener(SWT.Selection, new TypedListener(listener));
  }

  public void setDecorationColor(Color decorationColor) {
    this.fDecorationColor = decorationColor;
  }

  public Color getDecorationColor() {
    return fDecorationColor;
  }

  public void setActiveDecorationColor(Color activeColor) {
    this.fActiveColor = activeColor;
  }

  public void removeSelectionListener(SelectionListener listener) {
    checkWidget();
    if (listener != null)
      removeListener(SWT.Selection, listener);
  }

  public void setActiveCursor(Cursor activeCursor) {
    this.fActiveCursor = activeCursor;
  }

  public Color getActiveDecorationColor() {
    return fActiveColor;
  }

  public Point computeSize(int wHint, int hHint, boolean changed) {

    return new Point((wHint != SWT.DEFAULT ? wHint : fHeight + 2 * MARGIN_WIDTH), (hHint != SWT.DEFAULT ? hHint : fHeight + 2 * MARGIN_HEIGHT));
  }

  protected void paint(PaintEvent e) {
    GC gc = e.gc;
    Point size = getSize();
    gc.setFont(getFont());
    paint(gc);
    if (fHasFocus) {
      gc.setForeground(getForeground());
      gc.drawFocus(0, 0, size.x, size.y);
    }
  }

  /*
   * @see SelectableControl#paint(GC)
   */
  protected void paint(GC gc) {
    if (fHover && fActiveColor != null)
      gc.setBackground(fActiveColor);
    else if (fDecorationColor != null)
      gc.setBackground(fDecorationColor);
    else
      gc.setBackground(getForeground());
    Point size = getSize();
    gc.fillPolygon(translate((fCollapsed ? COLLAPSED_POINTS : EXPANDED_POINTS), ((size.x - fHeight) / 2), ((size.y - fHeight) / 2)));
    gc.setBackground(getBackground());
  }

  private int[] translate(int[] data, int x, int y) {
    int[] target = new int[data.length];
    for (int i = 0; i < data.length; i += 2) {
      target[i] = data[i]*fHeight/DEFAULT_HEIGHT + x;
    }
    for (int i = 1; i < data.length; i += 2) {
      target[i] = data[i]*fHeight/DEFAULT_HEIGHT + y;
    }
    return target;
  }
  
  public void setHeight(int y) {
    fHeight = y;
  }

  private void notifyListeners(int eventType) {
    Event event = new Event();
    event.type = eventType;
    event.widget = this;
    notifyListeners(eventType, event);
  }

  public boolean getSelection() {
    return fCollapsed;
  }

  public void setSelection(boolean selection) {
    this.fCollapsed = selection;
  }

  public void setName(String name) {
    fName = name;
  }

  public void setDescription(String description) {
    fDescription = description;
  }

  private void initAccessible() {
    getAccessible().addAccessibleListener(new AccessibleAdapter() {

      public void getName(AccessibleEvent e) {
        e.result = fName;
      }

      public void getDescription(AccessibleEvent e) {
        e.result = fDescription;
      }
    });

    getAccessible().addAccessibleControlListener(new AccessibleControlAdapter() {

      public void getLocation(AccessibleControlEvent e) {
        Rectangle location = getBounds();
        Point pt = toDisplay(new Point(location.x, location.y));
        e.x = pt.x;
        e.y = pt.y;
        e.width = location.width;
        e.height = location.height;
      }

      public void getChildCount(AccessibleControlEvent e) {
        e.detail = 0;
      }

      public void getRole(AccessibleControlEvent e) {
        e.detail = ACC.ROLE_TREE;
      }

      public void getState(AccessibleControlEvent e) {
        e.detail = fCollapsed ? ACC.STATE_COLLAPSED : ACC.STATE_EXPANDED;
      }

    });
  }

}
