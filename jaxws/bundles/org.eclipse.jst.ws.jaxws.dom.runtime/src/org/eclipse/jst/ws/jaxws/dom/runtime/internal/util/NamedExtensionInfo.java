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
package org.eclipse.jst.ws.jaxws.dom.runtime.internal.util;

import org.eclipse.jst.ws.jaxws.dom.runtime.INamedExtensionInfo;

/**
 * Implementation class for {@link INamedExtensionInfo}.
 */
public class NamedExtensionInfo implements INamedExtensionInfo
{
	private final String id;

	private final String name;

	protected NamedExtensionInfo(final String pId, final String pLabel)
	{
		this.id = pId;
		this.name = pLabel;
	}

	public String getId()
	{
		return id;
	}

	public String getName()
	{
		return name;
	}
}