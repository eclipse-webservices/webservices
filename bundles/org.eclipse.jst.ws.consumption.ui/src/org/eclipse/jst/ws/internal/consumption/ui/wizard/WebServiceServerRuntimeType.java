/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.internal.consumption.ui.wizard;


/**
* This is the interface for objects that represent a kind of
* Web Service-Server-Runtime artifact. The primary purpose of a WebServiceServerRuntimeType
* object is to manufacture the wizard pages that support the type, server, and runtime configuration.
*/
public interface WebServiceServerRuntimeType
{
  /**
  * Returns a short, locale specific name of this Web Service Server-Runtime type.
  * @return A short, locale specific name of this Web Service Server-Runtime type.
  */
  public String getName ();

  /**
  * Returns a locale specific description of this Web Service Server-Runtime type.
  * @return A locale specific description of this Web Service Server-Runtime type.
  */
  public String getDescription ();

  /**
  * Returns a label for the Server supported by this Web Service Server-Runtime type
  * @return A server label from server plugins
  */
  public String getServerLabel();

  /**
  * Sets a label for the Server supported by this Web Service Server-Runtime type
  * @param A server label from server plugins
  */
  public void setServerLabel(String serverLabel);

  /**
  * Returns a label for the deployment runtime of this Web Service Server-Runtime type
  * @return A label for the deployment runtime
  */
  public String getRuntimeLabel();

  /**
  * Sets a label for the deployment runtime of this Web Service Server-Runtime type
  * @param A label for the deployment runtime
  */
  public void setRuntimeLabel(String runtimeLabel);

  /**
  * Returns a label for the Web Service type associated with this WebServiceServerRuntimeType
  * @return A label for the Web Service type
  */
  public String getWebServiceTypeLabel();

  /**
  * Returns whether or not a Web Module is required for this WebServiceServerRuntimeType
  * @return true if requireWebModule
  */
  public boolean isWebModuleRequired();

  /**
  * Sets the boolean value at runtime according to requireWebModule attribute from the manifest file
  * @param requireWebModule 
  */
  public void setWebModuleRequired(boolean requireWebModule);

  /**
  * Returns whether or not an EJB Module is required for this WebServiceServerRuntimeType
  * @return true is requireEJBModule is true
  */
  public boolean isEJBModuleRequired();

  /**
  * Sets the boolean value at runtime according to requireEJBModule attribute from the manifest file
  * @param requireEJBModule 
  */
  public void setEJBModuleRequired(boolean requireEJBModule); 
}


