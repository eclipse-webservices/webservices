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
package org.eclipse.wst.wsdl.ui.internal.graph;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Display;

/**
 * A collection of color and font related constants.
 */
public interface GraphicsConstants 
{                          
  public final static Display display = Display.getDefault(); 
                                                 
  public final static Color groupBorderColor        = new Color(null, 118, 134, 164);
  public final static Color groupHeaderColor        = new Color(null, 232, 240, 248);

  public final static Color elementBorderColor      = new Color(null, 120, 152, 184);
  public final static Color elementBackgroundColor  = new Color(null, 232, 240, 248);  
  public final static Color elementLabelColor       = new Color(null,  80, 102, 144);
  public final static Color readOnlyBorderColor     = new Color(null, 164, 164, 164); 
  public final static Color red                     = new Color(null, 255,   0,   0); 

  public final static Color readOnlyBackgroundColor = ColorConstants.white;

  public final static Font  smallBoldFont           = new Font(Display.getCurrent(), "Tahoma", 8, SWT.BOLD);
  public final static Font  mediumFont              = new Font(Display.getCurrent(), "Tahoma", 10, SWT.NONE);
  public final static Font  mediumBoldFont          = new Font(Display.getCurrent(), "Tahoma", 10, SWT.BOLD); 
}