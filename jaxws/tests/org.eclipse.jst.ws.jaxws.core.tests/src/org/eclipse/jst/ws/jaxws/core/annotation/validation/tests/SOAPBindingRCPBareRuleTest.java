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
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.MemberValuePair;
import org.eclipse.jst.ws.annotations.core.AnnotationsCore;
import org.eclipse.jst.ws.annotations.core.utils.AnnotationUtils;
import org.eclipse.jst.ws.internal.jaxws.core.JAXWSCoreMessages;

public class SOAPBindingRCPBareRuleTest extends AbstractSOAPBindingValidationTest {

    @Override
    protected Annotation getAnnotation() {
        List<MemberValuePair> memberValuePairs = new ArrayList<MemberValuePair>();

        MemberValuePair styleValuePair = AnnotationsCore.createEnumMemberValuePair(ast,
                SOAPBinding.class.getCanonicalName(), "style", Style.RPC);

        MemberValuePair useValuePair = AnnotationsCore.createEnumMemberValuePair(ast,
                SOAPBinding.class.getCanonicalName(), "use", Use.LITERAL);

        MemberValuePair parameterStyleValuePair = AnnotationsCore.createEnumMemberValuePair(ast,
                SOAPBinding.class.getCanonicalName(), "parameterStyle", ParameterStyle.BARE);

        memberValuePairs.add(styleValuePair);
        memberValuePairs.add(useValuePair);
        memberValuePairs.add(parameterStyleValuePair);

        return AnnotationsCore.createNormalAnnotation(ast, SOAPBinding.class.getSimpleName(), memberValuePairs);
    }
    
    public void testSOAPBindingRPCBareRule() {
        try {
            assertNotNull(annotation);
            assertEquals(SOAPBinding.class.getSimpleName(), AnnotationUtils.getAnnotationName(annotation));
            
            textFileChange.addEdit(AnnotationUtils.createAddImportTextEdit(source.findPrimaryType(), SOAPBinding.class));

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
            assertEquals(JAXWSCoreMessages.SOAPBINDING_RPC_NO_BARE_PARAMETER_STYLE,
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
