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
import java.util.Locale;

import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.MemberValuePair;
import org.eclipse.jst.ws.internal.jaxws.core.JAXWSCorePlugin;
import org.eclipse.jst.ws.internal.jaxws.core.annotations.AnnotationAttributeInitializerAdapter;
import org.eclipse.jst.ws.internal.jaxws.core.annotations.AnnotationsCore;
import org.eclipse.jst.ws.internal.jaxws.core.utils.AnnotationUtils;
import org.eclipse.jst.ws.internal.jaxws.core.utils.JDTUtils;

/**
 * 
 * @author sclarke
 *
 */
public class RequestWrapperAttributeInitializer extends AnnotationAttributeInitializerAdapter {
    private static final String CLASS_NAME = "className";
    private static final String LOCAL_NAME = "localName";
    private static final String TARGET_NAMESPACE = "targetNamespace";

    @Override
    public List<MemberValuePair> getMemberValuePairs(IMember member, AST ast,
            Class<? extends Annotation> annotationClass) {
        List<MemberValuePair> memberValuePairs = new ArrayList<MemberValuePair>();

        try {
            IType type = member.getCompilationUnit().findPrimaryType();
            IMethod method = (IMethod)member;
    
            String methodName = method.getElementName();
            String packageName = type.getPackageFragment().getElementName();
            if (packageName == null || packageName.length() == 0) {
                packageName = "default_package"; //$NON-NLS-1$
            }
            packageName += "."; //$NON-NLS-1$

            String className = packageName + methodName.substring(0, 1).toUpperCase(Locale.getDefault())
                    + methodName.substring(1);
            
            String overloadedSuffix = AnnotationUtils.accountForOverloadedMethods(type, method);
            
            MemberValuePair classNameValuePair = AnnotationsCore.getStringMemberValuePair(ast, CLASS_NAME,
                    className + overloadedSuffix);

            MemberValuePair localNameValuePair = AnnotationsCore.getStringMemberValuePair(ast, LOCAL_NAME,
                    methodName + overloadedSuffix);

            MemberValuePair targetNamespaceValuePair = AnnotationsCore.getStringMemberValuePair(ast, TARGET_NAMESPACE,
                    JDTUtils.getTargetNamespaceFromPackageName(packageName));

            memberValuePairs.add(classNameValuePair);
            memberValuePairs.add(localNameValuePair);
            memberValuePairs.add(targetNamespaceValuePair);
        } catch (JavaModelException jme) {
            JAXWSCorePlugin.log(jme.getStatus());
        }
        return memberValuePairs;
    }

    @Override
    public List<MemberValuePair> getMemberValuePairs(ASTNode astNode, AST ast,
            Class<? extends Annotation> annotationClass) {
        return Collections.emptyList();
    }

}
