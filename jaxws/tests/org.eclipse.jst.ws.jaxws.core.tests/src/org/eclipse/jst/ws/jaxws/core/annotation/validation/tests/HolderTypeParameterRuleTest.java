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
package org.eclipse.jst.ws.jaxws.core.annotation.validation.tests;

import javax.jws.WebService;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jst.ws.annotations.core.AnnotationsCore;
import org.eclipse.jst.ws.annotations.core.utils.AnnotationUtils;
import org.eclipse.jst.ws.internal.jaxws.core.JAXWSCoreMessages;

public class HolderTypeParameterRuleTest extends AbstractAnnotationValidationTest {

    @Override
    public Annotation getAnnotation() {
        return AnnotationsCore.createNormalAnnotation(ast, WebService.class.getSimpleName(), null);
    }

    @Override
    public String getPackageName() {
        return "com.example";
    }

    @Override
    public String getClassName() {
        return "MyClass.java";
    }

    @Override
    public String getClassContents() {
        StringBuilder classContents = new StringBuilder("package com.example;\n\n");
        classContents.append("import javax.jws.WebParam;\n");
        classContents.append("import javax.xml.ws.Holder;\n\n");
        classContents.append("public class MyClass {\n\n\tpublic void myMethod(");
        classContents.append("@WebParam(mode=WebParam.Mode.IN) Holder<java.lang.String> param) {\n\n\t}\n}");
        return classContents.toString();
    }

    public void testHolderTypeParameterRule() {
        try {
            assertNotNull(annotation);
            assertEquals(WebService.class.getSimpleName(), AnnotationUtils.getAnnotationName(annotation));

            textFileChange.addEdit(AnnotationUtils.createAddImportTextEdit(source.findPrimaryType(), WebService.class.getCanonicalName()));

            textFileChange.addEdit(AnnotationUtils.createAddAnnotationTextEdit(source.findPrimaryType(), annotation));

            assertTrue(executeChange(new NullProgressMonitor(), textFileChange));

            assertTrue(AnnotationUtils.isAnnotationPresent(source, AnnotationUtils
                    .getAnnotationName(annotation)));
            assertTrue(source.getImport(WebService.class.getCanonicalName()).exists());

            Job.getJobManager().join(ResourcesPlugin.FAMILY_AUTO_BUILD, null);

            IMarker[] allmarkers = source.getResource().findMarkers(IMarker.PROBLEM, true,
                    IResource.DEPTH_INFINITE);

            assertEquals(1, allmarkers.length);

            IMarker annotationProblemMarker = allmarkers[0];

            assertEquals(source.getResource(), annotationProblemMarker.getResource());
            assertEquals(JAXWSCoreMessages.HOLDER_TYPE_MUST_BE_OUT_INOUT,
                    annotationProblemMarker.getAttribute(IMarker.MESSAGE));
        } catch (CoreException ce) {
            fail(ce.getLocalizedMessage());
        }catch (OperationCanceledException oce) {
            fail(oce.getLocalizedMessage());
        } catch (InterruptedException ie) {
            fail(ie.getLocalizedMessage());
        }
    }
}
