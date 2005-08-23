package org.eclipse.wst.ws.internal.wsfinder;

import java.util.List;

/**
 * @author joan
 *
 * Interface for web service locators that will be retrieved by the WebServiceFinder.  This interface must 
 * not be implemented directly.  Subclasses should extend the AbstractWebServiceLocator and implement the 
 * getWebServices() method.
 */

public interface IWebServiceLocator {

	public List getWebServices();
}
