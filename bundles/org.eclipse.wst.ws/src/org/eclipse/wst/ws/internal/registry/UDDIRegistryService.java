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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.EList;
import org.eclipse.wst.ws.internal.model.v10.taxonomy.Taxonomy;
import org.eclipse.wst.ws.internal.model.v10.uddiregistry.Taxonomies;
import org.eclipse.wst.ws.internal.model.v10.uddiregistry.UDDIRegistry;
import org.eclipse.wst.ws.internal.model.v10.uddiregistry.UDDIRegistryFactory;
import org.eclipse.wst.ws.internal.model.v10.uddiregistry.UDDIRegistryPackage;
import org.eclipse.wst.ws.internal.plugin.WSPlugin;

/**
 * UDDIRegistryService is an adjunct to <code>RegistryService</code>.
 * Call the <code>RegistryService.instance()</code> static method to
 * get the singleton instance of this class.
 * <p>
 * UDDIRegistryService provides utility methods to ease the creation
 * of new <code>UDDIRegistry</code> models and the management of their
 * relationships to <code>Taxonomy</code> models.
 * <p>
 * Utility methods for loading and saving <code>Registry</code> and
 * <code>Taxonomy</code> models are on <code>RegistryService</code>.
 * Methods for managing an indexed family of <code>Registry</code> and
 * <code>Taxonomy</code> models are on <code>IRegistryManager</code>.
 * <p>
 * A typical use of <code>UDDIRegistryService</code> and its related
 * classes to create and save a new family of models is:
 * <blockquote>
 * <code>
 * RegistryService registryService = RegistryService.instance();
 * UDDIRegistryService uddiRegistryService = UDDIRegistryService.instance();
 * UDDIRegistry uddiRegistry = uddiRegistryService.newUDDIRegistry();
 * // build up your registry model here
 * Taxonomy taxonomy = registryService.newTaxonomy();
 * // build up your taxonomy model here
 * uddiRegistryService.addTaxonomy(uddiRegistry,taxonomy);
 * IRegistryManager registryManager = registryService.getDefaultRegistryManager();
 * registryManager.saveRegistry(uddiRegistry);
 * </code>
 * </blockquote>
 * @see #instance()
 * @see RegistryService
 * @see IRegistryManager
 * @see UDDIRegistry
 * @see Taxonomy
 * @author cbrealey
 */
public class UDDIRegistryService
{
	private static UDDIRegistryService instance_;

	/**
	 * A UDDIRegistryService cannot be directly constructed.
	 * Use @link #instance() to get the singleton of this class.
	 */
	private UDDIRegistryService ()
	{
		UDDIRegistryPackage.eINSTANCE.getClass();
	}

	/**
	 * Returns the singleton of this class.
	 * @return The singleton of this class.
	 */
	public static UDDIRegistryService instance ()
	{
		if (instance_ == null)
		{
			instance_ = new UDDIRegistryService();
		}
		return instance_;
	}
	
	/**
	 * Adds a reference from the given <code>UDDIRegistry</code>
	 * to the given <code>Taxonomy</code> object.
	 * @param uddiRegistry The <code>UDDIRegistry</code> to which the reference
	 * to the given <code>Taxonomy</code> object should be added.
	 * @param taxonomy The <code>Taxonomy</code> object to add.
	 * @see org.eclipse.wst.ws.internal.movel.v10.uddiregistry.UDDIRegistry
	 * @see org.eclipse.wst.ws.internal.model.v10.taxonomy.Taxonomy
	 */
	public void addTaxonomy ( UDDIRegistry uddiRegistry, Taxonomy taxonomy )
	{
		addTaxonomies(uddiRegistry,new Taxonomy[] {taxonomy});
	}

	/**
	 * Adds the given <code>Taxonomy</code> objects as-is
	 * (whether they are references or full models)
	 * to the given <code>UDDIRegistry</code>.
	 * @param uddiRegistry The <code>UDDIRegistry</code> to which
	 * the given <code>Taxonomy</code> objects should be added.
	 * @param taxonomies The array of <code>Taxonomy</code> objects to add.
	 * @see org.eclipse.wst.ws.internal.movel.v10.uddiregistry.UDDIRegistry
	 * @see org.eclipse.wst.ws.internal.model.v10.taxonomy.Taxonomy
	 */
	public void addTaxonomies ( UDDIRegistry uddiRegistry, Taxonomy[] taxonomies )
	{
		if (taxonomies.length > 0)
		{
			Taxonomies taxonomySet = uddiRegistry.getTaxonomies();
			if (taxonomySet == null)
			{
				taxonomySet = UDDIRegistryFactory.eINSTANCE.createTaxonomies();
				uddiRegistry.setTaxonomies(taxonomySet);
			}
			EList list = taxonomySet.getTaxonomy();
			for (int i=0; i<taxonomies.length; i++)
			{
				list.add(taxonomies[i]);
			}
		}
	}
	
