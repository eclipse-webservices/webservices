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
package org.eclipse.wst.command.internal.env.eclipse;

import java.net.MalformedURLException;
import java.net.URL;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.wst.command.internal.provisional.env.core.common.MessageUtils;
import org.eclipse.wst.command.internal.provisional.env.core.common.StatusUtils;
import org.eclipse.wst.command.internal.provisional.env.core.uri.RelativeURI;
import org.eclipse.wst.command.internal.provisional.env.core.uri.URI;
import org.eclipse.wst.command.internal.provisional.env.core.uri.URIException;
import org.eclipse.wst.command.internal.provisional.env.core.uri.URIScheme;


public class EclipseScheme implements URIScheme
{
  private BaseEclipseEnvironment  environment_;
  private IProgressMonitor        monitor_;
  private MessageUtils            msg_;
  
  public EclipseScheme( BaseEclipseEnvironment environment, IProgressMonitor monitor )
  {
    monitor_     = monitor;
    environment_ = environment;
    msg_         = new MessageUtils( "org.eclipse.wst.command.internal.env.common.environment", this );
  }
  
  /**
   */
  public boolean isHierarchical()
  {
    return true;
  }

  /**
   */
  public boolean isValid(URI uri)
  {
    boolean result = true;
    
    if( uri == null ) return false;
    
    try
    {
      getPathFromPlatformURI( uri.toString() );
    }
    catch( URIException exc )
    {
      result = false;
    }
    
    return result;
  }

  /**
   */
  public URI newURI(String uri) throws URIException
  {
    String newURI = null;
    
    if( uri.startsWith( "platform:/resource") )
    {
      // The platform has been specified so keep it as is.
      newURI = uri;
    }
    else if( uri.indexOf( ":") != -1 )
    {
      // The platform uri is not allowed to contain some other protocol. 
      throw new URIException(
          StatusUtils.errorStatus(
              msg_.getMessage("MSG_INVALID_PLATFORM_URL", new Object[]{uri}) ) );
              
     }
    else if( uri.startsWith( "/") )
    {
      // The platform scheme has not been specified so we will add it.
      newURI = "platform:/resource" + uri;
    }
    
    if( newURI == null )
    {
      return new RelativeURI( uri );
    }
    else
    {
      return new EclipseURI( newURI, environment_, monitor_ );
    }
  }

  /**
   */
  public URI newURI(URI uri) throws URIException
  {
    return newURI( uri.toString() );
  }

  /**
   */
  public URI newURI(URL url) throws URIException
  {
    return newURI( url.toString() );
  }

  /**
   */
  public IStatus validate(URI uri)
  {
    IStatus status = null;
    
    try
    {
      getPathFromPlatformURI( uri.toString() );
      status = Status.OK_STATUS;
    }
    catch( URIException exc )
    {
      status = exc.getStatus(); 
    }
    
    return status;
  }
  
  /**
   * Gets the "platform:/resource" IPath given a url
   * 
   */
  public String getPathFromPlatformURI(String uri) throws URIException 
  {
    String resourceFile = null;
    URL    url          = null;
    
    try
    {
      url = new URL( uri ); 
    }
    catch( MalformedURLException exc )
    { 
    }
    
    if( url == null )
    {
      throw new URIException(
          StatusUtils.errorStatus(
              msg_.getMessage("MSG_INVALID_PLATFORM_URL", new Object[]{uri}) ) );
    }
    if( url.getProtocol().equals("platform") ) 
    {
      String resourceURL = url.getFile();
      
      if (resourceURL.startsWith("/resource")) 
      {
        resourceFile = resourceURL.substring(10);   // omit the "/resource" portion
      }
    }
    else 
    {
      throw new URIException(
          StatusUtils.errorStatus( msg_.getMessage("MSG_INVALID_PLATFORM_URL", new Object[]{url.getFile()}) ) );
    }
    
    return resourceFile;
  }
  
  /**
   * 
   * @param absolutePath an absolute IPath
   * @return returns the platform URI for this path.
   */
  public String getURLFromPath( IPath absolutePath )
  {
    return "platform:/resource" + absolutePath;
  }
}
