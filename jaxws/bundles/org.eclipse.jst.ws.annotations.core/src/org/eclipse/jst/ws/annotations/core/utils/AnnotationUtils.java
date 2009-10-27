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
package org.eclipse.jst.ws.annotations.core.utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.filebuffers.FileBuffers;
import org.eclipse.core.filebuffers.ITextFileBufferManager;
import org.eclipse.core.filebuffers.LocationKind;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IAnnotatable;
import org.eclipse.jdt.core.IAnnotation;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.ILocalVariable;
import org.eclipse.jdt.core.IMemberValuePair;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageDeclaration;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.Signature;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.AnnotationTypeDeclaration;
import org.eclipse.jdt.core.dom.AnnotationTypeMemberDeclaration;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.BooleanLiteral;
import org.eclipse.jdt.core.dom.ChildListPropertyDescriptor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.IExtendedModifier;
import org.eclipse.jdt.core.dom.MemberValuePair;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;
import org.eclipse.jdt.ui.CodeStyleConfiguration;
import org.eclipse.jdt.ui.SharedASTProvider;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jst.ws.annotations.core.AnnotationsCorePlugin;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.IUndoManager;
import org.eclipse.ltk.core.refactoring.RefactoringCore;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.TextChange;
import org.eclipse.ltk.core.refactoring.TextFileChange;
import org.eclipse.text.edits.MultiTextEdit;
import org.eclipse.text.edits.TextEdit;

import com.sun.mirror.declaration.AnnotationMirror;
import com.sun.mirror.declaration.AnnotationTypeElementDeclaration;
import com.sun.mirror.declaration.AnnotationValue;
import com.sun.mirror.declaration.Declaration;
import com.sun.mirror.declaration.ParameterDeclaration;

/**
 * Utility class for adding, removing and updating annotations and member value pairs.
 * <p>
 * <strong>Provisional API:</strong> This class/interface is part of an interim API that is still under
 * development and expected to change significantly before reaching stability. It is being made available at
 * this early stage to solicit feedback from pioneering adopters on the understanding that any code that uses
 * this API will almost certainly be broken (repeatedly) as the API evolves.
 * </p>
 */
public final class AnnotationUtils {
    private static final String FORMATTER_INSERT_SPACE_BEFORE_ASSIGNMENT_OPERATOR
        = "org.eclipse.jdt.core.formatter.insert_space_before_assignment_operator"; //$NON-NLS-1$
    private static final String FORMATTER_INSERT_SPACE_AFTER_ASSIGNMENT_OPERATOR
        = "org.eclipse.jdt.core.formatter.insert_space_after_assignment_operator"; //$NON-NLS-1$
    private static final String DO_NOT_INSERT = "do not insert"; //$NON-NLS-1$


    private AnnotationUtils() {
    }

    public static TextEdit createAddImportTextEdit(IJavaElement javaElement,
            Class<? extends java.lang.annotation.Annotation> annotation) throws CoreException {
        CompilationUnit compilationUnit = SharedASTProvider.getAST(getCompilationUnitFromJavaElement(javaElement), SharedASTProvider.WAIT_YES, null);
        String qualifiedName = annotation.getCanonicalName();
        ImportRewrite importRewrite = CodeStyleConfiguration.createImportRewrite(compilationUnit, true);
        importRewrite.addImport(qualifiedName);
        return importRewrite.rewriteImports(null);
    }

    @SuppressWarnings("unchecked")
    public static TextEdit createRemoveImportTextEdit(IJavaElement javaElement,
            Class<? extends java.lang.annotation.Annotation> annotation) throws CoreException {
        CompilationUnit compilationUnit = SharedASTProvider.getAST(getCompilationUnitFromJavaElement(javaElement), SharedASTProvider.WAIT_YES, null);
        String qualifiedName = annotation.getCanonicalName();
        ImportRewrite importRewrite = CodeStyleConfiguration.createImportRewrite(compilationUnit, true);
        final String annotationSimpleName = annotation.getSimpleName();
        final List<String> occurences = new ArrayList<String>();
        Target target = annotation.getAnnotation(Target.class);
        List<ElementType> elementTypes = Arrays.asList(target.value());
        for (ElementType elementType : elementTypes) {
            if (elementType == ElementType.PACKAGE) {
                compilationUnit.accept(new ASTVisitor() {
                    @Override
                    public boolean visit(PackageDeclaration packageDeclaration) {
                        countAnnotationOccurrences(packageDeclaration.annotations(),
                                annotationSimpleName, occurences);
                        return false;
                    }
                });
            }
            if (elementType == ElementType.TYPE) {
                compilationUnit.accept(new ASTVisitor() {
                    @Override
                    public boolean visit(TypeDeclaration typeDeclaration) {
                        countAnnotationOccurrences(typeDeclaration.modifiers(),
                                annotationSimpleName, occurences);
                        return false;
                    }
                });
            }
            if (elementType == ElementType.FIELD) {
                compilationUnit.accept(new ASTVisitor() {
                    @Override
                    public boolean visit(FieldDeclaration fieldDeclaration) {
                        countAnnotationOccurrences(
                                fieldDeclaration.modifiers(),
                                annotationSimpleName, occurences);
                        return false;
                    }
                });
            }
            if (elementType == ElementType.METHOD) {
                compilationUnit.accept(new ASTVisitor() {
                    @Override
                    public boolean visit(MethodDeclaration methodDeclaration) {
                        countAnnotationOccurrences(methodDeclaration
                                .modifiers(), annotationSimpleName, occurences);
                        return false;
                    }
                });
            }
            if (elementType == ElementType.PARAMETER) {
                compilationUnit.accept(new ASTVisitor() {
                    @Override
                    public boolean visit(
                            SingleVariableDeclaration singleVariableDeclaration) {
                        countAnnotationOccurrences(singleVariableDeclaration
                                .modifiers(), annotationSimpleName, occurences);
                        return false;
                    }
                });
            }
        }
        if (occurences.size() == 1) {
            importRewrite.removeImport(qualifiedName);
        }
        // TODO Cleanup imports. Repeatedly adding and removing an import
        // where none existed before will
        // insert a new line on each insert.
        return importRewrite.rewriteImports(null);
    }

