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
package org.eclipse.jst.ws.jaxws.utils.resources;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

/**
 * Adapter class that uses a StringReader to organize an InputStream
 * 
 * @author Danail Branekov
 * 
 */
public class StringInputStreamAdapter extends InputStream
{
	private StringReader reader;

	/**
	 * Constructor
	 * 
	 * @param string
	 * @throws NullPointerException
	 *             when <code>string</code> is null
	 */
	public StringInputStreamAdapter(String string)
	{
		if (string == null)
		{
			throw new NullPointerException("string"); //$NON-NLS-1$
		}

		reader = new StringReader(string);
	}

	@Override
	public int read() throws IOException
	{
		return reader.read();
	}

}
