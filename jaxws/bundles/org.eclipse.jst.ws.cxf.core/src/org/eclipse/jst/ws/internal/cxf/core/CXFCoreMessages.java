/*******************************************************************************
 * Copyright (c) 2008 IONA Technologies PLC
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
