/*******************************************************************************
 * Copyright (c) 2009 Shane Clarke.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Shane Clarke - initial API and implementation
 *******************************************************************************/

package org.eclipse.jst.ws.internal.jaxws.core;

import org.eclipse.osgi.util.NLS;

/**
 * 
 * @author sclarke
 *
 */
public class JAXWSCoreMessages extends NLS {
    private static final String BUNDLE_NAME = 
        "org.eclipse.jst.ws.internal.jaxws.core.JAXWSCoreMessages"; //$NON-NLS-1$

    public static String TYPE_NAME_DIFFERENT_CASE_EXISTS;
    public static String TYPE_WITH_NAME_ALREADY_EXISTS;
    
    public static String ONEWAY_NO_RETURN_VALUE_ERROR_MESSAGE;

    public static String WEBMETHOD_ONLY_SUPPORTED_ON_CLASSES_WITH_WEBSERVICE_MESSAGE;
    public static String WEBMETHOD_ONLY_ON_PUBLIC_METHODS_MESSAGE;
    public static String WEBMETHOD_NO_FINAL_MODIFIER_ALLOWED_MESSAGE;
    public static String WEBMETHOD_NO_STATIC_MODIFIER_ALLOWED_MESSAGE;
    public static String WEBMETHOD_EXCLUDE_SPECIFEID_NO_OTHER_ATTRIBUTES_ALLOWED_MESSAGE;
    public static String WEBMETHOD_EXCLUDE_NOT_ALLOWED_ON_SEI;

    public static String WEBSERVICE_PUBLIC_ABSTRACT_FINAL_MESSAGE;
    public static String WEBSERVICE_DEFAULT_PUBLIC_CONSTRUCTOR_MESSAGE;
    public static String WEBSERVICE_OVERRIDE_FINALIZE_MESSAGE;
    public static String WEBSERVICE_ENDPOINTINTERFACE_SEI_ERROR_MESSAGE;
    public static String WEBSERVICE_PORTNAME_SEI_ERROR_MESSAGE;
    public static String WEBSERVICE_SERVICENAME_SEI_ERROR_MESSAGE;
    public static String WEBSERVICE_ENPOINTINTERFACE_NO_WEBMETHOS_ERROR_MESSAGE;
    public static String WEBSERVICE_WEBSERVICEPROVIDER_COMBINATION_ERROR_MESSAGE;
    
    public static String SOAPBINDING_ON_METHOD_STYLE_DOCUMENT_ONLY_MESSAGE;
    public static String SOAPBINDING_NO_PARAMETERSTYLE_WHEN_ENCODED_MESSAGE;
    
    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, JAXWSCoreMessages.class);
    }

    private JAXWSCoreMessages() {
    }


}