    private static void countAnnotationOccurrences(List<IExtendedModifier> modifiers, String annotationSimpleName,
            List<String> occurences) {
        for (IExtendedModifier extendedModifier : modifiers) {
            if (extendedModifier instanceof Annotation) {
                Annotation existingAnnotation = (Annotation) extendedModifier;
                if (AnnotationUtils.getAnnotationName(existingAnnotation).equals(annotationSimpleName)) {
                    occurences.add(annotationSimpleName);
                }
            }
        }
    }

    public static TextEdit createAddAnnotationTextEdit(IJavaElement javaElement, Annotation annotation) throws CoreException {
        switch(javaElement.getElementType()) {
        case IJavaElement.PACKAGE_DECLARATION:
            return createAddAnnotationTextEdit((IPackageDeclaration) javaElement, annotation);
        case IJavaElement.TYPE:
            return createAddAnnotationTextEdit((IType) javaElement, annotation);
        case IJavaElement.FIELD:
            return createAddAnnotationTextEdit((IField) javaElement, annotation);
        case IJavaElement.METHOD:
            return createAddAnnotationTextEdit((IMethod) javaElement, annotation);
        case IJavaElement.LOCAL_VARIABLE:
            return createAddAnnotationTextEdit((ILocalVariable) javaElement, annotation);
        default:
            return new MultiTextEdit();
        }
    }

    public static TextEdit createRemoveAnnotationTextEdit(IJavaElement javaElement, Annotation annotation) throws CoreException {
        switch(javaElement.getElementType()) {
        case IJavaElement.PACKAGE_DECLARATION:
            return createRemoveAnnotationTextEdit((IPackageDeclaration) javaElement, annotation);
        case IJavaElement.TYPE:
            return createRemoveAnnotationTextEdit((IType) javaElement, annotation);
        case IJavaElement.FIELD:
            return createRemoveAnnotationTextEdit((IField) javaElement, annotation);
        case IJavaElement.METHOD:
            return createRemoveAnnotationTextEdit((IMethod) javaElement, annotation);
        case IJavaElement.LOCAL_VARIABLE:
            return createRemoveAnnotationTextEdit((ILocalVariable) javaElement, annotation);
        default:
            return new MultiTextEdit();
        }
    }

    public static void addAnnotation(IJavaElement javaElement, Annotation annotation) throws CoreException {
        TextChange change = AnnotationUtils.createTextFileChange("Add annotation", (IFile) javaElement.getResource());
        TextEdit annotationEdit = createAddAnnotationTextEdit(javaElement, annotation);
        change.addEdit(annotationEdit);
        applyChange(null, change);
    }

    public static void removeAnnotation(IJavaElement javaElement, Annotation annotation) throws CoreException {
        TextEdit textEdit = createRemoveAnnotationTextEdit(javaElement, annotation);
        TextChange change = AnnotationUtils.createTextFileChange("Add annotation", (IFile) javaElement.getResource());
        change.addEdit(textEdit);
        applyChange(null, change);
    }

    private static void applyChange(IProgressMonitor monitor, Change change) {
        if (change == null) {
            return;
        }

        if (monitor == null) {
            monitor = new NullProgressMonitor();
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
            AnnotationsCorePlugin.log(ce.getStatus());
        } finally {
            manager.changePerformed(change, successful);
        }
        if (undoChange != null) {
            undoChange.initializeValidationData(monitor);
            manager.addUndo(undoChange.getName(), undoChange);
        }
    }

    private static IDocument getDocument(ICompilationUnit source) throws CoreException {
        ITextFileBufferManager bufferManager = FileBuffers.getTextFileBufferManager();
        IProgressMonitor monitor = new NullProgressMonitor();

        IPath path = source.getResource().getFullPath();
        boolean connected = false;
        try {
            bufferManager.connect(path, LocationKind.IFILE, monitor);
            connected = true;
            IDocument document = bufferManager.getTextFileBuffer(path, LocationKind.IFILE).getDocument();
            return document;
        } finally {
            if (connected) {
                bufferManager.disconnect(path, LocationKind.IFILE, monitor);
            }
        }
    }

    private static TextEdit createAddAnnotationTextEdit(IPackageDeclaration packageDeclaration, Annotation annotation) throws CoreException {
        if (packageDeclaration != null && !isAnnotationPresent(packageDeclaration, AnnotationUtils.getAnnotationName(annotation))) {
            ICompilationUnit source = getCompilationUnitFromJavaElement(packageDeclaration);
            CompilationUnit compilationUnit = SharedASTProvider.getAST(source, SharedASTProvider.WAIT_YES, null);
            ASTRewrite rewriter = ASTRewrite.create(compilationUnit.getAST());

            ListRewrite listRewrite = rewriter.getListRewrite(compilationUnit.getPackage(), PackageDeclaration.ANNOTATIONS_PROPERTY);

            listRewrite.insertFirst(annotation, null);

            return rewriter.rewriteAST();
        }
        return new MultiTextEdit();
    }

