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
package org.eclipse.jst.ws.internal.jaxws.ui.annotations.correction;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.jws.Oneway;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPMessageHandlers;
import javax.xml.ws.WebServiceProvider;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.apt.core.util.EclipseMessager;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MemberValuePair;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.internal.ui.JavaPluginImages;
import org.eclipse.jdt.internal.ui.viewsupport.JavaElementImageProvider;
import org.eclipse.jdt.ui.JavaElementImageDescriptor;
import org.eclipse.jdt.ui.text.java.IInvocationContext;
import org.eclipse.jdt.ui.text.java.IJavaCompletionProposal;
import org.eclipse.jdt.ui.text.java.IProblemLocation;
import org.eclipse.jdt.ui.text.java.IQuickFixProcessor;
import org.eclipse.jst.ws.annotations.core.utils.AnnotationUtils;
import org.eclipse.jst.ws.internal.jaxws.core.JAXWSCoreMessages;
import org.eclipse.jst.ws.internal.jaxws.ui.JAXWSUIMessages;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

@SuppressWarnings({ "restriction", "deprecation" })
public class JAXWSQuickFixProcessor implements IQuickFixProcessor {

    public boolean hasCorrections(ICompilationUnit unit, int problemId) {
        if (problemId == EclipseMessager.APT_QUICK_FIX_PROBLEM_ID) {
            return true;
        }
        return false;
    }

    public IJavaCompletionProposal[] getCorrections(IInvocationContext context, IProblemLocation[] locations)
        throws CoreException {

        if (locations == null || locations.length == 0) {
            return null;
        }

        List<IJavaCompletionProposal> proposals = new ArrayList<IJavaCompletionProposal>();

        Set<String> handled = new HashSet<String>();
        for (IProblemLocation problemLocation : locations) {
            String[] problemArguments = problemLocation.getProblemArguments();
            if (problemArguments.length >= 2) {
                String problem = problemArguments[1];
                if (handled.add(problem)) {
                    process(context, problemLocation, proposals);
                }
            }
        }

        return proposals.toArray(new IJavaCompletionProposal[proposals.size()]);
    }

