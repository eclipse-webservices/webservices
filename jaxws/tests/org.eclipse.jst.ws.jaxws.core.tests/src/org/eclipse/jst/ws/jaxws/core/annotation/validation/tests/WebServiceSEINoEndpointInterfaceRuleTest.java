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

import java.util.ArrayList;
import java.util.List;

import javax.jws.WebService;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.MemberValuePair;
import org.eclipse.jst.ws.annotations.core.AnnotationsCore;
import org.eclipse.jst.ws.annotations.core.utils.AnnotationUtils;
import org.eclipse.jst.ws.internal.jaxws.core.JAXWSCoreMessages;

public class WebServiceSEINoEndpointInterfaceRuleTest extends AbstractAnnotationValidationTest {

    @Override
    protected Annotation getAnnotation() {
        List<MemberValuePair> memberValuePairs = new ArrayList<MemberValuePair>();

        MemberValuePair nameValuePair = AnnotationsCore.createStringMemberValuePair(ast, "name", "MyClass");

        MemberValuePair endpointInterfaceValuePair = AnnotationsCore.createStringMemberValuePair(ast,
                "endpointInterface", "MyInterface");

        MemberValuePair targetNamespaceValuePair = AnnotationsCore.createStringMemberValuePair(ast, 
                "targetNamespace", "http://example.com/");

        memberValuePairs.add(nameValuePair);
        memberValuePairs.add(endpointInterfaceValuePair);
        memberValuePairs.add(targetNamespaceValuePair);

        return AnnotationsCore.createAnnotation(ast, WebService.class, WebService.class.getSimpleName(),
                memberValuePairs);
    }

    @Override
    protected String getClassContents() {
        StringBuilder classContents = new StringBuilder("package com.example;\n\n");
        classContents.append("public interface MyInterface {\n\n\tpublic String myeMethod();\n\n}");
        return classContents.toString();
    }

    @Override
    protected String getClassName() {
        return "MyInterface.java";
    }

    @Override
    protected String getPackageName() {
        return "com.example";
    }
    
    public void testWebServiceSEINoEndpointInterfaceRule() {
        try {
            assertNotNull(annotation);
            assertEquals(WebService.class.getSimpleName(), AnnotationUtils.getAnnotationName(annotation));

            AnnotationUtils.addImportChange(compilationUnit, WebService.class, textFileChange,
                    true);

            AnnotationUtils.createTypeAnnotationChange(source, compilationUnit, rewriter, 
                    source.findPrimaryType(), annotation, textFileChange);

            assertTrue(executeChange(new NullProgressMonitor(), textFileChange));

            assertTrue(AnnotationUtils.isAnnotationPresent(source, AnnotationUtils
                    .getAnnotationName(annotation)));

            Job.getJobManager().join(ResourcesPlugin.FAMILY_AUTO_BUILD, null);

            IMarker[] allmarkers = source.getResource().findMarkers(IMarker.PROBLEM, true,
                    IResource.DEPTH_INFINITE);

            assertEquals(1, allmarkers.length);

            IMarker annotationProblemMarker = allmarkers[0];

            assertEquals(source.getResource(), annotationProblemMarker.getResource());
            assertEquals(JAXWSCoreMessages.WEBSERVICE_ENDPOINTINTERFACE_SEI,
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