	/**
	 * Returns an array of URI identifiers to <code>Taxonomy</code>
	 * models contained in or referenced by the <code>uddiRegistry</code>.
	 * Passing the result to <code>IRegistryManager.loadTaxonomies()</code>
	 * is a handy way to resolve and load the full taxonomy models used
	 * by the UDDI registry.
	 * 
	 * @return An array of URI identifiers, possibly empty but never null,
	 * to <code>Taxonomy</code> models.
	 * @see Taxonomy
	 * @see IRegistryManager#loadTaxonomies(String[])
	 */
	public String[] getTaxonomyURIs ( UDDIRegistry uddiRegistry )
	{
		Taxonomies taxonomies = uddiRegistry.getTaxonomies();
		if (taxonomies == null) return new String[0];
		EList taxonomyList = taxonomies.getTaxonomy();
		List taxonomyURIs = new ArrayList(taxonomyList.size());
		Iterator i = taxonomyList.iterator();
		while (i.hasNext())
		{
			Taxonomy taxonomy = (Taxonomy)i.next();
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

	/**
	 * Returns an array of full <code>Taxonomy</code> models
	 * contained in or referenced by the given <code>uddiRegistry</code>.
	 * Inline taxonomies are returned as-is. References to taxonomies are
	 * resolved by following the locations of the references and loading
	 * the models at the location URLs.
	 * This method never returns null, but may return an empty array.
	 * @param registry The <code>UDDIRegistry</code> whose
	 * <code>Taxonomy</code> models to find.
	 * @return The array, never null but possibly length zero,
	 * of <code>Taxonomy</code> models.
	 * @see org.eclipse.wst.ws.internal.movel.v10.uddiregistry.UDDIRegistry
	 * @see org.eclipse.wst.ws.internal.model.v10.taxonomy.Taxonomy
	 * @see #getTaxonomyURIs(UDDIRegistry)
	 * @see IRegistryManager#loadTaxonomies(String[])
	 * @deprecated This method suffers from a lack of awareness of the
	 * Index XML document that is often responsible for mapping URIs
	 * to location URLs. Use
	 * <code>IRegistryManager.loadTaxonomies(getTaxonomyURIs(uddiRegistry))</code>
	 * instead.
	 */
	public Taxonomy[] getTaxonomies ( UDDIRegistry uddiRegistry ) throws CoreException
	{
		RegistryService registryService = RegistryService.instance();
		Taxonomies taxonomies = uddiRegistry.getTaxonomies();
		if (taxonomies == null) return new Taxonomy[0];
		EList literalTaxonomyList = taxonomies.getTaxonomy();
		List resolvedTaxonomyList = new ArrayList(literalTaxonomyList.size());
		Iterator i = literalTaxonomyList.iterator();
		while (i.hasNext())
		{
			Taxonomy literalTaxonomy = (Taxonomy)i.next();
			if (literalTaxonomy.getId() != null)
			{
				resolvedTaxonomyList.add(literalTaxonomy);
			}
			else if (literalTaxonomy.getLocation() != null)
			{
				try
				{
					Taxonomy resolvedTaxonomy = registryService.loadTaxonomy(new URL(literalTaxonomy.getLocation()));
					resolvedTaxonomyList.add(resolvedTaxonomy);
				}
				catch (MalformedURLException e)
				{
					//TODO: Need message text here
					throw new CoreException(new Status(IStatus.ERROR, WSPlugin.ID, 0, "", e));
				}
			}
		}
		return (Taxonomy[])resolvedTaxonomyList.toArray(new Taxonomy[0]);
	}
	
	/**
	 * Creates and returns a new <code>UDDIRegistry</code> model.
	 * @return A new <code>UDDIRegistry</code> model, never null.
	 */
	public UDDIRegistry newUDDIRegistry ()
	{
		return UDDIRegistryFactory.eINSTANCE.createUDDIRegistry();
	}
}
