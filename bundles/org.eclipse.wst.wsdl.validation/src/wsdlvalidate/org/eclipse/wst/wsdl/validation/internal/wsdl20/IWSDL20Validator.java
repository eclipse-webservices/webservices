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

package org.eclipse.wst.wsdl.validation.internal.wsdl20;

import java.util.List;
import java.util.ResourceBundle;

import org.eclipse.wst.wsdl.validation.internal.ValidationInfo;

/**
 * Interface for a validator plugged into the WSDL 2.0 validator.
 */
public interface IWSDL20Validator
{
  /**
   * Validate the given element.
   * 
   * @param element The element to validate.
   * @param parents A list of parents of this element.
   * @param valInfo The validation info for the current validation.
   */
  public void validate(Object element, List parents, ValidationInfo valInfo);

  /**
   * Set the resource bundle of the validator.
   * 
   * @param rb The resource bundle to set.
   */
  public void setResourceBundle(ResourceBundle rb);

}
