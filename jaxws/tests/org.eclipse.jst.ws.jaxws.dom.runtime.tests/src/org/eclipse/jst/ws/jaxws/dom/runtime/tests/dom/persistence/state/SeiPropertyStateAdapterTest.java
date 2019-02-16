/*******************************************************************************
 * Copyright (c) 2009 by SAP AG, Walldorf. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
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
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebService;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.state.PropertyStateAdapterFactory;

/**
 * Test for {@ SeiPropertyStateAdapter} class.
 * 
 * @author Georgi Vachkov
 */
public class SeiPropertyStateAdapterTest extends TestCase 
{
	private IServiceEndpointInterface sei;
	private IWebService ws; 
	
	public void setUp()
	{
		sei = DomFactory.eINSTANCE.createIServiceEndpointInterface();
		ws = DomFactory.eINSTANCE.createIWebService();
		sei.getImplementingWebServices().add(ws);
	}	
	
	public void testIsAdapted()
	{
		IPropertyState adapter = (IPropertyState)PropertyStateAdapterFactory.INSTANCE.adapt(sei, IPropertyState.class);
		assertNotNull(adapter);		
	}
	
	public void testIsChangeableOutsideIn() 
	{
		ws.setWsdlLocation("C:/test");
		IPropertyState adapter = (IPropertyState)PropertyStateAdapterFactory.INSTANCE.adapt(sei, IPropertyState.class);
		assertFalse(adapter.isChangeable(DomPackage.Literals.IJAVA_WEB_SERVICE_ELEMENT__NAME));
		assertFalse(adapter.isChangeable(DomPackage.Literals.ISERVICE_ENDPOINT_INTERFACE__IMPLICIT));
		assertFalse(adapter.isChangeable(DomPackage.Literals.ISERVICE_ENDPOINT_INTERFACE__TARGET_NAMESPACE));
	}
	
	public void testIsChangealbeInOutImplicitInterface()
	{
		sei.eSet(DomPackage.Literals.ISERVICE_ENDPOINT_INTERFACE__IMPLICIT, Boolean.TRUE);
		
		IPropertyState adapter = (IPropertyState)PropertyStateAdapterFactory.INSTANCE.adapt(sei, IPropertyState.class);
		assertTrue(adapter.isChangeable(DomPackage.Literals.IJAVA_WEB_SERVICE_ELEMENT__NAME));
		assertFalse(adapter.isChangeable(DomPackage.Literals.ISERVICE_ENDPOINT_INTERFACE__IMPLICIT));
		assertFalse(adapter.isChangeable(DomPackage.Literals.ISERVICE_ENDPOINT_INTERFACE__TARGET_NAMESPACE));
	}
	
	public void testIsChangeableInOutExplicitInterface()
	{
		IPropertyState adapter = (IPropertyState)PropertyStateAdapterFactory.INSTANCE.adapt(sei, IPropertyState.class);
		assertTrue(adapter.isChangeable(DomPackage.Literals.IJAVA_WEB_SERVICE_ELEMENT__NAME));
		assertFalse(adapter.isChangeable(DomPackage.Literals.ISERVICE_ENDPOINT_INTERFACE__IMPLICIT));
		assertTrue(adapter.isChangeable(DomPackage.Literals.ISERVICE_ENDPOINT_INTERFACE__TARGET_NAMESPACE));
	}
}
