/*******************************************************************************
 * Copyright (c) 2009 Progress Software, Shane Clarke.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Shane Clarke - initial API and implementation for JAXWS
 *    Oisin Hurley - adapted for JAXB
 *******************************************************************************/
package org.eclipse.jst.ws.internal.jaxb.core;

import org.eclipse.osgi.util.NLS;

public class JAXBCoreMessages extends NLS {
    private static final String BUNDLE_NAME = 
        "org.eclipse.jst.ws.internal.jaxb.core.JAXBCoreMessages"; //$NON-NLS-1$

    public static String TYPE_NAME_DIFFERENT_CASE_EXISTS;
    public static String TYPE_WITH_NAME_ALREADY_EXISTS;
 
	public static String XML_SCHEMA_TYPE_MUST_HAVE_DEFAULT_ON_FIELD_OR_METHOD;
	
    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, JAXBCoreMessages.class);
    }

    private JAXBCoreMessages() {
    }

}
