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
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.internal.corext.fix.IProposableFix;
import org.eclipse.jdt.internal.corext.fix.UnimplementedCodeFix;
import org.eclipse.jdt.internal.corext.fix.CompilationUnitRewriteOperationsFix.CompilationUnitRewriteOperation;
import org.eclipse.jdt.internal.ui.JavaPluginImages;
import org.eclipse.jdt.internal.ui.fix.UnimplementedCodeCleanUp;
import org.eclipse.jdt.internal.ui.text.correction.proposals.ASTRewriteCorrectionProposal;
import org.eclipse.jdt.internal.ui.text.correction.proposals.FixCorrectionProposal;
import org.eclipse.jdt.internal.ui.text.correction.proposals.ModifierChangeCorrectionProposal;
import org.eclipse.jdt.internal.ui.text.correction.proposals.NewMethodCorrectionProposal;
import org.eclipse.jdt.internal.ui.text.correction.proposals.RemoveDeclarationCorrectionProposal;
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
            String problem = problemLocation.getProblemArguments()[1];
            if (handled.add(problem)) {
                process(context, problemLocation, proposals);
            }
        }
        
        return (IJavaCompletionProposal[]) proposals.toArray(new IJavaCompletionProposal[proposals.size()]);
    }

    private void process(IInvocationContext context, IProblemLocation problemLocation, 
            List<IJavaCompletionProposal> proposals) throws CoreException {
        
        String problem = problemLocation.getProblemArguments()[1];

        if (problem.equals(JAXWSCoreMessages.WEBSERVICE_ENPOINTINTERFACE_MUST_IMPLEMENT)) {
            addUnimplementedMethodsProposals(context, problemLocation, proposals);
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

        if (problem.equals(JAXWSCoreMessages.WEBSERVICE_ENPOINTINTERFACE_NO_SOAPBINDING)) {
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
    }

    private void addAnnotationToTypeProposal(IInvocationContext context, List<IJavaCompletionProposal> proposals, 
            Class<? extends java.lang.annotation.Annotation> annotationClass) {
        
        String label = JAXWSUIMessages.bind(JAXWSUIMessages.ADD_ANNOTATION, annotationClass.getSimpleName());
        Image image = JavaPluginImages.get(JavaPluginImages.IMG_CORRECTION_CHANGE);
        proposals.add(new AddAnnotationToTypeProposal(context.getASTRoot(), label, context.getCompilationUnit(),
                annotationClass, 5, image));
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
            proposals.add(new RemoveDeclarationCorrectionProposal(context.getCompilationUnit(),
                    (SimpleName) selectedNode, 5));
        }
    }

    private void addRemoveAnnotationProposal(IInvocationContext context, IProblemLocation problemLocation, 
            List<IJavaCompletionProposal> proposals, Class<? extends java.lang.annotation.Annotation> annotation) {
		
        ASTNode coveringNode = problemLocation.getCoveringNode(context.getASTRoot());
        ASTNode parentNode = coveringNode.getParent();
        
        ASTRewrite astRewrite = ASTRewrite.create(parentNode.getAST());

        if (parentNode instanceof Annotation) {
            Annotation jdtDomAnnotation = (Annotation) parentNode;
            astRewrite.remove(jdtDomAnnotation, null);
        }
        
		if (parentNode instanceof MethodDeclaration) {
			MethodDeclaration methodDeclaration = (MethodDeclaration) parentNode;
			Annotation jdtDomAnnotation = AnnotationUtils.getAnnotation(methodDeclaration, annotation);
			if (jdtDomAnnotation != null) {
			    astRewrite.remove(jdtDomAnnotation, null);
			}
		}
		
		if (parentNode.getParent() instanceof TypeDeclaration) {
		    TypeDeclaration typeDeclaration = (TypeDeclaration) parentNode.getParent();
            Annotation jdtDomAnnotation = AnnotationUtils.getAnnotation(typeDeclaration, annotation);
            if (jdtDomAnnotation != null) {
                astRewrite.remove(jdtDomAnnotation, null);
            }
		}

		String label = JAXWSUIMessages.bind(JAXWSUIMessages.REMOVE_ANNOTATION, annotation.getSimpleName());
        Image image = PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_TOOL_DELETE);
        proposals.add(new ASTRewriteCorrectionProposal(label, context.getCompilationUnit(), astRewrite, 5,
                image));
    }

    @SuppressWarnings("unchecked")
	private void addRemoveMemberValuePairProposal(IInvocationContext context, IProblemLocation problemLocation,
	        List<IJavaCompletionProposal> proposals, boolean removeAllOtherMVPs) {
	    
		ASTNode coveringNode = problemLocation.getCoveringNode(context.getASTRoot());
		
		if (coveringNode.getParent() instanceof MemberValuePair) {
		    MemberValuePair memberValuePair = (MemberValuePair) coveringNode.getParent();
		    if (memberValuePair.getParent() instanceof NormalAnnotation) {
		        String label = "";
		        
		        NormalAnnotation annotation = (NormalAnnotation) memberValuePair.getParent();

		        ASTRewrite astRewrite = ASTRewrite.create(annotation.getAST());
		        if (removeAllOtherMVPs) {
                    List<MemberValuePair> memberValuePairs = annotation.values();
                    for (MemberValuePair otherMemberValuePair : memberValuePairs) {
                        if (!otherMemberValuePair.equals(memberValuePair)) {
                            astRewrite.remove(otherMemberValuePair, null);
                        }
                    }
                    label = JAXWSUIMessages.REMOVE_ALL_OTHER_MEMBERVALUEPAIRS;
                } else {
                    astRewrite.remove(memberValuePair, null);
     	            label = JAXWSUIMessages.bind(JAXWSUIMessages.REMOVE_MEMBERVALUEPAIR,
     	                    memberValuePair.toString());
                }
     	        Image image = PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_TOOL_DELETE);
	            proposals.add(new ASTRewriteCorrectionProposal(label, context.getCompilationUnit(),
	                    astRewrite, 5, image));
		    }
		}		
	}

    private void addUnimplementedMethodsProposals(IInvocationContext context, IProblemLocation problemLocation, 
            List<IJavaCompletionProposal> proposals) {
        IProposableFix unimplementedMethodFix = createAddUnimplementedMethodsFix(context.getASTRoot(),
                problemLocation);
        if (unimplementedMethodFix != null) {
            Image image = JavaPluginImages.get(JavaPluginImages.IMG_CORRECTION_CHANGE);

            Map<String, String> settings= new HashMap<String, String>();
            settings.put("cleanup.add_missing_methods", "true"); //$NON-NLS-1$ //$NON-NLS-2$

            proposals.add(new FixCorrectionProposal(unimplementedMethodFix, 
                    new UnimplementedCodeCleanUp(settings), 5, image, context));
        }
    }
    
    private IProposableFix createAddUnimplementedMethodsFix(final CompilationUnit compilationUnit, 
            IProblemLocation problemLocation) {
        ASTNode endpointInterfaceValue = problemLocation.getCoveringNode(compilationUnit);
        ASTNode endpointInterfaceMVP = endpointInterfaceValue.getParent();
        ASTNode webServiceAnnotation = endpointInterfaceMVP.getParent();
        ASTNode typeDeclaration = webServiceAnnotation.getParent();
        
        String endpointInterface = ((StringLiteral) endpointInterfaceValue).getLiteralValue();
        AddUnimplementedSEIMethodsOperation operation= new AddUnimplementedSEIMethodsOperation(typeDeclaration, 
                endpointInterface);
        if (operation.getMethodsToImplement() != null && operation.getMethodsToImplement().length > 0) {
            return new UnimplementedCodeFix(JAXWSUIMessages.ADD_UNIMPLEMENTED_METHODS, compilationUnit, 
                    new CompilationUnitRewriteOperation[] { operation });
        }
        return null;
    }

	private void addChangeModifierProposal(IInvocationContext context, IProblemLocation problemLocation,
	        List<IJavaCompletionProposal> proposals, int relevance) {
		
	    ICompilationUnit compilationUnit = context.getCompilationUnit();

		ASTNode selectedNode = problemLocation.getCoveringNode(context.getASTRoot());

		if (!(selectedNode instanceof SimpleName)) {
			return;
		}

		IBinding binding = ((SimpleName) selectedNode).resolveBinding();
		if (binding != null) {
			String bindingName = binding.getName();
			String label = null;
			
			int excludedModifiers = 0;
			int includedModifiers = 0;
			
        	String problem = problemLocation.getProblemArguments()[1];

            if(problem.equals(JAXWSCoreMessages.WEBMETHOD_ONLY_ON_PUBLIC_METHODS)) {
    			excludedModifiers = ~(Modifier.PUBLIC);
    			includedModifiers = Modifier.PUBLIC ;
				label = JAXWSUIMessages.bind(JAXWSUIMessages.CHANGE_METHOD_VISIBILITY, "public"); //$NON-NLS-1$
            }

            if(problem.equals(JAXWSCoreMessages.WEBMETHOD_NO_FINAL_MODIFIER_ALLOWED)) {
            	excludedModifiers = Modifier.FINAL;
            	label = JAXWSUIMessages.bind(JAXWSUIMessages.REMOVE_FINAL_MODIFIER, bindingName);
            }

            if(problem.equals(JAXWSCoreMessages.WEBMETHOD_NO_STATIC_MODIFIER_ALLOWED)) {
            	excludedModifiers = Modifier.STATIC;
            	label = JAXWSUIMessages.bind(JAXWSUIMessages.REMOVE_STATIC_MODIFIER, bindingName);
            }
            
            if (problem.equals(JAXWSCoreMessages.WEBSERVICE_PUBLIC_ABSTRACT_FINAL)) {
    			excludedModifiers = Modifier.ABSTRACT | Modifier.FINAL;
    			label = JAXWSUIMessages.REMOVE_ILLEGAL_MODIFIER;
            }

			Image image = JavaPluginImages.get(JavaPluginImages.IMG_CORRECTION_CHANGE);
			proposals.add(new ModifierChangeCorrectionProposal(label, compilationUnit, binding, selectedNode, 
			        includedModifiers, excludedModifiers, relevance, image));			
		}
	}
	
    private void addConstructorProposal(IInvocationContext context, IProblemLocation problemLocation,
            List<IJavaCompletionProposal> proposals, int relevance) {

        ICompilationUnit cu = context.getCompilationUnit();

        CompilationUnit astRoot = context.getASTRoot();
        ASTNode selectedNode = problemLocation.getCoveringNode(astRoot);

        ITypeBinding targetBinding = ((AbstractTypeDeclaration) selectedNode.getParent()).resolveBinding();

        if (targetBinding != null && targetBinding.isFromSource()) {
            String label = JAXWSUIMessages.bind(JAXWSUIMessages.CREATE_CONSTRUCTOR,
                    targetBinding.getTypeDeclaration().getName());
            
            Image image = JavaElementImageProvider.getDecoratedImage(JavaPluginImages.DESC_MISC_PUBLIC,
                    JavaElementImageDescriptor.CONSTRUCTOR, JavaElementImageProvider.SMALL_SIZE);

            proposals.add(new NewMethodCorrectionProposal(label, cu, selectedNode, Collections.emptyList(),
                    targetBinding, 5, image));
        }
    }
}
