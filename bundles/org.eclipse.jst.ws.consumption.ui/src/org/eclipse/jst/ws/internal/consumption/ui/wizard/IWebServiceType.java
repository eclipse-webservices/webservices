/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.jst.ws.internal.consumption.ui.wizard;

import org.eclipse.core.runtime.IConfigurationElement;

/**
* This is the interface for objects that represent a kind of
* Web Service artifact. The primary purpose of a WebServiceType
* object is to manufacture the wizard pages that support the type.
*/
public interface IWebServiceType
{
  /**
  * Returns the id of this Web Service type.
  * @return id of this Web Service type.
  */
  public String getId ();
  /**
  * Returns a short, locale specific name of this Web Service type.
  * @return A short, locale specific name of this Web Service type.
  */
  public String getLabel ();

  /**
  * Returns a locale specific description of this Web Service type's extension metadata.
  * @return A locale specific description of this Web Service type's extension metadata.
  */
  public String[] getExtensionMetadata ();
  
  /**
  * Returns a locale specific description of this Web Service type's resource metadata.
  * @return A locale specific description of this Web Service type's resource metadata.
  */
  public String[] getResourceTypeMetadata ();  
  
  /**
  * Returns true if the plugin.xml contains an attribute of "canFinish" with a value of true.  Otherwise, 
  * false is returned.  Note: if the "canFinish" attribute is not specified true is returned by default.
  * @return Returns whether or not the user can finish on the pages before the web service type pages.
  */
  public boolean getCanFinish();  

  /**
   * Returns the configuration element associated with
   * this runtime.
   * 
   * @return org.eclipse.core.runtime.IConfigurationElement
   */
  public IConfigurationElement getConfigurationElement();

  public String getObjectSelectionWidget();
}
