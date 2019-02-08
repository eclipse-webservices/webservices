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

import java.util.Enumeration;
import org.eclipse.wst.ws.internal.datamodel.Model;
import org.eclipse.wst.ws.internal.explorer.platform.constants.ModelConstants;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.constants.UDDIModelConstants;

public class PublishedItemsElement extends AbstractUDDIElement
{
  public PublishedItemsElement(int type,String name,Model model)
  {
    super(name,model);
    setPropertyAsString(ModelConstants.REL_CHILDREN,UDDIModelConstants.REL_PUBLISHED_ITEMS);
    setPropertyAsString(UDDIModelConstants.QUERY_TYPE,String.valueOf(type));
  }

  public final int getType()
  {
    return Integer.parseInt(getPropertyAsString(UDDIModelConstants.QUERY_TYPE));
  }

  public Enumeration getItems()
  {
    return getElements(UDDIModelConstants.REL_PUBLISHED_ITEMS);
  }
}
