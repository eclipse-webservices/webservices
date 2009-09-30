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
package org.eclipse.jst.ws.jaxws.dom.runtime.validation;

import org.eclipse.core.resources.IResource;
import org.eclipse.jst.ws.jaxws.utils.annotations.ILocator;

/**
 * Interface that provides information on where the problem marker should be set/removed 
 * 
 * @author Georgi Vachkov
 */
public interface IProblemLocation 
{
	/**
	 * The resource on which the marker to be set or removed
	 * @return not <code>null</code> {@link IResource} instance
	 */
	public IResource getResource();
	
	/**
	 * The exact location in code where the marker to be put
	 * @return the {@link ILocator} instance to define the start char and length of text
	 * to be underlined by the marker. This method might return <code>null</code> in case
	 * location info is not relevant - for example when marker just needs to be removed.
	 */
	public ILocator getLocator();
}
