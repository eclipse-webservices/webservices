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

import org.eclipse.wst.wsdl.ui.internal.contentgenerator.ui.ContentGeneratorOptionsPage;

public class ContentGeneratorExtension
{
  protected String name;
  protected String namespace;
  protected ClassLoader classLoader;
  protected String contentGeneratorClassName;
  protected String portOptionsPageClassName;
  protected String bindingOptionsPageClassName;

  public ContentGeneratorExtension(String name, String contentGeneratorClassName)
  {
    this.name = name;
    this.contentGeneratorClassName = contentGeneratorClassName;
  }

  public String getName()
  {
    return name;
  }

  public void setClassLoader(ClassLoader classLoader)
  {
    this.classLoader = classLoader;
  }

  public ContentGenerator createBindingContentGenerator()
  {
    ContentGenerator result = null;
    if (contentGeneratorClassName != null)
    {
      try
      {
        Class theClass = classLoader != null ? classLoader.loadClass(contentGeneratorClassName) : Class.forName(contentGeneratorClassName);
        result = (ContentGenerator) theClass.newInstance();
      }
      catch (Exception e)
      {
      }
    }
    return result;
  }

  public ContentGeneratorOptionsPage createBindingContentGeneratorOptionsPage()
  {
    ContentGeneratorOptionsPage result = null;
    if (bindingOptionsPageClassName != null)
    {
      try
      {
        Class theClass = classLoader != null ? classLoader.loadClass(bindingOptionsPageClassName) : Class.forName(bindingOptionsPageClassName);
        result = (ContentGeneratorOptionsPage) theClass.newInstance();
      }
      catch (Exception e)
      {
      }
    }
    return result;
  }

  public ContentGeneratorOptionsPage createPortContentGeneratorOptionsPage()
  {
    ContentGeneratorOptionsPage result = null;
    if (portOptionsPageClassName != null)
    {
      try
      {
        Class theClass = classLoader != null ? classLoader.loadClass(portOptionsPageClassName) : Class.forName(portOptionsPageClassName);
        result = (ContentGeneratorOptionsPage) theClass.newInstance();
      }
      catch (Exception e)
      {
      }
    }
    return result;
  }

  public void setBindingOptionsPageClassName(String string)
  {
    bindingOptionsPageClassName = string;
  }

  public void setPortOptionsPageClassName(String string)
  {
    portOptionsPageClassName = string;
  }

  public String getNamespace()
  {
    return namespace;
  }

  public void setNamespace(String string)
  {
    namespace = string;
  }
}