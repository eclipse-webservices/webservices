/*******************************************************************************
 * Copyright (c) 2005, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20090310   242440 yenlu@ca.ibm.com - Yen Lu, Pluggable IFile to URI Converter
 *******************************************************************************/
package org.eclipse.wst.ws.internal.converter;

import org.eclipse.core.resources.IFile;

/**
 * <p>
 * The interface for an Eclipse IFile to URI converter. Adopters should
 * subclass AbstractIFile2UriConverter instead.
 * 
 * @see AbstractIFile2UriConverter
 */
public interface IIFile2UriConverter {
	/**
	 * <p>
	 * Produce a String URI from a given IFile. The IFile is from the workbench. If this method returns null,
	 * the result is considered a failure and the
	 * {@link #allowBaseConversionOnFailure allowBaseConversionOnFailure} method will be consulted to determine
	 * whether or not base conversion should be used as a backup.
	 * @param file An IFile reference.
	 * @return The String URI corresponding to the IFile reference.
	 * <p>
	 * @since 3.1
	 */
	public String convert(IFile file);
	
	/**
	 * <p>
	 * Determines whether or not a converter will allow base conversion routines to be performed
	 * when a failure occurs.
	 * @return <code>true</code> if base conversion should be performed when a failure occurs.
	 * <p>
	 * @since 3.1
	 */
	public boolean allowBaseConversionOnFailure();	
}
