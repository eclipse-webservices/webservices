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
package org.eclipse.jst.ws.internal.jaxws.core.annotations.initialization;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MemberValuePair;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jst.ws.annotations.core.AnnotationsCore;
import org.eclipse.jst.ws.annotations.core.utils.AnnotationUtils;
import org.eclipse.jst.ws.jaxws.core.utils.JDTUtils;

/**
 * 
 * @author sclarke
 * 
 */
public class WebParamAttributeInitializer extends JAXWSAnnotationAttributeInitializer {

    @Override
    public List<MemberValuePair> getMemberValuePairs(ASTNode astNode, AST ast,
    Class<? extends Annotation> annotationClass) {
        
        List<MemberValuePair> memberValuePairs = new ArrayList<MemberValuePair>();
        if (astNode instanceof SingleVariableDeclaration) {
            MemberValuePair nameValuePair = AnnotationsCore.createStringMemberValuePair(ast, NAME,
            getName((SingleVariableDeclaration) astNode));
            memberValuePairs.add(nameValuePair);
        }
        return memberValuePairs;
    }

    public List<ICompletionProposal> getCompletionProposalsForMemberValuePair(ASTNode astNode,
            MemberValuePair memberValuePair) {
        
        List<ICompletionProposal> completionProposals = new ArrayList<ICompletionProposal>();
        if (astNode instanceof SingleVariableDeclaration) {
            SingleVariableDeclaration parameter = (SingleVariableDeclaration) astNode;
            String memberValuePairName = memberValuePair.getName().getIdentifier();
            
            if (memberValuePairName.equals(NAME)) {
                completionProposals.add(AnnotationUtils.createCompletionProposal(getName(parameter),
                        memberValuePair.getValue()));
            }
            
            if (memberValuePairName.equals(PART_NAME)) {
                completionProposals.add(AnnotationUtils.createCompletionProposal(getPartName(parameter),
                        memberValuePair.getValue()));
            }
            
            if (memberValuePairName.equals(TARGET_NAMESPACE)) {
                completionProposals.add(AnnotationUtils.createCompletionProposal(getTargetNamespace(parameter),
                        memberValuePair.getValue()));
            }
            
        }
        return completionProposals;
    }

    private String getName(SingleVariableDeclaration parameter) {
        MethodDeclaration methodDeclaration = (MethodDeclaration) parameter.getParent();
        if (hasDocumentBareSOAPBinding(methodDeclaration)) {
            return getWebMethodOperationName(methodDeclaration);
        }

        List<?> siblings = (List<?>) methodDeclaration.getStructuralProperty(parameter.getLocationInParent());
        return ARG + siblings.indexOf(parameter);
    }
    
    private String getPartName(SingleVariableDeclaration parameter) {
        org.eclipse.jdt.core.dom.Annotation annotation = AnnotationUtils.getAnnotation(parameter,
                WebParam.class);
        if (annotation != null) {
            String name = AnnotationUtils.getStringValue(annotation, NAME);
            if (name != null) {
                return name;
            }
        }
        return getName(parameter);
    }
    
    private String getTargetNamespace(SingleVariableDeclaration parameter) {
        if (hasDocumentWrappedSOAPBinding(parameter) && !isHeader(parameter)) {
            return ""; 
        }
        return getDefaultTargetNamespace(parameter);
    }

    private String getDefaultTargetNamespace(SingleVariableDeclaration parameter) {
        MethodDeclaration methodDeclaration = (MethodDeclaration) parameter.getParent();
        TypeDeclaration typeDeclaration = (TypeDeclaration) methodDeclaration.getParent();
        
        org.eclipse.jdt.core.dom.Annotation annotation = AnnotationUtils.getAnnotation(typeDeclaration,
                WebService.class);
        if (annotation != null) {
            String targetNamespace = AnnotationUtils.getStringValue(annotation, TARGET_NAMESPACE);
            if (targetNamespace != null) {
                return targetNamespace;
            }
        }
        return JDTUtils.getTargetNamespaceFromPackageName(getPackageName(typeDeclaration));
    }

