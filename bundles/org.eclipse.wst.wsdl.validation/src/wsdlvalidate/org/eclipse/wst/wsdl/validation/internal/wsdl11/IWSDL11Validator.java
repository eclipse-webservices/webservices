/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.wsdl.validation.internal.wsdl11;

import java.util.List;
import java.util.ResourceBundle;

/**
 * Interface for a validator plugged into the WSDL 1.1 validator.
 */
public interface IWSDL11Validator
{
  /**
   * Validate the given element.
   * 
   * @param element The element to validate.
   * @param parents A list of parents of this element.
   * @param valInfo The current validation information.
   */
  public void validate(Object element, List parents, WSDL11ValidationInfo valInfo);

  /**
   * Set the resource bundle of the validator.
   * 
   * @param rb The resource bundle to set.
   */
  public void setResourceBundle(ResourceBundle rb);

}