    private void process(IInvocationContext context, IProblemLocation problemLocation,
            List<IJavaCompletionProposal> proposals) throws CoreException {

        String problem = problemLocation.getProblemArguments()[1];

        if (problem.equals(JAXWSCoreMessages.WEBSERVICE_ENPOINTINTERFACE_MUST_IMPLEMENT)) {
            addUnimplementedMethodsProposal(context, problemLocation, proposals);
        }

        if (problem.equals(JAXWSCoreMessages.WEBMETHOD_ONLY_SUPPORTED_ON_CLASSES_WITH_WEBSERVICE)) {
            addAnnotationToTypeProposal(context, proposals, WebService.class);
        }

        if (problem.equals(JAXWSCoreMessages.WEBSERVICE_ENPOINTINTERFACE_NO_NAME_ATTRIBUTE)
                || problem.equals(JAXWSCoreMessages.WEBSERVICE_PORTNAME_SEI)
                || problem.equals(JAXWSCoreMessages.WEBSERVICE_SERVICENAME_SEI)
                || problem.equals(JAXWSCoreMessages.WEBSERVICE_ENDPOINTINTERFACE_SEI)
                || problem.equals(JAXWSCoreMessages.WEBMETHOD_EXCLUDE_NOT_ALLOWED_ON_SEI)) {
            addRemoveMemberValuePairProposal(context, problemLocation, proposals, false);
        }

        if (problem.equals(JAXWSCoreMessages.WEBMETHOD_EXCLUDE_SPECIFIED_NO_OTHER_ATTRIBUTES_ALLOWED)) {
            addRemoveMemberValuePairProposal(context, problemLocation, proposals, true);
        }

        if (problem.equals(JAXWSCoreMessages.WEBSERVICE_ENPOINTINTERFACE_NO_WEBMETHODS)) {
            addRemoveAnnotationProposal(context, problemLocation, proposals, WebMethod.class);
        }

        if (problem.equals(JAXWSCoreMessages.WEBSERVICE_ENPOINTINTERFACE_NO_SOAPBINDING)
                || problem.equals(JAXWSCoreMessages.SOAPBINDING_NO_RPC_STYLE_ON_METHODS)) {
            addRemoveAnnotationProposal(context, problemLocation, proposals, SOAPBinding.class);
        }

        if (problem.equals(JAXWSCoreMessages.WEBSERVICE_ENPOINTINTERFACE_NO_WEBRESULT)) {
            addRemoveAnnotationProposal(context, problemLocation, proposals, WebResult.class);
        }

        if (problem.equals(JAXWSCoreMessages.WEBSERVICE_ENPOINTINTERFACE_NO_WEBPARAM)) {
            addRemoveAnnotationProposal(context, problemLocation, proposals, WebParam.class);
        }

        if (problem.equals(JAXWSCoreMessages.WEBSERVICE_ENPOINTINTERFACE_NO_ONEWAY)) {
            addRemoveAnnotationProposal(context, problemLocation, proposals, Oneway.class);
        }

        if (problem.equals(JAXWSCoreMessages.WEBSERVICE_PUBLIC_ABSTRACT_FINAL)) {
            addChangeModifierProposal(context, problemLocation, proposals, 5);
        }

        if (problem.equals(JAXWSCoreMessages.WEBSERVICE_DEFAULT_PUBLIC_CONSTRUCTOR)) {
            addConstructorProposal(context, problemLocation, proposals, 5);
        }

        if (problem.equals(JAXWSCoreMessages.WEBSERVICE_WEBSERVICEPROVIDER_COMBINATION)) {
            addRemoveAnnotationProposal(context, problemLocation, proposals, WebServiceProvider.class);
        }

        if (problem.equals(JAXWSCoreMessages.HANDLER_CHAIN_SOAP_MESSAGE_HANDLERS)) {
            addRemoveAnnotationProposal(context, problemLocation, proposals, SOAPMessageHandlers.class);
        }

        if (problem.equals(JAXWSCoreMessages.WEBSERVICE_OVERRIDE_FINALIZE)) {
            addRemoveMethodProposal(context, problemLocation, proposals);
        }

        if (problem.equals(JAXWSCoreMessages.WEBMETHOD_ONLY_ON_PUBLIC_METHODS)
                || problem.equals(JAXWSCoreMessages.WEBMETHOD_NO_STATIC_MODIFIER_ALLOWED)
                || problem.equals(JAXWSCoreMessages.WEBMETHOD_NO_FINAL_MODIFIER_ALLOWED)) {
            addChangeModifierProposal(context, problemLocation, proposals, 5);
            addRemoveAnnotationProposal(context, problemLocation, proposals, WebMethod.class);
        }

        if (problem.equals(JAXWSCoreMessages.WEBSERVICE_ENPOINTINTERFACE_REDUCED_VISIBILITY)) {
            addChangeModifierProposal(context, problemLocation, proposals, 5);
        }

        if (problem.equals(JAXWSCoreMessages.WEBSERVICE_ENPOINTINTERFACE_INCOMPATIBLE_RETURN_TYPE)) {
            addChangeReturnTypeProposal(context, problemLocation, proposals);
        }
    }

    private void addAnnotationToTypeProposal(IInvocationContext context, List<IJavaCompletionProposal> proposals,
            Class<? extends java.lang.annotation.Annotation> annotationClass) {

        String displayString = JAXWSUIMessages.bind(JAXWSUIMessages.ADD_ANNOTATION, annotationClass.getSimpleName());
        Image image = JavaPluginImages.get(JavaPluginImages.IMG_CORRECTION_CHANGE);
        proposals.add(new AddAnnotationToTypeCorrectionProposal(context, annotationClass, displayString, 5, image));
    }

