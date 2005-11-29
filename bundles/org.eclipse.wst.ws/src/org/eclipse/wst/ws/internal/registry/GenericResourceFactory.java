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

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.ExtendedMetaData;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMLResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMLResourceImpl;

/**
 * Patterned after the ResourceFactory classes generated
 * by the EMF tools, this generic factory handles creation
 * of EMF resources for XML documents in general.
 */
public class GenericResourceFactory extends XMLResourceFactoryImpl
{
	/**
	 * Holds an instance of <code>ExtendedMetaData</code>
	 * for associating extended metadata with the
	 * <code>Resource</code> returned by <code>createResource</code>.
	 */
	protected ExtendedMetaData extendedMetaData;

	/**
	 * Constructs a new <code>GenericResourceFactory</code>.
	 */
	public GenericResourceFactory()
	{
		super();
		extendedMetaData = ExtendedMetaData.INSTANCE;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.emf.ecore.resource.Resource$Factory#createResource(org.eclipse.emf.common.util.URI)
	 */
	public Resource createResource(URI uri)
	{
		XMLResource result = new XMLResourceImpl(uri);
		result.getDefaultSaveOptions().put(XMLResource.OPTION_EXTENDED_META_DATA, extendedMetaData);
		result.getDefaultLoadOptions().put(XMLResource.OPTION_EXTENDED_META_DATA, extendedMetaData);
		result.getDefaultSaveOptions().put(XMLResource.OPTION_SCHEMA_LOCATION, Boolean.TRUE);
		result.getDefaultSaveOptions().put(XMLResource.OPTION_USE_ENCODED_ATTRIBUTE_STYLE, Boolean.TRUE);
		result.getDefaultLoadOptions().put(XMLResource.OPTION_USE_LEXICAL_HANDLER, Boolean.TRUE);
		return result;
	}
}
