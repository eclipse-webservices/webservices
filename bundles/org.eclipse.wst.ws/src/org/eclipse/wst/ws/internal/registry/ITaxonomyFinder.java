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

import java.util.Iterator;

import org.eclipse.wst.ws.internal.model.v10.registry.Registry;

/**
 * An <code>ITaxonomyFinder</code> computes the set of
 * <code>Taxonomy</code> models referenced or used by a
 * <code>Registry</code>.
 * @author gilberta@ca.ibm.com
 * @see IRegistryManager
 */
public interface ITaxonomyFinder
{
	/**
	 * Returns an iterator over the set of taxonomies used
	 * by the given <code>registry</code>. This method may
	 * return an empty iterator or <b>null</b> if the
	 * <code>Registry</code> does not reference any taxonomies.
	 *
	 * @param registry The <code>Registry</code> for which
	 * a set of taxonomies might be found.
	 * @return An iterator of taxonomies or <b>null</b>.
	 */
	public Iterator taxonomies ( Registry registry );	
}