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
package org.eclipse.jst.ws.internal.cxf.creation.core;

import org.eclipse.osgi.util.NLS;

/**
 * 
 * @author sclarke
 *
 */
public class CXFCreationCoreMessages extends NLS {
    private static final String BUNDLE_NAME = 
        "org.eclipse.jst.ws.internal.cxf.creation.core.CXFCreationCoreMessages"; //$NON-NLS-1$
    public static String WSDL2JAVA_PROJECT_SELECTION_ERROR;
    public static String WSDL2JAVA_VALID_PORTTYPE_MESSAGE;

    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, CXFCreationCoreMessages.class);
    }

    private CXFCreationCoreMessages() {
    }
}
