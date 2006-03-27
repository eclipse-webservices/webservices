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
package org.eclipse.wst.wsdl.ui.internal.reconciler;

import org.eclipse.wst.validation.internal.ValidationRegistryReader;
import org.eclipse.wst.validation.internal.provisional.core.IValidator;
import org.eclipse.wst.xml.ui.internal.validation.DelegatingSourceValidator;

/**
 * 
 */
public class DelegatingSourceValidatorForWSDL extends DelegatingSourceValidator
{

  final private static String VALIDATOR_CLASS = "org.eclipse.wst.wsdl.validation.internal.ui.eclipse.Validator"; 

  public DelegatingSourceValidatorForWSDL()
  { super();
  }
  
  protected IValidator getDelegateValidator()
  {
    try
    {
    ValidationRegistryReader registry = ValidationRegistryReader.getReader();
      return registry.getValidator(VALIDATOR_CLASS);
    }
    catch (Exception e)
    { //
    } 
    return null;
  }
}
