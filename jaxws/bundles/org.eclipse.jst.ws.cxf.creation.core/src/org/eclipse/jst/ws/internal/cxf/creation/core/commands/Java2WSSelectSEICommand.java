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

import java.lang.reflect.InvocationTargetException;
import java.util.Set;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.corext.refactoring.structure.ExtractInterfaceProcessor;
import org.eclipse.jdt.internal.ui.actions.WorkbenchRunnableAdapter;
import org.eclipse.jdt.internal.ui.preferences.JavaPreferencesSettings;
import org.eclipse.jdt.internal.ui.util.BusyIndicatorRunnableContext;
import org.eclipse.jst.ws.internal.cxf.core.model.Java2WSDataModel;
import org.eclipse.jst.ws.internal.cxf.core.utils.CXFModelUtils;
import org.eclipse.jst.ws.internal.cxf.creation.core.CXFCreationCorePlugin;
import org.eclipse.jst.ws.jaxws.core.utils.JDTUtils;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.CheckConditionsOperation;
import org.eclipse.ltk.core.refactoring.CreateChangeOperation;
import org.eclipse.ltk.core.refactoring.PerformChangeOperation;
import org.eclipse.ltk.core.refactoring.Refactoring;
import org.eclipse.ltk.core.refactoring.RefactoringCore;
import org.eclipse.ltk.core.refactoring.participants.ProcessorBasedRefactoring;
import org.eclipse.ltk.core.refactoring.participants.RefactoringProcessor;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;

@SuppressWarnings("restriction")
public class Java2WSSelectSEICommand extends AbstractDataModelOperation {
    private Change undoExtractInterfaceChange;

    private Java2WSDataModel model;
    
    private  IType startingPointType;

    public Java2WSSelectSEICommand(Java2WSDataModel model) {
        this.model = model;
    }

    @Override
    public IStatus execute(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
        IStatus status = Status.OK_STATUS;
        try {
            String projectName = model.getProjectName();
            String javaStartingPoint = model.getJavaStartingPoint();

            IJavaProject javaProject = JDTUtils.getJavaProject(projectName);
            startingPointType = JDTUtils.getType(javaProject, javaStartingPoint);
            
            if (startingPointType.isInterface()) {
                model.setFullyQualifiedJavaInterfaceName(startingPointType.getFullyQualifiedName());
            } else if (startingPointType.isClass()) {
                model.setFullyQualifiedJavaClassName(startingPointType.getFullyQualifiedName());
                if (model.isUseServiceEndpointInterface() && model.isExtractInterface()) {
                    extractInterface(startingPointType, monitor);
                }
            }
        } catch (JavaModelException jme) {
            status = jme.getStatus();
            CXFCreationCorePlugin.log(status);
        }

        return status;
    }

    private void extractInterface(IType type, IProgressMonitor monitor) {
        try {
            final ExtractInterfaceProcessor extractInterfaceProcessor = new ExtractInterfaceProcessor(type,
                    JavaPreferencesSettings.getCodeGenerationSettings(type.getJavaProject()));
            Refactoring extractInterfaceRefactoring = new ProcessorBasedRefactoring(extractInterfaceProcessor) {

                @Override
                public RefactoringProcessor getProcessor() {
                    return extractInterfaceProcessor;
                }
            };
            extractInterfaceProcessor.setTypeName(model.getServiceEndpointInterfaceName());
            extractInterfaceProcessor.setReplace(false);
            
            Set<IMethod> methods = model.getMethodMap().keySet();
            extractInterfaceProcessor.setExtractedMembers(methods.toArray(new IMember[methods.size()]));
            extractInterfaceProcessor.setAbstract(false);
            extractInterfaceProcessor.setPublic(true);
            extractInterfaceProcessor.setComments(false);
            extractInterfaceProcessor.setInstanceOf(false);

            CreateChangeOperation createChangeOperation = new CreateChangeOperation(
                    new CheckConditionsOperation(extractInterfaceRefactoring,
                            CheckConditionsOperation.FINAL_CONDITIONS), RefactoringCore
                            .getConditionCheckingFailedSeverity());
            
            PerformChangeOperation performChangeOperation = new PerformChangeOperation(createChangeOperation);
            
            WorkbenchRunnableAdapter adapter = new WorkbenchRunnableAdapter(performChangeOperation);
            PlatformUI.getWorkbench().getProgressService().runInUI(
                new BusyIndicatorRunnableContext(), adapter, adapter.getSchedulingRule());

            if (performChangeOperation.changeExecuted()) {
                String packageName = type.getPackageFragment().getElementName();
                if (packageName.trim().length() > 0) {
                    packageName += ".";
                }
                String fullyQualifiedJavaInterfaceName = packageName + extractInterfaceProcessor.getTypeName();
                model.setFullyQualifiedJavaInterfaceName(fullyQualifiedJavaInterfaceName);
                model.setMethodMap(CXFModelUtils.getMethodMap(JDTUtils.getType(model.getProjectName(), 
                        fullyQualifiedJavaInterfaceName), model));
                
                undoExtractInterfaceChange = performChangeOperation.getUndoChange();
            }
        } catch (JavaModelException jme) {
            CXFCreationCorePlugin.log(jme.getStatus());
        } catch (InvocationTargetException ite) {
            CXFCreationCorePlugin.log(ite);
        } catch (InterruptedException ie) {
            CXFCreationCorePlugin.log(ie);
        } 
    }

    @Override
    public IStatus undo(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
        IStatus status = Status.OK_STATUS;
        if (undoExtractInterfaceChange != null) {
            try {
            	PerformChangeOperation changeOperation = new PerformChangeOperation(undoExtractInterfaceChange);
            
                WorkbenchRunnableAdapter adapter= new WorkbenchRunnableAdapter(changeOperation);
                PlatformUI.getWorkbench().getProgressService().runInUI(
                        new BusyIndicatorRunnableContext(), adapter, adapter.getSchedulingRule());
                
                if (!startingPointType.getCompilationUnit().isConsistent()) {
                    startingPointType.getCompilationUnit().makeConsistent(monitor);
                }

                model.setMethodMap(CXFModelUtils.getMethodMap(startingPointType, model));

            } catch (JavaModelException jme) {
                status = jme.getStatus();
                CXFCreationCorePlugin.log(status);
            } catch (InvocationTargetException ite) {
                status = new Status(IStatus.ERROR, CXFCreationCorePlugin.PLUGIN_ID, ite.getLocalizedMessage());
                CXFCreationCorePlugin.log(status);
            } catch (InterruptedException ie) {
                status = new Status(IStatus.ERROR, CXFCreationCorePlugin.PLUGIN_ID, ie.getLocalizedMessage());
                CXFCreationCorePlugin.log(status);
            }
        }
        return status;
    }
}
