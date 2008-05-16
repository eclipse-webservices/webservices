/*******************************************************************************
 * Copyright (c) 2007, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20071024   196997 pmoogk@ca.ibm.com - Peter Moogk
 * 20071024   196997 pmoogk@ca.ibm.com - Peter Moogk
 * 20080428   227501 pmoogk@ca.ibm.com - Peter Moogk, Fixed toLowerCase Locale problem.
 * 20080516   232603 pmoogk@ca.ibm.com - Peter Moogk, Clean up java doc
 *******************************************************************************/
package org.eclipse.wst.ws.service.policy.utils;

import java.util.Locale;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.wst.ws.service.internal.policy.DescriptorImpl;
import org.eclipse.wst.ws.service.policy.ServicePolicyActivator;

/**
 * 
 * This class is only intended for org.eclipse.wst.ws.service.policy.ui plugin to use.
 * It contains convenience method for parsing the service policy extension point.
 *
 */
public class RegistryUtils
{
  /**
   * Loads the attributes for a descriptor element.
   * @param element the descriptor element
   * @return returns the data for this element.
   */
  public static DescriptorImpl loadDescriptor( IConfigurationElement element )
  {
    DescriptorImpl descriptor = new DescriptorImpl();
    
    String shortName   = getAttribute( element, "shortname" ); //$NON-NLS-1$
    String longName    = getAttribute( element, "longname" ); //$NON-NLS-1$
    String description = getAttribute( element, "description" ); //$NON-NLS-1$
    String iconPath    = getAttribute( element, "iconpath" ); //$NON-NLS-1$
    String contextHelp = getAttribute( element, "contexthelpid" ); //$NON-NLS-1$
    
    if( shortName == null && longName == null )
    {
      ServicePolicyActivator.logError( "Short name or Long name missing from service policy descriptor.", null ); //$NON-NLS-1$
    }
    else if( shortName == null )
    {
      shortName = longName;
    }
    else if( longName == null )
    {
      longName = shortName;
    }
    
    descriptor.setDescription( description );
    descriptor.setShortName( shortName );
    descriptor.setLongName( longName );
    descriptor.setContextHelpId( contextHelp );
    
    if( iconPath != null )
    {
      descriptor.setIconPath( iconPath );
      descriptor.setIconBundleId( element.getContributor().getName() );
    }
    
    descriptor.resetHasChanged();
    return descriptor;
  }
  
  /**
   * This method takes in a lower case attribute name.  It will then return
   * the same attribute name that includes potentially uppercase characters.
   * This method allows clients to specify attribute names in a case insensitive
   * fashion.
   *  
   * @param element the element
   * @param attribute the attribute
   * @return returns the lowercase attribute name.
   */
  public static String getAttributeName( IConfigurationElement element, String attribute )
  {
    String[] names  = element.getAttributeNames();
    String   result = null;
    
    for( String name : names )
    {
      if( name.toLowerCase( Locale.ENGLISH ).equals( attribute ) )
      {
        result = name;
        break;
      }
    }
        
    return result;
  }
  
  /**
   * Gets a attribute value where the attribute name is case insensitive.
   * 
   * @param element the element.
   * @param attribute the attribute to get.
   * @return the value for this attribute or null if the attribute is not specified.
   */
  public static String getAttribute( IConfigurationElement element, String attribute )
  {
    String[] names = element.getAttributeNames();
    String   value = null;
    
    for( String name : names )
    {
      if( name.toLowerCase( Locale.ENGLISH ).equals( attribute ) )
      {
        value = element.getAttribute( name );
        break;
      }
    }
        
    return value;
  }
}
