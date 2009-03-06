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
package org.eclipse.jst.ws.internal.jaxws.core.utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.IExtendedModifier;
import org.eclipse.jdt.core.dom.MemberValuePair;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jst.ws.internal.jaxws.core.JAXWSCorePlugin;
import org.eclipse.ltk.core.refactoring.TextFileChange;
import org.eclipse.text.edits.MultiTextEdit;
import org.eclipse.text.edits.TextEdit;
import org.eclipse.text.edits.TextEditGroup;

/**
 * @author sclarke
 * 
 */
public final class AnnotationUtils {
    public static final String WEB_SERVICE = "WebService"; //$NON-NLS-1$
    public static final String WEB_METHOD = "WebMethod"; //$NON-NLS-1$
    public static final String WEB_PARAM = "WebParam"; //$NON-NLS-1$
    public static final String REQUEST_WRAPPER = "RequestWrapper"; //$NON-NLS-1$
    public static final String RESPONSE_WRAPPER = "ResponseWrapper"; //$NON-NLS-1$
    
    private static final String FORMATTER_INSERT_SPACE_BEFORE_ASSIGNMENT_OPERATOR 
            = "org.eclipse.jdt.core.formatter.insert_space_before_assignment_operator"; //$NON-NLS-1$
    private static final String FORMATTER_INSERT_SPACE_AFTER_ASSIGNMENT_OPERATOR 
            = "org.eclipse.jdt.core.formatter.insert_space_after_assignment_operator"; //$NON-NLS-1$
    private static final String DO_NOT_INSERT 
            = "do not insert"; //$NON-NLS-1$
    private AnnotationUtils() {
    }

    /*
    public static void getWebServiceAnnotationChange(IType type, 
    		TextFileChange textFileChange) throws CoreException {
        ICompilationUnit source = type.getCompilationUnit();

        CompilationUnit compilationUnit = getASTParser(source);
        
        AST ast = compilationUnit.getAST();
        ASTRewrite rewriter = ASTRewrite.create(ast);
        
        String typeName = type.getElementName();
        String packageName = type.getPackageFragment().getElementName();

        List<MemberValuePair> memberValuePairs = new ArrayList<MemberValuePair>();

        MemberValuePair nameValuePair = AnnotationsCore.getNameValuePair(ast, typeName);

//        MemberValuePair targetNamespaceValuePair = AnnotationsCore.getTargetNamespaceValuePair(
//                compilationUnit, model.getTargetNamespace());

        MemberValuePair portNameValuePair = AnnotationsCore.getPortNameValuePair(ast, typeName
                + "Port"); //$NON-NLS-1$

        MemberValuePair serviceNameValuePair = AnnotationsCore.getServiceNameValuePair(ast,
                typeName + "Service"); //$NON-NLS-1$

//        if (model.isUseServiceEndpointInterface()) {
//            try {
//                if (type.isInterface()) {
//                    memberValuePairs.add(nameValuePair);
//                    memberValuePairs.add(targetNamespaceValuePair);
//                } else if (type.isClass()) {
//                    MemberValuePair endpointInterfaceValuePair = AnnotationsCore
//                            .getEndpointInterfaceValuePair(compilationUnit, model
//                                    .getFullyQualifiedJavaInterfaceName());
//                    memberValuePairs.add(endpointInterfaceValuePair);
//                    memberValuePairs.add(portNameValuePair);
//                    memberValuePairs.add(serviceNameValuePair);
//                    
//                    if  (packageName == null || packageName.length() == 0) {
//                        memberValuePairs.add(targetNamespaceValuePair);
//                    }
//                    
//                }
//            } catch (JavaModelException jme) {
//                JAXWSCorePlugin.log(jme.getStatus());
//            }
//        } else {
            memberValuePairs.add(nameValuePair);
//            memberValuePairs.add(targetNamespaceValuePair);
            memberValuePairs.add(portNameValuePair);
            memberValuePairs.add(serviceNameValuePair);
//        }

        Annotation annotation = AnnotationsCore.getAnnotation(ast, WebService.class, memberValuePairs);
        AnnotationUtils.createTypeAnnotationChange(source, compilationUnit, rewriter, annotation, 
        		textFileChange);
    }*/

