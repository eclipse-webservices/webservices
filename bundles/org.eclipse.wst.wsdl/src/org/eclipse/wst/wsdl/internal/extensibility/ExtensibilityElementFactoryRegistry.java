package org.eclipse.wst.wsdl.internal.extensibility;

import java.util.HashMap;

import org.eclipse.wst.wsdl.internal.util.ExtensibilityElementFactory;


public class ExtensibilityElementFactoryRegistry {
  protected HashMap map = new HashMap();
  
  public void put(String namespace, ExtensibilityElementFactoryDescriptor descriptor)
  {
    map.put(namespace, descriptor);
  }
  
  public ExtensibilityElementFactory getExtensibilityElementFactory(String namespace)
  {
    ExtensibilityElementFactory result = null;
    ExtensibilityElementFactoryDescriptor descriptor = (ExtensibilityElementFactoryDescriptor)map.get(namespace);
    if (descriptor != null)
    {
      result = descriptor.getExtensiblityElementFactory();
    }
    return result;
  }
}
