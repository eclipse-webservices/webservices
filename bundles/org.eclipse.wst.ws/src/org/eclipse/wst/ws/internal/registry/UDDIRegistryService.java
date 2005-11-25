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

import org.eclipse.emf.common.util.EList;
import org.eclipse.wst.ws.internal.model.v10.registry.Registry;
import org.eclipse.wst.ws.internal.model.v10.taxonomy.Taxonomy;
import org.eclipse.wst.ws.internal.model.v10.uddiregistry.UDDIRegistry;
import org.eclipse.wst.ws.internal.model.v10.uddiregistry.UDDIRegistryFactory;
import org.eclipse.wst.ws.internal.model.v10.uddiregistry.UDDIRegistryPackage;

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
	 * @see org.eclipse.wst.ws.internal.model.v10.registry.Registry
	 * @see org.eclipse.wst.ws.internal.model.v10.taxonomy.Taxonomy
	 */
	public void addTaxonomy ( UDDIRegistry uddiRegistry, Taxonomy taxonomy )
	{
		EList list = uddiRegistry.getTaxonomies().getTaxonomy();
		list.add(taxonomy);
	}

	/**
	 * Adds references from the given <code>UDDIRegistry</code>
	 * to all of the given <code>Taxonomy</code> objects.
	 * @param uddiRegistry The <code>UDDIRegistry</code> to which references
	 * to the given <code>Taxonomy</code> objects should be added.
	 * @param taxonomies The array of <code>Taxonomy</code> objects to add.
	 * @see org.eclipse.wst.ws.internal.movel.v10.uddiregistry.UDDIRegistry
	 * @see org.eclipse.wst.ws.internal.model.v10.registry.Registry
	 * @see org.eclipse.wst.ws.internal.model.v10.taxonomy.Taxonomy
	 */
	public void addTaxonomies ( UDDIRegistry uddiRegistry, Taxonomy[] taxonomies )
	{
		EList list = uddiRegistry.getTaxonomies().getTaxonomy();
		for (int i=0; i<taxonomies.length; i++)
		{
			list.add(taxonomies[i]);
		}
	}

	/**
	 * Loads as necessary and returns an array of <code>Taxonomy</code>
	 * models referenced by the given <code>UDDIRegistry</code> model, or
	 * an empty array if there are no such taxonomies. 
	 * @param registry The <code>UDDIRegistry</code> whose
	 * <code>Taxonomy</code> models to find.
	 * @return The array, never null but possibly length zero,
	 * of <code>Taxonomy</code> models.
	 * @see org.eclipse.wst.ws.internal.movel.v10.uddiregistry.UDDIRegistry
	 * @see org.eclipse.wst.ws.internal.model.v10.registry.Registry
	 * @see org.eclipse.wst.ws.internal.model.v10.taxonomy.Taxonomy
	 */
	public Taxonomy[] getTaxonomies ( UDDIRegistry uddiRegistry )
	{
		EList list = uddiRegistry.getTaxonomies().getTaxonomy();
		return (Taxonomy[])list.toArray(new Taxonomy[0]);
	}
	
	/**
	 * Creates and returns a new <code>UDDIRegistry</code> model.
	 * @return A new <code>UDDIRegistry</code> model. Never returns null.
	 * @see Registry
	 */
	public UDDIRegistry newUDDIRegistry ()
	{
		return UDDIRegistryFactory.eINSTANCE.createUDDIRegistry();
	}
}
