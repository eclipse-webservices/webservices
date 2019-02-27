/*******************************************************************************
 * Copyright (c) 2001, 2005 IBM Corporation and others.
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

package org.eclipse.wst.wsdl.validation.internal.ui.ant;

import org.apache.tools.ant.Task;

/**
 * The URIResolver task allows specifying an extension URI resolver with
 * the WSDLValidate Ant task.
 */
public class URIResolver extends Task
{
  private String clazz = null;
  
  /**
   * Set the class name of the extension URI resolver.
   * 
   * @param clazz The class name of the extension URI resolver.
   */
  public void setClassName(String clazz)
  {
    this.clazz = clazz;
  }
  
  /**
   * Get the class name of the extension URI resolver.
   * 
   * @return The class name of the extension URI resolver.
   */
  public String getClassName()
  {
    return clazz;
  }
}
