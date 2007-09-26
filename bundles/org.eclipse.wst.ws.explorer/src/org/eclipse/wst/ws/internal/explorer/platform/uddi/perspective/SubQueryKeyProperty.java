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

package org.eclipse.wst.ws.internal.explorer.platform.uddi.perspective;

public class SubQueryKeyProperty
{
  private String subQueryKey_;

  public SubQueryKeyProperty()
  {
    subQueryKey_ = "";
  }

  public final void setSubQueryKey(String subQueryKey)
  {
    subQueryKey_ = subQueryKey;
  }

  public final String getSubQueryKey()
  {
    return subQueryKey_;
  }
}
