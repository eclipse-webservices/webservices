/*******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060517   131582 mahutch@ca.ibm.com - Mark Hutchinson
 * 20060906   141796 gilberta@ca.ibm.com - Gilbert Andrews
 *******************************************************************************/

package org.eclipse.wst.ws.internal.registry;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

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
			registryURL_ = new File(registryPathname_).toURL();
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

	/* (non-Javadoc)
	 * @see org.eclipse.wst.ws.internal.registry.IRegistryManager#refreshManager()
	 */
	public void refreshManager ()
	{
		index_ = null;
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
	 * Adds the given <code>registry</code> as-is, whether full model
	 * or reference, to the index only if a registry with the same ID
	 * or reference is not already stored there.
	 * @param registry The registry to add.
	 */
	private void addRegistryToIndex(Registry registry) throws CoreException
	{
		String id = registry.getId();
		if (id == null) id = registry.getRef();
		if (id != null && getRegistry(id) == null)
		{
			getIndex().getRegistry().add(registry);
		}
	}
	
	/**
	 * Adds the given <code>taxonomy</code> as-is, whether full model
	 * or reference, to the index only if a taxonomy with the same ID
	 * or reference is not already stored there.
	 * @param taxonomy The taxonomy to add.
	 */
	private void addTaxonomyToIndex(Taxonomy taxonomy) throws CoreException
	{
		String id = taxonomy.getId();
		if (id == null) id = taxonomy.getRef();
		if (id != null && getTaxonomy(id) == null)
		{
		 	getIndex().getTaxonomy().add(taxonomy);	
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.ws.internal.registry.IRegistryManager#saveRegistry(org.eclipse.wst.ws.internal.model.v10.registry.Registry)
	 */
	public Registry saveRegistry ( Registry registry ) throws CoreException
	{
		RegistryService registryService = RegistryService.instance();
		Registry registryRef = null;
		try
		{
			URL url = getURL(REGISTRY + registry.getId());
			registryService.saveRegistry(url,registry);
			registryRef = registryService.newRegistryReference(registry);
			registryRef.setLocation(url.toString());
			addRegistryToIndex(registryRef);
			saveIndex();
			/*
			 * TODO: The following pile of code is commented out
			 * since there isn't a reliable way under the current
			 * design to save taxonomy models held in a registry model
			 * 
			ITaxonomyFinder finder = (ITaxonomyFinder)taxonomyFinders_.get(registry.getClass().getName());
			if (finder != null)
			{
				Taxonomy[] taxonomies = finder.taxonomies(registry);
				for (int i=0; i<taxonomies.length; i++)
				{
					String id = taxonomies[i].getId();
					String ref = taxonomies[i].getRef();
					if (id != null)
					{
						URL taxonomyURL = getURL(TAXONOMY + id);
						registryService.saveTaxonomy(taxonomyURL,taxonomies[i]);
						Taxonomy taxonomyRef = registryService.newTaxonomyReference(taxonomies[i]);
						taxonomyRef.setLocation(taxonomyURL.toString());
						addTaxonomyToIndex(taxonomyRef);
					}
					else if (ref != null)
					{
						if (taxonomies[i].getLocation() == null)
						{
							String location = null;
							Taxonomy taxonomy = getTaxonomy(ref);
							if (taxonomy != null) location = taxonomy.getLocation();
							if (location == null) location = getURL(TAXONOMY + ref).toString();
							taxonomies[i].setLocation(location);
							if (taxonomy == null)
							{
								addTaxonomyToIndex(registryService.newTaxonomyReference(taxonomies[i]));
							}
						}
					}
				}
			}
		    */
		}
		catch ( MalformedURLException me )
		{
			//TODO: Message text required.
			throw new CoreException(new Status(IStatus.ERROR,WSPlugin.ID,0,"",me));
		}
		return registryService.newRegistryReference(registryRef);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.ws.internal.registry.IRegistryManager#loadRegistry(java.lang.String)
	 */
	public Registry loadRegistry ( String uri ) throws CoreException
	{
		RegistryService registryService = RegistryService.instance();
		try
		{
			Registry registry = getRegistry(uri);
			if (registry == null) return null;

			// If the Registry has an ID, it's a full model
			// inlined within the Index and is returned as-is.
			if (registry.getId() != null) return registry;

			// Otherwise it's a reference to a full model
			// which we load from the reference's location.
			String urlString = registry.getLocation();
			if (urlString == null) return null;
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
	 * @see org.eclipse.wst.ws.internal.registry.IRegistryManager#loadRegistries(java.lang.String[])
	 */
	public Registry[] loadRegistries ( String[] uris ) throws CoreException
	{
		List list = new ArrayList(uris.length);
		for (int i=0; i<uris.length; i++)
		{
			Registry registry = loadRegistry(uris[i]);
			if (registry != null)
			{
				list.add(registry);
			}
		}
		return (Registry[])list.toArray(new Registry[0]);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.ws.internal.registry.IRegistryManager#getRegistryURIs()
	 */
	public String[] getRegistryURIs () throws CoreException
	{
		EList list = getIndex().getRegistry();
		List registryURIs = new ArrayList(list.size());
		Iterator iterator = list.iterator();
		while(iterator.hasNext())
		{
			// Each Registry found in the index may be
			// either a full model or a reference to one.
			Registry registry = (Registry)iterator.next();
			if (registry.getId() != null)
			{
				registryURIs.add(registry.getId());
			}
			else if (registry.getRef() != null)
			{
				registryURIs.add(registry.getRef());
			}
		}	
		return (String[])registryURIs.toArray(new String[0]);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.ws.internal.registry.IRegistryManager#removeRegistry(java.lang.String, boolean)
	 */
	public void removeRegistry ( String uri, boolean deleteDocument ) throws CoreException
	{
		EList list = getIndex().getRegistry();
		Registry registry = getRegistry(uri);
		if (registry != null)
		{
			list.remove(registry);
			saveIndex();
			// The identified Registry may be either a full model
			// (ie. inlined in the Index) or a reference to one.
			// Only in the latter case is there a file to delete.
			if (deleteDocument && registry.getLocation() != null)
			{
				//TODO: Implement me.
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.ws.internal.registry.IRegistryManager#saveTaxonomy(org.eclipse.wst.ws.internal.model.v10.taxonomy.Taxonomy)
	 */
	public Taxonomy saveTaxonomy ( Taxonomy taxonomy ) throws CoreException
	{
		RegistryService registryService = RegistryService.instance();
		Taxonomy taxonomyRef = null;
		try
		{
			URL	url = getURL(TAXONOMY + taxonomy.getId());
		    registryService.saveTaxonomy(url,taxonomy);
			taxonomyRef = registryService.newTaxonomyReference(taxonomy);
			taxonomyRef.setLocation(url.toString());
			addTaxonomyToIndex(taxonomyRef);
			saveIndex();
		}
		catch ( MalformedURLException me )
		{
			//TODO: Message text required.
			throw new CoreException(new Status(IStatus.ERROR,WSPlugin.ID,0,"",me));
		}
		return registryService.newTaxonomyReference(taxonomyRef);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.ws.internal.registry.IRegistryManager#loadTaxonomy(java.lang.String)
	 */
	public Taxonomy loadTaxonomy ( String uri ) throws CoreException
	{
		RegistryService registryService = RegistryService.instance();
		try
		{
			Taxonomy taxonomy = getTaxonomy(uri);
			if (taxonomy == null) return null;

			// If the Taxonomy has an ID, it's a full model
			// inlined within the Index and is returned as-is.
			if (taxonomy.getId() != null) return taxonomy;

			// Otherwise it's a reference to a full model
			// which we load from the reference's location.
			String urlString = taxonomy.getLocation();
			if (urlString == null) return null;
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
	 * @see org.eclipse.wst.ws.internal.registry.IRegistryManager#loadTaxonomies(java.lang.String[])
	 */
	public Taxonomy[] loadTaxonomies ( String[] uris ) throws CoreException
	{
		List list = new ArrayList(uris.length);
		for (int i=0; i<uris.length; i++)
		{
			Taxonomy taxonomy = loadTaxonomy(uris[i]);
			if (taxonomy != null)
			{
				list.add(taxonomy);
			}
		}
		return (Taxonomy[])list.toArray(new Taxonomy[0]);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.ws.internal.registry.IRegistryManager#getTaxonomyURIs()
	 */
	public String[] getTaxonomyURIs () throws CoreException
	{
		EList list = getIndex().getTaxonomy();
		List taxonomyURIs = new ArrayList(list.size());
		Iterator iterator = list.iterator();
		while(iterator.hasNext())
		{
			// Each Taxonomy found in the index may be
			// either a full model or a reference to one.
			Taxonomy taxonomy = (Taxonomy)iterator.next();
			if (taxonomy.getId() != null)
			{
				taxonomyURIs.add(taxonomy.getId());
			}
			else if (taxonomy.getRef() != null)
			{
				taxonomyURIs.add(taxonomy.getRef());
			}
		}	
		return (String[])taxonomyURIs.toArray(new String[0]);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.ws.internal.registry.IRegistryManager#removeTaxonomy(java.lang.String, boolean)
	 */
	public void removeTaxonomy ( String uri, boolean deleteDocument ) throws CoreException
	{
		EList list = getIndex().getTaxonomy();
		Taxonomy taxonomy = getTaxonomy(uri);
		if (taxonomy != null)
		{
			list.remove(taxonomy);
			saveIndex();
			// The identified Taxonomy may be either a full model
			// (ie. inlined in the Index) or a reference to one.
			// Only in the latter case is there a file to delete.
			if (deleteDocument && taxonomy.getLocation() != null)
			{
				//TODO: Implement me.
			}
		}
	}

	/**
	 * Returns the Registry from the index whose ID or Reference
	 * matches the given URI. As such, this method may return a
	 * full model or a reference to a full model.
	 * @param uri The URI identifier of the Registry
	 * @return The <code>Registry</code> object whose ID or reference
	 * matches the given <code>uri</code>, or null if none match.
	 */
    private Registry getRegistry ( String uri ) throws CoreException
    {
		EList list = getIndex().getRegistry();	
		Iterator it = list.iterator();
		while (it.hasNext()){
			Registry registry = (Registry)it.next();
			if (uri.equals(registry.getId()) || uri.equals(registry.getRef())){
				return registry;  
			}
		}	
        return null;
    }
	
	/**
	 * Returns the Taxonomy from the index whose ID or Reference
	 * matches the given URI. As such, this method may return a
	 * full model or a reference to a full model.
	 * @param uri The URI identifier of the Taxonomy
	 * @return The <code>Taxonomy</code> object whose ID or reference
	 * matches the given <code>uri</code>, or null if none match.
	 */
    private Taxonomy getTaxonomy ( String uri ) throws CoreException
    {
		EList list = getIndex().getTaxonomy();	
		Iterator it = list.iterator();
		while (it.hasNext()){
			Taxonomy taxonomy = (Taxonomy)it.next();
			if (uri.equals(taxonomy.getId()) || uri.equals(taxonomy.getRef())){
				return taxonomy;
			}
		}	
        return null;
	}
}
