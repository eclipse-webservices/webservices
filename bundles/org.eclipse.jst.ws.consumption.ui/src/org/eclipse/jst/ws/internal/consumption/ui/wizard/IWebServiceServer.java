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
public interface IWebServiceServer
{
  // Copyright
  public static final String copyright = "(c) Copyright IBM Corporation 2000, 2002.";

  /**
  * Returns the id of this Web Service type.
  * @return id of this Web Service type.
  */
  public String getId ();
  /**
  * Returns a short, locale specific name of this Web Service type.
  * @return A short, locale specific name of this Web Service type.
  */
  public String getFactoryId ();

  /**
  * Returns a short, locale specific name of this Web Service type.
  * @return A short, locale specific name of this Web Service type.
  */
  public String getLabel ();
  
  /**
   * Returns true if set true in plugin.xml
   * @return boolean
   */
  public boolean getIsDefault();
  
  /**
   * Returns runtime target Id
   * @return String
   */
  public String getRuntimeTypeId();
  
  /**
   * Returns the configuration element associated with
   * this runtime.
   * 
   * @return org.eclipse.core.runtime.IConfigurationElement
   */
  public IConfigurationElement getConfigurationElement();

}
