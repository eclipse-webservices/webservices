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

/**
 * 
 * @author sclarke
 *
 */
public abstract class AbstractWebServiceSEIRule extends AbstractAnnotationValidationTest {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        testJavaProject.createCompilationUnit(getPackageName(), getInterfaceName(), getInterfaceContents());
    }

    @Override
    protected String getClassContents() {
        StringBuilder classContents = new StringBuilder("package com.example;\n\n");
        classContents.append("import javax.jws.WebService;\n\n");
        classContents.append("@WebService(endpointInterface=\"com.example.MyInterface\", ");
        classContents.append("targetNamespace=\"http://example.com/\", portName=\"MyClassPort\", ");
        classContents.append("serviceName=\"MyClassService\")\n");
        classContents.append("public class MyClass {\n\n");
        classContents.append("\tpublic void methodOne(String in) {\n\t}\n\n");
        classContents.append("\tpublic String myMethod(String in) {" + "\n\t\treturn \"txt\";\n\t}\n\n}");
        return classContents.toString();
    }
    
    private String getInterfaceContents() {
        StringBuilder seiContents = new StringBuilder("package com.example;\n\n");
        seiContents.append("import javax.jws.WebService;\n\n");
        seiContents.append("@WebService(name=\"MyInterface\", targetNamespace=\"http://example.com/\")\n");
        seiContents.append("public interface MyInterface {\n\n");
        seiContents.append("\tpublic String myMethod(String in);\n\n}");
        return seiContents.toString();
    }
    
    @Override
    protected String getClassName() {
        return "MyClass.java";
    }

    protected String getInterfaceName() {
        return "MyInterface.java";
    }

    @Override
    protected String getPackageName() {
        return "com.example";
    }

}
