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

import org.eclipse.jst.ws.jaxws.dom.runtime.IPropertyState;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.DomFactory;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.DomPackage;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IServiceEndpointInterface;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebMethod;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebService;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.state.MethodPropertyStateAdapter;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.state.PropertyStateAdapterFactory;

/**
 * Class containing tests for {@link MethodPropertyStateAdapter}
 * 
 * @author Georgi Vachkov
 */
public class MethodPropertyStateAdapterTest extends TestCase 
{
	private IWebMethod wm;
	private IWebService ws;
	private IPropertyState adapter; 
	
	public void setUp()	
	{
		wm = DomFactory.eINSTANCE.createIWebMethod();
		ws = DomFactory.eINSTANCE.createIWebService();
		
		IServiceEndpointInterface sei = DomFactory.eINSTANCE.createIServiceEndpointInterface();
		sei.getWebMethods().add(wm);
		sei.getImplementingWebServices().add(ws);
		adapter = (IPropertyState)PropertyStateAdapterFactory.INSTANCE.adapt(wm, IPropertyState.class);
		assertNotNull(adapter);
	}
	
	public void testIsChangebleOutsideInWs() 
	{
		ws.setWsdlLocation("C:/test/wsdl/location");
		assertFalse(adapter.isChangeable(DomPackage.Literals.IJAVA_WEB_SERVICE_ELEMENT__NAME));
		assertFalse(adapter.isChangeable(DomPackage.Literals.IWEB_METHOD__EXCLUDED));
		assertFalse(adapter.isChangeable(DomPackage.Literals.IWEB_METHOD__SOAP_BINDING_STYLE));
		assertFalse(adapter.isChangeable(DomPackage.Literals.IWEB_METHOD__SOAP_BINDING_USE));
		assertFalse(adapter.isChangeable(DomPackage.Literals.IWEB_METHOD__SOAP_BINDING_PARAMETER_STYLE));
	}
	
	public void testIsChangable()
	{
		assertTrue(adapter.isChangeable(DomPackage.Literals.IJAVA_WEB_SERVICE_ELEMENT__NAME));
		assertFalse(adapter.isChangeable(DomPackage.Literals.IWEB_METHOD__EXCLUDED));
		assertFalse(adapter.isChangeable(DomPackage.Literals.IWEB_METHOD__SOAP_BINDING_STYLE));
		assertFalse(adapter.isChangeable(DomPackage.Literals.IWEB_METHOD__SOAP_BINDING_USE));
		assertFalse(adapter.isChangeable(DomPackage.Literals.IWEB_METHOD__SOAP_BINDING_PARAMETER_STYLE));
	}
}
