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

package org.eclipse.wst.wsdl.validation.internal.wsdl11;

/**
 * The WSDL 1.1 validator delegate holds a reference to a validator to be instantiated at
 * a later point.
 */
public abstract class WSDL11ValidatorDelegate
{
  private IWSDL11Validator validator = null;


  /**
   * Get the validator specified in this delegate.
   * 
   * @return The WSDL 1.1 validator specified by this delegate.
   */
  public IWSDL11Validator getValidator()
  {
    if (validator == null)
    {
      validator = loadValidator();
    }
    return validator;
  }
  
  protected abstract IWSDL11Validator loadValidator();
}
