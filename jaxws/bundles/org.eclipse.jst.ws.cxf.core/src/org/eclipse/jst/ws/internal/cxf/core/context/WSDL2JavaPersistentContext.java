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
import org.eclipse.jst.ws.internal.cxf.core.model.WSDL2JavaContext;

/**
 * @author sclarke
 */
@SuppressWarnings("restriction")
public class WSDL2JavaPersistentContext extends CXFPersistentContext implements WSDL2JavaContext {
    public static final String PREFERENCE_W2J_GENERATE_CLIENT = "cxfW2JGenerateClient"; //$NON-NLS-1$
    public static final String PREFERENCE_W2J_GENERATE_SERVER = "cxfW2JGenerateServer"; //$NON-NLS-1$
    public static final String PREFERENCE_W2J_GENERATE_IMPLEMENTATION = "cxfW2JGenerateImplementation"; //$NON-NLS-1$
    public static final String PREFERENCE_W2J_GENERATE_ANT_BUILD_FILE = "cxfW2JGenerateAntBuildFile"; //$NON-NLS-1$

    public static final String PREFERENCE_W2J_PROCESS_SOAP_HEADERS = "cxfW2JProcessSOAPHeaders"; //$NON-NLS-1$
    public static final String PREFERENCE_W2J_LOAD_DEFAULT_NAMESPACE_PACKAGENAME_MAPPING 
        = "cxfW2JLoadDefaultNamespacePackagenameMapping"; //$NON-NLS-1$
    public static final String PREFERENCE_W2J_LOAD_DEFAULT_EXCLUDES_NAMESPACE_MAPPING 
        = "cxfW2JLoadDefaultExcludesNamepsaceMapping"; //$NON-NLS-1$

    public static final String PREFERENCE_W2J_VALIDATE_WSDL = "cxfW2JValidateWSDL"; //$NON-NLS-1$

    public static final String PREFERENCE_W2J_DATABINDING = "cxfW2JDatabinding"; //$NON-NLS-1$
    public static final String PREFERENCE_W2J_FRONTEND = "cxfW2JFrontend"; //$NON-NLS-1$
    public static final String PREFERENCE_W2J_WSDL_VERSION = "cxfW2JWSDLVersion"; //$NON-NLS-1$
    public static final String PREFERENCE_W2J_XJC_ARGS = "cxfW2JXJCArgs"; //$NON-NLS-1$
    public static final String PREFERENCE_W2J_NO_ADDRESS_BINDING = "cxfW2JNoAddressBinding"; //$NON-NLS-1$
    public static final String PREFERENCE_W2J_USE_DEFAULT_VALUES = "cxfW2JUseDefaultValues"; //$NON-NLS-1$

    public static final String XJC_USE_DEFAULT_VALUES = "xjcUseDefaultValues"; //$NON-NLS-1$
    public static final String XJC_TO_STRING = "xjcToString"; //$NON-NLS-1$
    public static final String XJC_TO_STRING_MULTI = "xjcToStringMulti"; //$NON-NLS-1$
    public static final String XJC_TO_STRING_SIMPLE = "xjcToStringSimple"; //$NON-NLS-1$
    public static final String XJC_LOCATOR = "xjcLocator"; //$NON-NLS-1$
    public static final String XJC_SYNC_METHODS = "xjcSyncMethods"; //$NON-NLS-1$
    public static final String XJC_MARK_GENERATED = "xjcMarkGenerated"; //$NON-NLS-1$
    public static final String XJC_EPISODE_FILE = "xjcEpisodeFile"; //$NON-NLS-1$

    public WSDL2JavaPersistentContext() {
        super(CXFCorePlugin.getDefault());
    }

