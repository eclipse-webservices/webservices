/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.command.internal.provisional.env.core.uri;

import java.net.URL;

import org.eclipse.wst.command.internal.provisional.env.core.common.SimpleStatus;
import org.eclipse.wst.command.internal.provisional.env.core.common.Status;


public class RelativeScheme implements URIScheme
{

  /* (non-Javadoc)
   * @see org.eclipse.env.uri.URIScheme#isHierarchical()
   */
  public boolean isHierarchical()
  {
    return true;
  }

  /* (non-Javadoc)
   * @see org.eclipse.env.uri.URIScheme#isValid(org.eclipse.env.uri.URI)
   */
  public boolean isValid(URI uri)
  {
    return !uri.toString().startsWith( "/" );
  }

  /* (non-Javadoc)
   * @see org.eclipse.env.uri.URIScheme#newURI(java.lang.String)
   */
  public URI newURI(String uri) 
  {
    return new RelativeURI( uri );
  }

  /* (non-Javadoc)
   * @see org.eclipse.env.uri.URIScheme#newURI(org.eclipse.env.uri.URI)
   */
  public URI newURI(URI uri) 
  {
    return new RelativeURI( uri.toString() );
  }

  /* (non-Javadoc)
   * @see org.eclipse.env.uri.URIScheme#newURI(java.net.URL)
   */
  public URI newURI(URL url) 
  {
    return new RelativeURI( url.toString() );
  }

  /* (non-Javadoc)
   * @see org.eclipse.env.uri.URIScheme#validate(org.eclipse.env.uri.URI)
   */
  public Status validate(URI uri)
  {
    Status result = null;
    
    if( isValid( uri ) )
    {
      result = new SimpleStatus( "", "", Status.OK );
    }
    else
    {
      result = new SimpleStatus( "", "", Status.ERROR );      
    }
    
    return result;
  }
}
