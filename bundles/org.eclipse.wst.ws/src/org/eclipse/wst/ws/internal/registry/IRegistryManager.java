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

import org.eclipse.core.runtime.CoreException;
import org.eclipse.wst.ws.internal.model.v10.registry.Registry;
import org.eclipse.wst.ws.internal.model.v10.taxonomy.Taxonomy;

/**
 * An IRegistryManager manages a family of <code>Registry</code>
 * and <code>Taxonomy</code> models persisted within some
 * addressable location such as under a directory in the
 * filesystem or under a folder in the Eclipse workspace,
 * and referenced from an index document at the same location. 
 * <p>
 * Call <code>RegisteryService.getDefaultRegistryManager()</code>
 * or <code>RegisteryService.getRegistryManager(URL)</code> to
 * get a new IRegistryManager.
 * 
 * @see org.eclipse.wst.ws.internal.registry.RegistryService#instance()
 * @see org.eclipse.wst.ws.internal.model.v10.registry.Registry
 * @see org.eclipse.wst.ws.internal.model.v10.taxonomy.Taxonomy
 * @author cbrealey
 */
public interface IRegistryManager
{
	/**
	 * A constant for use in the <code>removeRegistry(...)</code>
	 * and <code>removeTaxonomy(...)</code> methods that will
	 * cause the given registry or taxonomy document to be removed
	 * from the index, but not deleted.
	 * 
	 * @see #removeRegistry(String, boolean)
	 * @see #removeTaxonomy(String, boolean)
	 */
	public boolean KEEP_DOCUMENT = false;

	/**
	 * A constant for use in the <code>removeRegistry(...)</code>
	 * and <code>removeTaxonomy(...)</code> methods that will
	 * cause the given registry or taxonomy document to be removed
	 * from the index and deleted.
	 * 
	 * @see #removeRegistry(String, boolean)
	 * @see #removeTaxonomy(String, boolean)
	 */
	public boolean DELETE_DOCUMENT = true;

	/**
	 * Returns the URL of the XML index document that references
	 * the XML registry and taxonomy models managed by this
	 * Registry Manager.
	 * 
	 * @return The URL of the managing document.
	 * @throws MalformedURLException If a URL to the managing
	 * document cannot be determined or created.
	 */
	public URL getURL () throws MalformedURLException;

	/**
	 * Adds an <code>ITaxonomyFinder</code> that the
	 * <code>IRegistryManager</code> can use to find
	 * the set of <code>Taxonomy</code> models used
	 * by a given <code>Registry</code> model.
	 * <code>IRegistryManager.save(registry)</code> needs help finding
	 * the <code>Taxonomy</code> models used by a <code>Registry</code>,
	 * if any.
	 * 
	 * @param className The name of the concrete <code>Registry<code>
	 * class for which the <code>ITaxonomyFinder</code> is being added.
	 * @param taxonomyFinder An <code>ITaxonomyFinder</code> that knows
	 * how to determine the set of <code>Taxonomy</code> models a given
	 * concrete <code>Registry</code> model is using.
	 * @see ITaxonomyFinder
	 * @see #saveRegistry(Registry)
	 */
	public void addTaxonomyFinder ( String className, ITaxonomyFinder taxonomyFinder );

	/**
	 * Saves a <code>Taxonomy</code> model to an XML document
	 * located relative to the index XML document as identified by
	 * <code>getURL()</code>, and updates the index XML document
	 * with references to the saved taxonomy document.
	 * The relative pathname to the taxonomy XML document is
	 * computed by the <code>IRegistryManager</code> and cannot
	 * be influenced by the caller.
	 * 
	 * @param registry The <code>Registry</code> model (and
	 * referenced <code>Taxonomy</code> models) to save.
	 * @return A <code>Registry</code> reference.
	 * @throws CoreException If the operation fails for any reason,
	 * including but not limited to malformed location URIs in the
	 * index or general input/output error conditions.
	 * @see Registry
	 * @see #addTaxonomyFinder(String, ITaxonomyFinder)
	 */
	public Registry saveRegistry ( Registry registry ) throws CoreException;

	/**
	 * Loads as necessary and returns a full <code>Registry</code>
	 * model as identified by the given URI and referenced from
	 * the index XML document located at <code>getURL()</code>.
	 * 
	 * @param uri The URI identifier of the registry.
	 * @return The <code>Registry</code> model
	 * or null if no such model can be found.
	 * @throws CoreException If the operation fails for any reason,
	 * including but not limited to malformed location URIs in the
	 * index or general input/output error conditions.
	 * @see Registry
	 */
	public Registry loadRegistry ( String uri ) throws CoreException;

	/**
	 * Loads as necessary and returns an array of full <code>Registry</code>
	 * models as identified by the given array of URIs and referenced from
	 * the index XML document located at <code>getURL()</code>.
	 * Useful in conjunction with <code>getRegistryURIs()</code>.
	 * Note that the length of the returned array may be less than
	 * the length of the array of URIs in cases where one or more
	 * URIs fail to resolve to a loadable model.
	 * 
	 * @param uris The URI identifiers of the registries.
	 * @return The array of <code>Registry</code> models,
	 * never null, but possibly empty.
	 * @throws CoreException If the operation fails for any reason,
	 * including but not limited to malformed location URIs in the
	 * index or general input/output error conditions.
	 * @see Registry
	 */
	public Registry[] loadRegistries ( String[] uris ) throws CoreException;