    private static TextEdit createRemoveAnnotationTextEdit(IPackageDeclaration packageDeclaration, Annotation annotation) throws CoreException {
        if (packageDeclaration != null && isAnnotationPresent(packageDeclaration, getAnnotationName(annotation))) {
            ICompilationUnit source = getCompilationUnitFromJavaElement(packageDeclaration);
            CompilationUnit compilationUnit = SharedASTProvider.getAST(source, SharedASTProvider.WAIT_YES, null);
            ASTRewrite rewriter = ASTRewrite.create(compilationUnit.getAST());

            PackageDeclaration pkgDeclaration = compilationUnit.getPackage();

            ListRewrite listRewrite = rewriter.getListRewrite(pkgDeclaration, PackageDeclaration.ANNOTATIONS_PROPERTY);

            @SuppressWarnings("unchecked")
            List originalList = listRewrite.getOriginalList();
            for (Object object : originalList) {
                if (object instanceof Annotation && compareAnnotationNames((Annotation) object, annotation)) {
                    listRewrite.remove((Annotation) object, null);
                }
            }

            return rewriter.rewriteAST();
        }
        return new MultiTextEdit();
    }

    private static TextEdit createAddAnnotationTextEdit(IType type, Annotation annotation) throws CoreException {
        ICompilationUnit source = type.getCompilationUnit();
        CompilationUnit compilationUnit = SharedASTProvider.getAST(source, SharedASTProvider.WAIT_YES, null);

        AbstractTypeDeclaration typeDeclaration = getTypeDeclaration(compilationUnit, type);
        if(typeDeclaration != null && !isAnnotationPresent(typeDeclaration, annotation)) {
            ASTRewrite rewriter = ASTRewrite.create(compilationUnit.getAST());

            ListRewrite listRewrite = rewriter.getListRewrite(typeDeclaration,
                    getChildListPropertyDescriptorForType(typeDeclaration));

            listRewrite.insertFirst(annotation, null);

            return rewriter.rewriteAST();
        }
        return new MultiTextEdit();
    }

    private static TextEdit createRemoveAnnotationTextEdit(IType type, Annotation annotation) throws CoreException {
        ICompilationUnit source = type.getCompilationUnit();
        CompilationUnit compilationUnit = SharedASTProvider.getAST(source, SharedASTProvider.WAIT_YES, null);

        AbstractTypeDeclaration typeDeclaration = getTypeDeclaration(compilationUnit, type);
        if (typeDeclaration != null && isAnnotationPresent(typeDeclaration, annotation)) {
            ASTRewrite rewriter = ASTRewrite.create(compilationUnit.getAST());

            ListRewrite listRewrite = rewriter.getListRewrite(typeDeclaration,
                    getChildListPropertyDescriptorForType(typeDeclaration));

            @SuppressWarnings("unchecked")
            List originalList = listRewrite.getOriginalList();
            for (Object object : originalList) {
                if (object instanceof Annotation && compareAnnotationNames((Annotation)object, annotation)) {
                    listRewrite.remove((Annotation)object, null);
                }
            }

            return rewriter.rewriteAST();
        }
        return new MultiTextEdit();
    }

    private static TextEdit createAddAnnotationTextEdit(IMethod method, Annotation annotation) throws CoreException {
        ICompilationUnit source = method.getCompilationUnit();
        CompilationUnit compilationUnit = SharedASTProvider.getAST(source, SharedASTProvider.WAIT_YES, null);

        BodyDeclaration bodyDeclaration = getMethodDeclaration(compilationUnit, method);
        if (bodyDeclaration != null && !isAnnotationPresent(bodyDeclaration, annotation)) {
            ASTRewrite rewriter = ASTRewrite.create(compilationUnit.getAST());

            ListRewrite listRewrite = rewriter.getListRewrite(bodyDeclaration,
                    getChildListPropertyDescriptorForMethod(bodyDeclaration));
            listRewrite.insertAt(annotation, 0, null);

            return rewriter.rewriteAST();
        }
        return new MultiTextEdit();
    }

    private static TextEdit createRemoveAnnotationTextEdit(IMethod method, Annotation annotation) throws CoreException {
        ICompilationUnit source = method.getCompilationUnit();
        CompilationUnit compilationUnit = SharedASTProvider.getAST(source, SharedASTProvider.WAIT_YES, null);

        BodyDeclaration bodyDeclaration = getMethodDeclaration(compilationUnit, method);
        if (bodyDeclaration != null && isAnnotationPresent(bodyDeclaration, annotation)) {
            ASTRewrite rewriter = ASTRewrite.create(compilationUnit.getAST());

            ListRewrite listRewrite = rewriter.getListRewrite(bodyDeclaration,
                    getChildListPropertyDescriptorForMethod(bodyDeclaration));

            @SuppressWarnings("unchecked")
            List originalList = listRewrite.getOriginalList();
            for (Object object : originalList) {
                if (object instanceof Annotation && compareAnnotationNames((Annotation) object, annotation)) {
                    listRewrite.remove((Annotation) object, null);
                }
            }
            return rewriter.rewriteAST();
        }

        return new MultiTextEdit();
    }

    private static TextEdit createAddAnnotationTextEdit(IField field, Annotation annotation) throws CoreException {
        ICompilationUnit source = field.getCompilationUnit();
        CompilationUnit compilationUnit = SharedASTProvider.getAST(source, SharedASTProvider.WAIT_YES, null);

        FieldDeclaration fieldDeclaration = getFieldDeclaration(compilationUnit, field);
        if (fieldDeclaration != null && !isAnnotationPresent(fieldDeclaration, annotation)) {

            ASTRewrite rewriter = ASTRewrite.create(compilationUnit.getAST());

            ListRewrite listRewrite = rewriter.getListRewrite(fieldDeclaration, FieldDeclaration.MODIFIERS2_PROPERTY);
            listRewrite.insertAt(annotation, 0, null);

            return rewriter.rewriteAST();
        }

        return new MultiTextEdit();
    }

