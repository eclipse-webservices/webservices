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
package org.eclipse.jst.ws.internal.jaxws.ui.views;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IAnnotatable;
import org.eclipse.jdt.core.IAnnotation;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.ILocalVariable;
import org.eclipse.jdt.core.IMemberValuePair;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.Signature;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.ArrayInitializer;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MemberValuePair;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.ICellEditorValidator;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jst.ws.annotations.core.AnnotationsCore;
import org.eclipse.jst.ws.annotations.core.AnnotationsCorePlugin;
import org.eclipse.jst.ws.annotations.core.AnnotationsManager;
import org.eclipse.jst.ws.annotations.core.initialization.IAnnotationAttributeInitializer;
import org.eclipse.jst.ws.annotations.core.utils.AnnotationUtils;
import org.eclipse.jst.ws.internal.annotations.core.utils.SignatureUtils;
import org.eclipse.jst.ws.internal.jaxws.ui.JAXWSUIMessages;
import org.eclipse.jst.ws.internal.jaxws.ui.JAXWSUIPlugin;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.IUndoManager;
import org.eclipse.ltk.core.refactoring.RefactoringCore;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.TextFileChange;
import org.eclipse.text.edits.MultiTextEdit;

public class AnnotationsValuesEditingSupport extends EditingSupport {

    private AnnotationsView annotationsView;
    private TreeViewer treeViewer;

    private TextCellEditor textCellEditor;
    private CheckboxCellEditor checkboxCellEditor;
    private ComboBoxCellEditor comboBoxCellEditor;
    private ClassDialogCellEditor classDialogCellEditor;
    private AnnotationArrayCellEditor annotationArrayCellEditor;

    public AnnotationsValuesEditingSupport(AnnotationsView annotationsView, TreeViewer treeViewer) {
        super(treeViewer);
        this.treeViewer = treeViewer;
        this.annotationsView = annotationsView;
        textCellEditor = new TextCellEditor(treeViewer.getTree());
        checkboxCellEditor = new CheckboxCellEditor(treeViewer.getTree());
        comboBoxCellEditor = new ComboBoxCellEditor(treeViewer.getTree(), new String[] {});
        classDialogCellEditor = new ClassDialogCellEditor(treeViewer.getTree());
        annotationArrayCellEditor = new AnnotationArrayCellEditor(treeViewer.getTree(), new Object[] {});
    }

    @Override
    protected boolean canEdit(Object element) {
        if (element instanceof IMethod) {
            IMethod method = (IMethod) element;
            return (Boolean) getValue(method.getDeclaringType());
        }
        return true;
    }

    @Override
    protected CellEditor getCellEditor(Object element) {
        if (element instanceof IType) {
            return checkboxCellEditor;
        }
        if (element instanceof IMethod) {
            try {
                IMethod method = (IMethod) element;

                IType enumType = SignatureUtils.getEnumReturnType(method);
                if (enumType != null) {
                    comboBoxCellEditor.setItems(SignatureUtils.getEnumConstantsNames(enumType));
                    return comboBoxCellEditor;
                }

                final String returnType = method.getReturnType();

                if (SignatureUtils.isBoolean(returnType)) {
                    return checkboxCellEditor;
                }

                if (SignatureUtils.isClass(returnType)) {
                    return classDialogCellEditor;
                }

                if (SignatureUtils.isArray(returnType)) {
                    annotationArrayCellEditor.setMethod(method);
                    return annotationArrayCellEditor;
                }
                if (SignatureUtils.isPrimitive(returnType)) {
                    textCellEditor.setValidator(new ICellEditorValidator() {
                        public String isValid(Object value) {
                            try {
                                if (returnType.charAt(0) == Signature.C_BYTE) {
                                    Byte.parseByte((String) value);
                                }
                                if (returnType.charAt(0) == Signature.C_SHORT) {
                                    Short.parseShort((String) value);
                                }
                                if (returnType.charAt(0) == Signature.C_INT) {
                                    Integer.parseInt((String) value);
                                }
                                if (returnType.charAt(0) == Signature.C_LONG) {
                                    Long.parseLong((String) value);
                                }
                                if (returnType.charAt(0) == Signature.C_FLOAT) {
                                    Float.parseFloat((String) value);
                                }
                                if (returnType.charAt(0) == Signature.C_DOUBLE) {
                                    Double.parseDouble((String) value);
                                }
                            } catch (NumberFormatException nfe) {
                                return JAXWSUIMessages.ANNOTATION_EDITING_SUPPORT_NOT_VALID_MESSAGE_PREFIX + value;
                            }
                            return null;
                        }
                    });
                    return textCellEditor;
                }
                return textCellEditor;
            } catch (JavaModelException jme) {
                JAXWSUIPlugin.log(jme.getStatus());
            }
        }
        return checkboxCellEditor;
    }

