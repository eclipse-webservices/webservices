/*******************************************************************************
 * Copyright (c) 2009 by SAP AG, Walldorf. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
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