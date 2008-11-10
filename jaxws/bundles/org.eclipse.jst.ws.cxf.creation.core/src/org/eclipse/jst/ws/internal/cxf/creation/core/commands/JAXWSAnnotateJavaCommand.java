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
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.refactoring.CompilationUnitChange;
import org.eclipse.jdt.internal.ui.actions.WorkbenchRunnableAdapter;
import org.eclipse.jdt.internal.ui.util.BusyIndicatorRunnableContext;
import org.eclipse.jst.ws.internal.cxf.core.model.Java2WSDataModel;
import org.eclipse.jst.ws.internal.cxf.core.utils.AnnotationUtils;
import org.eclipse.jst.ws.internal.cxf.core.utils.JDTUtils;
import org.eclipse.jst.ws.internal.cxf.creation.core.CXFCreationCorePlugin;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.PerformChangeOperation;
import org.eclipse.ltk.ui.refactoring.RefactoringUI;
import org.eclipse.text.edits.MultiTextEdit;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;

/**
 * @author sclarke
 */
@SuppressWarnings("restriction")
public class JAXWSAnnotateJavaCommand extends AbstractDataModelOperation {

    private Stack<Change> interfaceUndoChanges = new Stack<Change>();
    private Stack<Change> classUndoChanges = new Stack<Change>();

    private Java2WSDataModel model;
    private IType javaClassType;
    private IType javaInterfaceType;
    
    public JAXWSAnnotateJavaCommand(Java2WSDataModel model) {
        this.model = model;
    }

    @Override
    public IStatus execute(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
        IStatus status = Status.OK_STATUS;
        try {
            if (model.isUseServiceEndpointInterface()) {
                annotateInterface(monitor);

                if (model.getFullyQualifiedJavaClassName() != null) {
                    annotateSEIClass(monitor);
                }
            } else if (model.getFullyQualifiedJavaClassName() != null) {
                annotateClass(monitor);
            }
        } catch (CoreException ce) {
            status = ce.getStatus();
            CXFCreationCorePlugin.log(status);
        } catch (InvocationTargetException ite) {
            status = new Status(IStatus.ERROR, CXFCreationCorePlugin.PLUGIN_ID, ite.getLocalizedMessage());
            CXFCreationCorePlugin.log(status);
        } catch (InterruptedException ie) {
            status = new Status(IStatus.ERROR, CXFCreationCorePlugin.PLUGIN_ID, ie.getLocalizedMessage());
            CXFCreationCorePlugin.log(status);
        } 
        return status;
    }
    
    private void annotateInterface(IProgressMonitor monitor) throws CoreException, InvocationTargetException,
            InterruptedException {
        javaInterfaceType = JDTUtils.getType(JDTUtils.getJavaProject(model.getProjectName()), model
                .getFullyQualifiedJavaInterfaceName());

        CompilationUnitChange compilationUnitChange = new CompilationUnitChange("Annotating Interface", 
                javaInterfaceType.getCompilationUnit());
        MultiTextEdit multiTextEdit = new MultiTextEdit();
        compilationUnitChange.setEdit(multiTextEdit);

        AnnotationUtils.getWebServiceAnnotationChange(javaInterfaceType, model, compilationUnitChange);

        IMethod[] typeMethods = javaInterfaceType.getMethods();
        for (int i = 0; i < typeMethods.length; i++) {
            IMethod method = typeMethods[i];
            Map<String, Boolean> methodAnnotationMap = model.getMethodMap().get(method);
            if (methodAnnotationMap.get(AnnotationUtils.WEB_METHOD)) {
                AnnotationUtils.getWebMethodAnnotationChange(javaInterfaceType, method, 
                        compilationUnitChange);
            }
            if (methodAnnotationMap.get(AnnotationUtils.REQUEST_WRAPPER)) {
                AnnotationUtils.getRequestWrapperAnnotationChange(javaInterfaceType, method, 
                        compilationUnitChange);
            }
            if (methodAnnotationMap.get(AnnotationUtils.RESPONSE_WRAPPER)) {
                AnnotationUtils.getResponseWrapperAnnotationChange(javaInterfaceType, method, 
                        compilationUnitChange);
            }
            if (methodAnnotationMap.get(AnnotationUtils.WEB_PARAM)) {
                List<SingleVariableDeclaration> parameters = AnnotationUtils.getMethodParameters(
                        javaInterfaceType, method);
                for (SingleVariableDeclaration parameter : parameters) {
                    AnnotationUtils.getWebParamAnnotationChange(javaInterfaceType, method, parameter, 
                            compilationUnitChange);
                }
            } 
        }
        
        AnnotationUtils.getImportsChange(javaInterfaceType.getCompilationUnit(), model, 
                compilationUnitChange, false);
        
        executeChange(monitor, compilationUnitChange, interfaceUndoChanges);
    }
    
