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
package org.eclipse.jst.ws.internal.jaxws.core.annotations;

import java.util.List;

import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.MemberValuePair;

/**
 * 
 * @author sclarke
 *
 */
public interface IAnnotationAttributeInitializer {  
    public List<MemberValuePair> getMemberValuePairs(IMember member, AST ast,
            Class<? extends java.lang.annotation.Annotation> annotationClass);

    public List<MemberValuePair> getMemberValuePairs(ASTNode astNode, AST ast,
            Class<? extends java.lang.annotation.Annotation> annotationClass);
}
