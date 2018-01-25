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

import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.MemberValuePair;
import org.eclipse.jst.ws.annotations.core.AnnotationsCore;
import org.eclipse.jst.ws.annotations.core.utils.AnnotationUtils;
import org.eclipse.jst.ws.internal.jaxws.core.JAXWSCoreMessages;

public class SOAPBindingMixedBindingsRuleTest extends AbstractSOAPBindingValidationTest {

    @Override
    protected Annotation getAnnotation() {
        List<MemberValuePair> memberValuePairs = new ArrayList<MemberValuePair>();

        MemberValuePair styleValuePair = AnnotationsCore.createEnumMemberValuePair(ast,
                SOAPBinding.class.getCanonicalName(), "style", Style.DOCUMENT);

        MemberValuePair useValuePair = AnnotationsCore.createEnumMemberValuePair(ast,
                SOAPBinding.class.getCanonicalName(), "use", Use.LITERAL);

        MemberValuePair parameterStyleValuePair = AnnotationsCore.createEnumMemberValuePair(ast,
                SOAPBinding.class.getCanonicalName(), "parameterStyle", ParameterStyle.WRAPPED);

        memberValuePairs.add(styleValuePair);
        memberValuePairs.add(useValuePair);
        memberValuePairs.add(parameterStyleValuePair);

        return AnnotationsCore.createNormalAnnotation(ast, SOAPBinding.class.getSimpleName(), memberValuePairs);
    }

    @Override
    protected String getClassContents() {
        StringBuilder classContents = new StringBuilder("package com.example;\n\n");
        classContents.append("import javax.jws.soap.SOAPBinding;\n\n");
        classContents.append("@SOAPBinding(style=SOAPBinding.Style.RPC, use=SOAPBinding.Use.LITERAL, ");
        classContents.append("parameterStyle=SOAPBinding.ParameterStyle.WRAPPED)\n");
        classContents.append("public class MyClass {\n\n\tpublic String myMethod(String in) {");
        classContents.append("\n\t\treturn \"txt\";\n\t}\n\n}");
        return classContents.toString();
    }

    public void testSOAPBindingMixedBindingsRule() {
        try {
            assertNotNull(annotation);
            assertEquals(SOAPBinding.class.getSimpleName(), AnnotationUtils.getAnnotationName(annotation));

            IMethod method = source.findPrimaryType().getMethod("myMethod", new String[] { "QString;" });
            assertNotNull(method);

            textFileChange.addEdit(AnnotationUtils.createAddImportTextEdit(method, SOAPBinding.class.getCanonicalName()));

            textFileChange.addEdit(AnnotationUtils.createAddAnnotationTextEdit(method, annotation));

            assertTrue(executeChange(new NullProgressMonitor(), textFileChange));

            assertTrue(AnnotationUtils.isAnnotationPresent(method, AnnotationUtils
                    .getAnnotationName(annotation)));
            assertTrue(source.getImport(SOAPBinding.class.getCanonicalName()).exists());

            Job.getJobManager().join(ResourcesPlugin.FAMILY_AUTO_BUILD, null);

            IMarker[] allmarkers = source.getResource().findMarkers(IMarker.PROBLEM, true,
                    IResource.DEPTH_INFINITE);

            assertEquals(1, allmarkers.length);

            IMarker annotationProblemMarker = allmarkers[0];

            assertEquals(source.getResource(), annotationProblemMarker.getResource());
            assertEquals(JAXWSCoreMessages.SOAPBINDING_NO_MIXED_BINDINGS,
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
