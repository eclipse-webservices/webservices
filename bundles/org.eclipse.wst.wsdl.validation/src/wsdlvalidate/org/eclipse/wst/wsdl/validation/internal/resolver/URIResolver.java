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

package org.eclipse.wst.wsdl.validation.internal.resolver;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.xerces.xni.XMLResourceIdentifier;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLEntityResolver;
import org.apache.xerces.xni.parser.XMLInputSource;
import org.eclipse.wst.wsdl.validation.internal.xml.XMLCatalog;

/**
 * This is the main URI resolver that calls out to all of the registered
 * external URI resolvers to locate an entity. If none of the external resolvers
 * can locate the entity the resolver will ask the internal WSDL validator XML
 * catalog to resolve the location.
 */
public class URIResolver implements IURIResolver, XMLEntityResolver
{
  //private static URIResolver instance = null;

  private List extURIResolversList = new ArrayList();

  //private URIResolverDelegate[] extResolversArray;

  //private int numExtResolvers;

  /**
   * Constructor. This class cannot be instantiated directly.
   */
  public URIResolver()
  {
    //numExtResolvers = URIResolver.extURIResolversList.size();
    //extResolversArray = (URIResolverDelegate[]) URIResolver.extURIResolversList
    //    .toArray(new URIResolverDelegate[numExtResolvers]);
  }

  /**
   * Return the one and only instance of this URIResolver.
   * 
   * @return The instance of this URIResolver.
   */
//  public static URIResolver getInstance()
//  {
//    if (instance == null)
//    {
//      instance = new URIResolver();
//    }
//    return instance;
//  }

  /**
   * Add an extension URI resolver.
   * 
   * @param uriResolver
   *          The extension URI resolver.
   */
  public void addURIResolver(IURIResolver uriResolver)
  {
    extURIResolversList.add(uriResolver);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.wsdl.validate.internal.resolver.IURIResolver#resolve(java.lang.String,
   *      java.lang.String, java.lang.String)
   */
  public String resolve(String baseLocation, String publicId, String systemId)
  {
    String result = null;
    Iterator resolverIter = extURIResolversList.iterator();
    while(resolverIter.hasNext())
    {
      IURIResolver resolver = (IURIResolver)resolverIter.next();
      if (resolver == null)
      {
        continue;
      }
      result = resolver.resolve(baseLocation, publicId, systemId);
      if (result == null || !result.equals(systemId))
      {
        break;
      }
    }

    // If we haven't been able to locate the result yet ask the internal XML
    // catalog.
    if (result == null)
    {
      result = XMLCatalog.getInstance().resolveEntityLocation(publicId, systemId);
    }
    if(result == null)
    {
      result =  normalize(baseLocation, systemId);
    }
    return result;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.apache.xerces.xni.parser.XMLEntityResolver#resolveEntity(org.apache.xerces.xni.XMLResourceIdentifier)
   */
  public XMLInputSource resolveEntity(XMLResourceIdentifier resourceIdentifier) throws XNIException, IOException
  {
    String publicId = resourceIdentifier.getPublicId();
    String systemId = resourceIdentifier.getLiteralSystemId();
    if (publicId == null || publicId.equals(""))
    {
      publicId = resourceIdentifier.getNamespace();
    }
    String result = resolve(resourceIdentifier.getBaseSystemId(), publicId, systemId);
    XMLInputSource xmlInputSource = null;
    if (result != null)
    {
      try
      {
        URL url = new URL(result);
        InputStream is = url.openStream();
        xmlInputSource = new XMLInputSource(publicId, result, result, is, null);
      }
      catch (FileNotFoundException e)
      {
        //System.out.println(e);
      }
    }
    return xmlInputSource;
  }
  
  protected String normalize(String baseLocation, String systemId)
	{
	  // If no systemId has been specified there is nothing to do
	  // so return null;
	  if(systemId == null)
	    return null;
		String result = systemId;
		// normalize the URI
		URI systemURI = URI.create(systemId);
		if (!systemURI.isAbsolute())
		{
			URI baseURI = URI.create(baseLocation);
			try
			{
			  result = baseURI.resolve(systemURI).toString();
			}
			catch(IllegalArgumentException e)
			{}
			
		}
		return result;
	}
}