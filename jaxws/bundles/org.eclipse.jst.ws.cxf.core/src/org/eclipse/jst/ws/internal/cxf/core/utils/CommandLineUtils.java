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
package org.eclipse.jst.ws.internal.cxf.core.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.jst.ws.internal.common.ResourceUtils;
import org.eclipse.jst.ws.internal.cxf.core.CXFCorePlugin;
import org.eclipse.jst.ws.internal.cxf.core.model.Java2WSDataModel;
import org.eclipse.jst.ws.internal.cxf.core.model.WSDL2JavaDataModel;
import org.eclipse.jst.ws.jaxws.core.utils.JDTUtils;

/**
 * @author sclarke
 */
@SuppressWarnings("restriction")
public final class CommandLineUtils {
    // Java2WSDL
    private static String OUT_FILE = "-o"; //$NON-NLS-1$
    private static String RESOURCE_DIR = "-d"; //$NON-NLS-1$
    private static String SOURCE_DIR = "-s"; //$NON-NLS-1$
    private static String CLASS_DIR = "-classdir"; //$NON-NLS-1$
    private static String J2W_CLASSPATH = "-cp"; //$NON-NLS-1$
    private static String INC_SOAP12 = "-soap12"; //$NON-NLS-1$
    private static String TARGET_NAMESPACE = "-t"; //$NON-NLS-1$
    private static String J2W_SERVICE_NAME = "-servicename"; //$NON-NLS-1$
    private static String PORT_NAME = "-portname"; //$NON-NLS-1$
    private static String GEN_XSD_IMPORTS = "-createxsdimports"; //$NON-NLS-1$
    private static String VERBOSE = "-verbose"; //$NON-NLS-1$

    // Java2WS
    private static String J2W_FRONTEND = "-frontend"; //$NON-NLS-1$
    private static String J2W_DATABINDING = "-databinding"; //$NON-NLS-1$
    private static String BEAN_PATH = "-beans"; //$NON-NLS-1$
    private static String GEN_WSDL = "-wsdl"; //$NON-NLS-1$
    private static String GEN_CLIENT = "-client"; //$NON-NLS-1$
    private static String GEN_SERVER = "-server"; //$NON-NLS-1$
    private static String GEN_WRAPPER_FAULT = "-wrapperbean"; //$NON-NLS-1$

    private static String W2J_PACKAGE_NAME = "-p"; //$NON-NLS-1$
    private static String W2J_BINDING_NAME = "-b"; //$NON-NLS-1$

    private static String W2J_FRONTEND = "-fe"; //$NON-NLS-1$
    private static String W2J_DATABINDING = "-db"; //$NON-NLS-1$
    private static String W2J_WSDL_VERSION = "-wv"; //$NON-NLS-1$
    private static String W2J_SERVICE_NAME = "-sn"; //$NON-NLS-1$
    private static String W2J_CATALOG = "-catalog"; //$NON-NLS-1$
    private static String W2J_COMPILE = "-compile"; //$NON-NLS-1$
    private static String W2J_GEN_IMPL = "-impl"; //$NON-NLS-1$
    private static String W2J_GEN_ALL = "-all"; //$NON-NLS-1$
    private static String W2J_NO_OVERWRITE = "-keep"; //$NON-NLS-1$
    private static String W2J_DEFAULT_VALUES = "-defaultValues"; //$NON-NLS-1$
    private static String W2J_AUTO_NAME_RESOLUTION = "-autoNameResolution"; //$NON-NLS-1$
    private static String W2J_EXCLUDE_NAMESPACE = "-nexclude"; //$NON-NLS-1$
    private static String W2J_EXT_SOAP_HEADER = "-exsh"; //$NON-NLS-1$
    private static String W2J_DEFAULT_NAMESPACE = "-dns"; //$NON-NLS-1$
    private static String W2J_DEFAULT_EXCLUDE_NS = "-dex"; //$NON-NLS-1$
    private static String W2J_WSDL_LOCATION = "-wsdlLocation"; //$NON-NLS-1$
    private static String W2J_XJC_ARGS = "-xjc"; //$NON-NLS-1$
    private static String W2J_NO_ADDRESS_BINDING = "-noAddressBinding"; //$NON-NLS-1$
    private static String W2J_VALIDATE_WSDL = "-validate"; //$NON-NLS-1$

    // XJC
    private static final String XJC_DV_ARG = "-Xdv"; //$NON-NLS-1$
    private static final String XJC_TS_ARG = "-Xts"; //$NON-NLS-1$
    private static final String XJC_TS_MULTI_ARG = "-Xts:style:multiline"; //$NON-NLS-1$
    private static final String XJC_TS_SIMPLE = "-Xts:style:simple"; //$NON-NLS-1$
    private static final String XJC_LOCATOR_ARG = "-Xlocator"; //$NON-NLS-1$
    private static final String XJC_SYNC_METHODS_ARG = "-Xsync-methods"; //$NON-NLS-1$
    private static final String XJC_MARK_GENERATED_ARG = "-mark-generated"; //$NON-NLS-1$
    private static final String XJC_EPISODE_FILE_ARG = "-episode"; //$NON-NLS-1$

