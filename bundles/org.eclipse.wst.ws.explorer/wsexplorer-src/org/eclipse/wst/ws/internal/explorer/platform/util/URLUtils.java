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
package org.eclipse.wst.ws.internal.explorer.platform.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * This class contains utility methods for managing URLs
 * as used by the Web Services Explorer Web application.
 * @author cbrealey@ca.ibm.com
 */
public final class URLUtils
{
	/**
	 * Objects of this class should not be constructed.
	 */
	private URLUtils ()
	{
	}

	/**
	 * UTF-8
	 */
	public static final String UTF8 = "UTF-8";

	/**
	 * Equivalent to {@link #encode(String,String)}
	 * with second parameter set to the "UTF-8" encoding.
	 * @param s The string to encode.
	 * @return The encoded string.
	 */
	public static String encode(String s)
	{
		return encode(s,UTF8);
	}
	
	/**
	 * Equivalent to {@link URLEncoder#encode(String,String)},
	 * only throws an unchecked {@link RuntimeException} wrapped
	 * around an {@link UnsupportedEncodingException} instead of
	 * an {@link UnsupportedEncodingException}.
	 * @param s The string to encode.
	 * @param enc The encoding to use.
	 * @return The encoded string.
	 */
	public static String encode(String s, String enc)
	{
		try
		{
			return URLEncoder.encode(s,enc);
		}
		catch (UnsupportedEncodingException e)
		{
			// TODO: MSG_BROKEN_VM_DOES_NOT_SUPPORT_UTF-8
			throw new RuntimeException("%MSG_BROKEN_VM_DOES_NOT_SUPPORT_UTF-8",e);
		}
	}
	
	/**
	 * Equivalent to {@link #decode(String,String)}
	 * with second parameter set to the "UTF-8" encoding.
	 * @param s The string to decode.
	 * @return The decoded string.
	 */
	public static String decode(String s)
	{
		return decode(s,UTF8);
	}
	
	/**
	 * Equivalent to {@link URLEncoder#decode(String,String)},
	 * only throws an unchecked {@link RuntimeException} wrapped
	 * around an {@link UnsupportedEncodingException} instead of
	 * an {@link UnsupportedEncodingException}.
	 * @param s The string to decode.
	 * @param enc The encoding to use.
	 * @return The decoded string.
	 */
	public static String decode(String s, String enc)
	{
		try
		{
			return URLDecoder.decode(s,enc);
		}
		catch (UnsupportedEncodingException e)
		{
			// TODO: MSG_BROKEN_VM_DOES_NOT_SUPPORT_UTF-8
			throw new RuntimeException("%MSG_BROKEN_VM_DOES_NOT_SUPPORT_UTF-8",e);
		}
	}
}
