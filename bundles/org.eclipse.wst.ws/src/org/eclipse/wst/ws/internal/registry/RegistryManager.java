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

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Hashtable;
import java.util.Iterator;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.wst.ws.internal.model.v10.registry.Registry;
import org.eclipse.wst.ws.internal.model.v10.rtindex.Index;
import org.eclipse.wst.ws.internal.model.v10.rtindex.RTIndexFactory;
import org.eclipse.wst.ws.internal.model.v10.rtindex.impl.RTIndexFactoryImpl;
import org.eclipse.wst.ws.internal.model.v10.taxonomy.Taxonomy;
import org.eclipse.wst.ws.internal.plugin.WSPlugin;

/**
 * A typical implementation of <code>IRegistryManager</code>.
 * @author cbrealey
 * @see IRegistryManager
 */
public class RegistryManager implements IRegistryManager
{
	private static String REGISTRY = "registry";
	private static String TAXONOMY = "taxonomy";
	private static String XML = "xml";
	private Hashtable taxonomyFinders_;
	private Index index_;
	private GenericResourceFactory resourceFactory_ = new GenericResourceFactory();
	private URL registryURL_= null;
	private String registryPathname_ = null;

	/**
	 * Constructs a new RegistryManager for the index XML file
	 * at the given <code>url</code>.
	 * @param url The URL to the index XML file.
	 */
	RegistryManager ( URL url )
	{
		registryURL_ = url;
		taxonomyFinders_ = new Hashtable();
	}

