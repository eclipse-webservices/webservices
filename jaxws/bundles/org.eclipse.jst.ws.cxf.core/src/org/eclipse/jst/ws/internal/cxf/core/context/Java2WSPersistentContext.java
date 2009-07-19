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
package org.eclipse.jst.ws.internal.cxf.core.context;

import org.eclipse.jst.ws.internal.cxf.core.CXFCorePlugin;
import org.eclipse.jst.ws.internal.cxf.core.model.CXFPackage;
import org.eclipse.jst.ws.internal.cxf.core.model.DataBinding;
import org.eclipse.jst.ws.internal.cxf.core.model.Frontend;
import org.eclipse.jst.ws.internal.cxf.core.model.Java2WSContext;
import org.eclipse.jst.ws.internal.cxf.core.utils.CXFModelUtils;

@SuppressWarnings("restriction")
public class Java2WSPersistentContext extends CXFPersistentContext implements Java2WSContext {
    /**
     * String constant used to lookup the cxf soap binding general preference
     * from the plugins local preferences store.
     */
    private static final String PREFERENCE_J2WS_SOAP12_BINDING = "cxfJ2WSSoap12Binding"; //$NON-NLS-1$
    /**
     * String constant used to lookup the cxf generate xsd imports general
     * preference from the plugins local preferences store.
     */
    private static final String PREFERENCE_J2WS_GENERATE_XSD_IMPORTS = "cxfJ2WSXSDImports"; //$NON-NLS-1$

    /**
     * String constant used to lookup the cxf default frontend general
     * preference from the plugins local preferences store.
     */
    public static final String PREFERENCE_J2WS_DEFAULT_FRONTEND = "cxfJ2WSDefaultFrontend"; //$NON-NLS-1$

    /**
     * String constant used to lookup the cxf default Databinding general
     * preference from the plugins local preferences store.
     */
    public static final String PREFERENCE_J2WS_DEFAULT_DATABINDING = "cxfJ2WSDefaultDatabinding"; //$NON-NLS-1$

    /**
     * String constant used to lookup the cxf generate client general preference
     * from the plugins local preferences store.
     */
    public static final String PREFERENCE_J2WS_GENERATE_CLIENT = "cxfJ2WSGenerateClient"; //$NON-NLS-1$

    /**
     * String constant used to lookup the cxf generate server general preference
     * from the plugins local preferences store.
     */
    public static final String PREFERENCE_J2WS_GENERATE_SERVER = "cxfJ2WSGenerateServer"; //$NON-NLS-1$

    /**
     * String constant used to lookup the cxf generate wrapper and fault beans
     * general preference from the plugins local preferences store.
     */
    public static final String PREFERENCE_J2WS_GENERATE_WRAPPER_FAULT_BEANS = "cxfJ2WSGenerateWrapperFault"; //$NON-NLS-1$

    /**
     * String constant used to lookup the cxf generate wsdl general preference
     * from the plugins local preferences store.
     */
    public static final String PREFERENCE_J2WS_GENERATE_WSDL = "cxfJ2WSGenerateWSDL"; //$NON-NLS-1$

    /**
     * String constant used to lookup the cxf generate ant build file general
     * preference from the plugins local preferences store.
     */
    public static final String PREFERENCE_J2WS_GENERATE_ANT_BUILD_FILE = "cxfJ2WSGenerateANTBuildFile"; //$NON-NLS-1$

    /**
     * String constant used to lookup the APT annotation processing general
     * preference from the plugins local preferences store.
     */
    public static final String PREFERENCE_J2WS_ENABLE_ANNOTATION_PROCESSING 
        = "cxfJ2WSEnableAnnotationProcessing"; //$NON-NLS-1$
 
    /**
     * String constant used to lookup the generate <code>@WebMethod</code> annotation general
     * preference from the plugins local preferences store.
     */
    public static final String PREFERENCE_GENERATE_WEB_METHOD_ANNOTATION 
        = "cxfJ2WSGenerateWebMethodAnnotation"; //$NON-NLS-1$
 
    /**
     * String constant used to lookup the generate <code>@WebParam</code> annotation general
     * preference from the plugins local preferences store.
     */
    public static final String PREFERENCE_GENERATE_WEB_PARAM_ANNOTATION 
        = "cxfJ2WSGenerateWebParamAnnotation"; //$NON-NLS-1$
 
    /**
     * String constant used to lookup the generate <code>@RequestWrapper</code> annotation general
     * preference from the plugins local preferences store.
     */
    public static final String PREFERENCE_GENERATE_REQUEST_WRAPPER_ANNOTATION 
        = "cxfJ2WSGenerateRequestWrapperAnnotation"; //$NON-NLS-1$

