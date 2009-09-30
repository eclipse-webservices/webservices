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
package org.eclipse.jst.ws.jaxws.dom.runtime.tests.dom.util;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jst.ws.jaxws.dom.runtime.DomUtil;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.DomFactory;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.DomPackage;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IServiceEndpointInterface;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebMethod;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebParam;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebService;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebServiceProject;
import org.eclipse.jst.ws.jaxws.testutils.jmock.Mock;
import org.eclipse.jst.ws.jaxws.testutils.jmock.MockObjectTestCase;

public class DomUtilTest extends MockObjectTestCase
{
	private DomUtil util; 
	
	public void setUp()
	{
		util = new DomUtil();
	}

	public void testIsOutideInWebService()
	{
		Mock<IWebService> ws = mock(IWebService.class);
		ws.stubs().method("getWsdlLocation").will(returnValue("C:/test/wsdl/location"));
		Mock<IServiceEndpointInterface> sei = mock(IServiceEndpointInterface.class);
		EList<IWebService> implemetingWs = new BasicEList<IWebService>();
		implemetingWs.add(ws.proxy());
		
		sei.stubs().method("getImplementingWebServices").will(returnValue(implemetingWs));
		assertTrue(util.isOutsideInWebService(sei.proxy()));
		
		ws.stubs().method("getWsdlLocation").will(returnValue(null));
		assertFalse(util.isOutsideInWebService(sei.proxy()));
	}
	
	public void testFindWsProject()
	{
		IWebServiceProject wsProject = DomFactory.eINSTANCE.createIWebServiceProject();
		IServiceEndpointInterface sei = DomFactory.eINSTANCE.createIServiceEndpointInterface();
		IWebMethod method = DomFactory.eINSTANCE.createIWebMethod();
		sei.getWebMethods().add(method);
		wsProject.getServiceEndpointInterfaces().add(sei);
		
		assertEquals(wsProject, util.findWsProject(method));		
	}
	
	public void testCalcUniqueImpl()
	{
		IWebMethod method = DomFactory.eINSTANCE.createIWebMethod();
		method.eSet(DomPackage.Literals.IJAVA_WEB_SERVICE_ELEMENT__IMPLEMENTATION, "method(I)");
		IWebParam param = DomFactory.eINSTANCE.createIWebParam();
		param.eSet(DomPackage.Literals.IJAVA_WEB_SERVICE_ELEMENT__IMPLEMENTATION, "a");
		method.getParameters().add(param);
		
		assertEquals("method(I)", util.calcUniqueImpl(method));
		assertEquals("method(I)[a]", util.calcUniqueImpl(param));
	}
}
