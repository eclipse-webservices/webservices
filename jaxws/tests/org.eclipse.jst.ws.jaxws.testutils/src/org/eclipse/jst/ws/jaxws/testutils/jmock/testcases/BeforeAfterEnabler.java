/*******************************************************************************
 * Copyright (c) 2009 by SAP AG, Walldorf. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * This class is based on work by Shay Banon (http://www.kimchy.org/before_after_testcase_with_junit/)
 * 
 * Contributors:
 *     SAP AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.jaxws.testutils.jmock.testcases;

import java.lang.reflect.Method;
import java.util.Vector;

import junit.framework.Test;

public class BeforeAfterEnabler {
	
	private final IBeforeAfterEnabled tc;
	
    private int testCount = 0;
 
    private final int totalTestCount;
 
    private boolean disableAfterTestCase = false;
 
    public BeforeAfterEnabler(IBeforeAfterEnabled tc) {
    	this.tc = tc;
    	totalTestCount = countTotalTests();
    }
 
    public void runBare() throws Throwable {
        Throwable exception = null;
        if (testCount == 0) {
            beforeTestCase();
        }
        testCount++;
        try {
            tc.runBareInternal();
        } catch (Throwable running) {
            exception = running;
        }
        if (testCount == totalTestCount) {
            if (!disableAfterTestCase) {
                try {
                    afterTestCase();
                } catch (Exception afterTestCase) {
                    if (exception == null) exception = afterTestCase;
                }
            } else {
                disableAfterTestCase = false;
            }
        }
        if (exception != null) throw exception;
    }
 
    public void disableAfterTestCase() {
        disableAfterTestCase = true;
    }
 
    /**
     * Called before any tests within this test case.
     *
     * @throws Exception
     */
    protected void beforeTestCase() throws Exception {
    	tc.beforeTestCase();
    }
 
    /**
     * Called after all the tests within the test case
     * have executed.
     *
     * @throws Exception
     */
    protected void afterTestCase() throws Exception {
    	tc.afterTestCase();
    }
 
    private int countTotalTests() {
        int count = 0;
        Class<?> superClass = tc.getClass();
        Vector<String> names = new Vector<String>();
        while (Test.class.isAssignableFrom(superClass)) {
            Method[] methods = superClass.getDeclaredMethods();
            for (Method method : methods) {
                String name = method.getName();
                if (names.contains(name))
                    continue;
                names.addElement(name);
                if (isTestMethod(method)) {
                    count++;
                }
            }
            superClass = superClass.getSuperclass();
        }
        return count;
    }
 
    private boolean isTestMethod(Method m) {
        String name = m.getName();
        Class<?>[] parameters = m.getParameterTypes();
        Class<?> returnType = m.getReturnType();
        return parameters.length == 0 && name.startsWith("test") && returnType.equals(Void.TYPE);
    }
}