    @Override
    protected Object getValue(Object element) {
        if (element instanceof IType) {
            return getValueForClass((IType) element);
        }
        if (element instanceof IMethod) {
            return getValueForMethod((IMethod) element);
        }
        return null;
    }

    private Object getValueForClass(IType type) {
        if (treeViewer.getInput() instanceof IAnnotatable) {
            return getValueForClass(type, (IAnnotatable) treeViewer.getInput());
        }
        return Boolean.FALSE;
    }

    private Object getValueForClass(IType type, IAnnotatable annotatedElement) {
        if (annotatedElement instanceof ILocalVariable) {
            ILocalVariable localVariable = getLocalVariable(annotatedElement);
            if (localVariable != null) {
                annotatedElement = localVariable;
            }
        }
        try {
            IAnnotation[] annotations = annotatedElement.getAnnotations();
            for (IAnnotation annotation : annotations) {
                String annotationName = annotation.getElementName();
                if (AnnotationUtils.isAnnotationPresent((IJavaElement)annotatedElement, annotationName)
                        && (annotationName.equals(type.getElementName())
                                || annotationName.equals(type.getFullyQualifiedName()))) {
                    return Boolean.TRUE;
                }
            }
        } catch (JavaModelException jme) {
            JAXWSUIPlugin.log(jme.getStatus());
        }
        return Boolean.FALSE;
    }

    private Object getValueForMethod(IMethod method) {
        Object value = null;
        try {
            if (treeViewer.getInput() instanceof IAnnotatable) {
                value = getValueForMethod(method, (IAnnotatable) treeViewer.getInput());
            }
        } catch (JavaModelException jme) {
            JAXWSUIPlugin.log(jme.getStatus());
        }
        return value;
    }

