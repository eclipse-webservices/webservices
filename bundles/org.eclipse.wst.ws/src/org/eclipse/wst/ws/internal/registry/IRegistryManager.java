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
	 * Saves a <code>Registry</code> and any <code>Taxonomy</code>
	 * models it references to a set of XML documents located
	 * relative to the index XML document as identified by
	 * <code>getURL()</code>, and updates the index XML document
	 * with references to the saved registry and taxonomy documents.
	 * Relative pathnames to the registry and taxonomy XML documents
	 * are computed by the <code>IRegistryManager</code> and cannot
	 * be influenced by the caller.
	 * <p>
	 * The set of referenced <code>Taxonomy</code> models, if any,
	 * is determined by looking up an <code>ITaxonomyFinder</code>
	 * for the class name of the given <code>registry</code> and
	 * calling its <code>taxonomies</code> method.
	 * If no <code>ITaxonomyFinder</code> exists for the given
	 * <code>registry</code>, then no <code>Taxonomy</code> models
	 * will be saved.  
	 * 
	 * @param registry The <code>Registry</code> model (and
	 * referenced <code>Taxonomy</code> models) to save.
	 * @return The URI identifier of the registry as returned by
	 * <code>registry.getId()</code>.
	 * @throws CoreException If the operation fails for any reason,
	 * including but not limited to malformed location URIs in the
	 * index or general input/output error conditions.
	 * @see Registry
	 * @see #addTaxonomyFinder(String, ITaxonomyFinder)
	 */
	public String saveRegistry ( Registry registry ) throws CoreException;

	/**
	 * Loads a <code>Registry</code> from a registry XML document
	 * identified by the given URI in the index XML document at the
	 * location returned by <code>getURL()</code>.
	 * 
	 * @param uri The URI identifier of the registry as returned by
	 * <code>registry.getId()</code>.
	 * @return The <code>Registry</code> model.
	 * @throws CoreException If the operation fails for any reason,
	 * including but not limited to malformed location URIs in the
	 * index or general input/output error conditions.
	 * @see Registry
	 */
	public Registry loadRegistry ( String uri ) throws CoreException;

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
	 * <p>
	 * Note that <code>Taxonomy</code> models referenced by a
	 * <code>Registry</code> model will be saved as a side effect
	 * of calling <code>saveRegistry</code> on that model. 
	 * 
	 * @param taxonomy The <code>Taxonomy</code> model to save.
	 * @return The URI identifier of the taxonomy as returned by
	 * <code>taxonomy.getId()</code>.
	 * @throws CoreException If the operation fails for any reason,
	 * including but not limited to malformed location URIs in the
	 * index or general input/output error conditions.
	 * @see Taxonomy
	 */
	public String saveTaxonomy ( Taxonomy taxonomy ) throws CoreException;

	/**
	 * Loads a <code>Taxonomy</code> from a taxonomy XML document
	 * identified by the given URI in the index XML document at the
	 * location returned by <code>getURL()</code>.
	 * <p>
	 * Note that <code>Taxonomy</code> models referenced by a
	 * <code>Registry</code> model will be loaded as a side effect
	 * of calling <code>loadRegistry()</code> followed by
	 * <code>UDDIRegistryService.getTaxonomies(Registry)</code>.
	 * 
	 * @param uri The URI identifier of the taxonomy as returned by
	 * <code>taxonomy.getId()</code>.
	 * @return The <code>Taxonomy</code> model.
	 * @throws CoreException If the operation fails for any reason,
	 * including but not limited to malformed location URIs in the
	 * index or general input/output error conditions.
	 * @see Taxonomy
	 */
	public Taxonomy loadTaxonomy ( String uri ) throws CoreException;

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