    @SuppressWarnings("unchecked")
    public static void getImportChange(CompilationUnit compilationUnit, 
            Class<? extends java.lang.annotation.Annotation> annotation, TextFileChange textFileChange, 
            boolean annotate) {
        try {
            String qualifiedName = annotation.getCanonicalName();
            ImportRewrite importRewrite = ImportRewrite.create(compilationUnit, true);
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
                    TextEdit importTextEdit = importRewrite.rewriteImports(null);
                    textFileChange.addEdit(importTextEdit);
            }
        } catch (CoreException ce) {
            JAXWSCorePlugin.log(ce.getStatus());
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
        ITextFileBufferManager bufferManager = FileBuffers.getTextFileBufferManager();
        try {
            if (packageDeclaration != null && !isAnnotationPresent(packageDeclaration, annotation)) {
                bufferManager.connect(path, LocationKind.IFILE, monitor);
                IDocument document = bufferManager.getTextFileBuffer(path, LocationKind.IFILE).getDocument();

                ListRewrite listRewrite = rewriter.getListRewrite(packageDeclaration,
                        PackageDeclaration.ANNOTATIONS_PROPERTY);

                listRewrite.insertFirst(annotation, null);

                TextEdit annotationTextEdit = rewriter.rewriteAST(document, source.getJavaProject()
                        .getOptions(true));

                textFileChange.addEdit(annotationTextEdit);

                textFileChange.addTextEditGroup(new TextEditGroup("AA", new //$NON-NLS-1$
                        TextEdit[] { annotationTextEdit }));
            }
        } catch (CoreException ce) {
            JAXWSCorePlugin.log(ce.getStatus());
        } finally {
            bufferManager.disconnect(path, LocationKind.IFILE, monitor);
        }
    }
    
    @SuppressWarnings("unchecked")
    public static void removeAnnotationFromPackageDeclaration(ICompilationUnit source,
            PackageDeclaration packageDeclaration, ASTRewrite rewriter, Annotation annotation,
            TextFileChange textFileChange) throws CoreException {
        IProgressMonitor monitor = new NullProgressMonitor();
        IPath path = source.getResource().getFullPath();
        ITextFileBufferManager bufferManager = FileBuffers.getTextFileBufferManager();
        try {
            if (packageDeclaration != null && isAnnotationPresent(packageDeclaration, annotation)) {
                bufferManager.connect(path, LocationKind.IFILE, monitor);
                IDocument document = bufferManager.getTextFileBuffer(path, LocationKind.IFILE).getDocument();

                ListRewrite listRewrite = rewriter.getListRewrite(packageDeclaration,
                        PackageDeclaration.ANNOTATIONS_PROPERTY);

                List originalList = listRewrite.getOriginalList();
                for (Object object : originalList) {
                    if (object instanceof Annotation && compareAnnotations((Annotation) object, annotation)) {
                        listRewrite.remove((Annotation) object, null);
                    }
                }

                TextEdit annotationTextEdit = rewriter.rewriteAST(document, source.getJavaProject()
                        .getOptions(true));

                textFileChange.addEdit(annotationTextEdit);

                textFileChange.addTextEditGroup(new TextEditGroup("AA", new //$NON-NLS-1$
                        TextEdit[] { annotationTextEdit }));
            }
        } catch (CoreException ce) {
            JAXWSCorePlugin.log(ce.getStatus());
        } finally {
            bufferManager.disconnect(path, LocationKind.IFILE, monitor);
        }
    }
    
    @SuppressWarnings("unchecked")
    public static void createTypeAnnotationChange(ICompilationUnit source, CompilationUnit 
            compilationUnit, ASTRewrite rewriter, Annotation annotation, 
            TextFileChange textFileChange) throws CoreException {
        IProgressMonitor monitor = new NullProgressMonitor();
        IPath path = source.getResource().getFullPath();
        ITextFileBufferManager bufferManager = FileBuffers.getTextFileBufferManager();
        try {
            IType type = compilationUnit.getTypeRoot().findPrimaryType();
            List<TypeDeclaration> types = compilationUnit.types();
            for (AbstractTypeDeclaration abstractTypeDeclaration : types) {
                if (compareTypes(abstractTypeDeclaration, type)
                        && !isAnnotationPresent(abstractTypeDeclaration, annotation)) {
                    bufferManager.connect(path, LocationKind.IFILE, monitor);
                    IDocument document = bufferManager.getTextFileBuffer(path, LocationKind.IFILE)
                            .getDocument();

                    ListRewrite listRewrite = rewriter.getListRewrite(abstractTypeDeclaration,
                            getChildListPropertyDescriptorForType(abstractTypeDeclaration));
                    
                    listRewrite.insertFirst(annotation, null);

                    TextEdit annotationTextEdit = rewriter.rewriteAST(document, source.getJavaProject()
                            .getOptions(true));

                    textFileChange.addEdit(annotationTextEdit);

                    textFileChange.addTextEditGroup(new TextEditGroup("AA", new //$NON-NLS-1$
                            TextEdit[] { annotationTextEdit }));
                }
            }
        } catch (CoreException ce) {
            JAXWSCorePlugin.log(ce.getStatus());
        } finally {
            bufferManager.disconnect(path, LocationKind.IFILE, monitor);
        }
    }

