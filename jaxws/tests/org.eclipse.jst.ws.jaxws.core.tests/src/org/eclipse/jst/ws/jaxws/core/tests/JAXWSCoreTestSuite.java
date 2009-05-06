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

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * 
 * @author sclarke
 *
 */
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
    }
    
}
