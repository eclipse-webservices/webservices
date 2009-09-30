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
package org.eclipse.jst.ws.jaxws.dom.runtime.tests.dom.persistence.state;

import junit.framework.TestCase;

import org.eclipse.jst.ws.jaxws.dom.runtime.api.DomFactory;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.DomPackage;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IServiceEndpointInterface;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebMethod;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebParam;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebService;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.SOAPBindingParameterStyle;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.SOAPBindingStyle;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.SOAPBindingUse;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.state.ParameterPropertyStateAdapter;

public class ParameterPropertyStateAdapterTest extends TestCase
{
	private ParameterPropertyStateAdapter adapter;;
	private IWebParam param;
	private IWebMethod method;
	private IServiceEndpointInterface sei;
	
	@Override
	public void setUp()
	{		
		sei = DomFactory.eINSTANCE.createIServiceEndpointInterface();
		method = DomFactory.eINSTANCE.createIWebMethod();
		method.setSoapBindingStyle(SOAPBindingStyle.DOCUMENT);
		method.setSoapBindingUse(SOAPBindingUse.LITERAL);
		method.setSoapBindingParameterStyle(SOAPBindingParameterStyle.WRAPPED);
		sei.getWebMethods().add(method);
		
		param = DomFactory.eINSTANCE.createIWebParam();
		param.eSet(DomPackage.Literals.IJAVA_WEB_SERVICE_ELEMENT__IMPLEMENTATION, "testParam");
		param.setName("testParam");
		method.getParameters().add(param);
		
		adapter = new ParameterPropertyStateAdapter();
		adapter.setTarget(param);
	}
	
	public void testIsChagebleNameCorrectSoapBinding()
	{
		assertTrue(adapter.isChangeable(DomPackage.Literals.IJAVA_WEB_SERVICE_ELEMENT__NAME));
	}
	
	public void testIsChagebleNameIncorrectSoapBinding()
	{
		method.setSoapBindingStyle(SOAPBindingStyle.RPC);
		assertFalse(adapter.isChangeable(DomPackage.Literals.IJAVA_WEB_SERVICE_ELEMENT__NAME));
		
		method.setSoapBindingStyle(SOAPBindingStyle.DOCUMENT);
		method.setSoapBindingUse(SOAPBindingUse.ENCODED);
		assertFalse(adapter.isChangeable(DomPackage.Literals.IJAVA_WEB_SERVICE_ELEMENT__NAME));
		
		method.setSoapBindingUse(SOAPBindingUse.LITERAL);
		method.setSoapBindingParameterStyle(SOAPBindingParameterStyle.BARE);
		assertFalse(adapter.isChangeable(DomPackage.Literals.IJAVA_WEB_SERVICE_ELEMENT__NAME));
	}	
	
	public void testIsChagebleNameParamInHeader()
	{
		param.setHeader(true);
		method.setSoapBindingStyle(SOAPBindingStyle.RPC);
		assertTrue(adapter.isChangeable(DomPackage.Literals.IJAVA_WEB_SERVICE_ELEMENT__NAME));
		method.setSoapBindingStyle(SOAPBindingStyle.DOCUMENT);
		assertTrue(adapter.isChangeable(DomPackage.Literals.IJAVA_WEB_SERVICE_ELEMENT__NAME));		
	}	
	
	public void testIsChagebleTargetNSCorrectSoapBinding()
	{
		assertTrue(adapter.isChangeable(DomPackage.Literals.IWEB_PARAM__TARGET_NAMESPACE));
	}
	
	public void testIsChagebleTargetNSIncorrectSoapBinding()
	{
		method.setSoapBindingStyle(SOAPBindingStyle.RPC);
		assertFalse(adapter.isChangeable(DomPackage.Literals.IWEB_PARAM__TARGET_NAMESPACE));
		
		method.setSoapBindingStyle(SOAPBindingStyle.DOCUMENT);
		method.setSoapBindingUse(SOAPBindingUse.ENCODED);
		assertFalse(adapter.isChangeable(DomPackage.Literals.IWEB_PARAM__TARGET_NAMESPACE));
		
		method.setSoapBindingUse(SOAPBindingUse.LITERAL);
		method.setSoapBindingParameterStyle(SOAPBindingParameterStyle.BARE);
		assertFalse(adapter.isChangeable(DomPackage.Literals.IWEB_PARAM__TARGET_NAMESPACE));
	}
	
