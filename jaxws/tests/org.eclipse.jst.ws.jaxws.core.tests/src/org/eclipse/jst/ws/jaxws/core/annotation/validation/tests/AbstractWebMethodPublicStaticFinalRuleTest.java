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

import javax.jws.WebMethod;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jst.ws.annotations.core.AnnotationsCore;
import org.eclipse.jst.ws.annotations.core.utils.AnnotationUtils;

public abstract class AbstractWebMethodPublicStaticFinalRuleTest extends AbstractAnnotationValidationTest {

    @Override
    protected Annotation getAnnotation() {
        return AnnotationsCore.createAnnotation(ast, WebMethod.class, WebMethod.class.getSimpleName(), null);
    }

    @Override
    protected String getClassContents() {
        StringBuilder classContents = new StringBuilder("package com.example;\n\n");
        classContents.append("import javax.jws.WebService;\n\n");
        classContents.append("@WebService(name=\"MyClass\")\n");
        classContents.append("public class MyClass {\n\n");
        classContents.append("\tString myPackagePrivateMethod() {\n\t\treturn \"package-private\";\n\t}\n\n");
        classContents.append("\tprivate String myPrivateMethod() {\n\t\treturn \"private\";\n\t}\n\n");
        classContents.append("\tprotected String myProtectedMethod() {\n\t\tmyPrivateMethod();\n");
        classContents.append("\t\treturn \"protected\";\n\t}\n");
        classContents.append("\tpublic static String myStaticMethod() {\n\t\treturn \"static\";\n\t}\n\n");
        classContents.append("\tpublic final String myFinalMethod() {\n\t\treturn \"final\";\n\t}\n\n}");
        return classContents.toString();
    }

    @Override
    protected String getClassName() {
        return "MyClass.java";
    }

    @Override
    protected String getPackageName() {
        return "com.example";
    }
    
    public abstract IMethod getMethodToTeset();
    public abstract String getErrorMessage();

    public void testWebMethodRestriction() {
        try {
            assertNotNull(annotation);
            assertEquals(WebMethod.class.getSimpleName(), AnnotationUtils.getAnnotationName(annotation));

            IMethod method = getMethodToTeset();
            assertNotNull(method);

            AnnotationUtils.addImportChange(compilationUnit, WebMethod.class, textFileChange, true);

            AnnotationUtils.createMethodAnnotationChange(source, compilationUnit, rewriter, method,
                    annotation, textFileChange);

            assertTrue(executeChange(new NullProgressMonitor(), textFileChange));

            assertTrue(AnnotationUtils.isAnnotationPresent(method, AnnotationUtils
                    .getAnnotationName(annotation)));

            Job.getJobManager().join(ResourcesPlugin.FAMILY_AUTO_BUILD, null);

            IMarker[] allmarkers = source.getResource().findMarkers(IMarker.PROBLEM, true,
                    IResource.DEPTH_INFINITE);

            assertEquals(1, allmarkers.length);

            IMarker annotationProblemMarker = allmarkers[0];

            assertEquals(source.getResource(), annotationProblemMarker.getResource());
            assertEquals(getErrorMessage(), annotationProblemMarker.getAttribute(IMarker.MESSAGE));
        } catch (CoreException ce) {
            fail(ce.getLocalizedMessage());
        } catch (OperationCanceledException oce) {
            fail(oce.getLocalizedMessage());
        } catch (InterruptedException ie) {
            fail(ie.getLocalizedMessage());
        }
    }
}