    private void annotateClass(IProgressMonitor monitor) throws CoreException, InvocationTargetException,
            InterruptedException {
        javaClassType = JDTUtils.getType(JDTUtils.getJavaProject(model.getProjectName()), model
                .getFullyQualifiedJavaClassName());

        CompilationUnitChange compilationUnitChange = new CompilationUnitChange("Annotating Class", 
                javaClassType.getCompilationUnit());
        MultiTextEdit multiTextEdit = new MultiTextEdit();
        compilationUnitChange.setEdit(multiTextEdit);
        
        AnnotationUtils.getWebServiceAnnotationChange(javaClassType, model, compilationUnitChange);

        IMethod[] typeMethods = javaClassType.getMethods();
        for (int i = 0; i < typeMethods.length; i++) {
            IMethod method = typeMethods[i];
            Map<String, Boolean> methodAnnotationMap = model.getMethodMap().get(method);
            if (methodAnnotationMap.get(AnnotationUtils.WEB_METHOD)) {
                AnnotationUtils.getWebMethodAnnotationChange(javaClassType, method, compilationUnitChange);
            }
            if (methodAnnotationMap.get(AnnotationUtils.REQUEST_WRAPPER)) {
                AnnotationUtils.getRequestWrapperAnnotationChange(javaClassType, method, 
                        compilationUnitChange);
            }
            if (methodAnnotationMap.get(AnnotationUtils.RESPONSE_WRAPPER)) {
                AnnotationUtils.getResponseWrapperAnnotationChange(javaClassType, method, 
                        compilationUnitChange);
            }
            if (methodAnnotationMap.get(AnnotationUtils.WEB_PARAM)) {
                List<SingleVariableDeclaration> parameters = AnnotationUtils.getMethodParameters(
                        javaClassType, method);
                for (SingleVariableDeclaration parameter : parameters) {
                    AnnotationUtils.getWebParamAnnotationChange(javaClassType, method, parameter, 
                            compilationUnitChange);
                }
            } 
        }
        
        AnnotationUtils.getImportsChange(javaClassType.getCompilationUnit(), model, 
                compilationUnitChange, false);
        
        executeChange(monitor, compilationUnitChange, classUndoChanges);
    }
    
    private void annotateSEIClass(IProgressMonitor monitor) throws CoreException, InvocationTargetException,
            InterruptedException {
        javaClassType = JDTUtils.getType(JDTUtils.getJavaProject(model.getProjectName()), model
                .getFullyQualifiedJavaClassName());

        CompilationUnitChange compilationUnitChange = new CompilationUnitChange("Annotation Changes",
                javaClassType.getCompilationUnit());
        MultiTextEdit multiTextEdit = new MultiTextEdit();
        compilationUnitChange.setEdit(multiTextEdit);

        AnnotationUtils.getWebServiceAnnotationChange(javaClassType, model, compilationUnitChange);
        
        AnnotationUtils.getImportsChange(javaClassType.getCompilationUnit(), model, 
                compilationUnitChange, true);

        executeChange(monitor, compilationUnitChange, classUndoChanges);
    }
    
    private void executeChange(IProgressMonitor monitor, Change change, Stack<Change> undoChanges) 
            throws InvocationTargetException, InterruptedException {
        
        if (change == null) {
            return;
        }
        
        change.initializeValidationData(monitor);

        PerformChangeOperation changeOperation = RefactoringUI
                .createUIAwareChangeOperation(change);

        WorkbenchRunnableAdapter adapter = new WorkbenchRunnableAdapter(changeOperation);
        PlatformUI.getWorkbench().getProgressService().runInUI(new BusyIndicatorRunnableContext(), adapter,
                adapter.getSchedulingRule());

        if (undoChanges != null && changeOperation.changeExecuted()) {
            undoChanges.push(changeOperation.getUndoChange());
        }        
    }

    @Override
    public IStatus undo(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
        IStatus status = Status.OK_STATUS;
        try {
            if (javaInterfaceType != null) {
                while (!interfaceUndoChanges.isEmpty()) {
                    Change undoChange = interfaceUndoChanges.pop();
                    if (undoChange != null) {
                        executeChange(monitor, undoChange, null);
                    }
                }
            }
            if (javaClassType != null) {
                while (!classUndoChanges.isEmpty()) {
                    Change undoChange = classUndoChanges.pop();
                    if (undoChange != null) {
                        executeChange(monitor, undoChange, null);
                    }
                }
            }
        } catch (InvocationTargetException ite) {
            status = new Status(IStatus.ERROR, CXFCreationCorePlugin.PLUGIN_ID, ite.getLocalizedMessage());
            CXFCreationCorePlugin.log(status);
        } catch (InterruptedException ie) {
            status = new Status(IStatus.ERROR, CXFCreationCorePlugin.PLUGIN_ID, ie.getLocalizedMessage());
            CXFCreationCorePlugin.log(status);
        }
        return status;
    }
}
