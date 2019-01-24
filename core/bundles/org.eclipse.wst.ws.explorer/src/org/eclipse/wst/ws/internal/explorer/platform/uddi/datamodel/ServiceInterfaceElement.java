/*******************************************************************************
 * Copyright (c) 2004, 2007 IBM Corporation and others.
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

import org.eclipse.wst.ws.internal.datamodel.Model;
import org.uddi4j.datatype.tmodel.TModel;

public class ServiceInterfaceElement extends AbstractUDDIElement
{
  private TModel tModel_;
    
  public ServiceInterfaceElement(TModel tModel,Model model)
  {
    super(tModel.getNameString(),model);
    tModel_ = tModel;
    setKey(tModel.getTModelKey());
  }

  public final TModel getTModel()
  {
    return tModel_;
  }
}
