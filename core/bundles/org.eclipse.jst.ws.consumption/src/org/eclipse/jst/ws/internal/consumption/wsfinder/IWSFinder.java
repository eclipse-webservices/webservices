/*******************************************************************************
 * Copyright (c) 2002, 2004 IBM Corporation and others.
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

package org.eclipse.jst.ws.internal.consumption.wsfinder;

import java.util.List;

public interface IWSFinder
{
  public String getID();
  public void setID(String id);

  public String getName();
  public void setName(String name);

  public String getDescription();
  public void setDescription(String desc);

  public List find();
}
