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

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.wst.command.env.core.common.MessageUtils;
import org.eclipse.wst.command.env.core.common.SimpleStatus;
import org.eclipse.wst.command.env.core.common.Status;


public class RelativeURI implements URI
{
  protected String uri_;
  
  public RelativeURI( String uri )
  {
    uri_ = uri;   
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.env.uri.URI#append(org.eclipse.env.uri.URI)
   */
  public URI append(URI relativeURI) throws URIException
  {
    if( !relativeURI.isRelative()) 
     {
      MessageUtils msg = new MessageUtils( "org.eclipse.wst.command.env.core.environment", new Dummy() );
      
      throw new URIException( 
          new SimpleStatus( "RelativeURI", 
              msg.getMessage( "MSG_URI_NOT_RELATIVE", new Object[]{ relativeURI.toString() }),
              Status.ERROR ));
    }
    
    String newURI = uri_ + "/" + relativeURI.toString();
    
    return getURIScheme().newURI( newURI );
  }

  /* (non-Javadoc)
   * @see org.eclipse.env.uri.URI#asFile()
   */
  public File asFile() 
  {
    return null;
  }

  /* (non-Javadoc)
   * @see org.eclipse.env.uri.URI#asString()
   */
  public String asString()
  {
    return uri_;
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.env.uri.URI#toString()
   */
  public String toString()
  {
    return uri_;
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.env.uri.URI#asURL()
   */
  public URL asURL() throws URIException
  {
    URL url = null;
    
    try
    {
      url = new URL( uri_ );
    }
    catch( MalformedURLException exc )
    {
      throw new URIException( 
              new SimpleStatus( "RelativeURI", exc.getMessage(), Status.ERROR ),
            this ); 
    }
    
    return url;
  }

  /* (non-Javadoc)
   * @see org.eclipse.env.uri.URI#erase()
   */
  public void erase() throws URIException
  {
  }

  /* (non-Javadoc)
   * @see org.eclipse.env.uri.URI#getInputStream()
   */
  public InputStream getInputStream() throws URIException
  {
    return null;
  }

  /* (non-Javadoc)
   * @see org.eclipse.env.uri.URI#getOutputStream()
   */
  public OutputStream getOutputStream() throws URIException
  {
    return null;
  }

  /* (non-Javadoc)
   * @see org.eclipse.env.uri.URI#getURIScheme()
   */
  public URIScheme getURIScheme()
  {
    return new RelativeScheme();
  }

  /* (non-Javadoc)
   * @see org.eclipse.env.uri.URI#isAvailableAsFile()
   */
  public boolean isAvailableAsFile()
  {
    return false;
  }

  /* (non-Javadoc)
   * @see org.eclipse.env.uri.URI#isAvailableAsURL()
   */
  public boolean isAvailableAsURL()
  {
    return true;
  }

  /* (non-Javadoc)
   * @see org.eclipse.env.uri.URI#isHierarchical()
   */
  public boolean isHierarchical()
  {
    return true;
  }

  /* (non-Javadoc)
   * @see org.eclipse.env.uri.URI#isLeaf()
   */
  public boolean isLeaf()
  {
    return false;
  }

  /* (non-Javadoc)
   * @see org.eclipse.env.uri.URI#isPresent()
   */
  public boolean isPresent()
  {
    return false;
  }

  /* (non-Javadoc)
   * @see org.eclipse.env.uri.URI#isReadable()
   */
  public boolean isReadable()
  {
    return false;
  }

  /* (non-Javadoc)
   * @see org.eclipse.env.uri.URI#isRelative()
   */
  public boolean isRelative()
  {
    return true;
  }

  /* (non-Javadoc)
   * @see org.eclipse.env.uri.URI#isWritable()
   */
  public boolean isWritable()
  {
    return false;
  }

  /* (non-Javadoc)
   * @see org.eclipse.env.uri.URI#list()
   */
  public URI[] list() throws URIException
  {
    return new URI[0];
  }

  /* (non-Javadoc)
   * @see org.eclipse.env.uri.URI#list(org.eclipse.env.uri.URIFilter)
   */
  public URI[] list(URIFilter uriFilter) throws URIException
  {
    return new URI[0];
  }

  /* (non-Javadoc)
   * @see org.eclipse.env.uri.URI#parent()
   */
  public URI parent() throws URIException
  {
    int lastSlash  = uri_.lastIndexOf( '/' );
    int firstSlash = uri_.indexOf( '/' );
    
    // If there is a parent, then it must start with a slash
    // and end with a slash.
    if( lastSlash == -1 || firstSlash == -1 ) return null;
        
    return getURIScheme().newURI( uri_.substring(0, lastSlash ) );
  }

  /* (non-Javadoc)
   * @see org.eclipse.env.uri.URI#rename(org.eclipse.env.uri.URI)
   */
  public void rename(URI newURI) throws URIException
  {
    uri_ = newURI.toString();
  }

  /* (non-Javadoc)
   * @see org.eclipse.env.uri.URI#touchFolder()
   */
  public void touchFolder() throws URIException
  {
  }

  /* (non-Javadoc)
   * @see org.eclipse.env.uri.URI#touchLeaf()
   */
  public void touchLeaf() throws URIException
  {
  }

  /* (non-Javadoc)
   * @see org.eclipse.env.uri.URI#visit(org.eclipse.env.uri.URIVisitor, org.eclipse.env.uri.URIFilter)
   */
  public void visit(URIVisitor uriVisitor, URIFilter uriFilter)
    throws URIException
  {
    boolean continueVisit = true;
    
    // If the filter accepts this we will visit it.
    if( uriFilter.accepts( this ) )
    {
      continueVisit = uriVisitor.visit( this );  
    }
       
    URI[] children  = list();
    
    for( int index = 0; index < children.length && continueVisit; index++ )
    {
      children[index].visit( uriVisitor, uriFilter );
    }  
  }

  /* (non-Javadoc)
   * @see org.eclipse.env.uri.URI#visit(org.eclipse.env.uri.URIVisitor)
   */
  public void visit(URIVisitor uriVisitor) throws URIException
  {
    boolean continueVisit = uriVisitor.visit( this );  
    
    URI[] children  = list();
    
    for( int index = 0; index < children.length && continueVisit; index++ )
     {
      children[index].visit( uriVisitor );
    }  
  }
    
  private class Dummy 
  {
  }
}
