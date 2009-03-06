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
package org.eclipse.jst.ws.internal.jaxws.core.tests;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.MemberValuePair;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jst.ws.internal.jaxws.core.annotations.AnnotationsCore;
import org.eclipse.jst.ws.internal.jaxws.core.utils.AnnotationUtils;
import org.eclipse.jst.ws.internal.jaxws.core.utils.JDTUtils;

/**
 * 
 * @author sclarke
 *
 */
public class AddAnnotationToMethodParameterTest extends AbstractAnnotationTest {
    @Override
    public String getPackageName() {
        return "com.example";
    }

    @Override
    public String getClassName() {
        return "Calculator.java";
    }
    
    @Override
    public String getClassContents() {
        return "public class Calculator {\n\n\tpublic int add(int i, int k) {" +
            "\n\t\treturn i + k;\n\t}\n}";
    }

    @Override
    public Annotation getAnnotation() {
        List<MemberValuePair> memberValuePairs = new ArrayList<MemberValuePair>();
        MemberValuePair nameValuePair = AnnotationsCore.getStringMemberValuePair(ast, "name", "i");
        memberValuePairs.add(nameValuePair);
        return AnnotationsCore.getAnnotation(ast, javax.jws.WebParam.class, memberValuePairs);
    }
    
    public void testAddAnnotationToMethodParameter() {
        try {
            assertNotNull(annotation);
            assertEquals("WebParam", AnnotationUtils.getAnnotationName(annotation));
            
            IMethod method = source.findPrimaryType().getMethod("add", new String[] {"I", "I"});
            assertNotNull(method);
            
            SingleVariableDeclaration parameter = JDTUtils.getMethodParameter(method, 44);
        
            AnnotationUtils.createMethodParameterAnnotationChange(source, compilationUnit, rewriter, 
                    parameter, method, annotation, textFileChange);
            
            assertTrue(executeChange(new NullProgressMonitor(), textFileChange));
            
            //refresh
            parameter = JDTUtils.getMethodParameter(method, 44);
            
            assertTrue(AnnotationUtils.isAnnotationPresent(parameter, annotation));
        } catch (CoreException ce) {
            ce.printStackTrace();
        }
    }
}
