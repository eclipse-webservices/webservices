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

package org.eclipse.wst.wsdl.validation.internal.ui;

import java.util.ResourceBundle;

import org.eclipse.wst.wsdl.validation.internal.ValidatorRegistry;
import org.eclipse.wst.wsdl.validation.internal.WSDLValidatorDelegate;
import org.eclipse.wst.wsdl.validation.internal.wsdl11.WSDL11ValidatorDelegate;
import org.eclipse.wst.wsdl.validation.internal.wsdl20.WSDL20ValidatorDelegate;

import com.ibm.wsdl.Constants;

/**
 * A class with methods to set validators and schemas that can be used
 * by all interfaces to the validator.
 */
public class WSDLConfigurator
{
  /**
   * Register a WSDL validator with the given namespace.
   * 
   * @param namespace The namespace of the validator.
   * @param validatorClassname The name of the validator class to register.
   * @param resourceBundle The name of the validator base resource bundle.
   * @param classloader The classloader to use to load the validator.
   */
  public static void registerWSDLValidator(String namespace, String validatorClassname, String resourceBundle, ClassLoader classloader)
  {
    WSDLValidatorDelegate delegate = new WSDLValidatorDelegate(validatorClassname, resourceBundle, classloader);
    ValidatorRegistry.getInstance().registerValidator(namespace, delegate, ValidatorRegistry.WSDL_VALIDATOR);
  }

  /**
   * Register an extension validator with the given namespace.
   * 
   * @param namespace The namespace of the validator.
   * @param validatorClassname The name of the validator class to register.
   * @param resourceBundle The name of the validator base resource bundle.
   * @param classloader The classloader to use to load the validator.
   */
  public static void registerExtensionValidator(String namespace, String validatorClassname, String resourceBundle, ClassLoader classloader)
  {
    WSDLValidatorDelegate delegate = new WSDLValidatorDelegate(validatorClassname, resourceBundle, classloader);
    ValidatorRegistry.getInstance().registerValidator(namespace, delegate, ValidatorRegistry.EXT_VALIDATOR);
  }

  /**
   * Register a WSDL 1.1 validator with the given namespace.
   * 
   * @param namespace The namespace of the validator
   * @param validatorClassname The name of the validator class to register
   * @param resourceBundle The name of the validator base resource bundle.
   * @param classloader The classloader to use to load the validator.
   */
  public static void registerWSDL11Validator(String namespace, String validatorClassname, String resourceBundle, ClassLoader classloader)
  {
  	WSDL11ValidatorDelegate delegate = new WSDL11ValidatorDelegate(validatorClassname, resourceBundle, classloader);
    org.eclipse.wst.wsdl.validation.internal.wsdl11.ValidatorRegistry.getInstance().registerValidator(namespace, delegate);
  }
  
  /**
   * Register a WSDL 2.0 validator with the given namespace.
   * 
   * @param namespace The namespace of the validator
   * @param validatorClassname The name of the validator class to register
   * @param resourceBundle The name of the validator base resource bundle.
   */
  public static void registerWSDL20Validator(String namespace, String validatorClassname, String resourceBundle)
  {
    WSDL20ValidatorDelegate delegate = new WSDL20ValidatorDelegate(validatorClassname, resourceBundle);
    org.eclipse.wst.wsdl.validation.internal.wsdl20.ValidatorRegistry.getInstance().registerValidator(namespace, delegate);
  }

  /**
   * Register the default validators. Registers validators for:
   * WSDL 1.1
   * WSDL 1.1 SOAP
   * WSDL 1.1 HTTP
   * WSDL 1.1 MIME
   * 
   * @param rb - the resource bundle of the WSDL 1.1 validator
   */
  public static void registerDefaultValidators(ResourceBundle rb)
  {
    // Register the WSDL 1.1 validator controller and validators.
    registerWSDLValidator(Constants.NS_URI_WSDL, "org.eclipse.wst.wsdl.validation.internal.wsdl11.WSDL11ValidatorController", "validatewsdl", null);
    registerWSDL11Validator(Constants.NS_URI_WSDL, "org.eclipse.wst.wsdl.validation.internal.wsdl11.WSDL11BasicValidator", "validatewsdl", null);
    registerWSDL11Validator(
      org.eclipse.wst.wsdl.validation.internal.Constants.NS_HTTP, "org.eclipse.wst.wsdl.validation.internal.wsdl11.http.HTTPValidator","validatewsdlhttp", null);
    registerWSDL11Validator(
      org.eclipse.wst.wsdl.validation.internal.Constants.NS_SOAP11,"org.eclipse.wst.wsdl.validation.internal.wsdl11.soap.SOAPValidator","validatewsdlsoap", null);
    registerWSDL11Validator(
      org.eclipse.wst.wsdl.validation.internal.Constants.NS_MIME,"org.eclipse.wst.wsdl.validation.internal.wsdl11.mime.MIMEValidator","validatewsdlmime", null);
    
    // The WSDL 1.1 schema validator is a special case as it is registered for three namespaces.
    // We will call directly here so we can use the same delegate for all three namespaces.
    WSDL11ValidatorDelegate delegate = new WSDL11ValidatorDelegate("org.eclipse.wst.wsdl.validation.internal.wsdl11.xsd.InlineSchemaValidator", "validatewsdl");
    org.eclipse.wst.wsdl.validation.internal.wsdl11.ValidatorRegistry.getInstance().registerValidator(Constants.NS_URI_XSD_1999, delegate);
    org.eclipse.wst.wsdl.validation.internal.wsdl11.ValidatorRegistry.getInstance().registerValidator(Constants.NS_URI_XSD_2000, delegate);
    org.eclipse.wst.wsdl.validation.internal.wsdl11.ValidatorRegistry.getInstance().registerValidator(Constants.NS_URI_XSD_2001, delegate);
    
    // Register the WSDL 2.0 validator controller and validators.
//    registerWSDLValidator(org.eclipse.wsdl20.model.impl.Constants.NS_URI_WSDL, "org.eclipse.wst.wsdl.validation.internal.wsdl20.WSDL20ValidatorController", "validatewsdl", null);
//    
//    registerWSDL20Validator(org.eclipse.wsdl20.model.impl.Constants.NS_URI_WSDL, "org.eclipse.wst.wsdl.validation.internal.wsdl20.WSDL20BasicValidator", "validatewsdl");
  }
}
