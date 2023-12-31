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

package org.eclipse.wst.ws.internal.explorer.platform.datamodel;

import org.eclipse.wst.ws.internal.datamodel.BasicElement;
import org.eclipse.wst.ws.internal.datamodel.Model;

public class TreeElement extends BasicElement
{
  private String key_;

  public TreeElement(String name,Model model)
  {
    super(name,model);
    key_ = name;
  }

  public void setKey(String key)
  {
    key_ = key;
  }

  public String getKey()
  {
    return key_;
  }

  public boolean equals(TreeElement element)
  {
    if (key_ == null)
      return false;
    return key_.equals(element.getKey());
  }
}
