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

package org.eclipse.wst.wsdl.validation.internal.wsdl11.mime;

import java.util.List;
import java.util.ResourceBundle;

import org.eclipse.wst.wsdl.validation.internal.util.MessageGenerator;
import org.eclipse.wst.wsdl.validation.internal.wsdl11.IWSDL11Validator;
import org.eclipse.wst.wsdl.validation.internal.wsdl11.WSDL11ValidationInfo;

/**
 * The MIME validator plugs into the WSDL validator to provide
 * validation for all elements in a WSDL document within the MIME namespace.
 */
public class MIMEValidator implements IWSDL11Validator
{
  protected MessageGenerator messagegenerator;

  /**
   * @see org.eclipse.wst.wsdl.validation.internal.wsdl11.IWSDL11Validator#validate(java.lang.Object, java.util.List, org.eclipse.wsdl.validate.wsdl11.WSDL11ValidationInfo)
   */
  public void validate(Object element, List parents, WSDL11ValidationInfo valInfo)
  {

  }
  
  /**
   * @see org.eclipse.wst.wsdl.validation.internal.wsdl11.validator.IWSDL11Validator#setResourceBundle(java.util.ResourceBundle)
   */
  public void setResourceBundle(ResourceBundle rb)
  {
    if (messagegenerator == null)
    {
      messagegenerator = new MessageGenerator(rb);
    }
  }
}
