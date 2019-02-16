/*******************************************************************************
 * Copyright (c) 2009 Progress Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    Oisin Hurley - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.jaxb.core.tests;

import junit.framework.Test;
import junit.framework.TestSuite;

public class JAXBCoreTestSuite extends TestSuite {

    public static Test suite() {
        return new JAXBCoreTestSuite();
    }
    
    public JAXBCoreTestSuite() {
        super("JAXB Core Tests");
        addTestSuite(AddAnnotationToPackageTest.class);
        addTestSuite(RemoveAnnotationFromPackageTest.class);    
    }
    
}
