/*******************************************************************************
 * Copyright (c) 2009 by SAP AG, Walldorf. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     SAP AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.jaxws.dom.runtime.tests.dom.persistence;

import org.eclipse.jdt.core.IType;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IServiceEndpointInterface;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebMethod;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebParam;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.SOAPBindingParameterStyle;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.SOAPBindingStyle;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.SOAPBindingUse;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.JaxWsDefaultsCalculator;
import org.eclipse.jst.ws.jaxws.testutils.jmock.Mock;
import org.eclipse.jst.ws.jaxws.testutils.jmock.MockObjectTestCase;
import org.eclipse.jst.ws.jaxws.utils.annotations.IAnnotation;

public class JaxWsDefaultsCalculatorTest extends MockObjectTestCase 
{
	private static final String METHOD_NAME = "method";
	
	private Mock<IWebMethod> webMethod;
	private JaxWsDefaultsCalculator defCalc = new JaxWsDefaultsCalculator();
	
	@Override
	public void setUp()
	{
		webMethod = mock(IWebMethod.class);
		webMethod.stubs().method("getSoapBindingStyle").will(returnValue(SOAPBindingStyle.DOCUMENT));
		webMethod.stubs().method("getSoapBindingParameterStyle").will(returnValue(SOAPBindingParameterStyle.BARE));
		webMethod.stubs().method("getName").will(returnValue(METHOD_NAME));
	}
	
	public void testDefineSBStyleDefaultValue() 
	{
		assertEquals(SOAPBindingStyle.DOCUMENT, defCalc.defineSBStyle(null));

		Mock<IAnnotation<IType>> ann = mock(IAnnotation.class);
		ann.stubs().method("getPropertyValue").will(returnValue(null));
		assertEquals(SOAPBindingStyle.DOCUMENT, defCalc.defineSBStyle(ann.proxy()));
		
		ann.stubs().method("getPropertyValue").will(returnValue("Style.DOCUMENT"));
		assertEquals(SOAPBindingStyle.DOCUMENT, defCalc.defineSBStyle(ann.proxy()));
		
		ann.stubs().method("getPropertyValue").will(returnValue("SOAPBinding.Style.DOCUMENT"));
		assertEquals(SOAPBindingStyle.DOCUMENT, defCalc.defineSBStyle(ann.proxy()));		
	}
	
	public void testDefineSBStyle() 
	{
		Mock<IAnnotation<IType>> ann = mock(IAnnotation.class);
		ann.stubs().method("getPropertyValue").will(returnValue("Style.RPC"));
		assertEquals(SOAPBindingStyle.RPC, defCalc.defineSBStyle(ann.proxy()));

		ann.stubs().method("getPropertyValue").will(returnValue("SOAPBinding.Style.RPC"));
		assertEquals(SOAPBindingStyle.RPC, defCalc.defineSBStyle(ann.proxy()));
	}
	
	public void testDefineSBUseDefaultValue() 
	{
		assertEquals(SOAPBindingUse.LITERAL, defCalc.defineSBUse(null));

		Mock<IAnnotation<IType>> ann = mock(IAnnotation.class);
		ann.stubs().method("getPropertyValue").will(returnValue(null));
		assertEquals(SOAPBindingUse.LITERAL, defCalc.defineSBUse(ann.proxy()));
		
		ann.stubs().method("getPropertyValue").will(returnValue("Style.LITERAL"));
		assertEquals(SOAPBindingUse.LITERAL, defCalc.defineSBUse(ann.proxy()));
		
		ann.stubs().method("getPropertyValue").will(returnValue("SOAPBinding.Style.LITERAL"));
		assertEquals(SOAPBindingUse.LITERAL, defCalc.defineSBUse(ann.proxy()));
	}
	
	public void testDefineSBUse()
	{
		Mock<IAnnotation<IType>> ann = mock(IAnnotation.class);
		ann.stubs().method("getPropertyValue").will(returnValue("Use.ENCODED"));
		assertEquals(SOAPBindingUse.ENCODED, defCalc.defineSBUse(ann.proxy()));

		ann.stubs().method("getPropertyValue").will(returnValue("SOAPBinding.Use.ENCODED"));
		assertEquals(SOAPBindingUse.ENCODED, defCalc.defineSBUse(ann.proxy()));
	}

	public void testDefineSBParameterStyleDefaultValue() 
	{
		assertEquals(SOAPBindingParameterStyle.WRAPPED, defCalc.defineSBParameterStyle(null));

		Mock<IAnnotation<IType>> ann = mock(IAnnotation.class);
		ann.stubs().method("getPropertyValue").will(returnValue(null));
		assertEquals(SOAPBindingParameterStyle.WRAPPED, defCalc.defineSBParameterStyle(ann.proxy()));
		
		ann.stubs().method("getPropertyValue").will(returnValue("ParameterStyle.WRAPPED"));
		assertEquals(SOAPBindingParameterStyle.WRAPPED, defCalc.defineSBParameterStyle(ann.proxy()));
		
		ann.stubs().method("getPropertyValue").will(returnValue("SOAPBinding.ParameterStyle.WRAPPED"));
		assertEquals(SOAPBindingParameterStyle.WRAPPED, defCalc.defineSBParameterStyle(ann.proxy()));
	}
	
	public void testDefineSBParameterStyle() 
	{
		Mock<IAnnotation<IType>> ann = mock(IAnnotation.class);
		ann.stubs().method("getPropertyValue").will(returnValue("ParameterStyle.BARE"));
		assertEquals(SOAPBindingParameterStyle.BARE, defCalc.defineSBParameterStyle(ann.proxy()));

		ann.stubs().method("getPropertyValue").will(returnValue("SOAPBinding.ParameterStyle.BARE"));
		assertEquals(SOAPBindingParameterStyle.BARE, defCalc.defineSBParameterStyle(ann.proxy()));	
	}	
	
	public void testCalcWebParamDefaultName() 
	{
		assertEquals(METHOD_NAME, defCalc.calcWebParamDefaultName(webMethod.proxy(), 0));
		
		webMethod.stubs().method("getSoapBindingStyle").will(returnValue(SOAPBindingStyle.RPC));
		assertEquals("arg0", defCalc.calcWebParamDefaultName(webMethod.proxy(), 0));
		
		webMethod.stubs().method("getSoapBindingStyle").will(returnValue(SOAPBindingStyle.DOCUMENT));
		webMethod.stubs().method("getSoapBindingParameterStyle").will(returnValue(SOAPBindingParameterStyle.WRAPPED));
		assertEquals("arg1", defCalc.calcWebParamDefaultName(webMethod.proxy(), 1));		
	}

	public void testCalcWebParamDefaultTargetNS() 
	{
		Mock<IWebParam> webParam = mock(IWebParam.class);
		webMethod.stubs().method("getSoapBindingParameterStyle").will(returnValue(SOAPBindingParameterStyle.WRAPPED));		
		webParam.stubs().method("isHeader").will(returnValue(false));
		
		Mock<IServiceEndpointInterface> sei = mock(IServiceEndpointInterface.class);
		sei.stubs().method("getTargetNamespace").will(returnValue("http://com.sap.test"));		
		webMethod.stubs().method("eContainer").will(returnValue(sei.proxy()));
		
		assertEquals("", defCalc.calcWebParamDefaultTargetNS(webMethod.proxy(), webParam.proxy()));

		webMethod.stubs().method("getSoapBindingStyle").will(returnValue(SOAPBindingStyle.RPC));		
		assertEquals("http://com.sap.test", defCalc.calcWebParamDefaultTargetNS(webMethod.proxy(), webParam.proxy()));
		
		webMethod.stubs().method("getSoapBindingStyle").will(returnValue(SOAPBindingStyle.DOCUMENT));	
		webMethod.stubs().method("getSoapBindingParameterStyle").will(returnValue(SOAPBindingParameterStyle.BARE));		
		assertEquals("http://com.sap.test", defCalc.calcWebParamDefaultTargetNS(webMethod.proxy(), webParam.proxy()));
		
		webMethod.stubs().method("getSoapBindingStyle").will(returnValue(SOAPBindingStyle.DOCUMENT));	
		webMethod.stubs().method("getSoapBindingParameterStyle").will(returnValue(SOAPBindingParameterStyle.WRAPPED));
		webParam.stubs().method("isHeader").will(returnValue(true));
		
		assertEquals("http://com.sap.test", defCalc.calcWebParamDefaultTargetNS(webMethod.proxy(), webParam.proxy()));		
	}

	public void testCalcWebResultDefaultName() 
	{
		assertEquals(METHOD_NAME + "Response", defCalc.calcWebResultDefaultName(webMethod.proxy()));
		
		webMethod.stubs().method("getSoapBindingStyle").will(returnValue(SOAPBindingStyle.RPC));
		assertEquals("return", defCalc.calcWebResultDefaultName(webMethod.proxy()));
		
		webMethod.stubs().method("getSoapBindingStyle").will(returnValue(SOAPBindingStyle.DOCUMENT));
		webMethod.stubs().method("getSoapBindingParameterStyle").will(returnValue(SOAPBindingParameterStyle.WRAPPED));
		assertEquals("return", defCalc.calcWebResultDefaultName(webMethod.proxy()));			
	}
}