    public void load() {
        super.load();
        setDefault(PREFERENCE_W2J_GENERATE_CLIENT, Boolean.parseBoolean(
                CXFPackage.eINSTANCE.getCXFContext_GenerateClient().getDefaultValueLiteral()));
        setDefault(PREFERENCE_W2J_GENERATE_SERVER, Boolean.parseBoolean(
                CXFPackage.eINSTANCE.getCXFContext_GenerateServer().getDefaultValueLiteral()));
        setDefault(PREFERENCE_W2J_GENERATE_IMPLEMENTATION, Boolean.parseBoolean(
                CXFPackage.eINSTANCE.getWSDL2JavaContext_GenerateImplementation().getDefaultValueLiteral()));
        setDefault(PREFERENCE_W2J_GENERATE_ANT_BUILD_FILE, Boolean.parseBoolean(
                CXFPackage.eINSTANCE.getCXFContext_GenerateAntBuildFile().getDefaultValueLiteral()));
        setDefault(PREFERENCE_W2J_XJC_ARGS, ""); //$NON-NLS-1$
        setDefault(PREFERENCE_W2J_PROCESS_SOAP_HEADERS, Boolean.parseBoolean(
                CXFPackage.eINSTANCE.getWSDL2JavaContext_ProcessSOAPHeaders().getDefaultValueLiteral()));
        setDefault(PREFERENCE_W2J_LOAD_DEFAULT_NAMESPACE_PACKAGENAME_MAPPING, Boolean.parseBoolean(CXFPackage.
            eINSTANCE.getWSDL2JavaContext_LoadDefaultNamespacePackageNameMapping().getDefaultValueLiteral()));
        setDefault(PREFERENCE_W2J_LOAD_DEFAULT_EXCLUDES_NAMESPACE_MAPPING, Boolean.parseBoolean(CXFPackage.
                eINSTANCE.getWSDL2JavaContext_LoadDefaultExcludesNamepsaceMapping().getDefaultValueLiteral()));
        setDefault(PREFERENCE_W2J_VALIDATE_WSDL, Boolean.parseBoolean(
                CXFPackage.eINSTANCE.getWSDL2JavaContext_Validate().getDefaultValueLiteral()));

        setDefault(PREFERENCE_W2J_DATABINDING, DataBinding.JAXB.getLiteral());
        setDefault(PREFERENCE_W2J_FRONTEND, Frontend.JAXWS.getLiteral());
        setDefault(PREFERENCE_W2J_WSDL_VERSION, CXFPackage.eINSTANCE.getWSDL2JavaContext_WsdlVersion()
                .getDefaultValueLiteral());
        setDefault(PREFERENCE_W2J_NO_ADDRESS_BINDING, Boolean.parseBoolean(
                CXFPackage.eINSTANCE.getWSDL2JavaContext_NoAddressBinding().getDefaultValueLiteral()));
        setDefault(PREFERENCE_W2J_USE_DEFAULT_VALUES, Boolean.parseBoolean(
                CXFPackage.eINSTANCE.getWSDL2JavaContext_UseDefaultValues().getDefaultValueLiteral()));

        setDefault(XJC_USE_DEFAULT_VALUES, Boolean.parseBoolean(
                CXFPackage.eINSTANCE.getWSDL2JavaContext_XjcUseDefaultValues().getDefaultValueLiteral()));
        setDefault(XJC_TO_STRING, Boolean.parseBoolean(
                CXFPackage.eINSTANCE.getWSDL2JavaContext_XjcToString().getDefaultValueLiteral()));
        setDefault(XJC_TO_STRING_MULTI, Boolean.parseBoolean(
                CXFPackage.eINSTANCE.getWSDL2JavaContext_XjcToStringMultiLine().getDefaultValueLiteral()));
        setDefault(XJC_TO_STRING_SIMPLE, Boolean.parseBoolean(
                CXFPackage.eINSTANCE.getWSDL2JavaContext_XjcToStringSimple().getDefaultValueLiteral()));
        setDefault(XJC_LOCATOR, Boolean.parseBoolean(
                CXFPackage.eINSTANCE.getWSDL2JavaContext_XjcLocator().getDefaultValueLiteral()));
        setDefault(XJC_SYNC_METHODS, Boolean.parseBoolean(
                CXFPackage.eINSTANCE.getWSDL2JavaContext_XjcSyncMethods().getDefaultValueLiteral()));
        setDefault(XJC_MARK_GENERATED, Boolean.parseBoolean(
                CXFPackage.eINSTANCE.getWSDL2JavaContext_XjcMarkGenerated().getDefaultValueLiteral()));
        setDefault(XJC_EPISODE_FILE, ""); //$NON-NLS-1$
    }

    public boolean isGenerateAntBuildFile() {
        return getValueAsBoolean(PREFERENCE_W2J_GENERATE_ANT_BUILD_FILE);
    }

    public void setGenerateAntBuildFile(boolean generateAntBuildFile) {
        setValue(PREFERENCE_W2J_GENERATE_ANT_BUILD_FILE, generateAntBuildFile);
    }

    public boolean isGenerateClient() {
        return getValueAsBoolean(PREFERENCE_W2J_GENERATE_CLIENT);
    }

    public void setGenerateClient(boolean generateClient) {
        setValue(PREFERENCE_W2J_GENERATE_CLIENT, generateClient);
    }

    public boolean isGenerateImplementation() {
        return getValueAsBoolean(PREFERENCE_W2J_GENERATE_IMPLEMENTATION);
    }

    public void setGenerateImplementation(boolean generateImplementation) {
        setValue(PREFERENCE_W2J_GENERATE_IMPLEMENTATION, generateImplementation);
    }

    public boolean isGenerateServer() {
        return getValueAsBoolean(PREFERENCE_W2J_GENERATE_SERVER);
    }

    public void setGenerateServer(boolean generateServer) {
        setValue(PREFERENCE_W2J_GENERATE_SERVER, generateServer);
    }

    public boolean isLoadDefaultNamespacePackageNameMapping() {
        return getValueAsBoolean(PREFERENCE_W2J_LOAD_DEFAULT_NAMESPACE_PACKAGENAME_MAPPING);
    }

    public void setLoadDefaultNamespacePackageNameMapping(boolean loadDefaultNamespacePackageNameMapping) {
        setValue(PREFERENCE_W2J_LOAD_DEFAULT_NAMESPACE_PACKAGENAME_MAPPING,
                loadDefaultNamespacePackageNameMapping);
    }

