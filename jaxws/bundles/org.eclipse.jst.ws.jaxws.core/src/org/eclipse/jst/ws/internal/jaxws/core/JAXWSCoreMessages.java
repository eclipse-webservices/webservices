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

public class JAXWSCoreMessages extends NLS {
    private static final String BUNDLE_NAME =
        "org.eclipse.jst.ws.internal.jaxws.core.JAXWSCoreMessages"; //$NON-NLS-1$

    public static String TYPE_NAME_DIFFERENT_CASE_EXISTS;
    public static String TYPE_WITH_NAME_ALREADY_EXISTS;

    public static String ONEWAY_NO_RETURN_VALUE;
    public static String ONEWAY_NO_CHECKED_EXCEPTIONS;
    public static String ONEWAY_NO_HOLDER_PARAMETERS;
    public static String WEBPARAM_MODE_OUT_INOUT_HOLDER_TYPE;

    public static String HOLDER_TYPE_MUST_BE_OUT_INOUT;

    public static String DOC_BARE_NON_VOID_RETURN_NO_INOUT_OUT_PARAMETER;
    public static String DOC_BARE_ONLY_ONE_NON_HEADER_IN_PARAMETER;
    public static String DOC_BARE_VOID_RETURN_ONE_IN_PARAMETER;
    public static String DOC_BARE_VOID_RETURN_ONE_OUT_PARAMETER;

    public static String HANDLER_CHAIN_SOAP_MESSAGE_HANDLERS;
    public static String HANDLER_CHAIN_ON_FIELD;
    public static String HANDLER_CHAIN_ON_METHOD;

    public static String WEBSERVICEREFS_NAME_REQUIRED;
    public static String WEBSERVICEREFS_TYPE_REQUIRED;

    public static String WEBPARAM_NAME_REQUIRED_WHEN_DOC_BARE_OUT_INOUT;

    public static String WEBMETHOD_ONLY_SUPPORTED_ON_CLASSES_WITH_WEBSERVICE;
    public static String WEBMETHOD_ONLY_ON_PUBLIC_METHODS;
    public static String WEBMETHOD_NO_FINAL_MODIFIER_ALLOWED;
    public static String WEBMETHOD_NO_STATIC_MODIFIER_ALLOWED;
    public static String WEBMETHOD_EXCLUDE_SPECIFIED_NO_OTHER_ATTRIBUTES_ALLOWED;
    public static String WEBMETHOD_EXCLUDE_NOT_ALLOWED_ON_SEI;

    public static String WEBSERVICE_PUBLIC_ABSTRACT_FINAL;
    public static String WEBSERVICE_DEFAULT_PUBLIC_CONSTRUCTOR;
    public static String WEBSERVICE_CLASS_OR_INTERFACE_ONLY;
    public static String WEBSERVICE_OVERRIDE_FINALIZE;
    public static String WEBSERVICE_ENDPOINTINTERFACE_SEI;
    public static String WEBSERVICE_PORTNAME_SEI;
    public static String WEBSERVICE_SERVICENAME_SEI;

    public static String WEBSERVICE_ENPOINTINTERFACE_MUST_IMPLEMENT;
    public static String WEBSERVICE_ENPOINTINTERFACE_REDUCED_VISIBILITY;
    public static String WEBSERVICE_ENPOINTINTERFACE_INCOMPATIBLE_RETURN_TYPE;
    public static String WEBSERVICE_ENPOINTINTERFACE_INCOMPATIBLE_EXCEPTIONS;
    public static String WEBSERVICE_ENPOINTINTERFACE_NOT_INTERFACE;
    public static String WEBSERVICE_ENPOINTINTERFACE_NOT_FOUND;
    public static String WEBSERVICE_ENPOINTINTERFACE_NOT_ANNOTATED;
    public static String WEBSERVICE_ENPOINTINTERFACE_NOT_PUBLIC;
    public static String WEBSERVICE_ENPOINTINTERFACE_NO_WEBMETHODS;
    public static String WEBSERVICE_ENPOINTINTERFACE_NOT_OUTER;
    public static String WEBSERVICE_ENPOINTINTERFACE_NO_NAME_ATTRIBUTE;
    public static String WEBSERVICE_ENPOINTINTERFACE_NO_SOAPBINDING;
    public static String WEBSERVICE_ENPOINTINTERFACE_NO_WEBRESULT;
    public static String WEBSERVICE_ENPOINTINTERFACE_NO_WEBPARAM;
    public static String WEBSERVICE_ENPOINTINTERFACE_NO_ONEWAY;

