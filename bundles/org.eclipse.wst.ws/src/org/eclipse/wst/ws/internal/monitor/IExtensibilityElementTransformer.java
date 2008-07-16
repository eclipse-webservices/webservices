/*******************************************************************************
 * Copyright (c) 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20080715   240722 makandre@ca.ibm.com - Andrew Mak, Cannot setup TCP/IP Monitor for soap12 endpoints
 *******************************************************************************/

package org.eclipse.wst.ws.internal.monitor;

import javax.wsdl.extensions.ExtensibilityElement;

public interface IExtensibilityElementTransformer {

	public String transform(ExtensibilityElement element);
}
