/*******************************************************************************
 * Copyright (c) 2008, 2009 IONA Technologies PLC
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * IONA Technologies PLC - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.internal.cxf.core;

import org.eclipse.osgi.util.NLS;

public class CXFCoreMessages extends NLS {
    private static final String BUNDLE_NAME = 
        "org.eclipse.jst.ws.internal.cxf.core.CXFCoreMessages"; //$NON-NLS-1$

    public static String CXF_FACET_INSTALL_DELEGATE_RUNTIME_LOCATION_NOT_SET;

    public static String CXF_CONTAINER_LIBRARY;
    
    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, CXFCoreMessages.class);
    }

    private CXFCoreMessages() {
    }

}
