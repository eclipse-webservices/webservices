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

import static org.eclipse.jst.ws.internal.jaxws.core.utils.JAXWSUtils.ENDPOINT_INTERFACE;
import static org.eclipse.jst.ws.internal.jaxws.core.utils.JAXWSUtils.NAME;
import static org.eclipse.jst.ws.internal.jaxws.core.utils.JAXWSUtils.PORT_NAME;
import static org.eclipse.jst.ws.internal.jaxws.core.utils.JAXWSUtils.PORT_SUFFIX;
import static org.eclipse.jst.ws.internal.jaxws.core.utils.JAXWSUtils.SERVICE_NAME;
import static org.eclipse.jst.ws.internal.jaxws.core.utils.JAXWSUtils.SERVICE_SUFFIX;
import static org.eclipse.jst.ws.internal.jaxws.core.utils.JAXWSUtils.TARGET_NAMESPACE;

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
import org.eclipse.jst.ws.internal.jaxws.ui.JAXWSUIPlugin;
import org.eclipse.jst.ws.jaxws.core.utils.JDTUtils;

public class WebServiceAttributeInitializer extends AnnotationAttributeInitializer {

    @Override
    public List<MemberValuePair> getMemberValuePairs(IJavaElement javaElement, AST ast, IType annotationType) {
        
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
    
                if (type.isInterface()) {
                    memberValuePairs.add(nameValuePair);
                }

                memberValuePairs.add(targetNamespaceValuePair);
                
                if (type.isClass()) {
                    memberValuePairs.add(portNameValuePair);
                    memberValuePairs.add(serviceNameValuePair);
                }
            } catch (JavaModelException jme) {
            	JAXWSUIPlugin.log(jme.getStatus());
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
                    completionProposals.add(createCompletionProposal(anInterface,
                            memberValuePair.getValue(), JavaUI.getSharedImages().getImage(
                                    ISharedImages.IMG_OBJS_INTERFACE), anInterface));
                }
            }
            if (memberValuePairName.equals(NAME)) {
                completionProposals.add(createCompletionProposal(getName(type),
                        memberValuePair.getValue()));
            }
            if (memberValuePairName.equals(PORT_NAME)) {
                completionProposals.add(createCompletionProposal(getPortName(type),
                        memberValuePair.getValue()));
            }
            if (memberValuePairName.equals(SERVICE_NAME)) {
                completionProposals.add(createCompletionProposal(getServiceName(type),
                        memberValuePair.getValue()));
            }
            if (memberValuePairName.equals(TARGET_NAMESPACE)) {
                completionProposals.add(createCompletionProposal(getTargetNamespace(type),
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
        	JAXWSUIPlugin.log(jme.getStatus());
        }
        return Collections.emptyList();
    }

}
