package org.eclipse.wst.wsdl;

import org.eclipse.core.runtime.IPluginDescriptor;
import org.eclipse.emf.common.EMFPlugin.EclipsePlugin;

/**
   * The actual implementation of the Eclipse <b>Plugin</b>.
   */
  public class WSDLPluginImplementation extends EclipsePlugin
  {
    /**
     * Creates an instance.
     * @param descriptor the description of the plugin.
     */
    public WSDLPluginImplementation(IPluginDescriptor descriptor)
    {
      super(descriptor);

      // Remember the static instance.
      //
      WSDLPlugin.plugin = this;
    }
  }