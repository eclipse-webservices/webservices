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
package org.eclipse.wst.wsdl.ui.internal.contentgenerator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ContentGeneratorExtensionRegistry
{
  protected List staticExtensionList = new ArrayList();
  protected List dynamicExtensionList = new ArrayList();

  public void add(ContentGeneratorExtension extension)
  {
    staticExtensionList.add(extension);
  }

  public void add(ContentGeneratorProviderExtension extension)
  {
    dynamicExtensionList.add(extension);
  }

  public List getRegisteredExtensionList()
  {
    List list = new ArrayList();
    list.addAll(staticExtensionList);
    for (Iterator i = dynamicExtensionList.iterator(); i.hasNext();)
    {
      try
      {
        ContentGeneratorProviderExtension dynamicExtension = (ContentGeneratorProviderExtension) i.next();
        ContentGeneratorProvider provider = dynamicExtension.createContentGeneratorProvider();
        List dynamicallyContributedExtensions = provider.getContentGeneratorExtensions();
        if (dynamicallyContributedExtensions != null)
        {
          list.addAll(dynamicallyContributedExtensions);
        }
      }
      catch (Exception e)
      {
      }
    }
    return list;
  }

  public List getBindingExtensionNames()
  {
    List list = new ArrayList();
    for (Iterator i = getRegisteredExtensionList().iterator(); i.hasNext();)
    {
      ContentGeneratorExtension extension = (ContentGeneratorExtension) i.next();
      list.add(extension.getName());
    }
    return list;
  }

  public ContentGeneratorExtension getExtensionForNamespace(String namespace)
  {
    ContentGeneratorExtension result = null;
    if (namespace != null)
    {
      for (Iterator i = getRegisteredExtensionList().iterator(); i.hasNext();)
      {
        ContentGeneratorExtension extension = (ContentGeneratorExtension) i.next();
        if (namespace.equals(extension.getNamespace()))
        {
          result = extension;
          break;
        }
      }
    }
    return result;
  }

  public ContentGeneratorExtension getExtensionForName(String name)
  {
    ContentGeneratorExtension result = null;
    if (name != null)
    {
      for (Iterator i = getRegisteredExtensionList().iterator(); i.hasNext();)
      {
        ContentGeneratorExtension extension = (ContentGeneratorExtension) i.next();
        if (name.equals(extension.getName()))
        {
          result = extension;
          break;
        }
      }
    }
    return result;
  }

  /*
   * @deprecated - use getExtensionForName
   */
  public ContentGeneratorExtension getContentGeneratorExtension(String name)
  {
    return getExtensionForName(name);
  }
}