    private Object getValueForMethod(IMethod method, IAnnotatable annotatedElement) throws JavaModelException {
        if (annotatedElement instanceof ILocalVariable) {
            ILocalVariable localVariable = getLocalVariable(annotatedElement);
            if (localVariable != null) {
                annotatedElement = localVariable;
            }
        }
        String returnType = method.getReturnType();
        IAnnotation[] annotations = annotatedElement.getAnnotations();
        for (IAnnotation annotation : annotations) {
            IType declaringType = method.getDeclaringType();

            String annotationName = annotation.getElementName();
            if (annotationName.equals(declaringType.getElementName())
                    || annotationName.equals(declaringType.getFullyQualifiedName())) {
                IMemberValuePair[] memberValuePairs = annotation.getMemberValuePairs();
                for (IMemberValuePair memberValuePair : memberValuePairs) {
                    if (memberValuePair.getMemberName().equals(method.getElementName())) {
                        if (SignatureUtils.isString(returnType)) {
                            return memberValuePair.getValue();
                        }

                        IType enumType = SignatureUtils.getEnumReturnType(method);
                        if (enumType != null) {
                            String enumValue = memberValuePair.getValue().toString();
                            String literal = enumValue.substring(enumValue.lastIndexOf(".") + 1); //$NON-NLS-1$
                            String[] enumConstants = SignatureUtils.getEnumConstantsNames(enumType);
                            for (int i = 0; i < enumConstants.length; i++) {
                                if (enumConstants[i].equals(literal)) {
                                    return i;
                                }
                            }
                        }
                        if (SignatureUtils.isClass(returnType)) {
                            return memberValuePair.getValue();
                        }

                        if (SignatureUtils.isBoolean(returnType)) {
                            return memberValuePair.getValue();
                        }

                        if (SignatureUtils.isPrimitive(returnType)) {
                            return ""; //$NON-NLS-1$
                        }
                        if (SignatureUtils.isArray(returnType)) {
                            if (memberValuePair.getValueKind() == IMemberValuePair.K_CLASS) {
                                Object[] arrayValues = (Object[])memberValuePair.getValue();
                                for (int i = 0; i < arrayValues.length; i++) {
                                    String value = arrayValues[i].toString();
                                    arrayValues[i] = value + ".class"; //$NON-NLS-1$
                                }
                                return arrayValues;
                            }
                            return memberValuePair.getValue();
                        }
                    }
                }
                return getDefaultValueForMethod(method);
            }
        }
        return null;
    }

    private Object getDefaultValueForMethod(IMethod method) throws JavaModelException {
        String returnType = method.getReturnType();

        if (SignatureUtils.isString(returnType)) {
            return ""; //$NON-NLS-1$
        }

        if (SignatureUtils.isBoolean(returnType)) {
            return Boolean.FALSE;
        }

        if (SignatureUtils.isEnum(method)) {
            return -1;
        }

        if (SignatureUtils.isPrimitive(returnType)) {
            return ""; //$NON-NLS-1$
        }

        if (SignatureUtils.isArray(returnType)) {
            return new Object[] {};
        }

        return null;
    }

    private ILocalVariable getLocalVariable(IAnnotatable annotatedElement) {
        ILocalVariable localVariable = (ILocalVariable) annotatedElement;
        if (localVariable.getParent() instanceof IMethod) {
            IMethod parent = (IMethod) localVariable.getParent();
            localVariable = AnnotationUtils.getLocalVariable(parent, localVariable.getElementName());
            if (localVariable != null) {
                return localVariable;
            }
        }
        return null;
    }

    @Override
    protected void setValue(Object element, Object value) {
        if (value == null) {
            return;
        }

        try {
            if (element instanceof IType && ((IType) element).isAnnotation()) {
                setValueForClass((IType) element, (Boolean) value);
            }

            if (element instanceof IMethod) {
                setValueForMethod((IMethod) element, value);
            }
        } catch (CoreException ce) {
            JAXWSUIPlugin.log(ce.getStatus());
        }
    }

    private void setValueForClass(IType type, Boolean annotate) throws CoreException {
        Object viewerInput = treeViewer.getInput();

        IAnnotationAttributeInitializer annotationAttributeInitializer =
            AnnotationsManager.getAnnotationDefinitionForClass(type.getFullyQualifiedName()).getAnnotationAttributeInitializer();

        if (viewerInput instanceof IJavaElement) {
            setValueForClass(type, annotate, (IJavaElement) viewerInput, annotationAttributeInitializer);
        }
    }

    private Annotation getAnnotation(AST ast, IType type, List<MemberValuePair> memberValuePairs) throws JavaModelException {
        Annotation annotation =  null;
        int numberOfDeclaredMethods = type.getMethods().length;
        if (numberOfDeclaredMethods == 0) {
            annotation = AnnotationsCore.createMarkerAnnotation(ast, type.getElementName());
        } else if (numberOfDeclaredMethods == 1) {
            Expression value = null;
            if (memberValuePairs != null && memberValuePairs.size() == 1) {
                MemberValuePair memberValuePair = memberValuePairs.get(0);
                if (memberValuePair != null) {
                    value = memberValuePair.getValue();
                }
            }
            if (value != null) {
                annotation = AnnotationsCore.createSingleMemberAnnotation(ast, type.getElementName(), value);
            } else {
                annotation = AnnotationsCore.createNormalAnnotation(ast, type.getElementName(), memberValuePairs);
            }
        } else if (numberOfDeclaredMethods > 1) {
            annotation = AnnotationsCore.createNormalAnnotation(ast, type.getElementName(), memberValuePairs);
        }

        return annotation;
    }

