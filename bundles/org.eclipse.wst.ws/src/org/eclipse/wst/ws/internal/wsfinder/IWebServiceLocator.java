/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

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
