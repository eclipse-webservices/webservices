/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.command.internal.provisional.ui.widgets;

import org.eclipse.wst.command.env.core.data.DataMappingRegistry;
import org.eclipse.wst.command.env.ui.widgets.WidgetContributorFactory;

public class SimpleWidgetContributor implements INamedWidgetContributor 
{
    private WidgetContributorFactory factory_;
	private String                   name_;
	private String                   description_;
	private String                   title_;
	
	public void setDescription(String description) 
	{
		description_ = description;
	}
	

	public void setFactory(WidgetContributorFactory factory) 
	{
		factory_ = factory;
	}
	

	public void setName(String name) 
	{
		name_ = name;
	}
	

	public void setTitle(String title) 
	{
		title_ = title;
	}
	

	public WidgetContributorFactory getWidgetContributorFactory() 
	{
		return factory_;
	}

	public String getName() 
	{
		return name_;
	}

	public String getDescription() 
	{
		return description_;
	}

	public String getTitle() 
	{
		return title_;
	}

	/**
	 * Subsclassers should override this method.
	 */
	public void registerDataMappings(DataMappingRegistry dataRegistry) 
	{

	}
}