    private String getPackageName(TypeDeclaration typeDeclaration) {
        if (typeDeclaration.isPackageMemberTypeDeclaration()) {
            PackageDeclaration packageDeclaration = ((CompilationUnit) typeDeclaration.getParent()).getPackage();
            if (packageDeclaration != null) {
                return packageDeclaration.getName().getFullyQualifiedName();
            }          
        }
        return "";
    }
    
    private boolean isHeader(SingleVariableDeclaration parameter) {
        org.eclipse.jdt.core.dom.Annotation annotation = AnnotationUtils.getAnnotation(parameter, WebParam.class);
        if (annotation != null) {
            Boolean header = AnnotationUtils.getBooleanValue(annotation, HEADER);
            if (header != null) {
                return header.booleanValue();
            }
        }
        return false;
    }

    private boolean hasDocumentWrappedSOAPBinding(SingleVariableDeclaration parameter) {
        MethodDeclaration methodDeclaration = (MethodDeclaration) parameter.getParent();

        org.eclipse.jdt.core.dom.Annotation annotation = AnnotationUtils.getAnnotation(methodDeclaration,
                SOAPBinding.class);
        if (annotation != null) {
            return isDocumentWrapped(annotation);
        }
        
        TypeDeclaration typeDeclaration = (TypeDeclaration) methodDeclaration.getParent();
        
        org.eclipse.jdt.core.dom.Annotation typeAnnotation = AnnotationUtils.getAnnotation(typeDeclaration,
                SOAPBinding.class);
        if (typeAnnotation != null) {
            return isDocumentWrapped(typeAnnotation);
        }
        return true;
    }

    private boolean isDocumentWrapped(org.eclipse.jdt.core.dom.Annotation annotation) {
        String style = AnnotationUtils.getEnumValue(annotation, STYLE);
        String use = AnnotationUtils.getEnumValue(annotation, USE);
        String parameterStyle = AnnotationUtils.getEnumValue(annotation, PARAMETER_STYLE);

        return (style == null || style.equals(Style.DOCUMENT.name()))
                && (use == null || use.equals(Use.LITERAL.name()))
                && (parameterStyle == null || parameterStyle.equals(ParameterStyle.WRAPPED.name()));
    }

    private boolean hasDocumentBareSOAPBinding(BodyDeclaration bodyDeclaration) {
        org.eclipse.jdt.core.dom.Annotation annotation = AnnotationUtils.getAnnotation(bodyDeclaration,
                SOAPBinding.class);
        if (annotation != null) {
            return isDocumentBare(annotation);
        }
        if (bodyDeclaration instanceof MethodDeclaration) {
            MethodDeclaration methodDeclaration = (MethodDeclaration) bodyDeclaration;
            return hasDocumentBareSOAPBinding((TypeDeclaration) methodDeclaration.getParent());
        }
        return false;
    }

    private boolean isDocumentBare(org.eclipse.jdt.core.dom.Annotation annotation) {
        String style = AnnotationUtils.getEnumValue(annotation, STYLE);
        String use = AnnotationUtils.getEnumValue(annotation, USE);
        String parameterStyle = AnnotationUtils.getEnumValue(annotation, PARAMETER_STYLE);

        return (style == null || style.equals(Style.DOCUMENT.name()))
                && (use == null || use.equals(Use.LITERAL.name()))
                && (parameterStyle != null && parameterStyle.equals(ParameterStyle.BARE.name()));
    }

    private String getWebMethodOperationName(MethodDeclaration methodDeclaration) {
        org.eclipse.jdt.core.dom.Annotation annotation = AnnotationUtils.getAnnotation(methodDeclaration,
                WebMethod.class);
        if (annotation != null) {
            String operationName = AnnotationUtils.getStringValue(annotation, OPERATION_NAME);
            if (operationName != null) {
                return operationName;
            }
        }
        return methodDeclaration.getName().getIdentifier();
    }

}
