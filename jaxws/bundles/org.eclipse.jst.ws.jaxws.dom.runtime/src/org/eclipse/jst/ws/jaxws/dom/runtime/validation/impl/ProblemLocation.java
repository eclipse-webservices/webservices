/*******************************************************************************
 * Copyright (c) 2009 by SAP AG, Walldorf. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     SAP AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.jaxws.dom.runtime.validation.impl;

import static org.eclipse.jst.ws.jaxws.utils.ContractChecker.nullCheckParam;

import org.eclipse.core.resources.IResource;
import org.eclipse.jst.ws.jaxws.dom.runtime.validation.IProblemLocation;
import org.eclipse.jst.ws.jaxws.utils.annotations.ILocator;


public class ProblemLocation implements IProblemLocation 
{
	private final IResource resource;
	private final ILocator locator;
	
	public ProblemLocation(IResource resource, ILocator locator) 
	{
		nullCheckParam(resource, "resource");//$NON-NLS-1$
		
		this.resource = resource;
		this.locator = locator;
	}
	
	public ILocator getLocator() {
		return locator;
	}

	public IResource getResource() {
		return resource;
	}	
}