    private void setValueForClass(IType type, Boolean annotate, IJavaElement javaElement,
            IAnnotationAttributeInitializer annotationAttributeInitializer) throws CoreException {
        ICompilationUnit source = AnnotationUtils.getCompilationUnitFromJavaElement(javaElement);
        CompilationUnit compilationUnit = getAST(source);
        AST ast = compilationUnit.getAST();

        List<MemberValuePair> memberValuePairs = getMemberValuePairs(annotationAttributeInitializer, javaElement,
                ast, type);

        Annotation annotation = getAnnotation(ast, type, memberValuePairs);

        TextFileChange change = new TextFileChange("Add/Remove Annotation", (IFile) source.getResource()); //$NON-NLS-1$
        MultiTextEdit multiTextEdit = new MultiTextEdit();
        change.setEdit(multiTextEdit);

        if (annotate) {
            if (javaElement.getElementType() == IJavaElement.PACKAGE_DECLARATION
                    || javaElement.getElementType() == IJavaElement.TYPE
                    || javaElement.getElementType() == IJavaElement.FIELD
                    || javaElement.getElementType() == IJavaElement.METHOD
                    || javaElement.getElementType() == IJavaElement.LOCAL_VARIABLE) {
                change.addEdit(AnnotationUtils.createAddImportTextEdit(javaElement, type.getFullyQualifiedName()));
                change.addEdit(AnnotationUtils.createAddAnnotationTextEdit(javaElement, annotation));
            }
        } else {
            if (javaElement.getElementType() == IJavaElement.PACKAGE_DECLARATION
                    || javaElement.getElementType() == IJavaElement.TYPE
                    || javaElement.getElementType() == IJavaElement.FIELD
                    || javaElement.getElementType() == IJavaElement.METHOD
                    || javaElement.getElementType() == IJavaElement.LOCAL_VARIABLE) {
                change.addEdit(AnnotationUtils.createRemoveImportTextEdit(javaElement, type.getFullyQualifiedName()));
                change.addEdit(AnnotationUtils.createRemoveAnnotationTextEdit(javaElement, annotation));
            }
        }
        executeChange(new NullProgressMonitor(), change);
    }

    private List<MemberValuePair> getMemberValuePairs(IAnnotationAttributeInitializer annotationAttributeInitializer,
            IJavaElement javaElement, AST ast, IType type) {
        if (annotationAttributeInitializer != null) {
            List<MemberValuePair> memberValuePairs = annotationAttributeInitializer.getMemberValuePairs(javaElement, ast, type);
            if (memberValuePairs.size() > 0) {
                return memberValuePairs;
            } else {
                return annotationAttributeInitializer.getMemberValuePairs(javaElement, ast,
                        AnnotationsManager.getAnnotationDefinitionForType(type).getAnnotationClass());
            }
        }
        return Collections.emptyList();
    }

    private void setValueForMethod(IMethod method, Object value) throws CoreException {
        if (value instanceof String) {
            Object currentValue = getValue(method);
            if (currentValue != null && currentValue instanceof String) {
                if (((String) value).equals(currentValue)) {
                    return;
                }
            }
        }
        if (((Boolean) getValue(method.getDeclaringType())).booleanValue()) {
            Object viewerInput = treeViewer.getInput();
            if (viewerInput instanceof IAnnotatable) {
                setValueForMethod(method, value, (IJavaElement) viewerInput);
            }
        }
    }

