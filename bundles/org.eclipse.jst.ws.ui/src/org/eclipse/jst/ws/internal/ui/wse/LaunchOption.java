/*******************************************************************************
 * Copyright (c) 2002, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.jst.ws.internal.ui.wse;

public class LaunchOption
{
  private String key_;
  private String option_;

  public LaunchOption(String key, String option)
  {
    key_ = key;
    option_ = option;
  }

  public String getKey()
  {
    return key_;
  }

  public String getOption()
  {
    return option_;
  }
}
