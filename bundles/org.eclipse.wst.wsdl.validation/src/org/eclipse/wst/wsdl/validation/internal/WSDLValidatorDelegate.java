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

package org.eclipse.wst.wsdl.validation.internal;

import java.util.Locale;
import java.util.ResourceBundle;


/**
 * A delegate holds a validator's information and can instantiate it
 * when requested to.
 */
public class WSDLValidatorDelegate
{
  private String validatorClassname = null;
  private String resourceBundle = null;
  private ClassLoader classLoader = null;
  private IWSDLValidator validator = null;

  /**
   * Create a delegate for a validator by its class name and resource bundle name.
   * 
   * @param validatorClassname The name of the validator class.
   * @param resourceBundle The name of the validator base resource bundle.
   */
  public WSDLValidatorDelegate(String validatorClassname, String resourceBundle)
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
  public WSDLValidatorDelegate(String validatorClassname, String resourceBundle, ClassLoader classLoader)
  {
    this(validatorClassname, resourceBundle);
    this.classLoader = classLoader;
  }

  /**
   * Get the validator specified in this delegate.
   * 
   * @return The WSDL validator specified by this delegate.
   */
  public IWSDLValidator getValidator()
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

        validator = (IWSDLValidator)validatorClass.newInstance();
        
        if (resourceBundle != null)
        {
         ResourceBundle validatorBundle = ResourceBundle.getBundle(resourceBundle, Locale.getDefault(), classLoader);
         validator.setResourceBundle(validatorBundle);
       }
      }
      catch (Exception e)
      {
        // TODO: add logging
        System.err.println(e);
      }
      catch(Throwable t)
      {
        System.err.println(t);
      }
    }
    return validator;
  }
  
  /**
   * Return the validator class name.
   * @return The validator class name.
   */
  public String getValidatorClassName()
  {
    return validatorClassname;
  }
}