    /**
     * String constant used to lookup the generate <code>@ResponseWrapper</code> annotation general
     * preference from the plugins local preferences store.
     */
    public static final String PREFERENCE_GENERATE_RESPONSE_WRAPPER_ANNOTATION 
        = "cxfJ2WSGenerateResponseWrapperAnnotation"; //$NON-NLS-1$

    public Java2WSPersistentContext() {
        super(CXFCorePlugin.getDefault());
    }

    public void load() {
        super.load();
        setDefault(PREFERENCE_J2WS_SOAP12_BINDING, CXFModelUtils.getDefaultBooleanValue(
                CXFPackage.JAVA2_WS_CONTEXT, CXFPackage.JAVA2_WS_CONTEXT__SOAP12_BINDING));

        setDefault(PREFERENCE_J2WS_GENERATE_XSD_IMPORTS, CXFModelUtils.getDefaultBooleanValue(
                CXFPackage.JAVA2_WS_CONTEXT, CXFPackage.JAVA2_WS_CONTEXT__GENERATE_XSD_IMPORTS));

        setDefault(PREFERENCE_J2WS_DEFAULT_DATABINDING, DataBinding.JAXB.getLiteral());

        setDefault(PREFERENCE_J2WS_DEFAULT_FRONTEND, Frontend.JAXWS.getLiteral());

        setDefault(PREFERENCE_J2WS_GENERATE_CLIENT, CXFModelUtils.getDefaultBooleanValue(
                CXFPackage.CXF_CONTEXT, CXFPackage.CXF_CONTEXT__GENERATE_CLIENT));

        setDefault(PREFERENCE_J2WS_GENERATE_SERVER, CXFModelUtils.getDefaultBooleanValue(
                CXFPackage.CXF_CONTEXT, CXFPackage.CXF_CONTEXT__GENERATE_SERVER));

        setDefault(PREFERENCE_J2WS_GENERATE_WRAPPER_FAULT_BEANS, CXFModelUtils.getDefaultBooleanValue(
                CXFPackage.JAVA2_WS_CONTEXT, CXFPackage.JAVA2_WS_CONTEXT__GENERATE_WRAPPER_FAULT_BEANS));

        setDefault(PREFERENCE_J2WS_GENERATE_WSDL, CXFModelUtils.getDefaultBooleanValue(
                CXFPackage.JAVA2_WS_CONTEXT, CXFPackage.JAVA2_WS_CONTEXT__GENERATE_WSDL));

        setDefault(PREFERENCE_J2WS_ENABLE_ANNOTATION_PROCESSING, CXFModelUtils.getDefaultBooleanValue(
                CXFPackage.JAVA2_WS_CONTEXT, CXFPackage.JAVA2_WS_CONTEXT__ANNOTATION_PROCESSING_ENABLED));

        //TODO MOVE THE FOLLOWING 4
        setDefault(PREFERENCE_GENERATE_WEB_METHOD_ANNOTATION, CXFModelUtils.getDefaultBooleanValue(
                CXFPackage.JAVA2_WS_CONTEXT, CXFPackage.JAVA2_WS_CONTEXT__GENERATE_WEB_METHOD_ANNOTATION));

        setDefault(PREFERENCE_GENERATE_WEB_PARAM_ANNOTATION, CXFModelUtils.getDefaultBooleanValue(
                CXFPackage.JAVA2_WS_CONTEXT, CXFPackage.JAVA2_WS_CONTEXT__GENERATE_WEB_PARAM_ANNOTATION));

        setDefault(PREFERENCE_GENERATE_REQUEST_WRAPPER_ANNOTATION, CXFModelUtils
                .getDefaultBooleanValue(CXFPackage.JAVA2_WS_CONTEXT,
                        CXFPackage.JAVA2_WS_CONTEXT__GENERATE_REQUEST_WRAPPER_ANNOTATION));

        setDefault(PREFERENCE_GENERATE_RESPONSE_WRAPPER_ANNOTATION, CXFModelUtils.getDefaultBooleanValue(
                CXFPackage.JAVA2_WS_CONTEXT,
                CXFPackage.JAVA2_WS_CONTEXT__GENERATE_RESPONSE_WRAPPER_ANNOTATION));
    }

    public boolean isSoap12Binding() {
        return getValueAsBoolean(PREFERENCE_J2WS_SOAP12_BINDING);
    }

    public void setSoap12Binding(boolean soap12Binding) {
        setValue(PREFERENCE_J2WS_SOAP12_BINDING, soap12Binding);
    }

    public boolean isGenerateXSDImports() {
        return getValueAsBoolean(PREFERENCE_J2WS_GENERATE_XSD_IMPORTS);
    }

