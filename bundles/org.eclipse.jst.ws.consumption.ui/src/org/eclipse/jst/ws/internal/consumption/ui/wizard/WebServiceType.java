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
* Web Service artifact. The primary purpose of a WebServiceType
* object is to manufacture the wizard pages that support the type.
*/
public interface WebServiceType
{
  /**
  * Returns a short, locale specific name of this Web Service type.
  * @return A short, locale specific name of this Web Service type.
  */
  public String getName ();

  /**
  * Returns a locale specific description of this Web Service type.
  * @return A locale specific description of this Web Service type.
  */
  public String getDescription ();

}