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
package org.eclipse.wst.wsdl.internal.extensibility;

import org.eclipse.wst.wsdl.util.ExtensibilityElementFactory;

public class ExtensibilityElementFactoryDescriptor
{
  private final static String CLASS_LOADING_ERROR = "CLASS_LOADING_ERROR";
  
  protected ClassLoader classLoader;
  protected String namespace;
  protected String className;  
  protected Object factory;

  public ExtensibilityElementFactoryDescriptor(String className, String namespace, ClassLoader classLoader)
  {
    this.classLoader = classLoader;
    this.className = className;
    this.namespace = namespace;
  }

  public ExtensibilityElementFactory getExtensiblityElementFactory()
  {
    if (factory == null)
    {
      try
      {
        Class theClass = classLoader != null ? classLoader.loadClass(className) : Class.forName(className);
        factory = (ExtensibilityElementFactory)theClass.newInstance();
      }
      catch (Exception e)
      {
        factory = CLASS_LOADING_ERROR;
        e.printStackTrace();
      }
    }
    return factory != CLASS_LOADING_ERROR ? (ExtensibilityElementFactory)factory : null;
  }
  
  public void setExtensiblityElementFactory(ExtensibilityElementFactory factory)
  {
    this.factory = factory;
  }
}