    private void setValueForMethod(IMethod method, Object value, IJavaElement javaElement) throws CoreException {
        ICompilationUnit source = AnnotationUtils.getCompilationUnitFromJavaElement(javaElement);
        CompilationUnit compilationUnit = getAST(source);
        AST ast = compilationUnit.getAST();

        TextFileChange change = new TextFileChange("Add/Update Annotation Value", (IFile) source.getResource());
        MultiTextEdit multiTextEdit = new MultiTextEdit();
        change.setEdit(multiTextEdit);

        List<Annotation> annotations = AnnotationUtils.getAnnotations(javaElement);
        for (Annotation annotation : annotations) {
            if (annotation instanceof NormalAnnotation) {
                NormalAnnotation normalAnnotation = (NormalAnnotation) annotation;
                IType declaringType = method.getDeclaringType();
                String annotationName = normalAnnotation.getTypeName().getFullyQualifiedName();
                if (annotationName.equals(declaringType.getElementName()) || annotationName.equals(declaringType.getFullyQualifiedName())) {
                    @SuppressWarnings("unchecked")
                    List<MemberValuePair> memberValuePairs = normalAnnotation.values();
                    boolean hasMemberValuePair = false;
                    for (MemberValuePair memberValuePair : memberValuePairs) {
                        if (memberValuePair.getName().getIdentifier().equals(method.getElementName())) {
                            ASTNode memberValue = getMemberValuePairValue(ast, method, value);
                            if (memberValue != null) {
                                change.addEdit(AnnotationUtils.createUpdateMemberValuePairTextEdit(memberValuePair, memberValue));
                                hasMemberValuePair = true;
                                break;
                            }
                        }
                    }
                    if (!hasMemberValuePair) {
                        MemberValuePair memberValuePair = getMemberValuePair(ast, method, value);
                        if (memberValuePair != null) {
                            change.addEdit(AnnotationUtils.createAddMemberValuePairTextEdit(normalAnnotation, memberValuePair));
                            break;
                        }
                    }
                }
            } else if (annotation instanceof SingleMemberAnnotation) {
                SingleMemberAnnotation singleMemberAnnotation = (SingleMemberAnnotation) annotation;
                IType declaringType = method.getDeclaringType();
                String annotationName = singleMemberAnnotation.getTypeName().getFullyQualifiedName();
                if (annotationName.equals(declaringType.getElementName()) || annotationName.equals(declaringType.getFullyQualifiedName())) {
                    MemberValuePair memberValuePair = getMemberValuePair(ast, method, value);
                    if (memberValuePair != null) {
                        change.addEdit(AnnotationUtils.createUpdateSingleMemberAnnotationTextEdit(singleMemberAnnotation, memberValuePair.getValue()));
                        break;
                    }
                }

            }
        }

        executeChange(new NullProgressMonitor(), change);
    }

