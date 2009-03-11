/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others.
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
 * The abstract implementation of an IFile to URI converter. Adopters who wish to
 * provide their own converter should subclass this abstract class rather than implementing IIFile2UriConverter. 
 *
 * @see IIFile2UriConverter
 */
public abstract class AbstractIFile2UriConverter implements IIFile2UriConverter {

	public String convert(IFile file) {
		return null;
	}
	
	public boolean allowBaseConversionOnFailure()
	{
		return true;
	}

}
