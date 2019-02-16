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
package org.eclipse.jst.ws.jaxws.utils.internal.annotations.impl;

import org.eclipse.jst.ws.jaxws.utils.annotations.ILocator;

/**
 * Implementor of {@link ILocator} interface
 */
public class LocatorImpl implements ILocator
{
	private int lineNumber;
	private int startPosition;
	private int length;
	
	public LocatorImpl(final int lineNumber, final int startPosition, final int length)
	{
		this.length = length;
		this.startPosition = startPosition;
		this.lineNumber = lineNumber;
	}
	
	public int getLength()
	{
		return this.length;
	}

	public int getStartPosition()
	{
		return this.startPosition;
	}

	public int getLineNumber() 
	{
		return lineNumber;
	}
}
