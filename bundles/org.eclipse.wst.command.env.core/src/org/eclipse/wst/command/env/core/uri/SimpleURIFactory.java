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
package org.eclipse.wst.command.env.core.uri;

import java.net.URL;
import java.util.Hashtable;

import org.eclipse.wst.command.env.core.common.MessageUtils;
import org.eclipse.wst.command.env.core.common.SimpleStatus;
import org.eclipse.wst.command.env.core.common.Status;


public class SimpleURIFactory implements URIFactory
{
  private Hashtable    schemes_ = new Hashtable();
  private MessageUtils msg_     = new MessageUtils( "org.eclipse.wst.command.env.core.environment", this );
  
  
  /* (non-Javadoc)
   */
  public URI newURI(String uri) throws URIException
  {
    URIScheme scheme = newURIScheme( uri );
    
    return scheme.newURI( uri );
  }

  /* (non-Javadoc)
   */
  public URI newURI(URL url) throws URIException
  {
    URIScheme scheme = newURIScheme( url.toString() );
    
    return scheme.newURI( url );
  }

  /* (non-Javadoc)
   */
  public URIScheme newURIScheme(String schemeOrURI) throws URIException
  {
    URIScheme newScheme = null;
    
    if( schemeOrURI == null )
    {
      throw new URIException( 
              new SimpleStatus( "SimpleURIFactory",
                                msg_.getMessage( "MSG_NULL_ARG_SPECIFIED", new Object[]{"newURIScheme"}),
                                Status.ERROR ) );
    }
    
    int colon    = schemeOrURI.indexOf(':');
    int slash    = schemeOrURI.indexOf('/');
    
    // A protocol was specified.  Note: a colon appearing after a path is not
    // considered part of the protocol for this URI.
    if( (colon != -1 && slash == -1) || ( colon != -1 && colon < slash ) )
    {
      String protocol = schemeOrURI.substring(0, colon );
      newScheme       = (URIScheme)schemes_.get( protocol );
      
      if( newScheme == null )
      {
        throw new URIException( 
            new SimpleStatus( "SimpleURIFactory",
                msg_.getMessage( "MSG_SCHEME_NOT_FOUND", new Object[]{ schemeOrURI }),
                Status.ERROR ) );       
      }
    }
    else if( schemeOrURI.startsWith( "/") )
    {
      throw new URIException( 
          new SimpleStatus( "SimpleURIFactory",
              msg_.getMessage( "MSG_ABSOLUTE_PATH_WITHOUT_SCHEME", new Object[]{ schemeOrURI }),
              Status.ERROR ) );
      
    }
    else
    {
      newScheme = new RelativeScheme();
    }
    
    return newScheme;
  }
  
  public void registerScheme( String protocol, URIScheme scheme )
  {
    schemes_.put( protocol, scheme );
  }
}