    @SuppressWarnings("unchecked")
    public static void removeAnnotationFromType(ICompilationUnit source, CompilationUnit 
            compilationUnit, ASTRewrite rewriter, Annotation annotation, 
            TextFileChange textFileChange) throws CoreException {
        IProgressMonitor monitor = new NullProgressMonitor();
        IPath path = source.getResource().getFullPath();
        ITextFileBufferManager bufferManager = FileBuffers.getTextFileBufferManager();
        try {
            IType type = compilationUnit.getTypeRoot().findPrimaryType();
            List<TypeDeclaration> types = compilationUnit.types();
            for (AbstractTypeDeclaration abstractTypeDeclaration : types) {
                if (compareTypes(abstractTypeDeclaration, type) 
                        && isAnnotationPresent(abstractTypeDeclaration, annotation)) {
                    bufferManager.connect(path, LocationKind.IFILE, monitor);
                    IDocument document = bufferManager.getTextFileBuffer(path, LocationKind.IFILE)
                            .getDocument();

                    ListRewrite listRewrite = rewriter.getListRewrite(abstractTypeDeclaration,
                            getChildListPropertyDescriptorForType(abstractTypeDeclaration));

                    List originalList = listRewrite.getOriginalList();
                    for (Object object : originalList) {
                        if (object instanceof Annotation && compareAnnotations((Annotation)object, 
                                annotation)) {
                            listRewrite.remove((Annotation)object, null);
                        }
                    }
                    
                    TextEdit annotationTextEdit = rewriter.rewriteAST(document, source.getJavaProject()
                            .getOptions(true));
                     
                    textFileChange.addEdit(annotationTextEdit);

                    textFileChange.addTextEditGroup(new TextEditGroup("AA", new  //$NON-NLS-1$
                             TextEdit[] {annotationTextEdit}));
                }
            }
        } catch (CoreException ce) {
            JAXWSCorePlugin.log(ce.getStatus());
        } finally {
            bufferManager.disconnect(path, LocationKind.IFILE, monitor);
        }
    }
    
    @SuppressWarnings("unchecked")
    public static void createMethodAnnotationChange(ICompilationUnit source, CompilationUnit 
            compilationUnit, ASTRewrite rewriter, IMethod method, Annotation annotation, 
            TextFileChange textFileChange) throws CoreException {
        IProgressMonitor monitor = new NullProgressMonitor();
        IPath path = source.getResource().getFullPath();
        ITextFileBufferManager bufferManager = FileBuffers.getTextFileBufferManager();
        try {
            IType type = method.getDeclaringType();
            List<AbstractTypeDeclaration> types = compilationUnit.types();
            for (AbstractTypeDeclaration abstractTypeDeclaration : types) {
                if (compareTypes(abstractTypeDeclaration, type)) {
                    List<BodyDeclaration> bodyDeclarations = JDTUtils
                            .getBodyDeclarationsForType(abstractTypeDeclaration);
                    BodyDeclaration bodyDeclaration = getMethodDeclaration(bodyDeclarations, method);
                    if (!isAnnotationPresent(bodyDeclaration, annotation)) {
                        bufferManager.connect(path, LocationKind.IFILE, monitor);
                        IDocument document = bufferManager.getTextFileBuffer(path, LocationKind.IFILE)
                                .getDocument();
                        
                        ListRewrite listRewrite = rewriter.getListRewrite(bodyDeclaration,
                                getChildListPropertyDescriptorForMethod(bodyDeclaration));
                        listRewrite.insertAt(annotation, 0, null);

                        TextEdit annotationTextEdit = rewriter.rewriteAST(document, source.getJavaProject()
                                .getOptions(true));

                        textFileChange.addEdit(annotationTextEdit);
                        textFileChange.addTextEditGroup(new TextEditGroup("AA", new //$NON-NLS-1$
                                TextEdit[] { annotationTextEdit }));
                    }
                }
            }
        } catch (CoreException ce) {
            JAXWSCorePlugin.log(ce.getStatus());
        } finally {
            bufferManager.disconnect(path, LocationKind.IFILE, monitor);
        }
    }
    
