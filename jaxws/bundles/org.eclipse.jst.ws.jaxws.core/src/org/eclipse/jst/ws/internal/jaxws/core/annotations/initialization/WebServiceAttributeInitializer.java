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

import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.MemberValuePair;
import org.eclipse.jst.ws.internal.jaxws.core.annotations.AnnotationAttributeInitializerAdapter;
import org.eclipse.jst.ws.internal.jaxws.core.annotations.AnnotationsCore;
import org.eclipse.jst.ws.internal.jaxws.core.utils.JDTUtils;

/**
 * 
 * @author sclarke
 *
 */
public class WebServiceAttributeInitializer extends AnnotationAttributeInitializerAdapter {
    private static final String TARGET_NAMESPACE = "targetNamespace";
    private static final String PORT = "Port"; //$NON-NLS-1$
    private static final String SERVICE = "Service"; //$NON-NLS-1$

    @Override
    public List<MemberValuePair> getMemberValuePairs(IMember member, AST ast,
            Class<? extends Annotation> annotationClass) {
        
        List<MemberValuePair> memberValuePairs = new ArrayList<MemberValuePair>();

        String typeName = member.getElementName();
        IType type = member.getCompilationUnit().findPrimaryType();
        
        String packageName = type.getPackageFragment().getElementName();

        MemberValuePair nameValuePair = AnnotationsCore.getNameValuePair(ast, typeName);

        MemberValuePair targetNamespaceValuePair = AnnotationsCore.getStringMemberValuePair(ast, TARGET_NAMESPACE,
                JDTUtils.getTargetNamespaceFromPackageName(packageName));

        MemberValuePair portNameValuePair = AnnotationsCore.getPortNameValuePair(ast, typeName
                + PORT); //$NON-NLS-1$

        MemberValuePair serviceNameValuePair = AnnotationsCore.getServiceNameValuePair(ast,
                typeName + SERVICE); //$NON-NLS-1$

        memberValuePairs.add(nameValuePair);
        memberValuePairs.add(targetNamespaceValuePair);
        memberValuePairs.add(portNameValuePair);
        memberValuePairs.add(serviceNameValuePair);

        return memberValuePairs;
    }

    @Override
    public List<MemberValuePair> getMemberValuePairs(ASTNode astNode, AST ast,
            Class<? extends Annotation> annotationClass) {
        return Collections.emptyList();
    }
}
