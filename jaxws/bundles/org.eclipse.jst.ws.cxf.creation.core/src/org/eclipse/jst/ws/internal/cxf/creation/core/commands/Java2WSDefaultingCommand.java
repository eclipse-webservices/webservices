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
package org.eclipse.jst.ws.internal.cxf.creation.core.commands;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jst.ws.internal.cxf.core.CXFCorePlugin;
import org.eclipse.jst.ws.internal.cxf.core.context.Java2WSPersistentContext;
import org.eclipse.jst.ws.internal.cxf.core.model.CXFDataModel;
import org.eclipse.jst.ws.internal.cxf.core.model.Java2WSDataModel;
import org.eclipse.jst.ws.internal.cxf.core.utils.CXFModelUtils;
import org.eclipse.jst.ws.internal.cxf.creation.core.CXFCreationCorePlugin;
import org.eclipse.jst.ws.jaxws.core.utils.JDTUtils;
import org.eclipse.jst.ws.jaxws.core.utils.WSDLUtils;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;

public class Java2WSDefaultingCommand extends AbstractDataModelOperation {
    private Java2WSDataModel model;

    public Java2WSDefaultingCommand(Java2WSDataModel model) {
        this.model = model;
    }

    @Override
    public IStatus execute(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
        IStatus status = Status.OK_STATUS;
        try {
            Java2WSPersistentContext context = CXFCorePlugin.getDefault().getJava2WSContext();
            model.setDefaultRuntimeVersion(context.getDefaultRuntimeVersion());
            model.setDefaultRuntimeType(context.getDefaultRuntimeType());

            IType startingPointType = getJavaStartingPointType();
            model.setUseServiceEndpointInterface(startingPointType.isInterface());
            model.setExtractInterface(false);

            String packageName = startingPointType.getPackageFragment().getElementName();
            model.setTargetNamespace(JDTUtils.getTargetNamespaceFromPackageName(packageName));

            model.setAnnotationProcessingEnabled(context.isAnnotationProcessingEnabled());
            model.setGenerateWebMethodAnnotation(context.isGenerateWebMethodAnnotation());
            model.setGenerateWebParamAnnotation(context.isGenerateWebParamAnnotation());
            model.setGenerateRequestWrapperAnnotation(context.isGenerateRequestWrapperAnnotation());
            model.setGenerateResponseWrapperAnnotation(context.isGenerateResponseWrapperAnnotation());
            model.setGenerateWebResultAnnotation(context.isGenerateWebResultAnnotation());
            model.setAnnotationMap(CXFModelUtils.getAnnotationMap(model));
            model.setMethodMap(CXFModelUtils.getMethodMap(startingPointType, model));

            model.setGenerateXSDImports(context.isGenerateXSDImports());
            model.setDatabinding(context.getDatabinding());
            model.setFrontend(context.getFrontend());
            model.setGenerateClient(context.isGenerateClient());
            model.setGenerateServer(context.isGenerateServer());
            model.setSoap12Binding(context.isSoap12Binding());
            model.setGenerateWrapperFaultBeans(context.isGenerateWrapperFaultBeans());
            model.setGenerateWSDL(context.isGenerateWSDL());
            model.setUseSpringApplicationContext(context.isUseSpringApplicationContext());
            model.setVerbose(context.isVerbose());

            String className = getClassName(model.getProjectName(), model.getJavaStartingPoint());
            model.setWsdlFileName(className.toLowerCase() + WSDLUtils.WSDL_FILE_EXTENSION);
        } catch (JavaModelException jme) {
            status = jme.getStatus();
            CXFCreationCorePlugin.log(status);
        }
        return status;
    }

    public CXFDataModel getJava2WSDataModel() {
        return model;
    }

    public IType getJavaStartingPointType() {
        String projectName = model.getProjectName();
        String javaStartingPoint = model.getJavaStartingPoint();

        IJavaProject javaProject = JDTUtils.getJavaProject(projectName);
        IType startingPointType = JDTUtils.findType(javaProject, javaStartingPoint);
        return startingPointType;
    }

    public String getClassName(String projectName, String fullyQualifiedClassName) {
        return JDTUtils.findType(JDTUtils.getJavaProject(projectName), fullyQualifiedClassName)
        .getElementName();
    }

    @Override
    public IStatus undo(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
        IStatus status = Status.OK_STATUS;

        return status;
    }
}
