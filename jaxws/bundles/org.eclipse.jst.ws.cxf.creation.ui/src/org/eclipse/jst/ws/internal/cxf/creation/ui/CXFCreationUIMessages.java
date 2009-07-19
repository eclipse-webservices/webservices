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
package org.eclipse.jst.ws.internal.cxf.creation.ui;

import org.eclipse.osgi.util.NLS;

public class CXFCreationUIMessages extends NLS {
    private static final String BUNDLE_NAME = 
        "org.eclipse.jst.ws.internal.cxf.creation.ui.CXFCreationUImessages"; //$NON-NLS-1$
    public static String JAVA2WS_ENTER_SEI_NAME;
    public static String JAVA2WS_SELECT_SEI_EXTRACTED_METHODS;
    public static String JAVA2WS_SELECT_SEI_FROM_TYPE_HIERARCHY;
    public static String JAVA2WS_SELECT_SEI_OPTION;
    public static String JAVA2WS_ENTER_VALID_WSDL_NAME;

    public static String JAVA2WS_CLASS_CONFIG_PAGE_TITLE;
    public static String JAVA2WS_CLASS_CONFIG_PAGE_DESCRIPTION;

    public static String JAVA2WS_INTERFACE_CONFIG_PAGE_TITLE;
    public static String JAVA2WS_INTERFACE_CONFIG_PAGE_DESCRIPTION;
                         
    public static String JAVA2WS_JAXWS_ANNOTATE_PAGE_TITLE;
    public static String JAVA2WS_JAXWS_ANNOTATE_PAGE_DESCRIPTION;
    
    public static String JAVA2WS_PAGE_TITLE;
    public static String JAVA2WS_PAGE_DESCRIPTION;
    public static String JAXWS_ANNOTATE_JJAVA_WIDGET_PREVIEW;
    public static String JAXWS_ANNOTATE_JJAVA_WIDGET_SELECT_METHOD_TO_ANNOTATE;

    public static String WSDL2JAVA_PAGE_TITLE;
    public static String WSDL2JAVA_PAGE_DESCRIPTION;

    public static String WSDL2JAVA_DEFAULTS_PAGE_TITLE;
    public static String WSDL2JAVA_DEFAULTS_PAGE_DESCRIPTION;

    public static String JAVA2WS_GROUP_LABEL;

    public static String WSDL2JAVA_GROUP_LABEL;
    public static String WSDL2JAVA_XJC_ARG_GROUP_LABEL;
    
    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, CXFCreationUIMessages.class);
    }

    private CXFCreationUIMessages() {
    }
}
