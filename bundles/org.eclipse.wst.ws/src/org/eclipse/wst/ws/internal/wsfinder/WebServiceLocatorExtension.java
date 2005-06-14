package org.eclipse.wst.ws.internal.wsfinder;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.wst.ws.internal.ext.WebServiceExtensionImpl;

/**
 * @author joan
 * 
 * This class is provided for the addition of locator extension specific functions
 * 
 */

public class WebServiceLocatorExtension extends WebServiceExtensionImpl {
	
//	 Copyright
	  public static final String copyright = "(c) Copyright IBM Corporation 2000, 2002.";
	
	 public WebServiceLocatorExtension(IConfigurationElement configElement)
	  {
	    super(configElement);
	  }

}
