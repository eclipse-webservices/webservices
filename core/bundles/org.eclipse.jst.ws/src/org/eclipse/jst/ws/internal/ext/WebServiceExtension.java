/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.jst.ws.internal.ext;

import org.eclipse.core.runtime.IConfigurationElement;


/**
* This represents an extension in the plugin registry 
* It job is to act as a proxy to the iconfigelement
*/
public interface WebServiceExtension
{
  // Copyright
  public static final String copyright = "(c) Copyright IBM Corporation 2000, 2002.";

  /**
  * If the extension has code that needs to be executed or a fragment added
  * @return WebServiceExecutable this interface holds executable code and a 
  * fragment
  */
  public Object getWebServiceExecutableExtension();

  /**
  * This is the config element that holds the extension info
  * @param IConfigurationElement Extension element
  */
  public void setConfigElement(IConfigurationElement configElement);
 
  /**
  * If the extension has code that 
  * @return IConfigurationElement Extension element
  */
  public IConfigurationElement getConfigElement();
}


