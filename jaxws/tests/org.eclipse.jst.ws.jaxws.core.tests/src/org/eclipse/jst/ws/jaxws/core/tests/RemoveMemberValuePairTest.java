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

import javax.jws.WebService;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.MemberValuePair;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jst.ws.annotations.core.utils.AnnotationUtils;

public class RemoveMemberValuePairTest extends AbstractAnnotationTest {

    @Override
    protected Annotation getAnnotation() {
        return null;
    }

    @Override
    protected String getClassContents() {
        StringBuilder classContents = new StringBuilder("package com.example;\n\n");
        classContents.append("import javax.jws.WebService;\n\n");
        classContents.append("@WebService(name=\"Calculator\")\n");
        classContents.append("public class Calculator {\n}");
        return classContents.toString();
    }

    @Override
    protected String getClassName() {
        return "Calculator.java";
    }

    @Override
    protected String getPackageName() {
        return "com.example";
    }

    public void testRemoveMemberValuePairFromAnnotation() {
        try {
            IType type = source.findPrimaryType();
            assertTrue(AnnotationUtils.isAnnotationPresent(source, "WebService"));
            Annotation webService = AnnotationUtils.getAnnotation(type, WebService.class);
            assertNotNull(webService);
            assertTrue(webService instanceof NormalAnnotation);

            NormalAnnotation webServiceAnnotation = (NormalAnnotation) webService;
            assertTrue(webServiceAnnotation.values().size() == 1);

            MemberValuePair nameValuePair = AnnotationUtils.getMemberValuePair(webServiceAnnotation, "name");

            AnnotationUtils.removeMemberValuePair(webServiceAnnotation, nameValuePair);
            webServiceAnnotation = (NormalAnnotation) AnnotationUtils.getAnnotation(type, WebService.class);
            assertTrue(webServiceAnnotation.values().size() == 0);
        } catch (CoreException ce) {
            fail(ce.getLocalizedMessage());
        }
    }

}
