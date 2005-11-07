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

import java.util.Locale;
import java.util.ResourceBundle;


/**
 * The WSDL 1.1 validator delegate holds a reference to a validator to be instantiated at
 * a later point.
 */
public class WSDL20ValidatorDelegate
{
  private String validatorClassname = null;
  private String resourceBundle = null;
  private ClassLoader classLoader = null;
  private IWSDL20Validator validator = null;

  /**
   * Create a delegate for a validator by its class name and resource bundle name.
   * 
   * @param validatorClassname The name of the validator class.
   * @param resourceBundle The name of the validator base resource bundle.
   */
  public WSDL20ValidatorDelegate(String validatorClassname, String resourceBundle)
  {
    this.validatorClassname = validatorClassname;
    this.resourceBundle = resourceBundle;
  }

  /**
   * Create a delegate for a validator by its class name, resource bundle name and 
   * a class loader to load the validator and bundle.
   * 
   * @param validatorClassname The name of the validator class.
   * @param resourceBundle The name of the validator base resource bundle.
   * @param classLoader The class loader to use to load the validator and bundle.
   */
  public WSDL20ValidatorDelegate(String validatorClassname, String resourceBundle, ClassLoader classLoader)
  {
    this(validatorClassname, resourceBundle);
    this.classLoader = classLoader;
  }

  /**
   * Get the validator specified in this delegate.
   * 
   * @return The WSDL 1.1 validator specified by this delegate.
   */
  public IWSDL20Validator getValidator()
  {
    if (validator == null)
    {
      if(classLoader == null)
      {
      	classLoader = getClass().getClassLoader();
      }
      try
      {
        Class validatorClass =
          classLoader != null ? classLoader.loadClass(validatorClassname) : Class.forName(validatorClassname);

        validator = (IWSDL20Validator)validatorClass.newInstance();
        if (resourceBundle != null)
        {
          ResourceBundle validatorBundle = ResourceBundle.getBundle(resourceBundle, Locale.getDefault(), classLoader);
          validator.setResourceBundle(validatorBundle);
        }
      }
      catch (ClassNotFoundException e)
      {
        // TODO: add logging
        System.err.println(e);
      }
      catch (IllegalAccessException e)
      {
        // TODO: add logging
        System.err.println(e);
      }
      catch (InstantiationException e)
      {
        // TODO: add logging
        System.err.println(e);
      }
    }
    return validator;
  }
}
