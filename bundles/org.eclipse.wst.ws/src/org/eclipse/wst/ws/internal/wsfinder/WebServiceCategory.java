/*******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060317   127456 cbrealey@ca.ibm.com - Chris Brealey
 *******************************************************************************/

package org.eclipse.wst.ws.internal.wsfinder;

import org.eclipse.core.runtime.IConfigurationElement;

/**
 * @author cbrealey
 * A WebServiceCategory represents a defined category of
 * Web service locators plugged into the WebServiceFinder
 * framework. Each category has a non-translatable identifier,
 * an optional non-translatable parent category identifier
 * (absent for root categories), a translatable short label,
 * a translatable long description and an icon.
 * @see WebServiceFinder#getWebServiceCategories() 
 */
public class WebServiceCategory
{
	private static final String ATTRIBUTE_ID = "id"; //$NON-NLS-1$
	private static final String ATTRIBUTE_CATEGORY = "category"; //$NON-NLS-1$
	private static final String ATTRIBUTE_LABEL = "label"; //$NON-NLS-1$
	private static final String ATTRIBUTE_DESCRIPTION = "description"; //$NON-NLS-1$
	private static final String ATTRIBUTE_ICON = "icon"; //$NON-NLS-1$
	
	private IConfigurationElement ice_;
	
	/**
	 * Constructs a new WebServiceCategory for the given
	 * configuration element from the extension registry.
	 * @param ice The configuration element on which this
	 * new WebServiceCategory is based. 
	 */
	public WebServiceCategory ( IConfigurationElement ice )
	{
		ice_ = ice;
	}
	
	/**
	 * Returns the non-translatable identifier of this category.
	 * @return The non-translatable identifier of this category.
	 * This should never be null.
	 */
	public String getId ()
	{
		return ice_.getAttributeAsIs(ATTRIBUTE_ID);
	}
	
	/**
	 * Returns the non-translatable identifier of this category's
	 * immediate parent category, or null if this is a root category
	 * and, as such, has no parent category.
	 * @return The non-translatable identifier of this category's
	 * parent, or null if there is none.
	 */
	public String getParentId ()
	{
		return ice_.getAttributeAsIs(ATTRIBUTE_CATEGORY);
	}
	
	/**
	 * Returns this category's immediate parent WebServiceCategory, or
	 * null if this is a root category and, as such, has no parent category.
	 * @return This category's immediate parent WebServiceCategory, or
	 * null if there is none.
	 */
	public WebServiceCategory getParent ()
	{
		return WebServiceFinder.instance().getWebServiceCategoryById(getParentId());
	}
	
	/**
	 * Returns the internationalized short label of this category,
	 * or null if there is none. Note that user interfaces may
	 * choose to bypass categories that do not have a label.
	 * @return The internationalized short label of this category,
	 * or null if there is none.
	 */
	public String getLabel ()
	{
		return ice_.getAttributeAsIs(ATTRIBUTE_LABEL);
	}
	
	/**
	 * Returns the internationalized description of this category,
	 * or null if there is none.
	 * @return The internationalized description of this category,
	 * or null if there is none.
	 */
	public String getDescription ()
	{
		return ice_.getAttributeAsIs(ATTRIBUTE_DESCRIPTION);
	}
	
	/**
	 * Returns the icon of this category, or null if there is none.
	 * @return The icon of this category, or null if there is none.
	 */
	public String getIcon ()
	{
		return ice_.getAttributeAsIs(ATTRIBUTE_ICON);
	}
}
