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
* Web Service artifact. 
*/
public interface IWebServiceRuntime
{
  // Copyright
  public static final String copyright = "(c) Copyright IBM Corporation 2000, 2002.";

  /**
  * Returns the id of this Web Service runtime.
  * @return id of this Web Service runtime.
  */
  public String getId ();

  /**
  * Returns a locale specific description of this Web Service runtime.
  * @return A locale specific description of this Web Service runtime.
  */
  public String getDescription ();

  /**
  * Returns a short, locale specific Label of this Web Service runtime.
  * @return A short, locale specific Label of this Web Service runtime.
  */
  public String getLabel ();
  
  /**
   * Returns a String, J2EE version
   * @return
   */
  public String[] getJ2EEVersions();

  /**
   * Returns the configuration element associated with
   * this runtime.
   * 
   * @return org.eclipse.core.runtime.IConfigurationElement
   */
  public IConfigurationElement getConfigurationElement();

}

