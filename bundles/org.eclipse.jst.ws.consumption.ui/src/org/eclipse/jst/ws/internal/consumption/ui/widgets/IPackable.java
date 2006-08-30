/*******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060829   155441 makandre@ca.ibm.com - Andrew Mak, web service wizard hangs during resize
 *******************************************************************************/
package org.eclipse.jst.ws.internal.consumption.ui.widgets;

/**
 * An interface that marks an UI element as having some custom packing code.
 */
public interface IPackable {
	
	/**
	 * Calling this method should cause the UI element to pack itself.  Note that
	 * this does not necessarily have the same behavior as the pack() method in SWT.
	 */
	void packIt();
}
