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

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jst.ws.internal.jaxws.core.annotations.AnnotationsCore;
import org.eclipse.jst.ws.internal.jaxws.core.utils.AnnotationUtils;

/**
 * 
 * @author sclarke
 *
 */
public class RemoveAnnotationFromFieldTest extends AbstractAnnotationTest {
    @Override
    public String getPackageName() {
        return "com.example";
    }

    @Override
    public String getClassName() {
        return "CalculatorClient.java";
    }
    
    @Override
    public String getClassContents() {
        return "public class CalculatorClient {\n\n@WebServiceRef()\nstatic CalculatorService service;\n\n}";
    }

    @Override
    public Annotation getAnnotation() {
        return AnnotationsCore.getAnnotation(ast, javax.xml.ws.WebServiceRef.class, null);
    }

    public void testRemoveAnnotationFromField() {
        try {
            assertNotNull(annotation);
            assertEquals("WebServiceRef", AnnotationUtils.getAnnotationName(annotation));

            IField field = source.findPrimaryType().getField("service");
            assertNotNull(field);

            assertTrue(AnnotationUtils.isAnnotationPresent(field,
                    AnnotationUtils.getAnnotationName(annotation)));
        
            AnnotationUtils.removeAnnotationFromField(source, compilationUnit, rewriter, field, annotation,
                    textFileChange);
            
            assertTrue(executeChange(new NullProgressMonitor(), textFileChange));
            
            assertFalse(AnnotationUtils.isAnnotationPresent(field,
                    AnnotationUtils.getAnnotationName(annotation)));
        } catch (CoreException ce) {
            ce.printStackTrace();
        }
    }
}