    private static TextEdit createRemoveAnnotationTextEdit(IField field, Annotation annotation) throws CoreException {
        ICompilationUnit source = field.getCompilationUnit();
        CompilationUnit compilationUnit = SharedASTProvider.getAST(source, SharedASTProvider.WAIT_YES, null);

        FieldDeclaration fieldDeclaration = getFieldDeclaration(compilationUnit, field);

        if (fieldDeclaration != null && isAnnotationPresent(fieldDeclaration, annotation)) {

            ASTRewrite rewriter = ASTRewrite.create(compilationUnit.getAST());

            ListRewrite listRewrite = rewriter.getListRewrite(fieldDeclaration, FieldDeclaration.MODIFIERS2_PROPERTY);

            @SuppressWarnings("unchecked")
            List originalList = listRewrite.getOriginalList();
            for (Object object : originalList) {
                if (object instanceof Annotation && compareAnnotationNames((Annotation) object, annotation)) {
                    listRewrite.remove((Annotation) object, null);
                }
            }

            return rewriter.rewriteAST();
        }

        return new MultiTextEdit();
    }

    private static TextEdit createAddAnnotationTextEdit(ILocalVariable methodParameter, Annotation annotation) throws CoreException {
        SingleVariableDeclaration parameter = getSingleVariableDeclaration(methodParameter);
        if (parameter != null && !isAnnotationPresent(methodParameter, AnnotationUtils.getAnnotationName(annotation))) {
            ASTRewrite rewriter = ASTRewrite.create(parameter.getAST());

            ListRewrite listRewrite = rewriter.getListRewrite(parameter, SingleVariableDeclaration.MODIFIERS2_PROPERTY);

            listRewrite.insertAt(annotation, -1, null);

            return rewriter.rewriteAST();
        }
        return new MultiTextEdit();
    }

    private static TextEdit createRemoveAnnotationTextEdit(ILocalVariable methodParameter, Annotation annotation) throws CoreException {
        SingleVariableDeclaration parameter = getSingleVariableDeclaration(methodParameter);
        if (isAnnotationPresent(methodParameter, AnnotationUtils.getAnnotationName(annotation))) {
            ASTRewrite rewriter = ASTRewrite.create(parameter.getAST());

            ListRewrite listRewrite = rewriter.getListRewrite(parameter, SingleVariableDeclaration.MODIFIERS2_PROPERTY);

            @SuppressWarnings("unchecked")
            List originalList = listRewrite.getOriginalList();
            for (Object object : originalList) {
                if (object instanceof Annotation && compareAnnotationNames((Annotation)object, annotation)) {
                    listRewrite.remove((Annotation)object, null);
                }
            }

            return rewriter.rewriteAST();
        }
        return new MultiTextEdit();
    }

    public static TextEdit createAddMemberValuePairTextEdit(NormalAnnotation annotation, MemberValuePair memberValuePair) throws CoreException {
        ASTRewrite rewriter = ASTRewrite.create(annotation.getAST());

        ListRewrite listRewrite = rewriter.getListRewrite(annotation, NormalAnnotation.VALUES_PROPERTY);

        listRewrite.insertLast(memberValuePair, null);

        if (annotation.getRoot() instanceof CompilationUnit) {
            CompilationUnit compilationUnit = (CompilationUnit) annotation.getRoot();
            ICompilationUnit source = AnnotationUtils.getCompilationUnitFromJavaElement(compilationUnit.getJavaElement());
            IDocument document = getDocument(source);
            return rewriter.rewriteAST(document, getOptions(source));
        } else {
            return rewriter.rewriteAST();
        }
    }

    public static TextEdit createUpdateMemberValuePairTextEdit(MemberValuePair memberValuePair, ASTNode value) throws CoreException {
        ASTRewrite rewriter = ASTRewrite.create(memberValuePair.getAST());

        rewriter.set(memberValuePair, MemberValuePair.VALUE_PROPERTY, value, null);

        return rewriter.rewriteAST();
    }

    public static TextEdit createUpdateSingleMemberAnnotationTextEdit(SingleMemberAnnotation annotation, ASTNode value) throws CoreException {
        ASTRewrite rewriter = ASTRewrite.create(annotation.getAST());

        rewriter.set(annotation, SingleMemberAnnotation.VALUE_PROPERTY, value, null);

        return rewriter.rewriteAST();
    }

    public static ICompilationUnit getCompilationUnitFromJavaElement(IJavaElement javaElement) {
        switch(javaElement.getElementType()) {
        case IJavaElement.COMPILATION_UNIT:
            return (ICompilationUnit) javaElement;
        case IJavaElement.PACKAGE_DECLARATION:
            IPackageDeclaration packageDeclaration = (IPackageDeclaration) javaElement;
            return (ICompilationUnit) packageDeclaration.getParent();
        case IJavaElement.TYPE:
            IType type = (IType) javaElement;
            return type.getCompilationUnit();
        case IJavaElement.METHOD:
            IMethod method = (IMethod) javaElement;
            return method.getCompilationUnit();
        case IJavaElement.FIELD:
            IField field = (IField) javaElement;
            return field.getCompilationUnit();
        case IJavaElement.LOCAL_VARIABLE:
            ILocalVariable localVariable = (ILocalVariable) javaElement;
            if (localVariable.getParent() instanceof IMethod) {
                return getCompilationUnitFromJavaElement(localVariable.getParent());
            }
        default:
            return JavaCore.createCompilationUnitFrom((IFile) javaElement.getResource());
        }
    }

