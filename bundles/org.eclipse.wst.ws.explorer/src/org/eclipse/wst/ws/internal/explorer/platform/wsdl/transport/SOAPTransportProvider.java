/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20070413   176493 makandre@ca.ibm.com - Andrew Mak, WSE: Make message/transport stack pluggable
 *******************************************************************************/
package org.eclipse.wst.ws.internal.explorer.platform.wsdl.transport;

import org.eclipse.wst.ws.internal.explorer.transport.ISOAPTransport;
import org.eclipse.wst.ws.internal.explorer.transport.ISOAPTransportProvider;

/**
 * WSE's default implementation for ISOAPTransportProvider.
 */
public class SOAPTransportProvider implements ISOAPTransportProvider {	
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.ws.internal.explorer.transport.ISOAPTransportProvider#newTransport()
	 */
	public ISOAPTransport newTransport() {
		return new SOAPTransport();		
	}
}