    private void addRemoveMethodProposal(IInvocationContext context, IProblemLocation problemLocation,
            List<IJavaCompletionProposal> proposals) {

        ASTNode selectedNode = problemLocation.getCoveringNode(context.getASTRoot());

        if (selectedNode.getParent() instanceof NormalAnnotation) {
            NormalAnnotation normalAnnotation = (NormalAnnotation) selectedNode.getParent();
            ASTNode parentNode = normalAnnotation.getParent();
            if (parentNode instanceof MethodDeclaration) {
                selectedNode = ((MethodDeclaration) parentNode).getName();
            }
            if (parentNode instanceof SingleVariableDeclaration) {
                SingleVariableDeclaration singleVariableDeclaration = (SingleVariableDeclaration) parentNode;
                selectedNode = ((MethodDeclaration) singleVariableDeclaration.getParent()).getName();
            }
        }

        if (selectedNode instanceof SimpleName) {
            SimpleName methodName = (SimpleName) selectedNode;
            String displayString = JAXWSUIMessages.bind(JAXWSUIMessages.REMOVE_METHOD, methodName.getIdentifier());
            Image image = PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_TOOL_DELETE);

            proposals.add(new RemoveMethodCorrectionProposal(context, methodName, displayString, 5, image));
        }
    }

    private void addRemoveAnnotationProposal(IInvocationContext context, IProblemLocation problemLocation,
            List<IJavaCompletionProposal> proposals, Class<? extends java.lang.annotation.Annotation> annotation) {

        ASTNode coveringNode = problemLocation.getCoveringNode(context.getASTRoot());
        ASTNode parentNode = coveringNode.getParent();

        String displayString = JAXWSUIMessages.bind(JAXWSUIMessages.REMOVE_ANNOTATION, annotation.getSimpleName());
        Image image = PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_TOOL_DELETE);
        proposals.add(new RemoveAnnotationCorrectionProposal(context, annotation, parentNode, displayString, 5, image));
    }

    private void addRemoveMemberValuePairProposal(IInvocationContext context, IProblemLocation problemLocation,
            List<IJavaCompletionProposal> proposals, boolean removeAllOtherMVPs) {

        ASTNode coveringNode = problemLocation.getCoveringNode(context.getASTRoot());

        if (coveringNode.getParent() instanceof MemberValuePair) {
            MemberValuePair memberValuePair = (MemberValuePair) coveringNode.getParent();
            String displayString = JAXWSUIMessages.bind(JAXWSUIMessages.REMOVE_MEMBERVALUEPAIR, memberValuePair
                    .toString());

            if (removeAllOtherMVPs) {
                displayString = JAXWSUIMessages.REMOVE_ALL_OTHER_MEMBERVALUEPAIRS;
            }
            Image image = PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_TOOL_DELETE);
            proposals.add(new RemoveMemberValuePairCorrectionProposal(context, memberValuePair,
                    removeAllOtherMVPs, displayString, 5, image));
        }
    }

    private void addUnimplementedMethodsProposal(IInvocationContext context, IProblemLocation problemLocation,
            List<IJavaCompletionProposal> proposals) {

        ASTNode endpointInterfaceValue = problemLocation.getCoveringNode(context.getASTRoot());
        if (endpointInterfaceValue instanceof StringLiteral) {
            ASTNode endpointInterfaceMVP = endpointInterfaceValue.getParent();
            ASTNode webServiceAnnotation = endpointInterfaceMVP.getParent();
            ASTNode typeDeclaration = webServiceAnnotation.getParent();

            String endpointInterface = ((StringLiteral) endpointInterfaceValue).getLiteralValue();

            Image image = JavaPluginImages.get(JavaPluginImages.IMG_CORRECTION_CHANGE);
            proposals.add(new AddUnimplementedMethodCorrectionProposal(context, typeDeclaration, endpointInterface,
                    JAXWSUIMessages.ADD_UNIMPLEMENTED_METHODS, 5, image));
        }
    }

    private void addChangeModifierProposal(IInvocationContext context, IProblemLocation problemLocation,
            List<IJavaCompletionProposal> proposals, int relevance) {

        CompilationUnit astRoot = context.getASTRoot();
        ASTNode selectedNode = problemLocation.getCoveringNode(astRoot);

        if (!(selectedNode instanceof SimpleName)) {
            return;
        }

        IBinding binding = ((SimpleName) selectedNode).resolveBinding();
        if (binding != null) {
            String bindingName = binding.getName();
            String displayString = "";

            int excludedModifiers = 0;
            int includedModifiers = 0;

            String problem = problemLocation.getProblemArguments()[1];

            if(problem.equals(JAXWSCoreMessages.WEBMETHOD_ONLY_ON_PUBLIC_METHODS)
                    || problem.equals(JAXWSCoreMessages.WEBSERVICE_ENPOINTINTERFACE_REDUCED_VISIBILITY)) {
                excludedModifiers = ~(Modifier.PUBLIC);
                includedModifiers = Modifier.PUBLIC ;
                displayString = JAXWSUIMessages.bind(JAXWSUIMessages.CHANGE_METHOD_VISIBILITY, "public"); //$NON-NLS-1$
            }

            if(problem.equals(JAXWSCoreMessages.WEBMETHOD_NO_FINAL_MODIFIER_ALLOWED)) {
                excludedModifiers = Modifier.FINAL;
                displayString = JAXWSUIMessages.bind(JAXWSUIMessages.REMOVE_FINAL_MODIFIER, bindingName);
            }

            if(problem.equals(JAXWSCoreMessages.WEBMETHOD_NO_STATIC_MODIFIER_ALLOWED)) {
                excludedModifiers = Modifier.STATIC;
                displayString = JAXWSUIMessages.bind(JAXWSUIMessages.REMOVE_STATIC_MODIFIER, bindingName);
            }

            if (problem.equals(JAXWSCoreMessages.WEBSERVICE_PUBLIC_ABSTRACT_FINAL)) {
                excludedModifiers = Modifier.ABSTRACT | Modifier.FINAL;
                displayString = JAXWSUIMessages.REMOVE_ILLEGAL_MODIFIER;
            }

            Image image = JavaPluginImages.get(JavaPluginImages.IMG_CORRECTION_CHANGE);
            proposals.add(new ChangeModifierCorrectionProposal(context, binding, includedModifiers,
                    excludedModifiers, displayString, 5, image));
        }
    }

    private void addConstructorProposal(IInvocationContext context, IProblemLocation problemLocation,
            List<IJavaCompletionProposal> proposals, int relevance) {

        CompilationUnit astRoot = context.getASTRoot();
        ASTNode selectedNode = problemLocation.getCoveringNode(astRoot);

        ITypeBinding typeBinding = ((AbstractTypeDeclaration) selectedNode.getParent()).resolveBinding();
        if (typeBinding != null && typeBinding.isFromSource()) {
            String displayString = JAXWSUIMessages.bind(JAXWSUIMessages.CREATE_CONSTRUCTOR,
                    typeBinding.getTypeDeclaration().getName());

            Image image = JavaElementImageProvider.getDecoratedImage(JavaPluginImages.DESC_MISC_PUBLIC,
                    JavaElementImageDescriptor.CONSTRUCTOR, JavaElementImageProvider.SMALL_SIZE);

            proposals.add(new NewDefaultConstructorCorrectionProposal(context, typeBinding, displayString, 5,
                    image));
        }
    }

    private void addChangeReturnTypeProposal(IInvocationContext context, IProblemLocation problemLocation,
            List<IJavaCompletionProposal> proposals) {

        CompilationUnit astRoot = context.getASTRoot();
        ASTNode selectedNode = problemLocation.getCoveringNode(astRoot);

        if (!(selectedNode instanceof SimpleName)) {
            return;
        }

        MethodDeclaration implMethodDeclaration = (MethodDeclaration)((SimpleName) selectedNode).getParent();
        TypeDeclaration implTypeDeclaration = (TypeDeclaration)implMethodDeclaration.getParent();

        Annotation annotation = AnnotationUtils.getAnnotation(implTypeDeclaration.resolveBinding().getJavaElement(), WebService.class);

        String endpointInterface = AnnotationUtils.getStringValue(annotation, "endpointInterface"); //$NON-NLS-1$
        String displayString = JAXWSUIMessages.CHANGE_METHOD_RETURN_TYPE;
        Image image = JavaPluginImages.get(JavaPluginImages.IMG_CORRECTION_CHANGE);
        proposals.add(new ChangeReturnTypeCorrectionProposal(context, implTypeDeclaration,
                implMethodDeclaration, endpointInterface, displayString, 5, image));
    }
}