	public void testIsChagebleTargetNSParamInHeader()
	{
		method.setSoapBindingStyle(SOAPBindingStyle.RPC);
		assertFalse(adapter.isChangeable(DomPackage.Literals.IWEB_PARAM__TARGET_NAMESPACE));
		param.setHeader(true);
		assertTrue(adapter.isChangeable(DomPackage.Literals.IWEB_PARAM__TARGET_NAMESPACE));
	}	
	
	public void testIsChageblePartNameCorrectSoapBinding()
	{
		method.setSoapBindingStyle(SOAPBindingStyle.RPC);		
		assertTrue(adapter.isChangeable(DomPackage.Literals.IWEB_PARAM__PART_NAME));
	}
	
	public void testIsChageblePartNameIncorrectSoapBinding()
	{
		assertFalse(adapter.isChangeable(DomPackage.Literals.IWEB_PARAM__PART_NAME));
		method.setSoapBindingStyle(SOAPBindingStyle.RPC);
		method.setSoapBindingUse(SOAPBindingUse.ENCODED);
		assertFalse(adapter.isChangeable(DomPackage.Literals.IWEB_PARAM__PART_NAME));
		
		method.setSoapBindingUse(SOAPBindingUse.LITERAL);
		method.setSoapBindingParameterStyle(SOAPBindingParameterStyle.BARE);
		assertFalse(adapter.isChangeable(DomPackage.Literals.IWEB_PARAM__PART_NAME));
	}
	
	public void testIsChagebleTargetNSHeader() 
	{
		assertFalse(adapter.isChangeable(DomPackage.Literals.IWEB_PARAM__HEADER));

		method.setSoapBindingStyle(SOAPBindingStyle.RPC);
		assertFalse(adapter.isChangeable(DomPackage.Literals.IWEB_PARAM__HEADER));
	}
	
	public void testReturnParamNotEditable()
	{
		param.eSet(DomPackage.Literals.IJAVA_WEB_SERVICE_ELEMENT__IMPLEMENTATION, "return");
		assertFalse(adapter.isChangeable(DomPackage.Literals.IJAVA_WEB_SERVICE_ELEMENT__NAME));
		assertFalse(adapter.isChangeable(DomPackage.Literals.IWEB_PARAM__PART_NAME));
		assertFalse(adapter.isChangeable(DomPackage.Literals.IWEB_PARAM__TARGET_NAMESPACE));
		
		method.setSoapBindingStyle(SOAPBindingStyle.RPC);		
		assertFalse(adapter.isChangeable(DomPackage.Literals.IJAVA_WEB_SERVICE_ELEMENT__NAME));
		assertFalse(adapter.isChangeable(DomPackage.Literals.IWEB_PARAM__PART_NAME));
		assertFalse(adapter.isChangeable(DomPackage.Literals.IWEB_PARAM__TARGET_NAMESPACE));
	}
	
	public void testOutsideInWebService()
	{
		IWebService ws = DomFactory.eINSTANCE.createIWebService();
		ws.setWsdlLocation("C:/test/wsdl/location");
		sei.getImplementingWebServices().add(ws);
		assertFalse(adapter.isChangeable(DomPackage.Literals.IJAVA_WEB_SERVICE_ELEMENT__NAME));
		assertFalse(adapter.isChangeable(DomPackage.Literals.IWEB_PARAM__PART_NAME));
		assertFalse(adapter.isChangeable(DomPackage.Literals.IWEB_PARAM__TARGET_NAMESPACE));
		
		method.setSoapBindingStyle(SOAPBindingStyle.RPC);
		assertFalse(adapter.isChangeable(DomPackage.Literals.IJAVA_WEB_SERVICE_ELEMENT__NAME));
		assertFalse(adapter.isChangeable(DomPackage.Literals.IWEB_PARAM__PART_NAME));
		assertFalse(adapter.isChangeable(DomPackage.Literals.IWEB_PARAM__TARGET_NAMESPACE));
	}
}
