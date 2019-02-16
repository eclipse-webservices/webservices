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

public abstract class AbstractSOAPBindingValidationTest extends AbstractAnnotationValidationTest {

    @Override
    protected String getClassContents() {
        StringBuilder classContents = new StringBuilder("package com.example;\n\n");
        classContents.append("public class MyClass {\n\n\tpublic String myMethod(String in) {");
        classContents.append("\n\t\treturn \"txt\";\n\t}\n\n}");
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
    
}