    @SuppressWarnings("unchecked")
    public static void removeAnnotationFromMethod(ICompilationUnit source, CompilationUnit 
            compilationUnit, ASTRewrite rewriter, IMethod method, Annotation annotation, 
            TextFileChange textFileChange) throws CoreException {
        IProgressMonitor monitor = new NullProgressMonitor();
        IPath path = source.getResource().getFullPath();
        ITextFileBufferManager bufferManager = FileBuffers.getTextFileBufferManager();
        try {
            IType type = method.getDeclaringType();
            List<AbstractTypeDeclaration> types = compilationUnit.types();
            for (AbstractTypeDeclaration abstractTypeDeclaration : types) {
                if (compareTypes(abstractTypeDeclaration, type)) {
                    List<BodyDeclaration> bodyDeclarations = JDTUtils
                            .getBodyDeclarationsForType(abstractTypeDeclaration);
                    BodyDeclaration bodyDeclaration = getMethodDeclaration(bodyDeclarations, method);
                    if (isAnnotationPresent(bodyDeclaration, annotation)) {
                        bufferManager.connect(path, LocationKind.IFILE, monitor);
                        IDocument document = bufferManager.getTextFileBuffer(path, LocationKind.IFILE)
                                .getDocument();

                        ListRewrite listRewrite = rewriter.getListRewrite(bodyDeclaration,
                                getChildListPropertyDescriptorForMethod(bodyDeclaration));

                        List originalList = listRewrite.getOriginalList();
                        for (Object object : originalList) {
                            if (object instanceof Annotation
                                    && compareAnnotations((Annotation) object, annotation)) {
                                listRewrite.remove((Annotation) object, null);
                            }
                        }

                        TextEdit annotationTextEdit = rewriter.rewriteAST(document, source.getJavaProject()
                                .getOptions(true));

                        textFileChange.addEdit(annotationTextEdit);
                        textFileChange.addTextEditGroup(new TextEditGroup("AA", new //$NON-NLS-1$
                                TextEdit[] { annotationTextEdit }));
                    }
                }
            }
        } catch (CoreException ce) {
            JAXWSCorePlugin.log(ce.getStatus());
        } finally {
            bufferManager.disconnect(path, LocationKind.IFILE, monitor);
        }
    }

    @SuppressWarnings("unchecked")
    public static void createFiedlAnnotationChange(ICompilationUnit source, CompilationUnit 
            compilationUnit, ASTRewrite rewriter, IField field, Annotation annotation, 
            TextFileChange textFileChange) throws CoreException {
        IProgressMonitor monitor = new NullProgressMonitor();
        IPath path = source.getResource().getFullPath();
        ITextFileBufferManager bufferManager = FileBuffers.getTextFileBufferManager();
        try {
            IType type = field.getDeclaringType();
            List<AbstractTypeDeclaration> types = compilationUnit.types();
            for (AbstractTypeDeclaration abstractTypeDeclaration : types) {
                if (compareTypes(abstractTypeDeclaration, type)) {
                    List<BodyDeclaration> bodyDeclarations = JDTUtils
                            .getBodyDeclarationsForType(abstractTypeDeclaration);
                    FieldDeclaration fieldDeclaration = (FieldDeclaration) getFieldDeclaration(
                            bodyDeclarations, field);
                    if (!isAnnotationPresent(fieldDeclaration, annotation)) {
                        bufferManager.connect(path, LocationKind.IFILE, monitor);
                        IDocument document = bufferManager.getTextFileBuffer(path, LocationKind.IFILE)
                                .getDocument();

                        ListRewrite listRewrite = rewriter.getListRewrite(fieldDeclaration,
                                FieldDeclaration.MODIFIERS2_PROPERTY);
                        listRewrite.insertAt(annotation, 0, null);

                        TextEdit annotationTextEdit = rewriter.rewriteAST(document, source.getJavaProject()
                                .getOptions(true));

                        textFileChange.addEdit(annotationTextEdit);
                        textFileChange.addTextEditGroup(new TextEditGroup("AA", new //$NON-NLS-1$
                                TextEdit[] { annotationTextEdit }));
                    }
                }
            }
        } catch (CoreException ce) {
            JAXWSCorePlugin.log(ce.getStatus());
        } finally {
            bufferManager.disconnect(path, LocationKind.IFILE, monitor);
        }
    }
    
