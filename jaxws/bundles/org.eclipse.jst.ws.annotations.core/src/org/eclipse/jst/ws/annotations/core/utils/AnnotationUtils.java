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
import org.eclipse.jdt.core.IAnnotation;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IMemberValuePair;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.Signature;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.AnnotationTypeDeclaration;
import org.eclipse.jdt.core.dom.AnnotationTypeMemberDeclaration;
import org.eclipse.jdt.core.dom.BodyDeclaration;
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
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;
import org.eclipse.jdt.ui.CodeStyleConfiguration;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jst.ws.annotations.core.AnnotationsCorePlugin;
import org.eclipse.ltk.core.refactoring.TextFileChange;
import org.eclipse.swt.graphics.Image;
import org.eclipse.text.edits.MultiTextEdit;
import org.eclipse.text.edits.TextEdit;
import org.eclipse.text.edits.TextEditGroup;

import com.sun.mirror.declaration.AnnotationMirror;
import com.sun.mirror.declaration.AnnotationTypeElementDeclaration;
import com.sun.mirror.declaration.AnnotationValue;

/**
 * Utility class for adding, removing and updating annotations and member value pairs.
 * <p>
 * <strong>Provisional API:</strong> This class/interface is part of an interim API that is still under 
 * development and expected to change significantly before reaching stability. It is being made available at 
 * this early stage to solicit feedback from pioneering adopters on the understanding that any code that uses 
 * this API will almost certainly be broken (repeatedly) as the API evolves.
 * </p>
 * @author sclarke
 */
public final class AnnotationUtils {
    private static final String FORMATTER_INSERT_SPACE_BEFORE_ASSIGNMENT_OPERATOR 
            = "org.eclipse.jdt.core.formatter.insert_space_before_assignment_operator"; //$NON-NLS-1$
    private static final String FORMATTER_INSERT_SPACE_AFTER_ASSIGNMENT_OPERATOR 
            = "org.eclipse.jdt.core.formatter.insert_space_after_assignment_operator"; //$NON-NLS-1$
    private static final String DO_NOT_INSERT 
            = "do not insert"; //$NON-NLS-1$

    private AnnotationUtils() {
    }

    @SuppressWarnings("unchecked")
    public static void getImportChange(CompilationUnit compilationUnit, 
            Class<? extends java.lang.annotation.Annotation> annotation, TextFileChange textFileChange, 
            boolean annotate) {
        try {
            String qualifiedName = annotation.getCanonicalName();
            ImportRewrite importRewrite = CodeStyleConfiguration.createImportRewrite(compilationUnit, true);

            if (annotate) {
                importRewrite.addImport(qualifiedName);
            } else {
                final String annotationSimpleName = annotation.getSimpleName();
                final List<String> occurences = new ArrayList<String>();
                Target target = annotation.getAnnotation(Target.class);
                List<ElementType> elementTypes = Arrays.asList(target.value());
                for (ElementType elementType : elementTypes) {
                    //TODO Handle package annotation imports
                    if (elementType == ElementType.TYPE) {
                        compilationUnit.accept(new ASTVisitor() {
                            @Override
                            public boolean visit(TypeDeclaration typeDeclaration) {
                                countAnnotationOccurences(typeDeclaration.modifiers(), annotationSimpleName,
                                        occurences);
                                return super.visit(typeDeclaration);
                            }
                        });
                    }
                    if (elementType == ElementType.FIELD) {
                        compilationUnit.accept(new ASTVisitor() {
                            @Override
                            public boolean visit(FieldDeclaration fieldDeclaration) {
                                countAnnotationOccurences(fieldDeclaration.modifiers(), annotationSimpleName,
                                        occurences);
                                return super.visit(fieldDeclaration);
                            }
                        });
                    }
                    if (elementType == ElementType.METHOD) {
                        compilationUnit.accept(new ASTVisitor() {
                            @Override
                            public boolean visit(MethodDeclaration methodDeclaration) {
                                countAnnotationOccurences(methodDeclaration.modifiers(), annotationSimpleName,
                                        occurences);                              
                                return super.visit(methodDeclaration);
                            }
                        });
                    }
                    if (elementType == ElementType.PARAMETER) {
                        compilationUnit.accept(new ASTVisitor() {
                            @Override
                            public boolean visit(SingleVariableDeclaration singleVariableDeclaration) {
                                countAnnotationOccurences(singleVariableDeclaration.modifiers(),
                                        annotationSimpleName, occurences);
                                return super.visit(singleVariableDeclaration);
                            }
                        });
                    }
                }
                if (occurences.size() == 1) {
                    importRewrite.removeImport(qualifiedName);   
                }
            }
            if (importRewrite.hasRecordedChanges()) {
                //TODO Cleanup imports
                // Repeatedly adding and removing an import where none existed before will
                // insert a new line on each insert.
                TextEdit importTextEdit = importRewrite.rewriteImports(null);
                textFileChange.addEdit(importTextEdit);
            }
        } catch (CoreException ce) {
            AnnotationsCorePlugin.log(ce.getStatus());
        }
    }
    
