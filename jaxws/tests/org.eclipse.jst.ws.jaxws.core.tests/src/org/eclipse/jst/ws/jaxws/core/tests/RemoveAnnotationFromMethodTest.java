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
import org.eclipse.jst.ws.annotations.core.AnnotationsCore;
import org.eclipse.jst.ws.annotations.core.utils.AnnotationUtils;

/**
 * 
 * @author sclarke
 *
 */
public class RemoveAnnotationFromMethodTest extends AbstractAnnotationTest {
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
        return "public class Calculator {\n\n\t@WebMethod\npublic int add(int i, int k) {" +
            "\n\t\treturn i + k;\n\t}\n}";
    }

    @Override
    public Annotation getAnnotation() {
        return AnnotationsCore.createAnnotation(ast, javax.jws.WebMethod.class,
                javax.jws.WebMethod.class.getSimpleName(), null);
    }

    public void testRemoveAnnotationFromMethod() {
        try {
            assertNotNull(annotation);
            assertEquals("WebMethod", AnnotationUtils.getAnnotationName(annotation));
            
            IMethod method = source.findPrimaryType().getMethod("add", new String[] {"I", "I"});
            assertNotNull(method);
        
            assertTrue(AnnotationUtils.isAnnotationPresent(method,
                    AnnotationUtils.getAnnotationName(annotation)));

            AnnotationUtils.removeAnnotationFromMethod(source, compilationUnit, rewriter, method, annotation,
                    textFileChange);
            
            assertTrue(executeChange(new NullProgressMonitor(), textFileChange));
            
            assertFalse(AnnotationUtils.isAnnotationPresent(method,
                    AnnotationUtils.getAnnotationName(annotation)));
        } catch (CoreException ce) {
            ce.printStackTrace();
        }
    }
}
