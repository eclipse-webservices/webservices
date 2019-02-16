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
package org.eclipse.jst.ws.internal.cxf.creation.core;

import org.eclipse.osgi.util.NLS;

public class CXFCreationCoreMessages extends NLS {
    private static final String BUNDLE_NAME = 
        "org.eclipse.jst.ws.internal.cxf.creation.core.CXFCreationCoreMessages"; //$NON-NLS-1$
    public static String WSDL2JAVA_PROJECT_SELECTION_ERROR;
    public static String WSDL2JAVA_VALID_PORTTYPE_MESSAGE;
    
    public static String JAVA2WS_SERVICE_IMPL_NOT_FOUND;
    public static String JAVA2WS_SERVICE_IMPL_NOT_BINARY;

    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, CXFCreationCoreMessages.class);
    }

    private CXFCreationCoreMessages() {
    }
}
