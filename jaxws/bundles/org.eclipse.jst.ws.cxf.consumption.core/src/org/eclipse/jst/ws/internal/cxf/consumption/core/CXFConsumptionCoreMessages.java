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
package org.eclipse.jst.ws.internal.cxf.consumption.core;

import org.eclipse.osgi.util.NLS;

/**
 * 
 * @author sclarke
 *
 */
public class CXFConsumptionCoreMessages extends NLS {
    private static final String BUNDLE_NAME = 
        "org.eclipse.jst.ws.internal.cxf.consumption.core.CXFConsumptionCoreMessages"; //$NON-NLS-1$

    static {
        // initialize resource bundle
        NLS.initializeMessages(CXFConsumptionCoreMessages.BUNDLE_NAME, CXFConsumptionCoreMessages.class);
    }

    private CXFConsumptionCoreMessages() {
    }
}