    public static String WEBSERVICE_WEBSERVICEPROVIDER_COMBINATION;

    public static String WEBSERVICE_DEFAULT_PACKAGE_TARGET_NAMESPACE;

    public static String WEBSERVICE_WSDL_LOCATION_UNABLE_TO_LOCATE;
    public static String WEBSERVICE_WSDL_LOCATION_UNABLE_TO_READ;
    public static String WEBSERVICE_WSDL_LOCATION_NO_PORT_NAME;
    public static String WEBSERVICE_WSDL_LOCATION_NO_SERVICE_NAME;
    public static String WEBSERVICE_WSDL_LOCATION_NO_OPERTATION_NAME;
    public static String WEBSERVICE_WSDL_LOCATION_WSDL_OPERATION_OUTPUT_METHOD_ONEWAY;
    public static String WEBSERVICE_WSDL_LOCATION_SOAP_BINDING_STYLE;

    public static String SOAPBINDING_NO_RPC_STYLE_ON_METHODS;
    public static String SOAPBINDING_RPC_ENCODED_NOT_SUPPORTED;
    public static String SOAPBINDING_DOCUMENT_ENCODED_NOT_SUPPORTED;
    public static String SOAPBINDING_NO_MIXED_BINDINGS;
    public static String SOAPBINDING_RPC_NO_BARE_PARAMETER_STYLE;

    public static String WEBSERVICEPROVIDER_DEFAULT_PUBLIC_CONSTRUCTOR;
    public static String WEBSERVICEPROVIDER_IMPLEMENT_TYPED_PROVIDER_INTERFACE;
    public static String WEBSERVICEPROVIDER_SOAPMESSAGE_SOAPBINDING;
    public static String WEBSERVICEPROVIDER_SOAPMESSAGE_MESSAGE_MODE;
    public static String WEBSERVICEPROVIDER_DATASOURCE_HTTPBINDING;
    public static String WEBSERVICEPROVIDER_DATASOURCE_MESSAGE_MODE;

    public static String OPERATION_NAMES_MUST_BE_UNIQUE_ERROR;
    public static String WRAPPER_FAULT_BEAN_NAMES_MUST_BE_UNIQUE;
    public static String DOC_BARE_METHODS_UNIQUE_XML_ELEMENTS;
    public static String LOCAL_NAME_ATTRIBUTES_MUST_BE_UNIQUE;
    public static String INVALID_NCNAME_ATTRIBUTE;

    public static String PARAMETER_NAME_CLASH;
    public static String GENERATED_PARAMETER_NAME_CLASH;

    public static String INTERFACES_NOT_SUPPORTED;
    public static String HAS_INADMISSIBLE_INNER_TYPES;
    public static String IS_REMOTE_OBJECT;
    public static String ABSTRACT_CLASS_NOT_IMPLEMENTED;
    public static String INHERITANCE_AND_IMPLEMENTATION;
    public static String IMPLEMENTS_MULTIPLE_INTERFACES;

    public static String WEBPARAM_NAME_REDUNDANT;
    public static String WEBSERVICE_ONLY_ON_STATELESS_OR_SINGLETON_SESSION_BEANS;
    public static String TARGET_NAMESPACE_URI_SYNTAX_ERROR;

    public static String EMPTY_ATTRIBUTE_VALUE;
    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, JAXWSCoreMessages.class);
    }

    private JAXWSCoreMessages() {
    }


}
