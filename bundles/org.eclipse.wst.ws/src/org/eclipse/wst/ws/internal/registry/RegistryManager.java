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

import java.io.File;
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
import org.eclipse.wst.ws.internal.model.v10.taxonomy.Taxonomy;
import org.eclipse.wst.ws.internal.plugin.WSPlugin;

/**
 * A typical implementation of <code>IRegistryManager</code>.
 * @author cbrealey
 * @see IRegistryManager
 */
public class RegistryManager implements IRegistryManager
{
	private static String REGISTRY = "Registry";
	private static String TAXONOMY = "Taxonomy";
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
			registryURL_ = new File(registryPathname_).toURI().toURL();
		}
		return registryURL_;
	}

	/**
	 * Returns an absolute URL computed from the absolute URL of the
	 * index XML document managed by this <code>RegistryManager</code>
	 * and the given <code>id</code>. This method is used to determine
	 * a reasonable location URL for registry and taxonomy documents
	 * when references to them are first to be added to the index XML
	 * document. This method is idempotent insofar as it will always
	 * compute the same URL for a given <code>RegistryManager</code>
	 * and a given <code>id</code>.
	 * 
	 * @param id The id of the referenced registry or taxonomy document. 
	 * @return The absolute URL to the referenced document.
	 * @throws MalformedURLException If a viable absolute URL cannot
	 * be computed.
	 */
	private URL getURL ( String id ) throws MalformedURLException
	{
		String baseUrl = ""; 
		URL indexUrl = getURL();
		String indexString = indexUrl.toString();
		int index = indexString.lastIndexOf("/"); 
		baseUrl = indexString.substring(0,index + 1);
		String urlString = baseUrl + id + "." + XML;
		return new URL(urlString);
	}

	/**
	 * Caches and returns the index model. On first call,
	 * if an index document exists, the index is loaded
	 * from it, otherwise, a new index is returned.
	 * @return The index.
	 * @throws CoreException If the index cannot be loaded for any reason.
	 */
	private Index getIndex () throws CoreException
	{
		if (index_ == null)
		{
			try
			{
				URL indexURL = getURL();
				if (RegistryService.exists(indexURL))
				{
					Resource resource = resourceFactory_.createResource(URI.createURI("*.xml"));
					resource.load(RegistryService.getInputStreamFor(indexURL),null);
					org.eclipse.wst.ws.internal.model.v10.rtindex.DocumentRoot document = (org.eclipse.wst.ws.internal.model.v10.rtindex.DocumentRoot)resource.getContents().get(0);
					index_ = document.getIndex();
				}
				else
				{
					index_ = RTIndexFactory.eINSTANCE.createIndex();
				}
			}
			catch (IOException e)
			{
				//TODO: Need a message for this Status.
				throw new CoreException(new Status(IStatus.ERROR,WSPlugin.ID,0,"",e));
			}
		}
		return index_;
	}

	/**
	 * Saves the index model to an XML document.	
	 * @throws CoreException If the save fails for any reason.
	 */
	private void saveIndex () throws CoreException
	{
		org.eclipse.wst.ws.internal.model.v10.rtindex.DocumentRoot document = RTIndexFactory.eINSTANCE.createDocumentRoot();
		document.setIndex(getIndex());
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
	 * @see org.eclipse.wst.ws.internal.registry.IRegistryManager#addTaxonomyFinder(java.lang.String, org.eclipse.wst.ws.internal.registry.ITaxonomyFinder)
	 */
	public void addTaxonomyFinder(String className, ITaxonomyFinder taxonomyFinder)
	{
		taxonomyFinders_.put(className,taxonomyFinder);
	}
	
	/**
	 * Adds the given <code>registry</code> to the index only
	 * if a registry of the same ID is not already stored there.
	 * @param registry The registry to add.
	 */
	private void addRegistryToIndex(Registry registry) throws CoreException
	{
		if (getRegistry(registry.getId()) == null)
		{
			getIndex().getRegistry().add(registry);
		}
	}
	
	/**
	 * Adds the given <code>taxonomy</code> to the index only
	 * if a taxonomy of the same ID is not already stored there.
	 * @param taxonomy The taxonomy to add.
	 */
	private void addTaxonomyToIndex(Taxonomy taxonomy) throws CoreException
	{
		if (getTaxonomy(taxonomy.getId()) == null)
		{
		 	getIndex().getTaxonomy().add(taxonomy);	
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.ws.internal.registry.IRegistryManager#saveRegistry(org.eclipse.wst.ws.internal.model.v10.registry.Registry)
	 */
	public String saveRegistry ( Registry registry ) throws CoreException
	{
		RegistryService registryService = RegistryService.instance();
		try
		{
			URL url = getURL(REGISTRY + registry.getId());
			registryService.saveRegistry(url,registry);
			Registry registryRef = registryService.newRegistryReference(registry);
			registryRef.setLocation(url.toString());
			addRegistryToIndex(registryRef);
			ITaxonomyFinder finder = (ITaxonomyFinder)taxonomyFinders_.get(registry.getClass().getName());
			if (finder != null)
			{
				Taxonomy[] taxonomies = finder.taxonomies(registry);
				for (int i=0; i<taxonomies.length; i++)
				{
					url = getURL(TAXONOMY + taxonomies[i].getId());
					registryService.saveTaxonomy(url,taxonomies[i]);
					Taxonomy taxonomyRef = registryService.newTaxonomyReference(taxonomies[i]);
					taxonomyRef.setLocation(url.toString());
					addTaxonomyToIndex(taxonomyRef);
				}
			}
		    saveIndex();
		}
		catch ( MalformedURLException me )
		{
			//TODO: Message text required.
			throw new CoreException(new Status(IStatus.ERROR,WSPlugin.ID,0,"",me));
		}
		return registry.getId();
	}

	/**
	 * Returns the Registry from the index with the given id,
	 * or null if no such entry exists in the index. 
	 * @param uri id name of the Registry
	 */
    private Registry getRegistry(String uri) throws CoreException
    {
		EList list = getIndex().getRegistry();	
		Iterator it = list.iterator();
		while(it.hasNext()){
			Registry registryRef = (Registry)it.next();
			if(registryRef.getId().equals(uri)){
				return registryRef;  
			}
		}	
        return null;
    }
	
	/**
	 * Returns the Taxonomy from the index with the given id,
	 * or null if no such entry exists in the index. 
	 * @param uri id name of the Taxonomy
	 */
    private Taxonomy getTaxonomy(String uri) throws CoreException
    {
		EList list = getIndex().getTaxonomy();	
		Iterator it = list.iterator();
		while(it.hasNext()){
			Taxonomy taxonomyRef = (Taxonomy)it.next();
			if(taxonomyRef.getId().equals(uri)){
				return taxonomyRef;
			}
		}	
        return null;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.ws.internal.registry.IRegistryManager#loadRegistry(java.lang.String)
	 */
	public Registry loadRegistry ( String uri ) throws CoreException
	{
		RegistryService registryService = RegistryService.instance();
		try
		{
			Registry registryRef = getRegistry(uri);
			if(registryRef == null) return null;
			String urlString = registryRef.getLocation();
			if(urlString == null) return null;
			URL url = new URL(urlString);
			return registryService.loadRegistry(url);
		}
		catch ( MalformedURLException me )
		{
			//TODO: Message text required.
			throw new CoreException(new Status(IStatus.ERROR,WSPlugin.ID,0,"",me));
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.ws.internal.registry.IRegistryManager#getRegistryURIs()
	 */
	public String[] getRegistryURIs () throws CoreException
	{
		EList list = getIndex().getRegistry();
		String[] registryURI = new String[list.size()];
		Iterator iterator = list.iterator();
		int i = 0;
		while(iterator.hasNext())
		{
			Registry registry = (Registry)iterator.next();  	
			registryURI[i] = registry.getId();	
			i++;  
		}
		return registryURI;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.ws.internal.registry.IRegistryManager#removeRegistry(java.lang.String, boolean)
	 */
	public void removeRegistry ( String uri, boolean deleteDocument ) throws CoreException
	{
		EList list = getIndex().getRegistry();
		Registry registryRef = getRegistry(uri);
		if(registryRef == null) return;
		list.remove(registryRef);
		saveIndex();
		if(deleteDocument)
		{
			//TODO: Implement me.
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.ws.internal.registry.IRegistryManager#saveTaxonomy(org.eclipse.wst.ws.internal.model.v10.taxonomy.Taxonomy)
	 */
	public String saveTaxonomy ( Taxonomy taxonomy ) throws CoreException
	{
		RegistryService registryService = RegistryService.instance();
		try
		{
			URL	url = getURL(TAXONOMY + taxonomy.getId());
		    registryService.saveTaxonomy(url,taxonomy);
			Taxonomy taxonomyRef = registryService.newTaxonomyReference(taxonomy);
			taxonomyRef.setLocation(url.toString());
			addTaxonomyToIndex(taxonomyRef);
			saveIndex();
		}
		catch ( MalformedURLException me )
		{
			//TODO: Message text required.
			throw new CoreException(new Status(IStatus.ERROR,WSPlugin.ID,0,"",me));
		}
		return taxonomy.getId();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.ws.internal.registry.IRegistryManager#loadTaxonomy(java.lang.String)
	 */
	public Taxonomy loadTaxonomy ( String uri ) throws CoreException
	{
		RegistryService registryService = RegistryService.instance();
		try
		{
			Taxonomy taxonomyRef = getTaxonomy(uri);
			if(taxonomyRef == null) return null;
			String urlString = taxonomyRef.getLocation();
			if(urlString == null) return null;
			URL url = new URL(urlString);
			return registryService.loadTaxonomy(url);
		}
		catch ( MalformedURLException me )
		{
			//TODO: Message text required.
			throw new CoreException(new Status(IStatus.ERROR,WSPlugin.ID,0,"",me));
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.ws.internal.registry.IRegistryManager#getTaxonomyURIs()
	 */
	public String[] getTaxonomyURIs () throws CoreException
	{
		EList list = getIndex().getTaxonomy();
		String[] taxonomyURIs = new String[list.size()];
		Iterator iterator = list.iterator();
		int i = 0;
		while(iterator.hasNext())
		{
			Taxonomy taxonomy = (Taxonomy)iterator.next(); 
			taxonomyURIs[i] = taxonomy.getId();  	
			i++;  
		}	
		return taxonomyURIs;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.ws.internal.registry.IRegistryManager#removeTaxonomy(java.lang.String, boolean)
	 */
	public void removeTaxonomy ( String uri, boolean deleteDocument ) throws CoreException
	{
		EList list = getIndex().getTaxonomy();
		Taxonomy taxonomyRef = getTaxonomy(uri);
		if(taxonomyRef == null) return;
		list.remove(taxonomyRef);
		saveIndex();
		if(deleteDocument)
		{
			//TODO: Implement me.
		}
	}
}
