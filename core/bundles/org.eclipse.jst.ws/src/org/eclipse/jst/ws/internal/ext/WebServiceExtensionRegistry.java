/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
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

package org.eclipse.jst.ws.internal.ext;


/**
* Returns the names of the extensions for a given extension-point 
* also returns the extension objects 
*
*/
public interface WebServiceExtensionRegistry
{
  
  /**
  * Returns the names of all registered extensions
  * @return The names of all registered extensions.
  */
  public String[] getWebServiceExtensionNames ();
  
  /**
  * Returns the extension object of the given name
  *@return WebServiceExtension object
  */
  public WebServiceExtension getWebServiceExtensionsByName( String name);

  /**
  * Returns All extention objects in this registry
  *@return WebServiceExtension objects
  */
  public WebServiceExtension[] getWebServiceExtensions();
  
  
}
