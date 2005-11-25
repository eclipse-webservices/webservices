/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.ws.internal.registry;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Hashtable;
import java.util.Iterator;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.wst.ws.internal.model.v10.registry.Registry;
import org.eclipse.wst.ws.internal.model.v10.taxonomy.Taxonomy;

/**
 * A typical implementation of <code>IRegistryManager</code>
 * @author cbrealey
 * @see IRegistryManager
 */
public class RegistryManager implements IRegistryManager
{
	private static String REGISTRY = "registry";
	private static String TAXONOMY = "taxonomy";
	private static String XML = "xml";
	private Hashtable taxonomyFinders_;
	
	private URL registryURL_= null;
	private String registryPathname_ = null;

	public RegistryManager ( URL url )
	{
		registryURL_ = url;
		taxonomyFinders_ = new Hashtable();
	}

	public RegistryManager ( String pathname )
	{
		registryPathname_ = pathname;
		taxonomyFinders_ = new Hashtable();
	}

	public URL getURL () throws MalformedURLException
	{
		if (registryURL_ == null)
		{
			registryURL_ = new URL(registryPathname_);
		}
		return registryURL_;
	}

	private URL getURL ( String id ) throws MalformedURLException
	{
		String baseUrl = ""; 
		try {
			URL indexUrl = getURL();
			String indexString = indexUrl.toString();
			int index = indexString.indexOf("/"); //TODO: Shouldn't this be lastIndexOf?
			baseUrl = indexString.substring(0,index);
		} catch ( MalformedURLException me ) {
			//TODO: Why are we suppressing this exception?
		}
		String urlString = baseUrl + id + "." + XML;
		return new URL(urlString);
	}
	
	public void addTaxonomyFinder(String className, ITaxonomyFinder taxonomyFinder)
	{
		taxonomyFinders_.put(className,taxonomyFinder);
	}

	public String saveRegistry ( Registry registry )
	{
		ITaxonomyFinder finder = (ITaxonomyFinder)taxonomyFinders_.get(registry.getClass().getName());
		RegistryService registryService = RegistryService.instance();
		try
		{
			URL url = getURL(REGISTRY + registry.getId());
			registryService.saveRegistry(url,registry);
			Iterator it = finder.taxonomies(registry);
			if (it != null)
			{
				while (it.hasNext())
				{
					Taxonomy taxonomy = (Taxonomy)it.next();	
					url = null; //TODO: Seems like dead code.
					url = getURL(TAXONOMY + taxonomy.getId());
					registryService.saveTaxonomy(url,taxonomy);
				}
			}
		}
		catch ( MalformedURLException me )
		{
			//TODO: Why are we suppressing this exception?
		}
		catch ( CoreException ce )
		{
			//TODO: Why are we suppressing this exception?
		}	
		return registry.getId();
	}
	
	public Registry loadRegistry ( String uri )
	{
		//TODO: Implement me.
		return null;
	}
	
	public String[] getRegistryURIs ()
	{
		//TODO: Implement me.
		return new String[0];
	}

	public void removeRegistry ( String uri, boolean deleteDocument )
	{
		//TODO: Implement me.
	}

	public String saveTaxonomy ( Taxonomy taxonomy )
	{
		//TODO: Implement me.
		String uri = taxonomy.getId();
		return uri;
	}
	
	public Taxonomy loadTaxonomy ( String uri )
	{
		//TODO: Implement me.
		return null;
	}
	
	public String[] getTaxonomyURIs ()
	{
		//TODO: Implement me.
		return new String[0];
	}

	public void removeTaxonomy ( String uri, boolean deleteDocument )
	{
		//TODO: Implement me.
	}
}
