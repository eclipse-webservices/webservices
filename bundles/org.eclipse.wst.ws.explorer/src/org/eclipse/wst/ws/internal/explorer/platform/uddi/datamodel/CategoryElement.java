/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.ws.internal.explorer.platform.uddi.datamodel;

import org.eclipse.wst.ws.internal.datamodel.Model;
import org.uddi4j.util.KeyedReference;

public class CategoryElement extends AbstractUDDIElement
{
  private KeyedReference category_;

  public CategoryElement(String name,KeyedReference category,Model model)
  {
    super(name,model);
    category_ = category;
    saveCategoryProperties();
  }

  private final void saveCategoryProperties()
  {
    if (category_ != null)
    {
      String keyName = category_.getKeyName();
      if (keyName != null)
        setName(keyName);
    }
  }

  public final KeyedReference getCategory()
  {
    return category_;
  }

  public final String getNameForTree()
  {
    StringBuffer nameForTree = new StringBuffer("[");
    nameForTree.append(category_.getKeyValue()).append("] ").append(category_.getKeyName());
    return nameForTree.toString();
  }

  public final void updateCategory(String keyName,String keyValue,String tModelKey)
  {
    category_.setKeyName(keyName);
    category_.setKeyValue(keyValue);
    category_.setTModelKey(tModelKey);
    saveCategoryProperties();
  }
}