    @SuppressWarnings("unchecked")
    public static void removeAnnotationFromField(ICompilationUnit source, CompilationUnit 
            compilationUnit, ASTRewrite rewriter, IField field, Annotation annotation, 
            TextFileChange textFileChange) throws CoreException {
        IProgressMonitor monitor = new NullProgressMonitor();
        IPath path = source.getResource().getFullPath();
        ITextFileBufferManager bufferManager = FileBuffers.getTextFileBufferManager();
        try {
            IType type = field.getDeclaringType();
            List<AbstractTypeDeclaration> types = compilationUnit.types();
            for (AbstractTypeDeclaration abstractTypeDeclaration : types) {
                if (compareTypes(abstractTypeDeclaration, type)) {
                    List<BodyDeclaration> bodyDeclarations = JDTUtils
                            .getBodyDeclarationsForType(abstractTypeDeclaration);
                    FieldDeclaration fieldDeclaration = (FieldDeclaration) getFieldDeclaration(
                            bodyDeclarations, field);

                    if (isAnnotationPresent(fieldDeclaration, annotation)) {
                        bufferManager.connect(path, LocationKind.IFILE, monitor);
                        IDocument document = bufferManager.getTextFileBuffer(path, LocationKind.IFILE)
                                .getDocument();

                        ListRewrite listRewrite = rewriter.getListRewrite(fieldDeclaration,
                                FieldDeclaration.MODIFIERS2_PROPERTY);

                        List originalList = listRewrite.getOriginalList();
                        for (Object object : originalList) {
                            if (object instanceof Annotation
                                    && compareAnnotations((Annotation) object, annotation)) {
                                listRewrite.remove((Annotation) object, null);
                            }
                        }

                        TextEdit annotationTextEdit = rewriter.rewriteAST(document, source.getJavaProject()
                                .getOptions(true));

                        textFileChange.addEdit(annotationTextEdit);
                        textFileChange.addTextEditGroup(new TextEditGroup("AA", new //$NON-NLS-1$
                                TextEdit[] { annotationTextEdit }));
                    }
                }
            }
        } catch (CoreException ce) {
            JAXWSCorePlugin.log(ce.getStatus());
        } finally {
            bufferManager.disconnect(path, LocationKind.IFILE, monitor);
        }
    }
   
    @SuppressWarnings("unchecked")
    public static void createMethodParameterAnnotationChange(ICompilationUnit source, CompilationUnit 
            compilationUnit, ASTRewrite rewriter, SingleVariableDeclaration singleVariableDeclaration, 
            IMethod method, Annotation annotation, TextFileChange textFileChange) throws CoreException {
        IProgressMonitor monitor = new NullProgressMonitor();
        IPath path = source.getResource().getFullPath();
        ITextFileBufferManager bufferManager = FileBuffers.getTextFileBufferManager();
        try {
            IType type = method.getDeclaringType();
            List<AbstractTypeDeclaration> types = compilationUnit.types();
            for (AbstractTypeDeclaration abstractTypeDeclaration : types) {
                if (compareTypes(abstractTypeDeclaration, type)) {
                    List<BodyDeclaration> bodyDeclarations = JDTUtils
                            .getBodyDeclarationsForType(abstractTypeDeclaration);
                    MethodDeclaration methodDeclaration = (MethodDeclaration) getMethodDeclaration(
                            bodyDeclarations, method);

                    List<SingleVariableDeclaration> parameters = methodDeclaration.parameters();
                    for (SingleVariableDeclaration parameter : parameters) {
                        if (compareMethodParameters(parameter, singleVariableDeclaration)
                                && !isAnnotationPresent(parameter, annotation)) {
                            bufferManager.connect(path, LocationKind.IFILE, monitor);
                            IDocument document = bufferManager.getTextFileBuffer(path, LocationKind.IFILE)
                                    .getDocument();

                            ListRewrite listRewrite = rewriter.getListRewrite(parameter,
                                    SingleVariableDeclaration.MODIFIERS2_PROPERTY);

                            listRewrite.insertAt(annotation, -1, null);

                            TextEdit annotationTextEdit = rewriter.rewriteAST(document, source
                                    .getJavaProject().getOptions(true));

                            textFileChange.addEdit(annotationTextEdit);

                            textFileChange.addTextEditGroup(new TextEditGroup("AA", new //$NON-NLS-1$
                                    TextEdit[] { annotationTextEdit }));
                        }
                    }
                }
            }
        } catch (CoreException ce) {
            JAXWSCorePlugin.log(ce.getStatus());
        } finally {
            bufferManager.disconnect(path, LocationKind.IFILE, monitor);
        }        
    }
    
