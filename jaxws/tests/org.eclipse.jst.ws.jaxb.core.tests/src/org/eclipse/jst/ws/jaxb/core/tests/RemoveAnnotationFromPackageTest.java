/*******************************************************************************
 * Copyright (c) 2009, 2014 Progress Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.jst.ws.jaxb.core.tests;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IPackageDeclaration;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jst.ws.annotations.core.AnnotationsCore;
import org.eclipse.jst.ws.annotations.core.utils.AnnotationUtils;

public class RemoveAnnotationFromPackageTest extends AbstractAnnotationTest {

	private static final String XML_SCHEMA_CANONICAL_NAME = "javax.xml.bind.annotation.XmlSchema";
	private static final String XML_SCHEMA_SIMPLE_NAME = "XmlSchema";
	
    @Override
    public String getPackageName() {
        return "com.example";
    }

    @Override
    public String getClassName() {
        return "package-info.java";
    }

    @Override
    public String getClassContents() {
        StringBuilder classContents = new StringBuilder("@XmlSchema\n\n");
        classContents.append("package com.example;\n\n");
        classContents.append("import javax.xml.bind.annotation.XmlSchema;\n\n");
        return classContents.toString();
    }

    @Override
    public Annotation getAnnotation() {
        return AnnotationsCore.createNormalAnnotation(ast, XML_SCHEMA_SIMPLE_NAME, null);
    }

    public void testRemoveAnnotationFromPackage() {
        try {
            assertNotNull(annotation);
            assertEquals(XML_SCHEMA_SIMPLE_NAME, AnnotationUtils.getAnnotationName(annotation));
            IPackageDeclaration myPackage = source.getPackageDeclaration(getPackageName());
            assertNotNull(myPackage);

            assertTrue(AnnotationUtils.isAnnotationPresent(myPackage, AnnotationUtils
                    .getAnnotationName(annotation)));

            textFileChange.addEdit(AnnotationUtils.createRemoveAnnotationTextEdit(myPackage, annotation));
            textFileChange.addEdit(AnnotationUtils.createRemoveImportTextEdit(myPackage, XML_SCHEMA_CANONICAL_NAME));

            assertTrue(executeChange(new NullProgressMonitor(), textFileChange));

            assertFalse(AnnotationUtils.isAnnotationPresent(myPackage, AnnotationUtils.getAnnotationName(annotation)));
            assertFalse(source.getImport(XML_SCHEMA_CANONICAL_NAME).exists());
        } catch (CoreException ce) {
            fail(ce.getLocalizedMessage());
        }
    }
}
