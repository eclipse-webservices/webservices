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

import javax.jws.soap.SOAPBinding;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jst.ws.annotations.core.utils.AnnotationUtils;
import org.eclipse.jst.ws.internal.jaxws.core.JAXWSCoreMessages;

public class DocBareNonVoidNoOutParametersRuleTest extends AbstractDocumentBareValidationTest {

    @Override
    protected String getClassContents() {
        StringBuilder classContents = new StringBuilder("package com.example;\n\n");
        classContents.append("import javax.jws.WebParam;\n");
        classContents.append("public class MyClass {\n\n\t");
        classContents.append("public String noOut(String in, @WebParam(name=\"out\", mode=WebParam.Mode.OUT) ");
        classContents.append("String out) {\n\t\treturn \"txt\";\n\t}\n\n}");
        return classContents.toString();
    }

    public void testNonVoidNoOUTParameterRule() {
        try {
            assertNotNull(annotation);
            assertEquals(SOAPBinding.class.getSimpleName(), AnnotationUtils.getAnnotationName(annotation));

            IMethod method = source.findPrimaryType().getMethod("noOut", new String[] { "QString;",
                    "QString; "});
            
            assertNotNull(method);

            textFileChange.addEdit(AnnotationUtils.createAddImportTextEdit(method, SOAPBinding.class));
            
            textFileChange.addEdit(AnnotationUtils.createAddAnnotationTextEdit(method, annotation));

            assertTrue(executeChange(new NullProgressMonitor(), textFileChange));

            assertTrue(AnnotationUtils.isAnnotationPresent(method, AnnotationUtils
                    .getAnnotationName(annotation)));

            Job.getJobManager().join(ResourcesPlugin.FAMILY_AUTO_BUILD, null);

            IMarker[] allmarkers = source.getResource().findMarkers(IMarker.PROBLEM, true,
                    IResource.DEPTH_INFINITE);
            
            assertEquals(1, allmarkers.length);

            IMarker annotationProblemMarker = allmarkers[0];

            assertEquals(source.getResource(), annotationProblemMarker.getResource());
            assertEquals(JAXWSCoreMessages.DOC_BARE_NON_VOID_RETURN_NO_INOUT_OUT_PARAMETER,
                    annotationProblemMarker.getAttribute(IMarker.MESSAGE));
        } catch (CoreException ce) {
            fail(ce.getLocalizedMessage());
        } catch (OperationCanceledException oce) {
            fail(oce.getLocalizedMessage());
        } catch (InterruptedException ie) {
            fail(ie.getLocalizedMessage());
        }
    }
}
