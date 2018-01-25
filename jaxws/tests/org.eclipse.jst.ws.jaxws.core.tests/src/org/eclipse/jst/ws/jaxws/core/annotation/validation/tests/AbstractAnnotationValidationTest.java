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

import org.eclipse.jst.ws.jaxws.core.tests.AbstractAnnotationTest;

public abstract class AbstractAnnotationValidationTest extends AbstractAnnotationTest {

    @Override
    protected boolean isAutoBuildingEnabled() {
        return true;
    }

    @Override
    protected boolean isAnnotationProcessingEnabled() {
        return true;
    }

}
