/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20071120   196997 pmoogk@ca.ibm.com - Peter Moogk
 * 20071219   213447 pmoogk@ca.ibm.com - Peter Moogk
 *******************************************************************************/
package org.eclipse.wst.ws.internal.preferences;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;

public class WSDLFilePropertyTester extends PropertyTester
{
  public boolean test(Object receiver, String property, Object[] args, Object expectedValue)
  {
    boolean result = false;
    
    if( receiver instanceof IContainer)
    {
      result = hasWSDLfile( (IContainer) receiver );
    }
    
    return result;
  }
  
  private boolean hasWSDLfile( IContainer container )
  {
    boolean result   = false;
    
    try
    {
      IResource[] children = container.members();
      
      for( IResource child : children )
      {
        if( child instanceof IContainer )
        {
          result = hasWSDLfile( (IContainer) child );
          
        }
        else if( child instanceof IFile )
        {
          String extension = child.getFileExtension();
          
          if( extension != null )
          {
            result = extension.equals( "wsdl" );
          }
        }
        
        // If we found a single wsdl file we will break out of the loop.
        if( result ) break;
      }    
    }
    catch( CoreException exc )
    {
      // Ignore exception and return false.  
    }
    
    return result;
  }
}
