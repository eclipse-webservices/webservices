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
import org.eclipse.wst.ws.internal.explorer.platform.datamodel.ListManager;
import org.uddi4j.datatype.business.BusinessEntity;

public class BusinessElement extends AbstractUDDIElement
{
  private BusinessEntity be_;
  private ListManager publisherAssertions_;
  
  public BusinessElement(BusinessEntity be,Model model)
  {
    super(be.getDefaultNameString(),model);
    be_ = be;
    setKey(be_.getBusinessKey());
    publisherAssertions_ = null;
  }

  public final BusinessEntity getBusinessEntity()
  {
    return be_;
  }
  
  public final void setBusinessEntity(BusinessEntity be)
  {
    be_ = be;
    setKey(be_.getBusinessKey());
  }

  public ListManager getPublisherAssertions()
  {
    return publisherAssertions_;
  }

  public void setPublisherAssertions(ListManager listManager)
  {
    publisherAssertions_ = listManager;
  }
}
