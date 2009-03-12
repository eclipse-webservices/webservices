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
package org.eclipse.jst.ws.jaxws.core.tests;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jst.ws.annotations.core.AnnotationsCore;
import org.eclipse.jst.ws.annotations.core.utils.AnnotationUtils;

/**
 * 
 * @author sclarke
 *
 */
public class RemoveAnnotationFromMethodParameterTest extends AbstractAnnotationTest {
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
        return "public class Calculator {\n\n\tpublic int add(@WebParam(name=\"i\")int i, int k) {" +
            "\n\t\treturn i + k;\n\t}\n}";
    }

    @Override
    public Annotation getAnnotation() {
        return AnnotationsCore.createAnnotation(ast, javax.jws.WebParam.class, null);
    }
    
    public void testRemoveAnnotationFromMethodParameter() {
        try {
            assertNotNull(annotation);
            assertEquals("WebParam", AnnotationUtils.getAnnotationName(annotation));
            
            IMethod method = source.findPrimaryType().getMethod("add", new String[] {"I", "I"});
            assertNotNull(method);
            
            SingleVariableDeclaration parameter = AnnotationUtils.getMethodParameter(method, 44);
            
            assertTrue(AnnotationUtils.isAnnotationPresent(parameter, annotation));

            AnnotationUtils.removeAnnotationFromMethodParameter(source, rewriter, parameter, annotation, 
                    textFileChange);
            
            assertTrue(executeChange(new NullProgressMonitor(), textFileChange));

            //refresh
            parameter = AnnotationUtils.getMethodParameter(method, 44);

            assertFalse(AnnotationUtils.isAnnotationPresent(parameter, annotation));
        } catch (CoreException ce) {
            ce.printStackTrace();
        }
    }
}