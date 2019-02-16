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
package org.eclipse.jst.ws.jaxws.dom.runtime.persistence;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.zip.ZipEntry;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;

public abstract class NonStreamResource extends ResourceImpl {

	/**
	 * Since this Resource is not stream based, this method should never be called.
	 * If called it will throw <c>UnsupportedException</c>
	 * @param arg0 - ignorred
	 */
	@Override
	protected final void doLoad(InputStream inputStream, Map<?,?> options) throws IOException {
		super.doLoad(inputStream, options);
	}
	
	/**
	 * Since this Resource is not stream based, this method should never be called.
	 * If called it will throw <c>UnsupportedException</c>
	 * @param arg0 - ignorred
	 */
	@Override
	protected final void doSave(OutputStream arg0, Map<?,?> arg1) throws IOException {
		super.doSave(arg0, arg1);
	}

	/**
	 * Allwasy throws <c>UnsupportedOperationException</c>. Streams are not supported by this resource.
	 * @return nothing. this method allways throws <c>UnsupportedOperationException</c>
	 */
	@Override
	protected final boolean isContentZipEntry(ZipEntry arg0) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Since this resource doesn't support streams this method should never be called. Allwasy throws <c>UnsupportedOperationException</c>.
	 * @return nothing. this method allways throws <c>UnsupportedOperationException</c>
	 */
	@Override
	protected final ZipEntry newContentZipEntry() {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Since this resource doesn't support streams this method should never be called. Allwasy throws <c>UnsupportedOperationException</c>.
	 * @return nothing. this method allways throws <c>UnsupportedOperationException</c>
	 */
	@Override
	protected final boolean useZip() {
		throw new UnsupportedOperationException();
	}

	@Override
	public final void load(Map<?,?> options) throws IOException {

		if (!isLoaded()) {
			Notification notification = setLoaded(true);
			isLoading = true;
			if (errors != null) {
				errors.clear();
			}
			if (warnings != null) {
				warnings.clear();
			}
			try {
			    Map<?,?> mergedOptions = mergeMaps(options, defaultLoadOptions);
				doLoad(mergedOptions);
			} finally {
				
				isLoading = false;
				if (notification != null) {
					eNotify(notification);
				}
				setModified(false);
			}
		}
	}
	
	/**
	 * Called to load the resource using no streams.
	 *
	 * @param options - options supplied by the caller. This metod will ignore any options that it doesn't recognize. The keys and the values can be arbitrary objects.
	 * @throws IOException - thrown if error occurred while loading the content of this resource
	 */
	abstract protected void doLoad(Map<?,?> options) throws IOException;
	
	@Override
	public final void save(Map<?,?> options) throws IOException {
	    if (errors != null)
	    {
	      errors.clear();
	    }
	    if (warnings != null)
	    {
	      warnings.clear();
	    }
	    Map<?,?> mergedOptions = mergeMaps(options, defaultSaveOptions);
	    doSave(mergedOptions);
	    setModified(false);
	}
	
	/**
	 * Called to save the resource using no streams.
	 * @param options - options supplied by the caller. This metod will ignore any options that it doesn't recognize. The keys and the values can be arbitrary objects.
	 * @throws IOException - if problem during the saving occurred.
	 */
	protected abstract void doSave(Map<?,?> options) throws IOException;
	

}