    public void setGenerateXSDImports(boolean generateXSDImports) {
        setValue(PREFERENCE_J2WS_GENERATE_XSD_IMPORTS, generateXSDImports);
    }

    public DataBinding getDatabinding() {
        return DataBinding.get(getValueAsString(PREFERENCE_J2WS_DEFAULT_DATABINDING));
    }

    public void setDatabinding(DataBinding dataBinding) {
        setValue(PREFERENCE_J2WS_DEFAULT_DATABINDING, dataBinding.getLiteral());
    }

    public Frontend getFrontend() {
        return Frontend.get(getValueAsString(PREFERENCE_J2WS_DEFAULT_FRONTEND));
    }

    public void setFrontend(Frontend defaultFrontend) {
        setValue(PREFERENCE_J2WS_DEFAULT_FRONTEND, defaultFrontend.getLiteral());
    }

    public boolean isGenerateClient() {
        return getValueAsBoolean(PREFERENCE_J2WS_GENERATE_CLIENT);
    }

    public void setGenerateClient(boolean generateClient) {
        setValue(PREFERENCE_J2WS_GENERATE_CLIENT, generateClient);
    }

    public boolean isGenerateServer() {
        return getValueAsBoolean(PREFERENCE_J2WS_GENERATE_SERVER);
    }

    public void setGenerateServer(boolean generateServer) {
        setValue(PREFERENCE_J2WS_GENERATE_SERVER, generateServer);
    }

    public boolean isGenerateWrapperFaultBeans() {
        return getValueAsBoolean(PREFERENCE_J2WS_GENERATE_WRAPPER_FAULT_BEANS);
    }

    public void setGenerateWrapperFaultBeans(boolean generateWrapperFaultBeans) {
        setValue(PREFERENCE_J2WS_GENERATE_WRAPPER_FAULT_BEANS, generateWrapperFaultBeans);
    }

    public boolean isGenerateWSDL() {
        return getValueAsBoolean(PREFERENCE_J2WS_GENERATE_WSDL);
    }

    public void setGenerateWSDL(boolean generateWSDL) {
        setValue(PREFERENCE_J2WS_GENERATE_WSDL, generateWSDL);
    }

    public boolean isGenerateAntBuildFile() {
        return getValueAsBoolean(PREFERENCE_J2WS_GENERATE_ANT_BUILD_FILE);
    }

    public void setGenerateAntBuildFile(boolean generateAntBuildFile) {
        setValue(PREFERENCE_J2WS_GENERATE_ANT_BUILD_FILE, generateAntBuildFile);
    }

    public boolean isAnnotationProcessingEnabled() {
        return getValueAsBoolean(PREFERENCE_J2WS_ENABLE_ANNOTATION_PROCESSING);
    }

    public boolean isGenerateWebMethodAnnotation() {
        return getValueAsBoolean(PREFERENCE_GENERATE_WEB_METHOD_ANNOTATION);
    }

    public boolean isGenerateWebParamAnnotation() {
        return getValueAsBoolean(PREFERENCE_GENERATE_WEB_PARAM_ANNOTATION);
    }

    public void setGenerateWebParamAnnotation(boolean generateWebParamAnnotation) {
        setValue(PREFERENCE_GENERATE_WEB_PARAM_ANNOTATION, generateWebParamAnnotation);
    }
    
    public void setGenerateWebMethodAnnotation(boolean generateWebMethodAnnotation) {
        setValue(PREFERENCE_GENERATE_WEB_METHOD_ANNOTATION, generateWebMethodAnnotation);
    }
    
    public void setAnnotationProcessingEnabled(boolean enableAnnotationProcessing) {
        setValue(PREFERENCE_J2WS_ENABLE_ANNOTATION_PROCESSING, enableAnnotationProcessing);
    }
    
    public boolean isGenerateRequestWrapperAnnotation() {
        return getValueAsBoolean(PREFERENCE_GENERATE_REQUEST_WRAPPER_ANNOTATION);
    }

    public void setGenerateRequestWrapperAnnotation(boolean generateRequestWrapperAnnotation) {
        setValue(PREFERENCE_GENERATE_REQUEST_WRAPPER_ANNOTATION, generateRequestWrapperAnnotation);
    }

    public boolean isGenerateResponseWrapperAnnotation() {
        return getValueAsBoolean(PREFERENCE_GENERATE_RESPONSE_WRAPPER_ANNOTATION);
    }

    public void setGenerateResponseWrapperAnnotation(boolean generateResponseWrapperAnnotation) {
        setValue(PREFERENCE_GENERATE_RESPONSE_WRAPPER_ANNOTATION, generateResponseWrapperAnnotation);
    }
}