    private MemberValuePair getMemberValuePair(AST ast, IMethod method, Object value) throws JavaModelException {
        String returnType = method.getReturnType();
        if (SignatureUtils.isString(returnType)) {
            return AnnotationsCore.createStringMemberValuePair(ast, method.getElementName(), (String) value);
        }
        if (SignatureUtils.isBoolean(returnType)) {
            return AnnotationsCore.createBooleanMemberValuePair(ast, method.getElementName(), (Boolean) value);
        }

        if (SignatureUtils.isPrimitive(returnType)) {
            if (returnType.charAt(0) == Signature.C_BYTE
                    || returnType.charAt(0) == Signature.C_SHORT
                    || returnType.charAt(0) == Signature.C_INT
                    || returnType.charAt(0) == Signature.C_LONG
                    || returnType.charAt(0) == Signature.C_FLOAT
                    || returnType.charAt(0) == Signature.C_DOUBLE) {
                return AnnotationsCore.createNumberMemberValuePair(ast, method.getElementName(), value.toString());
            }
        }

        if (SignatureUtils.isArray(returnType)) {
            IType componentType = getComponentType(method);
            if (componentType != null) {
                if (componentType.isAnnotation()) {
                    return createArrayMemberValuePair(ast, method, (Object[]) value);
                } else {
                    return AnnotationsCore.createArrayMemberValuePair(ast, method.getElementName(), (Object[]) value);
                }
            }
        }

        if (SignatureUtils.isClass(returnType)) {
            return AnnotationsCore.createTypeMemberValuePair(ast, method.getElementName(), value.toString());
        }

        IType enumType = SignatureUtils.getEnumReturnType(method);
        if (enumType != null) {
            int selected = ((Integer) value).intValue();
            if (selected != -1) {
                if (enumType.isMember()) {
                    return AnnotationsCore.createEnumMemberValuePair(ast, enumType.getDeclaringType().getFullyQualifiedName(),
                            method.getElementName(), SignatureUtils.getEnumConstants(enumType)[selected]);
                } else {
                    return AnnotationsCore.createEnumMemberValuePair(ast, enumType.getFullyQualifiedName(),
                            method.getElementName(), SignatureUtils.getEnumConstants(enumType)[selected]);
                }
            }
        }
        return null;
    }

    private ASTNode getMemberValuePairValue(AST ast, IMethod method, Object value) throws JavaModelException {
        String returnType = method.getReturnType();
        if (SignatureUtils.isString(returnType)) {
            return AnnotationsCore.createStringLiteral(ast, value.toString());
        }

        if (SignatureUtils.isBoolean(returnType)) {
            return AnnotationsCore.createBooleanLiteral(ast, ((Boolean) value).booleanValue());
        }

        if (SignatureUtils.isPrimitive(returnType)) {
            if (returnType.charAt(0) == Signature.C_BYTE
                    || returnType.charAt(0) == Signature.C_SHORT
                    || returnType.charAt(0) == Signature.C_INT
                    || returnType.charAt(0) == Signature.C_LONG
                    || returnType.charAt(0) == Signature.C_FLOAT
                    || returnType.charAt(0) == Signature.C_DOUBLE) {
                return AnnotationsCore.createNumberLiteral(ast, value.toString());
            }
        }

        if (SignatureUtils.isArray(returnType)) {
            IType componentType = getComponentType(method);
            if (componentType != null) {
                if (componentType.isAnnotation()) {
                    return createArrayValueLiteral(ast, method, (Object[]) value);
                } else {
                    return AnnotationsCore.createArrayValueLiteral(ast, (Object[]) value);
                }
            }
        }

        if (SignatureUtils.isClass(returnType)) {
            return AnnotationsCore.createTypeLiteral(ast, value.toString());
        }


        IType enumType = SignatureUtils.getEnumReturnType(method);
        if (enumType != null) {
            int selected = ((Integer) value).intValue();
            if (selected != -1) {
                if (enumType.isMember()) {
                    return AnnotationsCore.createEnumLiteral(ast, enumType.getDeclaringType().getFullyQualifiedName(),
                            SignatureUtils.getEnumConstants(enumType)[selected]);
                } else {
                    return AnnotationsCore.createEnumLiteral(ast, enumType.getFullyQualifiedName(),
                            SignatureUtils.getEnumConstants(enumType)[selected]);
                }
            }
        }

        return null;
    }

    private void executeChange(IProgressMonitor monitor, Change change) {
        if (change == null) {
            return;
        }

        IUndoManager manager = RefactoringCore.getUndoManager();
        boolean successful = false;
        Change undoChange = null;
        try {
            change.initializeValidationData(monitor);
            RefactoringStatus valid = change.isValid(monitor);
            if (valid.isOK()) {
                manager.aboutToPerformChange(change);
                undoChange = change.perform(monitor);
                successful = true;
            }
        } catch (CoreException ce) {
            JAXWSUIPlugin.log(ce.getStatus());
        } finally {
            manager.changePerformed(change, successful);
        }
        if (undoChange != null) {
            undoChange.initializeValidationData(monitor);
            manager.addUndo(undoChange.getName(), undoChange);
        }
        annotationsView.refreshLabels();
    }