	/**
	 * Constructs a new RegistryManager for the index XML file
	 * at the given <code>pathname</code>.
	 * @param pathname The pathname to the index XML file.
	 */
	RegistryManager ( String pathname )
	{
		registryPathname_ = pathname;
		taxonomyFinders_ = new Hashtable();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.ws.internal.registry.IRegistryManager#getURL()
	 */
	public URL getURL () throws MalformedURLException
	{
		if (registryURL_ == null)
		{
			registryURL_ = new URL(registryPathname_);
		}
		return registryURL_;
	}

	/**
	 * TODO: Document me. 
	 * @param id
	 * @return
	 * @throws MalformedURLException
	 */
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
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.ws.internal.registry.IRegistryManager#addTaxonomyFinder(java.lang.String, org.eclipse.wst.ws.internal.registry.ITaxonomyFinder)
	 */
	public void addTaxonomyFinder(String className, ITaxonomyFinder taxonomyFinder)
	{
		taxonomyFinders_.put(className,taxonomyFinder);
	}
	
	/**
	 * TODO: Document me!
	 * @param registry
	 */
	private void addRegistryToIndex(Registry registry)
	{
		EList list = index_.getRegistry();	
		if(!list.contains(registry.getId())){
			list.add(registry.getId());
			//TODO: The following line should not be necessary. Also, new code should never use deprecated stuff.
			index_.eSet(RTIndexFactoryImpl.getPackage().getIndex_Registry(),list);
		}
	}
	
	/**
	 * TODO: Document me!
	 * @param taxonomy
	 */
	private void addTaxonomyToIndex(Taxonomy taxonomy)
	{
		EList list = index_.getTaxonomy();	
		if(!list.contains(taxonomy.getId())){
			list.add(taxonomy.getId());
			//TODO: The following line should not be necessary. Also, new code should never use deprecated stuff.
			index_.eSet(RTIndexFactoryImpl.getPackage().getIndex_Taxonomy(),list);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.ws.internal.registry.IRegistryManager#saveRegistry(org.eclipse.wst.ws.internal.model.v10.registry.Registry)
	 */
	public String saveRegistry ( Registry registry )
	{
		ITaxonomyFinder finder = (ITaxonomyFinder)taxonomyFinders_.get(registry.getClass().getName());
		RegistryService registryService = RegistryService.instance();
		try
		{
			URL url = getURL(REGISTRY + registry.getId());
			registryService.saveRegistry(url,registry);
			addRegistryToIndex(registry);
			Iterator it = finder.taxonomies(registry);
			if (it != null)
			{
				while (it.hasNext())
				{
					Taxonomy taxonomy = (Taxonomy)it.next();	
					url = null; //TODO: Seems like dead code.
					url = getURL(TAXONOMY + taxonomy.getId());
					registryService.saveTaxonomy(url,taxonomy);
					addTaxonomyToIndex(taxonomy);
				}
			}
		    saveIndex();
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

	/**
	 * Saves the index model to an XML document.	
	 * @throws CoreException If the save failes for any reason.
	 */
	private void saveIndex () throws CoreException
	{
		org.eclipse.wst.ws.internal.model.v10.rtindex.DocumentRoot document = RTIndexFactory.eINSTANCE.createDocumentRoot();
		document.setIndex(index_);
		Resource resource = resourceFactory_.createResource(URI.createURI("*.xml"));
		resource.getContents().add(document);
		try
		{
			resource.save(RegistryService.getOutputStreamFor(getURL()),null);
		}
		catch (IOException e)
		{
			//TODO: Need a message for this Status.
			throw new CoreException(new Status(IStatus.ERROR,WSPlugin.ID,0,"",e));
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.ws.internal.registry.IRegistryManager#loadRegistry(java.lang.String)
	 */
	public Registry loadRegistry ( String uri )
	{
		RegistryService registryService = RegistryService.instance();
		try
		{
			//TODO: The location URL should be looked up in the index.
			URL url = getURL(REGISTRY + uri);
			return registryService.loadRegistry(url);
		}
		catch ( MalformedURLException me )
		{
			//TODO: Why are we suppressing this exception?
		}
		catch ( CoreException ce )
		{
			//TODO: Why are we suppressing this exception?
		}	
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.ws.internal.registry.IRegistryManager#getRegistryURIs()
	 */
	public String[] getRegistryURIs ()
	{
		EList list = index_.getRegistry();
		String[] registryURI = new String[list.size()];
		Iterator iterator = list.iterator();
		int i = 0;
		while(iterator.hasNext())
		{
			registryURI[i] = (String)iterator.next();  	
			i++;  
		}
		return registryURI;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.ws.internal.registry.IRegistryManager#removeRegistry(java.lang.String, boolean)
	 */
	public void removeRegistry ( String uri, boolean deleteDocument )
	{
		EList list = index_.getRegistry();
		list.remove(uri);
		//TODO: The following line should not be necessary. Also, new code should never use deprecated stuff.
		index_.eSet(RTIndexFactoryImpl.getPackage().getIndex_Registry(),list);
		if(deleteDocument)
		{
			//TODO: Implement me.
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.ws.internal.registry.IRegistryManager#saveTaxonomy(org.eclipse.wst.ws.internal.model.v10.taxonomy.Taxonomy)
	 */
	public String saveTaxonomy ( Taxonomy taxonomy )
	{
		RegistryService registryService = RegistryService.instance();
		try
		{
			URL	url = getURL(TAXONOMY + taxonomy.getId());
		    registryService.saveTaxonomy(url,taxonomy);
		    addTaxonomyToIndex(taxonomy);
			saveIndex();
		}
		catch ( MalformedURLException me )
		{
			//TODO: Why are we suppressing this exception?
		}
		catch ( CoreException ce )
		{
			//TODO: Why are we suppressing this exception?
		}	
		return taxonomy.getId();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.ws.internal.registry.IRegistryManager#loadTaxonomy(java.lang.String)
	 */
	public Taxonomy loadTaxonomy ( String uri )
	{
		RegistryService registryService = RegistryService.instance();
		try
		{
			//TODO: The location URL should be looked up in the index.
			URL url = getURL(TAXONOMY + uri);
			return registryService.loadTaxonomy(url);
		}
		catch ( MalformedURLException me )
		{
			//TODO: Why are we suppressing this exception?
		}
		catch ( CoreException ce )
		{
			//TODO: Why are we suppressing this exception?
		}	
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.ws.internal.registry.IRegistryManager#getTaxonomyURIs()
	 */
	public String[] getTaxonomyURIs ()
	{
		EList list = index_.getTaxonomy();
		String[] taxonomyURIs = new String[list.size()];
		Iterator iterator = list.iterator();
		int i = 0;
		while(iterator.hasNext())
		{
			taxonomyURIs[i] = (String)iterator.next();  	
			i++;  
		}	
		return taxonomyURIs;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.ws.internal.registry.IRegistryManager#removeTaxonomy(java.lang.String, boolean)
	 */
	public void removeTaxonomy ( String uri, boolean deleteDocument )
	{
		EList list = index_.getTaxonomy();
		list.remove(uri);
		//TODO: The following line should not be necessary. Also, new code should never use deprecated stuff.
		index_.eSet(RTIndexFactoryImpl.getPackage().getIndex_Taxonomy(),list);
		if(deleteDocument)
		{
			//TODO: Implement me.
		}
	}
}
