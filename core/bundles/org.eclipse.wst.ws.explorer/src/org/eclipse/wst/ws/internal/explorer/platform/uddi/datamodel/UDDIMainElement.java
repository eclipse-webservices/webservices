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

package org.eclipse.wst.ws.internal.explorer.platform.uddi.datamodel;

import java.util.Hashtable;
import org.eclipse.wst.ws.internal.datamodel.Model;
import org.eclipse.wst.ws.internal.explorer.platform.datamodel.TreeElement;

public class UDDIMainElement extends TreeElement
{
  private Hashtable registryNames_;

  public UDDIMainElement(String name,Model model)
  {
    super(name,model);
    registryNames_ = new Hashtable();
  }

  public final boolean containsRegistryName(String name)
  {
    return (registryNames_.get(name) != null);
  }

  public final void addRegistryName(String name)
  {
    registryNames_.put(name,Boolean.TRUE);
  }

  public final void removeRegistryName(String name)
  {
    registryNames_.remove(name);
  }

  public final void clearRegistryNames()
  {
    registryNames_.clear();
  }
}
