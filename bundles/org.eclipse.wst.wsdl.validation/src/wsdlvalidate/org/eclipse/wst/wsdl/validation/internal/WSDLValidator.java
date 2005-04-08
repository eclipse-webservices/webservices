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

import java.io.InputStream;
import java.util.Hashtable;
import java.util.ResourceBundle;

import org.eclipse.wst.wsdl.validation.internal.resolver.IURIResolver;
import org.eclipse.wst.wsdl.validation.internal.resolver.URIResolver;

/**
 * An main WSDL validator class. The WSDL validator validates WSDL documents.
 */
public class WSDLValidator
{
  private static String VALIDATOR_RESOURCE_BUNDLE = "validatewsdl";
  private ValidationController validationController;
  private URIResolver uriResolver;
  private Hashtable attributes = new Hashtable();
  
  /**
   * Constructor.
   */
  public WSDLValidator()
  {
    ResourceBundle rb = ResourceBundle.getBundle(VALIDATOR_RESOURCE_BUNDLE);
    uriResolver = new URIResolver();
    validationController = new ValidationController(rb, uriResolver);
  }
  
  /**
   * Validate the WSDL file at the given location.
   * 
   * @param uri The location of the WSDL file to validate.
   * @return A validation report summarizing the results of the validation.
   */
  public IValidationReport validate(String uri)
  {
   return validate(uri, null);
  }
  
  /**
   * 
   * Validate the inputStream
   * @param uri The location of the WSDL file being validated
   * @param inputStream The stream to validate
   * @return A Validation report summarizing the results of the validation
   */
  public IValidationReport validate(String uri, InputStream inputStream)
  {
    if(uri == null) 
      return null;
    validationController.setAttributes(attributes);
    return validationController.validate(formatURI(uri), inputStream);
  }
  
  /**
   * Add a URI resolver to the WSDL validator.
   * 
   * @param uriResolver The URI resolver to add to the WSDL validator.
   */
  public void addURIResolver(IURIResolver uriResolver)
  {
  	this.uriResolver.addURIResolver(uriResolver);
  }
  
  /**
   * Set an attribute on the validator. An attribute is
   * defined by a name and a value pair. An attribute may
   * be defined for any validator, built in or an extension.
   * Extension validators can probe the attributes set on
   * the WSDL validator to customize the way in which they
   * validate.
   * 
   * @param name The attribute identifier.
   * @param value The attribute itself.
   */
  public void setAttribute(String name, Object value)
  {
  	attributes.put(name, value);
  }
  protected String formatURI(String uri)
  {
    uri = uri.replace('\\','/');
    if(uri.startsWith("file:"))
  	{
  	  uri = uri.substring(6);
  	  while(uri.startsWith("\\") || uri.startsWith("/"))
  	  {
  	  	uri = uri.substring(1);
  	  }
  	  uri = "file:///" + uri;
  	}
    return uri;
  }
}
