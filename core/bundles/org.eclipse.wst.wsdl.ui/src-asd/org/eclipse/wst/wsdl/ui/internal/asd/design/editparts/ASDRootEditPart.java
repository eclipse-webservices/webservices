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
package org.eclipse.wst.wsdl.ui.internal.asd.design.editparts;

import org.eclipse.draw2d.BendpointConnectionRouter;
import org.eclipse.draw2d.ConnectionLayer;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.editparts.ScalableRootEditPart;

public class ASDRootEditPart extends ScalableRootEditPart implements org.eclipse.gef.RootEditPart
{  
  public void activate()
  {
    super.activate();
    // Set up Connection layer with a router, if it doesn't already have one
    ConnectionLayer connectionLayer = (ConnectionLayer) getLayer(LayerConstants.CONNECTION_LAYER);
    if (connectionLayer != null)
    {  
//      connectionLayer.setConnectionRouter(new ManhattanConnectionRouter());
      connectionLayer.setConnectionRouter(new BendpointConnectionRouter());
    }
    refresh();
  }
}
