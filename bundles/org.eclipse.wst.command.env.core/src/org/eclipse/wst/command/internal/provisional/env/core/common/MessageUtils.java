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
package org.eclipse.wst.command.internal.provisional.env.core.common;

import java.text.MessageFormat;
import java.util.Hashtable;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * This class provides a convienent way to access resource bundles and
 * retieve translated strings.
 *
 *
 */
public class MessageUtils
{
  private ResourceBundle resource_;
  
  private static Hashtable bundles_ = new Hashtable();
  
  /**
   * 
   * @param bundleId the resource bundle ID.
   * @param object the class loader for this object will be used to retrieve
   *               the resource bundle.
   */
  public MessageUtils( String bundleId, Object object )
  {
    this( bundleId, object.getClass().getClassLoader() );
  }
  
  /**
   * 
   * @param bundleId the resource bundle ID.
   * @param loader the class loader that will be used to retrieve
   *               the resource bundle.
   */
  public MessageUtils( String bundleId, ClassLoader loader )
  {
    resource_ = (ResourceBundle)bundles_.get( bundleId );
    
    if( resource_ == null )
    {
      resource_ = ResourceBundle.getBundle( bundleId, Locale.getDefault(), loader );
      bundles_.put( bundleId, resource_ );
    }
  }
  
  /**
   * 
   * @param key the key for the string to retrieve.
   * @return returns the translated string.
   */
  public String getMessage ( String key )
  {
    String value = key;
    
    try
    {
      value = resource_.getString( key ); 
    }
    catch( Throwable exc )
    {
    }
    
    return value;
  }
  
  /**
   * 
   * @param key the key for the string to retrieve.
   * @param args These arguments will be substituted into the translated string.
   * @return returns the translated string with any substitutions.
   */
  public String getMessage ( String key, Object[] args )
  {
    return MessageFormat.format( getMessage(key),args );
  } 
}
