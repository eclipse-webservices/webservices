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

import junit.framework.Test;
import junit.framework.TestSuite;

public class JAXWSAnnotationValidationTestSuite extends TestSuite {

    public static Test suite() {
        return new JAXWSAnnotationValidationTestSuite();
    }

    public JAXWSAnnotationValidationTestSuite() {
        super("JAX-WS Annotation Validation Tests");
        addTestSuite(DocBareNonVoidNoOutParametersRuleTest.class);
        addTestSuite(DocBareOneNonHeaderINParameterRuleTest.class);
        addTestSuite(DocBareVoidOneINOneOutParameterRuleTest.class);
        addTestSuite(OnewayNoReturnValueRuleTest.class);
        addTestSuite(OnewayNoCheckedExceptionsRuleTest.class);
        addTestSuite(HolderTypeParameterRuleTest.class);
        addTestSuite(OnewayNoHolderParametersRuleTest.class);
        addTestSuite(SOAPBindingRPCStyleOnMethodRuleTest.class);
        addTestSuite(SOAPBindingRPCEncodedRuleTest.class);
        addTestSuite(SOAPBindingDocumentEncodedRuleTest.class);
        addTestSuite(SOAPBindingRCPBareRuleTest.class);
        addTestSuite(SOAPBindingMixedBindingsRuleTest.class);
        addTestSuite(WebMethodCheckForWebServiceRuleTest.class);
        addTestSuite(WebMethodExcludeRuleOnSEITest.class);
        addTestSuite(WebMethodExcludeRuleOnImplTest.class);
        addTestSuite(WebMethodNoPackagePrivateMethodRuleTest.class);
        addTestSuite(WebMethodNoPrivateMethodRuleTest.class);
        addTestSuite(WebMethodNoProtectedMethodRuleTest.class);
        addTestSuite(WebMethodNoFinalModifierRuleTest.class);
        addTestSuite(WebMethodNoStaticModifierRuleTest.class);
        addTestSuite(WebParamModeHolderTypeRuleTest.class);
        addTestSuite(WebServiceDefaultPublicConstructorRuleTest.class);
        addTestSuite(WebServiceNoFinalizeMethodRuleTest.class);
        addTestSuite(WebServiceNoFinalModiferRuleTest.class);
        addTestSuite(WebServiceNoAbstractModifierRuleTest.class);
        addTestSuite(WebServiceSEINoServiceNameRuleTest.class);
        addTestSuite(WebServiceSEINoEndpointInterfaceRuleTest.class);
        addTestSuite(WebServiceSEINoPortNameRuleTest.class);
        addTestSuite(WebServiceSEINoWebMethodRuleTest.class);
        addTestSuite(WebServiceSEINoWebResultRuleTest.class);
        addTestSuite(WebServiceSEINoWebParamRuleTest.class);
        addTestSuite(WebServiceSEINoOnewayRuleTest.class);
        addTestSuite(WebServiceSEINoSOAPBindingRuleTest.class);
        addTestSuite(WebServiceWebServiceProviderCoExistRuleTest.class);
    }

}
