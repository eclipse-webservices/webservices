/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

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
