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
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.MemberValuePair;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jst.ws.internal.jaxws.core.annotations.AnnotationAttributeInitializerAdapter;
import org.eclipse.jst.ws.internal.jaxws.core.annotations.AnnotationsCore;
/**
 * 
 * @author sclarke
 *
 */
public class WebParamAttributeInitializer extends AnnotationAttributeInitializerAdapter {
    private static final String NAME = "name";
    
    @Override
    public List<MemberValuePair> getMemberValuePairs(IMember member, AST ast,
            Class<? extends Annotation> annotationClass) {
        return Collections.emptyList();
    }

    @Override
    public List<MemberValuePair> getMemberValuePairs(ASTNode astNode, AST ast,
            Class<? extends Annotation> annotationClass) {
        
        List<MemberValuePair> memberValuePairs = new ArrayList<MemberValuePair>();
        MemberValuePair nameValuePair = AnnotationsCore.getStringMemberValuePair(ast, NAME,
                ((SingleVariableDeclaration)astNode).getName().getIdentifier());
        memberValuePairs.add(nameValuePair);

        return memberValuePairs;
    }
}