    private CommandLineUtils() {
    }
    
    public static String[] getJava2WSProgramArguments(Java2WSDataModel model) {
        List<String> progArgs = new ArrayList<String>();

        IProject project = ResourceUtils.getWorkspaceRoot().getProject(model.getProjectName());
        if (project != null && project.exists() && JDTUtils.isJavaProject(project)) {
            String className = model.getJavaStartingPoint();
            if (model.isUseServiceEndpointInterface() && model.getFullyQualifiedJavaClassName() != null) {
                className = model.getFullyQualifiedJavaClassName();
            }
            // Add all the earlier wsdl2java tool options
            progArgs.addAll(Arrays.asList(CommandLineUtils.getStandardJava2WSDLProgramArguments(model)));

            if (model.getCxfRuntimeVersion().compareTo(CXFCorePlugin.CXF_VERSION_2_1) >= 0) {
                progArgs.add(J2W_FRONTEND);
                progArgs.add(model.getFrontend().getLiteral());

                progArgs.add(J2W_DATABINDING);
                progArgs.add(model.getDatabinding().getLiteral());

                // progArgs.add(BEAN_PATH);
                // progArgs.add("BEAN_PATH");

                if (model.isGenerateWSDL()) {
                    progArgs.add(GEN_WSDL);
                } else {
                    progArgs.remove(OUT_FILE);
                    progArgs.remove(model.getWsdlFileName());
                }

                if (model.isGenerateClient()) {
                    progArgs.add(GEN_CLIENT);
                }
                
                if (model.isGenerateServer()) {
                    progArgs.add(GEN_SERVER);
                }

                if (model.isGenerateWrapperFaultBeans()) {
                    progArgs.add(GEN_WRAPPER_FAULT);
                }
            }

            progArgs.add(className);
        }
        return (String[]) progArgs.toArray(new String[progArgs.size()]);
    }
    
    private static String[] getStandardJava2WSDLProgramArguments(Java2WSDataModel model) {
        String projectName = model.getProjectName();
        List<String> progArgs = new ArrayList<String>();
        progArgs.add(J2W_CLASSPATH);
        progArgs.add(JDTUtils.getJavaProjectOutputDirectoryPath(projectName));

        progArgs.add(SOURCE_DIR);
        progArgs.add(FileUtils.getTmpFolder(projectName) + "/src"); //$NON-NLS-1$

        progArgs.add(RESOURCE_DIR);
        progArgs.add(FileUtils.getTmpFolder(projectName) + "/wsdl"); //$NON-NLS-1$

        progArgs.add(CLASS_DIR);
        progArgs.add(JDTUtils.getJavaProjectOutputDirectoryPath(projectName));

        progArgs.add(OUT_FILE);
        progArgs.add(((Java2WSDataModel) model).getWsdlFileName());

        if (model.isSoap12Binding()) {
            progArgs.add(INC_SOAP12);
        }
        if (model.isGenerateXSDImports()) {
            progArgs.add(GEN_XSD_IMPORTS);
        }

        if (model.isVerbose()) {
            progArgs.add(VERBOSE);
        }
        
        return (String[]) progArgs.toArray(new String[progArgs.size()]);
    }

    public static String[] getWSDL2JavaProgramArguments(WSDL2JavaDataModel model) {
        String projectName = model.getProjectName();
        List<String> progArgs = new ArrayList<String>();

        IProject project = ResourceUtils.getWorkspaceRoot().getProject(projectName);
        if (project != null && project.exists() && JDTUtils.isJavaProject(project)) {
            // Add Standard args
            progArgs.addAll(Arrays.asList(CommandLineUtils.getStandardWSDL2JavaProgramArguments(model,
                    projectName)));
            
            String serviceName = model.getServiceName();
            if (serviceName != null && serviceName.length() > 0) {
                progArgs.add(W2J_SERVICE_NAME);
                progArgs.add(serviceName);
            }
            
            if (model.getCxfRuntimeVersion().compareTo(CXFCorePlugin.CXF_VERSION_2_1) >= 0) {
                progArgs.add(W2J_FRONTEND);
                progArgs.add(model.getFrontend().getLiteral());

                progArgs.add(W2J_DATABINDING);
                progArgs.add(model.getDatabinding().getLiteral());

                progArgs.add(W2J_WSDL_VERSION);
                progArgs.add(model.getWsdlVersion());

//                if (model.isAutoNameResolution()) {
//                    progArgs.add(W2J_AUTO_NAME_RESOLUTION);
//                }
                
                if (model.isUseDefaultValues()) {
                    progArgs.add(W2J_DEFAULT_VALUES);
                }
                
                if (model.isNoAddressBinding()) {
                    progArgs.add(W2J_NO_ADDRESS_BINDING);
                }
            }

            progArgs.add(model.getWsdlURL().toExternalForm());
        }
        return (String[]) progArgs.toArray(new String[progArgs.size()]);
    }

