/*******************************************************************************
 * Copyright (c) 2008 IBM Corporation and others.
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

import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.wst.wsdl.ui.internal.asd.design.figures.ComponentReferenceConnection;

abstract public class BaseConnectedEditPart extends BaseEditPart
{
  protected ComponentReferenceConnection connectionFigure;
  protected ComponentReferenceConnection connectionFeedbackFigure;

  abstract protected AbstractGraphicalEditPart getConnectionTargetEditPart();

  public ComponentReferenceConnection createConnectionFigure()
  {
    if (connectionFigure == null && shouldDrawConnection())
    {
      AbstractGraphicalEditPart referenceTypePart = getConnectionTargetEditPart();
      if (referenceTypePart != null)
      {
        connectionFigure = new ComponentReferenceConnection();
        refreshConnections();
      }
    }
    return connectionFigure;
  }

  protected boolean shouldDrawConnection()
  {
    AbstractGraphicalEditPart referenceTypePart = getConnectionTargetEditPart();
    return (referenceTypePart != null);
  }

  public void activate()
  {
    super.activate();
    activateConnection();
  }

  public void deactivate()
  {
    super.deactivate();
    deactivateConnection();
  }

  protected void activateConnection()
  {
    if (createConnectionFigure() != null)
    {
      getLayer(LayerConstants.CONNECTION_LAYER).add(connectionFigure);
    }
  }

  protected void deactivateConnection()
  {
    if (connectionFigure != null)
    {
      getLayer(LayerConstants.CONNECTION_LAYER).remove(connectionFigure);
    }
    removeConnectionFeedbackFigure();
  }

  protected void addConnectionFeedbackFigure()
  {
    // remove any preexisting connection feedback figures first
    removeConnectionFeedbackFigure();

    connectionFeedbackFigure = new ComponentReferenceConnection();
    connectionFeedbackFigure.setSourceAnchor(connectionFigure.getSourceAnchor());
    connectionFeedbackFigure.setTargetAnchor(connectionFigure.getTargetAnchor());
    connectionFeedbackFigure.setHighlight(true);
    getLayer(LayerConstants.FEEDBACK_LAYER).add(connectionFeedbackFigure);
  }

  protected void removeConnectionFeedbackFigure()
  {
    if (connectionFeedbackFigure != null)
    {
      connectionFeedbackFigure.setHighlight(false);
      getLayer(LayerConstants.FEEDBACK_LAYER).remove(connectionFeedbackFigure);
      connectionFeedbackFigure = null;
    }
  }

  public void addFeedback()
  {
    if (connectionFigure != null && connectionFigure.isVisible())
    {
      connectionFigure.setHighlight(true);
      addConnectionFeedbackFigure();
    }
  }

  public void removeFeedback()
  {
    removeConnectionFeedbackFigure();
    if (connectionFigure != null)
      connectionFigure.setHighlight(false);
  }
}
