package org.eclipse.wst.wsdl.internal.impl.wsdl4j;

import javax.wsdl.Definition;
import javax.wsdl.extensions.ExtensionRegistry;
import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLReader;
import javax.wsdl.xml.WSDLWriter;

public final class WSDLFactoryImpl extends WSDLFactory
{
  
  //public static WSDLFactory newInstance() throws WSDLException
  //{
  //  return new WSDLFactoryImpl();
  //}
  
  public WSDLFactoryImpl()
  {
  }
  
  public Definition newDefinition()
  {
    return org.eclipse.wst.wsdl.internal.impl.WSDLFactoryImpl.eINSTANCE.createDefinition();
  }
  
  public ExtensionRegistry newPopulatedExtensionRegistry()
  {
    return null;
  }
  
  public WSDLReader newWSDLReader()
  {
    WSDLReader reader = new WSDLReaderImpl();
    reader.setFactoryImplName(getClass().getName());
    return reader;
  }
  
  public WSDLWriter newWSDLWriter()
  {
    return new WSDLWriterImpl();
  }
}