    public static String[] getWSDL2JavaGenerateClientArguments(WSDL2JavaDataModel model) {
        String projectName = model.getProjectName();
        List<String> progArgs = new ArrayList<String>();

        IProject project = ResourceUtils.getWorkspaceRoot().getProject(projectName);
        if (project != null && project.exists() && JDTUtils.isJavaProject(project)) {
            // Add Standard args
            progArgs.addAll(Arrays.asList(CommandLineUtils.getWSDL2JavaProgramArguments(model)));
            
            progArgs.add(GEN_CLIENT);
            progArgs.add(model.getWsdlURL().toExternalForm());
        }
        return (String[]) progArgs.toArray(new String[progArgs.size()]);
    }
    
    public static String[] getStandardWSDL2JavaProgramArguments(WSDL2JavaDataModel model, String projectName) {
        List<String> progArgs = new ArrayList<String>();
        progArgs.add(RESOURCE_DIR);
        progArgs.add(FileUtils.getTmpFolder(projectName) + "/src"); //$NON-NLS-1$

        progArgs.add(CLASS_DIR);
        progArgs.add(JDTUtils.getJavaProjectOutputDirectoryPath(projectName));

        Map<String, String> includedNamespaces = model.getIncludedNamespaces();
        if (includedNamespaces != null && model.getIncludedNamespaces().size() > 0) {
            Set<Map.Entry<String, String>> includedNamespacesEntrySet = includedNamespaces.entrySet();
            for (Map.Entry<String, String> entry : includedNamespacesEntrySet) {
                progArgs.add(W2J_PACKAGE_NAME);
                String wsdlNamespace = entry.getKey();
                String packageName = entry.getValue();
                progArgs.add(wsdlNamespace + "=" + packageName); //$NON-NLS-1$
            }
        }

        List<String> bindingFiles = model.getBindingFiles();
        if (!bindingFiles.isEmpty()) {
            for (String bindingFile : bindingFiles) {
                progArgs.add(W2J_BINDING_NAME);
                progArgs.add(bindingFile);
            }
        }
        
        if (model.isGenerateServer()) {
            progArgs.add(GEN_SERVER);
        }

        if (model.isGenerateImplementation()) {
            progArgs.add(W2J_GEN_IMPL);
        }

        if(model.isValidate()) {
            progArgs.add(W2J_VALIDATE_WSDL);
        }
        
        progArgs.add(W2J_EXT_SOAP_HEADER);
        progArgs.add(Boolean.toString(model.isProcessSOAPHeaders()));
        
        progArgs.add(W2J_DEFAULT_NAMESPACE);
        progArgs.add(Boolean.toString(model.isLoadDefaultNamespacePackageNameMapping()));
        
        progArgs.add(W2J_DEFAULT_EXCLUDE_NS);
        progArgs.add(Boolean.toString(model.isLoadDefaultExcludesNamepsaceMapping()));

        String xjcArgs = CommandLineUtils.getXJCArgs(model);
        if (xjcArgs.trim().length() > 0) {
            progArgs.add(W2J_XJC_ARGS + xjcArgs);
        }

        if (model.getWsdlLocation() != null) {
            progArgs.add(W2J_WSDL_LOCATION);
            progArgs.add(model.getWsdlLocation());
        }
        
        if (model.isVerbose()) {
            progArgs.add(VERBOSE);
        }
        return (String[]) progArgs.toArray(new String[progArgs.size()]);
    }

    private static String getXJCArgs(WSDL2JavaDataModel model) {
        List<String> xjcArgs = new ArrayList<String>();
        if (model.isXjcUseDefaultValues()) {
            xjcArgs.add(XJC_DV_ARG);
        }
        if (model.isXjcToString()) {
            xjcArgs.add(XJC_TS_ARG);
        }
        if (model.isXjcToStringMultiLine()) {
            xjcArgs.add(XJC_TS_MULTI_ARG);
        }
        if (model.isXjcToStringSimple()) {
            xjcArgs.add(XJC_TS_SIMPLE);
        }
        if (model.isXjcLocator()) {
            xjcArgs.add(XJC_LOCATOR_ARG);
        }
        if (model.isXjcSyncMethods()) {
            xjcArgs.add(XJC_SYNC_METHODS_ARG);
        }
        if (model.isXjcMarkGenerated()) {
            xjcArgs.add(XJC_MARK_GENERATED_ARG);
        }

        String xjcArg = xjcArgs.toString();
        xjcArg = xjcArg.replace('[', ' ');        
        xjcArg = xjcArg.replace(']', ' ');
        
        xjcArg = xjcArg.replaceAll("\\s", ""); //$NON-NLS-1$ //$NON-NLS-2$
        
        return xjcArg.trim();
    }
}