    @SuppressWarnings("unchecked")
    private static AbstractTypeDeclaration getTypeDeclaration(CompilationUnit compilationUnit, IType type) {
        List<TypeDeclaration> types = compilationUnit.types();
        for (AbstractTypeDeclaration abstractTypeDeclaration : types) {
            if (compareTypeNames(abstractTypeDeclaration, type)) {
                return abstractTypeDeclaration;
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private static BodyDeclaration getMethodDeclaration(CompilationUnit compilationUnit, IMethod method) {
        AbstractTypeDeclaration typeDeclaration = getTypeDeclaration(compilationUnit, method.getDeclaringType());
        if (typeDeclaration != null) {
            List<BodyDeclaration> bodyDeclarations = typeDeclaration.bodyDeclarations();
            for (BodyDeclaration bodyDeclaration : bodyDeclarations) {
                if (bodyDeclaration instanceof MethodDeclaration) {
                    if (compareMethods((MethodDeclaration) bodyDeclaration, method)) {
                        return bodyDeclaration;
                    }
                }
                if (bodyDeclaration instanceof AnnotationTypeMemberDeclaration) {
                    if (compareMethodNames((AnnotationTypeMemberDeclaration)bodyDeclaration, method)) {
                        return bodyDeclaration;
                    }
                }
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private static FieldDeclaration getFieldDeclaration(CompilationUnit compilationUnit, IField field) {
        AbstractTypeDeclaration typeDeclaration = getTypeDeclaration(compilationUnit, field.getDeclaringType());
        if (typeDeclaration != null) {
            List<BodyDeclaration> bodyDeclarations = typeDeclaration.bodyDeclarations();
            for (BodyDeclaration bodyDeclaration : bodyDeclarations) {
                if (bodyDeclaration instanceof FieldDeclaration) {
                    FieldDeclaration fieldDeclaration = (FieldDeclaration) bodyDeclaration;
                    if (compareFieldNames(fieldDeclaration, field)) {
                        return fieldDeclaration;
                    }
                }
            }
        }
        return null;
    }

    private static BodyDeclaration getFieldDeclaration(List<BodyDeclaration> bodyDeclarations, IField field) {
        for (BodyDeclaration bodyDeclaration : bodyDeclarations) {
            if (bodyDeclaration instanceof FieldDeclaration) {
                if (compareFieldNames((FieldDeclaration)bodyDeclaration, field)) {
                    return bodyDeclaration;
                }
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private static Map getOptions(ICompilationUnit source) {
        Map options = source.getJavaProject().getOptions(true);
        options.put(FORMATTER_INSERT_SPACE_BEFORE_ASSIGNMENT_OPERATOR, DO_NOT_INSERT);
        options.put(FORMATTER_INSERT_SPACE_AFTER_ASSIGNMENT_OPERATOR, DO_NOT_INSERT);
        return options;
    }

    private static ChildListPropertyDescriptor getChildListPropertyDescriptorForType(AbstractTypeDeclaration
            abstractTypeDeclaration) {
        ChildListPropertyDescriptor childListPropertyDescriptor = null;
        if (abstractTypeDeclaration instanceof TypeDeclaration) {
            childListPropertyDescriptor = TypeDeclaration.MODIFIERS2_PROPERTY;
        }
        if (abstractTypeDeclaration instanceof EnumDeclaration) {
            childListPropertyDescriptor = EnumDeclaration.MODIFIERS2_PROPERTY;
        }
        if (abstractTypeDeclaration instanceof AnnotationTypeDeclaration) {
            childListPropertyDescriptor = AnnotationTypeDeclaration.MODIFIERS2_PROPERTY;
        }
        return childListPropertyDescriptor;
    }

    private static ChildListPropertyDescriptor getChildListPropertyDescriptorForMethod(
            BodyDeclaration bodyDeclaration) {
        if (bodyDeclaration instanceof MethodDeclaration) {
            return MethodDeclaration.MODIFIERS2_PROPERTY;
        }
        if (bodyDeclaration instanceof AnnotationTypeMemberDeclaration) {
            return AnnotationTypeMemberDeclaration.MODIFIERS2_PROPERTY;
        }
        return null;
    }

    public static TextFileChange createTextFileChange(String textFileChangeName, IFile file) {
        TextFileChange textFileChange = new TextFileChange(textFileChangeName, file);
        MultiTextEdit multiTextEdit = new MultiTextEdit();
        textFileChange.setEdit(multiTextEdit);
        return textFileChange;
    }

    public static String getAnnotationName(Annotation annotation) {
        Name annotationTypeName = annotation.getTypeName();
        return annotationTypeName.getFullyQualifiedName();
    }

    public static boolean compareTypeNames(AbstractTypeDeclaration abstractTypeDeclaration, IType type) {
        return abstractTypeDeclaration.getName().getIdentifier().equals(type.getElementName());
    }

    @SuppressWarnings("unchecked")
    public static boolean compareMethods(MethodDeclaration methodDeclaration, IMethod method) {
        if (methodDeclaration.getName().getIdentifier().equals(method.getElementName())) {
            String[] parametetTypes = method.getParameterTypes();
            List<SingleVariableDeclaration> methodDeclarationParameters = methodDeclaration.parameters();
            if (parametetTypes.length == methodDeclarationParameters.size()) {
                for (int i = 0; i < parametetTypes.length; i++) {
                    String simpleName1 = Signature.toString(parametetTypes[i]);
                    String simpleName2 = methodDeclarationParameters.get(i).getType().toString();
                    if (!simpleName1.equals(simpleName2)) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    public static boolean compareMethods(MethodDeclaration methodOne, MethodDeclaration methodTwo) {
        if (methodOne.getName().getIdentifier().equals(methodTwo.getName().getIdentifier())) {
            List<SingleVariableDeclaration> methodParametersOne = methodOne.parameters();
            List<SingleVariableDeclaration> methodParametersTwo = methodTwo.parameters();
            if (methodParametersOne.size() == methodParametersTwo.size()) {
                for (int i = 0; i < methodParametersOne.size(); i++) {
                    String simpleName1 = methodParametersOne.get(i).getType().toString();
                    String simpleName2 = methodParametersTwo.get(i).getType().toString();
                    if (!simpleName1.equals(simpleName2)) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    public static boolean compareMethods(com.sun.mirror.declaration.MethodDeclaration methodOne,
            com.sun.mirror.declaration.MethodDeclaration methodTwo) {
        return compareMethodNames(methodOne, methodTwo)
        && compareMethodParameterTypes(methodOne, methodTwo);
    }

    private static boolean compareMethodNames(com.sun.mirror.declaration.MethodDeclaration methodOne,
            com.sun.mirror.declaration.MethodDeclaration methodTwo) {
        return methodOne.getSimpleName().equals(methodTwo.getSimpleName());
    }

    private static boolean compareMethodParameterTypes(com.sun.mirror.declaration.MethodDeclaration methodOne,
            com.sun.mirror.declaration.MethodDeclaration methodTwo) {
        int numberOfParametersOne = methodOne.getParameters().size();
        int numberOfParametersTwo = methodTwo.getParameters().size();

        if (numberOfParametersOne == numberOfParametersTwo) {
            List<ParameterDeclaration> parametersOne = (List<ParameterDeclaration>) methodOne.getParameters();
            List<ParameterDeclaration> parametersTwo = (List<ParameterDeclaration>) methodTwo.getParameters();
            for (int i = 0; i < parametersOne.size(); i++) {
                if (!parametersOne.get(i).getType().equals(parametersTwo.get(i).getType())) {
                    return false;
                }
            }
            return true;

        }
        return false;
    }

    private static boolean compareMethodNames(AnnotationTypeMemberDeclaration memmberDeclaration, IMethod method) {
        return memmberDeclaration.getName().getIdentifier().equals(method.getElementName());
    }

    @SuppressWarnings("unchecked")
    private static boolean compareFieldNames(FieldDeclaration fieldDeclaration, IField field) {
        List<VariableDeclarationFragment> fragments = fieldDeclaration.fragments();
        for (VariableDeclarationFragment variableDeclarationFragment : fragments) {
            if (variableDeclarationFragment.getName().getIdentifier().equals(field.getElementName())) {
                return true;
            }
        }
        return false;
    }

    public static String accountForOverloadedMethods(IType type, IMethod method) throws JavaModelException {
        List<IMethod> methods =  Arrays.asList(type.getMethods());
        List<IMethod> similarMethods = new ArrayList<IMethod>();
        for (IMethod methodToTest : methods) {
            if (!method.equals(methodToTest) && method.getElementName().equalsIgnoreCase(
                    methodToTest.getElementName()) && methodToTest.getSourceRange().getOffset() <
                    method.getSourceRange().getOffset()) {
                similarMethods.add(methodToTest);
            }
        }
        return similarMethods.size() > 0 ? Integer.toString(similarMethods.size()) : ""; //$NON-NLS-1$
    }

    private static boolean compareAnnotationNames(Annotation newAnnotation, Annotation existingAnnotation) {
        return AnnotationUtils.getAnnotationName(existingAnnotation).equals(
                AnnotationUtils.getAnnotationName(newAnnotation));
    }

    public static boolean isAnnotationPresent(IJavaElement javaElement, Annotation annotation) {
        return AnnotationUtils.isAnnotationPresent(javaElement, AnnotationUtils.getAnnotationName(annotation));
    }

    public static boolean isAnnotationPresent(IJavaElement javaElement, String annotationName) {
        if (javaElement.getElementType() == IJavaElement.COMPILATION_UNIT) {
            return isAnnotationPresent(((ICompilationUnit)javaElement).findPrimaryType(), annotationName);
        }

        ICompilationUnit source = getCompilationUnitFromJavaElement(javaElement);

        int elementType = javaElement.getElementType();
        CompilationUnit compilationUnit = SharedASTProvider.getAST(source, SharedASTProvider.WAIT_YES, null);

        if (elementType == IJavaElement.PACKAGE_DECLARATION
                || elementType == IJavaElement.TYPE
                || elementType == IJavaElement.METHOD
                || elementType == IJavaElement.LOCAL_VARIABLE
                || elementType == IJavaElement.FIELD) {
            return isAnnotationPresent(compilationUnit, javaElement,
                    annotationName);
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    private static boolean isAnnotationPresent(BodyDeclaration bodyDeclaration, Annotation annatotationToAdd) {
        boolean exists = false;
        List<IExtendedModifier> modifiers = bodyDeclaration.modifiers();
        for (IExtendedModifier extendedModifier : modifiers) {
            if (extendedModifier instanceof Annotation) {
                Annotation existingAnnotation = (Annotation) extendedModifier;
                if (compareAnnotationNames(annatotationToAdd, existingAnnotation)) {
                    return true;
                }
            }
        }
        return exists;
    }

    private static boolean isAnnotationPresent(CompilationUnit compilationUnit, IJavaElement javaElement,
            String annotationName) {
        List<IExtendedModifier> modifiers = getExtendedModifiers(compilationUnit, javaElement);
        for (IExtendedModifier extendedModifier : modifiers) {
            if (extendedModifier.isAnnotation()) {
                Annotation existingAnnotation = (Annotation) extendedModifier;
                if (AnnotationUtils.getAnnotationName(existingAnnotation).equals(annotationName)) {
                    return true;
                }
            }
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    public static List<IExtendedModifier> getExtendedModifiers(CompilationUnit compilationUnit,
            IJavaElement javaElement) {

        if (javaElement.getElementType() == IJavaElement.PACKAGE_DECLARATION) {
            PackageDeclaration packageDeclaration = compilationUnit.getPackage();
            return packageDeclaration.annotations();
        }

        IType type = compilationUnit.getTypeRoot().findPrimaryType();
        List<AbstractTypeDeclaration> types = compilationUnit.types();
        for (AbstractTypeDeclaration abstractTypeDeclaration : types) {
            if (compareTypeNames(abstractTypeDeclaration, type)) {
                List<BodyDeclaration> bodyDeclarations = getBodyDeclarationsForType(abstractTypeDeclaration);

                BodyDeclaration bodyDeclaration = null;
                if (javaElement.getElementType() == IJavaElement.TYPE) {
                    bodyDeclaration = abstractTypeDeclaration;
                }

                if (javaElement.getElementType() == IJavaElement.METHOD) {
                    bodyDeclaration = getMethodDeclaration(bodyDeclarations, (IMethod) javaElement);
                }

                if (javaElement.getElementType() == IJavaElement.LOCAL_VARIABLE) {
                    SingleVariableDeclaration singleVariableDeclaration = getSingleVariableDeclaration((ILocalVariable) javaElement);
                    if (singleVariableDeclaration != null) {
                        return singleVariableDeclaration.modifiers();
                    }
                }

                if (javaElement.getElementType() == IJavaElement.FIELD) {
                    bodyDeclaration = getFieldDeclaration(bodyDeclarations, (IField) javaElement);
                }

                return bodyDeclaration != null ? bodyDeclaration.modifiers() : Collections.emptyList();
            }
        }
        return Collections.emptyList();
    }

    public static SingleVariableDeclaration getSingleVariableDeclaration(ILocalVariable javaElement) {
        if (javaElement instanceof ILocalVariable && javaElement.getParent() instanceof IMethod) {
            ILocalVariable localVariable = javaElement;
            IMethod method = (IMethod) localVariable.getParent();
            CompilationUnit compilationUnit = SharedASTProvider.getAST(getCompilationUnitFromJavaElement(javaElement), SharedASTProvider.WAIT_YES, null);

            MethodDeclaration methodDeclaration = (MethodDeclaration) getMethodDeclaration(compilationUnit, method);

            @SuppressWarnings("unchecked")
            List<SingleVariableDeclaration> parameters = methodDeclaration.parameters();
            for (SingleVariableDeclaration singleVariableDeclaration : parameters) {
                if (singleVariableDeclaration.getName().toString().equals(localVariable.getElementName())) {
                    return singleVariableDeclaration;
                }
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private static List<BodyDeclaration> getBodyDeclarationsForType(
            AbstractTypeDeclaration abstractTypeDeclaration) {
        if (abstractTypeDeclaration instanceof TypeDeclaration) {
            TypeDeclaration typeDeclaration = (TypeDeclaration) abstractTypeDeclaration;
            return typeDeclaration.bodyDeclarations();
        }
        if (abstractTypeDeclaration instanceof EnumDeclaration) {
            EnumDeclaration enumDeclaration = (EnumDeclaration) abstractTypeDeclaration;
            return enumDeclaration.bodyDeclarations();
        }
        if (abstractTypeDeclaration instanceof AnnotationTypeDeclaration) {
            AnnotationTypeDeclaration annotationTypeDeclaration =
                (AnnotationTypeDeclaration) abstractTypeDeclaration;
            return annotationTypeDeclaration.bodyDeclarations();
        }
        return Collections.emptyList();
    }

    private static MethodDeclaration getMethodDeclaration(List<BodyDeclaration> bodyDeclarations, IMethod method) {
        for (BodyDeclaration bodyDeclaration : bodyDeclarations) {
            if (bodyDeclaration instanceof MethodDeclaration) {
                if (compareMethods((MethodDeclaration)bodyDeclaration, method)) {
                    return (MethodDeclaration) bodyDeclaration;
                }
            }
            if (bodyDeclaration instanceof AnnotationTypeMemberDeclaration) {
                if (compareMethodNames((AnnotationTypeMemberDeclaration)bodyDeclaration, method)) {
                    return (MethodDeclaration) bodyDeclaration;
                }
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static List<SingleVariableDeclaration> getSingleVariableDeclarations(final IMethod method) {
        ICompilationUnit source = method.getCompilationUnit();
        CompilationUnit compilationUnit = SharedASTProvider.getAST(source, SharedASTProvider.WAIT_YES, null);
        final List<SingleVariableDeclaration> parameters = new ArrayList();
        compilationUnit.accept(new ASTVisitor() {
            @Override
            public boolean visit(MethodDeclaration methodDeclaration) {
                if (compareMethods(methodDeclaration, method)) {
                    parameters.addAll(methodDeclaration.parameters());
                }
                return false;
            }
        });
        return parameters;
    }

    public static ILocalVariable getLocalVariable(IMethod method, int offset) {
        List<SingleVariableDeclaration> parameters = getSingleVariableDeclarations(method);
        for (SingleVariableDeclaration parameter : parameters) {
            int parameterStartPosition = parameter.getStartPosition();
            int parameterLength = parameter.getLength();
            if (offset >= parameterStartPosition
                    && offset <= parameterStartPosition + parameterLength) {
                return (ILocalVariable) parameter.resolveBinding().getJavaElement();
            }
        }

        return null;
    }

    public static ILocalVariable getLocalVariable(IMethod method, String paramName) {
        List<SingleVariableDeclaration> parameters = getSingleVariableDeclarations(method);
        for (SingleVariableDeclaration parameter : parameters) {
            if (parameter.getName().getIdentifier().equals(paramName)) {
                return (ILocalVariable) parameter.resolveBinding().getJavaElement();
            }
        }
        return null;
    }

    public static String getStringValue(AnnotationMirror mirror, String attributeName) {
        AnnotationValue annotationValue = getAnnotationValue(mirror, attributeName);
        if (annotationValue != null) {
            return annotationValue.getValue().toString();
        }
        return null;
    }

    public static Boolean getBooleanValue(AnnotationMirror mirror, String attributeName) {
        String value = getStringValue(mirror, attributeName);
        if (value != null) {
            return Boolean.valueOf(value);
        }
        return null;
    }

    public static AnnotationValue getAnnotationValue(AnnotationMirror mirror, String attributeName) {
        Map<AnnotationTypeElementDeclaration, AnnotationValue> values = mirror.getElementValues();
        Set<Map.Entry<AnnotationTypeElementDeclaration, AnnotationValue>> entrySet = values.entrySet();
        for (Map.Entry<AnnotationTypeElementDeclaration, AnnotationValue> entry : entrySet) {
            AnnotationTypeElementDeclaration element = entry.getKey();
            if (element.getSimpleName().equals(attributeName)) {
                return entry.getValue();
            }
        }
        return null;
    }

    public static AnnotationMirror getAnnotation(Declaration declaration,
            Class<? extends java.lang.annotation.Annotation> annotation) {
        Collection<AnnotationMirror> aannotationMirrors = declaration.getAnnotationMirrors();

        for (AnnotationMirror annotationMirror : aannotationMirrors) {
            com.sun.mirror.declaration.AnnotationTypeDeclaration annotationTypeDeclaration = annotationMirror
            .getAnnotationType().getDeclaration();
            if (annotationTypeDeclaration != null
                    && annotationTypeDeclaration.getQualifiedName().equals(annotation.getCanonicalName())) {
                return annotationMirror;
            }
        }
        return null;
    }

    public static String getStringValue(Annotation annotation, String attributeName) {
        if (annotation instanceof NormalAnnotation) {
            Expression expression = getAnnotationValue((NormalAnnotation) annotation, attributeName);
            if (expression != null && expression instanceof StringLiteral) {
                return ((StringLiteral) expression).getLiteralValue();
            }
        }
        return null;
    }

    public static String getEnumValue(Annotation annotation, String attributeName) {
        if (annotation instanceof NormalAnnotation) {
            Expression expression = getAnnotationValue((NormalAnnotation) annotation, attributeName);
            if (expression != null && expression instanceof QualifiedName) {
                return ((QualifiedName) expression).getName().getIdentifier();
            }
        }
        return null;
    }

    public static Boolean getBooleanValue(Annotation annotation, String attributeName) {
        if (annotation instanceof NormalAnnotation) {
            Expression expression = getAnnotationValue((NormalAnnotation) annotation, attributeName);
            if (expression != null && expression instanceof BooleanLiteral) {
                return Boolean.valueOf(((BooleanLiteral) expression).booleanValue());
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static Expression getAnnotationValue(NormalAnnotation normalAnnotation, String attributeName) {
        List<MemberValuePair> memberValuePairs = normalAnnotation.values();
        for (MemberValuePair memberValuePair : memberValuePairs) {
            if (memberValuePair.getName().getIdentifier().equals(attributeName)) {
                return memberValuePair.getValue();
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static Annotation getAnnotation(BodyDeclaration bodyDeclaration,
            Class<? extends java.lang.annotation.Annotation> annotation) {

        return getAnnotation(bodyDeclaration.modifiers(), annotation);
    }

    @SuppressWarnings("unchecked")
    public static Annotation getAnnotation(SingleVariableDeclaration parameter,
            Class<? extends java.lang.annotation.Annotation> annotation) {

        return getAnnotation(parameter.modifiers(), annotation);
    }

    private static Annotation getAnnotation(List<IExtendedModifier> modifiers,
            Class<? extends java.lang.annotation.Annotation> annotation) {
        if (modifiers != null) {
            for (IExtendedModifier extendedModifier : modifiers) {
                if (extendedModifier instanceof Annotation) {
                    Annotation astAnnotation = (Annotation) extendedModifier;
                    String typeName = astAnnotation.getTypeName().getFullyQualifiedName();
                    if (typeName.equals(annotation.getCanonicalName())
                            || typeName.equals(annotation.getSimpleName())) {
                        return astAnnotation;
                    }
                }
            }
        }
        return null;
    }

    public static String getStringValue(IAnnotation annotation, String attributeName)
    throws JavaModelException {
        Object value = AnnotationUtils.getAnnotationValue(annotation, attributeName);
        if (value != null) {
            return value.toString();
        }
        return null;
    }

    public static String getEnumValue(IAnnotation annotation, String attributeName) throws JavaModelException {
        String value = AnnotationUtils.getStringValue(annotation, attributeName);
        if (value != null && value.indexOf(".") != -1) {
            return value.substring(value.lastIndexOf(".") + 1);
        }
        return null;
    }

    public static Boolean getBooleanValue(IAnnotation annotation, String attributeName) throws JavaModelException {
        String value = AnnotationUtils.getStringValue(annotation, attributeName);
        if (value != null) {
            return Boolean.valueOf(value);
        }
        return null;
    }

    public static Object getAnnotationValue(IAnnotation annotation, String attributeName)
    throws JavaModelException {
        IMemberValuePair[] memberValuePairs = annotation.getMemberValuePairs();
        if (memberValuePairs.length > 0) {
            for (IMemberValuePair memberValuePair : memberValuePairs) {
                if (memberValuePair.getMemberName().equals(attributeName)) {
                    return memberValuePair.getValue();
                }
            }
        }
        return null;
    }

    public static IAnnotation getAnnotation(IAnnotatable annotatable,
            Class<? extends java.lang.annotation.Annotation> annotation) throws JavaModelException {
        IAnnotation[] annotations = annotatable.getAnnotations();
        for (IAnnotation jdtAnnotation : annotations) {
            String annotationName = jdtAnnotation.getElementName();
            if (annotationName.equals(annotation.getCanonicalName())
                    || annotationName.equals(annotation.getSimpleName())) {
                return jdtAnnotation;
            }
        }
        return null;
    }

}
