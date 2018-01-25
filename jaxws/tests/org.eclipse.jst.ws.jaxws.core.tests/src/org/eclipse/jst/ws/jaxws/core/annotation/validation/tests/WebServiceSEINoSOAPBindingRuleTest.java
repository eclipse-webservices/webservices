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
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jst.ws.annotations.core.AnnotationsCore;
import org.eclipse.jst.ws.annotations.core.utils.AnnotationUtils;
import org.eclipse.jst.ws.internal.jaxws.core.JAXWSCoreMessages;

public class WebServiceSEINoSOAPBindingRuleTest extends AbstractWebServiceSEIRule {

    @Override
    protected Annotation getAnnotation() {
        return AnnotationsCore.createNormalAnnotation(ast, SOAPBinding.class.getSimpleName(), null);
    }

    public void testNoSOAPBindingOnMethodRule() {
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
            assertEquals(JAXWSCoreMessages.WEBSERVICE_ENPOINTINTERFACE_NO_SOAPBINDING,
                    annotationProblemMarker.getAttribute(IMarker.MESSAGE));
        } catch (CoreException ce) {
            fail(ce.getLocalizedMessage());
        } catch (OperationCanceledException oce) {
            fail(oce.getLocalizedMessage());
        } catch (InterruptedException ie) {
            fail(ie.getLocalizedMessage());
        }
    }

    public void testNoSOAPBindingOnTypeRule() {
        try {
            assertNotNull(annotation);
            assertEquals(SOAPBinding.class.getSimpleName(), AnnotationUtils.getAnnotationName(annotation));

            textFileChange.addEdit(AnnotationUtils.createAddImportTextEdit(source.findPrimaryType(), SOAPBinding.class.getCanonicalName()));

            textFileChange.addEdit(AnnotationUtils.createAddAnnotationTextEdit(source.findPrimaryType(), annotation));

            assertTrue(executeChange(new NullProgressMonitor(), textFileChange));

            assertTrue(AnnotationUtils.isAnnotationPresent(source, AnnotationUtils
                    .getAnnotationName(annotation)));

            Job.getJobManager().join(ResourcesPlugin.FAMILY_AUTO_BUILD, null);

            IMarker[] allmarkers = source.getResource().findMarkers(IMarker.PROBLEM, true,
                    IResource.DEPTH_INFINITE);

            assertEquals(1, allmarkers.length);

            IMarker annotationProblemMarker = allmarkers[0];

            assertEquals(source.getResource(), annotationProblemMarker.getResource());
            assertEquals(JAXWSCoreMessages.WEBSERVICE_ENPOINTINTERFACE_NO_SOAPBINDING,
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
