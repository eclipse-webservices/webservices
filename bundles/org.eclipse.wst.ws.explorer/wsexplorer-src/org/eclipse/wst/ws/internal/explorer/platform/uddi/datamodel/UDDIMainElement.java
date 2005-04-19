/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.ws.internal.explorer.platform.uddi.datamodel;

import org.eclipse.wst.ws.internal.datamodel.*;
import org.eclipse.wst.ws.internal.explorer.platform.datamodel.*;

import java.util.*;

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