    private MemberValuePair createArrayMemberValuePair(AST ast, IMethod method, Object[] values) throws JavaModelException {
        return AnnotationsCore.createMemberValuePair(ast, method.getElementName(), createArrayValueLiteral(ast,
                method, values));
    }

    private IType getComponentType(IMethod method) throws JavaModelException {
        String returnType = method.getReturnType();
        if (SignatureUtils.isArray(returnType)) {
            String elementType = Signature.getElementType(returnType);
            IType declaringType = method.getDeclaringType();
            IJavaProject javaProject = declaringType.getJavaProject();
            if (javaProject != null) {
                return javaProject.findType(Signature.toString(elementType));
            }
        }
        return null;
    }

    private ArrayInitializer createArrayValueLiteral(AST ast, IMethod method, Object[] values) throws JavaModelException {
        ArrayInitializer arrayInitializer = ast.newArrayInitializer();
        for (Object value : values) {
            if (value instanceof List<?>) {
                IType componentType = getComponentType(method);
                if (componentType != null) {

                    List<MemberValuePair> memberValuePairs = new ArrayList<MemberValuePair>();
                    @SuppressWarnings("unchecked")
                    List<Map<String, Object>> valuesList = (List<Map<String, Object>>) value;
                    Iterator<Map<String, Object>> valuesIterator = valuesList.iterator();
                    while (valuesIterator.hasNext()) {
                        Map<String, Object> annotationMap = valuesIterator.next();
                        Set<Entry<String, Object>> entrySet = annotationMap.entrySet();
                        Iterator<Map.Entry<String, Object>> iterator = entrySet.iterator();
                        while (iterator.hasNext()) {
                            Map.Entry<java.lang.String, Object> entry = iterator.next();
                            String memberName = entry.getKey();
                            try {
                                IMethod annotationMethod = componentType.getMethod(memberName, new String[] {});
                                if (annotationMethod != null) {
                                    Object memberValue = entry.getValue();
                                    String returnType = annotationMethod.getReturnType();
                                    if (SignatureUtils.isString(returnType)) {
                                        memberValuePairs.add(AnnotationsCore.createStringMemberValuePair(ast, memberName,
                                                memberValue.toString()));
                                    }
                                    if (SignatureUtils.isBoolean(returnType)) {
                                        memberValuePairs.add(AnnotationsCore.createBooleanMemberValuePair(ast, memberName,
                                                (Boolean) memberValue));
                                    }
                                    if (SignatureUtils.isClass(returnType)) {
                                        String className = memberValue.toString();
                                        if (className.endsWith(".class")) {
                                            className = className.substring(0, className.lastIndexOf("."));
                                        }
                                        memberValuePairs.add(AnnotationsCore.createMemberValuePair(ast, memberName,
                                                AnnotationsCore.createTypeLiteral(ast, className)));
                                    }
                                    //                                if (returnType.isPrimitive()) {
                                    //                                    memberValuePairs.add(getNumberMemberValuePair(ast, memberName, memberValue));
                                    //                                }
                                }

                            } catch (SecurityException se) {
                                AnnotationsCorePlugin.log(se);
                            }
                        }
                    }
                    arrayInitializer.expressions().add(AnnotationsCore.createNormalAnnotation(ast, componentType.getFullyQualifiedName(),
                            memberValuePairs));
                }
            }
        }
        return arrayInitializer;
    }

    private CompilationUnit getAST(ICompilationUnit source) {
        final ASTParser parser = ASTParser.newParser(AST.JLS3);
        parser.setSource(source);
        return (CompilationUnit) parser.createAST(null);
    }

}
