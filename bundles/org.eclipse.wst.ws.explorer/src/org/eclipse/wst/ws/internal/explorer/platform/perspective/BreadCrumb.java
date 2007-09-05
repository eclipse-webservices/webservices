/*******************************************************************************
 * Copyright (c) 2001, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.ws.internal.explorer.platform.perspective;

// A class used to represent a brower breadcrumb. Each breadcrumb encapsulates
// a navigated URL.
public class BreadCrumb
{
  private int perspectiveId_;
  private String url_;

  public BreadCrumb(int perspectiveId,String url)
  {
    perspectiveId_ = perspectiveId;
    url_ = url;
  }

  /**
  * Get the URL associated with this breadcrumb.
  * @return String The URL.
  */
  public String getURL()
  {
    return url_;
  }

  /**
  * Get the ID of the perspective where this breadcrumb belongs.
  * @return int The perspectiveID as defined in ActionInputs.
  */
  public int getPerspectiveId()
  {
    return perspectiveId_;
  }

  /**
  * Test equality between this breadcrumb and another. Both the URLs and perspective
  * IDs must be equal for two breadcrumbs to be equal.
  * @param BreadCrumb A breadcrumb.
  * @return boolean The result of the equality test.
  */
  public boolean equals(BreadCrumb b)
  {
    return ((url_.equals(b.getURL())) && (perspectiveId_ == b.getPerspectiveId()));
  }
}
