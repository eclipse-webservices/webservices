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

public class ContentGeneratorProviderExtension
{
  protected String className;
  protected ClassLoader classLoader;

  public ContentGeneratorProviderExtension(String className)
  {
    this.className = className;
  }

  public void setClassLoader(ClassLoader classLoader)
  {
    this.classLoader = classLoader;
  }

  public ContentGeneratorProvider createContentGeneratorProvider()
  {
	ContentGeneratorProvider result = null;
    if (className != null)
    {
      try
      {
        Class theClass = classLoader != null ? classLoader.loadClass(className) : Class.forName(className);
        result = (ContentGeneratorProvider) theClass.newInstance();
      }
      catch (Exception e)
      {
      }
    }
    return result;
  }
}