    public static void createMethodParameterAnnotationChange(ICompilationUnit source, ASTRewrite rewriter,
            SingleVariableDeclaration singleVariableDeclaration, Annotation annotation,
            TextFileChange textFileChange) throws CoreException {
        IProgressMonitor monitor = new NullProgressMonitor();
        IPath path = source.getResource().getFullPath();
        ITextFileBufferManager bufferManager = FileBuffers.getTextFileBufferManager();
        try {
            if (!isAnnotationPresent(singleVariableDeclaration, annotation)) {
                bufferManager.connect(path, LocationKind.IFILE, monitor);
                IDocument document = bufferManager.getTextFileBuffer(path, LocationKind.IFILE).getDocument();

                ListRewrite listRewrite = rewriter.getListRewrite(singleVariableDeclaration,
                        SingleVariableDeclaration.MODIFIERS2_PROPERTY);

                listRewrite.insertAt(annotation, -1, null);

                TextEdit annotationTextEdit = rewriter.rewriteAST(document, source.getJavaProject()
                        .getOptions(true));

                textFileChange.addEdit(annotationTextEdit);

                textFileChange.addTextEditGroup(new TextEditGroup("AA", new //$NON-NLS-1$
                        TextEdit[] { annotationTextEdit }));
            }
        } catch (CoreException ce) {
            JAXWSCorePlugin.log(ce.getStatus());
        } finally {
            bufferManager.disconnect(path, LocationKind.IFILE, monitor);
        }
    }
    
    @SuppressWarnings("unchecked")
    public static void removeAnnotationFromMethodParameter(ICompilationUnit source, ASTRewrite rewriter,
            SingleVariableDeclaration singleVariableDeclaration, Annotation annotation,
            TextFileChange textFileChange) throws CoreException {
        IProgressMonitor monitor = new NullProgressMonitor();
        IPath path = source.getResource().getFullPath();
        ITextFileBufferManager bufferManager = FileBuffers.getTextFileBufferManager();
        try {
            if (isAnnotationPresent(singleVariableDeclaration, annotation)) {
                bufferManager.connect(path, LocationKind.IFILE, monitor);
                IDocument document = bufferManager.getTextFileBuffer(path, LocationKind.IFILE).getDocument();

                ListRewrite listRewrite = rewriter.getListRewrite(singleVariableDeclaration,
                        SingleVariableDeclaration.MODIFIERS2_PROPERTY);

                List originalList = listRewrite.getOriginalList();
                for (Object object : originalList) {
                    if (object instanceof Annotation && compareAnnotations((Annotation)object, annotation)) {
                        listRewrite.remove((Annotation)object, null);
                    }
                }

                TextEdit annotationTextEdit = rewriter.rewriteAST(document, source.getJavaProject()
                        .getOptions(true));

                textFileChange.addEdit(annotationTextEdit);

                textFileChange.addTextEditGroup(new TextEditGroup("AA", new //$NON-NLS-1$
                        TextEdit[] { annotationTextEdit }));
            }
        } catch (CoreException ce) {
            JAXWSCorePlugin.log(ce.getStatus());
        } finally {
            bufferManager.disconnect(path, LocationKind.IFILE, monitor);
        }
    }

    @SuppressWarnings("unchecked")
    public static void createMemberVaulePairChange(ICompilationUnit source, CompilationUnit compilationUnit,
            ASTRewrite rewriter, IJavaElement javaElement, IAnnotation annotation, ASTNode memberValuePair,
            TextFileChange textFileChange) throws CoreException {
        IProgressMonitor monitor = new NullProgressMonitor();
        IPath path = source.getResource().getFullPath();
        ITextFileBufferManager bufferManager = FileBuffers.getTextFileBufferManager();
        try {
            List<IExtendedModifier> modifiers = getExtendedModifiers(compilationUnit, javaElement);
            for (IExtendedModifier extendedModifier : modifiers) {
                if (extendedModifier instanceof NormalAnnotation) {
                    NormalAnnotation existingAnnotation = (NormalAnnotation) extendedModifier;
                    if (AnnotationUtils.compareAnnotations(annotation, existingAnnotation)) {
                        bufferManager.connect(path, LocationKind.IFILE, monitor);
                        IDocument document = bufferManager.getTextFileBuffer(path, LocationKind.IFILE)
                                .getDocument();

                        ListRewrite listRewrite = rewriter.getListRewrite(existingAnnotation,
                                NormalAnnotation.VALUES_PROPERTY);

                        listRewrite.insertLast(memberValuePair, null);

                        Map options = source.getJavaProject().getOptions(true);
                        options.put(FORMATTER_INSERT_SPACE_BEFORE_ASSIGNMENT_OPERATOR, DO_NOT_INSERT);
                        options.put(FORMATTER_INSERT_SPACE_AFTER_ASSIGNMENT_OPERATOR, DO_NOT_INSERT);

                        TextEdit annotationTextEdit = rewriter.rewriteAST(document, options);

                        textFileChange.addEdit(annotationTextEdit);

                        textFileChange.addTextEditGroup(new TextEditGroup("AA", new //$NON-NLS-1$
                                TextEdit[] { annotationTextEdit }));
                    }
                }
            }
        } catch (CoreException ce) {
            JAXWSCorePlugin.log(ce.getStatus());
        } finally {
            bufferManager.disconnect(path, LocationKind.IFILE, monitor);
        }
    }
    
