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
package org.eclipse.wst.wsdl.ui.internal.extension;

import java.util.HashMap;

public class NSKeyedExtensionRegistry
{
  protected HashMap map = new HashMap();
  private static final String PLUGIN_LOADING_ERROR = "PLUGIN_LOADING_ERROR";

  public NSKeyedExtensionRegistry()
  {
  }

  public void put(String namespaceURI, String className, ClassLoader classLoader)
  {
    ExtensionInfo info = (ExtensionInfo) map.get(namespaceURI);
    if (info == null)
    {
      info = new SinglePropertyExtensionInfo(className, classLoader);
      map.put(namespaceURI, info);
    }
  }

  public void put(String namespaceURI, HashMap propertyToClassNameMap, ClassLoader classLoader)
  {
    ExtensionInfo info = (ExtensionInfo) map.get(namespaceURI);
    if (info == null)
    {
      info = new MultiPropertyExtensionInfo(propertyToClassNameMap, classLoader);
      map.put(namespaceURI, info);
    }
  }

  /**
   * 
   * @deprecated
   */
  public Object get(String namespaceURI)
  {
    ExtensionInfo info = (ExtensionInfo) map.get(namespaceURI);
    return info != null ? info.getObject("") : null;
  }

  public Object getProperty(String namespaceURI, String property)
  {
    ExtensionInfo info = (ExtensionInfo) map.get(namespaceURI);
    return info != null ? info.getObject(property) : null;
  }

  private abstract class ExtensionInfo
  {
    protected ClassLoader classLoader;

    public ExtensionInfo(ClassLoader classLoader)
    {
      this.classLoader = classLoader;
    }

    public abstract Object getObject(String property);
  }

  private class SinglePropertyExtensionInfo extends ExtensionInfo
  {
    protected String className;
    protected Object object;
    protected boolean error;

    public SinglePropertyExtensionInfo(String className, ClassLoader classLoader)
    {
      super(classLoader);
      this.className = className;
    }

    public Object getObject(String property)
    {
      if (object == null)
      {
        try
        {
          Class theClass = classLoader != null ? classLoader.loadClass(className) : Class.forName(className);
          object = theClass.newInstance();
        }
        catch (Exception e)
        {
          object = PLUGIN_LOADING_ERROR;
          e.printStackTrace();
        }
      }
      return object != PLUGIN_LOADING_ERROR ? object : null;
    }
  }

  private class MultiPropertyExtensionInfo extends ExtensionInfo
  {
    protected HashMap propertyToClassNameTable;
    protected HashMap propertyToObjectTable = new HashMap();

    public MultiPropertyExtensionInfo(HashMap propertToClassNameTable, ClassLoader classLoader)
    {
      super(classLoader);
      this.propertyToClassNameTable = propertToClassNameTable;
    }

    public Object getObject(String property)
    {
      Object result = null;
      String className = (String) propertyToClassNameTable.get(property);
      if (className != null)
      {
        result = propertyToObjectTable.get(property);
        if (result == null)
        {
          try
          {
            Class theClass = classLoader != null ? classLoader.loadClass(className) : Class.forName(className);
            result = theClass.newInstance();
            propertyToObjectTable.put(property, result);
          }
          catch (Exception e)
          {
            propertyToObjectTable.put(property, PLUGIN_LOADING_ERROR);
            result = PLUGIN_LOADING_ERROR;
            e.printStackTrace();
          }
        }
      }
	  return result != PLUGIN_LOADING_ERROR ? result : null;      
    }
  }
}