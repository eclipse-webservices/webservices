/*******************************************************************************
 * Copyright (c) 2009 Shane Clarke.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    Shane Clarke - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.jaxws.core.tests;

import junit.framework.Test;
import junit.framework.TestSuite;

public class JAXWSCoreTestSuite extends TestSuite {

    public static Test suite() {
        return new JAXWSCoreTestSuite();
    }

    public JAXWSCoreTestSuite() {
        super("JAX-WS Core Tests");
        addTestSuite(AddAnnotationToTypeTest.class);
        addTestSuite(RemoveAnnotationFromTypeTest.class);
        addTestSuite(AddAnnotationToFieldTest.class);
        addTestSuite(RemoveAnnotationFromFieldTest.class);
        addTestSuite(AddAnnotationToMethodTest.class);
        addTestSuite(RemoveAnnotationFromMethodTest.class);
        addTestSuite(AddAnnotationToMethodParameterTest.class);
        addTestSuite(RemoveAnnotationFromMethodParameterTest.class);
        addTestSuite(AddMemberValuePairToAnnotationTest.class);
        addTestSuite(RemoveMemberValuePairTest.class);
        addTestSuite(UpdateMemberValuePairTest.class);
        addTestSuite(UpdateSingleMemberAnnotationTest.class);
    }

}