    @SuppressWarnings("unchecked")
    public static void createMemberVaulePairChange(ICompilationUnit source, CompilationUnit compilationUnit,
            ASTRewrite rewriter, NormalAnnotation annotation, ASTNode memberValuePair,
            TextFileChange textFileChange) throws CoreException {
        IProgressMonitor monitor = new NullProgressMonitor();
        IPath path = source.getResource().getFullPath();
        ITextFileBufferManager bufferManager = FileBuffers.getTextFileBufferManager();
        try {
            bufferManager.connect(path, LocationKind.IFILE, monitor);
            IDocument document = bufferManager.getTextFileBuffer(path, LocationKind.IFILE).getDocument();

            ListRewrite listRewrite = rewriter.getListRewrite(annotation,
                    NormalAnnotation.VALUES_PROPERTY);

            listRewrite.insertLast(memberValuePair, null);

            Map options = source.getJavaProject().getOptions(true);
            options.put(FORMATTER_INSERT_SPACE_BEFORE_ASSIGNMENT_OPERATOR, DO_NOT_INSERT);
            options.put(FORMATTER_INSERT_SPACE_AFTER_ASSIGNMENT_OPERATOR, DO_NOT_INSERT);

            TextEdit annotationTextEdit = rewriter.rewriteAST(document, options);

            textFileChange.addEdit(annotationTextEdit);

            textFileChange.addTextEditGroup(new TextEditGroup("AA", new //$NON-NLS-1$
                    TextEdit[] { annotationTextEdit }));
        } catch (CoreException ce) {
            JAXWSCorePlugin.log(ce.getStatus());
        } finally {
            bufferManager.disconnect(path, LocationKind.IFILE, monitor);
        }
    }

    @SuppressWarnings("unchecked")
    public static void updateMemberVaulePairValue(ICompilationUnit source, CompilationUnit compilationUnit,
            ASTRewrite rewriter, IJavaElement javaElement, IAnnotation annotation, IMemberValuePair memberValuePair,
            ASTNode value, TextFileChange textFileChange) throws CoreException {
        IProgressMonitor monitor = new NullProgressMonitor();
        IPath path = source.getResource().getFullPath();
        ITextFileBufferManager bufferManager = FileBuffers.getTextFileBufferManager();
        try {
            List<IExtendedModifier> modifiers = getExtendedModifiers(compilationUnit, javaElement);
            for (IExtendedModifier extendedModifier : modifiers) {
                if (extendedModifier instanceof NormalAnnotation) {
                    NormalAnnotation existingAnnotation = (NormalAnnotation) extendedModifier;
                    if (AnnotationUtils.compareAnnotations(annotation, existingAnnotation)) {
                        List<MemberValuePair> memberVaulePairs = existingAnnotation.values();
                        for (MemberValuePair memberValuePairDOM : memberVaulePairs) {
                            if (memberValuePairDOM.getName().toString().equals(
                                    memberValuePair.getMemberName())) {
                                bufferManager.connect(path, LocationKind.IFILE, monitor);
                                IDocument document = bufferManager
                                        .getTextFileBuffer(path, LocationKind.IFILE).getDocument();

                                rewriter.set(memberValuePairDOM, MemberValuePair.VALUE_PROPERTY, value, null);

                                TextEdit annotationTextEdit = rewriter.rewriteAST(document, source
                                        .getJavaProject().getOptions(true));

                                textFileChange.addEdit(annotationTextEdit);

                                textFileChange.addTextEditGroup(new TextEditGroup("AA", new //$NON-NLS-1$
                                        TextEdit[] { annotationTextEdit }));
                            }
                        }
                    }
                }
            }
        } catch (CoreException ce) {
            JAXWSCorePlugin.log(ce.getStatus());
        } finally {
            bufferManager.disconnect(path, LocationKind.IFILE, monitor);
        }
    }

