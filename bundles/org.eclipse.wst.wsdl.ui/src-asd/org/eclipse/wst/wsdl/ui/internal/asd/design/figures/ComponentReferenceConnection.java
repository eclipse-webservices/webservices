/*******************************************************************************
 * Copyright (c) 2001, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.ui.internal.asd.design.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.ConnectionRouter;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.swt.graphics.Color;
import org.eclipse.wst.wsdl.ui.internal.asd.design.DesignViewGraphicsConstants;

public class ComponentReferenceConnection extends PolylineConnection
{
  protected boolean highlight = false;
  protected static final Color activeConnection = ColorConstants.black;
  protected static final Color inactiveConnection = new Color(null, 198, 195, 198);

  /**
   * Default constructor
   */
  public ComponentReferenceConnection()
  {
    super();
    setTargetDecoration(new PolygonDecoration());
  }

  public void setConnectionRouter(ConnectionRouter cr)
  {
    if (cr != null && getConnectionRouter() != null)// TODO:.... && !(getConnectionRouter() instanceof BOManhattanConnectionRouter))
      super.setConnectionRouter(cr);
  }

  /**
   * @return Returns the current highlight status.
   */
  public boolean isHighlighted()
  {
    return highlight;
  }

  /**
   * @param highlight
   *          The highlight to set.
   */
  public void setHighlight(boolean highlight)
  {
    this.highlight = highlight;
    setForegroundColor(highlight ? DesignViewGraphicsConstants.defaultForegroundColor : inactiveConnection);
    setOpaque(highlight);
  }
}
