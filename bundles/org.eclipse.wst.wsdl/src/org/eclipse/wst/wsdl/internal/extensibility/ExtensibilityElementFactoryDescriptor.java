/*
 * Created on Apr 23, 2004
 * 
 * @todo To change the template for this generated file go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
package org.eclipse.wst.wsdl.internal.extensibility;

import org.eclipse.wst.wsdl.internal.util.ExtensibilityElementFactory;

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
}