    private static void countAnnotationOccurences(List<IExtendedModifier> modifiers, String annotationSimpleName, 
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

    public static void createPackageDeclarationAnnotationChange(ICompilationUnit source, 
            PackageDeclaration packageDeclaration, ASTRewrite rewriter, Annotation annotation, 
            TextFileChange textFileChange) throws CoreException {
        IProgressMonitor monitor = new NullProgressMonitor();
        IPath path = source.getResource().getFullPath();
        boolean connected = false;
        ITextFileBufferManager bufferManager = FileBuffers.getTextFileBufferManager();
        try {
            if (packageDeclaration != null && !isAnnotationPresent(packageDeclaration, annotation)) {
                bufferManager.connect(path, LocationKind.IFILE, monitor);
                connected = true;
                IDocument document = bufferManager.getTextFileBuffer(path, LocationKind.IFILE).getDocument();

                ListRewrite listRewrite = rewriter.getListRewrite(packageDeclaration,
                        PackageDeclaration.ANNOTATIONS_PROPERTY);

                listRewrite.insertFirst(annotation, null);

                TextEdit annotationTextEdit = rewriter.rewriteAST(document, getOptions(source));

                textFileChange.addEdit(annotationTextEdit);

                textFileChange.addTextEditGroup(new TextEditGroup("AA", new //$NON-NLS-1$
                        TextEdit[] { annotationTextEdit }));
            }
        } finally {
            if (connected) {
                bufferManager.disconnect(path, LocationKind.IFILE, monitor);
            }
        }
    }
    
    public static void removeAnnotationFromPackageDeclaration(ICompilationUnit source,
            PackageDeclaration packageDeclaration, ASTRewrite rewriter, Annotation annotation,
            TextFileChange textFileChange) throws CoreException {
        ITextFileBufferManager bufferManager = FileBuffers.getTextFileBufferManager();
        IPath path = source.getResource().getFullPath();
        boolean connected = false;
        try {
            if (packageDeclaration != null && isAnnotationPresent(packageDeclaration, annotation)) {
                bufferManager.connect(path, LocationKind.IFILE, null);
                connected = true;
                IDocument document = bufferManager.getTextFileBuffer(path, LocationKind.IFILE).getDocument();

                ListRewrite listRewrite = rewriter.getListRewrite(packageDeclaration,
                        PackageDeclaration.ANNOTATIONS_PROPERTY);

                @SuppressWarnings("unchecked")
                List originalList = listRewrite.getOriginalList();
                for (Object object : originalList) {
                    if (object instanceof Annotation && compareAnnotations((Annotation) object, annotation)) {
                        listRewrite.remove((Annotation) object, null);
                    }
                }

                TextEdit annotationTextEdit = rewriter.rewriteAST(document, getOptions(source));

                textFileChange.addEdit(annotationTextEdit);

                textFileChange.addTextEditGroup(new TextEditGroup("AA", new //$NON-NLS-1$
                        TextEdit[] { annotationTextEdit }));
            }
        } finally {
            if (connected) {
                bufferManager.disconnect(path, LocationKind.IFILE, null);
            }
        }
    }
    
    public static void createTypeAnnotationChange(ICompilationUnit source, CompilationUnit 
            compilationUnit, ASTRewrite rewriter, Annotation annotation, 
            TextFileChange textFileChange) throws CoreException {
        ITextFileBufferManager bufferManager = FileBuffers.getTextFileBufferManager();
        IPath path = source.getResource().getFullPath();
        boolean connected = false;
        try {
            IType type = compilationUnit.getTypeRoot().findPrimaryType();
            @SuppressWarnings("unchecked")
            List<TypeDeclaration> types = compilationUnit.types();
            for (AbstractTypeDeclaration abstractTypeDeclaration : types) {
                if (compareTypes(abstractTypeDeclaration, type)
                        && !isAnnotationPresent(abstractTypeDeclaration, annotation)) {
                    bufferManager.connect(path, LocationKind.IFILE, null);
                    connected = true;
                    IDocument document = bufferManager.getTextFileBuffer(path, LocationKind.IFILE)
                            .getDocument();

                    ListRewrite listRewrite = rewriter.getListRewrite(abstractTypeDeclaration,
                            getChildListPropertyDescriptorForType(abstractTypeDeclaration));
                    
                    listRewrite.insertFirst(annotation, null);

                    TextEdit annotationTextEdit = rewriter.rewriteAST(document, getOptions(source));

                    textFileChange.addEdit(annotationTextEdit);

                    textFileChange.addTextEditGroup(new TextEditGroup("AA", new //$NON-NLS-1$
                            TextEdit[] { annotationTextEdit }));
                }
            }
        } finally {
            if (connected) {
                bufferManager.disconnect(path, LocationKind.IFILE, null);
            }
        }
    }

    public static void removeAnnotationFromType(ICompilationUnit source, CompilationUnit 
            compilationUnit, ASTRewrite rewriter, Annotation annotation, 
            TextFileChange textFileChange) throws CoreException {
        ITextFileBufferManager bufferManager = FileBuffers.getTextFileBufferManager();
        IPath path = source.getResource().getFullPath();
        boolean connected = false;
        try {
            IType type = compilationUnit.getTypeRoot().findPrimaryType();
            @SuppressWarnings("unchecked")
            List<TypeDeclaration> types = compilationUnit.types();
            for (AbstractTypeDeclaration abstractTypeDeclaration : types) {
                if (compareTypes(abstractTypeDeclaration, type) 
                        && isAnnotationPresent(abstractTypeDeclaration, annotation)) {
                    bufferManager.connect(path, LocationKind.IFILE, null);
                    connected = true;

                    IDocument document = bufferManager.getTextFileBuffer(path, LocationKind.IFILE)
                            .getDocument();

                    ListRewrite listRewrite = rewriter.getListRewrite(abstractTypeDeclaration,
                            getChildListPropertyDescriptorForType(abstractTypeDeclaration));

                    @SuppressWarnings("unchecked")
                    List originalList = listRewrite.getOriginalList();
                    for (Object object : originalList) {
                        if (object instanceof Annotation && compareAnnotations((Annotation)object, 
                                annotation)) {
                            listRewrite.remove((Annotation)object, null);
                        }
                    }
                    
                    TextEdit annotationTextEdit = rewriter.rewriteAST(document, getOptions(source));
                     
                    textFileChange.addEdit(annotationTextEdit);

                    textFileChange.addTextEditGroup(new TextEditGroup("AA", new  //$NON-NLS-1$
                             TextEdit[] {annotationTextEdit}));
                }
            }
        } finally {
            if (connected) {
                bufferManager.disconnect(path, LocationKind.IFILE, null);
            }
        }
    }
    
    public static void createMethodAnnotationChange(ICompilationUnit source, CompilationUnit 
            compilationUnit, ASTRewrite rewriter, IMethod method, Annotation annotation, 
            TextFileChange textFileChange) throws CoreException {
        ITextFileBufferManager bufferManager = FileBuffers.getTextFileBufferManager();
        IPath path = source.getResource().getFullPath();
        boolean connected = false;
        try {
            IType type = method.getDeclaringType();
            @SuppressWarnings("unchecked")
            List<AbstractTypeDeclaration> types = compilationUnit.types();
            for (AbstractTypeDeclaration abstractTypeDeclaration : types) {
                if (compareTypes(abstractTypeDeclaration, type)) {
                    List<BodyDeclaration> bodyDeclarations = getBodyDeclarationsForType(
                            abstractTypeDeclaration);
                    BodyDeclaration bodyDeclaration = getMethodDeclaration(bodyDeclarations, method);
                    if (!isAnnotationPresent(bodyDeclaration, annotation)) {
                        bufferManager.connect(path, LocationKind.IFILE, null);
                        connected = true;

                        IDocument document = bufferManager.getTextFileBuffer(path, LocationKind.IFILE)
                                .getDocument();
                        
                        ListRewrite listRewrite = rewriter.getListRewrite(bodyDeclaration,
                                getChildListPropertyDescriptorForMethod(bodyDeclaration));
                        listRewrite.insertAt(annotation, 0, null);

                        TextEdit annotationTextEdit = rewriter.rewriteAST(document, getOptions(source));

                        textFileChange.addEdit(annotationTextEdit);
                        textFileChange.addTextEditGroup(new TextEditGroup("AA", new //$NON-NLS-1$
                                TextEdit[] { annotationTextEdit }));
                    }
                }
            }
        } finally {
            if (connected) {
                bufferManager.disconnect(path, LocationKind.IFILE, null);
            }
        }
    }
    
    public static void removeAnnotationFromMethod(ICompilationUnit source, CompilationUnit 
            compilationUnit, ASTRewrite rewriter, IMethod method, Annotation annotation, 
            TextFileChange textFileChange) throws CoreException {
        ITextFileBufferManager bufferManager = FileBuffers.getTextFileBufferManager();
        IPath path = source.getResource().getFullPath();
        boolean connected = false;
        try {
            IType type = method.getDeclaringType();
            @SuppressWarnings("unchecked")
            List<AbstractTypeDeclaration> types = compilationUnit.types();
            for (AbstractTypeDeclaration abstractTypeDeclaration : types) {
                if (compareTypes(abstractTypeDeclaration, type)) {
                    List<BodyDeclaration> bodyDeclarations = getBodyDeclarationsForType(
                            abstractTypeDeclaration);
                    BodyDeclaration bodyDeclaration = getMethodDeclaration(bodyDeclarations, method);
                    if (isAnnotationPresent(bodyDeclaration, annotation)) {
                        bufferManager.connect(path, LocationKind.IFILE, null);
                        connected = true;

                        IDocument document = bufferManager.getTextFileBuffer(path, LocationKind.IFILE)
                                .getDocument();

                        ListRewrite listRewrite = rewriter.getListRewrite(bodyDeclaration,
                                getChildListPropertyDescriptorForMethod(bodyDeclaration));

                        @SuppressWarnings("unchecked")
                        List originalList = listRewrite.getOriginalList();
                        for (Object object : originalList) {
                            if (object instanceof Annotation
                                    && compareAnnotations((Annotation) object, annotation)) {
                                listRewrite.remove((Annotation) object, null);
                            }
                        }

                        TextEdit annotationTextEdit = rewriter.rewriteAST(document, getOptions(source));

                        textFileChange.addEdit(annotationTextEdit);
                        textFileChange.addTextEditGroup(new TextEditGroup("AA", new //$NON-NLS-1$
                                TextEdit[] { annotationTextEdit }));
                    }
                }
            }
        } finally {
            if (connected) {
                bufferManager.disconnect(path, LocationKind.IFILE, null);
            }
        }
    }

    public static void createFiedlAnnotationChange(ICompilationUnit source, CompilationUnit 
            compilationUnit, ASTRewrite rewriter, IField field, Annotation annotation, 
            TextFileChange textFileChange) throws CoreException {
        ITextFileBufferManager bufferManager = FileBuffers.getTextFileBufferManager();
        IPath path = source.getResource().getFullPath();
        boolean connected = false;
        try {
            IType type = field.getDeclaringType();
            @SuppressWarnings("unchecked")
            List<AbstractTypeDeclaration> types = compilationUnit.types();
            for (AbstractTypeDeclaration abstractTypeDeclaration : types) {
                if (compareTypes(abstractTypeDeclaration, type)) {
                    List<BodyDeclaration> bodyDeclarations = getBodyDeclarationsForType(
                            abstractTypeDeclaration);
                    FieldDeclaration fieldDeclaration = (FieldDeclaration) getFieldDeclaration(
                            bodyDeclarations, field);
                    if (!isAnnotationPresent(fieldDeclaration, annotation)) {
                        bufferManager.connect(path, LocationKind.IFILE, null);
                        connected = true;

                        IDocument document = bufferManager.getTextFileBuffer(path, LocationKind.IFILE)
                                .getDocument();

                        ListRewrite listRewrite = rewriter.getListRewrite(fieldDeclaration,
                                FieldDeclaration.MODIFIERS2_PROPERTY);
                        listRewrite.insertAt(annotation, 0, null);

                        TextEdit annotationTextEdit = rewriter.rewriteAST(document, getOptions(source));

                        textFileChange.addEdit(annotationTextEdit);
                        textFileChange.addTextEditGroup(new TextEditGroup("AA", new //$NON-NLS-1$
                                TextEdit[] { annotationTextEdit }));
                    }
                }
            }
        } finally {
            if (connected) {
                bufferManager.disconnect(path, LocationKind.IFILE, null);
            }
        }
    }
    
    public static void removeAnnotationFromField(ICompilationUnit source, CompilationUnit 
            compilationUnit, ASTRewrite rewriter, IField field, Annotation annotation, 
            TextFileChange textFileChange) throws CoreException {
        ITextFileBufferManager bufferManager = FileBuffers.getTextFileBufferManager();
        IPath path = source.getResource().getFullPath();
        boolean connected = false;
        try {
            IType type = field.getDeclaringType();
            @SuppressWarnings("unchecked")
            List<AbstractTypeDeclaration> types = compilationUnit.types();
            for (AbstractTypeDeclaration abstractTypeDeclaration : types) {
                if (compareTypes(abstractTypeDeclaration, type)) {
                    List<BodyDeclaration> bodyDeclarations = getBodyDeclarationsForType(
                            abstractTypeDeclaration);
                    FieldDeclaration fieldDeclaration = (FieldDeclaration) getFieldDeclaration(
                            bodyDeclarations, field);

                    if (isAnnotationPresent(fieldDeclaration, annotation)) {
                        bufferManager.connect(path, LocationKind.IFILE, null);
                        connected = true;

                        IDocument document = bufferManager.getTextFileBuffer(path, LocationKind.IFILE)
                                .getDocument();

                        ListRewrite listRewrite = rewriter.getListRewrite(fieldDeclaration,
                                FieldDeclaration.MODIFIERS2_PROPERTY);

                        @SuppressWarnings("unchecked")
                        List originalList = listRewrite.getOriginalList();
                        for (Object object : originalList) {
                            if (object instanceof Annotation
                                    && compareAnnotations((Annotation) object, annotation)) {
                                listRewrite.remove((Annotation) object, null);
                            }
                        }

                        TextEdit annotationTextEdit = rewriter.rewriteAST(document, getOptions(source));

                        textFileChange.addEdit(annotationTextEdit);
                        textFileChange.addTextEditGroup(new TextEditGroup("AA", new //$NON-NLS-1$
                                TextEdit[] { annotationTextEdit }));
                    }
                }
            }
        } finally {
            if (connected) {
                bufferManager.disconnect(path, LocationKind.IFILE, null);
            }
        }
    }
   
    public static void createMethodParameterAnnotationChange(ICompilationUnit source, CompilationUnit 
            compilationUnit, ASTRewrite rewriter, SingleVariableDeclaration singleVariableDeclaration, 
            IMethod method, Annotation annotation, TextFileChange textFileChange) throws CoreException {

        ITextFileBufferManager bufferManager = FileBuffers.getTextFileBufferManager();
        IPath path = source.getResource().getFullPath();
        boolean connected = false;
        try {
            IType type = method.getDeclaringType();
            @SuppressWarnings("unchecked")
            List<AbstractTypeDeclaration> types = compilationUnit.types();
            for (AbstractTypeDeclaration abstractTypeDeclaration : types) {
                if (compareTypes(abstractTypeDeclaration, type)) {
                    List<BodyDeclaration> bodyDeclarations = getBodyDeclarationsForType(abstractTypeDeclaration);
                    MethodDeclaration methodDeclaration = (MethodDeclaration) getMethodDeclaration(
                            bodyDeclarations, method);

                    @SuppressWarnings("unchecked")
                    List<SingleVariableDeclaration> parameters = methodDeclaration.parameters();
                    for (SingleVariableDeclaration parameter : parameters) {
                        if (compareMethodParameters(parameter, singleVariableDeclaration)
                                && !isAnnotationPresent(parameter, annotation)) {
                            bufferManager.connect(path, LocationKind.IFILE, null);
                            connected = true;

                            IDocument document = bufferManager.getTextFileBuffer(path, LocationKind.IFILE)
                                    .getDocument();

                            ListRewrite listRewrite = rewriter.getListRewrite(parameter,
                                    SingleVariableDeclaration.MODIFIERS2_PROPERTY);

                            listRewrite.insertAt(annotation, -1, null);

                            TextEdit annotationTextEdit = rewriter.rewriteAST(document, getOptions(source));

                            textFileChange.addEdit(annotationTextEdit);

                            textFileChange.addTextEditGroup(new TextEditGroup("AA", new //$NON-NLS-1$
                                    TextEdit[] { annotationTextEdit }));
                        }
                    }
                }
            }
        } finally {
            if (connected) {
                bufferManager.disconnect(path, LocationKind.IFILE, null);
            }
        }        
    }
    
    public static void createMethodParameterAnnotationChange(ICompilationUnit source, ASTRewrite rewriter,
            SingleVariableDeclaration singleVariableDeclaration, Annotation annotation,
            TextFileChange textFileChange) throws CoreException {
        ITextFileBufferManager bufferManager = FileBuffers.getTextFileBufferManager();
        IPath path = source.getResource().getFullPath();
        boolean connected = false;
        try {
            if (!isAnnotationPresent(singleVariableDeclaration, annotation)) {
                bufferManager.connect(path, LocationKind.IFILE, null);
                connected = true;

                IDocument document = bufferManager.getTextFileBuffer(path, LocationKind.IFILE).getDocument();

                ListRewrite listRewrite = rewriter.getListRewrite(singleVariableDeclaration,
                        SingleVariableDeclaration.MODIFIERS2_PROPERTY);

                listRewrite.insertAt(annotation, -1, null);

                TextEdit annotationTextEdit = rewriter.rewriteAST(document, getOptions(source));

                textFileChange.addEdit(annotationTextEdit);

                textFileChange.addTextEditGroup(new TextEditGroup("AA", new //$NON-NLS-1$
                        TextEdit[] { annotationTextEdit }));
            }
        } finally {
            if (connected) {
                bufferManager.disconnect(path, LocationKind.IFILE, null);
            }
        }
    }
    
    public static void removeAnnotationFromMethodParameter(ICompilationUnit source, ASTRewrite rewriter,
            SingleVariableDeclaration singleVariableDeclaration, Annotation annotation,
            TextFileChange textFileChange) throws CoreException {
        ITextFileBufferManager bufferManager = FileBuffers.getTextFileBufferManager();
        IPath path = source.getResource().getFullPath();
        boolean connected = false;
        try {
            if (isAnnotationPresent(singleVariableDeclaration, annotation)) {
                bufferManager.connect(path, LocationKind.IFILE, null);
                connected = true;

                IDocument document = bufferManager.getTextFileBuffer(path, LocationKind.IFILE).getDocument();

                ListRewrite listRewrite = rewriter.getListRewrite(singleVariableDeclaration,
                        SingleVariableDeclaration.MODIFIERS2_PROPERTY);

                @SuppressWarnings("unchecked")
                List originalList = listRewrite.getOriginalList();
                for (Object object : originalList) {
                    if (object instanceof Annotation && compareAnnotations((Annotation)object, annotation)) {
                        listRewrite.remove((Annotation)object, null);
                    }
                }

                TextEdit annotationTextEdit = rewriter.rewriteAST(document, getOptions(source));

                textFileChange.addEdit(annotationTextEdit);

                textFileChange.addTextEditGroup(new TextEditGroup("AA", new //$NON-NLS-1$
                        TextEdit[] { annotationTextEdit }));
            }
        } finally {
            if (connected) {
                bufferManager.disconnect(path, LocationKind.IFILE, null);
            }
        }
    }

    public static void createMemberVaulePairChange(ICompilationUnit source, CompilationUnit compilationUnit,
            ASTRewrite rewriter, IJavaElement javaElement, IAnnotation annotation, ASTNode memberValuePair,
            TextFileChange textFileChange) throws CoreException {
        ITextFileBufferManager bufferManager = FileBuffers.getTextFileBufferManager();
        IPath path = source.getResource().getFullPath();
        boolean connected = false;
        try {
            List<IExtendedModifier> modifiers = getExtendedModifiers(compilationUnit, javaElement);
            for (IExtendedModifier extendedModifier : modifiers) {
                if (extendedModifier instanceof NormalAnnotation) {
                    NormalAnnotation existingAnnotation = (NormalAnnotation) extendedModifier;
                    if (AnnotationUtils.compareAnnotations(annotation, existingAnnotation)) {
                        bufferManager.connect(path, LocationKind.IFILE, null);
                        connected = true;

                        IDocument document = bufferManager.getTextFileBuffer(path, LocationKind.IFILE)
                                .getDocument();

                        ListRewrite listRewrite = rewriter.getListRewrite(existingAnnotation,
                                NormalAnnotation.VALUES_PROPERTY);

                        listRewrite.insertLast(memberValuePair, null);

                        TextEdit annotationTextEdit = rewriter.rewriteAST(document, getOptions(source));

                        textFileChange.addEdit(annotationTextEdit);

                        textFileChange.addTextEditGroup(new TextEditGroup("AA", new //$NON-NLS-1$
                                TextEdit[] { annotationTextEdit }));
                    }
                }
            }
        } finally {
            if (connected) {
                bufferManager.disconnect(path, LocationKind.IFILE, null);
            }
        }
    }
    
    public static void createMemberVaulePairChange(ICompilationUnit source, CompilationUnit compilationUnit,
            ASTRewrite rewriter, NormalAnnotation annotation, ASTNode memberValuePair,
            TextFileChange textFileChange) throws CoreException {
        ITextFileBufferManager bufferManager = FileBuffers.getTextFileBufferManager();
        IPath path = source.getResource().getFullPath();
        boolean connected = false;
        try {
            bufferManager.connect(path, LocationKind.IFILE, null);
            connected = true;

            IDocument document = bufferManager.getTextFileBuffer(path, LocationKind.IFILE).getDocument();

            ListRewrite listRewrite = rewriter.getListRewrite(annotation,
                    NormalAnnotation.VALUES_PROPERTY);

            listRewrite.insertLast(memberValuePair, null);

            TextEdit annotationTextEdit = rewriter.rewriteAST(document, getOptions(source));

            textFileChange.addEdit(annotationTextEdit);

            textFileChange.addTextEditGroup(new TextEditGroup("AA", new //$NON-NLS-1$
                    TextEdit[] { annotationTextEdit }));
        } finally {
            if (connected) {
                bufferManager.disconnect(path, LocationKind.IFILE, null);
            }
        }
    }

    public static void updateMemberVaulePairValue(ICompilationUnit source, CompilationUnit compilationUnit,
            ASTRewrite rewriter, IJavaElement javaElement, IAnnotation annotation, IMemberValuePair memberValuePair,
            ASTNode value, TextFileChange textFileChange) throws CoreException {
        ITextFileBufferManager bufferManager = FileBuffers.getTextFileBufferManager();
        IPath path = source.getResource().getFullPath();
        boolean connected = false;
        try {
            List<IExtendedModifier> modifiers = getExtendedModifiers(compilationUnit, javaElement);
            for (IExtendedModifier extendedModifier : modifiers) {
                if (extendedModifier instanceof NormalAnnotation) {
                    NormalAnnotation existingAnnotation = (NormalAnnotation) extendedModifier;
                    if (AnnotationUtils.compareAnnotations(annotation, existingAnnotation)) {
                        @SuppressWarnings("unchecked")
                        List<MemberValuePair> memberVaulePairs = existingAnnotation.values();
                        for (MemberValuePair memberValuePairDOM : memberVaulePairs) {
                            if (memberValuePairDOM.getName().toString().equals(
                                    memberValuePair.getMemberName())) {
                                bufferManager.connect(path, LocationKind.IFILE, null);
                                connected = true;

                                IDocument document = bufferManager
                                        .getTextFileBuffer(path, LocationKind.IFILE).getDocument();

                                rewriter.set(memberValuePairDOM, MemberValuePair.VALUE_PROPERTY, value, null);

                                TextEdit annotationTextEdit = rewriter.rewriteAST(document,
                                        getOptions(source));

                                textFileChange.addEdit(annotationTextEdit);

                                textFileChange.addTextEditGroup(new TextEditGroup("AA", new //$NON-NLS-1$
                                        TextEdit[] { annotationTextEdit }));
                            }
                        }
                    }
                }
            }
        } finally {
            if (connected) {
                bufferManager.disconnect(path, LocationKind.IFILE, null);
            }
        }
    }

    public static void updateMemberVaulePairValue(ICompilationUnit source, CompilationUnit compilationUnit,
            ASTRewrite rewriter, NormalAnnotation annotation, MemberValuePair memberValuePair,
            ASTNode value, TextFileChange textFileChange) throws CoreException {
        ITextFileBufferManager bufferManager = FileBuffers.getTextFileBufferManager();
        IPath path = source.getResource().getFullPath();
        boolean connected = false;
        try {
            bufferManager.connect(path, LocationKind.IFILE, null);
            connected = true;

            IDocument document = bufferManager.getTextFileBuffer(path, LocationKind.IFILE).getDocument();

            rewriter.set(memberValuePair, MemberValuePair.VALUE_PROPERTY, value, null);

            TextEdit annotationTextEdit = rewriter.rewriteAST(document, getOptions(source));

            textFileChange.addEdit(annotationTextEdit);

            textFileChange.addTextEditGroup(new TextEditGroup("AA", new //$NON-NLS-1$
                    TextEdit[] { annotationTextEdit }));
        } finally {
            if (connected) {
                bufferManager.disconnect(path, LocationKind.IFILE, null);
            }
        }
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

    public static CompilationUnit getASTParser(ICompilationUnit source) {
        ASTParser parser = ASTParser.newParser(AST.JLS3);
        parser.setSource(source);
        CompilationUnit compilationUnit = (CompilationUnit) parser.createAST(new NullProgressMonitor());
        compilationUnit.recordModifications();
        return compilationUnit;
    }
    
    public static TextFileChange createTextFileChange(String textFileChangeName, IFile file) {
        TextFileChange textFileChange = new TextFileChange(textFileChangeName, file);
        MultiTextEdit multiTextEdit = new MultiTextEdit();
        textFileChange.setEdit(multiTextEdit);
        return textFileChange;
    }

    @SuppressWarnings("unchecked")
    public static List<SingleVariableDeclaration> getMethodParameters(IType type, final IMethod method) {
        ICompilationUnit source = type.getCompilationUnit();
        CompilationUnit compilationUnit = getASTParser(source);
        final List<SingleVariableDeclaration> parameters = new ArrayList();
        compilationUnit.accept(new ASTVisitor() {
            @Override
            public boolean visit(MethodDeclaration methodDeclaration) {
                if (compareMethods(methodDeclaration, method)) {
                    parameters.addAll(methodDeclaration.parameters());
                }
                return super.visit(methodDeclaration);
            }
        });     
        return parameters;
    }

    public static String getAnnotationName(Annotation annotation) {
        Name annotationTypeName = annotation.getTypeName();
        return annotationTypeName.getFullyQualifiedName();
    }
    
    public static boolean compareTypes(AbstractTypeDeclaration abstractTypeDeclaration, IType type) {
        return abstractTypeDeclaration.getName().getIdentifier().equals(type.getElementName());
    }
    
    @SuppressWarnings("unchecked")
    public static boolean compareMethods(MethodDeclaration methodDeclaration, IMethod method) {
    	if (methodDeclaration.getName().getIdentifier().equals(method.getElementName())) {
	    	String[] parametetNames = method.getParameterTypes();
	    	List<SingleVariableDeclaration> methodDeclarationParameters = methodDeclaration.parameters();
	    	if (parametetNames.length == methodDeclarationParameters.size()) {
		    	for (int i = 0; i < parametetNames.length; i++) {
	                String simpleName1 = Signature.toString(parametetNames[i]);
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
    
    public static boolean compareMethods(AnnotationTypeMemberDeclaration memmberDeclaration, IMethod method) {
        if (memmberDeclaration.getName().getIdentifier().equals(method.getElementName())) {
                return true;
        }
        return false;
    }
    
    @SuppressWarnings("unchecked")
    public static boolean compareFields(FieldDeclaration fieldDeclaration, IField field) {
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
    
    private static boolean compareAnnotations(Annotation newAnnotation, Annotation existingAnnotation) {
        return AnnotationUtils.getAnnotationName(existingAnnotation).equals(
                AnnotationUtils.getAnnotationName(newAnnotation));
    }
    
    private static boolean compareAnnotations(IAnnotation newAnnotation, Annotation existingAnnotation) {
        return AnnotationUtils.getAnnotationName(existingAnnotation).equals(newAnnotation.getElementName());
    }
    
    @SuppressWarnings("unchecked")
    public static boolean isAnnotationPresent(SingleVariableDeclaration parameter, String annotationName) {
        List<IExtendedModifier> modifiers = parameter.modifiers();
        for (IExtendedModifier extendedModifier : modifiers) {
            if (extendedModifier instanceof Annotation) {
                Annotation existingAnnotation = (Annotation) extendedModifier;
                if (AnnotationUtils.getAnnotationName(existingAnnotation).equals(annotationName)) {
                    return true;
                }
            }
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    public static boolean isAnnotationPresent(SingleVariableDeclaration parameter,
            Annotation annatotationToAdd) {
        boolean exists = false;
        List<IExtendedModifier> modifiers = parameter.modifiers();
        for (IExtendedModifier extendedModifier : modifiers) {
            if (extendedModifier instanceof Annotation) {
                Annotation existingAnnotation = (Annotation) extendedModifier;
                if (compareAnnotations(annatotationToAdd, existingAnnotation)) {
                    return true;
                }
            }
        }
        return exists;
    }
    
    public static boolean isAnnotationPresent(IJavaElement javaElement, String annotationName) {
        if (javaElement.getElementType() == IJavaElement.COMPILATION_UNIT) {
            return isAnnotationPresent(((ICompilationUnit)javaElement).findPrimaryType(), annotationName);
        }
        
        ICompilationUnit source = ((IMember)javaElement).getCompilationUnit();
        
        int elementType = javaElement.getElementType();
        CompilationUnit compilationUnit = getASTParser(source);
        if (elementType == IJavaElement.PACKAGE_DECLARATION) {
            return isAnnotationPresent(compilationUnit.getPackage(), annotationName);
        }
        if (elementType == IJavaElement.TYPE || elementType == IJavaElement.METHOD || 
                elementType == IJavaElement.FIELD) {
            return isAnnotationPresent(compilationUnit, javaElement, annotationName);
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    public static boolean isAnnotationPresent(PackageDeclaration packageDeclaration, String annotationName) {
        List<Annotation> annotations = packageDeclaration.annotations();
        for (Annotation existingAnnotation : annotations) {
            if (AnnotationUtils.getAnnotationName(existingAnnotation).equals(annotationName)) {
                return true;
            }
        }
        return false;
    }
    
    @SuppressWarnings("unchecked")
    public static boolean isAnnotationPresent(PackageDeclaration packageDeclaration, Annotation annotation) {
        List<Annotation> annotations = packageDeclaration.annotations();
        for (Annotation existingAnnotation : annotations) {
            if (compareAnnotations(annotation, existingAnnotation)) {
                return true;
            }
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
                if (compareAnnotations(annatotationToAdd, existingAnnotation)) {
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
        IType type = compilationUnit.getTypeRoot().findPrimaryType();
        List<AbstractTypeDeclaration> types = compilationUnit.types();
        for (AbstractTypeDeclaration abstractTypeDeclaration : types) {
            if (compareTypes(abstractTypeDeclaration, type)) {
                List<BodyDeclaration> bodyDeclarations = getBodyDeclarationsForType(abstractTypeDeclaration);
                
                BodyDeclaration bodyDeclaration = null;
                if (javaElement.getElementType() == IJavaElement.TYPE) {
                    bodyDeclaration = abstractTypeDeclaration;
                }
                
                if (javaElement.getElementType() == IJavaElement.METHOD) {
                    bodyDeclaration = getMethodDeclaration(bodyDeclarations, (IMethod) javaElement);
                }
                if (javaElement.getElementType() == IJavaElement.FIELD) {
                    bodyDeclaration = getFieldDeclaration(bodyDeclarations, (IField) javaElement);
                }

                return bodyDeclaration != null ? bodyDeclaration.modifiers() : Collections.emptyList();
            }
        }
        return Collections.emptyList();
    }
    
    @SuppressWarnings("unchecked")
    public static List<BodyDeclaration> getBodyDeclarationsForType(
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
    
    private static BodyDeclaration getMethodDeclaration(List<BodyDeclaration> bodyDeclarations, 
            IMethod method) {
        for (BodyDeclaration bodyDeclaration : bodyDeclarations) {
            if (bodyDeclaration instanceof MethodDeclaration) {
                if (compareMethods((MethodDeclaration)bodyDeclaration, method)) {
                    return bodyDeclaration;
                }
            }
            if (bodyDeclaration instanceof AnnotationTypeMemberDeclaration) {
                if (compareMethods((AnnotationTypeMemberDeclaration)bodyDeclaration, method)) {
                    return bodyDeclaration;
                }
            }
        }
        return null;
    }

    private static BodyDeclaration getFieldDeclaration(List<BodyDeclaration> bodyDeclarations, IField field) {
        for (BodyDeclaration bodyDeclaration : bodyDeclarations) {
            if (bodyDeclaration instanceof FieldDeclaration) {
                if (compareFields((FieldDeclaration)bodyDeclaration, field)) {
                    return bodyDeclaration;
                }
            }
        }
        return null;
    }
    
    @SuppressWarnings("unchecked")
    public static SingleVariableDeclaration getMethodParameter(CompilationUnit compilationUnit, 
                IMethod method, int offset) {
        if (compilationUnit == null) {
          compilationUnit = getASTParser(method.getCompilationUnit());                
        }
        IType type = method.getDeclaringType();
        List<AbstractTypeDeclaration> types = compilationUnit.types();
        for (AbstractTypeDeclaration abstractTypeDeclaration : types) {
            if (abstractTypeDeclaration.getName().getIdentifier().equals(type.getElementName())) {
                String methodToAnnotateName = method.getElementName();
                List<BodyDeclaration> bodyDeclarations = getBodyDeclarationsForType(abstractTypeDeclaration);
                for (BodyDeclaration bodyDeclaration : bodyDeclarations) {
                    if (bodyDeclaration instanceof MethodDeclaration) {
                        MethodDeclaration methodDeclaration = (MethodDeclaration) bodyDeclaration;
                        if (methodDeclaration.getName().getIdentifier().equals(methodToAnnotateName)) {
                            List<SingleVariableDeclaration> parameters = methodDeclaration.parameters();
                            for (SingleVariableDeclaration parameter : parameters) {
                                int parameterStartPosition = parameter.getStartPosition();
                                int parameterLength = parameter.getLength();
                                if (offset >= parameterStartPosition
                                        && offset <= parameterStartPosition + parameterLength) {
                                    return parameter;
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    private static boolean compareMethodParameters(SingleVariableDeclaration paramOne,
            SingleVariableDeclaration paramTwo) {
        String typeOne = paramOne.getType().toString();
        String nameOne = paramOne.getName().getIdentifier();

        String typeTwo = paramTwo.getType().toString();
        String nameTwo = paramTwo.getName().getIdentifier();

        return (typeOne.equals(typeTwo)) && (nameOne.equals(nameTwo));
    }

    public static CompletionProposal createCompletionProposal(String proposal, Expression value) {
        int replacementOffset = value.getStartPosition() + 1;
        int replacementLength = value.getLength() - 2;

        return new CompletionProposal(proposal, replacementOffset, replacementLength,
                proposal.length());
    }
    
    public static CompletionProposal createCompletionProposal(String proposal, Expression value,
            Image image) {
        int replacementOffset = value.getStartPosition() + 1;
        int replacementLength = value.getLength() - 2;

        return new CompletionProposal(proposal, replacementOffset, replacementLength, proposal.length(), 
                image, proposal, null, null);
    }
    
    /**
     * Searches the passed <code>AnnotationMirror</code> for an <code>AnnotationTypeElementDeclaration</code>
     * that matches the elementName. If a match is made the string representation of the 
     * <code>AnnotationValue</code> value object is returned. If no match is made a zero length String is 
     * returned.
     * @param mirror
     * @param elementName
     * @return
     */
    public static String findAnnotationValue(AnnotationMirror mirror, String elementName) {
        Map<AnnotationTypeElementDeclaration, AnnotationValue> values = mirror.getElementValues();
        Set<Map.Entry<AnnotationTypeElementDeclaration, AnnotationValue>> entrySet = values.entrySet();
        for (Map.Entry<AnnotationTypeElementDeclaration, AnnotationValue> entry : entrySet) {
            AnnotationTypeElementDeclaration element = entry.getKey();
            if (element.getSimpleName().equals(elementName)) {
                AnnotationValue annotationValue = entry.getValue();
                return annotationValue.getValue().toString();
            }
        }
        return "";
    }            

}
