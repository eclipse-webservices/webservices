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

public class WebServiceNoAbstractModifierRuleTest extends AbstractWebServicePublicAbstractFinalRuleTest {

    @Override
    protected String getClassContents() {
        StringBuilder classContents = new StringBuilder("package com.example;\n\n");
        classContents.append("public abstract class MyClass {\n\n");
        classContents.append("\tpublic String myMethod() {" + "\n\t\treturn \"txt\";\n\t}\n\n}");
        return classContents.toString();
    }

}
