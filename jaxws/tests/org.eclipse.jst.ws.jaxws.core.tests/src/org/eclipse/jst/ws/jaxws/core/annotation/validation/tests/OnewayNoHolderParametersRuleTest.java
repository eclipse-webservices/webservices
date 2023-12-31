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
package org.eclipse.jst.ws.jaxws.core.annotation.validation.tests;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jst.ws.internal.jaxws.core.JAXWSCoreMessages;

public class OnewayNoHolderParametersRuleTest extends AbstractOnewayValidationTest {

    @Override
    protected String getClassContents() {
        StringBuilder classContents = new StringBuilder("package com.example;\n\n");
        classContents.append("import javax.xml.ws.Holder;\n\n");
        classContents.append("public class MyClass {\n\n\tpublic void myMethod(");
        classContents.append("Holder<String> in) {\n\t}\n}");
        return classContents.toString();
    }

    @Override
    public String getErrorMessage() {
        return JAXWSCoreMessages.ONEWAY_NO_HOLDER_PARAMETERS;
    }

    @Override
    public IMethod getMethodToTest() {
        return source.findPrimaryType().getMethod("myMethod", new String[]{"QHolder<QString;>;"});
    }

}
