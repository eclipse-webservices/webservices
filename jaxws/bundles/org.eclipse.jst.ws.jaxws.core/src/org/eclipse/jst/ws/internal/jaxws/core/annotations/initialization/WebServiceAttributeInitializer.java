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
import java.util.Collections;
import java.util.List;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.MemberValuePair;
import org.eclipse.jdt.ui.ISharedImages;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jst.ws.annotations.core.AnnotationsCore;
import org.eclipse.jst.ws.annotations.core.initialization.AnnotationAttributeInitializer;
import org.eclipse.jst.ws.annotations.core.utils.AnnotationUtils;
import org.eclipse.jst.ws.internal.jaxws.core.JAXWSCorePlugin;
import org.eclipse.jst.ws.jaxws.core.utils.JDTUtils;

/**
 * 
 * @author sclarke
 *
 */
public class WebServiceAttributeInitializer extends AnnotationAttributeInitializer {
    private static final String NAME = "name"; //$NON-NLS-1$
    private static final String PORT_NAME = "portName"; //$NON-NLS-1$
    private static final String SERVICE_NAME= "serviceName"; //$NON-NLS-1$
    private static final String ENDPOINT_INTERFACE = "endpointInterface"; //$NON-NLS-1$
    private static final String TARGET_NAMESPACE = "targetNamespace"; //$NON-NLS-1$
    
    private static final String PORT_SUFFIX = "Port"; //$NON-NLS-1$
    private static final String SERVICE_SUFFIX = "Service"; //$NON-NLS-1$

    public WebServiceAttributeInitializer() {
    }
    
    @Override
    public List<MemberValuePair> getMemberValuePairs(IJavaElement javaElement, AST ast,
            Class<? extends Annotation> annotationClass) {
        
        List<MemberValuePair> memberValuePairs = new ArrayList<MemberValuePair>();
        if (javaElement.getElementType() == IJavaElement.TYPE) {
            try {
                IType type = (IType)javaElement;
    
                MemberValuePair nameValuePair = AnnotationsCore.createStringMemberValuePair(ast, NAME, getName(type));
        
                MemberValuePair portNameValuePair = AnnotationsCore.createStringMemberValuePair(ast, PORT_NAME, 
                        getPortName(type));
        
                MemberValuePair serviceNameValuePair = AnnotationsCore.createStringMemberValuePair(ast, SERVICE_NAME,
                        getServiceName(type));
    
                MemberValuePair targetNamespaceValuePair = AnnotationsCore.createStringMemberValuePair(ast, 
                        TARGET_NAMESPACE, getTargetNamespace(type));
    
                memberValuePairs.add(nameValuePair);
                memberValuePairs.add(targetNamespaceValuePair);
                
                if (type.isClass()) {
                    memberValuePairs.add(portNameValuePair);
                    memberValuePairs.add(serviceNameValuePair);
                }
            } catch (JavaModelException jme) {
                JAXWSCorePlugin.log(jme.getStatus());
            }
        }
        return memberValuePairs;
    }
    
    public List<ICompletionProposal> getCompletionProposalsForMemberValuePair(IJavaElement javaElement,
            MemberValuePair memberValuePair) {

        List<ICompletionProposal> completionProposals = new ArrayList<ICompletionProposal>();
        if (javaElement.getElementType() == IJavaElement.TYPE) {
            IType type = (IType) javaElement;

            String memberValuePairName = memberValuePair.getName().getIdentifier();

            if (memberValuePairName.equals(ENDPOINT_INTERFACE)) {
                List<String> interfaces = getInterfaces(type);
                for (String anInterface : interfaces) {
                    completionProposals.add(AnnotationUtils.createCompletionProposal(anInterface,
                            memberValuePair.getValue(), JavaUI.getSharedImages().getImage(
                                    ISharedImages.IMG_OBJS_INTERFACE)));
                }
            }
            if (memberValuePairName.equals(NAME)) {
                completionProposals.add(AnnotationUtils.createCompletionProposal(getName(type),
                        memberValuePair.getValue()));
            }
            if (memberValuePairName.equals(PORT_NAME)) {
                completionProposals.add(AnnotationUtils.createCompletionProposal(getPortName(type),
                        memberValuePair.getValue()));
            }
            if (memberValuePairName.equals(SERVICE_NAME)) {
                completionProposals.add(AnnotationUtils.createCompletionProposal(getServiceName(type),
                        memberValuePair.getValue()));
            }
            if (memberValuePairName.equals(TARGET_NAMESPACE)) {
                completionProposals.add(AnnotationUtils.createCompletionProposal(getTargetNamespace(type),
                        memberValuePair.getValue()));
            }
        }
        return completionProposals;
    }

    public String getName(IType type) {
        return type.getElementName();
    }
    
    public String getPortName(IType type) {
        return type.getElementName() + PORT_SUFFIX;
    }
    
    public String getServiceName(IType type) {
        return type.getElementName() + SERVICE_SUFFIX;
    }
    
    public String getTargetNamespace(IType type) {
        return JDTUtils.getTargetNamespaceFromPackageName(type.getPackageFragment().getElementName());
    }
    
    private List<String> getInterfaces(IType type) {
        try {
            List<String> interfaces = new ArrayList<String>();
            ITypeHierarchy typeHierarchy = type.newTypeHierarchy(type.getJavaProject(), null);
            IType[] allInterfaces = typeHierarchy.getAllInterfaces();
            for (IType aInterface : allInterfaces) {
                if (!aInterface.isBinary() && aInterface.getResource().getProject().equals(
                        type.getResource().getProject())) {
                    String packageName = aInterface.getPackageFragment().getElementName();
                    if (packageName.trim().length() > 0) {
                        packageName += "."; //$NON-NLS-1$
                    }
                    String qualifiedName = packageName + aInterface.getPrimaryElement().getElementName();
                    interfaces.add(qualifiedName);
                }
            }
            return interfaces;
        } catch (JavaModelException jme) {
            JAXWSCorePlugin.log(jme.getStatus());
        }
        return Collections.emptyList();
    }

}
