/*******************************************************************************
 * Copyright (c) 2001, 2006 IBM Corporation and others.
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

/**
 * An extension Ant WSDL validator.
 */
public class ExtensionValidator
{
  private String classname = null;
  private String namespace = null;
  
  public void setClass(String classname)
  {
    this.classname = classname;
  }
  
  public String getClassName()
  {
    return this.classname;
  }
  
  public void setNamespace(String namespace)
  {
    this.namespace = namespace;
  }
  
  public String getNamespace()
  {
    return this.namespace;
  }
}
