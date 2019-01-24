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
import org.uddi4j.datatype.service.BusinessService;

public class ServiceElement extends AbstractUDDIElement
{
  private BusinessService bs_;
  
  public ServiceElement(BusinessService bs,Model model)
  {
    super(bs.getDefaultNameString(),model);
    setKey(bs.getServiceKey());
    bs_ = bs;
  }
  
  public final BusinessService getBusinessService()
  {
    return bs_;
  }
}
