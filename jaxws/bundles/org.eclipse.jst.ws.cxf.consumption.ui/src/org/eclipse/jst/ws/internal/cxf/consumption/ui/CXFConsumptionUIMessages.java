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
package org.eclipse.jst.ws.internal.cxf.consumption.ui;

import org.eclipse.osgi.util.NLS;

public class CXFConsumptionUIMessages extends NLS {
    private static final String BUNDLE_NAME = 
        "org.eclipse.jst.ws.internal.cxf.consumption.ui.CXFConsumptionUIMessages"; //$NON-NLS-1$
    public static String CXFJAXWSSELECTIONLAUNCHABLE_SELECTION_DIALOG_MESSAGE;
    public static String CXFJAXWSSELECTIONLAUNCHABLE_SELECTION_DIALOG_TITLE;
    
    public static String WSDL2JAVA_CLIENT_PAGE_TITLE;
    public static String WSDL2JAVA_CLIENT_PAGE_DESCRIPTION;
    
    public static String WSDL2JAVA_ClIENT_DEFAULTS_PAGE_TITLE;
    public static String WSDL2JAVA_CLIENT_DEFAULTS_PAGE_DESCRIPTION;
    
    public static String WSDL2JAVA_GROUP_LABEL;
    public static String WSDL2JAVA_XJC_ARG_GROUP_LABEL;
    
    static {
        // initialize resource bundle
        NLS.initializeMessages(CXFConsumptionUIMessages.BUNDLE_NAME, CXFConsumptionUIMessages.class);
    }

    private CXFConsumptionUIMessages() {
    }
}
