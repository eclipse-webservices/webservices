// $Id: WSDLDefinitionFactory.java,v 1.1 2004/11/01 23:07:29 csalter Exp $
package org.eclipse.wst.wsdl.internal.util;

import javax.wsdl.WSDLException;
import javax.wsdl.xml.WSDLReader;

import org.eclipse.wst.wsdl.internal.impl.WSDLFactoryImpl;
import org.eclipse.wst.wsdl.internal.impl.WSDLPackageImpl;

import com.ibm.wsdl.xml.WSDLReaderImpl;

public class WSDLDefinitionFactory extends com.ibm.wsdl.factory.WSDLFactoryImpl
{

  private static WSDLDefinitionFactory instance = null;

  public WSDLDefinitionFactory()
  {
    // Make sure the WSDL package is initialized.
    WSDLPackageImpl.init();
  }

  /**
   * Create a new instance of Definition.
   */
  public javax.wsdl.Definition newDefinition()
  {
    javax.wsdl.Definition definition = WSDLFactoryImpl.eINSTANCE.createDefinition();
    return definition;
  }

  /**
   * Returns a singleton instance of this factory
   */
  public static javax.wsdl.factory.WSDLFactory getInstance() throws WSDLException
  {
    if (instance == null)
      instance = new WSDLDefinitionFactory();
    return instance;
  }

  /**
   * Creates a WSDLReader.
   */
  public WSDLReader newWSDLReader()
  {
    WSDLReader reader = new WSDLReaderImpl();
    reader.setFactoryImplName(getClass().getName());
    return reader;
  }

}
