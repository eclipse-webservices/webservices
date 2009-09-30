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
package org.eclipse.jst.ws.jaxws.dom.runtime.tests.dom.persistence.defaults;

import junit.framework.TestCase;

import org.eclipse.jst.ws.jaxws.dom.runtime.IPropertyDefaults;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.DomFactory;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.DomPackage;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IServiceEndpointInterface;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebMethod;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebParam;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.SOAPBindingParameterStyle;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.SOAPBindingStyle;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.SOAPBindingUse;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.WebParamKind;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.defaults.ParameterPropertyDefaultsAdapter;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.defaults.PropertyDefaultsAdapterFactory;

/**
 * Tests for {@link IWebParam} properties default values
 * 
 * @author Georgi Vachkov
 */
public class ParameterPropertyDefaultsAdapterTest extends TestCase 
{
	private IWebMethod webMethod;
	private IWebParam webParam;
	private IPropertyDefaults defaults;
	
	public void setUp() 
	{
		webMethod = DomFactory.eINSTANCE.createIWebMethod();
		webMethod.setSoapBindingStyle(SOAPBindingStyle.DOCUMENT);
		webMethod.setSoapBindingUse(SOAPBindingUse.LITERAL);
		webMethod.setSoapBindingParameterStyle(SOAPBindingParameterStyle.WRAPPED);
		
		webParam = DomFactory.eINSTANCE.createIWebParam();
		webParam.eSet(DomPackage.Literals.IJAVA_WEB_SERVICE_ELEMENT__IMPLEMENTATION, "myParam");
		
		IWebParam dummy = DomFactory.eINSTANCE.createIWebParam();
		dummy.eSet(DomPackage.Literals.IJAVA_WEB_SERVICE_ELEMENT__IMPLEMENTATION, "dummy");
		
		webMethod.getParameters().add(dummy);
		webMethod.getParameters().add(webParam);
		
		defaults = (IPropertyDefaults)PropertyDefaultsAdapterFactory.INSTANCE.adapt(webParam, IPropertyDefaults.class);
	}
	
	public void testNameDefaultValue() 
	{
		webParam.setName("test");
		assertEquals("arg1", defaults.getDefault(DomPackage.Literals.IJAVA_WEB_SERVICE_ELEMENT__NAME));
	}
	
	public void testPartNameDefaultValue()
	{
		webParam.setPartName("test");
		assertEquals("arg1", defaults.getDefault(DomPackage.Literals.IWEB_PARAM__PART_NAME));
	}
	
	public void testPartNameMethodParamStyleBare()
	{
		webMethod.setSoapBindingParameterStyle(SOAPBindingParameterStyle.BARE);
		webMethod.setName("test");
		assertEquals("test", defaults.getDefault(DomPackage.Literals.IJAVA_WEB_SERVICE_ELEMENT__NAME));
	}
	
	public void testTargetNSDefaultValue()
	{
		webParam.setTargetNamespace("test");
		assertEquals("", defaults.getDefault(DomPackage.Literals.IWEB_PARAM__TARGET_NAMESPACE));
	}
	
	public void testTargetNSParamInHeaderDefaultValue()
	{
		IServiceEndpointInterface sei = DomFactory.eINSTANCE.createIServiceEndpointInterface();
		sei.setTargetNamespace("http://test");
		
		sei.getWebMethods().add(webMethod);
		
		webParam.setHeader(true);
		assertEquals("http://test", defaults.getDefault(DomPackage.Literals.IWEB_PARAM__TARGET_NAMESPACE));	
	}
	
	public void testHeaderDefaultValue()
	{
		assertEquals(false, defaults.getDefault(DomPackage.Literals.IWEB_PARAM__HEADER));	
	}
	
	public void testKindDefaultValue() 
	{
		assertEquals(WebParamKind.IN, defaults.getDefault(DomPackage.Literals.IWEB_PARAM__KIND));
	}
	
	public void testAdaptedOnce()
	{
		IPropertyDefaults adapter = (IPropertyDefaults)PropertyDefaultsAdapterFactory.INSTANCE.adapt(webParam, IPropertyDefaults.class);
		assertTrue(adapter instanceof ParameterPropertyDefaultsAdapter);
		
		assertEquals(1, webParam.eAdapters().size());
		assertEquals(defaults, adapter);
	}	
}
