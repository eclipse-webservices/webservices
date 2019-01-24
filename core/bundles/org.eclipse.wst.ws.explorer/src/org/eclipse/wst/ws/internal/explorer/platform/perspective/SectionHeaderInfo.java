/*******************************************************************************
 * Copyright (c) 2001, 2007 IBM Corporation and others.
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

package org.eclipse.wst.ws.internal.explorer.platform.perspective;

public class SectionHeaderInfo
{
  private String containerId_;
  private boolean dynamic_;
  private Object otherProperties_;

  public SectionHeaderInfo()
  {
    clear();
  }

  public final void clear()
  {
    containerId_ = "";
    dynamic_ = true;
    otherProperties_ = null;
  }

  public final void setContainerId(String containerId)
  {
    containerId_ = containerId;
  }

  public final String getContainerId()
  {
    return containerId_;
  }

  public final void enableDynamic(boolean isEnabled)
  {
    dynamic_ = isEnabled;
  }

  public final boolean isDynamic()
  {
    return dynamic_;
  }

  public final void setOtherProperties(Object object)
  {
    otherProperties_ = object;
  }

  public final Object getOtherProperties()
  {
    return otherProperties_;
  }
}
