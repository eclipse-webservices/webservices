// $Id: WSDLPlugin.java,v 1.1 2004/11/01 23:07:29 csalter Exp $
package org.eclipse.wst.wsdl;

import javax.wsdl.factory.WSDLFactory;

import org.eclipse.core.runtime.IPluginDescriptor;
import org.eclipse.emf.common.EMFPlugin;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.wst.wsdl.internal.extensibility.ExtensibilityElementFactoryRegistry;
import org.eclipse.wst.wsdl.internal.extensibility.ExtensibilityElementFactoryRegistryReader;
import org.eclipse.wst.wsdl.internal.impl.wsdl4j.WSDLFactoryImpl;
import org.eclipse.wst.wsdl.internal.util.ExtensibilityElementFactory;


/**
 * The <b>Plugin</b> for the model.
 * The WSDL model needs to be able to run within an Eclipse workbench,
 * within a headless Eclipse workspace, or just stand-alone as part 
 * of some other application.
 * To support this, all access is directed to the static methods,
 * which can redirect the service as appopriate to the runtime.
 * During stand-alone invocation no plugin initialization takes place.
 * In this case you will need the resources jar on the class path.
 * @see #getBaseURL
 */
public final class WSDLPlugin extends EMFPlugin 
{
  /**
   * The singleton instance of the plugin.
   */
  public static final WSDLPlugin INSTANCE = new WSDLPlugin();

  /**
   * The one instance of this class.
   */
  static private Implementation plugin;
  
  private ExtensibilityElementFactoryRegistry extensibilityElementFactoryRegistry;

  /**
   * Creates the singleton instance.
   */
  private WSDLPlugin()
  {
    super(new ResourceLocator[] {});
  }

  /*
   * Javadoc copied from base class.
   */
  public ResourceLocator getPluginResourceLocator()
  {
    return plugin;
  }

  /**
   * Returns the singleton instance of the Eclipse plugin.
   * @return the singleton instance.
   */
  public static Implementation getPlugin()
  {
    return plugin;
  }

  /**
   * The actual implementation of the Eclipse <b>Plugin</b>.
   */
  public static class Implementation extends EclipsePlugin
  {
    /**
     * Creates an instance.
     * @param descriptor the description of the plugin.
     */
    public Implementation(IPluginDescriptor descriptor)
    {
      super(descriptor);

      // Remember the static instance.
      //
      plugin = this;
    }
  }
  
  public ExtensibilityElementFactory getExtensibilityElementFactory(String namespace)
  {
    if (extensibilityElementFactoryRegistry == null)
    {
      extensibilityElementFactoryRegistry = new ExtensibilityElementFactoryRegistry();
      new ExtensibilityElementFactoryRegistryReader(extensibilityElementFactoryRegistry).readRegistry();
    }
    return extensibilityElementFactoryRegistry.getExtensibilityElementFactory(namespace);
  }
  
  public WSDLFactory createWSDL4JFactory()
  {
    return new WSDLFactoryImpl();   	
  }
}