	/**
	 * Returns an array of URI identifiers to <code>Registry</code>
	 * models in the index managed by this <code>IRegistryManager</code>.
	 * 
	 * @return An array of URI identifiers, possibly empty but never null,
	 * to registered <code>Registry</code> models.
	 * @throws CoreException If the operation fails for any reason,
	 * including but not limited to malformed location URIs in the
	 * index or general input/output error conditions.
	 * @see Registry
	 */
	public String[] getRegistryURIs () throws CoreException;

	/**
	 * Removes the <code>Registry</code> model identified by the
	 * given <code>uri</code> from the index and either keeps or
	 * deletes the corresponding <code>Registry</code> document
	 * based on the value given for <code>deleteDocument</code>.
	 * 
	 * @param uri The URI identifier of the model to delete.
	 * @param deleteDocument Either <code>KEEP_DOCUMENT</code>
	 * or <code>DELETE_DOCUMENT</code> depending on whether the
	 * model document should be kept or deleted.
	 * @throws CoreException If the operation fails for any reason,
	 * including but not limited to malformed location URIs in the
	 * index or general input/output error conditions.
	 * @see Registry
	 * @see #KEEP_DOCUMENT
	 * @see #DELETE_DOCUMENT
	 */
	public void removeRegistry ( String uri, boolean deleteDocument ) throws CoreException;

	/**
	 * Saves a <code>Taxonomy</code> model to an XML document
	 * located relative to the index XML document as identified by
	 * <code>getURL()</code>, and updates the index XML document
	 * with references to the saved taxonomy document.
	 * The relative pathname to the taxonomy XML document is
	 * computed by the <code>IRegistryManager</code> and cannot
	 * be influenced by the caller.
	 * 
	 * @param taxonomy The <code>Taxonomy</code> model to save.
	 * @return A <code>Taxonomy</code> reference.
	 * @throws CoreException If the operation fails for any reason,
	 * including but not limited to malformed location URIs in the
	 * index or general input/output error conditions.
	 * @see Taxonomy
	 */
	public Taxonomy saveTaxonomy ( Taxonomy taxonomy ) throws CoreException;

	/**
	 * Loads as necessary and returns a full <code>Taxonomy</code>
	 * model as identified by the given URI and referenced from
	 * the index XML document located at <code>getURL()</code>.
	 * 
	 * @param uri The URI identifier of the taxonomy.
	 * @return The <code>Taxonomy</code> model
	 * or null if no such model can be found.
	 * @throws CoreException If the operation fails for any reason,
	 * including but not limited to malformed location URIs in the
	 * index or general input/output error conditions.
	 * @see Taxonomy
	 */
	public Taxonomy loadTaxonomy ( String uri ) throws CoreException;

	/**
	 * Loads as necessary and returns an array of full <code>Taxonomy</code>
	 * models as identified by the given array of URIs and referenced from
	 * the index XML document located at <code>getURL()</code>.
	 * Useful in conjunction with <code>getTaxonomyURIs()</code>.
	 * Note that the length of the returned array may be less than
	 * the length of the array of URIs in cases where one or more
	 * URIs fail to resolve to a loadable model. 
	 * 
	 * @param uris The URI identifiers of the taxonomies.
	 * @return The array of <code>Taxonomy</code> models,
	 * never null, but possibly empty.
	 * @throws CoreException If the operation fails for any reason,
	 * including but not limited to malformed location URIs in the
	 * index or general input/output error conditions.
	 * @see Registry
	 */
	public Taxonomy[] loadTaxonomies ( String[] uris ) throws CoreException;

	/**
	 * Returns an array of URI identifiers to <code>Taxonomy</code>
	 * models in the index managed by this <code>IRegistryManager</code>.
	 * 
	 * @return An array of URI identifiers, possibly empty but never null,
	 * to registered <code>Taxonomy</code> models.
	 * @throws CoreException If the operation fails for any reason,
	 * including but not limited to malformed location URIs in the
	 * index or general input/output error conditions.
	 * @see Taxonomy
	 */
	public String[] getTaxonomyURIs () throws CoreException;

	/**
	 * Removes the <code>Taxonomy</code> model identified by the
	 * given <code>uri</code> from the index and either keeps or
	 * deletes the corresponding <code>Taxonomy</code> document
	 * based on the value given for <code>deleteDocument</code>.
	 * 
	 * @param uri The URI identifier of the model to delete.
	 * @param deleteDocument Either <code>KEEP_DOCUMENT</code>
	 * or <code>DELETE_DOCUMENT</code> depending on whether the
	 * model document should be kept or deleted.
	 * @throws CoreException If the operation fails for any reason,
	 * including but not limited to malformed location URIs in the
	 * index or general input/output error conditions.
	 * @see Taxonomy
	 * @see #KEEP_DOCUMENT
	 * @see #DELETE_DOCUMENT
	 */
	public void removeTaxonomy ( String uri, boolean deleteDocument ) throws CoreException;
}
