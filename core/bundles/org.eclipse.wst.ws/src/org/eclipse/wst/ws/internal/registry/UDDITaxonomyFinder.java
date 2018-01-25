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

import org.eclipse.wst.ws.internal.model.v10.registry.Registry;
import org.eclipse.wst.ws.internal.model.v10.taxonomy.Taxonomy;
import org.eclipse.wst.ws.internal.model.v10.uddiregistry.UDDIRegistry;

/**
 * This <code>ITaxonomyFinder</code> for UDDI registries
 * returns an array of <code>Taxonomy</code>
 * models used (supported by) the registry.
 * @author gilberta@ca.ibm.com
 * @see UDDIRegistry
 */
public class UDDITaxonomyFinder implements ITaxonomyFinder
{
	/* (non-Javadoc)
	 * @see org.eclipse.wst.ws.internal.registry.ITaxonomyFinder#taxonomies(org.eclipse.wst.ws.internal.model.v10.registry.Registry)
	 */
	public Taxonomy[] taxonomies ( Registry registry )
	{
		if ( registry instanceof UDDIRegistry )
		{
			UDDIRegistry uddiRegistry = (UDDIRegistry)registry;	  
			return (Taxonomy[])uddiRegistry.getTaxonomies().getTaxonomy().toArray(new Taxonomy[0]);
		}
		return null;
	}
}