    public static void updateMemberVaulePairValue(ICompilationUnit source, CompilationUnit compilationUnit,
            ASTRewrite rewriter, NormalAnnotation annotation, MemberValuePair memberValuePair,
            ASTNode value, TextFileChange textFileChange) throws CoreException {
        IProgressMonitor monitor = new NullProgressMonitor();
        IPath path = source.getResource().getFullPath();
        ITextFileBufferManager bufferManager = FileBuffers.getTextFileBufferManager();
        try {
            bufferManager.connect(path, LocationKind.IFILE, monitor);
            IDocument document = bufferManager.getTextFileBuffer(path, LocationKind.IFILE).getDocument();

            rewriter.set(memberValuePair, MemberValuePair.VALUE_PROPERTY, value, null);

            TextEdit annotationTextEdit = rewriter.rewriteAST(document, source.getJavaProject().getOptions(
                    true));

            textFileChange.addEdit(annotationTextEdit);

            textFileChange.addTextEditGroup(new TextEditGroup("AA", new //$NON-NLS-1$
                    TextEdit[] { annotationTextEdit }));
        } catch (CoreException ce) {
            JAXWSCorePlugin.log(ce.getStatus());
        } finally {
            bufferManager.disconnect(path, LocationKind.IFILE, monitor);
        }
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
        String annotationName = annotationTypeName.getFullyQualifiedName();
        if (annotationTypeName.isQualifiedName()) {
            annotationName = ((QualifiedName) annotationTypeName).getName().getFullyQualifiedName();
        }
        return annotationName;
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
					String simpleName1 = Signature.getSimpleName(Signature.toString(Signature
							.getTypeErasure(parametetNames[i])));
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
    public static boolean isAnnotationPresent(final IMethod method, final String annotationKey) {
        ICompilationUnit source = method.getCompilationUnit();
        CompilationUnit compilationUnit = getASTParser(source);
        final List<MethodDeclaration> methodDeclarations = new ArrayList<MethodDeclaration>();
        compilationUnit.accept(new ASTVisitor() {
            @Override
            public boolean visit(MethodDeclaration methodDeclaration) {
                if (methodDeclaration.getName().getIdentifier().equals(method.getElementName())) {
                    methodDeclarations.add(methodDeclaration);
                }
                return super.visit(methodDeclaration);
            }
        });

        for (MethodDeclaration methodDeclaration : methodDeclarations) {
            if (annotationKey.equals(AnnotationUtils.WEB_PARAM)) {
                List<SingleVariableDeclaration> parameters = methodDeclaration.parameters();
                for (SingleVariableDeclaration singleVariableDeclaration : parameters) {
                    if (compareModifiers(singleVariableDeclaration.modifiers(), annotationKey)) {
                        return true;
                    }
                }
            } else {
                 return compareModifiers(methodDeclaration.modifiers(), annotationKey);
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
        
        
        ICompilationUnit source = JDTUtils.getCompilationUnitFromFile((IFile)javaElement.getResource());
        int elementType = javaElement.getElementType();
        CompilationUnit compilationUnit = JDTUtils.getCompilationUnit(source);
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
    private static List<IExtendedModifier> getExtendedModifiers(CompilationUnit compilationUnit, 
            IJavaElement javaElement) {
        IType type = compilationUnit.getTypeRoot().findPrimaryType();
        List<AbstractTypeDeclaration> types = compilationUnit.types();
        for (AbstractTypeDeclaration abstractTypeDeclaration : types) {
            if (compareTypes(abstractTypeDeclaration, type)) {
                List<BodyDeclaration> bodyDeclarations = 
                        JDTUtils.getBodyDeclarationsForType(abstractTypeDeclaration);
                
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

                return bodyDeclaration.modifiers();
            }
        }
        return Collections.emptyList();
    }
    
    private static BodyDeclaration getMethodDeclaration(List<BodyDeclaration> bodyDeclarations, IMethod method) {
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
    
    private static boolean compareMethodParameters(SingleVariableDeclaration paramOne,
            SingleVariableDeclaration paramTwo) {
        String typeOne = paramOne.getType().toString();
        String nameOne = paramOne.getName().getIdentifier();

        String typeTwo = paramTwo.getType().toString();
        String nameTwo = paramTwo.getName().getIdentifier();

        return (typeOne.equals(typeTwo)) && (nameOne.equals(nameTwo));
    }

    private static boolean compareModifiers(List<IExtendedModifier> modifiers, String annotationKey) {
        for (IExtendedModifier extendedModifier : modifiers) {
            if (extendedModifier instanceof Annotation) {
                Annotation existingAnnotation = (Annotation) extendedModifier;
                String annotationName = AnnotationUtils.getAnnotationName(existingAnnotation);
                if (annotationName.equals(annotationKey)) {
                    return true;
                }
            }
        }
        return false;
    }
}
