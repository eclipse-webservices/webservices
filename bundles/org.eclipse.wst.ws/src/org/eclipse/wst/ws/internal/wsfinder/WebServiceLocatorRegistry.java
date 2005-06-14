package org.eclipse.wst.ws.internal.wsfinder;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.wst.ws.internal.ext.WebServiceExtension;
import org.eclipse.wst.ws.internal.ext.WebServiceExtensionRegistryImpl;

public class WebServiceLocatorRegistry extends WebServiceExtensionRegistryImpl {

	 // Copyright
	  public static final String copyright = "(c) Copyright IBM Corporation 2000, 2002.";
	  /*
	   * This is a singleton becasue it was decided that the memory foot print 
	   * is not as expensive as the time taken retrieving the data
	   * 
	   */
	  
	  
	  private static WebServiceLocatorRegistry wslr;
	  
	  public static WebServiceLocatorRegistry getInstance()
	  {
	  	if(wslr == null) wslr = new WebServiceLocatorRegistry();
	    return wslr;
	  }
	  
	  private WebServiceLocatorRegistry()
	  {
	    super();
	  }
	  
	  /**
	  * Children registries will have different extension types 
	  * @return WebserviceExtension holds a config elem
	  * for that extension capable of creating an executable file
	  */
	  public WebServiceExtension createWebServiceExtension(IConfigurationElement configElement)
	  {
	    return new WebServiceLocatorExtension(configElement);
	  }

	  /**
	  * Children must implement how they get the IConfigurationElement[] 
	  * @return IConfigurationElement[] an array of elements particular to that
	  * extension
	  */
	  public IConfigurationElement[] getConfigElements()
	  {
	    IExtensionRegistry reg = Platform.getExtensionRegistry();
	    IConfigurationElement[] config = reg.getConfigurationElementsFor(
	                                     "org.eclipse.wst.ws",
	                                     "locator");
	    return config;    
	  }

}