    public boolean isLoadDefaultExcludesNamepsaceMapping() {
        return getValueAsBoolean(PREFERENCE_W2J_LOAD_DEFAULT_EXCLUDES_NAMESPACE_MAPPING);
    }

    public void setLoadDefaultExcludesNamepsaceMapping(boolean loadDefaultExcludesNamepsaceMapping) {
        setValue(PREFERENCE_W2J_LOAD_DEFAULT_EXCLUDES_NAMESPACE_MAPPING, loadDefaultExcludesNamepsaceMapping);
    }

    public boolean isProcessSOAPHeaders() {
        return getValueAsBoolean(PREFERENCE_W2J_PROCESS_SOAP_HEADERS);
    }

    public void setProcessSOAPHeaders(boolean processSoapHeaders) {
        setValue(PREFERENCE_W2J_PROCESS_SOAP_HEADERS, processSoapHeaders);
    }

    public boolean isValidate() {
        return getValueAsBoolean(PREFERENCE_W2J_VALIDATE_WSDL);
    }

    public void setValidate(boolean validate) {
        setValue(PREFERENCE_W2J_VALIDATE_WSDL, validate);
    }

    public DataBinding getDatabinding() {
        return DataBinding.get(getValueAsString(PREFERENCE_W2J_DATABINDING));
    }

    public void setDatabinding(DataBinding dataBinding) {
        setValue(PREFERENCE_W2J_DATABINDING, dataBinding.getLiteral());
    }

    public Frontend getFrontend() {
        return Frontend.get(getValueAsString(PREFERENCE_W2J_FRONTEND));
    }

    public void setFrontend(Frontend frontend) {
        setValue(PREFERENCE_W2J_FRONTEND, frontend.getLiteral());
    }

    public String getWsdlVersion() {
        return getValueAsString(PREFERENCE_W2J_WSDL_VERSION);
    }

    public void setWsdlVersion(String wsdlVersion) {
        setValue(PREFERENCE_W2J_WSDL_VERSION, wsdlVersion);
    }

    public String getXjcArgs() {
        return getValueAsString(PREFERENCE_W2J_XJC_ARGS);
    }

    public void setXjcArgs(String xjcArgs) {
        setValue(PREFERENCE_W2J_XJC_ARGS, xjcArgs);
    }

    public boolean isNoAddressBinding() {
        return getValueAsBoolean(PREFERENCE_W2J_NO_ADDRESS_BINDING);
    }

    public void setNoAddressBinding(boolean noAddressBinding) {
        setValue(PREFERENCE_W2J_NO_ADDRESS_BINDING, noAddressBinding);
    }

    public boolean isUseDefaultValues() {
        return getValueAsBoolean(PREFERENCE_W2J_USE_DEFAULT_VALUES);
    }

    public void setUseDefaultValues(boolean useDefaultValues) {
        setValue(PREFERENCE_W2J_USE_DEFAULT_VALUES, useDefaultValues);
    }

    public boolean isXjcUseDefaultValues() {
        return getValueAsBoolean(XJC_USE_DEFAULT_VALUES);
    }

    public void setXjcUseDefaultValues(boolean useXJCDefaultValues) {
        setValue(XJC_USE_DEFAULT_VALUES, useXJCDefaultValues);
    }

    public boolean isXjcToString() {
        return getValueAsBoolean(XJC_TO_STRING);
    }

    public void setXjcToString(boolean xjcToString) {
        setValue(XJC_TO_STRING, xjcToString);
    }

    public boolean isXjcToStringMultiLine() {
        return getValueAsBoolean(XJC_TO_STRING_MULTI);
    }

    public void setXjcToStringMultiLine(boolean xjcToStringMultiLine) {
        setValue(XJC_TO_STRING_MULTI, xjcToStringMultiLine);
    }

    public boolean isXjcToStringSimple() {
        return getValueAsBoolean(XJC_TO_STRING_SIMPLE);
    }

    public void setXjcToStringSimple(boolean xjcToStringSimple) {
        setValue(XJC_TO_STRING_SIMPLE, xjcToStringSimple);
    }

    public boolean isXjcLocator() {
        return getValueAsBoolean(XJC_LOCATOR);
    }

    public void setXjcLocator(boolean xjcLocator) {
        setValue(XJC_LOCATOR, xjcLocator);
    }

    public boolean isXjcSyncMethods() {
        return getValueAsBoolean(XJC_SYNC_METHODS);
    }

    public void setXjcSyncMethods(boolean xjcSyncMethods) {
        setValue(XJC_SYNC_METHODS, xjcSyncMethods);
    }

    public boolean isXjcMarkGenerated() {
        return getValueAsBoolean(XJC_MARK_GENERATED);
    }

    public void setXjcMarkGenerated(boolean xjcMarkGenerated) {
        setValue(XJC_MARK_GENERATED, xjcMarkGenerated);
    }

    public String getXjcEpisodeFile() {
        return getValueAsString(XJC_EPISODE_FILE);
    }

    public void setXjcEpisodeFile(String xjcEpisodeFile) {
        setValue(XJC_EPISODE_FILE, xjcEpisodeFile);
    }

}
