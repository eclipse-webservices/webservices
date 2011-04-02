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
package org.eclipse.jst.ws.internal.jaxws.ui.annotations.initialization;

import static org.eclipse.jst.ws.internal.jaxws.core.utils.JAXWSUtils.ARG;
import static org.eclipse.jst.ws.internal.jaxws.core.utils.JAXWSUtils.HEADER;
import static org.eclipse.jst.ws.internal.jaxws.core.utils.JAXWSUtils.NAME;
import static org.eclipse.jst.ws.internal.jaxws.core.utils.JAXWSUtils.OPERATION_NAME;
import static org.eclipse.jst.ws.internal.jaxws.core.utils.JAXWSUtils.PART_NAME;
import static org.eclipse.jst.ws.internal.jaxws.core.utils.JAXWSUtils.TARGET_NAMESPACE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.ILocalVariable;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.MemberValuePair;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jst.ws.annotations.core.AnnotationsCore;
import org.eclipse.jst.ws.annotations.core.initialization.AnnotationAttributeInitializer;
import org.eclipse.jst.ws.annotations.core.utils.AnnotationUtils;
import org.eclipse.jst.ws.internal.jaxws.core.utils.JAXWSUtils;
import org.eclipse.jst.ws.internal.jaxws.ui.JAXWSUIPlugin;
import org.eclipse.jst.ws.jaxws.core.utils.JDTUtils;

public class WebParamAttributeInitializer extends AnnotationAttributeInitializer {
    
    @Override
    public List<MemberValuePair> getMemberValuePairs(IJavaElement javaElement, AST ast, IType annotationType) {

        List<MemberValuePair> memberValuePairs = new ArrayList<MemberValuePair>();

        if (javaElement.getElementType() == IJavaElement.LOCAL_VARIABLE) {
            ILocalVariable parameter = (ILocalVariable) javaElement;
            MemberValuePair nameValuePair = AnnotationsCore.createStringMemberValuePair(ast, NAME, getName(parameter));
            memberValuePairs.add(nameValuePair);
        }
        return memberValuePairs;
    }

    @Override
    public List<ICompletionProposal> getCompletionProposalsForMemberValuePair(IJavaElement javaElement,
            MemberValuePair memberValuePair) {

        List<ICompletionProposal> completionProposals = new ArrayList<ICompletionProposal>();
        if (javaElement.getElementType() == IJavaElement.LOCAL_VARIABLE) {
            ILocalVariable parameter = (ILocalVariable) javaElement;

            String memberValuePairName = memberValuePair.getName().getIdentifier();

            if (memberValuePairName.equals(NAME)) {
                completionProposals.add(createCompletionProposal(getName(parameter),
                        memberValuePair.getValue()));
            }

            if (memberValuePairName.equals(PART_NAME)) {
                completionProposals.add(createCompletionProposal(getPartName(parameter),
                        memberValuePair.getValue()));
            }

            if (memberValuePairName.equals(TARGET_NAMESPACE)) {
                completionProposals.add(createCompletionProposal(getTargetNamespace(parameter),
                        memberValuePair.getValue()));
            }

        }
        return completionProposals;
    }

    private String getName(ILocalVariable parameter) {
        IMethod method = (IMethod) parameter.getParent();
        if (hasDocumentBareSOAPBinding(method)) {
            return getWebMethodOperationName(method);
        }
        try {
            List<String> methodNames = Arrays.asList(method.getParameterNames());
            return ARG + methodNames.indexOf(parameter.getElementName());
        } catch (JavaModelException jme) {
            JAXWSUIPlugin.log(jme.getStatus());
        }
        return parameter.getElementName();
    }

    private String getPartName(ILocalVariable parameter) {
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

    private String getTargetNamespace(ILocalVariable parameter) {
        if (hasDocumentWrappedSOAPBinding(parameter) && !isHeader(parameter)) {
            return "";  //$NON-NLS-1$
        }
        return getDefaultTargetNamespace(parameter);
    }

    private String getDefaultTargetNamespace(ILocalVariable parameter) {
        IMethod method = (IMethod) parameter.getParent();
        IType type = method.getDeclaringType();
        org.eclipse.jdt.core.dom.Annotation annotation = AnnotationUtils.getAnnotation(type,
                WebService.class);
        if (annotation != null) {
            String targetNamespace = AnnotationUtils.getStringValue(annotation, TARGET_NAMESPACE);
            if (targetNamespace != null) {
                return targetNamespace;
            }
        }
        return JDTUtils.getTargetNamespaceFromPackageName(type.getPackageFragment().getElementName());
    }

    private boolean isHeader(ILocalVariable parameter) {
        org.eclipse.jdt.core.dom.Annotation annotation = AnnotationUtils.getAnnotation(parameter, WebParam.class);
        if (annotation != null) {
            Boolean header = AnnotationUtils.getBooleanValue(annotation, HEADER);
            if (header != null) {
                return header.booleanValue();
            }
        }
        return false;
    }

    private boolean hasDocumentWrappedSOAPBinding(ILocalVariable parameter) {
        IMethod method = (IMethod) parameter.getParent();

        org.eclipse.jdt.core.dom.Annotation annotation = AnnotationUtils.getAnnotation(method,
                SOAPBinding.class);
        if (annotation != null) {
            return JAXWSUtils.isDocumentWrapped(annotation);
        }

        org.eclipse.jdt.core.dom.Annotation typeAnnotation = AnnotationUtils.getAnnotation(method.getDeclaringType(),
                SOAPBinding.class);
        if (typeAnnotation != null) {
            return JAXWSUtils.isDocumentWrapped(typeAnnotation);
        }
        return true;
    }

    private boolean hasDocumentBareSOAPBinding(IJavaElement javaElement) {
        org.eclipse.jdt.core.dom.Annotation annotation = AnnotationUtils.getAnnotation(javaElement,
                SOAPBinding.class);
        if (annotation != null) {
            return JAXWSUtils.isDocumentBare(annotation);
        }
        if (javaElement.getElementType() ==  IJavaElement.METHOD) {
            IMethod method = (IMethod) javaElement;
            return hasDocumentBareSOAPBinding(method.getDeclaringType());
        }
        return false;
    }


    private String getWebMethodOperationName(IMethod method) {
        org.eclipse.jdt.core.dom.Annotation annotation = AnnotationUtils.getAnnotation(method,
                WebMethod.class);
        if (annotation != null) {
            String operationName = AnnotationUtils.getStringValue(annotation, OPERATION_NAME);
            if (operationName != null) {
                return operationName;
            }
        }
        return method.getElementName();
    }

}
