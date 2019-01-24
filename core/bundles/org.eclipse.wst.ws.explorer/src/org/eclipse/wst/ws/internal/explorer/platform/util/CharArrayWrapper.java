/*******************************************************************************
 * Copyright (c) 2001, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.ws.internal.explorer.platform.util;

import java.io.CharArrayWriter;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public class CharArrayWrapper extends HttpServletResponseWrapper
{
	private CharArrayWriter charArrayWriter_;
	
	public CharArrayWrapper(HttpServletResponse response)
	{
		super(response);
	  charArrayWriter_ = new CharArrayWriter();
	}
	
	public PrintWriter getWriter()
	{
		return new PrintWriter(charArrayWriter_);
	}
	
	public String toString()
	{
		return charArrayWriter_.toString();
	}
}
