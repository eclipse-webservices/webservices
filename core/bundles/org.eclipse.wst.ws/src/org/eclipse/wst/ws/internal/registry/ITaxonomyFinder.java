/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.ws.internal.registry;

import org.eclipse.wst.ws.internal.model.v10.registry.Registry;
import org.eclipse.wst.ws.internal.model.v10.taxonomy.Taxonomy;

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
	 * Returns an array taxonomies used by the given
	 * <code>registry</code>. This method never returns null.
	 *
	 * @param registry The <code>Registry</code> for which
	 * a set of taxonomies might be found.
	 * @return The array of taxonomies, possibly empty.
	 */
	public Taxonomy[] taxonomies ( Registry registry );	
}